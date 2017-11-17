package net.gh.ghoshMyRmc.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.serial.SerialBlob;
import javax.validation.Valid;

import net.gh.ghoshMyRmc.mailService.MailService;
import net.gh.ghoshMyRmc.model.AccSpecControlModel;
import net.gh.ghoshMyRmc.model.AnswerModel;
import net.gh.ghoshMyRmc.model.AssessmentModel;
import net.gh.ghoshMyRmc.model.MailModel;
import net.gh.ghoshMyRmc.pdfGeneration.PdfSections;
import net.gh.ghoshMyRmc.riskAnalysis.DownloadExcel;
import net.gh.ghoshMyRmc.riskAnalysis.RiskCalculation;
import net.gh.ghoshMyRmcBackend.Util;
import net.gh.ghoshMyRmcBackend.dao.AccountDao;
import net.gh.ghoshMyRmcBackend.dao.AssessmentDao;
import net.gh.ghoshMyRmcBackend.dao.CategoryDao;
import net.gh.ghoshMyRmcBackend.dao.ControlDao;
import net.gh.ghoshMyRmcBackend.dao.UserDao;
import net.gh.ghoshMyRmcBackend.dto.AccountSpecificControl;
import net.gh.ghoshMyRmcBackend.dto.Answer;
import net.gh.ghoshMyRmcBackend.dto.AnswerCopy;
import net.gh.ghoshMyRmcBackend.dto.AnswerTrail;
import net.gh.ghoshMyRmcBackend.dto.Assessment;
import net.gh.ghoshMyRmcBackend.dto.AssessmentCategories;
import net.gh.ghoshMyRmcBackend.dto.AssessmentCategorySMEMapping;
import net.gh.ghoshMyRmcBackend.dto.AssessmentTrail;
import net.gh.ghoshMyRmcBackend.dto.Control;
import net.gh.ghoshMyRmcBackend.dto.User;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/approver")
public class ApproverController {

	@Autowired
	private GlobalController globalController;

	@Autowired
	private CategoryDao categoryDao;

	@Autowired
	private ControlDao controlDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private AccountDao accountDao;

	@Autowired
	private AssessmentDao assessmentDao;

	@Autowired
	private RiskCalculation riskCalculation;

	@Autowired
	private DownloadExcel downloadExcel;

	@Autowired
	private PdfSections pdfSections;

	@Autowired
	private MailService mailService;

	// ------------------Approver Home ----------------------
	@RequestMapping(value = "/approverHome")
	public ModelAndView home(
			@RequestParam(name = "operation", required = false) String operation) {
		ModelAndView mv = new ModelAndView("approverHome");
		mv.addObject("title", "Welcome Approver Home");
		mv.addObject("totalAccounts", true);

		if (operation != null) {
			if (operation.equals("submitted")) {
				mv.addObject("msg",
						"!! Selected Assessments have been Submitted !!");
			}
		}

		User approver = userDao.getUserByEmailId(globalController
				.getUserModel().getEmail());
		List<Assessment> assessments = getActivatedAccount(assessmentDao
				.assessmentListByApprover(approver));
		if (assessments == null) {
			assessments = new ArrayList<Assessment>();
		}
		AssessmentModel assessmentModel = new AssessmentModel();
		assessmentModel.setAssessments(assessments);
		mv.addObject("assessmentModel", assessmentModel);
		mv.addObject("total", assessments.size());

		List<Assessment> completeAssessments = getAssessmentByStatus(
				assessments, Util.COMPLETE_ASSESSMENT);
		mv.addObject("completed", completeAssessments.size());

		List<Assessment> submitAssessments = getAssessmentByStatus(assessments,
				Util.SUBMITTED_ASSESSMENT);
		mv.addObject("submitted", submitAssessments.size());

		List<Assessment> incompleteAssessments = getAssessmentByStatus(
				assessments, Util.INCOMPLETE_ASSESSMENT);
		mv.addObject("incomplete", incompleteAssessments.size());

		return mv;
	}

