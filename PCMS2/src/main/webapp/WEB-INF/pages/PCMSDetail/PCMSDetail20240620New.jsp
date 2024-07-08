<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/pages/config/meta.jsp"></jsp:include>
<title>PCMS - Detail</title>
<jsp:include page="/WEB-INF/pages/config/css/baseCSS.jsp"></jsp:include>
<link href="<c:url value="/resources/css/style_overide.css" />" rel="stylesheet" type="text/css">
<link href="<c:url value="/resources/css/datatable.overide.css" />" rel="stylesheet" type="text/css">
</head>
<body>
	<jsp:include page="/WEB-INF/pages/config/navbar.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/pages/config/loading.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/pages/config/searchDiv.jsp"></jsp:include>
	<div id="wrapper-center" class="row" style="margin: 0 5px;">
		<div class="col-12 col-sm-12 col-md-12 col-lg-12 col-xl-12 " style="font-size: 12.5px; padding: 0px; margin: 0px 0px;">
			<div class="table-responsive ">
				<table id="MainTable" class="table compact table-bordered table-striped text-center" style="font width: 100%; margin: 0px !important; zoom: 95%;">
					<thead>
						<tr>
							<th class="row-table" style="vertical-align: middle;">DIV</th>
							<th class="row-table" style="vertical-align: middle;">SO No.</th>
							<th class="row-table" style="vertical-align: middle;">SO Line</th>
							<th class="row-table" style="vertical-align: middle;">Cust.(Name4)</th>
							<th class="row-table" style="vertical-align: middle;">SO Date</th>
							<th class="row-table" style="vertical-align: middle;">P/O</th>
							<th class="row-table" style="vertical-align: middle;">Material</th>
							<th class="row-table" style="vertical-align: middle;">Cust.(Base)<span class="c" style="display: block;"> Mat.No.</span>
							</th>
							<th class="row-table" style="vertical-align: middle;">Cust.<span class="c" style="display: block;"> Mat.No.</span>
							</th>
							<th class="row-table" style="vertical-align: middle;">Price <span class="c" style="display: block;">(THB)</span></th>
							<th class="row-table" style="vertical-align: middle;">Unit</th>
							<th class="row-table" style="vertical-align: middle;">Order<span class="c" style="display: block;">Qty.</span>
							</th>
							<th class="row-table" style="vertical-align: middle;">Remain<span class="c" style="display: block;">Qty.</span>
							</th>
							<th class="row-table" style="vertical-align: middle;">Remain<span class="c" style="display: block;">Amt.(THB)</span>
							</th>
							<th class="row-table" style="vertical-align: middle;">จำนวนต่อ LOT</th>
							<th class="row-table" style="vertical-align: middle;">Volume FG Amt</th>
							<th class="row-table" style="vertical-align: middle;">Grade</th>
							<th class="row-table" style="vertical-align: middle;">GR Qty</th>

							<th class="row-table" style="vertical-align: middle;">จำนวน (FG) <span class="c" style="display: block;">KG/MR/YD</span>
							</th>
							<th class="row-table" style="vertical-align: middle;">จำนวนที่ส่ง</th>
							<th class="row-table" style="vertical-align: middle;">Amt.<span class="c" style="display: block;">(THB)</span>
							</th>
							<th class="row-table" style="vertical-align: middle;">Due Cus.</th>
							<th class="row-table" style="vertical-align: middle;">Due Date</th>
							<th class="row-table" style="vertical-align: middle;">Lab No</th>
							<th class="row-table" style="vertical-align: middle;">Lab Status</th>
							<th class="row-table" style="vertical-align: middle;">Lot</th>
							<th class="row-table" style="vertical-align: middle;">DyePlan</th>
							<th class="row-table" style="vertical-align: middle;">DyeActual</th>
							<th class="row-table" style="vertical-align: middle;">Dye<span class="c" style="display: block;">Status</span></th>
							<th class="row-table" style="vertical-align: middle;">วันนัด<span class="c" style="display: block;">CFM LAB</span>
							</th>
							<th class="row-table" style="vertical-align: middle;">วันส่ง<span class="c" style="display: block;"> CFM LAB</span>
							</th>
							<th class="row-table" style="vertical-align: middle;">วันที่ลูกค้า <span class="c" style="display: block;"> ตอบ LAB</span>
							</th>
							<th class="row-table" style="vertical-align: middle;">TK CFM</th>
							<th class="row-table" style="vertical-align: middle;">วันที่นัด CFM</th>
							<th class="row-table" style="vertical-align: middle;">Send CFM <span class="c" style="display: block;">Cus. Date</span>
							</th>

							<th class="row-table" style="vertical-align: middle;">วันที่ส่ง CFM จริง</th>
							<th class="row-table" style="vertical-align: middle;">User Status</th>
							<th class="row-table" style="vertical-align: middle;">CFM Detail</th>
							<th class="row-table" style="vertical-align: middle;">CFM Number</th>
							<th class="row-table" style="vertical-align: middle;">CFM Remark</th>
							<th class="row-table" style="vertical-align: middle;">Delivery<span class="c" style="display: block;">(วันที่นัดส่ง)</span>
							</th>
							<th class="row-table" style="vertical-align: middle;">Bill Date</th>
							<th class="row-table" style="vertical-align: middle;">EFFECT</th>
							<th class="row-table" style="vertical-align: middle;">Cause <span class="c" style="display: block;">of Delay</span>
							</th>
							<th class="row-table" style="vertical-align: middle;">Delayed <span class="c" style="display: block;">Department</span>
							</th>
							<th class="row-table" style="vertical-align: middle;">PC Remark</th>
							<th class="row-table" style="vertical-align: middle;">Replaced Remark</th>
							<th class="row-table" style="vertical-align: middle;">SwitchRemark</th>
							<th class="row-table" style="vertical-align: middle;">StockRemark</th>
							<th class="row-table" style="vertical-align: middle;">StockLoad</th>
						</tr>
					</thead>
					<tbody>
					</tbody>
					<tfoot>
					</tfoot>
				</table>
			</div>
		</div>
	</div>
	<jsp:include page="/WEB-INF/pages/config/footer.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/pages/config/PCMSDetailModal/ColumnSetting/ColumnSetting.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/pages/config/PCMSDetailModal/InputDateDetail/modalMain.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/pages/config/PCMSDetailModal/LockColumnDetail/LockColumnDetail.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/pages/config/PCMSDetailModal/RemarkSWModal/modalMain.jsp"></jsp:include>
</body>
<script src="<c:url value="/resources/js/DatatableSort.js" />"></script>
<script src="<c:url value="/resources/js/General.js" />"></script>
<style>
.p-r-15 {
	padding-right: 15px !important;
}
/* tfoot .dtfc-fixed-left{   */
/*     background-color: white;    */
/* }      */
</style>
<script>       	   
var userId = '' ; 
var preloader = document.getElementById('loader');    
var today = new Date();        //modalForm  
var dd = String('0' + today.getDate()).slice(-2); 
var mm = String('0' + (today.getMonth() + 1)).slice(-2); ; //January is 0!
var yyyy = today.getFullYear();
var startDate = dd+'/'+mm+'/'+yyyy; 
var MainTable ;  
var mapsDataHeader  = new Map(); 
var mapsTitleHeader  = new Map();    
var mapsColumnHeader  = new Map(); 
var check1 = 0	;
var check2 = 0	; 
var check3 = 0	;
var checkReplaced = 0;
var saleNumberList ; 
var userStatusList ; 
var cusNameList ;  	
var cusShortNameList ;  
var selectOptionDepText ; 
var depList ; 
var divisionList ; 
var colList ; 
var configCusList;
var soTmp ;   
var soLineTmp;
var soTmpExcel ;   
var soLineTmpExcel;     
var caseDupli = 0 ;
var InputDateTable ;
var SWMainTable ;
var collapsedGroups = {};        
var columnsHeader  = [];
var domain = "http://"+window.location.hostname+":8080"; 
$(document).on('keypress', 'input,select', function(e) { 
	if (e.which === 13) {   
		if (e.target.id == 'input_customer') { searchByDetail();    }   
		if (e.target.id == 'input_customerShortName') { searchByDetail();    }    
		if (e.target.id == 'input_saleOrder') { searchByDetail();    }      
		if (e.target.id == 'input_article') { searchByDetail();    }  
		if (e.target.id == 'input_prdOrder') { searchByDetail();    }  
		if (e.target.id == 'input_saleNumber') { searchByDetail();    }  
		if (e.target.id == 'input_designNo') { searchByDetail();    }  
		if (e.target.id == 'input_material') { searchByDetail();    }  
		if (e.target.id == 'input_labNo') { searchByDetail();    }  
		if (e.target.id == 'input_PO') { searchByDetail();    }  
// 		if (e.target.id == 'input_userStatus') { searchByDetail();    }     
	}  
});     
  
$('#input_prdOrderDate').daterangepicker({
    opens: 'right',             
    locale: {
        format: 'DD/MM/YYYY',
        cancelLabel: 'Clear'        
      },
      drops : "auto",     
//       autoUpdateInput: false,             
// 	  autoApply: true,       
// 	  autoUpdateInput: false,       
  }
  , function(start, end, label) { 
//   	var json = '{"StartDate":"'+start.format('DD/MM/YYYY')+'", "EndDate":"'+end.format('DD/MM/YYYY')+'"}'; 
//     var  obj = JSON.parse(json);    
// 	var arrayTmp = [];           
// 	arrayTmp.push(obj);       
//     createGroupBoxByDateRequiredLab(arrayTmp)
  }); 
$('#input_saleOrderDate').daterangepicker({
	   opens: 'right', 	
	    locale: {
	        format: 'DD/MM/YYYY',   
    	    cancelLabel: 'Clear'
	      },       
		  drops : "auto",        
// 		  autoUpdateInput: false,        
// 	 	  autoApply: true,                 
	  } 
, function(start, end, label) {   
});       
// 	function showThing() {
//   		$( "#loading").css("display","block");
// 	  	setTimeout(removeThing, 11000)  	
// 	}
//   	function removeThing() {
//   		$("#loading" ).css("display","none");       
//   	}
     $('#input_dueDate').daterangepicker({
	   opens: 'right', 	
	    locale: {
	        format: 'DD/MM/YYYY',   
 	    cancelLabel: 'Clear'    
	      },       
		  drops : "auto",        
//		  autoUpdateInput: false,        
//	 	  autoApply: true,                 
	  } 
, function(start, end, label) {   
});      
 $(document).on('show.bs.modal', '.modal', function (event) {
    	    var zIndex = 1040 + (10 * $('.modal:visible').length);
    	    $(this).css('z-index', zIndex);
    	    setTimeout(function() {
    	        $('.modal-backdrop').not('.modal-stack').css('z-index', zIndex - 1).addClass('modal-stack');
    	    }, 0);
  	});  
