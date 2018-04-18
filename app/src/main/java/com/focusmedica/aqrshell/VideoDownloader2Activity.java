package com.focusmedica.aqrshell;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

//import android.app.Dialog;
//import android.content.SharedPreferences;

public class VideoDownloader2Activity extends Activity {



    // Progress Dialog
    private ProgressDialog pDialog;

    //object that holds the activity's context
    private static Context context;

    // Name of the downloaded file at /data/data/com.focusmedica.ud_en_acne/files
    private static StringBuilder downloadedFile = new StringBuilder(256);

    // Path to the downloaded file at /data/data/com.focusmedica.ud_en_acne/files
    private static StringBuilder videoPath = new StringBuilder(256);

    //object that provides access to the Async Task child
    private DownloadFileFromURL asyncObj;

    // File Object that represents the downloaded file
    private static File videoFile;

    private RelativeLayout downloadLayout;
    String vdoname,language,titleName,titleNameCap,url1;
    int flag = 0;

    private static Context getAppContext() {

        return VideoDownloader2Activity.context;
    }

    //String[] mLanguage={"English","Hindi"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_downloader2_activity);
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayOptions(actionBar.getDisplayOptions() | ActionBar.DISPLAY_SHOW_CUSTOM);
        ImageView imageView = new ImageView(actionBar.getThemedContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        imageView.setImageResource(R.drawable.fmlogo);
        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT, Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        imageView.setLayoutParams(layoutParams);
        actionBar.setCustomView(imageView);

        /**
         * Show Progress bar click event
         */

        context = getApplicationContext();
        vdoname=getIntent().getStringExtra("vdoname");
        language=getIntent().getStringExtra("language");
        titleName=getIntent().getStringExtra("titleName");
        url1=getIntent().getStringExtra("url");
        /*String titleNamelower=titleName.toString().replaceAll(" ","_");
        titleNamelower=titleNamelower.toLowerCase();
        titleNameCap = titleNamelower.substring(0, 1).toUpperCase() + titleNamelower.substring(1);*/
        titleNameCap=titleName.toString().replaceAll(" ","_");
        System.out.println("something"+vdoname);


        downloadLayout = (RelativeLayout) findViewById(R.id.downloadLayout);
        asyncObj =new DownloadFileFromURL();
        String url =vdoname.toString().replaceAll(" ","%20");
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Downloading requested video. Please wait...");
        pDialog.setMax(100);
        pDialog.setIndeterminate(false);
        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                flag = 1;
                Log.d("ashu",flag+"flag");
                asyncObj.cancel(true);
                File file = new File("data/data/com.focusmedica.aqrshell/files/"+titleName+"/"+language+"/" + vdoname);
                if (file.exists()) {
                    file.delete();
                }
                Toast.makeText(getBaseContext(), "Download Cancelled", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);
        //calls execute() of custom AsyncTask
       // asyncObj = (DownloadFileFromURL) new DownloadFileFromURL().execute(URL + url);
        /*for(int i=0;i<mLanguage.length;i++) {
            if(language.equalsIgnoreCase(mLanguage[i])) {
                String orig="http://pantherpublishers.com/kriticalhealth/aqr/titles/Diabetesanditsmanagement/English/Videos/Diabetic%20Nephropathy.mp4";
                String getting="http://pantherpublishers.com/kriticalhealth/aqr/titles/"+titleName+"/" + mLanguage[i] + "/" + "Videos/" + url;
                Log.d("Archu","urlgett="+getting);
                asyncObj.execute("http://pantherpublishers.com/kriticalhealth/aqr/titles/"+titleNameCap+"/" + mLanguage[i] + "/" + "Videos/" + url);
                break;
            }
        }*/
        titleNameCap = language.substring(0, 1).toUpperCase() + language.substring(1);
        String getting=url1 + titleNameCap+ "/" + "Videos/" + url;
        Log.d("Archu","urlgett="+getting);
        asyncObj.execute(url1 + titleNameCap + "/" + "Videos/" + url);

    }

    private void unbindDrawables(View view) {
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup && !(view instanceof AdapterView)) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }

    }

    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        private long total = 0L;
        private long lengthOfFile = 0L;



        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.show();
        }

        /**
         * Custom method to handle download cancellations
         */
        /*private void onDownloadCancelled() {

            File file = new File("data/data/com.pantherpublishers.aqr.sparsh/files/"+language+"/" + vdoname + ".mp4");
            if (file.exists()) {
                file.delete();
            }
            finish();
        }*/

        /**
         * Called when the task cancelled ie. cancel(true) is called
         */
        /*@Override
        protected void onCancelled() {
            onDownloadCancelled();
        }*/

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.setRequestProperty("Accept-Encoding", "identity");
                conection.connect();
                //this will be useful so that you can show a typical 0-100% progress bar
                lengthOfFile = conection.getContentLength();

                InputStream input = new BufferedInputStream(url.openStream());
                // Output stream
                OutputStream output = new FileOutputStream("data/data/com.focusmedica.aqrshell/files/"+titleName+"/"+language+"/"+vdoname);

                byte data[] = new byte[1024];

                while ((count = input.read(data)) != -1 && flag==0) {

                    if (flag == 1) {
                        break;
                    }
                    if (flag == 0) {
                        total += count;
                        publishProgress("" + (int) ((total * 100) / lengthOfFile));
                        output.write(data, 0, count);
                    }

                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage

            //pDialog.setMax((int) Math.ceil(Float.parseFloat(setDownloadFileSize(lengthOfFile, 1048576))));
            //pDialog.setMax(100);
            //pDialog.setProgressNumberFormat("%1d MB/%2d MB");
            //pDialog.setProgress(Integer.parseInt(progress[0]));
            int prog = Integer.parseInt(progress[0]);
            pDialog.setProgress(prog);
            if (prog == 100) {
                Toast.makeText(getBaseContext(), "Download completed", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPostExecute(String file_url) {

            if (flag==0) {

                // dismiss the dialog after the file was downloaded
                pDialog.dismiss();

                //playIconSwitcher();

            	/*
            	 * Post download the video is passed on for playback
            	 */

                Intent intent = new Intent(VideoDownloader2Activity.this,MediaPlayerActivity.class);
                intent.putExtra("vdoname",vdoname);
                intent.putExtra("language", language);
                intent.putExtra("titleName",titleName);
                startActivity(intent);

                //destroy activity on finishing
                finish();
            }
            else {

                File file = new File("data/data/com.focusmedica.aqrshell/files/"+titleName+"/"+language+"/" + vdoname);
                if (file.exists()) {
                    file.delete();
                }
                finish();
                Log.d("ashu", vdoname + "else");
            }

        }

    }


    /** @Override
    protected void onDestroy(){

    if (asyncObj != null && asyncObj.getStatus() != AsyncTask.Status.FINISHED)
    asyncObj.cancel(true);

    unbindDrawables(downloadLayout);
    super.onDestroy();
    }**/

    /*@Override
    public void onBackPressed() {

    	Intent intent = new Intent (this, VideoActivity.class);
		startActivity(intent);
		finish();

    }*/
}
