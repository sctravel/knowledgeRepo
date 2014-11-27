package com.apps.knowledgeRepo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public final class DBHelper extends SQLiteOpenHelper {

	    
	
	private static final String SQL_CREATE_CEAQA =
	    	    "CREATE TABLE CEAQA (" + 
	    	    " 'COURSE_ID' varchar(256) ," +
	    	    " 'MODULE_ID' varchar(256), " +
	    	    " 'EXAM_ID' varchar(256), " +
	    	    " 'ATTEMPT' varchar(256), " +
	    	    " 'QNUM' varchar(256), " +
	    	    " 'ANSWER' varchar(256), " + 
	    	    " 'TIME' varchar(256), " + 
	    	    " PRIMARY KEY (COURSE_ID,MODULE_ID,EXAM_ID,ATTEMPT,QNUM) "  +
	    	    ")";
	    
	    private static final String SQL_CREATE_GRADE = 
	    		"CREATE TABLE GRADE ('COURSE_ID' varchar(256)," +
	    		"'MODULE_ID' varchar(256), "+
	    		"'EXAM_ID' varchar(256), " +
	    		"'ATTEMPT' varchar(256), " +
	    		"'IS_GRADE' boolean, " +
	    		"'GRADE' varchar(256), " +
	    		"'GRADE_TIME' TEXT, " +
	    		" PRIMARY KEY (COURSE_ID,MODULE_ID,EXAM_ID,ATTEMPT) " +
	    		")";
	    
	    private static final String SQL_CREATE_EXAM =
	    	    "CREATE TABLE EXAM ('COURSE_ID' varchar(256)," + 
	            "'COURSE_NAME' varchar(256)," + 
	            "'COURSE_TYPE' varchar(256)," +
	            "'COURSE_ORIENTATION' varchar(256)," +
	            "'MODULE_ID' varchar(256)," +
	            "'GUIDE' varchar(256)," +
	            "'EXAM_ID' varchar(256)," +
	            "'EXAM_NAME' varchar(256)," +
	            "'EXAM_CONTENT' TEXT, " +
	            " PRIMARY KEY (COURSE_ID,MODULE_ID,EXAM_ID)  )";
	    
	    private static final String SQL_CREATE_BUCKETS =
	    	    "CREATE TABLE BUCKETS (" + 
	    	    " 'BUCKET_ID' int ," +
	    	    " 'SEQUENCE' int, " +
	    	    " 'TYPE' varchar(256)," +
	    	    " 'TITLE' varchar(256)," +
	    	    " 'COURSE_ID' varchar(256)," + 
	    	    " PRIMARY KEY (BUCKET_ID)  )";
	    	    
	    
	  private static final String SQL_CREATE_CARDS = 
	    		"CREATE TABLE CARDS (" +
	            "'CARD_ID' int," +
	    		"'FRONTTEXT' varchar(256), " +
	    		"'BACKTEXT' varchar(256), " + 
	    		"'TYPE' varchar(256)," +
	    		" PRIMARY KEY (CARD_ID)  )";
	    
	      private static final String SQL_CREATE_BUCKETS_CARDS=
	    	    "CREATE TABLE BUCKETS_CARDS ('CARD_ID' int," + 
	            "'BUCKET_ID' int," + 
	            " PRIMARY KEY (CARD_ID,BUCKET_ID)  )";
		   
	     
	      
	      private static final String SQL_CREATE_FLASHCARD_COURSES =
		    	    "CREATE TABLE FLASHCARD_COURSES (" + 
		    	    " 'COURSE_ID' varchar(256)," + 
		    	    "'COURSE_NAME' varchar(256), " +
		    	    " PRIMARY KEY (COURSE_ID)  )";
	      
	      
	      //need to create video course related database 
	      //courseId, courseName, courseOrientation
	      private static final String SQL_CREATE_VIDEO_COURSES =
		    	    "CREATE TABLE VIDEO_COURSES (" + 
		    	    "'COURSE_ID' varchar(256)," + 
		    	    "'COURSE_NAME' varchar(256), " +
		    	    "'COURSE_ORIENTATION' varchar(256), " +
		    	    " PRIMARY KEY (COURSE_ID)  )";
	      
	      //int sequenceModuleId, String title, String courseId,Context context
	      private static final String SQL_CREATE_VIDEO_COURSES_MODULE =
		    	    "CREATE TABLE VIDEO_COURSES_MODULES (" + 
		    	    " 'SEQUENCE_MODULE_ID' varchar(256)," + 
		    	    " 'TITLE' varchar(256), " +
		    	    "'COURSE_ID' varchar(256), " +
		    	    " PRIMARY KEY (COURSE_ID,SEQUENCE_MODULE_ID ) )";
	      
	      //int sequenceModuleId, int sequence,String URL, String courseId, Context context
	      private static final String SQL_CREATE_VIDEO =
		    	    "CREATE TABLE VIDEO (" + 
		    	    " 'SEQUENCE' varchar(256), " +
		    	    " 'URL' varchar(256), " +
		    	    "'SEQUENCE_MODULE_ID' varchar(256), " +
		    	    "'COURSE_ID' varchar(256), " +
		    	    " PRIMARY KEY (COURSE_ID, SEQUENCE_MODULE_ID,SEQUENCE))";
	      
	      
		    	    
	    
	    public static final int DATABASE_VERSION = 1;
	    public static final String DATABASE_NAME = "knowledgeRepo.db";

	    public DBHelper(Context context) {
	        super(context, DATABASE_NAME, null, DATABASE_VERSION);
	    }
	    public void onCreate(SQLiteDatabase db) {
	       
	    	Log.d("Insert DB", "start creating DB table ");
	        db.execSQL(SQL_CREATE_EXAM);
	        db.execSQL(SQL_CREATE_CEAQA);
	        db.execSQL(SQL_CREATE_GRADE);
	        db.execSQL(SQL_CREATE_CARDS);
	        db.execSQL(SQL_CREATE_BUCKETS);  
	        db.execSQL(SQL_CREATE_BUCKETS_CARDS);
	        db.execSQL(SQL_CREATE_FLASHCARD_COURSES);
	        db.execSQL(SQL_CREATE_VIDEO_COURSES);
	        db.execSQL(SQL_CREATE_VIDEO_COURSES_MODULE);
	        db.execSQL(SQL_CREATE_VIDEO);
	        
	        Log.d("Insert DB", "finished creating DB table ");
	    }
	    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	        // This database is only a cache for online data, so its upgrade policy is
	        // to simply to discard the data and start over
	        onCreate(db);
	    }
	   
	    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	        onUpgrade(db, oldVersion, newVersion);
	    }
            
	
}