$(document) .ready( function() {         
// 	showThing();       
// 	document.getElementById("div_toOtherPath").style.display = "none"; 
// document.getElementById("btn_lockColumn").style.display = "none";
// EMERGENCY

	let os = JSON.parse('${OS}');   
	let result = os.includes("win");	
	let domain ='';
	if(result === true){ domain = "http://"+window.location.hostname+":8080"; }
	else{ domain = "https://"+window.location.hostname;  } 
	configCusList = JSON.parse('${ConfigCusList}'); 
	if(configCusList.length > 0 ){
		window.location.replace(domain+"/PCMS2/login");
	}
	 
	userId = JSON.parse('${UserID}');   ;     
// 	console.log(document.getElementById("btn_prdDetail"))   
	if(document.getElementById("btn_prdDetail") != null){
		document.getElementById("btn_prdDetail").style.display = "none";        
	} 
	if(document.getElementById("btn_lbms") != null){ 
		document.getElementById("btn_lbms").style.display = "none"; 
	} 
	if(document.getElementById("btn_qcms") != null){ 
		document.getElementById("btn_qcms").style.display = "none";  
	} 
	if(document.getElementById("btn_inspect") != null){  
		document.getElementById("btn_inspect").style.display = "none"; 
	} 
	if(document.getElementById("btn_sfc") != null){  
		document.getElementById("btn_sfc").style.display = "none";    
	} 
	$('#input_saleOrderDate').val('');    
	$('#input_prdOrderDate').val('');       
	$('#input_dueDate').val('');
	<%-- 	var saleNumberList = '<%=request.getAttribute("SaleNumberList")%>'; --%>    
	 $('input[name="daterange"]').on('cancel.daterangepicker', function(ev, picker) {
	      $(this).val('');     
	  });
	$(document).ajaxStart(function() {$( "#loading").css("display","block"); });   
	$(document).ajaxStop(function() {$("#loading" ).css("display","none"); });    
//   	var StartDate = $("#input_requestDate").data('daterangepicker').startDate.format('DD/MM/YYYY');
// 	 var EndDate = $("#input_requestDate").data('daterangepicker').endDate.format('DD/MM/YYYY');  
       	
	$('#MainTable thead tr').clone(true).appendTo('#MainTable thead');
	$('#MainTable thead tr:eq(1) th') .each( function(i) {        
		var title = $(this).text();      	      
		$(this).html( '<input type="text" class="monitor_search" style="width:100%" data-index="' + i + '" '  
// 				+'placeholder="Search '+title+'" '    
				+ '/>');   	     
	});             
	    MainTable = $('#MainTable').DataTable({   
			scrollX: true,                         
	        scrollY: '55vh' , //ขนาดหน้าจอแนวตั้ง         
	        scrollCollapse: true,    
	 	   	orderCellsTop : true,           
			select : {             
				style: 'os',          
			 	selector: 'td:not(.status)'  // .status is class        
	  		},         
	  		paging: true,
			pageLength:	 100,	          
// 		    lengthChange : false,   
// 		    "paging": true,
			colReorder: {            
			   realtime: false,   
			   enable: false
			},             
// 			deferRender: true, // run again in column
		    lengthMenu: [[100, 250, 500, 1000, 2500],[100, 250, 500, 1000, 2500]],
	 	   	columns :                    
	 	   		[      
				    {"data" : "division" ,        "title":"DIV"          },                              //0
				    {"data" : "saleOrder" ,       "title":"SO No.",
				          orderData: [ 1, 2,25 ]        ,       
						render: function (data, type, row) {	     
							let html = '<div name="n_'+row.saleOrder+' data-toggle="tooltip" title="' + row.typePrd + '"> '+row.saleOrder+'</div>'
							return  html; 
					   	  }         },                          //1
				    {"data" : "saleLine"   ,      "title":"SO Line" ,            
						render: function (data, type, row) {	     
							let html = '<div name="n_'+row.saleLine+' data-toggle="tooltip"  title="' + row.typePrd + '"> '+row.saleLine+'</div>'
							return  html; 
					   	  }             },                          //2
				    {"data" : "customerShortName","title":"Cust.(Name4)" ,      	     
						  	  	className : 'dt-custom-td140',      
						  	  	type: 'string'    },                              //3
				    {"data" : "saleCreateDate" ,  "title":"SO Date"   ,            
					  	  className : 'dt-custom-td80',    	           
					  	  type: 'date-euro'  },           //4 
				    {"data" : "purchaseOrder" ,   "title":"P/O" ,                    
				  	  	className : 'dt-custom-td160',      
				  	  	type: 'string'   },     								      //5 
				    {"data" : "materialNo" ,      "title":"Material" ,      	     
					  	  	className : 'dt-custom-td140',      
					  	  	type: 'string'   },     	   				          //6
				    {"data" : "customerMaterialBase" , "title":"Cust.Mat.Base",                    
				  	  	className : 'dt-custom-td240',       
				  	  	type: 'string'    },                        //7
				    {"data" : "customerMaterial" ,"title":"Cust.Mat.No.",                    
					  	  	className : 'dt-custom-td240',       
					  	  	type: 'string'   },                              //8
				    {"data" : "price" ,           "title":"Price (THB)",'type': 'num' },                 //9
				    {"data" : "saleUnit" ,        "title":"Unit" },                                      //10
				    {"data" : "saleQuantity" ,    "title":"Order Qty.",'type': 'num' },                  //11
				    {"data" : "remainQuantity" ,  "title":"Remain Qty." ,'type': 'num',                   
					   	  render: function (data, type, row) {	               
								var htmlEx = '';               
//		 	   					console.log(" omg "+caseDupli+"  "+data+" row.saleLine "+row.lotNo+" "+row.grade+" "+row.saleLine+" "+row.saleOrder+" soLineTmp "+soLineTmp+" soTmp "+soTmp)
					   			if(soLineTmp == '' && soTmp == ''  ){ 
					   				soLineTmp = row.saleLine;     
						   			soTmp = row.saleOrder;        
						   			caseDupli = 0; 
						   			htmlEx = '<div style="visibility: visible;color: red; font-weight: bolder;">'+data+' </div>'; 
						   		}  
					   			else if(soLineTmp == row.saleLine && soTmp == row.saleOrder  ){
					   				caseDupli = 1;
						   			htmlEx = '<div style="visibility: hidden;color: red; font-weight: bolder;">'+
//		 				   			row.remainQuantity
						   			data+' </div>'; 
						   		}   
						   		else{       
						   			soLineTmp = row.saleLine;     
						   			soTmp = row.saleOrder;       
						   			caseDupli = 2;
						   			htmlEx = '<div style="visibility: visible;color: red; font-weight: bolder;">'+data+' </div>'; 
						   		}      
//		 	   					console.log(htmlEx)
								return  htmlEx
						   	  }   },                 //12
				    {"data" : "remainAmount" ,    "title":"Remain Amt.(THB)",'type': 'num',        
					   	  render: function (data, type, row) {	   
			   					var htmlEx = '';   
				   				if(caseDupli == 0  ){ 
						   			htmlEx = '<div style="visibility: visible;color: red; font-weight: bolder;">'+row.remainAmount+' </div>'; 
						   		}  
					   			else if(caseDupli == 1  ){ 
						   			htmlEx = '<div style="visibility: hidden;color: red; font-weight: bolder;">'+row.remainAmount+' </div>'; 
						   		}   
						   		else{          
						   			htmlEx = '<div style="visibility: visible;color: red; font-weight: bolder;">'+row.remainAmount+' </div>'; 
						   		}  
								return  htmlEx
						   	  }     },            //13
				    {"data" : "volumn" ,          "title":"Volume FG",'type': 'num' },                   //14
				    {"data" : "volumnFGAmount" ,  "title":"Volume FG Amt(THB)",'type': 'num' },          //15
				    {"data" : "grade" ,           "title":"Grade" },                                     //16
				    {"data" : "grQuantity" ,      "title":"GR Qty<br>KG/MR/YD" ,                        
					  	  className : 'dt-custom-td160',    
					  	  type: 'string'    } ,                       //17
				    {"data" : "billSendWeightQuantity" ,"title":"Bill Qty (Class)<br>KG/MR/YD" ,                        
						  	  className : 'dt-custom-td160',    
						  	  type: 'string'    },        //18
				    {"data" : "billSendQuantity" ,"title":"Bill Qty" ,'type': 'num'},                    //19
				    {"data" : "orderAmount" ,     "title":"Amt.(THB)",'type': 'num' ,        
					   	  render: function (data, type, row) {	               
						   		var htmlEx = '';  
				   				if(caseDupli == 0  ){ 
						   			htmlEx = '<div style="visibility: visible;color: red; font-weight: bolder;">'+row.orderAmount+' </div>'; 
						   		}  
					   			else if(caseDupli == 1  ){ 
						   			htmlEx = '<div style="visibility: hidden;color: red; font-weight: bolder;">'+row.orderAmount+' </div>'; 
						   		}      
						   		else{          
						   			htmlEx = '<div style="visibility: visible;color: red; font-weight: bolder;">'+row.orderAmount+' </div>'; 
						   		}   
								return  htmlEx
						   	  }    },                   //20
				    {"data" : "customerDue" ,     "title":"Due Cus.",            
					  	  className : 'dt-custom-td80',    	        
					  	  type: 'date-euro'  },              //21
				    {"data" : "dueDate" ,         "title":"Due Date",            
					  	  className : 'dt-custom-td80',    	        
					  	  type: 'date-euro'  },              //22  
				    {"data" : "labNo",			  "title":"Lab No",                        
						  	  className : 'dt-custom-td100', type: 'string'      },                                    //23
				    {"data" : "labStatus",		  "title":"Lab Status"       	 ,    
				  	  	className : 'dt-custom-td140',      
				  	  	type: 'string'   },                                //24
				    {"data" : "lotNo",            "title":"Lot"    ,                        
					  	  className : 'dt-custom-td100', type: 'string'           ,             
						render: function (data, type, row) {	     
							let html = '<div name="n_'+row.lotNo+' data-toggle="tooltip" title="' + row.typePrd + '"> '+row.lotNo+'</div>'
							return  html; 
					   	  }     },                                       //25 
				    {"data" : "dyePlan","title":"Dye [Plan]" ,'type': 'date-euro',//,             
						  render: function (data, type, row) {	 
						   		var htmlEx = data;                                      
			   					htmlEx = ''          
			   					+ '<div data-search="' + data + '" '          
			   					+ ' name="DateInput" type="text" '
			   					+ ' value = "' + data   + "' "                 
			   					+ ' autocomplete="off" >'       
			   					+ dateDDMMYYYToDDMM(data) 
			   					+ '</div>';       
						   		return  htmlEx     ;         
							}        
				    },                      //26 
				    {"data" : "dyeActual","title":"Dye [Actual]",'type': 'date-euro' },                  //27 
				    {"data" : "dyeStatus","title":"Dye Status" },                                        //28  
				    {"data" : "cfmPlanLabDate","title":"Plan CFM LAB",
				    	  'type': 'date-euro',    
				  		  orderable: false,                   
					  	  className : 'CFMPlanLabDateParent dt-custom-td80',              
// 						  render: function (data, type, row) {	 
// 						   		var htmlEx = data;                                       
// 			   					htmlEx = ''      
// 			   					+ '<div data-search="' + data + '" '          
// //		 	   					+ ' class="form-control DateInput" '    
// 			   					+ ' name="DateInput" type="text" '
// 			   					+ ' value = "' + data   + "' "                 
// 			   					+ ' autocomplete="off" >'    
// 			   					+ dateDDMMYYYToDDMM(data)     
// 			   					+ '</div>';       
// 						   		return  htmlEx     ;         
// 							}       
					   	  render: function (data, type, row) {
				   			var htmlEx = ''     
				   				if(row.lotNo == "รอจัด Lot"   || row.lotNo  == "ขาย stock" ||row.lotNo == "รับจ้างถัก"	||row.lotNo == "Lot ขายแล้ว"
			   					|| row.lotNo == "พ่วงแล้วรอสวม"	|| row.lotNo == "รอสวมเคยมี Lot"){ 
									htmlEx = ''; 
								}
							else{
								htmlEx = '<input class="form-control CFMPlanLabDateInput" style=" cursor: pointer; padding: 4px 2px;font-size: 12.5px"  name="CFMPlanLabDate" type="text"  value = "' + row.cfmPlanLabDate+ '" autocomplete="off" >';
							}
							return  htmlEx;
					   	  }             
				  	  },             //29
				    {"data" : "cfmActualLabDate","title":"Send CFM LAB" ,'type': 'date-euro'},     	     //30 
				    {"data" : "cfmCusAnsLabDate","title":"Answer LAB",'type': 'date-euro'},              //31
				    {"data" : "tkCFM","title":"TK CFM"},                       						     //32 
				    {"data" : "cfmPlanDate","title":"Plan CFM Date" ,                      
					  	  className : 'CFMPlanDateParent dt-custom-td80',       
						  orderable: false,   
				  	      type: 'date-euro' },                //33  
				    {"data" : "sendCFMCusDate","title":"Send CFM Cus Date",'type': 'date-euro',                      
					  	  className : 'SendCFMCusDateParent dt-custom-td80',    
						  orderable: false,
		   	  			  render: function (data, type, row) {	
		 				   		var htmlEx = ''  
					   			if(row.lotNo == "รอจัด Lot"   || row.lotNo  == "ขาย stock" ||row.lotNo == "รับจ้างถัก"	||row.lotNo == "Lot ขายแล้ว"
			   					|| row.lotNo == "พ่วงแล้วรอสวม"	|| row.lotNo == "รอสวมเคยมี Lot"){ 
									htmlEx = row.sendCFMCusDate ; 
								}
								else{
									htmlEx = '<input class="form-control SendCFMCusDateInput" style=" cursor: pointer; padding: 4px 2px;font-size: 12.5px"  name="SendCFMCusDate" type="text"  value = "' 
									+ row.sendCFMCusDate
									+ '" autocomplete="off" >';
								} 
								return  htmlEx
					   	  }   },         //34 <------------- ss="form-control SendCFMCusDateInput" style=" cursor: pointer; padding: 4px 2px;font-size: 12.5px"  name="SendCFMCusDate" type="text"  value = "' 
						
 				    {"data" : "cfmDateActual","title":"Actual CFM Date",'type': 'date-euro'},            //35 
				    {"data" : "userStatus","title":"User Status",      	     
 				  	  	className : 'dt-custom-td140',      
 				  	  	type: 'string'   },                                       //36
					{"data" : "cfmDetailAll","title":"CFM Detail",                          
				  	  	className : 'dt-custom-td300',         
				  	  	type: 'string'   },                                      //37   
					{"data" : "cfmNumberAll","title":"CFM Number"},                                      //38
					{"data" : "rollNoRemarkAll","title":"CFM Remark",                          
				  	  	className : 'dt-custom-td300',         
				  	  	type: 'string'   },                                //39<------------- 09/08/2022
				    {"data" : "deliveryDate","title":"Plan Delivery Date",                     
					  	  className : 'DeliveryDateParent dt-custom-td100',       
					  	  type: 'date-euro'  ,    
						  orderable: false,     
					   	  render: function (data, type, row) {	     
							var htmlEx = ''   
					   		if(row.lotNo  == "รอจัด Lot"	 || row.lotNo  == "ขาย stock" ||row.lotNo == "รับจ้างถัก"	||row.lotNo == "Lot ขายแล้ว"
					   			|| row.lotNo == "พ่วงแล้วรอสวม"	|| row.lotNo == "รอสวมเคยมี Lot"){ 
								htmlEx = ''; 
							}
							else{   
								htmlEx = '<input class="form-control DeliveryDateInput" style=" cursor: pointer; padding: 4px 2px;font-size: 12.5px"  name="DeliveryDate" type="text"  value = "' + row.deliveryDate+ '" autocomplete="off" >';
							}
							return  htmlEx           
							}	},       //40
				    {"data" : "lotShipping","title":"Bill Date" ,            
							  	  className : 'dt-custom-td80',    	        
							  	  type: 'date-euro'  },                 //41 Replaced ShipDate {"data" : "ShipDate","title":"Bill Date",'type': 'date-euro'},                    //40 
				    {"data" : "remark","title":"EFFECT"  , 
				  	  	className : 'dt-custom-td450 p-r-15',      
				  	  	type: 'string'  ,     },                                             //42
				    {"data" : "causeOfDelay","title":"Cause of Delay",                       
				  	  	className : 'dt-custom-td450 p-r-15',      
				  	  	type: 'string'  ,     
					   	  render: function (data, type, row) {	 
						   		var htmlEx = ''      
					   			if( row.lotNo  == "รอจัด Lot" || row.lotNo == "ขาย stock" ||row.lotNo == "รับจ้างถัก" ||
					   					row.lotNo == "Lot ขายแล้ว"	|| row.lotNo  == ""        || row.lotNo == "พ่วงแล้วรอสวม"	|| 
					   					row.lotNo == "รอสวมเคยมี Lot" ){ 
					   				htmlEx = row.causeOfDelay; 
								}         
								else if(     
				   					( row.typePrd == "Replaced" && row.typePrdRemark == "MAIN")  ||  
				   					( row.typePrd == "MAIN" && row.typePrdRemark == "MAIN") || 
				   						row.typePrdRemark == "SUB" || 
				   						row.typePrdRemark == "" ||
			   						  	row.typePrd == "OrderPuang"){ 
				   					htmlEx = '<input data-search="' + row.causeOfDelay+ '" class="form-control CauseOfDelayInput" style=" cursor: pointer; padding: 4px 2px;font-size: 12.5px"maxlength="200"  name="CauseOfDelayRemark" type="text"  value = "' 
				   					+ row.causeOfDelay+ '" autocomplete="off" >'; 
								}      
								else{   
									htmlEx = '<input data-search="' + row.causeOfDelay
									+ '" class="form-control CauseOfDelayInput" style=" cursor: pointer; padding: 4px 2px;font-size: 12.5px"maxlength="200"  name="CauseOfDelayRemark" type="text"  value = "' 
									+ row.causeOfDelay+ '" autocomplete="off" >'; 
								}
//		 						htmlEx = '<input class="form-control CauseOfDelayInput" style=" cursor: pointer; padding: 4px 2px;font-size: 12.5px"maxlength="200"  name="CauseOfDelayRemark" type="text"  value = "' + row.CauseOfDelay+ '" autocomplete="off" >'; 
								return  htmlEx      
							}         
				    },                               //43<-------------
				    {"data" : "delayedDepartment","title":"Delayed Department",                    
				  	  	className : 'dt-custom-td240',       
				  	  	type: 'string'   ,       
					   	  render: function (data, type, row, meta) {	    
						   		var htmlEx = ''         
							   		 
					   			if( row.lotNo  == "รอจัด Lot" || row.lotNo == "ขาย stock" ||row.lotNo == "รับจ้างถัก" ||
					   					row.lotNo == "Lot ขายแล้ว"	|| row.lotNo  == ""        || row.lotNo == "พ่วงแล้วรอสวม"	|| 
					   					row.lotNo == "รอสวมเคยมี Lot" ){  
					   				htmlEx = row.delayedDepartment; 
								}   
					   			else if(    
				   					( row.typePrd == "Replaced" && row.typePrdRemark == "MAIN")  || 
				   					( row.typePrd == "MAIN" && row.typePrdRemark == "MAIN"|| 
			  							row.typePrdRemark == "SUB" || row.typePrdRemark == "") ||
			  							row.typePrd == "OrderPuang"){ 
									 htmlEx = '<select data-search="' + row.delayedDepartment+ 
											 '" class="form-control DelayedDepInput" data-col="' + meta.col + '">' + 
											 getSelectOptions(data) + '</select>';    
								} 
					   			                    
								else{   
									htmlEx = '<select data-search="' + row.delayedDepartment+ 
											 '" class="form-control DelayedDepInput" data-col="' + meta.col + '">' + 
											 getSelectOptions(data) + '</select>';     
								}  
						   		return  htmlEx      
								}  
				    },                      //44<-------------
					{"data" : "pcRemark","title":"PCRemark",                   
				  	  	className : 'dt-custom-td450 p-r-15',      
				  	  	type: 'string'  ,     
					   	  render: function (data, type, row) {	     
						   		var htmlEx = ''    
								htmlEx = '<input class="form-control PCRemarkInput" style=" cursor: pointer; padding: 4px 2px;font-size: 12.5px"maxlength="200"  name="PCRemark" type="text"  value = "' + row.pcRemark+ '" autocomplete="off" >'; 
								return  htmlEx      
								}    },                                         //45
				    {"data" : "replacedRemark","title":"ReplacedRemark",                       
							  	  	className : 'dt-custom-td450 p-r-15',      
							  	  	type: 'string'  ,     
					   	  render: function (data, type, row) {	     
						   		var htmlEx = '';
						   		if( ( row.typePrd == "Replaced" && row.typePrdRemark == "MAIN")  || ( row.typePrd == "MAIN" && row.typePrdRemark == "MAIN") ){  
									htmlEx = '<input class="form-control ReplacedRemarkInput" style=" cursor: pointer; padding: 4px 2px;font-size: 12.5px"maxlength="200"  name="ReplacedRemark" type="text"  value = "' + row.replacedRemark+ '" autocomplete="off" >'; 
								}         
						   		else if(row.lotNo  == "รอจัด Lot"	  || row.lotNo == "ขาย stock" ||row.lotNo == "รับจ้างถัก"	||row.lotNo == "Lot ขายแล้ว"	|| row.lotNo  == ""|| row.lotNo == "พ่วงแล้วรอสวม"	|| row.lotNo == "รอสวมเคยมี Lot" ){ 
						   			htmlEx = '<input class="form-control ReplacedRemarkInput" style=" cursor: pointer; padding: 4px 2px;font-size: 12.5px"maxlength="200"  name="ReplacedRemark" type="text"  value = "' + row.replacedRemark+ '" autocomplete="off" >'; 
								} 
								else if(row.typePrd == "Switch" || row.typePrd == "Replaced" || row.typePrd == "OrderPuang" || row.typePrdRemark == "SUB" || row.typePrdRemark == ""){ 
									htmlEx = ''; 
								}         
								else{   
									htmlEx = '<input class="form-control ReplacedRemarkInput" style=" cursor: pointer; padding: 4px 2px;font-size: 12.5px"maxlength="200"  name="ReplacedRemark" type="text"  value = "' + row.replacedRemark+ '" autocomplete="off" >'; 
								}
						   		return  htmlEx          
								}    
					},                             //46
				    {"data" : "switchRemark","title":"SwitchRemark",         
				  	  	type: 'string'  ,         
				   	  	render: function (data, type, row) {	          
							var htmlEx = ''           
							if( ( row.typePrd == "Switch" && row.typePrdRemark == "MAIN")  || ( row.typePrd == "MAIN" && row.typePrdRemark == "MAIN") ){  
								htmlEx = '<input class="form-control SwitchRemarkInput" style=" cursor: pointer; padding: 4px 2px;font-size: 12.5px"maxlength="200"  name="StockRemark" type="text"  value = "' + row.switchRemark+ '" autocomplete="off" >'; 
							}        
							else if(row.typePrd == "Replaced" || row.typePrd == "OrderPuang"
									||row.lotNo == "รอจัด Lot"	 || row.lotNo == "ขาย stock" ||row.lotNo == "รับจ้างถัก"	||row.lotNo == "Lot ขายแล้ว" || row.lotNo == "พ่วงแล้วรอสวม"	|| row.lotNo == "รอสวมเคยมี Lot"	
									|| row.typePrdRemark == "SUB" || row.typePrdRemark == ""){ 
								htmlEx = ''; 
							}     
							else{   
								htmlEx = '<input class="form-control SwitchRemarkInput" style=" cursor: pointer; padding: 4px 2px;font-size: 12.5px"maxlength="200"  name="StockRemark" type="text"  value = "' + row.switchRemark+ '" autocomplete="off" >'; 
							}
							return  htmlEx      
							}    },                                 //47
				    {"data" : "stockRemark","title":"StockRemark",                 
						  	  	className : 'dt-custom-td450 p-r-15',      
						  	  	type: 'string'  ,       
				   	  	render: function (data, type, row) {	     
							var htmlEx = ''    
							htmlEx = '<input class="form-control StockRemarkInput" style=" cursor: pointer; padding: 4px 2px;font-size: 12.5px"maxlength="200"  name="StockRemark" type="text"  value = "' + row.stockRemark+ '" autocomplete="off" >'; 
							return  htmlEx      
							}      },                                   //48
				    {"data" : "stockLoad","title":"StockLoad",                       
						  	  	className : 'dt-custom-td450 p-r-15',      
						  	  	type: 'string'  ,     
				   	  	render: function (data, type, row) {	     
							var htmlEx = ''       
							htmlEx = '<input class="form-control StockLoadInput" style=" cursor: pointer; padding: 4px 2px;font-size: 12.5px"maxlength="200"  name="StockLoadInput" type="text"  value = "' + row.stockLoad+ '" autocomplete="off" >'; 
							return  htmlEx      
							}       
						} ,                                                  //49
     
			],        	                
            columnDefs :  [	
// 			    { targets: [ 1 ],
// 		          orderData: [ 1, 2,25 ]  
// 		        },   	    
		        
// 			 	{  			     
// 				  targets : [26,29],             
// 				  render: function (data, type, row) {	 
// 				   		var htmlEx = data;                                      
// 	   					htmlEx = ''      
// 	   					+ '<div data-search="' + data + '" '         
// // 	   					+ ' class="form-control DateInput" '    
// 	   					+ ' name="DateInput" type="text" '
// 	   					+ ' value = "' + data   + "' "                 
// 	   					+ ' autocomplete="off" >'   
// 	   					+ dateDDMMYYYToDDMM(data) 
// 	   					+ '</div>';       
// 				   		return  htmlEx     ;         
// 					}                          
// 				} ,     
// 				{ targets : [ 4,21 , 22,30 ,41 ],                        
// 				  	  className : 'dt-custom-td80',    	        
// 				  	  type: 'date-euro'  
// 					} ,                              
// 				{ targets : [ 12,13,14,15,23,25,31 ,35 ],                        
// 				  	  className : 'dt-custom-td100', type: 'string'     
// 				} ,   
// 				{ targets : [ 17,18 ],                        
// 				  	  className : 'dt-custom-td160',    
// 				  	  type: 'string'     
// 				} ,                 
// 				{ targets : [ 23 ],                            
// 				  	  className : 'dt-custom-td120',    
// 				  	  type: 'string'            
// 				} ,     
// 				{ targets : [ 29  ],                      
// 			  	  className : 'CFMPlanLabDateParent dt-custom-td80',       
// 			  	  type: 'date-euro'     
// 				} ,   
// 					{ targets : [ 33 ],                      
// 				  	  className : 'CFMPlanDateParent dt-custom-td80',   
// 					  orderable: false,   
// 			  	      type: 'date-euro'  
// 				} ,  
// 				{ targets : [ 34 ],                       
// 				  	  className : 'SendCFMCusDateParent dt-custom-td80',       
// 				  	  type: 'date-euro'  
// 					} ,
// 				{ targets : [ 40 ],                      
// 			  	  className : 'DeliveryDateParent dt-custom-td100',       
// 			  	  type: 'date-euro'  
// 				} ,   
// 				{ targets : [3 ,6,24,36,38 ],             	     
// 			  	  	className : 'dt-custom-td140',      
// 			  	  	type: 'string'   
// 					} ,               
// 				{ targets : [5  ],                    
// 			  	  	className : 'dt-custom-td160',      
// 			  	  	type: 'string'   
// 					} ,            
// 				{ targets : [ 7,8 ,44 ],                    
// 			  	  	className : 'dt-custom-td240',       
// 			  	  	type: 'string'   
// 				} ,       
// 				{ targets : [  37,39 ],                          
// 			  	  	className : 'dt-custom-td300',         
// 			  	  	type: 'string'     
// 					} ,            
// 				{ targets : [  42,43,45,46 ,48,49],                       
// 			  	  	className : 'dt-custom-td450 p-r-15',      
// 			  	  	type: 'string'  
// 					} ,   
// 					{ targets:[1]  ,       
// 						render: function (data, type, row) {	     
// 							let html = '<div name="n_'+row.saleOrder+' data-toggle="tooltip" title="' + row.typePrd + '"> '+row.saleOrder+'</div>'
// 							return  html; 
// 					   	  }    
// 					},  
// 					{ targets:[2]  ,            
// 						render: function (data, type, row) {	     
// 							let html = '<div name="n_'+row.saleLine+' data-toggle="tooltip"  title="' + row.typePrd + '"> '+row.saleLine+'</div>'
// 							return  html; 
// 					   	  }    
// 					},  
// 				{ targets : [ 12 ],                   
// 			   	  render: function (data, type, row) {	               
// 						var htmlEx = '';               
// // 	   					console.log(" omg "+caseDupli+"  "+data+" row.saleLine "+row.lotNo+" "+row.grade+" "+row.saleLine+" "+row.saleOrder+" soLineTmp "+soLineTmp+" soTmp "+soTmp)
// 			   			if(soLineTmp == '' && soTmp == ''  ){ 
// 			   				soLineTmp = row.saleLine;     
// 				   			soTmp = row.saleOrder;        
// 				   			caseDupli = 0; 
// 				   			htmlEx = '<div style="visibility: visible;color: red; font-weight: bolder;">'+data+' </div>'; 
// 				   		}  
// 			   			else if(soLineTmp == row.saleLine && soTmp == row.saleOrder  ){
// 			   				caseDupli = 1;
// 				   			htmlEx = '<div style="visibility: hidden;color: red; font-weight: bolder;">'+
// // 				   			row.remainQuantity
// 				   			data+' </div>'; 
// 				   		}   
// 				   		else{       
// 				   			soLineTmp = row.saleLine;     
// 				   			soTmp = row.saleOrder;       
// 				   			caseDupli = 2;
// 				   			htmlEx = '<div style="visibility: visible;color: red; font-weight: bolder;">'+data+' </div>'; 
// 				   		}      
// // 	   					console.log(htmlEx)
// 						return  htmlEx
// 				   	  }     
// 						} ,       
// 				{ targets : [ 13 ],        
// 			   	  render: function (data, type, row) {	   
// 	   					var htmlEx = '';   
// 		   				if(caseDupli == 0  ){ 
// 				   			htmlEx = '<div style="visibility: visible;color: red; font-weight: bolder;">'+row.remainAmount+' </div>'; 
// 				   		}  
// 			   			else if(caseDupli == 1  ){ 
// 				   			htmlEx = '<div style="visibility: hidden;color: red; font-weight: bolder;">'+row.remainAmount+' </div>'; 
// 				   		}   
// 				   		else{          
// 				   			htmlEx = '<div style="visibility: visible;color: red; font-weight: bolder;">'+row.remainAmount+' </div>'; 
// 				   		}  
// 						return  htmlEx
// 				   	  }    
// 					} ,  
// 				{ targets : [ 20 ],        
// 				   	  render: function (data, type, row) {	               
// 				   		var htmlEx = '';  
// 		   				if(caseDupli == 0  ){ 
// 				   			htmlEx = '<div style="visibility: visible;color: red; font-weight: bolder;">'+row.orderAmount+' </div>'; 
// 				   		}  
// 			   			else if(caseDupli == 1  ){ 
// 				   			htmlEx = '<div style="visibility: hidden;color: red; font-weight: bolder;">'+row.orderAmount+' </div>'; 
// 				   		}      
// 				   		else{          
// 				   			htmlEx = '<div style="visibility: visible;color: red; font-weight: bolder;">'+row.orderAmount+' </div>'; 
// 				   		}   
// 						return  htmlEx
// 				   	  }    
// 					} , 
					         	
// 					{ targets:[25]  ,       
// 						render: function (data, type, row) {	     
// 							let html = '<div name="n_'+row.lotNo+' data-toggle="tooltip" title="' + row.typePrd + '"> '+row.lotNo+'</div>'
// 							return  html; 
// 					   	  }    
// 					},  
// 				{ targets : [ 29 ],    
// 		  		  orderable: false,    
// 			   	  render: function (data, type, row) {
// 		   			var htmlEx = ''     
// 		   				if(row.lotNo == "รอจัด Lot"   || row.lotNo  == "ขาย stock" ||row.lotNo == "รับจ้างถัก"	||row.lotNo == "Lot ขายแล้ว"
// 	   					|| row.lotNo == "พ่วงแล้วรอสวม"	|| row.lotNo == "รอสวมเคยมี Lot"){ 
// 							htmlEx = ''; 
// 						}
// 					else{
// 						htmlEx = '<input class="form-control CFMPlanLabDateInput" style=" cursor: pointer; padding: 4px 2px;font-size: 12.5px"  name="CFMPlanLabDate" type="text"  value = "' + row.cfmPlanLabDate+ '" autocomplete="off" >';
// 					}
// 					return  htmlEx;
// 			   	  }            
// 				} ,
// 				{ targets : [ 33 ],    
// 				  orderable: false,
// 				   	  render: function (data, type, row) {	
// // 				   		var htmlEx = '' 
// // 				   			if(row.lotNo  == "รอจัด Lot"	 || row.lotNo  == "ขาย stock"	){ 
// // 								htmlEx = ''; 
// // 							}
// // 						else{
// // 							htmlEx = '<input class="form-control CFMPlanDateInput" style=" cursor: pointer; padding: 4px 2px;font-size: 12.5px"  name="CFMPlanDate" type="text"  value = "' + row.CFMPlanDate+ '" autocomplete="off" >';
// // 						} 
// 						return  row.cfmPlanDate 
// 				   	  }         
// 					} ,    
// 				{ targets : [ 34 ],             
// 				  orderable: false,
//    	  			  render: function (data, type, row) {	
//  				   		var htmlEx = ''  
// 			   			if(row.lotNo == "รอจัด Lot"   || row.lotNo  == "ขาย stock" ||row.lotNo == "รับจ้างถัก"	||row.lotNo == "Lot ขายแล้ว"
// 	   					|| row.lotNo == "พ่วงแล้วรอสวม"	|| row.lotNo == "รอสวมเคยมี Lot"){ 
// 							htmlEx = row.sendCFMCusDate ; 
// 						}
// 						else{
// 							htmlEx = '<input class="form-control SendCFMCusDateInput" style=" cursor: pointer; padding: 4px 2px;font-size: 12.5px"  name="SendCFMCusDate" type="text"  value = "' 
// 							+ row.sendCFMCusDate
// 							+ '" autocomplete="off" >';
// 						} 
// 						return  htmlEx
// 			   	  }    
// 				} ,        
// 				{ targets : [ 40 ],    
// 				  orderable: false,     
// 			   	  render: function (data, type, row) {	     
// 					var htmlEx = ''   
// 			   		if(row.lotNo  == "รอจัด Lot"	 || row.lotNo  == "ขาย stock" ||row.lotNo == "รับจ้างถัก"	||row.lotNo == "Lot ขายแล้ว"
// 			   			|| row.lotNo == "พ่วงแล้วรอสวม"	|| row.lotNo == "รอสวมเคยมี Lot"){ 
// 						htmlEx = ''; 
// 					}
// 					else{   
// 						htmlEx = '<input class="form-control DeliveryDateInput" style=" cursor: pointer; padding: 4px 2px;font-size: 12.5px"  name="DeliveryDate" type="text"  value = "' + row.deliveryDate+ '" autocomplete="off" >';
// 					}
// 					return  htmlEx           
// 					}	          
// 				},      
// 				{ targets : [ 43 ],     
// 			   	  render: function (data, type, row) {	 
// 				   		var htmlEx = ''      
// 			   			if( row.lotNo  == "รอจัด Lot" || row.lotNo == "ขาย stock" ||row.lotNo == "รับจ้างถัก" ||
// 			   					row.lotNo == "Lot ขายแล้ว"	|| row.lotNo  == ""        || row.lotNo == "พ่วงแล้วรอสวม"	|| 
// 			   					row.lotNo == "รอสวมเคยมี Lot" ){ 
// 			   				htmlEx = row.causeOfDelay; 
// 						}         
// 						else if(     
// 		   					( row.typePrd == "Replaced" && row.typePrdRemark == "MAIN")  ||  
// 		   					( row.typePrd == "MAIN" && row.typePrdRemark == "MAIN") || 
// 		   						row.typePrdRemark == "SUB" || 
// 		   						row.typePrdRemark == "" ||
// 	   						  	row.typePrd == "OrderPuang"){ 
// 		   					htmlEx = '<input data-search="' + row.causeOfDelay+ '" class="form-control CauseOfDelayInput" style=" cursor: pointer; padding: 4px 2px;font-size: 12.5px"maxlength="200"  name="CauseOfDelayRemark" type="text"  value = "' 
// 		   					+ row.causeOfDelay+ '" autocomplete="off" >'; 
// 						}      
// 						else{   
// 							htmlEx = '<input data-search="' + row.causeOfDelay
// 							+ '" class="form-control CauseOfDelayInput" style=" cursor: pointer; padding: 4px 2px;font-size: 12.5px"maxlength="200"  name="CauseOfDelayRemark" type="text"  value = "' 
// 							+ row.causeOfDelay+ '" autocomplete="off" >'; 
// 						}
// // 						htmlEx = '<input class="form-control CauseOfDelayInput" style=" cursor: pointer; padding: 4px 2px;font-size: 12.5px"maxlength="200"  name="CauseOfDelayRemark" type="text"  value = "' + row.CauseOfDelay+ '" autocomplete="off" >'; 
// 						return  htmlEx      
// 					}         
// 				}  ,   
// 				{ targets : [ 44 ],       
// 			   	  render: function (data, type, row, meta) {	    
// 			   		var htmlEx = ''         
				   		 
// 		   			if( row.lotNo  == "รอจัด Lot" || row.lotNo == "ขาย stock" ||row.lotNo == "รับจ้างถัก" ||
// 		   					row.lotNo == "Lot ขายแล้ว"	|| row.lotNo  == ""        || row.lotNo == "พ่วงแล้วรอสวม"	|| 
// 		   					row.lotNo == "รอสวมเคยมี Lot" ){  
// 		   				htmlEx = row.delayedDepartment; 
// 					}   
// 		   			else if(    
// 	   					( row.typePrd == "Replaced" && row.typePrdRemark == "MAIN")  || 
// 	   					( row.typePrd == "MAIN" && row.typePrdRemark == "MAIN"|| 
//   							row.typePrdRemark == "SUB" || row.typePrdRemark == "") ||
//   							row.typePrd == "OrderPuang"){ 
// 						 htmlEx = '<select data-search="' + row.delayedDepartment+ 
// 								 '" class="form-control DelayedDepInput" data-col="' + meta.col + '">' + 
// 								 getSelectOptions(data) + '</select>';    
// 					} 
		   			                    
// 					else{   
// 						htmlEx = '<select data-search="' + row.delayedDepartment+ 
// 								 '" class="form-control DelayedDepInput" data-col="' + meta.col + '">' + 
// 								 getSelectOptions(data) + '</select>';     
// 					}  
// 			   		return  htmlEx      
// 					}       
// 				}  ,      
// 				{ targets : [ 45 ],     
// 				   	  render: function (data, type, row) {	     
// 				   		var htmlEx = ''    
// 						htmlEx = '<input class="form-control PCRemarkInput" style=" cursor: pointer; padding: 4px 2px;font-size: 12.5px"maxlength="200"  name="PCRemark" type="text"  value = "' + row.pcRemark+ '" autocomplete="off" >'; 
// 						return  htmlEx      
// 						}       
// 					}       
// 				,         
// 				{ targets : [ 46 ],     
// 			   	  render: function (data, type, row) {	     
// 			   		var htmlEx = '';
// 			   		if( ( row.typePrd == "Replaced" && row.typePrdRemark == "MAIN")  || ( row.typePrd == "MAIN" && row.typePrdRemark == "MAIN") ){  
// 						htmlEx = '<input class="form-control ReplacedRemarkInput" style=" cursor: pointer; padding: 4px 2px;font-size: 12.5px"maxlength="200"  name="ReplacedRemark" type="text"  value = "' + row.replacedRemark+ '" autocomplete="off" >'; 
// 					}         
// 			   		else if(row.lotNo  == "รอจัด Lot"	  || row.lotNo == "ขาย stock" ||row.lotNo == "รับจ้างถัก"	||row.lotNo == "Lot ขายแล้ว"	|| row.lotNo  == ""|| row.lotNo == "พ่วงแล้วรอสวม"	|| row.lotNo == "รอสวมเคยมี Lot" ){ 
// 			   			htmlEx = '<input class="form-control ReplacedRemarkInput" style=" cursor: pointer; padding: 4px 2px;font-size: 12.5px"maxlength="200"  name="ReplacedRemark" type="text"  value = "' + row.replacedRemark+ '" autocomplete="off" >'; 
// 					} 
// 					else if(row.typePrd == "Switch" || row.typePrd == "Replaced" || row.typePrd == "OrderPuang" || row.typePrdRemark == "SUB" || row.typePrdRemark == ""){ 
// 						htmlEx = ''; 
// 					}         
// 					else{   
// 						htmlEx = '<input class="form-control ReplacedRemarkInput" style=" cursor: pointer; padding: 4px 2px;font-size: 12.5px"maxlength="200"  name="ReplacedRemark" type="text"  value = "' + row.replacedRemark+ '" autocomplete="off" >'; 
// 					}
// 			   		return  htmlEx          
// 					}       
// 				} ,     
// 				{ targets : [ 47 ],         
// 			   	  	render: function (data, type, row) {	          
// 					var htmlEx = ''           
// 					if( ( row.typePrd == "Switch" && row.typePrdRemark == "MAIN")  || ( row.typePrd == "MAIN" && row.typePrdRemark == "MAIN") ){  
// 						htmlEx = '<input class="form-control SwitchRemarkInput" style=" cursor: pointer; padding: 4px 2px;font-size: 12.5px"maxlength="200"  name="StockRemark" type="text"  value = "' + row.switchRemark+ '" autocomplete="off" >'; 
// 					}        
// 					else if(row.typePrd == "Replaced" || row.typePrd == "OrderPuang"
// 							||row.lotNo == "รอจัด Lot"	 || row.lotNo == "ขาย stock" ||row.lotNo == "รับจ้างถัก"	||row.lotNo == "Lot ขายแล้ว" || row.lotNo == "พ่วงแล้วรอสวม"	|| row.lotNo == "รอสวมเคยมี Lot"	
// 							|| row.typePrdRemark == "SUB" || row.typePrdRemark == ""){ 
// 						htmlEx = ''; 
// 					}     
// 					else{   
// 						htmlEx = '<input class="form-control SwitchRemarkInput" style=" cursor: pointer; padding: 4px 2px;font-size: 12.5px"maxlength="200"  name="StockRemark" type="text"  value = "' + row.switchRemark+ '" autocomplete="off" >'; 
// 					}
// 					return  htmlEx      
// 					}       
// 				} ,    
// 				{ targets : [ 48 ],     
// 			   	  	render: function (data, type, row) {	     
// 					var htmlEx = ''    
// 					htmlEx = '<input class="form-control StockRemarkInput" style=" cursor: pointer; padding: 4px 2px;font-size: 12.5px"maxlength="200"  name="StockRemark" type="text"  value = "' + row.stockRemark+ '" autocomplete="off" >'; 
// 					return  htmlEx      
// 					}                
// 				} ,        
// 				{ targets : [ 49 ],     
// 			   	  	render: function (data, type, row) {	     
// 						var htmlEx = ''       
// 						htmlEx = '<input class="form-control StockLoadInput" style=" cursor: pointer; padding: 4px 2px;font-size: 12.5px"maxlength="200"  name="StockLoadInput" type="text"  value = "' + row.stockLoad+ '" autocomplete="off" >'; 
// 						return  htmlEx      
// 						}       
// 					} ,          
			], 
			createdRow : function(row, data, index) {
    // 				$('td', row).eq(22).addClass('bg-color-azure');  
	//   	        $('td', row).eq(27).addClass('bg-color-azure');        
	//   	        $('td', row).eq(31).addClass('bg-color-azure'); 
	     		
// 				if (data["TypePrd"] == "OrderPuang" ) { $(row).addClass('bg-color-azure'); } 
	// 			if (index == 16 || index == 21 || index == 22 ) { $(row).addClass('bg-color-azure'); } 
	// 			else if (data["OperationStartDate"] != "") { $(row).addClass('bg-start-im'); }  
				if(mapsDataHeader.size != 0){      
					if (data["typePrd"] == "OrderPuang" ) { 	
						$('td', row).eq(mapsDataHeader.get("saleOrder")).addClass('bg-orderpuang');
						$('td', row).eq(mapsDataHeader.get("saleLine")).addClass('bg-orderpuang');
						$('td', row).eq(mapsDataHeader.get("lotNo")).addClass('bg-orderpuang');
					}  
					else if (data["typePrd"] == "Switch" ) { 	
						$('td', row).eq(mapsDataHeader.get("saleOrder")).addClass('bg-switch');      
						$('td', row).eq(mapsDataHeader.get("saleLine")).addClass('bg-switch');
						$('td', row).eq(mapsDataHeader.get("lotNo")).addClass('bg-switch');     
					}    
					else if (data["typePrd"] == "Replaced" ) { 	
						$('td', row).eq(mapsDataHeader.get("saleOrder")).addClass('bg-replaced');
						$('td', row).eq(mapsDataHeader.get("saleLine")).addClass('bg-replaced');
						$('td', row).eq(mapsDataHeader.get("lotNo")).addClass('bg-replaced');
					}
					$('td', row).eq(mapsDataHeader.get("dyePlan")).addClass('bg-color-azure'); 
				} 
// 				if(mapsDataHeader.size != 0){ $('td', row).eq(mapsDataHeader.get("DyePlan")).addClass('bg-color-azure');      }
			},     
			drawCallback: function( settings ){  },   
			initComplete: function () { }  
	 	 });      	
 	// Filter event handler
    $( MainTable.table().container() ).on( 'keyup', 'tfoot input', function () {
    	let searchVal = this.value;         
		soLineTmp = '';            
		soTmp = '';      
		let indexAfterReCol =  MainTable.colReorder.transpose( $(this).data('index') );
		if(searchVal  == ' '){                         
			MainTable.column(indexAfterReCol).search( '^$', true, false ).draw();
		}      
		else{     
			MainTable.column(indexAfterReCol).search(searchVal).draw();  
		}      	
    } );
	// SEARCH BY FILTER UNDER COLUMN NAME : BOAT         
	$(".dataTables_scrollHead").on('keyup', '.monitor_search', function() {                  
// 		MainTable.column($(this).data('index')).search(this.value).draw(); 
		let searchVal = this.value;      
		soLineTmp = '';            
		soTmp = '';      
		let indexAfterReCol =  MainTable.colReorder.transpose( $(this).data('index') );
		let regrex = '';   
		let splitText = searchVal.split('/');
		let size = splitText.length;
		if(searchVal  == ' '){                        
// 			MainTable.column($(this).data('index')).search( '^$', true, false ).draw();
			MainTable.column(indexAfterReCol).search( '^$', true, false ).draw();
		}     
		else if(indexAfterReCol == 26 || indexAfterReCol == 27 ){
			if(size == 1){ regrex = '^'+searchVal+'';  } 
			else if(size == 2){
				if(splitText[0] == ''){ regrex = ''+searchVal+'$';  }
				else if(splitText[1] == ''){ regrex = '^'+searchVal+'';  }
				else{ regrex = '^'+searchVal+'$';  } 
			}    
			else{regrex = ''+searchVal+''; }
			MainTable.column(indexAfterReCol).search( regrex, true, false ).draw();   
		}   
		else if(indexAfterReCol == 4 ||  indexAfterReCol == 30 || indexAfterReCol == 31 || indexAfterReCol == 32 ||
				indexAfterReCol == 34 || indexAfterReCol == 35 || indexAfterReCol == 41 ){
			if(size == 1){ regrex = '^'+searchVal+'';  }
			else if(size == 2){
				if(splitText[0] == ''){ regrex = ''+searchVal+'';  }
				else if(splitText[1] == ''){ regrex = '^'+searchVal+'';  }
				else{ regrex = '^'+searchVal+'';  } 
			}       
			else{regrex = ''+searchVal+''; }
			MainTable.column(indexAfterReCol).search( regrex, true, false ).draw();  
		}
		else{     
			MainTable.column(indexAfterReCol).search(searchVal).draw();  
		}      	
	});   
	SWMainTable = $('#SWMainTable').DataTable({  
    	scrollY: '100px',            
    	scrollX: true,           
    	paging: false,      
//  	    select : true,               
//  	 	scrollCollapse: true,            
//  	   	orderCellsTop : true,
// 		orderClasses : false,     	
		lengthChange: false,         	  
		columns : 	
 	   		[   {"data" : "typePrd"  },               
			    {"data" : "productionOrder"  },     
			    {"data" : "switchRemark"     },         
		],        	         
		columnDefs :  [   
			{ targets : [0,1, 2 ], type: 'string'      
			} ,         
			{ targets : [ 2 ],     
			   	  render: function (data, type, row) {	     
			   		var htmlEx = '';
			   		if(row.typePrd == "MAIN"  ){  
			   			htmlEx = '<input class="form-control SwitchRemarkInput" style=" cursor: pointer; padding: 4px 2px;font-size: 12.5px"maxlength="200"  name="SwitchRemark" type="text"  value = "' + row.switchRemark+ '" autocomplete="off" >'; 
			   			}
					else{   
						 htmlEx = '';
					}
			   		return  htmlEx      
				}       
			} ,     
		],        
		ordering: false,
		 createdRow : function(row, data, index) {      },   
 	 });  
    InputDateTable = $('#InputDateTable').DataTable({  
    	scrollY:       '400px',        
    	scrollX: true,          
    	paging: false,      
//  	    select : true,               
//  	 	scrollCollapse: true,            
//  	   	orderCellsTop : true,
// 		orderClasses : false,     	
		lengthChange: false,         	  
		columns : 
 	   		[   {"data" : "createDate"  },               
			    {"data" : "planDate"  },     
			    {"data" : "createBy"     },      
			    {"data" : "createDate"},   
			    {"data" : "inputFrom"},     
			    {"data" : "lotNo"},     
		],        	         
		columnDefs :  [   
			{ targets : [0,1, 3 ],                    
// 			  	  className : 'dt-custom-td80',    
			  	  type: 'date-euro'  
				} ,                      
			{ targets : [2,4 ],                       
// 			  	  className : 'dt-custom-td100',    
			  	  type: 'string'     
				} ,     
		],        
		ordering: false,
		 createdRow : function(row, data, index) {     
// 			 $(row).addClass("data-custom-padding0505");
		 },   
 	 });  
    
    InputDateTable.on( 'order.dt search.dt', function () {
    	InputDateTable.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
            cell.innerHTML = i+1;
        } );          
    } ).draw();           
 