	@RequestMapping(value = "/assessmentPage")
	public ModelAndView assessmentPage(
			@RequestParam(name = "assessmentId", required = false) Long assessmentId,
			@RequestParam(name = "catId", required = false) Long catId,
			@RequestParam(name = "operation", required = false) String operation) {

		ModelAndView mv = new ModelAndView("assessmentPage");
		mv.addObject("title", "Assessment Page");
		Assessment assessment = assessmentDao.getAssessmentById(assessmentId);

		if (assessment.getAssessmentStatus().equals(Util.SUBMITTED_ASSESSMENT)) {
			mv = new ModelAndView("assessmentCopyPage");
			mv.addObject("title", "Assessment Page");
			List<AssessmentCategories> assessmentCategories = assessmentDao
					.assessmentCategoriesByAssessment(assessment);
			AssessmentCategories selectedAssessmentCategory = null;
			if (catId != null) {
				selectedAssessmentCategory = assessmentDao
						.getAssessmentCategoryById(catId);
			} else {
				if (assessmentCategories != null) {
					selectedAssessmentCategory = assessmentCategories.get(0);
				}
			}
			List<AnswerCopy> answers = controlDao
					.allAnswerCopiesbyAssessmentCategory(selectedAssessmentCategory);
			List<AnswerModel> answerModels = new ArrayList<AnswerModel>();
			for (AnswerCopy ans : answers) {
				AnswerModel answerModel = new AnswerModel();
				answerModel.setAnswerCopy(ans);
				answerModel.setStatus(true);
				answerModels.add(answerModel);
			}
			mv.addObject("answerModels", answerModels);
			mv.addObject("assessment", assessment);
			mv.addObject("assessmentCategories", assessmentCategories);
			mv.addObject("selectedAssessmentCategory",
					selectedAssessmentCategory);
			return mv;
		}

		System.out.println("**********************************" + catId);
		if (operation != null) {
			if (operation.equals("newAnswerAdded")) {
				mv.addObject("msg", "Response has been successfully submitted");
			} else if (operation.equals("updatedAnswer")) {
				mv.addObject("msg", "Response has been updated successfully");
			} else if (operation.equals("assessmentSaved")) {
				mv.addObject("msg", "Assessment has been saved successfully");
			} else if (operation.equals("assessmentfailed")) {
				mv.addObject("errorMsg",
						"Assessment has not been saved as few control have not been responded yet");
			}
		}

		if (!assessment.getApprover().getEmail()
				.equals(globalController.getUserModel().getEmail())) {
			mv = new ModelAndView("asdf");
		}

		List<AssessmentCategories> assessmentCategories = assessmentDao
				.assessmentCategoriesByAssessment(assessment);
		AssessmentCategories selectedAssessmentCategory = null;
		if (catId != null) {
			selectedAssessmentCategory = assessmentDao
					.getAssessmentCategoryById(catId);
		} else {
			if (assessmentCategories != null) {
				selectedAssessmentCategory = assessmentCategories.get(0);
			}
		}

		// ---------Preparing Answers for selected Assessment ---------

		List<AccountSpecificControl> accountSpecificControls = controlDao
				.accSpecControlByCategory(selectedAssessmentCategory);

		List<Answer> answers = controlDao
				.allAnswerbyAssessmentCategory(selectedAssessmentCategory);
		List<AnswerModel> answerModels = new ArrayList<AnswerModel>();
		for (AccountSpecificControl acc : accountSpecificControls) {
			boolean status = false;
			AnswerModel answerModel = new AnswerModel();
			for (Answer ans : answers) {
				if (ans.getControl().getId() == acc.getId()) {
					status = true;
					answerModel.setAnswer(ans);
					answerModel.setStatus(status);
					break;
				}
			}
			if (!status) {
				Answer a = new Answer();
				a.setControl(acc);
				answerModel.setAnswer(a);
				answerModel.setStatus(status);
			}
			answerModels.add(answerModel);
		}

		System.out
				.println("**********************************total answerModels are ["
						+ answerModels.size() + "]");
		mv.addObject("answerModels", answerModels);
		mv.addObject("assessment", assessment);
		mv.addObject("assessmentCategories", assessmentCategories);
		mv.addObject("selectedAssessmentCategory", selectedAssessmentCategory);
		return mv;
	}

	@RequestMapping(value = "/completeAssessment")
	public String completeAssessment(
			@RequestParam(name = "assId", required = false) Long assId) {

		Assessment assessment = assessmentDao.getAssessmentById(assId);
		List<AssessmentCategories> categories = assessmentDao
				.assessmentCategoriesByAssessment(assessment);

		List<AssessmentCategories> incompleteCategories = new ArrayList<AssessmentCategories>();
		List<AssessmentCategories> completeCategories = new ArrayList<AssessmentCategories>();

		for (AssessmentCategories asc : categories) {
			boolean status = false;
			List<AccountSpecificControl> accSpecControl = controlDao
					.accSpecControlByCategory(asc);
			for (AccountSpecificControl acspccl : accSpecControl) {
				Answer answer = controlDao.getAnswerByAccSpecControl(acspccl);
				if (answer == null) {
					status = true;
					break;
				}
			}
			if (status) {
				incompleteCategories.add(asc);
			} else {
				asc.setStatus("C");
				assessmentDao.updateAssessmentCategory(asc);
			}
		}

		if (incompleteCategories.size() == 0) {
			assessment.setAssessmentStatus(Util.COMPLETE_ASSESSMENT);
			assessmentDao.updateAssessment(assessment);
			return "redirect:/approver/assessmentPage?operation=assessmentSaved&assessmentId="
					+ assId;
		}

		return "redirect:/approver/assessmentPage?operation=assessmentfailed&assessmentId="
				+ assId;
	}

	@RequestMapping(value = "/addAccSpecControls")
	public ModelAndView addAccSpecControls(
			@RequestParam(name = "assCatId", required = false) long assCatId,
			@RequestParam(name = "operation", required = false) String operation) {

		ModelAndView mv = new ModelAndView("addAccSpecControls");
		mv.addObject("title", "Add Account Specific Control Page");

		AssessmentCategories assessmentCategory = assessmentDao
				.getAssessmentCategoryById(assCatId);

		if (!assessmentCategory.getAssessment().getApprover().getEmail()
				.equals(globalController.getUserModel().getEmail())) {
			mv = new ModelAndView("asdf");
		}

		if (operation != null) {
			if (operation.equals("accSpecControlAdded")) {
				mv.addObject("msg",
						"Control has been Successfully Added to this Assessment");
			} else if (operation.equals("accSpecControlremoved")) {
				mv.addObject("msg",
						"Account Specific control has been successfully Removed");
			} else if (operation.equals("accSpecControlNotRemoved")) {
				mv.addObject(
						"erroMsg",
						"Selected Control could not be removed as the Respnose has already been submitted for this Control");
			}
		}

		List<Control> allControlsforSelectedCat = controlDao
				.controlListsByCategory(assessmentCategory
						.getAssignedCategories());

		List<AccountSpecificControl> accountSpecificControls = controlDao
				.accSpecControlByCategory(assessmentCategory);

		List<AccSpecControlModel> controlModel = new ArrayList<AccSpecControlModel>();

		for (Control control : allControlsforSelectedCat) {
			boolean status = false;
			for (AccountSpecificControl accControl : accountSpecificControls) {
				if (control.getId() == accControl.getControl().getId()) {
					AccSpecControlModel m = new AccSpecControlModel();
					m.setControl(control);
					m.setStatus(true);
					status = true;
					controlModel.add(m);
					break;
				}
			}
			if (!status) {
				AccSpecControlModel m = new AccSpecControlModel();
				m.setControl(control);
				m.setStatus(false);
				controlModel.add(m);
			}
		}

		// ---------Preparing Answers for selected Assessment ---------

		mv.addObject("assessmentCategory", assessmentCategory);
		mv.addObject("allControlsforSelectedCat", controlModel);
		return mv;
	}

