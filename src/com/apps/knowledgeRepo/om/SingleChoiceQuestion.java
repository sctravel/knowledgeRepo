package com.apps.knowledgeRepo.om;

import java.util.ArrayList;
import java.util.List;

public class SingleChoiceQuestion {
	private String question;
	private List<String> choices;
	
	public SingleChoiceQuestion() {
		question=null;
		choices = new ArrayList<String>();
	}
	public SingleChoiceQuestion(String question, List<String> choices) {
		this.question = question;
		this.choices = choices;
	}
	
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	
	public List<String> getChoices() {
		return choices;
	}
	public void setChoices(List<String> choices) {
		this.choices = choices;
	}
}
