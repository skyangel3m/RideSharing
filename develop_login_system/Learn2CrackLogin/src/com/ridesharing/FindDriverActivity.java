package com.ridesharing;

import com.ridesharing.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class FindDriverActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_driver);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.find_driver, menu);
		return true;
	}

}