	@RequestMapping(value = "/addControl")
	public String handleAddAccSpecControlSubmission(
			@ModelAttribute("id") long id,
			@ModelAttribute("assCatId") long assCatId) {

		Control control = controlDao.getControl(id);
		AssessmentCategories assessmentCategory = assessmentDao
				.getAssessmentCategoryById(assCatId);

		AccountSpecificControl accountSpecificControl = new AccountSpecificControl();
		accountSpecificControl.setAssessmentCategories(assessmentCategory);
		accountSpecificControl.setControl(control);

		controlDao.addAccSpecControl(accountSpecificControl);

		assessmentCategory.setStatus("I");
		assessmentDao.updateAssessmentCategory(assessmentCategory);

		Assessment assessment = assessmentCategory.getAssessment();
		assessment.setAssessmentStatus(Util.INCOMPLETE_ASSESSMENT);
		assessmentDao.updateAssessment(assessment);

		return "redirect:/approver/addAccSpecControls?operation=accSpecControlAdded&assCatId="
				+ assessmentCategory.getId();
	}

	@RequestMapping(value = "/removeControl")
	public String handleRemoveSpecControlubmission(
			@ModelAttribute("id") long id,
			@ModelAttribute("assCatId") long assCatId) {

		Control control = controlDao.getControl(id);
		AssessmentCategories assessmentCategory = assessmentDao
				.getAssessmentCategoryById(assCatId);
		AccountSpecificControl accountSpecificControlforAssessmentCat = controlDao
				.accSpecificControlByCtrl(control, assessmentCategory);
		Answer answer = controlDao
				.getAnswerByAccSpecControl(accountSpecificControlforAssessmentCat);

		if (answer == null) {
			controlDao
					.deleteAccSpecControl(accountSpecificControlforAssessmentCat);
			return "redirect:/approver/addAccSpecControls?operation=accSpecControlremoved&assCatId="
					+ assessmentCategory.getId();
		} else {
			return "redirect:/approver/addAccSpecControls?operation=accSpecControlNotRemoved&assCatId="
					+ assessmentCategory.getId();
		}
	}

	@RequestMapping(value = "/responsePage")
	public ModelAndView responsePage(
			@RequestParam(name = "accSpecCtrlId", required = false) Long accSpecCtrlId) {

		ModelAndView mv = new ModelAndView("responsePage");
		mv.addObject("title", "Assessment Page");

		AccountSpecificControl accountSpecificControl = controlDao
				.getAccSpecControl(accSpecCtrlId);
		Answer answer = controlDao
				.getAnswerByAccSpecControl(accountSpecificControl);
		String[] answerOpions = accountSpecificControl.getControl()
				.getAnswers().split("/");

		if (answer == null) {
			answer = new Answer();
			answer.setControl(accountSpecificControl);
		}
		mv.addObject("answerOpions", answerOpions);
		mv.addObject("answer", answer);
		mv.addObject("userRole", "approver");
		return mv;
	}

	@RequestMapping(value = "/deleteAssessmentArtifact")
	public String deleteAssessmentArtifact(
			@Valid @ModelAttribute("answerId") Long answerId,
			BindingResult results) {
		Answer answer1 = controlDao.getAnswer(answerId);
		answer1.setArtifaceName(null);
		answer1.setArtifact(null);
		answer1.setArtifactType(null);
		answer1.setLastRespondedUser(userDao.getUserByEmailId(globalController
				.getUserModel().getEmail()));
		controlDao.updateAnswer(answer1);

		AnswerTrail answerTrail = new AnswerTrail();
		answerTrail.setAnswerId(answer1.getId());
		answerTrail.setAnswer(answer1.getAnswer());
		answerTrail.setArtifact(null);
		answerTrail.setArtifaceName("Artifact deleted by "
				+ answer1.getLastRespondedUser().getName());
		answerTrail.setArtifactType(null);
		answerTrail.setComment(answer1.getComment());
		answerTrail.setControl(answer1.getControl());
		answerTrail.setDateAnswered(new Timestamp(new Date().getTime()));
		answerTrail.setLastRespondedUser(userDao
				.getUserByEmailId(globalController.getUserModel().getEmail()));
		answerTrail.setReviewerComment(answer1.getReviewerComment());
		controlDao.addAnswerTrail(answerTrail);

		return "redirect:/approver/responsePage?accSpecCtrlId="
				+ answer1.getControl().getId();
	}

