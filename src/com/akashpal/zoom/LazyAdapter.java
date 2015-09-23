package com.akashpal.zoom;

import java.util.ArrayList;
import java.util.HashMap;

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
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class LazyAdapter extends BaseAdapter {
	

	private Activity activity;
    private static LayoutInflater inflater=null;
    ListView lv;
	// Hashmap for ListView
    private ArrayList<HashMap<String, String>> data;
    

    
	public LazyAdapter(Activity a,ArrayList<HashMap<String, String>> d) {
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
	      view = inflater.inflate(R.layout.list_item, null);
	      
	     
	  // Displaying all values on the screen
	        TextView lblName = (TextView) view.findViewById(R.id.name);
	        TextView lblType = (TextView) view.findViewById(R.id.rate);
	        ImageView imageView = (ImageView) view.findViewById(R.id.image);
	        
			
	        
	        
	        HashMap<String, String> map = data.get(position);	
			Log.d("SKY",map+"");
			
			
			lblName.setText(map.get("name"));
	        lblType.setText(map.get("hourly_rate")+" per hr");
	        Uri imageUri =Uri.parse(map.get("image"));
			Picasso.with(activity).load(imageUri.toString()).into(imageView);
	        
			Float rating = Float.parseFloat(map.get("rating"));
			
			RatingBar rb = (RatingBar) view.findViewById(R.id.ratingBar1);
	        rb.setRating(rating);
	        
			/*
			// Starting new intent
			Intent in = new Intent(MainActivity.this, SingleMenuItemActivity.class);
			in.putExtra(TAG_NAME, name);
			in.putExtra(TAG_TYPE, type);
			in.putExtra(TAG_RATING, rating);
			in.putExtra(TAG_IMAGE, map.get(TAG_IMAGE));
			startActivity(in);
	         */
			/*
			view.setOnClickListener(new OnClickListener() {

			    @Override
			    public void onClick(View v) {
                    int pos = (int) v.getTag();
                    Toast.makeText(activity, "CLICKED:"+pos,Toast.LENGTH_SHORT).show();        
			    }
			});	
			*/
	     
	      return view;  
	}

}
