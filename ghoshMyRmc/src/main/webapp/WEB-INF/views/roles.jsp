

<div class="container-fluid">
	<!-- Example DataTables Card-->
	<div class="card mb-3">
		<div class="card-header">
			<i class="fa fa-table"></i> Roles
		</div>
		<div class="card-body">
			<div class="table-responsive">
				<table class="table table-bordered" id="dataTable" width="100%"
					style="font-size: .9rem;" cellspacing="0">
					<thead>
						<tr>
							<th style="width: 20px;">ID</th>
							<th>Name</th>
							<th>State</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${roles}" var="role">
							<c:set var="i" value="${i+1}" scope="page" />
							<tr>
								<td>${i}.</td>
								<td>${role}</td>
								<td>Active</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>
<div id="dialog" title="Add New User">
	<p>This is an animated dialog which is useful for displaying
		information. The dialog window can be moved, resized and closed with
		the 'x' icon.</p>
</div>

<script>
	$(function() {
		dialog = $("#dialog").dialog({
			autoOpen : false,
			height : 400,
			width : 350,
			modal : true,
		});

		$("#opener").on("click", function() {
			$("#dialog").dialog("open");
		});
	});
</script>