	@RequestMapping(value = "/saveAnswer", method = RequestMethod.POST)
	public String handleResponseSubmission(
			@Valid @ModelAttribute("answer") Answer answer,
			BindingResult results, Model model,
			@RequestParam("file") MultipartFile file) {

		System.out.println("*******************File:"
				+ file.getOriginalFilename());
		System.out.println("****************ContentType:"
				+ file.getContentType());

		try {
			if (file.getOriginalFilename().equals("")) {
				answer.setArtifact(null);
				answer.setArtifaceName(null);
				answer.setArtifactType(null);
			} else {
				Blob b = new SerialBlob(file.getBytes());
				answer.setArtifact(b);
				answer.setArtifaceName(file.getOriginalFilename());
				answer.setArtifactType(file.getContentType());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		AccountSpecificControl accountSpecificControl = controlDao
				.getAccSpecControl(answer.getControl().getId());
		Answer answer1 = controlDao
				.getAnswerByAccSpecControl(accountSpecificControl);

		if (answer1 == null) {
			answer.setControl(accountSpecificControl);
			answer.setLastRespondedUser(userDao
					.getUserByEmailId(globalController.getUserModel()
							.getEmail()));
			answer.setDateAnswered(new Timestamp(new Date().getTime()));
			Long answerId = controlDao.saveAnswer(answer);

			AnswerTrail answerTrail = new AnswerTrail();
			answerTrail.setAnswerId(answerId);
			answerTrail.setAnswer(answer.getAnswer());
			answerTrail.setArtifact(answer.getArtifact());
			answerTrail.setArtifaceName(answer.getArtifaceName());
			answerTrail.setArtifactType(answer.getArtifactType());
			answerTrail.setComment(answer.getComment());
			answerTrail.setControl(answer.getControl());
			answerTrail.setDateAnswered(answer.getDateAnswered());
			answerTrail.setLastRespondedUser(answer.getLastRespondedUser());
			answerTrail.setReviewerComment(answer.getReviewerComment());
			controlDao.addAnswerTrail(answerTrail);

			return "redirect:/approver/assessmentPage?operation=newAnswerAdded&assessmentId="
					+ accountSpecificControl.getAssessmentCategories()
							.getAssessment().getId()
					+ "&catId="
					+ accountSpecificControl.getAssessmentCategories().getId();
		} else {
			answer1.setAnswer(answer.getAnswer());
			answer1.setComment(answer.getComment());
			answer1.setLastRespondedUser(userDao
					.getUserByEmailId(globalController.getUserModel()
							.getEmail()));
			answer1.setDateAnswered(new Timestamp(new Date().getTime()));
			if (answer.getArtifact() != null && answer1.getArtifact() == null) {
				answer1.setArtifact(answer.getArtifact());
				answer1.setArtifaceName(answer.getArtifaceName());
				answer1.setArtifactType(answer.getArtifactType());
			}
			controlDao.updateAnswer(answer1);

			AnswerTrail answerTrail = new AnswerTrail();
			answerTrail.setAnswerId(answer1.getId());
			answerTrail.setAnswer(answer1.getAnswer());
			answerTrail.setArtifact(answer1.getArtifact());
			answerTrail.setArtifaceName(answer1.getArtifaceName());
			answerTrail.setArtifactType(answer1.getArtifactType());
			answerTrail.setComment(answer1.getComment());
			answerTrail.setControl(answer1.getControl());
			answerTrail.setDateAnswered(answer1.getDateAnswered());
			answerTrail.setLastRespondedUser(answer1.getLastRespondedUser());
			answerTrail.setReviewerComment(answer1.getReviewerComment());
			controlDao.addAnswerTrail(answerTrail);

			return "redirect:/approver/assessmentPage?operation=updatedAnswer&assessmentId="
					+ accountSpecificControl.getAssessmentCategories()
							.getAssessment().getId()
					+ "&catId="
					+ accountSpecificControl.getAssessmentCategories().getId();
		}

	}

	@RequestMapping("/download/{answerId}")
	public String download(@PathVariable("answerId") Long answerId,
			HttpServletResponse response) {

		Answer answer = controlDao.getAnswer(answerId);
		try {
			response.setHeader("Content-Disposition", "inline;filename=\""
					+ answer.getArtifaceName() + "\"");
			OutputStream out = response.getOutputStream();
			response.setContentType(answer.getArtifactType());
			IOUtils.copy(answer.getArtifact().getBinaryStream(), out);
			out.flush();
			out.close();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	@RequestMapping("/downloadExcel/{assessmentId}")
	public String downloadExcel(
			@PathVariable("assessmentId") Long assessmentId,
			HttpServletRequest request, HttpServletResponse response) {

		Assessment assessment = assessmentDao.getAssessmentById(assessmentId);
		try {
			downloadExcel.getAssessmentExcel(request, response, assessment);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@RequestMapping("/downloadPdf/{assessmentId}")
	public String downloadPdf(@PathVariable("assessmentId") Long assessmentId,
			HttpServletRequest request, HttpServletResponse response) {

		Assessment assessment = assessmentDao.getAssessmentById(assessmentId);
		try {
			System.out.println("assessment id is  [" + assessmentId + "]");
			pdfSections.generatePdf(request, response, assessmentId);
			downloadExcel.getAssessmentExcel(request, response, assessment);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@RequestMapping("/downloadAnswerTrail/{answerTrailId}")
	public String downloadAnswerTrail(
			@PathVariable("answerTrailId") Long answerTrailId,
			HttpServletResponse response) {

		AnswerTrail answerTrail = controlDao.getAnswerTrail(answerTrailId);
		try {
			response.setHeader("Content-Disposition", "inline;filename=\""
					+ answerTrail.getArtifaceName() + "\"");
			OutputStream out = response.getOutputStream();
			response.setContentType(answerTrail.getArtifactType());
			IOUtils.copy(answerTrail.getArtifact().getBinaryStream(), out);
			out.flush();
			out.close();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	@RequestMapping(value = "/history")
	public ModelAndView answerHistory(
			@Valid @ModelAttribute("answerId") Long answerId,
			BindingResult results, Model model, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("history");
		mv.addObject("title", "Assessment Page");

		Answer answer = controlDao.getAnswer(answerId);
		List<AnswerTrail> answerTrails = controlDao
				.allAnswerTrailByAnswer(answer);
		mv.addObject("answer", answer);
		mv.addObject("answerTrails", answerTrails);

		return mv;
	}

	@RequestMapping(value = "/assessmentHistory")
	public ModelAndView assessmentHistory(
			@Valid @ModelAttribute("assessmentId") Long assessmentId,
			BindingResult results, Model model, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("assessmentHistory");
		mv.addObject("title", "Assessment History Page");

		Assessment assessment = assessmentDao.getAssessmentById(assessmentId);
		List<AssessmentTrail> assessmentTrails = assessmentDao
				.getAssessmentTrailByAssessment(assessment);
		mv.addObject("assessmentTrails", assessmentTrails);

		return mv;
	}

	@RequestMapping(value = "/sendApproverMail")
	public ModelAndView sendApproverMail(
			@Valid @ModelAttribute("assessmentId") Long assessmentId,
			BindingResult results, Model model, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("sendApproverMail");
		mv.addObject("title", "asd");
		Assessment assessment = assessmentDao.getAssessmentById(assessmentId);

		List<String> users = new ArrayList<String>();
		users.add(assessment.getAssessor().getEmail());
		List<AssessmentCategories> assessmentCategories = assessmentDao
				.assessmentCategoriesByAssessment(assessment);
		if (assessmentCategories != null) {
			for (AssessmentCategories ascCat : assessmentCategories) {
				if (ascCat.getReviwer() != null) {
					if (!users.contains(ascCat.getReviwer().getEmail())) {
						users.add(ascCat.getReviwer().getEmail());
					}
				}
				List<AssessmentCategorySMEMapping> assessmentCategorySMEMappings = assessmentDao
						.getAssessmentCategorySmeMappingByAssCat(ascCat);
				if (assessmentCategorySMEMappings != null) {
					for (AssessmentCategorySMEMapping ascCatSm : assessmentCategorySMEMappings) {
						if (!users.contains(ascCatSm.getSME().getEmail())) {
							users.add(ascCatSm.getSME().getEmail());
						}
					}
				}
			}
		}

		String user = "";
		for (String u : users) {
			if (user.equals("")) {
				user = u;
			} else {
				user = user + "," + u;
			}
		}

		MailModel mailModel = new MailModel();
		mailModel.setTo(user);
		mailModel.setCc(assessment.getApprover().getEmail());
		mailModel.setSubject("MyRMC : Change/Update Notification for "
				+ assessment.getAccount().getDepartment().getName() + " - "
				+ assessment.getAccount().getLocation().getName());
		mailModel.setMessage("Client/Department : "
				+ assessment.getAccount().getDepartment().getName()
				+ "<br>Location : "
				+ assessment.getAccount().getLocation().getName()
				+ "<br>LOB : " + assessment.getAccount().getLob().getName());
		mv.addObject("mailModel", mailModel);
		return mv;
	}

	@RequestMapping(value = "/sendApproverMail", method = RequestMethod.POST)
	public String sendApproverMail(
			@Valid @ModelAttribute("mailModel") MailModel mailModel,
			BindingResult results, Model model, HttpServletRequest request) {

		System.out.println(mailModel.getTo());
		System.out.println(mailModel.getMessage());
		System.out.println(mailModel.getSubject());
		System.out.println(mailModel.getCc());

		mailService.sendApproverNotificationMail(mailModel);

		return "redirect:/approver/approverHome?operation=MailSent";
	}

	@RequestMapping(value = "/submitAssessment", method = RequestMethod.POST)
	public String submitAssessmentHandle(
			@Valid @ModelAttribute("assessmentModel") AssessmentModel assessmentModel,
			BindingResult results, Model model, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("history");
		mv.addObject("title", "Assessment Page");

		System.out.println("helllo there ! iam in submit assessment ");
		List<Assessment> assessments = assessmentModel.getAssessments();
		for (Assessment a : assessments) {
			if (a.getAssessmentStatus() != null) {
				String[] status = a.getAssessmentStatus().split("_");
				if (status[0].equals(Util.COMPLETE_ASSESSMENT)) {
					long assessmentId = Long.parseLong(status[1]);
					Assessment assessment = assessmentDao
							.getAssessmentById(assessmentId);
					List<AssessmentCategories> assessmentCategories = assessmentDao
							.assessmentCategoriesByAssessment(assessment);
					for (AssessmentCategories asc : assessmentCategories) {
						asc.setStatus("S");
						assessmentDao.updateAssessmentCategory(asc);
						List<Answer> answers = controlDao
								.allAnswerbyAssessmentCategory(asc);
						for (Answer answer : answers) {
							answer.setConfirmationStatus(Util.REVIEW_PENDING);
							if (answer.getAnswer().equals(Util.ANS_NO)
									&& answer.getControl().getControl()
											.getFlag() == 1) {
								answer.setNC(true);
							} else if (answer.getAnswer().equals(Util.ANS_YES)
									&& answer.getControl().getControl()
											.getFlag() == 2) {
								answer.setNC(true);
							} else {
								answer.setNC(false);
							}
							controlDao.updateAnswer(answer);

							AnswerCopy answerCopy = new AnswerCopy();
							answerCopy.setAnswerId(answer.getId());
							answerCopy.setAnswer(answer.getAnswer());
							answerCopy
									.setArtifaceName(answer.getArtifaceName());
							answerCopy.setArtifact(answer.getArtifact());
							answerCopy
									.setArtifactType(answer.getArtifactType());
							answerCopy.setComment(answer.getComment());
							answerCopy.setConfirmationStatus(answer
									.getConfirmationStatus());
							answerCopy.setControl(answer.getControl());
							answerCopy
									.setDateAnswered(answer.getDateAnswered());
							answerCopy.setLastRespondedUser(answer
									.getLastRespondedUser());
							answerCopy.setMitigationDate(answer
									.getMitigationDate());
							answerCopy.setNC(answer.isNC());
							answerCopy.setReviewerComment(answer
									.getReviewerComment());
							answerCopy.setRiskAcceptance(answer
									.isRiskAcceptance());
							answerCopy.setRiskAcceptenceby(answer
									.getRiskAcceptenceby());
							controlDao.saveAnswerCopy(answerCopy);
						}
					}
					assessment.setAssessmentStatus(Util.SUBMITTED_ASSESSMENT);
					System.out
							.println("assessment id to save the assessment is ["
									+ assessment.getId() + "]");
					System.out.println("risk value for assessment id ["
							+ assessment.getId()
							+ "] is ["
							+ riskCalculation
									.getRiskvalueforAssessment(assessment)
							+ "]");
					assessment.setRiskValue(riskCalculation
							.getRiskvalueforAssessment(assessment));
					assessment.setRiskLevel(riskCalculation
							.getRiskLevelforAssessment(assessment));
					assessmentDao.updateAssessment(assessment);

					AssessmentTrail assessmentTrail = new AssessmentTrail();
					assessmentTrail.setAssessmentId(assessmentId);
					assessmentTrail.setDateOfSubmission(new Timestamp(
							new Date().getTime()));
					assessmentTrail.setDeclaration(assessmentModel
							.getDeclaration());
					assessmentTrail.setSubmittedUser(userDao
							.getUserByEmailId(globalController.getUserModel()
									.getEmail()));
					assessmentDao.addAssessmentTrail(assessmentTrail);
				}
			}

		}

		return "redirect:/approver/approverHome?operation=submitted";
	}

	@RequestMapping(value = "/riskTracker")
	public ModelAndView riskTrackerPage(
			@RequestParam(name = "assessmentId", required = false) Long assessmentId,
			@RequestParam(name = "catId", required = false) Long catId,
			@RequestParam(name = "operation", required = false) String operation) {

		ModelAndView mv = new ModelAndView("riskTrackerPage");
		mv.addObject("title", "Risk Tracker Page");

		Assessment assessment = assessmentDao.getAssessmentById(assessmentId);

		if (!assessment.getApprover().getEmail()
				.equals(globalController.getUserModel().getEmail())) {
			mv = new ModelAndView("asdf");
		}
		if (operation != null) {
			if (operation.equals("saveRiskTrackerResponse")) {
				mv.addObject("msg",
						"!!! Answer has been Updated successfully !!!");
			}
		}

		List<AssessmentCategories> assessmentCategories = assessmentDao
				.assessmentCategoriesByAssessment(assessment);
		AssessmentCategories selectedAssessmentCategory = null;

		if (catId != null) {
			selectedAssessmentCategory = assessmentDao
					.getAssessmentCategoryById(catId);
		} else {
			if (assessmentCategories != null) {
				selectedAssessmentCategory = assessmentCategories.get(0);
			}
		}

		List<Answer> answers = controlDao
				.allAnswerbyAssessmentCategory(selectedAssessmentCategory);

		// ---------- countring according to confirmation status --------

		int totalAnswer = 0;
		int totalRisks = 0;
		int reviewPendingforNC = 0;
		int reviewPendingforNonNC = 0;
		int reviewCompleteforNC = 0;
		int reviewCompleteforNonNC = 0;
		int changeRequiredforNC = 0;
		int changeRequiredforNonNC = 0;

		for (Answer answer : answers) {
			System.out.println("answer artifact is [" + answer.getArtifact()
					+ "]");
			if (answer.isNC()) {
				totalRisks++;
			}
			if (answer.isNC()
					&& answer.getConfirmationStatus().equals(
							Util.REVIEW_PENDING)) {
				reviewPendingforNC++;
			} else if (answer.isNC()
					&& answer.getConfirmationStatus().equals(
							Util.REVIEW_COMPLETE)) {
				reviewCompleteforNC++;
			} else if (!answer.isNC()
					&& answer.getConfirmationStatus().equals(
							Util.REVIEW_PENDING)) {
				reviewPendingforNonNC++;
			} else if (!answer.isNC()
					&& answer.getConfirmationStatus().equals(
							Util.REVIEW_COMPLETE)) {
				reviewCompleteforNonNC++;
			} else if (answer.isNC()
					&& answer.getConfirmationStatus().equals(
							Util.CHANGE_REQUIRED)) {
				changeRequiredforNC++;
			} else if (!answer.isNC()
					&& answer.getConfirmationStatus().equals(
							Util.CHANGE_REQUIRED)) {
				changeRequiredforNonNC++;
			}
		}

		mv.addObject("totalAnswer", answers.size());
		mv.addObject("totalRisks", totalRisks);
		mv.addObject("reviewPendingforNC", reviewPendingforNC);
		mv.addObject("reviewPendingforNonNC", reviewPendingforNonNC);
		mv.addObject("reviewCompleteforNC", reviewCompleteforNC);
		mv.addObject("reviewCompleteforNonNC", reviewCompleteforNonNC);
		mv.addObject("changeRequiredforNC", changeRequiredforNC);
		mv.addObject("changeRequiredforNonNC", changeRequiredforNonNC);

		// --------------------------------------------------------------

		mv.addObject("answers", answers);
		mv.addObject("assessment", assessment);
		mv.addObject("assessmentCategories", assessmentCategories);
		mv.addObject("selectedAssessmentCategory", selectedAssessmentCategory);
		mv.addObject("userRole", "approver");
		return mv;
	}

	@RequestMapping(value = "/riskTrackerResponsePage")
	public ModelAndView riskTrackerResponsePage(
			@Valid @ModelAttribute("answerId") Long answerId,
			BindingResult results, Model model, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("riskTrackerResponse");
		mv.addObject("title", "Response Page");
		Answer answer = controlDao.getAnswer(answerId);
		String[] answerOpions = answer.getControl().getControl().getAnswers()
				.split("/");
		mv.addObject("answerOpions", answerOpions);
		mv.addObject("answer", answer);
		mv.addObject("userRole", "approver");
		return mv;
	}

	@RequestMapping(value = "/saveRiskTrackerAnswer", method = RequestMethod.POST)
	public String saveRiskTrackerAnswer(
			@Valid @ModelAttribute("answer") Answer answer,
			BindingResult results, Model model,
			@RequestParam("file") MultipartFile file) {

		System.out.println("*******************File:" + file);
		System.out.println("*******************File:"
				+ file.getOriginalFilename());
		System.out.println("****************ContentType:"
				+ file.getContentType());

		try {
			if (file != null) {
				if (file.getOriginalFilename().equals("")) {
					answer.setArtifact(null);
					answer.setArtifaceName(null);
					answer.setArtifactType(null);
				} else {
					Blob b = new SerialBlob(file.getBytes());
					answer.setArtifact(b);
					answer.setArtifaceName(file.getOriginalFilename());
					answer.setArtifactType(file.getContentType());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		Answer answer1 = controlDao.getAnswer(answer.getId());
		answer1.setAnswer(answer.getAnswer());
		answer1.setComment(answer.getComment());
		answer1.setLastRespondedUser(userDao.getUserByEmailId(globalController
				.getUserModel().getEmail()));
		System.out.println("risk acceptance by current user is ["
				+ answer.isRiskAcceptance() + "]");
		System.out.println("risk mitigation date is ["
				+ answer.getMitigationDate() + "]");

		if (answer.isRiskAcceptance()) {
			answer1.setMitigationDate(null);
			answer1.setRiskAcceptance(answer.isRiskAcceptance());
			answer1.setRiskAcceptenceby("Risk Accepted by "
					+ globalController.getUserModel().getFullName());
		} else {
			answer1.setMitigationDate(answer.getMitigationDate());
			answer1.setRiskAcceptance(answer.isRiskAcceptance());
			answer1.setRiskAcceptenceby(null);
		}

		if (answer.getArtifact() != null && answer1.getArtifact() == null) {
			answer1.setArtifact(answer.getArtifact());
			answer1.setArtifaceName(answer.getArtifaceName());
			answer1.setArtifactType(answer.getArtifactType());
		}
		answer1.setDateAnswered(new Timestamp(new Date().getTime()));
		answer1.setConfirmationStatus(Util.REVIEW_PENDING);
		if (answer.getAnswer().equals(Util.ANS_NO)
				&& answer1.getControl().getControl().getFlag() == 1) {
			answer1.setNC(true);
		} else if (answer.getAnswer().equals(Util.ANS_YES)
				&& answer1.getControl().getControl().getFlag() == 2) {
			answer1.setNC(true);
		} else {
			answer1.setNC(false);
		}
		controlDao.updateAnswer(answer1);

		Assessment assessment = answer1.getControl().getAssessmentCategories()
				.getAssessment();
		assessment.setRiskValue(riskCalculation
				.getRiskvalueforAssessment(assessment));
		assessment.setRiskLevel(riskCalculation
				.getRiskLevelforAssessment(assessment));
		assessmentDao.updateAssessment(assessment);

		AnswerTrail answerTrail = new AnswerTrail();
		answerTrail.setAnswerId(answer1.getId());
		answerTrail.setAnswer(answer1.getAnswer());
		answerTrail.setArtifact(answer1.getArtifact());
		answerTrail.setArtifaceName(answer1.getArtifaceName());
		answerTrail.setArtifactType(answer1.getArtifactType());
		answerTrail.setComment(answer1.getComment());
		answerTrail.setControl(answer1.getControl());
		answerTrail.setDateAnswered(answer1.getDateAnswered());
		answerTrail.setLastRespondedUser(answer1.getLastRespondedUser());
		answerTrail.setReviewerComment(answer1.getReviewerComment());
		answerTrail.setRiskAcceptance(answer1.isRiskAcceptance());
		answerTrail.setRiskAcceptenceby(answer1.getRiskAcceptenceby());
		answerTrail.setMitigationDate(answer1.getMitigationDate());
		answerTrail.setNC(answer1.isNC());
		answerTrail.setConfirmationStatus(answer1.getConfirmationStatus());
		controlDao.addAnswerTrail(answerTrail);
		System.out.println("a11");
		// controlDao.addAnswerTrail(answerTrail);
		/*-return "redirect:/approver/assessmentPage?operation=updatedAnswer&assessmentId="
				+ accountSpecificControl.getAssessmentCategories()
						.getAssessment().getId()
				+ "&catId="
				+ accountSpecificControl.getAssessmentCategories().getId();*/
		return "redirect:/approver/riskTracker?assessmentId="
				+ answer1.getControl().getAssessmentCategories()
						.getAssessment().getId() + "&catId="
				+ answer1.getControl().getAssessmentCategories().getId()
				+ "&operation=saveRiskTrackerResponse";

	}

	@RequestMapping(value = "/deleteArtifact")
	public String deleteArtifact(
			@Valid @ModelAttribute("answerId") Long answerId,
			BindingResult results) {
		Answer answer1 = controlDao.getAnswer(answerId);
		answer1.setArtifaceName(null);
		answer1.setArtifact(null);
		answer1.setArtifactType(null);
		answer1.setLastRespondedUser(userDao.getUserByEmailId(globalController
				.getUserModel().getEmail()));
		controlDao.updateAnswer(answer1);

		AnswerTrail answerTrail = new AnswerTrail();
		answerTrail.setAnswerId(answer1.getId());
		answerTrail.setAnswer(answer1.getAnswer());
		answerTrail.setArtifact(null);
		answerTrail.setArtifaceName("Artifact deleted by "
				+ answer1.getLastRespondedUser().getName());
		answerTrail.setArtifactType(null);
		answerTrail.setComment(answer1.getComment());
		answerTrail.setControl(answer1.getControl());
		answerTrail.setDateAnswered(new Timestamp(new Date().getTime()));
		answerTrail.setMitigationDate(answer1.getMitigationDate());
		answerTrail.setRiskAcceptance(answer1.isRiskAcceptance());
		answerTrail.setRiskAcceptenceby(answer1.getRiskAcceptenceby());
		answerTrail.setLastRespondedUser(answer1.getLastRespondedUser());
		answerTrail.setReviewerComment(answer1.getReviewerComment());
		controlDao.addAnswerTrail(answerTrail);

		return "redirect:/approver/riskTrackerResponsePage";
	}

	@RequestMapping(value = "/riskAnalysis")
	public ModelAndView riskAnalysisPage(
			@Valid @ModelAttribute("assId") Long assId, BindingResult results,
			Model model, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("riskAnalysis");
		mv.addObject("title", "Risk Analysis");
		Assessment assessment = assessmentDao.getAssessmentById(assId);

		List<AssessmentCategories> assessmentCategories = assessmentDao
				.assessmentCategoriesByAssessment(assessment);
		System.out.println("total no of assessment categories are ["
				+ assessmentCategories.size() + "]");

		// ---------------Category wise risk -----------------
		if (assessmentCategories.size() > 0) {
			String category = "";
			String categoryRisk = "";
			for (AssessmentCategories asc : assessmentCategories) {

				category = category.concat(asc.getAssignedCategories()
						.getName() + ",");
				categoryRisk = categoryRisk.concat(String
						.valueOf(riskCalculation
								.getRiskValueforAssessmentCategory(asc))
						+ ",");
			}
			System.out.println("category length [" + category.length()
					+ "] and {" + category + "}");
			System.out.println("categoryRisk length [" + categoryRisk.length()
					+ "]");
			category = category.substring(0, category.length() - 1);
			categoryRisk = categoryRisk.substring(0, categoryRisk.length() - 1);

			System.out.println("categories are {" + category + "}");
			System.out.println("category risk are {" + categoryRisk + "}");
			mv.addObject("categories", category);
			mv.addObject("categoryRisk", categoryRisk);
		} else {
			String category = "";
			String categoryRisk = "";
			mv.addObject("categories", category);
			mv.addObject("categoryRisk", categoryRisk);
		}
		// -------------------------------------------------------

		// --------------------internal / external risk ------------
		int internalRisk = riskCalculation.getRiskLevelByRiskType(assessment,
				Util.INTERNAL_RISK);
		int externalRisk = riskCalculation.getRiskLevelByRiskType(assessment,
				Util.EXTERNAL_RISK);
		int bothRisk = riskCalculation.getRiskLevelByRiskType(assessment,
				Util.BOTH);
		String intExtRisk = "Internal,External,Both";
		String intExtRiskVal = internalRisk + "," + externalRisk + ","
				+ bothRisk;
		mv.addObject("intExtRisk", intExtRisk);
		mv.addObject("intExtRiskVal", intExtRiskVal);

		mv.addObject("criticalRiskFactor",
				riskCalculation.getCriticalRiskvalueforAssessment(assessment));
		mv.addObject("totalRiskFactor", assessment.getRiskValue());

		mv.addObject("currentRiskRating", assessment.getRiskLevel());
		mv.addObject("initialRiskRating", assessment.getAccount()
				.getInitialRating());

		mv.addObject("assessment", assessment);
		mv.addObject("assessmentId", assessment.getId());
		mv.addObject("assessmentCategories", assessmentCategories);
		return mv;
	}

	@RequestMapping(value = "/assessmentCatChart")
	public ModelAndView assessmentCatChart(
			@Valid @ModelAttribute("assCatId") Long assCatId) {
		ModelAndView mv = new ModelAndView("assessmentCatChart");
		mv.addObject("title", "Risk Analysis");
		mv.addObject("assessmentCatId", assCatId);

		AssessmentCategories assessmentCategory = assessmentDao
				.getAssessmentCategoryById(assCatId);

		List<Answer> answers = controlDao
				.allAnswerbyAssessmentCategory(assessmentCategory);
		System.out.println("total no of answer for category ["
				+ assessmentCategory.getAssignedCategories().getName()
				+ "] is [" + answers.size() + "]");
		String answerText = "";
		String answerVal = "";
		for (Answer answer : answers) {
			System.out.println("answer is  [" + answer.getAnswer() + "]");
			if (riskCalculation.getRiskForAnswer(answer) > 0) {
				System.out.println("[" + answer.getId() + "]["
						+ riskCalculation.getRiskForAnswer(answer) + "]");
				answerText = answerText.concat(answer.getControl().getControl()
						.getShortText()
						+ ",");
				answerVal = answerVal.concat(String.valueOf(riskCalculation
						.getRiskForAnswer(answer)) + ",");
			}
		}

		System.out.println("uityuiytu[" + answerVal + "]");
		if (answerText.equals("")) {
			mv.addObject("answerText", null);
			mv.addObject("answerVal", null);
		} else {
			answerText = answerText.substring(0, answerText.length() - 1);
			answerVal = answerVal.substring(0, answerVal.length() - 1);
			mv.addObject("answerText", answerText);
			mv.addObject("answerVal", answerVal);
		}

		System.out
				.println("first ajax request and the assessment catefory id is ["
						+ assCatId + "]");

		return mv;
	}

	private List<Assessment> getActivatedAccount(List<Assessment> assessment) {
		List<Assessment> activeAssessment = new ArrayList<Assessment>();
		for (Assessment as : assessment) {
			if (as.getAccount().getState().equals(Util.ACTIVATED_ACCOUNT)
					&& !as.getAccount().getPhase().equals(Util.SUNSET_PHASE)) {
				activeAssessment.add(as);
			}
		}
		return activeAssessment;
	}

	private List<Assessment> getAssessmentByStatus(
			List<Assessment> assessments, String status) {
		List<Assessment> activeAssessment = new ArrayList<Assessment>();
		for (Assessment as : assessments) {
			if (as.getAssessmentStatus().equals(status)) {
				activeAssessment.add(as);
			}
		}
		return activeAssessment;
	}
}
