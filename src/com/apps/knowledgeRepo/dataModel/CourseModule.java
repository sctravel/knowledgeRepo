package com.apps.knowledgeRepo.dataModel;

import java.util.List;

public class CourseModule {
	
	long moduleId; 
	String guide;
	
	List<Exam> exams;
	
	public long getModuleId() {
		return moduleId;
	}

	public void setModuleId(long moduleId) {
		this.moduleId = moduleId;
	}

	public String getGuide() {
		return guide;
	}

	public void setGuide(String guide) {
		this.guide = guide;
	}

	public List<Exam> getExams() {
		return exams;
	}

	public void setExams(List<Exam> exams) {
		this.exams = exams;
	}

 

}