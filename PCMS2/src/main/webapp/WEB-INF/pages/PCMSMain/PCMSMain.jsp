<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page isELIgnored="false"%> 
<!DOCTYPE html>
<html>
<head>   
	<jsp:include page="/WEB-INF/pages/config/meta.jsp"></jsp:include>  
	<title>PCMS - Summary</title>            	    
	<jsp:include page="/WEB-INF/pages/config/css/baseCSS.jsp"></jsp:include>       
	<link href="<c:url value="/resources/css/style_overide.css" />" rel="stylesheet" type="text/css">    
	<link href="<c:url value="/resources/css/datatable.overide.css" />" rel="stylesheet" type="text/css">       
</head>      
<body>       	         	
	<jsp:include page="/WEB-INF/pages/config/navbar.jsp"></jsp:include>      
	<jsp:include page="/WEB-INF/pages/config/loading.jsp"></jsp:include>    
    <jsp:include page="/WEB-INF/pages/config/searchDiv.jsp"></jsp:include>    
		<div id="wrapper-center" class="row" style="margin: 0px 5px;">     
			<div class="col-12 col-sm-12 col-md-12 col-lg-12 col-xl-12 " style="padding: 0px;margin: 0px 0px;" >                
				<div class="table-responsive ">        
					<table id="MainTable" class="table compact table-bordered table-striped text-center" style="zoom: 95%;   font-size: 12.5px;width:100%">
				        <thead>          
				            <tr>
				                <th class="row-table" style="vertical-align: middle;">SO No. </th>
				                <th class="row-table" style="vertical-align: middle;">SO Line</th> 
				                <th class="row-table" style="vertical-align: middle;">Article No</th>
				                <th class="row-table" style="vertical-align: middle;">Design No </th>
				                <th class="row-table" style="vertical-align: middle;">ATT<span class="c"style="display: block;"> Color</span> </th>  
				                <th class="row-table" style="vertical-align: middle;">Cust.<span class="c"style="display: block;"> Color</span> </th> 
				                <th class="row-table" style="vertical-align: middle;">Order<span class="c"style="display: block;"> Qty.</span> </th> 
				                <th class="row-table" style="vertical-align: middle;">Shipped	<span class="c"style="display: block;"> Qty.</span> </th> 
				                <th class="row-table" style="vertical-align: middle;">Unit </th> 
				                <th class="row-table" style="vertical-align: middle;">Prod.<span class="c"style="display: block;"> No.</span> </th> 
				                <th class="row-table" style="vertical-align: middle;">Prod.<span class="c"style="display: block;"> Qty.</span> </th> 
				                <th class="row-table" style="vertical-align: middle;">Greige<span class="c"style="display: block;"> In</span> </th> 
				                <th class="row-table" style="vertical-align: middle;">User<span class="c"style="display: block;"> Status</span> </th> 
				                <th class="row-table" style="vertical-align: middle;">Due<span class="c"style="display: block;"> Date</span> </th> 
				                <th class="row-table" style="vertical-align: middle;">Prepare	 </th>  
				                <th class="row-table" style="vertical-align: middle;">Relax	 </th>  
				                <th class="row-table" style="vertical-align: middle;">PS </th>      
				                <th class="row-table" style="vertical-align: middle;">Dye <span style="display: block;"> [Plan]</span> </th> 
				                <th class="row-table" style="vertical-align: middle;">Dye <span class="c"style="display: block;"> [Actual]</span> </th> 
				                <th class="row-table" style="vertical-align: middle;">Dye <span class="c"style="display: block;"> Status</span> </th> 
				                <th class="row-table" style="vertical-align: middle;">Dryer<span class="c"style="display: block;"> Date</span> </th> 
				                <th class="row-table" style="vertical-align: middle;">FN  </th> 
				                <th class="row-table" style="vertical-align: middle;">Inspect </th>  
				                <th class="row-table" style="vertical-align: middle;">CFM Date<span class="c"style="display: block;"> [Plan]</span> </th> 
				                <th class="row-table" style="vertical-align: middle;">CFM Date<span class="c"style="display: block;"> [Actual]</span> </th> 
				                <th class="row-table" style="vertical-align: middle;">Delivery Date </th> 
				                <th class="row-table" style="vertical-align: middle;">Shipping </th>   
				        	</tr>      
				        </thead>          	         
<!-- 				        <tbody>     -->
<!-- 				        </tbody>        --> 
				    </table>            
				</div>                
			</div>   	
		</div>               
<!-- 		<div id="wrapper-bot" class="row">   -->
		 
<!-- 		</div>        -->   
    <jsp:include page="/WEB-INF/pages/config/footer.jsp"></jsp:include> 
	<jsp:include page="/WEB-INF/pages/config/PCMSMainModal/modalMain.jsp"></jsp:include> 
	<jsp:include page="/WEB-INF/pages/config/PCMSDetailModal/ColumnSetting/ColumnSetting.jsp"></jsp:include> 
</body>                  
<script src="<c:url value="/resources/js/DatatableSort.js" />"></script>   
<script src="<c:url value="/resources/js/General.js" />"></script>     
<%-- <script src="<c:url value="/resources/js/Encrypt.js" />"></script>    --%>
  <style>
  .p-r-15 {padding-right: 15px !important; }
  </style>
<!-- <script src="https://cdn.datatables.net/fixedheader/3.2.0/js/dataTables.fixedHeader.min.js"></script>    -->
<script>              	   
var userId = '' ; 
<%-- JSON.stringify(<%= session.getAttribute("user")%> ) --%>
var preloader = document.getElementById('loader');  
var today = new Date();        //modalForm
var dd = String('0' + today.getDate()).slice(-2); 
var mm = String('0' + (today.getMonth() + 1)).slice(-2); ; //January is 0!
var yyyy = today.getFullYear();
var startDate = dd+'/'+mm+'/'+yyyy;      
var MainTable ;  
var poTable ;       
var mapsDataHeader  = new Map();  
var mapsTitleHeader  = new Map();  
var mapsColumnHeader  = new Map(); 
var presetTable ;var dyeingTable;var fnTable;var inspectTable;var packingTable;var sendTestQCTable;
var columnsHeader  = [];
var colList ;
var userStatusList ;  	
var cusNameList ; 
var divisionList ; 
var cusShortNameList ;  	
var workInLabTable ;var waitTestTable;var cfmTable;var saleTable;var saleInputTable;var submitDateTable;
var ncTable;var receipeTable;
var collapsedGroups = {};        
// var domain = "http://"+window.location.hostname+":8080";
var domain ;
var urlLBMS ;     
var urlLBMSObj ; 
var urlSFC ; 
var urlSFCObj  ; 
var urlInspect ; 
var urlInspectObj ; 
var urlQCMS ; 
var urlQCMSObj ; 
 
