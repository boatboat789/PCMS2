<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/pages/config/meta.jsp"></jsp:include>
<title>Job Management</title>
<jsp:include page="/WEB-INF/pages/config/css/baseCSS.jsp"></jsp:include>
<link href="<c:url value="/resources/vendor/bootstrap-datetimepicker/css/bootstrap-datetimepicker.css"/>" rel="stylesheet">
<script src="<c:url value="/resources/vendor/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min.js"/>"></script>
</head>
<body>
	<jsp:include page="/WEB-INF/pages/config/navbar.jsp"></jsp:include>
	<div id="wrapper">
		<div class="row" style="margin: 0px">
			<div class="col-0 col-sm-0 col-md-0 col-lg-2 col-xl-2"></div>
			<div class="col-12 col-sm-12 col-md-12 col-xl-8" style="padding: 20px;">

				<div class="page-header-bar"><h1 class="page-title">Job Management — ERP Sync</h1></div>

				<!-- Status Card -->
				<div class="card mb-4">
					<div class="card-header"><strong>สถานะระบบ</strong></div>
					<div class="card-body">
						<div class="row">
							<div class="col-md-6">
								<p class="mb-1">สถานะ Job:</p>
								<span id="badge_running" class="badge badge-secondary" style="font-size:1rem;" aria-live="polite">กำลังโหลด…</span>
							</div>
							<div class="col-md-6">
								<p class="mb-1">Schedule อัตโนมัติ:</p>
								<span id="badge_schedule" class="badge badge-secondary" style="font-size:1rem;" aria-live="polite">กำลังโหลด…</span>
							</div>
						</div>
						<div class="row mt-3">
							<div class="col-12">
								<button type="button" id="btn_toggle_schedule" class="btn btn-warning" onclick="toggleSchedule()">
									<i class="fas fa-power-off" aria-hidden="true"></i> Toggle Schedule
								</button>
								<button type="button" class="btn btn-outline-secondary ml-2" onclick="refreshStatus()">
									<i class="fas fa-sync" aria-hidden="true"></i> Refresh Status
								</button>
							</div>
						</div>
					</div>
				</div>

				<!-- Run Manual Card -->
				<div class="card mb-4">
					<div class="card-header"><strong>รันซ่อมข้อมูลแบบกำหนดช่วงวันที่</strong></div>
					<div class="card-body">
						<div class="row">
							<div class="col-md-5 mb-2">
								<label for="fromDate">วันที่เริ่มต้น (From)</label>
								<input type="text" id="fromDate" class="form-control" placeholder="yyyy-MM-dd" readonly>
							</div>
							<div class="col-md-5 mb-2">
								<label for="toDate">วันที่สิ้นสุด (To)</label>
								<input type="text" id="toDate" class="form-control" placeholder="yyyy-MM-dd" readonly>
							</div>
						</div>
						<div class="row mt-1">
							<div class="col-12">
								<button type="button" id="btn_run" class="btn btn-danger" onclick="runJob()">
									<i class="fas fa-play" aria-hidden="true"></i> Run Now (Repair Data)
								</button>
								<small class="text-muted ml-2">* อาจใช้เวลา 5-10 นาที</small>
							</div>
						</div>
						<div id="div_progress" class="mt-3" style="display:none;">
							<div class="alert alert-info mb-2">
								<strong><i class="fas fa-cog fa-spin" aria-hidden="true"></i> กำลังซ่อมข้อมูล ERP</strong>
								<div class="progress mt-2 mb-1" style="height:18px;">
									<div class="progress-bar progress-bar-striped progress-bar-animated bg-info" style="width:100%;">กำลังประมวลผล…</div>
								</div>
								<div class="d-flex justify-content-between">
									<small>เวลาที่ใช้ไป: <strong id="elapsed_time">00:00</strong></small>
									<small>อัปเดตสถานะล่าสุด: <span id="last_refresh">-</span></small>
								</div>
							</div>
							<div class="alert alert-warning py-2 mb-0">
								<i class="fas fa-info-circle" aria-hidden="true"></i> Job กำลังรันอยู่ในพื้นหลัง ปิดหน้านี้ได้โดยไม่กระทบการทำงาน — คาดว่าใช้เวลา <strong>5–10 นาที</strong>
							</div>
						</div>
					</div>
				</div>

			</div>
		</div>
	</div>

<script>
var BASE_URL = ctx + '/Setting/JobManagement';
var _elapsedStart = null;
var _elapsedTimer = null;
var _refreshTimer = null;

$(document).ready(function() {
    $('#fromDate').datetimepicker({
        format: 'YYYY-MM-DD',
        defaultDate: moment().subtract(7, 'days')
    });
    $('#toDate').datetimepicker({
        format: 'YYYY-MM-DD',
        defaultDate: moment()
    });
    refreshStatus();
    scheduleRefresh(10000);
});

function scheduleRefresh(interval) {
    if (_refreshTimer) clearInterval(_refreshTimer);
    _refreshTimer = setInterval(refreshStatus, interval);
}

function refreshStatus() {
    $.ajax({
        url: BASE_URL + '/api/status',
        type: 'GET',
        success: function(res) {
            var data = typeof res === 'string' ? JSON.parse(res) : res;
            updateStatusBadges(data);
            var now = new Date();
            var h = now.getHours().toString().padStart(2,'0');
            var m = now.getMinutes().toString().padStart(2,'0');
            var s = now.getSeconds().toString().padStart(2,'0');
            $('#last_refresh').text(h + ':' + m + ':' + s);
        },
        error: function() { }
    });
}

