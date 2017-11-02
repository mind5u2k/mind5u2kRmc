<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<c:set var="contextRoot" value="${pageContext.request.contextPath}"></c:set>
<sf:form modelAttribute="assessment" id="editApproverForm"
	action="${contextRoot}/admin/updateApprover" method="POST">
	<div class="modal-body editApproverModelBody">
		<div class="form-group row">
			<label for="example-text-input" class="col-2 col-form-label">Approver</label>
			<div class="col-10">
				<sf:select class="form-control" id="approver.id" path="approver.id"
					items="${approvers}" itemLabel="name" itemValue="id" />
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
