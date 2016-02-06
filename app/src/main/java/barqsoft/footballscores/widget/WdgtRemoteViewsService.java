package barqsoft.footballscores.widget;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.RemoteViews;

import barqsoft.footballscores.R;
import barqsoft.footballscores.data.DatabaseContract;
import barqsoft.footballscores.utilities.GeneralUtility;
import barqsoft.footballscores.utilities.TimeUtility;

/**
 * Created by Rowland on 2/2/2016.
 */
public class WdgtRemoteViewsService extends android.widget.RemoteViewsService {

    // Logging Identifier for class
    public final String LOG_TAG = WdgtRemoteViewsService.class.getSimpleName();

    private static final String[] SCORES_COLUMNS = {
            DatabaseContract.scores_table.HOME_COL,
            DatabaseContract.scores_table.AWAY_COL,
            DatabaseContract.scores_table.HOME_GOALS_COL,
            DatabaseContract.scores_table.AWAY_GOALS_COL,
            DatabaseContract.scores_table.LEAGUE_COL,
            DatabaseContract.scores_table.MATCH_DAY,
            DatabaseContract.scores_table.TIME_COL,
            DatabaseContract.scores_table.MATCH_ID
    };

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {
            private Cursor data = null;

            @Override
            public void onCreate() {
            }

            @Override
            public void onDataSetChanged() {
                if (data != null) {
                    data.close();
                }
                // Clear calling identity
                final long idToken = Binder.clearCallingIdentity();
                Uri uri = DatabaseContract.scores_table.buildScoreWithDate();
                String formatString = getString(R.string.date_format_ymd);
                String todayDate = TimeUtility.getToday(formatString);
                // Lets acquire the scores data
                data = getContentResolver().query(uri, SCORES_COLUMNS, null, new String[]{todayDate}, null);
                // Restore calling identity for calls to use our process and permission
                Binder.restoreCallingIdentity(idToken);
            }

            @Override
            public void onDestroy() {
                if (data != null) {
                    data.close();
                    data = null;
                }
            }

            @Override
            public int getCount() {
                Log.v(LOG_TAG, "Widget Data Size: " +data.getCount());
                return data == null ? 0 : data.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                // Data must not be null to continue
                if (position == AdapterView.INVALID_POSITION || data == null || !data.moveToPosition(position)) {
                    return null;
                }

                String homeTeamName = data.getString(data.getColumnIndex(DatabaseContract.scores_table.HOME_COL));
                String awayTeamName = data.getString(data.getColumnIndex(DatabaseContract.scores_table.AWAY_COL));
                String matchTime = data.getString(data.getColumnIndex(DatabaseContract.scores_table.TIME_COL));
                int homeGoals = data.getInt(data.getColumnIndex(DatabaseContract.scores_table.HOME_GOALS_COL));
                int awayGoals = data.getInt(data.getColumnIndex(DatabaseContract.scores_table.AWAY_GOALS_COL));
                String score = GeneralUtility.getScoresString(WdgtRemoteViewsService.this, homeGoals, awayGoals);

                RemoteViews views = new RemoteViews(getPackageName(), R.layout.wdgt_scores_list_item);

                views.setTextViewText(R.id.wdgt_home_name, homeTeamName);
                views.setTextViewText(R.id.wdgt_away_name, awayTeamName);
                views.setTextViewText(R.id.wdgt_match_time, matchTime);
                views.setTextViewText(R.id.wdgt_score, score);
                views.setImageViewResource(R.id.wdgt_home_crest, GeneralUtility.getTeamCrestByTeamName(WdgtRemoteViewsService.this, homeTeamName));
                views.setImageViewResource(R.id.wdgt_away_crest, GeneralUtility.getTeamCrestByTeamName(WdgtRemoteViewsService.this, awayTeamName));

                views.setContentDescription(R.id.wdgt_home_name, homeTeamName);
                views.setContentDescription(R.id.wdgt_away_name, awayTeamName);
                views.setContentDescription(R.id.wdgt_match_time, matchTime);
                views.setContentDescription(R.id.wdgt_score, score);
                views.setContentDescription(R.id.wdgt_home_crest, homeTeamName);
                views.setContentDescription(R.id.wdgt_away_crest, awayTeamName);

                Log.v(LOG_TAG, "Widget Data Size: " +homeTeamName);
                Log.v(LOG_TAG, "Widget Data Size: " +awayTeamName);
                Log.v(LOG_TAG, "Widget Data Size: " +matchTime);
                Log.v(LOG_TAG, "Widget Data Size: " +score);

                return views;
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.wdgt_scores_list_item);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                if (data.moveToPosition(position)) {
                    return data.getLong(data.getColumnIndex(DatabaseContract.scores_table.MATCH_ID));
                }
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }
}
