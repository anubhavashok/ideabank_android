package com.example.ideabank;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;

public class Store extends Activity {

	ArrayList<Button> tagButtons = new ArrayList<Button>();
	Bank ideaBank = new Bank(Store.this, "ideabank", null, 1);
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_store);

		Button saveButton = (Button) findViewById(R.id.saveButton);
		saveButton.setOnClickListener(new OnSaveClickListener());
		
		EditText ideaInput = (EditText) findViewById(R.id.ideaInput);
		IdeaTextWatcher watcher = new IdeaTextWatcher();
		ideaInput.addTextChangedListener(watcher);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.store, menu);
		return true;
	}

	class OnSaveClickListener implements OnClickListener
	{
		@Override
		public void onClick(View view) {
			Log.d("Store", "savebuttonclicked");
			EditText titleInput = (EditText) findViewById(R.id.titleInput);
			EditText ideaInput = (EditText) findViewById(R.id.ideaInput);
			
			Editable title = titleInput.getText();
			Editable idea = ideaInput.getText();

			ArrayList<String> tags= new ArrayList<String>();
			
			for(Button tagButton: tagButtons)
			{
				if(tagButton.getVisibility()==View.VISIBLE)
				{
					tags.add(tagButton.getText().toString());
				}
			}
			IdeaEntry ideaIn = new IdeaEntry(title.toString(),idea.toString());
			ideaBank.verifyInput(ideaIn,tags);
		}
	}
	class OnTagClickListener implements OnClickListener
	{
		@Override
		public void onClick(View view) {
			view.setVisibility(View.INVISIBLE);
			LinearLayout parent = (LinearLayout) findViewById(R.id.tagsContainer);
			parent.removeView(view);
		}
	}
	class IdeaTextWatcher implements TextWatcher
	{

		@Override
		public void afterTextChanged(Editable idea) {
			
			ArrayList<String> tags = getTagsFromIdea(idea.toString());
			LinearLayout parent = (LinearLayout) findViewById(R.id.tagsContainer);
			parent.removeAllViews();
			tagButtons.clear();
			for(String tag: tags)
			{
				Button b = new Button(Store.this);
				b.setOnClickListener(new OnTagClickListener());
				b.setText(tag);
				tagButtons.add(b);
				LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				parent.addView(b,lp);
			}
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub
			
		}
		
	}
	public ArrayList<String> getTagsFromIdea(String idea)
	{
		String[] words = idea.split(" ");
		ArrayList<String> tagWords = new ArrayList<String>();
		boolean validWord;
		if(idea.equals("")) return tagWords;
		for(String word: words)
		{
			validWord = true;
			for(String stopword: Stopwords.stopwords)
			{
				if(word.equals(stopword))
				{
					validWord = false;
					break;
				}
			}
			if(validWord)
				if(!tagWords.contains(word))
					tagWords.add(word);
		}
		return tagWords;
	}
}