var soTmpExcel ;   
var soLineTmpExcel;
// console.log(window.location.host)
// console.log(window.location.hostname)
// console.log(window.location.origin)   
$(document).on('show.bs.modal', '.modal', function (event) {
    var zIndex = 1040 + (10 * $('.modal:visible').length);
    $(this).css('z-index', zIndex);
    setTimeout(function() {
        $('.modal-backdrop').not('.modal-stack').css('z-index', zIndex - 1).addClass('modal-stack');
    }, 0);
});  
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
$(document) .ready( function() {  
<%-- 		var ctx2 = "<%=request.getContextPath()%>" --%>
// 		var ctx = "${pageContext.request.contextPath}";   
// 		console.log(ctx2)       ;               
<%-- 	var saleNumberList = '<%=request.getAttribute("SaleNumberList")%>'; --%> 
// 	var saleNumberList = JSON.parse('${SaleNumberList}');
// 	userStatusList = JSON.parse('${UserStatusList}');  
// 	$('#SL_userStatus').selectpicker();             
// 	addUserStatusOption(userStatusList );        
// 	$('#SL_userStatus').selectpicker('val', userStatusList);     
//   	addSelectOption(saleNumberList)
// 	$('input[name="daterange"]').on('apply.daterangepicker', function(ev, picker) {
// 	      $(this).val(picker.startDate.format('DD/MM/YYYY') + ' - ' + picker.endDate.format('DD/MM/YYYY'));
// 	  });               
	
	// ---------------------------------------- set---------------------------------------------
	let os = JSON.parse('${OS}');  
// 	console.log(os)
	let result = os.includes("win");
	console.log(result)     
	if(result === true){ domain = "http://"+window.location.hostname+":8080"; }
	else{ domain = "https://"+window.location.hostname;  } 
// 	domain = domain+window.location.hostname+:"8080";
	urlLBMS = domain+"/LBMS/";     
	urlLBMSObj = domain+"/LBMS/LabHistory"; 
	urlSFC = domain+"/SFC/"; 
	urlSFCObj = domain+"/SFC/HistoryWork"; 
	urlInspect = domain+"/InspectSystem/search/home.html"; 
	urlInspectObj = domain+"/InspectSystem/search/home.html"; 
	urlQCMS = domain+"/QCMS/first.html"; 
	urlQCMSObj = domain+"/QCMS/request/search.html";
// 	console.log(domain)
	// ---------------------------------------- set---------------------------------------------
	userId = JSON.parse('${UserID}');   ;    
	document.getElementById("btn_lockColumn").style.display = "none";
	$(document).ajaxStart(function() {$( "#loading").css("display","block"); });   
	$(document).ajaxStop(function() {$("#loading" ).css("display","none"); });
	$('#input_dueDate').val('');
	$('#input_saleOrderDate').val('');    
	$('#input_prdOrderDate').val('');
 	$('input[name="daterange"]').on('cancel.daterangepicker', function(ev, picker) {
	      $(this).val('');     
  	});     
	//--------------------------------------- SEARCH ----------------------------------------------
	$('#btn_saveDefault').on( 'click', function () {       
	     saveDefault();
	} );
    $('#btn_loadDefault').on( 'click', function () {       
	     loadDefault();
	} );
    $('#btn_search').on( 'click', function () {      
    	$("#submit_button").click()
    	soLineTmp = '';            
		soTmp = '';   
    	searchByDetail();          
 	} );                             
    $('#btn_colSetting').on( 'click', function () {      
// 		MainTable. colReorder.order( [ 5,24,25,2,3,4,0,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23 ,1  ] ); 
		$('#modalColSetting').modal('show'); 
	} );     	 
    $('#btn_download').on( 'click', function () {      
    	var ObjMarkup = MainTable.data().toArray(); 
//     	console.log(ObjMarkup)
		exportCSV(ObjMarkup)            
 	} );  
 	$('#btn_clear').on( 'click', function () {       
		clearInput();
 	} ); 
	$('#save_col_button').on( 'click', function () {       
	    var selectedItem = $('#multi_colVis').val();    // get selected list  
		setColVisibleTable(columnsHeader,selectedItem);
		saveColSettingToServer(selectedItem);     
		getVisibleColumnsTable()
 	} ); 
	//--------------------------------------- SEARCH ----------------------------------------------
    $('#btn_lbms').on( 'click', function () {       
    	var tblData = MainTable.rows( '.selected').data(); 
		if(tblData.length == 0 ){
			swal({
	   		    title: 'Warning',   
	   		    text: 'Need to select atleast 1 row.',
	   		    icon: 'warning',
	   		    timer: 1000,
	   		    buttons: false,
	   		})
		}         
		else {
			var prdOrder = tblData[0].ProductionOrder
			if(prdOrder  == "รอจัด Lot"	 || prdOrder  == "ขาย stock" ||prdOrder == "รับจ้างถัก" || prdOrder == "พ่วงแล้วรอสวม"	|| prdOrder == "รอสวมเคยมี Lot"	||prdOrder == "Lot ขายแล้ว"){
				swal({
		   		    title: 'Warning',   
		   		    text: 'Need to select atleast 1 row.',
		   		    icon: 'warning',
		   		    timer: 1000,
		   		    buttons: false, 
		   		})
			}      
			else {
				  
				  var arrayTmp = [];        
					arrayTmp.push();     
					$.ajax({
					   type: "POST",     
						contentType: "application/json",         
						url: "Main/getEncrypted/"+userId,      
					    data : JSON.stringify(arrayTmp),             
					    success : function(data) {   
					    	goToLBMS(tblData,userId,data);   
					    }   	
					});      
			}   
		} 
 	} ); 
    $('#btn_prdDetail').on( 'click', function () {    
    	var tblData = MainTable.rows( '.selected').data();    
		if(tblData.length == 0 ){
			swal({
	   		    title: 'Warning',   
	   		    text: 'Need to select atleast 1 row.',
	   		    icon: 'warning',
	   		    timer: 1000,
	   		    buttons: false,
	   		})
		}         
		else {
			var prdOrder = tblData[0].ProductionOrder
			if(prdOrder  == "รอจัด Lot"	 || prdOrder  == "ขาย stock" ||prdOrder == "รับจ้างถัก"	|| prdOrder == "พ่วงแล้วรอสวม"	|| prdOrder == "รอสวมเคยมี Lot"||prdOrder == "Lot ขายแล้ว"){
				swal({
		   		    title: 'Warning',   
		   		    text: 'Need to select atleast 1 row.',
		   		    icon: 'warning',
		   		    timer: 1000, 
		   		    buttons: false,
		   		})
			}      
			else {
				var arrayTmp = [];       
				$.each( tblData, function(i, val) {
//	 	   			tmpData = tblData[i];    
		 			arrayTmp.push(val); 
				});  
				getPrdDetailByRow(arrayTmp);  
				 $('#modalForm').modal('show');  
			}   
		}  
 	} ); 
    $('#btn_inspect').on( 'click', function () {    
    	var tblData = MainTable.rows( '.selected').data(); 
		if(tblData.length == 0 ){
			swal({
	   		    title: 'Warning',   
	   		    text: 'Need to select atleast 1 row.',
	   		    icon: 'warning',
	   		    timer: 1000,
	   		    buttons: false,
	   		})
		}         
		else {
			var prdOrder = tblData[0].ProductionOrder
			if(prdOrder  == "รอจัด Lot"	 || prdOrder  == "ขาย stock" ||prdOrder == "รับจ้างถัก"|| prdOrder == "พ่วงแล้วรอสวม"	|| prdOrder == "รอสวมเคยมี Lot"	||prdOrder == "Lot ขายแล้ว"){
				swal({
		   		    title: 'Warning',   
		   		    text: 'Need to select atleast 1 row.',
		   		    icon: 'warning',
		   		    timer: 1000,
		   		    buttons: false,
		   		})
			}      
			else {
				
				var arrayTmp = [];        
				arrayTmp.push();     
				$.ajax({
				   type: "POST",     
					contentType: "application/json",         
					url: "Main/getEncrypted/"+userId,      
				    data : JSON.stringify(arrayTmp),             
				    success : function(data) {   
				    	goToInspect(tblData,userId,data); 
				    }   	
				});      
			}   
		}  
 	} ); 
    $('#btn_sfc').on( 'click', function () {      
    	var tblData = MainTable.rows( '.selected').data(); 
		if(tblData.length == 0 ){
			swal({
	   		    title: 'Warning',   
	   		    text: 'Need to select atleast 1 row.',
	   		    icon: 'warning',
	   		    timer: 1000,
	   		    buttons: false,
	   		})
		}         
		else {
			var prdOrder = tblData[0].ProductionOrder
			if(prdOrder  == "รอจัด Lot"	 || prdOrder  == "ขาย stock" ||prdOrder == "รับจ้างถัก"|| prdOrder == "พ่วงแล้วรอสวม"	|| prdOrder == "รอสวมเคยมี Lot"	||prdOrder == "Lot ขายแล้ว"){
				swal({
		   		    title: 'Warning',   
		   		    text: 'Need to select atleast 1 row.',
		   		    icon: 'warning',
		   		    timer: 1000,
		   		    buttons: false,
		   		})
			}      
			else { 
				var arrayTmp = [];        
				arrayTmp.push();     
				$.ajax({
				   type: "POST",     
					contentType: "application/json",         
					url: "Main/getEncrypted/"+userId,      
				    data : JSON.stringify(arrayTmp),             
				    success : function(data) {   
				    	goToSFC(tblData,userId,data);   
				    }   	
				});         
					
			}   
		}
			 
 	} ); 
    $('#btn_qcms').on( 'click', function () {      
    	var tblData = MainTable.rows( '.selected').data(); 
		if(tblData.length == 0 ){
			swal({
	   		    title: 'Warning',   
	   		    text: 'Need to select atleast 1 row.',
	   		    icon: 'warning',
	   		    timer: 1000,
	   		    buttons: false,
	   		})
		}         
		else {
			var prdOrder = tblData[0].ProductionOrder 
			if(prdOrder  == "รอจัด Lot"	 || prdOrder  == "ขาย stock" ||prdOrder == "รับจ้างถัก"|| prdOrder == "พ่วงแล้วรอสวม"	|| prdOrder == "รอสวมเคยมี Lot"	||prdOrder == "Lot ขายแล้ว"){
				swal({
		   		    title: 'Warning',   
		   		    text: 'Need to select atleast 1 row.',
		   		    icon: 'warning',
		   		    timer: 1000,
		   		    buttons: false,
		   		})
			}      
			else {
				 
				 var arrayTmp = [];        
					arrayTmp.push();     
					$.ajax({
					   type: "POST",     
						contentType: "application/json",         
						url: "Main/getEncrypted/"+userId,      
					    data : JSON.stringify(arrayTmp),             
					    success : function(data) {    
					    	goToQCMS(tblData,userId,data);
					    }   	
					});      
			}   
		} 
 	} );  
//     var StartDate = $("#input_requestDate").data('daterangepicker').startDate.format('DD/MM/YYYY');
// 	 var EndDate = $("#input_requestDate").data('daterangepicker').endDate.format('DD/MM/YYYY');  
	$('#MainTable thead tr').clone(true).appendTo('#MainTable thead');
	$('#MainTable thead tr:eq(1) th') .each( function(i) {      
		var title = $(this).text();         
		$(this).html( '<input type="text" class="monitor_search" style="width:100%" data-index="' + i + '"/>');
	});     
	 MainTable = $('#MainTable').DataTable({    
//	     	stateSave: true ,    
//	 		data : jsonData,
	 	    select : true,           
	 	   	scrollX: true,              
//	  	    scrollY: '70vh' , //ขนาดหน้าจอแนวตั้ง 
	 	  	scrollY: '55vh' , //ขนาดหน้าจอแนวตั้ง       
	 	   	scrollCollapse: true,   
		   	orderCellsTop : true,           
			orderClasses : false,           
//	 		lengthChange: false,              
// 			"deferRender": true,              	
//	 		filter:false,       
			lengthChange : false,     
			pageLength: 1000,	 
//	 		lengthMenu: [[1000, -1], [1000, "All"]],
//	 		colReorder: {      
//	             realtime: true       
//	         },                       
	 		colReorder: true,  
			rowsGroup: [ 0 ,1,2,3,4,5,6,7,8  ],             
	 	   	columns :    
	 	   		[      
			    {"data" : "SaleOrder",  "title": 'SO No.'} ,         //0
			    {"data" : "SaleLine",  "title": 'SO Line'}, 
			    {"data" : "ArticleFG",  "title": 'Article No'}, 
			    {"data" : "DesignFG",  "title": 'Design No'}, 
			    {"data" : "Color",  "title": 'ATT Color'},     
			    {"data" : "ColorCustomer",  "title": 'Cust.Color'},      //5
			    {"data" : "SaleQuantity",  "title": 'Order Qty.','type': 'num'}, 
			    {"data" : "BillQuantity",  "title": 'Shipped Qty.','type': 'num'}, 
			    {"data" : "SaleUnit",  "title": 'Unit'}, 
			    {"data" : "ProductionOrder",  "title": 'Prod.No.'}, 
			    {"data" : "TotalQuantity",  "title": 'Prod.Qty.','type': 'num'},      //10 
			    {"data" : "GreigeInDate",  "title": 'Greige In'},    
			    {"data" : "UserStatus",  "title": 'User Status'},     
			    {"data" : "DueDate",  "title": 'Due Date','type': 'date-euro'},        
			    {"data" : "Prepare",  "title": 'Prepare'},   
			    {"data" : "Relax",  "title": 'Relax'},                 //15
			    {"data" : "Preset",  "title": 'PS'},                 //16
			    {"data" : "DyePlan",  "title": 'Dye [Plan]'},            //17
			    {"data" : "DyeActual",  "title": 'Dye [Actual]'},        //18
			    {"data" : "DyeStatus",  "title": 'Dye Status'},          //19
			    {"data" : "Dryer" ,  "title": 'Dryer Date'},     
			    {"data" : "Finishing",  "title": 'FN'},  
			    {"data" : "Inspectation",  "title": 'Inspect'},       //22 
			    {"data" : "CFMPlanDate",  "title": 'CFM Date[Plan]'},        //23 
			    {"data" : "CFMDateActual",  "title": 'CFM Date[Actual]'},        //24
			    {"data" : "DeliveryDate",  "title": 'Delivery Date'},       //25
			    {"data" : "LotShipping",  "title": 'Shipping'}         //26    
//	 		    {"data" : "ShipDate"}          //23   
			],       
//	     col,      	           
			columnDefs :  [	       
				{ targets : [ 0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16 ,17,18,19,20,21,22  ,23 ,24,25,26],                
//	 			  	  className : 'data-custom-main',        
//	 		  	 	  type: 'string'      
					} ,            
				{ targets :  [26],                   
				  	  className : 'p-r-15',                  
					} ,   
				{ targets :  [6,7,10],                         
			  	  type : 'num',                 
				} ,   
				{ targets:[9]  ,       
					render: function (data, type, row) {	   
						let html = '<div  name="n_'+row.ProductionOrder+' data-toggle="tooltip" title="' + row.TypePrd + '"> '+row.ProductionOrder+'</div>'
						return  html; 
				   	  }    
				},   
// 				{ targets :  [12],                               
// 	 				 className : 'dt-custom-td140',      
// 			  	  	type: 'string'                  
// 				} ,   
//	 			{ targets : [ 13],  
//	 				type: 'date-euro'          
//	 				} , 
							 
			 	{ targets : [ 13 ],      
					  type: 'date-euro' ,    
// 				   	  render: function (data, type, row) {	     
// //	 			   		  var html = ''   
// //	 				   		if(row.DueDate != "") {  
// //	 			 	    		var datearray = row.DueDate.split("/");
// //	 				  	    	var dueDate = new Date(datearray[1] + '/' + datearray[0] + '/' + datearray[2]) ;   
// //	 					  	    if (dueDate < today){     
// //	 					  	    	 $('td', row).eq(13).addClass('dt-custom-overdue');  
// //	 								html = ' <div class="dt-custom-overdue">' + row.DueDate+ '</div>' 
// //	 					        }
// //	 					  	    else{
// //	 					  	    	html = ' <div >' + row.DueDate+ '</div>' 
// //	 					  	    }
// //	 			  	    	}   
// //	 				   		else{	
// //	 				   			html = ' <div >' + row.DueDate+ '</div>' 
// //	 				   		}     
// 							return  row.DueDate;
// 				   	  }    
					} , 
// 					{ targets : [ 14 ],      
// 						  type: 'String' ,    
// 	 				   	  render: function (data, type, row) {	 
// 							let dateDDMM = row.Prepare;
// 	 				   		if(dateDDMM != "") {  
// 	 			 	    		var datearray = dateDDMM.split("/");
// 	 				  	    	dateDDMM =  String('0' + datearray[0]).slice(-2)   + '/' + String('0' + datearray[1]).slice(-2)   ;     
// 	 			  	    	}       
//  							return dateDDMM;
// 	 				   	  }    
// 					} , 
// 					{ targets : [ 15 ],      
// 						  type: 'String' ,    
// 	 				   	  render: function (data, type, row) {	 
// 							let dateDDMM = row.Relax;
// 	 				   		if(dateDDMM != "") {  
// 	 			 	    		var datearray = dateDDMM.split("/");
// 	 				  	    	dateDDMM =  String('0' + datearray[0]).slice(-2)   + '/' + String('0' + datearray[1]).slice(-2)   ;    
// 	 			  	    	}       
// 							return dateDDMM;
// 	 				   	  }    
// 					} , 
// 					{ targets : [ 16 ],      
// 						  type: 'String' ,    
// 	 				   	  render: function (data, type, row) {	 
// 							let dateDDMM = row.Preset;
// 	 				   		if(dateDDMM != "") {  
// 	 			 	    		var datearray = dateDDMM.split("/");
// 	 				  	    	dateDDMM =  String('0' + datearray[0]).slice(-2)   + '/' + String('0' + datearray[1]).slice(-2)   ;     
// 	 			  	    	}       
// 							return dateDDMM;
// 	 				   	  }    
// 					} , 
// 					{ targets : [ 17 ],      
// 						  type: 'String' ,    
// 	 				   	  render: function (data, type, row) {	 
// 							let dateDDMM = row.DyePlan;
// 	 				   		if(dateDDMM != "") {  
// 	 			 	    		var datearray = dateDDMM.split("/");
// 	 				  	    	dateDDMM =  String('0' + datearray[0]).slice(-2)   + '/' + String('0' + datearray[1]).slice(-2)   ;    
// 	 			  	    	}       
// 							return dateDDMM;
// 	 				   	  }    
// 					} , 
// 					{ targets : [ 18 ],      
// 						  type: 'String' ,    
// 	 				   	  render: function (data, type, row) {	 
// 							let dateDDMM = row.DyeActual;
// 	 				   		if(dateDDMM != "") {  
// 	 			 	    		var datearray = dateDDMM.split("/");
// 	 				  	    	dateDDMM =  String('0' + datearray[0]).slice(-2)   + '/' + String('0' + datearray[1]).slice(-2)   ;     
// 	 			  	    	}       
// 							return dateDDMM;
// 	 				   	  }    
// 					} , 
// 					{ targets : [ 20 ],      
// 						  type: 'String' ,    
// 	 				   	  render: function (data, type, row) {	 
// 							let dateDDMM = row.Dryer;
// 	 				   		if(dateDDMM != "") {  
// 	 			 	    		var datearray = dateDDMM.split("/"); 
// 	 				  	    	dateDDMM =  String('0' + datearray[0]).slice(-2)   + '/' + String('0' + datearray[1]).slice(-2)   ;   
// 	 			  	    	}       
// 							return dateDDMM;
// 	 				   	  }    
// 					} , 
// 					{ targets : [ 21 ],      
// 						  type: 'String' ,    
// 	 				   	  render: function (data, type, row) {	 
// 							let dateDDMM = row.Finishing;
// 	 				   		if(dateDDMM != "") {  
// 	 			 	    		var datearray = dateDDMM.split("/");
// 	 				  	    	dateDDMM =  String('0' + datearray[0]).slice(-2)   + '/' + String('0' + datearray[1]).slice(-2)   ;    
// 	 			  	    	}       
// 							return dateDDMM;
// 	 				   	  }    
// 					} , 
// 					{ targets : [ 22 ],      
// 						  type: 'String' ,    
// 	 				   	  render: function (data, type, row) {	 
// 							let dateDDMM = row.Inspectation;
// 	 				   		if(dateDDMM != "") {  
// 	 			 	    		var datearray = dateDDMM.split("/");
// 	 				  	    	dateDDMM =  String('0' + datearray[0]).slice(-2)   + '/' + String('0' + datearray[1]).slice(-2)   ;     
// 	 			  	    	}       
// 							return dateDDMM;
// 	 				   	  }    
// 					} , 
// 					{ targets : [ 23 ],      
// 						  type: 'String' ,    
// 	 				   	  render: function (data, type, row) {	 
// 							let dateDDMM = row.CFMPlanDate;
// 	 				   		if(dateDDMM != "") {  
// 	 			 	    		var datearray = dateDDMM.split("/");
// 	 				  	    	dateDDMM =  String('0' + datearray[0]).slice(-2)   + '/' + String('0' + datearray[1]).slice(-2)   ;   
// 	 			  	    	}       
// 							return dateDDMM;
// 	 				   	  }    
// 					} , 
// 					{ targets : [ 24 ],      
// 						  type: 'String' ,    
// 	 				   	  render: function (data, type, row) {	 
// 							let dateDDMM = row.CFMDateActual;
// 	 				   		if(dateDDMM != "") {  
// 	 			 	    		var datearray = dateDDMM.split("/");
// 	 				  	    	dateDDMM =  String('0' + datearray[0]).slice(-2)   + '/' + String('0' + datearray[1]).slice(-2)   ;    
// 	 			  	    	}       
// 							return dateDDMM;
// 	 				   	  }    
// 					} , 
// 					{ targets : [ 25 ],      
// 						  type: 'String' ,    
// 	 				   	  render: function (data, type, row) {	 
// 							let dateDDMM = row.DeliveryDate;
// 	 				   		if(dateDDMM != "") {  
// 	 			 	    		var datearray = dateDDMM.split("/");
// 	 				  	    	dateDDMM =  String('0' + datearray[0]).slice(-2)   + '/' + String('0' + datearray[1]).slice(-2)   ;     
// 	 			  	    	}       
// 							return dateDDMM;
// 	 				   	  }    
// 					} , 
// 					{ targets : [ 26 ],      
// 						  type: 'String' ,    
// 	 				   	  render: function (data, type, row) {	 
// 							let dateDDMM = row.LotShipping;
// 	 				   		if(dateDDMM != "") {  
// 	 			 	    		var datearray = dateDDMM.split("/");
// 	 				  	    	dateDDMM =  String('0' + datearray[0]).slice(-2)   + '/' + String('0' + datearray[1]).slice(-2)   ;   
// 	 			  	    	}       
// 							return dateDDMM;
// 	 				   	  }    
// 					} , 
						
			], 
//	 		 order: [[2, 'asc'], [1, 'asc']],  
//	 		rowsGroup: [ 0 ,1,2,3,4,5,6,7,8  ],   
		 	select : {              
				style: 'os',         
			 	selector: 'td:not(.status)'  // .status is class        
	  		},    
	  		createdRow : function(row, data, index) {
//	 			$('td', row).eq(16).addClass('bg-color-azure');
//	   	        $('td', row).eq(21).addClass('bg-color-azure');    
//	   	        $('td', row).eq(23).addClass('bg-color-azure');   
//	   			console.log(data.DueDate, index )                   
// 	  			if(mapsDataHeader.size != 0){     
// //	   				$('td', row).eq(mapsDataHeader.get("DueDate")).addClass('dt-custom-overdue') ;      
// 	  				if(data["DueDate"] != "") {      
// 		 	    		var datearray = data["DueDate"].split("/");
// 			  	    	var dueDate = new Date(datearray[1] + '/' + datearray[0] + '/' + datearray[2]) ;    
// 				  	    if (sevenDayAgo >= dueDate   ){             
// 				  	    	$('td', row).eq(mapsDataHeader.get("DueDate")).addClass('dt-custom-overdue');        
// 				        }
// 	  	    		}        
// 	  			}   
	  			if(mapsDataHeader.size != 0){       
					if (data["TypePrd"] == "OrderPuang" ) { 	         	 
						$('td', row).eq(mapsDataHeader.get("ProductionOrder")).addClass('bg-orderpuang'); 
					}  
					else if (data["TypePrd"] == "Switch" ) { 	 
						$('td', row).eq(mapsDataHeader.get("ProductionOrder")).addClass('bg-switch');     
					}   	
					else if (data["TypePrd"] == "Replaced" ) { 	 
						$('td', row).eq(mapsDataHeader.get("ProductionOrder")).addClass('bg-replaced');
					}
					if(data["DueDate"] != "") {      
		 	    		var datearray = data["DueDate"].split("/");
			  	    	var dueDate = new Date(datearray[1] + '/' + datearray[0] + '/' + datearray[2]) ;    
				  	    if (sevenDayAgo >= dueDate   ){             
				  	    	$('td', row).eq(mapsDataHeader.get("DueDate")).addClass('dt-custom-overdue');        
				        }
	  	    		}       
					$('td', row).eq(mapsDataHeader.get("DyePlan")).addClass('bg-color-azure');
					$('td', row).eq(mapsDataHeader.get("CFMPlanDate")).addClass('bg-color-azure');
					$('td', row).eq(mapsDataHeader.get("DeliveryDate")).addClass('bg-color-azure'); 
				} 
			},
			drawCallback: function( settings ) { 
//	 			console.log(settings)
			},   
			initComplete: function () { }      
	 	 });        
	var today = new Date();  
    var date = new Date((today.getMonth()+1)+'/'+today.getDate()+'/'+today.getFullYear()); 
    var startdate = moment();
    var subDate = startdate.add(7, "days");
    subDate = startdate.format("MM/DD/YYYY");      
    var sevenDayAgo = new Date(subDate) ;   
 
 
    poTable = $('#poTable').DataTable({  
    	scrollY: '400px',        
    	scrollX: true,      
    	paging: false,      
//  	    select : true,               
//  	 	scrollCollapse: true,            
//  	   	orderCellsTop : true,
// 		orderClasses : false,     	
		lengthChange: false,         	  
 	   	columns : 
 		[      
		    {"data" : "PONo"} ,         //0
		    {"data" : "POLine"},  
		    {"data" : "CreateDate"},  
		    {"data" : "RequiredDate"},  
		    {"data" : "RollNo"},  
		    {"data" : "QuantityKG"},  
		    {"data" : "QuantityMR"},    //6
		],  	      
		columnDefs :  [	   	
// 			{ targets : [ 0,1,2,3,4,5,6  ],                
// 			  	  className : 'data-custom-padding0505',    
// // 		  	 	  type: 'string'      
// 				} ,                       
		],                    
		 order: [[ 4, "asc" ]],   
		 createdRow : function(row, data, index) {     
// 			 $(row).addClass("data-custom-padding0505");
		 },   
 	 });  
//     $(row).addClass("data-custom-padding0505");
//     presetTable = $('#presetTable').DataTable({  
//     	scrollY:       '50px',         
//     	scrollX: true,    
//     	paging: false,
// //  	    select : true,             
// //  	 	scrollCollapse: true,            
// //  	   	orderCellsTop : true,
// // 		orderClasses : false, 
// 		lengthChange: false,         	  
//  	   	columns : 
//  		[      
// 		    {"data" : "PostingDate"} ,         //0
// 		    {"data" : "WorkCenter"},  
// 		],  	       
// 		columnDefs :  [	   	
// // 			{ targets : [ 0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23 ],                
// // 			  	  className : 'data-custom-padding0505',    
// // // 		  	 	  type: 'string'      
// // 				} ,            
// 		],  
//  	 });  
    dyeingTable = $('#dyeingTable').DataTable({  
    	scrollY: '160px',       
    	scrollX: true,            
    	paging: false,               
//  	 	scrollCollapse: true,            
//  	   	orderCellsTop : true,     
// 		orderClasses : false, 
// 		lengthChange: false,    
// 		"autoWidth":false,
 	   	columns : 
 	[      
		    {"data" : "PostingDate"} ,         //0
		    {"data" : "Operation"}, 
		    {"data" : "WorkCenter"}, 
		    {"data" : "DyeStatus"}, 
		    {"data" : "DeltaE"}, 
		    {"data" : "L"},                    //5
		    {"data" : "Da"}, 
		    {"data" : "Db"}, 
		    {"data" : "ST"}, 
		    {"data" : "Remark"},
		    {"data" : "Redye"},                //10
		    {"data" : "Batch"},
		    {"data" : "ColorStatus"},
		    {"data" : "ColorRemark"},          //12
		],  	      
		columnDefs :  [	   	 
			{ targets : [ 0,1,2,3,4,5,6,7,8,9,10,11,12 ],                
			  	  className : 'data-custom-padding0505',    
// 		  	 	  type: 'string'      
			} ,                    
		],     order: [[ 0, "desc" ]]
 	 });      
    fnTable = $('#fnTable').DataTable({     
    	scrollY:        '160px',        
    	scrollX: true,           
    	paging: false,    
//  	 	scrollCollapse: true,                    
//  	   	orderCellsTop : true,
// 		orderClasses : false, 
		lengthChange: false,         	  
 	   	columns : 
 		[      
			 {"data" : "PostingDate"} ,         //0
 		    {"data" : "WorkCenter"  }, 
 		    {"data" : "RollNo"}, 
 		    {"data" : "Status"}, 
 		    {"data" : "CCOperation"}, 
 		    {"data" : "CCPostingDate"},         //5
 		    {"data" : "DeltaE"}, 
 		    {"data" : "Color"}, 
 		    {"data" : "L"}, 
		    {"data" : "Da"}, 
		    {"data" : "Db"},                   //10
		    {"data" : "ST"},  
 		    {"data" : "NCDate"},
 		    {"data" : "Cause"},
 		    {"data" : "CarNo"},
 		    {"data" : "LotNo"},                //15
		],  	      
		columnDefs :  [	   	
			{ targets : [ 0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15 ],                
			  	  className : 'data-custom-padding0505',    
//		  	 	  type: 'string'      
				} ,                    
		],  order: [[ 0, "desc" ]]
 	 });     
    inspectTable = $('#inspectTable').DataTable({   
    	scrollY:       '190px',    
    	scrollX: true,               
    	paging: false,
//  	 	scrollCollapse: true,            
//  	   	orderCellsTop : true,
// 		orderClasses : false, 
		lengthChange: false,         	  
 	   	columns : 
 	[      
		    {"data" : "PostingDate"} ,         //0
		    {"data" : "Operation"},  
		    {"data" : "QuantityGreige"},  
		    {"data" : "QuantityFG"},  
		    {"data" : "Remark"},   
		],  	      
		columnDefs :  [	   	
			{ targets : [ 0,1,2,3,4 ],                
			  	  className : 'data-custom-padding0505',    
//		  	 	  type: 'string'      
				} ,            
		],  order: [[ 0, "desc" ]]
 	 });  
    packingTable = $('#packingTable').DataTable({    
    	scrollY:        '440px',     
    	scrollX: true,           
    	paging: false,
//  	 	scrollCollapse: true,            
//  	   	orderCellsTop : true,
// 		orderClasses : false, 
		lengthChange: false,         	  
 	   	columns : 
 		[      
		    {"data" : "PostingDate"} ,         //0
		    {"data" : "Quantity"},  
		    {"data" : "RollNo"},  
		    {"data" : "Grade"},  
		    {"data" : "Status"},  
		    {"data" : "QuantityKG"},           //5
		     
		],  	      
		columnDefs :  [	   	
			{ targets : [ 0,1,2,3,4,5 ],                
			  	  className : 'data-custom-padding0505',    
//		  	 	  type: 'string'      
				} ,                         
		],  order: [[ 0, "desc" ]]     
 	 });   
    sendTestQCTable = $('#sendTestQCTable').DataTable({   
    	scrollY:        '190px',    
    	scrollX: true,      
    	paging: false,     
//  	 	scrollCollapse: true,                  
//  	   	orderCellsTop : true,
// 		orderClasses : false, 
		lengthChange: false,         	  
 	   	columns : 
 		[           
		    {"data" : "SendDate"} ,         //0
		    {"data" : "RollNo"},  
		    {"data" : "Status"},  
		    {"data" : "CheckColorDate"},  
		    {"data" : "DeltaE"},  
		    {"data" : "Color"},  
		    {"data" : "Remark"},            //6
		],  	          
		columnDefs :  [	   	
			{ targets : [ 0,1,2,3,4,5,6 ],                
			  	  className : 'data-custom-padding0505',    
// 		  	 	  type: 'string'      
				} ,           
		],     order: [[ 0, "desc" ]]        
 	 });  
    workInLabTable = $('#workInLabTable').DataTable({   
    	scrollY:       '175px',    
    	scrollX: true,               
    	paging: false,
//  	 	scrollCollapse: true,            
//  	   	orderCellsTop : true,
// 		orderClasses : false, 
		lengthChange: false,         	  
	   	columns :  
		[                   
		    {"data" : "No"} ,         //0 
		    {"data" : "NOK"},  
		    {"data" : "NCDate"},  
		    {"data" : "LotNo"},   
		    {"data" : "L"},            //5
		    {"data" : "Da"},
		    {"data" : "Db"},
		    {"data" : "ST"},
		    {"data" : "ReceiveDate"},  
		    {"data" : "Remark"},       //  9
		],  	      
		columnDefs :  [	   	
			{ targets : [ 0,1,2,3,4,5,6,7,8,9],                
			  	  className : 'data-custom-padding0505',    
//		  	 	  type: 'string'      
				} ,            
		],  order: [[ 0, "desc" ]]
 	 });       
     
    waitTestTable = $('#waitTestTable').DataTable({   
    	scrollY:        '175px',       
    	scrollX: true,      
    	paging: false,      
// 		orderClasses : false, 
		lengthChange: false,         	  
 	   	columns :  
 		[           
		    {"data" : "No"} ,         //0
		    {"data" : "DateInTest"},  
		    {"data" : "DateOutTest"},  
		    {"data" : "Status"},  
		    {"data" : "Remark"},  
		],  	          
		columnDefs :  [	   	
			{ targets : [ 0,1,2,3,4 ],                
			  	  className : 'data-custom-padding0505',    
// 		  	 	  type: 'string'      
				} ,           
		],     order: [[ 0, "desc" ]]        
 	 }); 
     
    cfmTable = $('#cfmTable').DataTable({   
    	scrollY:        '175px',    
    	scrollX: true,      
    	paging: false,      
// 		orderClasses : false, 
		lengthChange: false,         	  
 	   	columns :  
 		[           
		    {"data" : "CFMNo"} ,         //0
		    {"data" : "CFMNumber"},  
		    {"data" : "CFMSendDate"},  
		    {"data" : "RollNo"},  
		    {"data" : "RollNoRemark"},    
		    {"data" : "L"},            //4
		    {"data" : "Da"},
		    {"data" : "Db"},
		    {"data" : "ST"},
		    {"data" : "SaleOrder"},   //9
		    {"data" : "SaleLine"}, 
		    {"data" : "CFMAnswerDate"},
		    {"data" : "CFMStatus"},
		    {"data" : "CFMRemark"},     //13
		    {"data" : "NextLot"},
		    {"data" : "SOChange"},
		    {"data" : "SOChangeQty"},
		    {"data" : "SOChangeUnit"},  //17    
		],  	          
		columnDefs :  [	   	   
			{ targets : [ 0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17 ],                
			  	  className : 'data-custom-padding0505',    
// 		  	 	  type: 'string'      
				} ,           
		],     order: [[ 0, "desc" ]]        
 	 });  
    
    
	  saleTable = $('#saleTable').DataTable({   
    	scrollY:        '175px',    
    	scrollX: true,      
    	paging: false,      
// 		orderClasses : false, 
		lengthChange: false,         	  
 	   	columns :  
 		[           
		    {"data" : "BillDate"} ,         //0
		    {"data" : "BillQtyPerSale"},  
		    {"data" : "SaleOrder"},  
		    {"data" : "SaleLine"},  
		    {"data" : "BillQtyPerStock"}, 
		],  	          
		columnDefs :  [	   	
			{ targets : [ 0,1,2,3,4 ],                
			  	  className : 'data-custom-padding0505',    
// 		  	 	  type: 'string'      
				} ,           
		],     order: [[ 0, "desc" ]]        
 	 });   
	  saleInputTable = $('#saleInputTable').DataTable({   
	    	scrollY:        '175px',    
	    	scrollX: true,      
	    	paging: false,      
//	 		orderClasses : false, 
			lengthChange: false,         	  
	 	   	columns :  
	 		[           
			    {"data" : "BillDate"} ,         //0
			    {"data" : "BillQtyPerSale"},  
			    {"data" : "SaleOrder"},  
			    {"data" : "SaleLine"},  
			    {"data" : "BillQtyPerStock"}, 
			],  	          
			columnDefs :  [	   	
				{ targets : [ 0,1,2,3,4 ],                
				  	  className : 'data-custom-padding0505',    
//	 		  	 	  type: 'string'      
					} ,           
			],     order: [[ 0, "desc" ]]        
	 	 });      
	    submitDateTable = $('#submitDateTable').DataTable({   
	    	scrollY:        '175px',    
	    	scrollX: true,      
	    	paging: false,      
//	 		orderClasses : false, 
			lengthChange: false,         	  
	 	   	columns :  
	 		[           
			    {"data" : "CreateDate"} ,         //0
			    {"data" : "PlanDate"},  
			    {"data" : "InputFrom"},   
			],  	          
			columnDefs :  [	   	
				{ targets : [ 0,1,2 ],                
				  	  className : 'data-custom-padding0505',    
//	 		  	 	  type: 'string'      
					} ,           
			],
			ordering: false,
// 			order: [[ 0, "desc" ]]        
	 	 });     
	    submitDateTable.on( 'order.dt search.dt', function () {
	    	submitDateTable.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
	            cell.innerHTML = i+1;
	        } );    
	    } ).draw();
	    ncTable = $('#ncTable').DataTable({   
	    	scrollY:        '175px',    
	    	scrollX: true,      
	    	paging: false,      
//	 		orderClasses : false, 
			lengthChange: false,         	  
	 	   	columns :   
	 		[           
			    {"data" : "No"} ,         //0
			    {"data" : "NCDate"},  
			    {"data" : "CarNo"},  
			    {"data" : "Quantity"},  
			    {"data" : "Unit"}, 
			    {"data" : "NCFrom"}, 
			    {"data" : "Remark"},      //6
			],  	          
			columnDefs :  [	   	
				{ targets : [ 0,1,2,3,4,5,6 ],                
				  	  className : 'data-custom-padding0505',    
//	 		  	 	  type: 'string'      
					} ,           
			],     order: [[ 0, "desc" ]]        
	 	 });    
	    receipeTable = $('#receipeTable').DataTable({   
	    	scrollY:        '175px',         
	    	scrollX: true,      
	    	paging: false,      
//	 		orderClasses : false,         
			lengthChange: false,         	  
	 	   	columns :   
	 		[               
			    {"data" : "No"} ,         //0
			    {"data" : "LotNo"},     
			    {"data" : "PostingDate"},  
			    {"data" : "Receipe"},   
			],  	          
			columnDefs :  [	   	
				{ targets : [ 0,1,2,3 ],                
				  	  className : 'data-custom-padding0505',    
//	 		  	 	  type: 'string'      
					} ,               
			],     order: [[ 0, "desc" ]]        
	 	 });
	// SEARCH BY FILTER UNDER COLUMN NAME : BOAT 
