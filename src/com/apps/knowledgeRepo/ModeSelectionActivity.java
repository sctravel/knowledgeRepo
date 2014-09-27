package com.apps.knowledgeRepo;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.content.res.ColorStateList;
import android.graphics.Color;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioGroup;
import android.widget.TextView;

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
import com.apps.knowledgeRepo.db.DBHelper;
import com.apps.knowledgeRepo.db.DBTool;
import com.apps.knowledgeRepo.utils.CourseUtil;

public class ModeSelectionActivity extends Activity {
	
	private Map<String, List<String>> courseMap = new HashMap<String, List<String>>();
	private String currentCourse;
	
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SQLiteDatabase db = DBTool.getDB(getApplicationContext());
        db.close();
    
        
     
        initializeCourses();
		setContentView(R.layout.mode_selection);
		//getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		// If your minSdkVersion is 11 or higher, instead use:
		getActionBar().setDisplayHomeAsUpEnabled(true);
		         
		initializeCourses();

		mainPage();		         
       

	}

	private void initializeCourses() {
		//TODO get all the courseIds and courseNames from DB
		List<String> course1ExamList = new ArrayList<String>();
		course1ExamList.add("iLF6");
		course1ExamList.add("Exam2");
		course1ExamList.add("Exam3");
		courseMap.put("iLF6", course1ExamList);
		
		List<String> course2ExamList = new ArrayList<String>();
		courseMap.put("course2", course2ExamList);
		course2ExamList.add("Exam4");
		course2ExamList.add("Exam5");

		List<String> course3ExamList = new ArrayList<String>();
		courseMap.put("course3", course3ExamList);
		course3ExamList.add("Exam6");
		course3ExamList.add("Exam7");
		course3ExamList.add("Exam8");

	}
	
	private void selectCoursesPage(){
		
		
		LayoutParams lptv = new LayoutParams((LayoutParams.WRAP_CONTENT), (LayoutParams.WRAP_CONTENT));
		lptv.gravity= Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL; 
		LayoutParams lpbt = new LayoutParams((LayoutParams.WRAP_CONTENT), (LayoutParams.WRAP_CONTENT));
		lpbt.gravity= Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL;
		
		LinearLayout linear = new LinearLayout(getApplicationContext());
		linear.setLayoutParams(lptv);
		linear.setVerticalGravity(MODE_APPEND);
		
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
	
		for(Map.Entry<String, List<String>> entry : courseMap.entrySet()) {
			final String courseId = entry.getKey();
			Button bt = new Button(getApplicationContext());
			bt.setText(courseId);
			linear.addView(bt);
			lpbt.topMargin=10;
			lpbt.bottomMargin=10;
		    bt.setLayoutParams(lpbt);
			bt.setTextColor(Color.parseColor("black"));
			bt.setGravity(Gravity.CENTER_VERTICAL);
			bt.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	            	Course course = CourseUtil.initilizeCourse(courseId, getApplicationContext());
	            	selectCourseModulePage(course);
	            }
	        });
		}
		
		Button back = new Button(getApplicationContext());
		back.setLayoutParams(lptv);
		lptv.topMargin=30;
		lptv.bottomMargin=30;
		back.setText("Back");
		back.setTextColor(Color.parseColor("black"));

		back.setGravity(Gravity.BOTTOM);
		linear.addView(back);
		back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	mainPage();
            }
        });
		linear.setOrientation(LinearLayout.VERTICAL);

        setContentView(linear);

	}
	
	private void selectCourseModulePage(final Course course) {
		Log.d("CourseInfo",course.getCourseId()+" "+course.getCourseName()+" "+
					course.getCourseType() );//+"  number of modules:"+course.getModules().size());
		
		List<CourseModule> moduleList = course.getModules();
		
		TextView tv = new TextView(getApplicationContext() ) ;//findViewById(R.id.courseSelectionText);
		tv.setText("Exams in Course "+course.getCourseName());
		tv.setTextColor(Color.parseColor("black"));
		tv.setTextSize(20);
		
		LayoutParams lptv = new LayoutParams((LayoutParams.WRAP_CONTENT), (LayoutParams.WRAP_CONTENT));
		lptv.gravity= Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL; 
		LayoutParams lpbt = new LayoutParams((LayoutParams.WRAP_CONTENT), (LayoutParams.WRAP_CONTENT));
		lpbt.gravity= Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL; 
		LinearLayout linear = new LinearLayout(getApplicationContext());
		linear.setLayoutParams(lptv);
		linear.setVerticalGravity(MODE_APPEND);
		linear.addView(tv);
		lptv.topMargin=50;
		lptv.bottomMargin=50;
		tv.setLayoutParams(lptv);
		tv.setGravity(Gravity.CENTER_VERTICAL);
		
		 for(final CourseModule courseModule : moduleList) {
		    	Button bt = new Button(getApplicationContext());
		    	bt.setText(courseModule.getModuleId()+"");
				linear.addView(bt);
				lpbt.topMargin=10;
				lpbt.bottomMargin=10;
				bt.setTextColor(Color.parseColor("black"));
				bt.setLayoutParams(lpbt);
				bt.setGravity(Gravity.CENTER_VERTICAL);
				
				bt.setOnClickListener(new View.OnClickListener() {
		            public void onClick(View v) {
		            	selectExamsPage(course,courseModule);
		            }
		        });
		    }
		    
			Button back = new Button(getApplicationContext());
			back.setText("Back");
			back.setTextColor(Color.parseColor("black"));
			
			lptv.topMargin=30;
			lptv.bottomMargin=30;
			back.setLayoutParams(lptv);
			back.setGravity(Gravity.BOTTOM);
			back.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	            	selectCoursesPage();
	            }
	        });
			linear.addView(back);
			linear.setOrientation(LinearLayout.VERTICAL);
	        setContentView(linear);
	}
	
	//TODO: Need to have course information in courseModule
	private void selectExamsPage(final Course course, CourseModule courseModule) {
		
		List<Exam> examList = courseModule.getExams();
		//setContentView(R.layout.course_selection);
		
		TextView tv = new TextView(getApplicationContext() ) ;//findViewById(R.id.courseSelectionText);
		tv.setText("Exams in CourseModule: "+course.getCourseName()+", Module: "+courseModule.getModuleId());
		tv.setTextColor(Color.parseColor("black"));
		tv.setTextSize(20);
		
		LayoutParams lptv = new LayoutParams((LayoutParams.WRAP_CONTENT), (LayoutParams.WRAP_CONTENT));
		lptv.gravity= Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL; 
		LayoutParams lpbt = new LayoutParams((LayoutParams.WRAP_CONTENT), (LayoutParams.WRAP_CONTENT));
		lpbt.gravity= Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL; 
		LinearLayout linear = new LinearLayout(getApplicationContext());
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
	            	beginExam(v, exam);
	            }
	        });
	    }
	    
		Button back = new Button(getApplicationContext());
		back.setText("Back");
		back.setTextColor(Color.parseColor("black"));
		
		lptv.topMargin=30;
		lptv.bottomMargin=30;
		back.setLayoutParams(lptv);
		back.setGravity(Gravity.BOTTOM);
		back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	selectCourseModulePage(course);
            }
        });
		linear.addView(back);
		linear.setOrientation(LinearLayout.VERTICAL);
        setContentView(linear);

	}
	private void mainPage() {
        setContentView(R.layout.mode_selection);
        final Button buttonHomeRoom = (Button) findViewById(R.id.homeRoom);
        buttonHomeRoom.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	selectCoursesPage();
            }
        });
        
        final Button buttonDownloadCourses = (Button) findViewById(R.id.downloadCoursesButton);
        buttonDownloadCourses.setOnClickListener(new View.OnClickListener() {
          
            public void onClick(View v) {    	
            	loginPage();

            }
        });
	}
	private void loginPage() {
        setContentView(R.layout.login_page);
        final Button buttonLogin = (Button) findViewById(R.id.loginButton);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
             	// download restful feeds and serialize to DB 
            	String filePath = getApplicationContext().getFilesDir().getPath().toString() + "/CourseDB.json";
            	new CoursesDownloaderTask().execute(getApplicationContext());
            	
   			    Toast.makeText(getApplicationContext(), "Downloading Courses... ", Toast.LENGTH_LONG).show();

            	selectCoursesPage();
            }
        });
        final Button buttonBack = (Button) findViewById(R.id.loginBackButton);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	mainPage();
            }
        });
        
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
        Intent intent = new Intent(this, ExamModeActivity.class);
        //EditText editText = (EditText) findViewById(R.id.edit_message);
        //String message = editText.getText().toString();
        //intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra("exam", exam);
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
