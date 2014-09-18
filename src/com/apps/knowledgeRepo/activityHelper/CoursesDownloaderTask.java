package com.apps.knowledgeRepo.activityHelper;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.apps.knowledgeRepo.dataModel.Answer;
import com.apps.knowledgeRepo.dataModel.Course;
import com.apps.knowledgeRepo.dataModel.CourseModule;
import com.apps.knowledgeRepo.dataModel.CoursePackage;
import com.apps.knowledgeRepo.dataModel.Exam;
import com.apps.knowledgeRepo.dataModel.Question;

import android.os.AsyncTask;

public class CoursesDownloaderTask extends AsyncTask<String, Void, Boolean>{
	
	protected String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException {
		InputStream in = entity.getContent();

		StringBuffer out = new StringBuffer();
		int n = 1;
		while (n>0) {
		byte[] b = new byte[4096];
		n =  in.read(b);

		if (n>0) out.append(new String(b, 0, n));
		}


		return out.toString();
		}
	
	
	private boolean parseJSON(String fileName){
		
		 JSONParser parser = new JSONParser();
		 
		 List<Course> courseObjs = new ArrayList<Course>(); 
		    
			try{
				Object obj = parser.parse(new FileReader(fileName));
			
			    JSONObject jsonObject = (JSONObject) obj;  
				
			    JSONArray listOfCourses = (JSONArray) jsonObject.get("Courses");  
			    Iterator<JSONObject> iterator = listOfCourses.iterator();
			   
			    while (iterator.hasNext()) {
			    	
			       Course courseObj= new Course();
			    	
				   JSONObject course= (JSONObject)iterator.next();
		           String courseid = (String) course.get("courseid");
		           String courseName = (String) course.get("courseName");
		           long courseType = (Long) course.get("courseType");
		           String courseOrientation = (String) course.get("courseOrientation");
		           
		           courseObj.setCourseid(courseid);
		           courseObj.setCourseName(courseName);
		           courseObj.setCourseType(courseType);
		           courseObj.setCourseOrientation(courseOrientation);
		           
		           //JSONArray courseModels = (JSONArray)course.get("CourseModules"); 
		           JSONArray courseModels = (JSONArray)course.get("Modules"); 
		           Iterator<JSONObject> modelIterator = courseModels.iterator();
		           
		           CourseModule couseModuleObjs= new CourseModule();
		           	        	   
		           while (modelIterator.hasNext()) {
		        	   
		        	   JSONObject module= (JSONObject)modelIterator.next();   	   
		        	   Long moduleId = (Long) module.get("module");    	   
		        	   String guide = (String) module.get("guide");      	   
		        	   JSONArray exams = (JSONArray)module.get("Exams");
		        	   
		        	   
		        	   couseModuleObjs.setModuleId(moduleId);
		        	   couseModuleObjs.setGuide(guide);
		        	   couseModuleObjs.setExams(exams);
		        	            	   
		        	   Iterator<JSONObject> examIterator = exams.iterator();
	        	   
		        	   List<Exam> examObjs = new ArrayList<Exam>(); 
		        	   
		        	   while (examIterator.hasNext()) {
		        		   
		        		   Exam examObj = new Exam(); 
		        		   
		        		   JSONObject exam= (JSONObject)examIterator.next();          		   
		        		   Long examid= (Long) exam.get("examid");           		   
		        		   String name= (String) exam.get("name");           		   
		        		   Long passing= (Long) exam.get("passing");            		   
		        		   Long timeLimit= (Long) exam.get("timeLimit");
		        		   
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
			            		   
			            	   }     		  		            	  
		            	   }
		        	   }         	             	   
		           }               	               
		           System.out.print("courseid:" + courseid); 
				 }		   	   
			}
			catch( ParseException ex){
				
				System.out.print(ex.toString()); 
				
				return false;
			} catch (FileNotFoundException ex) {
				System.out.print(ex.toString()); 
			
			} catch (IOException ex) {
				System.out.print(ex.toString()); 
		
			}
			
			return true; 
	}
		
	private boolean DownloadUsingRestfulAPI(String URL){
		
		HttpClient httpClient = new DefaultHttpClient();
		HttpContext localContext = new BasicHttpContext();
		HttpGet httpGet = new HttpGet(URL);
		String text = null;
		try {
			HttpResponse response = httpClient.execute(httpGet, localContext);
			HttpEntity entity = response.getEntity();
	
			text = getASCIIContentFromEntity(entity);			
			String testInput="abced";			
			CoursePackage page= null;	
		// serialize to SQL Lite database 
		} catch (Exception e) {
			return false;
		}
		
		return true; 
				
	}
	
	
	@Override
	protected Boolean doInBackground(String... urls) {
		// TODO Auto-generated method stub
		//return DownloadUsingRestfulAPI(urls[0]);					
	
		if(DownloadUsingRestfulAPI(urls[1])) 
			if(parseJSON(urls[1]))  //parse JSON	
				return true; 
		return false;	
	
	}
}
