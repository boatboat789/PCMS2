// escape ค่าที่จะใส่ใน attribute ของ HTML ที่ต่อสตริงเอง (value="...", data-search="...") — กัน " ตัดค่า / XSS
function escAttr(v) {
    if (v === null || v === undefined) { return ''; }
    return String(v).replace(/&/g, '&amp;').replace(/"/g, '&quot;').replace(/'/g, '&#39;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
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
