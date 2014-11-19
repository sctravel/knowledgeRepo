package com.apps.knowledgeRepo.dataModel;

import java.util.List;

public class VideoCourse extends Course{
		
	List<VideoLesson> videos;

	public List<VideoLesson> getVideos() {
		return videos;
	}

	public void setVideos(List<VideoLesson> videos) {
		this.videos = videos;
	} 
	
	

}
