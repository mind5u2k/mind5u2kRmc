package net.gh.ghoshMyRmcBackend.dto;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class AssessmentTrail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private long assessmentId;

	private Timestamp dateOfSubmission;

	@ManyToOne
	private User submittedUser;

	private String declaration;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getAssessmentId() {
		return assessmentId;
	}

	public void setAssessmentId(long assessmentId) {
		this.assessmentId = assessmentId;
	}

	public Timestamp getDateOfSubmission() {
		return dateOfSubmission;
	}

	public void setDateOfSubmission(Timestamp dateOfSubmission) {
		this.dateOfSubmission = dateOfSubmission;
	}

	public User getSubmittedUser() {
		return submittedUser;
	}

	public void setSubmittedUser(User submittedUser) {
		this.submittedUser = submittedUser;
	}

	public String getDeclaration() {
		return declaration;
	}

	public void setDeclaration(String declaration) {
		this.declaration = declaration;
	}

}