// 	$("#MainTable thead").on('keyup', '.monitor_search', function() {
// 		console.log(this.value)    
// 		MainTable.column($(this).data('index')).search(this.value).draw(); 
// 	});           
	$(".dataTables_scrollHead").on('keyup', '.monitor_search', function() {         
		let searchVal = this.value;      
		let indexAfterReCol =  MainTable.colReorder.transpose( $(this).data('index') );
// 		console.log($(this),$(this).data('index'),indexAfterReCol)
// 		11 , 14-25 date
// 		13 DUEDATE     
		let regrex = '';   
		let splitText = searchVal.split('/');
		let size = splitText.length;
		if(searchVal  == ''){       
			MainTable.column(indexAfterReCol).search(searchVal).draw();  
// 			MainTable.column(indexAfterReCol).search( '^', true, false ).draw();  
		}         
		else if(searchVal  == ' '){                
// 			var rows = MainTable.Select($(this).data('index')+" = ''"); 
// 			MainTable.column($(this).data('index')).search( '^$', true, false ).draw();; 
			MainTable.column(indexAfterReCol).search( '^$', true, false ).draw();  
		}           
		else if(indexAfterReCol == 11 || indexAfterReCol == 14 || indexAfterReCol == 15 ||indexAfterReCol == 16 ||
				indexAfterReCol == 17 ||indexAfterReCol == 18 ||indexAfterReCol == 20 ||indexAfterReCol == 21 ||
				indexAfterReCol == 22 ||indexAfterReCol == 23 ||indexAfterReCol == 24 ||indexAfterReCol == 25 ||
				indexAfterReCol == 26  ){
			if(size == 1){ regrex = '^'+searchVal+'';  }
			else if(size == 2){
				if(splitText[0] == ''){ regrex = ''+searchVal+'$';  }
				else if(splitText[1] == ''){ regrex = '^'+searchVal+'';  }
				else{ regrex = '^'+searchVal+'$';  } 
			}    
			else{regrex = ''+searchVal+''; }
			MainTable.column(indexAfterReCol).search( regrex, true, false ).draw();  
// 			regrex = '^'+searchVal+'$';
// 			MainTable   
// 			.column(indexAfterReCol)
// 			.search('^([/][0-9]{1}|[/][0-9]{2}|[0-9]{1}[/][0-9]{1}|[0-9]{1}[/][0-9]{2}|[0-9]{2}[/][0-9]{2}|[0-9]{2}[/][0-9]{2})$', true, false ).draw();
// 	  		.search( regrex, true, false ).draw();  
// 			console.log(search('^([/][0-9]{1}|[/][0-9]{2}|[0-9]{1}[/][0-9]{1}|[0-9]{1}[/][0-9]{2}|[0-9]{2}[/][0-9]{2}|[0-9]{2}[/][0-9]{2})$', true, false ))
		}    
		else if(indexAfterReCol == 13){
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
// 			MainTable.column($(this).data('index')).search( '^'+searchVal+'$', true, false ).draw(); 
			MainTable.column(indexAfterReCol).search(searchVal).draw();  
		}
		
	});      
