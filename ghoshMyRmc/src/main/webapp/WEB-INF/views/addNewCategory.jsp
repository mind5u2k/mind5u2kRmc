<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<c:set var="contextRoot" value="${pageContext.request.contextPath}"></c:set>
<sf:form modelAttribute="assessmentCategory" id="assessmentCategoryForm"
	action="${contextRoot}/admin/addNewAssessmentCategory" method="POST">
	<div class="modal-body">
		<div class="form-group row">
			<label for="example-text-input" class="col-2 col-form-label">Category</label>
			<div class="col-10">
				<sf:select class="form-control" id="assignedCategories.id"
					path="assignedCategories.id" items="${controlsCategory}"
					itemLabel="name" itemValue="id" />
			</div>
		</div>
		<div class="form-group row">
			<label for="example-text-input" class="col-2 col-form-label">Reviewer</label>
			<div class="col-10">
				<sf:select class="form-control" id="assignedCategories.id"
					path="reviwer.id" items="${reviewers}" itemLabel="name"
					itemValue="id" />
			</div>
		</div>
		<div class="form-group row">
			<label for="example-text-input" class="col-2 col-form-label">State</label>
			<div class="col-10">
				<select class="form-control" disabled="disabled">
					<option selected="selected">I</option>
				</select>
			</div>
		</div>
	</div>
	<div class="modal-footer">
		<button class="btn btn-secondary" type="button" data-dismiss="modal"
			style="margin-right: 12px;">Cancel</button>
		<input class="btn btn-primary" type="submit"
			style="margin-right: 12px;" value="Submit" />
		<sf:hidden path="id" />
		<sf:hidden path="assessment.id" />
	</div>
</sf:form>