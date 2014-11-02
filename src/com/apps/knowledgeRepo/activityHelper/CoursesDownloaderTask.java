package com.apps.knowledgeRepo.activityHelper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
import com.apps.knowledgeRepo.db.DBTool;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

public class CoursesDownloaderTask extends AsyncTask<Context, Integer, Boolean>{

	private final ProgressBar progressbar;
	
	private final String serviceEndPoint= "https://www.stcinteractive.com/servlet/stctrain?get=template&TemplateName=Rest.htm&username=test2014&password=test2014";
	
	private final String localFileName=  "/CourseDB.json";
	
	public CoursesDownloaderTask(ProgressBar progressbar){
		
		Log.d("DownloadUsingRestfulAPI", "construct progress bar:  "+ progressbar.getId());
		this.progressbar = progressbar; 
	}
	
	@Override
	protected void onProgressUpdate(Integer... progress) {
		
		Log.d("DownloadUsingRestfulAPI", "onProgressUpdate "+ progress[0]);
		progressbar.setProgress(progress[0]);
		
	}
	
	@Override
	protected void onPreExecute() {
		//textView.setText("Hello !!!");
		Log.d("DownloadUsingRestfulAPI", "onPreExecute "+ progressbar.getId());
		progressbar.setVisibility(View.VISIBLE);
		super.onPreExecute();
	}
	
	/*
	@Override
	protected void onPostExecute() {
		progressbar.setVisibility(View.INVISIBLE);
		//textView.setText(result);
	}*/
	
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
	           
