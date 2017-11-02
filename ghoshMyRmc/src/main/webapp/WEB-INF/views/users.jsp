<%@taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>

<div class="container-fluid">
	<!-- Example DataTables Card-->
	<div class="card mb-3">
		<div class="card-header">
			<i class="fa fa-table"></i> Manage Users<a class="btn btn-warning"
				href="${contextRoot}/admin/addNewUser"
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
							<th>Name</th>
							<th>Email</th>
							<th>Role</th>
							<th>State</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${users}" var="user">
							<c:set var="i" value="${i+1}" scope="page" />
							<tr>
								<td>${i}.</td>
								<td>${user.name}</td>
								<td>${user.email}</td>
								<td>${user.role}</td>
								<td>${user.state}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>
<div class="modal fade" id="addNewUser" tabindex="-1" role="dialog"
	aria-labelledby="exampleModalLabel" aria-hidden="true">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header"
				style="background: #b1afaf; padding: 8px 15px;">
				<h5 class="modal-title" id="exampleModalLabel">Add New User</h5>
				<button class="close" type="button" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">×</span>
				</button>
			</div>
			<sf:form modelAttribute="user" id="userForm"
				action="${contextRoot}/admin/addNewUser" method="POST">
				<div class="modal-body">
					<div class="form-group row">
						<label for="example-text-input" class="col-2 col-form-label">Name</label>
						<div class="col-10">
							<sf:input class="form-control" type="text"
								placeholder="User Name" id="name" path="name" />
						</div>
					</div>
					<div class="form-group row">
						<label for="example-text-input" class="col-2 col-form-label">Email</label>
						<div class="col-10">

							<sf:input class="form-control" type="text"
								placeholder="Email Address" id="email" path="email" />
						</div>
					</div>
					<div class="form-group row">
						<label for="example-text-input" class="col-2 col-form-label">Role</label>
						<div class="col-10">
							<sf:select class="form-control" id="role" path="role"
								items="${allRoles}" />
						</div>
					</div>
					<div class="form-group row">
						<label for="example-text-input" class="col-2 col-form-label">State</label>
						<div class="col-10">
							<sf:select class="form-control" id="state" path="state"
								items="${categoryState}" />
						</div>
					</div>
				</div>
				<div class="modal-footer">
					<button class="btn btn-secondary" type="button"
						data-dismiss="modal" style="margin-right: 12px;">Cancel</button>
					<input class="btn btn-primary" type="submit"
						style="margin-right: 12px;" value="Submit" />
					<sf:hidden path="id" />
					<sf:hidden path="password" />
				</div>
			</sf:form>
		</div>

	</div>
</div>
