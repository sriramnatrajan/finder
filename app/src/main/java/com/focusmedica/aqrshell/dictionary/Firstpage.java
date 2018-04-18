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
import com.focusmedica.aqrshell.PreviewActivity;
import com.focusmedica.aqrshell.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.focusmedica.aqrshell.app.AppConfig;
import com.focusmedica.aqrshell.utils.Activities.ApplicationController;
import com.focusmedica.aqrshell.utils.ImageLoader;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class Firstpage extends Activity {
     String  a3,urlValue,url;
    private static File videoFile;
    ImageView ivLoading,brandPage;
  //  IndexValue mStringValue=new IndexValue();
  //  String murl=mStringValue.getmUrl();
    String dbname;   double screenInches;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstpage);

        Intent i=getIntent();
        a3=i.getStringExtra("app_id");
        urlValue=i.getStringExtra("url");


        url=urlValue+"/"+a3+"_db.dll";

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
      /*  Glide.with( Firstpage.this ).load(urlValue+"/"+a3+"phone_brand.png").into( brandPage );
        Log.d("URL",urlValue+"/"+"phone_brand.png");*/

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

    public void adbName(){
             dbname=a3+"_db.dll";
             Log.d("DBFILE+++++",a3+"_db.dll");
    }
    public String dbname1() {
        return dbname;
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

  static class Mydatadata  extends SQLiteOpenHelper {

    static final int DATABASE_VERSION = 1;
      static Firstpage f=new Firstpage();
      static String s=f.dbname;
      static  String DATABASE_NAME =s+"_db.dll";
      String DB_PATH_SUFFIX = "/databases/";
      Context ctx;


    public Mydatadata (Context context ) {
        super(context,   DATABASE_NAME, null, DATABASE_VERSION);
        ctx = context;
     //  dtat();
    Log.d("TAAGG+++DB0",DATABASE_NAME);
    }

    public void dtat(){
        s=f.dbname;
        Log.d("TAAGG++++DB0",s);
    }

    public ArrayList<DIctionaryContent> getAppDetail() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<DIctionaryContent> chapterList = null;
        try{
            chapterList = new ArrayList<DIctionaryContent>();
            String QUERY = "SELECT AppName,Infodetails,IAP,KEY,Dlink FROM AppTable";
            Cursor cursor = db.rawQuery(QUERY, null);
            if(!cursor.isLast())
            {
                while (cursor.moveToNext())
                {
                    DIctionaryContent chapter = new DIctionaryContent();
                    chapter.setAppName(cursor.getString(0));
                    chapter.setInfo(cursor.getString(1));
                    chapter.setIAP(cursor.getString(2));
                    chapter.setKEY(cursor.getString(3));
                    chapter.setDlink(cursor.getString(4));
                    chapterList.add(chapter);
                }
            }
            db.close();
        }catch (Exception e){
            Log.e("error", e + "");
        }
        return chapterList;
    }

    public List Get_ContactDetails(String firstchar) {
        List listcontent=new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM WordsTable WHERE WAlphabet=?", new String[]{firstchar});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            DIctionaryContent content=parseContent(cursor);
            listcontent.add(content);
            cursor.moveToNext();

        }
        cursor.close();
        db.close();
        return listcontent;
    }

    public String get_content(String title) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT WDescription FROM WordsTable WHERE WTitle=?", new String[]{title});
        cursor.moveToFirst();
        DIctionaryContent content=new DIctionaryContent();
        content.setDescription(cursor.getString(cursor.getColumnIndex("WDescription")));
        String description=content.getDescription();
        cursor.close();
        db.close();
        return description;
    }

    private DIctionaryContent parseContent(Cursor cursor) {
        DIctionaryContent content = new DIctionaryContent();
        content.setID((cursor.getInt(cursor.getColumnIndex("WID"))));
        content.setTitle(cursor.getString(cursor.getColumnIndex("WTitle")));
        content.setDescription(cursor.getString(cursor.getColumnIndex("WDescription")));
        content.setVDOname(cursor.getString(cursor.getColumnIndex("WVideoName")));
        content.setBUYflag(cursor.getString(cursor.getColumnIndex("WPaidFlag")));
        content.setAlphaflsg(cursor.getString(cursor.getColumnIndex("WAlphabet")));
        return content;
    }

    public void CopyDataBaseFromAsset() throws IOException {
        InputStream myInput = ctx.getAssets().open(DATABASE_NAME);
        String outFileName = getDatabasePath();
        File f = new File(ctx.getApplicationInfo().dataDir + DB_PATH_SUFFIX);
        if (!f.exists())
            f.mkdir();
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    private    String getDatabasePath() {

        Log.d("TAG DATATBASEq NAME",ctx.getApplicationInfo().dataDir + DB_PATH_SUFFIX + DATABASE_NAME);
        return ctx.getApplicationInfo().dataDir + DB_PATH_SUFFIX + DATABASE_NAME;
 }

    public SQLiteDatabase openDataBase() throws SQLException {
        File dbFile = ctx.getDatabasePath(DATABASE_NAME);
       Log.d("TAG DATATBASE NAME",DATABASE_NAME);
        if (!dbFile.exists()) {
            try {
                CopyDataBaseFromAsset();
                System.out.println("Copying success from Assets folder");
            } catch (IOException e) {
                throw new RuntimeException("Error creating source database", e);
            }
        }

        return SQLiteDatabase.openDatabase(dbFile.getPath(), null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.CREATE_IF_NECESSARY);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}

}