// 	 var presetTable ;var dyeingTable;var fnTable;var inspectTable;var packingTable;var sendTestQCTable;
	$('#multi_userStatus').selectpicker();     
	$('#multi_colVis').selectpicker();   
	$('#multi_cusName').selectpicker();     
	$('#multi_cusShortName').selectpicker();   
	$('#multi_division').selectpicker();   
 	colList = JSON.parse('${ColList}');  
	cusNameList = JSON.parse('${CusNameList}');   	
	cusShortNameList = JSON.parse('${CusShortNameList}'); ;   
	userStatusList = JSON.parse('${UserStatusList}');       
	columnsHeader = MainTable.settings().init().columns;  
 	saleNumberList = JSON.parse('${SaleNumberList}');  
 	divisionList = JSON.parse('${DivisionList}');       
 	depList = JSON.parse('${DepList}');   
//  	console.log(depList)  
	selectOptionDepText = configDepSelectOption(depList) 
// 	console.log(selectOptionDepText)
 	addSelectOption(saleNumberList) ;   
	addUserStatusOption(userStatusList );        
	addCusNameOption(cusNameList );      
	addCusShortNameOption(cusShortNameList );        
	addColOption(columnsHeader ) ;        
	addDivisionOption(divisionList );      
	settingColumnOption(columnsHeader, colList);   
