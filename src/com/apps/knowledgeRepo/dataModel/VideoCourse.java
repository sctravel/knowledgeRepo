package com.apps.knowledgeRepo.dataModel;

import java.util.List;

public class VideoCourse extends Course{
		
	List<VideoModule> modules;

	public List<VideoModule> getVideos() {
		return modules;
	}

	public void setVideos(List<VideoModule> videmodulesos) {
		this.modules = modules;
	} 
	
	

}
