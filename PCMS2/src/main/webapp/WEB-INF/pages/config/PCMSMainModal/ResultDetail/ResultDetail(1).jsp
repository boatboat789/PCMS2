<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>       
 <div class="tab-pane fade" id="nav-ResultDetail_1" style=" height: -webkit-fill-available;min-height: 100%;"  role="tabpanel" aria-labelledby="nav-ResultDetail_1-tab">
	<div class="row" style="margin: 10px;" >          
<!-- 	LEFT -->        
		<div class="col-12 col-sm-12 col-md-12 col-lg-12 col-xl-12 " >
			<div class="row"  >     
				<div class="col-12 	col-sm-12 col-md-12 col-lg-9 col-xl-9 workInLabWrapper " style="display: inline-flex; 
				 			 text-align: right;"> 
<!-- 				 	<label class="my-1 mr-2 " for="workInLabTable">งานเข้า Lab</label>            -->
			  	 	<div class="table-responsive ">      
						<table id="workInLabTable" class="table table-bordered table-striped text-center display compact" style="width:100%">
					        <thead>    
					            <tr> 
					                <td style="vertical-align: middle;">LabNo</td>
					                <td style="vertical-align: middle;">Remark</td>   
					                <td style="vertical-align: middle;">ส่งจาก</td>   
					                <td style="vertical-align: middle;">วันที่ส่งเข้า Lab</td>   
					                <td style="vertical-align: middle;">วันที่ต้องการ Lab</td>  
				                 	<td style="vertical-align: middle;">ครั้งที่ส่งงานเข้า Lab </td>  
				                 	<td style="vertical-align: middle;">ครั้งที่ Start Lab </td>  
				                 	<td style="vertical-align: middle;">วันที Start Lab </td>  
				                 	<td style="vertical-align: middle;">วันที่ Stop Lab</td>   
					        	</tr> 
					        </thead>     
					        <tbody> 
					        </tbody> 
					    </table>  
			    	</div>  
				</div>               
				
				 <div class="col-12 	col-sm-12 col-md-12 col-lg-4 col-xl-4 submitDateWrapper " style="display: inline-flex;
				 			 text-align: right;"> 
<!-- 				 	<label class="my-1 mr-2 " for="submitDateTable">วันนัดส่ง CFM</label>            -->
			  	 	<div class="table-responsive ">
						<table id="submitDateTable" class="table table-bordered table-striped text-center display compact" style="width:100%">
					        <thead>
					            <tr> 
					                <td style="vertical-align: middle;">ครั้งที่</td>
					                <td style="vertical-align: middle;">วันที่</td>
					                <td style="vertical-align: middle;">หมายเหตุ</td> 
					        	</tr> 
					        </thead>     
					        <tbody> 
					        </tbody> 
					    </table>  
			    	</div>  
				</div>   
<!-- 			  	 <div class="col-12 col-sm-12 col-md-6 col-lg-6 col-xl-6 waitTestWrapper " style="display: inline-flex; -->
<!-- 				 			 text-align: right;">  --> 
<!-- 			  	 	<div class="table-responsive "> -->
<!-- 						<table id="waitTestTable" class="table table-bordered table-striped text-center display compact" style="width:100%"> -->
<!-- 					        <thead> -->
<!-- 					            <tr>  -->
<!-- 					                <td style="vertical-align: middle;">ครั้งที่</td> -->
<!-- 					                <td style="vertical-align: middle;">วันที่เข้า Test</td> -->
<!-- 					                <td style="vertical-align: middle;">วันที่ออกผล Test</td> -->
<!-- 					                <td style="vertical-align: middle;">Status</td> -->
<!-- 					                <td style="vertical-align: middle;">Remark</td>     -->
<!-- 					        	</tr>  -->
<!-- 					        </thead>      -->
<!-- 					        <tbody>  -->
<!-- 					        </tbody>  -->
<!-- 					    </table>   -->
<!-- 			    	</div>     -->
<!-- 				</div>         -->  
				 <div class="col-12 col-sm-12 col-md-12 col-lg-3 col-xl-3 submitDateWrapper " style="display: inline-flex;
				 			 text-align: right;"> 
<!-- 				 	<label class="my-1 mr-2 " for="submitDateTable">วันนัดส่ง CFM</label>            -->
			  	 	<div class="table-responsive ">
						<table id="submitDateTable" class="table table-bordered table-striped text-center display compact" style="width:100%">
					        <thead>
					            <tr> 
					                <td style="vertical-align: middle;">ครั้งที่</td>
					                <td style="vertical-align: middle;">วันที่</td>
					                <td style="vertical-align: middle;">หมายเหตุ</td> 
					        	</tr> 
					        </thead>     
					        <tbody> 
					        </tbody> 
					    </table>  
			    	</div>  
				</div> 
				<div class="col-12 	col-sm-12 col-md-12 col-lg-12 col-xl-12 cfmWrapper " style="display: inline-flex;
				 			 text-align: right;"> 
<!-- 				 	<label class="my-1 mr-2 " for="cfmTable">CFM</label>            -- 
			  	 	<div class="table-responsive ">
						<table id="cfmTable" class="table table-bordered table-striped text-center display compact" style="width:100%">
					        <thead>
					            <tr> 
					                <td style="vertical-align: middle;">No</td>
					                <td style="vertical-align: middle;">CFM <span class="c"style="display: block;">Number</span> </td>
					                <td style="vertical-align: middle;">วันที่ส่ง</td>
					                <td style="vertical-align: middle;">เบอร์ม้วน</td>   
					                <td style="vertical-align: middle;">หมายเหตุ</td> 
					                <td style="vertical-align: middle;">L. </td>  
				                 	<td style="vertical-align: middle;">Da. </td>  
				                 	<td style="vertical-align: middle;">Db. </td>  
				                 	<td style="vertical-align: middle;">ST. </td>   
				                 	<td style="vertical-align: middle;">DE. </td>   
				                 	<td style="vertical-align: middle;">SO</td>  
				                 	<td style="vertical-align: middle;">SO  <span class="c"style="display: block;">Line</span> </td>  
				                 	<td style="vertical-align: middle;">วันที่ตอบ</td>  
				                 	<td style="vertical-align: middle;">สถานะ</td>
				                 	<td style="vertical-align: middle;">หมายเหตุ</td>    
				                 	<td style="vertical-align: middle;">Next <span class="c"style="display: block;">Lot</span></td>    
				                 	<td style="vertical-align: middle;">Order <span class="c"style="display: block;">Change</span></td>     
				                 	<td style="vertical-align: middle;">จำนวน</td>     
				                 	<td style="vertical-align: middle;">Unit</td>        
					        	</tr> 
					        </thead>     
					        <tbody> 
					        </tbody> 
					    </table>      
			    	</div>      
				</div>         
			</div> 
		</div> 
		
<!-- 		RIGHT -->
		 
<!-- 		<div class="col-12 col-sm-12 col-md-6 col-lg-6 col-xl-6 " > -->
<!-- 			<div class="row"  >     -->
   
			  	 
               
<!-- 			</div>  -->
<!-- 		</div>  -->
	</div>
</div>