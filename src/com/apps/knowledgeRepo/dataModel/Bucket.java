package com.apps.knowledgeRepo.dataModel;

import java.util.List;


public class Bucket {
	
	long bucketId;	
	int sequence;	
	BucketType type;	
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


	public BucketType getType() {
		return type;
	}


	public void setType(BucketType type) {
		this.type = type;
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
	
		Chapeter, KeyConcept, ByColoumn
	}
	

}
