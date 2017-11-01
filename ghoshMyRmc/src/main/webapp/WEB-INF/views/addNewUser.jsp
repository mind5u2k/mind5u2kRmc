<%@taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>

<div class="container-fluid">
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
				action="${contextRoot}/admin/saveNewUser" method="POST">
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
							<sf:errors path="email" cssClass="help-block" element="em" />
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
						onclick="window.location.href='${contextRoot}/admin/users'"
						style="margin-right: 12px;">Cancel</button>
					<input class="btn btn-primary" type="submit"
						style="margin-right: 12px;" value="Submit" />
					<sf:hidden path="id" />
					<sf:hidden path="password" />
				</div>
			</sf:form>
		</div>

	</div>
</div>
