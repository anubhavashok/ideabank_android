package com.example.ideabank;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Uniqueness extends Activity {
	static Activity thisActivity;
	static boolean submitOnce = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_uniqueness);
		//save context
		thisActivity = this;
		//get idea
		JSONObject ideaJson = null;
		try {
			ideaJson = new JSONObject((String)getIntent().getExtras().get("IDEA_ENTRY_JSON"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//get tags
		ArrayList<String> tags = (ArrayList<String>) getIntent().getExtras().get("TAGS");
		//Initiate post
		if(submitOnce)
		{
			submitOnce=false;
			new API().execute(ideaJson);
		}
	}
	@Override
	public void onBackPressed() {
		thisActivity.finish();
		super.onBackPressed();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.uniqueness, menu);
		return true;
	}
	public static void handleResult(int nMatches)
	{
		submitOnce=true;
		// TODO change to less than a threshold
		RelativeLayout rootLayout = (RelativeLayout) thisActivity.findViewById(R.id.uniquenessRootView);
		if(nMatches == 0)
		{
			TextView uniqueIdeaView = new TextView(thisActivity);
			uniqueIdeaView.setText("Unique Idea!");
			// TODO make a constant which is determined at runtime in order to ensure text size fits the device it is being displayed on
			uniqueIdeaView.setTextSize(50);
			uniqueIdeaView.setTextColor(thisActivity.getResources().getColor(R.color.yellow));
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			//params.addRule(RelativeLayout.CENTER_HORIZONTAL);
			uniqueIdeaView.setLayoutParams(params);
			
			rootLayout.addView(uniqueIdeaView);
		}
		else
		{
			TextView nMatchesView = new TextView(thisActivity);
			nMatchesView.setText(String.valueOf(nMatches));
			// TODO make a constant which is determined at runtime in order to ensure text size fits the device it is being displayed on
			nMatchesView.setTextSize(100);
			nMatchesView.setTextColor(thisActivity.getResources().getColor(R.color.yellow));
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.CENTER_HORIZONTAL);
			nMatchesView.setLayoutParams(params);
			nMatchesView.setId(500000000);
			
			rootLayout.addView(nMatchesView);
			
			TextView text = new TextView(thisActivity);
			if(nMatches>1)
				text.setText("other similar ideas");
			else
				text.setText("other similar idea");
			// TODO make a constant which is determined at runtime in order to ensure text size fits the device it is being displayed on
			text.setTextSize(100);
			text.setTextColor(thisActivity.getResources().getColor(R.color.yellow));
			RelativeLayout.LayoutParams paramsText = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			paramsText.addRule(RelativeLayout.BELOW,500000000);
			text.setLayoutParams(paramsText);
			
			rootLayout.addView(text);
		}
	}
}
