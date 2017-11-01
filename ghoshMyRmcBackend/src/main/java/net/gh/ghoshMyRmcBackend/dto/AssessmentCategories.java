package net.gh.ghoshMyRmcBackend.dto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class AssessmentCategories {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@ManyToOne
	private Assessment assessment;

	@ManyToOne
	private Category assignedCategories;

	@ManyToOne
	private User reviwer;

	private String status;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Assessment getAssessment() {
		return assessment;
	}

	public void setAssessment(Assessment assessment) {
		this.assessment = assessment;
	}

	public Category getAssignedCategories() {
		return assignedCategories;
	}

	public void setAssignedCategories(Category assignedCategories) {
		this.assignedCategories = assignedCategories;
	}

	public User getReviwer() {
		return reviwer;
	}

	public void setReviwer(User reviwer) {
		this.reviwer = reviwer;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
