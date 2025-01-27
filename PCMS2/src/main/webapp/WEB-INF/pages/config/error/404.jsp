<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page isELIgnored="false"%>
<html>

<head>
	<jsp:include page="/WEB-INF/pages/config/meta.jsp"></jsp:include>    
	<jsp:include page="/WEB-INF/pages/config/css/baseCSS.jsp"></jsp:include>
	<title >404</title>      
</head>
<body>  
	<jsp:include page="/WEB-INF/pages/config/navbar.jsp"></jsp:include>
    <table>
        <tr>      
            <td>       
            <h1 style="margin-bottom: 10px;">Fail</h1>
                <h1 style="margin-bottom: 10px;">${statusCode}</h1>
            </td>
        </tr>
        <tr>
            <td>
                <h1 style="margin-bottom: 10px;">${errorMsg}</h1>
            </td>      
        </tr> 
    </table>
</body>
</html>