<%@taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>

<div class="container-fluid">
	<!-- Example DataTables Card-->
	<div class="card mb-3">
		<div class="card-header">
			<i class="fa fa-table"></i> Account Specific Controls
		</div>
		<div class="card-body">
			<div style="text-align: center;">
				<div class="btn-group">
					<button type="button" class="btn btn-primary dropdown-toggle"
						data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
						${assessmentCategory.assignedCategories.name} - [
						${assessmentCategory.status} ]&nbsp;&nbsp;</button>
					<div class="dropdown-menu">
						<c:forEach items="${assessmentCategories}"
							var="assessmentCategory">
							<a class="dropdown-item"
								href="${contextRoot}/admin/editAccSpecControl?assessmentId=${assessmentCategory.assessment.id}&catId=${assessmentCategory.id}">${assessmentCategory.assignedCategories.name}
								- [ ${assessmentCategory.status} ]</a>
						</c:forEach>
					</div>
				</div>
			</div>

			<c:if test="${not empty msg}">
				<div class="card card-login mx-auto mt-5"
					style="margin: 9px auto !important; border: 0;">
					<div class="alert alert-success" style="margin: 0;">${msg}</div>
				</div>
			</c:if>
			<div class="table-responsive">
				<table class="table table-bordered" id="dataTable" width="100%"
					style="font-size: .9rem;" cellspacing="0">
					<thead>
						<tr>
							<th style="white-space: nowrap;">ID</th>
							<th>Category</th>
							<th>Controls</th>
							<th>Answers</th>
							<th>Rating</th>
							<th>Flag</th>
							<th>Risk</th>
							<th style="width: 90px;">Add/Remove</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${allControlsforSelectedCat}" var="controlModel">
							<c:set var="i" value="${i+1}" scope="page" />
							<tr>
								<td>${i}.</td>
								<td>${controlModel.control.category.name}</td>
								<td>${controlModel.control.control}</td>
								<td>${controlModel.control.answers}</td>
								<td>${controlModel.control.rating}</td>
								<td>${controlModel.control.flag}</td>
								<td>${controlModel.control.risk}</td>
								<c:if test="${controlModel.status==true}">
									<td style="text-align: center; cursor: pointer;"
										class="btn-success"
										onclick="window.location.href='${contextRoot}/admin/removeControl?id=${controlModel.control.id}&assCatId=${assessmentCategory.id}'">
										<i class="fa fa-times" aria-hidden="true"></i> Remove
									</td>
								</c:if>
								<c:if test="${controlModel.status==false}">
									<td style="text-align: center; cursor: pointer;"
										onclick="window.location.href='${contextRoot}/admin/addControl?id=${controlModel.control.id}&assCatId=${assessmentCategory.id}'"
										class="btn-primary"><i class="fa fa-plus"
										aria-hidden="true"></i> Add</td>
								</c:if>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>
