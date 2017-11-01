<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<c:set var="contextRoot" value="${pageContext.request.contextPath}"></c:set>
<div class="container-fluid" style="padding-top: 0;">
	<div class="row" style="text-align: center; padding-bottom: 14px;">
		<div class="col-xl-12" style="font-size: .9rem;">
			<h6>Assessment Submission Trail</h6>
		</div>
	</div>
	<div class="row">
		<div class="col-xl-12">
			<div class="table-responsive">
				<table class="table table-bordered" id="dataTable1" width="100%"
					style="font-size: .8rem;" cellspacing="0">
					<thead>
						<tr>
							<th>Id</th>
							<th>Declaration</th>
							<th>Submitted By</th>
							<th>Date of Submission</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${assessmentTrails}" var="assessmentTrail">
							<c:set var="i" value="${i+1}" scope="page" />
							<tr>
								<td style="white-space: nowrap;">${i}</td>
								<td>${assessmentTrail.declaration}</td>
								<td>${assessmentTrail.submittedUser.name}</td>
								<td>${assessmentTrail.dateOfSubmission}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>