// 	addLockColOption(columnsHeader);  
	addLockColOption(mapsDataHeader,mapsTitleHeader);       
	$('#multi_userStatus option').attr("selected","selected");
	$('#multi_userStatus').selectpicker('refresh');
	$('#multi_cusName option').attr("selected","selected");
	$('#multi_cusName').selectpicker('refresh');
	$('#multi_cusShortName option').attr("selected","selected");
	$('#multi_cusShortName').selectpicker('refresh'); 
	$('#multi_lockCol').selectpicker('refresh');        
	$('#multi_division option').attr("selected","selected");
	$('#multi_division').selectpicker('refresh');    
// 	$('#SL_userStatus').selectpicker();      
// 	addUserStatusOption(userStatusList );     
// 	$('#SL_userStatus').selectpicker('val', userStatusList); 
	
	    
	$("#MainTable_filter").hide();    
	$("#SWMainTable_filter").hide();      
// 	$("#MainTable_info").hide();      
	  
	$("#InputDateTable_filter").hide();  
// 	$("#InputDateTable_info").hide();      
    $('a[data-toggle="tab"]').on('shown.bs.tab', function (e) { 
    	MainTable.columns.adjust();  
    	InputDateTable.columns.adjust();  
    	SWMainTable.columns.adjust();  
    });         
    $('.modal').on('shown.bs.modal', function() {   
		MainTable.columns.adjust();  
		InputDateTable.columns.adjust();  
		SWMainTable.columns.adjust();  
	})   	     
 //--------------------------------------- SEARCH ----------------------------------------------
 	$('#btn_lockColumn').on( 'click', function () {              
 		$('#multi_lockCol').selectpicker('refresh');       
 		$('#modalLockColumnDetail').modal('show');       
 	} );   
    $('#btn_setLockCol').on( 'click', function () {     
	    var selectedItem = $('#multi_lockCol').val();    // get selected list  
	    clearStickyInput();   
	    if(selectedItem == ''){
	    	 new $.fn.dataTable.FixedColumns( MainTable, {     
	 	        leftColumns: 0,
	 	        rightColumns: 0   
	 	    } );   
	    	swal({  
				title: "Success",    
			 	text: "Clear Lock Success" ,
				icon: "success",
				button: "confirm",
			});  
	    }
	    else{    
// 	    	 MainTable.colReorder.move( 23, 0 ,true);           
			let colReArray = colReOrderBySelect(columnsHeader,selectedItem);
			swal({  
				title: "Success",    
			 	text: "Lock all previous column of "+selectedItem ,
				icon: "success",
				button: "confirm",
			});  
			 
//			 MainTable.colReorder.order(          
//					 colReArray   
//			    		,true); // true make it https://datatables.net/reference/api/colReorder.order() 
//		    MainTable.colReorder.order(     
//		    		[23,0,1,2,3,4,5,6,7,8,9,10,  
//		    		 11,12,13,14,15,16,17,18,19,    
//		    		 20,21,22,24,25,26,27,28,29,         
//		    		 30,31,32,33,34,35 ]   
//		    		,true); // true make it https://datatables.net/reference/api/colReorder.order() 
	    }      
// 	      new $.fn.dataTable.FixedColumns( MainTable );
// 		columnsHeader = MainTable.settings().init().columns;  
//     	getVisibleColumnsTable(columnsHeader)        
	} );   
  	$('#btn_saveDefault').on( 'click', function () {        
	     saveDefault(); 
	} );
    $('#btn_loadDefault').on( 'click', function () {       
	     loadDefault();
	} );
 	$('#btn_clear').on( 'click', function () {       
	     clearInput();   
	 	} );  
	$('#save_col_button').on( 'click', function () {      
// 		MainTable.colReorder.reset();         
		 new $.fn.dataTable.FixedColumns( MainTable, {     
	 	        leftColumns: 0,      
	 	        rightColumns: 0
	 	    } );  
	 	clearStickyInput()        
	    var selectedItem = $('#multi_colVis').val();    // get selected list   
// 		columnsHeader = MainTable.settings().init().columns;   
		setColVisibleTable(columnsHeader,selectedItem); 
    	getVisibleColumnsTable(columnsHeader)      
    	saveColSettingToServer(selectedItem);  
 		addLockColOption(mapsDataHeader,mapsTitleHeader);           
 		     
 	} );     
    $('#btn_search').on( 'click', function () {     
    	$("#submit_button").click()  
    	soLineTmp = '';            
		soTmp = '';  
    	searchByDetail();    
    	 var selectedItem = $('#multi_lockCol').val();    // get selected list  
 	    clearStickyInput();
 	    if(selectedItem == ''){
 	    	 new $.fn.dataTable.FixedColumns( MainTable, {     
 	 	        leftColumns: 0,
 	 	        rightColumns: 0   
 	 	    } );    
 	    }
 	    else{    
 			let colReArray = colReOrderBySelect(columnsHeader,selectedItem); 
 	    }      
 	} );            
 	$('#btn_colSetting').on( 'click', function () {      
//  		MainTable. colReorder.order( [ 5,1,2,3,4,0,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23 ] ); 
 		$('#modalColSetting').modal('show');    
 	} );          
    $('#btn_download').on( 'click', function () {           
    	var ObjMarkup = MainTable.data().toArray(); 
    	exportCSV(ObjMarkup)                 
 	} );    
   $("#MainTable").on("change",".CauseOfDelayInput",function(){  
		  var $row = $(this).parents("tr");
		var idx = MainTable.row($row).index();  
		var rowData = MainTable.row($row).data();  
		var oldValue = rowData.causeOfDelay.trim();  
		var newValue = $(this).val().trim();            
// 	    rowData.CauseOfDelay = $(this).val();     
		handlerInputField("causeOfDelay" ,oldValue,newValue,check1,rowData ,MainTable,idx)
		//DelayedDep  
	}) 
  $("#MainTable").on("change",".DelayedDepInput",function(){  
		  var $row = $(this).parents("tr");
		var idx = MainTable.row($row).index();  
		var rowData = MainTable.row($row).data();  
		var oldValue = rowData.delayedDepartment.trim();  
		var newValue = $(this).val().trim();            
// 	    rowData.DelayedDepartment = $(this).val();     
		handlerInputField("delayedDep" ,oldValue,newValue,check1,rowData ,MainTable,idx)
		//DelayedDep  
	}) 
  $("#MainTable").on("keydown blur",".SwitchRemarkInput", function (e) { 
		var $row = $(this).parents("tr");
		var idx = MainTable.row($row).index();  
		var rowData = MainTable.row($row).data(); 
		var oldValue = rowData.switchRemark.trim();  
		var prodOrder = rowData.productionOrder.trim();  
        var newValue = $(this).val().trim();      
		if (event.keyCode === 13) {         
			 e.stopImmediatePropagation();
			 e.preventDefault();      
			 if(oldValue == newValue){ }
			 else if(prodOrder != newValue){
				handlerInputField("switchRemark" ,oldValue,newValue,check1,rowData ,MainTable,idx)
			 }
			 else{
				swal({   
		   		    title: 'Warning',      
		   		    text: "ProductionOrder can't switch same productionOrder.",    
		   		    icon: 'warning',
		   		    timer: 1000,
		   		    buttons: false,
				})
			  	rowData.switchRemark  = oldValue;
			  	MainTable.row(idx).invalidate() ;  
			 } 
		}                      
		else if (event.type === 'blur'  && check1 == 0){    
			 e.stopImmediatePropagation();
			 e.preventDefault();          
			 if(oldValue == newValue){ }
			 else if(prodOrder != newValue){
				handlerInputField("switchRemark" ,oldValue,newValue,check1,rowData ,MainTable,idx)
			 }
			 else{
				 swal({   
			   		    title: 'Warning',      
			   		    text: "ProductionOrder can't switch same productionOrder.",    
			   		    icon: 'warning',
			   		    timer: 1000,
			   		    buttons: false,
			   		})
			  	rowData.switchRemark = oldValue;
			  	MainTable.row(idx).invalidate() ;  
			 } 
		} 
	});  
      
  function handlerInputField(fieldName ,oldValue,newValue,checkField,rowData ,MainTable,idx){
	  let dataArray ;
	  checkField = 1 ;    
	  let objTmp = {   
			ProductionOrder: rowData.productionOrder ,   
		    fieldName:  fieldName,
			rowData: rowData,          
			newValue: newValue,       
			oldValue: oldValue,      
		    idx: idx,  
	  };  
		 swal({ 
			  title: "Are you sure to change "+fieldName+" ?",
			  text: "From : "+oldValue+" to "+newValue+" ",
			  icon: "warning",
			  buttons: true,      
			  dangerMode: true,						   								   																																	  
			})
			.then((willDelete) => {  
			  if (willDelete) {          
			  	setValueWithFieldName(fieldName,rowData,newValue)
				MainTable.row(idx).invalidate() ;  
		 		var json = createInputDateJsonData(rowData,fieldName ); 
	 			var  obj = JSON.parse(json);    
	 			var arrayTmp = [];            
				arrayTmp.push(obj);           
				saveInputDetailToServer(arrayTmp,objTmp);   
					 
			  } else {  
				  setValueWithFieldName(fieldName,rowData,oldValue)
				  MainTable.row(idx).invalidate() ; 
			  }
		});      
		 checkField= 0;  
  }
  $("#MainTable").on("keydown blur",".PCRemarkInput", function (e) { 
		var $row = $(this).parents("tr");
		var idx = MainTable.row($row).index();      
		var rowData = MainTable.row($row).data(); 
		var oldValue = rowData.pcRemark;  
        var newValue = $(this).val();      
		 if (event.keyCode === 13) {         
			 e.stopImmediatePropagation();
			 e.preventDefault();  
			 if(oldValue == newValue){  }
			 else { handlerInputField("pcRemark" ,oldValue,newValue,check1,rowData ,MainTable,idx) }
		}                      
		else if (event.type === 'blur'  && check1 == 0){    
			 e.stopImmediatePropagation();	
			 e.preventDefault();       
			 if(oldValue == newValue){  }
			 else { handlerInputField("pcRemark" ,oldValue,newValue,check1,rowData ,MainTable,idx)    }
		} 
	});  
  $("#MainTable").on("keydown blur",".StockLoadInput", function (e) { 
		var $row = $(this).parents("tr");
		var idx = MainTable.row($row).index();      
		var rowData = MainTable.row($row).data(); 
		var oldValue = rowData.stockLoad;  
      var newValue = $(this).val();      
		 if (event.keyCode === 13) {         
			 e.stopImmediatePropagation();
			 e.preventDefault();  
			 if(oldValue == newValue){  }
			 else { handlerInputField("stockLoad" ,oldValue,newValue,check1,rowData ,MainTable,idx) }
		}                      
		else if (event.type === 'blur'  && check1 == 0){    
			 e.stopImmediatePropagation();	
			 e.preventDefault();       
			 if(oldValue == newValue){  }
			 else { handlerInputField("stockLoad" ,oldValue,newValue,check1,rowData ,MainTable,idx)    }
		} 
	});  
  $("#MainTable").on("keydown blur",".ReplacedRemarkInput", function (e) { 
		var $row = $(this).parents("tr");
		var idx = MainTable.row($row).index();      
		var rowData = MainTable.row($row).data(); 
		var oldValue = rowData.replacedRemark;  
        var newValue = $(this).val();      
        var checkDigit = true;
		 if (event.keyCode === 13) {         	    
			 e.stopImmediatePropagation();
			 e.preventDefault();         
			 checkReplaced = 1 ;    
			 if(oldValue == newValue){  }
			 else {
				let mainSplit = newValue.split(",");
				for(let i = 0 ; i < mainSplit.length; i++) {
					let subSplit = mainSplit[i].split("="); 
					if(subSplit.length > 1){       
						let volume = subSplit[1].trim()   
						checkDigit = checkIfDigitOnly(volume)
					}
					if(checkDigit){ }
					else{ break;   } 
				}
				if(!checkDigit)  {
					swal({   
			   		    title: 'Warning',
			   		    text: 'After = only numbers are required.',    
			   		    icon: 'warning',
			   		    timer: 1000,
			   		    buttons: false,
			   		})
				}
				else{ handlerInputField("replacedRemark" ,oldValue,newValue,check1,rowData ,MainTable,idx);   } 
			 }
		 }            
		else if (event.type === 'blur'  && check1 == 0){    
			 e.stopImmediatePropagation();
			 e.preventDefault();         
			 checkReplaced = 1 ;    
			 if(oldValue == newValue){  }
			 else {
			 	let mainSplit = newValue.split(",");
				for(let i = 0 ; i < mainSplit.length; i++) {
					let subSplit = mainSplit[i].split("="); 
					if(subSplit.length > 1){       
						let volume = subSplit[1].trim()   
						checkDigit = checkIfDigitOnly(volume)
					}
					if(checkDigit){ }
					else{ break; } 
				}
				if(!checkDigit)  {
					swal({   
			  		    title: 'Warning',
			  		    text: 'After = only numbers are required..',    
			  		    icon: 'warning',
			  		    timer: 1000,
			  		    buttons: false,
			  		})
				}
				else{ handlerInputField("replacedRemark" ,oldValue,newValue,check1,rowData ,MainTable,idx);   } 
			 }
		}    
	});   
  $("#MainTable").on("keydown blur",".StockRemarkInput", function (e) { 
		var $row = $(this).parents("tr");
		var idx = MainTable.row($row).index();    
		var rowData = MainTable.row($row).data();	  
		var oldValue = rowData.stockRemark;  
        var newValue = $(this).val();      
		 if (event.keyCode === 13) {         
			 e.stopImmediatePropagation();
			 e.preventDefault();    
			 if(oldValue == newValue){  }
			 else if(rowData.grade == ''){
	        	swal({
		   		    title: 'Warning',
		   		    text: 'This StockRemark need grade for input.',    
		   		    icon: 'warning',
		   		    timer: 1000,
		   		    buttons: false,
		   		})
			 } 
			 else{ handlerInputField("stockRemark" ,oldValue,newValue,check1,rowData ,MainTable,idx)  }
		}                      
		else if (event.type === 'blur'  && check1 == 0){    
			 e.stopImmediatePropagation();
			 e.preventDefault();      
			 if(oldValue == newValue){  }
			 else if(rowData.grade == ''){
	        	swal({   
		   		    title: 'Warning',
		   		    text: 'This StockRemark need grade for input.',    
		   		    icon: 'warning',
		   		    timer: 1000,
		   		    buttons: false,
		   		})
			 }
			 else{ handlerInputField("stockRemark" ,oldValue,newValue,check1,rowData ,MainTable,idx); }    
		}  
	});  
  $("#MainTable").on("keydown blur",".SendCFMCusDateInput", function (e) { 
		var $row = $(this).parents("tr");
		var idx = MainTable.row($row).index();    
		var rowData = MainTable.row($row).data();	 
		var oldValue = rowData.sendCFMCusDate;     
      var newValue = $(this).val();     
		 if (event.keyCode === 13) {         
			e.stopImmediatePropagation();
			e.preventDefault();       
			check1 = 1 ;    
			newValue  = checkDateFormatInput( newValue,oldValue)  
			if(newValue == 'E0'){ 
	           	rowData.sendCFMCusDate  = oldValue;
	           	MainTable.row(idx).invalidate() ;  //      			MainTable.row(idx).invalidate().draw();  
			}
			else if(newValue == 'E1'){ 
				swal({
		   		    title: 'Warning',
		   		    text: 'Pleas check format input date ( DD/MM/YYYY ).',
		   		    icon: 'warning',
		   		    timer: 1000,
		   		    buttons: false,
		   		})
			}
			else if(newValue == 'E2'){
				swal({
		   		    title: 'Warning',
		   		    text: 'Date need greater than equal today.',
		   		    icon: 'warning',
		   		    timer: 1000,
		   		    buttons: false,
		   		})  
			}       
			else if(newValue == 'E3'){ }
			else{
// 				swal({ 
// 					  title: "Are you sure to change date?",
// 					  text: "From : "+oldValue+" to "+newValue+" ",
// 					  icon: "warning",
// 					  buttons: true,  
// 					  dangerMode: true,						   																																								  
// 					})
// 					.then((willDelete) => {        
// 					  if (willDelete) {     
// 						  rowData.SendCFMCusDate  = newValue;
// 						  MainTable.row(idx).invalidate() ;  
// 						  var json = createInputDateJsonData(rowData,'SendCFMCusDate'); 
// 						  var  obj = JSON.parse(json);    
// 						  var arrayTmp = [];  
// 						  arrayTmp.push(obj);       
// // 						  saveInputDateToServer(arrayTmp);
						  handlerInputField("sendCFMCusDate" ,oldValue,newValue,check1,rowData ,MainTable,idx) 
// 					  } else { 
// 						  rowData.SendCFMCusDate  = oldValue;
// 						  MainTable.row(idx).invalidate() ; 
// //						  MainTable.row(idx).invalidate().draw(); 
// 					  }
// 				});    
				
			}            
			check1= 0;   
		}                      
		else if (event.type === 'blur'  && check1 == 0){    
			check1= 1; 
			newValue  = checkDateFormatInput( newValue,oldValue)   
			 e.stopImmediatePropagation();
			 e.preventDefault();        
			if(newValue == 'E0'){ 
	           	rowData.sendCFMCusDate  = oldValue;
	           	MainTable.row(idx).invalidate() ; 
//    			MainTable.row(idx).invalidate().draw();  
           }
           else if(newValue == 'E1'){
					swal({
			   		    title: 'Warning',
			   		    text: 'Pleas check format input date ( DD/MM/YYYY ).',
			   		    icon: 'warning',
			   		    timer: 1000,
			   		    buttons: false,
			   		})
				}
				else if(newValue == 'E2'){
					swal({
			   		    title: 'Warning',
			   		    text: 'Date need greater than equal today.',
			   		    icon: 'warning',
			   		    timer: 1000,
			   		    buttons: false,
			   		}) 
				}      
				else if(newValue == 'E3'){ }
           else{  
// 	           	 swal({ 
// 					  title: "Are you sure to change date?",
// 					  text: "From : "+oldValue+" to "+newValue,
// 					  icon: "warning",
// 					  buttons: true,
// 					  dangerMode: true,																																														  
// 					})
// 					.then((willDelete) => {
// 					  if (willDelete) {  
// 						  rowData.SendCFMCusDate  = newValue;   
// 							MainTable.row(idx).invalidate() ;  
// 	 				 		var json = createInputDateJsonData(rowData,'SendCFMCusDate'); 
// 	 			 			var  obj = JSON.parse(json);    
// 	 			 			var arrayTmp = [];  
// 	 						arrayTmp.push(obj);       
// // 	 						saveInputDateToServer(arrayTmp);        

						  	handlerInputField("sendCFMCusDate" ,oldValue,newValue,check1,rowData ,MainTable,idx) 
// 					  } else {       
// 						  rowData.SendCFMCusDate  = oldValue; 
// 						  MainTable.row(idx).invalidate() ;  
// //						  MainTable.row(idx).invalidate().draw(); 
// 					  }
// 				}); 
			}
           check1= 0;  
		} 
	});  
	 $("#MainTable").on("keydown blur",".CFMPlanLabDateInput", function (e) { 
		var $row = $(this).parents("tr");
		var idx = MainTable.row($row).index();    
		var rowData = MainTable.row($row).data();	 
		var oldValue = rowData.cfmPlanLabDate;     
        var newValue = $(this).val();     
		 if (event.keyCode === 13) {         
			e.stopImmediatePropagation();   
			e.preventDefault();       
			check1 = 1 ;     
// 			console.log(' newValue ', newValue,' oldValue ',oldValue)     
			newValue  = checkDateFormatInput( newValue,oldValue)  
// 			console.log(' newValue ', newValue,' oldValue ',oldValue)   
// 			console.log('-----------------------------------' )   
			if(newValue == 'E0'){ 
             	rowData.cfmPlanLabDate  = oldValue;
             	MainTable.row(idx).invalidate() ;  //      			MainTable.row(idx).invalidate().draw();  
			}
			else if(newValue == 'E1'){ 
				swal({
		   		    title: 'Warning',
		   		    text: 'Pleas check format input date ( DD/MM/YYYY ).',
		   		    icon: 'warning',
		   		    timer: 1000,
		   		    buttons: false,
		   		})
			}
			else if(newValue == 'E2'){
				swal({
		   		    title: 'Warning',
		   		    text: 'Date need greater than equal today.',
		   		    icon: 'warning',
		   		    timer: 1000,
		   		    buttons: false,
		   		})  
			}       
			else if(newValue == 'E3'){ }
			else{
				swal({ 
					  title: "Are you sure to change date?",
					  text: "From : "+oldValue+" to "+newValue+" ",
					  icon: "warning",
					  buttons: true,  
					  dangerMode: true,						   																																								  
					})
					.then((willDelete) => {        
					  if (willDelete) {     
						  rowData.cfmPlanLabDate  = newValue;
						  MainTable.row(idx).invalidate() ; 
// 						  MainTable.row(idx).invalidate().draw();  
						  var json = createInputDateJsonData(rowData,'cfmPlanLabDate'); 
						  var  obj = JSON.parse(json);    
						  var arrayTmp = [];  
						  arrayTmp.push(obj);       
						  saveInputDateToServer(arrayTmp);      
					  } else { 
						  rowData.cfmPlanLabDate  = oldValue;
						  MainTable.row(idx).invalidate() ; 
// 						  MainTable.row(idx).invalidate().draw(); 
					  }
				});    
				
			}     
			check1= 0;   
		}                      
		else if (event.type === 'blur'  && check1 == 0){    
			check1= 1; 
			newValue  = checkDateFormatInput( newValue,oldValue)   
 			 e.stopImmediatePropagation();
			 e.preventDefault();        
			if(newValue == 'E0'){ 
             	rowData.cfmPlanLabDate  = oldValue;
             	MainTable.row(idx).invalidate() ; 
//      			MainTable.row(idx).invalidate().draw();  
             }
             else if(newValue == 'E1'){
					swal({
			   		    title: 'Warning',
			   		    text: 'Pleas check format input date ( DD/MM/YYYY ).',
			   		    icon: 'warning',
			   		    timer: 1000,
			   		    buttons: false,
			   		})
				}
				else if(newValue == 'E2'){
					swal({
			   		    title: 'Warning',
			   		    text: 'Date need greater than equal today.',
			   		    icon: 'warning',
			   		    timer: 1000,
			   		    buttons: false,
			   		}) 
				}      
				else if(newValue == 'E3'){ }
             else{  
	           	 swal({ 
					  title: "Are you sure to change date?",
					  text: "From : "+oldValue+" to "+newValue,
					  icon: "warning",
					  buttons: true,
					  dangerMode: true,																																														  
					})
					.then((willDelete) => {
					  if (willDelete) {  
						  rowData.cfmPlanLabDate  = newValue;
// 						  MainTable.row(idx).invalidate().draw();  
							MainTable.row(idx).invalidate() ;  
	 				 		var json = createInputDateJsonData(rowData,'cfmPlanLabDate'); 
	 			 			var  obj = JSON.parse(json);    
	 			 			var arrayTmp = [];  
	 						arrayTmp.push(obj);       
	 						saveInputDateToServer(arrayTmp);        
					  } else {       
						  rowData.cfmPlanLabDate  = oldValue; 
						  MainTable.row(idx).invalidate() ;  
// 						  MainTable.row(idx).invalidate().draw(); 
					  }
				}); 
			}
             check1= 0;  
		} 
	});  
	 $("#MainTable").on("keydown blur",".DeliveryDateInput", function (e) { 
			var $row = $(this).parents("tr");
			var idx = MainTable.row($row).index();    
			var rowData = MainTable.row($row).data();	 
			var oldValue = rowData.deliveryDate;
	        var newValue = $(this).val();           
			if (event.keyCode === 13) {      
				check3 = 1 ;    
				e.stopImmediatePropagation();
				e.preventDefault();       
				newValue  = checkDateFormatInput( newValue,oldValue)    
				if(newValue == 'E0'){ 
	             	rowData.deliveryDate  = oldValue;
	             	MainTable.row(idx).invalidate() ; 
// 	     			MainTable.row(idx).invalidate().draw();  
	             }else if(newValue == 'E1'){
						swal({
				   		    title: 'Warning',
				   		    text: 'Pleas check format input date ( DD/MM/YYYY ).',
				   		    icon: 'warning',
				   		    timer: 1000,
				   		    buttons: false,
				   		})
					}
					else if(newValue == 'E2'){
						swal({
				   		    title: 'Warning',
				   		    text: 'Date need greater than equal today.',
				   		    icon: 'warning',
				   		    timer: 1000,
				   		    buttons: false,
				   		})
					}      
					else if(newValue == 'E3'){ }
				else{
					swal({
						  title: "Are you sure to change date?",
						  text: "From : "+oldValue+" to "+newValue,  		
						  icon: "warning",
						  buttons: true,
						  dangerMode: true,																																														  
					})     
					.then((willDelete) => {
						if (willDelete) {  
							 rowData.deliveryDate  = newValue;
							 MainTable.row(idx).invalidate() ; 
// 							 MainTable.row(idx).invalidate().draw();  
							 var json = createInputDateJsonData(rowData,'deliveryDate'); 
		 			 			var  obj = JSON.parse(json);    
		 			 			var arrayTmp = [];  
		 						arrayTmp.push(obj);   
		 						saveInputDateToServer(arrayTmp);  
						} else { 
							 rowData.deliveryDate  = oldValue;
							 MainTable.row(idx).invalidate() ; 
// 							 MainTable.row(idx).invalidate().draw(); 
						}
					});    
					
				}  
				check3= 0;  
			}                
//				--------------------BLUR---------------------------
			else if (event.type === 'blur'  && check3 == 0){    
				check3= 1; 
				e.stopImmediatePropagation();
				e.preventDefault();         
				newValue  = checkDateFormatInput( newValue,oldValue)    
				if(newValue == 'E0'){ 
	             	rowData.deliveryDate  = oldValue;
	             	MainTable.row(idx).invalidate() ; 
// 	     			MainTable.row(idx).invalidate().draw();  
	             }   
				else if(newValue == 'E1'){
					swal({
			   		    title: 'Warning',
			   		    text: 'Please check format input date ( DD/MM/YYYY ).',
			   		    icon: 'warning',
			   		    timer: 1000,
			   		    buttons: false,
			   		})
				}
				else if(newValue == 'E2'){
					swal({
			   		    title: 'Warning',
			   		    text: 'Date need greater than equal today.',
			   		    icon: 'warning',
			   		    timer: 1000,
			   		    buttons: false,
			   		})
				}      
				else if(newValue == 'E3'){ }
				else{
					swal({
						  title: "Are you sure to change date?",
						  text: "From : "+oldValue+" to "+newValue,  		
						  icon: "warning",
						  buttons: true,
						  dangerMode: true,																																														  
						})     
						.then((willDelete) => {
						  if (willDelete) {  
							  rowData.deliveryDate  = newValue;
							  MainTable.row(idx).invalidate() ; 
// 							  MainTable.row(idx).invalidate().draw();   
							  var json = createInputDateJsonData(rowData,'deliveryDate'); 
		 			 		  var  obj = JSON.parse(json);    
		 			 		  var arrayTmp = [];  
		 					  arrayTmp.push(obj);   
		 					  saveInputDateToServer(arrayTmp);  
						  } else { 
							  rowData.deliveryDate  = oldValue;
							  MainTable.row(idx).invalidate() ; 
// 							  MainTable.row(idx).invalidate().draw(); 
						  }
						});    
					
				} 
	             check3= 0; 
			} 
		});  
	 
