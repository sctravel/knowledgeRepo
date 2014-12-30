package com.apps.knowledgeRepo.activityHelper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.apps.knowledagerepo.R;
import com.apps.knowledgeRepo.dataModel.ExamModuleMetaData;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class TextModuleArrayAdapter extends ArrayAdapter<ExamModuleMetaData>{
	private final Context context;
	private final List<ExamModuleMetaData> modules;
	private final int layoutId;
	  
	private static final Map<String, String> moduleIdNameMap = new HashMap<String, String>(); 
	static {
		moduleIdNameMap.put("0", "Simulation Exams");
		moduleIdNameMap.put("1", "Open Book Exams");
		moduleIdNameMap.put("2", "Close Book Exams");
		moduleIdNameMap.put("3", "By Topic Exams");
	};

	public TextModuleArrayAdapter(Context context, int layoutId, List<ExamModuleMetaData> modules){
		super(context, layoutId, modules);
		this.layoutId = layoutId;
	    this.context = context;
	    this.modules = modules;		  
	}
	public TextModuleArrayAdapter(Context context, int layoutId, ExamModuleMetaData[] modules) {
	    super(context, layoutId, modules);
	    this.layoutId = layoutId;
	    this.context = context;
	    this.modules = Arrays.asList(modules);
	}

	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	    LayoutInflater inflater = (LayoutInflater) context
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View rowView = inflater.inflate(this.layoutId, parent, false);
	    TextView textView = (TextView) rowView.findViewById(R.id.courseListItemTextLine);
	    ImageView imageView = (ImageView) rowView.findViewById(R.id.courseListItemIcon);
	    ExamModuleMetaData emd = modules.get(position);
	    textView.setText(moduleIdNameMap.get(emd.getModuleId()));
	    if(emd.getCourseType()==1) {
	    	imageView.setImageResource(R.drawable.icon_exam);
	    } else if(emd.getCourseType()==2) {
	    	imageView.setImageResource(R.drawable.icon_book);
	    }
	    
	    
	    return rowView;
	  
	 } 

}
