package com.apps.knowledgeRepo;

import com.apps.knowledagerepo.R;
import com.apps.knowledagerepo.R.layout;
import com.apps.knowledagerepo.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;

@SuppressLint("NewApi")
public class DisplayMessageActivity extends ActionBarActivity {

	@Override
    protected void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);

		    // Get the message from the intent
		    Intent intent = getIntent();
		    //String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

		    // Create the text view
		    TextView textView = new TextView(this);
		    textView.setTextSize(40);
		    textView.setText("AAA");

		    // Set the text view as the activity layout
		    setContentView(textView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    @SuppressLint("NewApi")
	public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() { }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                  Bundle savedInstanceState) {
              View rootView = inflater.inflate(R.layout.activity_display_message,
                      container, false);
              return rootView;
        }
    }

}
