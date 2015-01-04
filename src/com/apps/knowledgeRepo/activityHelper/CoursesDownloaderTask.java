package com.apps.knowledgeRepo.activityHelper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.apps.knowledagerepo.R;
import com.apps.knowledgeRepo.ModeSelectionActivity;
import com.apps.knowledgeRepo.dataModel.TextCourseModule;
import com.apps.knowledgeRepo.dataModel.Exam;
import com.apps.knowledgeRepo.db.DBTool;
import com.apps.knowledgeRepo.om.TableNames;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

public class CoursesDownloaderTask extends AsyncTask<Context, Integer, Boolean>{

	private final ProgressBar progressbar;
	private final NotificationManager nm;
	private final Notification notify;
	private final ModeSelectionActivity activity; 
	
	private final String serviceEndPointMetaData="https://www.stcinteractive.com/servlet/stctrain?get=template&TemplateName=Rest.htm&username=test2014&password=test2014";//mock
	
	private final String serviceEndPointCourseData="https://www.stcinteractive.com/servlet/stctrain?get=template&TemplateName=Rest.htm&username=test2014&password=test2014&courseid=";//mock
	
	
	
	private final List<String> localFileNames = new ArrayList<String>();
	
	public CoursesDownloaderTask(ProgressBar progressbar, NotificationManager nm,Notification notify,ModeSelectionActivity activity){
		
		Log.d("DownloadUsingRestfulAPI", "construct progress bar:  "+ progressbar.getId());
		this.progressbar = progressbar;
		this.nm=nm;
		this.notify=notify;
		this.activity =activity; 
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
	
	@Override
	protected void onPostExecute(Boolean result) {
		progressbar.setVisibility(View.INVISIBLE);
		
		nm.notify(0, notify);
		
		this.activity.setContentView(R.layout.course_selection);
		this.activity.selectCoursesPage();
		// setContentView();
		
		
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	public boolean parseJSON(String fileName,Context context){
		
		JSONParser parser = new JSONParser();
		SQLiteDatabase db = null;		    
			try{
				
				Log.d("JSON parser", "start parsing JSON, file Name: "+ fileName);
				Object obj = parser.parse(new FileReader(fileName));
			
			    //JSONObject jsonObject = (JSONObject) obj; 
				JSONObject course = (JSONObject) obj; 
				
							
			    Log.d("loop", "looping");
				db = DBTool.getDB(context);
		        String courseId = (String) course.get("courseid");
		        String courseName = (String) course.get("courseName");
		        long courseType = (Long) course.get("courseType");
		        String courseOrientation = (String) course.get("courseOrientation");
	           
		        //course type equals to 3 indicate it's flash card course 
		        if(courseType == 1 || courseType == 2){
		        	   
		        	   Log.d("loop", "parse course Type 1 (examCourse)");
		        	   
			           JSONArray courseModules = (JSONArray)course.get("Modules"); 
			           Iterator<JSONObject> modelIterator = courseModules.iterator();
			           
			           List<TextCourseModule> couseModuleObjs= new ArrayList<TextCourseModule>();
			           		           	        	   
			           while (modelIterator.hasNext()) {
			        	   
			        	   TextCourseModule couseModuleObj= new TextCourseModule();
			        	   
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
				        		   
				        		   Log.d("loop", "parse course Type 1 (examCourse) begin stroe to DB");
				        		   storeExamToDB(courseId, courseName, ""+courseType, courseOrientation, moduleId,guide, examid,examName, examContent, db);
				        		   Log.d("loop", "parse course Type 1 (examCourse) end stroe to DB");
				        	   }
			        	   }
			           }
		           else if(courseType == 3){
		        	   
		        	   Log.d("loop", "parse courseType 3 (FlashCardCourse)");
		        	   		        	   
		        	   //storeFlashcourseToDB(courseId,courseName,courseOrientation, db);
		        	   
		        	   JSONArray buckets = (JSONArray)course.get("Buckets"); 
			           Iterator<JSONObject> bucketIterator = buckets.iterator(); 
			           		           
			           while (bucketIterator.hasNext()) {
			           
			        	   JSONObject bucket= (JSONObject)bucketIterator.next(); 
			        	      			        	   
			        	   String bucketId = String.valueOf( bucket.get("id"));    
			        	   String sequence = String.valueOf( bucket.get("sequence"));
			        	   String type = String.valueOf( bucket.get("type"));
			        	   String title = String.valueOf( bucket.get("title"));
			        	   //Log.d("loop", "storeBucketToDB");
	        	        
			        	   storeBucketToDB(courseId,bucketId, type,sequence,title,db);
			        	   	        	   
			           }
			           
			           
		        	   JSONArray cards = (JSONArray)course.get("Cards"); 
			           Iterator<JSONObject> cardIterator = cards.iterator(); 
			           		           
			           while (cardIterator.hasNext()) {
			           
			        	   JSONObject card= (JSONObject)cardIterator.next(); 
			        	  		        	   			        	   
			        	   String cardId = String.valueOf( card.get("fcId"));    
			        	   String cardType = String.valueOf( card.get("fcType"));
			        	   String frontText = String.valueOf( card.get("front")).replaceAll("'", "''");
			        	   String endText = String.valueOf( card.get("back")).replaceAll("'", "''");
			        	   //Log.d("loop", "storeCardToDB");

			        	   storeCardToDB(cardId, cardType,frontText,endText,db);
			        	   	        	   
			           }
			           
			           
		        	   JSONArray cardsBucketMapping = (JSONArray)course.get("BucketCards"); 
			           Iterator<JSONObject> cardsBucketMappingIterator = cardsBucketMapping.iterator(); 
			           		           
			           while (cardsBucketMappingIterator.hasNext()) {
			           
			        	   JSONObject mapping= (JSONObject)cardsBucketMappingIterator.next(); 
			        	  		        	   			        	   
			        	   String cardId = String.valueOf( mapping.get("fcId"));    
			        	   String bucketId = String.valueOf( mapping.get("bucketId"));
			        	   //Log.d("loop", "storeMappingToDB");
	        	   
			        	   storeMappingToDB(cardId,bucketId,db);
			        	   	        	   
			           }
        	   
		           }



		         else if(courseType == 4){

		        	   Log.d("loop", "parse courseType 4");
		        	   //storeVideoCourseToDB(courseId,courseName,courseOrientation, context);

		        	   //do we need    "Modules" layer?:[{"sequence": "title":"About The Exam",
		        	   		        	   
		        	   JSONArray modulesLessons = (JSONArray)course.get("Modules");
		        	 
		        	   Iterator<JSONObject> modulesIterator = modulesLessons.iterator(); 	        	   	
		        	   
		        	   while (modulesIterator.hasNext()) {
		        		   
		        		   JSONObject videoModule = (JSONObject)modulesIterator.next(); 
		        		   
		        		   int sequenceModuleId = Integer.parseInt(String.valueOf(videoModule.get("sequence")));  
		        		   
		        		   String title = String.valueOf( videoModule.get("title"));  
		        		   
			        	   JSONArray videoLessons = (JSONArray)videoModule.get("Lessons"); 
				           Iterator<JSONObject> videoIterator = videoLessons.iterator();
				           
				           
				           storeVideoModuleToDB(sequenceModuleId, title, courseId, db);
				           
				           		           
				           while (videoIterator.hasNext()) {
				           
				        	   JSONObject video= (JSONObject)videoIterator.next(); 
				        	  		        	   			        	   
				        	   int sequence = Integer.parseInt(String.valueOf( video.get("sequence")));    
				        	   String URL = String.valueOf( video.get("URL"));
	                            //need local location colum as well
				        	   storeVideoToDB(sequenceModuleId, sequence, URL, courseId, db);
				        	   	        	   
				           }
		        	   }			           
		           }           
			    	           
			}
			catch(Exception ex){			
				Log.e("parser error",ex.toString()+"  "+ex.getStackTrace());
				return false;
			} finally {
				if(db!=null && db.isOpen()) {
					db.close();
				}
			}
			
			Log.d("JSON parser", "finished parsing JSON");
			return true; 
	}
	
	    
	public void storeVideoModuleToDB(int sequenceModuleId, String title, String courseId,SQLiteDatabase db){
				
		DBTool.insertVideoModule(db, sequenceModuleId, courseId, title);
		
	}
	
	
	public void storeExamToDB(String courseId, String courseName, String courseType, String courseOrientation, 
			String moduleId, String guide, String examid,String examName, String examContent, SQLiteDatabase db){
		
		DBTool.insertExam(db, courseId, courseName, courseType, courseOrientation, moduleId, guide, examid, examName, examContent);
		Log.d("storeToDB InDB", "COuseId---"+courseId+ "Exam Name" + examName +   "; Length---"+examContent.length());
		
		return; 
	
	}
	
	
	public void storeVideoToDB(int sequenceModuleId, int sequence,String URL, String courseId, SQLiteDatabase db){
		DBTool.insertVideo(db, sequenceModuleId, sequence, URL, courseId);
		Log.d("storeVideoToDB InDB", "COuseId---"+courseId+ "sequenceModuleId" + sequenceModuleId +   "URL: "+URL);
	  
	  }
	
	
	public void storeCardToDB(String cardId, String cardType,String frontText,String endText, SQLiteDatabase db){
		DBTool.insertCard(db, cardId, cardType, frontText, endText)   ;		
		
	}
	
	public void storeBucketToDB(String  courseId,String bucketId, String  type, String  sequence,String  title,SQLiteDatabase db){
		DBTool.insertBucket(db, courseId, bucketId, type, sequence, title);
	
	}
	
	public void storeMappingToDB(String cardId,String bucketId,SQLiteDatabase db){
		DBTool.insertBucketCard( db, cardId, bucketId); 
	 }
	


	
	@SuppressWarnings("unchecked")
	@Override
	public Boolean doInBackground(Context... contexts) {
		Context context = contexts[0];
		
		//if(DownloadUsingRestfulAPI(con)) 		
		//first call the get meta file list:
		
		
		HttpClient clientMeta = new DefaultHttpClient();
		
		HttpGet requestMeta = new HttpGet(serviceEndPointMetaData);
		
		
		try {
			HttpResponse responseMeta = clientMeta.execute(requestMeta);
			BufferedReader reader = new BufferedReader(new InputStreamReader(responseMeta.getEntity().getContent(), "UTF-8"));
			String jsonStr = "";
			String str="";
			while((str=reader.readLine())!=null){
				jsonStr+=str;	
			}
			
			Log.d("DownloadUsingRestfulAPI", "getting meta data jsonStr "+ jsonStr);
			
		    JSONParser parser = new JSONParser();
			Object obj = parser.parse(jsonStr);
			JSONObject jsonobj = (JSONObject) obj; 
			
		    JSONArray courses = (JSONArray)(jsonobj.get("Courses"));
		    
		    Iterator<JSONObject> iterator = courses.iterator();
		    
		    SQLiteDatabase db = DBTool.getDB(context);
		  
		    //TODO: Clean all the DB first, may need to remove after we enable individual download
		    cleanAllDBs(db);
		    
		    while (iterator.hasNext()) {
		     	
		    	JSONObject course= (JSONObject)iterator.next();
		        String courseId = (String) course.get("courseid");
		        String courseName = (String) course.get("courseName");
		        Long courseType = (Long) course.get("courseType");
		        String courseOrientation = (String) ("courseOrientation");
		        
		        //Insert Course MetaData to DB
		        Log.d("DownloadUsingRestfulAPI", "getting meta data for courseId : "+ courseId);
		        DBTool.insertCourseMetaData(context, db, courseId, courseName, courseType, courseOrientation);
		        localFileNames.add(courseId); 
		    }
		    if(db != null && db.isOpen()) {
		    	db.close();
		    }
				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		try {
			
		   int totalFileNumber = localFileNames.size();
		   int i=0;
			
			
		   for(String localName: localFileNames){
			
			  String filePath = context.getFilesDir().getPath().toString()  + localName;
			  
			  Log.d("DownloadUsingRestfulAPI", "local file names: "+ filePath);
			  
			  Log.d("DownloadUsingRestfulAPI", "start downloading from restful service: path: "+ filePath);
		      BufferedWriter  out = new BufferedWriter(new FileWriter(filePath));
			
		      HttpClient client = new DefaultHttpClient();
			
			  //HttpGet request = new HttpGet(serviceEndPoint);
		      HttpGet request = new HttpGet(serviceEndPointCourseData+localName);
			
			  HttpResponse response = client.execute(request);
			
			  BufferedReader rd = new BufferedReader (new InputStreamReader(response.getEntity().getContent()));
			
			  String line = "";
			  
			  int row=0;
	          
			  Log.d("DownloadUsingRestfulAPI", "start writing to the file from buffer");
			  while ((line = rd.readLine()) != null) {		  				  
				  out.write(line);			
			  }
			  
			  
			  Log.d("current downloading progress", "current : "+i*100/totalFileNumber);
			  		  
			  publishProgress(i*100/totalFileNumber);
			  
			  Log.d("DownloadUsingRestfulAPI", localName+" finish writing to the file from buffer, total rows: " + row);
			  out.close();
			  
			  i++;
			}
		}
		catch(Exception ex){
			
			Log.e("downloader error",ex.toString()+"  "+ex.getStackTrace());
			
			return false; 
		}
		  
		Log.d("DownloadUsingRestfulAPI", "finished downloading from restful service");

		 for(String localName: localFileNames){	
			 String filePath = context.getFilesDir().getPath().toString()  + localName;
			 if( ! parseJSON(filePath,context) ) //parse JSON	
				  return false; 
		 }	
		 
		 return true;
	
	}
	
	public void cleanAllDBs(SQLiteDatabase db) {
		DBTool.cleanDB( db, TableNames.FLASH_CARD_CARDS );
	    DBTool.cleanDB( db, TableNames.FLASH_CARD_BUCKETS );
	    DBTool.cleanDB( db, TableNames.FLASH_CARD_BUCKETS_CARDS_MAPPING );
	    DBTool.cleanDB( db, TableNames.COURSES_METADATA );
	    DBTool.cleanDB( db, TableNames.TEXT_EXAM );
	    DBTool.cleanDB( db, TableNames.VIDEO_COURSES_MODULES );
	    DBTool.cleanDB( db, TableNames.VIDEO_SEQUENCE );
	    DBTool.cleanDB( db, TableNames.TEXT_EXAM_ANSWER );
	    DBTool.cleanDB( db, TableNames.TEXT_EXAM_GRADE );
	}
	
	public static boolean DownloadUsingRestfulAPI(Context context) {
		return true;
		
	}

	
}
