<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<div class="modal fade" id="modalLockColumnDetail" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
<!-- 	<div class="modal-dialog" role="document"> -->
		<div class="modal-dialog modal-dialog-centered" role="document"   
			 style ="width: 99%;  "    >              
		<div class="modal-content">
			<div class="modal-header text-center" style="background-color: beige;">
				<h4 class="modal-title w-100 font-weight-bold">Lock Column</h4>
				<button id="modalCloseButton" type="button" class="close"
					data-dismiss="modal" aria-label="Close">     
					<span aria-hidden="true">&times;</span>
				</button>
			</div>         
			<div class="modal-body mx-12" style="display: inline-flex;"> 
				<label class=" margin-0" for="multi_colVis">Select column :</label>  	
				<div >     
					<select id="multi_lockCol"   class="selectpicker"     
					data-live-search="true"  
				 	data-actions-box="true">   
				 	<option value="">---Select---</option>
					</select>   
				</div>          
			</div>
			<div class="modal-footer d-flex justify-content-center">
				<button id="btn_setLockCol" class="btn btn-default btn-secondary">Set Lock Column</button>
			</div>
		</div>
	</div>
</div>