// 	$('#MainTable').on('dblclick','td',function(e){
// // 		scroll_to_contact_form_fn()  
// 	    var row_object  = MainTable.row(this).data();    
// 	    if(MainTable.cell(this).index() === undefined){
	    	
// 	    } 
// 	    else{      
// 	    	 var colIdx = MainTable.cell(this).index().column; 
// 		    var arrTmp = [];
// 			arrTmp.push(row_object);
// 			getInputDate(arrTmp,colIdx);
// 	    }
	       
// 	})      
	$('#MainTable').on('dblclick','td',function(e){ 
	    var row_object  = MainTable.row(this).data();             
	    var $rowC =$(this).attr('class') 
	    if(MainTable.cell(this).index() === undefined){  }     
	    else {          
	    	if($rowC != undefined){ 
		    	let myArray = $rowC.split(" ");
		    	if(myArray.length > 0){ 
			    	let classInput = myArray[1];  
			    	if(classInput == 'cfmPlanLabDateParent' || classInput == 'cfmPlanDateParent' 
		   			|| classInput == 'deliveryDateParent'   || classInput == 'sendCFMCusDateParent'){      
					    var arrTmp = [];   
						arrTmp.push(row_object);
						getInputDate(arrTmp,classInput);
				    }  
		    	}
	    	}
	    	
	    }
	       
	})                
	preLoaderHandler( preloader)   
});      
function clearInput(){
	document.getElementById("input_saleOrder").value = '';  
	document.getElementById("input_article").value   = '';  
	document.getElementById("input_prdOrder").value  = '';     
// 	document.getElementById("input_saleOrderDate").value = '';  
	document.getElementById("input_designNo").value  = '';  
// 	document.getElementById("input_prdOrderDate").value  = '';  
	document.getElementById("input_material").value  = '';         
	document.getElementById("input_labNo").value     = '';  
	document.getElementById("input_PO").value     = '';  
	document.getElementById('SL_saleNumber').value= '';
	document.getElementById('SL_delivStatus').value='';
	$('#input_saleOrderDate').val('');      
	$('#input_prdOrderDate').val('');    
	$('#input_dueDate').val('');
	$('#multi_userStatus').val('default').selectpicker('deselectAll');  
	$('#multi_cusName').val('default').selectpicker('deselectAll');      
	$('#multi_cusShortName').val('default').selectpicker('deselectAll');  
	$('#multi_division').val('default').selectpicker('deselectAll');  
	$('#multi_userStatus').selectpicker('refresh');     
	$('#multi_cusName').selectpicker('refresh');
	$('#multi_cusShortName').selectpicker('refresh'); 
	$('#multi_division').selectpicker('refresh');       
	
}
function searchByDetail(){ 
// 	var customer = document.getElementById("input_customer").value .trim();
// 	var customerShort = document.getElementById("input_customerShortName").value .trim();
	var saleOrder = document.getElementById("input_saleOrder").value .trim();  
	var article = document.getElementById("input_article").value .trim();  
	var prdOrder = document.getElementById("input_prdOrder").value .trim();
	var saleNumber = document.getElementById("SL_saleNumber").value .trim();
	var SaleOrderDate = document.getElementById("input_saleOrderDate").value .trim();
	var designNo = document.getElementById("input_designNo").value .trim(); 
	var prdOrderDate = document.getElementById("input_prdOrderDate").value .trim(); 
	var material = document.getElementById("input_material").value .trim(); 
	var po = document.getElementById("input_PO").value .trim(); 
	var labNo = document.getElementById("input_labNo").value .trim();  
	var dueDate = document.getElementById("input_dueDate").value .trim(); 
	var deliStatus = document.getElementById("SL_delivStatus").value .trim(); 
	var dmCheck = document.getElementById("check_DM").checked;  
	var exCheck = document.getElementById("check_EX").checked;  
	var hwCheck = document.getElementById("check_HW").checked;  
	
	var userStatus = $('#multi_userStatus').val(); 
	var customer = $('#multi_cusName').val();    
	var customerShort = $('#multi_cusShortName').val(); 
	var division = $('#multi_division').val(); 
	var dist = "";     
	 var saleStatus = document.querySelector('input[name="saleStatusRadio"]:checked').value;
	 if( dmCheck ){ dist = "DM";}
	 if( exCheck ){ if(dist != "") {dist = dist + "|" } dist = "EX";}     
	 if( hwCheck ){ if(dist != "") {dist = dist + "|"} dist = "HW";}     
	if(  (customer.length == 0 ||  customerShort.length == 0 ||  userStatus.length == 0 || division.length  == 0)  )  { 
		swal({
   		    title: 'Warning',
   		    text: 'Need select some field for search.',
   		    icon: 'warning',   
   		    timer: 1000,
   		    buttons: false,
   		})
	}  
	else{
		var json = createJsonData(); 
	    var  obj = JSON.parse(json);    
		var arrayTmp = [];   
		arrayTmp.push(obj); 
		searchByDetailToServer(arrayTmp); 
	}
} 

