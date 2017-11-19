<%@taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<sf:form modelAttribute="accountTransferModel"
	id="accountTransferModelForm"
	action="${contextRoot}/admin/transferAccount" method="POST">
	<div class="container-fluid">
		<c:if test="${not empty msg}">
			<div class="card card-login mx-auto mt-5"
				style="margin: auto !important; border: 0;">
				<div class="alert alert-success" style="margin: 0;">${msg}</div>
			</div>
		</c:if>
		<c:if test="${not empty erroMsg}">
			<div class="card card-login mx-auto mt-5"
				style="margin: auto !important; border: 0;">
				<div class="alert alert-danger" style="margin: 0;">${erroMsg}</div>
			</div>
		</c:if>
		<div class="row">
			<div class="col-6 ">
				<div class="modal-dialog" role="document">
					<div class="modal-content accFrom">
						<div class="modal-header"
							style="background: #b1afaf; padding: 8px 15px;">
							<h5 class="modal-title" id="exampleModalLabel">Account From</h5>
							<a class="btn btn-warning" data-toggle="modal"
								data-target="#selectAccountFrom"
								style="float: right; padding: 2px 11px; cursor: pointer; font-size: 14px;">Select
								Account</a>
						</div>
						<div class="modal-body">
							<div class="form-group row">
								<label for="example-text-input" class="col-4 col-form-label">Department</label>
								<div class="col-8">
									<sf:input class="form-control" type="text"
										placeholder="Department Name" id="accountFrom"
										path="accountFrom.department.name" disabled="true" />
								</div>
							</div>
							<div class="form-group row">
								<label for="example-text-input" class="col-4 col-form-label">Location</label>
								<div class="col-8">
									<sf:input class="form-control" type="text"
										placeholder="Location Name" id="accountFrom"
										path="accountFrom.location.name" disabled="true" />
								</div>
							</div>
							<div class="form-group row">
								<label for="example-text-input" class="col-4 col-form-label">LOB</label>
								<div class="col-8">
									<sf:input class="form-control" type="text"
										placeholder="LOB Name" id="accountFrom"
										path="accountFrom.lob.name" disabled="true" />
								</div>
							</div>
							<div class="form-group row">
								<label for="example-text-input" class="col-4 col-form-label">Country</label>
								<div class="col-8">
									<sf:input class="form-control" type="text"
										placeholder="Country Name" id="accountFrom"
										path="accountFrom.department.name" disabled="true" />
								</div>
							</div>
							<div class="form-group row">
								<label for="example-text-input" class="col-4 col-form-label">Phase</label>
								<div class="col-8">
									<sf:input class="form-control" type="text" placeholder="Phase"
										id="accountFrom" path="accountFrom.phase" disabled="true" />
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="col-6">
				<div class="modal-dialog" role="document">
					<div class="modal-content accTo">
						<div class="modal-header"
							style="background: #b1afaf; padding: 8px 15px;">
							<h5 class="modal-title" id="exampleModalLabel">Account To</h5>
							<a class="btn btn-warning" data-toggle="modal"
								data-target="#selectAccountTo"
								style="float: right; padding: 2px 11px; cursor: pointer; font-size: 14px;">Select
								Account</a>
						</div>
						<div class="modal-body">
							<div class="form-group row">
								<label for="example-text-input" class="col-4 col-form-label">Department</label>
								<div class="col-8">
									<sf:input class="form-control" type="text"
										placeholder="Department Name" id="accountFrom"
										path="accountTo.department.name" disabled="true" />
								</div>
							</div>
							<div class="form-group row">
								<label for="example-text-input" class="col-4 col-form-label">Location</label>
								<div class="col-8">
									<sf:input class="form-control" type="text"
										placeholder="Location Name" id="accountFrom"
										path="accountTo.location.name" disabled="true" />
								</div>
							</div>
							<div class="form-group row">
								<label for="example-text-input" class="col-4 col-form-label">LOB</label>
								<div class="col-8">
									<sf:input class="form-control" type="text"
										placeholder="LOB Name" id="accountFrom"
										path="accountTo.lob.name" disabled="true" />
								</div>
							</div>
							<div class="form-group row">
								<label for="example-text-input" class="col-4 col-form-label">Country</label>
								<div class="col-8">
									<sf:input class="form-control" type="text"
										placeholder="Country Name" id="accountFrom"
										path="accountTo.department.name" disabled="true" />
								</div>
							</div>
							<div class="form-group row">
								<label for="example-text-input" class="col-4 col-form-label">Phase</label>
								<div class="col-8">
									<sf:input class="form-control" type="text" placeholder="Phase"
										id="accountFrom" path="accountTo.phase" disabled="true" />
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div style="text-align: center;">
		<sf:hidden id="accountFromId" path="accountFrom.id" />
		<sf:hidden id="accountToId" path="accountTo.id" />
		<input class="btn btn-primary" type="submit" style="margin: auto;"
			onclick="return checkSelectedAccount();" value="Transfer Account" />
	</div>


