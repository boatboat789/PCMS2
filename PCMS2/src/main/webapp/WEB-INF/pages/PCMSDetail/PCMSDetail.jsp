 
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
		<div class="col-12 col-sm-12 col-md-12 col-lg-12 col-xl-12 " style="    font-size: 12.5px;padding: 0px;margin: 0px 0px;    " >        
				<div class="table-responsive ">             
					<table id="MainTable" class="table compact table-bordered table-striped text-center" style="font width:100%;    margin: 0px !important;zoom:95%;">
				        <thead>  
				            <tr>     
								<th class="row-table" style="vertical-align: middle;">DIV </th>
				                <th class="row-table" style="vertical-align: middle;">SO No.</th>
				                <th class="row-table" style="vertical-align: middle;">SO Line </th>
				                <th class="row-table" style="vertical-align: middle;">Cust.(Name4)</th> 
				                <th class="row-table" style="vertical-align: middle;">SO Date </th>   
				                <th class="row-table" style="vertical-align: middle;">P/O </th>
				                <th class="row-table" style="vertical-align: middle;">Material </th>	
				                <th class="row-table" style="vertical-align: middle;">Customer<span class="c"style="display: block;"> Mat.No.</span> </th> 
				                <th class="row-table" style="vertical-align: middle;">Price <span class="c"style="display: block;">(THB)</span></th>
				                <th class="row-table" style="vertical-align: middle;">Unit </th>
				                <th class="row-table" style="vertical-align: middle;">Order<span class="c"style="display: block;">Qty.</span> </th>
				                <th class="row-table" style="vertical-align: middle;">Remain<span class="c"style="display: block;">Qty.</span> </th>
				                <th class="row-table" style="vertical-align: middle;">Remain<span class="c"style="display: block;">Amt.(THB)</span> </th>
				                <th class="row-table" style="vertical-align: middle;">จำนวนต่อ LOT </th>
				                <th class="row-table" style="vertical-align: middle;">Volume FG Amt </th>
				                <th class="row-table" style="vertical-align: middle;">Grade </th>  
				                <th class="row-table" style="vertical-align: middle;">GR Qty </th>   
				                    
				                <th class="row-table" style="vertical-align: middle;">จำนวน (FG) <span class="c"style="display: block;">KG/MR/YD</span> </th> 
				                <th class="row-table" style="vertical-align: middle;">จำนวนที่ส่ง </th> 
				                <th class="row-table" style="vertical-align: middle;">Amt.<span class="c"style="display: block;">(THB)</span> </th>
				                <th class="row-table" style="vertical-align: middle;">Due Cus. </th> 
				                <th class="row-table" style="vertical-align: middle;">Due Date </th>  
				                <th class="row-table" style="vertical-align: middle;">Lab No </th> 
				                <th class="row-table" style="vertical-align: middle;">Lab Status</th> 
				                <th class="row-table" style="vertical-align: middle;">Lot </th>   
			                 	<th class="row-table" style="vertical-align: middle;">DyePlan </th> 
			                 	<th class="row-table" style="vertical-align: middle;">DyeActual </th> 
				                <th class="row-table" style="vertical-align: middle;">วันนัด<span class="c"style="display: block;">CFM LAB</span> </th> 
				                <th class="row-table" style="vertical-align: middle;">วันส่ง<span class="c"style="display: block;"> CFM LAB</span> </th> 
				                <th class="row-table" style="vertical-align: middle;">วันที่ลูกค้า <span class="c"style="display: block;"> ตอบ LAB</span> 	 </th> 
				                <th class="row-table" style="vertical-align: middle;">TK CFM </th> 
				                <th class="row-table" style="vertical-align: middle;">วันที่นัด CFM </th>  
				               	<th class="row-table" style="vertical-align: middle;">Send CFM <span class="c"style="display: block;">Cus. Date</span>  </th>    
				                   
				                <th class="row-table" style="vertical-align: middle;">วันที่ส่ง CFM จริง</th>  
				                <th class="row-table" style="vertical-align: middle;">User Status </th> 
								<th class="row-table" style="vertical-align: middle;">CFM Detail   </th> 
				                <th class="row-table" style="vertical-align: middle;">CFM Number </th> 
				                <th class="row-table" style="vertical-align: middle;">CFM Remark </th> 
								<th class="row-table" style="vertical-align: middle;">Delivery<span class="c"style="display: block;">(วันที่นัดส่ง)</span> </th> 
				                <th class="row-table" style="vertical-align: middle;">Bill Date </th> 
				                <th class="row-table" style="vertical-align: middle;">EFFECT </th>     
				                <th class="row-table" style="vertical-align: middle;">Cause <span class="c"style="display: block;">of Delay</span>  </th>    
				                <th class="row-table" style="vertical-align: middle;">Delayed <span class="c"style="display: block;">Department</span>  </th> 
				                <th class="row-table" style="vertical-align: middle;">PC Remark </th>  
				                <th class="row-table" style="vertical-align: middle;">Replaced Remark </th>
				                    
				                <th class="row-table" style="vertical-align: middle;">SwitchRemark </th>          
				                <th class="row-table" style="vertical-align: middle;">StockRemark </th>  
				                <th class="row-table" style="vertical-align: middle;">StockLoad </th>  
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
<!-- 		<div id="wrapper-bot" class="row">   -->
		       
<!-- 		</div>        -->       
    <jsp:include page="/WEB-INF/pages/config/footer.jsp"></jsp:include>  
	<jsp:include page="/WEB-INF/pages/config/PCMSDetailModal/ColumnSetting/ColumnSetting.jsp"></jsp:include> 
	<jsp:include page="/WEB-INF/pages/config/PCMSDetailModal/InputDateDetail/modalMain.jsp"></jsp:include>  
	<jsp:include page="/WEB-INF/pages/config/PCMSDetailModal/LockColumnDetail/LockColumnDetail.jsp"></jsp:include>   
	<jsp:include page="/WEB-INF/pages/config/PCMSDetailModal/RemarkSWModal/modalMain.jsp"></jsp:include>  
