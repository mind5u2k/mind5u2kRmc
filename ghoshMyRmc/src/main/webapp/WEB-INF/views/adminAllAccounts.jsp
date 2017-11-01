
<div class="container">
	<div class="row">
		<div class="col-xl-3 col-sm-6 mb-3">
			<div class="card text-white bg-primary o-hidden h-100">
				<div class="card-body">
					<div class="card-body-icon">
						<i class="fa fa-fw fa-database"></i>
					</div>
					<div class="mr-5">Total Accounts : ${total}</div>
				</div>
				<a class="card-footer text-white clearfix small z-1" href="#"> <span
					class="float-left">View Details</span> <span class="float-right">
						<i class="fa fa-angle-right"></i>
				</span>
				</a>
			</div>
		</div>
		<div class="col-xl-3 col-sm-6 mb-3">
			<div class="card text-white bg-warning o-hidden h-100">
				<div class="card-body">
					<div class="card-body-icon">
						<i class="fa fa-fw fa-exclamation-triangle"></i>
					</div>
					<div class="mr-5">Submission Pending : ${completed}</div>
				</div>
				<a class="card-footer text-white clearfix small z-1" href="#"> <span
					class="float-left">View Details</span> <span class="float-right">
						<i class="fa fa-angle-right"></i>
				</span>
				</a>
			</div>
		</div>
		<div class="col-xl-3 col-sm-6 mb-3">
			<div class="card text-white bg-success o-hidden h-100">
				<div class="card-body">
					<div class="card-body-icon">
						<i class="fa fa-fw fa-thumbs-o-up"></i>
					</div>
					<div class="mr-5">Submitted : ${submitted}</div>
				</div>
				<a class="card-footer text-white clearfix small z-1" href="#"> <span
					class="float-left">View Details</span> <span class="float-right">
						<i class="fa fa-angle-right"></i>
				</span>
				</a>
			</div>
		</div>
		<div class="col-xl-3 col-sm-6 mb-3">
			<div class="card text-white bg-danger o-hidden h-100">
				<div class="card-body">
					<div class="card-body-icon">
						<i class="fa fa-fw fa-thumbs-o-down"></i>
					</div>
					<div class="mr-5">Incomplete : ${incomplete}</div>
				</div>
				<a class="card-footer text-white clearfix small z-1" href="#"> <span
					class="float-left">View Details</span> <span class="float-right">
						<i class="fa fa-angle-right"></i>
				</span>
				</a>
			</div>
		</div>
	</div>
