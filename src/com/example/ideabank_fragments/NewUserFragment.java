package com.example.ideabank_fragments;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.ideabank.Login;
import com.example.ideabank.R;
import com.example.ideabank.Splash;

public class NewUserFragment extends Fragment{

	  View view;
	  @Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	      Bundle savedInstanceState) {
	    view = inflater.inflate(R.layout.newuser_fragment,
	        container, false);
	    Button newAccount = (Button) view.findViewById(R.id.newAccount);
	    newAccount.setOnClickListener(new OnNewUserClickListener());
	    return view;
	  }
	  
	  class OnNewUserClickListener implements OnClickListener
	  {
		@Override
		public void onClick(View arg0) {
			//send first name and last name
			EditText firstName = (EditText) view.findViewById(R.id.fragment_container);
			EditText lastName = (EditText) view.findViewById(R.id.lastName);
			
			//initiate json creation and transfer
			JSONObject newUserJson = Splash.replyJson;
			try {
				newUserJson.put("first_name", firstName.getText().toString());
				newUserJson.put("last_name", lastName.getText().toString());
				newUserJson.put("add_user", true);
				newUserJson.put("new_user", false);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			new Login().execute(newUserJson);
			
		}
		  
	  }
}
