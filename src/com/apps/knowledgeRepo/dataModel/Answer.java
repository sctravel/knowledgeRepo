package com.apps.knowledgeRepo.dataModel;

public class Answer {
	
	public long answerNumber;
	
	public Long score;
	
	public String answerText;

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

}
