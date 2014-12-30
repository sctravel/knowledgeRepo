package com.apps.knowledgeRepo.dataModel;

import java.io.Serializable;

public class FlashCardCard implements Serializable {
	
	private static final long serialVersionUID = 1L;
	int cardId; 	
	String name; 
	String frontText;
	String backText; 
	CardType cardType; 
	
	public CardType getCardType() {
		return cardType;
	}


	public void setCardType(CardType cardType) {
		this.cardType = cardType;
	}


	public int getCardId() {
		return cardId;
	}


	public void setCardId(int cardId) {
		this.cardId = cardId;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getFrontText() {
		return frontText;
	}


	public void setFrontText(String frontText) {
		this.frontText = frontText;
	}


	public String getBackText() {
		return backText;
	}


	public void setBackText(String backText) {
		this.backText = backText;
	}
	
	public FlashCardCard(){
		
			
	}
	
	
	public enum CardType { 
		
		Math(0), Normal(1);
		
		private int cardType;

	    private CardType(int cardType) {
	    	this.cardType = cardType;
	    }
	    public int getCardType() {
	    	return cardType;
	    }

	
	}
		

}
