package com.apps.knowledgeRepo.dataModel;

import java.util.List;

public class FlashCardCourse extends Course {
	
	
	private List<Bucket> buckets;

	public List<Bucket> getBucket() {
		return buckets;
	}

	public void setBucket(List<Bucket> buckets) {
		this.buckets = buckets;
	} 
	
	
	
	
	

}
