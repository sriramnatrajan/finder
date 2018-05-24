package com.focusmedica.aqrshell.dictionary;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.focusmedica.aqrshell.DataModel;
import com.focusmedica.aqrshell.app.AppConfig;
import com.focusmedica.aqrshell.utils.Activities.ApplicationController;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



public class Mydatabase  extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DB_PATH_SUFFIX = "/databases/";
    static Context ctx;

    //private static final String DATABASE_NAME = "7_db.dll";



    public Mydatabase(Context context, String dbName) {
        super(context, ((ApplicationController)context).getDataBaseName(), null, DATABASE_VERSION);
        ctx = context;
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

    public ArrayList<DIctionaryContent> getAlphabets() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<DIctionaryContent> chapterList = null;
        try{
            chapterList = new ArrayList<DIctionaryContent>();
            String QUERY = "SELECT DISTINCT  WAlphabet FROM WordsTable";
            Cursor cursor = db.rawQuery(QUERY, null);
            if(!cursor.isLast())
            {
                while (cursor.moveToNext())
                {
                    DIctionaryContent chapter = new DIctionaryContent();
                    chapter.setAlphabet(cursor.getString(0));

                    chapterList.add(chapter);
                }
            }
            db.close();
        }catch (Exception e){
            Log.e("error", e + "");
        }
        return chapterList;
    }
    public ArrayList<DIctionaryContent> getDescription(String text) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<DIctionaryContent> chapterList = null;
        try{
            chapterList = new ArrayList<DIctionaryContent>();
            String QUERY = "SELECT * FROM WordsTable WHERE  WDescription ='" + text + "'";
            Cursor cursor = db.rawQuery(QUERY, null);
            if(!cursor.isLast())
            {
                while (cursor.moveToNext())
                {
                    DIctionaryContent chapter = new DIctionaryContent();
                    chapter.setDescription(cursor.getString(0));

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

    public ArrayList<DIctionaryContent> getVideoFileName(String ch) {

        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<DIctionaryContent> imageList = new ArrayList<DIctionaryContent>();
        try {
            imageList = new ArrayList<DIctionaryContent>();
            String QUERY=" SELECT WVideoName FROM WordsTable WHERE WVideoName Like"+"'"+ch+"%"+"'";

            Cursor mCursor = db.rawQuery(QUERY, null);

            if (!mCursor.isLast()) {

                 DIctionaryContent m=new DIctionaryContent();

                int i=0;
                while (mCursor.moveToNext()) {

                    DIctionaryContent chapter = new DIctionaryContent();
                    chapter.setVDOname(mCursor.getString(0));
                    String si=mCursor.getString(mCursor.getColumnIndex("WVideoName"));

                    //chapter.setVDOname(mCursor.getString(mCursor.getColumnIndex("WVideoName")));
                    imageList.add(chapter);
                }
            }
            db.close();
            mCursor.close();
        } catch (Exception e) {
            Log.e("TITLE NAME", e + "" + e.getMessage());
        }
        return imageList;
    }
    public ArrayList<DIctionaryContent> getContent(String content) {

        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<DIctionaryContent> imageList = new ArrayList<DIctionaryContent>();
        try {
            imageList = new ArrayList<DIctionaryContent>();
            String QUERY=" SELECT WVideoName FROM WordsTable WHERE WVideoName Like"+"'"+content+"%"+"'";

            Cursor mCursor = db.rawQuery(QUERY, null);

            if (!mCursor.isLast()) {

                DIctionaryContent m=new DIctionaryContent();

                int i=0;
                while (mCursor.moveToNext()) {

                    DIctionaryContent chapter = new DIctionaryContent();
                    chapter.setVDOname(mCursor.getString(0));
                    String si=mCursor.getString(mCursor.getColumnIndex("WVideoName"));

                    //chapter.setVDOname(mCursor.getString(mCursor.getColumnIndex("WVideoName")));
                    imageList.add(chapter);
                }
            }
            db.close();
            mCursor.close();
        } catch (Exception e) {
            Log.e("TITLE NAME", e + "" + e.getMessage());
        }
        return imageList;
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
       /* InputStream myInput = ctx.getAssets().open(DATABASE_NAME);
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
        myInput.close();*/
    }

    private  String getDatabasePath() {
        return ctx.getApplicationInfo().dataDir + DB_PATH_SUFFIX +AppConfig.getDatabaseName();
    }

    public SQLiteDatabase openDataBase() throws SQLException {

        File dbFile = ctx.getDatabasePath(((ApplicationController)ctx).getDataBaseName());
        //Log.d("TAG",AppConfig.getDatabaseName());
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

