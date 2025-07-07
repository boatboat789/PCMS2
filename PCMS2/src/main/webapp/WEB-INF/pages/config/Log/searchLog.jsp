<%--     pageEncoding="UTF-8"%> --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>
<iframe id="remember" name="fakeSubmit" style="display: none;"></iframe>
<form target="fakeSubmit" method="post" action="Main/fakeSubmit">  
	<div id="wrapper-top" class="row" style="margin: 2px 5px; background-color: azure; padding: 10px 10px 10px 10px;">
		<div class="col-12 col-sm-12 col-md-12 col-lg-12 col-xl-12 ">
			<h2 style="text-align-last: center;">
				<span id="id_pageType" class="c"></span>
			</h2>
		</div>
		<div class="col-12 col-sm-12 col-md-12 col-lg-10 col-xl-10 ">
			<div class="row">    
				<div class="col-12 col-sm-6 col-md-4 col-lg-3 col-xl-3 form-group lab-print" id="div_input_prodOrder"style="display : none">
					<label class=" label-input  margin-0" for="input_prodOrder">Prod.Order</label>
					<div>    	         
						<input class="form-control " name="ProductionOrder" type="text" id="input_prodOrder" maxlength="30"  class='form-control' >
					</div>
				</div>
				<div class="col-12 col-sm-6 col-md-4 col-lg-3 col-xl-3 form-group lab-print" id="div_input_sale" style="display : none">
					<label class=" label-input  margin-0" for="input_saleOrder">SaleOrder</label>
					<div> 
						<input class="form-control " name="SaleOrder" type="text" id="input_saleOrder" maxlength="30"  class='form-control' > 
					</div>
				</div>
				<div class="col-12 col-sm-6 col-md-4 col-lg-3 col-xl-3 form-group lab-print" id="div_input_createDate" style="display : inline-flex">
					<label class=" label-input  margin-0" for="input_createDate" id="label_createDate">Create Date</label>
					<div>
						<input id="input_createDate" autocomplete="off" type="text" name="daterange" class='form-control' >
					</div>
				</div>
			</div>
		</div>
		<!--  ROW 2 -->
		<div class="col-12 col-sm-12 col-md-12 col-lg-2 col-xl-2  ">
			<div class="row">
				<div class="col-12  col-sm-4 col-md-4 col-lg-12 col-xl-12 " style="display: inline-flex; text-align: -webkit-right; margin-bottom: 5px;">
					<div class="btn-group d-flex" style="width: inherit;">
						<button id="btn_search" class="btn btn-primary w-100" type="button" style="width: -webkit-fill-available; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; padding: 1% 0;">
							<i class="fa fa-search"></i> Search
						</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	<button id="submit_button" type="submit" style="display: none;"></button>
</form>