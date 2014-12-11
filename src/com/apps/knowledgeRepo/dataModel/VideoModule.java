package com.apps.knowledgeRepo.dataModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class VideoModule implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	//TODO it is 
	int moduleSequenceId;
	String title;  
	List<VideoLesson> lessons;
	
	public VideoModule(){}
	
	public VideoModule(int moduleSequenceId, String title) {
		this.moduleSequenceId = moduleSequenceId;
		this.title = title;
		this.lessons = new ArrayList<VideoLesson>();
	}
	
	public VideoModule(int moduleSequenceId, String title, List<VideoLesson> lessons) {
		this.moduleSequenceId = moduleSequenceId;
		this.title = title;
		this.lessons = lessons;
	}
	  
	public int getModuleSequenceId() {
		return moduleSequenceId;
	}
	public void setModuleSequenceId(int moduleSequenceId) {
		this.moduleSequenceId = moduleSequenceId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<VideoLesson> getLessons() {
		return lessons;
	}
	public void setLessons(List<VideoLesson> lessons) {
		this.lessons = lessons;
	} 

}
