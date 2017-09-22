package com.example.gamerinfo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import parsers.JSONGamesParser;
import entityClasses_Others.GameAdapter;
import entityClasses_Others.GameEntity;
import entityClasses_Others.StringLibrary;
import android.os.AsyncTask;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class GamesList extends Activity {
	int platformID;
	String gameName;
	ListView lvGamesList;
	GameAdapter gameAdapter;
	ProgressDialog prgSpinner;
    boolean isPlatformID;
    
	@SuppressLint("SimpleDateFormat")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_games_list);

		lvGamesList = (ListView) findViewById(R.id.lvGamesList);

		Bundle extras = getIntent().getExtras();

		isPlatformID = extras.getBoolean(StringLibrary.ISPLATFORM);

		String url = null;

		if(isPlatformID){
			platformID = extras.getInt(StringLibrary.PLATFORM_ID);
			
			SimpleDateFormat dateFormat = new SimpleDateFormat(StringLibrary.INPUT_DATE_FORMAT);
			Calendar cal = Calendar.getInstance();
			String startDate = dateFormat.format(cal.getTime());
            cal.add(Calendar.MONTH, 2);
            String endDate = dateFormat.format(cal.getTime());
            
            url = preparePlatformSearchURL(platformID, startDate + "|" + endDate);
        }
		else{
			gameName = extras.getString(StringLibrary.GAME_NAME);
			url = prepareGameSearchURL(gameName);
		}
        
		prgSpinner = new ProgressDialog(this);
		prgSpinner.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		prgSpinner.setCancelable(false);
		
		JSONGamesAsyncTask jsonGames = new JSONGamesAsyncTask();
		jsonGames.execute(url);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.games_list, menu);
		return true;
	}

	private String prepareGameSearchURL(String gameName){
		StringBuilder urlBuilder = new StringBuilder();
		urlBuilder.append("http://www.giantbomb.com/api/search/");
		urlBuilder.append("?api_key=");
		urlBuilder.append(StringLibrary.GAME_API_KEY);
		urlBuilder.append("&format=json");
		urlBuilder.append("&query=");
		urlBuilder.append("%22");
		urlBuilder.append(gameName.replace(" ", "%20"));
		urlBuilder.append("%22");
		urlBuilder.append("&resources=game");
		return urlBuilder.toString();
	}
    
	private String preparePlatformSearchURL(int platformID,String dates){
		StringBuilder urlBuilder = new StringBuilder();
		urlBuilder.append("http://www.giantbomb.com/api/games/");
		urlBuilder.append("?api_key=");
		urlBuilder.append(StringLibrary.GAME_API_KEY);
		urlBuilder.append("&format=json");
		urlBuilder.append("&filter=original_release_date");
		urlBuilder.append("%3A");
		urlBuilder.append(dates.replace("|", "%7C"));
		urlBuilder.append("%2C");
		urlBuilder.append("platforms");
		urlBuilder.append("%3A");
		urlBuilder.append(platformID);
		urlBuilder.append("&sort=orginal_release_date");
		urlBuilder.append("%3A");
		urlBuilder.append("desc");
		urlBuilder.append("&resources=game");
		urlBuilder.append("&limit=50");
		
		return urlBuilder.toString();
	}
	
	class JSONGamesAsyncTask extends AsyncTask<String, Void, ArrayList<GameEntity>>{

		@Override
		protected ArrayList<GameEntity> doInBackground(String... params) {
			InputStream in;
			BufferedReader bfReader;
			StringBuilder stringBuilder;
			String line = null;
			ArrayList<GameEntity> gameArr = null;

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
				gameArr = JSONGamesParser.parseGamesList(stringBuilder.toString());
			}
			catch(Exception e){
				e.printStackTrace();
			}
			return gameArr;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
            
			if(isPlatformID)
				prgSpinner.setMessage(StringLibrary.DIALOG_LOADING_PLATFORM);
			else
				prgSpinner.setMessage(StringLibrary.DIALOG_LOADING);
			
			prgSpinner.show();
		}

		@Override
		protected void onPostExecute(ArrayList<GameEntity> result) {
			super.onPostExecute(result);
            
			gameAdapter = new GameAdapter(getBaseContext(), result);
          
			lvGamesList.setAdapter(gameAdapter);
            lvGamesList.setOnItemClickListener(new ListViewItemClick(result));
            
			prgSpinner.dismiss();
		}
	}
	
	class ListViewItemClick implements AdapterView.OnItemClickListener{
        ArrayList<GameEntity> arLstGames;
        
		public ListViewItemClick(ArrayList<GameEntity> arLstGames){
			this.arLstGames = arLstGames;
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
			Bundle bundle = new Bundle();
			
			Intent intent = new Intent(getBaseContext(),GameDetails.class);
			
			bundle.putString(StringLibrary.THUMBNAIL_URL, this.arLstGames.get(position).getThumbNailUrl());
			bundle.putString(StringLibrary.API_DETAIL_URL, this.arLstGames.get(position).getDetailURL());
			
			intent.putExtras(bundle);
			
			startActivity(intent);
		}
		
	}
}
