package com.apps.knowledgeRepo.dataModel;

import java.io.Serializable;

public class ExamAnswer implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public long answerNumber;
	
	public Long score; //0 is wrong and 1 is correct
	
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
