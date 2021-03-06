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
	<%@include file="./reviewerShared/header.jsp"%>
	<div class="container-fluid" style="padding: 18px;">
		<div class="row">
			<div class="col-xl-2"></div>
			<div class="col-xl-8" style="text-align: center;">
				<h5>
					Control Statement<br>
				</h5>
				<h6>${answer.control.control.control}</h6>
			</div>
		</div>
		<sf:form modelAttribute="answer" id="answerForm"
			action="${contextRoot}/${userRole}/saveRiskTrackerAnswer"
			method="POST" enctype="multipart/form-data">
			<div class="modal-dialog" role="document" style="max-width: 650px;">
				<div class="modal-content">
					<div class="modal-body">
						<div class="row">
							<div class="col-xl-12">
								<div class="form-group row">
									<label for="inputEmail3" class="col-sm-3 col-form-label">Response
										Options</label>
									<div class="col-sm-9">
										<sf:select class="form-control" id="answer" path="answer"
											items="${answerOpions}" disabled="true" />
									</div>
								</div>
								<div class="form-group row">
									<label for="inputEmail3" class="col-sm-3 col-form-label">Comment</label>
									<div class="col-sm-9">
										<sf:textarea cssClass="form-control" id="comment"
											path="comment" disabled="true" />
									</div>
								</div>

								<div class="form-group row">
									<label for="inputEmail3" class="col-sm-3 col-form-label">Artifact</label>
									<div class="col-sm-9">
										<c:if test="${empty answer.artifact}">
											--
										</c:if>
										<c:if test="${not empty answer.artifact}">
											<input type="file" class="form-control" name="file" id="file"
												style="display: none;"></input>
											<a href="${contextRoot}/${userRole}/download/${answer.id}"
												target="_blank">${answer.artifaceName}</a>
										</c:if>
									</div>

								</div>
								<div class="form-group row" style="margin-bottom: 0;">
									<label for="inputEmail3" class="col-sm-3 col-form-label">Mitigation
										Date / Accept Risk </label>
									<c:if test="${answer.riskAcceptance == true}">
										<label for="inputEmail3" class="col-sm-9 col-form-label">${answer.riskAcceptenceby}</label>
									</c:if>
									<c:if test="${answer.riskAcceptance == false}">
										<c:if test="${answer.mitigationDate == null}">
											<label for="inputEmail3" class="col-sm-9 col-form-label">--</label>
										</c:if>
										<c:if test="${answer.mitigationDate == null}">
											<label for="inputEmail3" class="col-sm-9 col-form-label">${answer.mitigationDate}</label>
										</c:if>
									</c:if>
								</div>
								<div class="dropdown-divider"></div>
								<div class="form-group row" style="margin-top: 13px;">
									<label for="inputEmail3" class="col-sm-3 col-form-label">Reviewer
										Comment</label>
									<div class="col-sm-9">
										<sf:textarea cssClass="form-control" id="reviewerComment"
											name="reviewerComment" path="reviewerComment" />
									</div>
								</div>
								<div class="form-group row">
									<label for="inputEmail3" class="col-sm-3 col-form-label">Confirmation</label>
									<div class="col-sm-9">
										<sf:radiobutton name="confirmationStatus"
											path="confirmationStatus" value="Review Pending" />
										Review Pending<br>
										<sf:radiobutton name="confirmationStatus"
											path="confirmationStatus" value="Review Complete" />
										Review Complete<br>
										<sf:radiobutton name="confirmationStatus"
											path="confirmationStatus" value="Change Required" />
										Change Required<br>
									</div>

								</div>

							</div>
						</div>
					</div>
					<div class="modal-footer">
						<button class="btn btn-secondary" type="button"
							onclick="window.location.href='${contextRoot}/reviewer/riskTracker?assessmentId=${answer.control.assessmentCategories.assessment.id}&catId=${answer.control.assessmentCategories.id}'"
							style="margin-right: 12px;">Cancel</button>
						<input class="btn btn-primary" type="submit"
							style="margin-right: 12px;" value="Save Response" />
						<sf:hidden path="id" />
					</div>
				</div>
			</div>
		</sf:form>
	</div>
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
	<script>
		$(function() {
			$('#riskAcceptance').click(function() {
				if ($(this).is(':checked')) {
					$('#mitigationDate').prop("disabled", true);
				} else {
					$('#mitigationDate').prop("disabled", false);
				}
			});

		});
	</script>
</body>

</html>

