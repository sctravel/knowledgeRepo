package com.apps.knowledgeRepo.dataModel;

import java.util.List;

public class Course {
	
	String courseid;
	public String getCourseid() {
		return courseid;
	}
	public void setCourseid(String courseid) {
		this.courseid = courseid;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public long getCourseType() {
		return courseType;
	}
	public void setCourseType(long courseType) {
		this.courseType = courseType;
	}
	public String getCourseOrientation() {
		return courseOrientation;
	}
	public void setCourseOrientation(String courseOrientation) {
		this.courseOrientation = courseOrientation;
	}
	public List<CourseModule> getModules() {
		return modules;
	}
	public void setModules(List<CourseModule> modules) {
		this.modules = modules;
	}
	String courseName;
	long courseType;
	String courseOrientation; 
	List<CourseModule> modules; 
	
	
	public void serialize(){
		
		
	}
	
	public void initialize(){
		
		
	}

}
