package com.example.gamerinfo;

import entityClasses_Others.StringLibrary;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

public class Home extends Activity {

	ImageView imVwGameSearch;
	ImageView imVwGameRev;
	ImageView imVwGameStoreSearch;
	ImageView imVwMyWishList;
	ImageView imVwPubWishList;
	String userName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
        
		SharedPreferences settings = getSharedPreferences(StringLibrary.PREF_NAME, 0);
		userName = settings.getString(StringLibrary.USER_NAME, null);
		if(userName == null)
			startUserActivity();
		
		imVwGameSearch = (ImageView)findViewById(R.id.imVwSearch);
		imVwGameSearch.setOnClickListener(new ImageViewClick());

		imVwGameRev = (ImageView)findViewById(R.id.imVwReviews);
		imVwGameRev.setOnClickListener(new ImageViewClick());

		imVwGameStoreSearch = (ImageView)findViewById(R.id.imVwStoreSearch);
		imVwGameStoreSearch.setOnClickListener(new ImageViewClick());

		imVwMyWishList = (ImageView)findViewById(R.id.imVwMyWishList);
		imVwMyWishList.setOnClickListener(new ImageViewClick());

		imVwPubWishList = (ImageView)findViewById(R.id.imVwPublicWishList);
		imVwPubWishList.setOnClickListener(new ImageViewClick());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}
    
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
		case R.id.action_setupUser:
			startUserActivity();
			break;
		case R.id.action_exit:
			System.exit(1);
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	private void startUserActivity(){
		Intent intent = new Intent(getBaseContext(),UserConfig.class);
		startActivity(intent);
		finish();
	}
	
	public class ImageViewClick implements View.OnClickListener{

		@Override
		public void onClick(View v) {
			Intent intent = null;
			
			switch(v.getId()){
			case R.id.imVwSearch:
				intent = new Intent(Home.this, GameSearch.class);
				break;
			case R.id.imVwReviews:
				intent = new Intent(Home.this,ReviewSearch.class);
				break;
			case R.id.imVwStoreSearch:
				intent = new Intent(Home.this,GameStoreSearch.class);
				break;
			case R.id.imVwMyWishList:
				intent = new Intent(Home.this,MyWishList.class);
				intent.putExtra(StringLibrary.USER_NAME, userName);
				break;
			case R.id.imVwPublicWishList:
				intent = new Intent(Home.this,PublicWishlist.class);
				break;
			default:
				break;
			}
			
			startActivity(intent);
		}

	}
}
