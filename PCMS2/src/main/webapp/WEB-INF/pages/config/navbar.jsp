<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>
<%@include file="../config/common/commonBase.jsp"%>
<header id="navbar" class="navbar navbar-expand-sm navbar-dark bg-dark flex-column flex-md-row">
	<!-- 	<nav class="navbar navbar-expand-sm bg-dark navbar-dark" style="padding: 2px 10px;"> -->
	<!-- Brand/logo -->
	<a class="navbar-brand mr-0 mr-md-2" href="#">
		<img src="${contextpath}/resources/images/icons/logo.png" style="width: 50px; height: auto;" />
	</a>

	<div class="navbar-nav-scroll" id="navbarNavDropdown">
		<!-- Links -->
		<ul class="navbar-nav mr-auto">
			<c:if test="${permit.isPCMSMain()  == true }">
				<li class="nav-item">
					<a id="PCMSSummaryPage" class="nav-link" href="${contextpath}/Main">PCMS - Summary</a>
				</li>
			</c:if>
			<c:if test="${permit.isPCMSDetail()  == true }">
				<li class="nav-item">
					<a id="PCMSDetailPage" class="nav-link" href="${contextpath}/Detail">PCMS - Detail</a>
				</li>
			</c:if>
			<c:if test="${permit.isReport()  == true }">
<!-- 				<li class="nav-item"> -->
<%-- 					<a id="ReportPage" class="nav-link" href="${contextpath}/Report/CFM/getReportDetail">Report - CFM Report</a> --%>
<!-- 				</li> -->
				<li class="dropdown">
					<a class="nav-link dropdown-toggle" href="#" id="navbarReportSplitWorkDropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"> Report </a>
     
					<div class="dropdown-menu">   
						<h6 class="dropdown-header">CFM</h6>
						<a id="ReportPage" class="dropdown-item" href="${contextpath}/Report/CFM/Detail">CFM Detail</a>
						<hr class="dropdown-divider">
<!-- 						<h6 class="dropdown-header">DataFromERP</h6> -->
<%-- 						<a class="dropdown-item" href="${contextpath}/Log/ProdOperationLog/ATT">Log - Production Order / Operation</a> --%>
<%-- 						<a class="dropdown-item" href="${contextpath}/Log/RollNumberLog/ATT">Log - เบอร์ม้วน</a> --%>
<!-- 						<hr class="dropdown-divider"> -->
<!-- 						<h6 class="dropdown-header">ERP365</h6> -->
<%-- 						<a class="dropdown-item" href="${contextpath}/Log/ProdOperationLog/ERP365">Log - Production Order / Operation</a> --%>
<%-- 						<a class="dropdown-item" href="${contextpath}/Log/RollNumberLog/ERP365">Log - เบอร์ม้วน</a> --%>
					</div>
				</li> 
			</c:if>
		</ul>
	</div>         
	<ul class="nav navbar-nav navbar-right ml-auto">    
				<li class="nav-item dropdown">
					<a class="nav-item nav-link dropdown-toggle mr-md-2" 
						href="#" 
						id="navbarViewPagesDropdownMenuLink" 
						data-toggle="dropdown"       
						aria-haspopup="true" 
						aria-expanded="false">
						<span>
						<i class="fa fa-user"  ></i> &nbsp;<%=session.getAttribute("user")%> </span>
					</a> 
					<div id="navbarDropdownLogout" 
						class="dropdown-menu dropdown-menu-right " 
						aria-labelledby="navbarViewPagesDropdownMenuLink">
						<a href="${contextpath}/logout" class=" dropdown-item">
							<i class="fa fa-sign-out" aria-hidden="true"></i>Sign Out
						</a>
					</div>
				</li>
			</ul>
	</header> 