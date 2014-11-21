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
import com.apps.knowledgeRepo.activityHelper.CoursesDownloaderTask;
import com.apps.knowledgeRepo.activityHelper.ExamDownloaderTask;
import com.apps.knowledgeRepo.dataModel.Course;
import com.apps.knowledgeRepo.dataModel.CourseModule;
import com.apps.knowledgeRepo.dataModel.Exam;
import com.apps.knowledgeRepo.dataModel.ExamMetaData;
import com.apps.knowledgeRepo.db.DBTool;

public class ModeSelectionActivity extends Activity {
	
	// Map< courseId, Map<moduleId, List<examId> >
	private Map<String,Course> courseMetaData = new HashMap<String, Course>();
	private List<ExamMetaData> examMetaDataList = new ArrayList<ExamMetaData>();
	
	private String currentCourseId=null;
	private String currentModuleId=null;
	private String currentExamId=null;
	
	private  static final Map<Long, String> moduleIdNameMap = new HashMap<Long, String>(); 
	private  static final Map<String, String> courseNameToIdMap = new HashMap<String,String>();
	private static final Map<String, Long> currentModuleNameToIdMap = new HashMap<String, Long>();

	static {
		moduleIdNameMap.put( 0L, "Simulation Exams");
		moduleIdNameMap.put(1L, "Open Book Exams");
		moduleIdNameMap.put(2L, "Close Book Exams");
		moduleIdNameMap.put(3L, "By Topic Exams");
	};
	
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*SQLiteDatabase db = DBTool.getDB(getApplicationContext());
      
        if(db.isOpen()){
          db.close();
        }*/
        
