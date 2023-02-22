<%-- <%@ page language="java" contentType="text/html; charset=UTF-8" --%>
<%--     pageEncoding="UTF-8"%> --%>
 <%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> 
<%@ page isELIgnored="false"%>              	               
<iframe id="remember" name= "fakeSubmit" style="    display: none;" ></iframe>      
<form target="fakeSubmit" method="post" action="Main/fakeSubmit" commandName="PCMSTable"  >    
	<div id="wrapper-top" class="row" style="  margin: 2px 5px; background-color: azure;     zoom: 85%; " >   
		<div class="col-12 col-sm-12 col-md-12 col-lg-10 col-xl-10 "  >            
			<div class="row">                   
				<div class="col-12 col-sm-6 col-md-4 col-lg-3 col-xl-3 form-group lab-print" >  
						<label class=" label-input  margin-0" style=" min-width: 100px; " for="multi_cusName">Customer</label>                
						<div class=" ">     		
							<select id="multi_cusName"   class="selectpicker" 
							multiple 
							data-width="100%"    
							data-selected-text-format="count > 1"     
							data-live-search="true"  
						 	data-actions-box="true">   
							</select>   
						</div>         
				</div>   
				<div class="col-12 col-sm-6 col-md-4 col-lg-3 col-xl-3 form-group lab-print" >       
				  	<label class=" label-input  margin-0" style="    min-width: 100px; " for="multi_cusShortName">Cust.(Name4)</label>            
					<div >        
							<select id="multi_cusShortName"     class="selectpicker" 
							multiple 
							data-width="100%"       
							data-selected-text-format="count > 1"   
							data-live-search="true"  
						 	data-actions-box="true"> 
							</select> 
						</div>            
				</div>             
				<div class="col-12 col-sm-6 col-md-4 col-lg-3 col-xl-3 form-group lab-print" >       
				  	<label class=" label-input  margin-0" for="input_saleOrder">SO No.</label>           
				  	<div >   
			    		<input class="form-control "  name="SaleOrder" type="text"  id="input_saleOrder"   maxlength="30" > 
					</div>     
				</div>         
				<div class="col-12 col-sm-6 col-md-4 col-lg-3 col-xl-3 form-group lab-print" >              
				  	<label class=" label-input  margin-0" for="input_article">Article No</label>           
				  	<div > 
			    		<input class="form-control "   name="article"  type="text"  id="input_article"   maxlength="30" > 
					</div>     
				</div>                      
				<div class="col-12 col-sm-6 col-md-4 col-lg-3 col-xl-3 form-group lab-print" >              
				  	<label class=" label-input  margin-0" for="input_prdOrder">Prod.No</label>           
				  	<div > 
			    		<input class="form-control "  name="prdOrder"   type="text"  id="input_prdOrder"   maxlength="30" > 
					</div>     
				</div>     
				<div class="col-12 col-sm-6 col-md-4 col-lg-3 col-xl-3 form-group lab-print" >              
				  	<label class=" label-input  margin-0" for="SL_saleNumber">SaleName</label>           
				  	<div >  
						<select id="SL_saleNumber" class="form-control "   >      
							<option value="" selected>Select</option>   
						</select>
					</div>  
				</div>                     
				<div class="col-12 col-sm-6 col-md-4 col-lg-3 col-xl-3 form-group lab-print" >              
				  	<label class=" label-input  margin-0" for="input_saleOrderDate">SO. Date</label>           
				  	<div > 
			  			<input  id="input_saleOrderDate" autocomplete="off" type="text" name="daterange"   class='form-control'
							style=' display: inline;margin-top: 0px;' />  
					</div>     
				</div>                    
				<div class="col-12 col-sm-6 col-md-4 col-lg-3 col-xl-3 form-group lab-print" >              
				  	<label class=" label-input  margin-0" for="input_designNo">Design No</label>           
				  	<div > 
			    		<input class="form-control "   type="text"  name="designNo"  id="input_designNo"   maxlength="30" > 
					</div>     
				</div>      
				<div class="col-12 col-sm-6 col-md-4 col-lg-3 col-xl-3 form-group lab-print" >              
				  	<label class=" label-input  margin-0" for="input_prdOrderDate">Prod.Date</label>           
				  	<div > 
				  	<input  id="input_prdOrderDate" type="text" autocomplete="off"  name="daterange"   class='form-control'
							style=' display: inline;margin-top: 0px;' />        
					</div>     
				</div>   
				<div class="col-12 col-sm-6 col-md-4 col-lg-3 col-xl-3 form-group lab-print" >              
				  	<label class=" label-input  margin-0" for="input_material">Mat No.</label>           
				  	<div > 
			    		<input class="form-control "   type="text"  name="material"  id="input_material"   maxlength="30" > 
					</div>     
				</div>      
				<div class="col-12 col-sm-6 col-md-4 col-lg-3 col-xl-3 form-group lab-print" >              
				  	<label class=" label-input  margin-0" for="input_labNo">LabNo</label>           
				  	<div > 
			    		<input class="form-control "   type="text"  name="labNo"  id="input_labNo"   maxlength="30" > 
					</div>     
				</div>   
				<div class="col-12 col-sm-6 col-md-4 col-lg-3 col-xl-3 form-group lab-print" >        
					<label class=" label-input  margin-0"  style="    min-width: 100px; " for="multi_userStatus">UserStatus</label>             
			  		<div >   
						<select id="multi_userStatus"   class="selectpicker" 
						multiple 
						data-selected-text-format="count > 1"     
						data-live-search="true"  
					 	data-actions-box="true">     
						</select>
					</div>      
				</div>   
				<div class="col-12 col-sm-6 col-md-4 col-lg-3 col-xl-3 form-group lab-print" >              
				  	<label class=" label-input  margin-0" for="input_dueDate">Due Date</label>           
				  	<div>    	
					  	<input  id="input_dueDate" type="text" autocomplete="off"  name="daterange"   class='form-control'
								style=' display: inline;margin-top: 0px;' />        
					</div>        
				</div>    
				<div class="col-12 col-sm-6 col-md-4 col-lg-3 col-xl-3 form-group lab-print" >              
				  	<label class=" label-input  margin-0" for="SLDelivStatus">Delivery Status</label>           
				  	<div >         
			    		<select id="SL_delivStatus" class="form-control "   >      
						<option value="" selected>Select</option>  
						<option value="C" >Completed</option>  
						<option value="B" >Partial Process</option>  
						<option value="A" >NotYet Process</option>  
					</select>
					</div>      
				</div>  
				<div class="col-12 col-sm-6 col-md-4 col-lg-3 col-xl-3 form-group lab-print" >        
					<label class=" label-input  margin-0"  style="    min-width: 100px; " for="multi_division">Division</label>             
			  		<div >      
						<select id="multi_division"   class="selectpicker" 
						multiple 
						data-selected-text-format="count > 1"     
						data-live-search="true"  
					 	data-actions-box="true">     
						</select>
					</div>      
				</div>    
			</div>       
		</div>               
		<!--  ROW 2 -->    
		<div class="col-12 col-sm-12 col-md-12 col-lg-2 col-xl-2  "  >      
			<div class="row">                           
				<div class="col-12  col-sm-4 col-md-4 col-lg-12 col-xl-12 " style="  display: inline-flex;  text-align: -webkit-right;    margin-bottom: 5px;" >  
	              	<div class="btn-group d-flex" style="    width: inherit; ">         
	<!-- w-100 -->         
		              	<button id="btn_search" class="btn btn-primary w-100"  type="button" 
		              		style=" width: -webkit-fill-available;
				              		white-space: nowrap;
								    overflow: hidden;              
								    text-overflow: ellipsis; 
							        padding: 1% 0;" > 
		                    <i class="fa fa-search"></i>   
		                    Search    
		              	</button>    
	              		<button id="btn_clear" class="btn btn-secondary w-100" type="button" 
		              		style="     padding: 1% 0;width: -webkit-fill-available;  white-space: nowrap; overflow: hidden; text-overflow: ellipsis;" >   
	                    <i class="fa fa-eraser"></i> 
	                    Clear Search
	              		</button>          
	              	</div>    
				</div>         
				<div class="col-12  col-sm-4 col-md-4 col-lg-12 col-xl-12  " style="    text-align: -webkit-right;    margin-bottom: 5px;" >  
	              	<button id="btn_download" class="btn btn-primary" type="button" 
	              		style="padding: 1% 0; width: -webkit-fill-available; " >
	                    <i class="fa fa-download"></i>     
	                    Download Excel     
	              	</button>      
				</div>               
				<div class="col-12  col-sm-4 col-md-4 col-lg-12 col-xl-12  " style="    text-align: -webkit-right;    margin-bottom: 5px;" >  
	              	<button id="btn_colSetting" class="btn btn-primary" type="button" 
	              			style=" padding: 1% 0;width: -webkit-fill-available; " >
	                    <i class="fa fa-cogs"></i> 
	                    Column Setting  
	              	</button>        
				</div>    
				<div class="col-12  col-sm-4 col-md-4 col-lg-12 col-xl-12  " style="    text-align: -webkit-right;    margin-bottom: 5px;" >  
	              	<div class="btn-group d-flex" style="    width: inherit; ">
		              	<button id="btn_saveDefault" class="btn btn-success w-100" type="button" 
		              		style=" padding: 1% 0;width: -webkit-fill-available; " >
		                    <i class="fa fa-save"></i> 
		                    Save Default   
		              	</button>  
		              	<button id="btn_loadDefault" class="btn btn-info  w-100" type="button" style="padding: 1% 0; width: -webkit-fill-available; " >
		                    <i class="fa fa-wrench"></i> 
		                    Load Default 
	              		</button>       
	              	</div>   
				</div>      
	<!-- 			<div class="col-12  col-sm-4 col-md-4 col-lg-4 col-xl-4  " style="    text-align: -webkit-right;    margin-bottom: 5px;" >   -->
	<!--               	<button id="btn_loadDefault" class="btn btn-primary" type="button" style="padding: 1% 0; width: -webkit-fill-available; " > -->
	<!--                     <i class="fa fa-wrench"></i>  -->
	<!--                     Load    -->
	<!--               	</button>       -->
	<!-- 			</div>        -->
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
				<div class="col-12 col-sm-12 col-md-4 col-lg-6 col-xl-6  form-group lab-print" id="div_toOtherPath" style="justify-content: right;" >   
					<div class="row">       
						<div class="col-12  col-sm-12 col-md-12 col-lg-12 col-xl-12  "  >     
							<c:if test="${userObject.isCustomer  == false }"> 
				              	<button id="btn_prdDetail" class="btn btn-primary" type="button"  >
				                    Production Detail 
				              	</button>     
			              	</c:if>   
							<c:if test="${userObject.isCustomer == false }"> 
			              	<button id="btn_lbms" class="btn btn-primary" type="button"  >
			                   LBMS Detail            
			              	</button>         
			              	</c:if>   
			              	<button id="btn_qcms" class="btn btn-primary" type="button"  >
			                    QCMS Result
			              	</button>   
			              	<button id="btn_inspect" class="btn btn-primary" type="button"  >
			                    Inspect Result     
			              	</button>       
							<c:if test="${userObject.isCustomer  == false }">  
				              	<button id="btn_sfc" class="btn btn-primary" type="button"  >
				                    SFC Detail
				              	</button>          
			              	</c:if>   
			              	<button id="btn_lockColumn" class="btn btn-primary" type="button"  >
			                    Lock Column
			              	</button>      
						</div>       
					</div>    
				</div>      
			</div>    
		</div>                  
	</div>   
	<button id="submit_button"type="submit" style="    display: none;"></button>
</form>