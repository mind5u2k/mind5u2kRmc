<%@taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<div class="container-fluid">
	<div class="table-responsive">
		<table class="table table-bordered" id="" width="100%"
			style="font-size: .9rem; border: 2px solid #d6d8da;" cellspacing="0">
			<thead>
				<tr>
					<th colspan="8" style="text-align: center;">Assessment Info</th>
				</tr>
				<tr>
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
				<tr>
					<td>${assessment.account.department.name}</td>
					<td>${assessment.account.location.name}</td>
					<td>${assessment.account.lob.name}</td>
					<td>${assessment.account.country.name}</td>
					<td>${assessment.account.sector}</td>
					<td>${assessment.account.phase}</td>
					<td>${assessment.account.initialRating}</td>
					<c:if test="${assessment.assessmentStatus=='Incomplete'}">
						<td class="btn-danger disabled">${assessment.assessmentStatus}</td>
					</c:if>
					<c:if test="${assessment.assessmentStatus=='Complete'}">
						<td class="btn-warning">${assessment.assessmentStatus}</td>
					</c:if>
					<c:if test="${assessment.assessmentStatus=='Submitted'}">
						<td class="btn-success" style="white-space: nowrap;">${assessment.assessmentStatus}
						</td>
					</c:if>
				</tr>
			</tbody>
		</table>
	</div>
	<c:if test="${not empty msg}">
		<div class="card card-login mx-auto mt-5"
			style="margin: auto !important; border: 0;">
			<div class="alert alert-success"
				style="margin-bottom: 9px; padding: 8px 2px;">${msg}</div>
		</div>
	</c:if>
	<div class="container-fluid">

		<div class="row">

			<div class="col-xl-5" style="text-align: center;">
				<div class="card o-hidden h-100"
					style="background: #7f7f7f; color: #fff;">
					<div class="card-body">
						<div class="">
							Approver <a class="btn btn-warning"
								onclick="editApprover(${assessment.id});"
								style="padding: 0px 6px; cursor: pointer; float: right;">Edit
								<i class="fa fa-pencil-square-o" aria-hidden="true"></i>
							</a>
						</div>
					</div>
					<span class="card-footer clearfix z-1"
						style="color: #000; background: #d4d4d4;"> <c:if
							test="${not empty assessment.approver}">${assessment.approver.name} [${assessment.approver.email}]</c:if>
						<c:if test="${empty assessment.approver}">!! Not Assigned !!</c:if>
					</span>
				</div>
			</div>
			<div class="col-xl-5" style="text-align: center;">
				<div class="card o-hidden h-100"
					style="background: #7f7f7f; color: #fff;">
					<div class="card-body" style="text-align: center;">
						<div class=" ">
							Assessor <a class="btn btn-warning"
								onclick="editAssessor(${assessment.id});"
								style="padding: 0px 6px; cursor: pointer; float: right;">Edit
								<i class="fa fa-pencil-square-o" aria-hidden="true"></i>
							</a>
						</div>
					</div>
					<span class="card-footer clearfix z-1"
						style="color: #000; background: #d4d4d4;"> <c:if
							test="${not empty assessment.assessor}">${assessment.assessor.name} [${assessment.assessor.email}]</c:if>
						<c:if test="${empty assessment.assessor}">!! Not Assigned !!</c:if>
					</span>
				</div>
			</div>
			<div class="col-xl-2" style="padding-bottom: 12px;">
				<a class="btn btn-warning"
					style="padding: 6px 10px; cursor: pointer; float: right; position: absolute; bottom: 13px;"
					onclick="addNewCategory(${assessment.id});">Add New Category</a>
			</div>
		</div>
		<div class="row">

			<div class="col-xl-12">

				<div class="table-responsive">
					<table class="table table-bordered" id="dataTable" width="100%"
						style="font-size: 0.9rem; border-collapse: collapse !important;"
						cellspacing="0">
						<thead>
							<tr>
								<th style="width: 20px;">ID</th>
								<th>Category</th>
								<th>Reviewer</th>
								<th>State</th>
								<th>SME's</th>
								<th style="width: 49px;">Edit</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${assessmentCategories}"
								var="assessmentCategory">
								<c:set var="i" value="${i+1}" scope="page" />
								<tr>
									<td>${i}.</td>
									<td>${assessmentCategory.assignedCategories.name}</td>
									<td><c:if test="${not empty assessmentCategory.reviwer}">${assessmentCategory.reviwer.name} [${assessmentCategory.reviwer.email}]</c:if>
										<c:if test="${empty assessmentCategory.reviwer}">
											<span style="color: #f00;">!! Not Assigned !!</span>
										</c:if></td>
									<td>${assessmentCategory.status}</td>
									<td><c:forEach items="${assessmentCategorySMEMappings}"
											var="assessmentCategorySmeMap">
											<c:set var="j" value="${j+1}" scope="page" />
											<c:if
												test="${assessmentCategorySmeMap.assessmentCategories.id == assessmentCategory.id}">
												<span style="color: #155724;">
													${assessmentCategorySmeMap.SME.name}
													[${assessmentCategorySmeMap.SME.email}] <a
													class="btn btn-secondary"
													onclick="editAssessmentCategory(${assessmentCategory.id});"
													style="padding: 0px 6px; cursor: pointer; float: right;"><i
														class="fa fa-trash-o" aria-hidden="true"></i></a>
												</span>
												<br>
												<div class="dropdown-divider"
													style="margin: 3px 0px; border-top: 1px solid #eca8ae;"></div>
											</c:if>
										</c:forEach><a class="btn btn-warning"
										onclick="addSme(${assessmentCategory.id});"
										style="padding: 2px 10px; cursor: pointer; float: right;">Assign
											SME</a></td>
									<td><a class="btn btn-warning"
										onclick="editAssessmentCategory(${assessmentCategory.id});"
										style="padding: 0px 6px; cursor: pointer; float: left;"><i
											class="fa fa-pencil-square-o" aria-hidden="true"></i></a><a
										class="btn btn-secondary"
										onclick="editAssessmentCategory(${assessmentCategory.id});"
										style="padding: 0px 6px; cursor: pointer; float: left; margin-left: 4px;"><i
											class="fa fa-trash-o" aria-hidden="true"></i></a></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
	<div class="modal-footer">
		<button class="btn btn-secondary" type="button"
			onclick="window.location.href='${contextRoot}/admin/activateAssessment'"
			style="margin-right: 12px; cursor: pointer;">Keep it Open</button>
		<input class="btn btn-primary" type="button"
			onclick="window.location.href='${contextRoot}/admin/completeAndActive?assessmentId=${assessment.id}'"
			style="margin-right: 12px; cursor: pointer;"
			value="Activate Assessment" />
	</div>
