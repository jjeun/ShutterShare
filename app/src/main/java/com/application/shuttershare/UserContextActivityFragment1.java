package com.application.shuttershare;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


/*
* Authors: Jesse Jeun
* Date: 10-19-2015
* Description: Context Fragment - Will hold instrucitonal info about functionality for the user
*/



public class UserContextActivityFragment1 extends Fragment {

    // Variables declared for the class
    public static final String ARG_PAGE = "ARG_PAGE";
    public static final String TAG = "ContextActivityFragment";
   // private ImageButton exitButton;

    public UserContextActivityFragment1() {
        // Required empty public constructor
    }



    // Method that creates the UserContextActivityFragment takes in arguement
    // page used to display correct page
    public static UserContextActivityFragment1 newInstance(int page) {
        Bundle args = new Bundle();  // declaring object args that is an instance of Bundle class
        args.putInt(ARG_PAGE, page); // storing the page into variable ARG_PAGE that will be passed to fragment
        UserContextActivityFragment1 fragment = new UserContextActivityFragment1(); // declaring objec fragment  that is a instance of UserContextActivityFragment1
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
        View rootView = inflater.inflate(R.layout.fragment_user_context1, container, false);

        return rootView;    //returning the rootView to be displayed on the fragment
    }


}
