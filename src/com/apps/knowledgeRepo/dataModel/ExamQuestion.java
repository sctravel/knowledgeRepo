package com.apps.knowledgeRepo.dataModel;

import java.io.Serializable;
import java.util.List;

public class ExamQuestion implements Serializable {
	
	private static final long serialVersionUID = 1L;

	long questionNumber;
	
	String category;
	
	String text;
	
	String explanation;
	
	List<ExamAnswer> answers;
	
	public List<ExamAnswer> getAnswers() {
		return answers;
	}

	public void setAnswers(List<ExamAnswer> answers) {
		this.answers = answers;
	}

	public long getQuestionNumber() {
		return questionNumber;
	}

	public void setQuestionNumber(long questionNumber) {
		this.questionNumber = questionNumber;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getExplanation() {
		return explanation;
	}

	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}

	
	
	
	
	/*
	int score;
	Explanation explain; 
	
	
	class Explanation{
		
		int answerNumber;
		int score;
		int answerText; 
		
	}*/

}
