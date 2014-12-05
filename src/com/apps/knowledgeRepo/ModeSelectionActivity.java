package com.apps.knowledgeRepo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.MenuItemCompat.OnActionExpandListener;
import android.support.v7.widget.SearchView;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import com.apps.knowledagerepo.R;
import com.apps.knowledgeRepo.activityHelper.CourseSelectionArrayAdapter;
import com.apps.knowledgeRepo.activityHelper.CoursesDownloaderTask;
import com.apps.knowledgeRepo.activityHelper.ExamDownloaderTask;
import com.apps.knowledgeRepo.activityHelper.FlashCardBucketArrayAdapter;
import com.apps.knowledgeRepo.activityHelper.VideoModuleArrayAdapter;
import com.apps.knowledgeRepo.dataModel.Course;
import com.apps.knowledgeRepo.dataModel.FlashCardBucket;
import com.apps.knowledgeRepo.dataModel.FlashCardCourse;
import com.apps.knowledgeRepo.dataModel.TextCourse;
import com.apps.knowledgeRepo.dataModel.TextCourseModule;
import com.apps.knowledgeRepo.dataModel.Exam;
import com.apps.knowledgeRepo.dataModel.ExamMetaData;
import com.apps.knowledgeRepo.dataModel.VideoCourse;
import com.apps.knowledgeRepo.dataModel.VideoModule;
import com.apps.knowledgeRepo.db.DBTool;
import com.apps.knowledgeRepo.om.Constants;
import com.apps.knowledgeRepo.utils.CourseUtil;

public class ModeSelectionActivity extends Activity {
	
	// Map< courseId, Map<moduleId, List<examId> >
	private Map<String,TextCourse> courseMetaData = new HashMap<String, TextCourse>();
	private List<Course> courseList = new ArrayList<Course>();
	
	private String currentCourseId=null;
	private String currentTextModuleId=null;
	private String currentTextExamId=null;
	private long currentBucketId = 0;
	private String currentVideoModuleId=null;
	private String currentVideoLessonId=null;
	private int currentModuleSequenceId = 0;
	
	private Course currentCourse = null;
	private final static int LOGIN_PAGE = 0;
	private final static int COURSE_PAGE = 1;
	private final static int TEXT_MODULE_PAGE = 2;
	private final static int TEXT_EXAM_PAGE = 3;
	private final static int FLASH_CARD_BUCKET_PAGE = 4;
	private final static int VIDEO_MODULE_PAGE = 5;
	private final static int VIDEO_LESSON_PAGE = 6;
	
	

	// 0 - loginPage
	// 1 - MainPage (Course Page)
	// 2 - ModulePage
	// 3 - examPage
	private int currentPage = COURSE_PAGE;
	private  static final Map<Long, String> moduleIdNameMap = new HashMap<Long, String>(); 
	private  static final Map<String, Exam> examNameToExamMap = new HashMap<String,Exam>();
	private static final Map<String, Long> currentModuleNameToIdMap = new HashMap<String, Long>();

	static {
		moduleIdNameMap.put( 0L, "Simulation Exams");
		moduleIdNameMap.put(1L, "Open Book Exams");
		moduleIdNameMap.put(2L, "Close Book Exams");
		moduleIdNameMap.put(3L, "By Topic Exams");
	};
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
                openSearch();
                return true;
            case R.id.action_settings:
                openSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

