package com.example.gamerinfo;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class SplashScreen extends Activity {

	final static int SLEEP_TIME = 3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
		
		TimerTask task = new TimerTask() {
			
			@Override
			public void run() {
				Intent intent = new Intent(SplashScreen.this,Home.class);
				startActivity(intent);
				finish();
			}
		};
		
		Timer timer = new Timer();
		timer.schedule(task, SLEEP_TIME * 1000);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash_screen, menu);
		return true;
	}

}
