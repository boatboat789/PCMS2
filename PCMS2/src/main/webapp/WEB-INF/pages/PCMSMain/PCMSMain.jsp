<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page isELIgnored="false"%> 
<!DOCTYPE html>
<html>
<head>  
	<jsp:include page="/WEB-INF/pages/config/meta.jsp"></jsp:include>  
	<title>PCMS - Main</title>            	    
	<jsp:include page="/WEB-INF/pages/config/css/baseCSS.jsp"></jsp:include>       
	<link href="<c:url value="/resources/css/style_overide.css" />" rel="stylesheet" type="text/css">    
	<link href="<c:url value="/resources/css/datatable.overide.css" />" rel="stylesheet" type="text/css">       
</head>      
<body>       	         
	<jsp:include page="/WEB-INF/pages/config/navbar.jsp"></jsp:include>  
	<jsp:include page="/WEB-INF/pages/config/loading.jsp"></jsp:include>    
		<div id="wrapper-top" class="row" style="  margin: 5px;  background-color: azure;  " >  
<!-- 		style="top: 0; margin: 5px; z-index: 1000; background-color: azure;  -->
<!-- 		       position: sticky;position: -webkit-sticky;      "  -->
			<div class="col-12 col-sm-10 col-md-10 col-lg-10 col-xl-10 "  >   
				<div class="row">        
					<div class="col-12 col-sm-6 col-md-4 col-lg-3 col-xl-3 form-group lab-print" >       
					  	<label class=" label-input  margin-0" for="input_customer">Customer</label>           
					  	<div > 
				    		<input class="form-control " autocomplete="off"  type="text"  id="input_customer"   maxlength="50" > 
						</div>     
					</div>   
					<div class="col-12 col-sm-6 col-md-4 col-lg-3 col-xl-3 form-group lab-print" >       
					  	<label class=" label-input  margin-0" for="input_customerShortName">Cust.(Name4)</label>           
					  	<div > 
				    		<input class="form-control " autocomplete="off"  type="text"  id="input_customerShortName"   maxlength="50" > 
						</div>     
					</div>             
					<div class="col-12 col-sm-6 col-md-4 col-lg-3 col-xl-3 form-group lab-print" >       
					  	<label class=" label-input  margin-0" for="input_saleOrder">SO No.</label>           
					  	<div > 
				    		<input class="form-control " autocomplete="off"  type="text"  id="input_saleOrder"   maxlength="30" > 
						</div>     
					</div>         
					<div class="col-12 col-sm-6 col-md-4 col-lg-3 col-xl-3 form-group lab-print" >              
					  	<label class=" label-input  margin-0" for="input_article">Article No</label>           
					  	<div > 
				    		<input class="form-control " autocomplete="off"  type="text"  id="input_article"   maxlength="30" > 
						</div>     
					</div>                      
					<div class="col-12 col-sm-6 col-md-4 col-lg-3 col-xl-3 form-group lab-print" >              
					  	<label class=" label-input  margin-0" for="input_prdOrder">Prod.No</label>           
					  	<div > 
				    		<input class="form-control " autocomplete="off"  type="text"  id="input_prdOrder"   maxlength="30" > 
						</div>     
					</div>     
					<div class="col-12 col-sm-6 col-md-4 col-lg-3 col-xl-3 form-group lab-print" >              
					  	<label class=" label-input  margin-0" for="SL_saleNumber">SaleName</label>           
					  	<div > 
<!-- 				    		<input class="form-control " autocomplete="off"  type="text"  id="input_saleNumber"   maxlength="30" >  -->
							<select id="SL_saleNumber" class="form-control "   >      
								<option value="" selected>Select</option>   
							</select>
						</div> 
						 
					</div>                     
					<div class="col-12 col-sm-6 col-md-4 col-lg-3 col-xl-3 form-group lab-print" >              
					  	<label class=" label-input  margin-0" for="input_saleOrderDate">SO. Date</label>           
					  	<div > 
				  			<input autocomplete="off" id="input_saleOrderDate" type="text" name="daterange"   class='form-control'
								style=' display: inline;margin-top: 0px;' /> 
<!-- 								onchange="checkDate(this)" />      -->
						</div>     
					</div>                    
					<div class="col-12 col-sm-6 col-md-4 col-lg-3 col-xl-3 form-group lab-print" >              
					  	<label class=" label-input  margin-0" for="input_designNo">Design No</label>           
					  	<div > 
				    		<input class="form-control " autocomplete="off"  type="text"  id="input_designNo"   maxlength="30" > 
						</div>     
					</div>   
					<div class="col-12 col-sm-6 col-md-4 col-lg-3 col-xl-3 form-group lab-print" >              
					  	<label class=" label-input  margin-0" for="input_prdOrderDate">Prod.Date</label>           
					  	<div > 
					  	<input autocomplete="off" id="input_prdOrderDate" type="text" name="daterange"   class='form-control'
								style=' display: inline;margin-top: 0px;' />       
<!-- 								onchange="checkDate(this)" />     -->
						</div>     
					</div>   
					<div class="col-12 col-sm-6 col-md-4 col-lg-3 col-xl-3 form-group lab-print" >              
					  	<label class=" label-input  margin-0" for="input_material">Mat No.</label>           
					  	<div > 
				    		<input class="form-control " autocomplete="off"  type="text"  id="input_material"   maxlength="30" > 
						</div>     
					</div>      
					<div class="col-12 col-sm-6 col-md-4 col-lg-3 col-xl-3 form-group lab-print" >              
					  	<label class=" label-input  margin-0" for="input_labNo">LabNo</label>           
					  	<div > 
				    		<input class="form-control " autocomplete="off"  type="text"  id="input_labNo"   maxlength="30" > 
						</div>     
					</div>   
					<div class="col-12 col-sm-6 col-md-4 col-lg-3 col-xl-3 form-group lab-print" >              
