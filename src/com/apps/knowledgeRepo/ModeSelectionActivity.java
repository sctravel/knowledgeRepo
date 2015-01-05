package com.apps.knowledgeRepo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import com.apps.knowledgeRepo.R;
import com.apps.knowledgeRepo.activityHelper.CourseSelectionArrayAdapter;
import com.apps.knowledgeRepo.activityHelper.CoursesDownloaderTask;
import com.apps.knowledgeRepo.activityHelper.FlashCardBucketArrayAdapter;
import com.apps.knowledgeRepo.activityHelper.TextExamArrayAdapter;
import com.apps.knowledgeRepo.activityHelper.TextModuleArrayAdapter;
import com.apps.knowledgeRepo.activityHelper.VideoModuleArrayAdapter;
import com.apps.knowledgeRepo.dataModel.Course;
import com.apps.knowledgeRepo.dataModel.ExamModuleMetaData;
import com.apps.knowledgeRepo.dataModel.FlashCardBucket;
import com.apps.knowledgeRepo.dataModel.FlashCardCourse;
import com.apps.knowledgeRepo.dataModel.ExamMetaData;
import com.apps.knowledgeRepo.dataModel.VideoCourse;
import com.apps.knowledgeRepo.dataModel.VideoModule;
import com.apps.knowledgeRepo.db.DBTool;
import com.apps.knowledgeRepo.om.Constants;
import com.apps.knowledgeRepo.utils.CourseUtil;

@SuppressLint("InflateParams")
public class ModeSelectionActivity extends Activity {
	
	// Map< courseId, Map<moduleId, List<examId> >
	private List<Course> courseList = new ArrayList<Course>();
	
	private String currentCourseId=null;
	private String username = null;
	private String password = null;
	private  NotificationManager nm=null;
	
	private Course currentCourse = null;
	private final static int LOGIN_PAGE = 0;
	private final static int COURSE_PAGE = 1;
	private final static int TEXT_MODULE_PAGE = 2;
	private final static int TEXT_EXAM_PAGE = 3;
	private final static int FLASH_CARD_BUCKET_PAGE = 4;
	private final static int FLASH_CARD_TYPE_PAGE = 5;
	private final static int VIDEO_MODULE_PAGE = 6;
	
	

