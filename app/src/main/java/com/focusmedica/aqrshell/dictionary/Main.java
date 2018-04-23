package com.focusmedica.aqrshell.dictionary;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.focusmedica.aqrshell.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class Main extends Activity implements  Sorting.OnHeadlineSelectedListener{
    ProgressDialog pDialog;int flag=0;
    ImageView buy;
    static final String TAG = "FMI";
    static String SKU_PREMIUM;
    static final int RC_REQUEST = 10001;
    String thumb;
    Mydatabase handler;
    DIctionaryContent content;
    String base64EncodedPublicKey;
    ArrayList<DIctionaryContent> AppDetails=new ArrayList<>();
    ArrayList<DIctionaryContent> videoDetails=new ArrayList<>();
    TextView mTextView;
    ImageView iv_download; String  url;
    String  vdoname,filterVideoName;
    Content.ListAdapter mListAdapter;
    Content c;
String[] dbvalues;
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
        url=content.getDlink();
        Log.d("URL File",content.getDlink());
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
              //TextView tv=(TextView)findViewById(R.id.textView3);
              //getVideoFileName

                videoDetails=handler.getVideoFileName("A");
                content=videoDetails.get(0);
                vdoname =content.getVDOname();

                filterVideoName=vdoname.replaceAll(" ","");
                String  s=handler.getVideoFileName("A").toString();
                new DownloadFileFromURL().execute(url+filterVideoName );
                Log.d("File",url+filterVideoName );
            }
        });
    }

    @Override
    public void onArticleSelected(final String firstcharacter) {
        Content contfrag = (Content) getFragmentManager().findFragmentById(R.id.content);
        contfrag.test(firstcharacter);
      //  Toast.makeText(Main.this,firstcharacter, Toast.LENGTH_SHORT).show();
        iv_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoDetails=handler.getVideoFileName(firstcharacter);
                content=videoDetails.get(0);
                Object[] i=videoDetails.toArray();
                vdoname =content.getVDOname();
                handler.getVideoFileName(firstcharacter).get(0);

                filterVideoName=vdoname.replaceAll(" ","%20");
                Log.d("Object",  videoDetails.toString());
                Log.d("VideoName", "++++++"+i);

            Log.d("VideoName",vdoname+"++++++"+ handler.getVideoFileName(firstcharacter));
                /*for(int i=0; i<videoDetails.size();i++){
                 // Log.d("VideoName",vdoname+"++++++"+i);
                 //Log.d("Object","++++++"+  content.getNameOfVid().toString());
               }*/
                for (int f=0; f<videoDetails.size();f++){
                 //   Toast.makeText(Main.this, "filter video name"+handler.getVideoFileName(firstcharacter).get(f), Toast.LENGTH_SHORT).show();
                    Log.d("VideoName","filter video name"+handler.getVideoFileName(firstcharacter).get(f));
              // content=videoDetails.subList(0,f)
               String s=content.getVDOname();
                    Log.d("VideoName","filter video name"+s);

                }
                //Toast.makeText(Main.this, filterVideoName, Toast.LENGTH_SHORT).show();
            //  String lower_case=content.getVDOname().toLowerCase().substring(0, content.getVDOname().indexOf(".")).replaceAll(" ", "").replaceAll("-","");

            new DownloadFileFromURL().execute(url+filterVideoName );
            Log.d("File",url+filterVideoName );
            }
        });
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

    public  class DownloadFileFromURL extends AsyncTask<String, String, String> {
        private long total = 0L;
        private long lengthOfFile = 0L;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Main.this);
            pDialog.setMessage("Downloading.... Please wait...");
            pDialog.setMax(100);
            pDialog.setIndeterminate(false);
            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //asyncobj.cancel(true);
                    // Content.ListAdapter.ViewHolder m ;
                    // m.img.setImageResource(R.drawable.download_enable);
                    flag = 1;
                }
            });
            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();
        }

        private void onDownloadCancelled() {

            pDialog.dismiss();
            File dir=getApplicationContext().getFilesDir();
            File file=new File(dir,filterVideoName);
            file.delete();
            //mListAdapter.notifyDataSetChanged();
        }

        @Override
        protected void onCancelled() {
            if(flag==1) {
                onDownloadCancelled();
                flag=0;
            }else {
                Toast.makeText(getApplicationContext(), "Video Not Exist...", Toast.LENGTH_LONG).show();
                onDownloadCancelled();
            }
        }


        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.setRequestProperty("Accept-Encoding", "Identity");
                conection.connect();
                lengthOfFile = conection.getContentLength();

                InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output = new FileOutputStream(getApplicationContext().getFilesDir()+"/"+filterVideoName);
                byte data[] = new byte[1024];
                while ((count = input.read(data)) != -1 && !isCancelled()) {
                if (isCancelled()) break;
                total += count;
                    publishProgress("" + (int) ((total * 100) / lengthOfFile));
                    output.write(data, 0, count);

                }
                output.flush();
                output.close();
                input.close();
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
                this.cancel(true);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... progress) {
            pDialog.setProgress(Integer.parseInt(progress[0]));

        }

        @Override
        protected void onPostExecute(String file_url) {
            if (!isCancelled()) {
                pDialog.dismiss();
               //mListAdapter.notifyDataSetChanged();
            } else {
                onDownloadCancelled();
            }
        }
    }
}


