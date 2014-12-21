package com.apps.knowledgeRepo;


import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.apps.knowledagerepo.R;
import com.apps.knowledgeRepo.dataModel.FlashCardBucket;
import com.apps.knowledgeRepo.dataModel.FlashCardCourse;
import com.apps.knowledgeRepo.om.Constants;
import com.apps.knowledgeRepo.utils.CourseUtil;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;


public class Fragment1 extends Fragment{
	String courseId = null;
	FlashCardBucket bucket= null;
	int currCardNum = 0;
	int next =  0;
	int prev = 0;
	TimerTask task;
    Timer timer;
	int max ;
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		
		Bundle extras = this.getActivity().getIntent().getExtras();
	
		if (extras != null) {
        	 bucket = (FlashCardBucket) extras.get(Constants.FLASH_CARD_BUCKET_NAME);	
        	 max = bucket.getCardList().size()-1;
        	 courseId = extras.getString(Constants.COURSE_ID_NAME);
        	 currCardNum = extras.getInt("currCardNum");
        	 
        }
        if(bucket==null) throw new RuntimeException("FlashCardBucket is null!");
        
		View fragment1 =  inflater.inflate(R.layout.fragment_1, container, false);

	     
		   Button button = (Button)fragment1.findViewById(R.id.button1);
		   Button buttonPrev = (Button)fragment1.findViewById(R.id.button2);
		   Button buttonAuto = (Button)fragment1.findViewById(R.id.button3);
		   Button buttonStop = (Button)fragment1.findViewById(R.id.button4);
		   
		   next = currCardNum + 1;
		   prev = currCardNum - 1;
		   
	       buttonPrev.setOnClickListener(new View.OnClickListener(){
	           
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//FlashCardCourse flashcard_course= (FlashCardCourse) CourseUtil.initilizeFlashCardCourse("iFC_04",  getActivity());
					//List<FlashCardBucket> buckets = flashcard_course.getBucket();
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
						
						if(next < max ){
						tv1.loadData(bucket.getCardList().get(next).getFrontText(),"text/html","utf-8");
						tv.loadData(bucket.getCardList().get(next).getBackText(),"text/html","utf-8");
		        	
					    next = next +1;
		        	    prev = next -1;
		        	    
						} else {
							
							tv1.loadData(bucket.getCardList().get(max).getFrontText(),"text/html","utf-8");
							tv.loadData(bucket.getCardList().get(max).getBackText(),"text/html","utf-8");
							
						}
		        	   
	                super.handleMessage(msg); 
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
					
				}}
				
	       
	       }); 
	       
	       buttonStop.setOnClickListener(new View.OnClickListener(){
	           
				@Override
				public void onClick(View v) {
			   
					if(task!=null){
				    	 
				    	 task.cancel();
				     }
					
					if(timer!=null){
			    	 
			    	 timer.cancel();
			    	 
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
					// TODO Auto-generated method stub
					//FlashCardCourse flashcard_course= (FlashCardCourse) CourseUtil.initilizeFlashCardCourse("iFC_04",  getActivity());
					//List<FlashCardBucket> buckets = flashcard_course.getBucket();
					WebView tv1 = (WebView)getView().findViewById(R.id.test1);
				    Fragment tv2=  (Fragment)getFragmentManager().findFragmentById(R.id.fragment2);
					
				    View view = (View)tv2.getView();
				    WebView tv =  (WebView)view.findViewById(R.id.test2);
				    
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

	     
	       return fragment1;
		
	   

		
	
	}

}
