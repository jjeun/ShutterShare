package com.application.shuttershare;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

/*
* Authors: Jesse Jeun
* Date: 10-19-2015
* Description: Login Activity - Activity page that will get the users name and email.
*       This info will be used to tag who took which photos.
*/

public class Login extends AppCompatActivity {

    // Variables declared for the class
    public static final String TAG = "LoginPage";
    ImageButton submitButton;
    EditText username;
    EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences shared = getSharedPreferences("SHUTTER_SHARE", MODE_PRIVATE);
        String user = shared.getString("username", "");

        // condition that will check if variable username and email exists in shared prefrences if it does
        // it will by pass the activity and move to the next
        if(!user.equals("")){

            Intent intent = new Intent(this, EventCode.class);
            startActivity(intent);  // starting the intent

        } else {

            submitButton = (ImageButton) findViewById(R.id.loginButton);
            username = (EditText) findViewById(R.id.name);
            email = (EditText) findViewById(R.id.email);

            // creating on click listener for submitButton
            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.v(TAG, "Submit Button Clicked For Login Page");
                    saveInformation(username.getText().toString(), email.getText().toString());
                    submit(); // calling the submit method
                }
            });
        }
    }


    // Method to create intent and activate it
    public void submit(){

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
    public void saveInformation(String username,String email) {
        SharedPreferences shared = getSharedPreferences("SHUTTER_SHARE", MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.putString("username", username);
        editor.putString("email", email);
        editor.commit();
    }
}
