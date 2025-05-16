<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>
<%@include file="../config/common/commonBase.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>        
<%-- 	<jsp:include page="/WEB-INF/pages/config/common/commonBase.jsp"></jsp:include>   --%>
	<jsp:include page="/WEB-INF/pages/config/meta.jsp"></jsp:include>           
	<jsp:include page="/WEB-INF/pages/config/css/baseCSS.jsp"></jsp:include>
    <title>${statusCode}</title> 
</head>
<body> 
	<jsp:include page="/WEB-INF/pages/config/navbar.jsp"></jsp:include>
    <table>
        <tr>      
            <td>       
            <h1 style="margin-bottom: 10px;">Fail</h1>
                <h1>${statusCode}</h1>
            </td>
        </tr>
        <tr>
            <td>
                <h1 style="margin-bottom: 10px;">${errorMsg}</h1>
            </td>      
        </tr>
        <tr>
			<td> 
				<a style="text-decoration: none;
            color: #0056b3;
            font-weight: bold;" href="${contextpath}/logout">back to login page</a>
			</td>
		</tr>
    </table>
</body>
</html>