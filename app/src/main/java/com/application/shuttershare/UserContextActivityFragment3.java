package com.application.shuttershare;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserContextActivityFragment3 extends Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public UserContextActivityFragment3() {
        // Required empty public constructor
    }


    public static UserContextActivityFragment3 newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        UserContextActivityFragment3 fragment = new UserContextActivityFragment3();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Intent intent = new Intent(getActivity(), Login.class);
        startActivity(intent);

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_user_context3, container, false);
        return rootView;
    }


}
