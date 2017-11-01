package net.gh.ghoshMyRmcBackend.dao;

import java.util.List;

import net.gh.ghoshMyRmcBackend.dto.Account;
import net.gh.ghoshMyRmcBackend.dto.Country;
import net.gh.ghoshMyRmcBackend.dto.Department;
import net.gh.ghoshMyRmcBackend.dto.LOB;
import net.gh.ghoshMyRmcBackend.dto.Location;

public interface AccountDao {

	// --------- Department --------
	Department getDepartment(long id);

	Department getDepartmentByName(String name);

	List<Department> departmentLists();

	boolean addDepartment(Department department);

	boolean updateDepartment(Department department);

	boolean deleteDepartment(Department department);

	// --------- Location --------
	Location getLocation(long id);

	Location getLocationByName(String name);

	List<Location> locationLists();

	boolean addLocation(Location location);

	boolean updateLocation(Location location);

	boolean deleteLocation(Location location);

	// --------- LOB --------
	LOB getLOB(long id);

	LOB getLOBByName(String name);

	List<LOB> lobLists();

	boolean addLOB(LOB lob);

	boolean updateLOB(LOB lob);

	boolean deleteLOB(LOB lob);

	// --------- Country --------
	Country getCountry(long id);

	List<Country> countryLists();

	Country getCountryByName(String name);

	boolean addCountry(Country country);

	boolean updateCountry(Country country);

	boolean deleteCountry(Country country);

	// --------- Account --------
	Account getAccount(long id);

	Account getExistingAccount(Account account);

	List<Account> accountList();

	List<Account> notAssignedAccountList();

	boolean addAccount(Account account);

	boolean updateAccount(Account account);

	boolean deleteAccount(Account account);

}
