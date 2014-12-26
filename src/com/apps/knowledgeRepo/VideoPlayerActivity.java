package com.apps.knowledgeRepo;

import com.apps.knowledagerepo.R;
import com.apps.knowledgeRepo.dataModel.VideoModule;
import com.apps.knowledgeRepo.om.Constants;

import android.app.Activity;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.net.wifi.WifiManager.WifiLock;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

// android:theme="@android:style/Theme.Translucent.NoTitleBar.Fullscreen"

public class VideoPlayerActivity extends Activity implements MediaPlayer.OnPreparedListener {
	
	//MediaPlayer mMediaPlayer = null;
	VideoView vidView = null;
	DisplayMetrics dm = null;
	SurfaceView surView =  null;
	MediaController mediaController = null;
	
    WifiLock wifiLock = null; //commented out for VideoView now
    int lastOrientation = 0;

    VideoModule module = null;
    
    int currentSequenceNumber = 0;
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //int mPos=savedInstanceState.getInt("pos");
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(
       //     WindowManager.LayoutParams.FLAG_FULLSCREEN,  
        //     WindowManager.LayoutParams.FLAG_FULLSCREEN);
   		setContentView(R.layout.video_player);
   		        
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
        	 module = (VideoModule) extras.get(Constants.VIDEO_MODULE_NAME);	    
        }
        if(module==null) throw new RuntimeException("VideoModule is null!");
        if(module.getLessons().isEmpty()) {
			Toast.makeText(getApplicationContext(), "This Video Module is Empty! ", Toast.LENGTH_LONG).show();
			finish();
        }
		//getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		// If your minSdkVersion is 11 or higher, instead use:
        initializeVideoPlayer();
        setTitle(module.getTitle());

        playVideo(currentSequenceNumber);
        
   		Button next = (Button) findViewById(R.id.nextVideoButton);
   		next.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(currentSequenceNumber<module.getLessons().size()-1) {
					++currentSequenceNumber;
				} else {
					currentSequenceNumber=0;
				}
				playVideo(currentSequenceNumber);
			}
		});
   		Button prev = (Button) findViewById(R.id.prevVideoButton);
   		prev.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(currentSequenceNumber>0 ) {
					--currentSequenceNumber;
				} else {
					currentSequenceNumber=module.getLessons().size()-1;
				}
				playVideo(currentSequenceNumber);
			}
		});
       
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.exam_mode_menu, menu);
		return true;
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
	public void initializeVideoPlayer() {
		vidView = (VideoView)findViewById(R.id.CourseVideoView);
   		/*wifiLock = ((WifiManager) getSystemService(Context.WIFI_SERVICE))
   			    .createWifiLock(WifiManager.WIFI_MODE_FULL, "mylock");

   		wifiLock.acquire();*/
   		dm = new DisplayMetrics(); 
   		this.getWindowManager().getDefaultDisplay().getMetrics(dm); 
   		int height = dm.heightPixels; 
   		int width = dm.widthPixels; 
   		vidView.setMinimumWidth(width); 
   		vidView.setMinimumHeight(height);
   		MediaController vidControl = new MediaController(this);
   		vidControl.setAnchorView(vidView);
   		vidView.setMediaController(vidControl);
   		vidView.requestFocus();
	}
	public void playVideo(int currentSequenceNumber) {
		TextView title = (TextView) findViewById(R.id.videoTitle);
		title.setText( "Video Sequence: "+(currentSequenceNumber+1) +"/"+ module.getLessons().size() ); 
		
		String vidAddress = module.getLessons().get(currentSequenceNumber).getURL().trim(); 
		//vidAddress="https://www.youtube.com/watch?v=JDGrr07rP88";
		//"http://p.demo.flowplayer.netdna-cdn.com/vod/demo.flowplayer/bbb-800.mp4"; // your URL here
   		Log.d("VideoURL", vidAddress);
		Uri vidUri = Uri.parse(vidAddress);
		//vidView.setVideoPath("android.resource://"+getPackageName()+"/"+R.raw.s63video02);
   		vidView.setVideoURI(vidUri);
   		vidView.start();
	}
	/*To move the MediaController over the video, 
	 * you can place an empty anchor view 88 pixels above the bottom line of the VideoView.
	 *  When attached to this anchor, the MediaController appears at the desired position.
	 * (non-Javadoc)
	 * @see android.app.Activity#onStop()
	 */
	
	@Override
	protected void onStop() {
		super.onStop();
		if(vidView !=  null ) {
			vidView.stopPlayback();
			vidView = null;
		}
		//wifiLock.release();.
	}
	@Override
	protected void onSaveInstanceState (Bundle outState){
        super.onSaveInstanceState(outState);
	    outState.putInt("pos", vidView.getCurrentPosition()); // save it here
	    vidView.pause();
	} 
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	    if (lastOrientation != newConfig.orientation) {
	        lastOrientation = newConfig.orientation;
	        // redraw your controls here
	    } 
	}
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
	    super.onRestoreInstanceState(savedInstanceState);
	    //we use onRestoreInstanceState in order to play the video playback from the stored position 
	    int lastPosition = savedInstanceState.getInt("pos");
	    vidView.seekTo(lastPosition);
	}

	/** Called when MediaPlayer is ready */
    public void onPrepared(MediaPlayer player) {
       // player.start();
    }
}
