package com.focusmedica.aqrshell;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

/**
 * Created by Lokesh on 01-Jun-16.
 */
public class PlayActivity extends Activity {
    VideoView videoView;
    int value,j;
    String videoName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        videoView=(VideoView)findViewById(R.id.videoView);
        videoName=getIntent().getExtras().getString("title");
       // value=getIntent().getExtras().getInt("secId");
        //j=getIntent().getExtras().getInt("videoId");
        getActionBar().hide();
        //Creating MediaController
        MediaController mediaController= new MediaController(this);
        mediaController.setAnchorView(videoView);
        //specify the location of media file
        Uri uri= Uri.parse(videoName);
        Log.i("abhay","/"+videoName);
        //Setting MediaController and URI, then starting the videoView
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(uri);
        videoView.requestFocus();
        videoView.start();

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            public void onCompletion(MediaPlayer mp) {
                finish();
            }
        });
    }
}
