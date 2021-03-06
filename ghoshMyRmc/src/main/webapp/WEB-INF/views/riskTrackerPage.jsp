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
<style>
a.sss {
	background: #f3f3f3;
	border-bottom: 1px solid #c1c0c0;
	margin-top: 4px;
	border-top: 1px solid #c1c0c0;
}

table.cTable td {
	border: 1px solid #a79d9d;
	padding: 2px 6px;
}

.dropdown-item:focus, .dropdown-item:hover {
	background: #e6e4e4;
	cursor: pointer;
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
	<div class="container-fluid" style="padding: 18px;">
		<div class="row" style="text-align: left;">
			<div class="col-xl-4">
				<div class="btn-group">
					<button type="button" class="btn btn-primary dropdown-toggle"
						style="width: 135px;" data-toggle="dropdown" aria-haspopup="true"
						aria-expanded="false">
						${selectedAssessmentCategory.assignedCategories.name} &nbsp;&nbsp;</button>
					<div class="dropdown-menu" style="border: 1px solid #c7c7c7;">
						<c:forEach items="${assessmentCategories}"
							var="assessmentCategory">
							<a class="dropdown-item s"
								href="riskTracker?assessmentId=${assessmentCategory.assessment.id}&catId=${assessmentCategory.id}">${assessmentCategory.assignedCategories.name}
							</a>
						</c:forEach>
					</div>
				</div>
				<div class="btn-group">
					<button type="button" class="btn btn-primary dropdown-toggle"
						data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">${ctrlTypeString}&nbsp;&nbsp;</button>
					<div class="dropdown-menu">
						<a class="dropdown-item s" style="color: #007bff;"
							href="riskTracker?assessmentId=${selectedAssessmentCategory.assessment.id}&catId=${selectedAssessmentCategory.id}&ctrlType=all"><i
							class="fa fa-thumbs-o-up" aria-hidden="true"></i> All Controls
							[${totalAnswer}] </a><a class="dropdown-item" style="color: #dc3545;"
							href="riskTracker?assessmentId=${selectedAssessmentCategory.assessment.id}&catId=${selectedAssessmentCategory.id}&ctrlType=totalRisk"><i
							class="fa fa-thumbs-o-down" aria-hidden="true"></i> Total Risks
							[${totalRisks}] </a><a class="dropdown-item" style="color: #dc3545;"
							href="riskTracker?assessmentId=${selectedAssessmentCategory.assessment.id}&catId=${selectedAssessmentCategory.id}&ctrlType=reviewPendingforNC"><i
							class="fa fa-thumbs-down" aria-hidden="true"></i> Review Pending
							for NC [${reviewPendingforNC}]</a><a class="dropdown-item"
							style="color: #28a745;"
							href="riskTracker?assessmentId=${selectedAssessmentCategory.assessment.id}&catId=${selectedAssessmentCategory.id}&ctrlType=reviewPendingforNonNC"><i
							class="fa fa-thumbs-o-down" aria-hidden="true"></i> Review
							Pending for Non NC [${reviewPendingforNonNC}]</a><a
							class="dropdown-item" style="color: #dc3545;"
							href="riskTracker?assessmentId=${selectedAssessmentCategory.assessment.id}&catId=${selectedAssessmentCategory.id}&ctrlType=reviewCompleteforNC"><i
							class="fa fa-thumbs-o-up" aria-hidden="true"></i> Review Complete
							for NC [${reviewCompleteforNC}]</a><a class="dropdown-item"
							style="color: #28a745;"
							href="riskTracker?assessmentId=${selectedAssessmentCategory.assessment.id}&catId=${selectedAssessmentCategory.id}&ctrlType=reviewCompleteforNonNC"><i
							class="fa fa-thumbs-up" aria-hidden="true"></i> Review Complete
							for Non NC [${reviewCompleteforNonNC}]</a><a class="dropdown-item"
							style="color: #dc3545;"
							href="riskTracker?assessmentId=${selectedAssessmentCategory.assessment.id}&catId=${selectedAssessmentCategory.id}&ctrlType=changeRequiredforNC"><i
							class="fa fa-exclamation-triangle" aria-hidden="true"></i> More
							info Requested for NC [${changeRequiredforNC}]</a><a
							class="dropdown-item" style="color: #28a745;"
							href="riskTracker?assessmentId=${selectedAssessmentCategory.assessment.id}&catId=${selectedAssessmentCategory.id}&ctrlType=changeRequiredforNonNC"><i
							class="fa fa-exclamation-triangle" aria-hidden="true"></i> More
							info Requested for Non NC [${changeRequiredforNonNC}]</a>
					</div>
				</div>
			</div>
			<div class="col-xl-8">
				<table>
					<tr>
						<td style="padding: 0px 16px;">Account Name :
							${assessment.account.department.name}</td>
						<td
							style="padding: 0px 16px; border-left: 1px solid #797676; border-right: 1px solid #797676;">Location
							: ${assessment.account.location.name}</td>
						<td style="padding: 0px 16px;">LOB :
							${assessment.account.lob.name}</td>
					</tr>
				</table>
			</div>
			<%-- <div class="col-xl-10">
				<table class="cTable"
					style="font-size: 13px; background: #f1f0f0; border: 1px solid #343a40; box-shadow: 3px 3px 2px #898f96;">
					<tr style="background: #d4d4d4;">
						<td
							style="background: #007bff; color: #fff; border: 1px solid #343a40;">All
							Controls</td>
						<td
							style="background: #dc3545; color: #fff; border: 1px solid #343a40;">Total
							Risks</td>
						<td
							style="background: #dc3545; color: #fff; border: 1px solid #343a40;">Review
							Pending for NC</td>
						<td
							style="background: #28a745; color: #fff; border: 1px solid #343a40;">Review
							Pending for Non NC</td>
						<td
							style="background: #dc3545; color: #fff; border: 1px solid #343a40;">Review
							Complete for NC</td>
						<td
							style="background: #28a745; color: #fff; border: 1px solid #343a40;">Review
							Complete for Non NC</td>
						<td
							style="background: #dc3545; color: #fff; border: 1px solid #343a40;">More
							info Requested for NC</td>
						<td
							style="background: #28a745; color: #fff; border: 1px solid #343a40;">More
							info Requested for Non NC</td>
					</tr>
					<tr style="background: #fff;">
						<td
							style="color: #007bff; cursor: pointer; border: 1px solid #343a40;">${totalAnswer}</td>
						<td
							style="color: #dc3545; cursor: pointer; border: 1px solid #343a40;"><div
								style="float: right;">
								<i class="fa fa-thumbs-o-down" aria-hidden="true"></i>
							</div>${totalRisks}</td>
						<td
							style="color: #dc3545; cursor: pointer; border: 1px solid #343a40;"><div
								style="float: right;">
								<i class="fa fa-thumbs-down" aria-hidden="true"></i>
							</div>${reviewPendingforNC}</td>
						<td
							style="color: #28a745; cursor: pointer; border: 1px solid #343a40;"><div
								style="float: right;">
								<i class="fa fa-thumbs-o-down" aria-hidden="true"></i>
							</div>${reviewPendingforNonNC}</td>
						<td
							style="color: #dc3545; cursor: pointer; border: 1px solid #343a40;"><div
								style="float: right;">
								<i class="fa fa-thumbs-o-up" aria-hidden="true"></i>
							</div>${reviewCompleteforNC}</td>
						<td
							style="color: #28a745; cursor: pointer; border: 1px solid #343a40;"><div
								style="float: right;">
								<i class="fa fa-thumbs-up" aria-hidden="true"></i>
							</div>${reviewCompleteforNonNC}</td>
						<td
							style="color: #dc3545; cursor: pointer; border: 1px solid #343a40;"><div
								style="float: right;">
								<i class="fa fa-exclamation-triangle" aria-hidden="true"></i>
							</div>${changeRequiredforNC}</td>
						<td
							style="color: #28a745; cursor: pointer; border: 1px solid #343a40;"><div
								style="float: right;">
								<i class="fa fa-exclamation-triangle" aria-hidden="true"></i>
							</div>${changeRequiredforNonNC}</td>
					</tr>
				</table>
			</div> --%>
		</div>
		<div class="dropdown-divider"></div>
		<div class="row">
			<div class="col-xl-12">
				<div class="table-responsive">
					<c:if test="${not empty msg}">
						<div class="card card-login mx-auto mt-5"
							style="margin: auto !important; border: 0; max-width: 34rem;">
							<div class="alert alert-success" style="margin: 0;">${msg}</div>
						</div>
					</c:if>
					<c:if test="${not empty errorMsg}">
						<div class="card card-login mx-auto mt-5"
							style="margin: auto !important; border: 0; max-width: 34rem;">
							<div class="alert alert-danger" style="margin: 0;">${errorMsg}</div>
						</div>
					</c:if>
					<table class="table table-bordered" id="dataTable" width="100%"
						style="font-size: .8rem;" cellspacing="0">
						<thead>
							<tr>
								<th>Id</th>
								<th>Control</th>
								<th>Response</th>
								<th>Mitigate/Accept Risk</th>
								<th>Artifact</th>
								<th>Last Responded By</th>
								<c:if
									test="${userModel.role != 'Admin' && userModel.role != 'Super Admin'}">
									<th>Edit</th>
								</c:if>
								<th>Trail</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${answers}" var="answer">
								<c:set var="i" value="${i+1}" scope="page" />
								<tr>
									<td style="white-space: nowrap;">${i}&nbsp;<a
										class="btn btn-info" data-toggle="tooltip"
										data-placement="right"
										title="${answer.control.control.helpData}"
										style="padding: 0px 6px; cursor: pointer;"><i
											class="fa fa-question" aria-hidden="true"></i></a></td>
									<td>${answer.control.control.control}</td>
									<td>${answer.answer}<c:if
											test="${answer.NC == true && answer.confirmationStatus=='Review Pending'}">
											<i class="fa fa-thumbs-down" aria-hidden="true"
												style="color: #dc3545; float: right; font-size: 17px;"></i>
										</c:if> <c:if
											test="${answer.NC == true && answer.confirmationStatus=='Review Complete'}">
											<i class="fa fa-thumbs-o-up" aria-hidden="true"
												style="color: #dc3545; float: right; font-size: 17px;"></i>
										</c:if> <c:if
											test="${answer.NC == true && answer.confirmationStatus=='Change Required'}">
											<i class="fa fa-exclamation-triangle" aria-hidden="true"
												style="color: #dc3545; float: right; font-size: 17px;"></i>
										</c:if> <c:if
											test="${answer.NC == false && answer.confirmationStatus=='Review Pending'}">
											<i class="fa fa-thumbs-o-down" aria-hidden="true"
												style="color: #28a745; float: right; font-size: 17px;"></i>
										</c:if> <c:if
											test="${answer.NC == false && answer.confirmationStatus=='Review Complete'}">
											<i class="fa fa-thumbs-up" aria-hidden="true"
												style="color: #28a745; float: right; font-size: 17px;"></i>
										</c:if> <c:if
											test="${answer.NC == false && answer.confirmationStatus=='Change Required'}">
											<i class="fa fa-exclamation-triangle" aria-hidden="true"
												style="color: #28a745; float: right; font-size: 17px;"></i>
										</c:if>
									</td>
									<c:if test="${answer.riskAcceptance == true}">
										<td>${answer.riskAcceptenceby}</td>
									</c:if>
									<c:if test="${answer.riskAcceptance == false}">
										<td>${answer.mitigationDate}</td>
									</c:if>
									<td><a href="download/${answer.id}" target="_blank">${answer.artifaceName}</a></td>
									<td>${answer.lastRespondedUser.name}</td>

									<c:if
										test="${userModel.role != 'Admin' && userModel.role != 'Super Admin'}">
										<td style="text-align: center;"><a
											class="btn btn-warning"
											href="riskTrackerResponsePage?answerId=${answer.id}"
											style="padding: 0px 6px; cursor: pointer;"><i
												class="fa fa-pencil-square-o" aria-hidden="true"></i></a></td>
									</c:if>
									<c:if test="${userModel.role == 'Assessor'}">
										<td style="text-align: center;"><a
											class="btn btn-dark historyBtn" data-toggle="modal"
											data-target="#myModal"
											onclick="return showAssessorHistory(${answer.id});"
											style="padding: 0px 6px; cursor: pointer;"><i
												class="fa fa-history" aria-hidden="true"></i></a></td>
									</c:if>
									<c:if test="${userModel.role == 'Approver'}">
										<td style="text-align: center;"><a
											class="btn btn-dark historyBtn" data-toggle="modal"
											data-target="#myModal"
											onclick="return showHistory(${answer.id});"
											style="padding: 0px 6px; cursor: pointer;"><i
												class="fa fa-history" aria-hidden="true"></i></a></td>
									</c:if>
									<c:if test="${userModel.role == 'Reviewer'}">
										<td style="text-align: center;"><a
											class="btn btn-dark historyBtn" data-toggle="modal"
											data-target="#myModal"
											onclick="return showReviewerHistory(${answer.id});"
											style="padding: 0px 6px; cursor: pointer;"><i
												class="fa fa-history" aria-hidden="true"></i></a></td>
									</c:if>
									<c:if test="${userModel.role == 'SME'}">
										<td style="text-align: center;"><a
											class="btn btn-dark historyBtn" data-toggle="modal"
											data-target="#myModal"
											onclick="return showHistorySme(${answer.id});"
											style="padding: 0px 6px; cursor: pointer;"><i
												class="fa fa-history" aria-hidden="true"></i></a></td>
									</c:if>
									<c:if test="${userModel.role == 'Admin'}">
										<td style="text-align: center;"><a
											class="btn btn-dark historyBtn" data-toggle="modal"
											data-target="#myModal"
											onclick="return showHistoryAdmin(${answer.id});"
											style="padding: 0px 6px; cursor: pointer;"><i
												class="fa fa-history" aria-hidden="true"></i></a></td>
									</c:if>
									<c:if test="${userModel.role == 'Super Admin'}">
										<td style="text-align: center;"><a
											class="btn btn-dark historyBtn" data-toggle="modal"
											data-target="#myModal"
											onclick="return showHistoryAdmin(${answer.id});"
											style="padding: 0px 6px; cursor: pointer;"><i
												class="fa fa-history" aria-hidden="true"></i></a></td>
									</c:if>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
	<footer class="sticky-footer" style="width: 100%;">
		<div class="container">
			<div class="text-right">
				<small>� 2017 All Rights Reserved</small>
			</div>
		</div>
	</footer>
	<!-- Scroll to Top Button-->
	<a class="scroll-to-top rounded" href="#page-top"> <i
		class="fa fa-angle-up"></i>
	</a>

	<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
		aria-labelledby="exampleModalLabel" aria-hidden="true">
		<div class="modal-dialog" role="document" style="max-width: 800px;">
			<div class="modal-content">
				<div class="modal-header"
					style="background: #b1afaf; padding: 8px 15px;">
					<h5 class="modal-title" id="exampleModalLabel">Answer History</h5>
					<button class="close" type="button" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">�</span>
					</button>
				</div>
				<div class="modal-body"></div>
				<div class="modal-footer">
					<button class="btn btn-secondary" type="button"
						data-dismiss="modal" style="margin-right: 12px;">Cancel</button>
				</div>
			</div>

		</div>
	</div>

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

	<script>
	 function showHistory(answerId){
		 $('.modal-body').load(
					'${contextRoot}/approver/history?answerId=' + answerId,
					function() {
						
						$('#myModal').modal({
							show : true
						});$('#dataTable1').DataTable();
					});
		 
	 } 
	 
	 function showAssessorHistory(answerId){
		 $('.modal-body').load(
					'${contextRoot}/assessor/history?answerId=' + answerId,
					function() {
						
						$('#myModal').modal({
							show : true
						});$('#dataTable1').DataTable();
					});
		 
	 } 
	 
	 function showReviewerHistory(answerId){
		 $('.modal-body').load(
					'${contextRoot}/reviewer/history?answerId=' + answerId,
					function() {
						
						$('#myModal').modal({
							show : true
						});$('#dataTable1').DataTable();
					});
		 
	 } 
	 
	 function showHistorySme(answerId){
		 $('.modal-body').load(
					'${contextRoot}/sme/history?answerId=' + answerId,
					function() {
						
						$('#myModal').modal({
							show : true
						});
					});
		 $('#dataTable1').DataTable();
	 }
	 
	 function showHistoryAdmin(answerId){
		 $('.modal-body').load(
					'${contextRoot}/admin/history?answerId=' + answerId,
					function() {
						
						$('#myModal').modal({
							show : true
						});
					});
		 $('#dataTable1').DataTable();
	 }
	</script>
</body>

</html>

