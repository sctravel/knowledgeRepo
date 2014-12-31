package com.apps.knowledgeRepo.dataModel;

import java.io.Serializable;

public class VideoLesson implements Serializable{
	
	private static final long serialVersionUID = 1L;
	int sequence;
	String URL;
	
	String localPath;

	public VideoLesson() {}
	public VideoLesson(int sequence, String URL) {
		this.sequence = sequence;
		this.URL = URL;
	}
	public VideoLesson(int sequence, String URL, String localPath) {
		this.sequence = sequence;
		this.URL = URL;
		this.localPath = localPath;
	}
	
	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public String getURL() {
		return URL;
	}

	public void setURL(String uRL) {
		URL = uRL;
	}

	public String getLocalPath() {
		return localPath;
	}

	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	} 

}
