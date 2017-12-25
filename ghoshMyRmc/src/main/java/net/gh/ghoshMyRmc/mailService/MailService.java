package net.gh.ghoshMyRmc.mailService;

import java.util.ArrayList;
import java.util.List;

import net.gh.ghoshMyRmc.model.MailModel;
import net.gh.ghoshMyRmc.riskAnalysis.RiskCalculation;
import net.gh.ghoshMyRmcBackend.dao.AssessmentDao;
import net.gh.ghoshMyRmcBackend.dao.ControlDao;
import net.gh.ghoshMyRmcBackend.dto.Answer;
import net.gh.ghoshMyRmcBackend.dto.Assessment;
import net.gh.ghoshMyRmcBackend.dto.AssessmentCategories;
import net.gh.ghoshMyRmcBackend.dto.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service("mailService")
public class MailService {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private AssessmentDao assessmentDao;

	@Autowired
	private ControlDao controlDao;

	@Autowired
	private RiskCalculation riskCalculation;

	public boolean sendMailToNewUser(User user, String originalPassword) {

		String mail = "<div style='border: 1px solid #3c56a2;border-bottom: 14px solid #3c56a2;width: 546px; box-shadow: 4px 4px 3px #526ab1 !important; margin: 10px;'><div style='text-align: center;background: #526ab1;padding: 11px;color: #fff;'>My Risk Management and Compliance</div><div style='padding: 11px;'>Hi "
				+ user.getName()
				+ ",<br><br><br>Please find below the UserID and password for My RMC tool.<br><br> MyRMC Tool URL - https://www.vedhacon.com/vedhacon/<br><br><br>Your Credentials are :<br>Email Address : "
				+ user.getEmail()
				+ "<br>password : "
				+ originalPassword
				+ "<br><br><br>If you any query or trouble please contact a site administrator.<br><br><br>Thank you <br>My Risk Management and compliance Team</div></div>";
		return SendMail.SendMail(user.getEmail(), "", "mail for new Password",
				mail);
	}

	public boolean sendNewGeneratedPassword(User user, String originalPassword) {

		String mail = "<div style='border: 1px solid #3c56a2;border-bottom: 14px solid #3c56a2;width: 546px; box-shadow: 4px 4px 3px #526ab1 !important; margin: 10px;'><div style='text-align: center;background: #526ab1;padding: 11px;color: #fff;'>My Risk Management and Compliance</div><div style='padding: 11px;'>Hi "
				+ user.getName()
				+ ",<br><br><br><br>Your password has been reset!<br><br>Your Credentials are :<br><br>Email Id : "
				+ user.getEmail()
				+ "<br>password : "
				+ originalPassword
				+ "<br><br><br>If you any query or trouble please contact a site administrator.<br><br><br>Thank you <br>My Risk Management and compliance Team</div></div>";
		return SendMail.SendMail(user.getEmail(), "", "mail for new Password",
				mail);
	}

	public boolean sendApproverNotificationMail(MailModel mailModel) {

		String mail = "<div style='border: 1px solid #3c56a2;border-bottom: 14px solid #3c56a2;width: 768px; box-shadow: 4px 4px 3px #526ab1 !important; margin: 10px;'><div style='text-align: center;background: #526ab1;padding: 11px;color: #fff;'>My Risk Management and Compliance</div><div style='padding: 11px;'>"
				+ mailModel.getMessage() + "</div></div>";
		return SendMail.SendMail(mailModel.getTo(), mailModel.getCc(),
				mailModel.getSubject(), mail);
	}

