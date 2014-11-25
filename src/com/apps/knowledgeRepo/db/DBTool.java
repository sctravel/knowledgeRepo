package com.apps.knowledgeRepo.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.apps.knowledgeRepo.dataModel.Bucket;
import com.apps.knowledgeRepo.dataModel.Bucket.BucketType;
import com.apps.knowledgeRepo.dataModel.Card;
import com.apps.knowledgeRepo.dataModel.Card.CardType;
import com.apps.knowledgeRepo.dataModel.ExamMetaData;
import com.apps.knowledgeRepo.dataModel.ExamStatus;
import com.apps.knowledgeRepo.dataModel.FlashCardCourse;

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
    			 row = row +"\t"+currentColumn;
    			 
    			 
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
     
     
     public static void insertVideoModule(Context context, SQLiteDatabase db, int SequenceModule_id, String Course_id, String title){
 		if( !db.isOpen()){
    		db=DBTool.getDB(context);
    		
    	}
 			db.execSQL("delete from VIDEO_COURSES_MODULES");	 
	    	String sqlInsertVideoCourseModules = "insert into VIDEO_COURSES_MODULES values (" 
	                                   + "'" + SequenceModule_id +  "'" + "," 
	                                   + "'" + title + "'" +","
	                                   + "'" + Course_id + "'" +
			                            ")"; 
			 db.execSQL(sqlInsertVideoCourseModules);
			 db.close();
     }

     public static void insertVideo(Context context, SQLiteDatabase db, int SequenceModule_id, int sequence, String URL, String courseId){
    	 
    	 if( !db.isOpen()){
     		db=DBTool.getDB(context);
     		
     	}
  			db.execSQL("delete from VIDEO");	 
 	    	String sqlInsertVideoCourseModules = "insert into VIDEO values (" 
 	                                   + "'" + sequence +  "'" + "," 
 	                                   + "'" + URL + "'" +","
 	                                   + "'" + SequenceModule_id + "'" 
 	                                   + "'" + courseId + "'" +
 			                            ")"; 
 			 db.execSQL(sqlInsertVideoCourseModules);
 			 db.close();
 
     
     }
     
     public static void insertVideoCourse(Context context, SQLiteDatabase db, String Course_id, String Course_name, String Course_Orientation){
 		if( !db.isOpen()){
    		db=DBTool.getDB(context);
    		
    	}
 			db.execSQL("delete from VIDEO_COURSES");	 
	    	String sqlInsertVideoCourse = "insert into VIDEO_COURSES values (" 
	                                   + "'" + Course_id +  "'" + "," 
	                                   + "'" + Course_name + "'" +","
	                                   + "'" + Course_Orientation + "'" +
			                            ")"; 
			 db.execSQL(sqlInsertVideoCourse);
			 db.close();
     }
    
     
     public static void insertFlashcardCourse(Context context, SQLiteDatabase db, String Course_id, String Course_name){
    		if( !db.isOpen()){
        		db=DBTool.getDB(context);
        		
        	}
    	    db.execSQL("delete from FLASHCARD_COURSES");	 
    	 String sqlInsertFlashcardCourse = "insert into FLASHCARD_COURSES values (" 
    	                                   + "'" + Course_id +  "'" + "," 
    	                                   + "'" + Course_name + "'" +
    			                            ")"; 
    	 db.execSQL(sqlInsertFlashcardCourse);
    	 db.close();
     }
     
     public static void insertCard(Context context, SQLiteDatabase db, String cardId, String cardType,String frontText,String endText){
 		if( !db.isOpen()){
     		db=DBTool.getDB(context);
     		
     	}
 	   db.execSQL("delete from CARDS");
 	 String sqlInsertCards = "insert into CARDS values (" 
 		                       	      + "'"  + cardId + "'" + "," 
 		                       	      + "'"   + cardType + "'" + ","
 		                       	      + "'"   + frontText + "'" + ","
 		                       	      + "'"  + endText + "'" + 
 		                       	    
 			                            ")"; 
 	 db.execSQL(sqlInsertCards);
 	 db.close();
  }
   
     public static void insertBucket(Context context, SQLiteDatabase db, String  courseId, String bucketId, String  type, String  sequence,String  title){
 		if( !db.isOpen()){
     		db=DBTool.getDB(context);
     		
     	}
 		 db.execSQL("delete from BUCKETS");
 	 String sqlInsertCards = "insert into BUCKETS values (" 
 			                     + "'"   + bucketId + "'" + "," 
 			                     + "'"   + sequence + "'" +","
 			                     + "'"   + type +  "'" + ","
 			                     + "'"   + title + "'" + ","
 			                     + "'"   + courseId + "'" +
 			                            ")"; 
 	 db.execSQL(sqlInsertCards);
 	 db.close();
  }
   
     public static void insertBucketCard(Context context, SQLiteDatabase db, String cardId, String bucketId){
  		if( !db.isOpen()){
      		db=DBTool.getDB(context);
      		
      	}
		 db.execSQL("delete from BUCKETS_CARDS");

  	 String insertBucketCard = "insert into BUCKETS_CARDS values (" 
  			                        + "'" + cardId +  "'" + "," 
  			                        + "'" + bucketId+  "'" + 
  			                            
  			                            ")"; 
  	 db.execSQL(insertBucketCard);
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
    		 Log.d("res", result.get(0));
			 attempt = Integer.parseInt(result.get(0).trim())+1;
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
      
     
     // Course table: COURSE_ID,COURSE_NAME
     // Buckets table: 
     // Cards table: FC_ID, FC_TYPE, FRONT, BACK 
     // BucketCards table: FC_ID, BUCKET_ID
     public static FlashCardCourse queryFlashCardCourse(Context context,SQLiteDatabase db, String cid){
    	 
    	 String queryCourseSQL = "select COURSE_NAME from FLASHCARD_COURSES where COURSE_ID= ?" ;
    	 	 
    	 String queryCourseBucketSQL = "select Buckets.BUCKET_ID, Buckets.SEQUENCE,Buckets.TYPE, Buckets.TITLE from FLASHCARD_COURSES join Buckets on FLASHCARD_COURSES.COURSE_ID = Buckets.COURSE_ID" +
    	 		" where Buckets.COURSE_ID= ?" ;
    	   	 
    	 //String queryCourseBucketAttribitesSQL =  "select Buckets.sequence,Buckets.type, Buckets.title" +
    	 //		" from Course join Buckets on Course.COURSE_ID = Buckets.COURSE_ID" +
     	 //		" where Buckets.BUCKET_ID= ?" ;
    	 
    	 String queryCourseBucketCardSQL =  "select Card.CARD_ID, Card.TYPE, Card.FRONTTEXT,Card.BACKTEXT " +
     	 		" from BUCKETS_CARDS join Cards on BucketCards.Card_ID = Cards.Card_ID" +
      	 		" where Buckets.BUCKET_ID= ?" ;
    	 
    	   	 
    	 if( !db.isOpen()){
     		db=DBTool.getDB(context);
     		
     	}
    	  	 
    	Log.d("DB","cid:"+cid);
    	
    	FlashCardCourse course= new FlashCardCourse();
    	
    	//get bucket ids
    	ArrayList<String> bucketStrs = DBTool.queryDB(context, db, queryCourseBucketSQL, new String[]{cid});
    	
    	List<Bucket> buckets= CreateBucket(bucketStrs);
    	
    	course.setBucket(buckets);
    	
    	for(Bucket bucket : buckets){
    		
    		ArrayList<String> cardIdStrs = DBTool.queryDB(context, db, queryCourseBucketCardSQL, new String[]{Long.toString(bucket.getBucketId())});
    		
    		List<Card> cards= CreateCard(cardIdStrs);
    		
    		bucket.setCardList(cards);
    		   		
    	}
     	//get cards for each associated bucket id;    
    	
    	db.close();
       	
    	return course;
     }
     
	static List<Bucket> CreateBucket(List<String> bucketStrs){
		
		List<Bucket> result = new ArrayList<Bucket>(); 
		// Buckets.BUCKET_ID, Buckets.SEQUENCE,Buckets.TYPE, Buckets.TITLE
		for(String bucketStr: bucketStrs){
			
			String[] resultStr = bucketStr.split("\t");
			int bucketId= Integer.parseInt(resultStr[0]); 
			int sequence= Integer.parseInt(resultStr[1]); 
			BucketType type= BucketType.valueOf(resultStr[2]); 
			String title = resultStr[3]; 
						
			Bucket bucket= new Bucket(); 
			bucket.setBucketId(bucketId);
			bucket.setSequence(sequence);
			bucket.setType(type);
			bucket.setTitle(title);
			
			result.add(bucket);
		}
		
		return result; 	
	}
		
	static List<Card> CreateCard(List<String> cardStrs){
		
		List<Card> result = new ArrayList<Card>(); 
		
		
		for(String cardStr: cardStrs){
			
			String[] resultStr = cardStr.split("\t");		
		    
			//Cards table: FC_ID, FC_TYPE, FRONT, BACK 			
			Card card= new Card(); 
			
			int cardId= Integer.parseInt(resultStr[0]); 	
			CardType type= CardType.valueOf(resultStr[1]); 
			String front = resultStr[2];
			String back = resultStr[3]; 
			
			card.setCardId(cardId);
			card.setFrontText(front);
			card.setBackText(back);
			card.setCardType(type); 
			
			result.add(card);
		}
		
		return result; 
	
	}
	
}
