package com.apps.knowledgeRepo.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.apps.knowledgeRepo.dataModel.Answer;
import com.apps.knowledgeRepo.dataModel.Course;
import com.apps.knowledgeRepo.dataModel.CourseModule;
import com.apps.knowledgeRepo.dataModel.Exam;
import com.apps.knowledgeRepo.dataModel.Question;
import com.apps.knowledgeRepo.db.DBTool;

public class CourseUtil {
	
	
	public static String retrieveFromDB(Long examId,Context context){
		SQLiteDatabase db = DBTool.getDB(context);
		String examContent = DBTool.queryExam(context, db, examId);
		
		if(examContent == null || examContent.isEmpty()) {
			Log.d("retrieveFromDB", " examContent is  null!!!");
		} else {
			examContent=examContent.replaceAll("!!pattern!!", "'");
			Log.d("retrieveFromDB", " examContent is  not null with length--"+examContent.length());
		}
		Log.d("Retrieve fromDB ", " Retrieve fromDB  with length--"+examContent.length());

		return examContent;
	}
	
	public static String retrieveFromDB(String courseId,Context context){
		SQLiteDatabase db = DBTool.getDB(context);
		String courseContent = DBTool.queryCourse(context, db, courseId);
		
		if(courseContent == null || courseContent.isEmpty()) {
			Log.d("retrieveFromDB", " courseContent is  null!!!");
		} else {
			courseContent=courseContent.replaceAll("!!pattern!!", "'");
			Log.d("retrieveFromDB", " courseContent is  not null with length--"+courseContent.length());
		}
		Log.d("Retrieve fromDB ", " Retrieve fromDB  with length--"+courseContent.length());

		return courseContent;
	}
	
	
	public static Exam initilizeExam(Long examId, Context context){
		
		   String jsonStr= retrieveFromDB(examId,context);		
		   JSONParser parser = new JSONParser();
		
		   Exam examObj = new Exam();
		 
		   JSONObject exam;
		try {
			exam = (JSONObject) parser.parse(jsonStr);
     		       		   
		   String name= (String) exam.get("name");           		   
		   Long passing= (Long) exam.get("passing");            		   
		   Long timeLimit= (Long) exam.get("timeLimit");
		   
		   Log.d("JSON parser", "exam: "+ name);
		   
		   examObj.setExamId(examId);
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
   	   	
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			Log.d("Exam initalizer","parser error during initialization"+e.getMessage());
		}
		
		catch (Exception e) {
			// TODO Auto-generated catch block
			Log.d("Exam initalizer","parser error during initialization"+e.getMessage());
		}
  
	    		        	   
		return examObj; 
	}
	
	public static Course initilizeCourse(String courseId, Context context){
		Course courseObj= new Course();

		String jsonStr= retrieveFromDB(courseId,context);		
		JSONParser parser = new JSONParser();
		Object obj;
		try {
			    Log.d("Json String",jsonStr);
				Log.d("Intialize Course", " Intialize Course with length--"+jsonStr.length());

				JSONObject course = (JSONObject) parser.parse(jsonStr);
				 //course = (JSONObject) obj;
				Log.d("Finish parsing","Finish parsing");
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

	        	            	   
	        	   @SuppressWarnings("unchecked")
	        	   Iterator<JSONObject> examIterator = exams.iterator();
     	   
	        	   List<Exam> examObjs = new ArrayList<Exam>(); 
	        	   
	        	   while (examIterator.hasNext()) {
	        		   
	        		   Exam examObj = new Exam(); 
	        		   
	        		   JSONObject exam= (JSONObject)examIterator.next();          		   
	        		   Long examId= (Long) exam.get("examid");           		   
	        		   String name= (String) exam.get("name");           		   
	        		   Long passing= (Long) exam.get("passing");            		   
	        		   Long timeLimit= (Long) exam.get("timeLimit");
	        		   
	        		   Log.d("JSON parser", "exam: "+ name);
	        		   
	        		   examObj.setExamId(examId);
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
		
			Log.d("Course initalizer","parser error during initialization");
		}
		catch(Exception ex){
			
			Log.d("Course initalizer",ex.getMessage());
		}	
		
		return courseObj;
	}
}
