package com.apps.knowledgeRepo.dataModel;

//This is a parent class for all the courses
// It contains all the common information for all the courses
// like Course MetaData
public class Course {
	String courseId;
	String courseName;
	long courseType;
	String courseOrientation; 
	
	public Course() {}
	
	public Course(String courseId, String courseName, long courseType, String courseOrientation) {
		this.courseId = courseId;
		this.courseName = courseName;
		this.courseType = courseType;
		this.courseOrientation = courseOrientation;
	}
	
	public String getCourseId() {
		return courseId;
	}
	public void setCourseid(String courseId) {
		this.courseId = courseId;
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
}
