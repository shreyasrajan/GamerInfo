package com.example.gamerinfo;

import java.text.DecimalFormat;
import java.util.ArrayList;
import entityClasses_Others.StringLibrary;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class GameStoreSearch extends Activity {
	Button btnStoreSearch;
	Spinner spRadius;
	LocationManager locationManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_store_search);

		btnStoreSearch = (Button)findViewById(R.id.btnStoreSearch);
		btnStoreSearch.setOnClickListener(new RadiusButtonClick());

		spRadius = (Spinner)findViewById(R.id.spnRadius);

		locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

		bindSpinner();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.game_store_search, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		if(item.getItemId() == R.id.action_setupUser)
			startUserActivity();

		return super.onOptionsItemSelected(item);
	}

	private void startUserActivity(){
		Intent intent = new Intent(getBaseContext(),UserConfig.class);
		startActivity(intent);
		finish();
	}

	private void bindSpinner(){
		ArrayList<String> arLstRadius = new ArrayList<String>();
		arLstRadius.add(StringLibrary.STORE_SEARCH_2);
		arLstRadius.add(StringLibrary.STORE_SEARCH_5);
		arLstRadius.add(StringLibrary.STORE_SEARCH_10);
		arLstRadius.add(StringLibrary.STORE_SEARCH_15);
		arLstRadius.add(StringLibrary.STORE_SEARCH_25);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,arLstRadius);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spRadius.setAdapter(adapter);
	}
    
	private String prepareLatLongString(String lat,String longitude){
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("lat=");
		stringBuilder.append(lat);
		stringBuilder.append("&");
		stringBuilder.append("long=");
		stringBuilder.append(longitude);
		return stringBuilder.toString();
	}
	
	class RadiusButtonClick implements View.OnClickListener{
		String latitude,longitude;
        DecimalFormat doubleFormat;
        
		@Override
		public void onClick(View v) {

			String radius = spRadius.getSelectedItem().toString();
			if(radius.equals(StringLibrary.SELECT)){
				Toast.makeText(GameStoreSearch.this, StringLibrary.RADIUS_ERROR, Toast.LENGTH_LONG).show();
				return;
			}
			
			boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            
			doubleFormat = new DecimalFormat(StringLibrary.DOUBLE_FORMAT);
			
			String provider = LocationManager.GPS_PROVIDER;
			if(isGPSEnabled){
				Location location = locationManager.getLastKnownLocation(provider);
				if(location != null){
					latitude = doubleFormat.format(location.getLatitude());
					longitude = doubleFormat.format(location.getLongitude());
				}
				else{
					LocationListener listener = new LocationListener() {

						@Override
						public void onStatusChanged(String provider, int status, Bundle extras) {
						}

						@Override
						public void onProviderEnabled(String provider) {
						}

						@Override
						public void onProviderDisabled(String provider) {
						}

						@Override
						public void onLocationChanged(Location location) {
							latitude = doubleFormat.format(location.getLatitude());
							longitude = doubleFormat.format(location.getLongitude());
						}
					};

					locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
				}
			}
			else{
				Toast.makeText(GameStoreSearch.this, StringLibrary.GPS_NOT_ENABLED, Toast.LENGTH_SHORT).show();
				return;
			}
			
			Intent intent = new Intent(GameStoreSearch.this,StoresList.class);
			
			Bundle bundle = new Bundle();
			bundle.putString(StringLibrary.RADIUS, radius);
			bundle.putString(StringLibrary.LAT_LONG, prepareLatLongString(latitude, longitude));
			
			intent.putExtras(bundle);
			startActivity(intent);
		}

	}
}
