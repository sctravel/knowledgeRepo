package com.apps.knowledgeRepo;


import com.apps.knowledagerepo.R;
import com.apps.knowledgeRepo.AnimationFactory.FlipDirection;
import com.apps.knowledgeRepo.dataModel.Exam;
import com.apps.knowledgeRepo.dataModel.FlashCardCourse;
import com.apps.knowledgeRepo.db.DBHelper;
import com.apps.knowledgeRepo.utils.CourseUtil;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ViewAnimator;

public class MainActivity extends FragmentActivity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
         String courseId ="1";
        
		FlashCardCourse flashcard_course= CourseUtil.initilizeFlashCardCourse(courseId, getBaseContext());
		
	  
		setContentView(R.layout.activity_main);
		final ViewAnimator viewAnimator1 = (ViewAnimator)this.findViewById(R.id.viewFlipper1);
	   
		
		 DBHelper dbHelper = new DBHelper(getBaseContext());
		 SQLiteDatabase db = dbHelper.getWritableDatabase();	
		
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
