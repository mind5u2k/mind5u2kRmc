package net.gh.ghoshMyRmcBackend.dto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class AssessmentCategorySMEMapping {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@ManyToOne
	private AssessmentCategories assessmentCategories;

	@ManyToOne
	private User SME;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public AssessmentCategories getAssessmentCategories() {
		return assessmentCategories;
	}

	public void setAssessmentCategories(
			AssessmentCategories assessmentCategories) {
		this.assessmentCategories = assessmentCategories;
	}

	public User getSME() {
		return SME;
	}

	public void setSME(User sME) {
		SME = sME;
	}

}