<!-- 					  	<label class=" label-input  margin-0" for="input_planStatus">UserStatus</label>            -->
<!-- 					  	<div >  -->
<!-- 				    		<input class="form-control " autocomplete="off"  type="text"  id="input_userStatus"   maxlength="30" >  -->
<!-- 						</div>         -->
						<label class=" label-input  margin-0" for="SL_userStatus">UserStatus</label>           
					  	<div >  
					  		<select id="SL_userStatus"   class="selectpicker"  data-live-search="true">
					  			<option value="" selected>SelectAll</option>
					  		</select>
						</div>    
					</div>   
					<div class="col-12 col-sm-6 col-md-4 col-lg-3 col-xl-3 form-group lab-print" >              
					  	<label class=" label-input  margin-0" for="SLDelivStatus">Delivery Status</label>           
					  	<div >    
				    		<select id="SLDelivStatus" class="form-control "   >      
							<option value="" selected>Select</option>  
							<option value="C" >Completed</option>  
							<option value="B" >Partial Process</option>  
							<option value="A" >NotYet Process</option>  
						</select>
						</div>      
					</div>   
				</div>       
			</div>            
			<!--  ROW 2 -->    
			<div class="col-12 col-sm-2 col-md-2 col-lg-2 col-xl-2  "  >   
				<div class="row">       
					<div class="col-12  col-sm-12 col-md-12 col-lg-12 col-xl-12  " style="    text-align: -webkit-right;    margin-bottom: 5px;" >  
		              	<button id="btn_search" class="btn btn-primary" type="button" style=" width: -webkit-fill-available; " >
		                    <i class="fa fa-search"></i> 
		                    Search 
		              	</button>      
					</div>          
					<div class="col-12  col-sm-12 col-md-12 col-lg-12 col-xl-12  " style="    text-align: -webkit-right;    margin-bottom: 5px;" >  
		              	 
		              	<button id="btn_download" class="btn btn-primary" type="button" style=" width: -webkit-fill-available; " >
		                    <i class="fa fa-download"></i>     
		                    Download Excel     
		              	</button>     
					</div>       
					<div class="col-12  col-sm-12 col-md-12 col-lg-12 col-xl-12  " style="    text-align: -webkit-right;    margin-bottom: 5px;" >  
	   
		              	<button id="btn_colSetting" class="btn btn-primary" type="button" style=" width: -webkit-fill-available; " >
		                    <i class="fa fa-cogs"></i> 
		                    Column Setting
		              	</button>      
					</div>       
				</div>    
			</div>                             
			 <div class="col-12 col-sm-12 col-md-12 col-lg-12 col-xl-12 "  >
			 	<div class="row">          
				 	<div class="col-12 col-sm-6 col-md-4 col-lg-3 col-xl-3 form-group lab-print" >       
					 	<div class="form-check" style="margin-left: 10px;  display: inline;">     
						  	<input class="form-check-input" type="checkbox" id="check_DM" value="DM" checked>
						  	<label class="form-check-label" for="check_DM">
						    	Domestic
						  	</label>  
						</div>    
					 	<div class="form-check" style="margin-left: 10px; display: inline;">     
							<input class="form-check-input" type="checkbox" id="check_EX" value="EX" checked>
							<label class="form-check-label" for="check_EX">
							    Export
						  	</label>
						</div>    
						<div class="form-check" style="margin-left: 10px; display: inline;">     
							  <input class="form-check-input" type="checkbox" id="check_HW" value="HW" checked>
							  <label class="form-check-label" for="check_HW">
							    Hire work  
							  </label>    
						</div>   	              
					</div>   
					<div class="col-12 col-sm-6 col-md-4 col-lg-3 col-xl-3 form-group lab-print" >       
						<div class="custom-control custom-radio" style="margin-left: 10px; display: inline;">
							<input type="radio" id="rad_all" name="saleStatusRadio" class="custom-control-input" value="" >
							<label class="custom-control-label" for="rad_all">All</label>
						</div>
						<div class="custom-control custom-radio" style="margin-left: 10px; display: inline;">
							<input type="radio" id="rad_inProc" name="saleStatusRadio" class="custom-control-input" value="O" checked>
							<label class="custom-control-label" for="rad_inProc">In-Process</label>
						</div> 
						<div class="custom-control custom-radio" style="margin-left: 10px; display: inline;">
							<input type="radio" id="rad_closed" name="saleStatusRadio" class="custom-control-input" value=C>    
							<label class="custom-control-label" for="rad_closed">Closed</label>
						</div> 
					</div>    
					
					
					    
					<div class="col-12 col-sm-12 col-md-4 col-lg-6 col-xl-6  form-group lab-print" style="justify-content: right;" >   
						<div class="row">       
							<div class="col-12  col-sm-12 col-md-12 col-lg-12 col-xl-12  "  >     
				              	<button id="btn_prdDetail" class="btn btn-primary" type="button" style="margin-bottom: 5px;" >
<!-- 				                    <i class="fa fa-search"></i>  -->
				                    Production Detail 
				              	</button>     
				              	<button id="btn_lbms" class="btn btn-primary" type="button" style="margin-bottom: 5px;" >
<!-- 				                    <i class="fa fa-save"></i>         -->
				                   LBMS Detail            
				              	</button>    
				              	<button id="btn_qcms" class="btn btn-primary" type="button" style="margin-bottom: 5px;" >
<!-- 				                    <i class="fa fa-print"></i>  -->
				                    QCMS Result
				              	</button>   
				              	<button id="btn_inspect" class="btn btn-primary" type="button" style="margin-bottom: 5px;" >
<!-- 				                    <i class="fa fa-print"></i>  -->
				                    Inspect Result
				              	</button>   
				              	<button id="btn_sfc" class="btn btn-primary" type="button" style="margin-bottom: 5px;" >
