package com.apps.knowledgeRepo.activityHelper;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.http.util.ByteArrayBuffer;

import android.os.AsyncTask;
import android.util.Log;



import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class ExamDownloaderTask extends AsyncTask<String, Void, Boolean>{
		     	    
        public boolean DownloadFromUrl(String fileURL, String fileName) {  //this is the downloader method
                try {
                        URL url = new URL(fileURL); 
                        File file = new File(fileName);
                        
                        if (file.exists()) {
                        	 Log.w("file already exisits, will try to overwrite it","before download check");
                        }
 
                        long startTime = System.currentTimeMillis();
                        Log.w("exam downloader", "download begining");
                        Log.w("exam downloader", "download url:" + url);
                        Log.w("exam downloader", "downloaded file name:" + fileName);
                        /* Open a connection to that URL. */
                        URLConnection ucon = url.openConnection();
 
                        /*
                         * Define InputStreams to read from the URLConnection.
                         */
                        InputStream is = ucon.getInputStream();
                        BufferedInputStream bis = new BufferedInputStream(is);
 
                        /*
                         * Read bytes to the Buffer until there is nothing more to read(-1).
                         */
                        ByteArrayBuffer baf = new ByteArrayBuffer(50);
                        int current = 0;
                        while ((current = bis.read()) != -1) {
                                baf.append((byte) current);
                        }
 
                        /* Convert the Bytes read to a String. */
                        FileOutputStream fos = new FileOutputStream(file);
                        fos.write(baf.toByteArray());
                        fos.close();
                        Log.w("exam downloader", "download ready in"
                                        + ((System.currentTimeMillis() - startTime) / 1000)
                                        + " sec");
                        
                        
                        //parse JSON
                        
                        JSONParser parser = new JSONParser();
                        
                    	try{
            				Object obj = parser.parse(new FileReader(fileName));
            			
            	
            			    JSONObject jsonObject = (JSONObject) obj;  
            				
            			    JSONArray listOfCourses = (JSONArray) jsonObject.get("Courses");  
            			    Iterator<JSONObject> iterator = listOfCourses.iterator();
            			   
            			    while (iterator.hasNext()) {
            				   JSONObject course= (JSONObject)iterator.next();
            	               String courseid = (String) course.get("courseid");
            	               String couseName = (String) course.get("courseName");
            	               long courseType = (Long) course.get("courseType");
            	               String courseOrientation = (String) course.get("courseOrientation");
            	               
            	               //JSONArray courseModels = (JSONArray)course.get("CourseModules"); 
            	               JSONArray courseModels = (JSONArray)course.get("Modules"); 
            	               Iterator<JSONObject> modelIterator = courseModels.iterator();
            	            	   
            	               while (modelIterator.hasNext()) {
            	            	   
            	            	   JSONObject module= (JSONObject)modelIterator.next();   	   
            	            	   Long moduleId = (Long) module.get("module");    	   
            	            	   String guide = (String) module.get("guide");      	   
            	            	   JSONArray exams = (JSONArray)module.get("Exams");
            	            	            	   
            	            	   Iterator<JSONObject> examIterator = exams.iterator();
            	            	   
            	            	   while (examIterator.hasNext()) {
            	            		   
            	            		   JSONObject exam= (JSONObject)examIterator.next();          		   
            	            		   Long examid= (Long) exam.get("examid");           		   
            	            		   String name= (String) exam.get("name");           		   
            	            		   Long passing= (Long) exam.get("passing");            		   
            	            		   Long timeLimit= (Long) exam.get("timeLimit");
            	            		   	            		   
            	            		   JSONArray questions = (JSONArray)exam.get("Questions");	            		   
            	            		   Iterator<JSONObject> questionIterator = questions.iterator();
            		            	   
            		            	   while (questionIterator.hasNext()) {
            		            		   
            		            		   JSONObject question= (JSONObject)questionIterator.next();		            		   
            		            		   Long questionNumber= (Long) question.get("questionNumber");		            		   
            		            		   String category= (String) question.get("category");	            		   
            		            		   String text= (String) question.get("text");	            		   
            		            		   String explanation= (String) question.get("explanation");	            		   
            		            		   JSONArray answers = (JSONArray)question.get("Answers");
            		            		   
            		            		   Iterator<JSONObject> answersIterator = answers.iterator();
            			            	   
            			            	   while (answersIterator.hasNext()) {
            			            		   
            			            		   JSONObject answer= (JSONObject)answersIterator.next();         		   
            			            		   Long answerNumber= (Long) answer.get("answerNumber");           		   
            			            		   Long score= (Long) answer.get("score");            					            		   
            			            		   String answerText= (String) answer.get("answerText");	            		          		   
            			            	   }     		  		            	  
            		            	   }
            	            	   }         	             	   
            	               }               	               
            	               System.out.print("courseid:" + courseid); 
            				 }		   	   
            			}
            			catch( ParseException ex){
            				
            				System.out.print(ex.toString()); 						   
            			}
                        
                                               
                        return true;
 
                } catch (IOException e) {
                        Log.w("exam downloader", "Error: " + e);
                }
                
                return false;
 
        }

		@Override
		protected Boolean doInBackground(String... urls) {
			// TODO Auto-generated method stub
			return DownloadFromUrl(urls[0], urls[1]);
		}
		
		
	

}
