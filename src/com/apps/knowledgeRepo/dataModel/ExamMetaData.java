package com.apps.knowledgeRepo.dataModel;

public class ExamMetaData {
	
	private String examName;
	private String examId;
	private ExamModuleMetaData examModuleMeta;
	
	public ExamMetaData(String courseId, String courseName, Long courseType, String courseOrientation,
			String moduleId, String guide, String examId, String examName  ){
		this.examModuleMeta = new ExamModuleMetaData(courseId, courseName, courseType, courseOrientation, moduleId, guide);
		this.examId=examId;
		this.examName=examName;
		
	}
	
	public ExamModuleMetaData getExamModuleMetaData() {
		return this.examModuleMeta;
	}
	public void set(ExamModuleMetaData examModuleMeta) {
		this.examModuleMeta = examModuleMeta;
	}
	
	public String getCourseId() {
		return examModuleMeta.getCourseMeta().getCourseId();
	}
	
	public String getCourseName() {
		return examModuleMeta.getCourseMeta().getCourseName();
	}

	public Long getCourseType() {
		return examModuleMeta.getCourseMeta().getCourseType();
	}
	
	public String getCourseOrientation() {
		return examModuleMeta.getCourseMeta().getCourseOrientation();
	}
	
	public String getModuleId() {
		return examModuleMeta.getModuleId();
	}
	

	public String getGuide() {
		return examModuleMeta.getGuide();
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
