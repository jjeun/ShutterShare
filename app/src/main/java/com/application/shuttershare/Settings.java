package com.application.shuttershare;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/*
* Authors: Jesse Jeun
* Date: 10-19-2015
* Description: Settings Fragment - Will hold settings functionality
*/


public class Settings extends Fragment {

    // Declaring variables to be used in the class
    public static final String ARG_PAGE = "ARG_PAGE";
    public static final String TAG = "SettingsFragment";

    // Variables declared for the class
    ImageButton submitButton;
    EditText eventCode;
    EditText userName;
    EditText userEmail;

    int success;
    String eventcode;
    String username;
    String email;
    String description;
    String date;
    int days;

    private ProgressDialog pDialog;
    JSONArray event = null;
    JSONParser jsonParser = new JSONParser();

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param page fragment page number
     * @return A new instance of fragment Settings.
     */

    public static Settings newInstance(int page) {
        Settings fragment = new Settings();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        fragment.setArguments(args);
        return fragment;
    }


    public Settings() {
        // Required empty public constructor
    }



    // onCreate variable for fragment
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    // onCreateView method for fragment. Handles what will be displayed in the fragment page
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        SharedPreferences shared = getActivity().getSharedPreferences("SHUTTER_SHARE", getActivity().MODE_PRIVATE);

        eventcode = shared.getString("eventcode", "");
        username = shared.getString("username", "");
        email = shared.getString("email", "");

        // intializing submitButton to value of type Button with id eventButton
        submitButton = (ImageButton) rootView.findViewById(R.id.accountButton);
        eventCode = (EditText) rootView.findViewById(R.id.eventCode);
        userName = (EditText) rootView.findViewById(R.id.userName);
        userEmail = (EditText) rootView.findViewById(R.id.userEmail);

        eventCode.setText(eventcode);
        userName.setText(username);
        userEmail.setText(email);

        // creating on click listener for submitButton
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "Submit Button Clicked For Login Page");
                eventcode = eventCode.getText().toString().trim();
                username = userName.getText().toString().trim();
                email = userEmail.getText().toString().trim();
                Log.v(TAG, eventcode);
                Log.v(TAG, username);
                Log.v(TAG, email);
                new CheckEventCode().execute();
            }
        });

        return rootView;
    }


    // Method to create intent and send user to Main
    public void submit(){

        // creating object intent of class Intent that will redirect to MainActivity page
        Toast.makeText(getActivity().getApplicationContext(), "Event Code Success!", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);  // starting the intent
    }



    // method that will be used the check if the event date has expired.
    // will return false if expired of true if the event is still ongoing
    public boolean checkDate(){

        // declaring shared of type SharedPreferences
        SharedPreferences shared = getActivity().getSharedPreferences("SHUTTER_SHARE", getActivity().MODE_PRIVATE);

        // initializing the dateFormat, date, and calendar variables which
        // will be used to get the current date and also determine the endDate
        DateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd");
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


    // method that will place information in the shared prefrences of the device
    // this will be used to determine if the user is a firstimer to the app.
    // if not then portions of the app will be bypassed.
    public void saveInformation() {
        SharedPreferences shared = getActivity().getSharedPreferences("SHUTTER_SHARE", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();

        editor.putString("description", description);
        editor.putString("date", date);
        editor.putInt("days", days);

        if (!(shared.getString("eventcode", "")).equals("")){
            editor.putString("eventcode", eventcode);
        }

        if (!(shared.getString("username", "")).equals("")){
            editor.putString("username", username);
        }

        if (!(shared.getString("email", "")).equals("")){
            editor.putString("email", email);
        }


        editor.commit();
    }




    /* method that asyncronously check if the eventCode exists in the database
    if does exist it will save information retrieved from the databse to the
    shared preferences and send the user to the main activity. If it doesn't
    exist the code will prompt user via toast and then take them back to enter
    a different eventCode. */
    class CheckEventCode extends AsyncTask<String, String, String> {


        // Before starting background thread Show Progress Dialog
        // indicates to user how long the check has left
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Updated Account Information. Please wait...");
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
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getActivity().getApplicationContext(),"Sorry Invalid Event Code!", Toast.LENGTH_LONG).show();
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
            getActivity().runOnUiThread(new Runnable() {
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
                            Toast.makeText(getActivity().getApplicationContext(),"Sorry Invalid Event Code!", Toast.LENGTH_LONG).show();
                        }
                    }

                    else{
                        Toast.makeText(getActivity().getApplicationContext(),"Sorry Invalid Event Code!", Toast.LENGTH_LONG).show();
                    }

                }
            });

        }

    }

}
