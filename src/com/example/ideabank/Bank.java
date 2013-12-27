package com.example.ideabank;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class Bank extends SQLiteOpenHelper{


	Context thisContext;
	public Bank(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
		thisContext = context;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE tags(id integer primary key,tag text)");
		db.execSQL("CREATE TABLE ideas(id integer primary key,title text,description text)");
		db.execSQL("CREATE TABLE ideatags(iid integer REFERENCES ideas(id), tid integer REFERENCES tags(id))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}
	public void verifyInput(IdeaEntry inputIdea,ArrayList<String> tags)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ArrayList<String> tagids= new ArrayList<String>();
		for(String tag : tags)
		{
			String sql = "SELECT id from tags where tag == " + "'"+tag+"'";
			Cursor tagCursor = db.rawQuery(sql, null);

			if(tagCursor.moveToFirst())
			{
				//if id exists, add id (first string in query result) to tagids
				tagids.add(tagCursor.getString(0));

			}
		}
		
		HashMap<String,Integer> ideatagmap = new HashMap<String,Integer>();
		String sql = "SELECT * from ideatags";
		Cursor cursor = db.rawQuery(sql, null);

		if(cursor.moveToFirst())
		{
			do
			{
				String tid = cursor.getString(1);
				String iid = cursor.getString(0);

				for(String tagid: tagids)
				{
					if(tid.equals(tagid))
					{
						Integer count = ideatagmap.get(iid);
						if(count==null) count = 0;
						count++;
						
						Log.d("Store","Idea: "+ iid+" Count:" + count);
						ideatagmap.put(iid, count);
					}
				}
			}while(cursor.moveToNext());
		}
		
		ArrayList<String> display = new ArrayList<String>();
		for(String key :ideatagmap.keySet())
		{
			if(ideatagmap.get(key)>2)
			{
				display.add(key);
			}
		}

		ArrayList<IdeaEntry> ideas = new ArrayList<IdeaEntry>();
		for(String iid: display)
		{
			sql = "SELECT * from ideas where id == "+iid;
			Cursor ideaCursor = db.rawQuery(sql, null);
			if(ideaCursor.moveToFirst())
			{
				IdeaEntry idea = new IdeaEntry(ideaCursor.getString(1),ideaCursor.getString(2));
				ideas.add(idea);
			}
		}
		//start new activity with data
		if(ideas.size()>0)
		{
			Intent intent = new Intent(thisContext,VerifyIdeas.class);
			intent.putExtra("SIMILAR_IDEA_ENTRIES", ideas);
			intent.putExtra("TAGS", tags);
			intent.putExtra("IDEA_ENTRY", (Serializable) inputIdea);
			thisContext.startActivity(intent);
		}
		else
		{
			storeEntry(inputIdea,tags);
			Toast unique = Toast.makeText(thisContext, "Idea has been stored", 200);
			unique.show();
		}

	}
	public void storeEntry(IdeaEntry idea, ArrayList<String> tags)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		//get idea count -> get iid
		String sql = "SELECT COUNT(*) from ideas";
		Cursor cursor = db.rawQuery(sql, null);
		String iid= "";
		if(cursor.moveToFirst())
		{
			iid = String.valueOf(cursor.getInt(0));
			Log.d("Store","idea count: "+iid);
			iid = String.valueOf(Integer.valueOf(iid) + 1);
			sql = "INSERT into ideas(title, description) values('"+idea.title+"','"+idea.description+"')";
			db.execSQL(sql);
		}
		//check if each tag is unique and update it
		for(String tag: tags)
		{
			sql = "SELECT id from tags where tag == "+ "'"+tag+"'";
			cursor = db.rawQuery(sql, null);
			String tid = "1";
			if(!cursor.moveToFirst())
			{
				//add to db
				sql = "INSERT into tags(tag) values("+"'"+tag+"'"+")";
				db.execSQL(sql);
				sql = "SELECT COUNT(*) from tags";
				Cursor cursor_temp = db.rawQuery(sql, null);
				if(cursor_temp.moveToFirst())
					tid = String.valueOf(cursor_temp.getInt(0));	
			}
			else
			{
				tid = cursor.getString(0);
			}
			Log.d("Store", "tag count "+ tid);
			sql = "INSERT into ideatags(iid,tid) values("+iid+","+tid+")";
			db.execSQL(sql);
		}
		showDB();
	}
	public void showDB()
	{
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "SELECT * from ideas";
		Cursor cursor = db.rawQuery(sql, null);
		Log.d("Store","showing db");
		if(cursor.moveToFirst())
		{
			do
			{
				Log.d("Store", "title: "+cursor.getString(0));
			}while(cursor.moveToNext());
		}
	}
}
class IdeaEntry implements Serializable
{
	String title;
	String description;
	
	IdeaEntry(String title, String description)
	{
		this.title=title;
		this.description=description;
	}
}
