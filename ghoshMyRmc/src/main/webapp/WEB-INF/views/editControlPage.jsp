<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<c:set var="contextRoot" value="${pageContext.request.contextPath}"></c:set>
<sf:form modelAttribute="control" id="editControlForm"
	action="${contextRoot}/admin/updateControl" method="POST">
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
				<sf:select class="form-control" id="category.id" path="category.id"
					items="${controlsCategory}" itemLabel="name" itemValue="id" />
			</div>
			<label for="example-text-input" class="col-2 col-form-label">Answers</label>
			<div class="col-4">
				<sf:input class="form-control" type="text" placeholder="Answers set"
					id="answers" path="answers" />
			</div>
		</div>
		<div class="form-group row">
			<label for="example-text-input" class="col-2 col-form-label">Help
				Data</label>
			<div class="col-10">
				<sf:textarea cssClass="form-control" path="helpData" id="helpData" />
			</div>
		</div>
		<div class="form-group row">
			<label for="example-text-input" class="col-2 col-form-label">Short
				Text</label>
			<div class="col-10">
				<sf:textarea cssClass="form-control" path="shortText" id="shortText" />
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
		<button class="btn btn-secondary" type="button" data-dismiss="modal"
			style="margin-right: 12px;">Cancel</button>
		<input class="btn btn-primary" type="submit"
			style="margin-right: 12px;" value="Submit" />
		<sf:hidden path="id" />
	</div>
</sf:form>

<script>
	$(function() {
		var $editControlForm = $("#editControlForm");
		if ($editControlForm.length) {
			$editControlForm.validate({
				rules : {
					control : {
						required : true
					},
					answers : {
						required : true
					},
					helpData : {
						required : true
					},
					shortText : {
						required : true
					}
				},
				messages : {
					control : {
						required : "!! Please add the Control !!"
					},
					answers : {
						required : "!! Please fill the answer sets !!"
					},
					helpData : {
						required : "!! Help Data is Required !!"
					},
					shortText : {
						required : "!! Short Text is Required !!"
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