package com.apps.knowledgeRepo.dataModel;

public class Card {
	
	int cardId; 	
	String name; 
	String frontText;
	String backText; 
	
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
	
	public Card(){
		
			
	}
	
	
	public enum CardType { 
		
		Math, Normal 
	}
		

}
