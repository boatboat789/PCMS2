<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<div class="modal fade" id="modalColSetting" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
<!-- 	<div class="modal-dialog" role="document"> -->
		<div class="modal-dialog modal-dialog-centered" role="document"   
			 style ="width: 99%;  "    >              
		<div class="modal-content">
			<div class="modal-header text-center" style="background-color: beige;">
				<h4 class="modal-title w-100 font-weight-bold">Show / Hide Column</h4>
				<button id="modalCloseButton" type="button" class="close"
					data-dismiss="modal" aria-label="Close">     
					<span aria-hidden="true">&times;</span>
				</button>
			</div>      
			<div class="modal-body mx-12" style="display: inline-flex;">
<!-- 					 <select id='select-MainColumn' name="mainSelect[]" multiple='multiple'> -->
<!-- 					    <option value='ProductionOrder' >Prod.Order</option> -->
<!-- 					    <option value='MaterialNumber'>Material</option> -->
<!-- 					    <option value='CustomerMat'>Cus.Material</option> -->
<!-- 					    <option value='WorkCenter'>WC.</option> -->
<!-- 					    <option value='DueDate''>DueDate</option> -->
<!-- 				     	<option value='CustomerDue''>CusDue</option> -->  
<!-- 				      	<option value='GreigeDue''>GreigeDue</option> -->
<!-- 					    <option value='CustomerName'	>Cus.Name</option> -->
<!-- 					    <option value='QuantityKG'>Qty(KG.)</option> -->
<!-- 					    <option value='QuantityMR'>Qty(M.)</option> -->
<!-- 					    <option value='Priority'>UserStatus</option> -->
<!-- 					    <option value='LabStatus'>LabStatus</option> -->
<!-- 					    <option value='DyeDate'>DyeDate</option> -->   
<!-- 					    <option value='GroupPlanningDetailId'>Group</option> -->
<!-- 					    <option value='Reason'>Reason</option> -->  
<!-- 					  </select> -->         
				<label class=" margin-0" for="multi_colVis">Select column :</label>  	
				<div >   
					<select id="multi_colVis"   class="selectpicker" 
					multiple 
					data-selected-text-format="count > 3"   
					data-live-search="true"  
				 	data-actions-box="true">
<!-- 				  <option>Mustard</option> -->       
<!-- 				  <option>Ketchup</option> -->   	
<!-- 				  <option>Relish</option> -->
					</select>
				</div>         
				


			</div>
			<div class="modal-footer d-flex justify-content-center">
				<button id="save_col_button" class="btn btn-default">Save</button>
			</div>
		</div>
	</div>
</div>