package com.focusmedica.aqrshell;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

/*import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;*/


public class VideoChecker2Activity extends Activity {


    // Object that holds the activity's context
    private static Context context;

    // File Object that represents the downloaded file
    private static File videoFile;
    String vdoname,language,titleName,url;


    /**
     * Method that returns the Context of the activity to frame the path to downloaded file.
     *
     */
    private static Context getAppContext() {

        return VideoChecker2Activity.context;
    }


    private static String stringOptimus(String s, int i) {
        char[] chars = s.toCharArray();
        for(int j = 0; j<chars.length; j++)
            chars[j] = (char)(chars[j] ^ i);
        return String.valueOf(chars);
    }

    /**
     * Method to check if a data connection is available
     * @return
     */
    private boolean haveNetworkConnection() {

        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    /**
     * This is the first method to be executed when the Activity starts.
     *
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //contains the Context of the app
        context=getApplicationContext();
        url=getIntent().getStringExtra("url");
        vdoname= getIntent().getStringExtra("vdoname");
        language=getIntent().getStringExtra("language");
        titleName=getIntent().getStringExtra("titleName");
        StringBuilder videoPath = new StringBuilder(256);


        System.out.println("myvdoname"+vdoname);

        if(new File("data/data/com.focusmedica.aqrshell/files/"+titleName+"/"+language+"/"+ vdoname).exists()) { //checks if the video file exists
            Log.d("ashu",vdoname+"check");
            /*
    		 *   if the file exists it is sent for playback
    		 */
            Intent intent = new Intent(this, MediaPlayerActivity.class);
            intent.putExtra("vdoname",vdoname);
            intent.putExtra("language",language);
            intent.putExtra("titleName",titleName);
            startActivity(intent);
          //  this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
            finish();
        }
    	/*
		 *   if the file does not exist then it is downloaded
		 */

        else {

            //is there a working data connection? if not print Toast!
            if(!haveNetworkConnection()){

                Toast.makeText(this, "No internet connection available", Toast.LENGTH_LONG).show();
            }

            //proceed to video download.
            else{

                Intent intent = new Intent(this, VideoDownloader2Activity.class);
                intent.putExtra("vdoname",vdoname);
                intent.putExtra("language", language);
                intent.putExtra("titleName",titleName);
                intent.putExtra("url",url);
                startActivity(intent);
                //executes a slide action from right to left to show the next activity entering the screen
               // this.overridePendingTransition(R.anim.slide_in_right,R.anim.slide_in_left);
                finish();
            }
        }
        //destroy the activity after all operations are completed
        finish();
    }
}
