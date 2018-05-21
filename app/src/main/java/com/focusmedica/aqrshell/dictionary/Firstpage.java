package com.focusmedica.aqrshell.dictionary;

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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.focusmedica.aqrshell.R;
import com.focusmedica.aqrshell.utils.Activities.ApplicationController;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class Firstpage extends Activity {
     String  a3,urlValue,url;
    private static File videoFile;
    ImageView ivLoading,brandPage;
  double screenInches;
  String b1,b2,b3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstpage);

        Intent i=getIntent();
        a3=i.getStringExtra("app_id");
        urlValue=i.getStringExtra("url");

        url=urlValue+"/"+a3+"_db.dll";
        b1=i.getStringExtra("titleName");
        b2=i.getStringExtra("app_id");
        b3=i.getStringExtra("url");
        Log.d("URL Values",b3+"  "+b2);

        RelativeLayout relate=(RelativeLayout)findViewById(R.id.apppic);
         brandPage=(ImageView)findViewById(R.id.brandPage);
        ivLoading = ((ImageView)findViewById(R.id.loding));
        switch(this.getResources().getConfiguration().orientation) {

            case Configuration.ORIENTATION_PORTRAIT		:
                if (screenInches<=5.571247911145661){
                    Glide.with( Firstpage.this ).load(urlValue+"/"+a3+"_phone_brand.png").into( brandPage );
                    Log.d("URL",urlValue+"/"+a3+"_tab_brand.png");
                }else {
                    Glide.with( Firstpage.this ).load(urlValue+"/"+a3+"_tab_brand.png").into( brandPage );
                    Log.d("URL",urlValue+"/"+a3+"_tab_brand.png");
                }
                break;

            case Configuration.ORIENTATION_LANDSCAPE	:
                Glide.with( Firstpage.this ).load(urlValue+"/"+a3+"_tab_landscape_brand.png").into( brandPage );
                Log.d("URL",urlValue+"/"+a3+"_tab_landscape_brand.png");
                break;

            default	:
                break;
        }

        File file = new File("/data/data/com.focusmedica.aqrshell/databases");
        if(!file.exists()){
            file.mkdirs();
        }else{

            ivLoading.setVisibility(View.GONE);
        }

        relate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File file = new File("/data/data/com.focusmedica.aqrshell/databases/"+a3+"_db.dll");
                ((ApplicationController)getApplicationContext()).setDataBaseName("/data/data/com.focusmedica.aqrshell/databases/"+a3+"_db.dll");

                if (file.exists()) {
                    Intent intent = new Intent(getApplicationContext(), Main.class);
                    intent.putExtra("thumbinfo",urlValue+"/"+a3);
                    intent.putExtra("b1_titleName",b1);
                    intent.putExtra("Thumbnail",b3+"/"+b2+"_thumbnail.png");
                    startActivity(intent);
                }else{
                    if (!haveNetworkConnection()) {
                        Toast.makeText(Firstpage.this, "Internet connection not available", Toast.LENGTH_LONG).show();
                    } else {
                        Glide.with( Firstpage.this ).load(R.drawable.loading).into( ivLoading);
                        new DownloadFileFromURL().execute(urlValue+"/"+a3+"_db.dll");
                        ((ApplicationController)getApplicationContext()).setDataBaseName("/data/data/com.focusmedica.aqrshell/databases/"+a3+"_db.dll");

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
                OutputStream output = new FileOutputStream("/data/data/com.focusmedica.aqrshell/databases/"+a3+"_db.dll");

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
                videoFile = new File("/data/data/com.focusmedica.aqrshell/databases/", "Cache");
                videoFile.delete();
                this.cancel(true);
            }

            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            ivLoading.setVisibility(View.GONE);
            Intent intent = new Intent(getApplicationContext(), Main.class);
            startActivity(intent);
        }
    }


}
