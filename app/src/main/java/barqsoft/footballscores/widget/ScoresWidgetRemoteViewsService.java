package barqsoft.footballscores.widget;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.SimpleDateFormat;
import java.util.Date;

import barqsoft.footballscores.R;
import barqsoft.footballscores.Utilies;
import barqsoft.footballscores.data.DatabaseContract;

/**
 * Created by alex on 7/11/15.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ScoresWidgetRemoteViewsService extends RemoteViewsService {
    public final String LOG_TAG = ScoresWidgetRemoteViewsService.class.getSimpleName();
    private static final String[] SCORE_COLUMNS = {
            DatabaseContract.scores_table.HOME_COL,
            DatabaseContract.scores_table.AWAY_COL,
            DatabaseContract.scores_table.HOME_GOALS_COL,
            DatabaseContract.scores_table.AWAY_GOALS_COL,
            DatabaseContract.scores_table.LEAGUE_COL,
            DatabaseContract.scores_table.MATCH_DAY,
            DatabaseContract.scores_table.TIME_COL,
            DatabaseContract.scores_table.MATCH_ID
    };

    static final int COL_HOME = 0;
    static final int COL_AWAY = 1;
    static final int COL_HOME_GOALS = 2;
    static final int COL_AWAY_GOALS = 3;
    static final int COL_LEAGUE = 4;
    static final int COL_MATCHDAY = 5;
    static final int COL_MATCHTIME = 6;
    static final int COL_MATCHID = 7;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {
            private Cursor data = null;

            @Override
            public void onCreate() {
                // Nothing to do
            }

            @Override
            public void onDataSetChanged() {
                if (data != null) {
                    data.close();
                }
                // This method is called by the app hosting the widget (e.g., the launcher)
                // However, our ContentProvider is not exported so it doesn't have access to the
                // data. Therefore we need to clear (and finally restore) the calling identity so
                // that calls use our process and permission
                final long identityToken = Binder.clearCallingIdentity();
                Uri uri = DatabaseContract.scores_table.buildScoreWithDate();
                String formatString = getString(R.string.date_format_ymd);
                SimpleDateFormat format = new SimpleDateFormat(formatString);
                String todayDate = format.format(new Date());

                data = getContentResolver().query(uri,
                        SCORE_COLUMNS,
                        null,
                        new String[] { todayDate },
                        null);
                Binder.restoreCallingIdentity(identityToken);
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
                return data == null ? 0 : data.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                if (position == AdapterView.INVALID_POSITION ||
                        data == null || !data.moveToPosition(position)) {
                    return null;
                }
                RemoteViews views = new RemoteViews(getPackageName(),
                        R.layout.widget_scores_list_item);

                String homeTeamName = data.getString(COL_HOME);
                String awayTeamName = data.getString(COL_AWAY);
                String matchTime = data.getString(COL_MATCHTIME);
                String score = Utilies.getScoresString(ScoresWidgetRemoteViewsService.this,
                        data.getInt(COL_HOME_GOALS), data.getInt(COL_AWAY_GOALS));

                views.setTextViewText(R.id.widget_home_name, homeTeamName);
                views.setTextViewText(R.id.widget_away_name, awayTeamName);
                views.setTextViewText(R.id.widget_match_time, matchTime);
                views.setTextViewText(R.id.widget_score, score);
                views.setImageViewResource(R.id.widget_home_crest, Utilies.getTeamCrestByTeamName(
                        ScoresWidgetRemoteViewsService.this, homeTeamName));
                views.setImageViewResource(R.id.widget_away_crest, Utilies.getTeamCrestByTeamName(
                        ScoresWidgetRemoteViewsService.this, awayTeamName));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                    setRemoteContentDescription(views, R.id.widget_home_crest, homeTeamName);
                    setRemoteContentDescription(views, R.id.widget_away_crest, awayTeamName);
                    setRemoteContentDescription(views, R.id.widget_home_name, homeTeamName);
                    setRemoteContentDescription(views, R.id.widget_away_name, awayTeamName);
                    setRemoteContentDescription(views, R.id.widget_match_time, matchTime);
                    setRemoteContentDescription(views, R.id.widget_score, score);
                }

                return views;
            }

            @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
            private void setRemoteContentDescription(RemoteViews views, int id, String description) {
                views.setContentDescription(id, description);
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.widget_scores_list_item);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                if (data.moveToPosition(position))
                    return data.getLong(COL_MATCHID);
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }
}
