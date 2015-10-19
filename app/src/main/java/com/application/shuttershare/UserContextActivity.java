package com.application.shuttershare;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import android.os.Bundle;



/*
* Authors: Jesse Jeun
* Date: 10-19-2015
* Description: UserContextActivity Container - Container for UserContextActivity, UserContextActivityFragment1,
*       UserContextActivityFragment2, and UserContextActivityFragment3. Also holds the logic for flipping from fragment to fragment.
*/

public class UserContextActivity extends FragmentActivity {

    // declared variables for the class
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;


    // onCreate method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_context);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

    }


    // onBackPressed method that will flip the fragment back when back button is pressed
    @Override
    public void onBackPressed(){
        if(mPager.getCurrentItem() == 0) {
            super.onBackPressed();
        }

        else{
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    // Pager adapter class that will determine which fragment should appear on the screen
    public class ScreenSlidePagerAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT = 4;

        // pager adapter method
        public ScreenSlidePagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        // method that will return the page the fragment is on
        @Override
        public int getCount() {
            return PAGE_COUNT;
        }


        // method that will return the specific fragment dependant on the position
        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return UserContextActivityFragment.newInstance(position);
                case 1:
                    return UserContextActivityFragment1.newInstance(position);
                case 2:
                    return UserContextActivityFragment2.newInstance(position);
                case 3:
                    return UserContextActivityFragment3.newInstance(position);
                default:
                    return null;
            }
        }

    }

}
