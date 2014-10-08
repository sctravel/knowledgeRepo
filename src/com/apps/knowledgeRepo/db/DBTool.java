package com.apps.knowledgeRepo.db;

import java.util.ArrayList;
import java.util.HashMap;

import com.apps.knowledgeRepo.dataModel.ExamStatus;

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
    
     public static HashMap<String, String> getIDsandNames (Context context){
    	 SQLiteDatabase db = DBTool.getDB(context);
    	 
    	 if( !db.isOpen()){
     		db=DBTool.getDB(context);
     		
     	}
    	 String getIDsandNames = "select course_id, course_name from course" ; 
    	 Cursor idsAndNames = db.rawQuery(getIDsandNames, null);
    	 
    	HashMap<String, String> id_names = new HashMap<String, String>();
    	while(idsAndNames.moveToNext()){
               String course_id = idsAndNames.getString(0);
               String course_name = idsAndNames.getString(1);
    		   id_names.put(course_id, course_name);
    	}
    	db.close();
    	 return id_names;
     }
     
     public static ArrayList<String> queryDB(Context context,SQLiteDatabase db, String sql, String[] selectionArgs ){
    		if( !db.isOpen()){
        		db=DBTool.getDB(context);
        		
        	}
    	 ArrayList<String> result =new ArrayList<String>();
    		
    	 Cursor cursor = db.rawQuery(sql, selectionArgs);
    
    	 while(cursor.moveToNext()){
    		 int count = cursor.getColumnCount();
    		 String row = "";
    		 for (int i =0; i < count; i++){
    			 int j = cursor.getColumnIndex("COURSE_CONTENT");
    			 Log.d("name","i-"+i+"; j-"+j);
    			 String currentColumn = cursor.getString(i);
    			 row = row +currentColumn;
    			 
    			 
    		 }
    		 result.add(row);           } 
    	
    	 //db.close();
    
		return result;
     }   
     
     public static ExamStatus retriveStatus (Context context,SQLiteDatabase db, String courseId, String examId, int attempt ) {
    	 if( !db.isOpen()){
     		db=DBTool.getDB(context);
     		
     	}
    	 
    	ExamStatus examStatus = new ExamStatus();
    	examStatus.setCourseId(courseId);
    	examStatus.setExamId(examId);
    	examStatus.setAttempt(attempt);
    	examStatus.setUsedTime(0); //set it to 0 initially

    	 String sqlQuery = "select * from ceaqa where "  + 
    	                   "course_id =? and exam_id=? and attempt = ?;";
        
    	Cursor cursor= db.rawQuery(sqlQuery, new String[]{courseId,examId,""+attempt});
    	//int courseIdIndex = cursor.getColumnIndex("COURSE_ID");
    	//int examIdIndex = cursor.getColumnIndex("EXAM_ID");
    	//int attIndex = cursor.getColumnIndex("ATTEMPT");
    	int qnumIndex = cursor.getColumnIndex("QNUM");
    	int answerIndex = cursor.getColumnIndex("ANSWER");
    	int usedTimeIndex = cursor.getColumnIndex("TIME");
    	
    	long usedTimeMax = examStatus.getUsedTime();
    	while(cursor.moveToNext()){
    		 
    		 Integer questionNumber = cursor.getInt(qnumIndex);
    		 String answer = cursor.getString(answerIndex);
    		 Long usedTime = cursor.getLong(usedTimeIndex);
    		 if(usedTime!=null && usedTime > usedTimeMax) {
    			 usedTimeMax = usedTime;
    		 }
    		 
    		 examStatus.getUserAnswerMap().put(questionNumber, answer);
    	}
    	examStatus.setUsedTime(usedTimeMax);
    	
    	//Log.d("DB operation", "getting status from DB! Number of answers-"+examStatus.getUserAnswerMap().size()+" and usedTime:"+usedTimeMax);
    	return examStatus;
     }
     
     
     public static void recordStatus(Context context,SQLiteDatabase db, String course_id, String exam_id, String att, String qnum, String ans, String time){
    		if( !db.isOpen()){
        		db=DBTool.getDB(context);
        		
        	}
    	 String sqlInsert = "insert into ceaqa values ( " + "'" +  course_id + "'" + "," 
    	                                            + "'" +  exam_id + "'" + ","
    			                                    + "'" +  att + "'" + ","
    	                                            + "'" +  qnum+ "'" + ","
    			                                    + "'" +  ans + "'" + "," 
    	                                            + "'" +  time +"'" +  ")" ; 
    	
    	 String sqlQuery = "select count(*) from ceaqa where "  + 
    	                   "course_id =? and exam_id=? and attempt = ? and qnum=  ?;";  
    	
    	 String sqlUpdate = "update ceaqa set answer=? , time= ? where course_id=? and exam_id=? and attempt=? and qnum=?; " ;
    	 
    	 DBTool.queryDB(context, db, sqlQuery, new String[]{course_id,exam_id,att,qnum}).get(0);
    	 
    	 int a = Integer.valueOf(DBTool.queryDB(context,db, sqlQuery, new String[]{course_id,exam_id,att,qnum}).get(0));
    	 
    	 if ( a  > 0){
    
    		db.execSQL(sqlUpdate, new String[]{ans,course_id,exam_id,att,qnum});
    	 } else {
    		 
    
    		 db.execSQL(sqlInsert);
    		 
    	 };
    	 
    	 db.close();
     }
     
     
   
     
     public static void insertCourse(Context context,SQLiteDatabase db, String courseId, String courseName, String moduleId, String examId,String examName, String courseContent){
    	 
    	 //Log.d("insertCourse","insertCourse");
    		if( !db.isOpen()){
        		db=DBTool.getDB(context);
        		Log.d("Open new DB","Open new DB");
        	}
    		
    	 courseContent=courseContent.replaceAll("'", "!!pattern!!") ;
    	 String sqlInsert = "insert into course values ( " + "'" +  courseId + "'" + "," 
    			 									+ "'" + courseName + "'"+ "," 
    			 									+ "'" + moduleId + "'"+ "," 
    			 									+ "'" + examId + "'"+ ","
    			 									+ "'" + examName + "'"+ ","
    			 									
    	                                            + "'" +  courseContent + "'" + ");"; 
    	
    	 String sqlQuery = "select count(*) from Course where "  + 
    	                   "course_id =? and exam_id=?;";  
    	
    	 String sqlUpdate = "update Course set course_content=? where course_id=? and exam_id=?; " ;
    	 
    	 //DBTool.queryDB(context, db, sqlQuery, new String[]{course_id}).get(0);
    	 //Log.d("after query","after query");

    	 int a = Integer.valueOf(DBTool.queryDB(context,db, sqlQuery, new String[]{courseId,examId}).get(0));
    	 
    	 if ( a > 0){
        	Log.d("DB operation","Doing Update!");

    		db.execSQL(sqlUpdate, new String[]{courseContent,courseId,examId});
    	 } else {
    		 
         	Log.d("DB operation","Doing Insert!");

    		 db.execSQL(sqlInsert);
    		 
    	 };
    	 
    	 db.close();
     }
     
     
     // todo BoChen fill in the details
    
     public static void recordGrade(Context context,SQLiteDatabase db, String cId, String examId, String moduleId, String attempt, boolean is_grade, String grade, String grade_time ){
    	
    		if( !db.isOpen()){
        		db=DBTool.getDB(context);
        		Log.d("Open new DB","Open new DB");
        	}
    		
    		String sqlRecordGrade = "insert into EXAM_GRADE values (" 
		    		                  +"'"+ cId + "',"
		    		                  +"'"+ moduleId + "',"
		    		                  +"'"+ examId + "',"
		    		                  +"'"+ attempt+ "',"
		    		                  +"'"+ is_grade + "',"
		    		                  +"'"+ grade + "',"
		    		                  +"'" +grade_time +"'"
		    				          + ")";
    		 
    		db.execSQL(sqlRecordGrade);
    		 db.close();
    	
     }
     
     public static String retriveGrade (Context context,SQLiteDatabase db, String cId,  String moduleId, String examId, String attempt ){
    	
    	 if( !db.isOpen()){
      		db=DBTool.getDB(context);
      		
      	}
    	 String queryGrade = "select grade from GRADE where COURSE_ID= ? and module_id=? and exam_id=? and attempt=?" ; 
    	
    	 ArrayList<String> grade = DBTool.queryDB(context, db, queryGrade, new String[]{cId,moduleId,examId,attempt});
    	 
    	 String  exam_grade = grade.get(0);
    	
    	 exam_grade = exam_grade.replaceAll( "!!pattern!!", "'") ;
    	 
    	 return exam_grade;
     }
     public static String queryExam(Context context,SQLiteDatabase db, String cid, String examId){
    	 
    	 String queryCourseSQL = "select COURSE_CONTENT from Course where COURSE_ID= ? and exam_id=? " ;
    	 if( !db.isOpen()){
     		db=DBTool.getDB(context);
     		
     	}
    	ArrayList<String> examContent = DBTool.queryDB(context, db, queryCourseSQL, new String[]{cid,examId});
    	
    	db.close();
    	if(examContent==null || examContent.isEmpty() ) {
    		Log.d("query course empty", "query course empty");
    		return null;
    	}
    	String content = examContent.get(0);
    	content=content.replaceAll( "!!pattern!!", "'") ;

    	return content;
     }
     
}
