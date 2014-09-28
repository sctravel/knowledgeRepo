package com.apps.knowledgeRepo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public final class DBHelper extends SQLiteOpenHelper {

	    private static final String SQL_CREATE_CEAQA =
	    	    "CREATE TABLE CEAQA ('COURSE_ID' varchar(256)," +
	    	    "'EXAM_ID' varchar(256), " +
	    	    "'ATTEMPT' varchar(256)," +
	    	    " 'QNUM' varchar(256)," +
	    	    "'ANSWER' varchar(256)," + 
	    	    "'TIME' varchar(256)"  +
	    	    ")";
	    
	    private static final String SQL_CREATE_COURSE =
	    	    "CREATE TABLE COURSE ('COURSE_ID' varchar(256)," + 
	            "'COURSE_NAME' varchar(256)," +  
	   	    	"'COURSE_CONTENT' TEXT)";
	    
	    
	    
	    public static final int DATABASE_VERSION = 1;
	    public static final String DATABASE_NAME = "knowledgeRepo.db";

	    public DBHelper(Context context) {
	        super(context, DATABASE_NAME, null, DATABASE_VERSION);
	    }
	    public void onCreate(SQLiteDatabase db) {
	        
	        db.execSQL(SQL_CREATE_COURSE);
	        db.execSQL(SQL_CREATE_CEAQA);
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
