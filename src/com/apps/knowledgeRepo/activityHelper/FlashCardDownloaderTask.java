package com.apps.knowledgeRepo.activityHelper;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.apps.knowledgeRepo.dataModel.CourseModule;
import com.apps.knowledgeRepo.dataModel.Exam;
import com.apps.knowledgeRepo.db.DBTool;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.ProgressBar;

public class FlashCardDownloaderTask extends CoursesDownloaderTask{
	
	private final String serviceEndPoint= "https://www.stcinteractive.com/servlet/stctrain?get=template&TemplateName=Rest.htm&username=test2014&password=test2014";
	
	private final String localFileName=  "/FlashCardDB.json";

	public FlashCardDownloaderTask(ProgressBar progressbar) {
		super(progressbar);
		
	}
	
	@Override
	public boolean parseJSON(String fileName,Context context){
		
	    JSONParser parser = new JSONParser();
	 		    
		try{
			
			Log.d("JSON parser", "start parsing JSON, file Name: "+ fileName);
			Object obj = parser.parse(new FileReader(fileName));
		
		    JSONObject jsonObject = (JSONObject) obj;  
		    Log.d("preloop", "prelooping");
		    JSONArray listOfCourses = (JSONArray) jsonObject.get("Courses");  
		    @SuppressWarnings("unchecked")
			Iterator<JSONObject> iterator = listOfCourses.iterator();
		   
		    while (iterator.hasNext()) {
		    	Log.d("loop", "looping");
		      // Course courseObj= new Course();
		    	
			   JSONObject course= (JSONObject)iterator.next();
	           String courseId = (String) course.get("courseid");
	           String courseName = (String) course.get("courseName");
	           long courseType = (Long) course.get("courseType");
	           String courseOrientation = (String) course.get("courseOrientation");
           
	           JSONArray courseModules = (JSONArray)course.get("Modules"); 
	           Iterator<JSONObject> modelIterator = courseModules.iterator();
	           
	           List<CourseModule> couseModuleObjs= new ArrayList<CourseModule>();
	           		           	        	   
	           while (modelIterator.hasNext()) {
	        	   
	        	   CourseModule couseModuleObj= new CourseModule();
	        	   
	        	   JSONObject module= (JSONObject)modelIterator.next();   	   
	        	   String moduleId = String.valueOf( module.get("module"));    
	        	   String guide = String.valueOf( module.get("guide")); 
	        	   JSONArray exams = (JSONArray)module.get("Exams");
    	            	   
	        	   Iterator<JSONObject> examIterator = exams.iterator();
        	   
	        	   List<Exam> examObjs = new ArrayList<Exam>(); 
	        	   
	        	   while (examIterator.hasNext()) {
	        		   
	        		   Exam examObj = new Exam(); 
	        		   
	        		   JSONObject exam= (JSONObject)examIterator.next();          		   
	        		   String examid= String.valueOf((Long) exam.get("examid"));           		   
	        		   String examName= (String) exam.get("name"); 
	        		   
	        		   String examContent = exam.toJSONString();
	        		   
	        		   storeToDB(courseId, courseName, ""+courseType, courseOrientation, moduleId,guide, examid,examName, examContent, context);
	        	   }
	           }
		           
	          // storeToDB(courseId, courseName, courseContent, context);	           
			 }	
		}
		catch(Exception ex){			

			Log.e("parser error",ex.toString()+"  "+ex.getStackTrace());
			return false;
		}
		
		Log.d("JSON parser", "finished parsing JSON");
		return true; 
}

	
@Override
public void storeToDB(String courseId, String courseName, String courseType, String courseOrientation, 
		String moduleId, String guide, String examid,String examName, String examContent, Context context){
	
	SQLiteDatabase db = DBTool.getDB(context);
	DBTool.insertExam(context, db, courseId, courseName, courseType, courseOrientation, moduleId, guide, examid,examName, examContent);
	Log.d("InDB", "COuseId---"+courseId+ "Exam Name" + examName +   "; Length---"+examContent.length());
	
	return; 

}

}
