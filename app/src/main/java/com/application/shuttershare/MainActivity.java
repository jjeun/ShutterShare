package com.application.shuttershare;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;




/*
* Authors: Jesse Jeun
* Date: 10-19-2015
* Description: MainActivity Container - Container for Camera, Video, Gallery and Setting Fragments
*           Also holds the logic for flipping from fragment to fragment.
*/



public class MainActivity extends FragmentActivity {

    int page;

    public class SampleFragmentPagerAdapter extends FragmentPagerAdapter implements PagerSlidingTabStrip.IconTabProvider {

        // declared variables for the class
        final int PAGE_COUNT = 3;
        private int tabIcons[] = {R.drawable.camera, R.drawable.gallery, R.drawable.settings};

        // method for fragment page adapter
        public SampleFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        // method that returns page count
        @Override
        public int getCount() {
            return PAGE_COUNT;
        }


        // fragment that will return with the specific page fragment that needs to be displayed
        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return Camera.newInstance(position + 1);
                case 1:
                    return Gallery.newInstance(position + 1);
                case 2:
                    return Settings.newInstance(position + 1);
                default:
                    return null;
            }
        }


        // method that will generate the page titles based on fragment page position
        @Override
        public int getPageIconResId(int position) {

            // Generate title based on item position
            return tabIcons[position];
        }
    }


    // onCreate method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new SampleFragmentPagerAdapter(getSupportFragmentManager()));

        // Give the PagerSlidingTabStrip the ViewPager
        PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);

        // Attach the view pager to the tab strip
        tabsStrip.setViewPager(viewPager);
    }


    // method to disable on back press button
    private Boolean exit = false;
    @Override
    public void onBackPressed(){

        if (exit) {
            finish(); // finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Override this method in the activity that hosts the Fragment and call super
        // in order to receive the result inside onActivityResult from the fragment.
        super.onActivityResult(requestCode, resultCode, data);

    }

}
