<%@taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>

<sf:form modelAttribute="assessmentModel" id="assessmentForm"
	action="${contextRoot}/approver/submitAssessment" method="POST">
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
					<a class="card-footer text-white clearfix small z-1" href="#">
						<span class="float-left">View Details</span> <span
						class="float-right"> </span>
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
					<a class="card-footer text-white clearfix small z-1" href="#">
						<span class="float-left">View Details</span> <span
						class="float-right"> </span>
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
					<a class="card-footer text-white clearfix small z-1" href="#">
						<span class="float-left">View Details</span> <span
						class="float-right"> </span>
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
					<a class="card-footer text-white clearfix small z-1" href="#">
						<span class="float-left">View Details</span> <span
						class="float-right"> </span>
					</a>
				</div>
			</div>
		</div>
	</div>
	<div class="mb-3">
		<div class="table-responsive">
			<c:if test="${not empty msg}">
				<div class="card card-login mx-auto mt-5"
					style="margin: auto !important; border: 0; max-width: 34rem;">
					<div class="alert alert-success" style="margin: 0;">${msg}</div>
				</div>
			</c:if>
			<table class="table table-bordered" id="dataTable" width="100%"
				style="font-size: .8rem;" cellspacing="0">
				<thead>
					<tr>
						<th rowspan="2">ID</th>
						<th colspan="3" style="text-align: center;">Account Info</th>
						<th rowspan="2">Phase</th>
						<th rowspan="2">Approver</th>
						<th rowspan="2">Assessment</th>
						<th colspan="4" style="text-align: center;">Risk</th>
						<th rowspan="2" style="text-align: center;">Status</th>
						<th rowspan="2" style="text-align: center;">Trail</th>
						<!-- 						<th rowspan="2">Submit</th> -->
						<!-- <th rowspan="2">Mail</th> -->
					</tr>
					<tr>
						<th>Department</th>
						<th>Location</th>
						<th>LOB</th>
						<th>Track</th>
						<th>Analysis</th>
						<th>Level</th>
						<th>Value</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${assessmentModel.assessments}" var="assessment"
						varStatus="status">
						<c:set var="i" value="${i+1}" scope="page" />
						<tr>
							<td>${i}</td>
							<td>${assessment.account.department.name}</td>
							<td>${assessment.account.location.name}</td>
							<td>${assessment.account.lob.name}</td>
							<td>${assessment.account.phase}</td>
							<td>${assessment.approver.name}</td>
							<td style="text-align: center;"><a class="btn btn-dark"
								href="${contextRoot}/assessor/assessmentPage?assessmentId=${assessment.id}"
								style="padding: 0px 6px; cursor: pointer;"><i
									class="fa fa-server" aria-hidden="true"></i></a></td>


							<c:if test="${assessment.assessmentStatus=='Incomplete'}">
								<td style="text-align: center;"><button
										class="btn btn-dark"
										style="padding: 0px 6px; cursor: not-allowed;"
										disabled="disabled">
										<i class="fa fa-calendar" aria-hidden="true"></i>
									</button></td>
							</c:if>
							<c:if test="${assessment.assessmentStatus=='Complete'}">
								<td style="text-align: center;"><button
										class="btn btn-dark"
										style="padding: 0px 6px; cursor: not-allowed;"
										disabled="disabled">
										<i class="fa fa-calendar" aria-hidden="true"></i>
									</button></td>
							</c:if>
							<c:if test="${assessment.assessmentStatus=='Submitted'}">
								<td style="text-align: center;"><a class="btn btn-dark"
									href="${contextRoot}/assessor/riskTracker?assessmentId=${assessment.id}"
									style="padding: 0px 6px; cursor: pointer;"><i
										class="fa fa-calendar" aria-hidden="true"></i></a></td>
							</c:if>



							<c:if test="${assessment.assessmentStatus=='Incomplete'}">
								<td style="text-align: center;"><button
										class="btn btn-dark"
										style="padding: 0px 6px; cursor: not-allowed;"
										disabled="disabled">
										<i class="fa fa-bar-chart" aria-hidden="true"></i>
									</button></td>
							</c:if>
							<c:if test="${assessment.assessmentStatus=='Complete'}">
								<td style="text-align: center;"><button
										class="btn btn-dark"
										style="padding: 0px 6px; cursor: not-allowed;"
										disabled="disabled">
										<i class="fa fa-bar-chart" aria-hidden="true"></i>
									</button></td>
							</c:if>
							<c:if test="${assessment.assessmentStatus=='Submitted'}">
								<td style="text-align: center;"><a class="btn btn-dark"
									href="${contextRoot}/assessor/riskAnalysis?assId=${assessment.id}"
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
								<td class="btn-danger" style="text-align: center;">${assessment.assessmentStatus}</td>
								<td>--</td>
							</c:if>
							<c:if test="${assessment.assessmentStatus=='Complete'}">
								<td class="btn-warning" style="text-align: center;">${assessment.assessmentStatus}</td>
								<td>--</td>
							</c:if>
							<c:if test="${assessment.assessmentStatus=='Submitted'}">
								<td class="btn-success"
									style="white-space: nowrap; text-align: center;">${assessment.assessmentStatus}
								</td>
								<td class="btn-success" style="text-align: center;"><a
									class="btn btn-success"
									onclick="return showHistory(${assessment.id});"
									style="padding: 0px 6px; cursor: pointer;"> <i
										class="fa fa-history" aria-hidden="true"></i>
								</a></td>
							</c:if>

							<%-- <c:if test="${assessment.assessmentStatus=='Incomplete'}">
								<td style="text-align: center;"><sf:checkbox
										path="assessments[${status.index}].assessmentStatus"
										value="Submitted" disabled="true" /></td>
							</c:if>
							<c:if test="${assessment.assessmentStatus=='Complete'}">
								<td style="text-align: center;"><sf:checkbox
										cssClass="form-control completeAssessment"
										path="assessments[${status.index}].assessmentStatus"
										value="Complete_${assessment.id}" /></td>
							</c:if>
							<c:if test="${assessment.assessmentStatus=='Submitted'}">
								<td style="text-align: center;"><sf:checkbox
										path="assessments[${status.index}].assessmentStatus"
										value="${assessment.assessmentStatus}" disabled="true" /></td>
							</c:if> --%>


							<!-- <td><a class="btn btn-warning"
								style="padding: 0px 6px; cursor: pointer;"><i
									class="fa fa-envelope-o" aria-hidden="true"></i></a></td> -->
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
	<%-- <div class="dropdown-divider"></div>
	<div class="row" style="text-align: center; font-size: .9rem;">
		<div class="col-xl-1"></div>
		<div class="col-xl-5">
			<h6>Attestation:</h6>
			I confirm that I have responded/validated the controls assessment as
			per the requirements or risks in my business. I shall be responsible
			for any adverse impact or penalties to the business due to incorrect
			or inaccurate responses in the controls assessment.
			<h6>
				<input type="radio" name="agree" value="Yes" checked="checked"
					disabled="disabled" /> I Agree
			</h6>
		</div>
		<div class="col-xl-5">
			<h6>Comment:</h6>
			<sf:textarea cssClass="form-control" path="declaration"
				id="declaration" />
		</div>
		<div class="col-xl-1"></div>
		<div class="col-xl-12" style="padding-top: 42px;">
			<input class="btn btn-primary" type="submit"
				onsubmit="return checkAssessment();" style="margin-right: 12px;"
				value="I Approve" disabled />
		</div>
	</div> --%>
</sf:form>

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
	$(function() {
		$('.completeAssessment').click(function() {
			var compAssess = 0;
			if ($(this).is(':checked')) {
				$.each($(".completeAssessment:checked"), function() {
					compAssess++;
				});
			} else {
				$.each($(".completeAssessment:checked"), function() {
					compAssess++;
				});
			}
			if (compAssess > 0) {
				$("input[type='submit']").removeAttr("disabled");
			} else {
				$("input[type='submit']").attr("disabled", "disabled");
			}
		});

	});
	
	 function showHistory(assessmentId){
		 $("#assessmentHistoryModelBody").load(
					'${contextRoot}/assessor/assessmentHistory?assessmentId=' + assessmentId,
					function() {
						
						$('#assessmentHistoryModel').modal({
							show : true
						});
					}); 
	 }
</script>