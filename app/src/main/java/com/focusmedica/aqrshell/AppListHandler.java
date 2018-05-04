package com.focusmedica.aqrshell;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;



public class AppListHandler extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "myDb.db";
    private static final String TABLE_NAME = "app_table";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_VALUE = "value";
    private static final String KEY_APPID = "app_id";
    //private static final String KEY_CREATED_AT = "created_at";
    private static final String KEY_INFO ="appInfo";
    private static final String KEY_TYPE ="app_type";
    private static final String KEY_FOLDER="app_folder";
    //String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+" ("+KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+KEY_NAME+" TEXT,"+ KEY_DLINK +" TEXT)";
    //String DROP_TABLE = "DROP TABLE IF EXISTS "+TABLE_NAME;

    public AppListHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL unique, " +
                "value TEXT NOT NULL, " +
                "app_id TEXT NOT NULL unique,"+
                "app_type TEXT NOT NULL, "+
             //   "app_folder TEXT NOT NULL,"+
                "appInfo TEXT NOT NULL)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exist " + TABLE_NAME);
        onCreate(db);
    }

    public void addAppList(DataModel city) {
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            ContentValues values = new ContentValues();
            values.put(KEY_NAME,city.getName());
            values.put(KEY_VALUE, city.getValue());
            values.put(KEY_APPID, city.getApp_id());
            values.put(KEY_INFO,city.getAppInfo());
            values.put(KEY_TYPE,city.getApp_type());
        //    values.put(KEY_FOLDER,city.getAppFolder());
            long result = db.insert(TABLE_NAME, null, values);

            if(result == -1){
                Log.d("@@" ,"failed to Insert record"+city.getTitleName());

                Log.d("@@" ,"Error in inserting" );
            }else{
                Log.d("@@" ,"Successfully Inserted" );
                Log.d("@@" ,"Successfully Inserted"+ city.getTitleName());
            }

           // Log.d("Log message", city.getTitleName() + " and " + city.getTitleDlink()  );
            db.close();
        }catch (Exception e){
            Log.e("problem", e + "");
        }
    }


    public ArrayList<DataModel> getAppList(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<DataModel> chapterList = null;
        try{
            chapterList = new ArrayList<DataModel>();
            String QUERY = "SELECT name,value,app_id,appInfo ,app_type FROM app_table";
            Cursor cursor = db.rawQuery(QUERY, null);
            if(!cursor.isLast())
            {

                while (cursor.moveToNext())
                {
                    DataModel chapter = new DataModel();
                    chapter.setName(cursor.getString(0));
                    chapter.setValue(cursor.getString(1));
                    chapter.setApp_id(cursor.getString(2));
                    chapter.setAppInfo(cursor.getString(3));
                    chapter.setApp_type(cursor.getString(4));
                   // chapter.setAppFolder(cursor.getString(5));
                    chapterList.add(chapter);
                    Log.d("@@@","adapter="+chapterList.size());

                }
            }
            db.close();
        }catch (Exception e){
            Log.e("error", e + "");
        }
        return chapterList;
    }
    public ArrayList<DataModel> getItemList(String titleName) {

        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<DataModel> chapterList = null;

            chapterList = new ArrayList<DataModel>();
            //String QUERY="SELECT titleName,titlePages,titleDlink FROM Titles where memberName='"+titleID+"'";
            String QUERY = "SELECT * FROM app_table where _name='"+titleName+"'";
            Cursor cursor = db.rawQuery(QUERY, null);
            if(cursor!=null&&cursor.moveToFirst()) {
                do
                {
                    DataModel chapter = new DataModel();
                    chapter.setTitleName(cursor.getString(0));
                    chapter.setTitlePages(cursor.getString(1));
                    chapter.setDlink(cursor.getString(2));
                    chapterList.add(chapter);
                    Log.d("@@@","adapter="+chapterList.size());

                }while (cursor.moveToNext());
            }

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        return chapterList;
    }
}

