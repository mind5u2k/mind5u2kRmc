<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<c:set var="contextRoot" value="${pageContext.request.contextPath}"></c:set>
<div class="container-fluid" style="padding-top: 0;">
	<div class="row" style="text-align: center; padding-bottom: 14px;">
		<div class="col-xl-12" style="font-size: .9rem;">
			<h6>Control Statement</h6>${answer.control.control.control}</div>
	</div>
	<div class="row">
		<div class="col-xl-12">
			<div class="table-responsive">
				<table class="table table-bordered" id="dataTable1" width="100%"
					style="font-size: .8rem;" cellspacing="0">
					<thead>
						<tr>
							<th>Id</th>
							<th>Response</th>
							<th>Comment</th>
							<th>Artifact</th>
							<th>Last Responded By</th>
							<th>Date</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${answerTrails}" var="answerTrail">
							<c:set var="i" value="${i+1}" scope="page" />
							<tr>
								<td style="white-space: nowrap;">${i}</td>
								<td>${answerTrail.answer}</td>
								<td>${answerTrail.comment}</td>
								<c:if
									test="${userModel.role == 'Admin' || userModel.role == 'Super Admin'}">
									<c:if test="${empty answerTrail.artifact}">
										<td>${answerTrail.artifaceName}</td>
									</c:if>
									<c:if test="${not empty answerTrail.artifact}">
										<td><a
											href="${contextRoot}/admin/downloadAnswerTrail/${answerTrail.id}"
											target="_blank">${answerTrail.artifaceName}</a></td>
									</c:if>
								</c:if>
								<c:if test="${userModel.role == 'Assessor'}">
									<c:if test="${empty answerTrail.artifact}">
										<td>${answerTrail.artifaceName}</td>
									</c:if>
									<c:if test="${not empty answerTrail.artifact}">
										<td><a
											href="${contextRoot}/assessor/downloadAnswerTrail/${answerTrail.id}"
											target="_blank">${answerTrail.artifaceName}</a></td>
									</c:if>
								</c:if>
								<c:if test="${userModel.role == 'Approver'}">
									<c:if test="${empty answerTrail.artifact}">
										<td>${answerTrail.artifaceName}</td>
									</c:if>
									<c:if test="${not empty answerTrail.artifact}">
										<td><a
											href="${contextRoot}/approver/downloadAnswerTrail/${answerTrail.id}"
											target="_blank">${answerTrail.artifaceName}</a></td>
									</c:if>
								</c:if>
								<c:if test="${userModel.role == 'Reviewer'}">
									<c:if test="${empty answerTrail.artifact}">
										<td>${answerTrail.artifaceName}</td>
									</c:if>
									<c:if test="${not empty answerTrail.artifact}">
										<td><a
											href="${contextRoot}/reviewer/downloadAnswerTrail/${answerTrail.id}"
											target="_blank">${answerTrail.artifaceName}</a></td>
									</c:if>
								</c:if>
								<c:if test="${userModel.role == 'SME'}">
									<c:if test="${empty answerTrail.artifact}">
										<td>${answerTrail.artifaceName}</td>
									</c:if>
									<c:if test="${not empty answerTrail.artifact}">
										<td><a
											href="${contextRoot}/sme/downloadAnswerTrail/${answerTrail.id}"
											target="_blank">${answerTrail.artifaceName}</a></td>
									</c:if>
								</c:if>
								<td>${answerTrail.lastRespondedUser.name}</td>
								<td>${answerTrail.dateAnswered}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>

<script>
	$('#dataTable1').DataTable();
</script>