   		setContentView(R.layout.mode_selection);
		//getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		// If your minSdkVersion is 11 or higher, instead use:
		getActionBar().setDisplayHomeAsUpEnabled(true);
		      
	
		mainPage();		         
       
	}
	
	private void extractCourseInfoFromExamMetaData() {
		
		Log.d("extractCourseInfoFromExamMetaData", "ExamMetaDataList size: "+examMetaDataList.size());
		
		for(ExamMetaData emd : examMetaDataList) {
			String courseId = emd.getCourseId();
			String moduleId = emd.getModuleId();
			String examId = emd.getExamId();
			String examName = emd.getExamName();

			
			//Get course Info
			Course course = null;
			course = courseMetaData.get(courseId);
			if(course == null) {
				course = new Course(emd.getCourseId(),emd.getCourseName(),emd.getCourseType(),emd.getCourseOrientation());
			}
			CourseModule module= null;
			//Get module info
			for(CourseModule m : course.getModules()) {
				if(m.getModuleId() == Long.parseLong(moduleId)) {
					module = m;
					break;
				}
			}
			if(module == null) {
				module = new CourseModule(Long.parseLong(emd.getModuleId()), emd.getGuide());
				course.getModules().add(module);
			}
			
			module.getExams().add(new Exam(courseId,moduleId, Long.parseLong(examId),examName));
			//new Course(emd.getCourseId(),emd.getCourseName(),emd.getCourseType(),emd.getCourseOrientation());
			//Exam exam = new Exam(Long.parseLong(examId));		
			
			courseMetaData.put(courseId, course);
		}
	}
	
	private void selectCoursesPage(){
		
		ScrollView scrollView = new ScrollView(getApplicationContext());

		LayoutParams lptv = new LayoutParams((LayoutParams.MATCH_PARENT), (LayoutParams.WRAP_CONTENT));
		lptv.gravity= Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL; 
		LayoutParams lpbt = new LayoutParams((LayoutParams.MATCH_PARENT), (LayoutParams.WRAP_CONTENT));
		lpbt.gravity= Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL;
		
		LinearLayout linear = new LinearLayout(getApplicationContext());
		linear.setLayoutParams(lptv);
		linear.setVerticalGravity(MODE_APPEND);
		
		//Set up the text view at the top
		TextView tv = new TextView(getApplicationContext() ) ;//findViewById(R.id.courseSelectionText);
		tv.setText("Please select a course: ");
		tv.setTextColor(Color.parseColor("black"));
		tv.setTextSize(20);

		lptv.topMargin=50;
		lptv.bottomMargin=50;
	    tv.setLayoutParams(lptv);
		linear.addView(tv);
		//tv.setGravity(Gravity.CENTER);
		//tv.setTextSize(10);
	
		//Adding ListView
		//List<String> courseNames = new ArrayList<String>();
		for(Map.Entry<String, Course> entry : courseMetaData.entrySet()) {
			final String courseId = entry.getKey();
			final Course course = entry.getValue();
			
			courseNameToIdMap.put( course.getCourseName(), courseId);
			//courseNames.add(course.getCourseName());
			/*
			Button bt = new Button(getApplicationContext());
			bt.setText(course.getCourseName());
			linear.addView(bt);
			lpbt.topMargin=10;
			lpbt.bottomMargin=10;
		    bt.setLayoutParams(lpbt);
			bt.setTextColor(Color.parseColor("black"));
			bt.setGravity(Gravity.CENTER_VERTICAL);
			bt.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	            	//Course course = CourseUtil.initilizeExam(courseId, moduleId, examId, getBaseContext());
	            	currentCourseId = course.getCourseId();
	            	if(course.getModules().size()>1) {
	            		selectCourseModulePage(course);
	            	} else {
	            		//If there's only one module, skip the select Module page
	            		currentModuleId=""+course.getModules().get(0).getModuleId();
	            		selectExamsPage(course,course.getModules().get(0));
	            	}
	            }
	        });*/
		}
		final ListView listView = new ListView(getApplicationContext());
		Log.d("aa","courseNames: "+courseNameToIdMap.keySet());
		listView.setBackgroundResource(R.drawable.background); //setBackgroundColor(Color.BLUE);
		listView.setLayoutParams(lpbt);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
	              R.layout.course_list_item, R.id.courseListItemTextLine,new ArrayList<String>(courseNameToIdMap.keySet()));
	    
	    
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
		                   currentCourseId = courseNameToIdMap.get(itemValue);
		                    // Show Alert 
		                   Toast.makeText(getApplicationContext(),
		                      "Position :"+itemPosition+"  ListItem : " +itemValue , Toast.LENGTH_LONG)
		                      .show();
		                   Course course = courseMetaData.get(currentCourseId);
		                   if(course.getModules().size()>1) {
			            		selectCourseModulePage(course);
			               } else {
			            		//If there's only one module, skip the select Module page
			            		currentModuleId=""+course.getModules().get(0).getModuleId();
			            		selectExamsPage(course,course.getModules().get(0));
			               }
	                   
	                  }
	    
	             }); 
	            
	    View listHeader = getLayoutInflater().inflate(R.layout.course_list_header, null);
	    listHeader.setClickable(false);
	    listView.addHeaderView(listHeader);		
	    //listView.addHeaderView(scrollView);
		Button back = new Button(getApplicationContext());
		LayoutParams lpbc = new LayoutParams((LayoutParams.WRAP_CONTENT), (LayoutParams.WRAP_CONTENT));
		lpbc.topMargin=30;
		lpbc.bottomMargin=30;
		back.setText("Back");
		back.setTextColor(Color.parseColor("black"));
		lpbc.gravity= Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL; 
		back.setLayoutParams(lpbc);
		back.setGravity(Gravity.BOTTOM);
		linear.addView(back);
		back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	mainPage();
            }
        });
		linear.setOrientation(LinearLayout.VERTICAL);
		scrollView.addView(linear);
        setContentView(listView);

	}
	
	private void selectCourseModulePage(final Course course) {
		Log.d("CourseInfo",course.getCourseId()+" "+course.getCourseName()+" "+
					course.getCourseType() );//+"  number of modules:"+course.getModules().size());
		
		ScrollView scrollView = new ScrollView(getApplicationContext());

		List<CourseModule> moduleList = course.getModules();
		//RelativeLayout rl = findViewById() 
		TextView tv = new TextView(getApplicationContext() ) ;//findViewById(R.id.courseSelectionText);
		tv.setText("Exams in Course "+course.getCourseName());
		tv.setTextColor(Color.parseColor("black"));
		tv.setTextSize(20);
		
		LayoutParams lptv = new LayoutParams((LayoutParams.MATCH_PARENT), (LayoutParams.WRAP_CONTENT));
		lptv.gravity= Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL; 
		LayoutParams lpbt = new LayoutParams((LayoutParams.MATCH_PARENT), (LayoutParams.WRAP_CONTENT));
		lpbt.gravity= Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL; 
		LinearLayout linear = new LinearLayout(getApplicationContext());
		linear.setLayoutParams(lptv);
		linear.setVerticalGravity(MODE_APPEND);
		linear.addView(tv);
		lptv.topMargin=50;
		lptv.bottomMargin=50;
		tv.setLayoutParams(lptv);
		tv.setGravity(Gravity.CENTER_VERTICAL);
		currentModuleNameToIdMap.clear();
		 for(final CourseModule courseModule : moduleList) {
		    	String moduleName = moduleIdNameMap.get(courseModule.getModuleId());
		    	currentModuleNameToIdMap.put(moduleName, courseModule.getModuleId());
		    	/*
		    	Button bt = new Button(getApplicationContext());
		    	Log.d("DB","ModuleName: "+moduleName +" for moduleId-" +courseModule.getModuleId() );
		    	bt.setText(moduleName!=null? moduleName : ""+courseModule.getModuleId() );
				linear.addView(bt);
				lpbt.topMargin=10;
				lpbt.bottomMargin=10;
				bt.setTextColor(Color.parseColor("black"));
				bt.setLayoutParams(lpbt);
				bt.setGravity(Gravity.CENTER_VERTICAL);
				
				bt.setOnClickListener(new View.OnClickListener() {
		            public void onClick(View v) {
		            	currentModuleId = ""+courseModule.getModuleId();
		            	selectExamsPage(course,courseModule);
		            }
		        });*/
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
			                   currentModuleId = currentModuleNameToIdMap.get(itemValue).toString();
			                    // Show Alert 
			                   Toast.makeText(getApplicationContext(),
			                      "Position :"+itemPosition+"  ListItem : " +itemValue , Toast.LENGTH_LONG)
			                      .show();
			                   Course course = courseMetaData.get(currentCourseId);
			                   CourseModule currentModule=null;
			                   for(CourseModule module : course.getModules()) {
			                	   if(module.getModuleId() == Long.parseLong(currentModuleId)) {
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
		    Button back = new Button(getApplicationContext());
			back.setText("Back");
			back.setTextColor(Color.parseColor("black"));
			LayoutParams lpbc = new LayoutParams((LayoutParams.WRAP_CONTENT), (LayoutParams.WRAP_CONTENT));
			back.setLayoutParams(lpbc);
			lpbc.topMargin=30;
			lpbc.bottomMargin=30;
			lpbc.gravity= Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL; 

			back.setLayoutParams(lpbc);
			back.setGravity(Gravity.BOTTOM);
			
			back.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	            	selectCoursesPage();
	            }
	        });
			linear.addView(back);
			linear.setOrientation(LinearLayout.VERTICAL);
			
			scrollView.addView(linear);
	        setContentView(listView);
	}
	
	//TODO: Need to have course information in courseModule
	private void selectExamsPage(final Course course, CourseModule courseModule) {
		
		ScrollView scrollView = new ScrollView(getApplicationContext());
		
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
		
		LayoutParams lptv = new LayoutParams((LayoutParams.MATCH_PARENT), (LayoutParams.WRAP_CONTENT));
		lptv.gravity= Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL; 
		LayoutParams lpbt = new LayoutParams((LayoutParams.MATCH_PARENT), (LayoutParams.WRAP_CONTENT));
		lpbt.gravity= Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL; 
		LinearLayout linear = new LinearLayout(getApplicationContext());
		linear.setLayoutParams(lptv);
		linear.setLayoutParams(lptv);
		linear.setVerticalGravity(MODE_APPEND);
		linear.addView(tv);
		lptv.topMargin=50;
		lptv.bottomMargin=50;
		tv.setLayoutParams(lptv);
		tv.setGravity(Gravity.CENTER_VERTICAL);

	    for(final Exam exam : examList) {
	    	Button bt = new Button(getApplicationContext());
	    	bt.setText(exam.getName());
			linear.addView(bt);
			lpbt.topMargin=10;
			lpbt.bottomMargin=10;
			bt.setTextColor(Color.parseColor("black"));
			bt.setLayoutParams(lpbt);
			bt.setGravity(Gravity.CENTER_VERTICAL);
			
			bt.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {         	
	            	currentExamId = ""+exam.getExamId();
	            	beginExam(v, exam);
	            }
	        });
	    }
	    
		Button back = new Button(getApplicationContext());
		back.setText("Back");
		back.setTextColor(Color.parseColor("black"));
		LayoutParams lpbc = new LayoutParams((LayoutParams.WRAP_CONTENT), (LayoutParams.WRAP_CONTENT));
		lpbc.topMargin=30;
		lpbc.bottomMargin=30;
		lpbc.gravity= Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL; 
		back.setLayoutParams(lpbc);
		back.setGravity(Gravity.BOTTOM);
		back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	if(course.getModules().size() > 1) {
            		selectCourseModulePage(course);
            	} else {
            		selectCoursesPage();
            	}
            }
        });
		linear.addView(back);
		linear.setOrientation(LinearLayout.VERTICAL);
		
		scrollView.addView(linear);
        setContentView(scrollView);

	}
	private void mainPage() {
        setContentView(R.layout.mode_selection);
        final Button buttonHomeRoom = (Button) findViewById(R.id.homeRoom);
        buttonHomeRoom.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	//Get course information  List of Ids and Names
        		examMetaDataList = DBTool.getExamMeataDataList(getApplicationContext());
        		extractCourseInfoFromExamMetaData();
            	selectCoursesPage();
            }
        });
        
        final Button buttonDownloadCourses = (Button) findViewById(R.id.downloadCoursesButton);
        buttonDownloadCourses.setOnClickListener(new View.OnClickListener() {
          
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
        
        
        final Button buttonPlayVideo = (Button) findViewById(R.id.playVideoButton);
        buttonPlayVideo.setOnClickListener(new View.OnClickListener() {
          
            public void onClick(View v) {    	
            	Intent intent = new Intent(ModeSelectionActivity.this, VideoPlayerActivity.class);
                //EditText editText = (EditText) findViewById(R.id.edit_message);
                //String message = editText.getText().toString();
                //intent.putExtra(EXTRA_MESSAGE, message);
                startActivity(intent);

            }
        });
        
        final Button quitAppButton = (Button) findViewById(R.id.quitAppButton);
        quitAppButton.setOnClickListener(new View.OnClickListener() {
          
            public void onClick(View v) { 
            	finish();
            	System.exit(0);

            }
        });
	}
	private void loginPage() {
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
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.exam_mode_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        // Configure the search info and add any event listeners
        
        // MenuItem shareItem = menu.findItem(R.id.action_share);
        // mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        // mShareActionProvider.setShareIntent(getDefaultIntent());
        
        // When using the support library, the setOnActionExpandListener() method is
        // static and accepts the MenuItem object as an argument
        MenuItemCompat.setOnActionExpandListener(searchItem, new OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Do something when collapsed
                return true;  // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Do something when expanded
                return true;  // Return true to expand action view
            }
        });
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
        intent.putExtra("moduleId", currentModuleId);
        intent.putExtra("examId", currentExamId);
        startActivity(intent);
    }
    public void beginPractice(View view, String courseId, String courseModuleId, String examId) {
        Intent intent = new Intent(this, ViewAnswerModeActivity.class);
        //EditText editText = (EditText) findViewById(R.id.edit_message);
        //String message = editText.getText().toString();
        //intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
    
    public void downloadTests() {
    	
    	String samplePath ="http://farm1.static.flickr.com/114/298125983_0e4bf66782_b.jpg";
    	
    	String localFileName = Environment.getExternalStorageDirectory().getAbsolutePath() +"sample.jpg";
    			  	
    	new ExamDownloaderTask().execute(new String[]{samplePath, localFileName});
      
  
    	
    }
}
