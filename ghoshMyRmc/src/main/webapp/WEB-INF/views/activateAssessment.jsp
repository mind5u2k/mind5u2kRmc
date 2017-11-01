<%@taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>

<div class="container-fluid">
	<!-- Example DataTables Card-->
	<div class="card mb-3">
		<div class="card-header">
			<i class="fa fa-table"></i> Activate Assessment
		</div>
		<div class="card-body">
			<c:if test="${not empty msg}">
				<div class="card card-login mx-auto mt-5"
					style="margin: auto !important; border: 0;">
					<div class="alert alert-success" style="margin: 0;">${msg}</div>
				</div>
			</c:if>
			<div class="table-responsive">
				<table class="table table-bordered" id="dataTable" width="100%"
					style="font-size: .9rem;" cellspacing="0">
					<thead>
						<tr>
							<th>ID</th>
							<th>Department</th>
							<th>Location</th>
							<th>LOB</th>
							<th>Country</th>
							<th>Sector</th>
							<th>Phase</th>
							<th>Initial Rating</th>
							<th>State</th>
							<th>Edit</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${assessments}" var="assessment">
							<c:set var="i" value="${i+1}" scope="page" />
							<tr>
								<td>${i}.</td>
								<td>${assessment.account.department.name}</td>
								<td>${assessment.account.location.name}</td>
								<td>${assessment.account.lob.name}</td>
								<td>${assessment.account.country.name}</td>
								<td>${assessment.account.sector}</td>
								<td>${assessment.account.phase}</td>
								<td>${assessment.account.initialRating}</td>
								<td>${assessment.account.state}</td>
								<td><a
									href="${contextRoot}/admin/editAssessment?accId=${assessment.account.id}"
									class="btn btn-dark" style="padding: 0px 6px; cursor: pointer;"><i
										class="fa fa-angle-double-right" aria-hidden="true"></i><i
										class="fa fa-angle-double-right" aria-hidden="true"></i></a></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>
