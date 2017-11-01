<%@taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<style>
input {
	border: 0;
}
</style>
<sf:form modelAttribute="assessmentModel" id="assessmentForm"
	action="${contextRoot}/admin/addNewAssessment" method="POST">
	<div class="container-fluid">

		<!-- Example DataTables Card-->
		<div class="card mb-3">
			<div class="card-header">
				<i class="fa fa-table"></i> &nbsp;Assessment
			</div>
			<div class="card-body">
				<div class="row">
					<div class="col-xl-3" style="border-right: 1px solid #ccc;">
						<div class=" " aria-labelledby="alertsDropdown">
							<h6 class="dropdown-header"
								style="text-align: center; font-size: initial; color: #525558;">Account
								Info:</h6>
							<div class="dropdown-divider"></div>
							<a class="dropdown-item" href="#"> <span class="text-success">
									<strong> Department </strong>
							</span>
								<div class="dropdown-message small">${assessmentModel.account.department.name}</div>
							</a>
							<div class="dropdown-divider"></div>
							<a class="dropdown-item" href="#"> <span class="text-danger">
									<strong> Location </strong>
							</span>
								<div class="dropdown-message small">
									${assessmentModel.account.location.name}</div>
							</a>
							<div class="dropdown-divider"></div>
							<a class="dropdown-item" href="#"> <span class="text-success">
									<strong> LOB </strong>
							</span>
								<div class="dropdown-message small">${assessmentModel.account.lob.name}</div>
							</a>
							<div class="dropdown-divider"></div>
							<a class="dropdown-item" href="#"> <span class="text-danger">
									<strong> Country </strong>
							</span>
								<div class="dropdown-message small">
									${assessmentModel.account.country.name}</div>
							</a>
							<div class="dropdown-divider"></div>
							<a class="dropdown-item" href="#"> <span class="text-success">
									<strong> Phase </strong>
							</span>
								<div class="dropdown-message small">${assessmentModel.account.phase}</div>
							</a>
							<div class="dropdown-divider"></div>
							<a class="dropdown-item" href="#"> <span class="text-danger">
									<strong> Initial Rating </strong>
							</span>
								<div class="dropdown-message small">
									${assessmentModel.account.initialRating}</div>
							</a>
						</div>
					</div>
					<div class="col-xl-9">
						<div class="form-group row">
							<label for="example-text-input" class="col-2 col-form-label">Approver</label>
							<div class="col-4">
								<sf:select class="form-control" id="approver.id"
									path="approver.id" items="${approvers}" itemLabel="name"
									itemValue="id" />
							</div>
							<label for="example-text-input" class="col-2 col-form-label">Assessor</label>
							<div class="col-4">
								<sf:select class="form-control" id="assessor.id"
									path="assessor.id" items="${assessors}" itemLabel="name"
									itemValue="id" />
							</div>
						</div>
						<div class="dropdown-divider"></div>
						<div class="col-xl-12" style="padding: 0;">
							<div class="card mb-3">
								<div class="card-header">
									<i class="fa fa-table"></i> &nbsp;Assigned Categories<a
										class="btn btn-warning" data-toggle="modal"
										data-target="#addNewCatagory"
										style="float: right; padding: 2px 11px; cursor: pointer; font-size: 14px;">Add
										New </a>
								</div>
								<div class="card-body">
									<table class="table table-bordered" id=" " width="100%"
										style="font-size: .9rem;" cellspacing="0">
										<thead>
											<tr>
												<th>Id</th>
												<th>Category</th>
												<th>SME</th>
												<th>Reviewer</th>
												<th>Status</th>
											</tr>
										</thead>
										<c:forEach items="${assessmentModel.assessmentCategories}"
											var="cat" varStatus="status">
											<tr>
												<td align="center">${status.count}</td>
												<td><input
													name="assessmentCategories[${status.index}].status"
													value="${cat.status}" /></td>
												<td><input
													name="assessmentCategories[${status.index}].status"
													value="${cat.status}" /></td>
												<td><input
													name="assessmentCategories[${status.index}].status"
													value="${cat.status}" /></td>
												<td><input
													name="assessmentCategories[${status.index}].status"
													value="${cat.status}" /></td>
											</tr>
										</c:forEach>
									</table>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="modal fade" id="addNewCatagory" tabindex="-1" role="dialog"
		aria-labelledby="exampleModalLabel" aria-hidden="true">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header"
					style="background: #b1afaf; padding: 8px 15px;">
					<h5 class="modal-title" id="exampleModalLabel">Add New
						Category</h5>
					<button class="close" type="button" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">×</span>
					</button>
				</div>

				<div class="modal-body">
					<div class="form-group row">
						<label for="example-text-input" class="col-2 col-form-label">Category</label>
						<div class="col-10">
							<sf:select class="form-control" id="category.id"
								path="category.id" items="${controlsCategory}" itemLabel="name"
								itemValue="id" />
						</div>
					</div>
				</div>
				<div class="modal-footer">
					<button class="btn btn-secondary" type="button"
						data-dismiss="modal" style="margin-right: 12px;">Cancel</button>
					<input class="btn btn-primary" type="submit"
						style="margin-right: 12px;" value="Submit" />
					<sf:hidden path="id" />
				</div>
			</div>

		</div>
	</div>
</sf:form>
