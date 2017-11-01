package net.gh.ghoshMyRmcBackend.daoImpl;

import java.util.List;

import net.gh.ghoshMyRmcBackend.dao.UserDao;
import net.gh.ghoshMyRmcBackend.dto.User;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("userDao")
@Transactional
public class UserDaoImpl implements UserDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public User getUser(long id) {
		return sessionFactory.getCurrentSession().get(User.class,
				Long.valueOf(id));
	}

	@Override
	public List<User> userLists() {
		String selectUsers = "FROM User";
		Query query = sessionFactory.getCurrentSession().createQuery(
				selectUsers);
		return query.getResultList();
	}

	@Override
	public List<User> userListsByRole(String role) {
		String selectUsers = "FROM User WHERE role = :role";
		Query query = sessionFactory.getCurrentSession().createQuery(
				selectUsers);
		query.setParameter("role", role);
		return query.getResultList();
	}

	@Override
	public boolean addUser(User user) {
		try {
			sessionFactory.getCurrentSession().persist(user);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean updateUser(User user) {
		try {
			sessionFactory.getCurrentSession().update(user);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean deleteUser(User user) {
		try {
			sessionFactory.getCurrentSession().delete(user);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public User getUserByEmailId(String emailId) {
		String selectUsers = "FROM User as u WHERE email = :email";

		try {
			return sessionFactory.getCurrentSession()
					.createQuery(selectUsers, User.class)
					.setParameter("email", emailId).getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
}
