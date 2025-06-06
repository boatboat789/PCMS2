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
				                <th class="row-table" style="vertical-align: middle;">PO</th>
				                <th class="row-table" style="vertical-align: middle;">Article No</th>
				                <th class="row-table" style="vertical-align: middle;">Design No </th>
				                <th class="row-table" style="vertical-align: middle;">ATT<span class="c"style="display: block;"> Color</span> </th>  
				                <th class="row-table" style="vertical-align: middle;">Cust.<span class="c"style="display: block;"> Color</span> </th> 
				                <th class="row-table" style="vertical-align: middle;">Order<span class="c"style="display: block;"> Qty.</span> </th> 
				                <th class="row-table" style="vertical-align: middle;">Shipped	<span class="c"style="display: block;"> Qty.</span> </th> 
				                <th class="row-table" style="vertical-align: middle;">Unit </th> 
				                <th class="row-table" style="vertical-align: middle;">Prod.<span class="c"style="display: block;"> No.</span> </th> 
				                <th class="row-table" style="vertical-align: middle;">Prod.<span class="c"style="display: block;"> Qty.</span> </th> 
				                <th class="row-table" style="vertical-align: middle;">Plan<span class="c"style="display: block;"> Greige Date</span> </th> 
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
								<th class="row-table" style="vertical-align: middle;">CFM Detail   </th>  
				                <th class="row-table" style="vertical-align: middle;">CFM Remark </th>  
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
var isCustomer = false  ;
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
var presetTable ;var dyeingTable;var fnTable;var inspectTable;var packingTable;
// var sendTestQCTable;
var columnsHeader  = [];
var colList ;
var userStatusList ;  	
var cusNameList ; 
var divisionList ; 
var configCusList;
var cusShortNameList ;  
var isCustomer = 0 ;  	
var workInLabTable ;
// var waitTestTable;
var cfmTable;var saleTable;
// var saleInputTable;
var submitDateTable;
var ncTable;
// var receipeTable;
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
var colMap = new Map();
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

	$(document).ajaxStart(function() {$( "#loading").css("display","block"); });   
	$(document).ajaxStop(function() {$("#loading" ).css("display","none"); });
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
	
  	DataTable.render.datetime('DD/MM/YYYY HH:mm:ss', 'DD/MM/YYYY HH:mm:ss', 'en')
  	DataTable.render.datetime('DD/MM/YYYY', 'DD/MM/YYYY', 'en')
	// ---------------------------------------- set---------------------------------------------
	let os = JSON.parse('${OS}');   
	let result = os.includes("win"); 
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
	// ---------------------------------------- set---------------------------------------------
	userId = JSON.parse('${UserID}' );        
	isCustomer = JSON.parse('${IsCustomer}' );    
	if(isCustomer  == true){  
		isCustomer = 1
	}             
	else{     
		isCustomer = 0;               
	}                   
	document.getElementById("btn_lockColumn").style.display = "none"; 
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
		handlerIsCustomer();
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
			var prdOrder = tblData[0].productionOrder
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
					getEncrypted('LBMS',tblData,userId,arrayTmp);   
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
			var prdOrder = tblData[0].productionOrder
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
			var prdOrder = tblData[0].productionOrder
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
				getEncrypted('INSPECT',tblData,userId,arrayTmp);     
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
			var prdOrder = tblData[0].productionOrder
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
				getEncrypted('SFC',tblData,userId,arrayTmp);       
					
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
			var prdOrder = tblData[0].productionOrder 
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
					getEncrypted('QCMS',tblData,userId,arrayTmp);     
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
// 			deferRender: true,              	
//	 		filter:false,       
// 			lengthChange : false,     
// 			pageLength: 1000,	 
			pageLength:	 250,	 
		    lengthMenu: [[100, 250, 500, 1000, 2500],[100, 250, 500, 1000, 2500]],
