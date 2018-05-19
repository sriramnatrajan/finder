
package com.focusmedica.aqrshell.dbHandler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.focusmedica.aqrshell.DataModel;

import java.util.ArrayList;

public class SQLiteHandler extends SQLiteOpenHelper {

	private static final String TAG = SQLiteHandler.class.getSimpleName();

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "android_api";

	// Login table name
	private static final String TABLE_USER = "user";

	// Login Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_NAME = "name";
	private static final String KEY_VALUE = "value";
	private static final String KEY_APPID = "app_id";
	private static final String KEY_APPTYPE= "app_type";
	private static final String KEY_INFO ="appInfo";
	private static final String KEY_FOLDER="app_folder";

	public SQLiteHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
				+ KEY_ID + " INTEGER PRIMARY KEY,"
				+ KEY_NAME + " TEXT NOT NULL UNIQUE,"
				+ KEY_VALUE + " TEXT NOT NULL UNIQUE,"
				+ KEY_APPID + " TEXT,"
				+ KEY_APPTYPE+ " INTEGER,"
			//	+ KEY_FOLDER+"INTEGER, "
				+ KEY_INFO +" TEXT" + ")";
		db.execSQL(CREATE_LOGIN_TABLE);
		Log.d(TAG, "Database tables created");
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

		// Create tables again
		onCreate(db);
	}

	/**
	 * Storing user details in database
	 * */
	public boolean addUser(String name, String value, String uid, String appInfo, String appType) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, name); // Name
		values.put(KEY_VALUE, value); // Email
		values.put(KEY_APPID, uid); // Email
		values.put(KEY_INFO, appInfo);
		values.put(KEY_APPTYPE,appType);// Created At
		//values.put(KEY_FOLDER,appFolder);

		// Inserting Row
		long id = db.insertWithOnConflict(TABLE_USER, null, values,SQLiteDatabase.CONFLICT_REPLACE);
		db.close(); // Closing database connection
		Log.d(TAG, "New user inserted into sqlite: " + id);
	return true;
	}


	public ArrayList<DataModel> getAppList(){
		SQLiteDatabase db = this.getReadableDatabase();
		ArrayList<DataModel> chapterList = null;
		try{
			chapterList = new ArrayList<DataModel>();
			String QUERY = "SELECT name,value,app_id,appInfo,app_type FROM user ";
			Cursor cursor = db.rawQuery(QUERY, null);
			if(!cursor.isLast())
			{

				while (cursor.moveToNext())
				{
					DataModel chapter = new DataModel(null,null,null,null,null);
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
	public ArrayList<DataModel> getDetails(String titleName) {

		SQLiteDatabase db = this.getReadableDatabase();
		ArrayList<DataModel> imageList = null;
		try {
			imageList = new ArrayList<DataModel>();
			String QUERY = "SELECT name,value,app_id,appInfo,app_type FROM user where name='" + titleName + "'";

			//String QUERY = "SELECT  * FROM " + TABLE_USER;
			 //memberName name changes
			Cursor mCursor = db.rawQuery(QUERY, null);
			if (!mCursor.isLast()) {
				while (mCursor.moveToNext()) {
					DataModel images = new DataModel(null,null,null,null,null);
					images.setName(mCursor.getString(0));
					images.setValue(mCursor.getString(1));
					images.setApp_id(mCursor.getString(2));
					images.setAppInfo(mCursor.getString(3));
					images.setApp_type(mCursor.getString(4));
					//images.setAppFolder(mCursor.getString(5));
					imageList.add(images);

				}
			}
			db.close();
		} catch (Exception e) {
			Log.e("TITLE NAME", e + "" + e.getMessage());
		}
		return imageList;
	}
	public void removeSingleContent(String title) {
		//Open the database
		SQLiteDatabase database = this.getWritableDatabase();
		database.execSQL("DELETE FROM   user   WHERE name  = '" + title + "'");
		database.close();
	}

	public ArrayList<DataModel> getUnd(String apptype ) {
		SQLiteDatabase db = this.getReadableDatabase();
		ArrayList<DataModel> imageList = null;
		try {
			imageList = new ArrayList<DataModel>();
			String QUERY = "SELECT name,value,app_id,appInfo,app_type FROM user where app_type='" + apptype+ "' ";
			Cursor mCursor = db.rawQuery(QUERY, null);

			if (!mCursor.isLast()) {
				while (mCursor.moveToNext()) {
					DataModel images = new DataModel(null,null,null,null,null);
					images.setName(mCursor.getString(0));
					images.setValue(mCursor.getString(1));
					images.setApp_id(mCursor.getString(2));
					images.setAppInfo(mCursor.getString(3));
					images.setApp_type(mCursor.getString(4));
					imageList.add(images);
				}
			}
			db.close();
		} catch (Exception e) {
			Log.e("TITLE NAME", e + "" + e.getMessage());
		}
		return imageList;
	}


	public void deleteUsers() {
		SQLiteDatabase db = this.getWritableDatabase();
		// Delete All Rows
		db.delete(TABLE_USER, null, null);
		db.close();

		Log.d(TAG, "Deleted all user info from sqlite");
	}

}