<!-- 				                    <i class="fa fa-print"></i>  -->
				                    SFC Detail
				              	</button>      
							</div>       
						</div>    
					</div>   
					      
				</div>   
			</div>          
		</div>        
		<div id="wrapper-center" class="row" style="margin: 0px 5px;">     
			<div class="col-12 col-sm-12 col-md-12 col-lg-12 col-xl-12 " style="padding: 0px;margin: 0px 0px;" >                
				<div class="table-responsive ">        
					<table id="MainTable" class="table compact table-bordered table-striped text-center" style="zoom: 90%;   font-size: 12.5px;width:100%">
				        <thead>
				            <tr>
				                <th class=" " style="vertical-align: middle;">SO No. </span></th>
				                <th class=" " style="vertical-align: middle;">SO Line</th> 
				                <th class=" " style="vertical-align: middle;">Article No</th>
				                <th class=" " style="vertical-align: middle;">Design No </th>
				                <th class=" " style="vertical-align: middle;">ATT<span class="c"style="display: block;"> Color</span> </th>  
				                <th class=" " style="vertical-align: middle;">Cust.<span class="c"style="display: block;"> Color</span> </th> 
				                <th class=" " style="vertical-align: middle;">Order<span class="c"style="display: block;"> Qty.</span> </th> 
				                <th class=" " style="vertical-align: middle;">Shipped	<span class="c"style="display: block;"> Qty.</span> </th> 
				                <th class=" " style="vertical-align: middle;">Unit </th> 
				                <th class=" " style="vertical-align: middle;">Prod.<span class="c"style="display: block;"> No.</span> </th> 
				                <th class=" " style="vertical-align: middle;">Prod.<span class="c"style="display: block;"> Qty.</span> </th> 
				                <th class=" " style="vertical-align: middle;">Greige<span class="c"style="display: block;"> In</span> </th> 
				                <th class=" " style="vertical-align: middle;">User<span class="c"style="display: block;"> Status</span> </th> 
				                <th class=" " style="vertical-align: middle;">Due<span class="c"style="display: block;"> Date</span> </th> 
				                <th class=" " style="vertical-align: middle;">Prepare	 </th>  
				                <th class=" " style="vertical-align: middle;">PS </th>      
				                <th class=" " style="vertical-align: middle;">Dye <span style="display: block;"> [Plan]</span> </th> 
				                <th class=" " style="vertical-align: middle;">Dye <span class="c"style="display: block;"> [Actual]</span> </th> 
				                <th class=" " style="vertical-align: middle;">Dryer<span class="c"style="display: block;"> Date</span> </th> 
				                <th class=" " style="vertical-align: middle;">FN  </th> 
				                <th class=" " style="vertical-align: middle;">Inspect </th>  
				                <th class=" " style="vertical-align: middle;">วันนัด CF<span class="c"style="display: block;"> [Plan]</span> </th> 
				                <th class=" " style="vertical-align: middle;">วันนัด CF<span class="c"style="display: block;"> [Actual]</span> </th> 
				                <th class=" " style="vertical-align: middle;">วันนัด<span class="c"style="display: block;"> ส่งของ</span> </th> 
				                <th class=" " style="vertical-align: middle;">Shipping </th>   
				        
				        	</tr> 
				        </thead>     	     
				        <tbody>    
				        </tbody>       
				    </table>            
				</div>                
			</div>   	
		</div>               
<!-- 		<div id="wrapper-bot" class="row">   -->
		 
<!-- 		</div>        -->   
      <jsp:include page="/WEB-INF/pages/config/footer.jsp"></jsp:include> 
	<jsp:include page="/WEB-INF/pages/config/PCMSMainModal/modalMain.jsp"></jsp:include> 
		<jsp:include page="/WEB-INF/pages/config/PCMSDetailModal/ColumnSetting/ColumnSetting.jsp"></jsp:include> 
<%-- 	<jsp:include page="/WEB-INF/pages/config/PCMSMainModal/Redirect/modalMain.jsp"></jsp:include>  --%>
</body>               
<%-- <script src="<c:url value="/resources/js/dataTables.rowsGroup.js" />"></script>          --%>
<script src="<c:url value="/resources/js/DatatableSort.js" />"></script>   
  <style>
  .p-r-15 {padding-right: 15px !important; }
  </style>
<!-- <script src="https://cdn.datatables.net/fixedheader/3.2.0/js/dataTables.fixedHeader.min.js"></script>    -->
<script>     
$(document).on('show.bs.modal', '.modal', function (event) {
    var zIndex = 1040 + (10 * $('.modal:visible').length);
    $(this).css('z-index', zIndex);
    setTimeout(function() {
        $('.modal-backdrop').not('.modal-stack').css('z-index', zIndex - 1).addClass('modal-stack');
    }, 0);  
});
var today = new Date();        //modalForm
var dd = String('0' + today.getDate()).slice(-2); 
var mm = String('0' + (today.getMonth() + 1)).slice(-2); ; //January is 0!
var yyyy = today.getFullYear();
var startDate = dd+'/'+mm+'/'+yyyy; 
var MainTable ;  
var poTable ;      
var userStatusList ;  	
var mapsDataHeader  = new Map();  
var mapsTitleHeader  = new Map();  
var presetTable ;var dyeingTable;var fnTable;var inspectTable;var packingTable;var sendTestQCTable;
var columnsHeader  = [];
var colList ;
var workInLabTable ;var waitTestTable;var cfmTable;var saleTable;var saleInputTable;var submitDateTable;
var ncTable;var receipeTable;
var collapsedGroups = {};        
var domain = "http://"+window.location.hostname+":8080";
var urlLBMS = domain+"/LBMS/";     
var urlLBMSObj = domain+"/LBMS/LabHistory"; 
var urlSFC = domain+"/SFC/"; 
var urlSFCObj = domain+"/SFC/HistoryWork"; 
var urlInspect = domain+"/InspectSystem/search/home.html"; 
var urlInspectObj = domain+"/InspectSystem/search/home.html"; 
var urlQCMS = domain+"/QCMS/first.html"; 
var urlQCMSObj = domain+"/QCMS/request/search.html"; 
// console.log(window.location.host)
// console.log(window.location.hostname)
// console.log(window.location.origin)
// var urlLBMS = "http://localhost:8080/LBMS/";     
// var urlLBMSObj = "http://localhost:8080/LBMS/LabHistory"; 
// var urlSFC = "http://localhost:8080/SFC/"; 
// var urlSFCObj = "http://localhost:8080/SFC/HistoryWork"; 
// var urlInspect = "http://localhost:8080/InspectSystem/search/home.html"; 
// var urlInspectObj = "http://localhost:8080/InspectSystem/search/home.html"; 
  
