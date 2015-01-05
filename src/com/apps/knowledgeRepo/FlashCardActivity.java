package com.apps.knowledgeRepo;

import com.apps.knowledgeRepo.R;
import com.apps.knowledgeRepo.activityHelper.AnimationFactory;
import com.apps.knowledgeRepo.activityHelper.AnimationFactory.FlipDirection;
import com.apps.knowledgeRepo.dataModel.FlashCardBucket;
import com.apps.knowledgeRepo.om.Constants;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ViewAnimator;

public class FlashCardActivity extends FragmentActivity {

    FlashCardBucket  bucket;
    String courseId ;
    String buckedId;
    int currCardNum = 0;
    Bundle extras;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	                  
        extras = getIntent().getExtras();
        if (extras != null) {
        	 bucket = (FlashCardBucket) extras.get(Constants.FLASH_CARD_BUCKET_NAME);	
         	 courseId = extras.getString(Constants.COURSE_ID_NAME);
         	 currCardNum = extras.getInt("currCardNum");
        }
        if(bucket == null) throw new RuntimeException("FlashCardBucket is null!");
     	     
		setContentView(R.layout.flash_card_mode);
		
		final ViewAnimator viewAnimator1 = (ViewAnimator)this.findViewById(R.id.viewFlipper1);
		
		
       	WebView test1 = (WebView)this.findViewById(R.id.fragment1).findViewById(R.id.test1);
    	WebView test2 = (WebView)this.findViewById(R.id.fragment2).findViewById(R.id.test2);
		 
    	test1.setOnClickListener(new OnClickListener(){    		
			@Override
			public void onClick(View v){
				Log.d("webview", "click webview1");
				AnimationFactory.flipTransition(viewAnimator1, FlipDirection.LEFT_RIGHT);
			}

		});
	    
	    test2.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				Log.d("webview", "click webview2");
				AnimationFactory.flipTransition(viewAnimator1, FlipDirection.LEFT_RIGHT);
			}

		});
	    //Button button = (Button)test1.findViewById(R.id.button1);
	    //Button buttonPrev = (Button)test1.findViewById(R.id.button2);
	  
	    test1.loadData(bucket.getCardList().get(currCardNum).getFrontText(), "text/html","utf-8");
	    test2.loadData(bucket.getCardList().get(currCardNum).getBackText(), "text/html","utf-8");
	 

	    setTitle(bucket.getTitle());
	   
	    
		viewAnimator1.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				AnimationFactory.flipTransition(viewAnimator1, FlipDirection.LEFT_RIGHT);
			}

		});
	    
	    
	    
	}
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.exam_mode_menu, menu);
		return true;
	}
	
	   @Override
			protected void onStop() {
				// TODO Auto-generated method stub
				super.onStop();
				
				
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
	
	@Override
	public boolean dispatchTouchEvent (MotionEvent ev) {
	    // Do your calculations
	    return super.dispatchTouchEvent(ev);
	}
}
