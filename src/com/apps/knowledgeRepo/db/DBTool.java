package com.apps.knowledgeRepo.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.apps.knowledgeRepo.dataModel.ExamMetaData;
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
    
     public static List<ExamMetaData> getExamMeataDataList (Context context){
    	 SQLiteDatabase db = DBTool.getDB(context);
    	 
    	 if( !db.isOpen()){
     		db=DBTool.getDB(context);
     		
     	}
    	 String getExamMataDataSQL = "select course_id, course_name, course_Type, course_orientation, "
    	 		+ " module_Id, guide, exam_id, exam_name from exam" ; 
    	 Cursor metaDataCursor = db.rawQuery(getExamMataDataSQL, null);
    	 
    	 List<ExamMetaData> metaDataList = new ArrayList<ExamMetaData>();
    	 while(metaDataCursor.moveToNext()){
               String courseId = metaDataCursor.getString(0);
               String courseName = metaDataCursor.getString(1);
               Long courseType = metaDataCursor.getLong(2);
               String courseOrientation = metaDataCursor.getString(3);
               String moduleId = metaDataCursor.getString(4);
               String guide = metaDataCursor.getString(5);
               String examId = metaDataCursor.getString(6);
               String examName = metaDataCursor.getString(7);
               ExamMetaData metaData = new ExamMetaData(courseId,courseName,courseType,courseOrientation,
            		   moduleId,guide,examId,examName  );
               metaDataList.add(metaData);
    	}
    	db.close();
    	
    	Log.d("DB", "number of exams in db: "+metaDataList.size());
    	return metaDataList;
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
    			 String currentColumn = cursor.getString(i);
    			 row = row +currentColumn;
    			 
    			 
    		 }
    		 result.add(row);           } 
    	
    	 //db.close();
    
		return result;
     }   
     
     public static ExamStatus retriveStatus (Context context,SQLiteDatabase db, String courseId, String moduleId, String examId, int attempt ) {
    	 if( !db.isOpen()){
     		db=DBTool.getDB(context);
     		
     	}
    	 
    	ExamStatus examStatus = new ExamStatus();
    	examStatus.setCourseId(courseId);
    	examStatus.setExamId(examId);
    	examStatus.setModuleId(moduleId);
    	examStatus.setAttempt(attempt);
    	examStatus.setUsedTime(0); //set it to 0 initially

    	 String sqlQuery = "select * from ceaqa where "  + 
    	                   "course_id =? and module_id=? and exam_id=? and attempt = ?;";
        
    	Cursor cursor= db.rawQuery(sqlQuery, new String[]{courseId,moduleId,examId,""+attempt});
    	//int courseIdIndex = cursor.getColumnIndex("COURSE_ID");
    	//int examIdIndex = cursor.getColumnIndex("EXAM_ID");
    	//int moduleIndex = cursor.getColumnIndex("MODULE_ID");
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
    	db.close();
    	Log.d("DB operation", "getting status from DB! Number of answers-"+examStatus.getUserAnswerMap().size()+" and usedTime:"+usedTimeMax);
    	return examStatus;
     }
     
     
     public static void recordStatus(Context context,SQLiteDatabase db, String course_id, String module_id,String exam_id, String att, String qnum, String ans, String time){
    		if( !db.isOpen()){
        		db=DBTool.getDB(context);
        		
        	}
    	 String sqlInsert = "insert into ceaqa values ( " + "'" +  course_id + "'" + "," 
    			 									+ "'" +  module_id + "'" + ","
    			                                    + "'" +  exam_id + "'" + ","
    			                                    + "'" +  att + "'" + ","
    	                                            + "'" +  qnum+ "'" + ","
    			                                    + "'" +  ans + "'" + "," 
    	                                            + "'" +  time +"'" +  ")" ; 
    	
    	 String sqlQuery = "select count(*) from ceaqa where "  + 
    	                   "course_id =? and module_id=? and exam_id=? and attempt = ? and qnum=  ?;";  
    	
    	 String sqlUpdate = "update ceaqa set answer=? , time= ? where course_id=? and module_id=? and exam_id=? and attempt=? and qnum=?; " ;
    	     	 
    	 int a = Integer.valueOf(DBTool.queryDB(context,db, sqlQuery, new String[]{course_id,module_id,exam_id,att,qnum}).get(0));
    	 
    	 if ( a  > 0){
         	Log.d("DB operation","Doing Update question status!");

    		db.execSQL(sqlUpdate, new String[]{ans,time,course_id,module_id,exam_id,att,qnum});
    	 } else {
    		 
         	Log.d("DB operation","Doing Insert question status!");
    		 db.execSQL(sqlInsert);
    		 
    	 };
    	 
    	 db.close();
     }
     
     
   
     
     public static void insertExam(Context context,SQLiteDatabase db, String courseId, String courseName, 
    		 String courseType, String courseOrientation, String moduleId, String guide, String examId,String examName, String examContent){
    	 
    	 //Log.d("insertCourse","insertCourse");
    		if( !db.isOpen()){
        		db=DBTool.getDB(context);
        		//Log.d("Open new DB","Open new DB");
        	}
    		
    	 examContent=examContent.replaceAll("'", "!!pattern!!") ;
    	 String sqlInsert = "insert into EXAM values ( " + "'" +  courseId + "'" + "," 
    			 									+ "'" + courseName + "'"+ "," 
    			 									+ "'" + courseType + "'"+ ","
    			 									+ "'" + courseOrientation + "'"+ ","
    			 									+ "'" + moduleId + "'"+ "," 
    			 									+ "'" + guide + "'"+ ","
    			 									+ "'" + examId + "'"+ ","
    			 									+ "'" + examName + "'"+ ","	 									
    	                                            + "'" +  examContent + "'" + ");"; 
    	
    	 String sqlQuery = "select count(*) from EXAM where "  + 
    	                   "course_id =? and module_id=? and exam_id=?;";  
    	
    	 String sqlUpdate = "update Exam set exam_content=? where course_id=? and module_id=? and exam_id=?; " ;
    	 
    	 //DBTool.queryDB(context, db, sqlQuery, new String[]{course_id}).get(0);
    	 //Log.d("after query","after query");

    	 int a = Integer.valueOf(DBTool.queryDB(context,db, sqlQuery, new String[]{courseId,moduleId,examId}).get(0));
    	 
    	 if ( a > 0){
        	Log.d("DB operation","Doing Update!");

    		db.execSQL(sqlUpdate, new String[]{examContent,courseId,moduleId,examId});
    	 } else {
    		 
         	Log.d("DB operation","Doing Insert!");

    		 db.execSQL(sqlInsert);
    		 
    	 };
    	 
    	 db.close();
     }
     
     
     // todo BoChen fill in the details
    
     public static void recordGrade(Context context,String cId, String examId, String moduleId, String attempt, boolean is_grade, String grade, String grade_time ){
    	
    	    SQLiteDatabase	db=DBTool.getDB(context);
        	
    		
    		String sqlRecordGrade = "insert into GRADE values (" 
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
     
     public static int retriveNewAttempt(Context context, String cId, String moduleId, String examId) {
    	 int attempt = 1;
    	 
         SQLiteDatabase	db=DBTool.getDB(context);	

    	 String queryMaxAttempt = "select count(1) from GRADE where COURSE_ID= ? and module_id=? and exam_id=? ";
    	 ArrayList<String> result = DBTool.queryDB(context, db, queryMaxAttempt, new String[]{cId,moduleId,examId});
    	 
    	 if( result!=null && !result.isEmpty() ) {
			 attempt = Integer.parseInt(result.get(0))+1;
    	 }
    	 
    	 return attempt;
     }
     
     public static String retriveGrade (Context context,  String cId,  String moduleId, String examId, String attempt ){
    	
         SQLiteDatabase	db=DBTool.getDB(context);	
      	
    	 String queryGrade = "select grade from GRADE where COURSE_ID= ? and module_id=? and exam_id=? and attempt=?" ; 
    	
    	 ArrayList<String> grade = DBTool.queryDB(context, db, queryGrade, new String[]{cId,moduleId,examId,attempt});
    	 
    	 String exam_grade = grade.get(0);
    	    	 
    	 return exam_grade;
     }
     public static String queryExam(Context context,SQLiteDatabase db, String cid, String moduleId, String examId){
    	 
    	 String queryCourseSQL = "select EXAM_CONTENT from EXAM where COURSE_ID= ? and module_id=? and exam_id=? " ;
    	 if( !db.isOpen()){
     		db=DBTool.getDB(context);
     		
     	}
    	Log.d("DB","cid:"+cid+"; moduleId: "+moduleId+"; examId: "+examId);
    	ArrayList<String> examContent = DBTool.queryDB(context, db, queryCourseSQL, new String[]{cid,moduleId,examId});
    	
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
