package barqsoft.footballscores.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

import com.rowland.common.ui.activities.BaseToolBarActivity;

import barqsoft.footballscores.R;
import barqsoft.footballscores.ui.fragments.PagerFragment;

public class MainActivity extends BaseToolBarActivity {

    // The class Log identifier
    private final String LOG_TAG = MainActivity.class.getSimpleName();

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

    // Insert the ScoresFragment
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
}
