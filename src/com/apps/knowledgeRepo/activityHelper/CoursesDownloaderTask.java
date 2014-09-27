package com.apps.knowledgeRepo.activityHelper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

public class CoursesDownloaderTask extends AsyncTask<Context, Void, Boolean>{
	/*
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
	*/
	
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
		           String courseContent = course.toJSONString();
		          // JSONObject test = (JSONObject) parser.parse(courseContent);
		          // Log.d("Test Parse","Parse the courseContent succeed. Length--"+courseContent.length()+"Storing into DB--"+test.get("courseName"));
		           storeToDB(courseId, courseName, courseContent, context);
		           
		           
				 }	
			}
			catch(Exception ex){			

				Log.e("parser error",ex.toString()+"  "+ex.getStackTrace());
				return false;
			}
			
			Log.d("JSON parser", "finished parsing JSON");
			return true; 
	}

	public void storeToDB(String courseId, String courseName, String jsonContent,Context context){
	
		SQLiteDatabase db = DBTool.getDB(context);
		DBTool.insertCourse(context, db, courseId, courseName, jsonContent);
		Log.d("InDB", "COuseId---"+courseId+"; Length---"+jsonContent.length());
	
		return; 
	
	}

/*		
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
	*/
	
	@Override
	public Boolean doInBackground(Context... context) {
		// TODO Auto-generated method stub
		//return DownloadUsingRestfulAPI(urls[0]);	
		Context con = context[0];
		if(DownloadUsingRestfulAPI(con)) 
			if(parseJSON(con.getFilesDir().getPath().toString() + "/CourseDB.json",con) ) //parse JSON	
				return true; 
		return false;	
	
	}
	
	public static boolean DownloadUsingRestfulAPI(Context context) {
		String filePath = context.getFilesDir().getPath().toString()  + "/CourseDB.json";
		try {
			
		
			//File f = new File(filePath);
			//need to serialize to DB 
			
		  Log.d("DownloadUsingRestfulAPI", "start downloading from restful service: path: "+ filePath);
	      BufferedWriter  out = new BufferedWriter(new FileWriter(filePath));
		
		  HttpClient client = new DefaultHttpClient();
		
		  HttpGet request = new HttpGet("https://www.stcinteractive.com/servlet/stctrain?get=template&TemplateName=Rest.htm&username=test2014&password=test2014");
		
		  HttpResponse response = client.execute(request);
		
		  BufferedReader rd = new BufferedReader (new InputStreamReader(response.getEntity().getContent()));
		
		  String line = "";
		  
		  int row=0;
	
		  Log.d("DownloadUsingRestfulAPI", "start writing to the file from buffer");
		  while ((line = rd.readLine()) != null) {	  
			  out.write(line);
			  
			  row++;
			
		  }
		  Log.d("DownloadUsingRestfulAPI", "finish writing to the file from buffer, total rows: " + row);
		  out.close();
		}
		catch(Exception ex){
			
			Log.e("downloader error",ex.toString()+"  "+ex.getStackTrace());
			
			return false; 
		}
		  
		  Log.d("DownloadUsingRestfulAPI", "finished downloading from restful service");
		  return true; 
	}


	
}
