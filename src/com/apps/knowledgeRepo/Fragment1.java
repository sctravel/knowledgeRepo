package com.apps.knowledgeRepo;


import java.util.List;

import com.apps.knowledagerepo.R;
import com.apps.knowledgeRepo.dataModel.FlashCardBucket;
import com.apps.knowledgeRepo.dataModel.FlashCardCourse;
import com.apps.knowledgeRepo.om.Constants;
import com.apps.knowledgeRepo.utils.CourseUtil;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		
		Bundle extras = this.getActivity().getIntent().getExtras();
		if (extras != null) {
        	 bucket = (FlashCardBucket) extras.get(Constants.FLASH_CARD_BUCKET_NAME);	
        	 courseId = extras.getString(Constants.COURSE_ID_NAME);
        }
        if(bucket==null) throw new RuntimeException("FlashCardBucket is null!");
        
		View fragment1 =  inflater.inflate(R.layout.fragment_1, container, false);
	     
		   Button button = (Button)fragment1.findViewById(R.id.button1);
		     
	       button.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//FlashCardCourse flashcard_course= (FlashCardCourse) CourseUtil.initilizeFlashCardCourse("iFC_04",  getActivity());
					//List<FlashCardBucket> buckets = flashcard_course.getBucket();
					
					WebView tv1 = (WebView)getView().findViewById(R.id.test1);
					Fragment tv2=  (Fragment)getFragmentManager().findFragmentById(R.id.fragment2);
					
					tv1.loadData(bucket.getCardList().get(1).getFrontText(),"text/html","utf-8");
					 View view = (View)tv2.getView();
					 WebView tv =  (WebView)view.findViewById(R.id.test2);
						tv.loadData(bucket.getCardList().get(1).getBackText(),"text/html","utf-8");
				}
	         });

	     
	 
		
	     return fragment1;
		
		
	}

}
