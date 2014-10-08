package com.apps.knowledgeRepo.dataModel;

public class ExamMetaData {
	
	private String courseId;
	private String courseName;
	private Long courseType;
	private String courseOrientation;
	private String moduleId;
	private String guide;
	private String examId;
	private String examName;

	
	public ExamMetaData(String courseId, String courseName, Long courseType, String courseOrientation,
			String moduleId, String guide, String examId, String examName  ){
		this.courseId = courseId;
		this.courseName = courseName;
		this.courseType = courseType;
		this.courseOrientation = courseOrientation;
		this.moduleId = moduleId;
		this.guide=guide;
		this.examId=examId;
		this.examName=examName;
		
	}
	
	public String getCourseId() {
		return courseId;
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	
	public Long getCourseType() {
		return courseType;
	}
	public void setCourseType(Long courseType) {
		this.courseType = courseType;
	}
	
	public String getCourseOrientation() {
		return courseOrientation;
	}
	public void setCourseOrientation(String courseOrientation) {
		this.courseOrientation = courseOrientation;
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
	
	public String getExamId() {
		return examId;
	}
	public void setExamId(String examId) {
		this.examId = examId;
	}
	public String getExamName() {
		return examName;
	}
	public void setExamName(String examName) {
		this.examName = examName;
	}
	
}
