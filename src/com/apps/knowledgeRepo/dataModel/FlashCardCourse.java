package com.apps.knowledgeRepo.dataModel;

import java.util.List;

public class FlashCardCourse extends Course {
	
	private List<FlashCardBucket> buckets;

	public FlashCardCourse() {
	}
	
	public FlashCardCourse(String courseId, String courseName, long courseType,
			String courseOrientation) {
		super(courseId, courseName, courseType, courseOrientation);
		// TODO Auto-generated constructor stub
	}
	
	public List<FlashCardBucket> getBucket() {
		return buckets;
	}

	public void setBucket(List<FlashCardBucket> buckets) {
		this.buckets = buckets;
	} 

}
