package com.akashpal.zoom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity  {
	
	
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
	
	
	// Hashmap for ListView
    ArrayList<HashMap<String, String>> carslist = new ArrayList<HashMap<String,String>>();		
    ArrayList<HashMap<String, String>> list= new ArrayList<HashMap<String,String>>();	
    HashMap<String, String> map;
    ListView lv;
    LazyAdapter adapter;
    Button sort_rate;
    Button sort_rating;
    EditText mSearch;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		
		sort_rate = (Button) findViewById(R.id.sort_rate); 
		sort_rating = (Button) findViewById(R.id.sort_rating); 
		mSearch = (EditText) findViewById(R.id.inputSearch);
		mSearch.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				final ArrayList<HashMap<String, String>> filterlist = new ArrayList<HashMap<String, String>>();
				String search = mSearch.getText().toString();
				
				if(!search.isEmpty())
				{
					for(int i=0;i<carslist.size();i++)
					{
						String searchName = carslist.get(i).get(TAG_NAME);
						if(searchName.contains(search)){
							filterlist.add(carslist.get(i));
						}
					}
					
					list = filterlist;
                    listupdate(list);
	
				}else{
					list=carslist;
					listupdate(list); 
				}
								
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
		
		
		// call AsynTask to perform network operation on separate thread
		//https://api.myjson.com/bins/1s6jk
		
		//new HttpAsyncTask().execute("https://zoomcar.0x10.info/api/zoomcar?type=json&query=list_cars");
		new HttpAsyncTask().execute("http://api.myjson.com/bins/29s14");
	}
	
    

	public static String GET(String url){
		InputStream inputStream = null;
		String result = "";
		try {
			
			// create HttpClient
			HttpClient httpclient = new DefaultHttpClient();
			
			// make GET request to the given URL
			HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
			
			// receive response as inputStream
			inputStream = httpResponse.getEntity().getContent();
			
			// convert inputstream to string
			if(inputStream != null)
				result = convertInputStreamToString(inputStream);
			else
				result = "Did not work!";
		
		} catch (Exception e) {
			Log.d("InputStream", e.getLocalizedMessage());
		}
		
		return result;
	}
	
    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;
        
        inputStream.close();
        return result;
        
    }
	
  
    private class HttpAsyncTask extends AsyncTask<String, Void, String> 
    {
        @Override
        protected String doInBackground(String... urls) {
              
            return GET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
        	Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
        	try {
				  // Create the root JSONObject from the JSON string.  
                JSONObject  jsonRootObject = new JSONObject(result);  

                //Get the instance of JSONArray that contains JSONObjects  
                 JSONArray jsonArray = jsonRootObject.optJSONArray(TAG_CARS); 
                 
                 
                 //Iterate the jsonArray and print the info of JSONObjects  
                 for(int i=0; i < jsonArray.length(); i++)
                 {  
                     JSONObject jsonObject = jsonArray.getJSONObject(i);  
                        
                     String name = jsonObject.getString(TAG_NAME).toString();
                     String image = jsonObject.getString(TAG_IMAGE).toString();
                     
                     String type = jsonObject.getString(TAG_TYPE).toString();
                     String hourly_rate = jsonObject.getString(TAG_RATE).toString();
                     String rating = jsonObject.getString(TAG_RATING).toString();
                     String seater = jsonObject.getString(TAG_SEATER).toString();
                     String ac = jsonObject.getString(TAG_AC).toString();
                    
                     JSONObject location = jsonObject.getJSONObject(TAG_LOCATION);
                     
                     String lat = location.getString(TAG_LAT).toString();
                     String lon = location.getString(TAG_LON).toString();
                     
                     
                     // creating new HashMap
     				 map = new HashMap<String, String>();
     				
     			     // adding each child node to HashMap key => value
     				 map.put(TAG_NAME, name);
     				 map.put(TAG_IMAGE,image);
     				 map.put(TAG_TYPE,type);
     				 map.put(TAG_RATE,hourly_rate);
     				 map.put(TAG_RATING, rating);
     				 map.put(TAG_SEATER,seater);
     				 map.put(TAG_AC,ac);
     				 map.put(TAG_LAT,lat);
     				 map.put(TAG_LON,lon);
     				
                     carslist.add(map); 				
                 }               

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        	/**
    		 * Updating parsed JSON data into ListView
    		 * */
        	list = carslist;
        	listupdate(list);
        	        	
       }//end of onPost execute.
            
    }//end of http async task
    
    
    
    public void listupdate(final ArrayList<HashMap<String, String>> data){
    	lv=(ListView)findViewById(R.id.list);
    	
        adapter=new LazyAdapter(MainActivity.this, data);
        lv.setAdapter(adapter);
    	
    	
    	lv.setOnItemClickListener(new OnItemClickListener() {

    		
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				Log.d("SKY","data");
				// getting values from selected ListItem
				String name = ((TextView) view.findViewById(R.id.name)).getText().toString();
				String type = ((TextView) view.findViewById(R.id.rate)).getText().toString();
				
			    map = data.get(position);	
				//Log.d("SKY",map+"");
				
				
				// Starting new intent
				Intent in = new Intent(MainActivity.this, SingleMenuItemActivity.class);
				in.putExtra(TAG_NAME,map.get(TAG_NAME) );
				in.putExtra(TAG_TYPE, map.get(TAG_TYPE));
				in.putExtra(TAG_RATING, map.get(TAG_RATING));
				in.putExtra(TAG_IMAGE, map.get(TAG_IMAGE));
				in.putExtra(TAG_RATE,map.get(TAG_RATE));
				in.putExtra(TAG_SEATER,map.get(TAG_SEATER));
				in.putExtra(TAG_AC,map.get(TAG_AC));
				in.putExtra(TAG_LAT,map.get(TAG_LAT));
				in.putExtra(TAG_LON,map.get(TAG_LON));
				startActivity(in);
			}
    		
		});

    }
    
    
    public void sortRate(View view){
    	
    	Toast.makeText(this, "Sorted Rate", Toast.LENGTH_SHORT).show();
    	Collections.sort(list, new Comparator<HashMap<String,String>>(){

			@Override
			public int compare(HashMap<String, String> lhs,
					HashMap<String, String> rhs) {
				return lhs.get(TAG_RATE).compareTo(rhs.get(TAG_RATE));
			}
    		
    	});
    	
    	listupdate(list);
    }
    
    public void sortRating(View view){
    	
	    	Toast.makeText(this, "Sorted Rating", Toast.LENGTH_SHORT).show();
	    	Collections.sort(list, new Comparator<HashMap<String,String>>(){
	
				@Override
				public int compare(HashMap<String, String> lhs,
						HashMap<String, String> rhs) {
					return lhs.get(TAG_RATING).compareTo(rhs.get(TAG_RATING));
				}
	    		
	    	});
	    	
	    	listupdate(list);
    }
    
    
}
