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
	
	String courseid;
	String courseName;
	long courseType;
	String courseOrientation; 
	List<CourseModule> modules; 
	
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

	Context context;
	public Context getContext() {
		return context;
	}
	public void setContext(Context context) {
		this.context = context;
	}




	
	//store courseID and JSON string to SQLLite database
	public void serialize(Context context){
		
		//output as JSON format

		String jsonContent = null;


		
		StringBuffer stringBuffer = new StringBuffer(1024*1024*10);
		
		stringBuffer.append("Courses: ");
		stringBuffer.append("[{");
		
		stringBuffer.append("courseid:"+"\""+ this.courseid+"\",");
		stringBuffer.append("courseName:"+"\""+ this.courseName+"\",");
		stringBuffer.append("courseType:"+ this.courseType+",");
		
		stringBuffer.append("courseOrientation:"+"\""+ this.courseOrientation+"\",");
		
		stringBuffer.append("CourseModules:");
		stringBuffer.append("[{");
		 
		for(int i=0;i<modules.size();i++){
			
			CourseModule module = modules.get(i); 
			
			stringBuffer.append("module:"+ module.moduleId+",");
			
			stringBuffer.append("guide:"+"\""+module.guide +"\",");
			
			stringBuffer.append("Exams:");
			
			stringBuffer.append("[{");
			
			
			for(int j=0;j<module.exams.size();j++){
				
				Exam exam= module.exams.get(j);
				stringBuffer.append("examid:"+ exam.examid+",");
				stringBuffer.append("name:"+ "\""+ exam.name+"\",");
				stringBuffer.append("passing:"+ exam.passing+",");
				stringBuffer.append("timeLimit:"+ exam.timeLimit+",");
				
				stringBuffer.append("Questions:");
				
				for(int k=0;k<exam.Questions.size();k++){
					
					Question question = exam.Questions.get(k);
					
					stringBuffer.append("questionNumber:"+ question.questionNumber+",");
					stringBuffer.append("category:"+ "\""+ question.category+"\",");
					
					stringBuffer.append("text:"+ "\""+ question.text+"\",");			
					stringBuffer.append("explanation:"+ "\""+ question.explanation+"\",");			
					stringBuffer.append("Answers");
					
					stringBuffer.append("[{");
								
					for(int p=0;p<question.answers.size();p++){
						
						Answer ans= question.answers.get(p);
						stringBuffer.append("answerNumber:"+ ans.answerNumber+",");
						
						stringBuffer.append("score:"+ ans.score+",");
						
						stringBuffer.append("answerText:"+ "\""+ ans.answerText+"\",");
				
					}
					
					stringBuffer.append("}]");
					
					
				}
			
			}
		
			stringBuffer.append("}]");
		}
		
	
		
		stringBuffer.append("}]");
		stringBuffer.append("}]");
		
		
		

		//save to database
		
		storeToDB(courseid, stringBuffer.toString(), context);
		
		
	}
	
	public Course(){
		
	
	}
	
	public Course(int CourseId,Context context){
				
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
	
	public void storeToDB(String CourseID, String jsonContent,Context context){
		
		SQLiteDatabase db = DBTool.getDB(context);
		DBTool.insertCourse(context, db, CourseID, jsonContent);
		
		
		return; 
		
	}

}
