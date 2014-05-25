package com.example.ridesharing;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class MainActivity2 extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_activity2);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_activity2, menu);
		return true;
	}
	
	public void gotoFind (View view) {
		Intent intent = new Intent(this, FindActivity.class);
		startActivity(intent);
	}
	
	public void gotoMyPage (View view) {
		Intent intent = new Intent(this, MyPageActivity.class);
		startActivity(intent);
	}
	
	public void gotoMap (View view) {
		Intent intent = new Intent(this, MapActivity.class);
		startActivity(intent);
	}
	
	public void gotoTopUp (View view) {
		Intent intent = new Intent(this, TopUpActivity.class);
		startActivity(intent);
	}
	
	public void gotoFeedback (View view) {
		Intent intent = new Intent(this, FeedbackActivity.class);
		startActivity(intent);
	}

}
