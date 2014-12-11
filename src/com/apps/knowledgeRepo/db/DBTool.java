package com.apps.knowledgeRepo.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.apps.knowledgeRepo.dataModel.FlashCardBucket;
import com.apps.knowledgeRepo.dataModel.FlashCardCard;
import com.apps.knowledgeRepo.dataModel.FlashCardCard.CardType;
import com.apps.knowledgeRepo.dataModel.Course;
import com.apps.knowledgeRepo.dataModel.ExamMetaData;
import com.apps.knowledgeRepo.dataModel.ExamStatus;
import com.apps.knowledgeRepo.dataModel.FlashCardCourse;
import com.apps.knowledgeRepo.dataModel.TextCourse;
import com.apps.knowledgeRepo.dataModel.VideoCourse;
import com.apps.knowledgeRepo.dataModel.VideoLesson;
import com.apps.knowledgeRepo.dataModel.VideoModule;
import com.apps.knowledgeRepo.om.Constants;
import com.apps.knowledgeRepo.om.TableNames;

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
     
     
     //TODO finish it
     public static TextCourse getTextCourse(Context context, String courseId) {
    	 TextCourse textCourse = new TextCourse();
    	 
    	 return textCourse;
     }
     
     //TODO: Finish it
     public static List<Course> getCourseMetaData(Context context) {
    	 List<Course> result = new ArrayList<Course>();
    	 SQLiteDatabase db = DBTool.getDB(context);
    	 
    	 if( !db.isOpen()){
     		db=DBTool.getDB(context);
     	 } 
    	 
    	 String sql = " select course_id, course_name, course_type, course_orientation  from " + TableNames.COURSES_METADATA  ;
		 Cursor courseCursor = db.rawQuery(sql, null);
    	 
    	 while(courseCursor.moveToNext()){  
    		 String courseId = courseCursor.getString(0);
             String courseName = courseCursor.getString(1);
             Long courseType = courseCursor.getLong(2);
             String courseOrientation = courseCursor.getString(3);
             
             Course course = new Course(courseId, courseName, courseType, courseOrientation);
             result.add(course);
    	 }
    	
    	 return result;
     }
     
     public static List<ExamMetaData> getExamListForCourse (Context context, String courseId){
    	 SQLiteDatabase db = DBTool.getDB(context);
    	 String getExamMataDataSQL = "select course_id, course_name, course_Type, course_orientation, " +
    	 		" module_Id, guide, exam_id, exam_name from " + TableNames.TEXT_EXAM +
    	 		" where course_id = ? "; 
    	 Cursor metaDataCursor = db.rawQuery(getExamMataDataSQL, new String[]{courseId});
    	 
    	 List<ExamMetaData> metaDataList = new ArrayList<ExamMetaData>();
    	 while(metaDataCursor.moveToNext()){
               //String courseId = metaDataCursor.getString(0);
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
     
     public static List<ArrayList<String> >queryDB(Context context,SQLiteDatabase db, String sql, String[] selectionArgs ){
    	if( !db.isOpen()){
        	db=DBTool.getDB(context);	
        }
    	List<ArrayList<String>> result =new ArrayList<ArrayList<String>>();
    		
    	Cursor cursor = db.rawQuery(sql, selectionArgs);
    
    	while(cursor.moveToNext()){
    		int count = cursor.getColumnCount();
    		ArrayList<String> row = new ArrayList<String>();
    		for (int i =0; i < count; i++){
    			String currentColumn = cursor.getString(i);
    			row.add(currentColumn); 
    		}
    		result.add(row);          } 
    	
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

    	 String sqlQuery = "select * from " + TableNames.TEXT_EXAM_ANSWER + " where "  + 
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
     public static void cleanDB(Context context,SQLiteDatabase db, String table){
    	 
    	 if( !db.isOpen()){
          	db=DBTool.getDB(context);	
         }
    	 
    	 String delete = "delete from " + table;
    	 db.execSQL(delete);
     }
     
    public static void recordStatus(Context context,SQLiteDatabase db, String course_id, String module_id,String exam_id, String att, String qnum, String ans, String time){
    	if( !db.isOpen()){
         	db=DBTool.getDB(context);	
        }
    	String sqlInsert = "insert into " + TableNames.TEXT_EXAM_ANSWER + 
    	    " values ( " + "'" +  course_id + "'" + "," +
  		    "'" +  module_id + "'" + "," +
    	    "'" +  exam_id + "'" + "," +
    	    "'" +  att + "'" + "," +
            "'" +  qnum+ "'" + "," +
    	    "'" +  ans + "'" + "," +
    	    "'" +  time +"'" +  ")" ; 
    	
    	 String sqlQuery = "select count(*) from +" + TableNames.TEXT_EXAM_ANSWER + " where "  + 
    	                   "course_id =? and module_id=? and exam_id=? and attempt = ? and qnum=  ?;";  
    	
    	 String sqlUpdate = "update " + TableNames.TEXT_EXAM_ANSWER + " set answer=? , time= ? where course_id=? and module_id=? and exam_id=? and attempt=? and qnum=?; " ;
    	     	 
    	// int a = Integer.valueOf(DBTool.queryDB(context,db, sqlQuery, new String[]{course_id,module_id,exam_id,att,qnum}).get(0));
    	 ArrayList result = DBTool.queryDB(context,db, sqlQuery, new String[]{course_id,module_id,exam_id,att,qnum}).get(0);
    	 int a = result.size();
    	 
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
	    	
 		String sqlInsertVideoCourseModules = 
 			"insert into " + TableNames.VIDEO_COURSES_MODULES +" values ( " +
	        " '" + SequenceModule_id +  "'" + "," +
	        " '" + title + "'" +"," +
	        " '" + Course_id + "'" +
			" )"; 
		db.execSQL(sqlInsertVideoCourseModules);
		db.close();
     }

     public static void insertVideo(Context context, SQLiteDatabase db, int SequenceModule_id, int sequence, String URL, String courseId){ 
    	if( !db.isOpen()){
     		db=DBTool.getDB(context);
     		
     	}
 	    
    	String sqlInsertVideoCourseModules = "insert into " + TableNames.VIDEO_SEQUENCE +" values ( " + 
 	        " '" + sequence +  "'" + ","  +
 	        " '" + URL + "'" +"," +
 	        " '" + SequenceModule_id + "'" +","+
 	        " '" + courseId + "'" +
 			" ) "; 
 	    db.execSQL(sqlInsertVideoCourseModules);
 	    db.close();
     }
     
     public static void insertVideoCourse(Context context, SQLiteDatabase db, String Course_id, String Course_name, String Course_Orientation){
 		if( !db.isOpen()){
    		db=DBTool.getDB(context);
    		
    	}
	    String sqlInsertVideoCourse = 
	    	"insert into " + TableNames.COURSES_METADATA + " values ( " + 
	        " '" + Course_id +  "'" + ", " +
	        " '" + Course_name + "'" +", " +
	        Constants.VIDEO_COURSE_TYPE + ", " + // The type of video course is 4  
	        " '" + Course_Orientation + "' " +
			" )"; 
		db.execSQL(sqlInsertVideoCourse);
		db.close();
     }
    
     
     public static void insertFlashcardCourse(SQLiteDatabase db, String Course_id, String Course_name,  String Course_Orientation){	 
    	 String sqlInsertFlashcardCourse = 
    		 "insert into " + TableNames.COURSES_METADATA + " values (" +
    	     " '" + Course_id +  "'" + "," +
    	     " '" + Course_name + "'" + "," +
    	     Constants.FLASH_CARD_COURSE_TYPE+" , " + // The type of flash card course is 3  
 	         " '" + Course_Orientation + "'" +
    		 " )"; 
    	 db.execSQL(sqlInsertFlashcardCourse);
     }
     
     public static void insertCard(SQLiteDatabase db, String cardId, String cardType,String frontText,String endText){
 		
 	   String sqlInsertCards = "insert into " + TableNames.FLASH_CARD_CARDS + " values (" + 
 		   "'"  + cardId + "'" + "," +
 		   "'"   + cardType + "'" + "," +
 		   "'"   + frontText + "'" + "," +
 		   "'"  + endText + "'" +                     	    
 		   ")"; 
 	   db.execSQL(sqlInsertCards);
     }
   
     public static void insertBucket(SQLiteDatabase db, String  courseId, String bucketId, String  type, String  sequence,String  title){
 		
 		String sqlInsertCards = "insert into " + TableNames.FLASH_CARD_BUCKETS + " values (" 
 			                     + "'"   + bucketId + "'" + "," 
 			                     + "'"   + sequence + "'" +","
 			                     + "'"   + type +  "'" + ","
 			                     + "'"   + title + "'" + ","
 			                     + "'"   + courseId + "'" +
 			                            ")"; 
 		db.execSQL(sqlInsertCards);
     }
   
     public static void insertBucketCard(SQLiteDatabase db, String cardId, String bucketId){
  		
	  	 String insertBucketCard = "insert into " + TableNames.FLASH_CARD_BUCKETS_CARDS_MAPPING + " values (" 
	  			                        + "'" + cardId +  "'" + "," 
	  			                        + "'" + bucketId+  "'" + 
	  			                            
	  			                            ")"; 
	  	 db.execSQL(insertBucketCard);
     }
     
     
     
     
     public static void insertExam(Context context,SQLiteDatabase db, String courseId, String courseName, 
    		 String courseType, String courseOrientation, String moduleId, String guide, String examId,String examName, String examContent){
    	 
    	 //Log.d("insertCourse","insertCourse");
    		if( !db.isOpen()){
        		db=DBTool.getDB(context);
        		//Log.d("Open new DB","Open new DB");
        	}
    		
    	 examContent=examContent.replaceAll("'", "!!pattern!!") ;
    	 String sqlInsert = "insert into " + TableNames.TEXT_EXAM +" values ( " + 
    	     "'" +  courseId + "'" + "," + 
    		 "'" + courseName + "'"+ "," +
    		 "'" + courseType + "'"+ "," +
    		 "'" + courseOrientation + "'"+ "," + 
    		 "'" + moduleId + "'"+ "," + 
    		 "'" + guide + "'"+ "," +
    		 "'" + examId + "'"+ "," +
    		 "'" + examName + "'"+ "," +	 									
    	     "'" +  examContent + "'" + ");"; 
    	
    	 String sqlQuery = "select count(*) from " + TableNames.TEXT_EXAM + " where "  + 
    	                   "course_id =? and module_id=? and exam_id=?;";  
    	
    	 String sqlUpdate = "update " + TableNames.TEXT_EXAM + " set exam_content=? where course_id=? and module_id=? and exam_id=?; " ;
    	 
    	 //DBTool.queryDB(context, db, sqlQuery, new String[]{course_id}).get(0);
    	 //Log.d("after query","after query");
    	
    	 ArrayList result =DBTool.queryDB(context,db, sqlQuery, new String[]{courseId,moduleId,examId}).get(0);
    	 int a = result.size();
    	 
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
        	
    		
    		String sqlRecordGrade = 
    			"insert into " + TableNames.TEXT_EXAM_GRADE +" values (" +
		    	"'"+ cId + "'," +
		    	"'"+ moduleId + "'," +
		    	"'"+ examId + "'," +
		    	"'"+ attempt+ "'," +
		    	"'"+ is_grade + "'," +
		    	"'"+ grade + "'," + 
		    	"'" +grade_time +"'" + 
		    	")";
    		 
    		db.execSQL(sqlRecordGrade);
    		db.close();
    	
     }
     
     public static int retriveNewAttempt(Context context, String cId, String moduleId, String examId) {
    	 int attempt = 1;
    	 
         SQLiteDatabase	db=DBTool.getDB(context);	

    	 String queryMaxAttempt = "select count(1) from " + TableNames.TEXT_EXAM_GRADE +" where COURSE_ID= ? and module_id=? and exam_id=? ";
    	List< ArrayList<String> >result = DBTool.queryDB(context, db, queryMaxAttempt, new String[]{cId,moduleId,examId});
    	 
    	 if( result!=null && !result.isEmpty() ) {
    		 Log.d("res", result.get(0).get(0));
			
    	 }
    	 
    	 return attempt;
     }
     
     public static String retriveGrade (Context context,  String cId,  String moduleId, String examId, String attempt ){
    	
         SQLiteDatabase	db=DBTool.getDB(context);	
      	
    	 String queryGrade = "select grade from " + TableNames.TEXT_EXAM_GRADE +" where COURSE_ID= ? and module_id=? and exam_id=? and attempt=?" ; 
    	
    	 ArrayList<String> grade = DBTool.queryDB(context, db, queryGrade, new String[]{cId,moduleId,examId,attempt}).get(0);
    	 
    	 String exam_grade = grade.get(0);
    	    	 
    	 return exam_grade;
     }
     public static String queryExam(Context context,SQLiteDatabase db, String cid, String moduleId, String examId){
    	 
    	 String queryCourseSQL = "select EXAM_CONTENT from " + TableNames.TEXT_EXAM +" where COURSE_ID= ? and module_id=? and exam_id=? " ;
    	 if( !db.isOpen()){
     		db=DBTool.getDB(context);
     		
     	}
    	Log.d("DB","cid:"+cid+"; moduleId: "+moduleId+"; examId: "+examId);
    	ArrayList<String> examContent = DBTool.queryDB(context, db, queryCourseSQL, new String[]{cid,moduleId,examId}).get(0);
    	
    	db.close();
    	if(examContent==null || examContent.isEmpty() ) {
    		Log.d("query course empty", "query course empty");
    		return null;
    	}
    	String content = examContent.get(0);
    	content=content.replaceAll( Constants.REPLACE_PATTERN, "'") ;

    	
    	return content;
    	
     }
      
     public static Course queryVideoCourse(Context context, SQLiteDatabase db, Course courseMeta) {
    	 String courseId = courseMeta.getCourseId();
    	 VideoCourse course= new VideoCourse(courseMeta.getCourseId(), courseMeta.getCourseName(),
    			 courseMeta.getCourseType(), courseMeta.getCourseOrientation());
    	 
    	 //Get all the modules
    	 String selectAllModules = " select sequence_module_id, title from " + TableNames.VIDEO_COURSES_MODULES + 
    			 " where course_id = ? ";
    	 List<ArrayList<String>> allModules = DBTool.queryDB(context, db, selectAllModules, new String[]{courseId});
    	 for(ArrayList<String> list : allModules) {
    		 VideoModule module = new VideoModule(Integer.parseInt(list.get(0)), list.get(1)); 
    		 course.getVideoModules().add(module);
    	 }
    	 
    	 String selectAllLessons = " select sequence_module_id, sequence, url from " + TableNames.VIDEO_SEQUENCE + 
    			 " where course_id = ?  and sequence_module_id = ?  ";
    	 
    	 //add all the lessons to the corresponding Module
    	 for(VideoModule module : course.getVideoModules()) {
	    	 int moduleSequenceId = module.getModuleSequenceId();
	    	 List<ArrayList<String>> allLessons = DBTool.queryDB(context, db, selectAllLessons, new String[]{courseId, ""+moduleSequenceId});
	    	 for(ArrayList<String> list : allLessons) {
	    		 int sequence = Integer.parseInt(list.get(1));
	    		 String url = list.get(2);
	    		 VideoLesson vl = new VideoLesson(sequence, url);
	    		 module.getLessons().add(vl);
	    	 }
    	 }
    	 
    	 return course;
     }
     
     // Course table: COURSE_ID,COURSE_NAME
     // Buckets table: 
     // Cards table: FC_ID, FC_TYPE, FRONT, BACK 
     // BucketCards table: FC_ID, BUCKET_ID
     public static Course queryFlashCardCourse(Context context,SQLiteDatabase db, Course courseMeta){
    	 
    	 String queryCourseBucketSQL = "select fcb.BUCKET_ID, fcb.SEQUENCE,fcb.TYPE, fcb.TITLE " +
    	 		"from " + TableNames.COURSES_METADATA + " fcc " +
    	 		" join " + TableNames.FLASH_CARD_BUCKETS + " fcb on fcc.COURSE_ID = fcb.COURSE_ID" +
    	 		" where fcc.COURSE_ID= ? and fcc.course_type = "+Constants.FLASH_CARD_COURSE_TYPE ;
    	   	 
    	 //String queryCourseBucketAttribitesSQL =  "select Buckets.sequence,Buckets.type, Buckets.title" +
    	 //		" from Course join Buckets on Course.COURSE_ID = Buckets.COURSE_ID" +
     	 //		" where Buckets.BUCKET_ID= ?" ;
    	 

    	 String queryCourseBucketCardSQL =  "select Cards.CARD_ID, Cards.TYPE, Cards.FRONTTEXT, Cards.BACKTEXT " +
     	 		" from " + TableNames.FLASH_CARD_BUCKETS_CARDS_MAPPING  + " " + 
    			"join " + TableNames.FLASH_CARD_CARDS +" Cards on FLASH_CARD_BUCKETS_CARDS_MAPPING.Card_ID = Cards.Card_ID" +
      	 		" where FLASH_CARD_BUCKETS_CARDS_MAPPING.BUCKET_ID= ?" ;

    	 
    	   	 
    	 if( !db.isOpen()){
     		db=DBTool.getDB(context);
     	 }
    	 String courseId = courseMeta.getCourseId();
    	  	 
    	Log.d("DB","cid:"+courseMeta);
    	
    	FlashCardCourse course= new FlashCardCourse(courseMeta.getCourseId(), courseMeta.getCourseName(), 
    			courseMeta.getCourseType(), courseMeta.getCourseOrientation());
    	
    	//get bucket ids
    	List<ArrayList<String>> bucketStrs = DBTool.queryDB(context, db, queryCourseBucketSQL, new String[]{courseId});
    	
    	List<FlashCardBucket> buckets= CreateBucket(bucketStrs);
    	
    	course.setBucket(buckets);
    	
    	for(FlashCardBucket bucket : buckets){
    		
    		List<ArrayList<String>> cardIdStrs = DBTool.queryDB(context, db, queryCourseBucketCardSQL, new String[]{Long.toString(bucket.getBucketId())});
    		
    		List<FlashCardCard> cards= CreateCard(cardIdStrs);
    		
    		bucket.setCardList(cards);
    		   		
    	}
     	//get cards for each associated bucket id;    
    	
    	db.close();
       	
    	return course;
     }
     
	static List<FlashCardBucket> CreateBucket(List<ArrayList<String>> bucketStrs){
		
		List<FlashCardBucket> result = new ArrayList<FlashCardBucket>(); 
		// Buckets.BUCKET_ID, Buckets.SEQUENCE,Buckets.TYPE, Buckets.TITLE
		for(List<String> bucketStr: bucketStrs){
			
			
			int bucketId= Integer.parseInt(bucketStr.get(0)); 
			int sequence= Integer.parseInt(bucketStr.get(1)); 
			Log.d("buckettype", bucketStr.get(2));
			String type= bucketStr.get(2); 
			String title = bucketStr.get(3); 
						
			FlashCardBucket bucket= new FlashCardBucket(); 
			bucket.setBucketId(bucketId);
			bucket.setSequence(sequence);
			bucket.setBucketType(type);
			bucket.setTitle(title);
			
			result.add(bucket);
		}
		
		return result; 	
	}
		
	static List<FlashCardCard> CreateCard(List<ArrayList<String>> cardIdStrs){
		
		List<FlashCardCard> result = new ArrayList<FlashCardCard>(); 
		
		
		for(List<String> cardStr: cardIdStrs){
			
					
		    
			//Cards table: FC_ID, FC_TYPE, FRONT, BACK 			
			FlashCardCard card= new FlashCardCard(); 
			
			int cardId= Integer.parseInt(cardStr.get(0)); 	
			CardType cardType = null;
		
			if(Integer.valueOf(cardStr.get(1))==0 ){
				
				cardType= CardType.Math;
			}else if (Integer.valueOf(cardStr.get(1))==1){
				cardType= CardType.Normal;
			}
			
			
		
			String front = cardStr.get(2);
			String back = cardStr.get(3); 
			
			card.setCardId(cardId);
			card.setFrontText(front);
			card.setBackText(back);
			card.setCardType(cardType); 
			
			result.add(card);
		}
		
		return result; 
	
	}
	
}
