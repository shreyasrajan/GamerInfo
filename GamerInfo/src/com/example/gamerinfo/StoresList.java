package com.example.gamerinfo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import parsers.JSONStoresParser;
import entityClasses_Others.StoreAdapter;
import entityClasses_Others.StoreEntity;
import entityClasses_Others.StringLibrary;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class StoresList extends Activity {
    
	ListView lvStoresList;
	StoreAdapter storeAdapter;
	ProgressDialog prgSpinner;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stores_list);
		
		lvStoresList = (ListView)findViewById(R.id.lvStoreList);
		
		prgSpinner = new ProgressDialog(this);
		prgSpinner.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		prgSpinner.setCancelable(false);
		prgSpinner.setMessage(StringLibrary.DIALOG_LOADING_STORE_SEARCH);
		
		Bundle bundle = getIntent().getExtras();
		String radius = bundle.getString(StringLibrary.RADIUS);
		String latLong = bundle.getString(StringLibrary.LAT_LONG);
		
		String url = prepareYelpUrl(radius, latLong);
		
		JSONStoresAsyncTask jsonGames = new JSONStoresAsyncTask();
		jsonGames.execute(url);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.stores_list, menu);
		return true;
	}
  
	private String prepareYelpUrl(String radius,String latLong){
		StringBuilder urlBuilder = new StringBuilder();
		
		urlBuilder.append("http://api.yelp.com/business_review_search?term=");
		urlBuilder.append(StringLibrary.YELP_SEARCH_TERM);
		urlBuilder.append("&categories=");
		urlBuilder.append(StringLibrary.YELP_SEARCH_CATEGORIES);
		urlBuilder.append("&");
		urlBuilder.append(latLong);
		urlBuilder.append("&");
		urlBuilder.append("radius=");
		urlBuilder.append(radius);
		urlBuilder.append("&limit=50");
		urlBuilder.append(";");
		urlBuilder.append("ywsid=");
		urlBuilder.append(StringLibrary.YELP_YWSID);
		
		return urlBuilder.toString();
	}
	
	class JSONStoresAsyncTask extends AsyncTask<String, Void, ArrayList<StoreEntity>>{
		
		@Override
		protected ArrayList<StoreEntity> doInBackground(String... params) {
			InputStream in;
			BufferedReader bfReader;
			StringBuilder stringBuilder;
			String line = null;
			ArrayList<StoreEntity> arLstStores = null;

			try{
				HttpClient httpClient = new DefaultHttpClient();
				HttpGet httpGet = new HttpGet(params[0]);
				HttpResponse httpResponse = httpClient.execute(httpGet);
				in = httpResponse.getEntity().getContent();

				bfReader = new BufferedReader(new InputStreamReader(in));
				stringBuilder = new StringBuilder();
				line = bfReader.readLine();
				while(line != null){
					stringBuilder.append(line);
					line = bfReader.readLine();
				}
				arLstStores = JSONStoresParser.parseStoreList(stringBuilder.toString());
			}
			catch(Exception e){
				e.printStackTrace();
			}
			return arLstStores;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
            
			prgSpinner.show();
		}

		@Override
		protected void onPostExecute(ArrayList<StoreEntity> result) {
			super.onPostExecute(result);
            
			storeAdapter = new StoreAdapter(getBaseContext(), result);
          
			lvStoresList.setAdapter(storeAdapter);
            lvStoresList.setOnItemClickListener(new ListViewItemClick(result));
            
			prgSpinner.dismiss();
		}
	}
	
	class ListViewItemClick implements AdapterView.OnItemClickListener{
        ArrayList<StoreEntity> arLstStores;
        
		public ListViewItemClick(ArrayList<StoreEntity> arLstStores){
			this.arLstStores = arLstStores;
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
			Intent intent = new Intent(getBaseContext(),GameStoreWeb.class);
			
			intent.putExtra(StringLibrary.YELP_STORE_DETAIL_URL, this.arLstStores.get(position).getUrl());
			
			startActivity(intent);
		}
		
	}
}