//	 		lengthMenu: [[1000, -1], [1000, "All"]],
//	 		colReorder: {      
//	             realtime: true       
//	         },                       
	 		colReorder: true,  
			rowsGroup: [ 0 ,1,2,3,4,5,6,7,8,9  ],             
	 	   	columns :    
	 	   		[      
			    {"data" : "saleOrder",  "title": 'SO No.'} ,         //0
			    {"data" : "saleLine",  "title": 'SO Line'},              //1
			    {"data" : "purchaseOrder",  "title": 'PO'}, 
			    {"data" : "articleFG",  "title": 'Article No'}, 
			    {"data" : "designFG",  "title": 'Design No'}, 
			    {"data" : "color",  "title": 'ATT Color'},               //5
			    {"data" : "colorCustomer",  "title": 'Cust.Color'},      //6
			    {"data" : "saleQuantity",  "title": 'Order Qty.','type': 'num'},      //7
			    {"data" : "billQuantity",  "title": 'Shipped Qty.','type': 'num'},    //8
			    {"data" : "saleUnit",  "title": 'Unit'},                              //9
			    {"data" : "productionOrder",  "title": 'Prod.No.'},                   //10
			    {"data" : "totalQuantity",  "title": 'Prod.Qty.','type': 'num'},      //11 
			    {"data" : "planGreigeDate",  "title": 'Plan Greige Date' ,'type': 'date-euro'},            //12
			    {"data" : "greigeInDate",  "title": 'Greige In' ,'type': 'date-euro'},                     //13
			    {"data" : "userStatus",  "title": 'User Status'},                     //14
			    {"data" : "dueDate",  "title": 'Due Date','type': 'date-euro'},       //15        
			    {"data" : "prepare",  "title": 'Prepare','type': 'date-euro'},                            //16
			    {"data" : "relax",  "title": 'Relax','type': 'date-euro'},                                //17
			    {"data" : "preset",  "title": 'PS','type': 'date-euro'},                                  //18
			    {"data" : "dyePlan",  "title": 'Dye [Plan]','type': 'date-euro'},                         //19
			    {"data" : "dyeActual",  "title": 'Dye [Actual]','type': 'date-euro'},                     //20
			    {"data" : "dyeStatus",  "title": 'Dye Status'},                       //21
			    {"data" : "dryer" ,  "title": 'Dryer Date','type': 'date-euro'},                          //22
			    {"data" : "finishing",  "title": 'FN','type': 'date-euro'},                               //23
			    {"data" : "inspectation",  "title": 'Inspect','type': 'date-euro'},                       //24
			    {"data" : "cfmPlanDate",  "title": 'CFM Date[Plan]','type': 'date-euro'},       //25   
			    {"data" : "cfmDateActual",  "title": 'CFM Date[Actual]','type': 'date-euro'},   //26
			    {"data" : "cfmDetailAll",  "title": 'CFM Detail' , "className" : 'dt-custom-td300'       },   //27
			    {"data" : "rollNoRemarkAll",  "title": 'CFM Remark' , "className" : 'dt-custom-td300'},   //28
			    {"data" : "deliveryDate",  "title": 'Delivery Date','type': 'date-euro'},       //29
			    {"data" : "lotShipping",  "title": 'Shipping','type': 'date-euro'}              //30    
//	 		    {"data" : "ShipDate"}          //23   
			],       	           
			columnDefs :  [	        
				{ targets:[10]  ,           
					render: function (data, type, row) {	   
						let html = '<div  name="n_'+row.productionOrder+' data-toggle="tooltip" title="' + row.typePrd + '"> '+row.productionOrder+'</div>'
						return  html; 
				   	  }    
				},     
				{ targets : [ 25],     
			   	  render: function (data, type, row) {	 
				   		var htmlEx = dateDDMMYYYToDDMM(row.cfmPlanDate);     
						if(isCustomer == true){
			   				htmlEx = dateDDMMYYYToDDMM(row.sendCFMCusDate)   ; 
						}   
				   		return  htmlEx      
					}         
				}  ,             
			 	{  			    
// 					targets : [13,14,17,18,19,20,21,23,24,25,26,27,28,29],   
				  targets : [12,13,16,17,18,19,20,22,23,24,26,29,30],             
				  render: DataTable.render.datetime('DD/MM/YYYY', 'DD/MM', 'en')  
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
				} ,          
						
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
					if (data["typePrd"] == "OrderPuang" ) { 	         	 
						$('td', row).eq(mapsDataHeader.get("productionOrder")).addClass('bg-orderpuang'); 
					}  
					else if (data["typePrd"] == "Switch" ) { 	 
						$('td', row).eq(mapsDataHeader.get("productionOrder")).addClass('bg-switch');     
					}   	
					else if (data["typePrd"] == "Replaced" ) { 	 
						$('td', row).eq(mapsDataHeader.get("productionOrder")).addClass('bg-replaced');
					}
					if(data["dueDate"] != "") {      
		 	    		var datearray = data["dueDate"].split("/");
			  	    	var dueDate = new Date(datearray[1] + '/' + datearray[0] + '/' + datearray[2]) ;    
				  	    if (sevenDayAgo >= dueDate   ){             
				  	    	$('td', row).eq(mapsDataHeader.get("dueDate")).addClass('dt-custom-overdue');        
				        }
	  	    		}       
					$('td', row).eq(mapsDataHeader.get("dyePlan")).addClass('bg-color-azure');
					$('td', row).eq(mapsDataHeader.get("cfmPlanDate")).addClass('bg-color-azure');
					$('td', row).eq(mapsDataHeader.get("deliveryDate")).addClass('bg-color-azure');  
				} 
			},      
			drawCallback: function( settings ) { 
//	 			console.log(settings) 
			},   
			initComplete: function () {  
				if ( isCustomer == 1 ) {      
			        // Hide Office column
// 			        api.column(27).visible( false );    
// 			        api.column(28).visible( false );    

// 			        api.column(mapsDataHeader.get("cfmDetailAll")).visible( false );
// 			        api.column(mapsDataHeader.get("rollNoRemarkAll")).visible( false );
			                
			      }       
			}      
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
		    {"data" : "poNo"} ,         //0
		    {"data" : "poLine"},  
		    {"data" : "poCreatedate"},  
		    {"data" : "requiredDate"},  
		    {"data" : "rollNumber"},  
		    {"data" : "rollWeight"},  
		    {"data" : "rollLength"},    //6
		],  	      
		columnDefs :  [	   	
// 			{ targets : [ 0,1,2,3,4,5,6  ],                
// 			  	  className : 'data-custom-padding0505',    
// // 		  	 	  type: 'string'      
// 				} ,                       
		],                    
		 order: [[ 4, "asc" ]],   
		 createdRow : function(row, data, index) {      
		 },   
 	 });    
    dyeingTable = $('#dyeingTable').DataTable({  
    	scrollY: '160px',       
    	scrollX: true,            
    	paging: false,                
 	   	columns : 
 	[        
		    {"data" : "workDate"} ,         //0
		    {"data" : "operation"}, 
		    {"data" : "workCenter"}, 
		    {"data" : "cartNo"}, 
		    {"data" : "cartType"}, 
		    
		    {"data" : "dyeingStatus"}, 
		    {"data" : "deltaE"},                   //5
		    {"data" : "da"}, 
		    {"data" : "db"}, 
		    {"data" : "st"}, 
		    
		    {"data" : "l"},    
		    {"data" : "dyeRemark"}, 
		    {"data" : "colorCheckStatus"},
		    {"data" : "colorCheckRemark"},          //12
		],  	      
		columnDefs :  [	   	 
			{ targets : [ 0,1,2,3,4,5,6,7,8,9,10,11,12 ,13],                
			  	  className : 'data-custom-padding0505',    
// 		  	 	  type: 'string'      
			} ,                    
		],     order: [[ 0, "desc" ]]
 	 });      
    fnTable = $('#fnTable').DataTable({     
    	scrollY:        '160px',        
    	scrollX: true,           
    	paging: false,     
		lengthChange: false,         	  
 	   	columns : 
 		[      
// 			{"data" : "lotNo"} ,         //0
 		    {"data" : "workDate"}, 
 		    {"data" : "operation"  }, 
 		    {"data" : "workCenter"}, 
 		    {"data" : "cartNo"}, 
 		    {"data" : "cartType"},

 		    {"data" : "colorCheckOperation"},    //5
 		    {"data" : "colorCheckWorkDate"},   
		    {"data" : "da"}, 
		    {"data" : "db"},     
 		    {"data" : "l"},     
 		    
		    {"data" : "st"},   //10 
 		    {"data" : "deltaE"},  
 		    {"data" : "colorCheckName"},      
 		    {"data" : "colorCheckStatus"},
 		    {"data" : "colorCheckRollNo"},
 		    
 		    {"data" : "colorCheckRemark"} 
		],  	      
		columnDefs :  [	   	
			{ targets : [ 0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15    ],                
			  	  className : 'data-custom-padding0505',    
//		  	 	  type: 'string'      
				} ,                    
		],  order: [[ 0, "desc" ]]
 	 });     
    inspectTable = $('#inspectTable').DataTable({   
    	scrollY:       '190px',    
    	scrollX: true,               
    	paging: false, 
		lengthChange: false,         	   
 	   	columns : 
 	[      
		    {"data" : "workDate"} ,         //0
		    {"data" : "operation"},  
		    {"data" : "workCenter"} ,         //0
		    {"data" : "machineInspect"},   
		    {"data" : "inspectRemark"}, 
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
		    {"data" : "postingDate"} ,         //0
		    {"data" : "quantity"},  
		    {"data" : "rollNo"},  
		    {"data" : "grade"},  
		    {"data" : "status"},  
		    {"data" : "quantityKG"},           //5
		     
		],  	      
		columnDefs :  [	   	
			{ targets : [ 0,1,2,3,4,5 ],                
			  	  className : 'data-custom-padding0505',    
//		  	 	  type: 'string'      
				} ,                         
		],  order: [[ 0, "desc" ]]     
 	 });   
