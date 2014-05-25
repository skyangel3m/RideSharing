package com.ridesharing;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public class SearchActivity extends Activity {

	public static final String EXTRA_MESSAGE = "message";
	public static final String PROPERTY_REG_ID = "registration_id";
	
	String SENDER_ID = "135591912514";
	
	static final String TAG = "GCMNotification";
	GoogleCloudMessaging gcm;
	
	TextView mDisplay;
    Context context;
    String regid;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mDisplay = (TextView) findViewById(R.id.display);
		context = getApplicationContext();

			gcm = GoogleCloudMessaging.getInstance(this);

				new RegisterBackground().execute();			
		}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override
	protected void onResume(){
		super.onResume();

	}
	class RegisterBackground extends AsyncTask<String,String,String>{

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			String msg = "";
			try {
                if (gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance(context);
                }
                regid = gcm.register(SENDER_ID);
                msg = "Dvice registered, registration ID=" + regid;
                Log.d("111", msg);
                sendRegistrationIdToBackend(regid);

            } catch (IOException ex) {
                msg = "Error :" + ex.getMessage();
            }
            return msg;
        }

		@Override
        protected void onPostExecute(String msg) {
            mDisplay.append(msg + "\n");

        }
		

	private void sendRegistrationIdToBackend(String regid) {

		String url = "http://autprojectdemo.me.pn/smartride/test/test_api/getregistrationid.php";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("regid", regid));
       DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        try {
			httpPost.setEntity(new UrlEncodedFormEntity(params));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

        try {
			HttpResponse httpResponse = httpClient.execute(httpPost);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}         

	}
	}

}
