package com.application.shuttershare;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Config;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.app.AppCompatActivity;

import java.io.File;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.IllegalFormatCodePointException;


/*
* Authors: Jesse Jeun
* Date: 10-19-2015
* Description: Camera Fragment - Will hold camera functionality
*/



public class Camera extends Fragment{

    // Declaring variables to be used in the class
    public static final String ARG_PAGE = "ARG_PAGE";
    public static final String TAG = "CameraFragment";

    private static int MEDIA_TYPE_IMAGE = 1;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final String IMAGE_DIRECTORY_NAME = "ShutterShare";
    private Uri outPutfileUri; // file url to store image
    ImageButton ClickMe;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param page fragment page number
     * @return A new instance of fragment Camera.
     */

    public static Camera newInstance(int page) {
        Camera fragment = new Camera();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        fragment.setArguments(args);
        return fragment;
    }


    // empty default constructor
    public Camera() {
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
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_camera, container, false);

        // declaring and initializing shared prefrences function of android devices. Holds in memory
        // variablse given to it for the life of the application
        SharedPreferences shared = this.getActivity().getSharedPreferences("SHUTTER_SHARE", Context.MODE_PRIVATE);
        String eventDescription = shared.getString("description", "ShutterShare"); // retrieving variable description in shared preferences initialized to eventDescription

        // programmically setting of the TextView "eventDescripition" to different style settings
        TextView event = (TextView) rootView.findViewById(R.id.eventDescription);
        event.setText(eventDescription);
        event.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        event.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);

        // initializing and implementing the ImageButton that will initiate the camera function
        ClickMe = (ImageButton) rootView.findViewById(R.id.cameraButton);
        ClickMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goGoGadgetCamera(getView());

            }
        });

        return rootView;  //  return fragment view

    }


    // Code for functionality of camera
    public void goGoGadgetCamera (View v){

        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // declaring and intializing shared preferences function on android devices and also initializing the editor
        // to write to shared preferences
        SharedPreferences shared = this.getActivity().getSharedPreferences("SHUTTER_SHARE", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();

        String username = shared.getString("username", "");  // getting variable username from shared preferences


        File mediaStorage = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);  // creating a new directory that the images will be stored

        // condition used to check if storage directory is created or not
        if (!mediaStorage.exists()) {
            if (!mediaStorage.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
            }
        }


        // getting date and time to be used to create a unique image name -- JJ
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd_HHmmss");
        String currentDateAndTime = sdf.format(new Date());

        // declaring File file and initializing it to the path of the specific image being taken
        File file;
        file = new File(mediaStorage.getPath() + File.separator + username +"_"+currentDateAndTime+".jpg");

        // getting uri info from the file
        outPutfileUri = Uri.fromFile(file);
        // storing the string version of the uri into shared preferences to be accessed later
        editor.putString("imageFile", outPutfileUri.toString());
        editor.commit();

        //launch camera
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outPutfileUri);

        // start of the image capture intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);

    }


    // to receive result of camera
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == getActivity().RESULT_OK) {

                // user cancelled Image capture
                Toast.makeText(getActivity().getApplicationContext(),
                        "Image Upload Page", Toast.LENGTH_SHORT)
                        .show();

                    launchUpload();  // calling launchUpload method below

            } else if (resultCode == getActivity().RESULT_CANCELED) {

                // user cancelled Image capture
                Toast.makeText(getActivity().getApplicationContext(),
                        "Image Capture Cancelled", Toast.LENGTH_SHORT)
                        .show();

            } else {

                // failed to capture image
                Toast.makeText(getActivity().getApplicationContext(),
                        "Sorry! Image Failed to Capture", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }


    // method to launch upload
    private void launchUpload(){
        // calling and declaring a new instance of shared preferences on android devices
        SharedPreferences shared = this.getActivity().getSharedPreferences("SHUTTER_SHARE", Context.MODE_PRIVATE);

        // initializing a new variable to values stored in shared preferences
        String username = shared.getString("username", "");
        String eventcode = shared.getString("eventcode", "");
        String image = shared.getString("imageFile", "");
        outPutfileUri = Uri.parse(image);

        // creating a new intent to UploadActivity class
        Intent i = new Intent(this.getActivity(), UploadActivity.class);

        // creating variable that will be passed to the activity of the intent
        i.putExtra("filePath", outPutfileUri.getPath());
        i.putExtra("username", username);
        i.putExtra("eventcode", eventcode);
        startActivity(i);  // start the activity of the intent
    }


}


