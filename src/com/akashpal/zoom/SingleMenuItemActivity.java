package com.akashpal.zoom;

import java.util.Calendar;

import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;


public class SingleMenuItemActivity  extends FragmentActivity implements OnMapReadyCallback, OnDateSetListener {
	
	public void setActionBarTitle(String title) {
	    getActionBar().setTitle("Booking Details");
	}
	
	// JSON node keys
	private static final String TAG_CARS = "cars";
	private static final String TAG_NAME = "name";
	private static final String TAG_IMAGE = "image";
	private static final String TAG_TYPE = "type";
	private static final String TAG_RATE = "hourly_rate";
	private static final String TAG_RATING = "rating";
	private static final String TAG_SEATER = "seater";
	private static final String TAG_AC = "ac";
	private static final String TAG_LOCATION="location";
	private static final String TAG_LON = "longitude";
	private static final String TAG_LAT = "latitude";
	Float lat;
	Float lon;
	String name,image;
	
	// Google Map
    private GoogleMap map;
    TextView datetxt;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_list_item);
        
        // getting intent data
        Intent in = getIntent();
        
        // Get JSON values from previous intent
        name = in.getStringExtra(TAG_NAME);
        String type = in.getStringExtra(TAG_TYPE);
        String rating = in.getStringExtra(TAG_RATING);
        image = in.getStringExtra(TAG_IMAGE);
        String rate = in.getStringExtra(TAG_RATE);
        String seater = in.getStringExtra(TAG_SEATER);
        String ac = in.getStringExtra(TAG_AC);
        lat = Float.parseFloat(in.getStringExtra(TAG_LAT));
        lon = Float.parseFloat(in.getStringExtra(TAG_LON));
        
        // Displaying all values on the screen
        TextView lblName = (TextView) findViewById(R.id.name_label);
        TextView lblType = (TextView) findViewById(R.id.rate_label);
        TextView lblSeater = (TextView) findViewById(R.id.seater_label);
        TextView lblAc = (TextView) findViewById(R.id.ac_label);
        
		ImageView imageView = (ImageView) findViewById(R.id.image);
		Uri imageUri =Uri.parse(image);
		Picasso.with(this).load(imageUri.toString()).into(imageView);
        
        lblName.setText(name);
        lblType.setText(rate +" per hr");
        lblSeater.setText("Seater: " + seater);
        if(Integer.parseInt(ac)==1)
           lblAc.setText("AC: yes");
        else
        	lblAc.setText("AC: no");
		
		RatingBar rb = (RatingBar) findViewById(R.id.ratingBar2);
        rb.setRating(Float.parseFloat(rating));
        
        
        datetxt = (TextView) findViewById(R.id.datetxt);
        final Calendar cal = Calendar.getInstance();
        int dd = cal.get(Calendar.DAY_OF_MONTH);
        int mm = cal.get(Calendar.MONTH);
        int yy = cal.get(Calendar.YEAR);
        
        // set current date into textview
	    cal.set(yy, mm, dd);
	    CharSequence output = DateFormat.format("dd/MM/yyyy", cal);
	    datetxt.setText(output);
	    
        datetxt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				DialogFragment newFragment = new DatePickerFragment();
			    newFragment.show(getSupportFragmentManager(), "datePicker");
			}
		});
        
        
        
        
        initialize();
    }

	
	
	private void initialize() {

		MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        map = mapFragment.getMap();
	}

	@Override
	public void onMapReady(GoogleMap arg0) {
		
		// latitude and longitude
		double latitude = lat;
		double longitude = lon ;
		 
		
		// create marker
		MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title(name);
		
		// GREEN color icon
		marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

		// adding marker
		map.addMarker(marker);		
		
		CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(latitude, longitude)).zoom(16).build();
 
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
	}
	
	@Override
	protected void onResume() {
		super.onResume();
        initialize();		
	}


	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		 // set current date into textview
		Calendar cal = Calendar.getInstance();
	    cal.set(year, monthOfYear, dayOfMonth);
	    CharSequence output = DateFormat.format("dd/MM/yyyy", cal);
	    datetxt.setText(output);
	}
	
	public void bookingHistory(View v){
	    Toast.makeText(this, "booking history", Toast.LENGTH_SHORT).show();	
	    //DatabaseHandler db = new DatabaseHandler(this);
	    //db.getBookingHistory();
	    Intent i = new Intent(SingleMenuItemActivity.this,BookingHistoryActivity.class);
	    startActivity(i);
	}
	
    public void book(View v){
	    Toast.makeText(this, "book", Toast.LENGTH_SHORT).show();
        Log.d("SKY",datetxt.getText().toString());
        
        DatabaseHandler db = new DatabaseHandler(this);
        db.addBook(name,datetxt.getText().toString(),image);
	}

}
