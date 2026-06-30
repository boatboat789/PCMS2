<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/pages/config/meta.jsp"></jsp:include>
<title>Log - การรับข้อมูลของ SaleOrder</title>
	<jsp:include page="/WEB-INF/pages/config/css/baseCSS.jsp"></jsp:include>       
	<link href="<c:url value="/resources/css/style_overide.css" />" rel="stylesheet" type="text/css">    
	<link href="<c:url value="/resources/css/datatable.overide.css" />" rel="stylesheet" type="text/css">     
</head>      
<body>
	<jsp:include page="/WEB-INF/pages/config/navbar.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/pages/config/loading.jsp"></jsp:include> 
	<jsp:include page="/WEB-INF/pages/config/Log/searchLog.jsp"></jsp:include>
	<div id="wrapper">
		<div class="content-panel">
			<div class="table-responsive font-Group14" style="margin-top: 10px;">
				<table id="reportTable" class="table compact  table-bordered table-striped text-center" style="width: 100%">
					<thead> 
						<tr>
							<th class="row-table" style="vertical-align: middle;">Sale<span class="c" style="display: block;">Order</span></th>
							<th class="row-table" style="vertical-align: middle;">Sale<span class="c" style="display: block;">Line</span></th>
							<th class="row-table" style="vertical-align: middle;">Division</th>
							<th class="row-table" style="vertical-align: middle;">Material<span class="c" style="display: block;">No</span></th>
							<th class="row-table" style="vertical-align: middle;">Article<span class="c" style="display: block;">FG</span></th>
							<th class="row-table" style="vertical-align: middle;">Design<span class="c" style="display: block;">FG</span></th>
							<th class="row-table" style="vertical-align: middle;">Color</th>
							<th class="row-table" style="vertical-align: middle;">Dist<span class="c" style="display: block;">Channel</span></th>
							<th class="row-table" style="vertical-align: middle;">Customer<span class="c" style="display: block;">Name</span></th>
							<th class="row-table" style="vertical-align: middle;">Customer<span class="c" style="display: block;">Short Name</span></th>
							<th class="row-table" style="vertical-align: middle;">Color<span class="c" style="display: block;">Customer</span></th>
							<th class="row-table" style="vertical-align: middle;">Sale<span class="c" style="display: block;">Create Date</span></th>
							<th class="row-table" style="vertical-align: middle;">Plan<span class="c" style="display: block;">Greige Date</span></th>
							<th class="row-table" style="vertical-align: middle;">Due<span class="c" style="display: block;">Date</span></th>
							<th class="row-table" style="vertical-align: middle;">Customer<span class="c" style="display: block;">Due</span></th>
							<th class="row-table" style="vertical-align: middle;">Sale<span class="c" style="display: block;">Quantity</span></th>
							<th class="row-table" style="vertical-align: middle;">Sale<span class="c" style="display: block;">Unit</span></th>
							<th class="row-table" style="vertical-align: middle;">Order<span class="c" style="display: block;">Amount</span></th>
							<th class="row-table" style="vertical-align: middle;">Remain<span class="c" style="display: block;">Quantity</span></th>
							<th class="row-table" style="vertical-align: middle;">Remain<span class="c" style="display: block;">Amount</span></th>
							<th class="row-table" style="vertical-align: middle;">Purchase<span class="c" style="display: block;">Order</span></th>
							<th class="row-table" style="vertical-align: middle;">Customer<span class="c" style="display: block;">No</span></th>
							<th class="row-table" style="vertical-align: middle;">Customer<span class="c" style="display: block;">Material</span></th>
							<th class="row-table" style="vertical-align: middle;">Sale<span class="c" style="display: block;">Org</span></th>
							<th class="row-table" style="vertical-align: middle;">Sale<span class="c" style="display: block;">Status</span></th>
							<th class="row-table" style="vertical-align: middle;">Sale<span class="c" style="display: block;">Full Name</span></th>
							<th class="row-table" style="vertical-align: middle;">Delivery<span class="c" style="display: block;">Status</span></th>
							<th class="row-table" style="vertical-align: middle;">Sync<span class="c" style="display: block;">Date</span></th>
							<th class="row-table" style="vertical-align: middle;">Sync<span class="c" style="display: block;">Date Header</span></th>
						</tr>
					</thead>   
					<tbody>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</body>