function startElapsedTimer() {
    _elapsedStart = Date.now();
    if (_elapsedTimer) clearInterval(_elapsedTimer);
    _elapsedTimer = setInterval(function() {
        var sec = Math.floor((Date.now() - _elapsedStart) / 1000);
        var m = Math.floor(sec / 60).toString().padStart(2,'0');
        var s = (sec % 60).toString().padStart(2,'0');
        $('#elapsed_time').text(m + ':' + s);
    }, 1000);
}

function stopElapsedTimer() {
    if (_elapsedTimer) { clearInterval(_elapsedTimer); _elapsedTimer = null; }
    _elapsedStart = null;
    $('#elapsed_time').text('00:00');
}

function updateStatusBadges(data) {
    var runBadge = $('#badge_running');
    if (data.isRunning) {
        runBadge.removeClass('badge-success badge-secondary').addClass('badge-warning').text('กำลังทำงาน...');
        $('#div_progress').show();
        $('#btn_run').prop('disabled', true);
        if (!_elapsedStart) { startElapsedTimer(); scheduleRefresh(5000); }
    } else {
        runBadge.removeClass('badge-warning badge-secondary').addClass('badge-success').text('ว่าง (Idle)');
        if (_elapsedStart) { stopElapsedTimer(); scheduleRefresh(10000); }
        $('#div_progress').hide();
        $('#btn_run').prop('disabled', false);
    }
    var schBadge = $('#badge_schedule');
    if (data.scheduleEnabled) {
        schBadge.removeClass('badge-danger badge-secondary').addClass('badge-success').text('เปิดใช้งาน');
        $('#btn_toggle_schedule').removeClass('btn-success').addClass('btn-warning').html('<i class="fas fa-pause"></i> ปิด Schedule');
    } else {
        schBadge.removeClass('badge-success badge-secondary').addClass('badge-danger').text('ปิดใช้งาน');
        $('#btn_toggle_schedule').removeClass('btn-warning').addClass('btn-success').html('<i class="fas fa-play"></i> เปิด Schedule');
    }
}

function toggleSchedule() {
    var current = $('#badge_schedule').hasClass('badge-success');
    var msg = current ? 'ต้องการปิด Schedule อัตโนมัติใช่ไหม?' : 'ต้องการเปิด Schedule อัตโนมัติใช่ไหม?';
    Swal.fire({
        title: 'ยืนยัน',
        text: msg,
        icon: 'warning',
        showCancelButton: true,
        cancelButtonText: 'ยกเลิก',
        confirmButtonText: 'ยืนยัน',
        confirmButtonColor: current ? '#d33' : '#3085d6'
    }).then(function(result) {
        if (!result.isConfirmed) return;
        $.ajax({
            url: BASE_URL + '/api/schedule/toggle',
            type: 'POST',
            success: function(res) {
                var data = typeof res === 'string' ? JSON.parse(res) : res;
                if (data.status === 'SUCCESS') {
                    updateStatusBadges(data);
                    Swal.fire({ icon: 'success', title: 'สำเร็จ', text: data.message, showConfirmButton: false, timer: 1500 });
                } else {
                    Swal.fire({ icon: 'error', title: 'เกิดข้อผิดพลาด', text: data.message });
                }
            },
            error: function() {
                Swal.fire({ icon: 'error', title: 'เกิดข้อผิดพลาด', text: 'ไม่สามารถเชื่อมต่อได้' });
            }
        });
    });
}

function runJob() {
    var fromDate = $('#fromDate').val();
    var toDate = $('#toDate').val();
    if (!fromDate || !toDate) {
        Swal.fire({ icon: 'warning', title: 'กรุณาระบุวันที่', text: 'กรุณาเลือกวันที่เริ่มต้นและวันที่สิ้นสุด' });
        return;
    }
    Swal.fire({
        title: 'ยืนยันการรัน',
        text: 'รันซ่อมข้อมูล ' + fromDate + ' ถึง ' + toDate + '\nอาจใช้เวลา 5-10 นาที',
        icon: 'warning',
        showCancelButton: true,
        cancelButtonText: 'ยกเลิก',
        confirmButtonText: 'ยืนยัน',
        confirmButtonColor: '#d33'
    }).then(function(result) {
        if (!result.isConfirmed) return;
        $.ajax({
            url: BASE_URL + '/api/run',
            type: 'POST',
            data: { fromDate: fromDate, toDate: toDate },
            success: function(res) {
                var data = typeof res === 'string' ? JSON.parse(res) : res;
                if (data.status === 'STARTED') {
                    Swal.fire({ icon: 'success', title: 'เริ่มแล้ว', text: data.message, showConfirmButton: false, timer: 2000 });
                    $('#div_progress').show();
                    $('#btn_run').prop('disabled', true);
                    startElapsedTimer();
                    scheduleRefresh(5000);
                } else {
                    Swal.fire({ icon: 'error', title: 'เกิดข้อผิดพลาด', text: data.message });
                }
            },
            error: function() {
                Swal.fire({ icon: 'error', title: 'เกิดข้อผิดพลาด', text: 'ไม่สามารถเชื่อมต่อได้' });
            }
        });
    });
}
</script>
</body>
</html>
