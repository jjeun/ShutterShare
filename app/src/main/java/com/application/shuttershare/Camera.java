package com.application.shuttershare;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/*
* Authors: Jesse Jeun
* Date: 10-19-2015
* Description: Camera Fragment - Will hold camera functionality
*/



public class Camera extends Fragment {

    // Declaring variables to be used in the class
    public static final String ARG_PAGE = "ARG_PAGE";
    public static final String TAG = "CameraFragment";

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
        return inflater.inflate(R.layout.fragment_camera, container, false);
    }
}
