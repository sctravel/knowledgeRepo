package com.apps.knowledgeRepo.dataModel;

import java.io.Serializable;
import java.util.List;


public class FlashCardBucket implements Serializable {
	
	private static final long serialVersionUID = 1L;
	long bucketId;	
	int sequence;	
	String BucketType;	
	String title;	
	List<FlashCardCard> cardList; 
	
	public String getBucketType() {
		return BucketType;
	}
	public void setBucketType(String bucketType) {
		BucketType = bucketType;
	}
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

	public List<FlashCardCard> getCardList() {
		return cardList;
	}


	public void setCardList(List<FlashCardCard> cardList) {
		this.cardList = cardList;
	}

	/*
	public enum BucketType { 
	
		Chapter, KeyConcept, ByColoumn
	}*/

	

}
