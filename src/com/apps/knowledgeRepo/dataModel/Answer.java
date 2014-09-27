package com.apps.knowledgeRepo.dataModel;

import java.io.Serializable;

public class Answer implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public long answerNumber;
	
	public Long score;
	
	public String answerText;
	
	public String userAnswer;



	public long getAnswerNumber() {
		return answerNumber;
	}

	public void setAnswerNumber(long answerNumber) {
		this.answerNumber = answerNumber;
	}

	public Long getScore() {
		return score;
	}

	public void setScore(Long score) {
		this.score = score;
	}

	public String getAnswerText() {
		return answerText;
	}

	public void setAnswerText(String answerText) {
		this.answerText = answerText;
	}
	public String getUserAnswer() {
		return userAnswer;
	}

	public void setUserAnswer(String userAnswer) {
		this.userAnswer = userAnswer;
	}
}