</div>
<div class="modal fade" id="editApproverModel" tabindex="-1"
	role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header"
				style="background: #b1afaf; padding: 8px 15px;">
				<h5 class="modal-title" id="exampleModalLabel">Edit Approver</h5>
				<button class="close" type="button" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">×</span>
				</button>
			</div>
			<div class="editApproverModelBody"></div>

		</div>
	</div>
</div>

<div class="modal fade" id="editAssessorModel" tabindex="-1"
	role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header"
				style="background: #b1afaf; padding: 8px 15px;">
				<h5 class="modal-title" id="exampleModalLabel">Edit Assessor</h5>
				<button class="close" type="button" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">×</span>
				</button>
			</div>
			<div class="editAssessorModelBody"></div>

		</div>
	</div>
</div>

<div class="modal fade" id="addNewCategoryModel" tabindex="-1"
	role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
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
			<div class="addNewCategoryModelBody"></div>

		</div>
	</div>
</div>

<div class="modal fade" id="addSmeModel" tabindex="-1" role="dialog"
	aria-labelledby="exampleModalLabel" aria-hidden="true">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header"
				style="background: #b1afaf; padding: 8px 15px;">
				<h5 class="modal-title" id="exampleModalLabel">Assign SME</h5>
				<button class="close" type="button" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">×</span>
				</button>
			</div>
			<div class="addSmeModelBody"></div>

		</div>
	</div>
</div>

<div class="modal fade" id="editAssessmentCategoryModel" tabindex="-1"
	role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header"
				style="background: #b1afaf; padding: 8px 15px;">
				<h5 class="modal-title" id="exampleModalLabel">Edit Assessment
					Category</h5>
				<button class="close" type="button" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">×</span>
				</button>
			</div>
			<div class="editAssessmentCategoryModelBody"></div>

		</div>
	</div>
</div>

<script>
	function editApprover(assessmentId) {
		$('.editApproverModelBody').load('${contextRoot}/admin/editApprover?assessmentId=' + assessmentId,
				function() {
					$('#editApproverModel').modal({
						show : true
					});
				}); 
	}
	
	function editAssessor(assessmentId) {
		$('.editAssessorModelBody').load('${contextRoot}/admin/editAssessor?assessmentId=' + assessmentId,
				function() {
					$('#editAssessorModel').modal({
						show : true
					});
				}); 
	}
	
	function addNewCategory(assessmentId){
		$('.addNewCategoryModelBody').load('${contextRoot}/admin/addNewCategory?assessmentId=' + assessmentId,
				function() {
					$('#addNewCategoryModel').modal({
						show : true
					});
				}); 
	}
	
	function addSme(assessmentCatId){
		$('.addSmeModelBody').load('${contextRoot}/admin/addNewSme?assessmentCatId=' + assessmentCatId,
				function() {
					$('#addSmeModel').modal({
						show : true
					});
				}); 
	}
	
	function editAssessmentCategory(assCatId){
		$('.editAssessmentCategoryModelBody').load('${contextRoot}/admin/editAssessmentCategory?assessmentCatId=' + assCatId,
				function() {
					$('#editAssessmentCategoryModel').modal({
						show : true
					});
				}); 
	}
</script>