package com.apps.knowledgeRepo;


import java.util.List;

import com.apps.knowledagerepo.R;
import com.apps.knowledgeRepo.AnimationFactory.FlipDirection;
import com.apps.knowledgeRepo.dataModel.FlashCardBucket;
import com.apps.knowledgeRepo.dataModel.Exam;
import com.apps.knowledgeRepo.dataModel.FlashCardCourse;
import com.apps.knowledgeRepo.dataModel.VideoModule;
import com.apps.knowledgeRepo.db.DBHelper;
import com.apps.knowledgeRepo.om.Constants;
import com.apps.knowledgeRepo.utils.CourseUtil;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewAnimator;

public class MainActivity extends FragmentActivity {

       FlashCardCourse flashcard_course;
       FlashCardBucket  bucket;
       String courseId ;
       int currCardNum = 0;
       
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	                  
         Bundle extras = getIntent().getExtras();
         if (extras != null) {
        	  bucket = (FlashCardBucket) extras.get(Constants.FLASH_CARD_BUCKET_NAME);	
         	  courseId = extras.getString(Constants.COURSE_ID_NAME);
         	  currCardNum = extras.getInt("currCardNum");
         }
         if(bucket == null) throw new RuntimeException("FlashCardBucket is null!");
     	
       
        
	     
		setContentView(R.layout.activity_main);
		
		final ViewAnimator viewAnimator1 = (ViewAnimator)this.findViewById(R.id.viewFlipper1);
		
		
       	WebView test1 = (WebView)this.findViewById(R.id.fragment1).findViewById(R.id.test1);
    	WebView test2 = (WebView)this.findViewById(R.id.fragment2).findViewById(R.id.test2);
		  
	  Button button = (Button)test1.findViewById(R.id.button1);
	  Button buttonPrev = (Button)test1.findViewById(R.id.button2);
	  
	 test1.loadData(bucket.getCardList().get(currCardNum).getFrontText(), "text/html","utf-8");
	 test2.loadData(bucket.getCardList().get(currCardNum).getBackText(), "text/html","utf-8");
	 
	
/*
	 button.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
			}
       });
		*/
		
		viewAnimator1.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				AnimationFactory.flipTransition(viewAnimator1, FlipDirection.LEFT_RIGHT);
			}

		});
	}
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_message, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
