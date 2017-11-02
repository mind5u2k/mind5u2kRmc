<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<c:set var="contextRoot" value="${pageContext.request.contextPath}"></c:set>
<sf:form modelAttribute="location" id="editLocationForm"
	action="${contextRoot}/admin/updateLocation" method="POST">
	<div class="modal-body">
		<div class="form-group row">
			<label for="example-text-input" class="col-2 col-form-label">Name</label>
			<div class="col-10">
				<sf:input class="form-control" type="text"
					placeholder="Location Name" id="name" path="name" />
			</div>
		</div>
	</div>
	<div class="modal-footer">
		<button class="btn btn-secondary" type="button" data-dismiss="modal"
			style="margin-right: 12px;">Cancel</button>
		<input class="btn btn-primary" type="submit"
			style="margin-right: 12px;" value="Submit" />
		<sf:hidden path="id" />
	</div>
</sf:form>

<script>
	$(function() {
		var $editLocationForm = $("#editLocationForm");
		if ($editLocationForm.length) {
			$editLocationForm
					.validate({
						rules : {
							name : {
								required : true,
								minlength : 2
							}
						},
						messages : {
							name : {
								required : "Please add the Location Name !!",
								minlength : "Location Name should not be less than 2 character !!"
							}
						},
						errorElement : 'em',
						errorPlacement : function(error, element) {
							error.addClass("help-block");
							error.insertAfter(element);
						}
					});
		}

	});
</script>