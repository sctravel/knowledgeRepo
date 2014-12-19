package com.apps.knowledgeRepo.dataModel;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

public class Exam implements Serializable{
	private static final long serialVersionUID = 1L;
	
	String courseId;
	String moduleId;
	long examId;
	
	String name;
	long passing;
	long timeLimit; 
	List<ExamQuestion> Questions; 
	
	public Exam() {}
	public Exam(String courseId, String moduleId, long examId, String name) {
		this.courseId =courseId;
		this.moduleId = moduleId;
		this.examId = examId;
		this.name=name;
	}
	
	/**
	   * Always treat de-serialization as a full-blown constructor, by
	   * validating the final state of the de-serialized object.
	   */
	   private void readObject(
	     ObjectInputStream aInputStream
	   ) throws ClassNotFoundException, IOException {
	     //always perform the default de-serialization first
	     aInputStream.defaultReadObject();

	  }

	    /**
	    * This is the default implementation of writeObject.
	    * Customise if necessary.
	    */
	    private void writeObject(
	      ObjectOutputStream aOutputStream
	    ) throws IOException {
	      //perform the default serialization for all non-transient, non-static fields
	      aOutputStream.defaultWriteObject();
	    }
	
	public String getCourseId() {
		return courseId;
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	public String getModuleId() {
		return moduleId;
	}
	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}
	
	public long getExamId() {
		return examId;
	}
	public void setExamId(long examId) {
		this.examId = examId;
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
	public List<ExamQuestion> getQuestions() {
		return Questions;
	}
	public void setQuestions(List<ExamQuestion> questions) {
		Questions = questions;
	}
	

}
