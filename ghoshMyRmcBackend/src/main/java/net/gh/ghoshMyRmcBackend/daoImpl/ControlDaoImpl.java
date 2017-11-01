package net.gh.ghoshMyRmcBackend.daoImpl;

import java.util.List;

import net.gh.ghoshMyRmcBackend.dao.ControlDao;
import net.gh.ghoshMyRmcBackend.dto.AccountSpecificControl;
import net.gh.ghoshMyRmcBackend.dto.Answer;
import net.gh.ghoshMyRmcBackend.dto.AnswerCopy;
import net.gh.ghoshMyRmcBackend.dto.AnswerTrail;
import net.gh.ghoshMyRmcBackend.dto.AssessmentCategories;
import net.gh.ghoshMyRmcBackend.dto.Category;
import net.gh.ghoshMyRmcBackend.dto.Control;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("controlDao")
@Transactional
public class ControlDaoImpl implements ControlDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public List<Control> controlLists() {
		String selectActiveCategory = "FROM Control";
		Query query = sessionFactory.getCurrentSession().createQuery(
				selectActiveCategory);
		return query.getResultList();
	}

	@Override
	public List<Control> controlListsByCategory(Category category) {
		String controlQuery = "FROM Control WHERE category.id = :catId";

		try {
			return sessionFactory.getCurrentSession()
					.createQuery(controlQuery, Control.class)
					.setParameter("catId", category.getId()).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<AccountSpecificControl> accSpecControlByCategory(
			AssessmentCategories category) {
		String accSpecControlQuery = "FROM AccountSpecificControl WHERE assessmentCategories.id = :catId";

		try {
			return sessionFactory
					.getCurrentSession()
					.createQuery(accSpecControlQuery,
							AccountSpecificControl.class)
					.setParameter("catId", category.getId()).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Answer> allAnswerbyAssessmentCategory(
			AssessmentCategories assessmentCategory) {
		String answerQuery = "FROM Answer WHERE control.assessmentCategories.id = :catId";

		try {
			return sessionFactory.getCurrentSession()
					.createQuery(answerQuery, Answer.class)
					.setParameter("catId", assessmentCategory.getId())
					.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<AnswerCopy> allAnswerCopiesbyAssessmentCategory(
			AssessmentCategories assessmentCategory) {
		String answerQuery = "FROM AnswerCopy WHERE control.assessmentCategories.id = :catId";

		try {
			return sessionFactory.getCurrentSession()
					.createQuery(answerQuery, AnswerCopy.class)
					.setParameter("catId", assessmentCategory.getId())
					.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<AnswerTrail> allAnswerTrailByAnswer(Answer answer) {
		String answerQuery = "FROM AnswerTrail WHERE answerId = :ansId";

		try {
			return sessionFactory.getCurrentSession()
					.createQuery(answerQuery, AnswerTrail.class)
					.setParameter("ansId", answer.getId()).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public AccountSpecificControl accSpecificControlByCtrl(Control control,
			AssessmentCategories assessmentCategories) {
		String accSpecControlQuery = "FROM AccountSpecificControl WHERE control.id = :controlId AND assessmentCategories.id = :assCat";

		try {

			Query query = sessionFactory.getCurrentSession().createQuery(
					accSpecControlQuery, AccountSpecificControl.class);
			query.setParameter("controlId", control.getId());
			query.setParameter("assCat", assessmentCategories.getId());
			return (AccountSpecificControl) query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Answer getAnswerByAccSpecControl(AccountSpecificControl control) {
		String answerQuery = "FROM Answer WHERE control.id = :controlId";

		try {
			return sessionFactory.getCurrentSession()
					.createQuery(answerQuery, Answer.class)
					.setParameter("controlId", control.getId())
					.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Control getControl(long id) {
		return sessionFactory.getCurrentSession().get(Control.class,
				Long.valueOf(id));
	}

	@Override
	public Answer getAnswer(long id) {
		return sessionFactory.getCurrentSession().get(Answer.class,
				Long.valueOf(id));
	}

	@Override
	public AnswerTrail getAnswerTrail(long id) {
		return sessionFactory.getCurrentSession().get(AnswerTrail.class,
				Long.valueOf(id));
	}

	@Override
	public AccountSpecificControl getAccSpecControl(long id) {
		return sessionFactory.getCurrentSession().get(
				AccountSpecificControl.class, Long.valueOf(id));
	}

	@Override
	public boolean addControl(Control control) {
		try {
			sessionFactory.getCurrentSession().persist(control);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean addAccSpecControl(
			AccountSpecificControl accountSpecificControl) {
		try {
			sessionFactory.getCurrentSession().persist(accountSpecificControl);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean updateControl(Control control) {
		try {
			sessionFactory.getCurrentSession().update(control);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean deleteControl(Control control) {
		try {
			sessionFactory.getCurrentSession().delete(control);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean deleteAccSpecControl(AccountSpecificControl control) {
		try {
			sessionFactory.getCurrentSession().delete(control);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean addAnswer(Answer answer) {
		try {
			sessionFactory.getCurrentSession().persist(answer);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Long saveAnswer(Answer answer) {
		try {
			return (Long) sessionFactory.getCurrentSession().save(answer);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Long saveAnswerCopy(AnswerCopy answerCopy) {
		try {
			return (Long) sessionFactory.getCurrentSession().save(answerCopy);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean addAnswerTrail(AnswerTrail answerTrail) {
		try {
			sessionFactory.getCurrentSession().persist(answerTrail);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean updateAnswer(Answer answer) {
		try {
			sessionFactory.getCurrentSession().update(answer);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
