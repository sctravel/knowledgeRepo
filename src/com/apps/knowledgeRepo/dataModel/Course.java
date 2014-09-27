package com.apps.knowledgeRepo.dataModel;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.apps.knowledgeRepo.db.DBTool;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Course {
	
	String courseId;
	String courseName;
	long courseType;
	String courseOrientation; 
	List<CourseModule> modules; 
	
	public String getCourseId() {
		return courseId;
	}
	public void setCourseid(String courseId) {
		this.courseId = courseId;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public long getCourseType() {
		return courseType;
	}
	public void setCourseType(long courseType) {
		this.courseType = courseType;
	}
	public String getCourseOrientation() {
		return courseOrientation;
	}
	public void setCourseOrientation(String courseOrientation) {
		this.courseOrientation = courseOrientation;
	}
	public List<CourseModule> getModules() {
		return modules;
	}
	public void setModules(List<CourseModule> modules) {
		this.modules = modules;
	}

	
	public Course(){
		
	}
	
	
}
