package com.ridesharing;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;
import android.view.Menu;

public class FeedbackActivity extends Activity implements 
RatingBar.OnRatingBarChangeListener {

	RatingBar rating;
	TextView ratingText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);
		ratingText=(TextView)findViewById(R.id.ratingText);
		
		rating=(RatingBar)findViewById(R.id.rating);
		rating.setOnRatingBarChangeListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.feedback, menu);
		return true;
	}
	
	public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser)
	{
			ratingText.setText(""+this.rating.getRating());
		
	}

}
