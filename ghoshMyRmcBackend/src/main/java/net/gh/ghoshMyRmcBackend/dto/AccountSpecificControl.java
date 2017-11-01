package net.gh.ghoshMyRmcBackend.dto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class AccountSpecificControl {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@ManyToOne
	private AssessmentCategories assessmentCategories;

	@ManyToOne
	private Control control;

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

	public Control getControl() {
		return control;
	}

	public void setControl(Control control) {
		this.control = control;
	}

}
