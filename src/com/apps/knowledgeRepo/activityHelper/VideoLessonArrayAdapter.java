package com.apps.knowledgeRepo.activityHelper;

import java.util.Arrays;
import java.util.List;
import com.apps.knowledagerepo.R;
import com.apps.knowledgeRepo.dataModel.VideoLesson;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

//This is not used !!!!
public class VideoLessonArrayAdapter extends ArrayAdapter<VideoLesson> {

	  private final Context context;
	  private final List<VideoLesson> lessons;
	  private final int layoutId;
	  
	  public VideoLessonArrayAdapter(Context context, int layoutId, List<VideoLesson> lessons){
		  super(context, layoutId, lessons);
		  this.layoutId = layoutId;
	      this.context = context;
	      this.lessons = lessons;		  
	  }
	  public VideoLessonArrayAdapter(Context context, int layoutId, VideoLesson[] lessons) {
	      super(context, layoutId, lessons);
	      this.layoutId = layoutId;
	      this.context = context;
	      this.lessons = Arrays.asList(lessons);
	  }

	  @SuppressLint("ViewHolder")
	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    LayoutInflater inflater = (LayoutInflater) context
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View rowView = inflater.inflate(this.layoutId, parent, false);
	    TextView textView = (TextView) rowView.findViewById(R.id.courseListItemTextLine);
	    ImageView imageView = (ImageView) rowView.findViewById(R.id.courseListItemIcon);
	    textView.setText(lessons.get(position).getSequence());
	    imageView.setImageResource(R.drawable.icon_video);
	    
	    return rowView;
	  
	 } 
}
