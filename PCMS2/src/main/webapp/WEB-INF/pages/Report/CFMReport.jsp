<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/pages/config/meta.jsp"></jsp:include>  	
<title><%=request.getAttribute("HeaderName")%></title>
<jsp:include page="/WEB-INF/pages/config/css/baseCSS.jsp"></jsp:include>
<link href="<c:url value="/resources/css/style_overide.css" />" rel="stylesheet" type="text/css">
<link href="<c:url value="/resources/css/datatable.overide.css" />" rel="stylesheet" type="text/css">
</head>
<body>
	<jsp:include page="/WEB-INF/pages/config/navbar.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/pages/config/loading.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/pages/config/Report/CFMReport/search.jsp"></jsp:include>
	<div id="wrapper">
		<div class="col-12 col-sm-12 col-md-12 col-xl-12">
			<div class="table-responsive " style="font-size: 12px;">
				<table id="mainTable" class="table compact  table-bordered table-striped text-center" style="width: 100%">
					<thead>
						<tr>
							<th style="vertical-align: middle;">Send<span class="c" style="display: block;">Date</span></th>
							<th style="vertical-align: middle;">No Per<span class="c" style="display: block;">Day</span></th>
							<th style="vertical-align: middle;">Reply<span class="c" style="display: block;">Date</span></th>
							<th style="vertical-align: middle;">CFM No</th>
							<th style="vertical-align: middle;">Customer<span class="c" style="display: block;">Name</span></th>
							<th style="vertical-align: middle;">SO</th>
							<th style="vertical-align: middle;">SO<span class="c" style="display: block;">Line</span></th>
							<th style="vertical-align: middle;">Due<span class="c" style="display: block;">Date</span></th>
							<th style="vertical-align: middle;">PO</th>
							<th style="vertical-align: middle;">Material</th>
							<th style="vertical-align: middle;">Product<span class="c" style="display: block;">Name</span></th>
							<th style="vertical-align: middle;">Lab No</th>
							<th style="vertical-align: middle;">Color</th>
							<th style="vertical-align: middle;">Prod<span class="c" style="display: block;">ID</span></th>
							<th style="vertical-align: middle;">Lot No</th>

							<th style="vertical-align: middle;">Dye L</th>
							<th style="vertical-align: middle;">Dye Da</th>
							<th style="vertical-align: middle;">Dye DB</th>
							<th style="vertical-align: middle;">Dye St</th>
							<th style="vertical-align: middle;">Dye<span class="c" style="display: block;">DeltaE</span></th>


							<th style="vertical-align: middle;">ColorCheck<span class="c" style="display: block;">L</span></th>
							<th style="vertical-align: middle;">ColorCheck<span class="c" style="display: block;">Da</span></th>
							<th style="vertical-align: middle;">ColorCheck<span class="c" style="display: block;">Db</span></th>
							<th style="vertical-align: middle;">ColorCheck<span class="c" style="display: block;">St</span></th>
							<th style="vertical-align: middle;">ColorCheck<span class="c" style="display: block;">DeltaE</span></th>

							<th style="vertical-align: middle;">CFM L</th>
							<th style="vertical-align: middle;">CFM Da</th>
							<th style="vertical-align: middle;">CFM DB</th>
							<th style="vertical-align: middle;">CFM St</th>
							<th style="vertical-align: middle;">CFM<span class="c" style="display: block;">DeltaE</span></th>

							<th style="vertical-align: middle;">Color Check<span class="c" style="display: block;">Date</span></th>
							<th style="vertical-align: middle;">Color Check<span class="c" style="display: block;">Status</span></th>
							<th style="vertical-align: middle;">Color Check<span class="c" style="display: block;">Remark</span></th>

							<th style="vertical-align: middle;">Result</th>
							<th style="vertical-align: middle;">QC<span class="c" style="display: block;">Comment</span></th>
							<th style="vertical-align: middle;">Remark From<span class="c" style="display: block;">Submit</span></th>
							<th style="vertical-align: middle;">Next Lot</th>
							<th style="vertical-align: middle;">Qty</th>
							<th style="vertical-align: middle;">Unit<span class="c" style="display: block;">Id</span></th>
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
tr.odd td {
	background-color: unset !important;
}