// 	 var presetTable ;var dyeingTable;var fnTable;var inspectTable;var packingTable;var sendTestQCTable; 
	$("#MainTable_filter").hide();  
// 	$("#presetTable_filter").hide();  
	$("#dyeingTable_filter").hide();  
	$("#fnTable_filter").hide();  
	$("#inspectTable_filter").hide();  
	$("#packingTable_filter").hide();  
	$("#sendTestQCTable_filter").hide(); 
	$("#poTable_filter").hide();	
	
	$("#workInLabTable_filter").hide();  $("#waitTestTable_filter").hide();  $("#cfmTable_filter").hide();  
	$("#saleTable_filter").hide();  $("#saleInputTable_filter").hide();  $("#submitDateTable_filter").hide();  
	$("#ncTable_filter").hide();  $("#receipeTable_filter").hide();  
	
	$("#workInLabTable_info").hide();$("#waitTestTable_info").hide();$("#cfmTable_info").hide();
	$("#saleTable_info").hide();$("#saleInputTable_info").hide();$("#submitDateTable_info").hide();
	$("#ncTable_info").hide();$("#receipeTable_info").hide(); 
	   
// 	$("#MainTable_info").hide();      
// 	$("#presetTable_info").hide();   
	$("#dyeingTable_info").hide();   
	$("#fnTable_info").hide();   
	$("#inspectTable_info").hide();   
	$("#packingTable_info").hide();   
	$("#sendTestQCTable_info").hide();      
	$("#poTable_info").hide();  
	
	$('#multi_userStatus').selectpicker();     
	$('#multi_colVis').selectpicker();   
	$('#multi_cusName').selectpicker();     
	$('#multi_cusShortName').selectpicker(); 
	$('#multi_division').selectpicker();  
	colList = JSON.parse('${ColList}'); 
	cusNameList = JSON.parse('${CusNameList}');   	
	cusShortNameList = JSON.parse('${CusShortNameList}'); ;   
	userStatusList = JSON.parse('${UserStatusList}');       
	divisionList = JSON.parse('${DivisionList}');       
	columnsHeader = MainTable.settings().init().columns;  
	var saleNumberList = JSON.parse('${SaleNumberList}');
	 
  	addSelectOption(saleNumberList)
	addUserStatusOption(userStatusList );      
  	addDivisionOption(divisionList );      
	addCusNameOption(cusNameList );      
	addCusShortNameOption(cusShortNameList );       
	addColOption(columnsHeader )  
	settingColumnOption(columnsHeader, colList); 
	     
	$('#multi_userStatus option').attr("selected","selected");
	$('#multi_userStatus').selectpicker('refresh');
	$('#multi_cusName option').attr("selected","selected");
	$('#multi_cusName').selectpicker('refresh');
	$('#multi_cusShortName option').attr("selected","selected");
	$('#multi_cusShortName').selectpicker('refresh');
	$('#multi_division option').attr("selected","selected");
	$('#multi_division').selectpicker('refresh');
	
 
    $('a[data-toggle="tab"]').on('shown.bs.tab', function (e) { 
//     	MainTable.columns.adjust(); 
    	poTable.columns.adjust();   
//     	presetTable.columns.adjust();              
    	dyeingTable.columns.adjust(); 
    	fnTable.columns.adjust(); 
    	inspectTable.columns.adjust(); 
    	packingTable.columns.adjust(); 
    	sendTestQCTable.columns.adjust(); 
    	
    	workInLabTable.columns.adjust(); waitTestTable.columns.adjust(); cfmTable.columns.adjust(); 
    	saleTable.columns.adjust(); saleInputTable.columns.adjust(); submitDateTable.columns.adjust(); 
    	ncTable.columns.adjust(); receipeTable.columns.adjust(); 
    });          
	$('.modal').on('shown.bs.modal', function() {   
// 		MainTable.columns.adjust(); 
		poTable.columns.adjust();         
// 		presetTable.columns.adjust();   
		dyeingTable.columns.adjust();   	
		fnTable.columns.adjust(); 
		inspectTable.columns.adjust(); 
		packingTable.columns.adjust(); 
		sendTestQCTable.columns.adjust(); 
    	workInLabTable.columns.adjust(); waitTestTable.columns.adjust(); cfmTable.columns.adjust(); 
    	saleTable.columns.adjust(); saleInputTable.columns.adjust(); submitDateTable.columns.adjust(); 
    	ncTable.columns.adjust(); receipeTable.columns.adjust(); 
	})  
	preLoaderHandler( preloader)   
});  
 
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
	var dueDate = document.getElementById("input_dueDate").value .trim(); 
	var material = document.getElementById("input_material").value .trim(); 
	var labNo = document.getElementById("input_labNo").value .trim();    
	var userStatus = $('#multi_userStatus').val(); 
	var customer = $('#multi_cusName').val();    
	var customerShort = $('#multi_cusShortName').val();
	var division = $('#multi_division').val(); 
