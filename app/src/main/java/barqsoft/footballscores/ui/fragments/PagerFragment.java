package barqsoft.footballscores.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import barqsoft.footballscores.R;
import barqsoft.footballscores.ui.activities.MainActivity;
import barqsoft.footballscores.ui.adapters.SmartNestedViewPagerAdapter;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Rowland on 1/30/2016.
 */
public class PagerFragment extends Fragment {

    public static final int NUM_PAGES = 5;
    // The class Log identifier
    private final String LOG_TAG = PagerFragment.class.getSimpleName();
    // ButterKnife injected views
    @Bind(R.id.viewpager)
    ViewPager mViewPager;
    @Bind(R.id.toolbar)
    Toolbar mToolBar;
    @Bind(R.id.pagertabstrip)
    PagerTabStrip mPagerTabStrip;

    private SmartNestedViewPagerAdapter mPagerAdapter;

    public static PagerFragment newInstance(Bundle args) {
        PagerFragment fragment = new PagerFragment();
        if (args != null) {
            fragment.setArguments(args);
        }
        return fragment;
    }

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

        mPagerAdapter = new SmartNestedViewPagerAdapter(getChildFragmentManager(), getActivity());
        mViewPager.setAdapter(mPagerAdapter);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    public int getCurrentItem() {
        return mViewPager.getCurrentItem();
    }
}
