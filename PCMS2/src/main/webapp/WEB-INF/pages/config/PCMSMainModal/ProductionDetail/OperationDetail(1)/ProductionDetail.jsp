<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>       
 <div class="tab-pane fade" id="nav-ProductionDetail_1" style=" height: -webkit-fill-available;min-height: 100%;"  role="tabpanel" aria-labelledby="nav-ProductionDetail_1-tab">
	<div class="row" style="margin: 10px;" >          
<!-- 	LEFT -->  
		<div class="col-12 col-sm-12 col-md-12 col-lg-12 col-xl-12 " >     
			<div class="row"  >           
				<div class="col-12 	col-sm-12 col-md-12 col-lg-12 col-xl-12 presetWrapper " style="display: inline-flex;
				 			 text-align: right; ">  
<!-- 			  	 	<div class="table-responsive "> -->
<!-- 						<table id="presetTable" class="table table-bordered table-striped text-center" style="width:100%"> -->
<!-- 					        <thead>     -->
<!-- 					            <tr>  -->
<!-- 					                <th style="vertical-align: middle;">วันที่</th> -->
<!-- 					                <th style="vertical-align: middle;">WorkCenter</th>    -->
<!-- 					        	</tr>  -->
<!-- 					        </thead>      -->
<!-- 					        <tbody>  -->
<!-- 					        </tbody>  -->
<!-- 					    </table>   -->
<!-- 			    	</div>   -->  
					 <label class="my-1 mr-2 " for="input_presetDate">WorkDate</label>           
				  	<div   style="    margin-right: 10px;" > 
			    		<input class="form-control " autocomplete="off"  type="text" id="input_presetDate" readonly  > 
					</div> 
					<label class="my-1 mr-2 " for="input_presetWorkCenter">WorkCenter</label>           
				  	<div    style="    margin-right: 10px;" > 
			    		<input class="form-control " autocomplete="off"  type="text" id="input_presetWorkCenter" readonly  > 
					</div>          
				</div>        
			  	 <div class="col-12 col-sm-12 col-md-12 col-lg-12 col-xl-12 dyeingWrapper " style="display: inline-flex;
				 			 text-align: right;">  
			  	 	<div class="table-responsive ">    
						<table id="dyeingTable" class="table table-bordered table-striped display compact  text-center" style="width:100%">
					        <thead>
					            <tr>       
					                <th style="vertical-align: middle;">WorkDate</th>
					                <th style="vertical-align: middle;">Operation</th>
					                <th style="vertical-align: middle;">WorkCenter</th>
					                <th style="vertical-align: middle;">CartNo</th> 
					                <th style="vertical-align: middle;">CartType</th> 
					                
					                <th style="vertical-align: middle;">Dye Status</th> 
				                  	<th style="vertical-align: middle;">Delta-E</th>
				                 	<th style="vertical-align: middle;">Da. </th>  
				                 	<th style="vertical-align: middle;">Db. </th>  
				                 	<th style="vertical-align: middle;">ST. </th>  
				                 	
					                <th style="vertical-align: middle;">L. </th>  
				                 	<th style="vertical-align: middle;">Dye Remark	 </th>   
				                 	<th style="vertical-align: middle;">Color Status </th>  
				                 	<th style="vertical-align: middle;">Color Remark</th>   
					        	</tr>       
					        </thead>     
					        <tbody> 
					        </tbody> 
					    </table>  
			    	</div>  
				</div>        
				<div class="col-12 	col-sm-12 col-md-12 col-lg-12 col-xl-12 finishingWrapper " style="display: inline-flex;
				 			 text-align: right;">  
			  	 	<div class="table-responsive ">
						<table id="fnTable" class="table display compact table-bordered table-striped text-center" style="width:100%">
					        <thead>  
				        		<tr>        
									<th colspan="6" style="vertical-align: middle;">Finishing (190-193) </th> 
<!-- 									<th colspan="5" style="vertical-align: middle;">Finishing<span class="c" style="display: block;">(190-193)</span></th>  -->
									<th colspan="11" style="vertical-align: middle;">ColorCheck (195-198) </th>  
								</tr>           
					            <tr> 
<!-- 					                <th style="vertical-align: middle; ">LotNo</th> -->
					                <th style="vertical-align: middle; ">WorkDate</th>
					                <th style="vertical-align: middle; ">Operation</th>
					                <th style="vertical-align: middle; ">WorkCenter</th>
					                <th style="vertical-align: middle; ">CartNo</th>
					                <th style="vertical-align: middle; ">CartType</th> 
				                  	<th style="vertical-align: middle; ">Operation</th>
				                  	<th style="vertical-align: middle; ">WorkDate</th>
				                 	<th style="vertical-align: middle; ">Da. </th>  
				                 	<th style="vertical-align: middle; ">Db. </th>  
					                <th style="vertical-align: middle; ">L. </th>  
				                 	<th style="vertical-align: middle; ">ST. </th>  
			                  	 	<th style="vertical-align: middle; ">Delta-E</th>  
			                  	 	<th style="vertical-align: middle; ">Name</th>  
				                 	<th style="vertical-align: middle; ">Status</th>      
				                 	<th style="vertical-align: middle; ">RollNo</th>  
				                 	<th style="vertical-align: middle; ">Remark </th>   
					        	</tr>   
					        </thead>     
					        <tbody> 
					        </tbody> 
					    </table>  
			    	</div>  
				</div>        
			</div> 
		</div> 
  
	</div>
</div>