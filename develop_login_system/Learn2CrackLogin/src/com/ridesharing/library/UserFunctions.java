package com.ridesharing.library;

/**
 * Author :Raj Amal
 * Email  :raj.amalw@learn2crack.com
 * Website:www.learn2crack.com
 **/

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Context;


public class UserFunctions {

    private JSONParser jsonParser;

    //URL of the PHP API
    private static String loginURL = "http://autprojectdemo.me.pn/smartride/Smartride_api/";
    private static String registerURL = "http://autprojectdemo.me.pn/smartride/Smartride_api/";
    private static String forpassURL = "http://autprojectdemo.me.pn/smartride/Smartride_api/";
    private static String chgpassURL = "http://autprojectdemo.me.pn/smartride/Smartride_api/";
    private static String locURL = "http://autprojectdemo.me.pn/smartride/Smartride_api/";


    private static String login_tag = "login";
    private static String register_tag = "register";
    private static String forpass_tag = "forpass";
    private static String chgpass_tag = "chgpass";
    private static String loc_tag = "userlocation";
    private static String search_tag = "search_ride";
    

    // constructor
    public UserFunctions(){
        jsonParser = new JSONParser();
    }

    /**
     * Function to Login
     **/

    public JSONObject loginUser(String email, String password){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", login_tag));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));
        JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
        return json;
    }
    
    /**
     * Function to Store Location
     **/
    
    public JSONObject locationUser(String uid, String latitude, String longitude){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", loc_tag));
        params.add(new BasicNameValuePair("uid", uid));
        params.add(new BasicNameValuePair("latitude", latitude));
        params.add(new BasicNameValuePair("longitude", longitude));
        JSONObject json = jsonParser.getJSONFromUrl(locURL, params);
        return json;
    }
    
    /**
     * Function to Search for Ride
     **/
    
    public JSONObject searchRide(String uid, String latitude, String longitude){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", search_tag));
        params.add(new BasicNameValuePair("uid", uid));
        params.add(new BasicNameValuePair("latitude", latitude));
        params.add(new BasicNameValuePair("longitude", longitude));
        JSONObject json = jsonParser.getJSONFromUrl(locURL, params);
        return json;
    }


    /**
     * Function to change password
     **/

    public JSONObject chgPass(String newpas, String email){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", chgpass_tag));

        params.add(new BasicNameValuePair("newpas", newpas));
        params.add(new BasicNameValuePair("email", email));
        JSONObject json = jsonParser.getJSONFromUrl(chgpassURL, params);
        return json;
    }


    /**
     * Function to reset the password
     **/

    public JSONObject forPass(String forgotpassword){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", forpass_tag));
        params.add(new BasicNameValuePair("forgotpassword", forgotpassword));
        JSONObject json = jsonParser.getJSONFromUrl(forpassURL, params);
        return json;
    }




     /**
      * Function to  Register
      **/
    public JSONObject registerUser(String fname, String lname, String email, String uname, String password, String weight_gender, String weight_smoker
    		, String pref_gender, String pref_smoker, String latitude, String longitude, String gcm_regid){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", register_tag));
        params.add(new BasicNameValuePair("fname", fname));
        params.add(new BasicNameValuePair("lname", lname));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("uname", uname));
        params.add(new BasicNameValuePair("password", password));
        params.add(new BasicNameValuePair("weight_gender", weight_gender));
        params.add(new BasicNameValuePair("weight_smoker", weight_smoker));
        params.add(new BasicNameValuePair("pref_gender", pref_gender));
        params.add(new BasicNameValuePair("pref_smoker", pref_smoker));
        params.add(new BasicNameValuePair("latitude", latitude));
        params.add(new BasicNameValuePair("longitude", longitude));
        params.add(new BasicNameValuePair("gcm_regid", gcm_regid));
        JSONObject json = jsonParser.getJSONFromUrl(registerURL,params);
        return json;
    }


    /**
     * Function to logout user
     * Resets the temporary data stored in SQLite Database
     * */
    public boolean logoutUser(Context context){
        DatabaseHandler db = new DatabaseHandler(context);
        db.resetTables();
        return true;
    }

}

