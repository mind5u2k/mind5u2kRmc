<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<c:set var="contextRoot" value="${pageContext.request.contextPath}"></c:set>
<sf:form modelAttribute="category" id="categoryForm"
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