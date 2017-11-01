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
		<ul class="navbar-nav ml-auto">
			<li class="nav-item"><a class="mr-lg-2 btn btn-warning"
				target="_blank" style="padding: 4px 9px; margin: 5px;"
				id="messagesDropdown"
				href='${contextRoot}/downloadRiskCalculationPdf'><i
					style="font-size: 19px;" class="fa fa-fw fa-file-pdf-o"></i></a></li>
			<li class="nav-item"><a class="mr-lg-2 btn btn-primary"
				style="padding: 4px 9px; margin: 5px;" id="messagesDropdown"
				href='${contextRoot}/reviewer/reviewerHome'><i
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