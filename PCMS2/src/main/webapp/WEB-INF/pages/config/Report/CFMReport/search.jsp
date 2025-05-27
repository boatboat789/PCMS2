<%-- <%@ page language="java" contentType="text/html; charset=UTF-8" --%>
<%--     pageEncoding="UTF-8"%> --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>
<iframe id="remember" name="fakeSubmit" style="display: none;"></iframe>
<form target="fakeSubmit" method="post" action="Main/fakeSubmit">
	<div id="wrapper-top" class="row" style="background-color: azure; margin: 2px 5px; padding: 10px 10px 10px 10px;">
		<div class="col-12 col-sm-12 col-md-12 col-lg-12 col-xl-12 ">
			<h2 style="text-align-last: center;">
				<span id="span_headerName" class="c"><%=request.getAttribute("HeaderName")%></span>
			</h2>

		</div>
		<div class="col-12 col-sm-12 col-md-12 col-lg-10 col-xl-10 ">
			<div class="col-12 col-sm-4 col-md-3 col-lg-2 col-xl-2 form-group lab-print">
				<label class="my-1 mr-2 label-input-0" for="input_custName" title="Cust. Name">Cust.Name</label>
				<div>
					<input class="form-control " autocomplete="off" type="text" id="input_custName">
				</div>
			</div>
			<div class="col-12 col-sm-4 col-md-3 col-lg-2 col-xl-3 form-group lab-print">
				<label class="my-1 mr-2 label-input-0" for="input_saleOrder" title="Sale Order">Sale Order</label>
				<div>
					<input class="form-control " autocomplete="off" type="text" id="input_saleOrder">
				</div>
			</div>
			<div class="col-12 col-sm-4 col-md-3 col-lg-2 col-xl-32 form-group lab-print">
				<label class="my-1 mr-2 label-input-0" for="input_lotNo" title="Lot No.">Lot No. </label>
				<div>
					<input class="form-control " autocomplete="off" type="text" id="input_lotNo">
				</div>
			</div>
			<div class="col-12  col-sm-4 col-md-3 col-lg-2 col-xl-3 form-group lab-print">
				<label class="my-1 mr-2 label-input-0" for="input_prdOrder" title="Production Id">Prod.Id</label>
				<div>
					<input class="form-control " autocomplete="off" type="text" id="input_prdOrder">
				</div>  
			</div>   
			<div class="col-12  col-sm-4 col-md-3 col-lg-3 col-xl-3 form-group lab-print">
				<label class="my-1 mr-2 label-input-0" for="input_sendDate" title="Send Date">Send Date</label>
				<div>
					<input class="form-control " autocomplete="off" type="text" name="daterange" class='form-control' id="input_sendDate">
				</div>
			</div>      
			<div class="col-12  col-sm-4 col-md-3 col-lg-3 col-xl-3 form-group lab-print">
				<label class="my-1 mr-2 label-input-0" for="input_replyDate" title="Reply Date">Reply Date</label>
				<div>
					<input class="form-control " autocomplete="off" type="text" name="daterange" class='form-control' id="input_replyDate">
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
				<div class="col-12  col-sm-4 col-md-4 col-lg-12 col-xl-12  " style="text-align: -webkit-right; margin-bottom: 5px;">
					<button id="btn_download" class="btn btn-primary" type="button" style="padding: 1% 0; width: -webkit-fill-available;">
						<i class="fa fa-download"></i> Download Excel
					</button>
				</div>
			</div>
		</div>
	</div>
	<button id="submit_button" type="submit" style="display: none;"></button>
</form>