<%-- <jsp:include page="/WEB-INF/pages/config/footer.jsp"></jsp:include>          --%>
<style>
/* td { */
/*     background-color: transparent !important; */
/* } */
/* tr.odd td { */     
/* 	background-color: unset !important; */
/* } */

/* tr.even td { */
/* 	background-color: unset !important; */
/* } */

.form-group {
	margin-bottom: 5px;
}
</style>
<script src="<c:url value="/resources/js/DatatableSort.js" />"></script>
<script src="<c:url value="/resources/js/General.js" />"></script> 
<script>
	var ctx = "${pageContext.request.contextPath}"
	var preloader = document.getElementById('loader');
          
	var titleName =JSON.parse('${titleName}');
	var dataType =JSON.parse('${dataType}');       
	var caseDupli = 0;
	var poTmp;
	var poTmpLine;
	var reportTable;
	var jsonStr;
	var today = new Date();
	var startDate = today.getDate() + '/' + (today.getMonth() + 1) + '/' + today.getFullYear();
	$(document).on('keypress', 'input,select', function(e) {
		// 		if (e.which === 13) {   
		// 			if (e.target.id == 'input_po') { searchByDetail()   ;   }         
		// 		}   
	});
  
	$('#input_createDate').daterangepicker({	
		opens : 'right',
		   startDate: moment().subtract(1, 'months'),   
		   endDate: moment(), 
		locale : {
			format : 'DD/MM/YYYY',
			cancelLabel : 'Clear'
		},
		drops : "auto",
	//	  autoUpdateInput: false,        
	// 	  autoApply: true,                 
	}, function(start, end, label) {
	});
	var CheckreportTable = 0;
	$(document).ready(function() {
		$(document).ajaxStart(function() {
			$("#loading").css("display", "block");
		});
		$(document).ajaxStop(function() {
			$("#loading").css("display", "none");
		});    
		// 	$.fn.dataTable.moment( 'DD/MM/YYYY hh:mm:ss' );   
		document.getElementById('id_pageType').innerHTML = titleName;

		const element = document.getElementById('div_input_sale'); 
		// Check if the element exists to prevent errors
		if (element) { 
		    element.style.display = 'inline-flex';
		}
		let  labelSearchType = '';   
		if(dataType == 'ERP365'){
			labelSearchType = 'SyncDate : ';
		}
		else{
			labelSearchType = 'Change Date : '; 
		}
		 var label = document.getElementById('label_createDate');
		label.textContent = labelSearchType; // sets the new label text 
		//   	document.getElementById("div_input_po").style.display = "none";
		//   	document.getElementById("div_input_createDate").style.display = "none";
		//   	document.getElementById("div_multi_logType").style.display = "none";
		//                        TO                     FROM
		DataTable.render.datetime('DD/MM/YYYY HH:mm:ss', 'DD/MM/YYYY HH:mm:ss', 'en');
		DataTable.datetime('DD/MM/YYYY HH:mm:ss');  

		$('#multi_logType').selectpicker();           
// 		$('#input_createDate').val('');
		$('input[name="daterange"]').on('cancel.daterangepicker', function(ev, picker) {
			$(this).val('');
		});
		$('#reportTable thead tr').clone(true).appendTo('#reportTable thead');
		$('#reportTable thead tr:eq(1) th').each(function(i) {
			var title = $(this).text();
			$(this).html('<input type="text" class="monitor_search" style="width:100%" data-index="' + i + '"/>');
		});
		reportTable = $('#reportTable').DataTable({
			columns: [
			    { data: "saleOrder", defaultContent: "" },
			    { data: "saleLine", defaultContent: "" },
			    { data: "division", defaultContent: "" },
			    { data: "materialNo", defaultContent: "" },
			    { data: "articleFG", defaultContent: "" },
			    { data: "designFG", defaultContent: "" },
			    { data: "color", defaultContent: "" },
			    { data: "distChannel", defaultContent: "" },
			    { data: "customerName", defaultContent: "" },
			    { data: "customerShortName", defaultContent: "" },
			    { data: "colorCustomer", defaultContent: "" },
			    { data: "saleCreateDate", defaultContent: "", 
			        render: DataTable.render.datetime('MMM D, YYYY', 'DD/MM/YYYY', 'en') },
			    { data: "planGreigeDate", defaultContent: "", 
			        render: DataTable.render.datetime('MMM D, YYYY', 'DD/MM/YYYY', 'en') },
			    { data: "dueDate", defaultContent: "", 
			        render: DataTable.render.datetime('MMM D, YYYY', 'DD/MM/YYYY', 'en') },
			    { data: "customerDue", defaultContent: "" },
			    { data: "saleQuantity", defaultContent: "", type: "num" },
			    { data: "saleUnit", defaultContent: "" },
			    { data: "orderAmount", defaultContent: "", type: "num" },
			    { data: "remainQuantity", defaultContent: "", type: "num" },
			    { data: "remainAmount", defaultContent: "", type: "num" },
			    { data: "purchaseOrder", defaultContent: "" },
			    { data: "customerNo", defaultContent: "" },
			    { data: "customerMaterial", defaultContent: "" },
			    { data: "saleOrg", defaultContent: "" },
			    { data: "saleStatus", defaultContent: "" },
			    { data: "saleFullName", defaultContent: "" },
			    { data: "deliveryStatus", defaultContent: "" },
			    { data: "syncDate", defaultContent: "", 
			        render: DataTable.render.datetime('MMM D, YYYY h:mm:ss A', 'DD/MM/YYYY HH:mm:ss', 'en') },
			    { data: "syncDateHeader", defaultContent: "", 
			        render: DataTable.render.datetime('MMM D, YYYY h:mm:ss A', 'DD/MM/YYYY HH:mm:ss', 'en') }
			],    

			scrollX : true,
			scrollY : '60vh',
			scrollCollapse : true,
			orderCellsTop : true,  
			pageLength : 1000,      
			lengthChange : false,
		    deferRender: true, // ***** เปิดใช้งานตรงนี้ ***** 
		    fixedColumns: {    
// 		        start: 1   
// // 		        ,end: 0     
    left: 2,  // Fix first 2 columns
    right: 0  // Fix 0 columns on the right       
		    },         
			select : {
				style : 'os',    
				selector : 'td:not(.status)'
			},
			createdRow : function(row, data, index) {
			},
			drawCallback : function(settings) {
			},
			initComplete : function() {       
			}              
		});        
		if(dataType == 'ERP365'){        
// 		    reportTable.column(28).visible(false);    	
// 		    reportTable.draw();
		            
// 		    reportTable.columns(20).visible(true, false);
		    
// 		    reportTable.columns(':hidden').visible(true, false);
// 		    reportTable.draw();
		} 
		else{
		    reportTable.column(28).visible(false);    	 
		    reportTable.draw();
		}
		$("#reportTable_wrapper .dataTables_scrollHead").on('keyup', '.monitor_search', function() {
			reportTable.column($(this).data('index')).search(this.value).draw(); 
		});
		$('#btn_search').on('click', function() {
			var createDate = document.getElementById("input_createDate").value.trim();
			if (document.getElementById("input_createDate").value.trim() == '' &&
				document.getElementById("input_saleOrder").value.trim() == '') {
				Swal.fire({
					title: 'คำเตือน',
					text : "Change Date or SaleOrder must input data.",
					icon : 'warning',
				})
			}
			else {
				$('#btn_search').prop('disabled', true);
				searchByDetail();
			}
		});

		$("#reportTable_filter").hide();
		preLoaderHandler(preloader)
	}); 

	function setStickyToFilterColumn( ) {   
		var elements = document.getElementsByClassName('monitor_search'); // 
		let filClass = '';
		let filClassParent = '';  
		let filIndex = 0;            
		let width = '0px';    
		let widthCol = '0px';    
		let check = '';    
		for (i =  0; i < elements.length/2 ; i++) {               
			filClass = elements[i];         
			filIndex = filClass.getAttribute('data-index');    
			if(filIndex <= 1    
//	 				&& check !== undefined   
			){       
				filClassParent = filClass.closest('.row-table')      ;  
				widthCol =  filClassParent.style.width;            
				filClassParent.style.left = width;            
			    filClassParent.style.position = "sticky";      
			    width = width.replace("px", "");
			    widthCol = widthCol.replace("px", "");      
			    width = parseFloat(width) + parseFloat(widthCol);
			    width = width+ 'px';   
			}  
			else{   
				filClassParent = filClass.closest('.row-table')      ;   
				filClassParent.style.position = "unset";      
			}
		}     
		//1st param is insert index = 2 means insert at index 2
		//2nd param is delete item count = 0 means delete 0 elements
		//3rd param is new item that you want to insert  
//	 	if (index > -1) {  
//	 		arrayCol.splice(index, 1); // 2nd parameter means remove one item only 
//	 		arrayCol.splice(0, 0 , index);
//	 	}   
// 		return arrayCol;  
	} 
