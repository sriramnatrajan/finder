package com.focusmedica.aqrshell.utils.Activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.focusmedica.aqrshell.CancelListener;
import com.focusmedica.aqrshell.DBHandler;
import com.focusmedica.aqrshell.DataModel;
import com.focusmedica.aqrshell.DownloadDialog;
import com.focusmedica.aqrshell.LanguageAdapter;
import com.focusmedica.aqrshell.NetworkUtils;
import com.focusmedica.aqrshell.PageActivity;
import com.focusmedica.aqrshell.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * Created by in_sane on 7/4/17.
 */

public class DMLanguageActivity extends Activity implements CancelListener{
    ListView listView;
    ArrayList list;
    LanguageAdapter adapter;
    ArrayList<DataModel> cityArrayList, citylist;
    String ziplanguage,url, titleName,zipFile,unzipLocation,titlePages;
    static ImageView ivDownload;
    int position;
    public static final int progress_bar_type = 0;
    private ProgressDialog pDialog1;
    DownloadDialog pDialog;
    private static File videoFile;
    int flag=0;
    boolean[] backgroundImageName;
    DBHandler handler;
    Button btnBack;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dm_language);
        //toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        //btnBack=(Button)findViewById(R.id.btnBack);
        final android.app.ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayOptions(actionBar.getDisplayOptions() | android.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        ImageView imageView = new ImageView(actionBar.getThemedContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        imageView.setImageResource(R.drawable.fmlogo);
        android.app.ActionBar.LayoutParams layoutParams = new android.app.ActionBar.LayoutParams(
                android.app.ActionBar.LayoutParams.WRAP_CONTENT,
                android.app.ActionBar.LayoutParams.WRAP_CONTENT, Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        imageView.setLayoutParams(layoutParams);
        actionBar.setCustomView(imageView);

        Bundle bundle=getIntent().getExtras();
        url=bundle.getString("url");
        titleName =bundle.getString("titleName");
        titlePages=bundle.getString("titlePages");
        Log.d("ankit","titlename="+titleName);

        File file = new File("/data/data/com.focusmedica.aqrshell/files/"+ titleName);
        if(!file.exists()){
            file.mkdirs();
        }

        listView = (ListView) findViewById(R.id.lvLanguageList);
        pDialog1 = new ProgressDialog(this);
        pDialog1.setMessage("Please Wait...");
        pDialog1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog1.setCancelable(false);
        pDialog1.show();


        /*btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/

        handler = new DBHandler(this);
        NetworkUtils utils = new NetworkUtils(DMLanguageActivity.this);
        if(utils.isConnectingToInternet()) {
            Log.d("ashu","if");
            //handler.delete(getApplicationContext());
            getApplicationContext().deleteDatabase("LanguageDatabase.db");
            new DataFetcherTask().execute();
        }
        else
        {
            ArrayList<DataModel> cityList = handler.getAllLanguage();
            adapter = new LanguageAdapter(DMLanguageActivity.this,cityList,url, titleName);
            listView.setAdapter(adapter);
            pDialog1.dismiss();
        }
        backgroundImageName=new boolean[14];


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // DownloadFileFromURL  asyncObj;
            @Override
            public void onItemClick(final AdapterView<?> adapterView, View view, int i, long l) {
                ivDownload = (ImageView) view.findViewById(R.id.ivDownload);
                //ivDownload.setImageResource(R.drawable.icn_download);
                position = i;
                //textElement=(TextView)view.findViewById(R.id.textView2);
                citylist = handler.getAllLanguage();

                DataModel city = citylist.get(position);
                final String url1 = city.getState();

                ziplanguage = url1.substring(url1.lastIndexOf("/") + 1, url1.indexOf(".z"));

                SharedPreferences appInfo = DMLanguageActivity.this.getSharedPreferences("Arrowcheck", MODE_PRIVATE);
                backgroundImageName[i]= appInfo.getBoolean("isArrow"+ titleName +position,false);

                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (ivDownload.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.right_arrow, getTheme()).getConstantState()) {
                        Log.d("ashu", "click");
                        hand.sendEmptyMessageDelayed(0, 100);
                    } else {
                        if (!haveNetworkConnection()) {
                            Toast.makeText(DMLanguageActivity.this, "No internet connection available", Toast.LENGTH_LONG).show();
                        }else {
                            flag = 0;
                            Log.d("ashu", "download");
                            new DownloadFileFromURL().execute(url1);
                            Toast.makeText(getApplicationContext(), "Downloading...", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else {
                    if (ivDownload.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.right_arrow).getConstantState()) {
                        hand.sendEmptyMessageDelayed(0, 100);
                    } else {
                        if (!haveNetworkConnection()) {
                            Toast.makeText(DMLanguageActivity.this, "No internet connection available", Toast.LENGTH_LONG).show();
                        }else {
                            flag = 0;
                            Log.d("ashu", "download");
                            new DownloadFileFromURL().execute(url1);
                            Toast.makeText(getApplicationContext(), "Downloading...", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }*/
                if (backgroundImageName[position]) {
                    Log.d("ashu", "click");
                    hand.sendEmptyMessageDelayed(0, 100);
                } else {
                    if (!haveNetworkConnection()) {
                        Toast.makeText(DMLanguageActivity.this, "No internet connection available", Toast.LENGTH_LONG).show();
                    } else {
                        flag = 0;
                        Log.d("ashu", "download");
                        new DownloadFileFromURL().execute(url1);
                        Toast.makeText(getApplicationContext(), "Downloading...", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    public int getViewTypeCount() {
        return 1;
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

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 0:
                pDialog = new DownloadDialog(this,this);
                pDialog.show();
                return pDialog;
        }
        return null;
    }

    @Override
    public void cancel() {
        flag = 1;
        new DownloadFileFromURL().cancel(true);
        pDialog1.dismiss();
        File file = new File("data/data/com.focusmedica.aqrshell/files/"+ titleName +"/" + ziplanguage + ".zip");
        if (file.exists()) {
            file.delete();
        }
        Toast.makeText(getBaseContext(), "Download Cancelled", Toast.LENGTH_SHORT).show();
        ivDownload.setImageResource(R.drawable.icn_download);
        Intent intent = new Intent(DMLanguageActivity.this, DMLanguageActivity.class);
        intent.putExtra("url",url);
        intent.putExtra("titlePages",titlePages);
        intent.putExtra("titleName",titleName);
        startActivity(intent);
        finish();
    }

    class DataFetcherTask extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected Void doInBackground(Void... params) {
            String serverData = null;// String object to store fetched data from server
            // Http Request Code start
            DefaultHttpClient httpClient = new DefaultHttpClient();
            //HttpGet httpGet = new HttpGet("http://panther-foundation.in:81/testapps/diabetesmanagement/lang1.json");
            HttpGet httpGet = new HttpGet(url+"lang_list.json");
            Log.d("TAG",url+"lang_list.json");
            // HttpGet httpGet = new HttpGet("http://panther-foundation.in:81/testapps/diabetesmanagement/Pregnency_lang_android.json");

            try {
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                serverData = EntityUtils.toString(httpEntity);
                Log.d("response", serverData);
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Http Request Code end
            // Json Parsing Code Start
            try {
                list=new ArrayList();
                cityArrayList = new ArrayList<DataModel>();
                JSONObject jsonObject = new JSONObject(serverData);
                JSONArray jsonArray = jsonObject.getJSONArray("languages");

                for (int i=0;i<jsonArray.length();i++)
                {
                    JSONObject jsonObjectCity = jsonArray.getJSONObject(i);
                    String cityName = jsonObjectCity.getString("lname");
                    String url = jsonObjectCity.getString("url");
                    String cityDescription = jsonObjectCity.getString("description");
                    DataModel city = new DataModel(null,null,null,null,null);
                    city.setName(cityName);
                    city.setState(url);
                    city.setDescription(cityDescription);
                    System.out.println("what1:" + city.getName());

                    handler.addLanguage(city);// Inserting into DB

                    //list.add(city);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Json Parsing code end
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ArrayList<DataModel> cityList = handler.getAllLanguage();
            System.out.println("what3:" + cityList);
            Log.d("ankit","json="+titleName);
            adapter = new LanguageAdapter(DMLanguageActivity.this, cityList,url, titleName);
            listView.setAdapter(adapter);
            pDialog1.dismiss();
           /* adapter = new CityAdapter(Third.this,list);
            listView.setAdapter(adapter);*/
        }

    }

    class DownloadFileFromURL extends AsyncTask<String, String, String> {
        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // pDialog.show();
            showDialog(progress_bar_type);
        }

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
                int lengthOfFile = conection.getContentLength();

                InputStream input = new BufferedInputStream(url.openStream());
                // Output stream
                Log.d("ankit","download="+titleName);
                OutputStream output = new FileOutputStream("data/data/com.focusmedica.aqrshell/files/"+ titleName + "/"+ziplanguage+".zip");

                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1 && flag==0) {

                    if (flag==1) break;

                    total += count;

                    publishProgress("" + (int) ((total * 100) / lengthOfFile));
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    //   publishProgress("" + (int) ((total * 100) / lengthOfFile));

                    // writing data to file
                    output.write(data, 0, count);

                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
                videoFile = new File(getCacheDir(), "Cache");
                videoFile.delete();
                this.cancel(true);
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(String... progress) {
            int prog = Integer.parseInt(progress[0]);
            pDialog.updateDialogProgress(prog+"% Downloading...",prog);
            if (prog == 100) {
                pDialog.dismiss();
                Toast.makeText(getBaseContext(), "Download completed", Toast.LENGTH_SHORT).show();
                ivDownload.setImageResource(R.drawable.right_arrow);
                SharedPreferences arrowcheck = DMLanguageActivity.this.getSharedPreferences("Arrowcheck", MODE_PRIVATE);
                SharedPreferences.Editor appInfoEditor = arrowcheck.edit();
                appInfoEditor.putBoolean("isArrow"+ titleName +position,true);
                appInfoEditor.commit();
                Intent intent=new Intent(DMLanguageActivity.this,DMLanguageActivity.class);
                intent.putExtra("url",url);
                intent.putExtra("titlePages",titlePages);
                intent.putExtra("titleName",titleName);
                startActivity(intent);
                finish();
            }
        }

        @Override
        protected void onPostExecute(String file_url) {

            if (flag==0) {

                // dismiss the dialog after the file was downloaded
                //  pDialog.dismiss();

                       /* Decompress d = new Decompress(zipFile, unzipLocation);
                        d.unzip();*/
                try {

                    unzip();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                //playIconSwitcher();

            	/*
            	 * Post download the video is passed on for playback
            	 */
                SharedPreferences sharedpreferences = getSharedPreferences("MyPREFERENCES.xml", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();

                System.out.println("checkingposition: " + position);
                switch (position){
                    case 0:   editor.putInt("english"+ titleName,position);
                        break;
                    case 1:    editor.putInt("hindi"+ titleName,position);
                        break;
                    case 2:    editor.putInt("marathi"+ titleName,position);
                        break;
                    case 3:    editor.putInt("bengali"+ titleName,position);
                        break;
                    case 4:    editor.putInt("gujarati"+ titleName,position);
                        break;
                    case 5:    editor.putInt("assamese"+ titleName,position);
                        break;
                    case 6:    editor.putInt("oriya"+ titleName,position);
                        break;
                    case 7:    editor.putInt("tamil"+ titleName,position);
                        break;
                    case 8:    editor.putInt("telugu"+ titleName,position);
                        break;
                    case 9:    editor.putInt("kannada"+ titleName,position);
                        break;
                    case 10:    editor.putInt("malayalam"+ titleName,position);
                        break;
                    case 11:    editor.putInt("punjabi"+ titleName,position);
                        break;
                    case 12:    editor.putInt("kashmiri"+ titleName,position);
                        break;
                    case 13:    editor.putInt("urdu"+ titleName,position);
                        break;
                }
                editor.commit();
                ivDownload.setImageResource(R.drawable.right_arrow);
                SharedPreferences arrowcheck = DMLanguageActivity.this.getSharedPreferences("Arrowcheck", MODE_PRIVATE);
                SharedPreferences.Editor appInfoEditor = arrowcheck.edit();
                appInfoEditor.putBoolean("isArrow"+ titleName +position,true);
                appInfoEditor.commit();
                //destroy activity on finishing

            } else {
                ivDownload.setImageResource(R.drawable.icn_download);
            }

        }

    }

    Handler hand=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==0){
                Log.d("ankit","hand="+titleName);
                Intent intent = new Intent(DMLanguageActivity.this, PageActivity.class);
                intent.putExtra("ziplang",ziplanguage);
                intent.putExtra("titlePages",titlePages);
                intent.putExtra("titleName",titleName);
                intent.putExtra("url",url);
                startActivity(intent);

            }
            super.handleMessage(msg);
        }
    };

    public void unzip() throws IOException {
        Log.d("ankit","unzip="+titleName);
        zipFile = "data/data/com.focusmedica.aqrshell/files/"+ titleName +"/" + ziplanguage + ".zip";
        unzipLocation = "data/data/com.focusmedica.aqrshell/files/"+ titleName +"/";

        // pDialog = (ProgressBar) findViewById(R.id.progressBar2);
        pDialog1 = new ProgressDialog(this);
        Log.d("ankit","upzip="+unzipLocation+"zip="+zipFile);
        new UnZipTask().execute("data/data/com.focusmedica.aqrshell/files/"+ titleName +"/" + ziplanguage +".zip",unzipLocation);

    }

    public class UnZipTask extends AsyncTask<String, Void, Boolean> {
        @SuppressWarnings("rawtypes")

        @Override
        protected Boolean doInBackground(String... params) {
            String filePath = params[0];
            String destinationPath = params[1];

            File archive = new File(filePath);

            try {
                ZipFile zipfile = new ZipFile(archive);
                for (Enumeration e = zipfile.entries(); e.hasMoreElements();) {
                    ZipEntry entry = (ZipEntry) e.nextElement();
                    unzipEntry(zipfile, entry, destinationPath);

                }


                UnzipUtil d = new UnzipUtil("data/data/com.focusmedica.aqrshell/files/"+ titleName +"/" +ziplanguage+".zip",unzipLocation);
                d.unzip();

            } catch (Exception e) {

                return false;
            }

            return true;
        }


        @Override
        protected void onPostExecute(Boolean result) {
            //pDialog1.dismiss();

        }

        private void unzipEntry(ZipFile zipfile, ZipEntry entry,
                                String outputDir) throws IOException {

            if (entry.isDirectory()) {
                createDir(new File(outputDir, entry.getName()));
                return;
            }

            File outputFile = new File(outputDir, entry.getName());
            if (!outputFile.getParentFile().exists()) {
                createDir(outputFile.getParentFile());
            }

            BufferedInputStream inputStream = new BufferedInputStream(zipfile.getInputStream(entry));
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFile));

            try {

            } finally {
                outputStream.flush();
                outputStream.close();
                inputStream.close();
            }
        }

        private void createDir(File dir) {
            if (dir.exists()) {
                return;
            }
            if (!dir.mkdirs()) {
                throw new RuntimeException("Can not create dir " + dir);
            }
        }
    }
}


class UnzipUtil {
    private String _zipFile;
    private String _location;

    public UnzipUtil(String zipFile, String location) {
        _zipFile = zipFile;
        _location = location;

        _dirChecker("");
    }

    public void unzip() {
        try  {
            FileInputStream fin = new FileInputStream(_zipFile);
            ZipInputStream zin = new ZipInputStream(fin);
            ZipEntry ze = null;
            while ((ze = zin.getNextEntry()) != null) {
                Log.v("Decompress", "Unzipping " + ze.getName());

                if(ze.isDirectory()) {
                    _dirChecker(ze.getName());
                } else {
                    Log.d("ankit","upzip="+_location);
                    FileOutputStream fout = new FileOutputStream(_location + ze.getName());
                    //   for (int c = zin.read(); c != -1; c = zin.read()) {
                    //   fout.write(c);

                    byte[] buffer = new byte[8192];
                    int len;
                    while ((len = zin.read(buffer)) != -1) {
                        fout.write(buffer, 0, len);
                    }
                    fout.close();

                    //  }

                    zin.closeEntry();
                    // fout.close();
                }

            }
            zin.close();
        } catch(Exception e) {
            Log.e("Decompress", "unzip", e);
        }

    }

    private void _dirChecker(String dir) {
        File f = new File(_location + dir);

        if(!f.isDirectory()) {
            f.mkdirs();
        }
    }
}
