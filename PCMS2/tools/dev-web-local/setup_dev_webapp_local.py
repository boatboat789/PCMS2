"""เตรียมสำเนา webapp PCMS2 ชี้ DEV นอก repo (ไม่แตะ Tomcat เดิม ไม่แตะ source) — ดัดแปลงจาก LBMS/tools/dev-web-local
  BASE = C:/Users/94687/pcms2-dev-tomcat (CATALINA_BASE แยก) · CATALINA_HOME = D:/apache-tomcat-9.0.100 (อ่านอย่างเดียว)
  - explode target/PCMS2.war -> BASE/webapps/PCMS2
  - database.properties ของสำเนา = src/test/resources/database.properties (DEV ทั้ง 4 datasource, gitignored) + internal.job.token ปลอม
  - ใส่ DevTestLoginFilter (jar) ก่อน FilterLogin ใน web.xml ของสำเนา
  - พอร์ต HTTP 8082 (localhost เท่านั้น) / shutdown 8007
ไม่พิมพ์ username/password
ใช้: python tools/dev-web-local/setup_dev_webapp_local.py   (รันจากรากโปรเจกต์ หลัง mvn -o package -DskipTests)"""
import os, re, shutil, subprocess, sys, zipfile

ROOT = 'C:/RedirectDoc/94687/My Documents/GitHub/PCMS2/PCMS2'
HOME = 'D:/apache-tomcat-9.0.100'
BASE = 'C:/Users/94687/pcms2-dev-tomcat'
J8 = 'C:/Program Files/Java/jdk-1.8/bin'

props = open(os.path.join(ROOT, 'src/test/resources/database.properties'), encoding='utf-8').read()
urls = re.findall(r'^\w+\.db\.url=(.*)$', props, re.M)
if len(urls) != 4 or any('10.11.44.101' not in u or 'PCMSDEV' not in u.upper() for u in urls):
    sys.exit('guard: database.properties (test) ไม่ใช่ DEV ครบ 4 datasource — หยุด')
if 'ERP_PROD' in props or 'SOR_PRODUCTION' in props:
    sys.exit('guard: พบ ERP_PROD/SOR_PRODUCTION — หยุด')
props = props.rstrip() + '\n\ninternal.job.token=devtest-not-a-real-token\n'

if os.path.exists(BASE):
    shutil.rmtree(BASE)
os.makedirs(BASE + '/webapps')
for d in ('conf', 'logs', 'temp', 'work', 'lib'):
    os.makedirs(BASE + '/' + d, exist_ok=True)
for f in os.listdir(HOME + '/conf'):
    p = HOME + '/conf/' + f
    if os.path.isfile(p):
        shutil.copy(p, BASE + '/conf/' + f)

sx = open(BASE + '/conf/server.xml', encoding='utf-8').read()
sx = sx.replace('port="8005"', 'port="8007"').replace('port="8080"', 'port="8082"').replace('port="8009"', 'port="8011"')
sx = re.sub(r'redirectPort="\d+"', 'redirectPort="8445"', sx)
sx = re.sub(r'(<Connector port="8082"[^>]*?)(/?>)', lambda m: m.group(1) + ' address="127.0.0.1" ' + m.group(2), sx, count=1)
open(BASE + '/conf/server.xml', 'w', encoding='utf-8').write(sx)

app = BASE + '/webapps/PCMS2'
with zipfile.ZipFile(os.path.join(ROOT, 'target', 'PCMS2.war')) as z:
    z.extractall(app)

cp = [HOME + '/lib/servlet-api.jar', app + '/WEB-INF/classes'] + [app + '/WEB-INF/lib/' + f for f in os.listdir(app + '/WEB-INF/lib') if f.endswith('.jar')]
out = BASE + '/filter-classes'
os.makedirs(out)
r = subprocess.run([J8 + '/javac.exe', '-encoding', 'UTF-8', '-nowarn', '-d', out, '-cp', ';'.join(cp),
                    os.path.join(os.path.dirname(os.path.abspath(__file__)), 'DevTestLoginFilter.java')], capture_output=True, text=True)
if r.returncode != 0:
    sys.exit(r.stdout + r.stderr)
subprocess.check_call([J8 + '/jar.exe', 'cf', app + '/WEB-INF/lib/devtest-filter.jar', '-C', out, '.'])

wx_path = app + '/WEB-INF/web.xml'
wx = open(wx_path, encoding='utf-8').read()
i = wx.index('<filter>')
wx = wx[:i] + '<filter><filter-name>DevTestLogin</filter-name><filter-class>devtest.DevTestLoginFilter</filter-class></filter>\n' + wx[i:]
j = wx.index('<filter-mapping>')
wx = wx[:j] + '<filter-mapping><filter-name>DevTestLogin</filter-name><url-pattern>/*</url-pattern></filter-mapping>\n' + wx[j:]
open(wx_path, 'w', encoding='utf-8').write(wx)

open(app + '/WEB-INF/classes/database.properties', 'w', encoding='utf-8').write(props)
print('พร้อม:', BASE, '· webapp =', app)