	// 0 - loginPage
	// 1 - MainPage (Course Page)
	// 2 - ModulePage
	// 3 - examPage
	private int currentPage = COURSE_PAGE;
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.exam_mode_menu, menu);
       
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_search:
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
   		//setContentView(R.layout.mode_selection);
        SharedPreferences pref = getSharedPreferences(Constants.PREF_ACCOUNT_FILE,MODE_PRIVATE);   
    	username = pref.getString(Constants.PREF_USERNAME, null);
    	password = pref.getString(Constants.PREF_PASSWORD, null);
    	//getActionBar().show();
    	selectCoursesPage();	
    	Button signIn = (Button) findViewById(R.id.courseListSignInNotice);
    	signIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	
            	if (username == null || password == null) {
            	    //Prompt for username and password
            		loginPage();
            	} else {
            		//TODO: direct download using the stored username password
            		loginPage();
            	}
            	
            }
        });	
	}

    //
    Map<String, List<ExamMetaData>> groupExamByModule(List<ExamMetaData> examList) {
    	Map<String, List<ExamMetaData>> map = new HashMap<String, List<ExamMetaData>>();
    	for(ExamMetaData emd : examList) {
    		String moduleId = emd.getModuleId();
    		if(map.containsKey(moduleId)) {
    			map.get(moduleId).add(emd);
    		} else {
    			List<ExamMetaData> list= new ArrayList<ExamMetaData>();
    			list.add(emd);
    			map.put(moduleId, list);
    		}
    	}
    	return map;
    }
    
	public void selectCoursesPage(){
		//refresh the course list every time we go to Course Page
		courseList = DBTool.getCourseMetaData(getApplicationContext());

		currentPage = COURSE_PAGE;
		 
		LayoutParams lpbt = new LayoutParams((LayoutParams.MATCH_PARENT), (LayoutParams.WRAP_CONTENT));
		lpbt.gravity= Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL;
		
		
		
		final ListView listView = new ListView(getApplicationContext());
		listView.setBackgroundResource(R.drawable.background); //setBackgroundColor(Color.BLUE);
		listView.setLayoutParams(lpbt);
		ArrayAdapter<Course> adapter = new CourseSelectionArrayAdapter(getApplicationContext(),
	        R.layout.course_list_item, courseList);    
	    // Assign adapter to ListView
		View listHeader = getLayoutInflater().inflate(R.layout.course_list_header, null);
	    listHeader.setClickable(false);
	    listView.addHeaderView(listHeader);	
	    
	    listView.setAdapter(adapter); 
	            
	    // ListView Item Click Listener
	    listView.setOnItemClickListener(new OnItemClickListener() {
	        @Override
	        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		        // ListView Clicked item value
		        Course  courseMeta    = (Course) listView.getItemAtPosition(position);
		        currentCourseId = courseMeta.getCourseId();
		        currentCourse = courseMeta;
		        long courseType = courseMeta.getCourseType();
		        
		        if(courseType==Constants.TEXT_COURSE_TYPE || courseType==Constants.FINAL_EXAM_TYPE) {
		        	List<ExamMetaData> examList = DBTool.getExamListForCourse(getApplicationContext(), courseMeta.getCourseId());
		        	Map<String, List<ExamMetaData>> examByModuleMap = groupExamByModule(examList);
			        if(examByModuleMap.size()>1) {
			        	List<ExamModuleMetaData> moduleList = getModuleListFromMap(examByModuleMap);
				        selectCourseModulePage(courseMeta, moduleList, examByModuleMap);
				    	Log.d("Select Course: ", "Select Module");

				    } else {
				        //If there's only one module, skip the select Module page
				    	Log.d("Select Course: ", "Skip Module selection");
				    	if(examList==null || examList.isEmpty()) {
			   			    Toast.makeText(getApplicationContext(), "Exam list is empty. ", Toast.LENGTH_LONG).show();

				    	} else {
				    		String currentModuleId=examList.get(0).getModuleId();
				    		selectExamsPage(courseMeta, currentModuleId, examList);
				    	}
				    }		    
		        	 
		         } else if(courseType==Constants.FLASH_CARD_COURSE_TYPE) {
		        	 currentCourse = CourseUtil.initilizeFlashCardCourse(courseMeta, getApplicationContext());
		        	 selectFlashCardTypePage( (FlashCardCourse) currentCourse );
		         } else if(courseType==Constants.VIDEO_COURSE_TYPE) { // Video
		        	 currentCourse = CourseUtil.initilizeVideoCourse(courseMeta, getApplicationContext());
		        	 selectVideoModulePage( (VideoCourse) currentCourse );
		         }
	                   
	        }
	    }); 
	            
	    	
	    
        setContentView(listView);
        
        addSignInButton();
        TextView pageText = (TextView) findViewById(R.id.courseListPageName);
        pageText.setText("Course Menu:");
        
	}
	List<ExamModuleMetaData> getModuleListFromMap(Map<String, List<ExamMetaData>> examByModuleMap) {
		List<ExamModuleMetaData> moduleList = new ArrayList<ExamModuleMetaData>();
		for(List<ExamMetaData> list : examByModuleMap.values()) {
			if(!list.isEmpty()) {
				ExamModuleMetaData emm = list.get(0).getExamModuleMetaData();
				moduleList.add(emm);
			}
		}
		return moduleList;
	}
	private void addSignInButton() {
		Button signIn = (Button) findViewById(R.id.courseListSignInNotice);
    	signIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	loginPage();
            }
        });
	}
	
	private Map<String, List<FlashCardBucket>> groupFlashCardBucketsByType(List<FlashCardBucket> bucketList) {
		Map<String, List<FlashCardBucket>> map = new HashMap<String, List<FlashCardBucket>>();
		for(FlashCardBucket fcb : bucketList) {
			String type = fcb.getBucketType();
			if(map.containsKey(type)) {
				map.get(type).add(fcb);
			} else {
				List<FlashCardBucket> list = new ArrayList<FlashCardBucket>();
				list.add(fcb);
				map.put(type, list);
			}
		}
		return map;
	}
	
	private void selectFlashCardTypePage( final FlashCardCourse course ) {
		
		final Map<String, List<FlashCardBucket>> map = groupFlashCardBucketsByType(course.getBucket());
		
		
		currentPage = FLASH_CARD_TYPE_PAGE;
		LayoutParams lpbt = new LayoutParams((LayoutParams.MATCH_PARENT), (LayoutParams.WRAP_CONTENT));
		lpbt.gravity= Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL;
			
		final ListView listView = new ListView(getApplicationContext());
		listView.setBackgroundResource(R.drawable.background); //setBackgroundColor(Color.BLUE);
		listView.setLayoutParams(lpbt);
		View listHeader = getLayoutInflater().inflate(R.layout.course_list_header, null);
	    listHeader.setClickable(false);
	    listView.addHeaderView(listHeader);		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
		        R.layout.course_list_item, R.id.courseListItemTextLine, new ArrayList<String>( map.keySet() ) );    
		// Assign adapter to ListView
		listView.setAdapter(adapter); 
		
		// ListView Item Click Listener
	    listView.setOnItemClickListener(new OnItemClickListener() {
	        @Override
	        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	            // ListView Clicked item index
		        // ListView Clicked item value
                String  bucketType    = (String) listView.getItemAtPosition(position);
		        
                selectFlashCardBucketPage( course, map.get(bucketType) ); 
	                  
	        }
	    }); 
	            
	    
	    
        setContentView(listView);
        addSignInButton();
        TextView pageName = (TextView) findViewById(R.id.courseListPageName);
		pageName.setText(course.getCourseName());
	}
	private void selectFlashCardBucketPage( final FlashCardCourse course, List<FlashCardBucket> bucketList ) {
		
		
		currentPage = FLASH_CARD_BUCKET_PAGE;
		
		LayoutParams lpbt = new LayoutParams((LayoutParams.MATCH_PARENT), (LayoutParams.WRAP_CONTENT));
		lpbt.gravity= Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL;
			
		final ListView listView = new ListView(getApplicationContext());
		listView.setBackgroundResource(R.drawable.background); //setBackgroundColor(Color.BLUE);
		listView.setLayoutParams(lpbt); 
		View listHeader = getLayoutInflater().inflate(R.layout.course_list_header, null);
	    listHeader.setClickable(false);
	    listView.addHeaderView(listHeader);		
	    
		ArrayAdapter<FlashCardBucket> adapter = new FlashCardBucketArrayAdapter(getApplicationContext(),
	        R.layout.course_list_item, bucketList);    
	    // Assign adapter to ListView
	    listView.setAdapter(adapter); 
	            
	    // ListView Item Click Listener
	    listView.setOnItemClickListener(new OnItemClickListener() {
	        @Override
	        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	            // ListView Clicked item index
		        // ListView Clicked item value
                FlashCardBucket  bucket    = (FlashCardBucket) listView.getItemAtPosition(position);
		        
		        beginFlashCardBucket( view, bucket ); 
	                  
	        }
	    }); 
	            
	   
        setContentView(listView);
        
        addSignInButton();
        TextView pageName = (TextView) findViewById(R.id.courseListPageName);
		pageName.setText(course.getCourseName());
	}
	private void selectVideoModulePage(final VideoCourse course) {
		
		
		currentPage = VIDEO_MODULE_PAGE;
		
		LayoutParams lpbt = new LayoutParams((LayoutParams.MATCH_PARENT), (LayoutParams.WRAP_CONTENT));
		lpbt.gravity= Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL;	
		
		final ListView listView = new ListView(getApplicationContext());
		listView.setBackgroundResource(R.drawable.background); //setBackgroundColor(Color.BLUE);
		listView.setLayoutParams(lpbt);
        
	    View listHeader = getLayoutInflater().inflate(R.layout.course_list_header, null);
	    listHeader.setClickable(false);
	    listView.addHeaderView(listHeader);		
	    
		ArrayAdapter<VideoModule> adapter = new VideoModuleArrayAdapter(getApplicationContext(),
	        R.layout.course_list_item, course.getVideoModules());    
	    // Assign adapter to ListView
	    listView.setAdapter(adapter); 
	            
	    // ListView Item Click Listener
	    listView.setOnItemClickListener(new OnItemClickListener() {
	        @Override
	        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	            
		        // ListView Clicked item value
                VideoModule  videoModule    = (VideoModule) listView.getItemAtPosition(position);
		        beginVideoModule( view, videoModule ); 
	        }
	    }); 
	    
        setContentView(listView);
        
        addSignInButton();
        TextView pageName = (TextView) findViewById(R.id.courseListPageName);
		pageName.setText(course.getCourseName());
	}
	
	
	
	private void selectCourseModulePage(final Course courseMeta, final List<ExamModuleMetaData> moduleList, 
			final Map<String, List<ExamMetaData>> examByModuleMap) {
		Log.d("CourseInfo",courseMeta.getCourseId()+" "+courseMeta.getCourseName()+" "+
				courseMeta.getCourseType() );//+"  number of modules:"+course.getModules().size());
		
		currentPage = TEXT_MODULE_PAGE;

		//List<TextCourseModule> moduleList = course.getModules();
		
		LayoutParams lpbt = new LayoutParams((LayoutParams.MATCH_PARENT), (LayoutParams.WRAP_CONTENT));
		lpbt.gravity= Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL; 
		
		 
		final ListView listView = new ListView(getApplicationContext());
	    listView.setBackgroundResource(R.drawable.background); //setBackgroundColor(Color.BLUE);
		listView.setLayoutParams(lpbt);
		View listHeader = getLayoutInflater().inflate(R.layout.course_list_header, null);
		listHeader.setClickable(false);
		listView.addHeaderView(listHeader);	
		
		ArrayAdapter<ExamModuleMetaData> adapter = new TextModuleArrayAdapter(getApplicationContext(),
				R.layout.course_list_item, moduleList);
		// Assign adapter to ListView
		listView.setAdapter(adapter); 
		            
		// ListView Item Click Listener
		listView.setOnItemClickListener(new OnItemClickListener() {
		 
		    @Override
		    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		                   
		    	// ListView Clicked item value
		    	ExamModuleMetaData  moduleMeta    = (ExamModuleMetaData) listView.getItemAtPosition(position);
		    	String moduleId = moduleMeta.getModuleId();
			    selectExamsPage(courseMeta, moduleId, examByModuleMap.get(moduleId));
		                   
		    }
		    
		}); 
		  
	    setContentView(listView);
	        
	    addSignInButton();
	    TextView pageName = (TextView) findViewById(R.id.courseListPageName);
		pageName.setText(courseMeta.getCourseName());
	}
	
	//TODO: Need to have course information in courseModule
	private void selectExamsPage(final Course courseMeta, final String moduleId, List<ExamMetaData> examList) {
		
		currentPage = TEXT_EXAM_PAGE; 
		
		
		Collections.sort(examList, new Comparator<ExamMetaData>(){

			// Overriding the compare method to sort the age 
			public int compare(ExamMetaData d, ExamMetaData d1){
			   return d.getExamName().compareTo(d1.getExamName());
			}
		});
		//setContentView(R.layout.course_selection);
		
		TextView tv = new TextView(getApplicationContext() ) ;//findViewById(R.id.courseSelectionText);
		tv.setText("Exams in CourseModule: "+courseMeta.getCourseName());
		tv.setTextColor(Color.parseColor("black"));
		tv.setTextSize(20);

		LayoutParams lpbt = new LayoutParams((LayoutParams.MATCH_PARENT), (LayoutParams.WRAP_CONTENT));
		lpbt.gravity= Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL; 
		
        final ListView listView = new ListView(getApplicationContext());
		listView.setBackgroundResource(R.drawable.background); //setBackgroundColor(Color.BLUE);
		listView.setLayoutParams(lpbt);
        
	    View listHeader = getLayoutInflater().inflate(R.layout.course_list_header, null);
	    listHeader.setClickable(false);
	    listView.addHeaderView(listHeader);	
	    
		ArrayAdapter<ExamMetaData> adapter = new TextExamArrayAdapter(getApplicationContext(),
	              R.layout.course_list_item, examList);    
	            // Assign adapter to ListView
	            listView.setAdapter(adapter); 
	            
	            // ListView Item Click Listener
	            listView.setOnItemClickListener(new OnItemClickListener() {
	 
	                  @Override
	                  public void onItemClick(AdapterView<?> parent, View view,
	                     int position, long id) {
	                     // ListView Clicked item value
		                   ExamMetaData  emd    = (ExamMetaData) listView.getItemAtPosition(position);
		                     
		                   beginExam(view, courseMeta, moduleId, emd.getExamId());
	                  }
	    
	             }); 
	   
	            
        setContentView(listView);
        addSignInButton();
        TextView pageName = (TextView) findViewById(R.id.courseListPageName);
		pageName.setText(courseMeta.getCourseName());
	}
	
	//Display the login page if there's no username/password stored in sharedPreferences file
	// and store the username/password in sharedPreferences file
	private void loginPage() {
		currentPage = LOGIN_PAGE;
        setContentView(R.layout.login_page);
        final Button buttonLogin = (Button) findViewById(R.id.loginButton);
        final ProgressBar mProgress = (ProgressBar) findViewById(R.id.progressDownloadBar);
        
        Log.d("loginPage", "construct progress bar:  "+ mProgress.getId());
        
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("deprecation")
			public void onClick(View v) {
             	// download restful feeds and serialize to DB 
            	final EditText usernameText = (EditText) findViewById(R.id.loginUserNameInput);
                final EditText passwordText = (EditText) findViewById(R.id.loginPasswordInput);
                
                String username = usernameText.getText().toString();
                String password = passwordText.getText().toString();
                
                 
                //It is not necessary to use Android AccountManager(class) to manage the account here.
                // store the username/password in sharedPreference
                getSharedPreferences(Constants.PREF_ACCOUNT_FILE,MODE_PRIVATE)
                .edit()
                .putString(Constants.PREF_USERNAME, username)
                .putString(Constants.PREF_PASSWORD, password)
                .commit();
                
                //TODO use the username/password here
                nm= (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        	  	Notification notify=new Notification(android.R.drawable. stat_notify_more,"STC downloader",System.currentTimeMillis());
      	        PendingIntent pending=PendingIntent.getActivity(getApplicationContext(), 0, new Intent(),0);
      	        notify.setLatestEventInfo(getApplicationContext(),"Finish Downloading","Downloading Finished",pending);
      	       
      	        CoursesDownloaderTask task = new CoursesDownloaderTask(mProgress, nm,notify, ModeSelectionActivity.this);
      	        task.execute(getApplicationContext());
            	
   			    Toast.makeText(getApplicationContext(), "Downloading Courses... ", Toast.LENGTH_LONG).show();
   			    	
   			     //setContentView(R.layout.mode_selection);
            	//selectCoursesPage();
            }
        });
      
        
	}

    
  
    
    
    
    /** Called when the user clicks the Send button */
    public void beginExam(View view, Course courseMeta, String moduleId, String examId) {
        Intent intent ;
        if(Integer.parseInt(moduleId)%2 == 0 ) {
        	intent = new Intent(this, ExamModeActivity.class);
    	} else {
        	intent = new Intent(this, PracticeModeActivity.class);
    	}
        intent.putExtra("courseId", courseMeta.getCourseId());
        intent.putExtra("moduleId", moduleId);
        intent.putExtra("examId", examId);
        startActivity(intent);
    }
    public void beginPractice(View view, String courseId, String courseModuleId, String examId) {
        Intent intent = new Intent(this, ViewAnswerModeActivity.class);
        //EditText editText = (EditText) findViewById(R.id.edit_message);
        //String message = editText.getText().toString();
        //intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
    public void  beginFlashCardBucket(View view, FlashCardBucket bucket ){
    	Intent intent ;
        intent = new Intent(this, FlashCardActivity.class);
    	/*query current card num 
    	 * 
    	 * 
    	 * */
        intent.putExtra(Constants.FLASH_CARD_BUCKET_NAME, bucket);
        intent.putExtra(Constants.COURSE_ID_NAME, currentCourseId);
        intent.putExtra("currCardNum", 2);

        
        startActivity(intent);
    }
    public void beginVideoModule(View view, VideoModule module) {
    	Intent intent ;
        intent = new Intent(this, VideoPlayerActivity.class);
    	
        intent.putExtra(Constants.VIDEO_MODULE_NAME, module);
        intent.putExtra(Constants.COURSE_ID_NAME, currentCourseId);
        
        startActivity(intent);
    }
    
    @Override
    public void onBackPressed() {
    	switch(currentPage) {
    		case COURSE_PAGE: 
    			super.onBackPressed();
    			break;
    		case FLASH_CARD_BUCKET_PAGE:
    			selectFlashCardTypePage((FlashCardCourse) currentCourse);
    			break;
    		
    		default : 
    			selectCoursesPage();
    			break;
    	}
    }

}
