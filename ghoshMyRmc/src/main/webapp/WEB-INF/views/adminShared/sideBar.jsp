<nav class="navbar navbar-expand-lg navbar-dark bg-dark fixed-top"
	id="mainNav">
	<a class="navbar-brand" href="#">MY RMC</a>
	<button class="navbar-toggler navbar-toggler-right" type="button"
		data-toggle="collapse" data-target="#navbarResponsive"
		aria-controls="navbarResponsive" aria-expanded="false"
		aria-label="Toggle navigation">
		<span class="navbar-toggler-icon"></span>
	</button>
	<div class="collapse navbar-collapse" id="navbarResponsive">
		<ul class="navbar-nav navbar-sidenav" id="exampleAccordion">
			<li id="setupAssessments" class="nav-item" data-toggle="tooltip"
				data-placement="right" title="Components"><a
				id="setupAssessmentLink"
				class="nav-link nav-link-collapse collapsed" data-toggle="collapse"
				href="#setupAssessment" data-parent="#exampleAccordion"> <i
					class="fa fa-fw fa-cogs"></i> <span class="nav-link-text">Setup
						Assessment</span>
			</a>
				<ul class="sidenav-second-level collapse" id="setupAssessment">
					<li id="categories"><a href="${contextRoot}/admin/categories">Define
							Categories</a></li>
					<li id="controls"><a href="${contextRoot}/admin/controls">Define
							Controls</a></li>
				</ul></li>
			<li class="nav-item" data-toggle="tooltip" data-placement="right"
				title="Components"><a id="AccessMgmtLink"
				class="nav-link nav-link-collapse collapsed" data-toggle="collapse"
				href="#accessManagement" data-parent="#exampleAccordion"> <i
					class="fa fa-fw fa-users"></i> <span class="nav-link-text">Access
						Management</span>
			</a>
				<ul class="sidenav-second-level collapse" id="accessManagement">
					<li id="roles"><a href="${contextRoot}/admin/roles">Role</a></li>
					<li id="users"><a href="${contextRoot}/admin/users">Manage
							Users</a></li>
				</ul></li>

			<li class="nav-item" data-toggle="tooltip" data-placement="right"
				title="Components"><a id="setUpProcessLink"
				class="nav-link nav-link-collapse collapsed" data-toggle="collapse"
				href="#setupProcess" data-parent="#exampleAccordion"> <i
					class="fa fa-fw fa-cubes"></i> <span class="nav-link-text">Setup
						Process </span>
			</a>
				<ul class="sidenav-second-level collapse" id="setupProcess">
					<li id="department"><a href="${contextRoot}/admin/department">Client/Department</a></li>
					<li id="location"><a href="${contextRoot}/admin/location">Location</a></li>
					<li id="lob"><a href="${contextRoot}/admin/LOB">LOB</a></li>
					<li id="country"><a href="${contextRoot}/admin/country">Country</a></li>
					<li id="accounts"><a href="${contextRoot}/admin/account">Accounts</a></li>
				</ul></li>


			<li class="nav-item" data-toggle="tooltip" data-placement="right"
				title="Components"><a id="processManagementLink"
				class="nav-link nav-link-collapse collapsed" data-toggle="collapse"
				href="#processMgmt" data-parent="#exampleAccordion"> <i
					class="fa fa-fw fa-wrench"></i> <span class="nav-link-text">Process
						Management</span>
			</a>
				<ul class="sidenav-second-level collapse" id="processMgmt">
					<li id="activateAssessment"><a
						href="${contextRoot}/admin/activateAssessment">Activate
							Assessment</a></li>
					<li id="accountSpecificQuestion"><a
						href="${contextRoot}/admin/accountSpecificControls">Account
							Specific Controls</a></li>
					<li id="accountTransfer"><a
						href="${contextRoot}/admin/accountTransfer">Account Transfer</a></li>
					<li id="accountDeletion"><a
						href="${contextRoot}/admin/accountDeleteion">Account Deletion</a></li>
				</ul></li>
			<li id="reporting" class="nav-item" data-toggle="tooltip"
				data-placement="right" title="Dashboard"><a class="nav-link"
				href="${contextRoot}/admin/reporting"> <i
					class="fa fa-fw fa-external-link"></i> <span class="nav-link-text">Reporting</span>
			</a></li>
			<li class="nav-item" data-toggle="tooltip" data-placement="right"
				title="Components"><a id="mailing"
				class="nav-link nav-link-collapse collapsed" data-toggle="collapse"
				href="#mailings" data-parent="#exampleAccordion"> <i
					class="fa fa-fw fa-envelope"></i> <span class="nav-link-text">Mailing</span>
			</a>
				<ul class="sidenav-second-level collapse" id="mailings">
					<li id="mitigationMailLink"><a
						href="${contextRoot}/admin/mitigationMail">Mitigation
							Mail</a></li>
					<li id="assessmentMailLink"><a
						href="${contextRoot}/admin/assessmentMail">Assessment
							Mail</a></li>
				</ul></li>
		</ul>
		<ul class="navbar-nav sidenav-toggler">
			<li class="nav-item"><a class="nav-link text-center"
				id="sidenavToggler"> <i class="fa fa-fw fa-angle-left"></i>
			</a></li>
		</ul>
		<ul class="navbar-nav ml-auto">
			<li class="nav-item"><a class="mr-lg-2 btn btn-warning"
				target="_blank" style="padding: 4px 9px; margin: 5px;"
				id="messagesDropdown"
				href='${contextRoot}/downloadRiskCalculationPdf'><i
					style="font-size: 19px;" class="fa fa-fw fa-file-pdf-o"></i></a></li>
			<li class="nav-item"><a class="mr-lg-2 btn btn-primary"
				style="padding: 4px 9px; margin: 5px;" id="messagesDropdown"
				href='${contextRoot}/admin/adminhome'><i
					style="font-size: 19px;" class="fa fa-fw fa-home"></i> Home</a></li>
			<li class="nav-item dropdown"><a
				class="nav-link dropdown-toggle mr-lg-2" id="messagesDropdown"
				href="#" data-toggle="dropdown" aria-haspopup="true"
				aria-expanded="false">${userModel.fullName}</a>
				<div class="dropdown-menu"
					style="right: 0; left: unset; border: 1px solid #919598; box-shadow: 4px 4px 5px #5f5a5a;"
					aria-labelledby="messagesDropdown">
					<h6 class="dropdown-header">Admin Profile:</h6>
					<div class="dropdown-divider" style="margin: .1rem 0;"></div>
					<a class="dropdown-item" href="#"> <strong>Name</strong>
						<div class="dropdown-message small">${userModel.fullName}</div>
					</a>
					<div class="dropdown-divider" style="margin: .1rem 0;"></div>
					<a class="dropdown-item" href="#"> <strong>Email</strong>
						<div class="dropdown-message small">${userModel.email}</div>
					</a>
					<div class="dropdown-divider" style="margin: .1rem 0;"></div>
					<a class="dropdown-item" href="#"> <strong>Role</strong>
						<div class="dropdown-message small">${userModel.role}</div>
					</a>
					<div class="dropdown-divider" style="margin: .1rem 0;"></div>
					<a class="dropdown-item" data-toggle="modal"
						style="cursor: pointer;" data-target="#exampleModal"> <i
						class="fa fa-fw fa-sign-out"></i>Logout
					</a>
				</div></li>
			<!-- <li class="nav-item"><a class="nav-link" data-toggle="modal"
				data-target="#exampleModal"> <i class="fa fa-fw fa-sign-out"></i>Logout
			</a></li> -->
		</ul>
	</div>
</nav>

<div class="modal fade" id="exampleModal" tabindex="-1" role="dialog"
	aria-labelledby="exampleModalLabel" aria-hidden="true">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header"
				style="background: #b1afaf; padding: 8px 15px;">
				<h5 class="modal-title" id="exampleModalLabel">Ready to Leave?</h5>
				<button class="close" type="button" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">×</span>
				</button>
			</div>
			<div class="modal-body">Select "Logout" below if you are ready
				to end your current session.</div>
			<div class="modal-footer">
				<button class="btn btn-secondary" type="button" data-dismiss="modal"
					style="margin-right: 12px;">Cancel</button>
				<a class="btn btn-primary" href="${contextRoot}/perform-logout">Logout</a>
			</div>
		</div>
	</div>
</div>