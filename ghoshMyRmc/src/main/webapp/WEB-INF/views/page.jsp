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

<body class="fixed-nav sticky-footer bg-dark" id="page-top">
	<!-- Navigation-->
	<%@include file="./adminShared/sideBar.jsp"%>
	<div class="content-wrapper">
		<c:if test="${userClickCategory==true}">
			<%@include file="./categories.jsp"%>
		</c:if>
		<c:if test="${userClickControls==true}">
			<%@include file="./controls.jsp"%>
		</c:if>
		<c:if test="${userClickRoles==true}">
			<%@include file="./roles.jsp"%>
		</c:if>
		<c:if test="${userClickusers==true}">
			<%@include file="./users.jsp"%>
		</c:if>
		<c:if test="${userClickAddNewUsers==true}">
			<%@include file="./addNewUser.jsp"%>
		</c:if>
		<c:if test="${userClickDepartment==true}">
			<%@include file="./department.jsp"%>
		</c:if>
		<c:if test="${userClickLocation==true}">
			<%@include file="./location.jsp"%>
		</c:if>
		<c:if test="${userClickLob==true}">
			<%@include file="./lob.jsp"%>
		</c:if>
		<c:if test="${userClickCountry==true}">
			<%@include file="./country.jsp"%>
		</c:if>
		<c:if test="${userClickAccount==true}">
			<%@include file="./account.jsp"%>
		</c:if>
		<c:if test="${userClickActivateAssessment==true}">
			<%@include file="./activateAssessment.jsp"%>
		</c:if>
		<c:if test="${userClickEditCreateActivateAssessment==true}">
			<%@include file="./editAssessmentPage.jsp"%>
		</c:if>
		<c:if test="${userClickNewActivateAssessment==true}">
			<%@include file="./addApproverAndAssessor.jsp"%>
		</c:if>
		<c:if test="${userClickAddCategoryAssessment==true}">
			<%@include file="./addAssessmentCategories.jsp"%>
		</c:if>
		<c:if test="${userClickAddSmeForAssessment==true}">
			<%@include file="./addSmeforAssessmentCategory.jsp"%>
		</c:if>
		<c:if test="${userClickAccSpecificControls==true}">
			<%@include file="./accountSpecificControls.jsp"%>
		</c:if>
		<c:if test="${userClickeditAccSpecControl==true}">
			<%@include file="./editAccSpecControl.jsp"%>
		</c:if>
		<c:if test="${userClickAccountTransfer==true}">
			<%@include file="./accountTransfer.jsp"%>
		</c:if>
		<c:if test="${userClickAccountDeletion==true}">
			<%@include file="./accountDeleteion.jsp"%>
		</c:if>
		<c:if test="${userClickReporting==true}">
			<%@include file="./reporting.jsp"%>
		</c:if>
		<c:if test="${userClickMitigationMail==true}">
			<%@include file="./mitigationMail.jsp"%>
		</c:if>
		<c:if test="${userClickAssessmentMail==true}">
			<%@include file="./assessmentMail.jsp"%>
		</c:if>
	</div>
	<%@include file="./adminShared/footer.jsp"%>

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
	<%-- 	<script src="${js}/sb-admin-charts.min.js"></script> --%>

	<script src="${js}/myapp.js"></script>
</body>

</html>

