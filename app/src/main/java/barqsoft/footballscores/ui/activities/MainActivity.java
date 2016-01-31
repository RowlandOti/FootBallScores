package barqsoft.footballscores.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.rowland.common.ui.activities.BaseToolBarActivity;

import barqsoft.footballscores.R;
import barqsoft.footballscores.ui.fragments.PagerFragment;

public class MainActivity extends BaseToolBarActivity {

    public static int selected_match_id;
    public static int current_fragment = 2;
    private final String save_tag = "Save Test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate the layout
        setContentView(R.layout.activity_main);
        // However, if we're being restored from a previous state, then we don't need to do anything
        // and should return or else we could end up with overlapping fragments.
        if (savedInstanceState != null) {
            return;
        }
        // Create the detail fragment and add it to the activity
        // using a fragment transaction.
        else {
            // Pass bundle to the fragment
            showPagerFragment(null);
        }
        initStetho();
    }

    // Insert the MainFragment
    private void showPagerFragment(Bundle args) {
        // Acquire the Fragment manger
        FragmentManager fm = getSupportFragmentManager();
        // Begin the transaction
        FragmentTransaction ft = fm.beginTransaction();
        // Create new fragment
        PagerFragment mPagerFragment = new PagerFragment().newInstance(args);
        // Prefer replace() over add() see <a>https://github.com/RowlandOti/PopularMovies/issues/1</a>
        ft.replace(R.id.container, mPagerFragment);
        ft.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //
        int id = item.getItemId();

        if (id == R.id.action_about) {
            Intent start_about = new Intent(this, AboutActivity.class);
            startActivity(start_about);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

   /* @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.v(save_tag, "will save");
        Log.v(save_tag, "fragment: " + String.valueOf(mPagerFragment.getCurrentItem()));
        Log.v(save_tag, "selected id: " + selected_match_id);
        outState.putInt("Pager_Current", mPagerFragment.getCurrentItem());
        outState.putInt("Selected_match", selected_match_id);
        //getSupportFragmentManager().putFragment(outState, "mPagerFragment", mPagerFragment);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.v(save_tag, "will retrieve");
        Log.v(save_tag, "fragment: " + String.valueOf(savedInstanceState.getInt("Pager_Current")));
        Log.v(save_tag, "selected id: " + savedInstanceState.getInt("Selected_match"));
        current_fragment = savedInstanceState.getInt("Pager_Current");
        selected_match_id = savedInstanceState.getInt("Selected_match");
        //mPagerFragment = (PagerFragment) getSupportFragmentManager().getFragment(savedInstanceState, "mPagerFragment");
        super.onRestoreInstanceState(savedInstanceState);
    }*/
}
