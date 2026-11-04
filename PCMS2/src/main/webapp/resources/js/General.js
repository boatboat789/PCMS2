// escape ค่าที่จะใส่ใน attribute ของ HTML ที่ต่อสตริงเอง (value="...", data-search="...") — กัน " ตัดค่า / XSS
function escAttr(v) {
    if (v === null || v === undefined) { return ''; }
    return String(v).replace(/&/g, '&amp;').replace(/"/g, '&quot;').replace(/'/g, '&#39;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
}

// แสดงวันครบกำหนด (dd/MM/yyyy) พร้อมสถานะ "เลยกำหนด N วัน / วันนี้ / อีก N วัน" — ใช้เป็น render ของคอลัมน์ Due Date
// คืนค่าเดิมสำหรับ sort/search/export (type !== 'display') จึงไม่กระทบการเรียงและตัวกรอง
// ไม่เตือนเมื่อ sale line ปิด/ยกเลิกแล้ว (saleStatus C/X) · ข้อความกำกับสถานะเสมอ ไม่พึ่งสีอย่างเดียว
function renderDueDate(data, type, row) {
	if (type !== 'display') { return data; }
	var s = (data === null || data === undefined) ? '' : String(data).trim();
	if (s === '') { return '<span class="due-empty" title="ยังไม่มีวันครบกำหนด">-</span>'; }
	var m = /^(\d{1,2})\/(\d{1,2})\/(\d{4})$/.exec(s);
	if (!m) { return escAttr(s); }
	var d = new Date(Number(m[3]), Number(m[2]) - 1, Number(m[1]));
	if (d.getDate() !== Number(m[1]) || d.getMonth() !== Number(m[2]) - 1) { return escAttr(s); }
	var closed = row && (row.saleStatus === 'C' || row.saleStatus === 'X');
	if (closed) { return '<span class="due-plain">' + escAttr(s) + '</span>'; }
	var today = new Date(); today.setHours(0, 0, 0, 0);
	var diff = Math.round((d.getTime() - today.getTime()) / 86400000);
	var cls = '', note = '';
	if (diff < 0) { cls = 'due-over'; note = 'เลย ' + (-diff) + ' วัน'; }
	else if (diff === 0) { cls = 'due-today'; note = 'วันนี้'; }
	else if (diff <= 7) { cls = 'due-soon'; note = 'อีก ' + diff + ' วัน'; }
	if (cls === '') { return '<span class="due-plain">' + escAttr(s) + '</span>'; }
	return '<span class="due-pill ' + cls + '" title="ครบกำหนด ' + escAttr(s) + ' (' + note + ')"><b>' + escAttr(s) + '</b><small>' + note + '</small></span>';
}

function preLoaderHandler(x) {
	x.style.display = 'none';
}
function trim(str) {
	if (str == null) { // test for null or undefined
		return "";
	}
	str = str.replace(/^\s+/, '');
	for (var i = str.length - 1; i >= 0; i--) {
		if (/\S/.test(str.charAt(i))) {
			str = str.substring(0, i + 1);
			break;
		}
	}
	return str;
}

function dateHeight(dateStr) {
	if (trim(dateStr) != '') {
		var frDate = trim(dateStr).split(' ');
		// 	var frTime = frDate[1].split(':');
		var frDateParts = frDate[0].split('/');
		var day = frDateParts[0] * 60 * 24;
		var month = frDateParts[1] * 60 * 24 * 31;
		var year = frDateParts[2] * 60 * 24 * 366;
		// 	var hour = frTime[0] * 60;
		// 	var minutes = frTime[1];
		// 	var x = day+month+year+hour+minutes;
		var x = day + month + year;
	} else {
		var x = 99999999999999999; //GoHorse!
	}
	return x;
}
function checkString(str) {
	var regex = /^[a-zA-Z]+$/;
	if (str.match(regex)) { return true; }
	else { return false; }
}
function checkInt(str) {
	var regex = /^[0-9]+$/;
	if (regex.test(str)) { return true; }
	else { return false; }
}
function isBlank(str) {
	return (!str || /^\s*$/.test(str));
}
function isNumeric(value) {
	return /^\d+$/.test(value);
}
function stringToDate(dateString) {
	var date = new Date();
	var datearray = dateString.split("/");
	date = new Date(datearray[1] + '-' + datearray[0] + '-' + datearray[2]);
	return date;
}

function dateDDMMYYYToDDMM(dateStr) {
	var datearray = dateStr.split("/");
	if (datearray.length > 1) {
		dateStr = datearray[0] + '/' + datearray[1]
	} else {
		dateStr = ''
	}
	return dateStr;
}

// ช่วยผู้กรอกในตาราง Detail: ใส่ placeholder ให้ช่องวันที่/หมายเหตุ และเครื่องหมาย ✎ ที่หัวคอลัมน์ที่กรองได้
// แก้เฉพาะ attribute/ป้ายหัวตาราง — ไม่เปลี่ยนค่า การบันทึก หรือ event ของช่อง · เรียกซ้ำได้ (idempotent)
function decorateDetailInputCells() {
	var dateSel = '#MainTable .CFMPlanLabDateInput, #MainTable .SendCFMCusDateInput, #MainTable .DeliveryDateInput';
	var textSel = '#MainTable .CauseOfDelayInput, #MainTable .PCRemarkInput, #MainTable .ReplacedRemarkInput, '
		+ '#MainTable .SwitchRemarkInput, #MainTable .StockRemarkInput, #MainTable .StockLoadInput';
	$(dateSel).each(function () { if (!this.placeholder) { this.placeholder = 'dd/MM/yyyy'; } });
	$(textSel).each(function () { if (!this.placeholder) { this.placeholder = 'พิมพ์ที่นี่'; } });
	var editable = ['Plan CFM LAB', 'Send CFM Cus Date', 'Plan Delivery Date', 'Cause of Delay', 'Delayed Department',
		'PCRemark', 'ReplacedRemark', 'SwitchRemark', 'StockRemark', 'StockLoad'];
	$('.dataTables_scrollHead th, #MainTable thead th').each(function () {
		var $th = $(this);
		if ($th.find('.th-edit-mark').length === 0 && editable.indexOf($.trim($th.text())) !== -1) {
			$th.prepend('<span class="th-edit-mark" aria-hidden="true" title="ช่องสำหรับกรอก">✎ </span>');
		}
	});
}

// เตือนเมื่อค้นหาไม่ได้เพราะยังไม่ได้เลือกตัวกรองบังคับ — บอกว่ายังขาดช่องไหน และไม่ปิดเอง (เดิมหายไปเองใน 1 วินาที ไม่บอกว่าขาดอะไร)
function warnEmptySearchFilters(customer, customerShort, userStatus, division) {
	var miss = [];
	if (!customer || customer.length === 0) { miss.push('Customer'); }
	if (!customerShort || customerShort.length === 0) { miss.push('Cust.(Name4)'); }
	if (!userStatus || userStatus.length === 0) { miss.push('UserStatus'); }
	if (!division || division.length === 0) { miss.push('Division'); }
	Swal.fire({
		title: 'ยังค้นหาไม่ได้',
		html: 'ต้องเลือกอย่างน้อย 1 รายการในช่อง: <b>' + miss.join(', ') + '</b>'
			+ '<br><small>หลังกด Clear Search ช่องเหล่านี้จะว่าง — กด "Select All" หรือเลือกรายการก่อน แล้วค้นหาอีกครั้ง</small>',
		icon: 'warning',
		confirmButtonText: 'ตกลง'
	});
}
