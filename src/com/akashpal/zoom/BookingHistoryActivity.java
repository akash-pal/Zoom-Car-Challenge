package com.akashpal.zoom;

import java.util.ArrayList;
import java.util.HashMap;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class BookingHistoryActivity extends ActionBarActivity {

	DatabaseHandler myDb;

	ArrayList<HashMap<String,String>> al = new ArrayList<HashMap<String,String>>(); 
	HistoryAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_booking_history);
		myDb = new DatabaseHandler(this);
		populateListView();
		
	}
	
	private void populateListView()
	{
		
		al = myDb.getBookingHistory();
    	Log.d("DATA", al+" ");
        adapter=new HistoryAdapter(BookingHistoryActivity.this, al);
    	
		/*
		Cursor cursor = myDb.getAllRows();
	
		//Setup mapping from cursor to view fields. 
		String[] fromFieldNames = new String[]
		        {DatabaseHandler.KEY_NAME,DatabaseHandler.KEY_BOOKING_DATE,DatabaseHandler.KEY_BOOKING_TIME};
		int[] toViewIDs = new int[] {R.id.book_name_label,R.id.book_date_label,R.id.book_time_label};
		
		SimpleCursorAdapter myCursorAdapter;
		myCursorAdapter = new SimpleCursorAdapter(getBaseContext(), R.layout.single_booking_history, cursor, fromFieldNames, toViewIDs, 0); 
		*/
		ListView myList = (ListView) findViewById(R.id.list_history);
		myList.setAdapter(adapter);
		 
	}


}