function createInputDateJsonData(val,caseSave){ 
	var CFMPlanLabDate = val.cfmPlanLabDate;      
	var CFMPlanDate = val.cfmPlanDate;       
	var DeliveryDate = val.deliveryDate;      
	var SaleOrder = val.saleOrder; 
	var SaleLine = val.saleLine; 
	var ProductionOrder = val.productionOrder; 
	var ReplacedRemark = val.replacedRemark; 
	var PCRemark = val.pcRemark; 
	var StockRemark = val.stockRemark; 
	var LotNo = val.lotNo; 
	var Grade = val.grade; 

	var CauseOfDelay = val.causeOfDelay; 
	var DelayedDepartment = val.delayedDepartment; 
	var SwitchRemark = val.switchRemark; 
	var StockLoad = val.stockLoad; 
	var SendCFMCusDate = val.sendCFMCusDate; 
	var UserStatus = val.userStatus; 
	var json = '{"productionOrder":'+JSON.stringify(ProductionOrder)+ 
	   ',"userStatus":'+JSON.stringify(UserStatus)+  
	   ',"cfmPlanLabDate":'+JSON.stringify(CFMPlanLabDate)+  
	   ',"cfmPlanDate":'+JSON.stringify(CFMPlanDate)+  
	   ',"deliveryDate":'+JSON.stringify(DeliveryDate)+  
	   ',"saleOrder": '+JSON.stringify(SaleOrder)+ 
	   ',"saleLine": '+JSON.stringify(SaleLine)+ 
	   ',"caseSave": '+JSON.stringify(caseSave)+ 
	   ',"replacedRemark": '+JSON.stringify(ReplacedRemark)+    
	   ',"stockRemark": '+JSON.stringify(StockRemark)+
	   ',"pcRemark":'+JSON.stringify(PCRemark)+
	   ',"grade": '+JSON.stringify(Grade)+  
	   ',"lotNo": '+JSON.stringify(LotNo)+
	   ',"switchRemark": '+JSON.stringify(SwitchRemark)+ 
	   ',"stockLoad": '+JSON.stringify(StockLoad)+    
	   ',"delayedDepartment": '+JSON.stringify(DelayedDepartment)+   
	   ',"causeOfDelay": '+JSON.stringify(CauseOfDelay)+    
	   ',"sendCFMCusDate": '+JSON.stringify(SendCFMCusDate)+    
	   '} ';          
	   return json; 
}
function createJsonData(){ 
// 	var customer = document.getElementById("input_customer").value .trim();      
// 	var customerShort = document.getElementById("input_customerShortName").value .trim();  
	var saleOrder = document.getElementById("input_saleOrder").value .trim();  
	var article = document.getElementById("input_article").value .trim();  
	var prdOrder = document.getElementById("input_prdOrder").value .trim();    
	var saleNumber = document.getElementById("SL_saleNumber").value .trim();
	var saleOrderDate = document.getElementById("input_saleOrderDate").value .trim();
	var po = document.getElementById("input_PO").value .trim(); 
	var designNo = document.getElementById("input_designNo").value .trim();  
	var prdOrderDate = document.getElementById("input_prdOrderDate").value .trim(); 
	var material = document.getElementById("input_material").value .trim(); 
	var labNo = document.getElementById("input_labNo").value .trim(); 
	var po = document.getElementById("input_PO").value .trim(); 
// 	var userStatus = document.getElementById("SL_userStatus").value .trim();  
	var deliStatus = document.getElementById("SL_delivStatus").value .trim();  
	var dueDate = document.getElementById("input_dueDate").value .trim(); 
	var dmCheck = document.getElementById("check_DM").checked;  
	var exCheck = document.getElementById("check_EX").checked;  
	var hwCheck = document.getElementById("check_HW").checked;    
	var division = $('#multi_division').val(); 
	var userStatus = $('#multi_userStatus').val();    
	var customer = $('#multi_cusName').val();
	var customerShort = $('#multi_cusShortName').val();
	var dist = "";    
	 var saleStatus = document.querySelector('input[name="saleStatusRadio"]:checked').value;
	 if( dmCheck ){ dist = "DM";}
	 if( exCheck ){ if(dist != "") {dist = dist + "|" } dist = dist + "EX";}       
	 if( hwCheck ){ if(dist != "") {dist = dist + "|" } dist = dist + "HW";}
	 var cusDiv = "";
// 	 if(configCusList.length > 0 ){ 
// 	 	let p_cusDiv = configCusList[0].customerDivision	 ;
// 	 	if(p_cusDiv!=''){ 
// 	 		cusDiv = p_cusDiv;
// 	 	} 
// 	 }
// 	 else{
// 		 cusDiv = p_cusDiv; 
// 	 }
	var json = '{'+
// 		'"CustomerName":'+JSON.stringify(customer)+ 
// 	   ',"CustomerShortName":'+JSON.stringify(customerShort)+ 
	   '"saleOrder":'+JSON.stringify(saleOrder)+ 
	   ',"articleFG":'+JSON.stringify(article)+  
	   ',"productionOrder":'+JSON.stringify(prdOrder)+  
	   ',"saleNumber": '+JSON.stringify(saleNumber)+
	   ',"saleOrderCreateDate":'+JSON.stringify(saleOrderDate)+
	   ',"designFG":'+JSON.stringify(designNo)+       
	   ',"productionOrderCreateDate":'+JSON.stringify(prdOrderDate)+ 
	   ',"materialNo":'+JSON.stringify(material)+          
	   ',"labNo":'+JSON.stringify(labNo)+   
// 	   ',"UserStatus":'+JSON.stringify(userStatus)+      
	   ',"userStatusList":'+JSON.stringify(userStatus)+       
	   ',"customerNameList":'+JSON.stringify(customer)+   
	   ',"customerDivision":'+JSON.stringify(cusDiv)+      
	   ',"purchaseOrder":'+JSON.stringify(po)+   
	   ',"customerShortNameList":'+JSON.stringify(customerShort)+   
	   ',"divisionList":'+JSON.stringify(division)+   
	   ',"deliveryStatus":'+JSON.stringify(deliStatus)+         
	   ',"saleStatus":'+JSON.stringify(saleStatus)+  
	   ',"distChannel":'+JSON.stringify(dist) + 
	   ',"dueDate":'+JSON.stringify(dueDate) + 
	   '} ';     
// 	   console.log(json)
// 	   console.log(json) 
	   return json; 
}
function exportCSV(data){ 
    var createXLSLFormatObj = [];   
    /* XLS Head Columns */      
//  var xlsHeader = [
//  	 "Division"            ,         
//  	 "SaleOrder"           ,    
//  	 "SaleLine"            ,     
//  	 "CustomerShortName"   , 
//  	 "SaleCreateDate"      ,    
//  	 "PurchaseOrder" ,   
//  	 "MaterialNo" , 
//  	 "CustomerMaterial" ,  
//  	 "Price" , 
//  	 "SaleUnit" , 
//  	 "SaleQuantity" ,          
//  	 "RemainQuantity" ,         
//  	 "RemainAmount" , 
//  	 "TotalQuantity" ,        
//  	 "Grade" , 
//  	 "BillSendWeightQuantity" , 
//  	 "BillSendQuantity" ,        
//  	 "CustomerDue" , 
//  	 "DueDate" ,               
//  	 "LotNo" , 
//  	 "LabNo" ,  
//  	 "LabStatus" ,              
//  	 "CFMPlanLabDate" ,         
//  	 "CFMActualLabDate" , 
//  	 "CFMCusAnsLabDate" ,          
//  	 "UserStatus" ,                  
//  	 "TKCFM" ,                      
//  	 "CFMPlanDate" ,   
//  	 "CFMSendDate" ,             
//  	 "CFMLastest" ,         
//  	 "CFMNumber" ,          
//  	 "DeliveryDate" ,       
//  	 "ShipDate" ,              
//  	 "Remark"  ];      
    /* XLS Rows Data */
    let xlsHeader = Array.from( mapsTitleHeader.keys() ); 
    var xlsRows = data     
    createXLSLFormatObj.push(xlsHeader);
//     createXLSLFormatObj.push(xlsHeader);    
    let indexArray = 0,colType = '';       
    let caseDupli = 0;

    soLineTmpExcel = '' ;
    soTmpExcel = '';
    $.each(xlsRows, function(index, value) { 
        var innerRowData = [];           
        caseDupli = checkSaleOrderLine( value); 
        $.each(value, function(data, val) {     	
//             innerRowData.push(val);         
        	 if(mapsDataHeader.size != 0){        
        		 indexArray = mapsDataHeader.get(data);
        		 colType = mapsColumnHeader.get(data);       
        		 if(indexArray !== undefined){               
 					if (colType === undefined){  
 						innerRowData[indexArray] = val;        
 					}                
 					else if (colType == 'num'){          
 						if(data == 'saleQuantity' || data == 'remainQuantity' || data == 'remainAmount' || data == 'orderAmount'){  
							if(caseDupli == 0 || caseDupli == 2){   
								if(val.trim() == ''){ innerRowData[indexArray] = '';  }
		 						else{ innerRowData[indexArray] = parseFloat(val.replace(/,/g, '')) ; } 
							}     
							else{ innerRowData[indexArray] = ''; }
						}       
						else{
							if(val == ''){ innerRowData[indexArray] = '';  }
	 						else{ innerRowData[indexArray] = parseFloat(val.replace(/,/g, '')) ; } 
						}  
 					}      
 					else if (colType == 'date-euro'){   
//  						if(data == "ShipDate" ){
// //  							LotShipping
//  						}     
//  						else{
	 						if(val == ''){ innerRowData[indexArray] = ''   ;   }
	 						else{ innerRowData[indexArray] =stringToDate(val)   ; }    
//  						}    
 					    
 					}
 			  		 
 			  	}  
             }
        });         
        createXLSLFormatObj.push(innerRowData);
    });    
    /* File Name */
    var filename = "PCMS-Detail.xlsx"; 
    /* Sheet Name */       
    var ws_name = "Detail";       
    if (typeof console !== 'undefined') console.log(new Date());       
    var wb = XLSX.utils.book_new(),   	     
        ws = XLSX.utils.aoa_to_sheet(
        		createXLSLFormatObj, 
        		{
        			dateNF:"dd/MM/yyyy",
        			rawNumbers: true
       			}
       		);   
    XLSX.utils.book_append_sheet(wb, ws, ws_name);     
    /* Write workbook and Download */
    if (typeof console !== 'undefined') console.log(new Date());
    XLSX.writeFile(wb, filename); 
    if (typeof console !== 'undefined') console.log(new Date());      
}     
function checkSaleOrderLine( val){       
	if(soLineTmpExcel == '' && soTmpExcel == ''  ){     
		soLineTmpExcel = val.saleLine;     
		soTmpExcel = val.saleOrder;   
		caseDupli = 0;      
	}  
	else if(soLineTmpExcel == val.saleLine && soTmpExcel   == val.saleOrder  ){
		caseDupli = 1; 
	}   
	else{           
		soLineTmpExcel = val.saleLine;     
		soTmpExcel = val.saleOrder;   
		caseDupli = 2 
	}  
	return caseDupli;
} 
function saveColSettingToServer(arrayTmp) {    
	$.ajax({   
		type: "POST",  
		contentType: "application/json",  
		data: JSON.stringify(arrayTmp),      
		url: "Detail/saveColSettingToServer", 
		success: function(data) {   
			if(data.length > 0){
				var bean = data[0];   
				if(bean.iconStatus == 'I'){
					swal({   
						title: "Success",    
					 	text: bean.systemStatus ,
						icon: "info",
						button: "confirm",
   					});  
				}
				else{  
					swal({   
						title: "Warning ",    
					 	text: bean.systemStatus  , 
			   		    icon: 'warning',
			   		    timer: 1000,
			   		    buttons: false,
			   		})
				}  
			}
		},   
		error: function(e) {
			swal("Fail", "Please contact to IT", "error");
		},
		done: function(e) {       
		}   	
	});   
}  
function saveInputDateToServer(arrayTmp) {   
// 	console.log(arrayTmp)
	$.ajax({   
		type: "POST",  
		contentType: "application/json",  
		data: JSON.stringify(arrayTmp),      
		url: "Detail/saveInputDate", 
		success: function(data) {  
			if(data.length > 0){  
				var bean = data[0]; 
				if(bean.iconStatus == 'I0'){    
					if( bean.countPlanDate >= 35){
						swal({  
							title: "Warning",    
						 	text: bean.systemStatus+' \r\n Plan by PCMS(count) : '+bean.countPlanDate ,
							icon: "warning",
							button: "confirm",
	   					});  
					} 
					else {
						swal({  
							title: "Success",    
						 	text: bean.systemStatus+' \r\n Plan by PCMS(count) : '+bean.countPlanDate ,
							icon: "success",
							button: "confirm",
	   					});  	
					}
					 
				}
				else if(bean.iconStatus == 'I1'){      
					swal({  
						title: "Success",    
					 	text: bean.systemStatus ,
						icon: "success",
						button: "confirm",
   					});  	  
				}
				else{  
					swal({   
						title: "Warning ",    
					 	text: bean.systemStatus  , 
			   		    icon: 'warning',
			   		    timer: 1000,
			   		    buttons: false,
			   		})
				}  
			} 
		},   
		error: function(e) {
			swal("Fail", "Please contact to IT", "error");
		},
		done: function(e) {       
		}   	  
	});   
}    
function getSwitchProdOrderDetailByPrd( dataP) {      
	let i = 0; 
	for (i = 0  ; i < dataP.length; i++) {      
		var prodOrder= dataP[i].productionOrder; 
		var saleOrder = dataP[i].saleOrder; 
		var saleLine = dataP[i].saleLine; 
		var typePrd= dataP[i].typePrd;  
		let indexes = MainTable .rows() .indexes() .filter( function ( value, index ) { 
			if( MainTable.row(value).data().productionOrder == 'รอจัด Lot'){
				return ( saleOrder === MainTable.row(value).data().saleOrder && 
						 saleLine === MainTable.row(value).data().saleLine );
			}
			else {
				return ( prodOrder === MainTable.row(value).data().productionOrder && 
						 MainTable.row(value).data().TypePrd !== "orderPuang" );// return 'P2D031' === MainTable.row(value).data()[1];
			}
			 // 			return prodOrder === MainTable.row(value).data().ProductionOrder; 
      	} );                   
		MainTable.rows(indexes).remove() ;   
	}             
 	MainTable.rows.add(dataP).draw(false); // Add new data 
}        

