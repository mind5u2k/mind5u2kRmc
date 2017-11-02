<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<c:set var="contextRoot" value="${pageContext.request.contextPath}"></c:set>
<sf:form modelAttribute="account" id="accountForm"
	action="${contextRoot}/admin/updateAccount" method="POST">
	<div class="modal-body">
		<div class="form-group row">
			<label for="example-text-input" class="col-2 col-form-label">Department</label>
			<div class="col-4">
				<sf:select class="form-control" id="department.id"
					path="department.id" items="${departmentList}" itemLabel="name"
					itemValue="id" />
			</div>
			<label for="example-text-input" class="col-2 col-form-label">Location</label>
			<div class="col-4">
				<sf:select class="form-control" id="location.id" path="location.id"
					items="${locationList}" itemLabel="name" itemValue="id" />
			</div>
		</div>
		<div class="form-group row">
			<label for="example-text-input" class="col-2 col-form-label">LOB</label>
			<div class="col-4">
				<sf:select class="form-control" id="lob.id" path="lob.id"
					items="${lobList}" itemLabel="name" itemValue="id" />
			</div>
			<label for="example-text-input" class="col-2 col-form-label">Country</label>
			<div class="col-4">
				<sf:select class="form-control" id="country.id" path="country.id"
					items="${countryList}" itemLabel="name" itemValue="id" />
			</div>
		</div>
		<div class="form-group row">
			<label for="example-text-input" class="col-2 col-form-label">Sector</label>
			<div class="col-4">
				<sf:select class="form-control" id="sector" path="sector"
					items="${sectorList}" />
			</div>
			<label for="example-text-input" class="col-2 col-form-label">Phase</label>
			<div class="col-4">
				<sf:select class="form-control" id="phase" path="phase"
					items="${phaseList}" />
			</div>
		</div>
		<div class="form-group row">
			<div class="col-4"></div>
			<label for="example-text-input" class="col-2 col-form-label">Initial
				Rating</label>
			<div class="col-2">
				<sf:select class="form-control" id="initialRating"
					path="initialRating" items="${initialRatingList}" />
			</div>
			<div class="col-4"></div>
		</div>
	</div>
	<div class="modal-footer">
		<button class="btn btn-secondary" type="button" data-dismiss="modal"
			style="margin-right: 12px;">Cancel</button>
		<input class="btn btn-primary" type="submit"
			style="margin-right: 12px;" value="Submit" />
		<sf:hidden path="id" />
		<sf:hidden path="state" />
	</div>
</sf:form>
<script>
	$(function() {
		var $editCountryForm = $("#editCountryForm");
		if ($editCountryForm.length) {
			$editCountryForm
					.validate({
						rules : {
							name : {
								required : true,
								minlength : 2
							}
						},
						messages : {
							name : {
								required : "Please add the Country Name !!",
								minlength : "Country Name should not be less than 2 character !!"
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