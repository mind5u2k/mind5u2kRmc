package net.gh.ghoshMyRmc.riskAnalysis;

import java.util.List;

import net.gh.ghoshMyRmcBackend.Util;
import net.gh.ghoshMyRmcBackend.dao.AssessmentDao;
import net.gh.ghoshMyRmcBackend.dao.ControlDao;
import net.gh.ghoshMyRmcBackend.dto.Answer;
import net.gh.ghoshMyRmcBackend.dto.Assessment;
import net.gh.ghoshMyRmcBackend.dto.AssessmentCategories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("riskCalculation")
public class RiskCalculation {

	@Autowired
	ControlDao controlDao;

	@Autowired
	AssessmentDao assessmentDao;

	public int getRiskvalueforAssessment(Assessment assessment) {
		int riskval = 0;
		List<AssessmentCategories> assessmentCategories = assessmentDao
				.assessmentCategoriesByAssessment(assessment);

		System.out.println("total assigned categories are ["
				+ assessmentCategories.size() + "]");
		for (AssessmentCategories asc : assessmentCategories) {
			System.out.println("Ids of assigned category is [" + asc.getId()
					+ "]");

			List<Answer> answers = controlDao
					.allAnswerbyAssessmentCategory(asc);
			System.out.println("total answers for category [" + asc.getId()
					+ "] is [" + answers.size() + "]");

			for (Answer answer : answers) {
				System.out.println("answers ids are [" + answer.getId() + "]");
				if (answer.getAnswer().equals(Util.ANS_NO)
						&& answer.getControl().getControl().getFlag() == 1) {
					System.out.println("************* no  answer id is ["
							+ answer.getId() + "]");
					riskval = riskval
							+ (answer.getControl().getControl().getRating() * 1);
				} else if (answer.getAnswer().equals(Util.ANS_YES)
						&& answer.getControl().getControl().getFlag() == 2) {
					System.out.println("*************yes  answer id is ["
							+ answer.getId() + "]");
					riskval = riskval
							+ (answer.getControl().getControl().getRating() * 1);
				}
			}
		}
		System.out.println("returned risk value is [" + riskval + "]");
		return riskval;
	}

	public int getRiskForAnswer(Answer answer) {
		int riskval = 0;

		if (answer.getAnswer().equals(Util.ANS_NO)
				&& answer.getControl().getControl().getFlag() == 1) {
			riskval = riskval
					+ (answer.getControl().getControl().getRating() * 1);
		} else if (answer.getAnswer().equals(Util.ANS_YES)
				&& answer.getControl().getControl().getFlag() == 2) {
			riskval = riskval
					+ (answer.getControl().getControl().getRating() * 1);
		}

		return riskval;
	}

	public int getCriticalRiskvalueforAssessment(Assessment assessment) {
		int riskval = 0;

		System.out.println("********* assessment is [" + assessment + "]");
		System.out.println("********* assessment dao is [" + assessmentDao
				+ "]");
		List<AssessmentCategories> assessmentCategories = assessmentDao
				.assessmentCategoriesByAssessment(assessment);
		for (AssessmentCategories asc : assessmentCategories) {
			List<Answer> answers = controlDao
					.allAnswerbyAssessmentCategory(asc);
			for (Answer answer : answers) {
				if (answer.getControl().getControl().getCritical()
						.equals(Util.YES)) {
					if (answer.getAnswer().equals(Util.ANS_NO)
							&& answer.getControl().getControl().getFlag() == 1) {
						riskval = riskval
								+ (answer.getControl().getControl().getRating() * 1);
					} else if (answer.getAnswer().equals(Util.ANS_YES)
							&& answer.getControl().getControl().getFlag() == 2) {
						riskval = riskval
								+ (answer.getControl().getControl().getRating() * 1);
					}
				}
			}
		}
		return riskval;
	}

	public int getRiskValueforAssessmentCategory(
			AssessmentCategories assessmentCategory) {
		int riskVal = 0;

		List<Answer> answers = controlDao
				.allAnswerbyAssessmentCategory(assessmentCategory);
		for (Answer answer : answers) {
			if (answer.getAnswer().equals(Util.ANS_NO)
					&& answer.getControl().getControl().getFlag() == 1) {
				riskVal = riskVal
						+ (answer.getControl().getControl().getRating() * 1);
			} else if (answer.getAnswer().equals(Util.ANS_YES)
					&& answer.getControl().getControl().getFlag() == 2) {
				riskVal = riskVal
						+ (answer.getControl().getControl().getRating() * 1);
			}
		}

		return riskVal;
	}

	public String getRiskLevelforAssessment(Assessment assessment) {
		String riskLevel = "NA";
		int criticalRiskVal = getCriticalRiskvalueforAssessment(assessment);
		int totalRiskVal = getCriticalRiskvalueforAssessment(assessment);
		if ((criticalRiskVal >= 20) || totalRiskVal >= 75) {
			riskLevel = "D";
		} else if ((criticalRiskVal >= 10 && criticalRiskVal < 20)
				|| (totalRiskVal >= 45 && totalRiskVal < 75)) {
			riskLevel = "C";
		} else if ((criticalRiskVal >= 5 && criticalRiskVal < 10)
				|| (totalRiskVal >= 15 && totalRiskVal < 45)) {
			riskLevel = "B";
		} else if ((criticalRiskVal < 5) || (totalRiskVal < 15)) {
			riskLevel = "A";
		}
		return riskLevel;
	}

	public int getRiskLevelByRiskType(Assessment assessment, String riskType) {
		int riskLevel = 0;
		List<AssessmentCategories> assessmentCategories = assessmentDao
				.assessmentCategoriesByAssessment(assessment);
		for (AssessmentCategories asc : assessmentCategories) {
			List<Answer> answers = controlDao
					.allAnswerbyAssessmentCategory(asc);

			for (Answer answer : answers) {
				if (answer.getControl().getControl().getRisk().equals(riskType)) {
					if (answer.getAnswer().equals(Util.ANS_NO)
							&& answer.getControl().getControl().getFlag() == 1) {
						riskLevel = riskLevel
								+ (answer.getControl().getControl().getRating() * 1);
					} else if (answer.getAnswer().equals(Util.ANS_YES)
							&& answer.getControl().getControl().getFlag() == 2) {
						riskLevel = riskLevel
								+ (answer.getControl().getControl().getRating() * 1);
					}
				}
			}
		}
		return riskLevel;
	}
}
