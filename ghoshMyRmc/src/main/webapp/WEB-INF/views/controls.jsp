<%@taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<style>
td.details-control {
	background: url('${images}/details_open.png') no-repeat center center;
	cursor: pointer;
}

tr.shown td.details-control {
	background: url('${images}/details_close.png') no-repeat center center;
}
</style>
<div class="container-fluid">
	<!-- Example DataTables Card-->
	<div class="card mb-3">
		<div class="card-header">
			<i class="fa fa-table"></i> Controls <a class="btn btn-warning"
				data-toggle="modal" data-target="#addNewControl"
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
				<table class="table table-bordered" id="controlDataTable"
					width="100%" style="font-size: .9rem;" cellspacing="0">
					<thead>
						<tr>
							<th></th>
							<th style="width: 20px;">ID</th>
							<th>Category</th>
							<th>Controls</th>
							<th>Answers</th>
							<th>Rating</th>
							<th>Flag</th>
							<th>Risk</th>
							<th>Edit</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${controls}" var="control">
							<c:set var="i" value="${i+1}" scope="page" />
							<tr>
								<td class="details-control"></td>
								<td>${i}.</td>
								<td>${control.category.name}</td>
								<td>${control.control}</td>
								<td>${control.answers}</td>
								<td>${control.rating}</td>
								<td>${control.flag}</td>
								<td>${control.risk}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>

<div class="modal fade" id="addNewControl" tabindex="-1" role="dialog"
	aria-labelledby="exampleModalLabel" aria-hidden="true">
	<div class="modal-dialog" role="document" style="max-width: 800px;">
		<div class="modal-content">
			<div class="modal-header"
				style="background: #b1afaf; padding: 8px 15px;">
				<h5 class="modal-title" id="exampleModalLabel">Add New Control</h5>
				<button class="close" type="button" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">×</span>
				</button>
			</div>
			<sf:form modelAttribute="control" id="controlForm"
				action="${contextRoot}/admin/addNewControl" method="POST">
				<div class="modal-body">
					<div class="form-group row">
						<label for="example-text-input" class="col-2 col-form-label">Control</label>
						<div class="col-10">
							<sf:textarea cssClass="form-control" path="control" id="control" />
						</div>
					</div>
					<div class="form-group row">
						<label for="example-text-input" class="col-2 col-form-label">Category</label>
						<div class="col-4">
							<sf:select class="form-control" id="category.id"
								path="category.id" items="${controlsCategory}" itemLabel="name"
								itemValue="id" />
						</div>
						<label for="example-text-input" class="col-2 col-form-label">Answers</label>
						<div class="col-4">
							<sf:input class="form-control" type="text"
								placeholder="Answers set" id="answers" path="answers" />
						</div>
					</div>
					<div class="form-group row">
						<label for="example-text-input" class="col-2 col-form-label">Help
							Data</label>
						<div class="col-10">
							<sf:textarea cssClass="form-control" path="helpData"
								id="helpData" />
						</div>
					</div>
					<div class="form-group row">
						<label for="example-text-input" class="col-2 col-form-label">Short
							Text</label>
						<div class="col-10">
							<sf:textarea cssClass="form-control" path="shortText"
								id="shortText" />
						</div>
					</div>
					<div class="form-group row">
						<label for="example-text-input" class="col-2 col-form-label">Critical</label>
						<div class="col-4">
							<sf:select class="form-control" id="critical" path="critical"
								items="${controlCritical}" />
						</div>
						<label for="example-text-input" class="col-2 col-form-label">Attachment</label>
						<div class="col-4">
							<sf:select class="form-control" id="att" path="att"
								items="${controlCritical}" />
						</div>
					</div>
					<div class="form-group row">
						<label for="example-text-input" class="col-2 col-form-label">Rating</label>
						<div class="col-4">
							<sf:select class="form-control" id="rating" path="rating"
								items="${controlRating}" />
						</div>
						<label for="example-text-input" class="col-2 col-form-label">Flag</label>
						<div class="col-4">
							<sf:select class="form-control" id="flag" path="flag"
								items="${controlFlag}" />
						</div>
					</div>
					<div class="form-group row">
						<label for="example-text-input" class="col-2 col-form-label">Risk</label>
						<div class="col-4">
							<sf:select class="form-control" id="risk" path="risk"
								items="${controlRisks}" />
						</div>
						<%-- <label for="example-text-input" class="col-2 col-form-label">Screen</label>
						<div class="col-4">
							<sf:select class="form-control" id="screen" path="screen"
								items="${controlScreens}" />
						</div> --%>
					</div>
				</div>
				<div class="modal-footer">
					<button class="btn btn-secondary" type="button"
						data-dismiss="modal" style="margin-right: 12px;">Cancel</button>
					<input class="btn btn-primary" type="submit"
						style="margin-right: 12px;" value="Submit" />
					<sf:hidden path="id" />
				</div>
			</sf:form>
		</div>

	</div>
</div>

<div class="modal fade" id="editControlModel" tabindex="-1"
	role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
	<div class="modal-dialog" role="document" style="max-width: 800px;">
		<div class="modal-content">
			<div class="modal-header"
				style="background: #b1afaf; padding: 8px 15px;">
				<h5 class="modal-title" id="exampleModalLabel">Edit Control</h5>
				<button class="close" type="button" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">×</span>
				</button>
			</div>
			<div class="modal-body editControlModelBody"></div>
		</div>

	</div>
</div>