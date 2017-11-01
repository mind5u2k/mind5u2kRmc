package net.gh.ghoshMyRmc.model;

import net.gh.ghoshMyRmcBackend.dto.Answer;
import net.gh.ghoshMyRmcBackend.dto.AnswerCopy;

public class AnswerModel {

	private Answer answer;
	private AnswerCopy answerCopy;
	private boolean status;

	public Answer getAnswer() {
		return answer;
	}

	public void setAnswer(Answer answer) {
		this.answer = answer;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public AnswerCopy getAnswerCopy() {
		return answerCopy;
	}

	public void setAnswerCopy(AnswerCopy answerCopy) {
		this.answerCopy = answerCopy;
	}

}