function setInputDetailToRowByPrd( array, objTmp) { //"DelayedDep"CauseOfDelay) {        
	let i = 0;  
	let fieldName = objTmp.fieldName 
   	var checkEQ = false;    
	MainTable.rows().every(function(rowIdx, tableLoop, rowLoop, data){
        var data = this.data();         
        if (data.productionOrder === array[0].productionOrder) { 
        	if(fieldName == 'DelayedDep'){
        		data.delayedDepartment = objTmp.newValue;
        	}    
        	else if(fieldName == 'CauseOfDelay'){
        		data.causeOfDelay = objTmp.newValue;
        	}  
        	else if(fieldName == 'SendCFMCusDate'){
        		data.sendCFMCusDate = objTmp.newValue;
        	}  
            checkEQ = true; 
            this.invalidate();
        } 
     })        
}    
function setUserStatusByStockLoad( array, objTmp,data) { //"DelayedDep"CauseOfDelay) {        
	let i = 0;  
	let fieldName = objTmp.fieldName 
	let idx = objTmp.idx 
	let newVal = objTmp.newValue 
	let oldVal = objTmp.oldValue 
   	var checkEQ = false;    
	let userStatus = array[0].userStatus; 
	let customerType = array[0].customerType;  
	rowData =  MainTable.row(idx).data();  
	if(data[0].userStatus != ''){ 
		rowData.userStatus = data[0].userStatus;
	} 
	MainTable.row(idx).invalidate() ;         
}    
function saveInputDetailToServer(arrayTmp,objTmp) {    
	$.ajax({   
		type: "POST",  	
		contentType: "application/json",       
		data: JSON.stringify(arrayTmp),      
		url: "Detail/saveInputDetail", 
// 		async: false,
		success: function(data) {  
			if(data.length > 0){   
				var bean = data[0];       
				if(bean.iconStatus == 'I'){ 
					swal({
						title: "Success",    
					 	text: bean.systemStatus ,
						icon: "info",
						button: "confirm",  
   					});  
					 
					if(objTmp.fieldName == 'switchRemark' || objTmp.fieldName == 'replacedRemark'){ 
						getSwitchProdOrderDetailByPrd( data) ;
			   		}
					else if(objTmp.fieldName == 'delayedDep' || objTmp.fieldName == 'causeOfDelay' ||  
							objTmp.fieldName == 'sendCFMCusDate'){
						setInputDetailToRowByPrd( arrayTmp ,objTmp ) ; //"DelayedDep"CauseOfDelay
					}
					else if(objTmp.fieldName == 'stockLoad' ){
						setUserStatusByStockLoad( arrayTmp ,objTmp,data ) ; //"DelayedDep"CauseOfDelay
					} 
				} 
				else{       
					swal({   
						title: "Warning ",        
					 	text: bean.systemStatus  , 
			   		    icon: 'warning',     
// 			   		    timer: 2000,
// 			   		    buttons: false,
			   		    button: "confirm",    
			   		})    
			   		if(objTmp.fieldName == 'switchRemark' || objTmp.fieldName == 'replacedRemark' ){ 
			   			setValueWithFieldName(objTmp.fieldName,objTmp.rowData,objTmp.oldValue)
				  		MainTable.row(objTmp.idx).invalidate() ; 
			   		}
				}  
			}     
		},   
		error: function(e) {
			swal("Fail", "Please contact to IT", "error");
		},
		done: function(e) {       
		}   	
	});     
}    
function searchByDetailToServer(arrayTmp) {    
	$.ajax({
		type: "POST",  
		contentType: "application/json",  
		data: JSON.stringify(arrayTmp),      
		url: "Detail/searchByDetail",  
		success: function(data) {    
			MainTable.clear();      
			MainTable.rows.add(data);      
			MainTable.columns.adjust().draw();     
		},   
		error: function(e) {
			swal("Fail", "Please contact to IT", "error");
		},
		done: function(e) {       
		}   	
	});   
}       
</script>
<script type="text/javascript">  
function checkDateFormatInput( value ,oldvalue) {
    var today = new Date();  
    var date = new Date((today.getMonth()+1)+'/'+today.getDate()+'/'+today.getFullYear()); 
    var datearray = value.split("/");
    var dateInput = '';     
    var result = ''     
    datearray[0] = addLeadingZero(datearray[0] ); 
    datearray[1] = addLeadingZero(datearray[1] );
    value = datearray[0] +"/"+datearray[1]+"/"+datearray[2] 
    if (value == oldvalue) { result = 'E3';  }   
    else if (value.trim() == '') { result = '';  }    
    // EX DD/MM/YYYY
 	else if(moment(value , 'DD/MM/YYYY',true).isValid()){   	 
 		dateInput = new Date( datearray[1]  + '/' +  datearray[0] + '/' + datearray[2]) ;  
//  	    console.log('dateInput ',dateInput)    
 		if (value == oldvalue) { result = 'E0';  } 
 		else if(dateInput < date){ result = 'E2'; }
 		else{ result = value; }  
 	} 
 	else{	     
		var day = parseInt(datearray[0]);   
		var month = parseInt(datearray[1]); 
	 	var stringDay = addLeadingZero(day) ;    
// 	 	 console.log(day,isBlank(datearray[1]),month,stringDay)
       	// EX. input = dd/MM 
		if(isNumeric(month) && isNumeric(day) && isBlank(datearray[2]) ){ 
            var stringMonth = addLeadingZero(month) ; 
            value = stringDay+'/'+stringMonth+'/'+ today.getFullYear(); 
            dateInput = new Date(stringMonth+'/'+stringDay+'/'+ today.getFullYear()) ;    
//             console.log(value ,dateInput)
//             console.log(!moment(value , 'DD/MM/YYYY',true).isValid() ,dateInput < date)
            if(!moment(value , 'DD/MM/YYYY',true).isValid() ){
    			value = 'E1';
            }
            else if (dateInput < date){
            	value = 'E2';
            }
   		}
        // EX input = dd
        else if(isNumeric(day) && isBlank(datearray[1])){  
           	var stringMonth = addLeadingZero((today.getMonth()+1)) ;      
            value = stringDay+'/'+stringMonth+'/'+ today.getFullYear(); 
            dateInput = new Date(stringMonth+'/'+stringDay+'/'+ today.getFullYear()) ;    
            if(!moment(value , 'DD/MM/YYYY',true).isValid() ){
    			value = 'E1';
            }
            else if (dateInput < date){
            	value = 'E2';
            }
		}	
        else{ value = ''; }   
		result = value;   
	}     
	return result;
} 
function formatDate(date) {
    var d = new Date(date),
        month = '' + (d.getMonth() + 1),
        day = '' + d.getDate(),
        year = d.getFullYear();
    if (month.length < 2) 
        month = '0' + month;
    if (day.length < 2) 
        day = '0' + day;
    return [day, month, year].join('-');
} 
function getTime() {
	  var time = "";
	  var today = new Date();
	  var h = today.getHours();
	  var m = today.getMinutes();
	  var s = today.getSeconds();
	  // add a zero in front of numbers<10
	  m = checkTime(m);
	  s = checkTime(s);
	  time = h + ":" + m + ":" + s; 
	  return time;
	}
function checkTime(i) {
	  if (i < 10) {
	    i = "0" + i;
	  }
	  return i;
	}
