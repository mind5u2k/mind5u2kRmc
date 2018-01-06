$(function() {

	switch (menu) {
	case 'categories':
		$("#setupAssessmentLink").removeClass("collapsed");
		$("#setupAssessment").addClass("show");
		$("#categories").addClass("active");
		break;
	case 'controls':
		$("#setupAssessmentLink").removeClass("collapsed");
		$("#setupAssessment").addClass("show");
		$("#controls").addClass("active");
		break;
	case 'roles':
		$("#AccessMgmtLink").removeClass("collapsed");
		$("#accessManagement").addClass("show");
		$("#roles").addClass("active");
		break;
	case 'users':
		$("#AccessMgmtLink").removeClass("collapsed");
		$("#accessManagement").addClass("show");
		$("#users").addClass("active");
		break;
	case 'Client / Department':
		$("#setUpProcessLink").removeClass("collapsed");
		$("#setupProcess").addClass("show");
		$("#department").addClass("active");
		break;
	case 'Locations':
		$("#setUpProcessLink").removeClass("collapsed");
		$("#setupProcess").addClass("show");
		$("#location").addClass("active");
		break;
	case 'LOB':
		$("#setUpProcessLink").removeClass("collapsed");
		$("#setupProcess").addClass("show");
		$("#lob").addClass("active");
		break;
	case 'Country':
		$("#setUpProcessLink").removeClass("collapsed");
		$("#setupProcess").addClass("show");
		$("#country").addClass("active");
		break;
	case 'Accounts':
		$("#setUpProcessLink").removeClass("collapsed");
		$("#setupProcess").addClass("show");
		$("#accounts").addClass("active");
		break;
	case 'Activate Assessment':
		$("#processManagementLink").removeClass("collapsed");
		$("#processMgmt").addClass("show");
		$("#activateAssessment").addClass("active");
		break;
	case 'Account Specific Controls':
		$("#processManagementLink").removeClass("collapsed");
		$("#processMgmt").addClass("show");
		$("#accountSpecificQuestion").addClass("active");
		break;
	case 'Account Transfer':
		$("#processManagementLink").removeClass("collapsed");
		$("#processMgmt").addClass("show");
		$("#accountTransfer").addClass("active");
		break;
	case 'Account Deletion':
		$("#processManagementLink").removeClass("collapsed");
		$("#processMgmt").addClass("show");
		$("#accountDeletion").addClass("active");
		break;
	case 'Reporting':
		$("#reporting").addClass("active");
		break;
	case 'Mitigation Mail':
		$("#mailing").removeClass("collapsed");
		$("#mailings").addClass("show");
		$("#mitigationMailLink").addClass("active");
		break;
	case 'Assessment Mail':
		$("#mailing").removeClass("collapsed");
		$("#mailings").addClass("show");
		$("#assessmentMailLink").addClass("active");
		break;
	default:
		break;
	}

	var $dataTable1 = $("#dataTable1");
	if ($dataTable1.length) {
		$dataTable1.DataTable();
	}

	var $controlDatatable = $("#controlDataTable");
	if ($controlDatatable.length) {

		var jsonUrl = '';
		jsonUrl = window.contextRoot + '/json/data/all/controls';
		var $tab = $controlDatatable
				.DataTable({
					ajax : {
						url : jsonUrl,
						dataSrc : '',
					},
					columns : [
							{
								className : 'details-control',
								orderable : false,
								data : null,
								defaultContent : ''
							},
							{
								data : 'id'
							},
							{
								data : 'category.name'
							},
							{
								data : 'control'
							},
							{
								data : 'answers'
							},
							{
								data : 'rating'
							},
							{
								data : 'flag'
							},
							{
								data : 'risk'
							},
							{
								className : 'details-controls',
								orderable : false,
								data : null,
								defaultContent : '<a class="btn btn-warning"onclick="editCategory(${category.id});" style="padding: 0px 6px; cursor: pointer;"><i class="fa fa-pencil-square-o" aria-hidden="true"></i></a>'
							} ],
					order : [ [ 1, 'asc' ] ]
				});

		$('#controlDataTable tbody').on('click', 'td.details-controls',
				function() {
					var tr = $(this).closest('tr');
					var row = $tab.row(tr);
					editControl(row.data());
				});

		function editControl(rowData) {
			$('.editControlModelBody').load(
					window.contextRoot + '/admin/editControl?controlId='
							+ rowData.id, function() {

						$('#editControlModel').modal({
							show : true
						});
					});
		}

		$('#controlDataTable tbody').on('click', 'td.details-control',
				function() {
					var tr = $(this).closest('tr');
					var row = $tab.row(tr);

					if (row.child.isShown()) {
						row.child.hide();
						tr.removeClass('shown');
					} else {
						row.child(format(row.data())).show();
						tr.addClass('shown');
					}
				});
		function format(rowData) {
			var string = "<table style='width: 100%;background: #dee0fb;'><tr><td style='width: 10%;width: 10%;background: #c8cbf1;'>Category</td><td style='background: #dee0fb;'>"
					+ rowData.category.name
					+ "</td></tr><tr><td style='width: 10%;width: 10%;background: #c8cbf1;'>Control</td><td>"
					+ rowData.control
					+ "</td></tr><tr><td style='width: 10%;width: 10%;background: #c8cbf1;'>Answers</td><td>"
					+ rowData.answers
					+ "</td></tr><tr><td style='width: 10%;width: 10%;background: #c8cbf1;'>Help Data</td><td>"
					+ rowData.helpData
					+ "</td></tr><tr><td style='width: 10%;width: 10%;background: #c8cbf1;'>Short Text</td><td>"
					+ rowData.shortText
					+ "</td></tr><tr><td style='width: 10%;width: 10%;background: #c8cbf1;'>Critical</td><td>"
					+ rowData.critical
					+ "</td></tr><tr><td style='width: 10%;width: 10%;background: #c8cbf1;'> Att</td><td>"
					+ rowData.att
					+ "</td></tr><tr><td style='width: 10%;width: 10%;background: #c8cbf1;'>Rating</td><td>"
					+ rowData.rating
					+ "</td></tr><tr><td style='width: 10%;width: 10%;background: #c8cbf1;'>Flag</td><td>"
					+ rowData.flag
					+ "</td></tr><tr><td style='width: 10%;width: 10%;background: #c8cbf1;'>Risk</td><td>"
					+ rowData.risk + "</td></tr></table>";
			var div = $('<div/>').addClass('loading').html(string);
			return div;
		}
	}

	var $alert = $(".alert");
	if ($alert.length) {
		setTimeout(function() {
			$alert.fadeOut('slow');
		}, 3000)
	}

	// ---- validation code for Category --------
	var $categoryForm = $("#categoryForm");
	if ($categoryForm.length) {
		$categoryForm
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

	// ---- validation code for Control --------
	var $controlForm = $("#controlForm");
	if ($controlForm.length) {
		$controlForm.validate({
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

	// ---- validation code for User --------
	var $userForm = $("#userForm");
	if ($userForm.length) {
		$userForm.validate({
			rules : {
				name : {
					required : true
				},
				email : {
					required : true,
					email : true
				}
			},
			messages : {
				name : {
					required : "!! Please fill the Name !!"
				},
				email : {
					required : "!! Please mention the Email Id !!",
					email : "!! Please enter a valid Email Id !!"
				}
			},
			errorElement : 'em',
			errorPlacement : function(error, element) {
				error.addClass("help-block");
				error.insertAfter(element);
			}
		});
	}

	// ---- validation code for Department --------
	var $departmentForm = $("#departmentForm");
	if ($departmentForm.length) {
		$departmentForm
				.validate({
					rules : {
						name : {
							required : true,
							minlength : 2
						}
					},
					messages : {
						name : {
							required : "Please add the Client / Department Name !!",
							minlength : "Client/Department Name should not be less than 2 character !!"
						}
					},
					errorElement : 'em',
					errorPlacement : function(error, element) {
						error.addClass("help-block");
						error.insertAfter(element);
					}
				});
	}

	// ---- validation code for Location --------
	var $locationForm = $("#locationForm");
	if ($locationForm.length) {
		$locationForm
				.validate({
					rules : {
						name : {
							required : true,
							minlength : 2
						}
					},
					messages : {
						name : {
							required : "Please add the Location Name !!",
							minlength : "Location Name should not be less than 2 character !!"
						}
					},
					errorElement : 'em',
					errorPlacement : function(error, element) {
						error.addClass("help-block");
						error.insertAfter(element);
					}
				});
	}

	// ---- validation code for LOB --------
	var $lobForm = $("#lobForm");
	if ($lobForm.length) {
		$lobForm
				.validate({
					rules : {
						name : {
							required : true,
							minlength : 2
						}
					},
					messages : {
						name : {
							required : "Please add the LOB Name !!",
							minlength : "LOB Name should not be less than 2 character !!"
						}
					},
					errorElement : 'em',
					errorPlacement : function(error, element) {
						error.addClass("help-block");
						error.insertAfter(element);
					}
				});
	}

	// ---- validation code for Country --------
	var $countryForm = $("#countryForm");
	if ($countryForm.length) {
		$countryForm
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

	/*
	 * var $answerForm = $("#answerForm"); if ($answerForm.length) {
	 * alert("asdf"); $answerForm.validate({ rules : { }, messages : { },
	 * 
	 * submitHandler : function(form) { alert("adsfasfdasdf"); var answer =
	 * $("answer").val(); if (answer != "No") { $("#answerForm div.error").html(
	 * "Please fill the comment area while answering no") return false; } else
	 * form.submit(); },
	 * 
	 * }); }
	 */

});