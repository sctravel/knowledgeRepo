package com.apps.knowledgeRepo.activityHelper;
import java.util.Arrays;
import java.util.List;
import com.apps.knowledgeRepo.R;
import com.apps.knowledgeRepo.dataModel.VideoModule;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
public class VideoModuleArrayAdapter extends ArrayAdapter<VideoModule> {

	  private final Context context;
	  private final List<VideoModule> modules;
	  private final int layoutId;
	  
	  public VideoModuleArrayAdapter(Context context, int layoutId, List<VideoModule> modules){
		  super(context, layoutId, modules);
		  this.layoutId = layoutId;
	      this.context = context;
	      this.modules = modules;		  
	  }
	  public VideoModuleArrayAdapter(Context context, int layoutId, VideoModule[] modules) {
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
	    textView.setText(modules.get(position).getTitle());
	    imageView.setImageResource(R.drawable.icon_video);
	    
	    return rowView;
	  
	 } 
}
