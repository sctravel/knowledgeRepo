package com.apps.knowledgeRepo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.MenuItemCompat.OnActionExpandListener;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import com.apps.knowledagerepo.R;
import com.apps.knowledgeRepo.activityHelper.ExamDownloaderTask;

public class ModeSelectionActivity extends Activity {
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mode_selection);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // If your minSdkVersion is 11 or higher, instead use:
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        final Button buttonExamMode = (Button) findViewById(R.id.examMode);
        buttonExamMode.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	beginExam(v);
            }
        });
        
        final Button buttonPracticeMode = (Button) findViewById(R.id.practiceMode);
        buttonPracticeMode.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	beginPractice(v);
            }
        });
        
        
        final Button buttonDownloadTests = (Button) findViewById(R.id.downloadTestButton);
        buttonDownloadTests.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	downloadTests();
            }
        });
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.exam_mode_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        // Configure the search info and add any event listeners
        
       // MenuItem shareItem = menu.findItem(R.id.action_share);
       // mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
       // mShareActionProvider.setShareIntent(getDefaultIntent());
        
     // When using the support library, the setOnActionExpandListener() method is
        // static and accepts the MenuItem object as an argument
        MenuItemCompat.setOnActionExpandListener(searchItem, new OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Do something when collapsed
                return true;  // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Do something when expanded
                return true;  // Return true to expand action view
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_search:
                openSearch();
                return true;
            case R.id.action_settings:
                openSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    
   private Intent getDefaultIntent() {
       Intent intent = new Intent(Intent.ACTION_SEND);
       intent.setType("image/*");
       return intent;
   }
   
    private void openSearch(){
    	
    }
    
    private void openSettings(){
    	
    }
    
    
    
    /** Called when the user clicks the Send button */
    public void beginExam(View view) {
        Intent intent = new Intent(this, ExamModeActivity.class);
        //EditText editText = (EditText) findViewById(R.id.edit_message);
        //String message = editText.getText().toString();
        //intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
    public void beginPractice(View view) {
        Intent intent = new Intent(this, PracticeModeActivity.class);
        //EditText editText = (EditText) findViewById(R.id.edit_message);
        //String message = editText.getText().toString();
        //intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
    
    public void downloadTests() {
    	
    	String samplePath ="http://farm1.static.flickr.com/114/298125983_0e4bf66782_b.jpg";
    	
    	String localFileName = Environment.getExternalStorageDirectory().getAbsolutePath() +"sample.jpg";
    			  	
    	new ExamDownloaderTask().execute(new String[]{samplePath, localFileName});
      
  
    	
    }
}
