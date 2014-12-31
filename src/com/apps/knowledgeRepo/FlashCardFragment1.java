package com.apps.knowledgeRepo;


import java.util.Timer;
import java.util.TimerTask;
import com.apps.knowledagerepo.R;
import com.apps.knowledgeRepo.dataModel.FlashCardBucket;

import com.apps.knowledgeRepo.dataModel.FlashCardCourse;
import com.apps.knowledgeRepo.db.DBHelper;
import com.apps.knowledgeRepo.db.DBTool;
import com.apps.knowledgeRepo.om.Constants;
import com.apps.knowledgeRepo.utils.CourseUtil;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.apps.knowledgeRepo.om.Constants;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewAnimator;


public class FlashCardFragment1 extends Fragment{
	String courseId = null;
	String bucketId = null;
	FlashCardBucket bucket= null;
	int currCardNum = 0;
	int next =  0;
	int prev = 0;
	TimerTask task;
    Timer timer;

	int max ;
	
	@SuppressLint("HandlerLeak")
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		
		Bundle extras = this.getActivity().getIntent().getExtras();
	
		if (extras != null) {
        	 bucket = (FlashCardBucket)extras.get(Constants.FLASH_CARD_BUCKET_NAME);	
        	 bucketId =  String.valueOf(bucket.getBucketId());	
        	 courseId = extras.getString(Constants.COURSE_ID_NAME);
        	 max = bucket.getCardList().size()-1;
        	 currCardNum = extras.getInt("currCardNum");     	 
        }
        if(bucket==null) throw new RuntimeException("FlashCardBucket is null!");
        
		View fragment1 =  inflater.inflate(R.layout.fragment_1, container, false);

	    Button button = (Button)fragment1.findViewById(R.id.button1);
	    final Button buttonPrev = (Button)fragment1.findViewById(R.id.button2);
	    Button buttonAuto = (Button)fragment1.findViewById(R.id.button3);
	    Button buttonStop = (Button)fragment1.findViewById(R.id.button4);
	    Button buttonJump = (Button)fragment1.findViewById(R.id.button5);
	                      
	    final EditText jumpTo = (EditText)fragment1.findViewById(R.id.jumpTo);

	    next = currCardNum + 1;
        prev = currCardNum - 1;

		buttonJump.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String jumpNum = jumpTo.getText().toString();
				
				int jump = Integer.valueOf(jumpNum);
				
				if(!(jump>max) && !(jump<0)){
					
					WebView tv1 = (WebView)getView().findViewById(R.id.test1);
					Fragment tv2=  (Fragment)getFragmentManager().findFragmentById(R.id.fragment2);
					currCardNum = jump;
					tv1.loadData(bucket.getCardList().get(jump).getFrontText(),"text/html","utf-8");
					View view = (View)tv2.getView();
				    WebView tv =  (WebView)view.findViewById(R.id.test2);
					tv.loadData(bucket.getCardList().get(jump).getBackText(),"text/html","utf-8");
					if(jump < max){
						next = jump + 1;
					}else{ 
						next = max; 
					}
				    prev = jump - 1;
				
				}
				
				jumpTo.setText(null);
			}
		});
		   


        buttonPrev.setOnClickListener(new View.OnClickListener(){
	           
			@Override
			public void onClick(View v) {
				if(prev >=0){

					WebView tv1 = (WebView)getView().findViewById(R.id.test1);
					Fragment tv2=  (Fragment)getFragmentManager().findFragmentById(R.id.fragment2);
					
					tv1.loadData(bucket.getCardList().get(prev).getFrontText(),"text/html","utf-8");
					View view = (View)tv2.getView();
					WebView tv =  (WebView)view.findViewById(R.id.test2);
				    tv.loadData(bucket.getCardList().get(prev).getBackText(),"text/html","utf-8");
					next = prev + 1;
					prev = prev - 1;

				}	
			}
	    });
	             
       final Handler handler = new Handler() { 
            public void handleMessage(Message msg) { 
            	    
        	    WebView tv1 = (WebView)getView().findViewById(R.id.test1);
				Fragment tv2=  (Fragment)getFragmentManager().findFragmentById(R.id.fragment2);
				
				View view = (View)tv2.getView();
				WebView tv =  (WebView)view.findViewById(R.id.test2);				
				
				if(currCardNum < max){
					currCardNum = currCardNum + 1;
				}
				if(next < max ){
					tv1.loadData(bucket.getCardList().get(next).getFrontText(),"text/html","utf-8");
					tv.loadData(bucket.getCardList().get(next).getBackText(),"text/html","utf-8");      	    
				    next = next +1;
	        	    prev = next -1;       	    
				} else {				
					tv1.loadData(bucket.getCardList().get(max).getFrontText(),"text/html","utf-8");
					tv.loadData(bucket.getCardList().get(max).getBackText(),"text/html","utf-8");
				}
			}	
       };
	         
	
	   buttonAuto.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				if(timer == null) {				
					timer = new Timer(true);
				}
				if(task == null ){
				   task = new TimerTask(){  
			           public void run() {  
			        	   Message m = new Message(); 
		                   handler.sendMessage(m); 
				       }  			       
				   };  
				   timer.schedule(task,1000, 1000); 
				}
			}   
	    }); 
	       
	    buttonStop.setOnClickListener(new View.OnClickListener(){          
			@Override
			public void onClick(View v) {		   
				if(task!=null){
			    	 task.cancel();
			     }
			     task = null;
			     timer = null;
			      
			     next =0;
			     prev =0;
					
			}
	    }); 
				
		   
	   button.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				if(currCardNum <max ){		
					currCardNum = currCardNum + 1; 
				}
				
				WebView tv1 = (WebView)getView().findViewById(R.id.test1);
			    Fragment tv2=  (Fragment)getFragmentManager().findFragmentById(R.id.fragment2);
				
			    View view = (View)tv2.getView();
			    WebView tv =  (WebView)view.findViewById(R.id.test2);
			    currCardNum = currCardNum+1;
			    if(next < max){
	
				    tv1.loadData(bucket.getCardList().get(next).getFrontText(),"text/html","utf-8");
				   
					tv.loadData(bucket.getCardList().get(next).getBackText(),"text/html","utf-8");
					
					prev = next -1 ;
					next = next +1;
				} else {
					tv1.loadData(bucket.getCardList().get(max).getFrontText(),"text/html","utf-8");
					   
					tv.loadData(bucket.getCardList().get(max).getBackText(),"text/html","utf-8");
					
					prev = max -1 ;
					next = max;
				}
			}
	   });    
	     
		   
       button.setOnClickListener(new View.OnClickListener(){
		   @Override
		   public void onClick(View v) {
			   WebView tv1 = (WebView)getView().findViewById(R.id.test1);
			   Fragment tv2=  (Fragment)getFragmentManager().findFragmentById(R.id.fragment2);
				
			   tv1.loadData(bucket.getCardList().get(next).getFrontText(),"text/html","utf-8");
			   View view = (View)tv2.getView();
			   WebView tv =  (WebView)view.findViewById(R.id.test2);
			   tv.loadData(bucket.getCardList().get(next).getBackText(),"text/html","utf-8");
					
			   prev = next -1 ;
			   next = next +1;
		   }
        });    
        return fragment1;
	}

    @Override
    public void onStop() {
				// TODO Auto-generated method stub
	    super.onStop();
		
	    Context context = this.getActivity().getBaseContext();
	    SQLiteDatabase db = DBTool.getDB(context);
		
	    DBTool.recordFlashcardNum(context, db, courseId, bucketId, String.valueOf(currCardNum));
				
    }					

}
