package com.example.gamerinfo;

import java.util.ArrayList;
import java.util.List;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import entityClasses_Others.GameEntity;
import entityClasses_Others.GameListEntity;
import entityClasses_Others.StringLibrary;
import entityClasses_Others.WishListAdapter;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class MyWishList extends Activity {
    static ArrayList<GameEntity> arGameEnt;
	ListView lvWishList;
	WishListAdapter wlAdapter;
	String userName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_wish_list);
		
		Parse.initialize(this, StringLibrary.PARSE_APP_ID, StringLibrary.PARSE_CLIENT_KEY);
		ParseAnalytics.trackAppOpened(getIntent());
		
		userName = getIntent().getExtras().getString(StringLibrary.USER_NAME);
		
		lvWishList = (ListView)findViewById(R.id.lvWishList);
		
		setAdapter();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.my_wish_list, menu);
		return true;
	}
    
	@Override
	protected void onResume() {
		super.onResume();
		
		GameEntity gameEnt;
		int position = 0;
		String gameName = GameListEntity.gameName;
		boolean flg = false;
		
		if(gameName != null){
			WishListAdapter wlAdapter = (WishListAdapter) lvWishList.getAdapter();
			if(wlAdapter != null){
				arGameEnt = wlAdapter.getObjects();
				for(int i=0;i<arGameEnt.size();i++){
					gameEnt = arGameEnt.get(i);
					if(gameEnt.getGameName().equals(gameName)){
						position = i;
						flg = true;
						break;
					}
					if(flg) 
						break;
				}
				arGameEnt.remove(position);
				wlAdapter.notifyDataSetChanged();
				GameListEntity.gameName = null;
				
				if(wlAdapter.getCount() == 0)
					Toast.makeText(MyWishList.this, StringLibrary.WISHLIST_NO_ITEMS, Toast.LENGTH_LONG).show();
			}
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
		case R.id.action_makePublic:
			updateToPublic();
			break;
		case R.id.action_removePublic:
			updateToPrivate();
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	private void updateToPublic(){
		ParseQuery<ParseObject> query = ParseQuery.getQuery(StringLibrary.PARSE_USER_TABLE);
		query.whereEqualTo(StringLibrary.PARSE_USERNAME, userName);
		query.findInBackground(new FindCallback<ParseObject>(){

			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				if(e == null){
					if(objects.size() > 0){
						int wishListStatus = objects.get(0).getInt(StringLibrary.PARSE_WL_STATUS);
						if(wishListStatus == 1){
							Toast.makeText(MyWishList.this, StringLibrary.WISHLIST_PUBLIC, Toast.LENGTH_SHORT).show();
							return;
						}
						else if(wishListStatus == 0){
							ParseObject userParseObj = objects.get(0);
							userParseObj.put(StringLibrary.PARSE_WL_STATUS, 1);
							userParseObj.saveInBackground();
							Toast.makeText(MyWishList.this, StringLibrary.WISHLIST_PUBLIC_SUCCESS, Toast.LENGTH_SHORT).show();
						}
					}
				}
				else
					e.printStackTrace();
			}
			
		});
	}
	
	private void updateToPrivate(){
		ParseQuery<ParseObject> query = ParseQuery.getQuery(StringLibrary.PARSE_USER_TABLE);
		query.whereEqualTo(StringLibrary.PARSE_USERNAME, userName);
		query.findInBackground(new FindCallback<ParseObject>(){

			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				if(e == null){
					if(objects.size() > 0){
						int wishListStatus = objects.get(0).getInt(StringLibrary.PARSE_WL_STATUS);
						if(wishListStatus == 0){
							Toast.makeText(MyWishList.this, StringLibrary.WISHLIST_PRIVATE, Toast.LENGTH_SHORT).show();
							return;
						}
						else if(wishListStatus == 1){
							ParseObject userParseObj = objects.get(0);
							userParseObj.put(StringLibrary.PARSE_WL_STATUS, 0);
							userParseObj.saveInBackground();
							Toast.makeText(MyWishList.this, StringLibrary.WISHLIST_PRIVATE_SUCCESS, Toast.LENGTH_SHORT).show();
						}
					}
				}
				else
					e.printStackTrace();
			}
			
		});
	}
	
	private void setAdapter(){
		ParseQuery<ParseObject> query = ParseQuery.getQuery(StringLibrary.PARSE_USER_TABLE);
		query.whereEqualTo(StringLibrary.PARSE_USERNAME, userName);
		query.findInBackground(new ParseCallback(userName));
	}
	
	class ParseCallback extends FindCallback<ParseObject>{
        String userName;
        String userObjectId;
       
		public ParseCallback(String userName){
			this.userName = userName;
		}
		
		@Override
		public void done(List<ParseObject> objects, ParseException e) {
			if(e == null){
				if(objects.size() > 0){
					userObjectId = objects.get(0).getObjectId();
					
					ParseQuery<ParseObject> query = ParseQuery.getQuery(StringLibrary.PARSE_WL_TABLE);
					query.whereEqualTo(StringLibrary.PARSE_WL_USEROBJID, userObjectId);
					query.findInBackground(new ParseWLCallback());
				}
			}
			else
				e.printStackTrace();
		}
	}
	
	class ParseWLCallback extends FindCallback<ParseObject>{
		
		@Override
		public void done(List<ParseObject> objects, ParseException e) {
			String gameName;
	        String thumbnailURL;
	        String detailURL;
	        String releaseDate;
	        String platform;
	        GameEntity gameEntObj;
	        ArrayList<GameEntity> arGamEnt;
	        
			if(e == null){
				if(objects.size() == 0){
					Toast.makeText(MyWishList.this, StringLibrary.WISHLIST_NO_ITEMS, Toast.LENGTH_LONG).show();
					return;
				}
				else{
					arGamEnt = new ArrayList<GameEntity>();
					for(int i=0;i<objects.size();i++){
						gameName = objects.get(i).getString(StringLibrary.PARSE_WL_GAMENAME);
						thumbnailURL = objects.get(i).getString(StringLibrary.PARSE_WL_THUMBNAIL);
						detailURL = objects.get(i).getString(StringLibrary.PARSE_WL_DETAIL);
						releaseDate = objects.get(i).getString(StringLibrary.PARSE_WL_RELEASEDATE);
						platform = objects.get(i).getString(StringLibrary.PARSE_WL_PLATFORM);
						
						gameEntObj = new GameEntity(thumbnailURL, gameName, 
								                    releaseDate, platform, detailURL);
						
						arGamEnt.add(gameEntObj);
					}
					
					wlAdapter = new WishListAdapter(MyWishList.this, arGamEnt);
					
					lvWishList.setAdapter(wlAdapter);
					lvWishList.setOnItemClickListener(new ListViewItemClick(arGamEnt));
				}
			}
			else
				e.printStackTrace();
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
