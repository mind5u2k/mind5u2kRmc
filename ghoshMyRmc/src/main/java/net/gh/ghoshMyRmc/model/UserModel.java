package net.gh.ghoshMyRmc.model;

import java.io.Serializable;

public class UserModel implements Serializable {

	/**
	 * @author anurag ghosh
	 */

	private static final long serialVersionUID = 1L;

	private long id;
	private String fullName;
	private String email;
	private String role;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "UserModel [id=" + id + ", fullName=" + fullName + ", email="
				+ email + ", role=" + role + "]";
	}

}