tr.even td {
	background-color: unset !important;
}

.form-group {
	margin-bottom: 5px;
}
</style>
<script src="<c:url value="/resources/js/DatatableSort.js" />"></script>
<script src="<c:url value="/resources/js/General.js" />"></script>
<script>    
var preloader = document.getElementById('loader');  
<%-- var userObj = <%= session.getAttribute("userObject")%>; --%>
// var userFullName = userObj.firstName+' '+userObj.lastName;
// var userId = userObj.employeeID;  
var userStatusList ;  	 
var labStatusList ;    
var caseDupli = 0;
var poTmp;
var poTmpLine; 
var mainTable;    
var jsonStr;
var today = new Date();
var startDate = today.getDate() + '/' + (today.getMonth() + 1) + '/' + today.getFullYear();  
 $(document).on('keypress', 'input,select', function (e) {   
		if (e.which === 13) {   
			if (e.target.id == 'input_custName') { searchByDetail()   ;   }   
			else if (e.target.id == 'input_saleOrder') { searchByDetail()   ;   } 
			else if (e.target.id == 'input_lotNo') { searchByDetail()   ;   } 
			else if (e.target.id == 'input_prdOrder') { searchByDetail()   ;   }   
		}   
});       
 $('#input_replyDate').daterangepicker({
	    opens: 'right',             
	    locale: {
	        format: 'DD/MM/YYYY',
	        cancelLabel: 'Clear'        
	      },
	      drops : "auto",     
//	       autoUpdateInput: false,             
//	 	  autoApply: true,       
//	 	  autoUpdateInput: false,       
 })  ;
 $('#input_sendDate').daterangepicker({
	    opens: 'right',             
	    locale: {
	        format: 'DD/MM/YYYY',
	        cancelLabel: 'Clear'        
	      },
	      drops : "auto",     
//	       autoUpdateInput: false,             
//	 	  autoApply: true,       
//	 	  autoUpdateInput: false,       
})  ;
 var CheckMainTable = 0;