function addLeadingZero(i) {
	const val = ('0' + i).slice(-2);
	return val;
}
function settingColumnOption(columnsHeader ,colVisible){ 
	 
// 	ADD COL VISIBLE CASE
	if(colVisible == null){
		$('#multi_colVis option').attr("selected","selected");
		$('#multi_colVis').selectpicker('refresh');
	}  
	else{   
// 		$('#multi_colVis').selectpicker();   
// 		columns = MainTable.settings().init().columns; 
// 		console.log(columns)
// 		console.log(colVisible)   
// 		var colVisible = ["Division","SaleOrder","PurchaseOrder"];	
// 		addColOption(columns ) 
		setColVisibleTable(columnsHeader,colVisible);    
		$('#multi_colVis').selectpicker('val', colVisible);        
	}       
	getVisibleColumnsTable(columnsHeader)      
// 	int index = row.MainTable.Columns["Division"].Ordinal;  
// 	console.log(index)  
// SELECTED ONLY   
// 	$("#multi_colVis").each(function(){     
// 	    console.log( $(this).val() )  
// 	});       
// SELECTED ALLOPTION
// 	$("#multi_colVis option").each(function(){     
// 		console.log( $(this).val() )  
// 	});  
}
function addUserStatusOption(data ){ 
	// The DOM way. 
	var sel = document.getElementById("multi_userStatus"); 
	for (i = sel.length - 1; i >= 0; i--) {
		sel.remove(i);
	}  
	let opt = document.createElement('option');
    opt.appendChild(document.createTextNode(i));
	 opt.text  = '\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0[รอจัด Lot]';
	 opt.value = 'รอจัด Lot';
	 sel.appendChild(opt);           
	 opt = document.createElement('option');
     opt.appendChild(document.createTextNode(i));
	 opt.text  = '\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0[ขาย stock]';
	 opt.value = 'ขาย stock'; 
	 sel.appendChild(opt); 
	 opt = document.createElement('option');
     opt.appendChild(document.createTextNode(i));
	 opt.text  = '\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0[รับจ้างถัก]';
	 opt.value = 'รับจ้างถัก'; 
	 sel.appendChild(opt);  
	 opt = document.createElement('option');
     opt.appendChild(document.createTextNode(i));
	 opt.text  = '\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0[Lot ขายแล้ว]';
	 opt.value = 'Lot ขายแล้ว'; 
	 sel.appendChild(opt);  
	 
	 opt = document.createElement('option');
     opt.appendChild(document.createTextNode(i));
	 opt.text  = '\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0[พ่วงแล้วรอสวม]';
	 opt.value = 'พ่วงแล้วรอสวม'; 
	 sel.appendChild(opt);           
	 opt = document.createElement('option');
     opt.appendChild(document.createTextNode(i));
	 opt.text  = '\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0[รอสวมเคยมี Lot]';
	 opt.value = 'รอสวมเคยมี Lot'; 
	 sel.appendChild(opt);      
	var size = data.length;
	for (var i = 0; i < size; i++) {		
		 var resultData = data[i]; 	   
		 opt = document.createElement('option');
	     opt.appendChild(document.createTextNode(i));
		 opt.text  = resultData.userStatus;
		 opt.value = resultData.userStatus;
		 sel.appendChild(opt);          
	}             
	$("#multi_userStatus").selectpicker("refresh");
}    
function addCusNameOption(data ){ 
	// The DOM way. 
	var sel = document.getElementById("multi_cusName"); 
	for (i = sel.length - 1; i >= 0; i--) {
		sel.remove(i);
	}  
	var size = data.length;
	for (var i = 0; i < size; i++) {		   
		 var resultData = data[i]; 	   
// 		 console.log(resultData)
		 var opt = document.createElement('option');
	     opt.appendChild(document.createTextNode(i));
		 opt.text  = resultData.customerName;  
		 opt.value = resultData.customerName;
		 sel.appendChild(opt);          
	}             
	$("#multi_cusName").selectpicker("refresh");
} 
function addCusShortNameOption(data ){ 
	// The DOM way. 
	var sel = document.getElementById("multi_cusShortName"); 
	for (i = sel.length - 1; i >= 0; i--) {
		sel.remove(i);
	}  
	var size = data.length;
	for (var i = 0; i < size; i++) {		
		 var resultData = data[i]; 	   
		 var opt = document.createElement('option');
	     opt.appendChild(document.createTextNode(i));
		 opt.text  = resultData.customerShortName;
		 opt.value = resultData.customerShortName;
		 sel.appendChild(opt);          
	}             
	$("#multi_cusShortName").selectpicker("refresh");
} 
function addColOption(colName ){ 
	// The DOM way.
// 	arrayProblem = colName;
// 	arrayProblem = data;
	var sel = document.getElementById('multi_colVis');
	for (i = sel.length - 1; i >= 0; i--) {
		sel.remove(i);
	}             
	var size = colName.length; 
	for (var i = 0; i < size; i++) {		   
		 let resultData = colName[i]; 	   
		 let opt = document.createElement('option');
		 let title = resultData.title.replace("<br>", " ");
	     opt.appendChild(document.createTextNode(i)); 
		 opt.text  = (i+1)+" : "+title;
		 
		 opt.value = resultData.data;  
		 sel.appendChild(opt);          
	}             
	$("#multi_colVis").selectpicker("refresh");
}  
function addDivisionOption(data ){ 
	// The DOM way. 
	var sel = document.getElementById("multi_division"); 
	for (i = sel.length - 1; i >= 0; i--) {
		sel.remove(i);
	}  
	var size = data.length;
	for (var i = 0; i < size; i++) {		
		 var resultData = data[i]; 	   
		 var opt = document.createElement('option');
	     opt.appendChild(document.createTextNode(i));
		 opt.text  = resultData.division;
		 opt.value = resultData.division;
		 sel.appendChild(opt);          
	}             
	$("#multi_division").selectpicker("refresh");
}  
function addLockColOption(mapsDataHeader,mapsTitleHeader)  { 
	// The DOM way.
// 	arrayProblem = colName;
// 	arrayProblem = data;   
	var sel = document.getElementById('multi_lockCol');
	for (i = sel.length - 1; i >= 1; i--) {
		sel.remove(i);
	}                  
	var size = mapsDataHeader.size;     
	for (var i = 0; i < size; i++) {		
		 var key = getByValue(mapsDataHeader, i);// mapsDataHeader.get(i);    
		 var title = getByValue(mapsTitleHeader, i);//mapsTitleHeader.get(i);
		 var opt = document.createElement('option');
	     opt.appendChild(document.createTextNode(i));
		 opt.text  = (i+1)+" : "+title;
		 opt.value = key;  
		 sel.appendChild(opt);          
	}             
	$("#multi_lockCol").selectpicker("refresh");
} 
function setColVisibleTable(mainCol,colVisible) {     
   let index ;  
   let check ;
   let colName = ''; 
   let tmpArrayShow = [];
   let tmpArrayHide = [];
   let i = 0; 
//    console.log(mainCol , colVisible)
	for (i =  0; i < mainCol.length ; i++) {              
		colName = mainCol[i].data;     
		check = colVisible.includes(colName);  
// 		console.log(colName ,check)    
		if(check === true){ 
			MainTable.column( i ).visible(true  )   ;        
		}    
		else{      
			MainTable.column( i ).visible( false )  
		}    
	}   
	MainTable.columns.adjust().draw( false ); // adjust column sizing and redraw 
} 
function colReOrderBySelect(mainCol,selectedItem) {  
	let arrayCol = []  ; 
	let index = -1;
	let colName = ''; 
	 
	for (i =  0; i < mainCol.length ; i++) {              
		colName = mainCol[i].data;         
		if(colName == selectedItem){ 
			index = i ;    
		}     
		arrayCol.push(i); 
	}   
	var elements = document.getElementsByClassName('monitor_search'); // 
	let filClass = '';
	let filClassParent = '';  
	let filIndex = 0;       
	let countLock = 0;     
	let width = '0px';    
	let widthCol = '0px';
	let check = '';    
	for (i =  0; i < elements.length/2 ; i++) {              
// 		colName = mainCol[i].data;    
		filClass = elements[i];        
		check = mapsDataHeader.get(colName);     
		filIndex = filClass.getAttribute('data-index');  
// 		console.log(filIndex,index)    
		if(filIndex <= index 
// 				&& check !== undefined   
		){       
			filClassParent = filClass.closest('.row-table')      ;  
			widthCol =  filClassParent.style.width;            
			filClassParent.style.left = width;            
		    filClassParent.style.position = "sticky";      
		    width = width.replace("px", "");
		    widthCol = widthCol.replace("px", "");      
		    width = parseFloat(width) + parseFloat(widthCol);
		    width = width+ 'px';  
		    countLock = countLock + 1;
		}  
		else{   
			filClassParent = filClass.closest('.row-table')      ;   
			filClassParent.style.position = "unset";      
		}
	}   
	   new $.fn.dataTable.FixedColumns( MainTable, {     
	        leftColumns: countLock,    
	        rightColumns: 0       
	    } );
	//1st param is insert index = 2 means insert at index 2
	//2nd param is delete item count = 0 means delete 0 elements
	//3rd param is new item that you want to insert  
// 	if (index > -1) {  
// 		arrayCol.splice(index, 1); // 2nd parameter means remove one item only 
// 		arrayCol.splice(0, 0 , index);
// 	}   
	return arrayCol;  
} 
function clearStickyInput() {   
	var elements = document.getElementsByClassName('monitor_search'); // 
	let filClass = '';
	let filClassParent = '';   
	for (i =  0; i < elements.length/2 ; i++) {              
		filClass = elements[i];        
		filClassParent = filClass.closest('.row-table')      ;   
		filClassParent.style.position = "unset";       
	}     
}     
function addSelectOption(data){ 
	// The DOM way.     
	arrayProblem = [];
	arrayProblem = data;
	var sel = document.getElementById('SL_saleNumber');
// 	for (i = sel.length - 1; i >= 0; i--) {
// 		sel.remove(i);
// 	}               
	var size = data.length;
	for (var i = 0; i < size; i++) {		
		 var resultData = data[i]; 	
// 		 console.log(data[i])
		 var opt = document.createElement('option');
	     opt.appendChild(document.createTextNode(i));
		 opt.text  = resultData.saleFullName;
		 opt.value = resultData.saleNumber;   
		 sel.appendChild(opt);          
	}         
} 
// function sumParseFloat(val1,val2){ 
// 	val1 = val1.replace("px", "");
// 	val2 = val2.replace("px", "");
// 	var result = parseFloat(val1) + parseFloat(val2);
// 	result = result.toFixed(4) + 'px';
// 	console.log(result)          
// 	return result  ;​
// }   
function getInputDate(arrTmp,colIdx){      
	var path ="";  
// 	console.log(colIdx)    
// 	if(colIdx == 23){
	if(colIdx == 'cfmPlanLabDateParent'){	
		path = "Detail/getCFMPlanLabDateDetail";
	}    
	else if(colIdx == 'cfmPlanDateParent'){
// 	else if(colIdx == 27){
		path = "Detail/getCFMPlanDateDetail";
	}    
// 	else if(colIdx == 32){
	else if(colIdx == 'deliveryDateParent'){
		path = "Detail/getDeliveryPlanDateDetail"; 
	}  
	else if(colIdx == 'sendCFMCusDateParent'){
		path = "Detail/getSendCFMCusDateDetail"; 
	}   
	if(path != ''){ 
		$.ajax({
		    type : "POST",
		    contentType : "application/json",
		    url : path,
		    data : JSON.stringify(arrTmp),    
		    success : function(data) {
		    	if(data.length == 0){
		    		swal({
			   		    title: 'Warning',
			   		    text: 'No Data found.',
			   		    icon: 'warning',
			   		    timer: 1000,
			   		    buttons: false,
			   		})
		    	}
		    	else{
		    		InputDateTable.clear();
			    	InputDateTable.rows.add(data);
			    	InputDateTable.draw(); 
			    	 $('#modalInputDateForm').modal('show');   
		    	} 
		    },
		    error : function(e) {
		        console.log("ERROR: ", e);
		        swal("Fail", "Please contact to IT", "error");
		    },
		    done : function(e) {
		    	console.log(e);
		    }
		});
	}
	else{
		
	}
}
function getVisibleColumnsTable(columnsHeader) {      
   	mapsDataHeader.clear();    
   	mapsTitleHeader.clear(); 
   	mapsColumnHeader.clear(); 
	var visible_columns = [];        
	var check = '';    
	var count = 0;
	for (var i in columnsHeader) {        
		check = (MainTable.column( i ).visible() === true ? 'visible' : 'not visible');
		if((check == 'visible'      
// 				&& all_columns[i].data == 'DueDate'
				)){         
// 			console.log(columnsHeader[i].data)
			mapsColumnHeader.set(columnsHeader[i].data, columnsHeader[i].type); 
			mapsTitleHeader.set(columnsHeader[i].title, count); 
			mapsDataHeader.set(columnsHeader[i].data, count);
			count = count + 1
		}    
		else{        
// 			unvisible 
		} 
	}     
}     
function saveDefault( ){  
	var json = createJsonData();      
    var  obj = JSON.parse(json);    
	var arrayTmp = [];   
	arrayTmp.push(obj);     
	$.ajax({     
		type: "POST",  
		contentType: "application/json",  
		data: JSON.stringify(arrayTmp),      
		url: "Detail/saveDefault", 
		success: function(data) {   
			if(data.length > 0){
				var bean = data[0];   
				if(bean.iconStatus == 'I'){
					swal({   
						title: "Success",    
					 	text: bean.systemStatus ,
						icon: "info",
						button: "confirm",
   					});  
				}
				else{  
					swal({   
						title: "Warning ",    
					 	text: bean.systemStatus  , 
			   		    icon: 'warning',
			   		    timer: 1000,
			   		    buttons: false,
			   		})
				}  
			}
		},   
		error: function(e) {
			swal("Fail", "Please contact to IT", "error");
		},
		done: function(e) {       
		}   	   
	});   
} 
function loadDefault(){  
	$.ajax({     
		type: "POST",  
		contentType: "application/json",  
// 		data: JSON.stringify(arrayTmp),      
		url: "Detail/loadDefault", 
		success: function(data) {   
			if(data.length > 0){
				var bean = data[0];    
				setSearchDefault(data); 
			}
			else{
				swal({   
					title: "Warning ",    
				 	text: "No search default data."  , 
		   		    icon: 'warning',
		   		    timer: 1000,
		   		    buttons: false,
		   		}) 
			}
		},   
		error: function(e) {
			swal("Fail", "Please contact to IT", "error");
		},
		done: function(e) {       
		}   	   
	});   
} 
function setSearchDefault(data){ 
	var innnerText = data[0];      
	document.getElementById("input_saleOrder").value = innnerText.saleOrder;  
	document.getElementById("input_article").value   = innnerText.articleFG;  
	document.getElementById("input_prdOrder").value  = innnerText.productionOrder;   
	document.getElementById("input_designNo").value  = innnerText.designFG;   
	document.getElementById("input_material").value  = innnerText.materialNo;  
	document.getElementById("input_labNo").value     = innnerText.labNo; 
	document.getElementById("input_PO").value     = innnerText.purchaseOrder;
	var saleCreateArray = innnerText.saleOrderCreateDate.split(' - ') ;
	if(saleCreateArray.length == 1){ $('#input_saleOrderDate').val('');     }
	else{
		$("#input_saleOrderDate").data('daterangepicker').setStartDate(saleCreateArray[0] );
		$("#input_saleOrderDate").data('daterangepicker').setEndDate(saleCreateArray[1]);  
	} 
	var prdOrderArray = innnerText.productionOrderCreateDate.split(' - ') ;
	if(prdOrderArray.length == 1){ $('#input_prdOrderDate').val('');     }
	else{
		$("#input_prdOrderDate").data('daterangepicker').setStartDate(prdOrderArray[0]);
		$("#input_prdOrderDate").data('daterangepicker').setEndDate(prdOrderArray[1]); 
	}     
	var dueDateArray = innnerText.dueDate.split(' - ') ;   
	if(dueDateArray.length == 1){ $('#input_dueDate').val('');     }
	else{   
		$("#input_dueDate").data('daterangepicker').setStartDate(dueDateArray[0]);
		$("#input_dueDate").data('daterangepicker').setEndDate(dueDateArray[1]);
	}       
	var saleStatus = innnerText.saleStatus;
	if(saleStatus == ''){ 
		document.getElementById("rad_all").checked = true;
	}
	else if(saleStatus == 'O'){ 
		document.getElementById("rad_inProc").checked = true;
	}
	else{//saleStatus == 'C'
		document.getElementById("rad_closed").checked = true;
	}   
	 
// 	console.log(innnerText.SaleNumber,innnerText.DeliveryStatus)
	document.getElementById('SL_saleNumber').value=innnerText.saleNumber;
	document.getElementById('SL_delivStatus').value=innnerText.deliveryStatus;
	
	 document.getElementById("check_DM").checked = false;
	 document.getElementById("check_EX").checked = false;
	 document.getElementById("check_HW").checked = false;
	 var distChannel = innnerText.distChannel.split('|');   
	 for( let i = 0 ;i < distChannel.length ; i++){
		 if(distChannel[i] == 'DM'){ document.getElementById("check_DM").checked = true; }
		 else if(distChannel[i] == 'EX'){ document.getElementById("check_EX").checked = true;  }
		 else if(distChannel[i] == 'HW'){ document.getElementById("check_HW").checked = true;  }
	 }             
	 var customer = innnerText.customerName.split('|'); 
	 var customerShort = innnerText.customerShortName.split('|'); 
	 var userStatusList = innnerText.userStatus.split('|');
	 var divisionList = innnerText.division.split('|'); 
	$('#multi_cusName').selectpicker('val', customer);    
	$('#multi_cusShortName').selectpicker('val', customerShort);    
	$('#multi_userStatus').selectpicker('val', userStatusList);
	$('#multi_division').selectpicker('val', divisionList);      
	$('#multi_cusName').selectpicker('refresh');     
	$('#multi_cusShortName').selectpicker('refresh');     
	$('#multi_userStatus').selectpicker('refresh'); 
	$('#multi_division').selectpicker('refresh');  
}
function getSelectOptions(value) {
    var select = $(selectOptionDepText);
    if (value) { select.val(value).find(':selected').attr('selected', true); }
    return select.html()   
  } 
function configDepSelectOption(data) {
//     var select = $("<select><option value='Select'>Select</option><option value='WaitAnswer'>ส่งแล้วรอตอบ</option><option value='OK'>OK</option><option value='NoK'>NoK</option><option value='Cancel'>Cancel</option></select>");
    let htmlSelectOption = "";
    htmlSelectOption += "<select>";    
    htmlSelectOption +=  "<option value='Select' selected>Select</option>";
    for(let i = 0 ; i < data.length; i++) {        
    	htmlSelectOption += "<option value='"+data[i].delayedDepartment+"'>"+(i+1)+":"+data[i].delayedDepartment+"</option>";
	} 
    htmlSelectOption += "</select>"; 
    return htmlSelectOption;
  } 
function getByValue(map, searchValue) {
  for (let [key, value] of map.entries()) {
    if (value === searchValue)
      return key;
  }  
}     
function setValueWithFieldName(fieldName, rowData,value) {      
	  if(fieldName == 'switchRemark')       {rowData.switchRemark = value ;  }
	  else if(fieldName == 'pcRemark')      {rowData.pcRemark = value  ;  }
	  else if(fieldName == 'replacedRemark'){rowData.replacedRemark = value  ; }
	  else if(fieldName == 'stockRemark')   {rowData.stockRemark = value  ; } 
	  else if(fieldName == 'stockLoad')     {rowData.stockLoad = value  ; } 
	  else if(fieldName == 'delayedDep')    {rowData.delayedDepartment = value  ; } 
	  else if(fieldName == 'causeOfDelay')  {rowData.causeOfDelay = value  ; }  
	  else if(fieldName == 'sendCFMCusDate'){rowData.sendCFMCusDate = value  ; }
	} 
function checkIfDigitOnly(_string) {
	var check = true;   
// 	const pattern = /^[0-9]+$/;     
// console.log(_string)
	const pattern = /^[1-9]\d*(\.\d+)?$/;         
// console.log(pattern.test(_string))  
    if(pattern.test(_string)) {
//         console.log("String contains only numbers")

    	check = true;    
    }
    else {
//         console.log("String does not contain only numbers") 
    	check = false;
    } 
    return check;
}function getSwitchProdOrderListByRowProd(arrayTmp) {    
	$.ajax({   
		type: "POST",  
		contentType: "application/json",  
		data: JSON.stringify(arrayTmp),          
		url: "Detail/getSwitchProdOrderListByRowProd", 
		async: false,
		success: function(data) {  
			if(data.length > 0){  
				var bean = data[0];  
				if(bean.iconStatus == 'E'){ 
					swal({   
						title: "Warning ",        
					 	text: bean.systemStatus  , 
			   		    icon: 'warning',
// 			   		    timer: 2000,  
// 			   		    buttons: false,   
			   		    button: "confirm",
			   		})
				}
				else{       
			   		SWMainTable.clear();              
					SWMainTable.rows.add(data);     
					SWMainTable.columns.adjust().draw(false); 
					$('#modalRemarkSW').modal('show');  
// 					swal({
// 						title: "Success",    
// 					 	text: bean.systemStatus ,   
// 						icon: "info",
// 						button: "confirm",   
//    					});  
				}  
				 
			}     
		},   
		error: function(e) {
			swal("Fail", "Please contact to IT", "error");
		},
		done: function(e) {       
		}   	
	});       
}    
</script>
</html>