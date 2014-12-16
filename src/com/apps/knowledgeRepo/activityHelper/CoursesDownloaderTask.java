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
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONTokener;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.apps.knowledgeRepo.dataModel.Answer;
import com.apps.knowledgeRepo.dataModel.TextCourse;
import com.apps.knowledgeRepo.dataModel.TextCourseModule;
import com.apps.knowledgeRepo.dataModel.CoursePackage;
import com.apps.knowledgeRepo.dataModel.Exam;
import com.apps.knowledgeRepo.dataModel.Question;
import com.apps.knowledgeRepo.dataModel.VideoModule;
import com.apps.knowledgeRepo.db.DBTool;
import com.apps.knowledgeRepo.om.TableNames;

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
	
	private final String serviceEndPoint= "https://www.stcinteractive.com/servlet/stctrain?get=template&TemplateName=Rest.htm&username=test2014&password=test2014";
	
	
	private final String serviceEndPointMetaData="https://www.stcinteractive.com/servlet/stctrain?get=template&TemplateName=Rest.htm&username=test2014&password=test2014";//mock
	
	private final String serviceEndPointCourseData="https://www.stcinteractive.com/servlet/stctrain?get=template&TemplateName=Rest.htm&username=test2014&password=test2014&courseid=";//mock
	
	private final String localFileName=  "/CourseDB.json";
	
	
	private final List<String> localFileNames = new ArrayList<String>();
	
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
			
			    //JSONObject jsonObject = (JSONObject) obj; 
				JSONObject course = (JSONObject) obj; 
				
				
			    /*
			    Log.d("preloop", "prelooping");
			    JSONArray listOfCourses = (JSONArray) jsonObject.get("Courses");  
		
				Iterator<JSONObject> iterator = listOfCourses.iterator();
		
			    while (iterator.hasNext()) {*/
				
				
			    	Log.d("loop", "looping");
			      // Course courseObj= new Course();
				    SQLiteDatabase db = DBTool.getDB(context);
				   //JSONObject course= (JSONObject)iterator.next();
		           String courseId = (String) course.get("courseid");
		           String courseName = (String) course.get("courseName");
		           long courseType = (Long) course.get("courseType");
		           String courseOrientation = (String) course.get("courseOrientation");
	           
		           //course type equals to 3 indicate it's flash card course 
		           if(courseType == 1){
		        	   
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
				        		   storeToDB(courseId, courseName, ""+courseType, courseOrientation, moduleId,guide, examid,examName, examContent, context);
				        		   Log.d("loop", "parse course Type 1 (examCourse) end stroe to DB");
				        	   }
			        	   }
			           }
		           else if(courseType == 3){
		        	   
		        	   Log.d("loop", "parse courseType 3 (FlashCardCourse)");
		        	   
		        	   cleanDB(context, db, TableNames.FLASH_CARD_CARDS);
		        	   cleanDB(context, db, TableNames.FLASH_CARD_BUCKETS);
		        	   cleanDB(context, db, TableNames.COURSES_METADATA);
		        	   cleanDB(context, db, TableNames.FLASH_CARD_BUCKETS_CARDS_MAPPING);
		        	   storeFlashcourseToDB(courseId,courseName,courseOrientation, db);
		        	   
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
		        	   storeVideoCourseToDB(courseId,courseName,courseOrientation, context);

		        	   //do we need    "Modules" layer?:[{"sequence": "title":"About The Exam",
		        	   		        	   
		        	   JSONArray modulesLessons = (JSONArray)course.get("Modules");
		        	 
		        	   Iterator<JSONObject> modulesIterator = modulesLessons.iterator(); 	        	   	
		        	   
		        	   while (modulesIterator.hasNext()) {
		        		   
		        		   JSONObject videoModule = (JSONObject)modulesIterator.next(); 
		        		   
		        		   int sequenceModuleId = Integer.parseInt(String.valueOf(videoModule.get("sequence")));  
		        		   
		        		   String title = String.valueOf( videoModule.get("title"));  
		        		   
			        	   JSONArray videoLessons = (JSONArray)videoModule.get("Lessons"); 
				           Iterator<JSONObject> videoIterator = videoLessons.iterator();
				           
				           
				           storeVideoModuleToDB(sequenceModuleId,title, courseId,context);
				           
				           		           
				           while (videoIterator.hasNext()) {
				           
				        	   JSONObject video= (JSONObject)videoIterator.next(); 
				        	  		        	   			        	   
				        	   int sequence = Integer.parseInt(String.valueOf( video.get("sequence")));    
				        	   String URL = String.valueOf( video.get("URL"));
	                            //need local location colum as well
				        	   storeVideoToDB(sequenceModuleId,sequence,URL, courseId,context);
				        	   	        	   
				           }
		        	   }

		        	   
		        	   
			           
		           }

		        	           
			    db.close();	           
		          // storeToDB(courseId, courseName, courseContent, context);	           	 	
			}
			catch(Exception ex){			

				Log.e("parser error",ex.toString()+"  "+ex.getStackTrace());
				return false;
			}
			
			Log.d("JSON parser", "finished parsing JSON");
			return true; 
	}
	
	    
	public void storeVideoModuleToDB(int sequenceModuleId, String title, String courseId,Context context){
		
		SQLiteDatabase db = DBTool.getDB(context);
		
		DBTool.insertVideoModule(context, db, sequenceModuleId, courseId, title);
		
	}
	
	
	public void storeToDB(String courseId, String courseName, String courseType, String courseOrientation, 
			String moduleId, String guide, String examid,String examName, String examContent, Context context){
		
		SQLiteDatabase db = DBTool.getDB(context);
		DBTool.insertExam(context, db, courseId, courseName, courseType, courseOrientation, moduleId, guide, examid,examName, examContent);
		Log.d("storeToDB InDB", "COuseId---"+courseId+ "Exam Name" + examName +   "; Length---"+examContent.length());
		
		return; 
	
	}
	
	
	public void storeVideoToDB(int sequenceModuleId, int sequence,String URL, String courseId, Context context){
		SQLiteDatabase db = DBTool.getDB(context);
		// storeVideoToDB(sequenceModuleId,sequence,URL, courseId,context);
		DBTool.insertVideo(context, db,sequenceModuleId,sequence,URL, courseId );
		Log.d("storeVideoToDB InDB", "COuseId---"+courseId+ "sequenceModuleId" + sequenceModuleId +   "URL: "+URL);
	  
	  }
	
	public void storeVideoCourseToDB(String courseId,String courseName,String courseOrientation, Context context){
		SQLiteDatabase db = DBTool.getDB(context);
		
		DBTool.insertVideoCourse(context, db, courseId, courseName, courseOrientation);
		
		Log.d("insertVideoCourse InDB", "COuseId---"+courseId+ "CourseName:"+courseName +"courseOrientation" + courseOrientation );
		
	}
	
	public void cleanDB(Context context, SQLiteDatabase db,  String tableName){
		
		DBTool.cleanDB(context, db, tableName);
	}
	public void storeFlashcourseToDB(String courseId, String courseName, String courseOrientation, SQLiteDatabase db){
		DBTool.insertFlashcardCourse(db, courseId, courseName, courseOrientation);
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
	


	
	@Override
	public Boolean doInBackground(Context... context) {
		// TODO Auto-generated method stub
		//return DownloadUsingRestfulAPI(urls[0]);	
		Context con = context[0];
		
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
		    
	 
		  
		    while (iterator.hasNext()) {
		     	
		    	   JSONObject course= (JSONObject)iterator.next();
		           String courseId = (String) course.get("courseid");
		           
		           Log.d("DownloadUsingRestfulAPI", "getting meta data courseId name: "+ courseId);
		      
		           localFileNames.add(courseId); 
		    }
				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		try {			
			
			
		 for(String localName: localFileNames){
			
			  String filePath = context[0].getFilesDir().getPath().toString()  + localName;
			  
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
					  if(row%1000==0)
						  {
						  publishProgress(row/1000);
						  Log.d("DownloadUsingRestfulAPI", "publishProgress" + row/10); 
						  }
					  		 
				  }
				  else
					  publishProgress(98);
				 			 			  
				  row++;			
			  }
			  		  
			  publishProgress(100);
			  
			  Log.d("DownloadUsingRestfulAPI", localName+" finish writing to the file from buffer, total rows: " + row);
			  out.close();
			}
		}
		catch(Exception ex){
			
			Log.e("downloader error",ex.toString()+"  "+ex.getStackTrace());
			
			return false; 
		}
		  
		Log.d("DownloadUsingRestfulAPI", "finished downloading from restful service");

		 for(String localName: localFileNames){	
			 String filePath = context[0].getFilesDir().getPath().toString()  + localName;
			 if(parseJSON(filePath,con) ) //parse JSON	
				  return false; 
		 }	
		 
		 return true;
	
	}
	
	public static boolean DownloadUsingRestfulAPI(Context context) {
		return true;
		
	}

	
}
