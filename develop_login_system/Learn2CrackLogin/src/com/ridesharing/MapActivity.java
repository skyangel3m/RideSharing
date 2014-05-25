package com.ridesharing;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ridesharing.library.LocationsDB;
import com.ridesharing.library.UserFunctions;

//Google Map
// latitude and longitude
// Loading map
/** function to load map. If map is not created it will create
	 * it for you */
//check if map is created successfully or not

public class MapActivity extends FragmentActivity implements LocationListener{
	
	private static String KEY_SUCCESS = "success";
    private static String KEY_UID = "uid";
    private static String KEY_LATITUDE = "latitude";
    private static String KEY_LONGITUDE = "longitude";
	
	//Google Map
			private GoogleMap googleMap;
			Location location;
			TextView text; 
			public String message;
			LocationsDB db;
			// latitude and longitude
			public String latitude ="";
			public String longitude ="";
			LocationManager locManager; 
			LocationListener locList;
			public String uid;
			
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		
		locManager = null;
		final LocationsDB db = new LocationsDB(getApplicationContext());
		
		Intent myintent = getIntent();
		Bundle extras = myintent.getExtras();
		if (extras != null)
		{
			uid = extras.getString("uid");
		}


		
		locList = new LocationListener(){
			
			public void onLocationChanged(Location location) {
				Location loc = new Location(location);
				
				longitude = String.valueOf(location.getLongitude());
                latitude = String.valueOf(location.getLatitude());

				Toast.makeText(MapActivity.this,"Latitude = "+
	                    latitude + "" +"Longitude = "+ longitude,
	                    Toast.LENGTH_LONG).show();
				
				
			}
			
			public void onStatusChanged(String provider, int status, Bundle extras){}
			public void onProviderEnabled(String provider) {}
			public void onProviderDisabled(String provider){}

		};
		
		message = "Hello";
		
		text = (TextView)findViewById(R.id.textview);
		
		
		locManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,this);
		
		Criteria criteria = new Criteria();
		String provider = locManager.getBestProvider(criteria, false);
		
		if (provider!=null && !provider.equals("")) {
			Location location = locManager.getLastKnownLocation(provider);
			
			if (location != null){
				longitude = String.valueOf(location.getLongitude());
                latitude = String.valueOf(location.getLatitude());
				locList.onLocationChanged(location);
			
			}
			else
				locList.onLocationChanged(location);
		}
		else {
			Toast.makeText(getBaseContext(), "No Provider Found", Toast.LENGTH_SHORT).show();
		}
		
		
		googleMap = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
		googleMap.setMyLocationEnabled(true);
		googleMap.getUiSettings().setMyLocationButtonEnabled(true);
		

		int errorCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if (errorCode != ConnectionResult.SUCCESS) {
		  GooglePlayServicesUtil.getErrorDialog(errorCode, this, 0).show();
		}
	 
		
		try {
			// Loading map
			initializeMap();
			showOtherUsers();
			new ProcessUserLocation().execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
     * Async Task to get and send data to My Sql database through JSON respone.
     **/
    private class ProcessUserLocation extends AsyncTask<String, String, JSONObject> {

        

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            }

        @Override
        protected JSONObject doInBackground(String... args) {

            UserFunctions userFunction = new UserFunctions();
            JSONObject json = userFunction.locationUser(uid, latitude, longitude);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            try {
               if (json.getString(KEY_SUCCESS) != null) {

                    String res = json.getString(KEY_SUCCESS);

                    if(Integer.parseInt(res) == 1){
                        
                        LocationsDB db = new LocationsDB(getApplicationContext());
                        JSONObject json_userlocation = json.getJSONObject("userlocation");
                        uid = json_userlocation.getString(KEY_UID);
                        latitude = json_userlocation.getString(KEY_LATITUDE);
                        longitude = json_userlocation.getString(KEY_LONGITUDE);
                        /**
                         * update location information on mySQL database
                         **/
                        
                        db.updateDatabase(uid, latitude, longitude);
                        
                        
                        /**
                         * Close Login Screen
                         **/
                        finish();
                    }else{

                    	Toast.makeText(getBaseContext(), "Cannot update location", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
       }
    }
	
	/** function to load map. If map is not created it will create
	 * it for you */ 
	private void initializeMap() {
		 if (googleMap == null) {
			 googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
					 .getMap();
			 
			 //check if map is created successfully or not
			 if (googleMap == null){
				 Toast.makeText(getApplicationContext(),
						 "Sorry! unable to create maps", Toast.LENGTH_SHORT)
						 .show();
			 }
		 }
	}
	
	private void showOtherUsers() {
		
		LocationsDB db = new LocationsDB(getApplicationContext());

		MarkerOptions mp = new MarkerOptions();

        HashMap<String,String> userlocation = new HashMap<String, String>();
        userlocation = db.getUserDetails();
		
        double other_lat = Double.parseDouble(userlocation.get(latitude));
        double other_long = Double.parseDouble(userlocation.get(longitude));
        
		mp.position(new LatLng(other_lat, other_long));
		mp.title("my position");

		googleMap.addMarker(mp);
	}
	
	
	public void onLocationChanged(Location location) {

	googleMap.clear();

	Toast.makeText(getApplicationContext(), uid,
			   Toast.LENGTH_SHORT).show();
	
	//db.updateDatabase(uid,latitude, longitude);

	googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
	new LatLng(location.getLatitude(), location.getLongitude()), 16));
	
	}

	public void onProviderDisabled(String provider) {
	// TODO Auto-generated method stub

	}

	public void onProviderEnabled(String provider) {
	// TODO Auto-generated method stub

	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
	// TODO Auto-generated method stub
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
		return true;}

}

	
	

