package com.application.shuttershare;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;


/*
* Authors: Jesse Jeun
* Date: 10-19-2015
* Description: MainActivity Container - Container for Camera, Video, Gallery and Setting Fragments
*           Also holds the logic for flipping from fragment to fragment.
*/



public class MainActivity extends FragmentActivity {

    int page;

    public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {

        // declared variables for the class
        final int PAGE_COUNT = 4;
        private String tabTitles[] = new String[] { "Camera", "Video", "Gallery", "Settings"};

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
                    return Video.newInstance(position + 1);
                case 2:
                    return Gallery.newInstance(position + 1);
                case 3:
                    return Settings.newInstance(position + 1);
                default:
                    return null;
            }
        }


        // method that will generate the page titles based on fragment page position
        @Override
        public CharSequence getPageTitle(int position) {

            // Generate title based on item position
            return tabTitles[position];
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
    @Override
    public void onBackPressed(){
        // intentionally left empty to disable back button
    }

}
