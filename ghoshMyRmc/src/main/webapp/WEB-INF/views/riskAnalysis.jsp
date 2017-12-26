<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%@page import="net.gh.ghoshMyRmcBackend.dto.Assessment"%>
<%@page import="net.gh.ghoshMyRmcBackend.dao.AssessmentDao"%>

<html>
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

	window.categories = '${categories}';
	window.categoryRisk = '${categoryRisk}';

	window.intExtRisk = '${intExtRisk}';
	window.intExtRiskVal = '${intExtRiskVal}';
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
<style>
.table-bordered th {
	border: 1px solid #aeb0b5;
	background: #d4d4d4;
}

.table-bordered td {
	border: 1px solid #aeb0b5;
}
</style>
</head>

<body class="fixed-nav sticky-footer" id="page-top">
	<!-- Navigation-->
	<c:if test="${userModel.role == 'Assessor'}">
		<%@include file="./assessorShared/header.jsp"%>
	</c:if>
	<c:if test="${userModel.role == 'Approver'}">
		<%@include file="./approverShared/header.jsp"%>
	</c:if>
	<c:if test="${userModel.role == 'Reviewer'}">
		<%@include file="./reviewerShared/header.jsp"%>
	</c:if>
	<c:if test="${userModel.role == 'SME'}">
		<%@include file="./smeShared/header.jsp"%>
	</c:if>
	<c:if test="${userModel.role == 'Admin'}">
		<%@include file="./adminShared/header.jsp"%>
	</c:if>
	<c:if test="${userModel.role == 'Super Admin'}">
		<%@include file="./adminShared/header.jsp"%>
	</c:if>
	<div class="container-fluid">
		<div class="row">
			<div class="col-xl-7" style="padding: 2px;">
				<div class="table-responsive">
					<table class="table table-bordered" id="" width="100%"
						style="font-size: .9rem; border: 1px solid #6e7277;"
						cellspacing="0">
						<tr>
							<th colspan="5" style="text-align: center;">Account Details</th>
						</tr>
						<tr>
							<th>Client/Department</th>
							<th>Location</th>
							<th>LOB</th>
							<th>Phase</th>
							<th>Status</th>
						</tr>
						<tbody>
							<tr>
								<td>${assessment.account.department.name}</td>
								<td>${assessment.account.location.name}</td>
								<td>${assessment.account.lob.name}</td>
								<td>${assessment.account.phase}</td>
								<td>${assessment.assessmentStatus}</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
			<div class="col-xl-2" style="padding: 2px;">
				<div class="table-responsive">
					<table class="table table-bordered" id=" " width="100%"
						style="font-size: .9rem; border: 1px solid #6e7277;">
						<tr>
							<th colspan="2" style="text-align: center;">Total - Risk
								Factor</th>
						</tr>
						<tr>
							<th>Critical Controls</th>
							<td>${criticalRiskFactor}</td>
						</tr>
						<tbody>
							<tr>
								<th>All Controls</th>
								<td>${totalRiskFactor}</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
			<div class="col-xl-2" style="padding: 2px;">
				<div class="table-responsive">
					<table class="table table-bordered" id=" " width="100%"
						style="font-size: .9rem; border: 1px solid #6e7277;">
						<tr>
							<th colspan="2" style="text-align: center;">Risk Rating</th>
						</tr>
						<tr>
							<th>Current</th>
							<td>${currentRiskRating}</td>
						</tr>
						<tbody>
							<tr>
								<th>Initial</th>
								<td>${initialRiskRating}</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
			<div class="col-xl-1" style="padding: 2px;">
				<div class="table-responsive">
					<table class="table table-bordered" id="" width="100%"
						style="font-size: .9rem; border: 1px solid #6e7277;">

						<tr>
							<th style="text-align: center;">Export</th>
						</tr>
						<tr>
							<td style="text-align: center;"><a target="_blank"
								href="downloadPdf/${assessment.id}">PDF</a></td>
						</tr>
						<tr>
							<td style="text-align: center;"><a target="_blank"
								href="downloadExcel/${assessment.id}">Excel</a></td>
						</tr>
					</table>
				</div>
			</div>
		</div>
	</div>
	<div class="dropdown-divider"></div>
	<div class="container-fluid">

		<div class="row">
			<div class="col-xl-8">
				<div class="card mb-3">
					<div class="card-header">
						<i class="fa fa-table"></i> Category Wise Risk
					</div>
					<div class="card-body" style="font-size: 0.9rem;">
						<canvas id="categoryWiseRiskChart" width="100%" height="35%"></canvas>
					</div>
				</div>
			</div>
			<div class="col-xl-4">
				<div class="card mb-3">
					<div class="card-header">
						<i class="fa fa-table"></i> Internal / External Risk
					</div>
					<div class="card-body" style="font-size: 0.9rem;">
						<canvas id="myPieChart" width="100" height="70%"></canvas>
					</div>
				</div>
			</div>
		</div>
		<div class="row" id="catWiseRiskBtn"
			style="text-align: center; padding: 18px;">
			<div class="col-xl-12">
				<button class="btn btn-primary" onclick="categoryWiseRisk();">Show
					Risk for All categories</button>
			</div>
		</div>
		<div class="row" id="catWiseRisk" style="display: none;">
			<%
				Long assessmentId = (Long) request.getAttribute("assessmentId");
				System.out.println("assessment id is [" + assessmentId + "]");
			%>
			<c:forEach items="${assessmentCategories}" var="assessmentCategory"
				varStatus="status">
				<div class="col-xl-6">
					<div class="card mb-3">
						<div class="card-header">
							<i class="fa fa-table"></i>
							${assessmentCategory.assignedCategories.name}
						</div>
						<div class="card-body" style="font-size: 0.9rem;"
							id="cardBody${assessmentCategory.id}"></div>
					</div>
				</div>
			</c:forEach>

		</div>
	</div>
	<footer class="sticky-footer" style="width: 100%;">
		<div class="container">
			<div class="text-right">
				<small>© 2017 All Rights Reserved</small>
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
	<script src="${vendor}/chart.js/Chart.min.js"></script>
	<script src="${vendor}/datatables/jquery.dataTables.js"></script>
	<script src="${vendor}/datatables/dataTables.bootstrap4.js"></script>
	<!-- Custom scripts for all pages-->
	<script src="${js}/sb-admin.min.js"></script>
	<!-- Custom scripts for this page-->
	<script src="${js}/sb-admin-datatables.min.js"></script>
	<script src="${js}/sb-admin-charts.js"></script>
	<script src="${js}/myapp.js"></script>

	<script>
		function categoryWiseRisk() {
			$("#catWiseRisk").css("display", "");
			$("#catWiseRiskBtn").css("display", "none");
			<c:forEach items="${assessmentCategories}" var="assessmentCategory"
				varStatus="status">
			$.ajax({
				type : "GET",
				url : "assessmentCatChart?assCatId=${assessmentCategory.id}",
				success : function(response) {
					$('#cardBody${assessmentCategory.id}').html(response);
				},
				error : function(e) {
					alert('Error: ' + e);
				}
			});
			</c:forEach>

		}
	</script>
</body>
</html>

