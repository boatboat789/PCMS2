<%@ page language="java" contentType="text/html; charset=UTF-8"    
    pageEncoding="UTF-8"%>           
 <div class="tab-pane fade show active" id="nav-POGreigeDetail" style=" height: -webkit-fill-available;min-height: 100%;" role="tabpanel" aria-labelledby="nav-POGreigeDetail-tab">
	<div class="row" style="margin: 10px;" >          
		<div class="col-12 col-sm-12 col-md-12 col-lg-12 col-xl-12 " >
			<div class="row"  >    
				<div class="col-12 	col-sm-12 col-md-12 col-lg-12 col-xl-12  " style="display: inline-flex;
				 padding: 0px; text-align: right;font-size: 12.5px;margin: 0px 0px 10px 0px;"> 
					 <label class="my-1 mr-2 " style="white-space: nowrap;" for="input_poDefault">PO Default</label>           
				  	<div   style="    margin-right: 10px;" > 
			    		<input class="form-control " autocomplete="off"  type="text" id="input_poDefault" readonly  > 
					</div> 
					<label class="my-1 mr-2 " style="white-space: nowrap;"  for="input_poLineDefault">Line</label>           
				  	<div    style="    margin-right: 10px;" > 
			    		<input class="form-control " autocomplete="off"  type="text" id="input_poLineDefault" readonly  > 
					</div>   
					<label class="my-1 mr-2 "  style="white-space: nowrap;" for="input_poCreateDateDefault">วันที่ออก PO</label>           
				  	<div    style="    margin-right: 10px;" > 
			    		<input class="form-control " autocomplete="off"  type="text" id="input_poCreateDateDefault"  readonly  > 
					</div> 
					<label class="my-1 mr-2 " style="white-space: nowrap;"  for="input_greigeDesign">Greige Design</label>           
				  	<div   style="    margin-right: 10px;" > 
			    		<input class="form-control " autocomplete="off"  type="text" id="input_greigeDesign" readonly  > 
					</div> 
					<label class="my-1 mr-2 " style="white-space: nowrap;" for="input_greigeArticle">Greige Article</label>           
				  	<div    style="    margin-right: 10px;" > 
			    		<input class="form-control " autocomplete="off"  type="text" id="input_greigeArticle" readonly  > 
					</div>      
				</div>        	
<!-- 			<div class="col-12 	col-sm-12 col-md-6 col-lg-6 col-xl-6  " style=" padding: 0px; text-align: right;font-size: 12.5px;">  -->
<!-- 				 <button id="btn_createBookNo" type="button" class="btn btn-success " value="Create" style=" float: right;" >CreateBookNo</button>    -->
<!-- 		 		 <button id="btn_cnfSale" type="button" class="btn btn-success " value="Save" style=" float: right;" >Save</button>    -->
<!-- 			</div>     -->     
				<div class="col-12 	col-sm-12 col-md-12 col-lg-8 col-xl-8 poDefaultWrapper " > 
<!-- 					<div class="poDefaultWrapper">  -->      
					  	<div class="table-responsive ">   
							<table id="poTable" class="table display compact table-bordered table-striped text-center" style="width:100%">
						        <thead>
						            <tr> 
						                <th style="vertical-align: middle;">PO No.</th>
						                <th style="vertical-align: middle;">PO Line</th>
						                <th style="vertical-align: middle;">วันที่ออก PO</th>
						                <th style="vertical-align: middle;">วันที่เข้า / ออก</th>
						                <th style="vertical-align: middle;">Batch</th> 
					                  	<th style="vertical-align: middle;">น้ำหนัก (kg)</th>
						                <th style="vertical-align: middle;">ความยาว(M)</th>  
						                
						        	</tr>    
						        </thead>        
						        <tbody> 
						        </tbody>     
						    </table>  
					    </div>  
<!-- 				    </div>   -->
			    </div>       
			</div> 
		</div> 
	</div>
</div>