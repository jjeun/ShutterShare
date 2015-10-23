package com.application.shuttershare;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;



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

    // onCreate Method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_code);


        // TODO: 10/22/2015 Need to enter code here to get date info from database to make sure not expired and eventcode exists.

        SharedPreferences shared = getSharedPreferences("shared", MODE_PRIVATE);

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
                    saveInformation(eventCode.getText().toString());
                    submit(); // calling the submit method
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


    // method to disable on back press button
    @Override
    public void onBackPressed(){
            // intentionally left empty to disable back button
    }


    // method that will place information in the shared prefrences of the device
    // this will be used to determine if the user is a firstimer to the app.
    // if not then portions of the app will be bypassed.
    public void saveInformation(String eventcode) {
        SharedPreferences shared = getSharedPreferences("shared", MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.putString("eventcode", eventcode);
        editor.commit();
    }

}
