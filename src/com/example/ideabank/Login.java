package com.example.ideabank;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class Login extends AsyncTask<JSONObject, Void, JSONObject>
{
	@Override
	protected JSONObject doInBackground(JSONObject... ideaJsonIn) {

		//TODO replace url with real url
		String url = "http://thawing-lake-8533.herokuapp.com/users";
		JSONObject ideaJson=ideaJsonIn[0];
		Log.d("platform",ideaJson.toString());
	    DefaultHttpClient httpclient = new DefaultHttpClient();
	    HttpPost httpost = new HttpPost(url);
	    StringEntity jsonEntity = null;
		try {
			jsonEntity = new StringEntity(ideaJson.toString());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	    httpost.setEntity(jsonEntity);
	    httpost.setHeader("Accept", "application/json");
	    httpost.setHeader("Content-type", "application/json");
	    try {
			Log.d("platform",ideaJson.toString());
		} catch (ParseException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
	    HttpResponse response = null;
		try {
			response = httpclient.execute(httpost);
		} catch (ClientProtocolException e2) {
			e2.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
	    String reply = null;
		try {
			reply = EntityUtils.toString(response.getEntity());
		} catch (ParseException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	    JSONObject replyJson = null;
		try {
			replyJson = new JSONObject(reply);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	    Log.d("platform", reply);
	    return replyJson;
	}
	@Override
	protected void onPostExecute(JSONObject replyJson) {
	    Splash.handleResult(replyJson);
		super.onPostExecute(replyJson);
	}
}