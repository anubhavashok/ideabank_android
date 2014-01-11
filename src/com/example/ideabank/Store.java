package com.example.ideabank;

import java.util.ArrayList;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class Store extends Activity {

	public static int privateCount = 0;
	public static String first_name = "";
	public static String last_name = "";
	public static int userid = -1;
	ArrayList<Button> tagButtons = new ArrayList<Button>();
	Bank ideaBank = new Bank(Store.this, "ideabank", null, 1);
	ColorStateList originalHintColors;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_store);
		
		CheckBox privateCheckbox = (CheckBox) findViewById(R.id.privateCheckbox);
		TextView privateCheckboxHint = (TextView) findViewById(R.id.privateCheckboxHint);
		originalHintColors = privateCheckboxHint.getHintTextColors();
		if(privateCount==0)
		{
			privateCheckbox.setTextColor(getResources().getColor(R.color.LightGrey));
			privateCheckbox.setClickable(false);
			privateCheckboxHint.setText("just ran out of private ideas!");
			privateCheckbox.setOnTouchListener(new OnTouchListener(){
				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					//TODO make activity where idea purchase is possible
					Toast t= Toast.makeText(Store.this, "Buy more private ideas", 200);
					t.show();
					return false;
				}
			});
		}
		privateCheckboxHint.setText(privateCount+" private ideas remaining.");
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
			CheckBox privateCheckbox = (CheckBox) findViewById(R.id.privateCheckbox);
			EditText titleInput = (EditText) findViewById(R.id.titleInput);
			EditText ideaInput = (EditText) findViewById(R.id.ideaInput);
			
			Editable title = titleInput.getText();
			Editable idea = ideaInput.getText();
			titleInput.setOnTouchListener(new OnTouchListener(){
				@Override
				public boolean onTouch(View view, MotionEvent event) {
					TextView thisView = (TextView) view;
					thisView.setHint("Title");
					thisView.setHintTextColor(originalHintColors);
					return false;
				}
			});
			ideaInput.setOnTouchListener(new OnTouchListener(){
				@Override
				public boolean onTouch(View view, MotionEvent event) {
					TextView thisView = (TextView) view;
					thisView.setHint("Idea");
					thisView.setHintTextColor(originalHintColors);
					return false;
				}
			});
			if(title.toString().equals(""))
			{
				titleInput.setHint("Title cannot be empty");
				titleInput.setHintTextColor(getResources().getColor(R.color.red));
				return;
			}
			if(idea.toString().equals(""))
			{
				ideaInput.setHint("Idea cannot be empty");
				ideaInput.setHintTextColor(getResources().getColor(R.color.red));
				return;
			}
			ArrayList<String> tags= new ArrayList<String>();
			
			for(Button tagButton: tagButtons)
			{
				if(tagButton.getVisibility()==View.VISIBLE)
				{
					tags.add(tagButton.getText().toString());
				}
			}
			boolean isPrivate = false;
			if(privateCheckbox.isChecked() && privateCount > 0) 
			{
				TextView privateCheckboxHint = (TextView) findViewById(R.id.privateCheckboxHint);
				isPrivate = true;
				privateCount--;
				if(privateCount==0)
				{
					privateCheckbox.setTextColor(getResources().getColor(R.color.LightGrey));
					privateCheckbox.setClickable(false);
					privateCheckboxHint.setText("just ran out of private ideas!");
					privateCheckbox.setOnTouchListener(new OnTouchListener(){
						@Override
						public boolean onTouch(View arg0, MotionEvent arg1) {
							Toast t= new Toast(Store.this);
							t.setText("Buy more private ideas");
							t.setDuration(200);
							t.show();
							return false;
						}
					});
				}
				else
				{
					privateCheckboxHint.setText(privateCount+" private ideas remaining.");
				}
			}
			IdeaEntry ideaIn = new IdeaEntry(title.toString(),idea.toString(),isPrivate,userid);
			JSONObject ideaJson = API.ideaEntry2JSON(ideaIn);
			
			Intent intent = new Intent(Store.this,Uniqueness.class);
			intent.putExtra("TAGS", tags);
			intent.putExtra("IDEA_ENTRY_JSON", ideaJson.toString());
			Store.this.startActivity(intent);
			//ideaBank.verifyInput(ideaIn,tags);
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
