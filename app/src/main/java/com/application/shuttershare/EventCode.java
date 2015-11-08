package com.application.shuttershare;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;



/*
* Authors: Jesse Jeun
* Date: 10-19-2015
* Description: EventCode Activity - Activity page that will get the users event code.
*       This info will be used to connect to the appropriate event to download pictures to.
*/


public class EventCode extends Activity {

    // Variables declared for the class
    public static final String TAG = "EventCode";
    ImageButton submitButton;
    ImageButton eventButton;
    EditText eventCode;
    int success;
    String eventcode;
    String description;
    String date;
    int days;
    private ProgressDialog pDialog;
    JSONArray event = null;
    JSONParser jsonParser = new JSONParser();


    // onCreate Method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_code);

        // declaring shared of type SharedPreferences
        SharedPreferences shared = getSharedPreferences("SHUTTER_SHARE", MODE_PRIVATE);

        // condition that will check if variable eventcode exists in shared prefrences if it does
        // it will by pass the activity and move to the next
        if (shared.contains("eventcode") && checkDate()) {

            // creating a new intent and also removing the event info from the stack to
            // prevent the user from returning back to this page
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);  // starting the intent

        } else {

            // intializing submitButton to value of type Button with id eventButton
            submitButton = (ImageButton) findViewById(R.id.eventButton);
            eventButton = (ImageButton) findViewById(R.id.createEventButton);
            eventCode = (EditText) findViewById(R.id.eventcode);

            // creating on click listener for submitButton
            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.v(TAG, "Submit Button Clicked For Login Page");
                    eventcode = eventCode.getText().toString().trim();
                    Log.v(TAG, eventcode);
                    new CheckEventCode().execute();
                }
            });


            // creating on click listener for submitButton
            eventButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.v(TAG, "Submit Button Clicked For Login Page");

                    createEvent();
                }
            });
        }
    }


    // Method to create intent and send user to Main
    public void submit(){

        // creating object intent of class Intent that will redirect to MainActivity page
        // also removing info from the stack to prevent user from returning with the back button
        Toast.makeText(getApplicationContext(),"Event Code Success!", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);  // starting the intent
    }



    // method that will be used the check if the event date has expired.
    // will return false if expired of true if the event is still ongoing
    public boolean checkDate(){

        // declaring shared of type SharedPreferences
        SharedPreferences shared = getSharedPreferences("SHUTTER_SHARE", MODE_PRIVATE);

        // initializing the dateFormat, date, and calendar variables which
        // will be used to get the current date and also determine the endDate
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        Date currentDate = c.getTime();

        // getting the date and days from shared preferences
        String eventDate = shared.getString("date", "");
        int days = shared.getInt("days", 0);

        // try-catch to pared the String eventDate that was saved in shared preferences from the database
        try {
            c.setTime(dateFormat.parse(eventDate));
        }catch (ParseException e){
            e.printStackTrace();
        }

        c.add(Calendar.DATE, days); // adding the number of days to the eventDate to get the endDate
        Date endDate =  c.getTime();    // getting the endDate

        Log.v(TAG, eventDate);
        Log.v(TAG,dateFormat.format(endDate));

        // condition to check if the current date less than or equal to expiration date
        // if true the will return true.
        if (currentDate.after(endDate)){
            Log.v(TAG, "Event has expired");
            return false;
        }

        Log.v(TAG, "Event is still going on");
        return true;
    }


    // method to disable on back press button
    @Override
    public void onBackPressed(){
            // intentionally left empty to disable back button
    }


    // method that will place information in the shared prefrences of the device
    // this will be used to determine if the user is a firstimer to the app.
    // if not then portions of the app will be bypassed.
    public void saveInformation() {
        // calling instance of shared preference of the android device
        SharedPreferences shared = getSharedPreferences("SHUTTER_SHARE", MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();

        // declaring and intializing variables that will be stored in shared preferences
        editor.putString("eventcode", eventcode);
        editor.putString("description", description);
        editor.putString("date", date);
        editor.putInt("days", days);
        editor.commit();  // sending variable to be stored in shared preferences
    }


    // method that will send user to mobile website when create event button is clicked
    public void createEvent(){

        Uri uri = Uri.parse("http://52.27.86.208:8080/ShutterShare");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }


    /* method that asyncronously check if the eventCode exists in the database
     if does exist it will save information retrieved from the database to the
     shared preferences and send the user to the main activity. If it doesn't
     exist the code will prompt user via toast and then take them back to enter
     a different eventCode. */
    class CheckEventCode extends AsyncTask<String, String, String> {


         // Before starting background thread Show Progress Dialog
        // indicates to user how long the check has left
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EventCode.this);
            pDialog.setMessage("Checking Event Code. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }


            // getting the eventcode information from the background thread
            // will save shared information and redirect to Main if it exsits
            // otherwise will return user to enter a different eventCode.
            protected String doInBackground(String... args) {

                    // Building Parameters
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("eventcode", eventcode));


                    // getting eventCode details by making HTTP request
                    JSONObject json = jsonParser.makeHttpRequest(
                        UploadConfig.CHECK_URL, "POST", params);

                        // try checing the the database catch any issues in connecting
                        try {

                            // Check your log cat for JSON reponse
                            Log.d("Event Info: ", json.toString());

                            // Checking for SUCCESS TAG
                            success = json.getInt("success");

                            if (success == 1) {
                                // products found
                                // Getting Array of Products
                                event = json.getJSONArray("event");

                                // looping through all the elements in an Event
                                for (int i = 0; i < event.length(); i++) {
                                    JSONObject c = event.getJSONObject(i);

                                    // Storing each json item in variable
                                    eventcode = c.getString("eventcode");           // eventCode
                                    description = c.getString("description");       // event description
                                    days = c.getInt("days");                        // # of days for the event
                                    date = c.getString("date");                     // the start date of the event

                                }

                            } else {
                                // no eventCode found
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "Sorry Invalid Event Code!", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                return null; // returning null if successful
            }



        // method that executes after background method is complete
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting eventCode
            pDialog.dismiss();

            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {


                    // condition if database query successful update shared preferences by
                    // calling saveInformation method and then the submit method to take user
                    // to main. Otherwise call tryAgain method
                    if(success == 1){
                        saveInformation();

                        // condition to check the eventDate to make sure the event hasn't expired
                        // if not send user to Main. If it is expired return user to eventCode page.
                        if (checkDate()){
                            submit();
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Sorry Invalid Event Code!", Toast.LENGTH_LONG).show();
                        }
                    }

                    else{
                        Toast.makeText(getApplicationContext(),"Sorry Invalid Event Code!", Toast.LENGTH_LONG).show();
                    }

                }
            });

        }

    }

}
