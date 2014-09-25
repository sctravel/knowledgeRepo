package com.apps.knowledgeRepo.db;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class DBTool {
     public static  SQLiteDatabase getDB(Context context){
    	 
    	 DBHelper dbHelper = new DBHelper(context);
    	    
    	 SQLiteDatabase db = dbHelper.getWritableDatabase();
    	 
    	 return db;
    	 
    	 
    	 
     } 
    
     
     public static ArrayList<String> queryDB(Context context,SQLiteDatabase db, String sql, String[] selectionArgs ){
    		if( !db.isOpen()){
        		db=DBTool.getDB(context);
        		
        	}
    	 ArrayList<String> result =new ArrayList<String>();
    		
    	 Cursor cursor = db.rawQuery(sql, selectionArgs);
    
    	 while(cursor.moveToNext()){
    	       result.add(cursor.getString(cursor.getPosition()));              } 
    	
    	 db.close();
    
		return result;
     }   
     
     
     public static void recordStatus(Context context,SQLiteDatabase db, String cid, String eid, String att, String qnum, String ans){
    		if( !db.isOpen()){
        		db=DBTool.getDB(context);
        		
        	}
    	 String sqlInsert = "insert into ceaqa values ( " + "'" +  cid + "'" + "," 
    	                                            + "'" +  eid + "'" + ","
    			                                    + "'" +  att + "'" + ","
    	                                            + "'" +  qnum+ "'" + ","
    			                                    + "'" +  ans + "'" + ");"; 
    	
    	 String sqlQuery = "select count(*) from ceaqa where "  + 
    	                   "cid =? and eid=? and attempt = ? and qnum=  ?;";  
    	
    	 String sqlUpdate = "update ceaqa set answer=? where cid=? and eid=? and attempt=? and qnum=?; " ;
    	 
    	 DBTool.queryDB(context, db, sqlQuery, new String[]{cid,eid,att,qnum}).get(0);
    	 
    	 int a = Integer.valueOf(DBTool.queryDB(context,db, sqlQuery, new String[]{cid,eid,att,qnum}).get(0));
    	 
    	 if ( a  > 0){
    
    		db.execSQL(sqlUpdate, new String[]{ans,cid,eid,att,qnum});
    	 } else {
    		 
    
    		 db.execSQL(sqlInsert);
    		 
    	 };
    	 
    	 db.close();
     }
     
     public static void insertCourse(Context context,SQLiteDatabase db, String cid, String courseContent){
    	 
    		if( !db.isOpen()){
        		db=DBTool.getDB(context);
        		
        	}
    	 String sqlInsert = "insert into course values ( " + "'" +  cid + "'" + "," 
    	                                            + "'" +  courseContent + "'" + ");"; 
    	
    	 String sqlQuery = "select count(*) from Course where "  + 
    	                   "cid =?;";  
    	
    	 String sqlUpdate = "update Course set courseContent=? where cid=?; " ;
    	 
    	 DBTool.queryDB(context, db, sqlQuery, new String[]{cid}).get(0);
    	 
    	 int a = Integer.valueOf(DBTool.queryDB(context,db, sqlQuery, new String[]{cid}).get(0));
    	 
    	 if ( a  > 0){
    
    		db.execSQL(sqlUpdate, new String[]{cid,courseContent});
    	 } else {
    		 
    
    		 db.execSQL(sqlInsert);
    		 
    	 };
    	 
    	 db.close();
     }
     public static String queryCourse(Context context,SQLiteDatabase db, String cid){
    	 
    	 String queryCourseSQL = "select COURSE_CONTENT from Course where COURSE_ID= ?" ;
    	 if( !db.isOpen()){
     		db=DBTool.getDB(context);
     		
     	}
    	ArrayList<String> courseContent = DBTool.queryDB(context, db, queryCourseSQL, new String[]{cid});
    	return courseContent.get(0);
     }
     
}
