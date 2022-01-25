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
<%-- 	<jsp:include page="/WEB-INF/pages/config/css/multi_Select.jsp"></jsp:include> --%>
		<link href="<c:url value="/resources/css/style_overide.css" />" rel="stylesheet" type="text/css">    
		<link href="<c:url value="/resources/css/datatable.overide.css" />" rel="stylesheet" type="text/css">       
</head>      
<body>       	     
	<jsp:include page="/WEB-INF/pages/config/navbar.jsp"></jsp:include> 
	<jsp:include page="/WEB-INF/pages/config/loading.jsp"></jsp:include>             
		<div class="row" id="wrapper-top"       
		style="  margin: 2px 0px;  background-color: azure; ">    
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
					  	<label class=" label-input  margin-0" for="input_saleNumber">SaleName</label>           
					  	<div > 
				    		<select id="SL_saleNumber" class="form-control "   >      
								<option value="" selected>Select</option>   
							</select>
						</div>     
					</div>                     
					<div class="col-12 col-sm-6 col-md-4 col-lg-3 col-xl-3 form-group lab-print" >              
					  	<label class=" label-input  margin-0" for="input_saleOrderDate">SO Date</label>           
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
					  	<label class=" label-input  margin-0" for="input_labNo">Lab No.</label>           
					  	<div > 
				    		<input class="form-control " autocomplete="off"  type="text"  id="input_labNo"   maxlength="30" > 
						</div>     
					</div>   
					<div class="col-12 col-sm-6 col-md-4 col-lg-3 col-xl-3 form-group lab-print" >              
					  	<label class=" label-input  margin-0" for="SL_userStatus">UserStatus</label>           
					  	<div > 
<!-- 				    		<input class="form-control " autocomplete="off"  type="text"  id="input_userStatus"   maxlength="30" >  -->
							<select id="SL_userStatus"   class="selectpicker"  data-live-search="true">
								<option value="" selected>SelectAll</option> 
							</select>
						</div>        
					</div>   
					<div class="col-12 col-sm-6 col-md-4 col-lg-3 col-xl-3 form-group lab-print" >              
					  	<label class=" label-input  margin-0" for="SL_delivStatus">Delivery Status</label>           
					  	<div >    
				    		<select id="SL_delivStatus" class="form-control "   >      
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
					
					
					 
				</div>   
			</div>           
		</div>                  
		<div id="wrapper-center" class="row" style="margin: 0 5px;">     
		<div class="col-12 col-sm-12 col-md-12 col-lg-12 col-xl-12 " style="    font-size: 12.5px;padding: 0px;margin: 0px 0px;    " >        
<!-- 			<div style="    font-size: 12.5px;padding:  0px;margin: 0px 0px;    " >                     -->
				<div class="table-responsive ">             
					<table id="MainTable" class="table compact table-bordered table-striped text-center" style="font width:100%;    margin: 0px !important;">
				        <thead>
				            <tr>    
				                <th class=" " style="vertical-align: middle;">DIV </th>
				                <th class=" " style="vertical-align: middle;">SO No.</th>
				                <th class=" " style="vertical-align: middle;">SO Line </th>
				                <th class=" " style="vertical-align: middle;">Cust.(Name4)</th> 
				                <th class=" " style="vertical-align: middle;">SO Date </th>  
				                    
				                <th class=" " style="vertical-align: middle;">P/O </th>
				                <th class=" " style="vertical-align: middle;">Material </th>	
				                <th class=" " style="vertical-align: middle;">Customer<span class="c"style="display: block;"> Mat.No.</span> </th> 
				                <th class=" " style="vertical-align: middle;">Price <span class="c"style="display: block;">(THB)</span></th>
				                <th class=" " style="vertical-align: middle;">Unit </th>
				                <th class=" " style="vertical-align: middle;">Order<span class="c"style="display: block;">Qty.</span> </th>
				                <th class=" " style="vertical-align: middle;">Remain<span class="c"style="display: block;">Qty.</span> </th>
				                <th class=" " style="vertical-align: middle;">Remain<span class="c"style="display: block;">Amt.(THB)</span> </th>
				                <th class=" " style="vertical-align: middle;">จำนวนต่อ LOT </th>
				                <th class=" " style="vertical-align: middle;">Grade </th>  
				                <th class=" " style="vertical-align: middle;">จำนวน (FG) <span class="c"style="display: block;">KG/MR/YD</span> </th> 
				                <th class=" " style="vertical-align: middle;">จำนวนที่ส่ง </th> 
				                <th class=" " style="vertical-align: middle;">Amt.<span class="c"style="display: block;">(THB)</span> </th>
				                <th class=" " style="vertical-align: middle;">Due Cus. </th> 
				                <th class=" " style="vertical-align: middle;">Due Date </th>  
				                <th class=" " style="vertical-align: middle;">Lab No </th> 
				                <th class=" " style="vertical-align: middle;">Lab Status</th> 
				                <th class=" " style="vertical-align: middle;">Lot </th> 
				                <th class=" " style="vertical-align: middle;">วันนัด<span class="c"style="display: block;">CFM LAB</span> </th> 
				                <th class=" " style="vertical-align: middle;">วันส่ง<span class="c"style="display: block;"> CFM LAB</span> </th> 
				                  
				                <th class=" " style="vertical-align: middle;">วันที่ลูกค้า <span class="c"style="display: block;"> ตอบ LAB</span> 	 </th> 
				                 
				                <th class=" " style="vertical-align: middle;">TK CFM</span> </th> 
				                <th class=" " style="vertical-align: middle;">วันที่นัด CFM </th>    
				                <th class=" " style="vertical-align: middle;">วันที่ส่ง CFM จริง</th> 
				                <th class=" " style="vertical-align: middle;">User Status </th>    
