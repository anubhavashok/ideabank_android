package com.example.ideabank;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

public class Splash extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);
/*		new Handler().postDelayed(new Runnable(){

			@Override
			public void run() {
				Intent intent = new Intent(Splash.this,Store.class);
				startActivity(intent);
				finish();
			}
		}, 3000);
*/
		Button loginButton = (Button) findViewById(R.id.login);
		loginButton.setOnClickListener(new OnLoginClickListener());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash, menu);
		return true;
	}
	class OnLoginClickListener implements OnClickListener
	{

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent(Splash.this,Store.class);
			startActivity(intent);
			finish();
		}
		
	}
}