   		setContentView(R.layout.mode_selection);
		
		
    	selectCoursesPage();	
    	Button signIn = (Button) findViewById(R.id.courseListSignInNotice);
    	signIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	loginPage();
            }
        });
    	
        final Button flashcard = (Button) findViewById(R.id.Flashcard);
         flashcard.setOnClickListener(new View.OnClickListener() {
                 
            public void onClick(View v) {    	
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
        
            }
          });  	
	}

	private void selectCoursesPage(){
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
	    listView.setAdapter(adapter); 
	            
	    // ListView Item Click Listener
	    listView.setOnItemClickListener(new OnItemClickListener() {
	        @Override
	        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	            // ListView Clicked item index
                int itemPosition = position;
		        // ListView Clicked item value
		        Course  courseMeta    = (Course) listView.getItemAtPosition(position);
		        currentCourseId = courseMeta.getCourseId();
		        Toast.makeText(getApplicationContext(),"Position :"+itemPosition+"  ListItem : " +courseMeta.getCourseName() , Toast.LENGTH_LONG)
		             .show();
		        
		        long courseType = courseMeta.getCourseType();
		        
		        if(courseType==Constants.TEXT_COURSE_TYPE) {
		        /*   TextCourse textCourse = DBTool;//(TextCourse) course;
			        if(course.getModules().size()>1) {
				            		selectCourseModulePage(course);
				               } else {
				            		//If there's only one module, skip the select Module page
				            		currentModuleId=""+course.getModules().get(0).getModuleId();
				            		selectExamsPage(course,course.getModules().get(0));
				               }*/
		         } else if(courseType==Constants.FINAL_EXAM_TYPE) {
		        	 
		         } else if(courseType==Constants.FLASH_CARD_COURSE_TYPE) {
		        	 currentCourse = CourseUtil.initilizeFlashCardCourse(courseMeta, getApplicationContext());
		        	 selectFlashCardBucketPage( (FlashCardCourse) currentCourse );
		         } else if(courseType==Constants.VIDEO_COURSE_TYPE) { // Video
		        	 currentCourse = CourseUtil.initilizeVideoCourse(courseMeta, getApplicationContext());
		        	 selectVideoModulePage( (VideoCourse) currentCourse );
		         }
	                   
	        }
	    }); 
	            
	    View listHeader = getLayoutInflater().inflate(R.layout.course_list_header, null);
	    listHeader.setClickable(false);
	    listView.addHeaderView(listHeader);		
	    
        setContentView(listView);
        
        addSignInButton();
        TextView pageText = (TextView) findViewById(R.id.courseListPageName);
        pageText.setText("Course Menu:");
        //pageText.setTextSize(20);
        //pageText.setTextColor(0);
        final Button flashcard = (Button) findViewById(R.id.Flashcard);
        flashcard.setOnClickListener(new View.OnClickListener() {
         
           public void onClick(View v) {    	
           	 Intent intent = new Intent(getBaseContext(), MainActivity.class);
          	 startActivity(intent);

           }
        });
	}
	
	private void addSignInButton() {
		Button signIn = (Button) findViewById(R.id.courseListSignInNotice);
    	signIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	loginPage();
            }
        });
	}
	private void selectFlashCardBucketPage( final FlashCardCourse course ) {
		TextView pageName = (TextView) findViewById(R.id.courseListPageName);
		pageName.setText(course.getCourseName());
		
		currentPage = FLASH_CARD_BUCKET_PAGE;
		
		LayoutParams lpbt = new LayoutParams((LayoutParams.MATCH_PARENT), (LayoutParams.WRAP_CONTENT));
		lpbt.gravity= Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL;
			
		final ListView listView = new ListView(getApplicationContext());
		listView.setBackgroundResource(R.drawable.background); //setBackgroundColor(Color.BLUE);
		listView.setLayoutParams(lpbt);
		ArrayAdapter<FlashCardBucket> adapter = new FlashCardBucketArrayAdapter(getApplicationContext(),
	        R.layout.course_list_item, course.getBucket());    
	    // Assign adapter to ListView
	    listView.setAdapter(adapter); 
	            
	    // ListView Item Click Listener
	    listView.setOnItemClickListener(new OnItemClickListener() {
	        @Override
	        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	            // ListView Clicked item index
                int itemPosition = position;
		        // ListView Clicked item value
                FlashCardBucket  bucket    = (FlashCardBucket) listView.getItemAtPosition(position);
		        currentBucketId = bucket.getBucketId();
		        
		        beginFlashCardBucket( view, bucket ); 
	                  
	        }
	    }); 
	            
	    View listHeader = getLayoutInflater().inflate(R.layout.course_list_header, null);
	    listHeader.setClickable(false);
	    listView.addHeaderView(listHeader);		
	    
        setContentView(listView);
        
        addSignInButton();
        TextView pageText = (TextView) findViewById(R.id.courseListPageName);
        pageText.setText("Video Course Module:");
	}
	private void selectVideoModulePage(final VideoCourse course) {
		TextView pageName = (TextView) findViewById(R.id.courseListPageName);
		pageName.setText(course.getCourseName());
		
		currentPage = VIDEO_MODULE_PAGE;
		
		LayoutParams lpbt = new LayoutParams((LayoutParams.MATCH_PARENT), (LayoutParams.WRAP_CONTENT));
		lpbt.gravity= Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL;
		
		
		
		final ListView listView = new ListView(getApplicationContext());
		listView.setBackgroundResource(R.drawable.background); //setBackgroundColor(Color.BLUE);
		listView.setLayoutParams(lpbt);
		ArrayAdapter<VideoModule> adapter = new VideoModuleArrayAdapter(getApplicationContext(),
	        R.layout.course_list_item, course.getVideoModules());    
	    // Assign adapter to ListView
	    listView.setAdapter(adapter); 
	            
	    // ListView Item Click Listener
	    listView.setOnItemClickListener(new OnItemClickListener() {
	        @Override
	        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	            // ListView Clicked item index
                int itemPosition = position;
		        // ListView Clicked item value
                VideoModule  videoModule    = (VideoModule) listView.getItemAtPosition(position);
		        currentModuleSequenceId = videoModule.getModuleSequenceId();
		        
		        beginVideoModule( view, videoModule ); 
	                  
	        }
	    }); 
	            
	    View listHeader = getLayoutInflater().inflate(R.layout.course_list_header, null);
	    listHeader.setClickable(false);
	    listView.addHeaderView(listHeader);		
	    
        setContentView(listView);
        
        addSignInButton();
        TextView pageText = (TextView) findViewById(R.id.courseListPageName);
        pageText.setText("Video Course Module:");
	}
	
	
	
	private void selectCourseModulePage(final TextCourse course) {
		Log.d("CourseInfo",course.getCourseId()+" "+course.getCourseName()+" "+
					course.getCourseType() );//+"  number of modules:"+course.getModules().size());
		
		TextView pageName = (TextView) findViewById(R.id.courseListPageName);
		pageName.setText(course.getCourseName());
		
		currentPage = TEXT_MODULE_PAGE;

		List<TextCourseModule> moduleList = course.getModules();
		
		LayoutParams lpbt = new LayoutParams((LayoutParams.MATCH_PARENT), (LayoutParams.WRAP_CONTENT));
		lpbt.gravity= Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL; 
		
		currentModuleNameToIdMap.clear();
		 for(final TextCourseModule courseModule : moduleList) {
		    	String moduleName = moduleIdNameMap.get(courseModule.getModuleId());
		    	currentModuleNameToIdMap.put(moduleName, courseModule.getModuleId());
		    
		 }
		 
		final ListView listView = new ListView(getApplicationContext());
	    listView.setBackgroundResource(R.drawable.background); //setBackgroundColor(Color.BLUE);
		listView.setLayoutParams(lpbt);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
		R.layout.course_list_item, R.id.courseListItemTextLine,new ArrayList<String>(currentModuleNameToIdMap.keySet()));
		// Assign adapter to ListView
		listView.setAdapter(adapter); 
		            
		// ListView Item Click Listener
		listView.setOnItemClickListener(new OnItemClickListener() {
		 
		              @Override
		                  public void onItemClick(AdapterView<?> parent, View view,
		                     int position, long id) {
		                    
			                   // ListView Clicked item index
			                   int itemPosition     = position;
			                   
			                   // ListView Clicked item value
			                   String  itemValue    = (String) listView.getItemAtPosition(position);
			                   currentTextModuleId = currentModuleNameToIdMap.get(itemValue).toString();
			                    // Show Alert 
			                   Toast.makeText(getApplicationContext(),
			                      "Position :"+itemPosition+"  ListItem : " +itemValue , Toast.LENGTH_LONG)
			                      .show();
			                   TextCourse course = courseMetaData.get(currentCourseId);
			                   TextCourseModule currentModule=null;
			                   for(TextCourseModule module : course.getModules()) {
			                	   if(module.getModuleId() == Long.parseLong(currentTextModuleId)) {
			                		   currentModule = module;
			                		   break;
			                	   }
			                   }
			                   selectExamsPage(course, currentModule);
		                   
		                  }
		    
		             }); 
		    ;
		    View listHeader = getLayoutInflater().inflate(R.layout.course_list_header, null);
		    listHeader.setClickable(false);
		    listView.addHeaderView(listHeader);	
		  
	        setContentView(listView);
	        
	        addSignInButton();
	        TextView pageText = (TextView) findViewById(R.id.courseListPageName);
	        pageText.setText("Module Menu for Course - "+ courseMetaData.get(currentCourseId).getCourseName());
	        //pageText.setTextSize(20);
	        //pageText.setTextColor(0);

	}
	
	//TODO: Need to have course information in courseModule
	private void selectExamsPage(final TextCourse course, TextCourseModule courseModule) {
		
		currentPage = TEXT_EXAM_PAGE; 
		
		//ScrollView scrollView = new ScrollView(getApplicationContext());
		TextView pageName = (TextView) findViewById(R.id.courseListPageName);
		pageName.setText(course.getCourseName());
		List<Exam> examList = courseModule.getExams();
		Collections.sort(examList, new Comparator<Exam>(){

			// Overriding the compare method to sort the age 
			public int compare(Exam d, Exam d1){
			   return d.getName().compareTo(d1.getName());
			}
		});
		//setContentView(R.layout.course_selection);
		
		TextView tv = new TextView(getApplicationContext() ) ;//findViewById(R.id.courseSelectionText);
		tv.setText("Exams in CourseModule: "+course.getCourseName()+", Module: "+courseModule.getModuleId());
		tv.setTextColor(Color.parseColor("black"));
		tv.setTextSize(20);
		
		examNameToExamMap.clear();
	    for(final Exam exam : examList) {
	    	examNameToExamMap.put(exam.getName(), exam);
	    }
	    

		LayoutParams lpbt = new LayoutParams((LayoutParams.MATCH_PARENT), (LayoutParams.WRAP_CONTENT));
		lpbt.gravity= Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL; 
		
        final ListView listView = new ListView(getApplicationContext());
		listView.setBackgroundResource(R.drawable.background); //setBackgroundColor(Color.BLUE);
		listView.setLayoutParams(lpbt);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
	              R.layout.course_list_item, R.id.courseListItemTextLine,new ArrayList<String>(examNameToExamMap.keySet()));    
	            // Assign adapter to ListView
	            listView.setAdapter(adapter); 
	            
	            // ListView Item Click Listener
	            listView.setOnItemClickListener(new OnItemClickListener() {
	 
	                  @Override
	                  public void onItemClick(AdapterView<?> parent, View view,
	                     int position, long id) {
	                    
		                   // ListView Clicked item index
		                   int itemPosition     = position;
		                   
		                   // ListView Clicked item value
		                   String  itemValue    = (String) listView.getItemAtPosition(position);
		                   Exam currentExam = examNameToExamMap.get(itemValue);
		                   currentTextExamId = ""+currentExam.getExamId();
		                    // Show Alert 
		                   Toast.makeText(getApplicationContext(),
		                      "Position :"+itemPosition+"  ListItem : " +itemValue , Toast.LENGTH_LONG)
		                      .show();
		                   
		                   beginExam(view, currentExam);
	                   
	                  }
	    
	             }); 
	            
	    View listHeader = getLayoutInflater().inflate(R.layout.course_list_header, null);
	    listHeader.setClickable(false);
	    listView.addHeaderView(listHeader);	
	            
        setContentView(listView);
        addSignInButton();
        TextView pageText = (TextView) findViewById(R.id.courseListPageName);
        pageText.setText("Exam Menu for Course - "+ courseMetaData.get(currentCourseId).getCourseName());
        //pageText.setTextSize(20);
        //pageText.setTextColor(0);

	}
	
	private void loginPage() {
		currentPage = LOGIN_PAGE;
        setContentView(R.layout.login_page);
        final Button buttonLogin = (Button) findViewById(R.id.loginButton);
        final ProgressBar mProgress = (ProgressBar) findViewById(R.id.progressDownloadBar);
        
        Log.d("loginPage", "construct progress bar:  "+ mProgress.getId());
        
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
             	// download restful feeds and serialize to DB 
            	String filePath = getApplicationContext().getFilesDir().getPath().toString() + "/CourseDB.json";
            	new CoursesDownloaderTask(mProgress).execute(getApplicationContext());
            	
   			    Toast.makeText(getApplicationContext(), "Downloading Courses... ", Toast.LENGTH_LONG).show();

            	//selectCoursesPage();
            }
        });
        /*
        final Button buttonBack = (Button) findViewById(R.id.loginBackButton);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	mainPage();
            }
        });*/
        
	}
	
	
    
    
   private Intent getDefaultIntent() {
       Intent intent = new Intent(Intent.ACTION_SEND);
       intent.setType("image/*");
       return intent;
   }
   
    private void openSearch(){
    	
    }
    
    private void openSettings(){
    	
    }
    
    
    
    /** Called when the user clicks the Send button */
    public void beginExam(View view, Exam exam) {
        Intent intent ;
        if(Integer.parseInt(exam.getModuleId())%2 == 0 ) {
        	intent = new Intent(this, ExamModeActivity.class);
    	} else {
        	intent = new Intent(this, PracticeModeActivity.class);
    	}
        intent.putExtra("courseId", currentCourseId);
        intent.putExtra("moduleId", currentTextModuleId);
        intent.putExtra("examId", currentTextExamId);
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
        intent = new Intent(this, MainActivity.class);
    	
        intent.putExtra(Constants.FLASH_CARD_BUCKET_NAME, bucket);
        intent.putExtra(Constants.COURSE_ID_NAME, currentCourseId);

        
        startActivity(intent);
    }
    public void beginVideoModule(View view, VideoModule module) {
    	Intent intent ;
        intent = new Intent(this, VideoPlayerActivity.class);
    	
        intent.putExtra(Constants.VIDEO_MODULE_NAME, module);
        
        startActivity(intent);
    }
    /*
    public void beginFlashCard(View view) {
    	
    }
    public void beginVideoCourse(View view) {
    }
    */
    
    @Override
    public void onBackPressed() {
    	if(currentPage == COURSE_PAGE) {
    		super.onBackPressed();
    	} else {
    		selectCoursesPage();
    	}
    }

}