// GET - LIST JSONS  
	function searchByDetail() {  
		const query = createParam(); 
	    getSaleOrderLogBySearch(query);
	}     
	function createParam() {    
	    const saleOrder = document.getElementById("input_saleOrder").value.trim();
	    const createDate = document.getElementById("input_createDate").value.trim();
	    const words = createDate.split(" - "); 
	    // Create an object and convert it to JSON
	    const data = {  
	    	saleOrder: saleOrder,
        	changeDateStart: words[0] ,
        	changeDateEnd: words[1]   
	    };            
	    var query = "saleOrder=" + encodeURIComponent(data.saleOrder)
	              + "&changeDateStart=" + encodeURIComponent(data.changeDateStart)
	              + "&changeDateEnd=" + encodeURIComponent(data.changeDateEnd); 
	    return query  ;
	}

	function getSaleOrderLogBySearch(query) {       
		$.ajax({    
			type : "GET",        
			dataType : "json", 
			contentType : "application/json; charset=utf-8",
			url : ctx + "/Log/SaleOrderLog/"+dataType+"/getSaleOrderLogBySearch?"+ query,  
			success : function(response) {
				reportTable.clear();
				if(response.status === "success") {
					if (response.data.length > 0) {
						reportTable.rows.add(response.data);
					}
				}
				reportTable.columns.adjust();
				reportTable.draw(); 
				setStickyToFilterColumn() ;   
			},
			error : function(e) {
				Swal.fire("Fail", "เกิดข้อผิดพลาด / กรุณาติดต่อทีม IT", "error");
			},
			complete : function() {
				$('#btn_search').prop('disabled', false);
			}
		});
	}