</body>               
<script src="<c:url value="/resources/js/DatatableSort.js" />"></script>  
<script src="<c:url value="/resources/js/General.js" />"></script>     
<style>        
.p-r-15 {padding-right: 15px !important; }      
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
var caseDupli;
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
// 	$('#MainTable thead tr').clone(true).appendTo('#MainTable tfoot');
// 	$('#MainTable tfoot th').each( function (i) {
// 		var title = $(this).text();      	      
// 		$(this).html( '<input type="text" class="monitor_search" style="width:100%" data-index="' + i + '" '  
// 				+'placeholder="'+title+'" '    
// 				+ '/>');   
//     } );  
	    MainTable = $('#MainTable').DataTable({   
			scrollX: true,                         
	        scrollY: '55vh' , //ขนาดหน้าจอแนวตั้ง         
	        scrollCollapse: true,    
	 	   	orderCellsTop : true,           
	// 		orderClasses : false,         	          
	// 		 select : true,  	
			select : {             
				style: 'os',          
			 	selector: 'td:not(.status)'  // .status is class        
	  		},         
			pageLength:	 1000,	 
		    lengthChange : false,  
	// 		lengthMenu: [[1000, -1], [1000, "All"]], 
// 	 		colReorder: true,           
			colReorder: {           
// 			   realtime: true      
			   realtime: false,   
			   enable: false
			},    
	// 		autoFill: { 
	// 			alwaysAsk: true,
	// 	 		horizontal: false,
	// 	 		focus: 'click',  
	// 	 	 	columns: [ 22, 27, 31 ]
	// 	    },      
	 	   	columns :                    
	 	   		[      
				    {"data" : "Division" ,        "title":"DIV"          },             //0
				    {"data" : "SaleOrder" ,       "title":"SO No."           },         //1
				    {"data" : "SaleLine"   ,      "title":"SO Line"          },         //2
				    {"data" : "CustomerShortName","title":"Cust.(Name4)"  },            //3
				    {"data" : "SaleCreateDate" ,  "title":"SO Date"   ,'type': 'date-euro'   },           //4 
				    {"data" : "PurchaseOrder" ,   "title":"P/O" },     								      //5 
				    {"data" : "MaterialNo" ,      "title":"Material" },     
				    {"data" : "CustomerMaterial" ,"title":"Customer Mat.No." },                           //7
				    {"data" : "Price" ,           "title":"Price (THB)",'type': 'num' }, 
				    {"data" : "SaleUnit" ,        "title":"Unit" },                                      //9
				    {"data" : "SaleQuantity" ,    "title":"Order Qty.",'type': 'num' },                  //10
				    {"data" : "RemainQuantity" ,  "title":"Remain Qty." ,'type': 'num'},                 //11
				    {"data" : "RemainAmount" ,    "title":"Remain Amt.(THB)",'type': 'num' }, 
				    {"data" : "Volumn" ,          "title":"Volume FG",'type': 'num' },                   //13
				    {"data" : "VolumnFGAmount" ,  "title":"Volume FG Amt(THB)",'type': 'num' },          //14
				    {"data" : "Grade" ,           "title":"Grade" },                                     //15
				    {"data" : "GRQuantity" ,      "title":"GR Qty<br>KG/MR/YD" } ,                       //16
				    {"data" : "BillSendWeightQuantity" ,"title":"Bill Qty (Class)<br>KG/MR/YD" },        //17
				    {"data" : "BillSendQuantity" ,"title":"Bill Qty" ,'type': 'num'},                    //18
				    {"data" : "OrderAmount" ,     "title":"Amt.(THB)",'type': 'num' },                   //19
				    {"data" : "CustomerDue" ,     "title":"Due Cus.",'type': 'date-euro' },              //20
				    {"data" : "DueDate" ,         "title":"Due Date",'type': 'date-euro' },              //21  
				    {"data" : "LabNo",			  "title":"Lab No" },                                    //22
				    {"data" : "LabStatus",		  "title":"Lab Status" },                                //23
				    {"data" : "LotNo",            "title":"Lot" },                                       //24  
				    {"data" : "DyePlan","title":"Dye [Plan]" },                                          //25 
				    {"data" : "DyeActual","title":"Dye [Actual]" },                                      //26 
				    {"data" : "CFMPlanLabDate","title":"Plan CFM LAB",'type': 'date-euro' },             //27
				    {"data" : "CFMActualLabDate","title":"Send CFM LAB" ,'type': 'date-euro'},     	     //28 
				    {"data" : "CFMCusAnsLabDate","title":"Answer LAB",'type': 'date-euro'},              //29
				    {"data" : "TKCFM","title":"TK CFM"},                       						     //30 
				    {"data" : "CFMPlanDate","title":"Plan CFM Date",'type': 'date-euro'},                //31  
				    {"data" : "SendCFMCusDate","title":"Send CFM Cus Date",'type': 'date-euro'},         //32 <-------------
       
 				    {"data" : "CFMDateActual","title":"Actual CFM Date",'type': 'date-euro'},          
// 				    {"data" : "CFMSendDate","title":"Actual CFM Date",'type': 'date-euro'},              //33 
				    {"data" : "UserStatus","title":"User Status"},                                       //34
					{"data" : "CFMDetailAll","title":"CFM Detail"},                                      //35   
					{"data" : "CFMNumberAll","title":"CFM Number"},                                      //36
					{"data" : "RollNoRemarkAll","title":"CFM Remark"},                                //37<------------- 09/08/2022
				    {"data" : "DeliveryDate","title":"Plan Delivery Date",'type': 'date-euro'},       //38
// 				    {"data" : "ShipDate","title":"Bill Date",'type': 'date-euro'},                    //39 
				    {"data" : "LotShipping","title":"Bill Date",'type': 'date-euro'},                    //39 Replaced ShipDate
				    {"data" : "Remark","title":"EFFECT"},                                             //40
				    {"data" : "CauseOfDelay","title":"Cause of Delay"},                               //41<-------------
				    {"data" : "DelayedDepartment","title":"Delayed Department"},                      //42<-------------
					{"data" : "PCRemark","title":"PCRemark"},                                         //43
				    {"data" : "ReplacedRemark","title":"ReplacedRemark"},                             //44
				    {"data" : "SwitchRemark","title":"SwitchRemark"},                                 //45
				    {"data" : "StockRemark","title":"StockRemark"},                                   //46
				    {"data" : "StockLoad","title":"StockLoad"},                                       //47
     
			],        	                
            columnDefs :  [	
			    { targets: [ 1 ],
		          orderData: [ 1, 2,24 ]  
		        },   	    
				{ targets : [ 4,20 , 21,28 ,39 ],                        
				  	  className : 'dt-custom-td80',    	        
				  	  type: 'date-euro'  
					} ,                              
				{ targets : [ 11,12,13,14,22,24,29 ,33 ],                        
				  	  className : 'dt-custom-td100',    
				  	  type: 'string'     
				} ,   
				{ targets : [ 16,17 ],                        
				  	  className : 'dt-custom-td160',    
				  	  type: 'string'     
				} ,                 
// 				{ targets : [ 23 ],                            
// 				  	  className : 'dt-custom-td120',    
// 				  	  type: 'string'            
// 				} ,     
				{ targets : [ 27  ],                      
			  	  className : 'CFMPlanLabDateParent dt-custom-td80',       
			  	  type: 'date-euro'     
				} ,   
				{ targets : [ 31 ],                      
			  	  className : 'CFMPlanDateParent dt-custom-td80',       
			  	  type: 'date-euro'  
				} ,  
				{ targets : [ 32 ],                       
				  	  className : 'SendCFMCusDateParent dt-custom-td80',       
				  	  type: 'date-euro'  
					} ,
				{ targets : [ 38 ],                      
			  	  className : 'DeliveryDateParent dt-custom-td100',       
			  	  type: 'date-euro'  
				} ,   
				{ targets : [3 ,6,23,34,36 ],             	     
			  	  	className : 'dt-custom-td140',      
			  	  	type: 'string'   
					} ,               
				{ targets : [5  ],                    
			  	  	className : 'dt-custom-td160',      
			  	  	type: 'string'   
					} ,            
				{ targets : [ 7 ,42 ],                    
			  	  	className : 'dt-custom-td240',       
			  	  	type: 'string'   
				} ,       
				{ targets : [  35,37 ],                          
			  	  	className : 'dt-custom-td300',         
			  	  	type: 'string'     
					} ,            
				{ targets : [  40,41,43,44 ,46,47],                       
			  	  	className : 'dt-custom-td450 p-r-15',      
			  	  	type: 'string'  
					} ,   
					{ targets:[1]  ,       
						render: function (data, type, row) {	     
							let html = '<div name="n_'+row.SaleOrder+' data-toggle="tooltip" title="' + row.TypePrd + '"> '+row.SaleOrder+'</div>'
							return  html; 
					   	  }    
					},  
					{ targets:[2]  ,            
						render: function (data, type, row) {	     
							let html = '<div name="n_'+row.SaleLine+' data-toggle="tooltip"  title="' + row.TypePrd + '"> '+row.SaleLine+'</div>'
							return  html; 
					   	  }    
					},  
				{ targets : [ 11 ],            
			   	  render: function (data, type, row) {	   
						var htmlEx = '';   
			   			if(soLineTmp == '' && soTmp == ''  ){
			   				soLineTmp = row.SaleLine;     
				   			soTmp = row.SaleOrder;   
				   			caseDupli = 0;
				   			htmlEx = '<div style="visibility: visible;color: red; font-weight: bolder;">'+row.RemainQuantity+' </div>'; 
				   		}  
			   			else if(soLineTmp == row.SaleLine && soTmp == row.SaleOrder  ){
			   				caseDupli = 1;
				   			htmlEx = '<div style="visibility: hidden;color: red; font-weight: bolder;">'+row.RemainQuantity+' </div>'; 
				   		}   
				   		else{      
				   			soLineTmp = row.SaleLine;     
				   			soTmp = row.SaleOrder;   
				   			caseDupli = 2;
				   			htmlEx = '<div style="visibility: visible;color: red; font-weight: bolder;">'+row.RemainQuantity+' </div>'; 
				   		}  
						return  htmlEx
				   	  }     
						} ,  
				{ targets : [ 12 ],        
			   	  render: function (data, type, row) {	   
	   					var htmlEx = '';  
		   				if(caseDupli == 0  ){ 
				   			htmlEx = '<div style="visibility: visible;color: red; font-weight: bolder;">'+row.RemainAmount+' </div>'; 
				   		}  
			   			else if(caseDupli == 1  ){ 
				   			htmlEx = '<div style="visibility: hidden;color: red; font-weight: bolder;">'+row.RemainAmount+' </div>'; 
				   		}   
				   		else{          
				   			htmlEx = '<div style="visibility: visible;color: red; font-weight: bolder;">'+row.RemainAmount+' </div>'; 
				   		}  
						return  htmlEx
				   	  }    
					} ,  
				{ targets : [ 19 ],        
				   	  render: function (data, type, row) {	   
				   		var htmlEx = ''; 
		   				if(caseDupli == 0  ){ 
				   			htmlEx = '<div style="visibility: visible;color: red; font-weight: bolder;">'+row.OrderAmount+' </div>'; 
				   		}  
			   			else if(caseDupli == 1  ){ 
				   			htmlEx = '<div style="visibility: hidden;color: red; font-weight: bolder;">'+row.OrderAmount+' </div>'; 
				   		}      
				   		else{          
				   			htmlEx = '<div style="visibility: visible;color: red; font-weight: bolder;">'+row.OrderAmount+' </div>'; 
				   		}   
						return  htmlEx
				   	  }    
					} , 
					         	
					{ targets:[24]  ,       
						render: function (data, type, row) {	     
							let html = '<div name="n_'+row.LotNo+' data-toggle="tooltip" title="' + row.TypePrd + '"> '+row.LotNo+'</div>'
							return  html; 
					   	  }    
					},  
				{ targets : [ 27 ],    
					  orderable: false,    
					   	  render: function (data, type, row) {
				   			var htmlEx = ''     
				   				if(row.LotNo == "รอจัด Lot"   || row.LotNo  == "ขาย stock" ||row.LotNo == "รับจ้างถัก"	||row.LotNo == "Lot ขายแล้ว"
			   					|| row.LotNo == "พ่วงแล้วรอสวม"	|| row.LotNo == "รอสวมเคยมี Lot"){ 
									htmlEx = ''; 
								}
							else{
								htmlEx = '<input class="form-control CFMPlanLabDateInput" style=" cursor: pointer; padding: 4px 2px;font-size: 12.5px"  name="CFMPlanLabDate" type="text"  value = "' + row.CFMPlanLabDate+ '" autocomplete="off" >';
							}
							return  htmlEx;
					   	  }            
						} ,
				{ targets : [ 31 ],    
				  orderable: false,
				   	  render: function (data, type, row) {	
// 				   		var htmlEx = '' 
// 				   			if(row.LotNo  == "รอจัด Lot"	 || row.LotNo  == "ขาย stock"	){ 
// 								htmlEx = ''; 
// 							}
// 						else{
// 							htmlEx = '<input class="form-control CFMPlanDateInput" style=" cursor: pointer; padding: 4px 2px;font-size: 12.5px"  name="CFMPlanDate" type="text"  value = "' + row.CFMPlanDate+ '" autocomplete="off" >';
// 						} 
						return  row.CFMPlanDate 
				   	  }         
					} ,    
				{ targets : [ 32 ],      
				  orderable: false,
   	  			  render: function (data, type, row) {	
 				   		var htmlEx = ''  
			   			if(row.LotNo == "รอจัด Lot"   || row.LotNo  == "ขาย stock" ||row.LotNo == "รับจ้างถัก"	||row.LotNo == "Lot ขายแล้ว"
	   					|| row.LotNo == "พ่วงแล้วรอสวม"	|| row.LotNo == "รอสวมเคยมี Lot"){ 
							htmlEx = row.SendCFMCusDate ; 
						}
						else{
							htmlEx = '<input class="form-control SendCFMCusDateInput" style=" cursor: pointer; padding: 4px 2px;font-size: 12.5px"  name="SendCFMCusDate" type="text"  value = "' 
							+ row.SendCFMCusDate
							+ '" autocomplete="off" >';
						} 
						return  htmlEx
			   	  }    
				} ,        
				{ targets : [ 38 ],    
				  orderable: false,     
			   	  render: function (data, type, row) {	     
					var htmlEx = ''   
			   		if(row.LotNo  == "รอจัด Lot"	 || row.LotNo  == "ขาย stock" ||row.LotNo == "รับจ้างถัก"	||row.LotNo == "Lot ขายแล้ว"
			   			|| row.LotNo == "พ่วงแล้วรอสวม"	|| row.LotNo == "รอสวมเคยมี Lot"){ 
						htmlEx = ''; 
					}
					else{   
						htmlEx = '<input class="form-control DeliveryDateInput" style=" cursor: pointer; padding: 4px 2px;font-size: 12.5px"  name="DeliveryDate" type="text"  value = "' + row.DeliveryDate+ '" autocomplete="off" >';
					}
					return  htmlEx           
					}	          
				},      
				{ targets : [ 41 ],     
			   	  render: function (data, type, row) {	 
				   		var htmlEx = ''      
			   			if( row.LotNo  == "รอจัด Lot" || row.LotNo == "ขาย stock" ||row.LotNo == "รับจ้างถัก" ||
			   					row.LotNo == "Lot ขายแล้ว"	|| row.LotNo  == ""        || row.LotNo == "พ่วงแล้วรอสวม"	|| 
			   					row.LotNo == "รอสวมเคยมี Lot" ){ 
			   				htmlEx = row.CauseOfDelay; 
						}  
// 			   			else  if( row.TypePrd == "OrderPuang"  ){ 
// 							htmlEx = row.CauseOfDelay	; 
// 						}        
						else if(     
		   					( row.TypePrd == "Replaced" && row.TypePrdRemark == "MAIN")  ||  
		   					( row.TypePrd == "MAIN" && row.TypePrdRemark == "MAIN") || 
		   						row.TypePrdRemark == "SUB" || 
		   						row.TypePrdRemark == "" ||
	   						  	row.TypePrd == "OrderPuang"){ 
		   					htmlEx = '<input data-search="' + row.CauseOfDelay+ '" class="form-control CauseOfDelayInput" style=" cursor: pointer; padding: 4px 2px;font-size: 12.5px"maxlength="200"  name="CauseOfDelayRemark" type="text"  value = "' + row.CauseOfDelay+ '" autocomplete="off" >'; 
						}      
						else{   
							htmlEx = '<input data-search="' + row.CauseOfDelay
							+ '" class="form-control CauseOfDelayInput" style=" cursor: pointer; padding: 4px 2px;font-size: 12.5px"maxlength="200"  name="CauseOfDelayRemark" type="text"  value = "' 
							+ row.CauseOfDelay+ '" autocomplete="off" >'; 
						}
// 						htmlEx = '<input class="form-control CauseOfDelayInput" style=" cursor: pointer; padding: 4px 2px;font-size: 12.5px"maxlength="200"  name="CauseOfDelayRemark" type="text"  value = "' + row.CauseOfDelay+ '" autocomplete="off" >'; 
						return  htmlEx      
						}         
				}  ,   
				{ targets : [ 42 ],       
			   	  render: function (data, type, row, meta) {	    
			   		var htmlEx = ''         
				   		 
		   			if( row.LotNo  == "รอจัด Lot" || row.LotNo == "ขาย stock" ||row.LotNo == "รับจ้างถัก" ||
		   					row.LotNo == "Lot ขายแล้ว"	|| row.LotNo  == ""        || row.LotNo == "พ่วงแล้วรอสวม"	|| 
		   					row.LotNo == "รอสวมเคยมี Lot" ){  
		   				htmlEx = row.DelayedDepartment; 
					}
// 		   			else if( row.TypePrd == "OrderPuang" ){ 
// 						htmlEx = row.DelayedDepartment; 
// 					}    
		   			else if(    
	   					( row.TypePrd == "Replaced" && row.TypePrdRemark == "MAIN")  || 
	   					( row.TypePrd == "MAIN" && row.TypePrdRemark == "MAIN"|| 
  							row.TypePrdRemark == "SUB" || row.TypePrdRemark == "") ||
  							row.TypePrd == "OrderPuang"){ 
						 htmlEx = '<select data-search="' + row.DelayedDepartment+ 
								 '" class="form-control DelayedDepInput" data-col="' + meta.col + '">' + 
								 getSelectOptions(data) + '</select>';    
					} 
		   			                    
					else{   
						htmlEx = '<select data-search="' + row.DelayedDepartment+ 
								 '" class="form-control DelayedDepInput" data-col="' + meta.col + '">' + 
								 getSelectOptions(data) + '</select>';     
					}  
			   		return  htmlEx      
					}       
				}  ,      
				{ targets : [ 43 ],     
				   	  render: function (data, type, row) {	     
				   		var htmlEx = ''    
						htmlEx = '<input class="form-control PCRemarkInput" style=" cursor: pointer; padding: 4px 2px;font-size: 12.5px"maxlength="200"  name="PCRemark" type="text"  value = "' + row.PCRemark+ '" autocomplete="off" >'; 
						return  htmlEx      
						}       
					}       
				,         
				{ targets : [ 44 ],     
			   	  render: function (data, type, row) {	     
			   		var htmlEx = '';
			   		if( ( row.TypePrd == "Replaced" && row.TypePrdRemark == "MAIN")  || ( row.TypePrd == "MAIN" && row.TypePrdRemark == "MAIN") ){ 
// 						htmlEx = '<button type="button" class="btn btn-warning btn-cancelSW">ยกเลิกโยกขาย</button>'; 
						htmlEx = '<input class="form-control ReplacedRemarkInput" style=" cursor: pointer; padding: 4px 2px;font-size: 12.5px"maxlength="200"  name="ReplacedRemark" type="text"  value = "' + row.ReplacedRemark+ '" autocomplete="off" >'; 
					}         
			   		else if(row.LotNo  == "รอจัด Lot"	  || row.LotNo == "ขาย stock" ||row.LotNo == "รับจ้างถัก"	||row.LotNo == "Lot ขายแล้ว"	|| row.LotNo  == ""|| row.LotNo == "พ่วงแล้วรอสวม"	|| row.LotNo == "รอสวมเคยมี Lot" ){ 
			   			htmlEx = '<input class="form-control ReplacedRemarkInput" style=" cursor: pointer; padding: 4px 2px;font-size: 12.5px"maxlength="200"  name="ReplacedRemark" type="text"  value = "' + row.ReplacedRemark+ '" autocomplete="off" >'; 
					} 
					else if(row.TypePrd == "Switch" || row.TypePrd == "Replaced" || row.TypePrd == "OrderPuang" || row.TypePrdRemark == "SUB" || row.TypePrdRemark == ""){ 
						htmlEx = ''; 
					}         
					else{   
						htmlEx = '<input class="form-control ReplacedRemarkInput" style=" cursor: pointer; padding: 4px 2px;font-size: 12.5px"maxlength="200"  name="ReplacedRemark" type="text"  value = "' + row.ReplacedRemark+ '" autocomplete="off" >'; 
					}
			   		return  htmlEx          
					}       
				} ,     
				{ targets : [ 45 ],         
			   	  	render: function (data, type, row) {	          
					var htmlEx = ''           
					if( ( row.TypePrd == "Switch" && row.TypePrdRemark == "MAIN")  || ( row.TypePrd == "MAIN" && row.TypePrdRemark == "MAIN") ){ 
// 						htmlEx = '<button type="button" class="btn btn-warning btn-cancelSW">ยกเลิกโยกขาย</button>'; 
						htmlEx = '<input class="form-control SwitchRemarkInput" style=" cursor: pointer; padding: 4px 2px;font-size: 12.5px"maxlength="200"  name="StockRemark" type="text"  value = "' + row.SwitchRemark+ '" autocomplete="off" >'; 
					}        
					else if(row.TypePrd == "Replaced" || row.TypePrd == "OrderPuang"
							||row.LotNo == "รอจัด Lot"	 || row.LotNo == "ขาย stock" ||row.LotNo == "รับจ้างถัก"	||row.LotNo == "Lot ขายแล้ว" || row.LotNo == "พ่วงแล้วรอสวม"	|| row.LotNo == "รอสวมเคยมี Lot"	
							|| row.TypePrdRemark == "SUB" || row.TypePrdRemark == ""){ 
						htmlEx = ''; 
					}     
					else{   
						htmlEx = '<input class="form-control SwitchRemarkInput" style=" cursor: pointer; padding: 4px 2px;font-size: 12.5px"maxlength="200"  name="StockRemark" type="text"  value = "' + row.SwitchRemark+ '" autocomplete="off" >'; 
					}
					return  htmlEx      
					}       
				} ,    
				{ targets : [ 46 ],     
			   	  	render: function (data, type, row) {	     
					var htmlEx = ''    
					htmlEx = '<input class="form-control StockRemarkInput" style=" cursor: pointer; padding: 4px 2px;font-size: 12.5px"maxlength="200"  name="StockRemark" type="text"  value = "' + row.StockRemark+ '" autocomplete="off" >'; 
					return  htmlEx      
					}         
				} ,        { targets : [ 47 ],     
			   	  	render: function (data, type, row) {	     
						var htmlEx = ''    
						htmlEx = '<input class="form-control StockLoadInput" style=" cursor: pointer; padding: 4px 2px;font-size: 12.5px"maxlength="200"  name="StockLoadInput" type="text"  value = "' + row.StockLoad+ '" autocomplete="off" >'; 
						return  htmlEx      
						}       
					} ,          
			], 
			createdRow : function(row, data, index) {
    // 				$('td', row).eq(22).addClass('bg-color-azure');  
	//   	        $('td', row).eq(27).addClass('bg-color-azure');        
	//   	        $('td', row).eq(31).addClass('bg-color-azure'); 
	     		
// 				if (data["TypePrd"] == "OrderPuang" ) { $(row).addClass('bg-color-azure'); } 
	// 			if (index == 16 || index == 21 || index == 22 ) { $(row).addClass('bg-color-azure'); } 
	// 			else if (data["OperationStartDate"] != "") { $(row).addClass('bg-start-im'); } 
	
				if(mapsDataHeader.size != 0){       
					if (data["TypePrd"] == "OrderPuang" ) { 	
						$('td', row).eq(mapsDataHeader.get("SaleOrder")).addClass('bg-orderpuang');
						$('td', row).eq(mapsDataHeader.get("SaleLine")).addClass('bg-orderpuang');
						$('td', row).eq(mapsDataHeader.get("LotNo")).addClass('bg-orderpuang');
					}  
					else if (data["TypePrd"] == "Switch" ) { 	
						$('td', row).eq(mapsDataHeader.get("SaleOrder")).addClass('bg-switch');      
						$('td', row).eq(mapsDataHeader.get("SaleLine")).addClass('bg-switch');
						$('td', row).eq(mapsDataHeader.get("LotNo")).addClass('bg-switch');     
					}
					else if (data["TypePrd"] == "Replaced" ) { 	
						$('td', row).eq(mapsDataHeader.get("SaleOrder")).addClass('bg-replaced');
						$('td', row).eq(mapsDataHeader.get("SaleLine")).addClass('bg-replaced');
						$('td', row).eq(mapsDataHeader.get("LotNo")).addClass('bg-replaced');
					}
					$('td', row).eq(mapsDataHeader.get("DyePlan")).addClass('bg-color-azure');
				}
				
// 				if(mapsDataHeader.size != 0){ $('td', row).eq(mapsDataHeader.get("DyePlan")).addClass('bg-color-azure');      }
			},     
			drawCallback: function( settings ){  },   
			initComplete: function () { }  
	 	 });     
