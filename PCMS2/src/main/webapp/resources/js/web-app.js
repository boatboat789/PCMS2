
var domain;
var urlLBMS;
var urlLBMSObj;
var urlSFC;
var urlSFCObj;
var urlInspect;
var urlInspectObj;
var urlQCMS;
var urlQCMSObj;
var os;
var userId = '';
var isCustomer;

var VIRTUAL_PRD_ORDERS = ["รอจัด Lot", "ขาย stock", "รับจ้างถัก", "พ่วงแล้วรอสวม", "รอสวมเคยมี Lot", "Lot ขายแล้ว"];

$(document).ready(function() {
    initializeData();
    //--------------------------------------- SEARCH ----------------------------------------------
    $('#btn_lbms').on('click', function() {
        var tblData = MainTable.rows('.selected').data();
        if (tblData.length == 0) {
            Swal.fire({
                title: 'คำเตือน',
                text: 'Need to select atleast 1 row.',
                icon: 'warning',
                timer: 1000,
                showConfirmButton: false,
            })
        }
        else {
            var prdOrder = tblData[0].productionOrder
            if (VIRTUAL_PRD_ORDERS.includes(prdOrder)) {
                Swal.fire({
                    title: 'คำเตือน',
                    text: 'Need to select atleast 1 row.',
                    icon: 'warning',
                    timer: 1000,
                    showConfirmButton: false,
                })
            }
            else {
                var arrayTmp = [];
                arrayTmp.push();
                getEncrypted('LBMS', tblData, userId, arrayTmp);
            }
        }
    });
    $('#btn_inspect').on('click', function() {
        var tblData = MainTable.rows('.selected').data();
        if (tblData.length == 0) {
            Swal.fire({
                title: 'คำเตือน',
                text: 'Need to select atleast 1 row.',
                icon: 'warning',
                timer: 1000,
                showConfirmButton: false,
            })
        }
        else {
            var prdOrder = tblData[0].productionOrder
            if (VIRTUAL_PRD_ORDERS.includes(prdOrder)) {
                Swal.fire({
                    title: 'คำเตือน',
                    text: 'Need to select atleast 1 row.',
                    icon: 'warning',
                    timer: 1000,
                    showConfirmButton: false,
                })
            }
            else {

                var arrayTmp = [];
                arrayTmp.push();
                getEncrypted('INSPECT', tblData, userId, arrayTmp);
            }
        }
    });
    $('#btn_sfc').on('click', function() {
        var tblData = MainTable.rows('.selected').data();
        if (tblData.length == 0) {
            Swal.fire({
                title: 'คำเตือน',
                text: 'Need to select atleast 1 row.',
                icon: 'warning',
                timer: 1000,
                showConfirmButton: false,
            })
        }
        else {
            var prdOrder = tblData[0].productionOrder
            if (VIRTUAL_PRD_ORDERS.includes(prdOrder)) {
                Swal.fire({
                    title: 'คำเตือน',
                    text: 'Need to select atleast 1 row.',
                    icon: 'warning',
                    timer: 1000,
                    showConfirmButton: false,
                })
            }
            else {
                var arrayTmp = [];
                arrayTmp.push();
                getEncrypted('SFC', tblData, userId, arrayTmp);

            }
        }

    });
    $('#btn_qcms').on('click', function() {
        var tblData = MainTable.rows('.selected').data();
        if (tblData.length == 0) {
            Swal.fire({
                title: 'คำเตือน',
                text: 'Need to select atleast 1 row.',
                icon: 'warning',
                timer: 1000,
                showConfirmButton: false,
            })
        }
        else {
            var prdOrder = tblData[0].productionOrder
            if (VIRTUAL_PRD_ORDERS.includes(prdOrder)) {
                Swal.fire({
                    title: 'คำเตือน',
                    text: 'Need to select atleast 1 row.',
                    icon: 'warning',
                    timer: 1000,
                    showConfirmButton: false,
                })
            }
            else {
                var arrayTmp = [];
                arrayTmp.push();
                getEncrypted('QCMS', tblData, userId, arrayTmp);
            }
        }
    });
})

