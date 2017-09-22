package com.example.gamerinfo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import parsers.JSONGamesParser;
import entityClasses_Others.GameDetailEntity;
import entityClasses_Others.GameListEntity;
import entityClasses_Others.StringLibrary;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import asynctask_and_parseHandlers.BitmapDownloadAsyncTask;

public class GameDetails extends Activity {
    
	TextView tvDetRelDate;
    TextView tvDetPlatform;
    TextView tvDetGenres;
    TextView tvDetGameRat;
    TextView tvDetDev;
    TextView tvDetPublisher;
	TextView tvDetGameVal;
    TextView tvDetRelDateVal;
    TextView tvDetPlatformVal;
    TextView tvDetGenresVal;
    TextView tvDetGameRatVal;
    TextView tvDetDevVal;
    TextView tvDetPublisherVal;
    ImageView imVwDetGame;
    Button btnAddWishList;
    Button btnRemWishList;
    ProgressDialog prgSpinner;
    String thumbNailURL;
    String detailURL;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_details);
		
		Parse.initialize(this, StringLibrary.PARSE_APP_ID, StringLibrary.PARSE_CLIENT_KEY);
		ParseAnalytics.trackAppOpened(getIntent());
		
		setControlParams();
		
		detailURL = getIntent().getExtras().getString(StringLibrary.API_DETAIL_URL);
		detailURL += "?api_key=";
		detailURL += StringLibrary.GAME_API_KEY;
		detailURL += "&format=json";
		
		thumbNailURL = getIntent().getExtras().getString(StringLibrary.THUMBNAIL_URL);
		
		prgSpinner = new ProgressDialog(this);
		prgSpinner.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		prgSpinner.setCancelable(false);
		prgSpinner.setMessage(StringLibrary.DIALOG_LOADING_DETAIL);
		
		JSONGameAsyncTask jsonGames = new JSONGameAsyncTask();
		jsonGames.execute(detailURL);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.game_details, menu);
		return true;
	}
    
	private void setControlParams(){
		btnAddWishList = (Button)findViewById(R.id.btnAddWishList);
		btnRemWishList = (Button)findViewById(R.id.btnRemWishList);
		
		tvDetRelDate = (TextView)findViewById(R.id.tvDetReleaseDate);
		tvDetPlatform = (TextView)findViewById(R.id.tvDetPlatform);
		tvDetGenres = (TextView)findViewById(R.id.tvDetGenres);
		tvDetGameRat = (TextView)findViewById(R.id.tvDetRating);
		tvDetDev = (TextView)findViewById(R.id.tvDetDeveloper);
		tvDetPublisher = (TextView)findViewById(R.id.tvDetPublisher);
		
		imVwDetGame = (ImageView)findViewById(R.id.imVwDetGame);
		tvDetGameVal = (TextView)findViewById(R.id.tvDetGameName);
		tvDetRelDateVal = (TextView)findViewById(R.id.tvDetReleaseDateVal);
		tvDetPlatformVal = (TextView)findViewById(R.id.tvDetPlatformVal);
		tvDetGenresVal = (TextView)findViewById(R.id.tvDetGenresVal);
		tvDetGameRatVal = (TextView)findViewById(R.id.tvDetRatingVal);
		tvDetDevVal = (TextView)findViewById(R.id.tvDetDeveloperVal);
		tvDetPublisherVal = (TextView)findViewById(R.id.tvDetPublisherVal);
		
		tvDetRelDate.setVisibility(View.INVISIBLE);
		tvDetPlatform.setVisibility(View.INVISIBLE);
		tvDetGenres.setVisibility(View.INVISIBLE);
		tvDetGameRat.setVisibility(View.INVISIBLE);
		tvDetDev.setVisibility(View.INVISIBLE);
		tvDetPublisher.setVisibility(View.INVISIBLE);
		btnAddWishList.setVisibility(View.INVISIBLE);
		btnRemWishList.setVisibility(View.INVISIBLE);
	}
	
	class ButtonClick implements View.OnClickListener{
		GameDetailEntity gameDetObj;
		String userObjectId;
		
		public ButtonClick(GameDetailEntity gameDetObj,String userObjectId){
			this.gameDetObj = gameDetObj;
			this.userObjectId = userObjectId;
		}
		
		@Override
		public void onClick(View v) {
			ParseObject parseGameObj;
			
			int buttonId = v.getId();
			if(buttonId == R.id.btnAddWishList){
				parseGameObj = new ParseObject(StringLibrary.PARSE_WL_TABLE);
				parseGameObj.put(StringLibrary.PARSE_WL_USEROBJID, this.userObjectId);
				parseGameObj.put(StringLibrary.PARSE_WL_GAMENAME,this.gameDetObj.getGameName());
				parseGameObj.put(StringLibrary.PARSE_WL_THUMBNAIL, thumbNailURL);
				parseGameObj.put(StringLibrary.PARSE_WL_DETAIL, detailURL);
				parseGameObj.put(StringLibrary.PARSE_WL_RELEASEDATE,this.gameDetObj.getReleaseDate());
				parseGameObj.put(StringLibrary.PARSE_WL_PLATFORM,this.gameDetObj.getPlatform());
				parseGameObj.saveInBackground();
				
				Toast.makeText(GameDetails.this, StringLibrary.WISHLIST_ADD_SUCCESS, Toast.LENGTH_SHORT).show();
				
				btnAddWishList.setVisibility(View.INVISIBLE);
				btnRemWishList.setVisibility(View.VISIBLE);
			}
			if(buttonId == R.id.btnRemWishList){
				ParseQuery<ParseObject> query = ParseQuery.getQuery(StringLibrary.PARSE_WL_TABLE);
				query.whereEqualTo(StringLibrary.PARSE_WL_USEROBJID, userObjectId);
				query.whereEqualTo(StringLibrary.PARSE_WL_GAMENAME, this.gameDetObj.getGameName());
				query.findInBackground(new FindCallback<ParseObject>() {

					@Override
					public void done(List<ParseObject> objects, ParseException e) {
						if(e == null){
							if(objects.size() > 0){
								ParseObject deleteObject = objects.get(0);
								deleteObject.deleteInBackground();
								
								Toast.makeText(GameDetails.this, StringLibrary.WISHLIST_DEL_SUCCESS, Toast.LENGTH_SHORT).show();
								
								btnRemWishList.setVisibility(View.INVISIBLE);
								btnAddWishList.setVisibility(View.VISIBLE);
								
								GameListEntity.gameName = deleteObject.getString(StringLibrary.PARSE_WL_GAMENAME);
							}
						} 
						else
							e.printStackTrace();
					}
				});
			}
		}
		
	}
	
	private void checkAndEnableButtons(String userName,GameDetailEntity gameDetObj){
		ParseQuery<ParseObject> query = ParseQuery.getQuery(StringLibrary.PARSE_USER_TABLE);
		query.whereEqualTo(StringLibrary.PARSE_USERNAME, userName);
		query.findInBackground(new ParseCallback(gameDetObj));
	}
	
	class ParseCallback extends FindCallback<ParseObject>{
        GameDetailEntity gameObj;
        String userObjectId;
        
		public ParseCallback(GameDetailEntity gameObj){
			this.gameObj = gameObj;
		}

		@Override
		public void done(List<ParseObject> objects, ParseException e) {
			if(e == null){
				if(objects.size() > 0){
					userObjectId = objects.get(0).getObjectId();
					
					ParseQuery<ParseObject> query = ParseQuery.getQuery(StringLibrary.PARSE_WL_TABLE);
					query.whereEqualTo(StringLibrary.PARSE_WL_USEROBJID, userObjectId);
					query.whereEqualTo(StringLibrary.PARSE_WL_GAMENAME, this.gameObj.getGameName());
					query.findInBackground(new FindCallback<ParseObject>() {

						@Override
						public void done(List<ParseObject> objects, ParseException e) {
							if(e == null){
								if(objects.size() == 0){
									btnAddWishList.setVisibility(View.VISIBLE);
									btnAddWishList.setOnClickListener(new ButtonClick(gameObj,userObjectId));
								}
								else if(objects.size() > 0){
									btnRemWishList.setVisibility(View.VISIBLE);
									btnRemWishList.setOnClickListener(new ButtonClick(gameObj,userObjectId));
								}
							} 
							else
								e.printStackTrace();
						}
					});
				}
			}
			else
				e.printStackTrace();
		}
		
	}
	
	class JSONGameAsyncTask extends AsyncTask<String, Void, GameDetailEntity>{

		@Override
		protected GameDetailEntity doInBackground(String... params) {
			InputStream in;
			BufferedReader bfReader;
			StringBuilder stringBuilder;
			String line = null;
			GameDetailEntity gameDetailObj = null;

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
				gameDetailObj = JSONGamesParser.parseGame(stringBuilder.toString());
			}
			catch(Exception e){
				e.printStackTrace();
			}
			return gameDetailObj;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
            
		    prgSpinner.show();
		}

		@Override
		protected void onPostExecute(GameDetailEntity result) {
			super.onPostExecute(result);
            
			tvDetRelDate.setVisibility(View.VISIBLE);
			tvDetPlatform.setVisibility(View.VISIBLE);
			tvDetGenres.setVisibility(View.VISIBLE);
			tvDetGameRat.setVisibility(View.VISIBLE);
			tvDetDev.setVisibility(View.VISIBLE);
			tvDetPublisher.setVisibility(View.VISIBLE);
			
			tvDetGameVal.setText(result.getGameName());
			
			BitmapDownloadAsyncTask bmpAsync = new BitmapDownloadAsyncTask(imVwDetGame);
			bmpAsync.execute(result.getMediumImageURL());
			
			tvDetRelDateVal.setText(result.getReleaseDate());
			tvDetPlatformVal.setText(result.getPlatform());
			tvDetGenresVal.setText(result.getGenres());
			tvDetGameRatVal.setText(result.getRatings());
			tvDetDevVal.setText(result.getDevelopers());
			tvDetPublisherVal.setText(result.getPublishers());
			
			SharedPreferences settings = getSharedPreferences(StringLibrary.PREF_NAME, 0);
			String userName = settings.getString(StringLibrary.USER_NAME, null);
			checkAndEnableButtons(userName,result);
			
			prgSpinner.dismiss();
		}
	}
}
