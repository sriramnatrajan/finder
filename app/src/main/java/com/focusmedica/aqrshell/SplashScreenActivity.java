package com.focusmedica.aqrshell;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.focusmedica.aqrshell.utils.Activities.LogActivity;
import com.focusmedica.aqrshell.utils.ImageLoader;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;


public class SplashScreenActivity extends Activity {
    String url="http://192.168.1.42/TESTING/udshelltesting/home/home.dll";
  //  String url="http://focusmedica.in/aqr-shell/demo/home/home.dll";
    private static File videoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_in_right);
        RelativeLayout relate=(RelativeLayout)findViewById(R.id.splash);

        File file = new File("/data/data/com.focusmedica.aqrshell/databases");
        if(!file.exists()){
            file.mkdirs();
        }

        switch(this.getResources().getConfiguration().orientation) {

            case Configuration.ORIENTATION_PORTRAIT		:
                relate.setBackgroundDrawable(ImageLoader.loadDrawable(this, R.drawable.splash_xp));
                break;

            case Configuration.ORIENTATION_LANDSCAPE	:
                 relate.setBackgroundDrawable(ImageLoader.loadDrawable(this,R.drawable.splash_xl));
                break;

            default	:
                break;
        }

        relate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File file = new File("/data/data/com.focusmedica.aqrshell/databases/home.dll");
                if (file.exists()) {
                    Intent intent = new Intent(getApplicationContext(), LogActivity.class);
                    startActivity(intent);
                } else {
                    if (!haveNetworkConnection()) {
                        Toast.makeText(SplashScreenActivity.this, "Internet connection not available", Toast.LENGTH_LONG).show();
                    } else {
                        new DownloadFileFromURL().execute(url);
                    }
                }
            }
        });
    }


    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
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

    class DownloadFileFromURL extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(SplashScreenActivity.this, "Loading....", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.setRequestProperty("Accept-Encoding", "identity");
                conection.connect();
                //this will be useful so that you can show a typical 0-100% progress bar
                int lengthOfFile = conection.getContentLength();

                InputStream input = new BufferedInputStream(url.openStream());
                // Output stream
                OutputStream output = new FileOutputStream("/data/data/com.focusmedica.aqrshell/databases/home.dll");

                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress("" + (int) ((total * 100) / lengthOfFile));
                    output.write(data, 0, count);

                }
                output.flush();
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            File file = new File("/data/data/com.focusmedica.aqrshell/databases/home.dll");
            if (file.exists()) {
                Intent intent = new Intent(getApplicationContext(), LogActivity.class);
                startActivity(intent);
                finish();
            }else{
                Toast.makeText(SplashScreenActivity.this, "Some Error occured Try again later", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
