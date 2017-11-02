<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<c:set var="contextRoot" value="${pageContext.request.contextPath}"></c:set>
<sf:form modelAttribute="category" id="editCategoryForm"
	action="${contextRoot}/admin/updateCategory" method="POST">

	<div class="form-group row">
		<label for="example-text-input" class="col-2 col-form-label">Name</label>
		<div class="col-10">
			<sf:input class="form-control" type="text"
				placeholder="Category Name" id="name" path="name" />
		</div>
	</div>
	<div class="form-group row">
		<label for="example-text-input" class="col-2 col-form-label">Phase</label>
		<div class="col-10">
			<sf:select class="form-control" id="phase" path="phase"
				items="${categoryPhases}" />
		</div>
	</div>
	<div class="form-group row">
		<label for="example-text-input" class="col-2 col-form-label">State</label>
		<div class="col-10">
			<sf:select class="form-control" id="state" path="state"
				items="${categoryState}" />
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
		var $editCategoryForm = $("#editCategoryForm");
		if ($editCategoryForm.length) {
			$editCategoryForm
					.validate({
						rules : {
							name : {
								required : true,
								minlength : 2
							}
						},
						messages : {
							name : {
								required : "Please add the Category Name !!",
								minlength : "Category Name should not be less than 2 character !!"
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