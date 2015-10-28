package com.application.shuttershare;

import android.content.Entity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



/*
* Authors: Jesse Jeun
* Date: 10-19-2015
* Description: EventCode Activity - Activity page that will get the users event code.
*       This info will be used to connect to the appropriate event to download pictures to.
*/


public class EventCode extends AppCompatActivity {

    // Variables declared for the class
    public static final String TAG = "EventCode";
    Button submitButton;
    EditText eventCode;

    HttpPost httppost;
    StringBuffer buffer;
    HttpResponse response;
    HttpClient httpclient;
    List<NameValuePair> nameValuePairs;
    String success;
    String eventcode;
    String description;
    String date;
    int days;


    JSONArray event = null;

    // onCreate Method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_code);

        // declaring shared of type SharedPreferences
        SharedPreferences shared = getSharedPreferences("SHUTTER_SHARE", MODE_PRIVATE);

        // condition that will check if variable eventcode exists in shared prefrences if it does
        // it will by pass the activity and move to the next
        if (shared.contains("eventcode")) {

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);  // starting the intent

        } else {

            // intializing exitButton to value of type Button with id eventtButton
            submitButton = (Button) findViewById(R.id.eventButton);
            eventCode = (EditText) findViewById(R.id.eventcode);

            // creating on click listener for submitButton
            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.v(TAG, "Submit Button Clicked For Login Page");


                    try {

                        // TODO: 10/27/2015 Need to re-code this try section. This code is not hitting the database at all.

                        httpclient = new DefaultHttpClient();
                        httppost = new HttpPost(UploadConfig.CHECK_URL);
                        // Add your data
                        nameValuePairs = new ArrayList<NameValuePair>();
                        nameValuePairs.add(new BasicNameValuePair("eventCode", eventcode.trim()));
                        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                        // Execute HTTP Post Request
                        response = httpclient.execute(httppost);
                        String responseString = response.toString();

                        JSONObject json = new JSONObject(responseString);
                        success = json.getString("sucess");

                        Log.v(TAG, success);

                        JSONArray event = json.getJSONArray("event");

//                        for (int i=0; i < event.length(); i++){
//                            JSONObject c = event.getJSONObject(i);
//
//                            eventcode = c.getString("eventcode");
//                            description = c.getString("description");
//                            date = c.getString("date");
//                            days = c.getInt("days");
//
//                        }


                    }

                    catch (Exception e)
                    {
                        Toast.makeText(EventCode.this, "Sorry! There was a Database error", Toast.LENGTH_LONG).show();
                        Log.v(TAG, e.toString());
                    }


                    if(success == "1")
                    {
                        Toast.makeText(EventCode.this, "Event Code Exists", Toast.LENGTH_LONG).show();
                        saveInformation(eventcode, description, date, days );
                        submit(); // calling the submit method
                    }
                    else
                    {
                        Toast.makeText(EventCode.this, "Invalid Event Code", Toast.LENGTH_LONG).show();
                        tryAgain();
                    }
                }
            });
        }
    }


    // Method to create intent and activate it
    public void submit(){

        // creating object intent of class Intent that will redirect to MainActivity page
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);  // starting the intent
    }


    // Method to create intent and activate it
    public void tryAgain(){

        // creating object intent of class Intent that will redirect to MainActivity page
        Intent intent = new Intent(this, EventCode.class);
        startActivity(intent);  // starting the intent
    }


    // method to disable on back press button
    @Override
    public void onBackPressed(){
            // intentionally left empty to disable back button
    }


    // method that will place information in the shared prefrences of the device
    // this will be used to determine if the user is a firstimer to the app.
    // if not then portions of the app will be bypassed.
    public void saveInformation(String eventcode, String descripition, String date, int days) {
        SharedPreferences shared = getSharedPreferences("SHUTTER_SHARE", MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.putString("eventcode", eventcode);
        editor.putString("description", description);
        editor.putString("date", date);
        editor.putInt("days", days);
        editor.commit();
    }

}
