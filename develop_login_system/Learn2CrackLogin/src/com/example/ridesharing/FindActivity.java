package com.example.ridesharing;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class FindActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.find, menu);
		return true;
	}
	
	public void gotoFindRider (View view) {
		Intent intent = new Intent(this, FindRiderActivity.class);
		startActivity(intent);
	}
	
	public void gotoFindDriver (View view) {
		Intent intent = new Intent(this, FindDriverActivity.class);
		startActivity(intent);
	}

}
