package com.ridesharing;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends Activity {

	private static final int RESULT_SETTINGS = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		showUserSettings();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
		int id=item.getItemId();
		if(id==R.id.menu_settings){
            Intent i = new Intent(this, UserSettingsActivity.class);
            startActivityForResult(i, RESULT_SETTINGS);
        }
        return true;
    }
	
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
 
        switch (requestCode) {
        case RESULT_SETTINGS:
            showUserSettings();
            break;
        }
        
    }
    
    private void showUserSettings() {
        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this);
 
        StringBuilder builder = new StringBuilder();
 
        builder.append("\n Username: "
                + sharedPrefs.getString("prefUsername", "NULL"));
 
        builder.append("\n User Mode:"
                + sharedPrefs.getString("setMode", "NULL"));
 
 
        TextView settingsTextView = (TextView) findViewById(R.id.textUserSettings);
 
        settingsTextView.setText(builder.toString());
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
	
	public void gotoLogin (View view) {
		Intent intent = new Intent(this, Login.class);
		//Intent intent = new Intent(getApplicationContext(), Login.class);
		//intent.putExtra("new_variable", "value");
		startActivity(intent);
	}

}
