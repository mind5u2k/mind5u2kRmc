package net.gh.ghoshMyRmcBackend.dto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import net.gh.ghoshMyRmcBackend.Util;

@Entity
public class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@ManyToOne
	private Department department;

	@ManyToOne
	private Location location;

	@ManyToOne
	private LOB lob;

	@ManyToOne
	private Country country;
	private String sector;
	private String phase;
	private String initialRating;
	private String state = Util.NOT_ASSIGNED_ACCOUNT;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public LOB getLob() {
		return lob;
	}

	public void setLob(LOB lob) {
		this.lob = lob;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public String getSector() {
		return sector;
	}

	public void setSector(String sector) {
		this.sector = sector;
	}

	public String getPhase() {
		return phase;
	}

	public void setPhase(String phase) {
		this.phase = phase;
	}

	public String getInitialRating() {
		return initialRating;
	}

	public void setInitialRating(String initialRating) {
		this.initialRating = initialRating;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

}