async function initializeData() {
    // ใช้ $.ajax เพื่อให้รองรับ async/await ได้เสถียรขึ้น
    const response = await $.ajax({
        url: ctx + "/Main/getInitData", // ตรวจสอบ URL นี้ใน Controller ให้ดีนะครับ
        method: "GET",
        dataType: "json" // บอกให้ jQuery parse JSON ให้เลย ไม่ต้อง JSON.parse เอง
    });

    // ถ้าสำเร็จ ข้อมูลจะอยู่ใน response
    os = response.os;
    userId = response.userId;
    isCustomer = response.isCustomer;

    let result = os.includes("win");
    if (result === true) { domain = "http://" + window.location.hostname + ":8080"; }
    else { domain = "https://" + window.location.hostname; }
    // 	domain = domain+window.location.hostname+:"8080";
    urlLBMS = domain + "/LBMS/";
    urlLBMSObj = domain + "/LBMS/LabHistory";
    urlSFC = domain + "/SFC/";
    urlSFCObj = domain + "/SFC/HistoryWork";
    urlInspect = domain + "/InspectSystem/search/home.html";
    urlInspectObj = domain + "/InspectSystem/search/home.html";
    urlQCMS = domain + "/QCMS/first.html";
    urlQCMSObj = domain + "/QCMS/request/search.html";
    // ---------------------------------------- set----------
    if (isCustomer == true) {
        isCustomer = 1
    }
    else {
        isCustomer = 0;
    }
}
function getEncrypted(webApp, tblData, userId, arrayTmp) {
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: ctx + "/Main/getEncrypted/" + userId,
        data: JSON.stringify(arrayTmp),
        success: function(data) {
            if (webApp == 'LBMS') {
                goToLBMS(tblData, userId, data);
            }
            else if (webApp == 'SFC') {
                goToSFC(tblData, userId, data);
            }
            else if (webApp == 'INSPECT') {
                goToInspect(tblData, userId, data);
            }
            else if (webApp == 'QCMS') {
                goToQCMS(tblData, userId, data);
            }

        }
    });
}
function goToLBMS(tblData, pUserId, data) {
    var prdOrder = tblData[0].productionOrder
    var article = tblData[0].articleFG
    var color = tblData[0].color
	var matNo = tblData[0].materialNo
	if (typeof article === 'undefined' || typeof color === 'undefined' ) {
		// 1. เช็คว่ามีข้อมูล และยาวพอที่จะตัด (Prefix 1 + Article 8 = 9 ตัว)
		article = '';
		color = '';
		if (matNo.length >= 9) {

		    // 2. เช็ค Prefix ตัวแรก (K, P, O)
		    var prefix = matNo.charAt(0).toUpperCase();
		    if (prefix === 'K' || prefix === 'P' || prefix === 'O'|| prefix === 'H'|| prefix === 'V') {

		        article = matNo.substring(1, 9); // ตัดเอาตัวที่ 2 ถึง 9
		        color = matNo.substring(9);      // ที่เหลือคือ Color

		        // 3. ถ้า color ว่าง (กรณีไม่มีข้อมูลต่อท้าย) ให้ใส่ default
		        if (color === "") color = "-";
		    }
		}
	}
    $.ajax({
        url: urlLBMS,
        type: 'GET',
        data: {
            "comeFrom": data.encrypted,
            "isCustomer": isCustomer
        },
        success: function(data) {
            var url = urlLBMSObj;
            var tab = window.open(url);  //var tab = window.open(url, '_blank').focus();
            tab.onload = function() {
                tab.document.getElementById('input_article').value = article;
                tab.document.getElementById('input_color').value = color;  //'S2A001'
                tab.searchHistory();
                tab.document.getElementById('nav-prd-tab').click();
                setTimeout(function() {
                    tab.$('#prodOrderTable').DataTable().search(prdOrder).draw();
                }, 500);
            };
            tab.addEventListener('load', (event) => {

            });
        },
        error: function(e) {
            Swal.fire("Fail", "เกิดข้อผิดพลาด / กรุณาติดต่อทีม IT", "error");
            console.log(e)
        }
    });
}

