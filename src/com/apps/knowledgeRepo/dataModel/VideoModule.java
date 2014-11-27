package com.apps.knowledgeRepo.dataModel;

import java.util.List;

public class VideoModule {
	
	int sequence;
	String title;  
	List<VideoLesson> lessons;
	  
	  
	public int getSequence() {
		return sequence;
	}
	public void setSequence(int sequence) {
		this.sequence = sequence;
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
