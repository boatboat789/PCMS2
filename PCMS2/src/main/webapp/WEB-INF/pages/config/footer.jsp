<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> 
<%@ page isELIgnored="false"%>  
<footer class="page-footer font-small blue pt-4">
   
  <!-- Footer Links -->
  <div class="container-fluid text-center text-md-left">
  </div>      
  <div class="footer-copyright text-center py-3">©<span id="footer-year"></span> Copyright:
  </div>
  <script>document.getElementById('footer-year').textContent = new Date().getFullYear();</script>
</footer> 