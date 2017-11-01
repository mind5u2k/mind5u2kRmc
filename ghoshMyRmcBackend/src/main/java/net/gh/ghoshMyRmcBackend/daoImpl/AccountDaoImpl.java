package net.gh.ghoshMyRmcBackend.daoImpl;

import java.util.List;

import net.gh.ghoshMyRmcBackend.Util;
import net.gh.ghoshMyRmcBackend.dao.AccountDao;
import net.gh.ghoshMyRmcBackend.dto.Account;
import net.gh.ghoshMyRmcBackend.dto.Country;
import net.gh.ghoshMyRmcBackend.dto.Department;
import net.gh.ghoshMyRmcBackend.dto.LOB;
import net.gh.ghoshMyRmcBackend.dto.Location;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("accountDao")
@Transactional
public class AccountDaoImpl implements AccountDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public Department getDepartment(long id) {
		return sessionFactory.getCurrentSession().get(Department.class,
				Long.valueOf(id));
	}

	@Override
	public Department getDepartmentByName(String name) {
		String departmentQuery = "FROM Department WHERE name = :name";

		try {
			return sessionFactory.getCurrentSession()
					.createQuery(departmentQuery, Department.class)
					.setParameter("name", name).getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Department> departmentLists() {
		String selectActiveCategory = "FROM Department";
		Query query = sessionFactory.getCurrentSession().createQuery(
				selectActiveCategory);
		return query.getResultList();
	}

	@Override
	public boolean addDepartment(Department department) {
		try {
			sessionFactory.getCurrentSession().persist(department);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean updateDepartment(Department department) {
		try {
			sessionFactory.getCurrentSession().update(department);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean deleteDepartment(Department department) {
		try {
			sessionFactory.getCurrentSession().delete(department);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Location getLocation(long id) {
		return sessionFactory.getCurrentSession().get(Location.class,
				Long.valueOf(id));
	}

	@Override
	public Location getLocationByName(String name) {
		String locationQuery = "FROM Location WHERE name = :name";

		try {
			return sessionFactory.getCurrentSession()
					.createQuery(locationQuery, Location.class)
					.setParameter("name", name).getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Location> locationLists() {
		String selectActiveCategory = "FROM Location";
		Query query = sessionFactory.getCurrentSession().createQuery(
				selectActiveCategory);
		return query.getResultList();
	}

	@Override
	public boolean addLocation(Location location) {
		try {
			sessionFactory.getCurrentSession().persist(location);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean updateLocation(Location location) {
		try {
			sessionFactory.getCurrentSession().update(location);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean deleteLocation(Location location) {
		try {
			sessionFactory.getCurrentSession().delete(location);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public LOB getLOB(long id) {
		return sessionFactory.getCurrentSession().get(LOB.class,
				Long.valueOf(id));
	}

	@Override
	public LOB getLOBByName(String name) {
		String lobQuery = "FROM LOB WHERE name = :name";

		try {
			return sessionFactory.getCurrentSession()
					.createQuery(lobQuery, LOB.class)
					.setParameter("name", name).getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<LOB> lobLists() {
		String selectActiveCategory = "FROM LOB";
		Query query = sessionFactory.getCurrentSession().createQuery(
				selectActiveCategory);
		return query.getResultList();
	}

	@Override
	public boolean addLOB(LOB lob) {
		try {
			sessionFactory.getCurrentSession().persist(lob);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean updateLOB(LOB lob) {
		try {
			sessionFactory.getCurrentSession().update(lob);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean deleteLOB(LOB lob) {
		try {
			sessionFactory.getCurrentSession().delete(lob);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Country getCountry(long id) {
		return sessionFactory.getCurrentSession().get(Country.class,
				Long.valueOf(id));
	}

	@Override
	public Country getCountryByName(String name) {
		String countryQuery = "FROM Country WHERE name = :name";

		try {
			return sessionFactory.getCurrentSession()
					.createQuery(countryQuery, Country.class)
					.setParameter("name", name).getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Country> countryLists() {
		String selectActiveCategory = "FROM Country";
		Query query = sessionFactory.getCurrentSession().createQuery(
				selectActiveCategory);
		return query.getResultList();
	}

	@Override
	public boolean addCountry(Country country) {
		try {
			sessionFactory.getCurrentSession().persist(country);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean updateCountry(Country country) {
		try {
			sessionFactory.getCurrentSession().update(country);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean deleteCountry(Country country) {
		try {
			sessionFactory.getCurrentSession().delete(country);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Account getAccount(long id) {
		return sessionFactory.getCurrentSession().get(Account.class,
				Long.valueOf(id));
	}

	@Override
	public Account getExistingAccount(Account account) {
		String accountQuery = "FROM Account WHERE department.id = :depId AND location.id = :locationId AND lob.id = :lobId AND country.id = :countryId";

		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					accountQuery);
			query.setParameter("depId", account.getDepartment().getId());
			query.setParameter("locationId", account.getLocation().getId());
			query.setParameter("lobId", account.getLob().getId());
			query.setParameter("countryId", account.getCountry().getId());
			Account existingAccount = null;
			if (query.getSingleResult() != null) {
				existingAccount = (Account) query.getSingleResult();
			}
			return existingAccount;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Account> accountList() {
		String selectActiveCategory = "FROM Account";
		Query query = sessionFactory.getCurrentSession().createQuery(
				selectActiveCategory);
		return query.getResultList();
	}

	@Override
	public List<Account> notAssignedAccountList() {
		String selectActiveCategory = "FROM Account WHERE state=:state";
		Query query = sessionFactory.getCurrentSession()
				.createQuery(selectActiveCategory)
				.setParameter("state", Util.NOT_ASSIGNED_ACCOUNT);
		return query.getResultList();
	}

	@Override
	public boolean addAccount(Account account) {
		try {
			sessionFactory.getCurrentSession().persist(account);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean updateAccount(Account account) {
		try {
			sessionFactory.getCurrentSession().update(account);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean deleteAccount(Account account) {
		try {
			sessionFactory.getCurrentSession().delete(account);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
