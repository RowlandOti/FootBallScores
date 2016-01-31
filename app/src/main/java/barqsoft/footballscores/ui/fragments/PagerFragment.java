package barqsoft.footballscores.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.Date;

import barqsoft.footballscores.R;
import barqsoft.footballscores.ui.activities.MainActivity;
import barqsoft.footballscores.ui.adapters.SmartFragmentStatePagerAdapter;
import barqsoft.footballscores.utilities.TimeUtility;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by yehya khaled on 2/27/2015.
 */
public class PagerFragment extends Fragment {

    public static final int NUM_PAGES = 5;
    // ButterKnife injected views
    @Bind(R.id.viewpager)
    ViewPager mViewPager;
    @Bind(R.id.toolbar)
    Toolbar mToolBar;
    @Bind(R.id.pagertabstrip)
    PagerTabStrip mPagerTabStrip;
    private myPageAdapter mPagerAdapter;
    private MainFragment[] viewFragments = new MainFragment[5];

    // Called to instantiate the fragment's view hierarchy
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.pager_fragment, container, false);
        // Inflate all views
        ButterKnife.bind(this, rootView);
        // Return the view for this fragment
        return rootView;
    }

    // Called after onCreateView() is done i.e the fragment's view has been created
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPagerAdapter = new myPageAdapter(getChildFragmentManager());
        for (int i = 0; i < NUM_PAGES; i++) {
            Date fragmentdate = new Date(System.currentTimeMillis() + ((i - 2) * 86400000));
            String formatString = getString(R.string.date_format_ymd);
            SimpleDateFormat mformat = new SimpleDateFormat(formatString);
            viewFragments[i] = new MainFragment();
            viewFragments[i].setFragmentDate(mformat.format(fragmentdate));
        }
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(MainActivity.current_fragment);
        mPagerTabStrip.setBackgroundColor(getResources().getColor(R.color.apptheme_accent_red));
        mPagerTabStrip.setTabIndicatorColor(getResources().getColor(R.color.apptheme_accent_teal));
    }

    // Called when the containing activity onCreate() is done, and after onCreateView() of fragment
    // Do final modification on the hierarchy e.g modify view elements and restore previous state
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Check which instance we are dealing with
        if (getActivity() instanceof MainActivity) {
            // Set the ToolBar
            ((MainActivity) getActivity()).setToolbar(mToolBar, false, true, R.drawable.toolbar_logo);
        }
    }

    public int getCurrentItem(){
        return mViewPager.getCurrentItem();
    }

    private class myPageAdapter extends SmartFragmentStatePagerAdapter {
        public myPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return viewFragments[i];
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return TimeUtility.getDayName(getActivity(), System.currentTimeMillis() + ((position - 2) * 86400000));
        }
    }
}
