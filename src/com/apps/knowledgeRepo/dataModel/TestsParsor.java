package com.apps.knowledgeRepo.dataModel;
import java.io.FileReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TestsParsor {
	
	static CoursePackage parse(String text) throws JSONException{
		//JSONArray jsonArray = JSONArray.p;
	
		// FileReader reader = new FileReader("C:\test.json");

		 JSONObject obj = new JSONObject(text);
         
         String pageName = obj.getJSONObject("pageInfo").getString("pageName");

         JSONArray arr = obj.getJSONArray("posts");
         
        
         return null;
			
	}
	
	
	public static void main(String[] args){
		
		String test = "{\"pageInfo\": {\"pageName\": \"abc\",\"pagePic\": \"abv\"}}";
		
		
		}
		
	}


