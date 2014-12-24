package com.apps.knowledgeRepo.dataModel;

public class ExamModuleMetaData {
	private Course courseMeta;

	private String moduleId;
	private String guide;
	
	public ExamModuleMetaData(String courseId, String courseName, long courseType, String courseOrientation,
			String moduleId, String guide) {
		this.courseMeta = new Course(courseId, courseName, courseType, courseOrientation);
		this.moduleId = moduleId;
		this.guide = guide;
	}
		
	public String getCourseId() {
		return this.getCourseMeta().getCourseId();
	}
	public String getCourseName() {
		return this.getCourseMeta().getCourseName();
	}
	public Long getCourseType() {
		return this.getCourseMeta().getCourseType();
	}
	public String getCourseOrientation() {
		return this.getCourseMeta().getCourseOrientation();
	}
	
	public Course getCourseMeta() {
		return courseMeta;
	}
	public void setCourseMeta(Course courseMeta) {
		this.courseMeta = courseMeta;
	}
	
	public String getModuleId() {
		return moduleId;
	}
	
	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}
	
	public String getGuide() {
		return guide;
	}
	public void setGuide(String guide) {
		this.guide = guide;
	}
	
}