<!-- 				                <th class=" " style="vertical-align: middle;">วันที่ ตอบ และ <span class="c"style="display: block;">Result ที่ตอบ CFM</span> </th>  -->
			                    <th class=" " style="vertical-align: middle;">Lastest<span class="c"style="display: block;">CFM Detail  </span> </th> 
				                <th class=" " style="vertical-align: middle;">Lastest<span class="c"style="display: block;">CFM Number</span> </th> 
<!-- 				                <th class=" " style="vertical-align: middle;">NO.CFM<span class="c"style="display: block;">แสดงประวัติทั้งหมด</span> <span class="c"style="display: block;">(เฉพาะที่รอตอบ)</span> </th>    -->
				                <th class=" " style="vertical-align: middle;">Delivery<span class="c"style="display: block;">(วันที่นัดส่ง)</span> </th> 
				                <th class=" " style="vertical-align: middle;">Bill Date </th> 
				                <th class=" " style="vertical-align: middle;">EFFECT </th>     
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
	<jsp:include page="/WEB-INF/pages/config/PCMSDetailModal/ColumnSetting/ColumnSetting.jsp"></jsp:include> 
	<jsp:include page="/WEB-INF/pages/config/PCMSDetailModal/InputDateDetail/modalMain.jsp"></jsp:include> 
				    
</body>             
<%-- <script src="<c:url value="/resources/js/dataTables.rowsGroup.js" />"></script>          --%>
<script src="<c:url value="/resources/js/DatatableSort.js" />"></script>   
  <!-- Latest compiled and minified CSS -->
<!-- <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-select@1.13.14/dist/css/bootstrap-select.min.css">  -->
<!-- Latest compiled and minified JavaScript --> 
<!-- <script src="https://cdn.jsdelivr.net/npm/bootstrap-select@1.13.14/dist/js/bootstrap-select.min.js"></script> -->

<!-- <script src="https://cdn.datatables.net/fixedheader/3.2.0/js/dataTables.fixedHeader.min.js"></script>    -->
<style>        
.p-r-15 {padding-right: 15px !important; }     
/* table.dataTable.fixedHeader-floating { */
/*      display: none !important; /* Hide the fixedHeader since we dont need it*/ */
/* } */
 
 
/* .dataTables_scrollHeadInner{ */
/*     margin-left: 0px; */
/*     width: 100% !important;   */   
/*     position: fixed; */
/*     display: block; */
/*     overflow: hidden; */
/*     margin-right: 30px; */
/*     background: white; */
/*     z-index: 1000; */
/* } */
 
/* .dataTables_scrollBody{ */
/*     padding-top: 60px; */
/* } */
/* .dataTables_scrollBody { */
/*     overflow: overlay; */
/* }    */
</style>

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
var mapsDataHeader  = new Map(); 
var mapsTitleHeader  = new Map();    
var check1 = 0	;
var check2 = 0	; 
var check3 = 0	;
var saleNumberList ; 
var userStatusList ; 
var colList ; 
var soTmp ;   
var soLineTmp;
var InputDateTable ;
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
     
