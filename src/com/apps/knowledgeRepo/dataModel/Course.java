package com.apps.knowledgeRepo.dataModel;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import android.util.Log;

public class Course {
	
	String courseid;
	public String getCourseid() {
		return courseid;
	}
	public void setCourseid(String courseid) {
		this.courseid = courseid;
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
	String courseName;
	long courseType;
	String courseOrientation; 
	List<CourseModule> modules; 
	
	//store courseID and JSON string to SQLLite database
	public void serialize(){
		
		//output as JSON format
		String jsonContent = null;
		
		
		
		
		
		
		
		
		
		
		
		
		//save to database
		
		storeToDB(courseid, jsonContent);
		
		
	}
	
	public Course(){
		
	
	}
	
	public Course(int CourseId){
				
		String jsonStr= retrieveFromDB(CourseId);
		
		JSONParser parser = new JSONParser();
		Object obj;
		try {
				obj = parser.parse(jsonStr);
				JSONObject course = (JSONObject) obj;
				
				Course courseObj= new Course();
	           String courseid = (String) course.get("courseid");
	           String courseName = (String) course.get("courseName");
	           long courseType = (Long) course.get("courseType");
	           String courseOrientation = (String) course.get("courseOrientation");
	           
	           Log.d("Couse initalizer", "course: "+ courseName);
	           
	           courseObj.setCourseid(courseid);
	           courseObj.setCourseName(courseName);
	           courseObj.setCourseType(courseType);
	           courseObj.setCourseOrientation(courseOrientation);
	           
	           JSONArray courseModules = (JSONArray)course.get("Modules"); 
	           Iterator<JSONObject> modelIterator = courseModules.iterator();
	           
	           List<CourseModule> couseModuleObjs= new ArrayList<CourseModule>();
	           		           	        	   
	           while (modelIterator.hasNext()) {
	        	   
	        	   CourseModule couseModuleObj= new CourseModule();
	        	   
	        	   JSONObject module= (JSONObject)modelIterator.next();   	   
	        	   Long moduleId = (Long) module.get("module");    	   
	        	   String guide = (String) module.get("guide");      	   
	        	   JSONArray exams = (JSONArray)module.get("Exams");
	        	   		        	   
	        	   couseModuleObj.setModuleId(moduleId);
	        	   couseModuleObj.setGuide(guide);

	        	            	   
	        	   Iterator<JSONObject> examIterator = exams.iterator();
     	   
	        	   List<Exam> examObjs = new ArrayList<Exam>(); 
	        	   
	        	   while (examIterator.hasNext()) {
	        		   
	        		   Exam examObj = new Exam(); 
	        		   
	        		   JSONObject exam= (JSONObject)examIterator.next();          		   
	        		   Long examid= (Long) exam.get("examid");           		   
	        		   String name= (String) exam.get("name");           		   
	        		   Long passing= (Long) exam.get("passing");            		   
	        		   Long timeLimit= (Long) exam.get("timeLimit");
	        		   
	        		   Log.d("JSON parser", "exam: "+ name);
	        		   
	        		   examObj.setExamid(examid);
	        		   examObj.setName(name);
	        		   examObj.setPassing(passing);
	        		   examObj.setTimeLimit(timeLimit);
	        		   	            		   
	        		   JSONArray questions = (JSONArray)exam.get("Questions");	            		   
	        		   Iterator<JSONObject> questionIterator = questions.iterator();
	        		   
	        		   List<Question> quesstionObjs = new ArrayList<Question>(); 
	            	   
	            	   while (questionIterator.hasNext()) {
	            		   
	            		   Question questionObj = new Question();
	            		   
	            		   JSONObject question= (JSONObject)questionIterator.next();		            		   
	            		   Long questionNumber= (Long) question.get("questionNumber");		            		   
	            		   String category= (String) question.get("category");	            		   
	            		   String text= (String) question.get("text");	            		   
	            		   String explanation= (String) question.get("explanation");	            		   
	            		   JSONArray answers = (JSONArray)question.get("Answers");
	            		   
	            		   
	            		   questionObj.setCategory(category);
	            		   questionObj.setText(text);
	            		   questionObj.setQuestionNumber(questionNumber);
	            		   questionObj.setExplanation(explanation);
	            		   		            		   
	            		   List<Answer> answerObjs = new ArrayList<Answer>(); 
	            		   
	            		   Iterator<JSONObject> answersIterator = answers.iterator();
		            	   
		            	   while (answersIterator.hasNext()) {
		            		   
		            		   Answer answerObj = new Answer();
		            		   
		            		   JSONObject answer= (JSONObject)answersIterator.next();         		   
		            		   Long answerNumber= (Long) answer.get("answerNumber");           		   
		            		   Long score= (Long) answer.get("score");            					            		   
		            		   String answerText= (String) answer.get("answerText");
		            		   			            		   
		            		   answerObj.setAnswerNumber(answerNumber);
		            		   answerObj.setAnswerText(answerText);
		            		   answerObj.setScore(score);			            		   
		            		   answerObjs.add(answerObj);			            		   
		            	   } 
		            	   questionObj.setAnswers(answerObjs);
		            	   quesstionObjs.add(questionObj);
	            	   }
	            	   	examObj.setQuestions(quesstionObjs);		            	   	
	            	   	examObjs.add(examObj);
	        	   } 		        	   
	        	   couseModuleObj.setExams(examObjs);
	        	   couseModuleObjs.add(couseModuleObj);
	           } 
	           
	           courseObj.setModules(couseModuleObjs);
	           Log.d("Couse initalizer","courseid:" + courseid); 				
		} catch (ParseException e) {
		
			Log.d("Couse initalizer","parser error during initialization");
		}
		catch(Exception ex){
			
			Log.d("Couse initalizer",ex.getMessage());
		}		 
		
	}
	 // to-do BoChen to connect SQLLite
	public String retrieveFromDB(int CourseID){
		
		return null;
	}
	
	public void storeToDB(String CourseID, String jsonContent){
		
		return; 
		
	}

}
