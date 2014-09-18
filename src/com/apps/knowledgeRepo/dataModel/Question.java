package com.apps.knowledgeRepo.dataModel;

public class Question {
	
	long QuestionNumber;
	
	String category;
	
	String text;
	
	public long getQuestionNumber() {
		return QuestionNumber;
	}

	public void setQuestionNumber(long questionNumber) {
		QuestionNumber = questionNumber;
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

	String explanation;
	
	
	
	/*
	int score;
	Explanation explain; 
	
	
	class Explanation{
		
		int answerNumber;
		int score;
		int answerText; 
		
	}*/

}
