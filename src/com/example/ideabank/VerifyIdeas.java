package com.example.ideabank;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class VerifyIdeas extends Activity {
	Bank bank = new Bank(VerifyIdeas.this, "ideabank", null, 1);
	IdeaEntry idea;
	ArrayList<String> tags;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_verify_ideas);
		ArrayList<IdeaEntry> ideas = (ArrayList<IdeaEntry>) getIntent().getExtras().get("SIMILAR_IDEA_ENTRIES");
		LinearLayout verifyContainer = (LinearLayout) findViewById(R.id.verifyContainer);
		for(IdeaEntry idea: ideas)
		{
			//create 3 views for id, title, description
			TextView title = new TextView(VerifyIdeas.this);
			title.setText(idea.title);
			TextView description = new TextView(VerifyIdeas.this);
			description.setText(idea.description);
			//set params
			LayoutParams titleParams= new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			titleParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			LayoutParams descriptionParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			descriptionParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			RelativeLayout container = new RelativeLayout(VerifyIdeas.this);

			container.addView(title,titleParams);
			container.addView(description,descriptionParams);
			//add to root view
			verifyContainer.addView(container);
			Button storeButton = (Button) findViewById(R.id.storeButton);
			storeButton.setOnClickListener(new OnStoreClickListener());
		}

		tags = (ArrayList<String>) getIntent().getExtras().get("TAGS");
		idea = (IdeaEntry) getIntent().getExtras().get("IDEA_ENTRY");
	}
	class OnStoreClickListener implements OnClickListener
	{
		@Override
		public void onClick(View view) {
			Toast stored = Toast.makeText(VerifyIdeas.this, "Idea has been added to the database", 200);
			stored.show();
			bank.storeEntry(idea, tags);
			Intent intent = new Intent(VerifyIdeas.this,Store.class);
			startActivity(intent);
		}
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.verify_ideas, menu);
		return true;
	}

}
