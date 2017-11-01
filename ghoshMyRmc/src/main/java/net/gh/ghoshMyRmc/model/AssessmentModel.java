package net.gh.ghoshMyRmc.model;

import java.io.Serializable;
import java.util.List;

import net.gh.ghoshMyRmcBackend.dto.Assessment;

public class AssessmentModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private long id;

	private List<Assessment> assessments;

	private String declaration;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<Assessment> getAssessments() {
		return assessments;
	}

	public void setAssessments(List<Assessment> assessments) {
		this.assessments = assessments;
	}

	public String getDeclaration() {
		return declaration;
	}

	public void setDeclaration(String declaration) {
		this.declaration = declaration;
	}

}
