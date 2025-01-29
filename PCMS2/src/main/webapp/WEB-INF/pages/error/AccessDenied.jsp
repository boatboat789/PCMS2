<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>
<%@include file="../config/common/commonBase.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>        
	<jsp:include page="/WEB-INF/pages/config/meta.jsp"></jsp:include>           
	<jsp:include page="/WEB-INF/pages/config/css/baseCSS.jsp"></jsp:include>
    <title>Access Denied</title> 
</head>    
<body class="body-error">  
	<jsp:include page="/WEB-INF/pages/config/navbar.jsp"></jsp:include>
    <div class="container-error  "> 
        <h1 style="margin-bottom: 10px;">Access Denied</h1>
        <p style="margin: 5px 0;">${errorMsg}</p>   
        <p>You will be redirected to the 
        <a 
       		style="text-decoration: none;
            color: #0056b3;
            font-weight: bold;"
             href="${contextpath}/logout">login page</a>  </p>
    </div>
</body>
</html>