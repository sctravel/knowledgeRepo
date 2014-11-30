package com.apps.knowledgeRepo.dataModel;

import java.util.ArrayList;
import java.util.List;

public class VideoCourse extends Course{
		
	List<VideoModule> modules;

	public VideoCourse() {}
	public VideoCourse(String courseId, String courseName, long courseType,
			String courseOrientation) {
		super(courseId, courseName, courseType, courseOrientation);
		this.modules = new ArrayList<VideoModule>();
	}
	
	public VideoCourse(String courseId, String courseName, long courseType,
			String courseOrientation, List<VideoModule> modules) {
		super(courseId, courseName, courseType, courseOrientation);
		this.modules = modules;
	}


	public List<VideoModule> getVideos() {
		return modules;
	}

	public void setVideos(List<VideoModule> videoModules) {
		this.modules = videoModules;
	} 
	
	

}
