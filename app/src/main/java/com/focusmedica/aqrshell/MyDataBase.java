package com.focusmedica.aqrshell;

/**
 * Created by windev on 5/11/2016.
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class MyDataBase extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "home.dll";
    private static final String TABLENAME = "Titles";
    private static final String COLUMNNAME = "Title";

    static Context ctx;

    public MyDataBase(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        ctx = context;

    }

    public ArrayList<DataModel> getChapter() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<DataModel> chapterList = null;
        try {
            chapterList = new ArrayList<DataModel>();
            String QUERY = "SELECT titleID,titleName,titleDlink FROM Titles";
            Cursor cursor = db.rawQuery(QUERY, null);
            if (!cursor.isLast()) {
                while (cursor.moveToNext()) {
                    DataModel chapter = new DataModel(null,null,null,null,null);
                    chapter.setTitleId(cursor.getString(0));
                    chapter.setDescriptionData(cursor.getString(1));
                    chapter.setImageName(cursor.getString(2));
                    chapterList.add(chapter);
                }
            }
            db.close();
        } catch (Exception e) {
            Log.e("error", e + "");
        }
        return chapterList;
    }

    SQLiteDatabase db = this.getReadableDatabase();

    public ArrayList<DataModel> getItem(String productname) {
        String query = "Select * FROM " + TABLENAME + " WHERE " + COLUMNNAME + " =  \"" + productname + "\"";
        ArrayList<DataModel> chapterList = null;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        DataModel chapter = new DataModel(null,null,null,null,null);

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();

            chapter.setTitleId(cursor.getString(0));
            chapter.setDescriptionData(cursor.getString(1));
            chapter.setImageName(cursor.getString(2));
            cursor.close();
        } else {
            chapter = null;
        }
        db.close();
        return chapterList;
    }

    public ArrayList<DataModel> getContent(int position) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<DataModel> chapterList = null;
        try {
            chapterList = new ArrayList<DataModel>();
            String QUERY = "SELECT titleID,titleName,titleDlink FROM Titles WHERE rowid=" + position;
            Cursor cursor = db.rawQuery(QUERY, null);
            if (!cursor.isLast()) {
                while (cursor.moveToNext()) {
                    DataModel chapter = new DataModel(null,null,null,null,null);
                    chapter.setTitleId(cursor.getString(0));
                    chapter.setTitleName(cursor.getString(1));
                    chapter.setDescriptionData(cursor.getString(2));
                    chapterList.add(chapter);
                }
            }
            db.close();
        } catch (Exception e) {
            Log.e("error", e + "");
        }
        return chapterList;
    }

    public ArrayList<DataModel> getNameAndImage() {

        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<DataModel> imageList = null;
        try {
            imageList = new ArrayList<DataModel>();
            String QUERY = "SELECT titleID,titleName,titleDlink FROM Titles";
            Cursor mCursor = db.rawQuery(QUERY, null);

            if (!mCursor.isLast()) {
                while (mCursor.moveToNext()) {
                    DataModel images = new DataModel(null,null,null,null,null);
                    images.setTitleId(mCursor.getString(0));
                    images.setTitleName(mCursor.getString(1));
                    images.setDlink(mCursor.getString(2));
                    imageList.add(images);
                }
            }
            db.close();
        } catch (Exception e) {
            Log.e("TITLE NAME", e + "" + e.getMessage());
        }

        return imageList;
    }

    public ArrayList<DataModel> getNameAndImage(String titleName) {

        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<DataModel> imageList = null;
        try {
            imageList = new ArrayList<DataModel>();

            String QUERY = "SELECT titleName,titlePages,titleDlink FROM Titles where titleCode='" + titleName + "'";
            //memberName name changes
            Cursor mCursor = db.rawQuery(QUERY, null);

            if (!mCursor.isLast()) {
                while (mCursor.moveToNext()) {
                    DataModel images = new DataModel(null,null,null,null,null);
                    images.setTitleName(mCursor.getString(0));
                    images.setTitlePages(mCursor.getString(1));
                    images.setDlink(mCursor.getString(2));
                    imageList.add(images);
                }
            }
            db.close();
        } catch (Exception e) {
            Log.e("TITLE NAME", e + "" + e.getMessage());
        }

        return imageList;
    }

    public ArrayList<DataModel> getThumbnail() {

        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<DataModel> imageList = null;
        try {
            imageList = new ArrayList<DataModel>();

            String QUERY = "SELECT titleName,titlePages,titleDlink FROM Titles";
            //memberName name changes
            Cursor mCursor = db.rawQuery(QUERY, null);

            if (!mCursor.isLast()) {
                while (mCursor.moveToNext()) {
                    DataModel images = new DataModel(null,null,null,null,null);
                    images.setTitleName(mCursor.getString(0));
                    images.setTitlePages(mCursor.getString(1));
                    images.setDlink(mCursor.getString(2));
                    imageList.add(images);
                }
            }
            db.close();
        } catch (Exception e) {
            Log.e("TITLE NAME", e + "" + e.getMessage());
        }

        return imageList;
    }

    public ArrayList<DataModel> getDetails() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<DataModel> chapterList = null;
        try {
            chapterList = new ArrayList<DataModel>();
            String QUERY = "SELECT AppName,AppInfo,IAP,SKU,Dlink FROM AppDetails";
            Cursor cursor = db.rawQuery(QUERY, null);
            if (!cursor.isLast()) {
                while (cursor.moveToNext()) {
                    DataModel chapter = new DataModel(null,null,null,null,null);
                    chapter.setInfo(cursor.getString(0));
                    chapter.setIAP(cursor.getString(1));
                    chapter.setKEY(cursor.getString(2));
                    chapter.setDatabaseName(cursor.getString(3));
                    chapter.setDlink(cursor.getString(4));
                    chapterList.add(chapter);
                }
            }
            db.close();
        } catch (Exception e) {
            Log.e("error", e + "");
        }
        return chapterList;
    }
    public ArrayList<DataModel> getWordMs(String search) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<DataModel> imageList = null;
        String selectQuery = "SELECT * FROM Titles where titleName LIKE '%" + search + "%'";
        Cursor cursorur = db.rawQuery(selectQuery, null);
        if (cursorur == null) {
            return null;
        } else if (!cursorur.moveToFirst()) {
            DataModel images = new DataModel(null,null,null,null,null);
            images.setTitleName(cursorur.getString(0));
            images.setTitlePages(cursorur.getString(1));
            images.setDlink(cursorur.getString(2));
            imageList.add(images);
        }
        while (cursorur.moveToNext()) ;

        cursorur.close();

        return imageList;
    }


    public Cursor getWordMatches(String search) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<DataModel> imageList = null;
        String selectQuery = "SELECT titleID titlePages,titleDlink FROM Titles where titleName LIKE '%" + search + "%'";
        Cursor cursorur = db.rawQuery(selectQuery, null);
        if (cursorur == null) {
            return null;
        } else if (!cursorur.moveToFirst()) {
            DataModel images = new DataModel(null,null,null,null,null);
            images.setTitleId(cursorur.getColumnName(0));
            images.setTitlePages(cursorur.getString(0));
            images.setDlink(cursorur.getString(1));
            imageList.add(images);
        }
        while (cursorur.moveToNext()) ;

        cursorur.close();

        return cursorur;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}