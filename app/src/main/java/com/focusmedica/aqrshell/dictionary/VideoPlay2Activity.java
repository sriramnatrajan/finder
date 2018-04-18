package com.focusmedica.aqrshell.dictionary;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

import com.focusmedica.aqrshell.R;

import java.io.File;
import java.util.ArrayList;

public class VideoPlay2Activity extends Activity {
    VideoView videoview;
    String vdoname, lowercase;
    Uri uri;
    Mydatabase mDMydatabase;

    ArrayList<DIctionaryContent> AppDetails = new ArrayList<>();
    DIctionaryContent content;
    int chapterid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.videoplay2activity);


        videoview = (VideoView) findViewById(R.id.videoView);
        vdoname = getIntent().getStringExtra("vdoname");

        mDMydatabase = new Mydatabase(getApplicationContext(), "");
        AppDetails = mDMydatabase.getAppDetail();
        content = AppDetails.get(0);
        chapterid = content.getID();


        String lower_case = vdoname.toLowerCase().substring(0, vdoname.indexOf(".")).replaceAll(" ", "").replaceAll("-", "");
        try {
            MediaController mediacontroller = new MediaController(VideoPlay2Activity.this);
            mediacontroller.setAnchorView(videoview);
            if (new File("data/data/com.focusmedica.aqrshell/files/" + vdoname).exists()) {
                uri = Uri.parse("data/data/com.focusmedica.aqrshell/files/" + vdoname);
            } else {
                uri = Uri.parse("android.resource://" + getPackageName() + "/raw/" + lower_case);
            }
            videoview.setMediaController(mediacontroller);
            videoview.setVideoURI(uri);
            videoview.requestFocus();
            videoview.start();
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            // Close the progress bar and play the video
            public void onPrepared(MediaPlayer mp) {
                videoview.start();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}

