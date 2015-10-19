package com.application.shuttershare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;



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


    // onCreate Method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_code);

        // intializing exitButton to value of type Button with id eventtButton
        submitButton = (Button) findViewById(R.id.eventButton);

        // creating on click listener for submitButton
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "Submit Button Clicked For EventCode Page");
                submit();
            }
        });
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

}
