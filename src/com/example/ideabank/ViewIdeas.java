package com.example.ideabank;

import java.util.ArrayList;

import com.example.ideabank.VerifyIdeas.OnStoreClickListener;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

public class ViewIdeas extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_ideas);
		@SuppressWarnings("unchecked")
		ArrayList<IdeaEntry> allIdeas = new ArrayList<IdeaEntry>((ArrayList<IdeaEntry>)getIntent().getExtras().get("ALLIDEAS"));
		LinearLayout allIdeasContainer = (LinearLayout) findViewById(R.id.allIdeasContainer);
		for(IdeaEntry idea: allIdeas)
		{
			//create 3 views for id, title, description
			TextView title = new TextView(ViewIdeas.this);
			title.setText(idea.title);
			TextView description = new TextView(ViewIdeas.this);
			description.setText(idea.description);
			//set params
			LayoutParams titleParams= new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			titleParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			LayoutParams descriptionParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			//descriptionParams.leftMargin=50;
			descriptionParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			RelativeLayout container = new RelativeLayout(ViewIdeas.this);

			container.addView(title,titleParams);
			container.addView(description,descriptionParams);
			//add to root view
			allIdeasContainer.addView(container);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_ideas, menu);
		return true;
	}

}
