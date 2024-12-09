<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<html>
<head>
       <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${statusCode}</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f8d7da;
            color: #721c24;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }
        .container {
            text-align: center;
            border: 1px solid #f5c6cb;
            background-color: #f8d7da;
            padding: 20px;
            border-radius: 5px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
        }
        h1 {
            margin-bottom: 10px;
        }
        p {
            margin: 5px 0;
        }
        a {
            text-decoration: none;
            color: #0056b3;
            font-weight: bold;
        }
    </style>
</head>
<body>
<%@include file="../common/commonBase.jsp"%>
    
    <table>
        <tr>      
            <td>       
            <h1>Fail</h1>
                <h1>${statusCode}</h1>
            </td>
        </tr>
        <tr>
            <td>
                <h1>${errorMsg}</h1>
            </td>      
        </tr>
        <tr>
			<div>
				<a href="${contextpath}/logout">back to login page</a>
			</div>
		</tr>
    </table>
</body>
</html>