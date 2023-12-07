<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page isELIgnored="false"%> 
<!DOCTYPE html>
<html>
<head>    
	<meta http-equiv="Content-Type" content="text/html;" charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="icon" type="image/x-icon" href="resources/images/favicon_io/favicon.ico" />
	<link rel="apple-touch-icon" sizes="180x180" href="resources/images/favicon_io/apple-touch-icon.png">
	<link rel="manifest" href="resources/images/favicon_io/site.webmanifest">  
	<title>Login</title>  
	<jsp:include page="/WEB-INF/pages/config/css/baseCSS.jsp"></jsp:include> 
	<link href="<c:url value="/resources/css/login.css" />" rel="stylesheet" type="text/css"> 
</head> 
<body class="login-page" id="login_page"> 
    <div class="login-box " style="text-align: -webkit-center;">
        <div class="logo" >
            <h1>  
                <small style="color: #000000 !important;">
                 	<b class="btitle">PCMS v.2</b>
             	</small>
         	</h1>
         <h4 >
             <small style="color: #000000 !important;"><b class="bsubtitle">Production Control Monitoring System</b></small>
         </h4>
     </div>
     <div class="card"> 
        <div class="body">
<%-- 			<form name="login" action="${pageContext.request.contextPath}/login/loginAuth" method="get"> --%>
<%-- 			<form name="login" action="${pageCo	ntext.request.contextPath}/login/loginAuth" method="post"> --%>
			<form name="login" action="login/loginAuth" method="post">
                 <div class="input-group">
                     <span class="input-group-addon"> 
                         <i class="fa fa-user" aria-hidden="true"></i>
                     </span> 
                     <div class="form-line">
                         <input class="form-control" id="id_userId" name="userId" type="text" aria-describedby="userHelp" 
                         		placeholder="Username" pattern="[A-Za-z0-9]{1,20}" required>
                     </div>
                 </div>  
                 <div class="input-group">
                     <span class="input-group-addon"> 
						<i class="fa fa-unlock" aria-hidden="true"></i>
                     </span>
                     <div class="form-line">
                         <input class="form-control password-field"  id="userPassword" name="userPassword" type="password" placeholder="Password">
                     </div>
                     <span class="input-group-addon"> 
                        <span class="toggle-password fa fa-fw fa-eye fa-eye-slash"></span>
                     </span>
                 </div>  
<%--                  <div class="alert" th:if="${errorMessage}" th:text="${errorMessage}"></div> --%>
                 <button class="btn btn-xl btn-primary btn-block styleButton green-darken-2" type="submit">LOGIN</button>                                   
             </form>    
         </div>
      </div> 
   </div>    
<script> 
// $(document).ready(function(){
// 		console.log(String('000' + '5').slice(-4))
// 		console.log(String('000' + '10').slice(-4))
// 		console.log(String('000' + '220').slice(-4))
// 		console.log(String('000' + '0220').slice(-4))
// 		console.log(String('000' + '1220').slice(-4))
// })
//            authen
//    $(document).ready(function () {
//        var errormsg = "<c:out value="${errormsg}"/>";
//        console.log(errormsg); 

//    });  
	$(".toggle-password").click( function () {
	    $(this).toggleClass("fa-eye-slash");
	    var input = $("#userPassword"); 
	    if (input.attr("type") == "password") {
	        input.attr("type", "text");
	    } else {
	        input.attr("type", "password");
	    }
	});
	  $(document).ready( function(){
		 
          var alert = '${alertmsg}'; 
          var alertType ='${alerttyp}'; 
//           console.log(alert)
//           console.log(alertType)
          if(alert) {
              swal({
                  title: 'Warning',
                  text: alert,
                  icon: alertType ,
//                   type: '${alerttyp}'
              }).then( function() {
                  $('#username').focus();
              });
          } else { $('#username').focus(); }
      });
</script>
</body>

</html>
