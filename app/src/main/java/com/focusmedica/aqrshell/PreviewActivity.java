package com.focusmedica.aqrshell;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.focusmedica.aqrshell.dbHandler.SQLiteHandler;
import com.focusmedica.aqrshell.dictionary.Info;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class PreviewActivity extends Activity {
String url, titleName;
    ImageView transcript;
    ImageView thumbDownload,thumbplay;
    private ProgressDialog pDialog;
    int flag=0; ArrayList<DataModel> appList;
    File mFile;
    SQLiteHandler mSqLiteHandler;
    public static final int progress_bar_type = 0;
    String tn0; File fileDir;
    double screenInches;
    android.widget.ProgressBar spinner;   DataModel dataModel;
    String a0,a1,a2,a3;RelativeLayout mRelativeLayout;
    View mCustomView;  ImageView info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        mRelativeLayout=(RelativeLayout)findViewById(R.id.bg);
        thumbDownload=(ImageView)findViewById(R.id.thumbdownload);
        transcript=(ImageView)findViewById(R.id.transcriptbtn);
        ActionBar mActionBar = getActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);
        mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
        info=(ImageView)mCustomView.findViewById(R.id.imageView4);
        ImageView i=(ImageView)mCustomView.findViewById(R.id.iv_download) ;
        i.setVisibility(View.INVISIBLE);
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        double x = Math.pow(dm.widthPixels/dm.xdpi,2);
        double y = Math.pow(dm.heightPixels/dm.ydpi,2);
        screenInches= Math.sqrt(x+y);
        Log.d("debug","Screen inches : " + screenInches);
        appList=new ArrayList<>();
         mSqLiteHandler=new SQLiteHandler(this);
        Bundle b=getIntent().getExtras();
        if (b!=null){
          //  url=b.getString("url");
            titleName=b.getString("titleName");
            appList = mSqLiteHandler.getDetails(titleName);
            dataModel = appList.get(0);
            a0=dataModel.getName();
            a1=dataModel.getValue();
            a2=dataModel.getAppInfo();
            a3=dataModel.getApp_id();
        }
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(), Info.class);
                i .putExtra("thumbinfo",a2);
                i.putExtra("app Image",a1+"/"+a3+"_thumbnail.png");
                i.putExtra("APP NAME",a0);
                Log.d("TAG THUMBNAIL",a1+a3);
                startActivity(i);
            }
        });

     tn0=titleName.toLowerCase();
        if (screenInches<=5.571247911145661){
            Glide.with(this).load(a1+"/phone_brand.png").asBitmap().into(new SimpleTarget<Bitmap>(100, 100) {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    Drawable drawable = new BitmapDrawable(getApplicationContext().getResources(), resource);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        mRelativeLayout.setBackground(drawable);
                    }
                }
            });
            Log.d("TAGURL",a1+"/phone_brand.png");
            //spinner.setVisibility(View.GONE);

        }else {
            Glide.with(this).load(a1+"/phone_brand.png").asBitmap().into(new SimpleTarget<Bitmap>(200, 200) {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                Drawable drawable = new BitmapDrawable(getApplicationContext().getResources(), resource);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        mRelativeLayout.setBackground(drawable);
                    }
                }
            });
            Log.d("TAGURL",a1+"/phone_brand.png");
           // spinner.setVisibility(View.GONE);
        }

        mFile=new File(getApplicationContext().getFilesDir()+"/"+tn0+"/");
       if (!mFile.exists())
            mFile.mkdirs();
         fileDir=new File(mFile+"video.mp4");
         Log.d("TAG",fileDir.toString());
         if (fileDir.exists()){
         thumbDownload.setImageResource(R.drawable.ud_play_video_pressed);
         }else{
         thumbDownload.setImageResource(R.drawable.ud_download_pressed);
        }

        thumbDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
                    if (thumbDownload.getDrawable().getConstantState()==PreviewActivity.this.getResources().getDrawable(R.drawable.ud_download_pressed, getApplicationContext().getTheme()).getConstantState()){
                        if (!haveNetworkConnection()){
                            Toast.makeText(PreviewActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                        }else {
                            flag=0;
                            new DownloadFileFromURL().execute(a1+"/video.mp4");
                            Log.d("TAG",a1+"/video.mp4");
                        }
                    }else {
                        Intent i=new Intent(PreviewActivity.this,MediaPlayerActivity.class);
                        i.putExtra("title",fileDir.toString());
                        startActivity(i);
                    }
                } else {
                    if (thumbDownload.getDrawable().getConstantState()==PreviewActivity.this.getResources().getDrawable(R.drawable.ud_download_pressed).getConstantState()){
                        if (!haveNetworkConnection()){
                            Toast.makeText(PreviewActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                        }else {
                            flag=0;
                            new DownloadFileFromURL().execute(a1+"/video.mp4");
                            Log.d("TAG",a1+"/video.mp4");
                        }
                    }else {
                        Intent i=new Intent(PreviewActivity.this,MediaPlayerActivity.class);
                        i.putExtra("title",fileDir.toString());
                        startActivity(i);
                    }
                }


            }
        });

        transcript.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),Transcript.class);
                i.putExtra("url",a1);
                startActivity(i);
            }
        });


    }
    @TargetApi(Build.VERSION_CODES.M)
    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
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

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (fileDir.exists()){
            thumbDownload.setImageResource(R.drawable.ud_play_video_pressed);
        }else{
            thumbDownload.setImageResource(R.drawable.ud_download_pressed);
        }
    }

    public void Downloader(){
    }
    class DownloadFileFromURL extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(PreviewActivity.this);
            pDialog.setMessage("Download in progress...please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setMax(100);
            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pDialog.setCancelable(false);
            pDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    flag = 1;

                    File file = new File(mFile+"video.mp4");
                    if (file.exists()) {
                        file.delete();
                    }
                    new DownloadFileFromURL().cancel(true);
                    pDialog.dismiss();
                    Toast.makeText(PreviewActivity.this, "Download Cancelled", Toast.LENGTH_SHORT).show();
                }
            });
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {

                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.setRequestProperty("Accept-Encoding", "Identity");
                connection.connect();
                int lenghtOfFile = connection.getContentLength();
                InputStream input = new BufferedInputStream(url.openStream());
                if (flag != 1) {
                    OutputStream output = new FileOutputStream(mFile+"video.mp4");
                    Log.d("TAGH",mFile.toString());
                    byte data[] = new byte[1024];
                    long total = 0;
                    while ((count = input.read(data)) != -1 && flag == 0) {
                        if (flag == 1) {
                            break;
                        }
                        if (flag == 0) {
                            total += count;
                            publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                            output.write(data, 0, count);
                        }
                    }
                    output.flush();
                    output.close();
                    input.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            int prog = Integer.parseInt(progress[0]);
            pDialog.setProgress(prog);
            if (prog == 100) {
                Toast.makeText(PreviewActivity.this, "Download completed", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPostExecute(String file_url) {
         //  getApplicationContext().removeDialog(progress_bar_type);
            pDialog.dismiss();
            File file = new File(mFile +"video.mp4");
            if (file.exists()) {
             thumbDownload.setImageResource(R.drawable.ud_play_video_pressed);
            }else {
                thumbDownload.setImageResource(R.drawable.ud_download_pressed);

                }
            }
        }
    }