		           //course type equals to 3 indicate it's flash card course 
		           if(courseType == 1){
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
			           }
		           else if(courseType == 3){
		        	   
			           storeCourseToDB(courseId,courseName, ""+courseType, courseOrientation,context);
		        	   
		        	   JSONArray buckets = (JSONArray)course.get("Buckets"); 
			           Iterator<JSONObject> bucketIterator = buckets.iterator(); 
			           		           
			           while (bucketIterator.hasNext()) {
			           
			        	   JSONObject bucket= (JSONObject)bucketIterator.next(); 
			        	      			        	   
			        	   String bucketId = String.valueOf( bucket.get("id"));    
			        	   String sequence = String.valueOf( bucket.get("sequence"));
			        	   String type = String.valueOf( bucket.get("type"));
			        	   String title = String.valueOf( bucket.get("title"));
			        	   		        	   
			        	   storeBucketToDB(courseId,bucketId, type,sequence,title,context);
			        	   	        	   
			           }
			           
			           
		        	   JSONArray cards = (JSONArray)course.get("Cards"); 
			           Iterator<JSONObject> cardIterator = cards.iterator(); 
			           		           
			           while (cardIterator.hasNext()) {
			           
			        	   JSONObject card= (JSONObject)cardIterator.next(); 
			        	  		        	   			        	   
			        	   String cardId = String.valueOf( card.get("fcId"));    
			        	   String cardType = String.valueOf( card.get("fcType"));
			        	   String frontText = String.valueOf( card.get("front"));
			        	   String endText = String.valueOf( card.get("back"));
			        	   		        	   
			        	   storeCardToDB(courseId,cardId, cardType,frontText,endText,context);
			        	   	        	   
			           }
			           
			           
		        	   JSONArray cardsBucketMapping = (JSONArray)course.get("BucketCards"); 
			           Iterator<JSONObject> cardsBucketMappingIterator = cardsBucketMapping.iterator(); 
			           		           
			           while (cardsBucketMappingIterator.hasNext()) {
			           
			        	   JSONObject mapping= (JSONObject)cardsBucketMappingIterator.next(); 
			        	  		        	   			        	   
			        	   String cardId = String.valueOf( mapping.get("fcId"));    
			        	   String bucketId = String.valueOf( mapping.get("bucketId"));
			        	   		        	   
			        	   storeMappingToDB(courseId,cardId,bucketId,context);
			        	   	        	   
			           }
			           
			           
			           
		        	  
		   		        	   
		           }
			    }
			    
			           
		          // storeToDB(courseId, courseName, courseContent, context);	           
				 	
			}
			catch(Exception ex){			

				Log.e("parser error",ex.toString()+"  "+ex.getStackTrace());
				return false;
			}
			
			Log.d("JSON parser", "finished parsing JSON");
			return true; 
	}
	
	
	public void storeToDB(String courseId, String courseName, String courseType, String courseOrientation, 
			String moduleId, String guide, String examid,String examName, String examContent, Context context){
		
		SQLiteDatabase db = DBTool.getDB(context);
		DBTool.insertExam(context, db, courseId, courseName, courseType, courseOrientation, moduleId, guide, examid,examName, examContent);
		Log.d("InDB", "COuseId---"+courseId+ "Exam Name" + examName +   "; Length---"+examContent.length());
		
		return; 
	
	}
	
	public void storeCourseToDB(String courseId, String courseName, String courseType, String courseOrientation,Context context){
		 
		 
	 }
	
	public void storeCardToDB(String courseId,String cardId, String cardType,String frontText,String endText,Context context){
		
		
	}
	
	public void storeBucketToDB(String  courseId,String bucketId, String  type, String  sequence,String  title,Context context){
		
	
	}
	
	public void storeMappingToDB(String courseId,String cardId,String bucketId,Context context){
		 
		 
	 }
	
	@Override
	public Boolean doInBackground(Context... context) {
		// TODO Auto-generated method stub
		//return DownloadUsingRestfulAPI(urls[0]);	
		Context con = context[0];
		
		//if(DownloadUsingRestfulAPI(con)) 
			
		String filePath = context[0].getFilesDir().getPath().toString()  + localFileName;
		try {			
			
		  Log.d("DownloadUsingRestfulAPI", "start downloading from restful service: path: "+ filePath);
	      BufferedWriter  out = new BufferedWriter(new FileWriter(filePath));
		
		  HttpClient client = new DefaultHttpClient();
		
		  HttpGet request = new HttpGet(serviceEndPoint);
		
		  HttpResponse response = client.execute(request);
		
		  BufferedReader rd = new BufferedReader (new InputStreamReader(response.getEntity().getContent()));
		
		  String line = "";
		  
		  int row=0;
		  
		  /*
		  URL url = new URL("https://www.stcinteractive.com/servlet/stctrain?get=template&TemplateName=Rest.htm&username=test2014&password=test2014");
		  HttpURLConnection connection =  (HttpURLConnection) url.openConnection();
          connection.connect();
          int fileLength = connection.getContentLength();  
          //long total = 0;
          Log.d("DownloadUsingRestfulAPI", "fileLength: " + fileLength);*/
          
          
		  Log.d("DownloadUsingRestfulAPI", "start writing to the file from buffer");
		  while ((line = rd.readLine()) != null) {	  
			  out.write(line);	  
			  // total+=line.length(); 		  			  
			  //Log.d("DownloadUsingRestfulAPI", "total: " + total);			  
			  //int percentage = (int) ((total*100)/fileLength); 
			  if(row<98000){
				  publishProgress(row/1000);
				  Log.d("DownloadUsingRestfulAPI", "publishProgress" + row/10);  
			  }
			  else
				  publishProgress(98);
			 			 			  
			  row++;			
		  }
		  		  
		  publishProgress(100);
		  
		  Log.d("DownloadUsingRestfulAPI", "finish writing to the file from buffer, total rows: " + row);
		  out.close();
		}
		catch(Exception ex){
			
			Log.e("downloader error",ex.toString()+"  "+ex.getStackTrace());
			
			return false; 
		}
		  
		  Log.d("DownloadUsingRestfulAPI", "finished downloading from restful service");

		
		if(parseJSON(con.getFilesDir().getPath().toString() + "/CourseDB.json",con) ) //parse JSON	
			return true; 
		else			
		    return false;	
	
	}
	
	public static boolean DownloadUsingRestfulAPI(Context context) {
		return true;
		
	}

	
}