$(document) .ready( function() {

	DataTable.datetime('DD/MM/YYYY HH:mm:ss');
	$(document).ajaxStart(function() {$( "#loading").css("display","block"); });   
	$(document).ajaxStop(function() {$("#loading" ).css("display","none"); });  
	$('#mainTable thead tr').clone(true).appendTo('#mainTable thead');
	$('#mainTable thead tr:eq(1) th') .each( function(i) {
		var title = $(this).text();
		$(this).html( '<input type="text" class="monitor_search" style="width:100%" data-index="' + i + '"/>');
	});      
   
	$('#input_replyDate').val(''); 
	$('#input_sendDate').val(''); 
	 $('input[name="daterange"]').on('cancel.daterangepicker', function(ev, picker) {
	      $(this).val('');     
	  });   
	mainTable = $('#mainTable').DataTable({ 
		columns : [
			{ data : "sendDate"                        
			  	, className : 'dt-custom-td80'    
// 				, render: DataTable.render.datetime('DD/MM/YYYY', 'DD/MM/YYYY', 'en') 
			    , render: function (data, type, row) {
			      if (type === 'display' || type === 'filter') {
			          const date = moment(data, 'MMM D, YYYY', true); // Strict parsing
			          return date.isValid() ? date.format('DD/MM/YYYY') : "";
			        }
			        return data; // Return original data for sorting/other operations
			      }
				, orderData : [ 0,2,1 ]
			},                  //0
			{ data : "noPerDay" },              //1
			{ data : "replyDate"                     
			  	, className : 'dt-custom-td80'  
			    , render: function (data, type, row) {
					if (type === 'display' || type === 'filter') {
				        const date = moment(data, 'MMM D, YYYY', true); // Strict parsing
				        return date.isValid() ? date.format('DD/MM/YYYY') : "";
			        }
			        return data; // Return original data for sorting/other operations
			      }
				, orderData : [ 2,1,0 ] },              //2
			{ data : "cfmNo" },             //3  
			{ data : "customerName"                     
			  	, className : 'dt-custom-td240'  },        //4
			{ data : "so" },          //5
			{ data : "soLine"
					, type:'num-fmt' },         //6
			{ data : "dueDate"                      
// 			  		, className : 'dt-custom-td80'  
				    , render: function (data, type, row) {
						if (type === 'display' || type === 'filter') {
					        const date = moment(data, 'MMM D, YYYY', true); // Strict parsing
					        return date.isValid() ? date.format('DD/MM/YYYY') : "";
				        }
				        return data; // Return original data for sorting/other operations
				      }  
// 					, type: 'date-euro' 
			},              //7
			{ data : "po" },              //8
			{ data : "material" },       //9
			{ data : "productName"
		  		, className : 'dt-custom-td3'            
	  		},           //10
			{ data : "labNo" ,                          
			  	  className : 'dt-custom-td100'  },                   //11
			{ data : "color"  },         //12
			{ data : "prodID" }, //13 
			{ data : "lotNo" },  //14
			{ data : "dyeL"
					, type:'num-fmt'},      //15
			{ data : "dyeDa"
					, type:'num-fmt' },   	  		  //16
			{ data : "dyeDb" 
					, type:'num-fmt'},   	  //17
			{ data : "dyeSt"
					, type:'num-fmt' },      	  //18 
			{ data : "dyeDeltaE"
					, type:'num-fmt' },    		  //19
			{ data : "colorCheckL" 
					, type:'num-fmt'},        //20 
			{ data : "colorCheckDa"
					, type:'num-fmt' },           //21
			{ data : "colorCheckDb"
					, type:'num-fmt' },          //22
			{ data : "colorCheckSt" 
					, type:'num-fmt'},     //23
			{ data : "colorCheckDeltaE"
					, type:'num-fmt' },     //24
			{ data : "cfmL"
				, type:'num-fmt' },     //25
			{ data : "cfmDa"
					, type:'num-fmt' },        //26
			{ data : "cfmDb"
					, type:'num-fmt' },           //27
			{ data : "cfmSt"
					, type:'num-fmt' },          //28
			{ data : "cfmDeltaE" },     //29
			{ data : "colorCheckDate"
// 					, type: 'date-euro' 
// 					, render: DataTable.render.datetime('DD/MM/YYYY', 'DD/MM/YYYY', 'en')   
				    , render: function (data, type, row) {
						if (type === 'display' || type === 'filter') {
					        const date = moment(data, 'MMM D, YYYY', true); // Strict parsing
					        return date.isValid() ? date.format('DD/MM/YYYY') : "";
				        }
				        return data; // Return original data for sorting/other operations
				      } 
			},     //30
			{ data : "colorCheckStatus" },        //31
			{ data : "colorCheckRemark" },           //32
			{ data : "result" },          //33   
			{ data : "qcComment",                           
		  	  	className : 'dt-custom-td450' },     //34    
			{ data : "remarkFromSubmit",                          
			  	  	className : 'dt-custom-td300' },     //35
			{ data : "nextLot" },        //36
			{ data : "qty"
					, type:'num-fmt' },           //37
			{ data : "unitId" }                 //38
		],
		columnDefs : [   
		    { 
		        // Applies to ALL columns
		        targets: "_all", 
		        defaultContent: "" // Empty string if data is missing
	      	} 
// 			{   targets : [2,15,16,18,19,20],              
// 				className : 'font-bold'  
// 				},
// 			{   targets : [21],  
// 				type: 'string',                 	
// 				className : 'dt-custom-td3'
// 				},  
// 				{   targets : [25],  
// 					type: 'string',              
// 					className : 'dt-custom-td5'
// 					}
// 				,{   targets : [3],    
// 					 orderData: [3,17,13,14,19,20,21],
// 					} 
// 				,{   targets : [13],   
// 					 orderData: [13,14,17,21,19,20,10,3],
// 					} 

// 				,{   targets : [17],   
// 					 orderData: [17,21,19,20,10,3],
// 					} 
// 				,{   targets : [19],   
// 					 orderData: [19,20,21,17,13,14,3],
// 					}      
		],                    
// 		   fixedColumns: {
// 		        left: 3        
// 		    },
		scrollX: true,       
 	  	scrollY: '70vh' , //ขนาดหน้าจอแนวตั้ง       
// 		scrollY: '100vh',             	
        scrollCollapse: true,            
	    orderCellsTop : true,   
// 		pageLength:	 1000,	          
			pageLength:	 1000 ,	 
		    lengthMenu: [[100, 250, 500, 1000, 2500],[100, 250, 500, 1000, 2500]],
// 	    lengthChange : false ,  
	 	select : {      
			style: 'os',     	        
		 	selector: 'td:not(.status)'             
      	},             
     	order : [  [ 2, 'asc' ], [1, 'asc' ],[0, 'asc' ]  ],        
//      	order : [  [ 17, 'asc' ], [21, 'asc' ],[13, 'asc' ], [ 14, 'asc' ] ],        
//      	order : [  [ 22, 'asc' ], [23, 'asc' ],[2, 'asc' ], [ 12, 'asc' ], [ 0, 'asc' ],[ 1, 'asc' ] ,[ 19, 'asc' ],[ 15, 'asc' ] ],  
//      	order : [  [ 12, 'asc' ], [ 21, 'asc' ], [22, 'asc' ], [ 0, 'asc' ],[ 1, 'asc' ],[ 2, 'asc' ],[ 7, 'asc' ] ],  
		createdRow : function(row, data, index) {      
		},
		drawCallback : function(settings) {   
		},
		initComplete : function() {     
   		}
	});         
	$("#mainTable_wrapper .dataTables_scrollHead").on('keyup', '.monitor_search', function() { 
		mainTable.column($(this).data('index')).search(this.value).draw();  
	});      
 	$('#btn_search').on( 'click', function () {        
	     searchByDetail(); 
 	} );   
 	$('#btn_download').on( 'click', function () {      
		var ObjMarkup = mainTable.data().toArray(); 
		exportCSV(ObjMarkup)
	 	} );   
	$("#mainTable_filter").hide();  
	preLoaderHandler( preloader)        
});     
// function searchByDetail(){    
// 	var json = createJsonData(); 
//     var  obj = JSON.parse(json);    
// 	var arrayTmp = [];   
// 	arrayTmp.push(obj); 
// 	getReportSplitWorkDetail(arrayTmp);  
// } 