</sf:form>
<div class="modal fade" id="selectAccountFrom" tabindex="-1"
	role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
	<div class="modal-dialog" role="document" style="max-width: 800px;">
		<div class="modal-content">
			<div class="modal-header"
				style="background: #b1afaf; padding: 8px 15px;">
				<h5 class="modal-title" id="exampleModalLabel">Select Account
					From</h5>
				<button class="close" type="button" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">×</span>
				</button>
			</div>
			<div class="modal-body">

				<div class="table-responsive">
					<table class="table table-bordered" id="dataTable" width="100%"
						style="font-size: .9rem;" cellspacing="0">
						<thead>
							<tr>
								<th>ID</th>
								<th>Department</th>
								<th>Location</th>
								<th>LOB</th>
								<th>Country</th>
								<th>Phase</th>
								<th></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${assessments}" var="assessment">
								<c:set var="i" value="${i+1}" scope="page" />
								<tr>
									<td>${i}.</td>
									<td>${assessment.account.department.name}</td>
									<td>${assessment.account.location.name}</td>
									<td>${assessment.account.lob.name}</td>
									<td>${assessment.account.country.name}</td>
									<td>${assessment.account.phase}</td>
									<c:if test="${not empty accountTransferModel.accountTo}">
										<td
											onclick="window.location.href='${contextRoot}/admin/accountTransfer?accountFromId=${assessment.account.id}&accountToId=${accountTransferModel.accountTo.id}'"
											class="btn-warning"
											style="text-align: center; cursor: pointer;"><i
											class="fa fa-plus" aria-hidden="true"></i></td>
									</c:if>
									<c:if test="${empty accountTransferModel.accountTo}">
										<td
											onclick="window.location.href='${contextRoot}/admin/accountTransfer?accountFromId=${assessment.account.id}'"
											class="btn-warning"
											style="text-align: center; cursor: pointer;"><i
											class="fa fa-plus" aria-hidden="true"></i></td>
									</c:if>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
</div>
<div class="modal fade" id="selectAccountTo" tabindex="-1" role="dialog"
	aria-labelledby="exampleModalLabel" aria-hidden="true">
	<div class="modal-dialog" role="document" style="max-width: 800px;">
		<div class="modal-content">
			<div class="modal-header"
				style="background: #b1afaf; padding: 8px 15px;">
				<h5 class="modal-title" id="exampleModalLabel">Select Account
					To</h5>
				<button class="close" type="button" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">×</span>
				</button>
			</div>
			<div class="modal-body">
				<div class="table-responsive">
					<table class="table table-bordered" id="dataTable1" width="100%"
						style="font-size: .9rem;" cellspacing="0">
						<thead>
							<tr>
								<th>ID</th>
								<th>Department</th>
								<th>Location</th>
								<th>LOB</th>
								<th>Country</th>
								<th>Phase</th>
								<th></th>
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
									<td>${account.phase}</td>
									<c:if test="${not empty accountTransferModel.accountFrom}">
										<td
											onclick="window.location.href='${contextRoot}/admin/accountTransfer?accountToId=${account.id}&accountFromId=${accountTransferModel.accountFrom.id}'"
											class="btn-warning"
											style="text-align: center; cursor: pointer;"><i
											class="fa fa-plus" aria-hidden="true"></i></td>
									</c:if>
									<c:if test="${empty accountTransferModel.accountFrom}">
										<td
											onclick="window.location.href='${contextRoot}/admin/accountTransfer?accountToId=${account.id}'"
											class="btn-warning"
											style="text-align: center; cursor: pointer;"><i
											class="fa fa-plus" aria-hidden="true"></i></td>
									</c:if>

								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
</div>
<script>
	$("#dataTable1").datatable();

	function checkSelectedAccount() {
		if ($("#accountFromId").val() == 0) {
			var $comEr = $("#comment-error");
			if (!$comEr.length) {
				$(".accFrom")
						.after(
								'<em id="comment-error" class="error help-block" style="text-align:center;">!! Please Select Account from !!</em>');
			}
			return false;

		}

		if ($("#accountToId").val() == 0) {
			var $comEr1 = $("#comment-error1");
			if (!$comEr1.length) {
				$(".accTo")
						.after(
								'<em id="comment-error1" class="error help-block" style="text-align:center;">!! Please Select Account To !!</em>');
			}
			return false;
		}
	}
</script>