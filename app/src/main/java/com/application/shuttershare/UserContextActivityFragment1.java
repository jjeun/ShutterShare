package com.application.shuttershare;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserContextActivityFragment1 extends Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";

    public UserContextActivityFragment1() {
        // Required empty public constructor
    }


    public static UserContextActivityFragment1 newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        UserContextActivityFragment1 fragment = new UserContextActivityFragment1();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_user_context1, container, false);
        return rootView;
    }


}
