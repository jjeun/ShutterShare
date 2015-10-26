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
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.app.AppCompatActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


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
    private ImageView imgPreview;

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


    public Camera() {
        // Required empty public constructor
    }

    Button ClickMe;
    TextView tv;


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

        imgPreview = (ImageView) rootView.findViewById(R.id.imgPreview);

        ClickMe = (Button) rootView.findViewById(R.id.cameraButton);

        ClickMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goGoGadgetCamera(getView());

            }
        });

        return rootView;

    }


    // Code for functionality of camera
    public void goGoGadgetCamera (View v){
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File mediaStorage = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorage.exists()) {
            if (!mediaStorage.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
            }
        }


        // getting date and time to be used to create a unique image name -- JJ
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd_HHmmss");
        String currentDateAndTime = sdf.format(new Date());

        File file;

        file = new File(mediaStorage.getPath() + File.separator + "Image_"+currentDateAndTime+".jpg");

        outPutfileUri = Uri.fromFile(file);

        //launch camera
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outPutfileUri);

        // start of the image capture intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);

//        startActivityForResult(intent,111);
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

                    launchUpload();

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
        SharedPreferences shared = this.getActivity().getSharedPreferences("SHUTTER_SHARE", Context.MODE_PRIVATE);

        String username = shared.getString("username", "");
        String eventcode = shared.getString("eventcode", "");
        Intent i = new Intent(this.getActivity(), UploadActivity.class);
        
        i.putExtra("filePath", outPutfileUri.getPath());
        i.putExtra("username", username);
        i.putExtra("eventcode", eventcode);
        startActivity(i);
    }
}


