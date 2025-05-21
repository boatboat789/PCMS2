<%-- <%@ page language="java" contentType="text/html; charset=UTF-8" --%>
<%--     pageEncoding="UTF-8"%> --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>
<div id="wrapper-top" class="row" style="margin: 2px 5px; background-color: azure; margin: 2px 5px; padding: 10px 10px 10px 10px;">
	<div class="col-12 col-sm-12 col-md-12 col-lg-12 col-xl-12 ">
		<h2 style="text-align-last: center;">
			<span id="span_headerName" class="c"><%=request.getAttribute("HeaderName")%></span>
		</h2> 

	</div>
	<div class="col-12 col-sm-12 col-md-12 col-lg-10 col-xl-10 " style="display: none;">
		<div class="row">
			<div class="col-12 col-sm-6 col-md-4 col-lg-3 col-xl-3 form-group lab-print">
				<label class=" my-1 mr-2 " for="multi_userStatusList">User Status</label>
				<div class=" " style='width: inherit;'>
					<select disabled id="multi_userStatusList" class="selectpicker" multiple data-width="100%" data-selected-text-format="count > 1" data-live-search="true" data-actions-box="true">
					</select>
				</div>
			</div>
			<div class="col-12 col-sm-6 col-md-4 col-lg-3 col-xl-3 form-group lab-print">
				<label class=" my-1 mr-2 " for="multi_labStatusList">Lab Status</label>
				<div  class=" " style='width: inherit;'>
					<select disabled id="multi_labStatusList" class="selectpicker" multiple data-width="100%" data-selected-text-format="count > 1" data-live-search="true" data-actions-box="true">
					</select>
				</div>
			</div>
		</div>
	</div>
	<!--  ROW 2 -->
	<div class="col-12 col-sm-12 col-md-12 col-lg-12 col-xl-12  ">
		<div class="row" style="justify-content: right;">
			<div class="col-12  col-sm-4 col-md-4 col-lg-4 col-xl-4  " style="text-align: -webkit-right; margin-bottom: 5px;">
				<button id="btn_download" class="btn btn-primary" type="button">
					<i class="fa fa-download"></i> Download Excel
				</button>
			</div>
		</div>
	</div>
</div>
