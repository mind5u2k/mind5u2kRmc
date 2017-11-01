package net.gh.ghoshMyRmcBackend.daoImpl;

import java.util.List;

import net.gh.ghoshMyRmcBackend.dao.CategoryDao;
import net.gh.ghoshMyRmcBackend.dto.Category;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("categoryDao")
@Transactional
public class CategoryDaoImpl implements CategoryDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public List<Category> categoryLists() {
		String selectActiveCategory = "FROM Category";
		Query query = sessionFactory.getCurrentSession().createQuery(
				selectActiveCategory);
		return query.getResultList();
	}

	@Override
	public boolean addCategory(Category category) {
		try {
			sessionFactory.getCurrentSession().persist(category);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	@Override
	public Category getCategory(long id) {
		return sessionFactory.getCurrentSession().get(Category.class,
				Long.valueOf(id));
	}

	@Override
	public boolean updateCategory(Category category) {
		try {
			sessionFactory.getCurrentSession().update(category);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean deleteCategory(Category category) {
		try {
			sessionFactory.getCurrentSession().delete(category);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
