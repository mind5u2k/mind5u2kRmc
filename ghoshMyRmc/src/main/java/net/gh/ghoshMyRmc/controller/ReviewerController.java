package net.gh.ghoshMyRmc.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import net.gh.ghoshMyRmc.model.AnswerModel;
import net.gh.ghoshMyRmc.model.AssessmentModel;
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
import net.gh.ghoshMyRmcBackend.dto.AssessmentTrail;
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
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/reviewer")
public class ReviewerController {

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

	// ------------------Approver Home ----------------------
	@RequestMapping(value = "/reviewerHome")
	public ModelAndView home(
			@RequestParam(name = "operation", required = false) String operation) {
		ModelAndView mv = new ModelAndView("reviewerHome");
		mv.addObject("title", "Welcome Reviewer Home");
		mv.addObject("totalAccounts", true);

		User reviewer = userDao.getUserByEmailId(globalController
				.getUserModel().getEmail());

		List<AssessmentCategories> assCatByReviewer = assessmentDao
				.assessmentCategoriesListByReviewer(reviewer);

		if (assCatByReviewer == null) {
			assCatByReviewer = new ArrayList<AssessmentCategories>();
		}

		List<Assessment> assessments = new ArrayList<Assessment>();
		List<Long> assessmentIds = new ArrayList<Long>();
		for (AssessmentCategories asc : assCatByReviewer) {
			if (!assessmentIds.contains(asc.getAssessment().getId())) {
				assessmentIds.add(asc.getAssessment().getId());
				assessments.add(asc.getAssessment());
			}
		}

		List<Assessment> activateAssessments = getActivatedAccount(assessments);

		if (activateAssessments == null) {
			activateAssessments = new ArrayList<Assessment>();
		}

		AssessmentModel assessmentModel = new AssessmentModel();
		assessmentModel.setAssessments(activateAssessments);
		mv.addObject("assessmentModel", assessmentModel);
		mv.addObject("total", activateAssessments.size());

		List<Assessment> completeAssessments = getAssessmentByStatus(
				activateAssessments, Util.COMPLETE_ASSESSMENT);
		mv.addObject("completed", completeAssessments.size());

		List<Assessment> submitAssessments = getAssessmentByStatus(
				activateAssessments, Util.SUBMITTED_ASSESSMENT);
		mv.addObject("submitted", submitAssessments.size());

		List<Assessment> incompleteAssessments = getAssessmentByStatus(
				activateAssessments, Util.INCOMPLETE_ASSESSMENT);
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
		User reviewer = userDao.getUserByEmailId(globalController
				.getUserModel().getEmail());

		if (assessment.getAssessmentStatus().equals(Util.SUBMITTED_ASSESSMENT)) {
			mv = new ModelAndView("assessmentCopyPage");
			mv.addObject("title", "Assessment Page");

			List<AssessmentCategories> assessmentCategories = assessmentDao
					.assessmentCategoriesByAssessmentandReviewer(reviewer,
							assessment);
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

		List<AssessmentCategories> assessmentCategories = assessmentDao
				.assessmentCategoriesByAssessmentandReviewer(reviewer,
						assessment);

		System.out.println("hello there assessment categories are ["
				+ assessmentCategories.size() + "]");
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
			@RequestParam(name = "operation", required = false) String operation,
			@RequestParam(name = "ctrlType", required = false) String ctrlType) {

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
				.assessmentCategoriesByAssessmentandReviewer(userDao
						.getUserByEmailId(globalController.getUserModel()
								.getEmail()), assessment);
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
		List<Answer> answersByCtrlType = new ArrayList<Answer>();
		for (Answer answer : answers) {
			System.out.println("answer artifact is [" + answer.getArtifact()
					+ "]");
			if (ctrlType == null) {
				answersByCtrlType.add(answer);
			}
			if (ctrlType != null) {
				if (ctrlType.equals("all")) {
					answersByCtrlType.add(answer);
				}
			}
			if (answer.isNC()) {
				if (ctrlType != null) {
					if (ctrlType.equals("totalRisk")) {
						answersByCtrlType.add(answer);
					}
				}
				totalRisks++;
			}
			if (answer.isNC()
					&& answer.getConfirmationStatus().equals(
							Util.REVIEW_PENDING)) {
				if (ctrlType != null) {
					if (ctrlType.equals("reviewPendingforNC")) {
						answersByCtrlType.add(answer);
					}
				}
				reviewPendingforNC++;
			} else if (answer.isNC()
					&& answer.getConfirmationStatus().equals(
							Util.REVIEW_COMPLETE)) {
				if (ctrlType != null) {
					if (ctrlType.equals("reviewCompleteforNC")) {
						answersByCtrlType.add(answer);
					}
				}
				reviewCompleteforNC++;
			} else if (!answer.isNC()
					&& answer.getConfirmationStatus().equals(
							Util.REVIEW_PENDING)) {
				if (ctrlType != null) {
					if (ctrlType.equals("reviewPendingforNonNC")) {
						answersByCtrlType.add(answer);
					}
				}
				reviewPendingforNonNC++;
			} else if (!answer.isNC()
					&& answer.getConfirmationStatus().equals(
							Util.REVIEW_COMPLETE)) {
				if (ctrlType != null) {
					if (ctrlType.equals("reviewCompleteforNonNC")) {
						answersByCtrlType.add(answer);
					}
				}
				reviewCompleteforNonNC++;
			} else if (answer.isNC()
					&& answer.getConfirmationStatus().equals(
							Util.CHANGE_REQUIRED)) {
				if (ctrlType != null) {
					if (ctrlType.equals("changeRequiredforNC")) {
						answersByCtrlType.add(answer);
					}
				}
				changeRequiredforNC++;
			} else if (!answer.isNC()
					&& answer.getConfirmationStatus().equals(
							Util.CHANGE_REQUIRED)) {
				if (ctrlType != null) {
					if (ctrlType.equals("changeRequiredforNonNC")) {
						answersByCtrlType.add(answer);
					}
				}
				changeRequiredforNonNC++;
			}
		}
		String ctrlTypeString = "";
		if (ctrlType == null) {
			ctrlTypeString = "All Controls [" + answers.size() + "]";
		} else {
			if (ctrlType.equals("all")) {
				ctrlTypeString = "All Controls [" + answers.size() + "]";
			} else if (ctrlType.equals("totalRisk")) {
				ctrlTypeString = "Total Risks [" + answersByCtrlType.size()
						+ "]";
			} else if (ctrlType.equals("reviewPendingforNC")) {
				ctrlTypeString = "Review Pending for NC ["
						+ answersByCtrlType.size() + "]";
			} else if (ctrlType.equals("reviewCompleteforNC")) {
				ctrlTypeString = "Review Complete for NC ["
						+ answersByCtrlType.size() + "]";
			} else if (ctrlType.equals("reviewPendingforNonNC")) {
				ctrlTypeString = "Review Pending for Non NC ["
						+ answersByCtrlType.size() + "]";
			} else if (ctrlType.equals("reviewCompleteforNonNC")) {
				ctrlTypeString = "Review Complete for Non NC ["
						+ answersByCtrlType.size() + "]";
			} else if (ctrlType.equals("changeRequiredforNC")) {
				ctrlTypeString = "More Info Requested for NC ["
						+ answersByCtrlType.size() + "]";
			} else if (ctrlType.equals("changeRequiredforNonNC")) {
				ctrlTypeString = "More Info Requested for Non NC ["
						+ answersByCtrlType.size() + "]";
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

		mv.addObject("answers", answersByCtrlType);
		mv.addObject("ctrlTypeString", ctrlTypeString);
		mv.addObject("assessment", assessment);
		mv.addObject("assessmentCategories", assessmentCategories);
		mv.addObject("selectedAssessmentCategory", selectedAssessmentCategory);
		mv.addObject("userRole", "reviewer");
		return mv;
	}

	@RequestMapping(value = "/riskTrackerResponsePage")
	public ModelAndView riskTrackerResponsePage(
			@Valid @ModelAttribute("answerId") Long answerId,
			BindingResult results, Model model, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("riskTrackerResponseReviewerPage");
		mv.addObject("title", "Response Page");
		Answer answer = controlDao.getAnswer(answerId);
		String[] answerOpions = answer.getControl().getControl().getAnswers()
				.split("/");
		mv.addObject("answerOpions", answerOpions);
		mv.addObject("answer", answer);
		mv.addObject("userRole", "reviewer");
		return mv;
	}

	@RequestMapping(value = "/saveRiskTrackerAnswer", method = RequestMethod.POST)
	public String saveRiskTrackerAnswer(
			@Valid @ModelAttribute("answer") Answer answer,
			BindingResult results, Model model) {

		Answer answer1 = controlDao.getAnswer(answer.getId());
		answer1.setLastRespondedUser(userDao.getUserByEmailId(globalController
				.getUserModel().getEmail()));
		answer1.setDateAnswered(new Timestamp(new Date().getTime()));
		answer1.setConfirmationStatus(answer.getConfirmationStatus());
		answer1.setReviewerComment(answer.getReviewerComment());
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
		return "redirect:/reviewer/riskTracker?assessmentId="
				+ answer1.getControl().getAssessmentCategories()
						.getAssessment().getId() + "&catId="
				+ answer1.getControl().getAssessmentCategories().getId()
				+ "&operation=saveRiskTrackerResponse";

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
