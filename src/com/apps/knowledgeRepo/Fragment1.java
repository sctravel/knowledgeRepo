package com.apps.knowledgeRepo;


import java.util.List;

import com.apps.knowledagerepo.R;
import com.apps.knowledgeRepo.dataModel.Bucket;
import com.apps.knowledgeRepo.dataModel.FlashCardCourse;
import com.apps.knowledgeRepo.utils.CourseUtil;

import android.os.Bundle;
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
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		
		
		View fragment1 =  inflater.inflate(R.layout.fragment_1, container, false);
     	     return fragment1;
		
		
	}

}