// var urlLBMS = "http://pcms.a-tech.co.th:8080/LBMS/";     
// var urlLBMSObj = "http://pcms.a-tech.co.th:8080/LBMS/LabHistory";        
// var urlSFC = "http://pcms.a-tech.co.th:8080/SFC/"; 
// var urlSFCObj = "http://pcms.a-tech.co.th:8080/SFC/HistoryWork";   
// var urlInspect = "http://pcms.a-tech.co.th:8080/InspectSystem/"; 
// var urlInspectObj = "http://pcms.a-tech.co.th:8080/InspectSystem/search/home.html";    
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
$(document) .ready( function() {
<%-- 		var ctx2 = "<%=request.getContextPath()%>" --%>
// 		var ctx = "${pageContext.request.contextPath}";
// 		console.log(window.location.hostname)       ;       
// 		console.log(ctx2)       ;            
// 		console.log(window.location.protocol)
// 		console.log( window.location.pathname);

 
<%-- 	var saleNumberList = '<%=request.getAttribute("SaleNumberList")%>'; --%>
// 	  ;    
	   
	var saleNumberList = JSON.parse('${SaleNumberList}');
	userStatusList = JSON.parse('${UserStatusList}');  
	$('#SL_userStatus').selectpicker();             
	addUserStatusOption(userStatusList );        
// 	$('#SL_userStatus').selectpicker('val', userStatusList);     
  	addSelectOption(saleNumberList)
// 	$('input[name="daterange"]').on('apply.daterangepicker', function(ev, picker) {
// 	      $(this).val(picker.startDate.format('DD/MM/YYYY') + ' - ' + picker.endDate.format('DD/MM/YYYY'));
// 	  });
	$('#input_saleOrderDate').val('');    
	$('#input_prdOrderDate').val('');
	 $('input[name="daterange"]').on('cancel.daterangepicker', function(ev, picker) {
	      $(this).val('');     
	  });
	$(document).ajaxStart(function() {$( "#loading").css("display","block"); });   
	$(document).ajaxStop(function() {$("#loading" ).css("display","none"); });     
	  
	
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
			if(prdOrder == 'รอจัด Lot' ){
				swal({
		   		    title: 'Warning',   
		   		    text: 'Need to select atleast 1 row.',
		   		    icon: 'warning',
		   		    timer: 1000,
		   		    buttons: false,
		   		})
			}      
			else {
				  goToLBMS(tblData);
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
			if(prdOrder == 'รอจัด Lot' ){
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
			if(prdOrder == 'รอจัด Lot' ){
				swal({
		   		    title: 'Warning',   
		   		    text: 'Need to select atleast 1 row.',
		   		    icon: 'warning',
		   		    timer: 1000,
		   		    buttons: false,
		   		})
			}      
			else {
				goToInspect(tblData); 
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
			if(prdOrder == 'รอจัด Lot' ){
				swal({
		   		    title: 'Warning',   
		   		    text: 'Need to select atleast 1 row.',
		   		    icon: 'warning',
		   		    timer: 1000,
		   		    buttons: false,
		   		})
			}      
			else {
				goToSFC(tblData);
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
			if(prdOrder == 'รอจัด Lot' ){
				swal({
		   		    title: 'Warning',   
		   		    text: 'Need to select atleast 1 row.',
		   		    icon: 'warning',
		   		    timer: 1000,
		   		    buttons: false,
		   		})
			}      
			else {
				 goToQCMS(tblData);
			}   
		} 
 	} ); 
    $('#btn_search').on( 'click', function () {      
    	searchByDetail();   
 	} );       
    $('#btn_colSetting').on( 'click', function () {      
//		MainTable. colReorder.order( [ 5,1,2,3,4,0,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23 ] ); 
		$('#modalColSetting').modal('show'); 
	} );     	 
    $('#btn_download').on( 'click', function () {      
    	var ObjMarkup = MainTable.data().toArray(); 
		exportCSV(ObjMarkup)            
 	} );  
//     var StartDate = $("#input_requestDate").data('daterangepicker').startDate.format('DD/MM/YYYY');
// 	 var EndDate = $("#input_requestDate").data('daterangepicker').endDate.format('DD/MM/YYYY'); 


	$('#MainTable thead tr').clone(true).appendTo('#MainTable thead');
	$('#MainTable thead tr:eq(1) th') .each( function(i) {      
		var title = $(this).text();         
		$(this).html( '<input type="text" class="monitor_search" style="width:100%" data-index="' + i + '"/>');
	});     
	var today = new Date();  
    var date = new Date((today.getMonth()+1)+'/'+today.getDate()+'/'+today.getFullYear()); 
    var startdate = moment();
   var  subDate = startdate.add(7, "days");
    subDate = startdate.format("MM/DD/YYYY");      
    var sevenDayAgo = new Date(subDate) ;   
    MainTable = $('#MainTable').DataTable({    
// 		data : jsonData,
 	    select : true,    
 	   	scrollX: true,        
 	    scrollY: '70vh' , //ขนาดหน้าจอแนวตั้ง 
 	   	scrollCollapse: true,   
	   	orderCellsTop : true,           
		orderClasses : false,           
// 		lengthChange: false,              
// "deferRender": true,              	
// 		filter:false,       
		lengthChange : false,     
		pageLength: 1000,	 
// 		lengthMenu: [[1000, -1], [1000, "All"]],
// 		colReorder: {      
//             realtime: true       
//         },                       
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
		    {"data" : "SaleQuantity",  "title": 'Order Qty.'}, 
		    {"data" : "BillQuantity",  "title": 'Shipped Qty.'}, 
		    {"data" : "SaleUnit",  "title": 'Unit'}, 
		    {"data" : "ProductionOrder",  "title": 'Prod.No.'}, 
		    {"data" : "TotalQuantity",  "title": 'Prod.Qty.'},      //10 
		    {"data" : "GreigeInDate",  "title": 'Greige In'}, 
		    {"data" : "UserStatus",  "title": 'User Status'},     
		    {"data" : "DueDate",  "title": 'Due Date'},        
		    {"data" : "Prepare",  "title": 'Prepare'},   
		    {"data" : "Preset",  "title": 'PS'},                 //15
		    {"data" : "DyePlan",  "title": 'Dye [Plan]'},            //16
		    {"data" : "DyeActual",  "title": 'Dye [Actual]'},     
		    {"data" : "Dryer" ,  "title": 'Dryer Date'},     
		    {"data" : "Finishing",  "title": 'FN'},  
		    {"data" : "Inspectation",  "title": 'Inspect'},       //20 
		    {"data" : "CFMPlanDate",  "title": 'วันนัด CF[Plan]'},        //21 
		    {"data" : "CFMDateActual",  "title": 'วันนัด CF[Actual]'},        //22
		    {"data" : "DeliveryDate",  "title": 'วันนัดส่งของ'},       //23
		    {"data" : "LotShipping",  "title": 'Shipping'}         //24    
// 		    {"data" : "ShipDate"}          //23   
		],       
//     col,      	           
		columnDefs :  [	       
			{ targets : [ 0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16 ,17,18,19,20,21,22  ,23 ,24],                
// 			  	  className : 'data-custom-main',        
// 		  	 	  type: 'string'      
				} ,            
			{ targets :  [24],                   
			  	  className : 'p-r-15',                  
				} ,   
				{ targets :  [16,21,23],                            
// 				  	  className : 'bg-color-azure',                  
					} ,   
// 			{ targets : [ 13],  
// 				type: 'date-euro'          
// 				} , 
						 
		 	{ targets : [ 13 ],       
		  	 	  type: 'string', 
			   	  render: function (data, type, row) {	     
// 			   		  var html = ''  
// 			   		  console.log(row,type)
// 				   		if(row.DueDate != "") {  
// 			 	    		var datearray = row.DueDate.split("/");
// 				  	    	var dueDate = new Date(datearray[1] + '/' + datearray[0] + '/' + datearray[2]) ;   
// 					  	    if (dueDate < today){     
// 					  	    	 $('td', row).eq(13).addClass('dt-custom-overdue'); 
// 	//		 		  	    	$(row).addClass('dt-custom-overdue');    
// 								html = ' <div class="dt-custom-overdue">' + row.DueDate+ '</div>' 
// 					        }
// 					  	    else{
// 					  	    	html = ' <div >' + row.DueDate+ '</div>' 
// 					  	    }
// 			  	    	}   
// 				   		else{
// 				   			html = ' <div >' + row.DueDate+ '</div>' 
// 				   		}     
						return  row.DueDate;
			   	  }    
				} ,    
// 		 	{ targets : [ 2 ], 
// 		 		className : 'status',   
// 	  	 	  type: 'string',
// 		   	  render: function (data, type, row) {	 
// 					return '<input class="form-control ColorCusInput"  name="ColorCus" type="text"  value = "' + row.ColorCustomer+ '" autocomplete="off">';
// 		   	  } 
// 			} ,   
// 			{  targets : [ 3 ],     
// 				className : 'status',       
// 				type: 'string',     
// 				render: function (data, type, row) {
//  					return '<input  class="form-control ShadeTestInput"  name="ShadeTest" type="text"  style="width:100%;overflow:auto;"  value = "' + row.ShadeTest + '" autocomplete="off">';
//                	} 
// 			},    
		],
// 		 order: [[2, 'asc'], [1, 'asc']],  
// 		rowsGroup: [ 0 ,1,2,3,4,5,6,7,8  ],   
	 	select : {             
			style: 'os',         
		 	selector: 'td:not(.status)'  // .status is class        
  		},    
  		createdRow : function(row, data, index) {
// 			$('td', row).eq(16).addClass('bg-color-azure');
//   	        $('td', row).eq(21).addClass('bg-color-azure');    
//   	        $('td', row).eq(23).addClass('bg-color-azure');   
//   			console.log(data.DueDate, index )                   
  			if(mapsDataHeader.size != 0){     
//   				$('td', row).eq(mapsDataHeader.get("DueDate")).addClass('dt-custom-overdue') ;      
  				if(data["DueDate"] != "") {      
	 	    		var datearray = data["DueDate"].split("/");
		  	    	var dueDate = new Date(datearray[1] + '/' + datearray[0] + '/' + datearray[2]) ;   
// 		  	    	console.log(sevenDayAgo >= dueDate,sevenDayAgo ,dueDate)
			  	    if (sevenDayAgo >= dueDate   ){             
			  	    	$('td', row).eq(mapsDataHeader.get("DueDate")).addClass('dt-custom-overdue');        
			        }
  	    		}     
  			}
  			if(mapsDataHeader.size != 0){ $('td', row).eq(mapsDataHeader.get("DyePlan")).addClass('bg-color-azure');      }
  			if(mapsDataHeader.size != 0){ $('td', row).eq(mapsDataHeader.get("CFMPlanDate")).addClass('bg-color-azure');      }
  			if(mapsDataHeader.size != 0){ $('td', row).eq(mapsDataHeader.get("DeliveryDate")).addClass('bg-color-azure');      }
//   			console.log(data["SaleOrder"], index )              
// 			if (index == 16 || index == 21 || index == 22 ) { $(row).addClass('bg-color-azure'); } 
// 			console.log(row  )      
//   	    	if(data["DueDate"] != "") {  
//  	    		var datearray = data["DueDate"].split("/");
// 	  	    	var dueDate = new Date(datearray[1] + '/' + datearray[0] + '/' + datearray[2]) ;   
// 		  	    if (dueDate < today){     
// 		  	    	 $('td', row).eq(13).addClass('dt-custom-overdue'); 
// // 		  	    	$(row).addClass('dt-custom-overdue');    
// 		        }
//   	    	}
// 			if (data["OperationStartDate"] != "") { $(row).addClass('dt-custom-overdue'); } 
		},
		drawCallback: function( settings ) { 
// 			console.log(settings)
		},   
		initComplete: function () { }      
 	 });   
// 	console.log( 'Table\'s column visibility are set to: '+MainTable.columns().visible(true).join(', ') );
// 	console.log( 'Table\'s column non visibility are set to: '+MainTable.columns().visible().join(', ') );
//     $('#MainTable').on('dblclick','td',function(e){
// // 		scroll_to_contact_form_fn()      
// 	    var row_object  = MainTable.row(this).data();
// 	    var cellDate  = MainTable.row(this).data().Date;
// 	    var col = MainTable.cell(this).index().column;
// 	    arrTmp = [];
// 		checkClick = '1';
// 		cellDateTmp = cellDate;
// 		colDataTmp = col;
// 		arrTmp.push(cellDate)   
// 		arrTmp.push(col) 
// 		console.log(col)
// 		console.log(cellDate)    
// 		console.log(row_object)
// 		$('#modalRedirect').modal('show');   
		
// 	})
 
    poTable = $('#poTable').DataTable({  
    	scrollY:       '400px',        
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
// 		console.log(this.value)        
		let searchVal = this.value;      
		if(searchVal  == ' '){                       
// 			console.log(" DyePlan = ''" )  
// 			var rows = MainTable.Select($(this).data('index')+" = ''"); 
			MainTable.column($(this).data('index')).search( '^$', true, false ).draw();; 
		}      
		else{
			MainTable.column($(this).data('index')).search(searchVal).draw(); 
// 			MainTable.column($(this).data('index')).search( '^'+searchVal+'$', true, false ).draw(); 
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
	colList = JSON.parse('${ColList}');   
  	columnsHeader = MainTable.settings().init().columns;  
	addColOption(columnsHeader ) 
	$('#multi_colVis').selectpicker();   
	settingColumnOption(columnsHeader, colList);
	$('#save_col_button').on( 'click', function () {       
	    var selectedItem = $('#multi_colVis').val();    // get selected list  
		setColVisible(columnsHeader,selectedItem);
		saveColSettingToServer(selectedItem);     
		get_visible_columns()
 	} ); 
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
});  
 
function searchByDetail(){ 
	var customer = document.getElementById("input_customer").value .trim();
	var customerShort = document.getElementById("input_customerShortName").value .trim();
	 
	var saleOrder = document.getElementById("input_saleOrder").value .trim();  
	var article = document.getElementById("input_article").value .trim();  
	var prdOrder = document.getElementById("input_prdOrder").value .trim();
	var saleNumber = document.getElementById("SL_saleNumber").value .trim();
	var SaleOrderDate = document.getElementById("input_saleOrderDate").value .trim();
	var designNo = document.getElementById("input_designNo").value .trim(); 
	var prdOrderDate = document.getElementById("input_prdOrderDate").value .trim(); 
	var material = document.getElementById("input_material").value .trim(); 
	var labNo = document.getElementById("input_labNo").value .trim(); 
	var userStatus = document.getElementById("SL_userStatus").value .trim();  
// 	console.log(userStatus)  
	var deliStatus = document.getElementById("SLDelivStatus").value .trim();      
	var dmCheck = document.getElementById("check_DM").checked;        
	var exCheck = document.getElementById("check_EX").checked;  
	var hwCheck = document.getElementById("check_HW").checked;  
	var dist = "";     
	 var saleStatus = document.querySelector('input[name="saleStatusRadio"]:checked').value;
	 if( dmCheck ){ dist = "DM";}
	 if( exCheck ){ if(dist != "") {dist = dist + "," } dist = "EX";}     
	 if( hwCheck ){ if(dist != "") {dist = dist + "," } dist = "HW";}     
	if(customer == '' && saleOrder == '' && article == '' && prdOrder == '' && saleNumber == '' && SaleOrderDate == '' && 
	   designNo == '' && prdOrderDate == '' && material == '' && labNo == '' && userStatus == '' && deliStatus == '' &&
	   customerShort == '')  {    
		swal({
   		    title: 'Warning',
   		    text: 'Need input some field for search.',
   		    icon: 'warning',
   		    timer: 1000,
   		    buttons: false,
   		})
	}
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
	var customer = document.getElementById("input_customer").value .trim();  
	var customerShort = document.getElementById("input_customerShortName").value .trim();
	 
	var saleOrder = document.getElementById("input_saleOrder").value .trim();  
	var article = document.getElementById("input_article").value .trim();  
	var prdOrder = document.getElementById("input_prdOrder").value .trim();
	var saleNumber = document.getElementById("SL_saleNumber").value .trim();
	var saleOrderDate = document.getElementById("input_saleOrderDate").value .trim();
	var designNo = document.getElementById("input_designNo").value .trim(); 
	var prdOrderDate = document.getElementById("input_prdOrderDate").value .trim(); 
	var material = document.getElementById("input_material").value .trim(); 
	var labNo = document.getElementById("input_labNo").value .trim(); 
	var userStatus = document.getElementById("SL_userStatus").value .trim();  
	var deliStatus = document.getElementById("SLDelivStatus").value .trim();  
	var dmCheck = document.getElementById("check_DM").checked;  
	var exCheck = document.getElementById("check_EX").checked;  
	var hwCheck = document.getElementById("check_HW").checked;     
	var dist = "";    
	 var saleStatus = document.querySelector('input[name="saleStatusRadio"]:checked').value;
	 if( dmCheck ){ dist = "DM";}
	 if( exCheck ){ if(dist != "") {dist = dist + "," } dist = dist + "EX";}       
	 if( hwCheck ){ if(dist != "") {dist = dist + "," } dist = dist + "HW";}
	var json = '{"CustomerName":'+JSON.stringify(customer)+ 
	 ',"CustomerShortName":'+JSON.stringify(customerShort)+ 
	   ',"SaleOrder":'+JSON.stringify(saleOrder)+ 
	   ',"ArticleFG":'+JSON.stringify(article)+  
	   ',"ProductionOrder":'+JSON.stringify(prdOrder)+  
	   ',"SaleNumber": '+JSON.stringify(saleNumber)+
	   ' ,"SaleOrderCreateDate":'+JSON.stringify(saleOrderDate)+
	   ',"DesignFG":'+JSON.stringify(designNo)+       
	   ',"ProductionOrderCreateDate":'+JSON.stringify(prdOrderDate)+ 
	   ',"MaterialNo":'+JSON.stringify(material)+
	   ',"LabNo":'+JSON.stringify(labNo)+ 
	   ',"UserStatus":'+JSON.stringify(userStatus)+ 
	   ',"DeliveryStatus":'+JSON.stringify(deliStatus)+         
	   ',"SaleStatus":'+JSON.stringify(saleStatus)+  
	   ',"DistChannel":'+JSON.stringify(dist) + 
	   '} ';     
// 	   console.log(json) 
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
//     console.log(colList );   
//     console.log(columnsHeader );    
    let xlsHeader = Array.from( mapsTitleHeader.keys() );
//     let xlsHeader = Array.from(map, ([name, value]) => ({ name, value }));
     
    /* XLS Rows Data */   
    var xlsRows = data   
    createXLSLFormatObj.push(xlsHeader);
    $.each(xlsRows, function(index, value) {    
        var innerRowData = [];  
//         console.log(index,value)       
        $.each(value, function(ind, val) {               	
//             console.log(ind,val)        
// 			console.log(mapsDataHeader.get(ind))          
            if(mapsDataHeader.size != 0){                
			  	if(mapsDataHeader.get(ind) !== undefined){   
// 			  		 console.log(ind,val) 
			  		 innerRowData.push(val); 
			  	}
            }
        }); 
//         innerRowData.push(value.SaleOrder ); 
//         innerRowData.push(value.SaleLine ); 
//         innerRowData.push(value.ArticleFG ); 
//         innerRowData.push(value.DesignFG ); 
//         innerRowData.push(value.Color ); 
//         innerRowData.push(value.ColorCustomer ); 
//         innerRowData.push(value.SaleQuantity ); 
//         innerRowData.push(value.BillQuantity ); 
//         innerRowData.push(value.SaleUnit ); 
//         innerRowData.push(value.ProductionOrder ); 
//         innerRowData.push(value.TotalQuantity ); 
//         innerRowData.push(value.GreigeInDate ); 
//         innerRowData.push(value.UserStatus ); 
//         innerRowData.push(value.DueDate );   
//         innerRowData.push(value.Prepare ); 
//         innerRowData.push(value.Preset ); 
//         innerRowData.push(value.DyePlan ); 
//         innerRowData.push(value.DyeActual ); 
//         innerRowData.push(value.Dryer ); 
//         innerRowData.push(value.Finishing );  
//         innerRowData.push(value.Inspectation );  
//         innerRowData.push(value.CFMDatePlan );  
//         innerRowData.push(value.CFMDateActual );   
//         innerRowData.push(value.DeliveryDate );  
//         innerRowData.push(value.LotShipping );         
        
//         innerRowData.push(value);
        createXLSLFormatObj.push(innerRowData);
    }); 
    /* File Name */
    var filename = "PCMSMain.xlsx"; 
    /* Sheet Name */
    var ws_name = "PCMSMain"; 
    if (typeof console !== 'undefined') console.log(new Date());
    var wb = XLSX.utils.book_new(),
        ws = XLSX.utils.aoa_to_sheet(createXLSLFormatObj); 
    /* Add worksheet to workbook */
    XLSX.utils.book_append_sheet(wb, ws, ws_name); 
    /* Write workbook and Download */
    if (typeof console !== 'undefined') console.log(new Date());
    XLSX.writeFile(wb, filename);
    if (typeof console !== 'undefined') console.log(new Date()); 
	  
}   
function getPrdDetailByRow(arrayTmp) {     
// 	console.log(arrayTmp)
	$.ajax({   
		type: "POST",
		contentType: "application/json",  
		data: JSON.stringify(arrayTmp),      
		url: "PCMSMain/getPrdDetailByRow", 
		success: function(data) {      
// 			MainTable.clear();          
// 			MainTable.rows.add(data);     
// 			MainTable.draw();  
// 			console.log(data)
setModalDetail(data);   
		},   
		error: function(e) {
			swal("Fail", "Please contact to IT", "error");
		},
		done: function(e) {       
		}   	
	});   
}       
function searchByDetailToServer(arrayTmp) {   
// 	console.log(arrayTmp)
	$.ajax({
		type: "POST",  
		contentType: "application/json",  
		data: JSON.stringify(arrayTmp),      
		url: "PCMSMain/searchByDetail", 
		success: function(data) { 
// 			console.log(data)
			
// 			get_visible_columns();
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
function goToLBMS(tblData){    
	var prdOrder = tblData[0].ProductionOrder
	var article = tblData[0].ArticleFG
// 	var prdOrder = document.getElementById('input_prdOrder').value ;
	var color = tblData[0].Color 
	$.ajax({
//   	    url: "http:/pcms.a-tech.co.th:8080/LBMS/LabHistory",  
  	    url: urlLBMS,
  	    type : 'GET',   
  	    data : { "comeFrom": "PCMS" },   
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
					},1000);                      
// 				console.log( tab.$('#byPrdTable').DataTable()  )  
   	    	};     
	   		tab.addEventListener('load', (event) => { 

   	    	});        
   	    } 
   	}); 
}
function timeFunction() {
    setTimeout(function(){ alert("After 2 seconds!"); }, 2000);
}
function goToSFC(tblData){ 
	var prdOrder = tblData[0].ProductionOrder
	$.ajax({
// 	    url: "http://localhost:8080/SFC/",    
	    url: urlSFC,
	    type : 'GET',      
	    data : {
	    	"comeFrom": "PCMS"           
	    },          
	    success : function(data) {    
//   	    	var url = "http://localhost:8080/SFC/HistoryWork"; 
  	    	var url = urlSFCObj; 
  	    	var tab = window.open(url );  //var tab = window.open(url, '_blank').focus();
  	    	tab.onload = function() {               
//	  	    		console.log(tab.document.getElementById('input_searchProductionOrder'))
					tab.document.getElementById('input_searchProductionOrder').value = prdOrder;  
// 					console.log(tab.document.getElementById('id_searchPrdBtn'))    
					tab.searchByPrdOrder(prdOrder);                
   	    	};        
	    }   	
	});      
// 	test3T();
} 
function goToInspect(tblData){
// 1. FOR CREATE SESSION BY AJAX
// BCOZ SESSION IS NULL IN AJAX
// 2. FOR CREATE USER LOGIN IN SESSION IN SUCCESS
// SESSION ALREADY GENERATE SO WE CAN ADD USER ATRRIBUTE
// 3. 
// GO TO PAGE WHERE WE WANT   
	var prdOrder = tblData[0].ProductionOrder
	$.ajax({
// 	    url: "http://localhost:8080/InspectSystem/search/home.html",   
	    url:urlInspect,
	    type : 'GET',      
// 	    async : false,
	    data : {
	    	"comeFrom": "PCMS"           
	    },    
	    success : function(data) { 
// 	    	var url = "http://localhost:8080/InspectSystem/search/home.html";  
	    	var url = urlInspectObj; 
	    	var tab = window.open(url );
	    	tab.onload = function() {    
	    	      tab.document.getElementById('prdNumber').value = prdOrder  ;  //'S2A001'
	    	      tab.document.getElementById('btnSearch').click();     
    	    };      
	    }
	});      
// 	test3T(); 
}  
function goToQCMS(tblData){
	// 1. FOR CREATE SESSION BY AJAX
	// BCOZ SESSION IS NULL IN AJAX
	// 2. FOR CREATE USER LOGIN IN SESSION IN SUCCESS
	// SESSION ALREADY GENERATE SO WE CAN ADD USER ATRRIBUTE
	// 3. 
	// GO TO PAGE WHERE WE WANT   
		var article = tblData[0].ArticleFG
		var lotNo = tblData[0].LotNo
		var color = tblData[0].Color
		$.ajax({
//	 	    url: "http://localhost:8080/InspectSystem/search/home.html",   
		    url: urlQCMSObj,
		    type : 'GET',      
//	 	    async : false,
		    data : {
		    	"comeFrom": "PCMS"            
		    },    
		    success : function(data) { 
//	 	    	var url = "http://localhost:8080/InspectSystem/search/home.html";  
		    	var url = urlQCMSObj; 
		    	var tab = window.open(url );
		    	tab.onload = function() {    
		    	      tab.document.getElementById('article').value = article  ;  //'S2A001'
		    	      tab.document.getElementById('lotNumber').value = lotNo  ;  //'S2A001'
		    	      tab.document.getElementById('color').value = color  ;  //'S2A001' 
		    	      tab.document.getElementById('btnSearchRequest').click();       
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
function addUserStatusOption(data ){ 
	// The DOM way. 
	var sel = document.getElementById("SL_userStatus");
// 	for (i = sel.length - 1; i >= 0; i--) {       
// 		sel.remove(i);
// 	}             
	var size = data.length;
	for (var i = 0; i < size; i++) {		
		 var resultData = data[i]; 	   
		 var opt = document.createElement('option');
	     opt.appendChild(document.createTextNode(i));
		 opt.text  = resultData.UserStatus;
		 opt.value = resultData.UserStatus;
		 sel.appendChild(opt);          
	}             
	$("#SL_userStatus").selectpicker("refresh");
}         
function saveInputDateToServer(arrayTmp) {   
// 	console.log(arrayTmp)
	$.ajax({   
		type: "POST",  
		contentType: "application/json",  
		data: JSON.stringify(arrayTmp),         
		url: "PCMSMain/saveInputDate", 
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
		setColVisible(columnsHeader,colVisible);    
		$('#multi_colVis').selectpicker('val', colVisible);     
	}
	get_visible_columns()     
}
        
function get_visible_columns() {   
// 	columnsHeader = MainTable.settings().init().columns;
   	mapsDataHeader.clear();   
   	mapsTitleHeader.clear();
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
			mapsTitleHeader.set(columnsHeader[i].title, count); 
			mapsDataHeader.set(columnsHeader[i].data, count);
			count = count + 1
		}   
		else{        
// 			unvisible 
		} 
	}    
// 	console.log(mapsDataHeader )
}     
function setColVisible(mainCol,colVisible) {    
// 	console.log(colVisible)   
// 	console.log(mainCol)  
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
		url: "PCMSMain/saveColSettingToServer", 
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
</script>
</html>