// 	console.log(userStatus)    
	var deliStatus = document.getElementById("SL_delivStatus").value .trim();      
	var dmCheck = document.getElementById("check_DM").checked;         
	var exCheck = document.getElementById("check_EX").checked;  
	var hwCheck = document.getElementById("check_HW").checked;  
	var dist = "";     
	 var saleStatus = document.querySelector('input[name="saleStatusRadio"]:checked').value;
	 if( dmCheck ){ dist = "DM";}
	 if( exCheck ){ if(dist != "") {dist = dist + "|" } dist = "EX";}     
	 if( hwCheck ){ if(dist != "") {dist = dist + "|" } dist = "HW";}     
// 	if(  customer.length == 0 ||  customerShort.length == 0 ||  userStatus.length == 0){
// 		swal({
//    		    title: 'Warning',
//    		    text: 'Customer CustomerShortName and UserStatus need atleast 1 selected rwo.',
//    		    icon: 'warning',
//    		    timer: 1000,
//    		    buttons: false,
//    		})
// 	}
// 	else if( saleOrder == '' && article == '' && prdOrder == '' && saleNumber == '' && SaleOrderDate == '' && 
// 	   designNo == '' && prdOrderDate == '' && material == '' && labNo == '' && deliStatus == '' )  {     
	if(  (customer.length == 0 ||  customerShort.length == 0 ||  userStatus.length == 0 || division.length  == 0) 
  			)  { 
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

	else if (dist == ''){
		swal({
   		    title: 'Warning',
   		    text: 'Need to choose  Dist. from check box.',
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
function createJsonData(){ 
// 	var customer = document.getElementById("input_customer").value .trim();  
// 	var customerShort = document.getElementById("input_customerShortName").value .trim();
	 
	var saleOrder = document.getElementById("input_saleOrder").value .trim();  
	var article = document.getElementById("input_article").value .trim();  
	var prdOrder = document.getElementById("input_prdOrder").value .trim();
	var saleNumber = document.getElementById("SL_saleNumber").value .trim();
	var saleOrderDate = document.getElementById("input_saleOrderDate").value .trim();
	var designNo = document.getElementById("input_designNo").value .trim(); 
	var prdOrderDate = document.getElementById("input_prdOrderDate").value .trim(); 
	var material = document.getElementById("input_material").value .trim(); 
	var labNo = document.getElementById("input_labNo").value .trim();      
// 	var userStatus = document.getElementById("multi_userStatus").value;    
	var userStatus = $('#multi_userStatus').val();    
	var customer = $('#multi_cusName').val();
	var customerShort = $('#multi_cusShortName').val();
// 	var customer = document.getElementById("multi_cusName").value .trim();
// 	var customerShort = document.getElementById("multi_cusShortName").value .trim(); 
	var deliStatus = document.getElementById("SL_delivStatus").value .trim();  
	var dueDate = document.getElementById("input_dueDate").value .trim(); 
	var dmCheck = document.getElementById("check_DM").checked;  
	var exCheck = document.getElementById("check_EX").checked;  
	var hwCheck = document.getElementById("check_HW").checked;     
	var dist = "";    
	var division = $('#multi_division').val(); 
	 var saleStatus = document.querySelector('input[name="saleStatusRadio"]:checked').value;
	 if( dmCheck ){ dist = "DM";}
	 if( exCheck ){ if(dist != "") {dist = dist + "|" } dist = dist + "EX";}       
	 if( hwCheck ){ if(dist != "") {dist = dist + "|" } dist = dist + "HW";}
	var json = '{'+   
// 		"CustomerName":'+JSON.stringify(customer)+ 
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
	   ',"CustomerShortNameList":'+JSON.stringify(customerShort)+   
	   ',"DivisionList":'+JSON.stringify(division)+   
	   ',"DeliveryStatus":'+JSON.stringify(deliStatus)+            
	   ',"SaleStatus":'+JSON.stringify(saleStatus)+  
	   ',"DistChannel":'+JSON.stringify(dist) + 
	   ',"DueDate":'+JSON.stringify(dueDate) + 
	   '} ';            
	   return json; 
}
function exportCSV(data){ 
    var createXLSLFormatObj = []; 
    /* XLS Head Columns */ 
    
//     var xlsHeader = [
//    	"SO No.",
//    	"SO Line",
//     "Article No",
// 	"Design No",
// 	"ATT Color",
// 	"Cust.Color",   
// 	"Order Qty.", 
// 	"Shipped Qty.",
// 	"Unit",
// 	"Prod.No.",
// 	"Prod.Qty.",
// 	"Greige In",	
// 	"User Status",
// 	"Due Date",
// 	"Prepare",
// 	"PS",
// 	"Dye[Plan]",
// 	"Dye[Actual]",
// 	"Dryer Date",   
// 	"FN",   
// 	"Inspect",
// 	"วันนัด CF[Actual]",
// 	"วันนัด CF[Plan]",
// 	"วันนัดส่งของ",//วันนัดส่งของ         
// 	"Shipping" ];        
    let xlsHeader = Array.from( mapsTitleHeader.keys() );
//     let xlsHeader = Array.from(map, ([name, value]) => ({ name, value })); 
    /* XLS Rows Data */    
    xlsHeader.push("CustomerName");
    xlsHeader.push("CustomerShortName");      
    soLineTmpExcel = '';
    soTmpExcel = '';
    var xlsRows = data   
    let indexArray = 0,colType = '';
    createXLSLFormatObj.push(xlsHeader);     
    var sizeMap = mapsDataHeader.size;
    let caseDupli = 0;
    soLineTmpExcel = '' ;soTmpExcel = '';
    $.each(xlsRows, function(index, value) {    
        var innerRowData = [];         
        caseDupli = checkSaleOrderLine( value);
        $.each(value, function(data, val) {     
            if(mapsDataHeader.size != 0){                   
            	 indexArray = mapsDataHeader.get(data); 
            	 colType = mapsColumnHeader.get(data);   
			  	if(indexArray !== undefined){             
					if (colType === undefined){  
						innerRowData[indexArray] = val;      
					}
					else if (colType == 'num'){   
						if(data == 'SaleQuantity' || data == 'BillQuantity'){  
							if(caseDupli == 0 || caseDupli == 2){   
								if(val.trim() == ''){ innerRowData[indexArray] = '';  }
		 						else{ innerRowData[indexArray] = parseFloat(val.replace(/,/g, '')) ; } 
							}      
							else{ innerRowData[indexArray] = ''; }
						}
						else{
							if(val == ''){ innerRowData[indexArray] = parseFloat(0);       }
	 						else{ innerRowData[indexArray] = parseFloat(val.replace(/,/g, '')) ; } 
						} 
					}      
					else if (colType == 'date-euro'){       
						if(val == ''){ innerRowData[indexArray] = ''   ;   }
 						else{ innerRowData[indexArray] =stringToDate(val)   ; } 
					}                
			  	}   
			  	else if(data == 'CustomerName'){innerRowData[sizeMap] = val  ;      }
			  	else if(data == 'CustomerShortName'){innerRowData[sizeMap+1] =val ; } 
            }
        });  
        createXLSLFormatObj.push(innerRowData);
    }); 
    /* File Name */
    var filename = "PCMS-Main.xlsx"; 
    /* Sheet Name */  
    var ws_name = "Main";     
    if (typeof console !== 'undefined') console.log(new Date());
    var wb = XLSX.utils.book_new(),
//  ws = XLSX.utils.aoa_to_sheet(createXLSLFormatObj);   
   	ws = XLSX.utils.aoa_to_sheet(createXLSLFormatObj, {dateNF:"dd/MM/yyyy",rawNumbers: true});  
    /* Add worksheet to workbook */ 
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
function getPrdDetailByRow(arrayTmp) {      
	$.ajax({   
		type: "POST",
		contentType: "application/json",  
		data: JSON.stringify(arrayTmp),      
		url: "Main/getPrdDetailByRow", 
		success: function(data) {      
// 			MainTable.clear();          
// 			MainTable.rows.add(data);     
// 			MainTable.draw();   
			setModalDetail(data);   
		},   
		error: function(e) {
			swal("Fail", "Please contact to IT", "error");
		},
		done: function(e) {       
		}   	
	});      
}     
function clearInput(){
	document.getElementById("input_saleOrder").value = '';  
	document.getElementById("input_article").value   = '';  
	document.getElementById("input_prdOrder").value  = '';     
// 	document.getElementById("input_saleOrderDate").value = '';  
	document.getElementById("input_designNo").value  = '';  
// 	document.getElementById("input_prdOrderDate").value  = '';  
	document.getElementById("input_material").value  = '';         
	document.getElementById("input_labNo").value     = '';  
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
function searchByDetailToServer(arrayTmp) {   
// 	console.log(arrayTmp)
	$.ajax({
		type: "POST",  
		contentType: "application/json",   
		data: JSON.stringify(arrayTmp),           
		url: "Main/searchByDetail", 
		success: function(data) {  
// 			getVisibleColumnsTable(); 
// 			console.log(new Date())  
			MainTable.clear();        
			MainTable.rows.add(data);           
			MainTable.draw();     
// 			console.log(new Date(	))  
		},   
		error: function(e) {
			swal("Fail", "Please contact to IT", "error");
		},
		done: function(e) {       
		}   	
	});   
}      
function setModalDetail(data){
    
   	var innnerText = data[0];       
//    	console.log(innnerText)
   	poTable.clear();    	     
	if(innnerText.poDetailList.length == 0){       }     
	else{ 
		poTable.rows.add(innnerText.poDetailList);
		document.getElementById("input_poDefault").value = innnerText.poDetailList[0].PODefault;    
		document.getElementById("input_poLineDefault").value = innnerText.poDetailList[0].POLineDefault;    
		document.getElementById("input_poCreateDateDefault").value = innnerText.poDetailList[0].POPostingDateDefault;    
	}  
	poTable.draw();     
// 	presetTable.clear();    	   
	if(innnerText.presetDetailList.length == 0){       }     
	else{ 
		document.getElementById("input_presetDate").value = innnerText.presetDetailList[0].PostingDate;    
		document.getElementById("input_presetWorkCenter").value = innnerText.presetDetailList[0].WorkCenter; 
	}  
// 	presetTable.draw();            
	 
	
	dyeingTable.clear();    	   
	if(innnerText.dyeingDetailList.length == 0){       }     
	else{ dyeingTable.rows.add(innnerText.dyeingDetailList); }  
	dyeingTable.draw();                 
	
	sendTestQCTable.clear();    	   
	if(innnerText.sendTestQCDetailList.length == 0){       }     
	else{ sendTestQCTable.rows.add(innnerText.sendTestQCDetailList); }  
	sendTestQCTable.draw();   
	
	fnTable.clear();    	   
	if(innnerText.finishingDetailList.length == 0){       }     
	else{ fnTable.rows.add(innnerText.finishingDetailList); }  
	fnTable.draw();   
	
	inspectTable.clear();    	   
	if(innnerText.inspectDetailList.length == 0){       }     
	else{ inspectTable.rows.add(innnerText.inspectDetailList); }  
	inspectTable.draw();   
	
	packingTable.clear();    	   
	if(innnerText.packingDetailList.length == 0){       }     
	else{ packingTable.rows.add(innnerText.packingDetailList); }  
	packingTable.draw();   
	 
	workInLabTable.clear();    	   
	if(innnerText.workInLabDetailList.length == 0){       }     
	else{ workInLabTable.rows.add(innnerText.workInLabDetailList); }  
	workInLabTable.draw();  
	
	waitTestTable.clear();    	   
	if(innnerText.waitTestDetailList.length == 0){       }     
	else{ waitTestTable.rows.add(innnerText.waitTestDetailList); }  
	waitTestTable.draw();  
	
	cfmTable.clear();    	   
	if(innnerText.cfmDetailList.length == 0){       }     
	else{ cfmTable.rows.add(innnerText.cfmDetailList); }  
	cfmTable.draw();  
	
	saleTable.clear();    	       
	if(innnerText.saleDetailList.length == 0){       }     
	else{ saleTable.rows.add(innnerText.saleDetailList); }  
	saleTable.draw();     
	
	submitDateTable.clear();    	   
	if(innnerText.submitDateDetailList.length == 0){       }     
	else{ submitDateTable.rows.add(innnerText.submitDateDetailList); }  
	submitDateTable.draw();  
	
	ncTable.clear();    	   
	if(innnerText.ncDetailList.length == 0){       }     
	else{ ncTable.rows.add(innnerText.ncDetailList); }  
	ncTable.draw();  
	
	receipeTable.clear();    	   
	if(innnerText.receipeDetailList.length == 0){       }     
	else{ receipeTable.rows.add(innnerText.receipeDetailList); }  
	receipeTable.draw();  
	  	
	 
	document.getElementById("input_greigeDesign").value = innnerText.GreigeDesign  ;    
	document.getElementById("input_greigeArticle").value = innnerText.GreigeArticle;    
	document.getElementById("input_prdOrderCM").value = innnerText.ProductionOrder;    
	document.getElementById("input_lotNoCM").value = innnerText.LotNo;    
	document.getElementById("input_batchCM").value = innnerText.Batch;    
	document.getElementById("input_labNoCM").value = innnerText.LabNo;    
	document.getElementById("input_dateCM").value = innnerText.PrdCreateDate;    
	document.getElementById("input_dueDateCM").value = innnerText.DueDate;    
	document.getElementById("input_saleCM").value = innnerText.SaleOrder+'/'+innnerText.SaleLine;    
	document.getElementById("input_poCM").value = innnerText.PurchaseOrder;    
	document.getElementById("input_articleFGCM").value = innnerText.ArticleFG;    
	document.getElementById("input_designFGCM").value = innnerText.DesignFG;    
	document.getElementById("input_customerNameCM").value = innnerText.CustomerName;    
	document.getElementById("input_customerShortNameCM").value = innnerText.CustomerShortName;      
	document.getElementById("input_shadeCM").value = innnerText.Shade;    
	document.getElementById("input_bookNoCM").value = innnerText.BookNo;    
	document.getElementById("input_centerCM").value = innnerText.Center;    
	document.getElementById("input_matNoCM").value = innnerText.MaterialNo;    
	document.getElementById("input_volumnCM").value = innnerText.Volumn+" "+innnerText.SaleUnit;    
	document.getElementById("input_stdUnitCM").value = innnerText.STDUnit;    
	   
// 	console.log(innnerText)
	document.getElementById("input_colorCM").value = innnerText.Color + ' ( '+ innnerText.ColorCustomer +' ) ';        
	document.getElementById("input_planGreigeDateCM").value = innnerText.PlanGreigeDate;   
	document.getElementById("input_refPrdCM").value = innnerText.RefPrd;   
	document.getElementById("input_greigeInDateCM").value = innnerText.GreigeInDate;  
	document.getElementById("input_awarenessBCCM").value = innnerText.BCAware;   
	document.getElementById("input_orderPuangCM").value = innnerText.OrderPuang;   
	document.getElementById("input_userStatusCM").value = innnerText.UserStatus;   
	document.getElementById("input_labStatusCM").value = innnerText.LabStatus;    
	document.getElementById("input_awarenessBCCM").value = innnerText.BCAware;    
	document.getElementById("input_sendCFMPlanCM").value = innnerText.CFMDatePlan;   
	document.getElementById("input_sendShipPlanCM").value = innnerText.DeliveryDate;   
	document.getElementById("input_BCDateCM").value = innnerText.BCDate;   
	document.getElementById("input_remarkCM").value = innnerText.RemarkOne+" "+innnerText.RemarkTwo+" "+innnerText.RemarkThree;   
	document.getElementById("input_remarkAfterCloseCM").value = innnerText.RemAfterCloseOne+" "+innnerText.RemAfterCloseTwo+" "+innnerText.RemAfterCloseThree;    
}    
function goToLBMS(tblData,pUserId,data){     
// 	let obj = createEncryptObj(pUserId);      
	var prdOrder = tblData[0].ProductionOrder
	var article = tblData[0].ArticleFG 
	var color = tblData[0].Color 
	$.ajax({   
  	    url: urlLBMS,
  	    type : 'GET',   
  	    data : { 
  	    	"comeFrom": data.Encrypted  ,   
//   	        "comeFrom": "PCMS"
	    	},   
  	    success : function(data) {   
//   	    	var url = "http:/pcms.a-tech.co.th:8080/LBMS/LabHistory"; 
  	  		var url = urlLBMSObj; 
  	    	var tab = window.open(url );  //var tab = window.open(url, '_blank').focus();
  	    	tab.onload = function() {    
				tab.document.getElementById('input_article').value = article;     
			 	tab.document.getElementById('input_color').value = color  ;  //'S2A001'  
				tab.searchHistory();     
				tab.document.getElementById('nav-prd-tab').click();  
// 				setTimeout(function(){
// 					tab.document.getElementById('input_prdMS0').value = "ddddddddd"  ;    
// 					}, 2000);   
				setTimeout(function(){     
// 					tab.document.getElementById('byPrdTable_filter').value = prdOrder  ;
// 					tab.$("#byPrdTable_filter input").value(prdOrder);     
					tab.$('#byPrdTable').DataTable().search(prdOrder).draw();
					},500);                      
// 				console.log( tab.$('#byPrdTable').DataTable()  )  
   	    	};     
	   		tab.addEventListener('load', (event) => { 

   	    	});        
   	    } ,	
	    error: function(e) {
			swal("Fail", "Please contact to IT", "error");
			console.log(e)
		},
		done: function(e) {       
		}   
   	}); 
}    
function goToSFC(tblData,pUserId,data){          
	var prdOrder = tblData[0].ProductionOrder  
	$.ajax({    
	    url: urlSFC,
	    type : 'GET',      
	    data : {     
			"comeFrom": data.Encrypted  ,   
// 		    "comeFrom":"PCMS"
	    },               
	    success : function(data) {     
  	    	var url = urlSFCObj; 
  	    	var tab = window.open(url );  //var tab = window.open(url, '_blank').focus();
  	    	tab.onload = function() {                
					tab.document.getElementById('input_searchProductionOrder').value = prdOrder;       
					tab.searchByPrdOrder(prdOrder);                
   	    	};        
	    },	
	    error: function(e) {
			swal("Fail", "Please contact to IT", "error");
			console.log(e)
		},
		done: function(e) {       
		}   	
	});       
} 
function goToInspect(tblData,pUserId,data){ 
// 	let obj = createEncryptObj(pUserId);    
	var prdOrder = tblData[0].ProductionOrder
// 	console.log(tblData[0].ProductionOrder,data.Encrypted)
	$.ajax({
// 	    url: "http://localhost:8080/InspectSystem/search/home.html",   
	    url: urlInspect,
	    type : 'GET',      
// 	    async : false,
	    data : {
	    	"comeFrom": data.Encrypted  ,      	
// 	    	"comeFrom": "PCMS"
	    },    
	    success : function(data) { 
// 	    	var url = "http://localhost:8080/InspectSystem/search/home.html";  
	    	var url = urlInspectObj; 
	    	var tab = window.open(url );
	    	tab.onload = function() {    
	    	      tab.document.getElementById('prdNumber').value = prdOrder  ;  //'S2A001'
	    	      tab.document.getElementById('btnSearch').click();     
    	    };      
	    },	
	    error: function(e) {
			swal("Fail", "Please contact to IT", "error");
			console.log(e)
		},
		done: function(e) {       
		}   
	});      
// 	test3T(); 
}  
function goToQCMS(tblData,pUserId,data){ 
// 	let obj = createEncryptObj(pUserId);    
		var article = tblData[0].ArticleFG
		var lotNo = tblData[0].LotNo
		var color = tblData[0].Color 
		$.ajax({
//	 	    url: "http://localhost:8080/InspectSystem/search/home.html",   
		    url: urlQCMSObj,
		    type : 'GET',      
//	 	    async : false,
		    data : {    
		    	"comeFrom": data.Encrypted  ,   
// 		    		    	"comeFrom": "PCMS"
		    },    
		    success : function(data) { 
//	 	    	var url = "http://localhost:8080/InspectSystem/search/home.html";  
		    	var url = urlQCMSObj; 
		    	var tab = window.open(url );
		    	tab.onload = function() {    
		    	      tab.document.getElementById('article').value = article  ;  //'S2A001'
		    	      tab.document.getElementById('lotNumber').value = lotNo  ;  //'S2A001'
		    	      tab.document.getElementById('color').value = color  ;  //'S2A001' 
		    	      setTimeout(function(){     
  
			    	      	tab.document.getElementById('btnSearchRequest').click();  
						},500);         
	    	    };      
		    }    
		});      
//	 	test3T(); 
	} 
</script>      
<script type="text/javascript">  
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
 
function saveInputDateToServer(arrayTmp) {   
// 	console.log(arrayTmp)
	$.ajax({   
		type: "POST",  
		contentType: "application/json",  
		data: JSON.stringify(arrayTmp),         
		url: "Main/saveInputDate", 
		success: function(data) {  
			if(data.length > 0){
				var bean = data[0]; 
				if(bean.IconStatus == 'I'){
					swal({
						title: "Success",    
					 	text: bean.StatusSystem ,
						icon: "info",
						button: "confirm",
   					});  
				}
				else{  
					swal({   
						title: "Warning ",    
					 	text: bean.StatusSystem  , 
			   		    icon: 'warning',
			   		    timer: 1000,
			   		    buttons: false,
			   		})
				}  
			}
// 			MainTable.clear();           
// 			MainTable.rows.add(data);      
// 			MainTable.columns.adjust().draw();       
		},   
		error: function(e) {
			swal("Fail", "Please contact to IT", "error");
		},
		done: function(e) {       
		}   	
	});   
}    
function settingColumnOption(columns ,colVisible){  
	if(colVisible == null){
		$('#multi_colVis option').attr("selected","selected");
		$('#multi_colVis').selectpicker('refresh');
	}  
	else{   
// 		$('#multi_colVis').selectpicker();   
// 		columns = MainTable.settings().init().columns; 
// 		console.log(columns)
// 		var colVisible = ["Division","SaleOrder","PurchaseOrder"];	
// 		addColOption(columns ) 
		setColVisibleTable(columnsHeader,colVisible);    
		$('#multi_colVis').selectpicker('val', colVisible);     
	}
	getVisibleColumnsTable()     
}
        
function getVisibleColumnsTable() {   
// 	columnsHeader = MainTable.settings().init().columns;
   	mapsDataHeader.clear();   
   	mapsTitleHeader.clear();
   	mapsColumnHeader.clear();
// 	console.log('all_columns', all_columns);
	var visible_columns = [];        
	var check = '';
	var count = 0;
	for (var i in columnsHeader) {        
		check = (MainTable.column( i ).visible() === true ? 'visible' : 'not visible');
		if((check == 'visible'      
// 				&& all_columns[i].data == 'DueDate'
				)){    
// 			visible        
			mapsColumnHeader.set(columnsHeader[i].data, columnsHeader[i].type); 
			mapsTitleHeader.set(columnsHeader[i].title, columnsHeader[i].type); 
			mapsDataHeader.set(columnsHeader[i].data, count);
			count = count + 1  
// 			console.log(columnsHeader[i])
		}   
		else{        
// 			unvisible 
		} 
	}    
// 	console.log(mapsDataHeader )
}     
function setColVisibleTable(mainCol,colVisible) {     
	let index ; 
	let colName = '';  
   let i = 0;
//    console.log(mainCol)   
//    console.log(colVisible)
	for (i =  0; i < mainCol.length ; i++) {           
		colName = mainCol[i].data;     
		check = colVisible.includes(colName);  
		if(check === true){ 
			MainTable.column( i ).visible(true  )   ;        
		}    
		else{      
			MainTable.column( i ).visible( false )  
			}  
	}   
	MainTable.columns.adjust().draw( false ); // adjust column sizing and redraw

}    
function saveColSettingToServer(arrayTmp) {   
// 	console.log(arrayTmp)
	$.ajax({     
		type: "POST",  
		contentType: "application/json",  
		data: JSON.stringify(arrayTmp),      
		url: "Main/saveColSettingToServer", 
		success: function(data) {  
// 			MainTable.clear();           
// 			MainTable.rows.add(data);      
// 			MainTable.columns.adjust().draw();   
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
	var sel = document.getElementById('multi_colVis');
	for (i = sel.length - 1; i >= 0; i--) {
		sel.remove(i);
	}             
	var size = colName.length;
	for (var i = 0; i < size; i++) {		   
		 var resultData = colName[i]; 	   
// 		 console.log(resultData)   
		 var opt = document.createElement('option');
	     opt.appendChild(document.createTextNode(i));
		 opt.text  = (i+1)+" : "+resultData.title;
		 opt.value = resultData.data;  
		 sel.appendChild(opt);              
	}         
// 	$('#multi_colVis option').attr("selected","selected");
// 	$('#multi_colVis').selectpicker('refresh');
// 	 var selectobject = sel.getElementsByTagName("option");
// 	for (var i = 0; i < 9; i++) {		
// 		selectobject[i].disabled = true;      
// 	}        
	$("#multi_colVis").selectpicker("refresh");
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
		url: "Main/saveDefault", 
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
		url: "Main/loadDefault", 
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

// function preLoaderHandler (){         
// 	   preloader.style.display = 'none';  
// 	} 
</script>
</html>