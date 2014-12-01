package com.apps.knowledgeRepo.activityHelper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.apps.knowledagerepo.R;
import com.apps.knowledgeRepo.dataModel.Course;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
public class CourseSelectionArrayAdapter extends ArrayAdapter<Course> {

	  private final Context context;
	  private final List<Course> courses;
	  private final int layoutId;
	  
	  public CourseSelectionArrayAdapter(Context context, int layoutId, List<Course> courses){
		  super(context, layoutId, courses);
		  this.layoutId = layoutId;
	      this.context = context;
	      this.courses = courses;		  
	  }
	  public CourseSelectionArrayAdapter(Context context, int layoutId, Course[] courses) {
	      super(context, layoutId, courses);
	      this.layoutId = layoutId;
	      this.context = context;
	      this.courses = Arrays.asList(courses);
	  }

	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    LayoutInflater inflater = (LayoutInflater) context
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View rowView = inflater.inflate(this.layoutId, parent, false);
	    TextView textView = (TextView) rowView.findViewById(R.id.courseListItemTextLine);
	    ImageView imageView = (ImageView) rowView.findViewById(R.id.courseListItemIcon);
	    textView.setText(courses.get(position).getCourseName());
	    // change the icon for Windows and iPhone
	    int courseType = (int) courses.get(position).getCourseType();
	    switch(courseType) {
	        case 1 : 
	            imageView.setImageResource(R.drawable.icon_exam);
	            break;
	        case 2 :
	        	imageView.setImageResource(R.drawable.icon_book);
	            break;
	        case 3 :
	        	imageView.setImageResource(R.drawable.icon_flash_card);
	            break;
	        case 4 :
	        	imageView.setImageResource(R.drawable.icon_video);
	            break;
	        default :
	        	imageView.setImageResource(R.drawable.icon_book);
	            
	    } 
	    
	    return rowView;
	  
	 } 
}
