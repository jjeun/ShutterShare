package com.application.shuttershare;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.app.AppCompatActivity;

import java.io.File;


/*
* Authors: Jesse Jeun
* Date: 10-19-2015
* Description: Camera Fragment - Will hold camera functionality
*/



public class Camera extends Fragment{

    // Declaring variables to be used in the class
    public static final String ARG_PAGE = "ARG_PAGE";
    public static final String TAG = "CameraFragment";
    static int TAKE_PIC = 1;
    Uri outPutfileUri;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;



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

        ClickMe = (Button) rootView.findViewById(R.id.cameraButton);

        ClickMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goGoGadgetCamera(getView());

            }
        });
        return rootView;

    }
// Code to Open camera
    public void goGoGadgetCamera (View v){
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File file = new File(Environment.getExternalStorageDirectory(),
                "MyPhoto.jpg");
        outPutfileUri = Uri.fromFile(file);
        //launch camera
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outPutfileUri);
        getActivity().startActivityForResult(intent, 1);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TAKE_PIC && resultCode==getActivity().RESULT_OK){
            Toast.makeText(getActivity(), outPutfileUri.toString(), Toast.LENGTH_LONG).show();
        }
    }
}
