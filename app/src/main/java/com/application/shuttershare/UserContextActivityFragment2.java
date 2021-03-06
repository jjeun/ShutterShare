package com.application.shuttershare;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;


/*
* Authors: Jesse Jeun
* Date: 10-19-2015
* Description: Context Fragment - Will hold instrucitonal info about functionality for the user
*/


public class UserContextActivityFragment2 extends Fragment {

    // Variables declared for the class
    public static final String ARG_PAGE = "ARG_PAGE";
    public static final String TAG = "ContextActivityFragment";
    private ImageButton exitButton;

    public UserContextActivityFragment2() {
        // Required empty public constructor
    }



    // Method that creates the UserContextActivityFragment takes in arguement
    // page used to display correct page
    public static UserContextActivityFragment2 newInstance(int page) {
        Bundle args = new Bundle();  // declaring object args that is an instance of Bundle class
        args.putInt(ARG_PAGE, page); // storing the page into variable ARG_PAGE that will be passed to fragment
        UserContextActivityFragment2 fragment = new UserContextActivityFragment2(); // declaring objec fragment  that is a instance of UserContextActivityFragment2
        fragment.setArguments(args); // calling method setArguments of fragment object and passing variable args as argument
        return fragment;            // returning the fragment variable
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
        // creating object rootView that is istantiated to value of type ViewGroup
        View rootView = inflater.inflate(R.layout.fragment_user_context2, container, false);

        // intializing exitButton to value of type ImageButton with id exitButton
        exitButton = (ImageButton) rootView.findViewById(R.id.exitButton);

        // creating on click listener for exitButton
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "Exit Button Clicked ContextFragment");

                // creating object intent of class Intent that will redirect to Login page
                Intent intent = new Intent(getActivity(), Login.class);
                startActivity(intent);  // starting the intent
            }
        });

        return rootView;    //returning the rootView to be displayed on the fragment
    }


}
