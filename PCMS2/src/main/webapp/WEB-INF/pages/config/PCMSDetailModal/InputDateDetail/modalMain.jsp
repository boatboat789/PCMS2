<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!-- Modal -->    
<div class="modal fade  multipleTabNav" id="modalInputDateForm"   
	style="overflow: auto ;  zoom: 95%;">    
	<div class="modal-dialog modal-dialog-centered" role="document" 
	 style ="width: 99%;  min-width: 50%;   "    >       
  		             
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
				<jsp:include page="/WEB-INF/pages/config/PCMSDetailModal/InputDateDetail/modalContent.jsp"></jsp:include> 
				 
			</div>   
		</div>
	</div>
</div>  