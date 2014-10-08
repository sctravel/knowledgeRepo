package com.apps.knowledgeRepo.dataModel;

import java.util.ArrayList;
import java.util.List;

public class CourseModule {
	
	long moduleId; 
	String guide;
	
	List<Exam> exams;
	
	public CourseModule(){
		this.exams = new ArrayList<Exam>();
	}
	public CourseModule(long moduleId, String guide){
		this.moduleId = moduleId;
		this.guide = guide;
		this.exams = new ArrayList<Exam>();
	}
	
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