//  $('#MainTable').wrap("<div class='scrolledTable'></div>");  
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
		else if(indexAfterReCol == 25 || indexAfterReCol == 26 ){
			if(size == 1){ regrex = '^'+searchVal+'';  } 
			else if(size == 2){
				if(splitText[0] == ''){ regrex = ''+searchVal+'$';  }
				else if(splitText[1] == ''){ regrex = '^'+searchVal+'';  }
				else{ regrex = '^'+searchVal+'$';  } 
			}    
			else{regrex = ''+searchVal+''; }
			MainTable.column(indexAfterReCol).search( regrex, true, false ).draw();   
		}   
		else if(indexAfterReCol == 4 ||  indexAfterReCol == 28 || indexAfterReCol == 29 || indexAfterReCol == 30 ||
				indexAfterReCol == 32 || indexAfterReCol == 33 || indexAfterReCol == 39 ){
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
// 			MainTable.column($(this).data('index')).search(searchVal).draw();  
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
 	   		[   {"data" : "TypePrd"  },               
			    {"data" : "ProductionOrder"  },     
			    {"data" : "SwitchRemark"     },         
		],        	         
		columnDefs :  [   
			{ targets : [0,1, 2 ], type: 'string'      
			} ,         
			{ targets : [ 2 ],     
			   	  render: function (data, type, row) {	     
			   		var htmlEx = '';
			   		if(row.TypePrd == "MAIN"  ){  
			   			htmlEx = '<input class="form-control SwitchRemarkInput" style=" cursor: pointer; padding: 4px 2px;font-size: 12.5px"maxlength="200"  name="SwitchRemark" type="text"  value = "' + row.SwitchRemark+ '" autocomplete="off" >'; 
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
 	   		[   {"data" : "CreateDate"  },               
			    {"data" : "PlanDate"  },     
			    {"data" : "CreateBy"     },      
			    {"data" : "CreateDate"},   
			    {"data" : "InputFrom"},     
			    {"data" : "LotNo"},     
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
//     $('#MainTable tbody ').on( 'click', '.btn-cancelSW', function () { 
//         var $row = $(this).parents("tr");
// 		var idx = MainTable.row($row).index();  
// 		var rowData = MainTable.row($row).data(); 
// // 		var oldValue = rowData.SwitchRemark.trim();  
// // 		var prodOrder = rowData.ProductionOrder.trim();  
// //         var newValue = $(this).val().trim();               
// 		var json = createInputDateJsonData(rowData,'SWCancel'); 
// 		var  obj = JSON.parse(json);       
// 		var arrayTmp = [];  
// 		arrayTmp.push(obj);        
// 		getSwitchProdOrderListByRowProd(arrayTmp);         
// // 		MainTable.rows(idx).remove().draw(false);        
//     } );  
    
  //--------------------------------------- SEARCH ----------------------------------------------
//     MainTable.on( 'autoFill', function ( e, datatable, cells ) {   
// // 		var arrayTmp = []
// // 	    var i;     
// // 		var columnSelect ;
// // 	    for (i = 0; i < cells.length; i++) { 
// // 	      var data = MainTable.row(cells[i][0].index.row).data(); 
// // 	      arrayTmp.push(data);   	
// // 	      columnSelect = cells[i][0].index.column;
// // 	    }   
// // 		if(columnSelect == 14){
// // 			jsonMoreThanOneToServerGroupCase(arrayTmp)	  
// // 		}
// // 		else if(columnSelect == 15){
// // 			jsonMoreThanOneToServerGroupCase(arrayTmp)	  
// // 		}
// // 		else if(columnSelect == 16){
// // 			jsonMoreThanOneToServer(arrayTmp)	  
// // 		}      
// 	} );    
   $("#MainTable").on("change",".CauseOfDelayInput",function(){  
		  var $row = $(this).parents("tr");
		var idx = MainTable.row($row).index();  
		var rowData = MainTable.row($row).data();  
		var oldValue = rowData.CauseOfDelay.trim();  
		var newValue = $(this).val().trim();            
// 	    rowData.CauseOfDelay = $(this).val();     
		handlerInputField("CauseOfDelay" ,oldValue,newValue,check1,rowData ,MainTable,idx)
		//DelayedDep  
	}) 
  $("#MainTable").on("change",".DelayedDepInput",function(){  
		  var $row = $(this).parents("tr");
		var idx = MainTable.row($row).index();  
		var rowData = MainTable.row($row).data();  
		var oldValue = rowData.DelayedDepartment.trim();  
		var newValue = $(this).val().trim();            
// 	    rowData.DelayedDepartment = $(this).val();     
		handlerInputField("DelayedDep" ,oldValue,newValue,check1,rowData ,MainTable,idx)
		//DelayedDep  
	}) 
  $("#MainTable").on("keydown blur",".SwitchRemarkInput", function (e) { 
		var $row = $(this).parents("tr");
		var idx = MainTable.row($row).index();  
		var rowData = MainTable.row($row).data(); 
		var oldValue = rowData.SwitchRemark.trim();  
		var prodOrder = rowData.ProductionOrder.trim();  
        var newValue = $(this).val().trim();      
		if (event.keyCode === 13) {         
			 e.stopImmediatePropagation();
			 e.preventDefault();      
			 if(oldValue == newValue){ }
			 else if(prodOrder != newValue){
				handlerInputField("SwitchRemark" ,oldValue,newValue,check1,rowData ,MainTable,idx)
			 }
			 else{
				swal({   
		   		    title: 'Warning',      
		   		    text: "ProductionOrder can't switch same productionOrder.",    
		   		    icon: 'warning',
		   		    timer: 1000,
		   		    buttons: false,
				})
			  	rowData.SwitchRemark  = oldValue;
			  	MainTable.row(idx).invalidate() ;  
			 } 
		}                      
		else if (event.type === 'blur'  && check1 == 0){    
			 e.stopImmediatePropagation();
			 e.preventDefault();          
			 if(oldValue == newValue){ }
			 else if(prodOrder != newValue){
				handlerInputField("SwitchRemark" ,oldValue,newValue,check1,rowData ,MainTable,idx)
			 }
			 else{
				 swal({   
			   		    title: 'Warning',      
			   		    text: "ProductionOrder can't switch same productionOrder.",    
			   		    icon: 'warning',
			   		    timer: 1000,
			   		    buttons: false,
			   		})
			  	rowData.SwitchRemark = oldValue;
			  	MainTable.row(idx).invalidate() ;  
			 } 
		} 
	});  
      
  function handlerInputField(fieldName ,oldValue,newValue,checkField,rowData ,MainTable,idx){
	  let dataArray ;
	  checkField = 1 ;    
	  let objTmp = {   
			ProductionOrder: rowData.ProductionOrder ,   
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
		var oldValue = rowData.PCRemark;  
        var newValue = $(this).val();      
		 if (event.keyCode === 13) {         
			 e.stopImmediatePropagation();
			 e.preventDefault();  
			 if(oldValue == newValue){  }
			 else { handlerInputField("PCRemark" ,oldValue,newValue,check1,rowData ,MainTable,idx) }
		}                      
		else if (event.type === 'blur'  && check1 == 0){    
			 e.stopImmediatePropagation();	
			 e.preventDefault();       
			 if(oldValue == newValue){  }
			 else { handlerInputField("PCRemark" ,oldValue,newValue,check1,rowData ,MainTable,idx)    }
		} 
	});  
  $("#MainTable").on("keydown blur",".StockLoadInput", function (e) { 
		var $row = $(this).parents("tr");
		var idx = MainTable.row($row).index();      
		var rowData = MainTable.row($row).data(); 
		var oldValue = rowData.StockLoad;  
      var newValue = $(this).val();      
		 if (event.keyCode === 13) {         
			 e.stopImmediatePropagation();
			 e.preventDefault();  
			 if(oldValue == newValue){  }
			 else { handlerInputField("StockLoad" ,oldValue,newValue,check1,rowData ,MainTable,idx) }
		}                      
		else if (event.type === 'blur'  && check1 == 0){    
			 e.stopImmediatePropagation();	
			 e.preventDefault();       
			 if(oldValue == newValue){  }
			 else { handlerInputField("StockLoad" ,oldValue,newValue,check1,rowData ,MainTable,idx)    }
		} 
	});  
  $("#MainTable").on("keydown blur",".ReplacedRemarkInput", function (e) { 
		var $row = $(this).parents("tr");
		var idx = MainTable.row($row).index();      
		var rowData = MainTable.row($row).data(); 
		var oldValue = rowData.ReplacedRemark;  
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
				else{ handlerInputField("ReplacedRemark" ,oldValue,newValue,check1,rowData ,MainTable,idx);   } 
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
				else{ handlerInputField("ReplacedRemark" ,oldValue,newValue,check1,rowData ,MainTable,idx);   } 
			 }
		}    
	});   
  $("#MainTable").on("keydown blur",".StockRemarkInput", function (e) { 
		var $row = $(this).parents("tr");
		var idx = MainTable.row($row).index();    
		var rowData = MainTable.row($row).data();	  
		var oldValue = rowData.StockRemark;  
        var newValue = $(this).val();      
		 if (event.keyCode === 13) {         
			 e.stopImmediatePropagation();
			 e.preventDefault();    
			 if(oldValue == newValue){  }
			 else if(rowData.Grade == ''){
	        	swal({
		   		    title: 'Warning',
		   		    text: 'This StockRemark need grade for input.',    
		   		    icon: 'warning',
		   		    timer: 1000,
		   		    buttons: false,
		   		})
			 } 
			 else{ handlerInputField("StockRemark" ,oldValue,newValue,check1,rowData ,MainTable,idx)  }
		}                      
		else if (event.type === 'blur'  && check1 == 0){    
			 e.stopImmediatePropagation();
			 e.preventDefault();      
			 if(oldValue == newValue){  }
			 else if(rowData.Grade == ''){
	        	swal({   
		   		    title: 'Warning',
		   		    text: 'This StockRemark need grade for input.',    
		   		    icon: 'warning',
		   		    timer: 1000,
		   		    buttons: false,
		   		})
			 }
			 else{ handlerInputField("StockRemark" ,oldValue,newValue,check1,rowData ,MainTable,idx); }    
		}  
	});  
  $("#MainTable").on("keydown blur",".SendCFMCusDateInput", function (e) { 
		var $row = $(this).parents("tr");
		var idx = MainTable.row($row).index();    
		var rowData = MainTable.row($row).data();	 
		var oldValue = rowData.SendCFMCusDate;     
      var newValue = $(this).val();     
		 if (event.keyCode === 13) {         
			e.stopImmediatePropagation();
			e.preventDefault();       
			check1 = 1 ;    
			newValue  = checkDateFormatInput( newValue,oldValue)  
			if(newValue == 'E0'){ 
	           	rowData.SendCFMCusDate  = oldValue;
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
						  handlerInputField("SendCFMCusDate" ,oldValue,newValue,check1,rowData ,MainTable,idx) 
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
           	rowData.SendCFMCusDate  = oldValue;
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

						  	handlerInputField("SendCFMCusDate" ,oldValue,newValue,check1,rowData ,MainTable,idx) 
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
		var oldValue = rowData.CFMPlanLabDate;     
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
             	rowData.CFMPlanLabDate  = oldValue;
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
						  rowData.CFMPlanLabDate  = newValue;
						  MainTable.row(idx).invalidate() ; 
// 						  MainTable.row(idx).invalidate().draw();  
						  var json = createInputDateJsonData(rowData,'CFMPlanLabDate'); 
						  var  obj = JSON.parse(json);    
						  var arrayTmp = [];  
						  arrayTmp.push(obj);       
						  saveInputDateToServer(arrayTmp);      
					  } else { 
						  rowData.CFMPlanLabDate  = oldValue;
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
             	rowData.CFMPlanLabDate  = oldValue;
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
						  rowData.CFMPlanLabDate  = newValue;
// 						  MainTable.row(idx).invalidate().draw();  
							MainTable.row(idx).invalidate() ;  
	 				 		var json = createInputDateJsonData(rowData,'CFMPlanLabDate'); 
	 			 			var  obj = JSON.parse(json);    
	 			 			var arrayTmp = [];  
	 						arrayTmp.push(obj);       
	 						saveInputDateToServer(arrayTmp);        
					  } else {       
						  rowData.CFMPlanLabDate  = oldValue; 
						  MainTable.row(idx).invalidate() ;  
// 						  MainTable.row(idx).invalidate().draw(); 
					  }
				}); 
			}
             check1= 0;  
		} 
	});  
// 	 $("#MainTable").on("keydown blur",".CFMPlanDateInput", function (e) { 
// 			var $row = $(this).parents("tr");
// 			var idx = MainTable.row($row).index();    
// 			var rowData = MainTable.row($row).data();	 
// 			var oldValue = rowData.CFMPlanDate;
// 	        var newValue = $(this).val();           
// 			if (event.keyCode === 13) {     
// 				check2 = 1 ;    
// 				e.stopImmediatePropagation();
// 				e.preventDefault();       
// 				newValue  = checkDateFormatInput( newValue,oldValue)    
// 				if(newValue == 'E0'){ 
// 	             	rowData.CFMPlanDate  = oldValue;MainTable.row(idx).invalidate() ; 
// // 	     			MainTable.row(idx).invalidate().draw();  
// 	             }
// 				else if(newValue == 'E1'){
// 					swal({     	
// 			   		    title: 'Warning',
// 			   		    text: 'Pleas check format input date ( DD/MM/YYYY ).',
// 			   		    icon: 'warning',
// 			   		    timer: 1000,
// 			   		    buttons: false,
// 			   		})
// 				}
// 				else if(newValue == 'E2'){
// 					swal({
// 			   		    title: 'Warning',
// 			   		    text: 'Date need greater than equal today.',
// 			   		    icon: 'warning',
// 			   		    timer: 1000,
// 			   		    buttons: false,
// 			   		})
// 				}      
// 				else if(newValue == 'E3'){ }
// 				else{
// 					swal({
// 						  title: "Are you sure to change date?",
// 						  text: "From : "+oldValue+" to "+newValue,  		
// 						  icon: "warning",
// 						  buttons: true,
// 						  dangerMode: true,																																														  
// 					})     
// 						.then((willDelete) => {
// 						  if (willDelete) {  
// 							  rowData.CFMPlanDate  = newValue;MainTable.row(idx).invalidate() ; 
// // 							  MainTable.row(idx).invalidate().draw();  
// 							  var json = createInputDateJsonData(rowData,'CFMPlanDate'); 
// 		 			 			var  obj = JSON.parse(json);    
// 		 			 			var arrayTmp = [];  
// 		 						arrayTmp.push(obj);   
// 		 						saveInputDateToServer(arrayTmp);  
// 						  } else { 
// 							  rowData.CFMPlanDate  = oldValue;MainTable.row(idx).invalidate() ; 
// // 							  MainTable.row(idx).invalidate().draw(); 
// 						  }
// 						});    
// 				}	
// 				check2= 0;   
// 			}
// 			else if (event.type === 'blur'  && check2 == 0){    
// 				check2= 1; 
// 	 			 e.stopImmediatePropagation();  
// 				 e.preventDefault();       
// 				 newValue  = checkDateFormatInput( newValue,oldValue)     
// 				if(newValue == 'E0'){ 
// 	             	rowData.CFMPlanDate  = oldValue;
// 	             	MainTable.row(idx).invalidate() ; 
// // 		     			MainTable.row(idx).invalidate().draw();  
// 	             }
// 				 else if(newValue == 'E1'){
// 						swal({
// 				   		    title: 'Warning',
// 				   		    text: 'Pleas check format input date ( DD/MM/YYYY ).',
// 				   		    icon: 'warning',
// 				   		    timer: 1000,
// 				   		    buttons: false,
// 				   		})
// 					}
// 					else if(newValue == 'E2'){
// 						swal({
// 				   		    title: 'Warning',
// 				   		    text: 'Date need greater than equal today.',
// 				   		    icon: 'warning',
// 				   		    timer: 1000,
// 				   		    buttons: false,
// 				   		})
// 					}      
// 					else if(newValue == 'E3'){ }
// 					else{
// 						swal({
// 							  title: "Are you sure to change date?",
// 							  text: "From : "+oldValue+" to "+newValue,  		
// 							  icon: "warning",
// 							  buttons: true,
// 							  dangerMode: true,																																														  
// 						})     
// 							.then((willDelete) => {
// 							  if (willDelete) {  
// 								  rowData.CFMPlanDate  = newValue;
// 								  MainTable.row(idx).invalidate() ; 
// // 								  MainTable.row(idx).invalidate().draw();   
// 								  var json = createInputDateJsonData(rowData,'CFMPlanDate'); 
// 			 			 			var  obj = JSON.parse(json);    
// 			 			 			var arrayTmp = [];  
// 			 						arrayTmp.push(obj);   
// 			 						saveInputDateToServer(arrayTmp);  
// 							  } else { 
// 								  rowData.CFMPlanDate  = oldValue;
// 								  MainTable.row(idx).invalidate() ; 
// // 								  MainTable.row(idx).invalidate().draw(); 
// 							  }
// 							});     
// 					}    
// 	             check2= 0; 
// 			}    
// 		});       
	 $("#MainTable").on("keydown blur",".DeliveryDateInput", function (e) { 
			var $row = $(this).parents("tr");
			var idx = MainTable.row($row).index();    
			var rowData = MainTable.row($row).data();	 
			var oldValue = rowData.DeliveryDate;
	        var newValue = $(this).val();           
			if (event.keyCode === 13) {      
				check3 = 1 ;    
				e.stopImmediatePropagation();
				e.preventDefault();       
				newValue  = checkDateFormatInput( newValue,oldValue)    
				if(newValue == 'E0'){ 
	             	rowData.DeliveryDate  = oldValue;
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
							 rowData.DeliveryDate  = newValue;
							 MainTable.row(idx).invalidate() ; 
// 							 MainTable.row(idx).invalidate().draw();  
							 var json = createInputDateJsonData(rowData,'DeliveryDate'); 
		 			 			var  obj = JSON.parse(json);    
		 			 			var arrayTmp = [];  
		 						arrayTmp.push(obj);   
		 						saveInputDateToServer(arrayTmp);  
						} else { 
							 rowData.DeliveryDate  = oldValue;
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
	             	rowData.DeliveryDate  = oldValue;
	             	MainTable.row(idx).invalidate() ; 
// 	     			MainTable.row(idx).invalidate().draw();  
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
							  rowData.DeliveryDate  = newValue;
							  MainTable.row(idx).invalidate() ; 
// 							  MainTable.row(idx).invalidate().draw();   
							  var json = createInputDateJsonData(rowData,'DeliveryDate'); 
		 			 		  var  obj = JSON.parse(json);    
		 			 		  var arrayTmp = [];  
		 					  arrayTmp.push(obj);   
		 					  saveInputDateToServer(arrayTmp);  
						  } else { 
							  rowData.DeliveryDate  = oldValue;
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
			    	if(classInput == 'CFMPlanLabDateParent' || classInput == 'CFMPlanDateParent' 
		   			|| classInput == 'DeliveryDateParent'   || classInput == 'SendCFMCusDateParent'){      
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
// 	var userStatus = document.getElementById("SL_userStatus").value .trim(); 
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
// 	else if( (saleOrder == '' && article == '' && prdOrder == '' && saleNumber == '' && SaleOrderDate == '' && 
//   			designNo == '' && prdOrderDate == '' && material == '' && labNo == '' && deliStatus == '' && 
//    			dueDate == ''  )
//   			)  { 
// 		swal({
//    		    title: 'Warning',
//    		    text: 'Need input some field for search.',
//    		    icon: 'warning',
//    		    timer: 1000,
//    		    buttons: false,
//    		})
// 	}

	else{
		var json = createJsonData(); 
	    var  obj = JSON.parse(json);    
		var arrayTmp = [];   
		arrayTmp.push(obj); 
		searchByDetailToServer(arrayTmp); 
	}
} 

function createInputDateJsonData(val,caseSave){ 
	var CFMPlanLabDate = val.CFMPlanLabDate;      
	var CFMPlanDate = val.CFMPlanDate;       
	var DeliveryDate = val.DeliveryDate;      
	var SaleOrder = val.SaleOrder; 
	var SaleLine = val.SaleLine; 
	var ProductionOrder = val.ProductionOrder; 
	var ReplacedRemark = val.ReplacedRemark; 
	var PCRemark = val.PCRemark; 
	var StockRemark = val.StockRemark; 
	var LotNo = val.LotNo; 
	var Grade = val.Grade; 

	var CauseOfDelay = val.CauseOfDelay; 
	var DelayedDepartment = val.DelayedDepartment; 
	var SwitchRemark = val.SwitchRemark; 
	var StockLoad = val.StockLoad; 
	var SendCFMCusDate = val.SendCFMCusDate; 
	var UserStatus = val.UserStatus; 
	var json = '{"ProductionOrder":'+JSON.stringify(ProductionOrder)+ 
	   ',"UserStatus":'+JSON.stringify(UserStatus)+  
	   ',"CFMPlanLabDate":'+JSON.stringify(CFMPlanLabDate)+  
	   ',"CFMPlanDate":'+JSON.stringify(CFMPlanDate)+  
	   ',"DeliveryDate":'+JSON.stringify(DeliveryDate)+  
	   ',"SaleOrder": '+JSON.stringify(SaleOrder)+ 
	   ',"SaleLine": '+JSON.stringify(SaleLine)+ 
	   ',"CaseSave": '+JSON.stringify(caseSave)+ 
	   ',"ReplacedRemark": '+JSON.stringify(ReplacedRemark)+    
	   ',"StockRemark": '+JSON.stringify(StockRemark)+
	   ',"PCRemark":'+JSON.stringify(PCRemark)+
	   ',"Grade": '+JSON.stringify(Grade)+  
	   ',"LotNo": '+JSON.stringify(LotNo)+
	   ',"SwitchRemark": '+JSON.stringify(SwitchRemark)+ 
	   ',"StockLoad": '+JSON.stringify(StockLoad)+    
	   ',"DelayedDepartment": '+JSON.stringify(DelayedDepartment)+   
	   ',"CauseOfDelay": '+JSON.stringify(CauseOfDelay)+    
	   ',"SendCFMCusDate": '+JSON.stringify(SendCFMCusDate)+    
	   '} ';         
// 	   console.log(json)   
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
	 if(configCusList.length > 0 ){ 
	 	let p_cusDiv = configCusList[0].CustomerDivision	 ;
	 	if(p_cusDiv!=''){ 
	 		cusDiv = p_cusDiv;
	 	}
// 	 	else{
// 	 	} 
	 }
// 	 else{
// 		 cusDiv = p_cusDiv; 
// 	 }
	var json = '{'+
// 		'"CustomerName":'+JSON.stringify(customer)+ 
// 	   ',"CustomerShortName":'+JSON.stringify(customerShort)+ 
	   '"SaleOrder":'+JSON.stringify(saleOrder)+ 
	   ',"ArticleFG":'+JSON.stringify(article)+  
	   ',"ProductionOrder":'+JSON.stringify(prdOrder)+  
	   ',"SaleNumber": '+JSON.stringify(saleNumber)+
	   ' ,"SaleOrderCreateDate":'+JSON.stringify(saleOrderDate)+
	   ',"DesignFG":'+JSON.stringify(designNo)+       
	   ',"ProductionOrderCreateDate":'+JSON.stringify(prdOrderDate)+ 
	   ',"MaterialNo":'+JSON.stringify(material)+          
	   ',"LabNo":'+JSON.stringify(labNo)+   
// 	   ',"UserStatus":'+JSON.stringify(userStatus)+      
		',"UserStatusList":'+JSON.stringify(userStatus)+       
	   ',"CustomerNameList":'+JSON.stringify(customer)+   
	   ',"CustomerDivision":'+JSON.stringify(cusDiv)+      
	   ',"PurchaseOrder":'+JSON.stringify(po)+   
	   ',"CustomerShortNameList":'+JSON.stringify(customerShort)+   
	   ',"DivisionList":'+JSON.stringify(division)+   
	   ',"DeliveryStatus":'+JSON.stringify(deliStatus)+         
	   ',"SaleStatus":'+JSON.stringify(saleStatus)+  
	   ',"DistChannel":'+JSON.stringify(dist) + 
	   ',"DueDate":'+JSON.stringify(dueDate) + 
	   '} ';     
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
 						if(data == 'SaleQuantity' || data == 'RemainQuantity' || data == 'RemainAmount' || data == 'OrderAmount'){  
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
		soLineTmpExcel = val.SaleLine;     
		soTmpExcel = val.SaleOrder;   
		caseDupli = 0;      
	}  
	else if(soLineTmpExcel == val.SaleLine && soTmpExcel   == val.SaleOrder  ){
		caseDupli = 1; 
	}   
	else{           
		soLineTmpExcel = val.SaleLine;     
		soTmpExcel = val.SaleOrder;   
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
				if(bean.IconStatus == 'I'){
					swal({   
						title: "Success",    
					 	text: bean.SystemStatus ,
						icon: "info",
						button: "confirm",
   					});  
				}
				else{  
					swal({   
						title: "Warning ",    
					 	text: bean.SystemStatus  , 
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
				if(bean.IconStatus == 'I0'){    
					if( bean.CountPlanDate >= 35){
						swal({  
							title: "Warning",    
						 	text: bean.SystemStatus+' \r\n Plan by PCMS(count) : '+bean.CountPlanDate ,
							icon: "warning",
							button: "confirm",
	   					});  
					} 
					else {
						swal({  
							title: "Success",    
						 	text: bean.SystemStatus+' \r\n Plan by PCMS(count) : '+bean.CountPlanDate ,
							icon: "success",
							button: "confirm",
	   					});  	
					}
					 
				}
				else if(bean.IconStatus == 'I1'){      
					swal({  
						title: "Success",    
					 	text: bean.SystemStatus ,
						icon: "success",
						button: "confirm",
   					});  	  
				}
				else{  
					swal({   
						title: "Warning ",    
					 	text: bean.SystemStatus  , 
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
// 	var prodOrderSW= dataP[0].SwitchRemark;
// 	var prodOrder= dataP[0].ProductionOrder;
// 	if(prodOrderSW == ''){   }
// 	else{         
// 		$.ajax({   
// 			type: "POST",  
// 			contentType: "application/json",  
// 			data: JSON.stringify(arrayTmp),      
// 			url: "Detail/getSwitchProdOrderListByPrd", 
// 			success: function(data) {   
// 				if(data.length > 0){        
// //	 				var bean = data[0]; 
// //	 				 MainTable.row.add(bean).draw(false);  
// 		     		let indexes = MainTable .rows() .indexes() .filter( function ( value, index ) { 
// 		    	 		return prodOrder === MainTable.row(value).data().ProductionOrder;// return 'P2D031' === MainTable.row(value).data()[1];
// 			      	} );               
// 		     		MainTable.rows(indexes).remove().draw(false);      
// 		     	 	indexes = MainTable .rows() .indexes() .filter( function ( value, index ) { 
// 		    	 		return prodOrderSW === MainTable.row(value).data().ProductionOrder; 
// 		      		} );     
// 	     			MainTable.rows(indexes).remove().draw(false);       
// 				 	MainTable.rows.add(data).draw(false); // Add new data
// 				}	          
// 			},   
// 			error: function(e) {
// 				swal("Fail", "Please contact to IT", "error");
// 			},
// 			done: function(e) {         
// 			}   	
// 		});          
// 	}       
//			var bean = data[0]; 
//			 MainTable.row.add(bean).draw(false);       
	let i = 0; 
	for (i = 0  ; i < dataP.length; i++) {      
		var prodOrder= dataP[i].ProductionOrder; 
		var saleOrder = dataP[i].SaleOrder; 
		var saleLine = dataP[i].SaleLine; 
		var typePrd= dataP[i].TypePrd;  
		let indexes = MainTable .rows() .indexes() .filter( function ( value, index ) { 
			if( MainTable.row(value).data().ProductionOrder == 'รอจัด Lot'){
				return ( saleOrder === MainTable.row(value).data().SaleOrder && 
						 saleLine === MainTable.row(value).data().SaleLine );
			}
			else {
				return ( prodOrder === MainTable.row(value).data().ProductionOrder && 
						 MainTable.row(value).data().TypePrd !== "OrderPuang" );// return 'P2D031' === MainTable.row(value).data()[1];
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
        if (data.ProductionOrder === array[0].ProductionOrder) { 
        	if(fieldName == 'DelayedDep'){
        		data.DelayedDepartment = objTmp.newValue;
        	}    
        	else if(fieldName == 'CauseOfDelay'){
        		data.CauseOfDelay = objTmp.newValue;
        	}  
        	else if(fieldName == 'SendCFMCusDate'){
        		data.SendCFMCusDate = objTmp.newValue;
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
	let userStatus = array[0].UserStatus; 
	let customerType = array[0].CustomerType;  
	rowData =  MainTable.row(idx).data(); 
	// FIX EMERGENCY CASE WAIT TO CALL FROM DB 
// 	if(userStatus == 'รอเปิดบิล' && newVal == ''){
// 		if(userStatus=='Anita'){
// 			rowData.UserStatus = 'รอแจ้งส่ง';
// 		}   
// 		else{ 
// 			rowData.UserStatus = 'รอขาย';
// 		}
// 	}
// 	else if( (userStatus == 'รอขาย' || userStatus == 'รอแจ้งส่ง' )){     
// 		rowData.UserStatus = 'รอเปิดบิล';
// 	}         
	console.log(data[0])
	if(data[0].UserStatus != ''){ 
		rowData.UserStatus = data[0].UserStatus;
	}
// 	rowData.UserStatus = 'รอเปิดบิล';
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
				if(bean.IconStatus == 'I'){ 
					swal({
						title: "Success",    
					 	text: bean.SystemStatus ,
						icon: "info",
						button: "confirm",  
   					});  
					 
					if(objTmp.fieldName == 'SwitchRemark' || objTmp.fieldName == 'ReplacedRemark'){ 
						getSwitchProdOrderDetailByPrd( data) ;
			   		}
					else if(objTmp.fieldName == 'DelayedDep' || objTmp.fieldName == 'CauseOfDelay' ||  
							objTmp.fieldName == 'SendCFMCusDate'){
						setInputDetailToRowByPrd( arrayTmp ,objTmp ) ; //"DelayedDep"CauseOfDelay
					}
					else if(objTmp.fieldName == 'StockLoad' ){
						setUserStatusByStockLoad( arrayTmp ,objTmp,data ) ; //"DelayedDep"CauseOfDelay
					} 
				} 
				else{       
					swal({   
						title: "Warning ",        
					 	text: bean.SystemStatus  , 
			   		    icon: 'warning',     
// 			   		    timer: 2000,
// 			   		    buttons: false,
			   		    button: "confirm",    
			   		})    
			   		if(objTmp.fieldName == 'SwitchRemark' || objTmp.fieldName == 'ReplacedRemark' ){ 
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
// 			console.log(data)
		
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
		 opt.text  = resultData.UserStatus;
		 opt.value = resultData.UserStatus;
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
		 opt.text  = resultData.CustomerName;  
		 opt.value = resultData.CustomerName;
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
		 opt.text  = resultData.CustomerShortName;
		 opt.value = resultData.CustomerShortName;
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
// function addLockColOption(columnsHeader)  {  
// 	var sel = document.getElementById('multi_lockCol');
// // 	for (i = sel.length - 1; i >= 0; i--) {   
// // 		sel.remove(i);
// // 	}                
// 	var size = mapsDataHeader.size;     
// // 	 var opt = document.createElement('option');
// //      opt.appendChild(document.createTextNode(i));
// // 	 opt.text  = (i+1)+" : "+title;
// // 	 opt.value = key;  
// // 	 sel.appendChild(opt);          
	 
// 	for (var i = 0; i < size; i++) {		
// 		 var key = columnsHeader[i].data;// mapsDataHeader.get(i);    
// 		 var title = columnsHeader[i].title;//mapsTitleHeader.get(i);
// 		 var opt = document.createElement('option');
// 	     opt.appendChild(document.createTextNode(i));
// 		 opt.text  = (i+1)+" : "+title;
// 		 opt.value = key;  
// 		 sel.appendChild(opt);          
// 	}             
// 	$("#multi_lockCol").selectpicker("refresh");
// }  
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
		 opt.text  = resultData.Division;
		 opt.value = resultData.Division;
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
		 opt.text  = resultData.SaleFullName;
		 opt.value = resultData.SaleNumber;   
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
	if(colIdx == 'CFMPlanLabDateParent'){	
		path = "Detail/getCFMPlanLabDateDetail";
	}    
	else if(colIdx == 'CFMPlanDateParent'){
// 	else if(colIdx == 27){
		path = "Detail/getCFMPlanDateDetail";
	}    
// 	else if(colIdx == 32){
	else if(colIdx == 'DeliveryDateParent'){
		path = "Detail/getDeliveryPlanDateDetail"; 
	}  
	else if(colIdx == 'SendCFMCusDateParent'){
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
				if(bean.IconStatus == 'I'){
					swal({   
						title: "Success",    
					 	text: bean.SystemStatus ,
						icon: "info",
						button: "confirm",
   					});  
				}
				else{  
					swal({   
						title: "Warning ",    
					 	text: bean.SystemStatus  , 
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
	document.getElementById("input_saleOrder").value = innnerText.SaleOrder;  
	document.getElementById("input_article").value   = innnerText.ArticleFG;  
	document.getElementById("input_prdOrder").value  = innnerText.ProductionOrder;   
	document.getElementById("input_designNo").value  = innnerText.DesignFG;   
	document.getElementById("input_material").value  = innnerText.MaterialNo;  
	document.getElementById("input_labNo").value     = innnerText.LabNo; 
	document.getElementById("input_PO").value     = innnerText.PurchaseOrder;
	var saleCreateArray = innnerText.SaleOrderCreateDate.split(' - ') ;
	if(saleCreateArray.length == 1){ $('#input_saleOrderDate').val('');     }
	else{
		$("#input_saleOrderDate").data('daterangepicker').setStartDate(saleCreateArray[0] );
		$("#input_saleOrderDate").data('daterangepicker').setEndDate(saleCreateArray[1]);  
	} 
	var prdOrderArray = innnerText.ProductionOrderCreateDate.split(' - ') ;
	if(prdOrderArray.length == 1){ $('#input_prdOrderDate').val('');     }
	else{
		$("#input_prdOrderDate").data('daterangepicker').setStartDate(prdOrderArray[0]);
		$("#input_prdOrderDate").data('daterangepicker').setEndDate(prdOrderArray[1]); 
	}     
	var dueDateArray = innnerText.DueDate.split(' - ') ;   
	if(dueDateArray.length == 1){ $('#input_dueDate').val('');     }
	else{   
		$("#input_dueDate").data('daterangepicker').setStartDate(dueDateArray[0]);
		$("#input_dueDate").data('daterangepicker').setEndDate(dueDateArray[1]);
	}       
	var saleStatus = innnerText.SaleStatus;
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
	document.getElementById('SL_saleNumber').value=innnerText.SaleNumber;
	document.getElementById('SL_delivStatus').value=innnerText.DeliveryStatus;
	
	 document.getElementById("check_DM").checked = false;
	 document.getElementById("check_EX").checked = false;
	 document.getElementById("check_HW").checked = false;
	 var distChannel = innnerText.DistChannel.split('|');   
	 for( let i = 0 ;i < distChannel.length ; i++){
		 if(distChannel[i] == 'DM'){ document.getElementById("check_DM").checked = true; }
		 else if(distChannel[i] == 'EX'){ document.getElementById("check_EX").checked = true;  }
		 else if(distChannel[i] == 'HW'){ document.getElementById("check_HW").checked = true;  }
	 }             
	 var customer = innnerText.CustomerName.split('|'); 
	 var customerShort = innnerText.CustomerShortName.split('|'); 
	 var userStatusList = innnerText.UserStatus.split('|');
	 var divisionList = innnerText.Division.split('|'); 
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
    	htmlSelectOption += "<option value='"+data[i].DelayedDepartment+"'>"+(i+1)+":"+data[i].DelayedDepartment+"</option>";
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
	  if(fieldName == 'SwitchRemark'){rowData.SwitchRemark = value ;  }
	  else if(fieldName == 'PCRemark'){rowData.PCRemark = value  ;  }
	  else if(fieldName == 'ReplacedRemark'){rowData.ReplacedRemark = value  ; }
	  else if(fieldName == 'StockRemark'){rowData.StockRemark = value  ; } 
	  else if(fieldName == 'StockLoad'){rowData.StockLoad = value  ; } 
	  else if(fieldName == 'DelayedDep'){rowData.DelayedDepartment = value  ; } 
	  else if(fieldName == 'CauseOfDelay'){rowData.CauseOfDelay = value  ; }  
	  else if(fieldName == 'SendCFMCusDate'){rowData.SendCFMCusDate = value  ; }
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
				if(bean.IconStatus == 'E'){ 
					swal({   
						title: "Warning ",        
					 	text: bean.SystemStatus  , 
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
// 					 	text: bean.SystemStatus ,   
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