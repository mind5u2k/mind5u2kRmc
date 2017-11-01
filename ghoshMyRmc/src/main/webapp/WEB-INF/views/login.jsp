<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:url var="css" value="resources/css"></spring:url>
<spring:url var="js" value="resources/js"></spring:url>
<spring:url var="images" value="resources/images"></spring:url>
<spring:url var="vendor" value="resources/vendor"></spring:url>
<c:set var="contextRoot" value="${pageContext.request.contextPath}"></c:set>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">
<meta name="description" content="">
<meta name="author" content="">
<title>MY-RMC</title>
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
</head>

<body style="background-color: #afb7bf !important;">
	<nav class="navbar navbar-expand-lg navbar-dark bg-dark fixed-top"
		id="mainNav"> <a class="navbar-brand" href="#"><i
		class="fa fa-fw fa-clone" style="font-size: 27px;"></i> MY RMC (My
		Risk Management and Compliance)</a> </nav>
	<div class="container" style="margin-top: 80px; margin-bottom: 80px;">
		<c:if test="${not empty msg}">
			<div class="card card-login mx-auto mt-5">
				<div class="alert alert-danger" style="margin: 0;">${msg}</div>
			</div>
		</c:if>
		<c:if test="${not empty logout}">
			<div class="card card-login mx-auto mt-5">
				<div class="alert alert-success" style="margin: 0;">${logout}</div>
			</div>
		</c:if>
		<div class="card card-login mx-auto mt-5"
			style="box-shadow: 5px 5px 5px #343a40; margin-top: 22px !important;">
			<div class="card-header">Login</div>
			<div class="card-body" style="padding: 1rem 1.75rem;">
				<form action="${contextRoot}/login" method="POST" id="loginForm">
					<div class="form-group">
						<label for="exampleInputEmail1">Email address</label> <input
							class="form-control" id="username" name="username" type="email"
							aria-describedby="emailHelp" placeholder="Enter email"
							style="line-height: 1;" />
					</div>
					<div class="form-group">
						<label for="exampleInputPassword1">Password</label> <input
							class="form-control" id="password" name="password"
							type="password" placeholder="Password" style="line-height: 1;" />
					</div>
					<div class="form-group">
						<div class="form-check">
							<label class="form-check-label"> <input
								class="form-check-input" type="checkbox"> Remember
								Password
							</label>
						</div>
					</div>
					<input type="submit" class="btn btn-primary btn-block"
						value="login" href="index.html" /> <input type="hidden"
						name="${_csrf.parameterName}" value="${_csrf.token}" />
				</form>
				<div class="text-center">
					<a class="d-block small mt-3" href="forgot-password.html">Forgot
						Password?</a>
				</div>
			</div>
		</div>

	</div>
	<footer class="sticky-footer" style="width: 100%;background: #343a40; ">
	<div class="container-fluid" style="padding: 0 18px;">
		<div class="text-right" style="color: #fff;">
			<small>Developed by <a style="cursor: pointer;"
				target="_blank" href="https://www.linkedin.com/in/anuraghosh/">Anurag
					Ghosh</a>
			</small>
		</div>
	</div>
	</footer>
	<!-- 	Scroll to Top Button -->
	<a class="scroll-to-top rounded" href="#page-top"> <i
		class="fa fa-angle-up"></i>
	</a>
	<!-- Bootstrap core JavaScript-->
	<script src="${vendor}/jquery/jquery.min.js"></script>
	<script src="${vendor}/popper/popper.min.js"></script>
	<script src="${vendor}/bootstrap/js/bootstrap.min.js"></script>
	<!-- Core plugin JavaScript-->
	<script src="${vendor}/jquery-easing/jquery.easing.min.js"></script>
	<!-- Page level plugin JavaScript-->
	<script src="${vendor}/chart.js/Chart.min.js"></script>
	<script src="${vendor}/datatables/jquery.dataTables.js"></script>
	<script src="${vendor}/datatables/dataTables.bootstrap4.js"></script>
	<!-- Custom scripts for all pages-->
	<script src="${js}/sb-admin.min.js"></script>
	<!-- Custom scripts for this page-->
	<script src="${js}/sb-admin-datatables.min.js"></script>
	<script src="${js}/sb-admin-charts.min.js"></script>
</body>

</html>
