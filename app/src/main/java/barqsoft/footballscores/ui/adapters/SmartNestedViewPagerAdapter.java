/*
 * Copyright 2015 Oti Rowland
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package barqsoft.footballscores.ui.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.rowland.common.ui.adapters.SmartFragmentStatePagerAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;

import barqsoft.footballscores.R;
import barqsoft.footballscores.ui.fragments.PagerFragment;
import barqsoft.footballscores.ui.fragments.ScoresFragment;
import barqsoft.footballscores.utilities.TimeUtility;


/**
 * Created by Rowland on 6/11/2015.
 */
public class SmartNestedViewPagerAdapter extends SmartFragmentStatePagerAdapter {

    private PagerFragment ht = new PagerFragment();
    private Context mContext;

    public SmartNestedViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.mContext = context;
    }

    @Override
    public Fragment getItem(int index) {
        // Construct the date
        Date fragmentdate = new Date(System.currentTimeMillis() + ((index - 2) * 86400000));
        String formatString = mContext.getString(R.string.date_format_ymd);
        SimpleDateFormat mformat = new SimpleDateFormat(formatString);
        // Scores fragment
        ScoresFragment scoresFragment = new ScoresFragment();
        scoresFragment.setFragmentDate(mformat.format(fragmentdate));

        return scoresFragment;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return ht.NUM_PAGES;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Returns the page title for the tabstrip
        return TimeUtility.getDayName(mContext, System.currentTimeMillis() + ((position - 2) * 86400000));
    }
}
