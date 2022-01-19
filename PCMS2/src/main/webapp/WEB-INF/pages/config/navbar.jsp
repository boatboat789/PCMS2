<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> 
<%@ page isELIgnored="false"%>  
<div id="navbar">
	<nav class="navbar navbar-expand-sm bg-dark navbar-dark">    
		<!-- Brand/logo -->  
		<img src="resources/images/icons/logo.png"
			style="width: 50px; height: auto;    display: -webkit-box;" />
		<!-- Links -->  
		<ul class="navbar-nav">      
			<li class="nav-item"><a class="nav-link" href="PCMSMain">PCMS - Summary</a></li>  
 			<li class="nav-item"><a class="nav-link" href="PCMSDetail">PCMS - Detail</a></li>  
		</ul>
       <ul class="nav navbar-nav navbar-right ml-auto">
   			<li class="dropdown text-small nav-profile">
   				<a class="nav-link dropdown-toggle" href="#"
				id="navbarViewPagesDropdownMenuLink" data-toggle="dropdown"
				data-target="#navbarDropdownLogout" aria-haspopup="true"
				aria-expanded="false">
					<span><i  class="fa fa-user" aria-hidden="true" data-toggle="dropdown" aria-haspopup="true" 
					aria-expanded="true"></i>
					&nbsp;
					<%= session.getAttribute("user")%>
<%-- <%= session.getAttribute("userObject")%>.FirstName <%= session.getAttribute("userObject")%>.LastName --%>
					</span> 
				</a>
				<div id="navbarDropdownLogout" class="dropdown-menu" aria-labelledby="navbarDropdownLogout">
					<a href="logout" class=" waves-effect waves-block">
					<i class="fa fa-sign-out" aria-hidden="true"></i>Sign Out</a> 
				</div> 
			</li>  
		</ul>
	</nav>
</div>