//     sendTestQCTable = $('#sendTestQCTable').DataTable({   
//     	scrollY:        '190px',    
//     	scrollX: true,      
//     	paging: false,     
// //  	 	scrollCollapse: true,                  
// //  	   	orderCellsTop : true,
// // 		orderClasses : false, 
// 		lengthChange: false,         	  
//  	   	columns : 
//  		[           
// 		    {"data" : "sendDate"} ,         //0
// 		    {"data" : "rollNo"},  
// 		    {"data" : "status"},  
// 		    {"data" : "checkColorDate"},  
// 		    {"data" : "deltaE"},  
// 		    {"data" : "color"},  
// 		    {"data" : "remark"},            //6
// 		],  	          
// 		columnDefs :  [	   	
// 			{ targets : [ 0,1,2,3,4,5,6 ],                
// 			  	  className : 'data-custom-padding0505',    
// // 		  	 	  type: 'string'      
// 				} ,           
// 		],     order: [[ 0, "desc" ]]        
//  	 });  
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
				    {"data" : "labNo"} ,         //0 
				    {"data" : "remark"},  
				    {"data" : "sendFrom"},  
				    {"data" : "sendLabDate"},   
				    {"data" : "dateRequiredLab"},            //5
				    {"data" : "noOfSendInLab"},
				    {"data" : "noOfStartLab"},
				    {"data" : "labStartDate"},    
				    {"data" : "labStopDate"} 
				],  	      
		columnDefs :  [	   	
			{ targets : [ 0,1,2,3,4,5,6,7,8 ],                
			  	  className : 'data-custom-padding0505',    
//		  	 	  type: 'string'      
				} ,            
		],  order: [[ 0, "desc" ]]
 	 });        
    cfmTable = $('#cfmTable').DataTable({   
    	scrollY:        '300px',    
    	scrollX: true,      
    	paging: false,      
// 		orderClasses : false, 
		lengthChange: false,         	  
 	   	columns :  
 		[           
		    {"data" : "cfmNo"} ,         //0
		    {"data" : "cfmNumber"},  
		    {"data" : "cfmSendDate"},  
		    {"data" : "rollNo"},  
		    {"data" : "rollNoRemark"},    
		    {"data" : "l"},            //4
		    {"data" : "da"},
		    {"data" : "db"},
		    {"data" : "st"},
		    {"data" : "de"},
		    {"data" : "saleOrder"},   //9
		    {"data" : "saleLine"}, 
		    {"data" : "cfmAnswerDate"},
		    {"data" : "cfmStatus"},
		    {"data" : "cfmRemark"},     //13
		    {"data" : "nextLot"},  
		    {"data" : "soChange"},
		    {"data" : "soChangeQty"},
		    {"data" : "soChangeUnit"},  //17    
		],  	          
		columnDefs :  [	   	   
			{ targets : [ 0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17 ,18],                
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
		    {"data" : "billDate"} ,         //0
		    {"data" : "billQtyPerSale"},  
		    {"data" : "saleOrder"},  
		    {"data" : "saleLine"},  
		    {"data" : "billQtyPerStock"}, 
		],  	          
		columnDefs :  [	   	
			{ targets : [ 0,1,2,3,4 ],                
			  	  className : 'data-custom-padding0505',    
// 		  	 	  type: 'string'      
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
			    {"data" : "createDate"} ,         //0
			    {"data" : "planDate"},  
			    {"data" : "inputFrom"},   
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
			    {"data" : "no"} ,         //0
			    {"data" : "ncDate"},  
			    {"data" : "ncCarNumber"},  
			    {"data" : "ncLength"},   
			    {"data" : "ncReceiverBase"}, 
			    {"data" : "ncProblem"},
			    {"data" : "ncSolution"},      //6
			],  	          
			columnDefs :  [	   	
				{ targets : [ 0,1,2,3,4,5,6 ],                
				  	  className : 'data-custom-padding0505',    
//	 		  	 	  type: 'string'      
					} ,           
			],     order: [[ 0, "desc" ]]        
	 	 });    
// 	    receipeTable = $('#receipeTable').DataTable({   
// 	    	scrollY:        '175px',         
// 	    	scrollX: true,      
// 	    	paging: false,      
// //	 		orderClasses : false,         
// 			lengthChange: false,         	  
// 	 	   	columns :   
// 	 		[               
// 			    {"data" : "no"} ,         //0
// 			    {"data" : "lotNo"},     
// 			    {"data" : "postingDate"},  
// 			    {"data" : "receipe"},   
// 			],  	          
// 			columnDefs :  [	   	
// 				{ targets : [ 0,1,2,3 ],                
// 				  	  className : 'data-custom-padding0505',    
// //	 		  	 	  type: 'string'      
// 					} ,               
// 			],     order: [[ 0, "desc" ]]        
// 	 	 });      
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
			MainTable.column(indexAfterReCol).search(searchVal).draw();  
		}
		
	});       
	$("#MainTable_filter").hide();   
	$("#dyeingTable_filter").hide();  
	$("#fnTable_filter").hide();  
	$("#inspectTable_filter").hide();  
	$("#packingTable_filter").hide();    
	$("#poTable_filter").hide();	
	
	$("#workInLabTable_filter").hide();   
	$("#cfmTable_filter").hide();  
	$("#saleTable_filter").hide();   
	$("#submitDateTable_filter").hide();  
	$("#ncTable_filter").hide();      
	$("#workInLabTable_info").hide(); 
	$("#cfmTable_info").hide();
	$("#saleTable_info").hide(); 
	$("#submitDateTable_info").hide();
	$("#ncTable_info").hide();  
	$("#dyeingTable_info").hide();   
	$("#fnTable_info").hide();   
	$("#inspectTable_info").hide();   
	$("#packingTable_info").hide();          
	$("#poTable_info").hide();  
	
	$('#multi_userStatus').selectpicker();     
	$('#multi_colVis').selectpicker();   
	$('#multi_cusName').selectpicker();     
	$('#multi_cusShortName').selectpicker(); 
	$('#multi_division').selectpicker();   
	colList = JSON.parse('${ColList}');  
	columnsHeader = MainTable.settings().init().columns;   
	cusNameList = JSON.parse('${CusNameList}');   	
	cusShortNameList = JSON.parse('${CusShortNameList}'); ;   
	userStatusList = JSON.parse('${UserStatusList}');       
	divisionList = JSON.parse('${DivisionList}');        
	var saleNumberList = JSON.parse('${SaleNumberList}');
	configCusList = JSON.parse('${ConfigCusList}');      
	handlerIsCustomer(); 
  	addSelectOption(saleNumberList)
	addUserStatusOption(userStatusList );      
  	addDivisionOption(divisionList );       
	addColOption(columnsHeader )   
	settingColumnOption(columnsHeader, colList); 
	          
	addCusNameOption(cusNameList );      
	addCusShortNameOption(cusShortNameList );        
	$('#multi_userStatus option').attr("selected","selected");
	$('#multi_userStatus').selectpicker('refresh');
	$('#multi_cusName option').attr("selected","selected");
	$('#multi_cusName').selectpicker('refresh');
	$('#multi_cusShortName option').attr("selected","selected");
	$('#multi_cusShortName').selectpicker('refresh');
	$('#multi_division option').attr("selected","selected");
	$('#multi_division').selectpicker('refresh');
	
 
    $('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {  
    	poTable.columns.adjust();                 
    	dyeingTable.columns.adjust(); 
    	fnTable.columns.adjust(); 
    	inspectTable.columns.adjust(); 
    	packingTable.columns.adjust();  
    	workInLabTable.columns.adjust();  
    	cfmTable.columns.adjust(); 
    	saleTable.columns.adjust();  
    	submitDateTable.columns.adjust(); 
    	ncTable.columns.adjust();  
    });          
	$('.modal').on('shown.bs.modal', function() {    
		poTable.columns.adjust();         
		dyeingTable.columns.adjust();   	
		fnTable.columns.adjust(); 
		inspectTable.columns.adjust(); 
		packingTable.columns.adjust();  
    	workInLabTable.columns.adjust();  
    	cfmTable.columns.adjust(); 	
    	saleTable.columns.adjust();  
    	submitDateTable.columns.adjust(); 
    	ncTable.columns.adjust();  
	})  
	preLoaderHandler( preloader)   
});   
function searchByDetail(){  
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
	var PO = document.getElementById("input_PO").value .trim();    
	var userStatus = $('#multi_userStatus').val(); 
	var customer = $('#multi_cusName').val();    
	var customerShort = $('#multi_cusShortName').val(); 
	var deliStatus = document.getElementById("SL_delivStatus").value .trim();     
	var division = $('#multi_division').val();  
	var dmCheck = document.getElementById("check_DM").checked;         
	var exCheck = document.getElementById("check_EX").checked;  
	var hwCheck = document.getElementById("check_HW").checked;  
	var distChannel = "";  
	 if( dmCheck ){ distChannel = "DM";}
	 if( exCheck ){ if(distChannel != "") {distChannel = distChannel + "|" } distChannel = distChannel + "EX";}       
	 if( hwCheck ){ if(distChannel != "") {distChannel = distChannel + "|" } distChannel = distChannel + "HW";}   
	 var saleStatus = document.querySelector('input[name="saleStatusRadio"]:checked').value;

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

	else if (distChannel == ''){
		swal({
   		    title: 'Warning',
   		    text: 'Need to choose distribute channel from check box.',
   		    icon: 'warning',
   		    timer: 1000,
   		    buttons: false,
   		})
	}
	else{
		var json = createJsonData();        
// 		console.log(json)
	    var  obj = JSON.parse(json);    
		var arrayTmp = [];   
		arrayTmp.push(obj);      
		searchByDetailToServer(arrayTmp); 
	}
} 
function createJsonData(){  
	var saleOrder = document.getElementById("input_saleOrder").value .trim();  
	var article = document.getElementById("input_article").value .trim();  
	var prdOrder = document.getElementById("input_prdOrder").value .trim();
	var saleNumber = document.getElementById("SL_saleNumber").value .trim();
	var saleOrderDate = document.getElementById("input_saleOrderDate").value .trim();
	var designNo = document.getElementById("input_designNo").value .trim(); 
	var prdOrderDate = document.getElementById("input_prdOrderDate").value .trim(); 
	var material = document.getElementById("input_material").value .trim(); 
	var labNo = document.getElementById("input_labNo").value .trim();      
	var PO = document.getElementById("input_PO").value .trim();          
	var userStatus = $('#multi_userStatus').val();    
	var customer = $('#multi_cusName').val();
	var customerShort = $('#multi_cusShortName').val(); 
	var deliStatus = document.getElementById("SL_delivStatus").value .trim();  
	var dueDate = document.getElementById("input_dueDate").value .trim(); 
	var dmCheck = document.getElementById("check_DM").checked;  
	var exCheck = document.getElementById("check_EX").checked;  
	var hwCheck = document.getElementById("check_HW").checked;      
	var division = $('#multi_division').val(); 
	 var saleStatus = document.querySelector('input[name="saleStatusRadio"]:checked').value;
	 
	 var cusDiv = "";  
	 if(configCusList.length > 0 ){ 
// 	 	let p_cusDiv = configCusList[0].customerDivision	 ;
// 	 	if(p_cusDiv!=''){  cusDiv = p_cusDiv; } 
	 }     
		var distChannel = "";  
		 if( dmCheck ){ distChannel = "DM";}
		 if( exCheck ){ if(distChannel != "") {distChannel = distChannel + "|" } distChannel = distChannel + "EX";}       
		 if( hwCheck ){ if(distChannel != "") {distChannel = distChannel + "|" } distChannel = distChannel + "HW";}   
	var json = '{'+    
	    '"saleOrder":'+JSON.stringify(saleOrder)+      
	   ',"articleFG":'+JSON.stringify(article)+  
	   ',"productionOrder":'+JSON.stringify(prdOrder)+  
	   ',"saleNumber": '+JSON.stringify(saleNumber)+
	   ',"saleOrderCreateDate":'+JSON.stringify(saleOrderDate)+
	   ',"designFG":'+JSON.stringify(designNo)+       
	   ',"productionOrderCreateDate":'+JSON.stringify(prdOrderDate)+ 
	   ',"materialNo":'+JSON.stringify(material)+
	   ',"labNo":'+JSON.stringify(labNo)+    
	   ',"customerDivision":'+JSON.stringify(cusDiv)+                
	   ',"purchaseOrder":'+JSON.stringify(PO)+  
	   ',"userStatusList":'+JSON.stringify(userStatus)+       
	   ',"customerNameList":'+JSON.stringify(customer)+   
	   ',"customerShortNameList":'+JSON.stringify(customerShort)+   
	   ',"divisionList":'+JSON.stringify(division)+   
	   ',"deliveryStatus":'+JSON.stringify(deliStatus)+            
	   ',"saleStatus":'+JSON.stringify(saleStatus)+  
	   ',"distChannel":'+JSON.stringify(distChannel) + 
	   ',"dueDate":'+JSON.stringify(dueDate) + 
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
						if(isCustomer == true && data == 'cfmPlanDate'   ){   
							val = value.sendCFMCusDate;    
						} 
						else{   
// 							innerRowData[indexArray] = val;             
						}   
						innerRowData[indexArray] = val;   
					}
					else if (colType == 'num'){   
						if(data == 'saleQuantity' || data == 'billQuantity'){  
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
// 						console.log(userId,data,val,value.SendCFMCusDate)
						
// 						if(userId == 'violetta01' && data == 'CFMPlanDate'   ){
// 							val = value.SendCFMCusDate;    
// 							if(val == ''){ innerRowData[indexArray] = ''   ;   }
// 	 						else{ innerRowData[indexArray] =stringToDate(val)   ; } 
// 						}   
// 						else{         
// 							console.log(data+" "+val)
							if(val == ''){ innerRowData[indexArray] = ''   ;   }
	 						else{ innerRowData[indexArray] =stringToDate(val)   ; }     
// 						}
						
					}                
		  		}   
			  	else if(data == 'customerName'){innerRowData[sizeMap] = val  ;      }
			  	else if(data == 'customerShortName'){innerRowData[sizeMap+1] =val ; } 
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
function getPrdDetailByRow(arrayTmp) {      
	$.ajax({   
		type: "POST",
		contentType: "application/json",  
		data: JSON.stringify(arrayTmp),      
// 	       data :{   
// //	     	   data:encodeURI(value)
// 	    	   data:JSON.stringify( arrayTmp 	)
// 	       },  
		url: ctx+"/Main/getPrdDetailByRow", 
		success: function(data) {       
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
	document.getElementById("input_designNo").value  = '';   
	document.getElementById("input_material").value  = '';         
	document.getElementById("input_PO").value     = '';     
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
	$.ajax({
		type: "POST",  
		contentType: "application/json",   
		data: JSON.stringify(arrayTmp),      
// 	       data :{   
// //	     	   data:encodeURI(value)
// 	    	   data:JSON.stringify( arrayTmp 	)
// 	       },       
		url: ctx+"/Main/searchByDetail", 
		success: function(data) {   
			MainTable.clear();        
			MainTable.rows.add(data);           
			MainTable.draw();      
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
   	poTable.clear();    
//    	console.log(innnerText)
	if(innnerText.poDetailList.length == 0){       }     
	else{ 
		poTable.rows.add(innnerText.poDetailList);
		document.getElementById("input_poDefault").value = innnerText.poDetailList[0].poDefault;    
		document.getElementById("input_poLineDefault").value = innnerText.poDetailList[0].poLineDefault;    
		document.getElementById("input_poCreateDateDefault").value = innnerText.poDetailList[0].poPostingDateDefault;    
	}  
	poTable.draw();       	   
	if(innnerText.presetDetailList.length == 0){       }     
	else{ 
		document.getElementById("input_presetDate").value = innnerText.presetDetailList[0].workDate;    
		document.getElementById("input_presetWorkCenter").value = innnerText.presetDetailList[0].workCenter; 
	}   
	
	dyeingTable.clear();    	   
	if(innnerText.dyeingDetailList.length == 0){       }     
	else{ dyeingTable.rows.add(innnerText.dyeingDetailList); }  
	dyeingTable.draw();                 
	 
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
	 
	document.getElementById("input_greigeDesign").value = innnerText.greigeDesign  ;    
	document.getElementById("input_greigeArticle").value = innnerText.greigeArticle;    
	document.getElementById("input_prdOrderCM").value = innnerText.productionOrder;    
	document.getElementById("input_lotNoCM").value = innnerText.lotNo;    
	document.getElementById("input_batchCM").value = innnerText.batch;    
	document.getElementById("input_labNoCM").value = innnerText.labNo;    
	document.getElementById("input_dateCM").value = innnerText.productionOrderCreateDate;    
	document.getElementById("input_dueDateCM").value = innnerText.dueDate;    
	document.getElementById("input_saleCM").value = innnerText.saleOrder+'/'+innnerText.saleLine;    
	document.getElementById("input_poCM").value = innnerText.purchaseOrder;    
	document.getElementById("input_articleFGCM").value = innnerText.articleFG;    
	document.getElementById("input_designFGCM").value = innnerText.designFG;    
	document.getElementById("input_customerNameCM").value = innnerText.customerName;    
	document.getElementById("input_customerShortNameCM").value = innnerText.customerShortName;      
	document.getElementById("input_shadeCM").value = innnerText.shade;    
	document.getElementById("input_bookNoCM").value = innnerText.bookNo;    
	document.getElementById("input_centerCM").value = innnerText.center;    
	document.getElementById("input_matNoCM").value = innnerText.materialNo;    
	document.getElementById("input_volumnCM").value = innnerText.volumn+" "+innnerText.saleUnit;    
	document.getElementById("input_stdUnitCM").value = innnerText.stdUnit;    
	   
// 	console.log(innnerText)
	document.getElementById("input_colorCM").value = innnerText.color + ' ( '+ innnerText.colorCustomer +' ) ';        
	document.getElementById("input_planGreigeDateCM").value = innnerText.planGreigeDate;   
	document.getElementById("input_refPrdCM").value = innnerText.refPrd;   
	document.getElementById("input_greigeInDateCM").value = innnerText.greigeInDate;  
	document.getElementById("input_awarenessBCCM").value = innnerText.bcAware;   
	document.getElementById("input_orderPuangCM").value = innnerText.orderPuang;   
	document.getElementById("input_userStatusCM").value = innnerText.userStatus;   
	document.getElementById("input_labStatusCM").value = innnerText.labStatus;    
	document.getElementById("input_awarenessBCCM").value = innnerText.bcAware;    
	document.getElementById("input_sendCFMPlanCM").value = innnerText.cfmDatePlan;   
	document.getElementById("input_sendShipPlanCM").value = innnerText.deliveryDate;   
	document.getElementById("input_BCDateCM").value = innnerText.bcDate;   
	document.getElementById("input_remarkCM").value = innnerText.remarkOne+" "+innnerText.remarkTwo+" "+innnerText.remarkThree;   
	document.getElementById("input_remarkAfterCloseCM").value = innnerText.remAfterCloseOne+" "+innnerText.remAfterCloseTwo+" "+innnerText.remAfterCloseThree;    
}    
function goToLBMS(tblData,pUserId,data){     
// 	let obj = createEncryptObj(pUserId);      
	var prdOrder = tblData[0].productionOrder
	var article = tblData[0].articleFG 
	var color = tblData[0].color 
	$.ajax({   
  	    url: urlLBMS,
  	    type : 'GET',   
  	    data : { 
  	    	"comeFrom": data.encrypted  ,   
  	        "isCustomer": isCustomer
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
// 					tab.document.getElementById('prodOrderTable_filter').value = prdOrder  ;
// 					tab.$("#prodOrderTable_filter input").value(prdOrder);     
					tab.$('#prodOrderTable').DataTable().search(prdOrder).draw();
					},500);                      
// 				console.log( tab.$('#prodOrderTable').DataTable()  )  
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
	var prdOrder = tblData[0].productionOrder  
	$.ajax({    
	    url: urlSFC,
	    type : 'GET',      
	    data : {     
			"comeFrom": data.encrypted  ,   
  	        "isCustomer": isCustomer
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
	var prdOrder = tblData[0].productionOrder 
	$.ajax({ 
	    url: urlInspect,
	    type : 'GET',      
// 	    async : false,
	    data : {
	    	"comeFrom": data.encrypted  ,
  	        "isCustomer": isCustomer      	
// 	    	"comeFrom": "PCMS"
	    },    
	    success : function(data) {  
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
		var article = tblData[0].articleFG
		var lotNo = tblData[0].lotNo
		var color = tblData[0].color 
		$.ajax({ 
		    url: urlQCMSObj,
		    type : 'GET',      
//	 	    async : false,
		    data : {    
		    	"comeFrom": data.encrypted  ,   
	  	        "isCustomer": isCustomer 
		    },    
		    success : function(data) {   
		    	var url = urlQCMSObj; 
		    	var tab = window.open(url );
		    	tab.onload = function() {    
		    	      tab.document.getElementById('article').value = article  ;  //'S2A001'
		    	      tab.document.getElementById('lotNumber').value = lotNo  ;  //'S2A001'
		    	      tab.document.getElementById('color').value = color  ;  //'S2A001' 
		    	      if(isCustomer != 1){
			    	      setTimeout(function(){      
			    	      	 tab.document.getElementById('btnSearchRequest').click();  
						 }, 500);         
		    	      }   
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
		 opt.text  = resultData.saleFullName;
		 opt.value = resultData.saleNumber;   
		 sel.appendChild(opt);          
	}      
}    
function settingColumnOption(columnsHeader ,colVisible){  
	if(colVisible == null){
		$('#multi_colVis option').attr("selected","selected");
		$('#multi_colVis').selectpicker('refresh');
	}  
	else{    
		setColVisibleTable(columnsHeader,colVisible);    
		$('#multi_colVis').selectpicker('val', colVisible);     
	}
	getVisibleColumnsTable()     
}
        
function getVisibleColumnsTable() {    
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
   
// 	var columnsHeaderArr = MainTable.settings().init().columns;   
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
	$.ajax({     
		type: "POST",  
		contentType: "application/json",  
		data: JSON.stringify(arrayTmp),      
		url: ctx+"/Main/saveColSettingToServer", 
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
// 	var arrColOption = [];
// 	for (var i = colName.length - 1; i > -1; i--) { 
// 		 var resultData = colName[i].data; 	   	
// 		 if(resultData == 'cfmDetailAll' || resultData == "rollNoRemarkAll" ){ 
// 			 colName.splice(i, 1);
// 		 }
// 	} 
	
	var sel = document.getElementById('multi_colVis');
	for (i = sel.length - 1; i >= 0; i--) {
		sel.remove(i);
	}             
	let counter = 0;  
	var size = colName.length;
	for (var i = 0; i < size; i++) {		   
		 var resultData = colName[i]; 	  
		 if(resultData.data != 'cfmDetailAll' && resultData.data != "rollNoRemarkAll" ){  
			counter = counter + 1;
			 var opt = document.createElement('option');
		     opt.appendChild(document.createTextNode(i));
			 opt.text  = (counter+1)+" : "+resultData.title;  
			 opt.value = resultData.data;  
			 sel.appendChild(opt);         
		 }
		 else if(resultData.data == 'cfmDetailAll' || resultData.data == "rollNoRemarkAll" ){ 
		 	if(isCustomer == 1 ){   }
		 	else{    
				counter = counter + 1;
				var opt = document.createElement('option');
				opt.appendChild(document.createTextNode(i));
				opt.text  = (counter+1)+" : "+resultData.title;  
				opt.value = resultData.data;  
				sel.appendChild(opt);     
		 	}
		 }
	}          
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
		url: ctx+"/Main/saveDefault", 
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
		url: ctx+"/Main/loadDefault", 
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
	document.getElementById("input_PO").value     	 = innnerText.purchaseOrder;
	
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
	else if(saleStatus == 'X'){ 
		document.getElementById("rad_canceled").checked = true;
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
function getEncrypted(webApp,tblData,userId,arrayTmp){
	$.ajax({
	   type: "POST",     
		contentType: "application/json",         
		url: ctx+"/Main/getEncrypted/"+userId,        
	    data : JSON.stringify(arrayTmp),             
	    success : function(data) {   
	    	if(webApp == 'LBMS'){
	    		goToLBMS(tblData,userId,data);
	    	}
	    	else if(webApp == 'SFC'){
	    		goToSFC(tblData,userId,data);
	    	}
	    	else if(webApp == 'INSPECT'){
	    		goToInspect(tblData,userId,data);
	    	}
	    	else if(webApp == 'QCMS'){
	    		goToQCMS(tblData,userId,data);
	    	}
	    	
	    }   	
	});    
}
function handlerIsCustomer(){

	if(isCustomer == 1 ){ 
		if(colList == null){ }
		else{      
			for (var i = colList.length - 1; i > -1; i--) { 
				 var resultData = colList[i]; 	   	
				 if(resultData == 'cfmDetailAll' || resultData == "rollNoRemarkAll" ){ 
					 colList.splice(i, 1);
				 }
			}
		}     
	}
} 
</script>
</html>