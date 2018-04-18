package com.focusmedica.aqrshell;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by windev on 8/14/2017.
 */

public class SearchHelper {

    private static final String TAG = "DictionaryDatabase";

    //The columns we'll include in the dictionary table
    public static final String COL_WORD = "WORD";
    public static final String COL_DEFINITION = "DEFINITION";

    private static final String DATABASE_NAME = "DICTIONARY";
    private static final String FTS_VIRTUAL_TABLE = "FTS";
    private static final int DATABASE_VERSION = 1;

    private final DatabaseOpenHelper mDatabaseOpenHelper;

    public SearchHelper(Context context) {
        mDatabaseOpenHelper = new DatabaseOpenHelper(context);
    }

    private static class DatabaseOpenHelper extends SQLiteOpenHelper {

        private final Context mHelperContext;
        private SQLiteDatabase mDatabase;

        private static final String FTS_TABLE_CREATE =
                "CREATE VIRTUAL TABLE " + FTS_VIRTUAL_TABLE +
                        " USING fts3 (" +
                        COL_WORD + ", " +
                        COL_DEFINITION + ")";

        DatabaseOpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            mHelperContext = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            mDatabase = db;
            mDatabase.execSQL(FTS_TABLE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + FTS_VIRTUAL_TABLE);
            onCreate(db);
        }
    }
/*    public ArrayList<DataModel> getItemList(String query){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<DataModel> chapterList = null;
        try{
            chapterList = new ArrayList<DataModel>();
            //String QUERY="SELECT titleName,titlePages,titleDlink FROM Titles where memberName='"+titleID+"'";
            String QUERY = "SELECT * FROM app_table where _name='"+query+"'";
            Cursor cursor = db.rawQuery(QUERY, null);
            if(cursor!=null&&cursor.moveToFirst())
            {
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
            db.close();
        }catch (Exception e){
            Log.e("error", e + "");
        }
        return chapterList;
    }
    */
}