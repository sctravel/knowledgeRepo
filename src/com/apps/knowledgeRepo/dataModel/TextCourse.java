package com.apps.knowledgeRepo.dataModel;

import java.util.ArrayList;
import java.util.List;


public class TextCourse extends Course{
	
	
	List<TextCourseModule> modules; 
	
	public TextCourse(){}
	
	public TextCourse(String courseId, String courseName, long courseType, String courseOrientation) {
		super(courseId, courseName, courseType, courseOrientation);
		this.modules=new ArrayList<TextCourseModule>();
	}
	
	public TextCourse(String courseId, String courseName, long courseType, String courseOrientation, 
			List<TextCourseModule> modules) {
		super(courseId, courseName, courseType, courseOrientation);
		this.modules = modules;
	}
		
	public List<TextCourseModule> getModules() {
		return modules;
	}
	public void setModules(List<TextCourseModule> modules) {
		this.modules = modules;
	}

	
}
