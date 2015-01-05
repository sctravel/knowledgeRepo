package com.apps.knowledgeRepo.activityHelper;
import java.util.Arrays;
import java.util.List;
import com.apps.knowledgeRepo.R;
import com.apps.knowledgeRepo.dataModel.FlashCardBucket;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("ViewHolder")
public class FlashCardBucketArrayAdapter extends ArrayAdapter<FlashCardBucket> {

	  private final Context context;
	  private final List<FlashCardBucket> buckets;
	  private final int layoutId;
	  
	  public FlashCardBucketArrayAdapter(Context context, int layoutId, List<FlashCardBucket> buckets){
		  super(context, layoutId, buckets);
		  this.layoutId = layoutId;
	      this.context = context;
	      this.buckets = buckets;		  
	  }
	  public FlashCardBucketArrayAdapter(Context context, int layoutId, FlashCardBucket[] buckets) {
	      super(context, layoutId, buckets);
	      this.layoutId = layoutId;
	      this.context = context;
	      this.buckets = Arrays.asList(buckets);
	  }

	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    LayoutInflater inflater = (LayoutInflater) context
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View rowView = inflater.inflate(this.layoutId, parent, false);
	    TextView textView = (TextView) rowView.findViewById(R.id.courseListItemTextLine);
	    ImageView imageView = (ImageView) rowView.findViewById(R.id.courseListItemIcon);
	    textView.setText(buckets.get(position).getTitle());
	    imageView.setImageResource(R.drawable.icon_flash_card);
	    
	    return rowView;
	  
	 } 
}