function goToSFC(tblData, pUserId, data) {
    var prdOrder = tblData[0].productionOrder
    $.ajax({
        url: urlSFC,
        type: 'GET',
        data: {
            "comeFrom": data.encrypted,
            "isCustomer": isCustomer
        },
        success: function(data) {
            var url = urlSFCObj;
            var tab = window.open(url);  //var tab = window.open(url, '_blank').focus();
            tab.onload = function() {
                tab.document.getElementById('input_searchProductionOrder').value = prdOrder;
                tab.searchByPrdOrder(prdOrder);
            };
        },
        error: function(e) {
            Swal.fire("Fail", "เกิดข้อผิดพลาด / กรุณาติดต่อทีม IT", "error");
            console.log(e)
        }
    });
}
function goToInspect(tblData, pUserId, data) {
    var prdOrder = tblData[0].productionOrder
    $.ajax({
        url: urlInspect,
        type: 'GET',
        data: {
            "comeFrom": data.encrypted,
            "isCustomer": isCustomer
        },
        success: function(data) {
            var url = urlInspectObj;
            var tab = window.open(url);
            tab.onload = function() {
                tab.document.getElementById('prdNumber').value = prdOrder;  //'S2A001'
                tab.document.getElementById('btnSearch').click();
            };
        },
        error: function(e) {
            Swal.fire("Fail", "เกิดข้อผิดพลาด / กรุณาติดต่อทีม IT", "error");
            console.log(e)
        }
    });
}
function goToQCMS(tblData, pUserId, data) {
    var article = tblData[0].articleFG
    var lotNo = tblData[0].lotNo
    var color = tblData[0].color
	var matNo = tblData[0].materialNo
	if (typeof article === 'undefined' || typeof color === 'undefined' ) {
		// 1. เช็คว่ามีข้อมูล และยาวพอที่จะตัด (Prefix 1 + Article 8 = 9 ตัว)
		article = '';
		color = '';
		if (matNo.length >= 9) {

		    // 2. เช็ค Prefix ตัวแรก (K, P, O)
		    var prefix = matNo.charAt(0).toUpperCase();
		    if (prefix === 'K' || prefix === 'P' || prefix === 'O'|| prefix === 'H'|| prefix === 'V') {

		        article = matNo.substring(1, 9); // ตัดเอาตัวที่ 2 ถึง 9
		        color = matNo.substring(9);      // ที่เหลือคือ Color

		        // 3. ถ้า color ว่าง (กรณีไม่มีข้อมูลต่อท้าย) ให้ใส่ default
		        if (color === "") color = "-";
		    }
		}
	}
    $.ajax({
        url: urlQCMSObj,
        type: 'GET',
        //	 	    async : false,
        data: {
            "comeFrom": data.encrypted,
            "isCustomer": isCustomer
        },
        success: function(data) {
            var url = urlQCMSObj;
            var tab = window.open(url);
            tab.onload = function() {
                tab.document.getElementById('article').value = article;  //'S2A001'
                tab.document.getElementById('lotNumber').value = lotNo;  //'S2A001'
                tab.document.getElementById('color').value = color;  //'S2A001'
                if (isCustomer != 1) {
                    setTimeout(function() {
                        tab.document.getElementById('btnSearchRequest').click();
                    }, 500);
                }
            };
        },
        error: function(e) {
            Swal.fire("Fail", "เกิดข้อผิดพลาด / กรุณาติดต่อทีม IT", "error");
            console.log(e)
        }
    });
}
