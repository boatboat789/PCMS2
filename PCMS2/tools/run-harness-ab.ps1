# A/B regression + timing run of PCMSRegressionHarness: OLD code (git worktree) vs NEW code (this project).
# Read-only against the DB (harness only builds #temp tables; scheduler is excluded). See docs/prd-verification-perf-search.md
#
#   powershell -ExecutionPolicy Bypass -File tools\run-harness-ab.ps1 -Target DEV -Rounds 1 -Yes   # dry run on DEV
#   powershell -ExecutionPolicy Bypass -File tools\run-harness-ab.ps1 -Target PRD -Rounds 3        # real run (asks to type YES)
#
# Messages are English on purpose (script is saved without BOM; Windows PowerShell 5.1 would garble Thai).
param(
    [Parameter(Mandatory = $true)][ValidateSet('DEV', 'PRD')][string]$Target,
    [string]$OldRef = '8371211',
    [string]$OldWorktree = 'C:\temp\pcms2_old',
    [string]$Out = 'C:\temp\pcms2_ab',
    [int]$Rounds = 3,
    [switch]$Yes
)
$ErrorActionPreference = 'Stop'
$env:JAVA_HOME = 'C:\Program Files\Java\jdk-1.8'
$env:JRE_HOME = $env:JAVA_HOME

$newProj = (Resolve-Path (Join-Path $PSScriptRoot '..')).Path
$repoRoot = (& git -C $newProj rev-parse --show-toplevel).Trim() -replace '/', '\'
$testRes = Join-Path $newProj 'src\test\resources'
$propsFile = Join-Path $testRes 'database.properties'
$devBak = "$propsFile.dev.bak"
$baselineDirNew = Join-Path $testRes 'pcms2\regression\baseline'
$casesDirNew = Join-Path $testRes 'pcms2\regression\cases'

# ---- 1. pick DB properties for the target (never print passwords) ----
if (-not (Test-Path $devBak)) { Copy-Item $propsFile $devBak }   # first run: remember the DEV file
if ($Target -eq 'PRD') { $srcProps = Join-Path $newProj 'src\main\resources\database.properties' } else { $srcProps = $devBak }
if (-not (Test-Path $srcProps)) { throw "database.properties source not found: $srcProps" }
$url = (Select-String -Path $srcProps -Pattern '^pcms\.db\.url=(.*)$').Matches[0].Groups[1].Value
$isDevUrl = $url -match 'PCMSDEV'
if ($Target -eq 'PRD' -and $isDevUrl) { throw 'Target=PRD but pcms.db.url points to PCMSDEV. Stop.' }
if ($Target -eq 'DEV' -and -not $isDevUrl) { throw 'Target=DEV but pcms.db.url does not point to PCMSDEV. Stop.' }
$hostPart = ($url -replace '^jdbc:sqlserver://', '') -replace ';.*$', ''
Write-Host "Target=$Target  pcms.db.url host=$hostPart  rounds=$Rounds  old=$OldRef  out=$Out"

# ---- 2. timing guard: PRD sync jobs run at :14 and :44 and 01:00 and change data mid-run ----
$now = Get-Date
$min = $now.Minute
$bad = ($now.Hour -eq 1 -and $min -lt 20) -or ($min -ge 12 -and $min -le 18) -or ($min -ge 42 -and $min -le 48)
if ($bad -and $Target -eq 'PRD') { throw ("Now is {0:HH:mm}: too close to a sync job (:14 / :44 / 01:00). Start at about :20 or :50 so the runs finish before the next job." -f $now) }
if (-not $Yes) {
    $a = Read-Host "Run harness against $Target ($hostPart)? type YES"
    if ($a -ne 'YES') { Write-Host 'Cancelled.'; return }
}

# ---- 3. old worktree + cases ----
if (-not (Test-Path (Join-Path $OldWorktree '.git'))) {
    & git -C $repoRoot worktree add --detach $OldWorktree $OldRef
}
$oldProj = Join-Path $OldWorktree 'PCMS2'
$oldRes = Join-Path $oldProj 'src\test\resources'
$baselineDirOld = Join-Path $oldRes 'pcms2\regression\baseline'
New-Item -ItemType Directory -Force (Join-Path $oldRes 'pcms2\regression\cases') | Out-Null
Copy-Item (Join-Path $casesDirNew '*.json') (Join-Path $oldRes 'pcms2\regression\cases') -Force
New-Item -ItemType Directory -Force $Out | Out-Null

$baselineKeep = Join-Path $Out 'new_project_baseline_before'
if (Test-Path $baselineDirNew) { Copy-Item $baselineDirNew $baselineKeep -Recurse -Force }

function Invoke-Harness($proj, $log) {
    Push-Location $proj
    try {
        cmd /c "mvn -o -q test -Dtest=PCMSRegressionHarness -Dpcms.harness.enabled=true -Dpcms.harness.mode=capture > `"$log`" 2>&1"
        return $LASTEXITCODE
    }
    finally { Pop-Location }
}

try {
    Copy-Item $srcProps $propsFile -Force
    Copy-Item $srcProps (Join-Path $oldRes 'database.properties') -Force

    for ($i = 1; $i -le $Rounds; $i++) {
        foreach ($side in @('old', 'new')) {
            if ($side -eq 'old') { $proj = $oldProj; $bdir = $baselineDirOld } else { $proj = $newProj; $bdir = $baselineDirNew }
            $log = Join-Path $Out ("{0}_r{1}.log" -f $side, $i)
            $t0 = Get-Date
            $code = Invoke-Harness $proj $log
            Write-Host ("[{0:HH:mm:ss}] {1} round {2}: mvn exit={3} ({4:N0}s) -> {5}" -f (Get-Date), $side, $i, $code, ((Get-Date) - $t0).TotalSeconds, $log)
            if ($code -ne 0) { throw "harness failed ($side round $i). See $log" }
            $dest = Join-Path $Out ("{0}_r{1}" -f $side, $i)
            if (Test-Path $dest) { Remove-Item $dest -Recurse -Force }
            Copy-Item $bdir $dest -Recurse
        }
    }
}
finally {
    Copy-Item $devBak $propsFile -Force                                   # always put the DEV properties back
    Copy-Item $devBak (Join-Path $oldRes 'database.properties') -Force
    if (Test-Path $baselineKeep) {                                        # restore the project baseline
        Remove-Item $baselineDirNew -Recurse -Force -ErrorAction SilentlyContinue
        Copy-Item $baselineKeep $baselineDirNew -Recurse -Force
    }
    Write-Host 'database.properties restored to DEV; project baseline restored.'
}

# ---- 4. analysis ----
$py = Join-Path $PSScriptRoot 'harness-ab-diff.py'
if ($Rounds -ge 2) {
    Write-Host "`n===== CONTROL: old r1 vs old r2 (same code twice) - must show 0 changes; if not, data moved during the run =====" -ForegroundColor Cyan
    & python -I $py diff (Join-Path $Out 'old_r1') (Join-Path $Out 'old_r2')
}
Write-Host "`n===== RESULT: old r1 vs new r1 =====" -ForegroundColor Cyan
& python -I $py diff (Join-Path $Out 'old_r1') (Join-Path $Out 'new_r1')
Write-Host "`n===== TIMING (median over rounds) =====" -ForegroundColor Cyan
$oldLogs = 1..$Rounds | ForEach-Object { Join-Path $Out "old_r$_.log" }
$newLogs = 1..$Rounds | ForEach-Object { Join-Path $Out "new_r$_.log" }
& python -I $py timing --old $oldLogs --new $newLogs
Write-Host "`nAll files are in $Out . Send me the three outputs above."
