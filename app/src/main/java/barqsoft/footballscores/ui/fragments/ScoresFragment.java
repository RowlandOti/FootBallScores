package barqsoft.footballscores.ui.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.rowland.common.ui.fragments.BaseFragment;

import barqsoft.footballscores.R;
import barqsoft.footballscores.data.DatabaseContract;
import barqsoft.footballscores.services.UpdateScoresService;
import barqsoft.footballscores.ui.adapters.RecyclerViewCursorAdapter;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ScoresFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor>, SwipeRefreshLayout.OnRefreshListener {

    // The class Log identifier
    private final String LOG_TAG = ScoresFragment.class.getSimpleName();

    private static final String FRAGMENT_DATE = "fragmentDate";
    public static final int SCORES_LOADER = 0;
    public RecyclerViewCursorAdapter mScoresRecyclerViewAdapter;
    // ButterKnife injected Views
    @Bind(R.id.sw_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.scores_recycle_view_list)
    RecyclerView mScoresRecyclerView;
    @Bind(R.id.empty_list_view)
    LinearLayout mEmptyListView;
    // Each fragments date
    private String[] fragmentdate = new String[1];
    // Refresh state
    private boolean mIsRefreshing = false;
    // Refresh broadcastreceiver
    private BroadcastReceiver mRefreshingReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (UpdateScoresService.REFRESH_STATE_CHANGED_ACTION.equals(intent.getAction())) {
                mIsRefreshing = intent.getBooleanExtra(UpdateScoresService.EXTRA_REFRESHING_ACTION, false);
                updateRefreshingUI();
            }
        }
    };

    public static ScoresFragment newInstance(Bundle args) {
        ScoresFragment fragment = new ScoresFragment();
        if (args != null) {
            fragment.setArguments(args);
        }
        return fragment;
    }

    public ScoresFragment() {

    }

    public void setFragmentDate(String date) {
        fragmentdate[0] = date;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Called to instantiate the fragment's view hierarchy
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_scores, container, false);
        // Inflate all views
        ButterKnife.bind(this, rootView);
        // Return the view for this fragment
        return rootView;
    }

    // Called after onCreateView() is done i.e the fragment's view has been created
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Configure the refresh layout look
        mSwipeRefreshLayout.setColorSchemeResources(R.color.apptheme_accent_teal);
        // Create new instance of layout manager
        final LinearLayoutManager mStaggeredLayoutManger = new LinearLayoutManager(getContext());
        // Set the layout manger
        mScoresRecyclerView.setLayoutManager(mStaggeredLayoutManger);
        mScoresRecyclerView.setHasFixedSize(false);
        // Call is actually only necessary with custom ItemAnimators
        mScoresRecyclerView.setItemAnimator(new DefaultItemAnimator());
        // Initialize new adapter
        mScoresRecyclerViewAdapter = new RecyclerViewCursorAdapter(getActivity(), null);
        // Set RecycleView's adapter
        mScoresRecyclerView.setAdapter(mScoresRecyclerViewAdapter);

  /*      mScoresRecyclerViewAdapter.detail_match_id = MainActivity.selected_match_id;
        mScoresRecyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ScoresAdapter.ViewHolder selected = (ScoresAdapter.ViewHolder) view.getTag();
                mScoresRecyclerViewAdapter.detail_match_id = selected.match_id;
                MainActivity.selected_match_id = (int) selected.match_id;
                mScoresRecyclerViewAdapter.notifyDataSetChanged();
            }
        });*/
    }

    // Called when the containing activity onCreate() is done, and after onCreateView() of fragment
    // Do final modification on the hierarchy e.g modify view elements and restore previous state
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Restore any previous states
        if (savedInstanceState != null) {
            // Restore query string
            fragmentdate = savedInstanceState.getStringArray(ScoresFragment.FRAGMENT_DATE);
        } else {
            onRefresh();
        }
        // Update the listview
        getLoaderManager().initLoader(SCORES_LOADER, null, this);
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().registerReceiver(mRefreshingReceiver, new IntentFilter(UpdateScoresService.REFRESH_STATE_CHANGED_ACTION));
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(mRefreshingReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(SCORES_LOADER, null, this);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    //Here you Save your data
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArray(ScoresFragment.FRAGMENT_DATE, fragmentdate);
    }

    @Override
    public void onRefresh() {
        getActivity().startService(new Intent(getActivity(), UpdateScoresService.class));
    }

    @OnClick(R.id.empty_list_view_button)
    public void onRefresh(View view) {
        onRefresh();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Create new loader
        CursorLoader cursorLoader = new CursorLoader(getActivity(), DatabaseContract.scores_table.buildScoreWithDate(), null, null, fragmentdate, null);
        // Return new loader
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        int i = 0;
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            i++;
            cursor.moveToNext();
        }
        mScoresRecyclerViewAdapter.swapCursor(cursor);
        // Failure to notify adapter will result in no data displayed
        mScoresRecyclerViewAdapter.notifyDataSetChanged();
        // Update the Empty View
        updateEmptyView();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mScoresRecyclerViewAdapter.swapCursor(null);
    }

    // Update Refresh UI state
    private void updateRefreshingUI() {
        mSwipeRefreshLayout.setRefreshing(mIsRefreshing);
    }

    // Update the empty view
    public void updateEmptyView() {
        if (mScoresRecyclerViewAdapter.getItemCount() == 0) {
            // Show Empty TextView
            mSwipeRefreshLayout.setVisibility(View.GONE);
            mEmptyListView.setVisibility(View.VISIBLE);
        } else {
            // Show RecycleView filled with scores
            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
            mEmptyListView.setVisibility(View.GONE);
        }
    }
}
