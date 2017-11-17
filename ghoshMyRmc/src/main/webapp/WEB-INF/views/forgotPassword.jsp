<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
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

<body style="background-color: #afb7bf !important;">
	<nav class="navbar navbar-expand-lg navbar-dark bg-dark fixed-top"
		id="mainNav">
		<a class="navbar-brand" href="#"><i class="fa fa-fw fa-clone"
			style="font-size: 27px;"></i> MY RMC (My Risk Management and
			Compliance)</a>
	</nav>
	<div class="container" style="margin-top: 80px; margin-bottom: 80px;">
		<c:if test="${not empty errorMsg}">
			<div class="card card-login mx-auto mt-5">
				<div class="alert alert-danger" style="margin: 0;">${errorMsg}</div>
			</div>
		</c:if>
		<c:if test="${not empty msg}">
			<div class="card card-login mx-auto mt-5">
				<div class="alert alert-success" style="margin: 0;">${msg}</div>
			</div>
		</c:if>
		<div class="card card-login mx-auto mt-5"
			style="box-shadow: 5px 5px 5px #343a40; margin-top: 22px !important;">
			<div class="card-header">Forgot Password</div>
			<div class="card-body" style="padding: 1rem 1.75rem;">

				<sf:form modelAttribute="user" id="userForm"
					action="${contextRoot}/sendPassword" method="GET">
					<div class="form-group">
						<label for="exampleInputPassword1">Enter Your Mail Id</label> <input
							class="form-control" id="mailId" name="mailId" type="text"
							placeholder="Mail Id" style="line-height: 1;" />
					</div>
					<div style="display: inline-block; float: right;">
						<a class="btn btn-secondary" href="${contextRoot}/perform-logout">Cancel</a>
						<input type="submit" class="btn btn-primary  "
							value="Send New Password" />
					</div>
				</sf:form>
			</div>
		</div>

	</div>
	<footer class="sticky-footer" style="width: 100%; background: #343a40;">
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
	<script src="${js}/jquery.validate.js"></script>
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

	<script>
		var $userForm = $("#userForm");
		if ($userForm.length) {
			$userForm.validate({
				rules : {
					mailId : {
						required : true
					}
				},
				messages : {
					mailId : {
						required : "Please Enter your Registered Mail Id !!",
					}
				},
				errorElement : 'em',
				errorPlacement : function(error, element) {
					error.addClass("help-block");
					error.insertAfter(element);
				}
			});
		}
	</script>

	<%-- 	<script src="${js}/sb-admin-charts.min.js"></script> --%>

</body>

</html>
