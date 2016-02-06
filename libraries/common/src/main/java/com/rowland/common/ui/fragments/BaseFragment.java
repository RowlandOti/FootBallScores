package com.rowland.common.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by Oti Rowland on 2/6/2016.
 */
public class BaseFragment extends Fragment{

    // Logging tracker for this class
    private final String LOG_TAG = BaseFragment.class.getSimpleName();

    protected static BaseFragment newInstance(BaseFragment fragment, Bundle args) {
        // Create the new fragment instance
        BaseFragment fragmentInstance = fragment;
        // Set arguments if it is not null
        if (args != null) {
            fragmentInstance.setArguments(args);
        }
        // Return the new fragment
        return fragmentInstance;
    }

    // Default constructor
    public BaseFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Don't destroy fragment across orientation change
        setRetainInstance(true);
    }
}
