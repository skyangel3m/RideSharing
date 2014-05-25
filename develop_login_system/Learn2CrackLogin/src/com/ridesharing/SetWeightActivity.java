package com.ridesharing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class SetWeightActivity extends Activity implements OnSeekBarChangeListener {

	private SeekBar barWeight1, barWeight2;
	private TextView textProgress1, textProgress2;
	private int weightGender=0, weightSmoker=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_weight);
		
		barWeight1 = (SeekBar)findViewById(R.id.weightBar1);
		barWeight1.setOnSeekBarChangeListener(this);
		
		barWeight2 = (SeekBar)findViewById(R.id.weightBar2);
		barWeight2.setOnSeekBarChangeListener(this);
		
		textProgress1 = (TextView)findViewById(R.id.textViewProgress1);
		textProgress2 = (TextView)findViewById(R.id.textViewProgress2);
	}

	
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
	
		if (seekBar == barWeight1) {
			textProgress2.setText("The value is: " + progress);
			weightGender = progress;
		}
		else if (seekBar == barWeight2) {
			textProgress1.setText("The value is: " + progress);
			weightSmoker = progress;
		}
	}
	
	
	public void onStopTrackingTouch(SeekBar arg0) {};
	public void onStartTrackingTouch(SeekBar arg0) {};
	
	public void gotoRegister (View view) {
		
		Intent intent = new Intent(this, Register.class);
		
		intent.putExtra("weightGender", weightGender);
		intent.putExtra("weightSmoker", weightSmoker);
		
		startActivity(intent);
	}
}
