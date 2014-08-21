package com.apps.knowledgeRepo.om;

public class SingleChoiceAnswer {
	
	private String answer;
	private String explaination;

	public SingleChoiceAnswer(){}
	public SingleChoiceAnswer(String answer, String expl) {
		this.answer = answer;
		this.explaination = expl;
	}
	
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
	public String getExplaination() {
		return explaination;
	}
	public void setExplaination(String explaination) {
		this.explaination = explaination;
	}
}
