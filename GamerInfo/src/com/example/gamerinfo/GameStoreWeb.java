package com.example.gamerinfo;

import entityClasses_Others.StringLibrary;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.Menu;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class GameStoreWeb extends Activity {

	WebView webView;
	String url;
	
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_store_web);
		
		webView = new WebView(this);
		setContentView(webView);
		webView.setWebViewClient(new WVCLient());
		webView.getSettings().setJavaScriptEnabled(true);
		
		url = getIntent().getExtras().getString(StringLibrary.YELP_STORE_DETAIL_URL);
		webView.loadUrl(url);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game_store_web, menu);
		return true;
	}

	public class WVCLient extends WebViewClient{

		@Override
		public boolean shouldOverrideUrlLoading(WebView wv,String s){
			wv.loadUrl(s);
			return true;
		}
	}
}