// POST - JSONS
// 	function searchByDetail() {  
// 		const json = createJsonData();
// // 	    const obj = JSON.parse(json);
// // 	    const arrayTmp = [obj]; // Directly create an array with the object
// 	    getSaleOrderLogBySearch(json);    
// 	}    
// 	function createJsonData() {
// 	    const prodOrder = document.getElementById("input_prodOrder").value.trim();
// 	    const createDate = document.getElementById("input_createDate").value.trim();
// 	    const words = createDate.split(" - "); 
// 	    // Create an object and convert it to JSON
// 	    const data = {
// 	        productionOrder: prodOrder,
//         	changeDateStart: words[0] ,   
//         	changeDateEnd: words[1]     
// 	    	,
// 	    	  "typeCarList": [ "Mustang", "Cardiac" ],
// 	    	  "carList": [
// 	    	    { "name": "CAR1", "color": "Red" },
// 	    	    { "name": "Car2", "color": "Blue" }
// 	    };          
// 	    return JSON.stringify(data);
// 	}
// 	function getSaleOrderLogBySearch(value) {
// 			console.log(value)        
// 		$.ajax({     
// 			type : "POST",  
// 			dataType : "json", 
// 			contentType : "application/json; charset=utf-8",
// 			url : ctx + "/Log/SaleOrderLog/"+dataType+"/getSaleOrderLogBySearch", 
// 	        data: value,
// 			success : function(response) {
// 				if(response.status === "success") {
// 					reportTable.clear();
// 					if (response.data.length > 0) { 
// 						reportTable.rows.add(response.data); 
// 					} 
// 					reportTable.columns.adjust();
// 					reportTable.draw(); 
// // 					swal({
// // 						title: 'สำเร็จ',
// // 						text: response.message,
// // 						icon: "info",
// // 						button: 'ตกลง'
// // 					});
// 				} else {
// // 					swal({
// // 						title: 'คำเตือน',
// // 						text: response.message,
// // 						icon: "warning",
// // 						timer: 1000,
// // 						buttons: false
// // 					});
// 				}
// 			},
// 			error : function(e) {
// 				Swal.fire("Fail", "เกิดข้อผิดพลาด / กรุณาติดต่อทีม IT", "error");
// 			},
// 			done : function(e) {
// 				console.log(response);
// 			}
// 		});
// 	}

