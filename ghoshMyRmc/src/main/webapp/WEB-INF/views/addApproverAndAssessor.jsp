<%@taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<style>
input {
	border: 0;
}
</style>
<sf:form modelAttribute="assessment" id="assessmentForm"
	action="${contextRoot}/admin/addApproverAssessor" method="POST">
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

						<div class="container-fluid">
							<div class="modal-dialog" role="document">
								<div class="modal-content">
									<div class="modal-header"
										style="background: #b1afaf; padding: 8px 15px;">
										<h5 class="modal-title" id="exampleModalLabel">Approver
											And Assessor</h5>
									</div>

									<div class="modal-body">
										<div class="form-group row">
											<label for="example-text-input" class="col-3 col-form-label">Approver</label>
											<div class="col-9">
												<sf:select class="form-control" id="approver.id"
													path="approver.id" items="${approvers}" itemLabel="name"
													itemValue="id" />
											</div>
										</div>
										<div class="form-group row">
											<label for="example-text-input" class="col-3 col-form-label">Assessor</label>
											<div class="col-9">
												<sf:select class="form-control" id="assessor.id"
													path="assessor.id" items="${assessors}" itemLabel="name"
													itemValue="id" />
											</div>
										</div>
									</div>
									<div class="modal-footer">
										<button class="btn btn-secondary" type="button"
											onclick="window.location.href='${contextRoot}/admin/activateAssessment?accId=${assessment.account.id}'"
											style="margin-right: 12px;">Cancel</button>
										<input class="btn btn-primary" type="submit"
											style="margin-right: 12px;" value="Save and Next" />
										<sf:hidden path="id" />
									</div>

								</div>

							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</sf:form>
