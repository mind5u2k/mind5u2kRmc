<%@taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>

<div class="container-fluid">
	<!-- Example DataTables Card-->
	<div class="card mb-3">
		<div class="card-header">
			<i class="fa fa-table"></i> Accounts <a class="btn btn-warning"
				data-toggle="modal" data-target="#addnewAccount"
				style="float: right; padding: 2px 11px; cursor: pointer; font-size: 14px;">Add
				New </a>
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
									onclick="editAccount(${account.id});"
									style="padding: 0px 6px; cursor: pointer;"><i
										class="fa fa-pencil-square-o" aria-hidden="true"></i></a></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>

<div class="modal fade" id="addnewAccount" tabindex="-1" role="dialog"
	aria-labelledby="exampleModalLabel" aria-hidden="true">
	<div class="modal-dialog" role="document" style="max-width: 800px;">
		<div class="modal-content">
			<div class="modal-header"
				style="background: #b1afaf; padding: 8px 15px;">
				<h5 class="modal-title" id="exampleModalLabel">Add New Account</h5>
				<button class="close" type="button" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">×</span>
				</button>
			</div>

			<sf:form modelAttribute="account" id="accountForm"
				action="${contextRoot}/admin/addNewAccount" method="POST">
				<div class="modal-body">
					<div class="form-group row">
						<label for="example-text-input" class="col-2 col-form-label">Department</label>
						<div class="col-4">
							<sf:select class="form-control" id="department.id"
								path="department.id" items="${departmentList}" itemLabel="name"
								itemValue="id" />
						</div>
						<label for="example-text-input" class="col-2 col-form-label">Location</label>
						<div class="col-4">
							<sf:select class="form-control" id="location.id"
								path="location.id" items="${locationList}" itemLabel="name"
								itemValue="id" />
						</div>
					</div>
					<div class="form-group row">
						<label for="example-text-input" class="col-2 col-form-label">LOB</label>
						<div class="col-4">
							<sf:select class="form-control" id="lob.id" path="lob.id"
								items="${lobList}" itemLabel="name" itemValue="id" />
						</div>
						<label for="example-text-input" class="col-2 col-form-label">Country</label>
						<div class="col-4">
							<sf:select class="form-control" id="country.id" path="country.id"
								items="${countryList}" itemLabel="name" itemValue="id" />
						</div>
					</div>
					<div class="form-group row">
						<label for="example-text-input" class="col-2 col-form-label">Sector</label>
						<div class="col-4">
							<sf:select class="form-control" id="sector" path="sector"
								items="${sectorList}" />
						</div>
						<label for="example-text-input" class="col-2 col-form-label">Phase</label>
						<div class="col-4">
							<sf:select class="form-control" id="phase" path="phase"
								items="${phaseList}" />
						</div>
					</div>
					<div class="form-group row">
						<div class="col-4"></div>
						<label for="example-text-input" class="col-2 col-form-label">Initial
							Rating</label>
						<div class="col-2">
							<sf:select class="form-control" id="initialRating"
								path="initialRating" items="${initialRatingList}" />
						</div>
						<div class="col-4"></div>
					</div>
				</div>
				<div class="modal-footer">
					<button class="btn btn-secondary" type="button"
						data-dismiss="modal" style="margin-right: 12px;">Cancel</button>
					<input class="btn btn-primary" type="submit"
						style="margin-right: 12px;" value="Submit" />
					<sf:hidden path="id" />
					<sf:hidden path="state" />
				</div>
			</sf:form>
		</div>

	</div>
</div>
<div class="modal fade" id="editAccountModel" tabindex="-1"
	role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
	<div class="modal-dialog" role="document" style="max-width: 800px;">
		<div class="modal-content">
			<div class="modal-header"
				style="background: #b1afaf; padding: 8px 15px;">
				<h5 class="modal-title" id="exampleModalLabel">Edit Country</h5>
				<button class="close" type="button" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">×</span>
				</button>
			</div>
			<div class="modal-body editAccountModelBody"></div>
		</div>

	</div>
</div>

<script>
	function editAccount(accountId) { 
		$('.editAccountModelBody').load('${contextRoot}/admin/editAccount?accountId=' + accountId,
				function() {

					$('#editAccountModel').modal({
						show : true
					});
				});
		$('#dataTable1').DataTable();
	}
</script>
