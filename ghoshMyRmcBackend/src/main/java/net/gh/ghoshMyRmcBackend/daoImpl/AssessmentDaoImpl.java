package net.gh.ghoshMyRmcBackend.daoImpl;

import java.util.List;

import net.gh.ghoshMyRmcBackend.dao.AssessmentDao;
import net.gh.ghoshMyRmcBackend.dto.Account;
import net.gh.ghoshMyRmcBackend.dto.Assessment;
import net.gh.ghoshMyRmcBackend.dto.AssessmentCategories;
import net.gh.ghoshMyRmcBackend.dto.AssessmentCategorySMEMapping;
import net.gh.ghoshMyRmcBackend.dto.AssessmentTrail;
import net.gh.ghoshMyRmcBackend.dto.Category;
import net.gh.ghoshMyRmcBackend.dto.User;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("assessmentDao")
@Transactional
public class AssessmentDaoImpl implements AssessmentDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public List<Assessment> assessmentListByStatus(String status) {
		String assessmentQuery = "FROM Assessment WHERE assessmentStatus = :status";

		try {
			return sessionFactory.getCurrentSession()
					.createQuery(assessmentQuery, Assessment.class)
					.setParameter("status", status).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Assessment> assessmentList() {
		String selectActiveCategory = "FROM Assessment";
		Query query = sessionFactory.getCurrentSession().createQuery(
				selectActiveCategory);
		return query.getResultList();
	}

	@Override
	public boolean addAssessment(Assessment assessment) {
		try {
			sessionFactory.getCurrentSession().persist(assessment);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean addAssessmentTrail(AssessmentTrail assessmentTrail) {
		try {
			sessionFactory.getCurrentSession().persist(assessmentTrail);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean deleteAssessment(Assessment assessment) {
		try {
			sessionFactory.getCurrentSession().delete(assessment);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean deleteAssessmentCategory(
			AssessmentCategories assessmentCategories) {
		try {
			sessionFactory.getCurrentSession().delete(assessmentCategories);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean deleteAssessmentCategorySmeMappings(
			AssessmentCategorySMEMapping smeMapping) {
		try {
			sessionFactory.getCurrentSession().delete(smeMapping);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<Assessment> assessmentListByApprover(User user) {
		String assessmentQuery = "FROM Assessment WHERE approver.id = :approverId";

		try {
			return sessionFactory.getCurrentSession()
					.createQuery(assessmentQuery, Assessment.class)
					.setParameter("approverId", user.getId()).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Assessment> assessmentListByAssessor(User user) {
		String assessmentQuery = "FROM Assessment WHERE assessor.id = :assessorId";

		try {
			return sessionFactory.getCurrentSession()
					.createQuery(assessmentQuery, Assessment.class)
					.setParameter("assessorId", user.getId()).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<AssessmentCategories> assessmentCategoriesListByReviewer(
			User user) {
		String assessmentQuery = "FROM AssessmentCategories WHERE reviwer.id = :reviwerId";

		try {
			return sessionFactory.getCurrentSession()
					.createQuery(assessmentQuery, AssessmentCategories.class)
					.setParameter("reviwerId", user.getId()).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<AssessmentCategorySMEMapping> assessmentCategoriessmeMappingListBySmes(
			User Sme) {
		String assessmentQuery = "FROM AssessmentCategorySMEMapping WHERE SME.id = :smeId";

		try {
			return sessionFactory
					.getCurrentSession()
					.createQuery(assessmentQuery,
							AssessmentCategorySMEMapping.class)
					.setParameter("smeId", Sme.getId()).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<AssessmentCategories> assessmentCategoriesByAssessmentandReviewer(
			User reviewer, Assessment assessment) {
		String assessmentQuery = "FROM AssessmentCategories WHERE reviwer.id = :reviwerId AND assessment.id = :assessmentId";

		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					assessmentQuery, AssessmentCategories.class);
			query.setParameter("reviwerId", reviewer.getId());
			query.setParameter("assessmentId", assessment.getId());

			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Assessment getAssessmentByAccount(Account account) {
		String assessmentQuery = "FROM Assessment WHERE account.id = :accId";

		try {
			return sessionFactory.getCurrentSession()
					.createQuery(assessmentQuery, Assessment.class)
					.setParameter("accId", account.getId()).getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<AssessmentCategorySMEMapping> getAssessmentCategorySmeMappingByAssCat(
			AssessmentCategories assessmentCategory) {
		String assessmentQuery = "FROM AssessmentCategorySMEMapping WHERE assessmentCategories.id = :assessmentCategoryId";

		try {
			return sessionFactory
					.getCurrentSession()
					.createQuery(assessmentQuery,
							AssessmentCategorySMEMapping.class)
					.setParameter("assessmentCategoryId",
							assessmentCategory.getId()).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<AssessmentTrail> getAssessmentTrailByAssessment(
			Assessment assessment) {
		String assessmentQuery = "FROM AssessmentTrail WHERE assessmentId = :assessmentId";

		try {
			return sessionFactory.getCurrentSession()
					.createQuery(assessmentQuery, AssessmentTrail.class)
					.setParameter("assessmentId", assessment.getId())
					.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<AssessmentCategorySMEMapping> getAssessmentCategorySmeMappingByAssCatandSme(
			AssessmentCategories assessmentCategory, User sme) {
		String assessmentQuery = "FROM AssessmentCategorySMEMapping WHERE assessmentCategories.id = :assessmentCategoryId AND SME.id = :smeId";

		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					assessmentQuery, AssessmentCategorySMEMapping.class);
			query.setParameter("assessmentCategoryId",
					assessmentCategory.getId());
			query.setParameter("smeId", sme.getId());
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean updateAssessment(Assessment assessment) {
		try {
			sessionFactory.getCurrentSession().update(assessment);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean updateAssessmentCategory(
			AssessmentCategories assessmentCategories) {
		try {
			sessionFactory.getCurrentSession().update(assessmentCategories);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Assessment getAssessmentById(long id) {
		return sessionFactory.getCurrentSession().get(Assessment.class,
				Long.valueOf(id));
	}

	@Override
	public List<AssessmentCategories> assessmentCategoriesByAssessment(
			Assessment assessment) {
		String assessmentQuery = "FROM AssessmentCategories WHERE assessment.id = :assessmentId";

		try {
			return sessionFactory.getCurrentSession()
					.createQuery(assessmentQuery, AssessmentCategories.class)
					.setParameter("assessmentId", assessment.getId())
					.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public AssessmentCategories getExistingAssessmentCategory(
			Assessment assessment, Category category) {
		String assessmentQuery = "FROM AssessmentCategories WHERE assessment.id = :assessmentId AND assignedCategories.id = :catId";

		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					assessmentQuery, AssessmentCategories.class);
			query.setParameter("assessmentId", assessment.getId());
			query.setParameter("catId", category.getId());

			return (AssessmentCategories) query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean addAssessmentCategory(AssessmentCategories assessmentCategory) {
		try {
			sessionFactory.getCurrentSession().persist(assessmentCategory);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean addAssessmentCatSmeMapping(
			AssessmentCategorySMEMapping assessmentCategorySMEMapping) {
		try {
			sessionFactory.getCurrentSession().persist(
					assessmentCategorySMEMapping);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public AssessmentCategories getAssessmentCategoryById(long id) {
		return sessionFactory.getCurrentSession().get(
				AssessmentCategories.class, Long.valueOf(id));
	}

}
