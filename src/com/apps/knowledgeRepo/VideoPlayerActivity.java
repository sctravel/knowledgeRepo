package com.apps.knowledgeRepo;

import java.io.IOException;

import com.apps.knowledagerepo.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.DisplayMetrics;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
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

    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //int mPos=savedInstanceState.getInt("pos");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,  
             WindowManager.LayoutParams.FLAG_FULLSCREEN);
   		setContentView(R.layout.video_player);
   		
		//getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		// If your minSdkVersion is 11 or higher, instead use:
   		String vidAddress = "http://p.demo.flowplayer.netdna-cdn.com/vod/demo.flowplayer/bbb-800.mp4"; // your URL here
   		Uri vidUri = Uri.parse(vidAddress);
   		vidView = (VideoView)findViewById(R.id.CourseVideoView);
   		/*wifiLock = ((WifiManager) getSystemService(Context.WIFI_SERVICE))
   			    .createWifiLock(WifiManager.WIFI_MODE_FULL, "mylock");

   		wifiLock.acquire();*/
   		vidView.setVideoURI(vidUri);
   		dm = new DisplayMetrics(); 
   		this.getWindowManager().getDefaultDisplay().getMetrics(dm); 
   		int height = dm.heightPixels; int width = dm.widthPixels; 
   		vidView.setMinimumWidth(width); 
   		vidView.setMinimumHeight(height);
   		MediaController vidControl = new MediaController(this);
   		vidControl.setAnchorView(vidView);
   		vidView.setMediaController(vidControl);
   		vidView.requestFocus();
   		vidView.start();
   		//vidView.seekTo(mPos);
   		/*
   		mMediaPlayer = new MediaPlayer();
   		//mMediaPlayer.set .setAudioStreamType(AudioManager .STREAM_MUSIC);
   		mMediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
   	    
   		try {
   			mMediaPlayer.setDataSource(vidAddress);
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.prepareAsync(); // prepare async to not block main thread
            mMediaPlayer.start();
		} catch ( IllegalArgumentException e) {
			e.printStackTrace();
		} catch ( SecurityException e ) {
			e.printStackTrace();
		} catch ( IllegalStateException e ) {
			e.printStackTrace();
		} catch ( IOException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
   		*/
	
       
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
			int currentPosition = vidView.getCurrentPosition();
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
