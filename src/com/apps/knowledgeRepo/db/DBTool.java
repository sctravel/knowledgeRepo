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
    
     public static void getIDsandNames (Context context,SQLiteDatabase db){
    	 if( !db.isOpen()){
     		db=DBTool.getDB(context);
     		
     	}
    	 String getIDsandNames = "select course_id, course_name from course" ; 
    	 Cursor idsAndNames = db.rawQuery(getIDsandNames, null);
    	 idsAndNames.moveToNext();
    	 int a = idsAndNames.getPosition();
    	 Log.d("idandnames",idsAndNames.getString(idsAndNames.getPosition()));
    	 
    	 
     }
     
     public static ArrayList<String> queryDB(Context context,SQLiteDatabase db, String sql, String[] selectionArgs ){
    		if( !db.isOpen()){
        		db=DBTool.getDB(context);
        		
        	}
    	 ArrayList<String> result =new ArrayList<String>();
    		
    	 Cursor cursor = db.rawQuery(sql, selectionArgs);
    
    	 while(cursor.moveToNext()){
    	       result.add(cursor.getString(cursor.getPosition()));              } 
    	
    	 //db.close();
    
		return result;
     }   
     
     
     public static void recordStatus(Context context,SQLiteDatabase db, String course_id, String exam_id, String att, String qnum, String ans){
    		if( !db.isOpen()){
        		db=DBTool.getDB(context);
        		
        	}
    	 String sqlInsert = "insert into ceaqa values ( " + "'" +  course_id + "'" + "," 
    	                                            + "'" +  exam_id + "'" + ","
    			                                    + "'" +  att + "'" + ","
    	                                            + "'" +  qnum+ "'" + ","
    			                                    + "'" +  ans + "'" + ");"; 
    	
    	 String sqlQuery = "select count(*) from ceaqa where "  + 
    	                   "cid =? and eid=? and attempt = ? and qnum=  ?;";  
    	
    	 String sqlUpdate = "update ceaqa set answer=? where cid=? and eid=? and attempt=? and qnum=?; " ;
    	 
    	 DBTool.queryDB(context, db, sqlQuery, new String[]{course_id,exam_id,att,qnum}).get(0);
    	 
    	 int a = Integer.valueOf(DBTool.queryDB(context,db, sqlQuery, new String[]{course_id,exam_id,att,qnum}).get(0));
    	 
    	 if ( a  > 0){
    
    		db.execSQL(sqlUpdate, new String[]{ans,course_id,exam_id,att,qnum});
    	 } else {
    		 
    
    		 db.execSQL(sqlInsert);
    		 
    	 };
    	 
    	 db.close();
     }
     
     public static void insertCourse(Context context,SQLiteDatabase db, String course_id, String course_name, String course_content){
    	 
    	 Log.d("insertCourse","insertCourse");
    		if( !db.isOpen()){
        		db=DBTool.getDB(context);
        		Log.d("Open new DB","Open new DB");
        	}
    		
    	 course_content=course_content.replaceAll("'", "!!pattern!!") ;
    	 String sqlInsert = "insert into course values ( " + "'" +  course_id + "'" + "," 
    			 									+ "'" + course_name + "'"+ "," 
    	                                            + "'" +  course_content + "'" + ");"; 
    	
    	 String sqlQuery = "select count(*) from Course where "  + 
    	                   "course_id =?;";  
    	
    	 String sqlUpdate = "update Course set course_content=? where course_id=?; " ;
    	 
    	 //DBTool.queryDB(context, db, sqlQuery, new String[]{course_id}).get(0);
    	 //Log.d("after query","after query");

    	 int a = Integer.valueOf(DBTool.queryDB(context,db, sqlQuery, new String[]{course_id}).get(0));
    	 
    	 if ( a  > 0){
        	Log.d("DB operation","Doing Update!");

    		db.execSQL(sqlUpdate, new String[]{course_content,course_id});
    	 } else {
    		 
         	Log.d("DB operation","Doing Insert!");

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
    	
    	db.close();
    	if(courseContent==null || courseContent.isEmpty() ) {
    		Log.d("query course empty", "query course empty");
    		return null;
    	}
    	String content = courseContent.get(0);
    	content=content.replaceAll( "!!pattern!!", "'") ;

    	return content;
     }
     
}