</div>
<div class="mb-3">
	<div class="table-responsive">
		<table class="table table-bordered" id="dataTable" width="100%"
			style="font-size: .8rem;" cellspacing="0">
			<thead>
				<tr>
					<th rowspan="2">ID</th>
					<th colspan="4" style="text-align: center;">Account Info</th>
					<th rowspan="2">Phase</th>
					<th rowspan="2">Approver</th>
					<th rowspan="2">Assessor</th>
					<th rowspan="2">Assessment</th>
					<th colspan="4" style="text-align: center;">Risk</th>
					<th rowspan="2" colspan="2" style="text-align: center;">Status</th>
				</tr>
				<tr>
					<th>Department</th>
					<th>Location</th>
					<th>LOB</th>
					<th>Country</th>
					<th>Track</th>
					<th>Analysis</th>
					<th>Level</th>
					<th>Value</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${assessments}" var="assessment">
					<c:set var="i" value="${i+1}" scope="page" />
					<tr>
						<td>${i}</td>
						<td>${assessment.account.department.name}</td>
						<td>${assessment.account.location.name}</td>
						<td>${assessment.account.lob.name}</td>
						<td>${assessment.account.country.name}</td>
						<td>${assessment.account.phase}</td>
						<td>${assessment.approver.name}</td>
						<td>${assessment.assessor.name}</td>
						<td style="text-align: center;"><a class="btn btn-dark"
							href="${contextRoot}/admin/assessmentPage?assessmentId=${assessment.id}"
							style="padding: 0px 6px; cursor: pointer;"><i
								class="fa fa-server" aria-hidden="true"></i></a></td>
						<c:if test="${assessment.assessmentStatus=='Incomplete'}">
							<td style="text-align: center;"><button class="btn btn-dark"
									style="padding: 0px 6px; cursor: not-allowed;"
									disabled="disabled">
									<i class="fa fa-calendar" aria-hidden="true"></i>
								</button></td>
						</c:if>
						<c:if test="${assessment.assessmentStatus=='Complete'}">
							<td style="text-align: center;"><button class="btn btn-dark"
									style="padding: 0px 6px; cursor: not-allowed;"
									disabled="disabled">
									<i class="fa fa-calendar" aria-hidden="true"></i>
								</button></td>
						</c:if>
						<c:if test="${assessment.assessmentStatus=='Submitted'}">
							<td style="text-align: center;"><a class="btn btn-dark"
								href="${contextRoot}/admin/riskTracker?assessmentId=${assessment.id}"
								style="padding: 0px 6px; cursor: pointer;"><i
									class="fa fa-calendar" aria-hidden="true"></i></a></td>
						</c:if>
						<c:if test="${assessment.assessmentStatus=='Incomplete'}">
							<td style="text-align: center;"><button class="btn btn-dark"
									style="padding: 0px 6px; cursor: not-allowed;"
									disabled="disabled">
									<i class="fa fa-bar-chart" aria-hidden="true"></i>
								</button></td>
						</c:if>
						<c:if test="${assessment.assessmentStatus=='Complete'}">
							<td style="text-align: center;"><button class="btn btn-dark"
									style="padding: 0px 6px; cursor: not-allowed;"
									disabled="disabled">
									<i class="fa fa-bar-chart" aria-hidden="true"></i>
								</button></td>
						</c:if>
						<c:if test="${assessment.assessmentStatus=='Submitted'}">
							<td style="text-align: center;"><a class="btn btn-dark"
								href="${contextRoot}/admin/riskAnalysis?assId=${assessment.id}"
								style="padding: 0px 6px; cursor: pointer;"><i
									class="fa fa-bar-chart" aria-hidden="true"></i></a></td>
						</c:if>
						<c:if test="${assessment.assessmentStatus=='Incomplete'}">

							<td>-</td>
							<td>-</td>
						</c:if>
						<c:if test="${assessment.assessmentStatus=='Complete'}">

							<td>-</td>
							<td>-</td>
						</c:if>
						<c:if test="${assessment.assessmentStatus=='Submitted'}">

							<td>${assessment.riskLevel}</td>
							<td>${assessment.riskValue}</td>
						</c:if>
						<c:if test="${assessment.assessmentStatus=='Incomplete'}">
							<td class="btn-danger">${assessment.assessmentStatus}</td>
							<td></td>
						</c:if>
						<c:if test="${assessment.assessmentStatus=='Complete'}">
							<td class="btn-warning">${assessment.assessmentStatus}</td>
							<td></td>
						</c:if>
						<c:if test="${assessment.assessmentStatus=='Submitted'}">
							<td class="btn-success" style="white-space: nowrap;">${assessment.assessmentStatus}
							</td>
							<td class="btn-success"><a class="btn btn-success"
								onclick="return showHistory(${assessment.id});"
								style="padding: 0px 6px; cursor: pointer;"> <i
									class="fa fa-history" aria-hidden="true"></i>
							</a></td>
						</c:if>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</div>
<div class="modal fade" id="assessmentHistoryModel" tabindex="-1"
	role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
	<div class="modal-dialog" role="document" style="max-width: 800px;">
		<div class="modal-content">
			<div class="modal-header"
				style="background: #b1afaf; padding: 8px 15px;">
				<h5 class="modal-title" id="exampleModalLabel">Answer History</h5>
				<button class="close" type="button" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">×</span>
				</button>
			</div>
			<div class="modal-body" id="assessmentHistoryModelBody"></div>
			<div class="modal-footer">
				<button class="btn btn-secondary" type="button" data-dismiss="modal"
					style="margin-right: 12px;">Cancel</button>
			</div>
		</div>

	</div>
</div>

<script>
function showHistory(assessmentId){
	 $("#assessmentHistoryModelBody").load(
				'${contextRoot}/admin/assessmentHistory?assessmentId=' + assessmentId,
				function() {
					
					$('#assessmentHistoryModel').modal({
						show : true
					});
				}); 
}
</script>