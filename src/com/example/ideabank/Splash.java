package com.example.ideabank;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ideabank_fragments.NewUserFragment;

public class Splash extends Activity {
	static Activity thisContext = null;
	static ProgressDialog myProgressDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);
		thisContext = Splash.this;
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
			
			EditText username = (EditText) findViewById(R.id.username);
			EditText password = (EditText) findViewById(R.id.password);
			
			String usernameString = username.getText().toString();
			String passwordString = password.getText().toString();
			if(usernameString.equals("")||passwordString.equals(""))
			{
				TextView loginHint = (TextView) findViewById(R.id.loginHint);
				loginHint.setText("Username and password cannot be blank");
				return;
			}
			JSONObject creds = new JSONObject();
			try {
				creds.put("username", usernameString);
				creds.put("password", passwordString);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//myProgressDialog = new ProgressDialog(Splash.this,ProgressDialog.THEME_HOLO_DARK);
			myProgressDialog = ProgressDialog.show(Splash.this,"Hold on", "Logging in", true);
			new Login().execute(creds);
		}
		
	}
	public static JSONObject replyJson;
	public static void handleResult(JSONObject result)
	{			
		myProgressDialog.dismiss();
		replyJson = result;
		try {
			//check if new user
			if(result.getBoolean("successful"))
			{
				if(result.getBoolean("new_user"))
				{
			       // get an instance of FragmentTransaction from your Activity
					FragmentManager fragmentManager = thisContext.getFragmentManager();
					FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

					//start first_name and last_name fragment
					NewUserFragment newUserFragment = new NewUserFragment();
					
					fragmentTransaction.add(R.id.fragment_container, newUserFragment);
					fragmentTransaction.commit();
					//get and store first_name and last_name
				}
				else
				{
					//store private count
					Store.privateCount = result.getInt("private_count");
					//store userid
					Store.userid = result.getInt("user_id");
					//store name
					Store.first_name = result.getString("first_name");
					Store.last_name = result.getString("last_name");
					
					Intent intent = new Intent(thisContext,Store.class);
					thisContext.startActivity(intent);
					thisContext.finish();
				}
			}
			else
			{
				//red line above username, password saying wrong username or password
				TextView loginHint = (TextView)thisContext.findViewById(R.id.loginHint);
				loginHint.setText("Username or password not valid!");
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
