package com.example.gamerinfo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import parsers.JSONReviewsParser;
import entityClasses_Others.ReviewAdapter;
import entityClasses_Others.ReviewEntity;
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

public class GameReviews extends Activity {
	int platformID;
	ListView lvReviews;
	ReviewAdapter revAdapter;
	ProgressDialog prgSpinner;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_reviews);
		
		lvReviews = (ListView)findViewById(R.id.lvReviews);
		
		platformID = getIntent().getExtras().getInt(StringLibrary.PLATFORM_ID);
		
		String url = prepareReviewsURL(platformID);
		
		prgSpinner = new ProgressDialog(this);
		prgSpinner.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		prgSpinner.setCancelable(false);
		prgSpinner.setMessage(StringLibrary.DIALOG_LOADING_REV);
		
		JSONReviewsAsyncTask jsonReviews = new JSONReviewsAsyncTask();
		jsonReviews.execute(url);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.game_reviews, menu);
		return true;
	}
    
	private String prepareReviewsURL(int platformID){
		StringBuilder urlBuilder = new StringBuilder();
		urlBuilder.append("http://www.giantbomb.com/api/reviews/");
		urlBuilder.append("?api_key=");
		urlBuilder.append(StringLibrary.GAME_API_KEY);
		urlBuilder.append("&format=json");
		urlBuilder.append("&filter=platforms");
		urlBuilder.append("%3A");
		urlBuilder.append(platformID);
		urlBuilder.append("&sort=publish_date");
		urlBuilder.append("%3A");
		urlBuilder.append("desc");
		urlBuilder.append("&resources=game");
		urlBuilder.append("&limit=50");
		
		return urlBuilder.toString();
	}
	
	class JSONReviewsAsyncTask extends AsyncTask<String, Void, ArrayList<ReviewEntity>>{

		@Override
		protected ArrayList<ReviewEntity> doInBackground(String... params) {
			InputStream in;
			BufferedReader bfReader;
			StringBuilder stringBuilder;
			String line = null;
			ArrayList<ReviewEntity> revArr = null;

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
				revArr = JSONReviewsParser.parseReviews(stringBuilder.toString());
			}
			catch(Exception e){
				e.printStackTrace();
			}
			return revArr;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
            
			prgSpinner.show();
		}

		@Override
		protected void onPostExecute(ArrayList<ReviewEntity> result) {
			super.onPostExecute(result);
            
			revAdapter = new ReviewAdapter(getBaseContext(), result);
          
			lvReviews.setAdapter(revAdapter);
			lvReviews.setOnItemClickListener(new ListViewItemClick(result));
			
			prgSpinner.dismiss();
		}
	}
	
	class ListViewItemClick implements AdapterView.OnItemClickListener{
        
		ArrayList<ReviewEntity> revArr;
		
		public ListViewItemClick(ArrayList<ReviewEntity> revArr){
			this.revArr = revArr;
		}
		
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
			Intent intent = new Intent(getBaseContext(),ReviewWeb.class);
			intent.putExtra(StringLibrary.REVIEW_DETAIL_URL, this.revArr.get(position).getDetailURL());
			startActivity(intent);
		}
		
	}
}
