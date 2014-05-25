package com.ridesharing.library;

import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LocationsDB {
	
	private Context context;
	private DbHelper dbHelper;
	private SQLiteDatabase db;
	private static String DBNAME = "1510674_srdata";
	private static int VERSION = 10;
	public static final String KEY_ID = "id";
	public static final String KEY_UID = "uid";
	public static final String KEY_LATITUDE = "latitude";
	public static final String KEY_LONGITUDE = "longitude";
	private static final String DB_TABLE_REF = "users";
	private static final String DB_TABLE = "userlocation";
	private static final String KEY_GCM_REGID = "gcm_regid";
	
	
	public LocationsDB(Context context) {
		this.context = context;
		dbHelper = new DbHelper(context);
		
	}
	
	public class DbHelper extends SQLiteOpenHelper {
		public DbHelper(Context context) {
			super(context, DBNAME, null, VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			
			
			String sql = "CREATE TABLE " + DB_TABLE + "(" +
					KEY_ID + " TEXT PRIMARY KEY, " +
					KEY_UID + " INTEGER, " + 
					KEY_LATITUDE + " TEXT, " +
					KEY_LONGITUDE + " TEXT, " + 
					KEY_GCM_REGID + " TEXT, " +
					" constraint USERLOC_IND1 foreign key("+KEY_UID+") references"+DB_TABLE_REF+"("+KEY_UID+")" +")";
			db.execSQL(sql);
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			
			// TODO Auto-generated method stub
		}
		
	}
	/*
	public void insertRows(String lat_s, String log_s, double latitude, double longitude) {
		ContentValues value = new ContentValues();
		String latitude_s = String.valueOf(latitude);
		String longitude_s = String.valueOf(longitude);
		value.put(lat_s, latitude_s);
		value.put(log_s, longitude_s);
		db.insert(DB_TABLE, null, value);
	} */
	
	public Cursor getAllRows() {
		Cursor cursor = db.query(DB_TABLE, new String[]{KEY_ID, KEY_UID, KEY_LATITUDE, KEY_LONGITUDE, KEY_GCM_REGID},
		null, null, null, null, null);
		return cursor;
	}
	
	
	public void updateDatabase(String id, String latitude, String longitude) {
		
		db = dbHelper.getWritableDatabase();
		ContentValues value = new ContentValues();
		value.put(KEY_UID, id);
		value.put(KEY_LATITUDE, latitude);
		value.put(KEY_LONGITUDE, longitude);
		
		db.update(DB_TABLE, value, KEY_UID + "=?" , new String[] {id} );
	
		dbHelper.close();
		
	}
	
	public void getRegid(String id, String gcm_regid) {
		
		db = dbHelper.getWritableDatabase();
		ContentValues value = new ContentValues();
		value.put(KEY_UID, id);
		value.put(KEY_GCM_REGID, gcm_regid);
		
		db.update(DB_TABLE, value, KEY_UID + "=?" , new String[] {id} );
	
		dbHelper.close();
		
	}
	
	public HashMap<String, String> getUserDetails(){
        HashMap<String,String> user = new HashMap<String,String>();
        String selectQuery = "SELECT  * FROM " + DB_TABLE;

        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
        	user.put("id", cursor.getString(1));
            user.put("uid", cursor.getString(2));
            user.put("latitude", cursor.getString(3));
            user.put("longitude", cursor.getString(4));
            user.put("gcm_regid", cursor.getString(5));
        }
        cursor.close();
        db.close();
        // return user
        return user;
    }
	

	
}
	
	
