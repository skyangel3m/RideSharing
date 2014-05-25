package com.ridesharing;

/**
 * Author :Raj Amal
 * Email  :raj.amalw@learn2crack.com
 * Website:www.learn2crack.com
 **/

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ridesharing.library.DatabaseHandler;
import com.ridesharing.library.UserFunctions;

public class Register extends Activity implements OnItemSelectedListener{

    /**
     *  JSON Response node names.
     **/

    private static String KEY_SUCCESS = "success";
    private static String KEY_UID = "uid";
    private static String KEY_FIRSTNAME = "fname";
    private static String KEY_LASTNAME = "lname";
    private static String KEY_USERNAME = "uname";
    private static String KEY_EMAIL = "email";
    private static String KEY_CREATED_AT = "created_at";
    private static String KEY_ERROR = "error";
    private static String KEY_WEIGHT_GENDER = "weight_gender";
    private static String KEY_WEIGHT_SMOKER = "weight_smoker";
    private static String KEY_PREF_GENDER = "pref_gender";
    private static String KEY_PREF_SMOKER = "pref_smoker";
    private static String KEY_LATITUDE = "latitude";
    private static String KEY_LONGITUDE = "longitude";

    /**
     * Defining layout items.
     **/

    EditText inputFirstName;
    EditText inputLastName;
    EditText inputUsername;
    EditText inputEmail;
    EditText inputPassword;
    Button btnRegister;
    TextView registerErrorMsg;
    String pref_gender, pref_smoker;
    Spinner spinnerGender, spinnerSmoker;
    
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.register);
       
       spinnerGender = (Spinner) findViewById(R.id.spinnerGender);
       spinnerSmoker = (Spinner) findViewById(R.id.spinnerSmoker);
              
     //create an ArrayAdapter using the string array and a default spinner layout
     ArrayAdapter<CharSequence> adapterGender = ArrayAdapter.createFromResource(this, R.array.GenderPreferences, android.R.layout.simple_spinner_item);
     ArrayAdapter<CharSequence> adapterSmoker = ArrayAdapter.createFromResource(this, R.array.SmokerPreferences, android.R.layout.simple_spinner_item);
     //Specify the layout to use when the list of choices appears
     adapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
     adapterSmoker.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
     //Apply the adapter to the spinner
     spinnerGender.setAdapter(adapterGender);
     spinnerSmoker.setAdapter(adapterSmoker);
     

    /**
     * Defining all layout items
     **/
        inputFirstName = (EditText) findViewById(R.id.fname);
        inputLastName = (EditText) findViewById(R.id.lname);
        inputUsername = (EditText) findViewById(R.id.uname);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.pword);
        btnRegister = (Button) findViewById(R.id.register);
        registerErrorMsg = (TextView) findViewById(R.id.register_error);

