package com.apps.knowledgeRepo.activityHelper;

import java.util.Arrays;
import java.util.List;
import com.apps.knowledgeRepo.R;
import com.apps.knowledgeRepo.dataModel.ExamMetaData;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TextExamArrayAdapter extends ArrayAdapter<ExamMetaData>{
	private final Context context;
	private final List<ExamMetaData> exams;
	private final int layoutId;
	  
	public TextExamArrayAdapter(Context context, int layoutId, List<ExamMetaData> exams){
		super(context, layoutId, exams);
		this.layoutId = layoutId;
	    this.context = context;
	    this.exams = exams;		  
	}
	public TextExamArrayAdapter(Context context, int layoutId, ExamMetaData[] exams) {
	    super(context, layoutId, exams);
	    this.layoutId = layoutId;
	    this.context = context;
	    this.exams = Arrays.asList(exams);
	}

	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	    LayoutInflater inflater = (LayoutInflater) context
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View rowView = inflater.inflate(this.layoutId, parent, false);
	    TextView textView = (TextView) rowView.findViewById(R.id.courseListItemTextLine);
	    ImageView imageView = (ImageView) rowView.findViewById(R.id.courseListItemIcon);
	    ExamMetaData emd = exams.get(position);
	    textView.setText(emd.getExamName());
	    if(emd.getCourseType()==1) {
	    	imageView.setImageResource(R.drawable.icon_exam);
	    } else if(emd.getCourseType()==2) {
	    	imageView.setImageResource(R.drawable.icon_book);
	    }
	    
	    
	    return rowView;
	  
	 } 
}
