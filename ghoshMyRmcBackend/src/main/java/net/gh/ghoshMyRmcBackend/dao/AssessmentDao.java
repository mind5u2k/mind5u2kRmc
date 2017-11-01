package net.gh.ghoshMyRmcBackend.dao;

import java.util.List;

import net.gh.ghoshMyRmcBackend.dto.Account;
import net.gh.ghoshMyRmcBackend.dto.Assessment;
import net.gh.ghoshMyRmcBackend.dto.AssessmentCategories;
import net.gh.ghoshMyRmcBackend.dto.AssessmentCategorySMEMapping;
import net.gh.ghoshMyRmcBackend.dto.AssessmentTrail;
import net.gh.ghoshMyRmcBackend.dto.Category;
import net.gh.ghoshMyRmcBackend.dto.User;

public interface AssessmentDao {

	List<Assessment> assessmentList();

	List<Assessment> assessmentListByStatus(String status);

	List<Assessment> assessmentListByApprover(User user);

	List<Assessment> assessmentListByAssessor(User user);

	List<AssessmentCategories> assessmentCategoriesListByReviewer(User user);

	List<AssessmentCategorySMEMapping> assessmentCategoriessmeMappingListBySmes(
			User Sme);

	List<AssessmentCategories> assessmentCategoriesByAssessmentandReviewer(
			User reviewer, Assessment assessment);

	boolean addAssessment(Assessment assessment);

	boolean addAssessmentCategory(AssessmentCategories assessmentCategory);

	boolean addAssessmentCatSmeMapping(
			AssessmentCategorySMEMapping assessmentCategorySMEMapping);

	Assessment getAssessmentById(long id);

	Assessment getAssessmentByAccount(Account account);

	List<AssessmentTrail> getAssessmentTrailByAssessment(Assessment assessment);

	List<AssessmentCategorySMEMapping> getAssessmentCategorySmeMappingByAssCat(
			AssessmentCategories assessmentCategory);

	List<AssessmentCategorySMEMapping> getAssessmentCategorySmeMappingByAssCatandSme(
			AssessmentCategories assessmentCategory, User sme);

	boolean updateAssessment(Assessment assessment);

	boolean updateAssessmentCategory(AssessmentCategories assessmentCategories);

	List<AssessmentCategories> assessmentCategoriesByAssessment(
			Assessment assessment);

	AssessmentCategories getExistingAssessmentCategory(Assessment assessment,
			Category category);

	AssessmentCategories getAssessmentCategoryById(long id);

	boolean addAssessmentTrail(AssessmentTrail assessmentTrail);
}
