package com.akashpal.zoom;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {

	// DataBase info:
	private static final int DATABASE_VERSION = 3 ;
	// The version number must be incremented each time a change to DB structure occurs.
	private static final String DATABASE_NAME = "BookingHistory";
	private static final String TABLE_NAME = "Book";
	
	// Field Names:
	public static final String KEY_ROWID = "_id";
	public static final String KEY_NAME = "name";
	public static final String KEY_BOOKING_DATE = "dt";
	public static final String KEY_BOOKING_TIME = "tm";
	public static final String KEY_IMAGE_URL ="img";
	
	// Column Numbers for each Field Name:
	public static final int COL_ROWID = 0;
	public static final int COL_NAME = 1;
	public static final int COL_BOOKING_DATE = 2;
	public static final int COL_BOOKING_TIME = 3;
    public static final int COL_IMAGE_URL = 4;

	public static final String[] ALL_KEYS = new String[] {KEY_ROWID,KEY_NAME, KEY_BOOKING_DATE, KEY_BOOKING_TIME,KEY_IMAGE_URL};
	
	private SQLiteDatabase db;

	
	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d("DATA","onCreate db");
		
		String CREATE_BOOK_TABLE = "CREATE TABLE " + TABLE_NAME +
		" ( " + 
	    KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
	    KEY_NAME + " TEXT, " +
	    KEY_BOOKING_DATE + " DATETIME, " +
	    KEY_BOOKING_TIME + " DATETIME DEFAULT CURRENT_TIME, " +
	    KEY_IMAGE_URL + " TEXT " +
	    " );" ;
		
		db.execSQL(CREATE_BOOK_TABLE);
		
	}
	

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d("DATA","onUpgrade db");
		
		db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
		
		onCreate(db);
	}
	
	

	// Return all data in the database.
	public Cursor getAllRows() {
		String where = null;

		String selectQuery = "SELECT * FROM " + TABLE_NAME;
		SQLiteDatabase db =  this.getReadableDatabase();
		//Cursor c = 	db.query(true, TABLE_NAME, ALL_KEYS, where, null, null, null, null, null);
		Cursor c = db.rawQuery(selectQuery, null);
		if (c != null) {
			c.moveToFirst();
		}
		return c;
	}
	
	
	void addBook(String name,String date,String img){
		
		Log.d("DATA","addBook");
		
		SQLiteDatabase db =  this.getWritableDatabase();
		
		//SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		//String date1 = sdf.format(date);
		
		ContentValues values = new ContentValues();
		values.put(KEY_NAME,name);
		values.put(KEY_BOOKING_DATE,date);
		values.put(KEY_IMAGE_URL, img);
		
		db.insert(TABLE_NAME, null, values);
		db.close();
	}
	
	
	
	
	public ArrayList<HashMap<String, String>> getBookingHistory() {
		ArrayList<HashMap<String,String>> al = new ArrayList<HashMap<String,String>>(); 
		HashMap<String,String> map;
		
		String selectQuery = "SELECT * FROM " + TABLE_NAME;
		
		SQLiteDatabase db =  this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if(cursor.moveToFirst()){
			do{
				String n = cursor.getString(COL_NAME);
				String d = cursor.getString(COL_BOOKING_DATE);
				String t = cursor.getString(COL_BOOKING_TIME);
				String img = cursor.getString(COL_IMAGE_URL);
				Date now = new Date();
				
				
				Date date = null;
				
				SimpleDateFormat df =new  SimpleDateFormat("dd/MM/yyyy");
				String dayNumberSuffix = getDayNumberSuffix(df.getCalendar().get(Calendar.DAY_OF_MONTH));
				SimpleDateFormat ndf =new  SimpleDateFormat("dd'" + dayNumberSuffix + "' MMMM yyyy");
				
				
				Date time=null; 
				
				SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
                SimpleDateFormat sdfs = new SimpleDateFormat("hh:mm a");
				
				try {
					time = sdf.parse(t);
					date = df.parse(d);
					
					
					sdfs.setTimeZone(TimeZone.getTimeZone("UTC"));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
				//Log.d("DATA",df.format(date)+" "+df.format(now)+"\n");
				int result = df.format(date).compareTo(df.format(now));
				String bookedOn=" ";
				
				if(result!=0){
					bookedOn= " Booked on " + ndf.format(date)+" at "+sdfs.format(time);
					Log.d("DATA", n +" "+bookedOn);	
				}
				else{
                    bookedOn=" Booked on today";
					Log.d("DATA", n +" "+bookedOn);
				}

                // creating new HashMap
				 map = new HashMap<String, String>();
			    map.put(KEY_NAME, n);
			    map.put("description", bookedOn);
			    map.put(KEY_IMAGE_URL, img);
			    al.add(map);
					
			}while(cursor.moveToNext());
		}
		
		return al;
	}
	
	
	private String getDayNumberSuffix(int day) {
	    if (day >= 11 && day <= 13) {
	        return "th";
	    }
	    switch (day % 10) {
	    case 1:
	        return "st";
	    case 2:
	        return "nd";
	    case 3:
	        return "rd";
	    default:
	        return "th";
	    }
	}
	

}
