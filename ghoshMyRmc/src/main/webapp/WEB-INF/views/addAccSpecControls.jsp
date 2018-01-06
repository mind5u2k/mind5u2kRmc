<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:url var="css" value="/resources/css"></spring:url>
<spring:url var="js" value="/resources/js"></spring:url>
<spring:url var="images" value="/resources/images"></spring:url>
<spring:url var="vendor" value="/resources/vendor"></spring:url>
<spring:url var="jqueryUi" value="/resources/jqueryUi"></spring:url>
<c:set var="contextRoot" value="${pageContext.request.contextPath}"></c:set>
<!DOCTYPE html>
<html lang="en">

<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">
<meta name="description" content="">
<meta name="author" content="">
<title>${title}</title>
<script>
	window.menu = '${title}';
	window.page = '${totalAccounts}';
	window.contextRoot = '${contextRoot}';
</script>
<link rel="stylesheet" href="${jqueryUi}/jquery-ui.css">
<link rel="stylesheet" href="${jqueryUi}/style.css">

<!-- Bootstrap core CSS-->
<link href="${vendor}/bootstrap/css/bootstrap.min.css" rel="stylesheet">
<!-- Custom fonts for this template-->
<link href="${vendor}/font-awesome/css/font-awesome.min.css"
	rel="stylesheet" type="text/css">
<!-- Page level plugin CSS-->
<link href="${vendor}/datatables/dataTables.bootstrap4.css"
	rel="stylesheet">
<!-- Custom styles for this template-->
<link href="${css}/sb-admin.css" rel="stylesheet">
<script src="${vendor}/jquery/jquery.min.js"></script>
<script src="${jqueryUi}/jquery-ui.js"></script>
</head>

<body class="fixed-nav sticky-footer" id="page-top">
	<!-- Navigation-->
	<%@include file="./approverShared/header.jsp"%>
	<div class="container-fluid" style="padding: 18px;">
		<div class="row">
			<div class="col-xl-12" style="text-align: center;">
				<h4>Add/Remove Controls for Category -
					${assessmentCategory.assignedCategories.name}</h4>
			</div>
		</div>
		<div class="dropdown-divider"></div>
		<div class="row">
			<div class="col-xl-12"
				style="text-align: right; padding-right: 37px;">
				<a class="btn btn-primary"
					onclick="window.location.href='addAllQuestion?assCatId=${assessmentCategory.id}'">Assign
					All</a>
			</div>
			<div class="col-xl-12">
				<c:if test="${not empty msg}">
					<div class="card card-login mx-auto mt-5"
						style="margin: auto !important; border: 0; max-width: 34rem;">
						<div class="alert alert-success" style="margin: 0;">${msg}</div>
					</div>
				</c:if>
				<c:if test="${not empty erroMsg}">
					<div class="card card-login mx-auto mt-5"
						style="margin: auto !important; border: 0; max-width: 34rem;">
						<div class="alert alert-danger" style="margin: 0;">${erroMsg}</div>
					</div>
				</c:if>
				<div class="table-responsive">
					<table class="table table-bordered" id="dataTable" width="100%"
						style="font-size: .9rem;" cellspacing="0">
						<thead>
							<tr>
								<th style="white-space: nowrap;">ID</th>
								<th>Category</th>
								<th>Controls</th>
								<th>Answers</th>
								<th>Rating</th>
								<th>Flag</th>
								<th>Risk</th>
								<th style="width: 93px;">Add/Remove</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${allControlsforSelectedCat}"
								var="controlModel">
								<c:set var="i" value="${i+1}" scope="page" />
								<tr>
									<td>${i}.</td>
									<td>${controlModel.control.category.name}</td>
									<td>${controlModel.control.control}</td>
									<td>${controlModel.control.answers}</td>
									<td>${controlModel.control.rating}</td>
									<td>${controlModel.control.flag}</td>
									<td>${controlModel.control.risk}</td>
									<c:if test="${controlModel.status==true}">
										<td style="text-align: center; cursor: pointer;"
											class="btn-success"
											onclick="window.location.href='removeControl?id=${controlModel.control.id}&assCatId=${assessmentCategory.id}'">
											Remove</td>
									</c:if>
									<c:if test="${controlModel.status==false}">
										<td style="text-align: center; cursor: pointer;"
											class="btn-primary"
											onclick="window.location.href='addControl?id=${controlModel.control.id}&assCatId=${assessmentCategory.id}'">
											Add</td>
									</c:if>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</div>
		<div class="dropdown-divider"></div>
		<div class="row" style="float: right;">
			<div class="col-xl-12">
				<button class="btn btn-primary" style="cursor: pointer;"
					onclick="window.location.href='assessmentPage?assessmentId=${assessmentCategory.assessment.id}'">Go
					to Assessment Page</button>
			</div>
		</div>
	</div>
	<footer class="sticky-footer" style="width: 100%;">
		<div class="container">
			<div class="text-right">
				<small>Developed by Anurag Ghosh</small>
			</div>
		</div>
	</footer>
	<!-- Scroll to Top Button-->
	<a class="scroll-to-top rounded" href="#page-top"> <i
		class="fa fa-angle-up"></i>
	</a>

	<!-- Bootstrap core JavaScript-->

	<script src="${vendor}/popper/popper.min.js"></script>
	<script src="${vendor}/bootstrap/js/bootstrap.min.js"></script>
	<!-- Core plugin JavaScript-->
	<script src="${vendor}/jquery-easing/jquery.easing.min.js"></script>
	<!-- Page level plugin JavaScript-->
	<%-- 	<script src="${vendor}/chart.js/Chart.min.js"></script> --%>
	<script src="${vendor}/datatables/jquery.dataTables.js"></script>
	<script src="${vendor}/datatables/dataTables.bootstrap4.js"></script>
	<!-- Custom scripts for all pages-->
	<script src="${js}/sb-admin.min.js"></script>
	<!-- Custom scripts for this page-->
	<script src="${js}/sb-admin-datatables.min.js"></script>
	<%-- 	<script src="${js}/sb-admin-charts.min.js"></script> --%>

	<script src="${js}/myapp.js"></script>
</body>

</html>

