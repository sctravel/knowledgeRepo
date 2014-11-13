package com.apps.knowledgeRepo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
	    	    " 'BUCKET_ID' varchar(256) ," +
	    	    " 'SEQUENCE' int, " +
	    	    " 'TITLE' varchar(256)," +
	    	    " 'COURSE_ID' varchar(256)," + 
	    	    " PRIMARY KEY (BUCKET_ID)  )";
	    	    
	    
	  private static final String SQL_CREATE_CARDS = 
	    		"CREATE TABLE CARDS (" +
	            "'CARD_ID' varchar(256)," +
	    		"'NAME' varchar(256), "+
	    		"'FRONTTEXT' varchar(256), " +
	    		"'BACKTEXT' varchar(256), " + 
	    		"'TYPE' varchar(256)," +
	    		" PRIMARY KEY (CARD_ID)  )";
	    
	      private static final String SQL_CREATE_BUCKETS_CARDS=
	    	    "CREATE TABLE BUCKETS_CARDS ('COURSE_ID' varchar(256)," + 
	            "'BUCKET_ID' varchar(256)," + 
	            " PRIMARY KEY (COURSE_ID,BUCKET_ID)  )";
	    
	    
	    public static final int DATABASE_VERSION = 1;
	    public static final String DATABASE_NAME = "knowledgeRepo.db";

	    public DBHelper(Context context) {
	        super(context, DATABASE_NAME, null, DATABASE_VERSION);
	    }
	    public void onCreate(SQLiteDatabase db) {
	        
	        db.execSQL(SQL_CREATE_EXAM);
	        db.execSQL(SQL_CREATE_CEAQA);
	        db.execSQL(SQL_CREATE_GRADE);
	        db.execSQL(SQL_CREATE_CARDS);
	        db.execSQL(SQL_CREATE_BUCKETS);  
	        db.execSQL(SQL_CREATE_BUCKETS_CARDS);
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
