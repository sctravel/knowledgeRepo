package com.apps.knowledgeRepo.dataModel;

import java.util.List;


public class Bucket {
	
	long bucketId;	
	int sequence;	
	String BucketType;	
	public String getBucketType() {
		return BucketType;
	}
	public void setBucketType(String bucketType) {
		BucketType = bucketType;
	}

	String title;	
	List<Card> cardList; 
	
	
	public long getBucketId() {
		return bucketId;
	}
	public void setBucketId(long bucketId) {
		this.bucketId = bucketId;
	}

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

	public List<Card> getCardList() {
		return cardList;
	}


	public void setCardList(List<Card> cardList) {
		this.cardList = cardList;
	}


	public enum BucketType { 
	
		Chapter, KeyConcept, ByColoumn
	}

	

}