/**
 * Button which Switches back to the login screen on clicked
 **/

        Button login = (Button) findViewById(R.id.bktologin);
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), Login.class);
                startActivityForResult(myIntent, 0);
                finish();
            }

        });
        
        

        /**
         * Register Button click event.
         * A Toast is set to alert when the fields are empty.
         * Another toast is set to alert Username must be 5 characters.
         **/

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (  ( !inputUsername.getText().toString().equals("")) && ( !inputPassword.getText().toString().equals("")) && ( !inputFirstName.getText().toString().equals("")) && ( !inputLastName.getText().toString().equals("")) && ( !inputEmail.getText().toString().equals("")) )
                {
                    if ( inputUsername.getText().toString().length() > 4 ){
                    NetAsync(view);

                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),
                                "Username should be minimum 5 characters", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),
                            "One or more fields are empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
       }
    
    /**
     * Async Task to check whether internet connection is working
     **/

    private class NetCheck extends AsyncTask<String,String,Boolean>
    {
        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            nDialog = new ProgressDialog(Register.this);
            nDialog.setMessage("Loading..");
            nDialog.setTitle("Checking Network");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... args){


/**
 * Gets current device state and checks for working internet connection by trying Google.
 **/
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try {
                    URL url = new URL("http://www.google.com");
                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    urlc.setConnectTimeout(3000);
                    urlc.connect();
                    if (urlc.getResponseCode() == 200) {
                        return true;
                    }
                } catch (MalformedURLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return false;

        }
        @Override
        protected void onPostExecute(Boolean th){

            if(th == true){
                nDialog.dismiss();
                new ProcessRegister().execute();
            }
            else{
                nDialog.dismiss();
                registerErrorMsg.setText("Error in Network Connection");
            }
        }
    }

    String latitude = "0";
    String longitude = "0";
    String gcm_regid = "0";
    
    private class ProcessRegister extends AsyncTask<String, String, JSONObject> {

/**
 * Defining Process dialog
 **/
        private ProgressDialog pDialog;

        Intent intent = getIntent();
        String email,password,fname,lname,uname,pref_gender,pref_smoker;
        
        
        int weight_Gender = intent.getIntExtra("weightGender",0);
        int weight_Smoker = intent.getIntExtra("weightSmoker",0);
        
        
        String weight_gender = Integer.toString(weight_Gender);
        String weight_smoker = Integer.toString(weight_Smoker);
        
        
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            inputUsername = (EditText) findViewById(R.id.uname);
            inputPassword = (EditText) findViewById(R.id.pword);
               fname = inputFirstName.getText().toString();
               lname = inputLastName.getText().toString();
                email = inputEmail.getText().toString();
                uname= inputUsername.getText().toString();
                password = inputPassword.getText().toString();
              	String str1, str2;
            	str1 = spinnerGender.getSelectedItem().toString();
            	str2 = spinnerSmoker.getSelectedItem().toString();
            	if (str1.equalsIgnoreCase("Any")) {
            		pref_gender = "0";}
            	else if (str1.equalsIgnoreCase("Male")) {
            		pref_gender = "1";}
            	else if (str1.equalsIgnoreCase("Female")) {
            		pref_gender = "2";}
            	else {
            		pref_gender = "error";
            	}
            	
            	if (str2.equalsIgnoreCase("Any")) {
            		pref_smoker = "0";}
            	else if (str2.equalsIgnoreCase("Smoker")) {
            		pref_smoker = "1";}
            	else if (str2.equalsIgnoreCase("Non-smoker")) {
            		pref_smoker = "2";}
            	else {
            		pref_smoker = "error";
            	}
            pDialog = new ProgressDialog(Register.this);
            pDialog.setTitle("Contacting Servers");
            pDialog.setMessage("Registering ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {


        UserFunctions userFunction = new UserFunctions();
        JSONObject json = userFunction.registerUser(fname, lname, email, uname, password, weight_gender, weight_smoker, pref_gender, pref_smoker, latitude, longitude, gcm_regid);

            return json;


        }
       @Override
        protected void onPostExecute(JSONObject json) {
       /**
        * Checks for success message.
        **/
                try {
                    if (json.getString(KEY_SUCCESS) != null) {
                        registerErrorMsg.setText("");
                        String res = json.getString(KEY_SUCCESS);

                        String red = json.getString(KEY_ERROR);

                        if(Integer.parseInt(res) == 1){
                            pDialog.setTitle("Getting Data");
                            pDialog.setMessage("Loading Info");

                            registerErrorMsg.setText("Successfully Registered");


                            DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                            JSONObject json_user = json.getJSONObject("user");

                            /**
                             * Removes all the previous data in the SQlite database
                             **/

                            UserFunctions logout = new UserFunctions();
                            logout.logoutUser(getApplicationContext());
                            db.addUser(json_user.getString(KEY_FIRSTNAME),json_user.getString(KEY_LASTNAME),json_user.getString(KEY_EMAIL),json_user.getString(KEY_USERNAME), json_user.getString(KEY_UID),
                            		json_user.getString(KEY_CREATED_AT), json_user.getString(KEY_WEIGHT_GENDER), json_user.getString(KEY_WEIGHT_SMOKER)	,
                            		json_user.getString(KEY_PREF_GENDER), json_user.getString(KEY_PREF_SMOKER));
                            /**
                             * Stores registered data in SQlite Database
                             * Launch Registered screen
                             **/

                            Intent registered = new Intent(getApplicationContext(), Registered.class);

                            /**
                             * Close all views before launching Registered screen
                            **/
                            registered.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            pDialog.dismiss();
                            startActivity(registered);


                              finish();
                        }

                        else if (Integer.parseInt(red) ==2){
                            pDialog.dismiss();
                            registerErrorMsg.setText("User already exists");
                        }
                        else if (Integer.parseInt(red) ==3){
                            pDialog.dismiss();
                            registerErrorMsg.setText("Invalid Email id");
                        }

                    }

                        else{
                        pDialog.dismiss();

                            registerErrorMsg.setText("Error occured in registration");
                        }

                } catch (JSONException e) {
                    e.printStackTrace();


                }
            }}
        public void NetAsync(View view){
            new NetCheck().execute();
        }
        
       
        public void onNothingSelected(AdapterView<?> parent) {
        }
        
    	public void gotoSetWeight (View view) {
    		Intent intent = new Intent(this, SetWeightActivity.class);
    		startActivity(intent);
    	}

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			
		}
        }


