package com.apps.knowledgeRepo.dataModel;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

public class Exam implements Serializable{
	private static final long serialVersionUID = 1L;
	
	long examid;
	String name;
	long passing;
	long timeLimit; 
	List<Question> Questions; 
	
	/**
	   * Always treat de-serialization as a full-blown constructor, by
	   * validating the final state of the de-serialized object.
	   */
	   private void readObject(
	     ObjectInputStream aInputStream
	   ) throws ClassNotFoundException, IOException {
	     //always perform the default de-serialization first
	     aInputStream.defaultReadObject();

	  }

	    /**
	    * This is the default implementation of writeObject.
	    * Customise if necessary.
	    */
	    private void writeObject(
	      ObjectOutputStream aOutputStream
	    ) throws IOException {
	      //perform the default serialization for all non-transient, non-static fields
	      aOutputStream.defaultWriteObject();
	    }
	
	public long getExamid() {
		return examid;
	}
	public void setExamid(long examid) {
		this.examid = examid;
	}
	public String getName() {
		return name;
	}
	public void setName(String examName) {
		this.name = examName;
	}
	public long getPassing() {
		return passing;
	}
	public void setPassing(long passing) {
		this.passing = passing;
	}
	public long getTimeLimit() {
		return timeLimit;
	}
	public void setTimeLimit(long timeLimit) {
		this.timeLimit = timeLimit;
	}
	public List<Question> getQuestions() {
		return Questions;
	}
	public void setQuestions(List<Question> questions) {
		Questions = questions;
	}
	

}
