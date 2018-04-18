package com.focusmedica.aqrshell;

import android.app.ActionBar;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;



public class Transcript extends Activity {
    WebView webView;
    boolean isHome=true;
String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transcript);

        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        Bundle b=getIntent().getExtras();
        if (b!=null){
            url=b.getString("url");
           // titleName=b.getString("titleName");

        }
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayOptions(actionBar.getDisplayOptions()| ActionBar.DISPLAY_SHOW_CUSTOM);
        ImageView imageView = new ImageView(actionBar.getThemedContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        imageView.setImageResource(R.drawable.fmlogo);
        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT, Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
        imageView.setLayoutParams(layoutParams);
        actionBar.setCustomView(imageView);
        webView=(WebView)findViewById(R.id.webView);
        webView.loadUrl(url+"/transcript.html");
        Log.d("TAG",url+"/transcript.html");
        this.webView.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                isHome=false;
                view.loadUrl(url);
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(isHome==false) {
            isHome=true;
            webView.loadUrl(url+"/transcript.html");
        }
        else{
            finish();
        }
    }
}
