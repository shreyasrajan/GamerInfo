package com.example.gamerinfo;

import java.util.ArrayList;

import entityClasses_Others.StringLibrary;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class ReviewSearch extends Activity {
    
	Spinner spRevPlatform;
	Button btnRevSearch;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_review_search);
		
		spRevPlatform = (Spinner)findViewById(R.id.spnRevPlatform);
		
		bindPlatformDropDown();
		
		btnRevSearch = (Button)findViewById(R.id.btnRevSearch);
		btnRevSearch.setOnClickListener(new ButtonClick());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.review_search, menu);
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
	
	private void bindPlatformDropDown(){
		ArrayList<String> platFormArr = new ArrayList<String>();
		platFormArr.add(StringLibrary.SELECT);
		platFormArr.add(StringLibrary.PS3);
		platFormArr.add(StringLibrary.XBOX360);
		platFormArr.add(StringLibrary.PC);
		platFormArr.add(StringLibrary.WII);
		platFormArr.add(StringLibrary.PSP);
		platFormArr.add(StringLibrary.DS);
		platFormArr.add(StringLibrary.DS3);
		platFormArr.add(StringLibrary.VITA);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,platFormArr);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spRevPlatform.setAdapter(adapter);
	}
	
	private int getPlatFormID(String platform){
		int platformID = 0;

		if(platform.equals(StringLibrary.PS3))
			platformID = 35;
		else if(platform.equals(StringLibrary.XBOX360))
			platformID = 20;
		else if(platform.equals(StringLibrary.PC))
			platformID = 94;
		else if(platform.equals(StringLibrary.WII))
			platformID = 36;
		else if(platform.equals(StringLibrary.PSP))
			platformID = 18;
		else if(platform.equals(StringLibrary.DS))
			platformID = 52;
		else if(platform.equals(StringLibrary.DS3))
			platformID = 117;
		else if(platform.equals(StringLibrary.VITA))
			platformID = 143;

		return platformID;
	}
	
	class ButtonClick implements View.OnClickListener{
		Intent intent;
		
		@Override
		public void onClick(View v) {
			
			String platform = spRevPlatform.getSelectedItem().toString();
			if(platform.equals(StringLibrary.SELECT)){
				Toast.makeText(ReviewSearch.this, StringLibrary.PLATFORM_ERROR, Toast.LENGTH_LONG).show();
				return;
			}
			int platformID = getPlatFormID(platform);
			
			intent = new Intent(ReviewSearch.this,GameReviews.class);
			intent.putExtra(StringLibrary.PLATFORM_ID, platformID);
			startActivity(intent);
		}
		
	}
}
