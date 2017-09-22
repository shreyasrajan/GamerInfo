package com.example.gamerinfo;

import java.util.List;

import entityClasses_Others.StringLibrary;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class UserConfig extends Activity {

	EditText edUser;
	Button btnConfig;
	SharedPreferences settings;
	final static int WLSTATUS = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_config);

		Parse.initialize(this, StringLibrary.PARSE_APP_ID, StringLibrary.PARSE_CLIENT_KEY);
		ParseAnalytics.trackAppOpened(getIntent());

		edUser = (EditText) findViewById(R.id.edTxtUser);

		btnConfig = (Button)findViewById(R.id.btnConfig);
		btnConfig.setOnClickListener(new ButtonClick());

		settings = getSharedPreferences(StringLibrary.PREF_NAME, 0);

		String userName = settings.getString(StringLibrary.USER_NAME, null);
		if(userName != null)
			edUser.setText(userName);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.user_config, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		if(item.getItemId() == R.id.action_exit)
			System.exit(1);

		return super.onOptionsItemSelected(item);
	}

	class ButtonClick implements View.OnClickListener{
		String userName;
		@Override
		public void onClick(View v) {
			SharedPreferences.Editor editor;
			ParseQuery<ParseObject> query;
			
            userName = edUser.getText().toString().trim();

			if(v.getId() == R.id.btnConfig){
				if((userName.length() == 0) || (userName == null)){
					edUser.setError(StringLibrary.USER_ERROR_TEXT);
					return;
				}

				editor = settings.edit();
				editor.putString(StringLibrary.USER_NAME, userName);
				editor.commit();

				query = ParseQuery.getQuery(StringLibrary.PARSE_USER_TABLE);
				query.whereEqualTo(StringLibrary.PARSE_USERNAME, userName);
				query.findInBackground(new FindCallback<ParseObject>() {

					@Override
					public void done(List<ParseObject> objects, ParseException e) {
						if(e == null){
							if(objects.size() == 0){
								ParseObject userObject = new ParseObject(StringLibrary.PARSE_USER_TABLE);
								userObject.put(StringLibrary.PARSE_USERNAME,userName);
								userObject.put(StringLibrary.PARSE_WL_STATUS, WLSTATUS);
								userObject.saveInBackground();
							}
						}
						else
							e.printStackTrace();
					}
				});

				Intent intent = new Intent(UserConfig.this,Home.class);
				startActivity(intent);
				finish();
			}
		}

	}
	
}