	public boolean sendMitigationMailToAssessor(List<Assessment> assessments) {

		for (Assessment a : assessments) {
			String mail = "<div style='border: 1px solid #3c56a2;border-bottom: 14px solid #3c56a2;width: 80%; box-shadow: 4px 4px 3px #526ab1 !important; margin: 10px;'><div style='text-align: center;background: #526ab1;padding: 11px;color: #fff;'>My Risk Management and Compliance</div><div style='padding: 11px;'><div align='left' class='mailBodyTemplate'>"
					+ "Dear User,<br> <br>Our records indicate that mitigation activity for the following Client/Department is pending on MyRMC Tool and require immediate attention:<br><br>Name : "
					+ a.getAccount().getDepartment().getName()
					+ " <br>Location : "
					+ a.getAccount().getLocation().getName()
					+ " <br>LOB : "
					+ a.getAccount().getLob().getName()
					+ " <br>Assessor Name : "
					+ a.getAssessor().getName()
					+ " <br>Approver Name : "
					+ a.getApprover().getName()
					+ " <br><br>You as the registered Assessor for this Client/Department to conduct Information Security Risk Assessment can take either of the following actions:<br>1) Provide the mitigation date with action plan on tool and implement the same. Once implemented, update the response to	indicate closure.<br>2) Accept the risk in case it cannot be mitigated and mention the justification in comments section. Upload the mitigation	evidence as attachment against the accepted risk.<br><br>Please use the following link to launch MyRMC Tool:https://www.vedhacon.com/myrmc<br><br>Below are the risk sources to be mitigated:<br><table width='100%' border='1' cellspacing='0' cellpadding='5'	style='font-family:Arial;border: 0;border-collapse: collapse;'><tbody><tr style='background: #526ab1;'><td style='border: 1px solid #2b4aa6;' align='center' nowrap=''><font color='#FFFFFF'>Category</font></td><td style='border: 1px solid #2b4aa6;' align='center' nowrap=''><font color='#FFFFFF'>Control</font></td><td style='border: 1px solid #2b4aa6;' align='center' nowrap=''><font color='#FFFFFF'>Mitigation Date</font></td><td style='border: 1px solid #2b4aa6;' align='center' nowrap=''><font color='#FFFFFF'>Status</font></td></tr>";

			List<AssessmentCategories> assessmentCategories = assessmentDao
					.assessmentCategoriesByAssessment(a);

			List<Answer> answers = new ArrayList<Answer>();

			if (assessmentCategories != null) {
				for (AssessmentCategories category : assessmentCategories) {
					List<Answer> a1 = controlDao
							.allAnswerbyAssessmentCategory(category);
					if (a1 != null) {
						answers.addAll(a1);
					}
				}
			}
			String answerText = "";
			String answerVal = "";
			String riskAnswers = "";
			if (answers != null) {
				for (Answer answer : answers) {
					System.out.println("answer is  [" + answer.getAnswer()
							+ "]");
					if (riskCalculation.getRiskForAnswer(answer) > 0) {

						riskAnswers = riskAnswers
								+ "<tr><td style='border: 1px solid #2b4aa6;'  nowrap=''>"
								+ answer.getControl().getAssessmentCategories()
										.getAssignedCategories().getName()
								+ "</td><td style='border: 1px solid #2b4aa6;' nowrap='' >"
								+ answer.getControl().getControl().getControl()
								+ "</td><td style='border: 1px solid #2b4aa6;' nowrap=''>"
								+ answer.getMitigationDate()
								+ "</td><td style='border: 1px solid #2b4aa6;' nowrap='' >"
								+ answer.getConfirmationStatus() + "</td></tr>";
						answerText = answerText.concat(answer.getControl()
								.getControl().getShortText()
								+ ",");
						answerVal = answerVal.concat(String
								.valueOf(riskCalculation
										.getRiskForAnswer(answer))
								+ ",");
					}
				}
			}

			mail = mail + riskAnswers;
			mail = mail
					+ "</tbody></table><br><br>You should have received a system generated mail from querymedin@gmail.com with login credentials. In case of a	challenge or you are not able to locate the email, please drop a note at admin@myrmc.com<br><br>Regards<br>MyRMC Team</approver></assessor></lob></location></client></div></div></div>";

			SendMail.SendMail(a.getAssessor().getEmail(), "",
					"Mitigation Mail", mail);
		}

		return true;
	}