// POST - LIST JSONS 
// 	function searchByDetail() {  
// 		const list = createListJsonData(); 
// 	    getSaleOrderLogBySearch(list);    
// 	}    
// 	function createListJsonData() {
// 	    const prodOrder = document.getElementById("input_prodOrder").value.trim();
// 	    const createDate = document.getElementById("input_createDate").value.trim();
// 	    const words = createDate.split(" - "); 
// 	    // Create an object and convert it to JSON
// 	    const list = [
// 	    	{
// 	        productionOrder: prodOrder+'2',
//         	changeDateStart: words[0] ,   
//         	changeDateEnd: words[1]     
// // 	    	,
// // 	    	  "typeCarList": [ "Mustang", "Cardiac" ],
// // 	    	  "carList": [
// // 	    	    { "name": "CAR1", "color": "Red" },
// // 	    	    { "name": "Car2", "color": "Blue" }
// 	    	},
// 	    	{
// 		    productionOrder: prodOrder+'1',
// 	        changeDateStart: words[0] ,   
// 	        changeDateEnd: words[1]     
// // 	    	,
// //	    	  "typeCarList": [ "Mustang", "Cardiac" ],
// //	    	  "carList": [
// //	    	    { "name": "CAR1", "color": "Red" },
// //	    	    { "name": "Car2", "color": "Blue" }
// 		    }
//     	];              
	        
// 	    return JSON.stringify(list);
// 	} 
// 	function getSaleOrderLogBySearch(list) {
// 			console.log(list)        
// 		$.ajax({     
// 			type : "POST",  
// 			dataType : "json", 
// 			contentType : "application/json; charset=utf-8",
// 			url : ctx + "/Log/SaleOrderLog/"+dataType+"/getSaleOrderLogBySearch", 
// 	        data: list,
// 			success : function(response) { 
// 				if(response.status === "success") {
// 					reportTable.clear();
// 					if (response.data.length > 0) { 
// 						reportTable.rows.add(response.data); 
// 					} 
// 					reportTable.columns.adjust();
// 					reportTable.draw(); 
// // 					swal({
// // 						title: 'สำเร็จ',
// // 						text: response.message,
// // 						icon: "info",
// // 						button: 'ตกลง'
// // 					});
// 				} else {
// // 					swal({
// // 						title: 'คำเตือน',
// // 						text: response.message,
// // 						icon: "warning",
// // 						timer: 1000,
// // 						buttons: false
// // 					});
// 				}
// 			},
// 			error : function(e) {
// 				Swal.fire("Fail", "เกิดข้อผิดพลาด / กรุณาติดต่อทีม IT", "error");
// 			},
// 			done : function(e) {
// 				console.log(response);
// 			}
// 		});
// 	}
</script>

</html>