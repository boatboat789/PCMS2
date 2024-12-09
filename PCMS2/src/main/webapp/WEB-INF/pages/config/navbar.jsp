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
			<li class="nav-item">
				<a id="PCMSSummaryPage" class="nav-link" href="${contextpath}/Main">PCMS - Summary</a>
			</li>
			<c:if test="${ConfigCusListTest[0].getIsPCMSDetailPage()  == true || ConfigCusListTest.size() == 0}">
				<li class="nav-item">
					<a id="PCMSDetailPage" class="nav-link" href="${contextpath}/Detail">PCMS - Detail</a>
				</li>
			</c:if>
		</ul>
	</div>
	<ul class="nav navbar-nav navbar-right ml-auto">
		<li class="dropdown text-small nav-profile">
			<a class="nav-link dropdown-toggle" href="#" id="navbarViewPagesDropdownMenuLink" data-toggle="dropdown" data-target="#navbarDropdownLogout" aria-haspopup="true" aria-expanded="false">
				<span><i class="fa fa-user" aria-hidden="true" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true"></i> &nbsp; <%=session.getAttribute("user")%> <%-- <%= session.getAttribute("userObject")%>.FirstName  --%> <%-- <%= session.getAttribute("userObject")%>.LastName --%> </span>
			</a>
			<div id="navbarDropdownLogout" class="dropdown-menu" aria-labelledby="navbarDropdownLogout">
				<a href="${contextpath}/logout" class=" waves-effect waves-block">
					<i class="fa fa-sign-out" aria-hidden="true"></i>Sign Out
				</a>
			</div>
		</li>
	</ul>  
	</header> 