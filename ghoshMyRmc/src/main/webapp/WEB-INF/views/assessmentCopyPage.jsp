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
		<div class="row">
			<div class="col-xl-8">
				<div class="btn-group">
					<button type="button" class="btn btn-primary dropdown-toggle"
						data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
						${selectedAssessmentCategory.assignedCategories.name} - [
						${selectedAssessmentCategory.status} ]&nbsp;&nbsp;</button>
					<div class="dropdown-menu">
						<c:forEach items="${assessmentCategories}"
							var="assessmentCategory">
							<c:if test="${userModel.role == 'Assessor'}">
								<a class="dropdown-item"
									href="${contextRoot}/assessor/assessmentPage?assessmentId=${assessmentCategory.assessment.id}&catId=${assessmentCategory.id}">${assessmentCategory.assignedCategories.name}
									- [ ${assessmentCategory.status} ]</a>
							</c:if>
							<c:if test="${userModel.role == 'Approver'}">
								<a class="dropdown-item"
									href="${contextRoot}/approver/assessmentPage?assessmentId=${assessmentCategory.assessment.id}&catId=${assessmentCategory.id}">${assessmentCategory.assignedCategories.name}
									- [ ${assessmentCategory.status} ]</a>
							</c:if>
							<c:if test="${userModel.role == 'Reviewer'}">
								<a class="dropdown-item"
									href="${contextRoot}/reviewer/assessmentPage?assessmentId=${assessmentCategory.assessment.id}&catId=${assessmentCategory.id}">${assessmentCategory.assignedCategories.name}
									- [ ${assessmentCategory.status} ]</a>
							</c:if>
							<c:if test="${userModel.role == 'SME'}">
								<a class="dropdown-item"
									href="${contextRoot}/sme/assessmentPage?assessmentId=${assessmentCategory.assessment.id}&catId=${assessmentCategory.id}">${assessmentCategory.assignedCategories.name}
									- [ ${assessmentCategory.status} ]</a>
							</c:if>
							<c:if test="${userModel.role == 'Admin'}">
								<a class="dropdown-item"
									href="${contextRoot}/admin/assessmentPage?assessmentId=${assessmentCategory.assessment.id}&catId=${assessmentCategory.id}">${assessmentCategory.assignedCategories.name}
									- [ ${assessmentCategory.status} ]</a>
							</c:if>
							<c:if test="${userModel.role == 'Super Admin'}">
								<a class="dropdown-item"
									href="${contextRoot}/admin/assessmentPage?assessmentId=${assessmentCategory.assessment.id}&catId=${assessmentCategory.id}">${assessmentCategory.assignedCategories.name}
									- [ ${assessmentCategory.status} ]</a>
							</c:if>
						</c:forEach>
					</div>
				</div>
			</div>
			<div class="col-xl-4" style="text-align: right;">Assessment
				Status : ${assessment.assessmentStatus}</div>
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
								<th>Comment</th>
								<th>Artifact</th>
								<th>Last Responded By</th>
								<th>Trail</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${answerModels}" var="answerModel">
								<c:set var="i" value="${i+1}" scope="page" />
								<tr>
									<td style="white-space: nowrap;">${i}&nbsp;<a
										class="btn btn-info" data-toggle="tooltip"
										data-placement="right"
										title="${answerModel.answerCopy.control.control.helpData}"
										style="padding: 0px 6px; cursor: pointer;"><i
											class="fa fa-question" aria-hidden="true"></i></a></td>
									<td>${answerModel.answerCopy.control.control.control}</td>
									<c:if test="${answerModel.status==false}">
										<td class="btn-danger" style="text-align: center;">! Not
											Responded</td>
									</c:if>
									<c:if test="${answerModel.status==true}">
										<td>${answerModel.answerCopy.answer}</td>
									</c:if>
									<td>${answerModel.answerCopy.comment}</td>

									<c:if test="${userModel.role == 'Assessor'}">
										<td><a
											href="${contextRoot}/assessor/download/${answerModel.answerCopy.answerId}"
											target="_blank">${answerModel.answerCopy.artifaceName}</a></td>
									</c:if>
									<c:if test="${userModel.role == 'Approver'}">
										<td><a
											href="${contextRoot}/approver/download/${answerModel.answerCopy.answerId}"
											target="_blank">${answerModel.answerCopy.artifaceName}</a></td>
									</c:if>
									<c:if test="${userModel.role == 'Reviewer'}">
										<td><a
											href="${contextRoot}/reviewer/download/${answerModel.answerCopy.answerId}"
											target="_blank">${answerModel.answerCopy.artifaceName}</a></td>
									</c:if>
									<c:if test="${userModel.role == 'SME'}">
										<td><a
											href="${contextRoot}/sme/download/${answerModel.answerCopy.answerId}"
											target="_blank">${answerModel.answerCopy.artifaceName}</a></td>
									</c:if>
									<c:if test="${userModel.role == 'Admin'}">
										<td><a
											href="${contextRoot}/admin/download/${answerModel.answerCopy.answerId}"
											target="_blank">${answerModel.answerCopy.artifaceName}</a></td>
									</c:if>
									<c:if test="${userModel.role == 'Super Admin'}">
										<td><a
											href="${contextRoot}/admin/download/${answerModel.answerCopy.answerId}"
											target="_blank">${answerModel.answerCopy.artifaceName}</a></td>
									</c:if>

									<td>${answerModel.answerCopy.lastRespondedUser.name}</td>

									<c:if test="${userModel.role == 'Assessor'}">
										<td style="text-align: center;"><a
											class="btn btn-dark historyBtn" data-toggle="modal"
											data-target="#myModal"
											onclick="return showHistoryAssessor(${answerModel.answerCopy.answerId});"
											style="padding: 0px 6px; cursor: pointer;"><i
												class="fa fa-history" aria-hidden="true"></i></a></td>
									</c:if>
									<c:if test="${userModel.role == 'Approver'}">
										<td style="text-align: center;"><a
											class="btn btn-dark historyBtn" data-toggle="modal"
											data-target="#myModal"
											onclick="return showHistory(${answerModel.answerCopy.answerId});"
											style="padding: 0px 6px; cursor: pointer;"><i
												class="fa fa-history" aria-hidden="true"></i></a></td>
									</c:if>
									<c:if test="${userModel.role == 'Reviewer'}">
										<td style="text-align: center;"><a
											class="btn btn-dark historyBtn" data-toggle="modal"
											data-target="#myModal"
											onclick="return showHistoryReviewer(${answerModel.answerCopy.answerId});"
											style="padding: 0px 6px; cursor: pointer;"><i
												class="fa fa-history" aria-hidden="true"></i></a></td>
									</c:if>
									<c:if test="${userModel.role == 'SME'}">
										<td style="text-align: center;"><a
											class="btn btn-dark historyBtn" data-toggle="modal"
											data-target="#myModal"
											onclick="return showHistorySme(${answerModel.answerCopy.answerId});"
											style="padding: 0px 6px; cursor: pointer;"><i
												class="fa fa-history" aria-hidden="true"></i></a></td>
									</c:if>
									<c:if test="${userModel.role == 'Admin'}">
										<td style="text-align: center;"><a
											class="btn btn-dark historyBtn" data-toggle="modal"
											data-target="#myModal"
											onclick="return showHistoryAdmin(${answerModel.answerCopy.answerId});"
											style="padding: 0px 6px; cursor: pointer;"><i
												class="fa fa-history" aria-hidden="true"></i></a></td>
									</c:if>
									<c:if test="${userModel.role == 'Super Admin'}">
										<td style="text-align: center;"><a
											class="btn btn-dark historyBtn" data-toggle="modal"
											data-target="#myModal"
											onclick="return showHistoryAdmin(${answerModel.answerCopy.answerId});"
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
				<small>© 2017 All Rights Reserved</small>
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
						<span aria-hidden="true">×</span>
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
						});
					});
		 $('#dataTable1').DataTable();
	 }
	 
	 function showHistoryAssessor(answerId){
		 $('.modal-body').load(
					'${contextRoot}/assessor/history?answerId=' + answerId,
					function() {
						
						$('#myModal').modal({
							show : true
						});
					});
		 $('#dataTable1').DataTable();
	 } 
	 
	 function showHistoryReviewer(answerId){
		 $('.modal-body').load(
					'${contextRoot}/reviewer/history?answerId=' + answerId,
					function() {
						
						$('#myModal').modal({
							show : true
						});
					});
		 $('#dataTable1').DataTable();
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