$(document) .ready( function() {   
// 	showThing();       
	$('#input_saleOrderDate').val('');    
	$('#input_prdOrderDate').val('');    
	<%-- 	var saleNumberList = '<%=request.getAttribute("SaleNumberList")%>'; --%>   
	 saleNumberList = JSON.parse('${SaleNumberList}'); 
	 colList = JSON.parse('${ColList}'); 
	 userStatusList = JSON.parse('${UserStatusList}'); 
  	addSelectOption(saleNumberList) 
//   	showThing();    
  
	
	
	 $('input[name="daterange"]').on('cancel.daterangepicker', function(ev, picker) {
	      $(this).val('');     
	  });
	$(document).ajaxStart(function() {$( "#loading").css("display","block"); });   
	$(document).ajaxStop(function() {$("#loading" ).css("display","none"); });    
//     var StartDate = $("#input_requestDate").data('daterangepicker').startDate.format('DD/MM/YYYY');
// 	 var EndDate = $("#input_requestDate").data('daterangepicker').endDate.format('DD/MM/YYYY');  
	$('#MainTable thead tr').clone(true).appendTo('#MainTable thead');
	$('#MainTable thead tr:eq(1) th') .each( function(i) {      
		var title = $(this).text();      
		$(this).html( '<input type="text" class=" monitor_search" style="width:100%" data-index="' + i + '"/>');
	});        
    MainTable = $('#MainTable').DataTable({   
		scrollX: true,                      
        scrollY: '70vh' , //ขนาดหน้าจอแนวตั้ง     
        scrollCollapse: true,   
 	   	orderCellsTop : true,           
		orderClasses : false,                      
// 		 fixedHeader: {  header: true, },    
// 		 select : true,  	
select : {             
			style: 'os',         
		 	selector: 'td:not(.status)'  // .status is class        
  		},    
		pageLength:	 1000,	 
	    lengthChange : false,  
// 		lengthMenu: [[1000, -1], [1000, "All"]], 
// //  	colReorder: true,   
// 		autoFill: { 
// 			alwaysAsk: true,
// 	 		horizontal: false,
// 	 		focus: 'click',
// 	 	 	columns: [ 22, 27, 31 ]
// 	    },
 	   	columns : 
 	   		[      
			    {"data" : "Division" ,"title":"DIV"          },         //0
			    {"data" : "SaleOrder" ,"title":"SO No."           },         //1
			    {"data" : "SaleLine"   ,"title":"SO Line"          },         //2
			    {"data" : "CustomerShortName"  ,"title":"Cust.(Name4)"  }, 
			    {"data" : "SaleCreateDate" ,"title":"SO Date"      },    
			    {"data" : "PurchaseOrder" ,"title":"P/O" },      //5 
			    {"data" : "MaterialNo" ,"title":"Material" }, 
			    {"data" : "CustomerMaterial" ,"title":"Customer Mat.No." },    //7
			    {"data" : "Price" ,"title":"Price (THB)" }, 
			    {"data" : "SaleUnit" ,"title":"Unit" }, 
			    {"data" : "SaleQuantity" ,"title":"Order Qty." },              //10
			    {"data" : "RemainQuantity" ,"title":"Remain Qty." },            //11
			    {"data" : "RemainAmount" ,"title":"Remain Amt.(THB)" }, 
			    {"data" : "TotalQuantity" ,"title":"จำนวนต่อ LOT" },             //13
			    {"data" : "Grade" ,"title":"Grade" }, 
			    {"data" : "BillSendWeightQuantity" ,"title":"จำนวน(FG)KG/MR/YD" }, 
			    {"data" : "BillSendQuantity" ,"title":"จำนวนที่ส่ง" },          //16
			    {"data" : "OrderAmount" ,"title":"Amt.(THB)" },               //17
			    {"data" : "CustomerDue" ,"title":"Due Cus." }, 
			    {"data" : "DueDate" ,"title":"Due Date" },                   //19 
			     
			    {"data" : "LabNo","title":"Lab No" },  
			    {"data" : "LabStatus","title":"Lab Status" },                 //21 
			    {"data" : "LotNo","title":"Lot" },                     //22    
			    {"data" : "CFMPlanLabDate","title":"วันนัด CFM LAB" },            //23
			    {"data" : "CFMActualLabDate","title":"วันส่ง CFM LAB" },     	    //24
			    
			    
			    
			    {"data" : "CFMCusAnsLabDate","title":"วันที่ลูกค้าตอบ LAB"},           //25
			    {"data" : "TKCFM","title":"TK CFM"},                       //26
			    {"data" : "CFMPlanDate","title":"วันที่นัด CFM"},                 //27
			    {"data" : "CFMSendDate","title":"วันที่ส่ง CFM จริง"},                 //28 
			    {"data" : "UserStatus","title":"User Status"},                  //29
				{"data" : "CFMLastest","title":"Lastest CFM Detail"},         //30
				{"data" : "CFMNumber","title":"Lastest CFM Number"},          
			    {"data" : "DeliveryDate","title":"Delivery (วันที่นัดส่ง)"},       //32
			    {"data" : "ShipDate","title":"Bill Date"},              
			    {"data" : "Remark","title":"EFFECT"},              //34
		],        	         
		columnDefs :  [	   
			{ targets : [ 4,18,23,24,24,27,32,33 ],                    
			  	  className : 'dt-custom-td80',    
			  	  type: 'date-euro'  
				} ,                   
			{ targets : [ 20 ],                     
			  	  className : 'dt-custom-td100',    
			  	  type: 'string'     
			} ,     
			{ targets : [ 22 ],                         
			  	  className : 'dt-custom-lotNo',    
			  	  type: 'string'     
			} ,     
			{ targets : [3, 5,6 , 7 ,21,29],                    
		  	  	className : 'dt-custom-td140',      
		  	  	type: 'string'   
				} ,         
			{ targets : [  30 ],                    
		  	  	className : 'dt-custom-td400',         
		  	  	type: 'string'  
				} ,            
			{ targets : [  34],                       
		  	  	className : 'dt-custom-td450 p-r-15',      
		  	  	type: 'string'  
				} ,    
		 	{ targets : [ 23 ],    
			   	  render: function (data, type, row) {
		   			var htmlEx = ''     
					if(row.LotNo.trim() == "รอจัด Lot"){ 
						htmlEx = ''; 
					}
					else{
						htmlEx = '<input class="form-control CFMPlanLabDateInput" style=" cursor: pointer; padding: 4px 2px;font-size: 12.5px"  name="CFMPlanLabDate" type="text"  value = "' + row.CFMPlanLabDate+ '" autocomplete="off" >';
					}
					return  htmlEx;
			   	  }        
				} ,  
			{ targets : [ 11 ],    
			   	  render: function (data, type, row) {	   
			   		var htmlEx = '' 
			   		
			   		if(soLineTmp == row.SaleLine && soTmp   == row.SaleOrder  ){
			   			htmlEx = '<div style="visibility: hidden;color: red; font-weight: bolder;">'+row.RemainQuantity+' </div>'; 
			   		}   
			   		else{      
			   			soLineTmp = row.SaleLine;     
			   			soTmp = row.SaleOrder;   
			   			htmlEx = '<div style="visibility: visible;color: red; font-weight: bolder;">'+row.RemainQuantity+' </div>'; 
			   		}
// 			   		console.log(soTmp,row.SaleOrder  ,row.SaleLine,soLineTmp )
			   		
// 			   		if(row.RemainQuantity.trim() == ""){ 
// 						htmlEx = ''; 
// 					}
// 					else{
// 						htmlEx = row.RemainQuantity;
// 					}
					return  htmlEx
			   	  }    
				} ,  
			{ targets : [ 27 ],    
			   	  render: function (data, type, row) {	
			   		var htmlEx = '' 
			   		if(row.LotNo.trim() == "รอจัด Lot"){ 
						htmlEx = ''; 
					}
					else{
						htmlEx = '<input class="form-control CFMPlanDateInput" style=" cursor: pointer; padding: 4px 2px;font-size: 12.5px"  name="CFMPlanDate" type="text"  value = "' + row.CFMPlanDate+ '" autocomplete="off" >';
					}
					return  htmlEx
			   	  }    
				} ,  
			{ targets : [ 32 ],     
			   	  render: function (data, type, row) {	     
			   		var htmlEx = ''   
				   		if(row.LotNo.trim() == "รอจัด Lot"){ 
							htmlEx = ''; 
						}
						else{
							htmlEx = '<input class="form-control DeliveryDateInput" style=" cursor: pointer; padding: 4px 2px;font-size: 12.5px"  name="DeliveryDate" type="text"  value = "' + row.DeliveryDate+ '" autocomplete="off" >';
						}
						return  htmlEx      
					}       
			} ,     
		],
// 		 order: [[2, 'asc'], [1, 'asc']],     	   
// 		rowsGroup: [ 0 ,1,2,3,4,5,6,7,8,9 ,10,11,12,13],   
// 		rowsGroup: [ 0 ,1,2, 11 ],       
// 		order : [],   
		createdRow : function(row, data, index) {
// 			$('td', row).eq(22).addClass('bg-color-azure');
//   	        $('td', row).eq(27).addClass('bg-color-azure');    
//   	        $('td', row).eq(31).addClass('bg-color-azure'); 
// 			if (index == 16 || index == 21 || index == 22 ) { $(row).addClass('bg-color-azure'); } 
// 			else if (data["OperationStartDate"] != "") { $(row).addClass('bg-start-im'); } 
		},
		drawCallback: function( settings ) {  
		},   
		initComplete: function () { }  
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
//     $('#MainTable').wrap("<div class='scrolledTable'></div>");     
	// SEARCH BY FILTER UNDER COLUMN NAME : BOAT         
	$(".dataTables_scrollHead").on('keyup', '.monitor_search', function() {
// 		console.log(this.value)    
		MainTable.column($(this).data('index')).search(this.value).draw(); 
	});                
// 	 var presetTable ;var dyeingTable;var fnTable;var inspectTable;var packingTable;var sendTestQCTable;
	columnsHeader = MainTable.settings().init().columns;  
	addColOption(columnsHeader ) 
	$('#multi_colVis').selectpicker();   
	settingColumnOption(columnsHeader, colList);   
// 	console.log(columns,colList)  
	$('#SL_userStatus').selectpicker();      
	addUserStatusOption(userStatusList );    
// 	console.log(userStatusList) 
// 	$('#SL_userStatus').selectpicker('val', userStatusList); 
	
	
	$("#MainTable_filter").hide();      
// 	$("#MainTable_info").hide();      
	  
	$("#InputDateTable_filter").hide();  
// 	$("#InputDateTable_info").hide();      
    $('a[data-toggle="tab"]').on('shown.bs.tab', function (e) { 
    	MainTable.columns.adjust();  
    	InputDateTable.columns.adjust();  
    });         
    $('.modal').on('shown.bs.modal', function() {   
		MainTable.columns.adjust();  
		InputDateTable.columns.adjust();  
	})   	
 
// ----------------------------------------------
	$('#save_col_button').on( 'click', function () {      
// 		console.log(colList)
	    var selectedItem = $('#multi_colVis').val();    // get selected list 
// 		for (i =  0; i < selectedItem.length ; i++) {              
// 			let index = columns.findIndex(element => element.data === selectedItem[i] );
// 			console.log(index )       
// 			MainTable.column( index ).visible( false ); 
// 		}    
		setColVisible(columnsHeader,selectedItem);
		saveColSettingToServer(selectedItem);
		get_visible_columns()       
 	} );   
    $('#btn_search').on( 'click', function () {      
    	searchByDetail();   
 	} );            
 	$('#btn_colSetting').on( 'click', function () {      
//  		MainTable. colReorder.order( [ 5,1,2,3,4,0,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23 ] ); 
 		$('#modalColSetting').modal('show'); 
 	} );      
    $('#btn_download').on( 'click', function () {      
    	var ObjMarkup = MainTable.data().toArray(); 
		exportCSV(ObjMarkup)            
 	} );   
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
			newValue  = checkDateFormatInput( newValue,oldValue)  
			if(newValue == ''){ 
             	rowData.CFMPlanLabDate  = oldValue;
     			MainTable.row(idx).invalidate().draw();  
             }
			else if(newValue == 'E0'){ 
             	rowData.CFMPlanLabDate  = oldValue;
     			MainTable.row(idx).invalidate().draw();  
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
			else{
				swal({ 
					  title: "Are you sure to change date?",
					  text: "From : "+oldValue+" to "+newValue+" ",
					  icon: "warning",
					  buttons: true,  
					  dangerMode: true,						   																																								  
					})
					.then((willDelete) => {
// 						console.log(willDelete)
					  if (willDelete) {     
						  rowData.CFMPlanLabDate  = newValue;
						  MainTable.row(idx).invalidate().draw();  
	 				 		var json = createInputDateJsonData(rowData,'CFMPlanLabDate'); 
	 			 			var  obj = JSON.parse(json);    
	 			 			var arrayTmp = [];  
	 						arrayTmp.push(obj);       
	 						saveInputDateToServer(arrayTmp);     
					  } else { 
						  rowData.CFMPlanLabDate  = oldValue;
						  MainTable.row(idx).invalidate().draw(); 
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
			 if(newValue == ''){ 
	             	rowData.CFMPlanLabDate  = oldValue;
	     			MainTable.row(idx).invalidate().draw();  
	             }
				else if(newValue == 'E0'){ 
             	rowData.CFMPlanLabDate  = oldValue;
     			MainTable.row(idx).invalidate().draw();  
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
						  MainTable.row(idx).invalidate().draw();  
	 				 		var json = createInputDateJsonData(rowData,'CFMPlanLabDate'); 
	 			 			var  obj = JSON.parse(json);    
	 			 			var arrayTmp = [];  
	 						arrayTmp.push(obj);       
	 						saveInputDateToServer(arrayTmp);  
					  } else { 
						  rowData.CFMPlanLabDate  = oldValue;
						  MainTable.row(idx).invalidate().draw(); 
					  }
				}); 
			}
             check1= 0;  
		} 
	});  
	 $("#MainTable").on("keydown blur",".CFMPlanDateInput", function (e) { 
			var $row = $(this).parents("tr");
			var idx = MainTable.row($row).index();    
			var rowData = MainTable.row($row).data();	 
			var oldValue = rowData.CFMPlanDate;
	        var newValue = $(this).val();           
			if (event.keyCode === 13) {     
				check2 = 1 ;    
				 e.stopImmediatePropagation();
				 e.preventDefault();       
				newValue  = checkDateFormatInput( newValue,oldValue)   
				if(newValue == ''){ 
	             	rowData.CFMPlanDate  = oldValue;
	     			MainTable.row(idx).invalidate().draw();      
	             }
				else if(newValue == 'E0'){ 
	             	rowData.CFMPlanDate  = oldValue;
	     			MainTable.row(idx).invalidate().draw();  
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
							  rowData.CFMPlanDate  = newValue;
							  MainTable.row(idx).invalidate().draw();  
							  var json = createInputDateJsonData(rowData,'CFMPlanDate'); 
		 			 			var  obj = JSON.parse(json);    
		 			 			var arrayTmp = [];  
		 						arrayTmp.push(obj);   
		 						saveInputDateToServer(arrayTmp);  
						  } else { 
							  rowData.CFMPlanDate  = oldValue;
							  MainTable.row(idx).invalidate().draw(); 
						  }
						});    
				}	
				check2= 0;  
			
				
			}
			else if (event.type === 'blur'  && check2 == 0){    
				check2= 1; 
	 			 e.stopImmediatePropagation();  
				 e.preventDefault();       
				 newValue  = checkDateFormatInput( newValue,oldValue)    
				 if(newValue == ''){ 
		             	rowData.CFMPlanDate  = oldValue;
		     			MainTable.row(idx).invalidate().draw();  
		             }
					else if(newValue == 'E0'){ 
		             	rowData.CFMPlanDate  = oldValue;
		     			MainTable.row(idx).invalidate().draw();  
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
								  rowData.CFMPlanDate  = newValue;
								  MainTable.row(idx).invalidate().draw();   
								  var json = createInputDateJsonData(rowData,'CFMPlanDate'); 
			 			 			var  obj = JSON.parse(json);    
			 			 			var arrayTmp = [];  
			 						arrayTmp.push(obj);   
			 						saveInputDateToServer(arrayTmp);  
							  } else { 
								  rowData.CFMPlanDate  = oldValue;
								  MainTable.row(idx).invalidate().draw(); 
							  }
							});    
						
					}    
	             check2= 0; 
			}    
		});       
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
				if(newValue == ''){ 
	             	rowData.DeliveryDate  = oldValue;
	     			MainTable.row(idx).invalidate().draw();  
	             }
				else if(newValue == 'E0'){ 
	             	rowData.DeliveryDate  = oldValue;
	     			MainTable.row(idx).invalidate().draw();  
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
							 MainTable.row(idx).invalidate().draw();  
							 var json = createInputDateJsonData(rowData,'DeliveryDate'); 
		 			 			var  obj = JSON.parse(json);    
		 			 			var arrayTmp = [];  
		 						arrayTmp.push(obj);   
		 						saveInputDateToServer(arrayTmp);  
						} else { 
							 rowData.DeliveryDate  = oldValue;
							 MainTable.row(idx).invalidate().draw(); 
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
				if(newValue == ''){ 
	             	rowData.DeliveryDate  = oldValue;
	     			MainTable.row(idx).invalidate().draw();  
	             }
				else if(newValue == 'E0'){ 
	             	rowData.DeliveryDate  = oldValue;
	     			MainTable.row(idx).invalidate().draw();  
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
							  MainTable.row(idx).invalidate().draw();   
							  var json = createInputDateJsonData(rowData,'DeliveryDate'); 
		 			 			var  obj = JSON.parse(json);    
		 			 			var arrayTmp = [];  
		 						arrayTmp.push(obj);   
		 						saveInputDateToServer(arrayTmp);  
						  } else { 
							  rowData.DeliveryDate  = oldValue;
							  MainTable.row(idx).invalidate().draw(); 
						  }
						});    
					
				} 
	             check3= 0; 
			} 
		});  
	 
	$('#MainTable').on('dblclick','td',function(e){
// 		scroll_to_contact_form_fn()  
	    var row_object  = MainTable.row(this).data();    
	    if(MainTable.cell(this).index() === undefined){
	    	
	    } 
	    else{      
	    	 var colIdx = MainTable.cell(this).index().column; 
		    var arrTmp = [];
			arrTmp.push(row_object);
			getInputDate(arrTmp,colIdx);
	    }
	    
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
	var deliStatus = document.getElementById("SL_delivStatus").value .trim(); 
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
	else{
		var json = createJsonData(); 
	    var  obj = JSON.parse(json);    
		var arrayTmp = [];   
		arrayTmp.push(obj);
// 		console.log(json)
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
	var json = '{"ProductionOrder":'+JSON.stringify(ProductionOrder)+ 
	   ',"CFMPlanLabDate":'+JSON.stringify(CFMPlanLabDate)+ 
	   ',"CFMPlanDate":'+JSON.stringify(CFMPlanDate)+  
	   ',"DeliveryDate":'+JSON.stringify(DeliveryDate)+  
	   ',"SaleOrder": '+JSON.stringify(SaleOrder)+ 
	   ',"SaleLine": '+JSON.stringify(SaleLine)+ 
	   ',"CaseSave": '+JSON.stringify(caseSave)+ 
	   '} ';     
// 	   console.log(json) 
	   return json; 
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
	var deliStatus = document.getElementById("SL_delivStatus").value .trim();  
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
//     	 "Division"            ,         
//     	 "SaleOrder"           ,    
//     	 "SaleLine"            , 
//     	 "CustomerShortName"   , 
//     	 "SaleCreateDate"      ,    
//     	 "PurchaseOrder" ,   
//     	 "MaterialNo" , 
//     	 "CustomerMaterial" ,  
//     	 "Price" , 
//     	 "SaleUnit" , 
//     	 "SaleQuantity" ,          
//     	 "RemainQuantity" ,         
//     	 "RemainAmount" , 
//     	 "TotalQuantity" ,        
//     	 "Grade" , 
//     	 "BillSendWeightQuantity" , 
//     	 "BillSendQuantity" ,        
//     	 "CustomerDue" , 
//     	 "DueDate" ,               
//     	 "LotNo" , 
//     	 "LabNo" ,  
//     	 "LabStatus" ,              
//     	 "CFMPlanLabDate" ,         
//     	 "CFMActualLabDate" , 
//     	 "CFMCusAnsLabDate" ,          
//     	 "UserStatus" ,                  
//     	 "TKCFM" ,                      
//     	 "CFMPlanDate" ,   
//     	 "CFMSendDate" ,             
//     	 "CFMLastest" ,         
//     	 "CFMNumber" ,          
//     	 "DeliveryDate" ,       
//     	 "ShipDate" ,              
//     	 "Remark"  ];   
    /* XLS Rows Data */
    let xlsHeader = Array.from( mapsTitleHeader.keys() );
    var xlsRows = data
    createXLSLFormatObj.push(xlsHeader);
    $.each(xlsRows, function(index, value) {
        var innerRowData = [];  
        $.each(value, function(ind, val) {     	
//             innerRowData.push(val); 
        	 if(mapsDataHeader.size != 0){                
 			  	if(mapsDataHeader.get(ind) !== undefined){   
//  			  		 console.log(ind,val) 
 			  		 innerRowData.push(val); 
 			  	}
             }
        }); 
//         innerRowData.push(value.Division);        
// innerRowData.push(value.SaleOrder);    
// innerRowData.push(value.SaleLine);
// innerRowData.push(value.CustomerShortName); 
// innerRowData.push(value.SaleCreateDate); 
// innerRowData.push(value.PurchaseOrder);    
// innerRowData.push(value.MaterialNo);  
// innerRowData.push(value.CustomerMaterial); 
// innerRowData.push(value.Price); 
// innerRowData.push(value.SaleUnit);  
// innerRowData.push(value.SaleQuantity);           
// innerRowData.push(value.RemainQuantity);          
// innerRowData.push(value.RemainAmount);  
// innerRowData.push(value.TotalQuantity);         
// innerRowData.push(value.Grade);  
// innerRowData.push(value.BillSendWeightQuantity);  
// innerRowData.push(value.BillSendQuantity);         
// innerRowData.push(value.CustomerDue);  
// innerRowData.push(value.DueDate);                
// innerRowData.push(value.LotNo);  
// innerRowData.push(value.LabNo);   
// innerRowData.push(value.LabStatus);               
// innerRowData.push(value.CFMPlanLabDate);          
// innerRowData.push(value.CFMActualLabDate);  
// innerRowData.push(value.CFMCusAnsLabDate);           
// innerRowData.push(value.UserStatus);                   
// innerRowData.push(value.TKCFM);                       
// innerRowData.push(value.CFMPlanDate);  
// innerRowData.push(value.CFMSendDate);          
// innerRowData.push(value.CFMLastest);          
// innerRowData.push(value.CFMNumber);           
// innerRowData.push(value.DeliveryDate);        
// innerRowData.push(value.ShipDate);               
// innerRowData.push(value.Remark);          
              
//         innerRowData.push(value);
        createXLSLFormatObj.push(innerRowData);
    }); 
    /* File Name */
    var filename = "PCMSDetail.xlsx"; 
    /* Sheet Name */
    var ws_name = "PCMSDetail"; 
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
function saveColSettingToServer(arrayTmp) {   
// 	console.log(arrayTmp)
	$.ajax({   
		type: "POST",  
		contentType: "application/json",  
		data: JSON.stringify(arrayTmp),      
		url: "PCMSDetail/saveColSettingToServer", 
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
function saveInputDateToServer(arrayTmp) {   
// 	console.log(arrayTmp)
	$.ajax({   
		type: "POST",  
		contentType: "application/json",  
		data: JSON.stringify(arrayTmp),      
		url: "PCMSDetail/saveInputDate", 
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
function searchByDetailToServer(arrayTmp) {   
// 	console.log(arrayTmp)
	$.ajax({
		type: "POST",  
		contentType: "application/json",  
		data: JSON.stringify(arrayTmp),      
		url: "PCMSDetail/searchByDetail", 
		success: function(data) { 
// 			console.log(data)
// 			get_visible_columns()
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
//     console.log(value)
 	if (value.trim() == '') { result = '';  } 
    // EX DD/MM/YYYY
 	else if(moment(value , 'DD/MM/YYYY',true).isValid()){   	 
 		dateInput = new Date(datearray[1] + '/' + datearray[0] + '/' + datearray[2]) ;   
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
//             console.log(value ,dateInput)   
//             console.log(!moment(value , 'DD/MM/YYYY',true).isValid() ,dateInput < date)
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
//     console.log(value)
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
		setColVisible(columnsHeader,colVisible);    
		$('#multi_colVis').selectpicker('val', colVisible);     
	}
	get_visible_columns()  
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
		 var resultData = colName[i]; 	   
		 var opt = document.createElement('option');
	     opt.appendChild(document.createTextNode(i));
		 opt.text  = (i+1)+" : "+resultData.title;
		 opt.value = resultData.data;  
		 sel.appendChild(opt);          
	}             
	$("#multi_colVis").selectpicker("refresh");
} 
function addUserStatusOption(data  ){   
	// The DOM way. 
	var sel = document.getElementById("SL_userStatus");
// 	for (i = sel.length - 1; i >= 0; i--) {
// 		sel.remove(i);
// 	}                   
// 	console.log(data)
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
function setColVisible(mainCol,colVisible) {    
// 	console.log(colVisible)   
// 	console.log(mainCol)  
	let index ;
	let check ;
	let colName = '';
// 	function sparseIndexOf(arr, value) {
// 	    return Object.keys(arr).find(function(k) {
// 	        return arr[k] === value;
// 	    })	
// 	}
   let tmpArrayShow = [];
   let tmpArrayHide = [];
   let i = 0;
//    console.log(mainCol)   
//    console.log(colVisible)
	for (i =  0; i < mainCol.length ; i++) {           
		colName = mainCol[i].data;    
// 		index = colVisible.findIndex(element => element === colName);   
// 		console.log(' i ' ,i ,' index ' ,index ,colName)  
// 		if(index < 0){ 
// 			MainTable.column( i ).visible(true  )   ;
// // 			tmpArrayShow.push(i)   
// // 			arrayTmp.push(obj);   
// 		}    
// 		else{ 
// 			MainTable.column( i ).visible( false ) 
// // 			tmpArrayHide.push(index)       
// 			}   
		check = colVisible.includes(colName);  
		if(check === true){ 
			MainTable.column( i ).visible(true  )   ;        
		}    
		else{      
			MainTable.column( i ).visible( false )  
			}   
		
		
// 		index = sparseIndexOf(colVisible, colName)
// 		if(index === undefined){      
// 			MainTable.column( i ).visible( false )     
// 			}  
// 		else{ MainTable.column( index ).visible( true )     }   
// 		console.log(index,i , mainCol[i].data)  
	}       
// 	console.log( 'Table\'s column visibility are set to: '+MainTable.columns().visible(true).join(', ') );
// 	console.log( 'Table\'s column non visibility are set to: '+MainTable.columns().visible().join(', ') );
// 	console.log(tmpArrayShow,tmpArrayHide)
// 	MainTable.column( tmpArrayShow ).visible( true ) ;
// 	MainTable.column( tmpArrayHide).visible( false ) ;
// 	for (i =  0; i < mainCol.length ; i++) {                   
// 		 MainTable.column( i ).visible( true )      
// 	}        
// 	for (i =  0; i < colVisible.length ; i++) {              
// // 		let index = colVisible.findIndex(element => element === mainCol[i] .data);    
// 		MainTable.column( index ).visible( false );    
// 	}   
	MainTable.columns.adjust().draw( false ); // adjust column sizing and redraw

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
function getInputDate(arrTmp,colIdx){      
	var path ="";
	if(colIdx == 23){
		path = "PCMSDetail/getCFMPlanLabDateDetail";
	}    
	else if(colIdx == 27){
		path = "PCMSDetail/getCFMPlanDateDetail";
	}  
	else if(colIdx == 32){
		path = "PCMSDetail/getDeliveryPlanDateDetail"; 
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
// 			console.log(all_columns[i].data, count)
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
</script> 
</html>