package com.apps.knowledgeRepo.dataModel;

import java.util.List;

public class Exam {
	
	long examid;
	public long getExamid() {
		return examid;
	}
	public void setExamid(long examid) {
		this.examid = examid;
	}
	public String getName() {
		return name;
	}
	public void setName(String examName) {
		this.name = examName;
	}
	public long getPassing() {
		return passing;
	}
	public void setPassing(long passing) {
		this.passing = passing;
	}
	public long getTimeLimit() {
		return timeLimit;
	}
	public void setTimeLimit(long timeLimit) {
		this.timeLimit = timeLimit;
	}
	public List<Question> getQuestions() {
		return Questions;
	}
	public void setQuestions(List<Question> questions) {
		Questions = questions;
	}
	String name;
	long passing;
	long timeLimit; 
	List<Question> Questions; 

}
