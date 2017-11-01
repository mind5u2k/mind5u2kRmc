<%@taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<style>
input {
	border: 0;
}
</style>
<div class="container-fluid">

	<!-- Example DataTables Card-->
	<div class="card mb-3">
		<div class="card-header">
			<i class="fa fa-table"></i> &nbsp;Add Approver and Assessor
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
							<div class="dropdown-message small">${assessment.account.department.name}</div>
						</a>
						<div class="dropdown-divider"></div>
						<a class="dropdown-item" href="#"> <span class="text-danger">
								<strong> Location </strong>
						</span>
							<div class="dropdown-message small">
								${assessment.account.location.name}</div>
						</a>
						<div class="dropdown-divider"></div>
						<a class="dropdown-item" href="#"> <span class="text-success">
								<strong> LOB </strong>
						</span>
							<div class="dropdown-message small">${assessment.account.lob.name}</div>
						</a>
						<div class="dropdown-divider"></div>
						<a class="dropdown-item" href="#"> <span class="text-danger">
								<strong> Country </strong>
						</span>
							<div class="dropdown-message small">
								${assessment.account.country.name}</div>
						</a>
						<div class="dropdown-divider"></div>
						<a class="dropdown-item" href="#"> <span class="text-success">
								<strong> Phase </strong>
						</span>
							<div class="dropdown-message small">${assessment.account.phase}</div>
						</a>
						<div class="dropdown-divider"></div>
						<a class="dropdown-item" href="#"> <span class="text-danger">
								<strong> Initial Rating </strong>
						</span>
							<div class="dropdown-message small">
								${assessment.account.initialRating}</div>
						</a>
					</div>
				</div>
				<div class="col-xl-9">
					<div class="row">
						<div class="col-xl-6" style="text-align: center;">
							<div class="card text-white bg-primary o-hidden h-100">
								<div class="card-body">
									<div class="mr-5">Approver</div>
								</div>
								<span class="card-footer text-white clearfix z-1">
									${assessment.approver.name} [${assessment.approver.email}] </span>
							</div>
						</div>
						<div class="col-xl-6" style="text-align: center;">
							<div class="card text-white bg-primary o-hidden h-100">
								<div class="card-body" style="text-align: center;">
									<div class="mr-5">Assessor</div>
								</div>
								<span class="card-footer text-white clearfix z-1">
									${assessment.assessor.name} [${assessment.assessor.email}] </span>
							</div>
						</div>
					</div>
					<div class="dropdown-divider"></div>
					<div class="row">
						<div class="col-xl-6">
							<div class="container-fluid">
								<sf:form modelAttribute="assCatSmeMapping" id="assessmentForm"
									action="${contextRoot}/admin/addAssCatSme" method="POST">
									<div class="modal-dialog" role="document">
										<div class="modal-content">
											<div class="modal-header"
												style="background: #b1afaf; padding: 8px 15px;">
												<h5 class="modal-title" id="exampleModalLabel">Add SME
													for ${assessmentCategory.assignedCategories.name}</h5>
											</div>

											<div class="modal-body">
												<div class="form-group row">
													<label for="example-text-input"
														class="col-5 col-form-label">Select SME</label>
													<div class="col-5">
														<sf:select class="form-control" id="SME.id" path="SME.id"
															items="${allSmes}" itemLabel="name" itemValue="id" />
													</div>
												</div>
											</div>
											<div class="modal-footer">
												<sf:hidden path="id" />
												<sf:hidden path="assessmentCategories.id" />
												<input class="btn btn-primary" type="submit"
													style="margin-right: 12px;" value="Add" />
											</div>
										</div>
									</div>
								</sf:form>
							</div>
						</div>
						<div class="col-xl-6">
							<c:if test="${not empty msg}">
								<div class="card card-login mx-auto mt-5"
									style="margin: auto !important; border: 0;">
									<div class="alert alert-success"
										style="margin-bottom: 9px; padding: 2px 0px;">${msg}</div>
								</div>
							</c:if>
							<c:if test="${not empty errorMsg}">
								<div class="card card-login mx-auto mt-5"
									style="margin: auto !important; border: 0;">
									<div class="alert alert-danger"
										style="margin-bottom: 9px; padding: 2px 0px;">${errorMsg}</div>
								</div>
							</c:if>
							<div class="table-responsive">
								<table class="table table-bordered" id=" " width="100%"
									style="font-size: .9rem;" cellspacing="0">
									<thead>
										<tr>
											<th>ID</th>
											<th>Name</th>
											<th>Email Id</th>
											<th>Remove</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${assessmentCategorySMEMappings}"
											var="assessmentCategorySMEMapping">
											<c:set var="i" value="${i+1}" scope="page" />
											<tr>
												<td>${i}.</td>
												<td>${assessmentCategorySMEMapping.SME.name}</td>
												<td>${assessmentCategorySMEMapping.SME.email}</td>
												<td>Remove</td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="modal-footer">
		<button class="btn btn-secondary" type="button"
			onclick="window.location.href='${contextRoot}/admin/activateAssessment'"
			style="margin-right: 12px; cursor: pointer;">Keep it Open</button>
		<input class="btn btn-primary" type="button"
			onclick="window.location.href='${contextRoot}/admin/completeAndActive?assessmentId=${assessmentCategory.assessment.id}'"
			style="margin-right: 12px; cursor: pointer;"
			value="Activate Assessment" />
	</div>
</div>

<div class="modal fade" id="addnewCategory" tabindex="-1" role="dialog"
	aria-labelledby="exampleModalLabel" aria-hidden="true">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header"
				style="background: #b1afaf; padding: 8px 15px;">
				<h5 class="modal-title" id="exampleModalLabel">Add New Category</h5>
				<button class="close" type="button" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">×</span>
				</button>
			</div>
			<sf:form modelAttribute="assessmentCategory"
				id="assessmentCategoryForm"
				action="${contextRoot}/admin/addNewAssessmentCategory" method="POST">
				<div class="modal-body">
					<div class="form-group row">
						<label for="example-text-input" class="col-2 col-form-label">Category</label>
						<div class="col-10">
							<sf:select class="form-control" id="assignedCategories.id"
								path="assignedCategories.id" items="${controlsCategory}"
								itemLabel="name" itemValue="id" />
						</div>
					</div>
					<div class="form-group row">
						<label for="example-text-input" class="col-2 col-form-label">Reviewer</label>
						<div class="col-10">
							<sf:select class="form-control" id="assignedCategories.id"
								path="reviwer.id" items="${reviewers}" itemLabel="name"
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
					<sf:hidden path="assessment.id" />
				</div>
			</sf:form>
		</div>

	</div>
</div>
