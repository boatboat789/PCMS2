<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!-- Modal -->    
<div class="modal fade  multipleTabNav" id="modalForm"   
	style="overflow: auto ;  zoom: 95%;">    
	<div class="modal-dialog modal-dialog-centered" role="document" 
			style ="width: 99%;             
		    max-width: 99%;   
		    min-width: 99%;       
  		        margin: 5px             
		     ">     
		    <!--  		    margin: 1.75rem auto;" -->    
		<div class="modal-content">      
			<div class="modal-header " style="    text-align: end;padding: 5px;background-color: beige;">
			<h4  class="modal-title font-weight-bold" style="      background-color: lawngreen;  flex: none;"  id="input_OperationNow" > </h4> 
				<h4 class="modal-title w-100 font-weight-bold">
					<span id="header_SFC">
					</span> 
					</h4>  
				<button id="modalCloseButton" type="button" class="close"
					data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>                        	   
			</div>    
			<div class="modal-body mx-12" style="    padding: 10px; background-color: gainsboro;">    
				<jsp:include page="/WEB-INF/pages/config/PCMSMainModal/modalContent.jsp"></jsp:include> 
				<div class="row">
				  <div class="col-sm-12 col-md-12 col-lg-12 col-xl-12"> 
					<nav>    	     
					  <div class="nav nav-tabs" id="nav-tab" role="tablist"> 
					    <a class="nav-item nav-link active" style="padding:5px;background-color: ghostwhite; font-weight: bold;    "  id="nav-POGreigeDetaill-tab" data-toggle="tab" href="#nav-POGreigeDetail" role="tab" aria-controls="nav-POGreigeDetail" aria-selected="true">POGreigeDetail</a>
					    <a class="nav-item nav-link" style="padding:5px;background-color: ghostwhite; font-weight: bold; "  id="nav-ProductionDetail_1-tab" data-toggle="tab" href="#nav-ProductionDetail_1" role="tab" aria-controls="nav-ProductionDetail_1" aria-selected="true">
<!-- 					    ProductionDetail(1) -->
<!-- 								50/(100-103)/190-193    -->
								Preset/Dyeing/Finishing
						    </a>
					    <a class="nav-item nav-link" style="padding:5px;background-color: ghostwhite; font-weight: bold; "  id="nav-ProductionDetail_2-tab" data-toggle="tab" href="#nav-ProductionDetail_2" role="tab" aria-controls="nav-ProductionDetail_2" aria-selected="true">
<!-- 					    ProductionDetail(2) -->
<!-- 								(60/145/180/199/200)/220 -->
							Inspect/Packing/รอส่งผลเทส QC     
					    </a>        
					    <a class="nav-item nav-link" style="padding:5px;background-color: ghostwhite; font-weight: bold; "  id="nav-ResultDetail_1-tab"  data-toggle="tab" href="#nav-ResultDetail_1" role="tab" aria-controls="nav-ResultDetail_1" aria-selected="true">ResultDetail(1)</a>   
					    <a class="nav-item nav-link" style="padding:5px;background-color: ghostwhite; font-weight: bold; "  id="nav-ResultDetail_2-tab"  data-toggle="tab" href="#nav-ResultDetail_2" role="tab" aria-controls="nav-ResultDetail_2" aria-selected="true">ResultDetail(2)</a>   
<!-- 					    <a class="nav-item nav-link" style="padding:5px; "  id="nav-ResultDetail-tab"  data-toggle="tab" href="#nav-ResultDetail" role="tab" aria-controls="nav-ResultDetail" aria-selected="true">ResultDetail</a>    -->
					  </div>           
					</nav>       
					<div class="tab-content " id="nav-tabContent"        
						style="     
/* 						display: table;  */  
/*  		 							height: 490px;             */
/* height: 70vh;    */
    height: calc(100vh - 160px);
								border: 1px solid #212529;      
								padding : 5px;   
								background-color: honeydew;     
								-webkit-box-shadow: inset 2px 2px 2px rgba(255, 255, 255, .4), inset -2px -2px 2px rgba(0, 0, 0, .4);
								box-shadow: inset 2px 2px 2px rgba(255, 255, 255, .4), inset w-2px -2px 2px rgba(0, 0, 0, .4);	"> 
							<jsp:include page="/WEB-INF/pages/config/PCMSMainModal/POGreigeDetail/POGreigeDetail.jsp"></jsp:include>  
							<jsp:include page="/WEB-INF/pages/config/PCMSMainModal/ProductionDetail/OperationDetail(1)/ProductionDetail.jsp"></jsp:include> 
							<jsp:include page="/WEB-INF/pages/config/PCMSMainModal/ProductionDetail/OperationDetail(2)/ProductionDetail.jsp"></jsp:include> 
							<jsp:include page="/WEB-INF/pages/config/PCMSMainModal/ResultDetail/ResultDetail(1).jsp"></jsp:include>  
							<jsp:include page="/WEB-INF/pages/config/PCMSMainModal/ResultDetail/ResultDetail(2).jsp"></jsp:include>  
					  	</div>         
					</div>
				</div>    
			</div>   
		</div>
	</div>
</div>  