	public boolean sendMitigationMailToApprover(List<Assessment> assessments) {

		for (Assessment a : assessments) {
			String mail = "<div style='border: 1px solid #3c56a2;border-bottom: 14px solid #3c56a2;width: 80%; box-shadow: 4px 4px 3px #526ab1 !important; margin: 10px;'><div style='text-align: center;background: #526ab1;padding: 11px;color: #fff;'>My Risk Management and Compliance</div><div style='padding: 11px;'><div align='left' class='mailBodyTemplate'>"
					+ "Dear User,<br> <br>Our records indicate that mitigation activity for the following Client/Department is pending on MyRMC Tool and require immediate attention:<br><br>Name : "
					+ a.getAccount().getDepartment().getName()
					+ " <br>Location : "
					+ a.getAccount().getLocation().getName()
					+ " <br>LOB : "
					+ a.getAccount().getLob().getName()
					+ " <br>Assessor Name : "
					+ a.getAssessor().getName()
					+ " <br>Approver Name : "
					+ a.getApprover().getName()
					+ " <br><br>You as the registered Approver for this Client/Department to conduct Information Security Risk Assessment is requested to guide the Assessor to take either of the following actions:<br>1) Provide the mitigation date with action plan on tool and implement the same. Once implemented, update the response to	indicate closure.<br>2) Accept the risk in case it cannot be mitigated and mention the justification in comments section. Upload the mitigation	evidence as attachment against the accepted risk.<br><br>Please use the following link to launch MyRMC Tool:https://www.vedhacon.com/myrmc<br><br>Below are the risk sources to be mitigated:<br><table width='100%' border='1' cellspacing='0' cellpadding='5'	style='font-family:Arial;border: 0;border-collapse: collapse;'><tbody><tr style='background: #526ab1;'><td style='border: 1px solid #2b4aa6;' align='center' nowrap=''><font color='#FFFFFF'>Category</font></td><td style='border: 1px solid #2b4aa6;' align='center' nowrap=''><font color='#FFFFFF'>Control</font></td><td style='border: 1px solid #2b4aa6;' align='center' nowrap=''><font color='#FFFFFF'>Mitigation Date</font></td><td style='border: 1px solid #2b4aa6;' align='center' nowrap=''><font color='#FFFFFF'>Status</font></td></tr>";

			List<AssessmentCategories> assessmentCategories = assessmentDao
					.assessmentCategoriesByAssessment(a);

			List<Answer> answers = new ArrayList<Answer>();

			if (assessmentCategories != null) {
				for (AssessmentCategories category : assessmentCategories) {
					List<Answer> a1 = controlDao
							.allAnswerbyAssessmentCategory(category);
					if (a1 != null) {
						answers.addAll(a1);
					}
				}
			}
			String answerText = "";
			String answerVal = "";
			String riskAnswers = "";
			if (answers != null) {
				for (Answer answer : answers) {
					System.out.println("answer is  [" + answer.getAnswer()
							+ "]");
					if (riskCalculation.getRiskForAnswer(answer) > 0) {

						riskAnswers = riskAnswers
								+ "<tr><td style='border: 1px solid #2b4aa6;'  nowrap=''>"
								+ answer.getControl().getAssessmentCategories()
										.getAssignedCategories().getName()
								+ "</td><td style='border: 1px solid #2b4aa6;' nowrap='' >"
								+ answer.getControl().getControl().getControl()
								+ "</td><td style='border: 1px solid #2b4aa6;' nowrap=''>"
								+ answer.getMitigationDate()
								+ "</td><td style='border: 1px solid #2b4aa6;' nowrap='' >"
								+ answer.getConfirmationStatus() + "</td></tr>";
						answerText = answerText.concat(answer.getControl()
								.getControl().getShortText()
								+ ",");
						answerVal = answerVal.concat(String
								.valueOf(riskCalculation
										.getRiskForAnswer(answer))
								+ ",");
					}
				}
			}

			mail = mail + riskAnswers;
			mail = mail
					+ "</tbody></table><br><br>You should have received a system generated mail from with login credentials. In case of a challenge or you are not able to locate the email, please drop a note at admin@myrmc.com<br><br>Regards<br>MyRMC Team</approver></assessor></lob></location></client></div></div></div>";

			SendMail.SendMail(a.getApprover().getEmail(), "",
					"Mitigation Mail", mail);
		}

		return true;
	}

	public static void main(String[] args) {
		String mail = "<div style='border: 1px solid #3c56a2;border-bottom: 14px solid #3c56a2;width: 80%; box-shadow: 4px 4px 3px #526ab1 !important; margin: 10px;'><div style='text-align: center;background: #526ab1;padding: 11px;color: #fff;'>My Risk Management and Compliance</div><div style='padding: 11px;'><div align='left' class='mailBodyTemplate'>"
				+ "Dear User,<br> <br>Our records indicate that mitigation activity for the following Client/Department is pending on MyRMC Tool and require immediate attention:<br><br>Name :<client department='' name=''> <br>Location : <location name=''> <br>LOB : <lob name=''> <br>Assessor Name : <assessor name=''> <br>Approver Name : <approver name=''> <br><br>You as the registered Assessor for this Client/Department to conduct Information Security Risk Assessment can take either of the following actions:<br>1) Provide the mitigation date with action plan on tool and implement the same. Once implemented, update the response to	indicate closure.<br>2) Accept the risk in case it cannot be mitigated and mention the justification in comments section. Upload the mitigation	evidence as attachment against the accepted risk.<br><br>Please use the following link to launch MyRMC Tool:https://www.vedhacon.com/myrmc<br><br>Below are the risk sources to be mitigated:<br><table width='100%' border='1' cellspacing='0' cellpadding='5'	style='font-family:Arial;border: 0;border-collapse: collapse;'><tbody><tr style='background: #526ab1;'><td style='border: 1px solid #2b4aa6;' align='center' nowrap=''><font color='#FFFFFF'>Category</font></td><td style='border: 1px solid #2b4aa6;' align='center' nowrap=''><font color='#FFFFFF'>Control</font></td><td style='border: 1px solid #2b4aa6;' align='center' nowrap=''><font color='#FFFFFF'>Mitigation Date</font></td><td style='border: 1px solid #2b4aa6;' align='center' nowrap=''><font color='#FFFFFF'>Status</font></td></tr><tr><td style='border: 1px solid #2b4aa6;'  nowrap='' align='center'>--</td><td style='border: 1px solid #2b4aa6;' nowrap='' align='center'>--</td><td style='border: 1px solid #2b4aa6;' nowrap='' align='center'>--</td><td style='border: 1px solid #2b4aa6;' nowrap='' align='center'>--</td></tr></tbody></table><br><br>You should have received a system generated mail from querymedin@gmail.com with login credentials. In case of a	challenge or you are not able to locate the email, please drop a note at admin@myrmc.com<br><br>Regards<br>MyRMC Team</approver></assessor></lob></location></client></div></div></div>";
		SendMail.SendMail("anurag.ghosh.1014@gmail.com",
				"anuraghosh.1992@rediffmail.com", "zzxcvzxcvzx", mail);
		System.out.println("qwerty");
	}
}
