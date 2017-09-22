package com.example.gamerinfo;

import java.util.ArrayList;
import java.util.List;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import entityClasses_Others.StringLibrary;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class PublicWishlist extends Activity {
	ListView lvPubWishList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_public_wishlist);

		Parse.initialize(this, StringLibrary.PARSE_APP_ID, StringLibrary.PARSE_CLIENT_KEY);
		ParseAnalytics.trackAppOpened(getIntent());

		lvPubWishList = (ListView)findViewById(R.id.lvPubWishList);
        
		setListView();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.public_wishlist, menu);
		return true;
	}

	private void setListView(){
		ParseQuery<ParseObject> query = ParseQuery.getQuery(StringLibrary.PARSE_USER_TABLE);
		query.findInBackground(new FindCallback<ParseObject>(){

			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				int wlStatus;
				ArrayList<String> pubUserList = null;

				if(e == null){
					if(objects.size() > 0){
						pubUserList = new ArrayList<String>();
						for(int i=0;i<objects.size();i++){
							wlStatus = objects.get(i).getInt(StringLibrary.PARSE_WL_STATUS);
							if(wlStatus == 1)
								pubUserList.add(objects.get(i).getString(StringLibrary.PARSE_USERNAME));
						}

						if(pubUserList.size() > 0){
							ArrayAdapter<String> adapter = new ArrayAdapter<String>(PublicWishlist.this, 
									                                                R.layout.public_wishlist_custom_layout,
									                                                R.id.tvPubUserName,
									                                                pubUserList);
							lvPubWishList.setAdapter(adapter);
							lvPubWishList.setOnItemClickListener(new ListViewItemClick(pubUserList));
						}
						else if(pubUserList.size() == 0)
							Toast.makeText(PublicWishlist.this, StringLibrary.WISHLIST_PUBLIC_NOT_AVAILABLE, Toast.LENGTH_SHORT).show();
					}
				}
				else
					e.printStackTrace();
			}

		});
	}
	
	class ListViewItemClick implements AdapterView.OnItemClickListener{
		ArrayList<String> pubUserList;
		
		public ListViewItemClick(ArrayList<String> pubUserList){
			this.pubUserList = pubUserList;
		}
		
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
			String userName = this.pubUserList.get(position);
			Intent intent = new Intent(getBaseContext(),MyWishList.class);
			intent.putExtra(StringLibrary.USER_NAME, userName);
		    startActivity(intent);
		}
		
	}

}
