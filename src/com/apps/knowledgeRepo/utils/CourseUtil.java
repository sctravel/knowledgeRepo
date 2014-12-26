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

import com.apps.knowledgeRepo.dataModel.ExamAnswer;
import com.apps.knowledgeRepo.dataModel.Course;
import com.apps.knowledgeRepo.dataModel.Exam;
import com.apps.knowledgeRepo.dataModel.ExamQuestion;
import com.apps.knowledgeRepo.db.DBTool;

public class CourseUtil {
	
	
	public static String retrieveFromDB(String cid, String moduleId, String examId,Context context){
		SQLiteDatabase db = DBTool.getDB(context);
		String examContent = DBTool.queryExam(context, db, cid, moduleId, examId);
		
		if(examContent == null || examContent.isEmpty()) {
			Log.d("retrieveFromDB", " examContent is  null!!!");
		} else {
			examContent=examContent.replaceAll("!!pattern!!", "'");
			Log.d("retrieveFromDB", " examContent is  not null with length--"+examContent.length());
		}
		Log.d("Retrieve fromDB ", " Retrieve fromDB  with length--"+examContent.length());

		return examContent;
	}
	
	public static Course initilizeVideoCourse(Course courseMeta, Context context) {		
		Course videoCourse = DBTool.queryVideoCourse(context, courseMeta);
		return videoCourse;
	}
	
	public static Course initilizeFlashCardCourse(Course courseMeta, Context context){
		
		Course courseObj  = DBTool.queryFlashCardCourse(context, courseMeta);					
		return courseObj;
			
	}
	
	public static Exam initilizeExam( String courseId, String moduleId, String examId, Context context){
		
	    String jsonStr= retrieveFromDB(courseId, moduleId,examId,context);		
		JSONParser parser = new JSONParser();
		
		Exam examObj = new Exam();
		 
		JSONObject exam;
		try {
		   exam = (JSONObject) parser.parse(jsonStr);
     		       		   
		   String name= (String) exam.get("name");           		   
		   Long passing= (Long) exam.get("passing");            		   
		   Long timeLimit= (Long) exam.get("timeLimit");
		   
		   Log.d("JSON parser", "exam: "+ name);
		   
		   examObj.setExamId(Long.valueOf(examId));
		   examObj.setName(name);
		   examObj.setPassing(passing);
		   examObj.setTimeLimit(timeLimit);
		   	            		   
		   JSONArray questions = (JSONArray)exam.get("Questions");	            		   
		   @SuppressWarnings("unchecked")
		   Iterator<JSONObject> questionIterator = questions.iterator();
		   
		   List<ExamQuestion> quesstionObjs = new ArrayList<ExamQuestion>(); 
   	   
	   	   while (questionIterator.hasNext()) {
	   		   
	   		   ExamQuestion questionObj = new ExamQuestion();
	   		   
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
	   		   		            		   
	   		   List<ExamAnswer> answerObjs = new ArrayList<ExamAnswer>(); 
	   		   
	   		   @SuppressWarnings("unchecked")
			   Iterator<JSONObject> answersIterator = answers.iterator();
	       	   
	       	   while (answersIterator.hasNext()) {
	       		   
	       		   ExamAnswer answerObj = new ExamAnswer();
	       		   
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
	

}
