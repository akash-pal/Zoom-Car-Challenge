package com.akashpal.zoom;

import java.util.ArrayList;
import java.util.HashMap;

import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class HistoryAdapter extends BaseAdapter {


	private Activity activity;
    private static LayoutInflater inflater=null;
    ListView lv;
	// Hashmap for ListView
    private ArrayList<HashMap<String, String>> data;
    
	public HistoryAdapter(Activity a,ArrayList<HashMap<String, String>> d) {
        Log.d("DATA",d+"");
		activity = a;
		data = d;
		inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		 View view=convertView;
	     if(convertView==null)
	       view = inflater.inflate(R.layout.single_booking_history, null);
	       

		  // Displaying all values on the screen
		    TextView lblName = (TextView) view.findViewById(R.id.book_name_label);
		    TextView lblType = (TextView) view.findViewById(R.id.booked_label);
		    ImageView imageView = (ImageView) view.findViewById(R.id.book_image);
	        
		    HashMap<String, String> map = data.get(position);	
			Log.d("SKY",map+"");
			
			
			lblName.setText(map.get(DatabaseHandler.KEY_NAME));
	        lblType.setText(map.get("description"));
	        Uri imageUri =Uri.parse(map.get(DatabaseHandler.KEY_IMAGE_URL));
			Picasso.with(activity).load(imageUri.toString()).into(imageView);
	        
		return view;
	}

}
