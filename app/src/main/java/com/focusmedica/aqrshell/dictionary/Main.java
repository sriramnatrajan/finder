package com.focusmedica.aqrshell.dictionary;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.focusmedica.aqrshell.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

public class Main extends Activity implements  Sorting.OnHeadlineSelectedListener{
    
    ImageView buy;

    static final String TAG = "FMI";

    static String SKU_PREMIUM;
    static final int RC_REQUEST = 10001;

    String thumb;
    Mydatabase handler;
    DIctionaryContent content;
    String base64EncodedPublicKey;
    ArrayList<DIctionaryContent> AppDetails=new ArrayList<>();
    TextView mTextView;
    ImageView iv_download;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mTextView=(TextView)findViewById(R.id.title);
        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        Intent i=getIntent();
          thumb=i.getStringExtra("thumbinfo");
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        ActionBar mActionBar = getActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
        ImageView info=(ImageView)mCustomView.findViewById(R.id.imageView4);
        iv_download=(ImageView)mCustomView.findViewById(R.id.iv_download);
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);

        handler=new Mydatabase(getApplicationContext(), "");
        AppDetails=handler.getAppDetail();
        content=AppDetails.get(0);
        mTextView.setText(content.getAppNme());

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getApplicationContext(), Info.class);
                in.putExtra("thumbinfo",thumb);
                startActivity(in);
            }
        });
        iv_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Content c=new Content();
                
            }
        });

    }

    @Override
    public void onArticleSelected(String firstcharacter) {
        Content contfrag = (Content) getFragmentManager().findFragmentById(R.id.content);
        contfrag.test(firstcharacter);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);

    }

    void complain(String message) {
        Log.e(TAG, "**** TrivialDrive Error: " + message);
        alert("Error: " + message);
    }

    void alert(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(this);
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        Log.d(TAG, "Showing alert dialog: " + message);
        bld.create().show();
    }
}
