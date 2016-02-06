package barqsoft.footballscores.ui.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;

import barqsoft.footballscores.R;

/**
 * Created by Oti Rowland on 12/21/2015.
 * <p/>
 * Heavily adapted from <a>http://stackoverflow.com/a/27732748/1464571</a> with hints from
 * <a>https://github.com/androidessence/RecyclerViewCursorAdapter</a>
 */
public class RecyclerViewCursorAdapter extends RecyclerView.Adapter<RecyclerViewCursorAdapter.ViewHolder> {

    // The class Log identifier
    private final String LOG_TAG = RecyclerViewCursorAdapter.class.getSimpleName();

    // PATCH: Because RecyclerView.Adapter in its current form doesn't natively support
    // cursors, we "wrap" a CursorAdapter that will do all the job for us
    ScoresAdapter mScoreCursorAdapter;
    // A quick acess to the context
    Context mContext;
    // A Calendar object to help in formatting time
    private Calendar mCalendar;


    public RecyclerViewCursorAdapter(Context context, Cursor c) {
        mContext = context;
        mScoreCursorAdapter = new ScoresAdapter(mContext, c, 0);
    }


    // Called when RecyclerView needs a new ViewHolder of the given type to represent an item.
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Passing the inflater job to the CursorAdapter object
        View view = mScoreCursorAdapter.newView(mContext, mScoreCursorAdapter.getCursor(), parent);
        ViewHolder holder = new ViewHolder(view);
        //Set the tag ,for access casting later
        view.setTag(holder);
        // Return new ViewHolder
        return holder;
    }

    // Called by RecyclerView to display the data at the specified position.
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Passing the binding operation to cursor loader
        mScoreCursorAdapter.getCursor().moveToPosition(position);
        mScoreCursorAdapter.bindView(holder.itemView, mContext, mScoreCursorAdapter.getCursor());
    }

    @Override
    public int getItemCount() {
        return mScoreCursorAdapter.getCount();
    }

    public void swapCursor(Cursor cursor) {
        mScoreCursorAdapter.swapCursor(cursor);
    }

    // Takes care of the overhead of recycling and gives better performance and scrolling
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public double match_id;

        public TextView mHomeName;
        public TextView mAwayName;
        public TextView mGameDate;
        public TextView mGameScore;
        public ImageView mHomeCrest;
        public ImageView mAwayCrest;

        public ViewHolder(View itemView) {
            super(itemView);

            mHomeName = (TextView) itemView.findViewById(R.id.home_name);
            mAwayName = (TextView) itemView.findViewById(R.id.away_name);
            mGameScore = (TextView) itemView.findViewById(R.id.score_textview);
            mGameDate = (TextView) itemView.findViewById(R.id.date_textview);
            mHomeCrest = (ImageView) itemView.findViewById(R.id.home_crest);
            mAwayCrest = (ImageView) itemView.findViewById(R.id.away_crest);
        }
    }

}
