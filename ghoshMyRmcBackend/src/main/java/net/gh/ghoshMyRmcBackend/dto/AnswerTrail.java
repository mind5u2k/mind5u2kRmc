package net.gh.ghoshMyRmcBackend.dto;

import java.sql.Blob;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class AnswerTrail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private long answerId;

	@ManyToOne
	private AccountSpecificControl control;

	private String answer;

	private String comment;

	private String reviewerComment;

	private Blob artifact;

	private String artifaceName;

	private String artifactType;

	private Timestamp dateAnswered;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date mitigationDate;

	private String confirmationStatus;

	private boolean riskAcceptance;

	private String riskAcceptenceby;

	private boolean NC;

	public Date getMitigationDate() {
		return mitigationDate;
	}

	public void setMitigationDate(Date mitigationDate) {
		this.mitigationDate = mitigationDate;
	}

	@ManyToOne
	private User lastRespondedUser;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getAnswerId() {
		return answerId;
	}

	public void setAnswerId(long answerId) {
		this.answerId = answerId;
	}

	public AccountSpecificControl getControl() {
		return control;
	}

	public void setControl(AccountSpecificControl control) {
		this.control = control;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getReviewerComment() {
		return reviewerComment;
	}

	public void setReviewerComment(String reviewerComment) {
		this.reviewerComment = reviewerComment;
	}

	public Blob getArtifact() {
		return artifact;
	}

	public void setArtifact(Blob artifact) {
		this.artifact = artifact;
	}

	public String getArtifaceName() {
		return artifaceName;
	}

	public void setArtifaceName(String artifaceName) {
		this.artifaceName = artifaceName;
	}

	public String getArtifactType() {
		return artifactType;
	}

	public void setArtifactType(String artifactType) {
		this.artifactType = artifactType;
	}

	public Timestamp getDateAnswered() {
		return dateAnswered;
	}

	public void setDateAnswered(Timestamp dateAnswered) {
		this.dateAnswered = dateAnswered;
	}

	public User getLastRespondedUser() {
		return lastRespondedUser;
	}

	public void setLastRespondedUser(User lastRespondedUser) {
		this.lastRespondedUser = lastRespondedUser;
	}

	public String getConfirmationStatus() {
		return confirmationStatus;
	}

	public void setConfirmationStatus(String confirmationStatus) {
		this.confirmationStatus = confirmationStatus;
	}

	public boolean isRiskAcceptance() {
		return riskAcceptance;
	}

	public void setRiskAcceptance(boolean riskAcceptance) {
		this.riskAcceptance = riskAcceptance;
	}

	public String getRiskAcceptenceby() {
		return riskAcceptenceby;
	}

	public void setRiskAcceptenceby(String riskAcceptenceby) {
		this.riskAcceptenceby = riskAcceptenceby;
	}

	public boolean isNC() {
		return NC;
	}

	public void setNC(boolean nC) {
		NC = nC;
	}

}
