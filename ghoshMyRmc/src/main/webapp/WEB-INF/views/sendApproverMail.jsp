<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<c:set var="contextRoot" value="${pageContext.request.contextPath}"></c:set>

<div class="card card-register mx-auto"
	style="max-width: 74rem !important;">
	<div class="card-header">Send Mail</div>
	<div class="card-body">
		<sf:form modelAttribute="mailModel" id="mailForm"
			action="${contextRoot}/approver/sendApproverMail" method="POST">
			<div class="form-group">
				<div class="form-row">
					<div class="col-md-12">
						To <label for="to"></label>
						<sf:input cssClass="form-control" path="to" id="to" />
					</div>
				</div>
			</div>
			<div class="form-group">
				<div class="form-row">
					<div class="col-md-12">
						CC <label for="cc"></label>
						<sf:input cssClass="form-control" path="cc" id="cc" />
					</div>
				</div>
			</div>
			<div class="form-group">
				<div class="form-row">
					<div class="col-md-12">
						Subject <label for="Subject"></label>
						<sf:textarea cssClass="form-control" path="subject" id="subject" />
					</div>
				</div>
			</div>
			<div class="form-group">
				<div class="form-row">
					<div class="col-md-12">
						Message<label for="message"></label>
						<div>
							<div class="jarviswidget-editbox"></div>
							<div class="widget-body no-padding">
								<textarea class="form-control" name="msg" id="msg"
									onkeyup="updateDiv();">${mailModel.message}</textarea>
							</div>
						</div>
					</div>
				</div>
			</div>
			<input type="submit" class="btn btn-primary" style="float: right;"
				onclick="return updateDiv();" value="Send Mail" />
			<a id="button1" class="btn btn-secondary" onclick="hideEffect();"
				style="float: right; margin-right: 5px;">Cancel</a>
			<sf:hidden id="message" path="message" />
		</sf:form>
	</div>
</div>

<script type="text/javascript">
	$(document).ready(function() {
		CKEDITOR.replace('msg', {
			height : '120px',
			startupFocus : true
		});

	});

	function updateDiv() {
		var editorText = CKEDITOR.instances.msg.getData();
		$("#message").val(editorText);
		return true;
	};
</script>