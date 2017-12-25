<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div style="padding: 17px; color: #099409;">!! ${msg} !!</div>
<div>

	<button id="approverBtn" style="margin: 22px 0;"
		onclick="sendApproverMail();" class="btn btn-primary">Send
		Mail To Approver</button>
	<button id="assessorBtn" style="margin: 22px 0; display: none;"
		onclick="sendAssessorMail();" class="btn btn-primary">Send
		Mail To Assessor</button>
</div>