package com.apps.knowledgeRepo.activityHelper;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.util.ByteArrayBuffer;

import android.os.AsyncTask;
import android.util.Log;

public class ExamDownloaderTask extends AsyncTask<String, Void, Boolean>{
		     	    
        public boolean DownloadFromUrl(String fileURL, String fileName) {  //this is the downloader method
                try {
                        URL url = new URL(fileURL); 
                        File file = new File(fileName);
 
                        long startTime = System.currentTimeMillis();
                        Log.d("exam downloader", "download begining");
                        Log.d("exam downloader", "download url:" + url);
                        Log.d("exam downloader", "downloaded file name:" + fileName);
                        /* Open a connection to that URL. */
                        URLConnection ucon = url.openConnection();
 
                        /*
                         * Define InputStreams to read from the URLConnection.
                         */
                        InputStream is = ucon.getInputStream();
                        BufferedInputStream bis = new BufferedInputStream(is);
 
                        /*
                         * Read bytes to the Buffer until there is nothing more to read(-1).
                         */
                        ByteArrayBuffer baf = new ByteArrayBuffer(50);
                        int current = 0;
                        while ((current = bis.read()) != -1) {
                                baf.append((byte) current);
                        }
 
                        /* Convert the Bytes read to a String. */
                        FileOutputStream fos = new FileOutputStream(file);
                        fos.write(baf.toByteArray());
                        fos.close();
                        Log.d("exam downloader", "download ready in"
                                        + ((System.currentTimeMillis() - startTime) / 1000)
                                        + " sec");
                        
                        return true;
 
                } catch (IOException e) {
                        Log.d("ImageManager", "Error: " + e);
                }
                
                return false;
 
        }

		@Override
		protected Boolean doInBackground(String... urls) {
			// TODO Auto-generated method stub
			return DownloadFromUrl(urls[0], urls[1]);
		}
		
		
	

}
