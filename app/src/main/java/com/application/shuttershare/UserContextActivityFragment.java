package com.application.shuttershare;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A placeholder fragment containing a simple view.
 */
public class UserContextActivityFragment extends Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";

    public UserContextActivityFragment() {
    }

    public static UserContextActivityFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        UserContextActivityFragment fragment = new UserContextActivityFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_user_context, container, false);
        return rootView;
    }
}
