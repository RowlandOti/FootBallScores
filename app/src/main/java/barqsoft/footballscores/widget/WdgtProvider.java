package barqsoft.footballscores.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.RemoteViews;

import barqsoft.footballscores.R;
import barqsoft.footballscores.services.UpdateScoresService;
import barqsoft.footballscores.ui.activities.MainActivity;

/**
 * Created by Rowland on 1/30/2016.
 */
public class WdgtProvider extends AppWidgetProvider {

    // Logging Identifier for class
    public final String LOG_TAG = WdgtProvider.class.getSimpleName();

    /**
     * Called in response to the {@link AppWidgetManager#ACTION_APPWIDGET_UPDATE} and
     * {@link AppWidgetManager#ACTION_APPWIDGET_RESTORED} broadcasts when this AppWidget
     * provider is being asked to provide {@link android.widget.RemoteViews RemoteViews}
     * for a set of AppWidgets.  Override this method to implement your own AppWidget functionality.
     * <p/>
     * {@more}
     *
     * @param context          The {@link android.content.Context Context} in which this receiver is
     *                         running.
     * @param appWidgetManager A {@link AppWidgetManager} object you can call {@link
     *                         AppWidgetManager#updateAppWidget} on.
     * @param appWidgetIds     The appWidgetIds for which an update is needed.  Note that this
     *                         may be all of the AppWidget instances for this provider, or just
     *                         a subset of them.
     * @see AppWidgetManager#ACTION_APPWIDGET_UPDATE
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // For a collection of widgets we shoule iterate through all of them
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.wdgt_scores);
            // A click onthe widget bar should launch the main app activity
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            views.setOnClickPendingIntent(R.id.wdgt, pendingIntent);
            // Well, set up the remote adapter required to fill in the listview items
            views.setRemoteAdapter(R.id.wdgt_list, new Intent(context, WdgtRemoteViewsService.class));
            // A fallback view for when their is no data to display
            views.setEmptyView(R.id.wdgt_list, R.id.wdgt_empty);
            // Require an update on the widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    /**
     * Implements {@link BroadcastReceiver#onReceive} to dispatch calls to the various
     * other methods on AppWidgetProvider.
     *
     * @param context The Context in which the receiver is running.
     * @param intent  The Intent being received.
     */
    @Override
    public void onReceive(@NonNull Context context, @NonNull Intent intent) {
        super.onReceive(context, intent);
        // Check for
        if (UpdateScoresService.DATA_SOURCE_UPDATED_ACTION.equals(intent.getAction())) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, getClass()));
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.wdgt_list);
        }
    }
}