// function createJsonData(){  
// 	var prodOrder = document.getElementById("input_prodOrder").value .trim();  
// 	var lotNo = document.getElementById("lotNo").value .trim();  
// 	var dueDate = document.getElementById("input_dueDate").value .trim(); 
// 	var replyDate = document.getElementById("replyDate").value .trim();  
// 	var json = '' +
// 	   '{'+  
// 	   ' "prodID":'+JSON.stringify(prodOrder)+       
// 	   ',"lotNo":'+JSON.stringify(lotNo)+    
// 	   ',"replyDate":'+JSON.stringify(replyDate)+     
// 	   '} ';      
// 	   return json;           
// } 
// 	function getReportSplitWorkDetail(value) {
// 		$.ajax({
// 			type : "POST",
// 			dataType : "json",
// 			contentType : "application/json; charset=utf-8",
// 			url : ctx + "/Report/CFM/getReportDetail",
// 			data : JSON.stringify(value),
// 			success : function(data) {
// 				mainTable.clear();
// 				if (data.length > 0) {
// 					mainTable.rows.add(data);
// 				} else {
// 					// 					swal("Warnning !", "No Data", "error");   
// 				}
// 				mainTable.columns.adjust();
// 				mainTable.draw();
// 			},
// 			error : function(e) {
// 				swal("Fail", "Please contact to IT", "error");
// 			},
// 			done : function(e) {
// 				console.log(data);
// 			}
// 		});
// 	} 
	function searchByDetail() {    
	    const custName = document.getElementById("input_custName").value.trim(); 
	    const sO = document.getElementById("input_saleOrder").value.trim();  
	    const prodOrder = document.getElementById("input_prdOrder").value.trim();  
	    const lotNo = document.getElementById("input_lotNo").value.trim();  
	    const replyDate = document.getElementById("input_replyDate").value.trim();  
	    const sendDate = document.getElementById("input_sendDate").value.trim(); 
	 // Check if any field has a value
	    if (custName !== '' || sO !== '' || prodOrder !== '' || lotNo !== '' || replyDate !== '' || sendDate !== '') {
	        // At least one field has a value

		    const jsonData = createJsonData(); 
		    const obj = JSON.parse(jsonData);    
		    const arrayTmp = [obj]; 
		    getReportSplitWorkDetail(arrayTmp); 
	    } else {
	        // All fields are empty
	        swal("Fail", "Please fill at least one field to proceed.", "error");
// 	        console.log("Please fill at least one field to proceed.");
	    } 
	}    

	function createJsonData() {  
	    const custName = document.getElementById("input_custName").value.trim(); 
	    const sO = document.getElementById("input_saleOrder").value.trim();  
	    const prodOrder = document.getElementById("input_prdOrder").value.trim();  
	    const lotNo = document.getElementById("input_lotNo").value.trim();  
	    const replyDate = document.getElementById("input_replyDate").value.trim();  
	    const sendDate = document.getElementById("input_sendDate").value.trim(); 

	    const json = {
	    	customerName: custName,
	    	so: sO,
	        prodID: prodOrder,
	        lotNo: lotNo,
	        replyDateRange: replyDate, 
	        sendDateRange: sendDate
	    };  
	    return JSON.stringify(json);           
	} 

	function getReportSplitWorkDetail(value) { 	 
	    $.ajax({
	        type: "GET", 
	        dataType: "json",
	        contentType: "application/json; charset=utf-8",  
	        url: ctx+'/Report/CFM/getReportDetail', 
	       data :{    
		    	   data:JSON.stringify( value 	)
	       },  
	        success: function(data) { 
// 	        	console.log(data)
	            mainTable.clear();
	            if (data.length > 0) {
	                mainTable.rows.add(data);
	            }  
	            mainTable.columns.adjust();    
	            mainTable.draw();
	        },
	        error: function(e) {
	            swal("Fail", "Please contact IT", "error");
	            console.log(e);
	        },
	        complete: function() {
	            console.log("Request completed");   
// 	            console.log(e);
	        }
	    });
	}

	function exportCSV(value) { 	 
	    $.ajax({
	        type: "POST",  
	        contentType: "application/json; charset=utf-8",  
	        url: ctx+'/Report/CFM/createExcelReportDetail',
	        data: JSON.stringify(value),  
	        xhrFields: {
	            responseType: 'blob'  // Handle the response as a binary blob
	        },
	        success: function(blob, status, xhr) {
	            // Extract filename from the response header
	            const disposition = xhr.getResponseHeader('Content-Disposition');
	            let filename = "CFM_Report.xlsx";

	            if (disposition && disposition.indexOf('attachment') !== -1) {
	                const filenameMatch = disposition.match(/filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/);
	                if (filenameMatch != null && filenameMatch[1]) {
	                    filename = filenameMatch[1].replace(/['"]/g, '');
	                }
	            }

	            // Create a link and trigger the download
	            const url = window.URL.createObjectURL(blob);
	            const link = document.createElement('a');
	            link.href = url;
	            link.download = filename;
	            document.body.appendChild(link);
	            link.click();

	            // Cleanup
	            window.URL.revokeObjectURL(url);
	            link.remove();
	        },
	        error: function(e) {
	            swal("Fail", "Please contact IT", "error");
	            console.log(e);
	        },
	        complete: function() {
	            console.log("Request completed");    
	        }
	    });
	}
// 	function exportCSV(data) {
// 		var createXLSLFormatObj = [];
// 		/* XLS Head Columns */
// 		var xlsHeader = [ "po", "poLine", "productionOrder", "lotNo", "batch", "materialNo", "customerMaterial", "articleFG", "designFG", "customerName", "quantityMR", "quantityKG", "userStatus", "labStatus", "planGreigeDate", "griegeInDate", "dueDate", "sorDueDate", "groupNo", "subGroup", "planDyeDate", "ppmmStatus", "saleOrder", "saleLine", "bookNo", "labNo", "shade", "planningRemark" ]
// 		/* XLS Rows Data */
// 		var xlsRows = data
// 		createXLSLFormatObj.push(xlsHeader);
// 		$.each(xlsRows, function(index, value) {
// 			var innerRowData = [];
// 			$.each(value, function(data, val) {
// 				if (data == 'userStatusList' || data == 'labStatusList') {

// 				} else if (data == 'quantityMR' || data == 'quantityKG') {
// 					if (val == '') {
// 						innerRowData.push('');
// 					} else {
// 						innerRowData.push(parseFloat(val.replace(/,/g, '')));
// 					}
// 					// 				}  
// 				} else if (data == 'planGreigeDate' || data == 'griegeInDate' || data == 'dueDate' || data == 'sorDueDate' || data == 'planDyeDate') {
// 					if (val == '') {
// 						innerRowData.push('');
// 					} else {
// 						innerRowData.push(stringToDate(val));
// 					}
// 				} else {
// 					innerRowData.push(val);
// 				}
// 			});
// 			createXLSLFormatObj.push(innerRowData);
// 		});
// 		/* File Name */
// 		var filename = "CFMReport.xlsx";
// 		/* Sheet Name */
// 		var ws_name = "CFMReport";
// 		if (typeof console !== 'undefined')
// 			console.log(new Date());
// 		var wb = XLSX.utils.book_new(), ws = XLSX.utils.aoa_to_sheet(createXLSLFormatObj);

// 		/* Add worksheet to workbook */
// 		XLSX.utils.book_append_sheet(wb, ws, ws_name);
// 		/* Write workbook and Download */
// 		if (typeof console !== 'undefined')
// 			console.log(new Date());
// 		XLSX.writeFile(wb, filename);
// 		if (typeof console !== 'undefined')
// 			console.log(new Date()); 
// 	}

// 	function exportCSV(data) {
// 	    var createXLSLFormatObj = [];    
	    
// 	    // XLS Head Columns based on your DataTable column definitions
// 	    var xlsHeader = [
// 	        "sendDate", "noPerDay", "replyDate", "cFMNo", "customerName", 
// 	        "sO", "sOLine", "dueDate", "pO", "material", 
// 	        "productName", "labNo", "color", "prodID", "lotNo", 
// 	        "dye_L", "dye_Da", "dye_DB", "dye_St", "dye_DeltaE", 
// 	        "colorCheck_L", "colorCheck_Da", "colorCheck_DB", "colorCheck_St", 
// 	        "colorCheck_DeltaE", "cFM_L", "cFM_Da", "cFM_DB", "cFM_St", 
// 	        "cFM_DeltaE", "colorCheckDate", "colorCheckStatus", "colorCheckRemark", 
// 	        "result", "qCComment", "remarkFromSubmit", "nextLot", "qty", "unitId"
// 	    ];
// 	 // Convert to uppercase    
// // 	    var upperCaseHeader = toUpperCaseArray(xlsHeader);
// 	 // Convert to capitalize first letter
// 	    var capitalizedHeader = xlsHeader.map(function(header) {
// 	        return capitalizeFirstLetter(header);
// 	    });
// 	    // Add header to the XLS format object
// 	    createXLSLFormatObj.push(capitalizedHeader);
	       
// 	    // Process each row of data
// 	    $.each(data, function(index, value) {
// 	        var innerRowData = [];
	        
// 	        // Extract values based on the header
// 	        xlsHeader.forEach(function(header) {
// 	            var val = value[header] || ''; // Get the value or default to empty string
// 	            // Convert date strings into Date objects for Excel to recognize datetime type
// 	            if (header === 'sendDate' || header === 'replyDate' || header === 'dueDate') {
// 	            	 var dateObj = parseDateStringToDate(val, true); // มีเวลาส่ง true
// 	            	    innerRowData.push(dateObj ? dateObj : '');    
// 	            } else if (header === 'colorCheckDate') {
// 	            	var dateObj = parseDateStringToDate(val, false); // ไม่มีเวลาส่ง false
// 	                innerRowData.push(dateObj ? dateObj : '');
// 	            }  
// 	            else if (header === 'qty' || header === 'dye_L' || header === 'dye_Da' || header === 'dye_DB' || 
//                     header === 'dye_St' || header === 'dye_DeltaE' || header === 'colorCheck_L' || header === 'colorCheck_Da' || 
//                     header === 'colorCheck_DB' || header === 'colorCheck_St' || header === 'colorCheck_DeltaE' || header === 'cFM_L' || 
//                     header === 'cFM_Da' || header === 'cFM_DB' || header === 'cFM_DeltaE'  || header === 'cFM_St' || header === 'qty') {
// 		             // attempt to parse to number, fallback empty string
// 		             var numVal = val === '' ? '' : Number(val.toString().replace(/,/g, ''));
// 		             innerRowData.push(isNaN(numVal) ? '' : numVal);
// 		         } else {
// 	                innerRowData.push(val);
// 	            }
// 	        });
	         
// 	        // Add the processed row to the XLS format object
// 	        createXLSLFormatObj.push(innerRowData);
// 	    });
	    
// 	 // File Name and Sheet Name
// 	    var filename = "CFMReport.xlsx";
// 	    var ws_name = "CFMReport";

// 	    var ws = XLSX.utils.aoa_to_sheet(createXLSLFormatObj);
// 	    // 🟢 **ตำแหน่งที่ 2: หลังจากสร้าง Worksheet** 🟢

// 	    // Define border style
// 	    const borderStyle = {
// 	        top: { style: "thin" },
// 	        bottom: { style: "thin" },
// 	        left: { style: "thin" },
// 	        right: { style: "thin" },
// 	    };

// 	    // Define header style
// 	    const headerStyle = {
// 	        font: { bold: true, color: { rgb: "FFFFFF" } },
// 	        fill: { fgColor: { rgb: "4F81BD" } },
// 	        border: borderStyle,
// 	    };

// 	    // Define alternate row colors
// 	    const evenRowStyle = {
// 	        fill: { fgColor: { rgb: "D9EAD3" } },
// 	        border: borderStyle,
// 	    };

// 	    const oddRowStyle = {
// 	        fill: { fgColor: { rgb: "FFFFFF" } },
// 	        border: borderStyle,
// 	    };

// 	    // Apply Header Style
// 	    capitalizedHeader.forEach(function(_, colIdx) {
// 	        var cellRef = XLSX.utils.encode_cell({ r: 0, c: colIdx });
// 	        if (ws[cellRef]) {
// 	            ws[cellRef].s = headerStyle;
// 	        }
// 	    });

// 	    // Apply Row Styles
// 	    data.forEach(function(_, rowIdx) {
// 	        var isEven = (rowIdx + 1) % 2 === 0;
// 	        var rowStyle = isEven ? evenRowStyle : oddRowStyle;

// 	        xlsHeader.forEach(function(_, colIdx) {
// 	            var cellRef = XLSX.utils.encode_cell({ r: rowIdx + 1, c: colIdx });
// 	            if (ws[cellRef]) {
// 	                ws[cellRef].s = rowStyle;
// 	            }
// 	        });
// 	    });
  
// 	    // Create a new workbook and add the worksheet
// 		var dateColsIndices = [];
// 		xlsHeader.forEach(function(colName, idx) {
// 		    if (colName === 'sendDate' || colName === 'replyDate' || colName === 'dueDate' || colName === 'colorCheckDate') {
// 		        dateColsIndices.push(idx);
// 		    }
// 		});
		
// 		// ตั้งค่ารูปแบบวันที่เป็น Text
// 		dateColsIndices.forEach(function(C) {
// 		    var isColorCheckDate = (xlsHeader[C] === 'colorCheckDate');
// 		    var format = isColorCheckDate ? 'dd/MM/yyyy' : 'dd/MM/yyyy HH:mm:ss';
		    
// 		    for (var R = 1; R <= data.length; ++R) {
// 		        var cell_ref = XLSX.utils.encode_cell({ r: R, c: C });
// 		        var cell = ws[cell_ref];
		        
// 		        if (!cell) continue;
		
// 		        // แปลงเป็น Date Object
// 		        var dateObj = parseDateStringToDate(cell.v, !isColorCheckDate);
// 		        if (dateObj) {
// 		            // แปลงเป็น String ในรูปแบบ dd/MM/yyyy หรือ dd/MM/yyyy HH:mm:ss
// 		            var day = String(dateObj.getDate()).padStart(2, '0');
// 		            var month = String(dateObj.getMonth() + 1).padStart(2, '0');
// 		            var year = dateObj.getFullYear();
// 		            var hours = String(dateObj.getHours()).padStart(2, '0');
// 		            var minutes = String(dateObj.getMinutes()).padStart(2, '0');
// 		            var seconds = String(dateObj.getSeconds()).padStart(2, '0');
		
// 		            var formattedDate = isColorCheckDate 
// 		                ? `${day}/${month}/${year}`
// 		                : `${day}/${month}/${year} ${hours}:${minutes}:${seconds}`;
		
// 		            // แปลงเป็น Text เพื่อป้องกันการแปลงโดย Excel
// 		            cell.v = formattedDate;
// 		            cell.t = 's'; // Set type to string
// 		        }
// 		    }
// 		});

// 	    var wb = XLSX.utils.book_new(); 
// 	    XLSX.utils.book_append_sheet(wb, ws, ws_name);
	    
// 	    // Write workbook and download
// 	    XLSX.writeFile(wb, filename);
// 	}     
	// Capitalize First Letter  
	function capitalizeFirstLetter(str) {
	    return str.toLowerCase().split('_').map(word => word.charAt(0).toUpperCase() + word.slice(1)).join('_');
	}

// Parse a date string in 'dd/MM/yyyy HH:mm:ss' or 'm/d/yyyy HH:mm:ss' format to a JS Date object
function parseDateStringToDate(dateString, hasTime) {
    if (!dateString || typeof dateString !== 'string') return null;

    try {
        var dateObj = null;

        // ตรวจสอบรูปแบบ dd/MM/yyyy HH:mm:ss
        var dateRegex1 = /^(\d{2})\/(\d{2})\/(\d{4})(?:\s+(\d{2}):(\d{2}):(\d{2}))?$/;
        var match1 = dateString.match(dateRegex1);

        if (match1) {
            var day = parseInt(match1[1], 10);
            var month = parseInt(match1[2], 10) - 1; // เดือนใน JavaScript เป็น 0-based
            var year = parseInt(match1[3], 10);

            var hours = hasTime && match1[4] ? parseInt(match1[4], 10) : 0;
            var minutes = hasTime && match1[5] ? parseInt(match1[5], 10) : 0;
            var seconds = hasTime && match1[6] ? parseInt(match1[6], 10) : 0;

            dateObj = new Date(year, month, day, hours, minutes, seconds);
        }

        // ถ้าไม่ตรงกับรูปแบบ dd/MM/yyyy ให้ลองรูปแบบ m/d/yyyy HH:mm:ss
        if (!dateObj || isNaN(dateObj.getTime())) {
            var dateRegex2 = /^(\d{1,2})\/(\d{1,2})\/(\d{4})(?:\s+(\d{1,2}):(\d{2}):(\d{2})\s*(AM|PM)?)?$/i;
            var match2 = dateString.match(dateRegex2);

            if (match2) {
                var month = parseInt(match2[1], 10) - 1;
                var day = parseInt(match2[2], 10);
                var year = parseInt(match2[3], 10);

                var hours = hasTime && match2[4] ? parseInt(match2[4], 10) : 0;
                var minutes = hasTime && match2[5] ? parseInt(match2[5], 10) : 0;
                var seconds = hasTime && match2[6] ? parseInt(match2[6], 10) : 0;

                // ตรวจสอบ AM/PM
                var period = match2[7] ? match2[7].toUpperCase() : '';
                if (period === 'PM' && hours < 12) {
                    hours += 12;
                } else if (period === 'AM' && hours === 12) {
                    hours = 0;
                }

                dateObj = new Date(year, month, day, hours, minutes, seconds);
            }
        }

        // ตรวจสอบความถูกต้องของวันที่
        if (dateObj && !isNaN(dateObj.getTime())) {
            return dateObj;
        }

        return null;
    } catch (e) {
        console.error("Error parsing date:", e);
        return null;
    }
}

	function onlyNumbers(str) {
		return /^[0-9]+$/.test(str);
	}
	// Function to convert string to date (you may need to adjust this based on your date format)
	function stringToDate(dateString) {
	    // Example: Convert 'DD/MM/YYYY' to a Date object
	    var parts = dateString.split('/');
	    return new Date(parts[2], parts[1] - 1, parts[0]); // Year, Month (0-based), Day
	}
</script>
</html>