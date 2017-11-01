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
	<c:if test="${userModel.role == 'Assessor'}">
		<%@include file="./assessorShared/header.jsp"%>
	</c:if>
	<c:if test="${userModel.role == 'Approver'}">
		<%@include file="./approverShared/header.jsp"%>
	</c:if>
	<c:if test="${userModel.role == 'SME'}">
		<%@include file="./smeShared/header.jsp"%>
	</c:if>
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
		<c:if test="${userModel.role == 'Assessor'}">
			<%@include file="./assessorShared/header.jsp"%>
		</c:if>
		<c:if test="${userModel.role == 'Approver'}">
			<%@include file="./approverShared/header.jsp"%>
		</c:if>
		<sf:form modelAttribute="answer" id="answerForm"
			action="${contextRoot}/${userRole}/saveAnswer" method="POST"
			enctype="multipart/form-data">
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
											items="${answerOpions}" />
									</div>
								</div>
								<div class="form-group row">
									<label for="inputEmail3" class="col-sm-3 col-form-label">Comment</label>
									<div class="col-sm-9">
										<sf:textarea cssClass="form-control" id="comment"
											path="comment" />
									</div>
								</div>
								<div class="form-group row">
									<label for="inputEmail3" class="col-sm-3 col-form-label">Reviewer
										Comment</label>
									<div class="col-sm-9">
										<sf:textarea cssClass="form-control" id="reviewerComment"
											path="reviewerComment" disabled="true" />
									</div>
								</div>

								<div class="form-group row">
									<label for="inputEmail3" class="col-sm-3 col-form-label">Artifact</label>
									<div class="col-sm-9">
										<c:if test="${empty answer.artifact}">
											<input type="file" class="form-control" name="file" id="file"></input>
										</c:if>
										<c:if test="${not empty answer.artifact}">
											<input type="file" class="form-control" name="file" id="file"
												style="display: none;"></input>

											<c:if test="${userModel.role == 'Assessor'}">
												<a href="${contextRoot}/assessor/download/${answer.id}"
													target="_blank">${answer.artifaceName}</a>
												<input type="button" class="btn btn-primary"
													style="float: right;" value="Delete"
													onclick="window.location.href='${contextRoot}/assessor/deleteAssessmentArtifact?answerId=${answer.id}'" />
											</c:if>
											<c:if test="${userModel.role == 'Approver'}">
												<a href="${contextRoot}/approver/download/${answer.id}"
													target="_blank">${answer.artifaceName}</a>
												<input type="button" class="btn btn-primary"
													style="float: right;" value="Delete"
													onclick="window.location.href='${contextRoot}/approver/deleteAssessmentArtifact?answerId=${answer.id}'" />
											</c:if>
											<c:if test="${userModel.role == 'SME'}">
												<a href="${contextRoot}/sme/download/${answer.id}"
													target="_blank">${answer.artifaceName}</a>
												<input type="button" class="btn btn-primary"
													style="float: right;" value="Delete"
													onclick="window.location.href='${contextRoot}/sme/deleteAssessmentArtifact?answerId=${answer.id}'" />
											</c:if>
										</c:if>
									</div>

								</div>
							</div>
						</div>
					</div>
					<div class="modal-footer">
						<button class="btn btn-secondary" type="button"
							onclick="window.location.href='${contextRoot}/${userRole}/assessmentPage?assessmentId=${answer.control.assessmentCategories.assessment.id}&catId=${answer.control.assessmentCategories.id}'"
							style="margin-right: 12px;">Cancel</button>
						<input class="btn btn-primary" type="submit"
							onsubmit="return checkAnswer();" style="margin-right: 12px;"
							value="Save Response" />
						<sf:hidden path="id" />
						<sf:hidden path="control.id" />
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
		function checkAnswer() {
			alert($("#answer").val());
			if ($("#answer").val() == "No" && $("#comment").val() == "") {
				alert("hahahaha");
				return false;
			}
		}
	</script>
</body>

</html>

