package com.apps.knowledgeRepo;


import com.apps.knowledagerepo.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class Fragment1 extends Fragment{
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		
	
		View fragment1 =  inflater.inflate(R.layout.fragment_1, container, false);
	     
		   Button button = (Button)fragment1.findViewById(R.id.button1);
		     
	       button.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
				
					
					TextView tv1 = (TextView)getView().findViewById(R.id.test1);
					Fragment tv2=  (Fragment)getFragmentManager().findFragmentById(R.id.fragment2);
					
					tv1.setText("next A");
					 View view = (View)tv2.getView();
					  TextView tv =  (TextView)view.findViewById(R.id.test2);
					  tv.setText("Next B");
				}
	         });

	     
	 
		
	     return fragment1;
		
		
	}

}
