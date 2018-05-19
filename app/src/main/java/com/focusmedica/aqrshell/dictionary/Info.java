package com.focusmedica.aqrshell.dictionary;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.focusmedica.aqrshell.R;

import java.util.ArrayList;

public class Info extends Activity {
    ImageView info,fb,twitter,focusweb,linkedin,thumbnailimageview;
    TextView tvInfo;
    private static StringBuilder appVersion;
    Mydatabase handler;
    DIctionaryContent content;
    ArrayList<DIctionaryContent> AppDetails=new ArrayList<>();

    String thumb;
    String imageInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Intent i=getIntent();
        thumb=i.getStringExtra("thumbinfo");
        imageInfo=i.getStringExtra("imageInfo");

        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        thumbnailimageview=(ImageView)findViewById(R.id.imageView5) ;
        fb = (ImageView) findViewById(R.id.imageView6);
        twitter = (ImageView) findViewById(R.id.imageView7);
        focusweb = (ImageView) findViewById(R.id.imageView8);
        linkedin = (ImageView) findViewById(R.id.imageView9);
        tvInfo=(TextView)findViewById(R.id.tvInfo);

        ActionBar mActionBar = getActionBar();
        mActionBar.show();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
        info= (ImageView) mCustomView.findViewById(R.id.imageView4);
        ImageView  iv_download=(ImageView)mCustomView. findViewById(R.id.iv_download);
        iv_download.setVisibility(View.INVISIBLE);
        Glide.with(Info.this).load(imageInfo).into(thumbnailimageview);
        Log.d("Thumbnail",imageInfo+"_thumbnail.png");
        info.setVisibility(View.INVISIBLE);
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);

        tvInfo.setText(thumb);

        Button feedTextView = (Button) findViewById(R.id.button);
        feedTextView.setText(R.string.text_feedback);

        try {
            PackageInfo appInfo = getPackageManager().getPackageInfo(getPackageName(),0);
            appVersion = new StringBuilder(appInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/FocusMedicaApps/"));
                startActivity(intent);
            }
        });

        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/focusmedica"));
                startActivity(intent);
            }
        });

        focusweb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/results?q=focus+medica"));
                startActivity(intent);
            }
        });

        linkedin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/company/focus-medica-india-pvt-ltd?trk=biz-companies-cym"));
                startActivity(intent);
            }
        });
    }

    public void feedBack(View view) {
        StringBuilder devBrand = new StringBuilder(android.os.Build.BRAND);
        StringBuilder devOEM = new StringBuilder(android.os.Build.MANUFACTURER);
        StringBuilder devModel = new StringBuilder(android.os.Build.MODEL);
        StringBuilder deviceInfo;
        if(devBrand.toString().equals(devOEM.toString())) {
            deviceInfo = new StringBuilder(devBrand.toString()+" "+devModel.toString());
        }else {
            deviceInfo = new StringBuilder(devBrand.toString()+" "+devOEM.toString()+" "+devModel.toString());
        }
        StringBuilder osVersion = new StringBuilder(""+android.os.Build.VERSION.RELEASE);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.text_feedback_mail_id)});
        intent.putExtra(Intent.EXTRA_SUBJECT, ("Feedback : " + getString(R.string.app_name) + " (Google Play)"));
        intent.putExtra(Intent.EXTRA_TEXT, ("App Version : " + appVersion.toString() + "\nDevice : " + deviceInfo.toString() + "\nAndroid : " + osVersion.toString() + "\n<Insert feedback below>\n\n"));
        startActivity(Intent.createChooser(intent, "Feedback"));
    }
}
