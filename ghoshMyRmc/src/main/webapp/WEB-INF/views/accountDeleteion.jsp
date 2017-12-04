<%@taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>

<div class="container-fluid">
	<!-- Example DataTables Card-->
	<div class="card mb-3">
		<div class="card-header">
			<i class="fa fa-table"></i> Accounts
		</div>
		<div class="card-body">
			<c:if test="${not empty msg}">
				<div class="card card-login mx-auto mt-5"
					style="margin: auto !important; border: 0;">
					<div class="alert alert-success" style="margin: 0;">${msg}</div>
				</div>
			</c:if>
			<c:if test="${not empty errorMsg}">
				<div class="card card-login mx-auto mt-5"
					style="margin: auto !important; border: 0;">
					<div class="alert alert-danger" style="margin: 0;">${errorMsg}</div>
				</div>
			</c:if>
			<div class="table-responsive">
				<table class="table table-bordered" id="dataTable" width="100%"
					style="font-size: .9rem;" cellspacing="0">
					<thead>
						<tr>
							<th style="width: 20px;">ID</th>
							<th>Department</th>
							<th>Location</th>
							<th>LOB</th>
							<th>Country</th>
							<th>Sector</th>
							<th>Phase</th>
							<th>Initial Rating</th>
							<th style="width: 26px;">Edit</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${accounts}" var="account">
							<c:set var="i" value="${i+1}" scope="page" />
							<tr>
								<td>${i}.</td>
								<td>${account.department.name}</td>
								<td>${account.location.name}</td>
								<td>${account.lob.name}</td>
								<td>${account.country.name}</td>
								<td>${account.sector}</td>
								<td>${account.phase}</td>
								<td>${account.initialRating}</td>
								<td style="text-align: center;"><a class="btn btn-warning"
									onclick="window.location.href='${contextRoot}/admin/deleteAccount?accountId=${account.id}'"
									style="padding: 0px 6px; cursor: pointer;"><i
										class="fa fa-trash-o" aria-hidden="true"></i></a></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>
