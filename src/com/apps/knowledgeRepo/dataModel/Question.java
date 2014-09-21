package com.apps.knowledgeRepo.dataModel;

import java.util.List;

public class Question {
	
	long questionNumber;
	
	String category;
	
	String text;
	
	String explanation;
	
	List<Answer> answers;
	
	public List<Answer> getAnswers() {
		return answers;
	}

	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}

	public long getQuestionNumber() {
		return questionNumber;
	}

	public void setQuestionNumber(long questionNumber) {
		questionNumber = questionNumber;
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
