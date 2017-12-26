package net.gh.ghoshMyRmc.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import net.gh.ghoshMyRmc.mailService.MailService;
import net.gh.ghoshMyRmc.mailService.SendMail;
import net.gh.ghoshMyRmc.model.AccSpecControlModel;
import net.gh.ghoshMyRmc.model.AccountTransferModel;
import net.gh.ghoshMyRmc.model.AnswerModel;
import net.gh.ghoshMyRmc.pdfGeneration.PdfSections;
import net.gh.ghoshMyRmc.riskAnalysis.DownloadExcel;
import net.gh.ghoshMyRmc.riskAnalysis.RiskCalculation;
import net.gh.ghoshMyRmc.validator.UserValidator;
import net.gh.ghoshMyRmcBackend.Util;
import net.gh.ghoshMyRmcBackend.dao.AccountDao;
import net.gh.ghoshMyRmcBackend.dao.AssessmentDao;
import net.gh.ghoshMyRmcBackend.dao.CategoryDao;
import net.gh.ghoshMyRmcBackend.dao.ControlDao;
import net.gh.ghoshMyRmcBackend.dao.UserDao;
import net.gh.ghoshMyRmcBackend.dto.Account;
import net.gh.ghoshMyRmcBackend.dto.AccountSpecificControl;
import net.gh.ghoshMyRmcBackend.dto.Answer;
import net.gh.ghoshMyRmcBackend.dto.AnswerCopy;
import net.gh.ghoshMyRmcBackend.dto.AnswerTrail;
import net.gh.ghoshMyRmcBackend.dto.Assessment;
import net.gh.ghoshMyRmcBackend.dto.AssessmentCategories;
import net.gh.ghoshMyRmcBackend.dto.AssessmentCategorySMEMapping;
import net.gh.ghoshMyRmcBackend.dto.AssessmentTrail;
import net.gh.ghoshMyRmcBackend.dto.Category;
import net.gh.ghoshMyRmcBackend.dto.Control;
import net.gh.ghoshMyRmcBackend.dto.Country;
import net.gh.ghoshMyRmcBackend.dto.Department;
import net.gh.ghoshMyRmcBackend.dto.LOB;
import net.gh.ghoshMyRmcBackend.dto.Location;
import net.gh.ghoshMyRmcBackend.dto.User;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

	/**
	 * @author anurag ghosh
	 */

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
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private GlobalController globalController;

	@Autowired
	private RiskCalculation riskCalculation;

	@Autowired
	private DownloadExcel downloadExcel;

	@Autowired
	private MailService mailService;

	@Autowired
	private PdfSections pdfSections;

	// ------------------Admin home ----------------------

	@RequestMapping(value = "/adminhome")
	public ModelAndView home() {

		ModelAndView mv = new ModelAndView("adminHome");
		mv.addObject("title", "Welcome Admin Home");
		mv.addObject("totalAccounts", true);

		List<Assessment> assessments = assessmentDao.assessmentList();
		if (assessments == null) {
			assessments = new ArrayList<Assessment>();
		}

		mv.addObject("assessments", assessments);
		mv.addObject("total", assessments.size());

		List<Assessment> completeAssessments = assessmentDao
				.assessmentListByStatus(Util.COMPLETE_ASSESSMENT);
		if (completeAssessments == null) {
			completeAssessments = new ArrayList<Assessment>();
		}
		mv.addObject("completed", completeAssessments.size());

		List<Assessment> submitAssessments = assessmentDao
				.assessmentListByStatus(Util.SUBMITTED_ASSESSMENT);
		if (submitAssessments == null) {
			submitAssessments = new ArrayList<Assessment>();
		}
		mv.addObject("submitted", submitAssessments.size());

		List<Assessment> incompleteAssessments = assessmentDao
				.assessmentListByStatus(Util.INCOMPLETE_ASSESSMENT);
		if (incompleteAssessments == null) {
			incompleteAssessments = new ArrayList<Assessment>();
		}
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

		List<AssessmentCategories> assessmentCategories = assessmentDao
				.assessmentCategoriesByAssessment(assessment);
		System.out.println("assessment Categories are [" + assessmentCategories
				+ "] and size is [" + assessmentCategories.size() + "]");
		AssessmentCategories selectedAssessmentCategory = null;
		if (catId != null) {
			selectedAssessmentCategory = assessmentDao
					.getAssessmentCategoryById(catId);
		} else {
			if (assessmentCategories != null) {
				if (assessmentCategories.size() > 0) {
					selectedAssessmentCategory = assessmentCategories.get(0);
				}
			}
		}

		// ---------Preparing Answers for selected Assessment ---------
		List<AccountSpecificControl> accountSpecificControls = new ArrayList<AccountSpecificControl>();
		List<Answer> answers = new ArrayList<Answer>();
		if (selectedAssessmentCategory != null) {
			accountSpecificControls = controlDao
					.accSpecControlByCategory(selectedAssessmentCategory);
			answers = controlDao
					.allAnswerbyAssessmentCategory(selectedAssessmentCategory);
		}
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

	@RequestMapping(value = "/riskTracker")
	public ModelAndView riskTrackerPage(
			@RequestParam(name = "assessmentId", required = false) Long assessmentId,
			@RequestParam(name = "catId", required = false) Long catId,
			@RequestParam(name = "operation", required = false) String operation) {

		ModelAndView mv = new ModelAndView("riskTrackerPage");
		mv.addObject("title", "Risk Tracker Page");

		Assessment assessment = assessmentDao.getAssessmentById(assessmentId);

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
		mv.addObject("userRole", "admin");
		return mv;
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

	@RequestMapping(value = "/page")
	public ModelAndView index() {
		ModelAndView mv = new ModelAndView("page");
		return mv;
	}

	// ------------------------Category ---------------
	@RequestMapping(value = "/categories", method = RequestMethod.GET)
	public ModelAndView categories(
			@RequestParam(name = "operation", required = false) String operation) {
		ModelAndView mv = new ModelAndView("page");
		mv.addObject("title", "categories");
		mv.addObject("userClickCategory", true);
		mv.addObject("categories", categoryDao.categoryLists());

		if (operation != null) {
			if (operation.equals("categoryAdded")) {
				mv.addObject("msg", "Category Added Successfully");
			} else if (operation.equals("categoryUpdated")) {
				mv.addObject("msg", "Category Updated Successfully");
			}
		}

		Category newCategory = new Category();
		mv.addObject("category", newCategory);
		return mv;
	}

	@RequestMapping(value = "/addNewCategory", method = RequestMethod.POST)
	public String handleCategorySubmission(
			@Valid @ModelAttribute("category") Category category,
			BindingResult results, Model model, HttpServletRequest request) {

		// new SpringValidator().validate(category, results);

		/*-if (results.hasErrors()) {
			model.addAttribute("title", "categories");
			model.addAttribute("userClickCategory", true);
			return "page";
		}*/

		/*-if (!category.getMultipartFile().getOriginalFilename().equals("")) {
			System.out.println("multipart file is ["
					+ category.getMultipartFile().getOriginalFilename() + "]");
			FileUploadUtility.uploadFile(request, category.getMultipartFile(),
					category.getId());
		}*/
		categoryDao.addCategory(category);
		return "redirect:/admin/categories?operation=categoryAdded";
	}

	@RequestMapping(value = "/editcategory")
	public ModelAndView editcategorySubmission(
			@RequestParam(name = "categoryId", required = false) Long categoryId) {
		ModelAndView mv = new ModelAndView("editCategoryPage");
		System.out.println("category id is  [" + categoryId + "]");
		Category category = categoryDao.getCategory(categoryId);
		mv.addObject("category", category);
		return mv;
	}

	@RequestMapping(value = "/updateCategory", method = RequestMethod.POST)
	public String updateCategory(
			@Valid @ModelAttribute("category") Category category,
			BindingResult results, Model model, HttpServletRequest request) {
		categoryDao.updateCategory(category);
		return "redirect:/admin/categories?operation=categoryUpdated";
	}

	// --------------------Controls ------------------

	@RequestMapping(value = "/controls", method = RequestMethod.GET)
	public ModelAndView controls(
			@RequestParam(name = "operation", required = false) String operation) {
		ModelAndView mv = new ModelAndView("page");
		mv.addObject("title", "controls");
		mv.addObject("userClickControls", true);
		mv.addObject("controls", controlDao.controlLists());

		if (operation != null) {
			if (operation.equals("controlAdded")) {
				mv.addObject("msg", "Control Added Successfully");
			} else if (operation.equals("controlUpdated")) {
				mv.addObject("msg", "Control has been Successfully Updated");
			}
		}

		Control newControl = new Control();
		newControl.setAnswers(Util.ANS_YES + "/" + Util.ANS_NO + "/"
				+ Util.ANS_NA);
		newControl.setScreen(Util.NA);
		mv.addObject("control", newControl);

		return mv;
	}

	@RequestMapping(value = "/addNewControl", method = RequestMethod.POST)
	public String handleControlSubmission(
			@Valid @ModelAttribute("control") Control control,
			BindingResult results, Model model, HttpServletRequest request) {
		System.out.println("category id is [" + control.getCategory().getId()
				+ "]");
		controlDao.addControl(control);
		return "redirect:/admin/controls?operation=controlAdded";
	}

	@RequestMapping(value = "/editControl")
	public ModelAndView editControlSubmission(
			@RequestParam(name = "controlId", required = false) Long controlId) {
		ModelAndView mv = new ModelAndView("editControlPage");

		Control control = controlDao.getControl(controlId);
		mv.addObject("control", control);
		return mv;
	}

	@RequestMapping(value = "/updateControl", method = RequestMethod.POST)
	public String updateCategory(
			@Valid @ModelAttribute("control") Control control,
			BindingResult results, Model model, HttpServletRequest request) {
		controlDao.updateControl(control);
		return "redirect:/admin/controls?operation=controlUpdated";
	}

	// --------------------------Roles -------------------

	@RequestMapping(value = { "/roles" })
	public ModelAndView roles() {
		ModelAndView mv = new ModelAndView("page");
		mv.addObject("title", "roles");
		mv.addObject("userClickRoles", true);
		if (globalController.getUserModel().getRole().equals(Util.SUPERADMIN)) {
			mv.addObject("roles", Util.getAllRolesForSuperAdmin());
		} else if (globalController.getUserModel().getRole().equals(Util.ADMIN)) {
			mv.addObject("roles", Util.getAllRoles());
		}
		return mv;
	}

	// ------------------ users ----------------------

	@RequestMapping(value = "/users")
	public ModelAndView users(
			@RequestParam(name = "operation", required = false) String operation) {
		ModelAndView mv = new ModelAndView("page");
		mv.addObject("title", "users");
		mv.addObject("userClickusers", true);
		mv.addObject("users", userDao.userLists());

		if (operation != null) {
			if (operation.equals("userAdded")) {
				mv.addObject("msg", "User Added Successfully");
			}
		}

		User user = new User();
		mv.addObject("user", user);
		return mv;
	}

	@RequestMapping(value = "/addNewUser")
	public ModelAndView addNewUser() {
		ModelAndView mv = new ModelAndView("page");
		mv.addObject("title", "users");
		mv.addObject("userClickAddNewUsers", true);
		User user = new User();
		mv.addObject("user", user);
		return mv;
	}

	@RequestMapping(value = "/saveNewUser", method = RequestMethod.POST)
	public String handleUserSubmission(
			@Valid @ModelAttribute("user") User user, BindingResult results,
			Model model, HttpServletRequest request) {

		System.out.println("here i am");

		new UserValidator(userDao.getUserByEmailId(user.getEmail())).validate(
				user, results);
		System.out.println("here i am1");
		if (results.hasErrors()) {
			model.addAttribute("title", "AddNewUser");
			model.addAttribute("userClickAddNewUsers", true);
			return "page";
		}

		String randomPassword = Util.getSaltString();

		user.setPassword(passwordEncoder.encode(randomPassword));
		user.setEnabled(false);
		userDao.addUser(user);

		mailService.sendMailToNewUser(user, randomPassword);

		return "redirect:/admin/users?operation=userAdded";
	}

	// --------------------------Client / Department -------------------

	@RequestMapping(value = { "/department" })
	public ModelAndView department(
			@RequestParam(name = "operation", required = false) String operation) {
		ModelAndView mv = new ModelAndView("page");
		mv.addObject("title", "Client / Department");
		mv.addObject("userClickDepartment", true);
		mv.addObject("departments", accountDao.departmentLists());
		mv.addObject("dapartment", new Department());
		if (operation != null) {
			if (operation.equals("departmentAdded")) {
				mv.addObject("msg",
						"New Department has been Added Successfully");
			} else if (operation.equals("departmentAvailable")) {
				mv.addObject("msg",
						"Department with same name is already available");
			} else if (operation.equals("departmentUpdated")) {
				mv.addObject("msg", "Department has been Updated Successfully");
			}
		}
		return mv;
	}

	@RequestMapping(value = "/addNewDepartment", method = RequestMethod.POST)
	public String handleDepartmentSubmission(
			@Valid @ModelAttribute("dapartment") Department department,
			BindingResult results, Model model, HttpServletRequest request) {
		Department existingDepartment = accountDao
				.getDepartmentByName(department.getName());

		if (existingDepartment != null) {
			return "redirect:/admin/department?operation=departmentAvailable";
		}

		accountDao.addDepartment(department);
		return "redirect:/admin/department?operation=departmentAdded";
	}

	@RequestMapping(value = "/editDepartment")
	public ModelAndView editDepartmentSubmission(
			@RequestParam(name = "depId", required = false) Long depId) {
		ModelAndView mv = new ModelAndView("editDepartmentPage");
		Department department = accountDao.getDepartment(depId);
		mv.addObject("department1", department);
		return mv;
	}

	@RequestMapping(value = "/updateDepartment", method = RequestMethod.POST)
	public String updateDepartment(
			@Valid @ModelAttribute("department1") Department department1,
			BindingResult results, Model model, HttpServletRequest request) {
		accountDao.updateDepartment(department1);
		return "redirect:/admin/department?operation=departmentUpdated";
	}

	// --------------------------Location -------------------

	@RequestMapping(value = "/location")
	public ModelAndView location(
			@RequestParam(name = "operation", required = false) String operation) {
		ModelAndView mv = new ModelAndView("page");
		mv.addObject("title", "Locations");
		mv.addObject("userClickLocation", true);
		mv.addObject("locations", accountDao.locationLists());
		mv.addObject("location", new Location());
		if (operation != null) {
			if (operation.equals("locationAdded")) {
				mv.addObject("msg", "New Location has been Added Successfully");
			} else if (operation.equals("locationAvailable")) {
				mv.addObject("msg",
						"Location with same name is already available");
			} else if (operation.equals("locationUpdated")) {
				mv.addObject("msg", "Location has been Updated Successfully");
			}
		}
		return mv;
	}

	@RequestMapping(value = "/addNewLocation", method = RequestMethod.POST)
	public String handleLocationSubmission(
			@Valid @ModelAttribute("location") Location location,
			BindingResult results, Model model, HttpServletRequest request) {
		Location existingLocation = accountDao.getLocationByName(location
				.getName());

		if (existingLocation != null) {
			return "redirect:/admin/location?operation=locationAvailable";
		}

		accountDao.addLocation(location);
		return "redirect:/admin/location?operation=locationAdded";
	}

	@RequestMapping(value = "/editLocation")
	public ModelAndView editLocation(
			@RequestParam(name = "locationId", required = false) Long locationId) {
		ModelAndView mv = new ModelAndView("editLocationPage");
		Location location = accountDao.getLocation(locationId);
		mv.addObject("location", location);
		return mv;
	}

	@RequestMapping(value = "/updateLocation", method = RequestMethod.POST)
	public String updateLocation(
			@Valid @ModelAttribute("location") Location location,
			BindingResult results, Model model, HttpServletRequest request) {
		accountDao.updateLocation(location);
		return "redirect:/admin/location?operation=locationUpdated";
	}

	// --------------------------LOB -------------------

	@RequestMapping(value = { "/LOB" })
	public ModelAndView lob(
			@RequestParam(name = "operation", required = false) String operation) {
		ModelAndView mv = new ModelAndView("page");
		mv.addObject("title", "LOB");
		mv.addObject("userClickLob", true);
		mv.addObject("lobs", accountDao.lobLists());
		mv.addObject("lob", new LOB());
		if (operation != null) {
			if (operation.equals("lobAdded")) {
				mv.addObject("msg", "New LOB has been Added Successfully");
			} else if (operation.equals("lobAvailable")) {
				mv.addObject("msg", "LOB with same name is already available");
			} else if (operation.equals("lobUpdated")) {
				mv.addObject("msg", "LOB has been successfully Updated");
			}
		}
		return mv;
	}

	@RequestMapping(value = "/addNewLob", method = RequestMethod.POST)
	public String handleLOBSubmission(@Valid @ModelAttribute("lob") LOB lob,
			BindingResult results, Model model, HttpServletRequest request) {
		LOB existingLob = accountDao.getLOBByName(lob.getName());

		if (existingLob != null) {
			return "redirect:/admin/LOB?operation=lobAvailable";
		}

		accountDao.addLOB(lob);
		return "redirect:/admin/LOB?operation=lobAdded";
	}

	@RequestMapping(value = "/editLob")
	public ModelAndView editLob(
			@RequestParam(name = "lobId", required = false) Long lobId) {
		ModelAndView mv = new ModelAndView("editLobPage");
		LOB lob = accountDao.getLOB(lobId);
		mv.addObject("lob", lob);
		return mv;
	}

	@RequestMapping(value = "/updateLob", method = RequestMethod.POST)
	public String updateLob(@Valid @ModelAttribute("lob") LOB lob,
			BindingResult results, Model model, HttpServletRequest request) {
		accountDao.updateLOB(lob);
		return "redirect:/admin/LOB?operation=lobUpdated";
	}

	// --------------------------Country -------------------

	@RequestMapping(value = "/country")
	public ModelAndView country(
			@RequestParam(name = "operation", required = false) String operation) {
		ModelAndView mv = new ModelAndView("page");
		mv.addObject("title", "Country");
		mv.addObject("userClickCountry", true);
		mv.addObject("countries", accountDao.countryLists());
		mv.addObject("country", new Country());
		if (operation != null) {
			if (operation.equals("countryAdded")) {
				mv.addObject("msg", "New Country has been Added Successfully");
			} else if (operation.equals("countryAvailable")) {
				mv.addObject("msg",
						"Country with same name is already available");
			} else if (operation.equals("countryUpdated")) {
				mv.addObject("msg", "Country has been Successfully Updated");
			}
		}
		return mv;
	}

	@RequestMapping(value = "/addNewCountry", method = RequestMethod.POST)
	public String handleCountrySubmission(
			@Valid @ModelAttribute("country") Country country,
			BindingResult results, Model model, HttpServletRequest request) {

		Country existingCountry = accountDao
				.getCountryByName(country.getName());

		if (existingCountry != null) {
			return "redirect:/admin/country?operation=countryAvailable";
		}

		accountDao.addCountry(country);
		return "redirect:/admin/country?operation=countryAdded";
	}

	@RequestMapping(value = "/editCountry")
	public ModelAndView editCountry(
			@RequestParam(name = "countryId", required = false) Long countryId) {
		ModelAndView mv = new ModelAndView("editCountryPage");
		Country country = accountDao.getCountry(countryId);
		mv.addObject("country", country);
		return mv;
	}

	@RequestMapping(value = "/updateCountry", method = RequestMethod.POST)
	public String updateCountry(
			@Valid @ModelAttribute("country") Country country,
			BindingResult results, Model model, HttpServletRequest request) {
		accountDao.updateCountry(country);
		return "redirect:/admin/country?operation=countryUpdated";
	}

	// --------------------------Account -------------------

	@RequestMapping(value = { "/account" })
	public ModelAndView account(
			@RequestParam(name = "operation", required = false) String operation) {
		ModelAndView mv = new ModelAndView("page");
		mv.addObject("title", "Accounts");
		mv.addObject("userClickAccount", true);
		mv.addObject("accounts", accountDao.accountList());
		Account account = new Account();
		account.setInitialRating(Util.RATING_NA);
		mv.addObject("account", account);
		if (operation != null) {
			if (operation.equals("accountAdded")) {
				mv.addObject("msg", "New Account has been Added Successfully");
			} else if (operation.equals("accountAvailable")) {
				mv.addObject("msg", "Account is already available");
			} else if (operation.equals("accountUpdated")) {
				mv.addObject("msg", "Account has been Updated Successfully");
			}
		}
		return mv;
	}

	@RequestMapping(value = "/addNewAccount", method = RequestMethod.POST)
	public String handleAccountSubmission(
			@Valid @ModelAttribute("account") Account account,
			BindingResult results, Model model, HttpServletRequest request) {

		Account existingCountry = accountDao.getExistingAccount(account);

		if (existingCountry != null) {
			return "redirect:/admin/account?operation=accountAvailable";
		}

		accountDao.addAccount(account);
		return "redirect:/admin/account?operation=accountAdded";
	}

	@RequestMapping(value = "/editAccount")
	public ModelAndView editAccount(
			@RequestParam(name = "accountId", required = false) Long accountId) {
		ModelAndView mv = new ModelAndView("editAccountPage");
		Account account = accountDao.getAccount(accountId);
		mv.addObject("account", account);
		return mv;
	}

	@RequestMapping(value = "/updateAccount", method = RequestMethod.POST)
	public String updateAccount(
			@Valid @ModelAttribute("account") Account account,
			BindingResult results, Model model, HttpServletRequest request) {
		accountDao.updateAccount(account);
		return "redirect:/admin/account?operation=accountUpdated";
	}

	// ----------------------- Activate Assessment ---------------------
	@RequestMapping(value = "/activateAssessment")
	public ModelAndView activateAssessment(
			@RequestParam(name = "operation", required = false) String operation) {
		ModelAndView mv = new ModelAndView("page");
		mv.addObject("title", "Activate Assessment");
		mv.addObject("userClickActivateAssessment", true);
		mv.addObject("assessments", getAllAssessments());
		return mv;
	}

	@RequestMapping(value = "/editAssessments")
	public ModelAndView editAssessments(
			@RequestParam(name = "accId", required = false) long accId,
			@RequestParam(name = "operation", required = false) String operation) {
		ModelAndView mv = new ModelAndView("page");
		mv.addObject("title", "Activate Assessment");
		System.out.println("selected account id is [" + accId + "]");
		Account account = accountDao.getAccount(accId);
		Assessment assessment = null;
		if (operation != null) {
			if (operation.equals("approverUpdated")) {
				mv.addObject("msg", "Approver has been updated successfully");
			} else if (operation.equals("assessorUpdated")) {
				mv.addObject("msg", "Assessor has been updated successfully");
			} else if (operation.equals("categoryAvailable")) {
				mv.addObject("msg", "Category is already available");
			} else if (operation.equals("categoryAdded")) {
				mv.addObject("msg", "Category has been Added Successfully");
			} else if (operation.equals("smeNotAdded")) {
				mv.addObject("msg",
						"SME already available for selected Assessment Category");
			} else if (operation.equals("smeAdded")) {
				mv.addObject("msg", "SME has been Added Successfully");
			} else if (operation.equals("categoryUpdated")) {
				mv.addObject("msg",
						"Assessment Category has been Updated Successfully");
			}
		}
		List<AssessmentCategories> assessmentCategories = new ArrayList<AssessmentCategories>();
		if (account.getState().equals(Util.NOT_ASSIGNED_ACCOUNT)) {
			account.setState(Util.OPEN_ACCOUNT);
			accountDao.updateAccount(account);
			assessment = new Assessment();
			assessment.setAccount(account);
			assessment.setAssessmentStatus(Util.INCOMPLETE_ASSESSMENT);
			assessmentDao.addAssessment(assessment);
		} else if (account.getState().equals(Util.OPEN_ACCOUNT)) {
			assessment = assessmentDao.getAssessmentByAccount(account);
			assessmentCategories = assessmentDao
					.assessmentCategoriesByAssessment(assessment);
		} else if (account.getState().equals(Util.ACTIVATED_ACCOUNT)) {
			assessment = assessmentDao.getAssessmentByAccount(account);
			assessmentCategories = assessmentDao
					.assessmentCategoriesByAssessment(assessment);
		}

		List<AssessmentCategorySMEMapping> assessmentCategorySMEMappings = new ArrayList<AssessmentCategorySMEMapping>();
		if (assessmentCategories != null) {
			for (AssessmentCategories asc : assessmentCategories) {
				assessmentCategorySMEMappings.addAll(assessmentDao
						.getAssessmentCategorySmeMappingByAssCat(asc));
			}
		}

		mv.addObject("userClickEditCreateActivateAssessment", true);
		mv.addObject("assessment", assessment);
		mv.addObject("assessmentCategories", assessmentCategories);
		mv.addObject("assessmentCategorySMEMappings",
				assessmentCategorySMEMappings);
		return mv;
	}

	@RequestMapping(value = "/editApprover")
	public ModelAndView editApprover(
			@RequestParam(name = "assessmentId", required = false) Long assessmentId) {
		ModelAndView mv = new ModelAndView("editApproverPage");
		Assessment assessment = assessmentDao.getAssessmentById(assessmentId);
		mv.addObject("assessment", assessment);
		return mv;
	}

	@RequestMapping(value = "/updateApprover", method = RequestMethod.POST)
	public String updateApprover(
			@Valid @ModelAttribute("assessment") Assessment assessment,
			BindingResult results, Model model, HttpServletRequest request) {
		Assessment assessment2 = assessmentDao.getAssessmentById(assessment
				.getId());
		assessment2.setApprover(userDao.getUser(assessment.getApprover()
				.getId()));
		assessmentDao.updateAssessment(assessment2);
		return "redirect:/admin/editAssessments?operation=approverUpdated&accId="
				+ assessment2.getAccount().getId();
	}

	@RequestMapping(value = "/editAssessor")
	public ModelAndView editAssessor(
			@RequestParam(name = "assessmentId", required = false) Long assessmentId) {
		ModelAndView mv = new ModelAndView("editAssessorPage");
		Assessment assessment = assessmentDao.getAssessmentById(assessmentId);
		mv.addObject("assessment", assessment);
		return mv;
	}

	@RequestMapping(value = "/updateAssessor", method = RequestMethod.POST)
	public String updateAssessor(
			@Valid @ModelAttribute("assessment") Assessment assessment,
			BindingResult results, Model model, HttpServletRequest request) {
		Assessment assessment2 = assessmentDao.getAssessmentById(assessment
				.getId());
		assessment2.setAssessor(userDao.getUser(assessment.getAssessor()
				.getId()));
		assessmentDao.updateAssessment(assessment2);
		return "redirect:/admin/editAssessments?operation=assessorUpdated&accId="
				+ assessment2.getAccount().getId();
	}

	@RequestMapping(value = "/addNewCategory")
	public ModelAndView addNewCategory(
			@RequestParam(name = "assessmentId", required = false) Long assessmentId) {
		ModelAndView mv = new ModelAndView("addNewCategory");
		Assessment assessment = assessmentDao.getAssessmentById(assessmentId);
		AssessmentCategories asc = new AssessmentCategories();
		asc.setAssessment(assessment);
		asc.setStatus(Util.INCOMPLETE_CATEGORY);
		mv.addObject("assessmentCategory", asc);
		return mv;
	}

	@RequestMapping(value = "/addNewAssessmentCategory", method = RequestMethod.POST)
	public String handleAssessmentCategorySubmission(
			@Valid @ModelAttribute("assessmentCategory") AssessmentCategories assessmentCategory,
			BindingResult results, Model model, HttpServletRequest request) {

		Assessment assessment = assessmentDao
				.getAssessmentById(assessmentCategory.getAssessment().getId());
		AssessmentCategories existingAssessmentCategories = assessmentDao
				.getExistingAssessmentCategory(assessmentDao
						.getAssessmentById(assessmentCategory.getAssessment()
								.getId()), assessmentCategory
						.getAssignedCategories());

		System.out.println("existence of assessment category is ["
				+ existingAssessmentCategories + "]");

		if (existingAssessmentCategories != null) {
			return "redirect:/admin/editAssessments?operation=categoryAvailable&accId="
					+ existingAssessmentCategories.getAssessment().getAccount()
							.getId();
		}

		assessmentCategory.setStatus(Util.INCOMPLETE_CATEGORY);
		System.out.println("addedd category is  ["
				+ assessmentCategory.getReviwer().getId() + "] and name is ["
				+ assessmentCategory.getReviwer().getName() + "]");
		if (assessmentCategory.getReviwer().getId() == 0) {
			assessmentCategory.setReviwer(null);
		}
		assessmentDao.addAssessmentCategory(assessmentCategory);
		return "redirect:/admin/editAssessments?operation=categoryAdded&accId="
				+ assessment.getAccount().getId();
	}

	@RequestMapping(value = "/addNewSme")
	public ModelAndView addNewSme(
			@RequestParam(name = "assessmentCatId", required = false) Long assessmentCatId) {
		ModelAndView mv = new ModelAndView("addNewSme");
		AssessmentCategories assessmentCategory = assessmentDao
				.getAssessmentCategoryById(assessmentCatId);
		AssessmentCategorySMEMapping ascCatSmeMapping = new AssessmentCategorySMEMapping();
		ascCatSmeMapping.setAssessmentCategories(assessmentCategory);
		mv.addObject("ascCatSmeMapping", ascCatSmeMapping);
		return mv;
	}

	@RequestMapping(value = "/addAssCatSme", method = RequestMethod.POST)
	public String addAssCatSme(
			@Valid @ModelAttribute("ascCatSmeMapping") AssessmentCategorySMEMapping ascCatSmeMapping,
			BindingResult results, Model model, HttpServletRequest request) {

		User sme = userDao.getUser(ascCatSmeMapping.getSME().getId());
		AssessmentCategories assessmentCategory = assessmentDao
				.getAssessmentCategoryById(ascCatSmeMapping
						.getAssessmentCategories().getId());
		List<AssessmentCategorySMEMapping> assessmentCategorySMEMappings = assessmentDao
				.getAssessmentCategorySmeMappingByAssCatandSme(
						assessmentCategory, sme);

		System.out.println("gffhgfhfg[" + assessmentCategorySMEMappings + "]");

		if (assessmentCategorySMEMappings != null) {
			if (assessmentCategorySMEMappings.size() != 0) {
				return "redirect:/admin/editAssessments?operation=smeNotAdded&accId="
						+ assessmentCategory.getAssessment().getAccount()
								.getId();
			}
		}
		AssessmentCategorySMEMapping assessmentCategorySMEMapping = new AssessmentCategorySMEMapping();
		assessmentCategorySMEMapping.setSME(sme);
		assessmentCategorySMEMapping
				.setAssessmentCategories(assessmentCategory);
		assessmentDao.addAssessmentCatSmeMapping(assessmentCategorySMEMapping);

		return "redirect:/admin/editAssessments?operation=smeAdded&accId="
				+ assessmentCategory.getAssessment().getAccount().getId();
	}

	@RequestMapping(value = "/editAssessmentCategory")
	public ModelAndView editAssessmentCategory(
			@RequestParam(name = "assessmentCatId", required = false) Long assessmentCatId) {
		ModelAndView mv = new ModelAndView("editAssessmentCategoryPage");

		List<String> assCatStatus = new ArrayList<String>();
		assCatStatus.add(Util.INCOMPLETE_CATEGORY);
		AssessmentCategories asc = assessmentDao
				.getAssessmentCategoryById(assessmentCatId);
		mv.addObject("assessmentCategory", asc);
		mv.addObject("assCatStatus", assCatStatus);
		return mv;
	}

	@RequestMapping(value = "/updateAssessmentCategory", method = RequestMethod.POST)
	public String updateAssessmentCategory(
			@Valid @ModelAttribute("assessmentCategory") AssessmentCategories assessmentCategory,
			BindingResult results, Model model, HttpServletRequest request) {

		AssessmentCategories assessmentCategory1 = assessmentDao
				.getAssessmentCategoryById(assessmentCategory.getId());
		if (assessmentCategory.getReviwer().getId() == 0) {
			assessmentCategory1.setReviwer(null);
		} else {
			assessmentCategory1.setReviwer(userDao.getUser(assessmentCategory
					.getReviwer().getId()));
		}
		assessmentCategory1.setStatus(assessmentCategory.getStatus());

		assessmentDao.updateAssessmentCategory(assessmentCategory1);
		return "redirect:/admin/editAssessments?operation=categoryUpdated&accId="
				+ assessmentCategory1.getAssessment().getAccount().getId();
	}

	@RequestMapping(value = "/editAssessment")
	public ModelAndView editAssessment(
			@RequestParam(name = "accId", required = false) long accId) {
		ModelAndView mv = new ModelAndView("page");
		mv.addObject("title", "Activate Assessment");
		System.out.println("selected account id is [" + accId + "]");
		Account account = accountDao.getAccount(accId);

		if (account.getState().equals(Util.NOT_ASSIGNED_ACCOUNT)) {
			account.setState(Util.OPEN_ACCOUNT);
			accountDao.updateAccount(account);
			Assessment assessment = new Assessment();
			assessment.setAccount(account);
			assessment.setAssessmentStatus(Util.INCOMPLETE_ASSESSMENT);
			assessmentDao.addAssessment(assessment);
			mv.addObject("userClickNewActivateAssessment", true);
			mv.addObject("assessment", assessment);

		} else if (account.getState().equals(Util.OPEN_ACCOUNT)) {
			Assessment assessment = assessmentDao
					.getAssessmentByAccount(account);
			mv.addObject("userClickNewActivateAssessment", true);
			mv.addObject("assessment", assessment);
		} else if (account.getState().equals(Util.ACTIVATED_ACCOUNT)) {
			Assessment assessment = assessmentDao
					.getAssessmentByAccount(account);
			mv.addObject("userClickNewActivateAssessment", true);
			mv.addObject("assessment", assessment);
		}
		return mv;
	}

	@RequestMapping(value = "/addApproverAssessor", method = RequestMethod.POST)
	public ModelAndView handleAssessmentSubmission(
			@Valid @ModelAttribute("assessment") Assessment assessment,
			BindingResult results, Model model, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("page");
		mv.addObject("title", "Activate Assessment");
		Assessment assessment2 = assessmentDao.getAssessmentById(assessment
				.getId());
		assessment2.setApprover(userDao.getUser(assessment.getApprover()
				.getId()));
		assessment2.setAssessor(userDao.getUser(assessment.getAssessor()
				.getId()));
		assessmentDao.updateAssessment(assessment2);
		mv.addObject("userClickAddCategoryAssessment", true);
		mv.addObject("assessment", assessment2);

		List<AssessmentCategories> assessmentCategories = assessmentDao
				.assessmentCategoriesByAssessment(assessment2);
		mv.addObject("assessmentCategories", assessmentCategories);
		AssessmentCategories asc = new AssessmentCategories();
		asc.setAssessment(assessment2);
		mv.addObject("assessmentCategory", asc);

		return mv;
	}

	@RequestMapping(value = "/addSme")
	public ModelAndView addSme(
			@RequestParam(name = "assCatId", required = false) Long assCatId,
			@RequestParam(name = "operation", required = false) String operation) {

		ModelAndView mv = new ModelAndView("page");
		mv.addObject("title", "Activate Assessment");
		mv.addObject("userClickAddSmeForAssessment", true);
		if (operation != null) {
			if (operation.equals("smeNotAdded")) {
				mv.addObject("errorMsg", "SME already Exist");
			}
			if (operation.equals("smeAdded")) {
				mv.addObject("msg", "SME Added Successfully");
			}
		}
		AssessmentCategories assessmentCategory = assessmentDao
				.getAssessmentCategoryById(assCatId);
		Assessment assessment = assessmentCategory.getAssessment();

		List<AssessmentCategorySMEMapping> assessmentCategorySMEMappings = assessmentDao
				.getAssessmentCategorySmeMappingByAssCat(assessmentCategory);
		List<User> allSmes = getAllSMEs();

		mv.addObject("assessmentCategorySMEMappings",
				assessmentCategorySMEMappings);
		mv.addObject("assessmentCategory", assessmentCategory);
		mv.addObject("assessment", assessment);
		AssessmentCategorySMEMapping assessmentCategorySMEMapping = new AssessmentCategorySMEMapping();
		assessmentCategorySMEMapping
				.setAssessmentCategories(assessmentCategory);
		mv.addObject("assCatSmeMapping", assessmentCategorySMEMapping);
		mv.addObject("allSmes", allSmes);
		return mv;
	}

	@RequestMapping(value = "/addedAssessmentCategory")
	public ModelAndView handleAddedAssessmentCatSubmission(
			@RequestParam(name = "operation", required = false) String operation,
			@RequestParam(name = "accId", required = false) long accId) {
		ModelAndView mv = new ModelAndView("page");
		mv.addObject("title", "Activate Assessment");
		mv.addObject("userClickAddCategoryAssessment", true);
		Assessment assessment = assessmentDao.getAssessmentByAccount(accountDao
				.getAccount(accId));
		mv.addObject("assessment", assessment);
		List<AssessmentCategories> assessmentCategories = assessmentDao
				.assessmentCategoriesByAssessment(assessment);
		mv.addObject("assessmentCategories", assessmentCategories);
		if (operation != null) {
			if (operation.equals("categoryAdded")) {
				mv.addObject("msg", "Added Successfully");
			} else if (operation.equals("categoryAvailable")) {
				mv.addObject("msg",
						"Categroy is already available for this Assessment ");
			}
		}
		AssessmentCategories asc = new AssessmentCategories();
		asc.setAssessment(assessment);
		mv.addObject("assessmentCategory", asc);
		return mv;
	}

	@RequestMapping(value = "/completeAndActive")
	public ModelAndView completeAndActive(
			@RequestParam(name = "assessmentId", required = false) long assessmentId) {
		ModelAndView mv = new ModelAndView("page");
		mv.addObject("title", "Activate Assessment");
		mv.addObject("userClickActivateAssessment", true);

		Assessment assessment = assessmentDao.getAssessmentById(assessmentId);
		assessment.setAssessmentStatus(Util.INCOMPLETE_ASSESSMENT);
		assessmentDao.updateAssessment(assessment);

		Account account = assessment.getAccount();
		account.setState(Util.ACTIVATED_ACCOUNT);
		accountDao.updateAccount(account);

		mv.addObject("assessments", getAllAssessments());
		mv.addObject("msg", "Assessment Activated Successfully");
		return mv;
	}

	// ------------------- Account Specific Controls ---------------------------
	@RequestMapping(value = "/accountSpecificControls")
	public ModelAndView accountSpecificControls(
			@RequestParam(name = "operation", required = false) String operation) {
		ModelAndView mv = new ModelAndView("page");
		mv.addObject("title", "Account Specific Controls");
		mv.addObject("userClickAccSpecificControls", true);
		mv.addObject("assessments",
				getActivatedAccount(assessmentDao.assessmentList()));
		return mv;
	}

	@RequestMapping(value = "/editAccSpecControl")
	public ModelAndView editAccSpecControl(
			@RequestParam(name = "catId", required = false) Long catId,
			@RequestParam(name = "assessmentId", required = false) Long assessmentId,
			@RequestParam(name = "operation", required = false) String operation) {

		ModelAndView mv = new ModelAndView("page");
		mv.addObject("title", "Account Specific Controls");
		mv.addObject("userClickeditAccSpecControl", true);

		System.out.println("assessment id is [" + assessmentId + "]");
		Assessment assessment = assessmentDao.getAssessmentById(assessmentId);
		List<AssessmentCategories> assessmentCategories = assessmentDao
				.assessmentCategoriesByAssessment(assessment);
		AssessmentCategories assessmentCategory = null;
		if (catId != null) {
			assessmentCategory = assessmentDao.getAssessmentCategoryById(catId);
		} else {
			if (assessmentCategories != null) {
				if (assessmentCategories.size() >= 0) {
					assessmentCategory = assessmentCategories.get(0);
				}
			}
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

		if (assessmentCategory != null) {
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
			mv.addObject("assessmentCategory", assessmentCategory);
			mv.addObject("allControlsforSelectedCat", controlModel);
		} else {

			List<Control> allControlsforSelectedCat = new ArrayList<Control>();
			List<AccountSpecificControl> accountSpecificControls = new ArrayList<AccountSpecificControl>();
			List<AccSpecControlModel> controlModel = new ArrayList<AccSpecControlModel>();
			mv.addObject("assessmentCategory", assessmentCategory);
			mv.addObject("allControlsforSelectedCat", controlModel);
		}

		// ---------Preparing Answers for selected Assessment ---------
		mv.addObject("assessmentCategories", assessmentCategories);
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

		return "redirect:/admin/editAccSpecControl?operation=accSpecControlAdded&catId="
				+ assessmentCategory.getId()
				+ "&assessmentId="
				+ assessmentCategory.getAssessment().getId();
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
			return "redirect:/admin/editAccSpecControl?operation=accSpecControlremoved&catId="
					+ assessmentCategory.getId()
					+ "&assessmentId="
					+ assessmentCategory.getAssessment().getId();
		} else {
			return "redirect:/admin/editAccSpecControl?operation=accSpecControlNotRemoved&catId="
					+ assessmentCategory.getId()
					+ "&assessmentId="
					+ assessmentCategory.getAssessment().getId();
		}
	}

	// ----------------------- Account Transfer ------------------------------
	@RequestMapping(value = "/accountTransfer")
	public ModelAndView accountTransfer(
			@RequestParam(name = "operation", required = false) String operation,
			@RequestParam(name = "accountFromId", required = false) Long accountFromId,
			@RequestParam(name = "accountToId", required = false) Long accountToId) {
		ModelAndView mv = new ModelAndView("page");

		List<Assessment> assessments = assessmentDao.assessmentList();
		List<Account> accounts = accountDao.notAssignedAccountList();

		mv.addObject("assessments", assessments);
		mv.addObject("accounts", accounts);
		if (operation != null) {
			if (operation.equals("accountTransfer")) {
				mv.addObject("msg", "Account has been successfully Transfered");
			} else if (operation.equals("accountNotTransfer")) {
				mv.addObject("erroMsg",
						"Account has not been successfully transfered");
			}
		}

		AccountTransferModel accountTransferModel = new AccountTransferModel();
		if (accountFromId != null) {
			Account account = accountDao.getAccount(accountFromId);
			accountTransferModel.setAccountFrom(account);
		}
		if (accountToId != null) {
			Account account = accountDao.getAccount(accountToId);
			accountTransferModel.setAccountTo(account);
		}

		mv.addObject("accountTransferModel", accountTransferModel);
		mv.addObject("title", "Account Transfer");
		mv.addObject("userClickAccountTransfer", true);
		return mv;
	}

	@RequestMapping(value = "/transferAccount", method = RequestMethod.POST)
	public String transferAccount(
			@Valid @ModelAttribute("accountTransferModel") AccountTransferModel accountTransferModel) {
		ModelAndView mv = new ModelAndView("page");

		Account accountFrom = accountDao.getAccount(accountTransferModel
				.getAccountFrom().getId());
		Account accountTo = accountDao.getAccount(accountTransferModel
				.getAccountTo().getId());

		Assessment assessment = assessmentDao
				.getAssessmentByAccount(accountFrom);
		Assessment assessmentTo = assessmentDao
				.getAssessmentByAccount(accountTo);

		System.out.println("assessment from [" + assessment
				+ "] and assessment to is [" + assessmentTo + "]");

		if (assessment == null) {
			return "redirect:/admin/accountTransfer?operation=accountNotTransfer";
		} else {
			if (assessmentTo == null) {

				accountTo.setState(accountFrom.getState());
				accountDao.updateAccount(accountTo);

				Assessment asc = new Assessment();
				asc.setAccount(accountTo);
				asc.setApprover(assessment.getApprover());
				asc.setAssessmentStatus(assessment.getAssessmentStatus());
				asc.setAssessor(assessment.getAssessor());
				asc.setRiskLevel(assessment.getRiskLevel());
				asc.setRiskValue(assessment.getRiskValue());
				assessmentDao.addAssessment(asc);

				System.out.println(assessmentDao.getAssessmentByAccount(
						accountTo).getId());
				assessmentTo = assessmentDao.getAssessmentByAccount(accountTo);

				List<AssessmentCategories> assessmentCategories = assessmentDao
						.assessmentCategoriesByAssessment(assessment);
				for (AssessmentCategories ascCat : assessmentCategories) {
					AssessmentCategories ascCatTo = new AssessmentCategories();
					ascCatTo.setAssessment(assessmentTo);
					ascCatTo.setAssignedCategories(ascCat
							.getAssignedCategories());
					ascCatTo.setReviwer(ascCat.getReviwer());
					ascCatTo.setStatus(ascCat.getStatus());
					assessmentDao.addAssessmentCategory(ascCatTo);
					AssessmentCategories assessmentCategoriesTo = assessmentDao
							.getExistingAssessmentCategory(assessmentTo,
									ascCat.getAssignedCategories());

					// -------- Assessment Category sme mapping -------------
					List<AssessmentCategorySMEMapping> smeMappings = assessmentDao
							.getAssessmentCategorySmeMappingByAssCat(ascCat);
					if (smeMappings != null) {
						for (AssessmentCategorySMEMapping sm : smeMappings) {
							AssessmentCategorySMEMapping mapping = new AssessmentCategorySMEMapping();
							mapping.setAssessmentCategories(assessmentCategoriesTo);
							mapping.setSME(sm.getSME());
							assessmentDao.addAssessmentCatSmeMapping(sm);
						}
					}

					// --------- Account Specific control ---------------
					List<AccountSpecificControl> accountSpecificControls = controlDao
							.accSpecControlByCategory(ascCat);
					if (accountSpecificControls != null) {
						for (AccountSpecificControl sm : accountSpecificControls) {
							AccountSpecificControl c = new AccountSpecificControl();
							c.setAssessmentCategories(assessmentCategoriesTo);
							c.setControl(sm.getControl());
							controlDao.addAccSpecControl(c);
						}
					}
					// -------------Answers -------------------
					List<AccountSpecificControl> specificControls = controlDao
							.accSpecControlByCategory(ascCat);
					List<AccountSpecificControl> specificControlsTo = controlDao
							.accSpecControlByCategory(assessmentCategoriesTo);
					if (specificControls != null) {
						for (AccountSpecificControl sm : specificControls) {
							AccountSpecificControl accountSpecificControlTo = controlDao
									.accSpecificControlByCtrl(sm.getControl(),
											assessmentCategoriesTo);
							Answer answer = controlDao
									.getAnswerByAccSpecControl(sm);
							if (answer != null) {
								Answer answerTo = new Answer();
								answerTo.setControl(accountSpecificControlTo);
								answerTo.setAnswer(answer.getAnswer());
								answerTo.setArtifaceName(answer
										.getArtifaceName());
								answerTo.setArtifact(answer.getArtifact());
								answerTo.setArtifactType(answer
										.getArtifactType());
								answerTo.setComment(answer.getComment());
								answerTo.setConfirmationStatus(answer
										.getConfirmationStatus());
								answerTo.setDateAnswered(answer
										.getDateAnswered());
								answerTo.setLastRespondedUser(answer
										.getLastRespondedUser());
								answerTo.setMitigationDate(answer
										.getMitigationDate());
								answerTo.setNC(answer.isNC());
								answerTo.setReviewerComment(answer
										.getReviewerComment());
								answerTo.setRiskAcceptance(answer
										.isRiskAcceptance());
								answerTo.setRiskAcceptenceby(answer
										.getRiskAcceptenceby());
								controlDao.addAnswer(answerTo);
							}

						}
					}

					List<AnswerCopy> answerCopies = controlDao
							.allAnswerCopiesbyAssessmentCategory(ascCat);
					if (answerCopies != null) {
						for (AnswerCopy answer : answerCopies) {

							AccountSpecificControl accountSpecificControlTo = controlDao
									.accSpecificControlByCtrl(answer
											.getControl().getControl(),
											assessmentCategoriesTo);
							Answer anTo = controlDao
									.getAnswerByAccSpecControl(accountSpecificControlTo);

							AnswerCopy answerTo = new AnswerCopy();
							answerTo.setAnswerId(anTo.getId());
							answerTo.setControl(accountSpecificControlTo);
							answerTo.setAnswer(answer.getAnswer());
							answerTo.setArtifaceName(answer.getArtifaceName());
							answerTo.setArtifact(answer.getArtifact());
							answerTo.setArtifactType(answer.getArtifactType());
							answerTo.setComment(answer.getComment());
							answerTo.setConfirmationStatus(answer
									.getConfirmationStatus());
							answerTo.setDateAnswered(answer.getDateAnswered());
							answerTo.setLastRespondedUser(answer
									.getLastRespondedUser());
							answerTo.setMitigationDate(answer
									.getMitigationDate());
							answerTo.setNC(answer.isNC());
							answerTo.setReviewerComment(answer
									.getReviewerComment());
							answerTo.setRiskAcceptance(answer
									.isRiskAcceptance());
							answerTo.setRiskAcceptenceby(answer
									.getRiskAcceptenceby());
							controlDao.saveAnswerCopy(answerTo);
						}
					}

				}
			}
		}

		return "redirect:/admin/accountTransfer?operation=accountTransfer";
	}

	// ----------- Account Deletetion ------------------------

	@RequestMapping(value = "/accountDeleteion")
	public ModelAndView accountDeleteion(
			@RequestParam(name = "operation", required = false) String operation,
			@RequestParam(name = "accountId", required = false) Long accountId) {
		ModelAndView mv = new ModelAndView("page");
		mv.addObject("accounts", accountDao.accountList());
		if (operation != null) {
			if (operation.equals("accountDeleted")) {
				mv.addObject("msg", "Account has been deleted Successfully");
			} else if (operation.equals("accountNotDeleted")) {
				mv.addObject(
						"errorMsg",
						"Account can not be deleted as Responses have already been provided for this Account");
			}
		}
		mv.addObject("title", "Account Deletion");
		mv.addObject("userClickAccountDeletion", true);
		return mv;
	}

	@RequestMapping(value = "/deleteAccount")
	public String deleteAccount(@ModelAttribute("accountId") long accountId) {

		Account account = accountDao.getAccount(accountId);
		System.out.println("account id is [" + account.getId() + "]");
		Assessment assessment = assessmentDao.getAssessmentByAccount(account);
		System.out.println("assessment id is [" + assessment + "]");
		if (assessment == null) {
			accountDao.deleteAccount(account);
			return "redirect:/admin/accountDeleteion?operation=accountDeleted";
		}

		List<AssessmentCategories> assessmentCategories = assessmentDao
				.assessmentCategoriesByAssessment(assessment);
		if (assessmentCategories == null) {
			assessmentDao.deleteAssessment(assessment);
			accountDao.deleteAccount(account);
			return "redirect:/admin/accountDeleteion?operation=accountDeleted";
		} else {
			if (assessmentCategories.size() == 0) {
				assessmentDao.deleteAssessment(assessment);
				accountDao.deleteAccount(account);
				return "redirect:/admin/accountDeleteion?operation=accountDeleted";
			} else {
				boolean answerAvailability = false;
				for (AssessmentCategories category : assessmentCategories) {
					List<Answer> answers = controlDao
							.allAnswerbyAssessmentCategory(category);
					if (answers != null) {
						if (answers.size() > 0) {
							answerAvailability = true;
							break;
						}
					}
				}
				if (answerAvailability) {
					return "redirect:/admin/accountDeleteion?operation=accountNotDeleted";
				} else {
					for (AssessmentCategories category : assessmentCategories) {
						List<AccountSpecificControl> accountSpecificControls = controlDao
								.accSpecControlByCategory(category);
						if (accountSpecificControls != null) {
							for (AccountSpecificControl ac : accountSpecificControls) {
								controlDao.deleteAccSpecControl(ac);
							}
						}
						List<AssessmentCategorySMEMapping> mappings = assessmentDao
								.getAssessmentCategorySmeMappingByAssCat(category);
						if (mappings != null) {
							for (AssessmentCategorySMEMapping ac : mappings) {
								assessmentDao
										.deleteAssessmentCategorySmeMappings(ac);
							}
						}
					}
					for (AssessmentCategories category : assessmentCategories) {
						assessmentDao.deleteAssessmentCategory(category);
					}
					assessmentDao.deleteAssessment(assessment);
					accountDao.deleteAccount(account);
					return "redirect:/admin/accountDeleteion?operation=accountDeleted";
				}
			}
		}
	}

	// ------------------- Reporting ---------------------------
	@RequestMapping(value = "/reporting")
	public ModelAndView reporting(
			@RequestParam(name = "operation", required = false) String operation) {
		ModelAndView mv = new ModelAndView("page");
		mv.addObject("title", "Reporting");
		mv.addObject("userClickReporting", true);
		mv.addObject("assessments",
				getActivatedAccount(assessmentDao.assessmentList()));
		return mv;
	}

	// ------------------- Mailing Part ---------------------------
	@RequestMapping(value = "/mitigationMail")
	public ModelAndView mitigationMail(
			@RequestParam(name = "operation", required = false) String operation) {
		ModelAndView mv = new ModelAndView("page");
		mv.addObject("title", "Mitigation Mail");
		mv.addObject("userClickMitigationMail", true);
		List<Country> countries = accountDao.countryLists();
		if (countries != null) {
		} else {
			countries = new ArrayList<Country>();
		}
		mv.addObject("countries", countries);
		return mv;
	}

	@RequestMapping(value = "/updatedLob")
	public ModelAndView updatedLob(
			@RequestParam(name = "approvercountryId", required = false) long approvercountryId) {
		ModelAndView mv = new ModelAndView("updatedLob");
		List<Assessment> assessments = assessmentDao
				.assessmentListByCountry(approvercountryId);
		List<LOB> lobs = new ArrayList<LOB>();
		if (assessments != null) {
			for (Assessment assessment : assessments) {
				System.out.println("lob id is ["
						+ assessment.getAccount().getLob().getId()
						+ "] and name is ["
						+ assessment.getAccount().getLob().getName() + "]");
				LOB lob = accountDao.getLOB(assessment.getAccount().getLob()
						.getId());
				lobs.add(lob);
			}
		}
		mv.addObject("lobs", lobs);
		return mv;
	}

	@RequestMapping(value = "/updatedLocation")
	public ModelAndView updatedLocation(
			@RequestParam(name = "approvercountryId", required = false) long approvercountryId,
			@RequestParam(name = "approverLOBId", required = false) long approverLOBId) {
		ModelAndView mv = new ModelAndView("updatedLocation");
		List<Assessment> assessments = assessmentDao
				.assessmentListByCountryAndLob(approvercountryId, approverLOBId);
		List<Location> locations = new ArrayList<Location>();
		if (assessments != null) {
			for (Assessment assessment : assessments) {
				System.out
						.println("Location id is ["
								+ assessment.getAccount().getLocation().getId()
								+ "] and name is ["
								+ assessment.getAccount().getLocation()
										.getName() + "]");
				locations.add(assessment.getAccount().getLocation());
			}
		}
		mv.addObject("locations", locations);
		return mv;
	}

	@RequestMapping(value = "/updatedDepartment")
	public ModelAndView updatedDepartment(
			@RequestParam(name = "approvercountryId", required = false) long approvercountryId,
			@RequestParam(name = "approverLOBId", required = false) long approverLOBId,
			@RequestParam(name = "approverLocationId", required = false) long approverLocationId) {
		ModelAndView mv = new ModelAndView("updatedDepartment");
		List<Assessment> assessments = assessmentDao
				.assessmentListByCountryLobAndLocation(approvercountryId,
						approverLOBId, approverLocationId);
		List<Department> departments = new ArrayList<Department>();
		if (assessments != null) {
			for (Assessment assessment : assessments) {
				System.out.println("Department id is ["
						+ assessment.getAccount().getDepartment().getId()
						+ "] and name is ["
						+ assessment.getAccount().getDepartment().getName()
						+ "]");
				departments.add(assessment.getAccount().getDepartment());
			}
		}
		mv.addObject("departments", departments);
		return mv;
	}

	@RequestMapping(value = "/sendApproverMail")
	public ModelAndView sendApproverMail(
			@RequestParam(name = "approvercountryId", required = false) long approvercountryId,
			@RequestParam(name = "approverLOBId", required = false) long approverLOBId,
			@RequestParam(name = "approverLocationId", required = false) long approverLocationId,
			@RequestParam(name = "approverDepartmentId", required = false) long approverDepartmentId) {

		System.out.println("hello there ! i am here");
		ModelAndView mv = new ModelAndView("sendApproverMailMsg");
		List<Assessment> assessments = new ArrayList<Assessment>();
		if (approvercountryId == 0) {
			List<Assessment> allAssessments = assessmentDao.assessmentList();
			assessments = getActivatedAccount(allAssessments);
		} else if (approverLOBId == 0) {
			List<Assessment> allAssessments = assessmentDao
					.assessmentListByCountry(approvercountryId);
			assessments = getActivatedAccount(allAssessments);
		} else if (approverLocationId == 0) {
			List<Assessment> allAssessments = assessmentDao
					.assessmentListByCountryAndLob(approvercountryId,
							approverLOBId);
			assessments = getActivatedAccount(allAssessments);
		} else if (approverDepartmentId == 0) {
			List<Assessment> allAssessments = assessmentDao
					.assessmentListByCountryLobAndLocation(approvercountryId,
							approverLOBId, approverLocationId);
			assessments = getActivatedAccount(allAssessments);
		} else {
			List<Assessment> allAssessments = assessmentDao
					.assessmentListByCountryLobLocationAndDepartment(
							approvercountryId, approverLOBId,
							approverLocationId, approverDepartmentId);
			assessments = getActivatedAccount(allAssessments);
		}
		mailService.sendMitigationMailToApprover(assessments);
		mv.addObject("msg", "Sent Successfully");
		return mv;
	}

	@RequestMapping(value = "/sendAssessorMail")
	public ModelAndView sendAssessorMail(
			@RequestParam(name = "approvercountryId", required = false) long approvercountryId,
			@RequestParam(name = "approverLOBId", required = false) long approverLOBId,
			@RequestParam(name = "approverLocationId", required = false) long approverLocationId,
			@RequestParam(name = "approverDepartmentId", required = false) long approverDepartmentId) {

		System.out.println("hello there ! i am here");
		ModelAndView mv = new ModelAndView("sendApproverMailMsg");
		List<Assessment> assessments = new ArrayList<Assessment>();
		if (approvercountryId == 0) {
			List<Assessment> allAssessments = assessmentDao.assessmentList();
			assessments = getActivatedAccount(allAssessments);
		} else if (approverLOBId == 0) {
			List<Assessment> allAssessments = assessmentDao
					.assessmentListByCountry(approvercountryId);
			assessments = getActivatedAccount(allAssessments);
		} else if (approverLocationId == 0) {
			List<Assessment> allAssessments = assessmentDao
					.assessmentListByCountryAndLob(approvercountryId,
							approverLOBId);
			assessments = getActivatedAccount(allAssessments);
		} else if (approverDepartmentId == 0) {
			List<Assessment> allAssessments = assessmentDao
					.assessmentListByCountryLobAndLocation(approvercountryId,
							approverLOBId, approverLocationId);
			assessments = getActivatedAccount(allAssessments);
		} else {
			List<Assessment> allAssessments = assessmentDao
					.assessmentListByCountryLobLocationAndDepartment(
							approvercountryId, approverLOBId,
							approverLocationId, approverDepartmentId);
			assessments = getActivatedAccount(allAssessments);
		}
		mailService.sendMitigationMailToAssessor(assessments);
		mv.addObject("msg", "Sent Successfully");
		return mv;
	}

	@RequestMapping(value = "/assessmentMail")
	public ModelAndView assessmentMail(
			@RequestParam(name = "operation", required = false) String operation) {
		ModelAndView mv = new ModelAndView("page");
		mv.addObject("title", "Assessment Mail");
		mv.addObject("userClickAssessmentMail", true);
		return mv;
	}

	@RequestMapping("/downloadAllAssessmentDetails")
	public String downloadAllAssessmentDetails(HttpServletRequest request,
			HttpServletResponse response) {

		List<Assessment> assessments = assessmentDao.assessmentList();
		try {
			downloadExcel.getAllAssessmentExcel(request, response, assessments);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	// --------------------------------------------------------------------

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

	// ------------------------ Model Attributes -----------------------------

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

	private List<Assessment> getAllAssessments() {
		List<Assessment> assessments = new ArrayList<Assessment>();
		if (assessmentDao.assessmentList() != null) {
			assessments = assessmentDao.assessmentList();
		}
		List<Account> accounts = accountDao.notAssignedAccountList();
		if (accounts != null) {
			for (Account acc : accounts) {
				Assessment assessment = new Assessment();
				assessment.setAccount(acc);
				assessments.add(assessment);
			}
		}
		return assessments;
	}

	@ModelAttribute("allRoles")
	List<String> getAllRoles() {
		if (globalController.getUserModel().getRole().equals(Util.SUPERADMIN)) {
			return Util.getAllRolesForSuperAdmin();
		} else if (globalController.getUserModel().getRole().equals(Util.ADMIN)) {
			return Util.getAllRoles();
		} else {
			return null;
		}
	}

	@ModelAttribute("categoryPhases")
	List<String> getCategoryPhases() {
		return Util.getAllPhases();
	}

	@ModelAttribute("categoryState")
	List<String> getCategoryState() {
		return Util.getAllStates();
	}

	@ModelAttribute("controlsCategory")
	List<Category> getControlsCategory() {
		List<Category> categories = new ArrayList<Category>();
		categories = categoryDao.categoryLists();
		return categories;
	}

	@ModelAttribute("controlCritical")
	List<String> getControlCriticals() {
		return Util.getAllConditions();
	}

	@ModelAttribute("controlRating")
	List<Integer> getControlRatings() {
		List<Integer> controlRatings = new ArrayList<Integer>();
		controlRatings.add(0);
		controlRatings.add(1);
		controlRatings.add(2);
		controlRatings.add(3);
		controlRatings.add(4);
		controlRatings.add(5);
		controlRatings.add(6);
		controlRatings.add(7);
		controlRatings.add(8);
		controlRatings.add(9);
		controlRatings.add(10);
		return controlRatings;
	}

	@ModelAttribute("controlFlag")
	List<Integer> getControlFlags() {
		List<Integer> controlFlags = new ArrayList<Integer>();
		controlFlags.add(0);
		controlFlags.add(1);
		controlFlags.add(2);
		return controlFlags;
	}

	@ModelAttribute("controlRisks")
	List<String> getControlRisks() {
		return Util.getAllRisks();
	}

	@ModelAttribute("controlScreens")
	List<String> getControlScreens() {
		return Util.getAllScreens();
	}

	@ModelAttribute("departmentList")
	List<Department> getAllDepartments() {
		return accountDao.departmentLists();
	}

	@ModelAttribute("locationList")
	List<Location> getAllLocations() {
		return accountDao.locationLists();
	}

	@ModelAttribute("lobList")
	List<LOB> getAllLobs() {
		return accountDao.lobLists();
	}

	@ModelAttribute("countryList")
	List<Country> getAllCountries() {
		return accountDao.countryLists();
	}

	@ModelAttribute("sectorList")
	List<String> getAllSectors() {
		List<String> allSectores = new ArrayList<String>();
		allSectores.add("NA");
		return allSectores;
	}

	@ModelAttribute("phaseList")
	List<String> getAllPhases() {
		return Util.getAllPhases();
	}

	@ModelAttribute("initialRatingList")
	List<String> getInitialRatingList() {
		return Util.getAllRatings();
	}

	@ModelAttribute("approvers")
	List<User> getAllApprovers() {
		return userDao.userListsByRole(Util.APPROVER);
	}

	@ModelAttribute("assessors")
	List<User> getAllAssessors() {
		return userDao.userListsByRole(Util.ASSESSOR);
	}

	@ModelAttribute("sme")
	List<User> getAllSMEs() {
		return userDao.userListsByRole(Util.SME);
	}

	@ModelAttribute("reviewers")
	List<User> getAllReviewers() {
		List<User> reviewers = userDao.userListsByRole(Util.REVIEWER);
		if (reviewers != null) {
			User user = new User();
			user.setName("Select Reviewer");
			user.setId(0);
			reviewers.add(user);
		}
		return reviewers;
	}
}
