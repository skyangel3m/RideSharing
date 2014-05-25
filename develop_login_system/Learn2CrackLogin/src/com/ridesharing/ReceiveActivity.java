package com.ridesharing;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

public class ReceiveActivity extends Activity {
	
	TextView message;
	JSONObject json;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_receive);
		Intent intent = getIntent();
		
		message = (TextView) findViewById(R.id.message);
		
		String msg = intent.getExtras().getString("message");
		
		message.setText(msg);
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.receive, menu);
		return true;
	}

	
}