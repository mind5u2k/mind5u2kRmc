package net.gh.ghoshMyRmcBackend.dao;

import java.util.List;

import net.gh.ghoshMyRmcBackend.dto.AccountSpecificControl;
import net.gh.ghoshMyRmcBackend.dto.Answer;
import net.gh.ghoshMyRmcBackend.dto.AnswerCopy;
import net.gh.ghoshMyRmcBackend.dto.AnswerTrail;
import net.gh.ghoshMyRmcBackend.dto.AssessmentCategories;
import net.gh.ghoshMyRmcBackend.dto.Category;
import net.gh.ghoshMyRmcBackend.dto.Control;

public interface ControlDao {

	Control getControl(long id);

	AccountSpecificControl getAccSpecControl(long id);

	List<Control> controlLists();

	List<Control> controlListsByCategory(Category category);

	List<AccountSpecificControl> accSpecControlByCategory(
			AssessmentCategories assessmentCategory);

	List<Answer> allAnswerbyAssessmentCategory(
			AssessmentCategories assessmentCategory);

	List<AnswerCopy> allAnswerCopiesbyAssessmentCategory(
			AssessmentCategories assessmentCategory);

	List<AnswerTrail> allAnswerTrailByAnswer(Answer answer);

	AccountSpecificControl accSpecificControlByCtrl(Control control,
			AssessmentCategories assCat);

	Answer getAnswerByAccSpecControl(AccountSpecificControl control);

	Answer getAnswer(long id);

	AnswerTrail getAnswerTrail(long id);

	boolean addControl(Control control);

	boolean addAccSpecControl(AccountSpecificControl accountSpecificControl);

	boolean updateControl(Control control);

	boolean deleteControl(Control control);

	boolean deleteAccSpecControl(AccountSpecificControl control);

	boolean addAnswer(Answer answer);

	Long saveAnswer(Answer answer);

	Long saveAnswerCopy(AnswerCopy answerCopy);

	boolean updateAnswer(Answer answer);

	boolean addAnswerTrail(AnswerTrail answerTrail);

}
