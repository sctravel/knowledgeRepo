package com.apps.knowledgeRepo.dataModel;

import java.util.List;

public class FlashCardCourse extends Course {
	
	private List<Bucket> buckets;

	public FlashCardCourse() {
	}
	
	public FlashCardCourse(String courseId, String courseName, long courseType,
			String courseOrientation) {
		super(courseId, courseName, courseType, courseOrientation);
		// TODO Auto-generated constructor stub
	}
	
	public List<Bucket> getBucket() {
		return buckets;
	}

	public void setBucket(List<Bucket> buckets) {
		this.buckets = buckets;
	} 

}
