package barqsoft.footballscores.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import barqsoft.footballscores.R;
import barqsoft.footballscores.utilities.GeneralUtility;

/**
 * Created by yehya khaled on 2/26/2015.
 */
public class ScoresAdapter extends CursorAdapter {

    // Logging Identifier for class
    public static final String LOG_TAG = ScoresAdapter.class.getSimpleName();

    public static final int COL_HOME = 3;
    public static final int COL_AWAY = 4;
    public static final int COL_HOME_GOALS = 6;
    public static final int COL_AWAY_GOALS = 7;
    public static final int COL_DATE = 1;
    public static final int COL_LEAGUE = 5;
    public static final int COL_MATCHDAY = 9;
    public static final int COL_ID = 8;
    public static final int COL_MATCHTIME = 2;
    public double detail_match_id = 0;
    private String FOOTBALL_SCORES_HASHTAG = "#Football_Scores";

    public ScoresAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate the view here with layout to inflate for ViewHolder
        View view = LayoutInflater.from(context).inflate(R.layout.scores_list_item, parent, false);
        // Return this view
        return view;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        final RecyclerViewCursorAdapter.ViewHolder mHolder = (RecyclerViewCursorAdapter.ViewHolder) view.getTag();

        if (cursor != null) {
            String homeTeamName = cursor.getString(COL_HOME);
            String awayTeamName = cursor.getString(COL_AWAY);
            String matchTime = cursor.getString(COL_MATCHTIME);
            String score = GeneralUtility.getScoresString(context, cursor.getInt(COL_HOME_GOALS), cursor.getInt(COL_AWAY_GOALS));

            mHolder.mHomeName.setText(homeTeamName);
            mHolder.mHomeName.setContentDescription(context.getString(R.string.a11y_home_team, homeTeamName));

            mHolder.mAwayName.setText(awayTeamName);
            mHolder.mAwayName.setContentDescription(context.getString(R.string.a11y_away_team, awayTeamName));

            mHolder.mGameDate.setText(matchTime);
            mHolder.mGameDate.setContentDescription(context.getString(R.string.a11y_match_time, matchTime));

            mHolder.mGameScore.setText(score);
            mHolder.mGameScore.setContentDescription(context.getString(R.string.a11y_score, score));

            mHolder.match_id = cursor.getDouble(COL_ID);

            mHolder.mHomeCrest.setImageResource(GeneralUtility.getTeamCrestByTeamName(context, homeTeamName));
            mHolder.mAwayCrest.setImageResource(GeneralUtility.getTeamCrestByTeamName(context, awayTeamName));

            /*Log.v(LOG_TAG,mHolder.mHomeName.getText() + " Vs. " + mHolder.mAwayName.getText() +" id " + String.valueOf(mHolder.match_id));
            Log.v(LOG_TAG,String.valueOf(detail_match_id));*/

            LayoutInflater vi = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = vi.inflate(R.layout.detail_fragment, null);
            ViewGroup container = (ViewGroup) view.findViewById(R.id.details_fragment_container);

            if (ifShowDetails(mHolder)) {
                container.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

                String matchDayDescription = GeneralUtility.getMatchDayString(context, cursor.getInt(COL_MATCHDAY), cursor.getInt(COL_LEAGUE));
                String leagueDescription = context.getString(GeneralUtility.getLeagueStringResource(cursor.getInt(COL_LEAGUE)));

                TextView match_day = (TextView) v.findViewById(R.id.matchday_textview);
                match_day.setText(matchDayDescription);
                match_day.setContentDescription(matchDayDescription);

                TextView league = (TextView) v.findViewById(R.id.league_textview);
                league.setText(leagueDescription);
                league.setContentDescription(context.getString(R.string.a11y_league, leagueDescription));

                Button share_button = (Button) v.findViewById(R.id.share_button);
                share_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //add Share Action
                        context.startActivity(createShareForecastIntent(mHolder.mHomeName.getText() + " " + mHolder.mGameScore.getText() + " " + mHolder.mAwayName.getText() + " "));
                    }
                });
            } else {
                container.removeAllViews();
            }
        }
    }

    private boolean ifShowDetails(RecyclerViewCursorAdapter.ViewHolder mHolder) {
        return mHolder.match_id == detail_match_id;
    }

    public Intent createShareForecastIntent(String ShareText) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, ShareText + FOOTBALL_SCORES_HASHTAG);
        return shareIntent;
    }
}
