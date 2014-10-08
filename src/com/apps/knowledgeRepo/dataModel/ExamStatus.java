package com.apps.knowledgeRepo.dataModel;

import java.util.HashMap;
import java.util.Map;

public class ExamStatus {
	
	private String courseId;
	private String examId;
	private String moduleId;
	private int attempt;
	private long usedTime; //in mili-second
	
	private Map<Integer, String> userAnswerMap;
	
	public ExamStatus() {
		userAnswerMap = new HashMap<Integer, String>();
		usedTime=0;
		attempt=1;
	}
	
	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}
	public String getModuleId() {
		return moduleId;
	}
	public void setCourseId(String courseId){
		this.courseId = courseId;
	}
	public String getCourseId() {
		return courseId;
	}
	
	public void setExamId(String examId) {
		this.examId = examId;
	}
	public String getExamId() {
		return examId;
	}
	public void setAttempt(int attempt) {
		this.attempt = attempt;
	}
	public int getAttempt() {
		return attempt;
	}
	public void setUsedTime(long usedTime) {
		this.usedTime = usedTime;
	}
    public long getUsedTime() {
    	return this.usedTime;
    }
	
    public Map<Integer, String> getUserAnswerMap() {
    	return this.userAnswerMap;
    }
}
