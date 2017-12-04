<%@taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>

<div class="container-fluid">
	<!-- Example DataTables Card-->
	<div class="card mb-3">
		<div class="card-header" style="padding: 0px;">
			<i class="fa fa-table" style="padding: 10px;"></i> Reporting <a
				target="_blank" href="downloadAllAssessmentDetails"
				class="btn btn-primary" style="float: right; padding: 7px 11px;">
				<i class="fa fa-download" aria-hidden="true"></i> &nbsp;Export
			</a>
		</div>
		<div class="card-body">
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
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>
