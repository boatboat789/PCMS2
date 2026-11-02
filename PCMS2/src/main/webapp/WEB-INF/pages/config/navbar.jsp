<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>
<%@include file="../config/common/commonBase.jsp"%>
<header id="navbar" class="navbar navbar-expand navbar-dark bg-dark">
<!-- 	<nav class="navbar navbar-expand-sm bg-dark navbar-dark" style="padding: 2px 10px;"> -->
	<!-- Brand/logo -->
	<a class="navbar-brand mr-2" href="#">
		<img src="${contextpath}/resources/images/icons/logo.png" style="width: 50px; height: auto;" />
	</a>

<nav class="navbar-collapse" id="navbarNavDropdown" aria-label="เมนูหลัก">
    <ul class="navbar-nav mr-auto">
        <%-- ส่วนของ Summary --%>
        <c:if test="${permit.isPCMSMain() == true}">
            <li class="nav-item">
                <a id="PCMSSummaryPage" class="nav-link" href="${contextpath}/Main">PCMS - Summary</a>
            </li>
        </c:if>

        <%-- ปรับส่วน Detail ให้เป็น Dropdown --%>
        <c:if test="${permit.isPCMSDetail() == true}">
            <li class="nav-item">
                <a id="PCMSDetailPage" class="nav-link" href="${contextpath}/Detail">PCMS - Detail</a> 
            </li>
<!--             <li class="nav-item dropdown"> -->
<!--                 <a class="nav-link dropdown-toggle" href="#" id="navbarDetailDropdown" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"> -->
<!--                     PCMS - Detail -->
<!--                 </a> -->
<!--                 <div class="dropdown-menu" aria-labelledby="navbarDetailDropdown">  -->
<%--                     <a id="PCMSDetailPage" class="dropdown-item" href="${contextpath}/Detail">PCMS - Detail</a> --%>
<!--                 </div> -->
<!--             </li> -->
        </c:if>

        <%-- ส่วนของ Report (คงเดิมไว้) --%>
        <c:if test="${permit.isReport() == true}">
            <li class="nav-item dropdown">
                <a class="nav-link dropdown-toggle" href="#" id="navbarReportSplitWorkDropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"> Report </a>
                <div class="dropdown-menu">   
                    <h6 class="dropdown-header">CFM</h6>
                    <a id="ReportPage" class="dropdown-item" href="${contextpath}/Report/CFM/Detail">CFM Detail</a>
                    <div class="dropdown-divider"></div>
                    <h6 class="dropdown-header">DataFromERP</h6>
                    <a class="dropdown-item" href="${contextpath}/Log/ProductionOrderLog/ATT">Log - Production Order</a>
                    <a class="dropdown-item" href="${contextpath}/Log/SaleOrderLog/ATT">Log - Sale Order</a>
                    <div class="dropdown-divider"></div>
                    <h6 class="dropdown-header">ERP365</h6>
                    <a class="dropdown-item" href="${contextpath}/Log/ProductionOrderLog/ERP365">Log - Production Order</a>
                    <a class="dropdown-item" href="${contextpath}/Log/SaleOrderLog/ERP365">Log - Sale Order</a>
                </div>
            </li> 
        </c:if>
        <c:if test="${permit.permitId == 'ADMIN' || permit.permitId == 'ITSUPP'}">
            <li class="nav-item dropdown">
                <a class="nav-link dropdown-toggle" href="#" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"> Setting </a>
                <div class="dropdown-menu">
                    <a class="dropdown-item" href="${contextpath}/Setting/JobManagement">Job Management</a>
                    <a class="dropdown-item" href="${contextpath}/Setting/PermitManagement">Permit Management</a>
                </div>
            </li>
        </c:if>
    </ul>
		<ul class="navbar-nav navbar-right ml-lg-auto">    
				<li class="nav-item dropdown">
					<a class="nav-item nav-link dropdown-toggle mr-md-2" 
						href="#" 
						id="navbarViewPagesDropdownMenuLink" 
						data-toggle="dropdown"       
						aria-haspopup="true" 
						aria-expanded="false">
						<span>
						<i class="fas fa-user"  ></i> &nbsp;<%=session.getAttribute("user")%> </span>
					</a> 
					<div id="navbarDropdownLogout" 
						class="dropdown-menu dropdown-menu-lg-right" 
						aria-labelledby="navbarViewPagesDropdownMenuLink">
						<a href="${contextpath}/logout" class=" dropdown-item">
							<i class="fas fa-sign-out" aria-hidden="true"></i>Sign Out
						</a>
					</div>
				</li>
			</ul>
	</nav>
</header>
<script>
// ทำเครื่องหมายหน้าปัจจุบันในเมนู (aria-current + class active) — ไม่ต้องแก้ทีละหน้า
(function () {
	var path = location.pathname.replace(/\/+$/, '');
	var best = null, bestLen = 0;
	var links = document.querySelectorAll('#navbarNavDropdown a[href]:not([href="#"]):not(.disabled-href)');
	for (var i = 0; i < links.length; i++) {
		var p = links[i].pathname.replace(/\/+$/, '');
		if (p && (path === p || path.indexOf(p + '/') === 0) && p.length > bestLen) { best = links[i]; bestLen = p.length; }
	}
	if (!best) return;
	best.classList.add('active');
	best.setAttribute('aria-current', 'page');
	var menu = best.closest('.dropdown-menu');
	if (menu && menu.parentElement) {
		var toggle = menu.parentElement.querySelector('.dropdown-toggle');
		if (toggle) toggle.classList.add('active');
	}
})();
</script>
 