package barqsoft.footballscores.ui.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Oti Rowland on 12/21/2015.
 *
 * Heavily adapted from <a>http://stackoverflow.com/a/27732748/1464571</a> with hints from
 * <a>https://github.com/androidessence/RecyclerViewCursorAdapter</a>
 * ToDo: Upgrade to Realm using <a>http://stackoverflow.com/a/33568015/1464571</a>
 */
public class RecyclerViewCursorAdapter extends RecyclerView.Adapter<RecyclerViewCursorAdapter.CustomViewHolder> {

    // PATCH: Because RecyclerView.Adapter in its current form doesn't natively support
    // cursors, we "wrap" a CursorAdapter that will do all the job for us
    CursorAdapter mCursorAdapter;
    // A quick acess to the context
    Context mContext;
    // A Calendar object to help in formatting time
    private Calendar mCalendar;


    public RecyclerViewCursorAdapter(Context context, Cursor c) {

        mContext = context;

        mCursorAdapter = new CursorAdapter(mContext, c, 0) {

            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                // Inflate the view here with layout to inflate for CustomViewHolder
                View view = LayoutInflater.from(context).inflate(R.layout.grid_item_column, parent, false);
                // Return this view
                return view;
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                // Get hold of the CustomViewHolder
                CustomViewHolder holder = (CustomViewHolder) view.getTag();
                // Binding operations
                holder.mGridItemContainer.setContentDescription(holder.mGridItemContainer.getContext().getString(R.string.movie_title, movie.getOriginalTitle()));
                // ToDo: Replace this with code for cursor
                if (cursor!= null) {
                    mCalendar.setTime(cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_MOVIE_RELEASEDATE)));
                    holder.mReleaseDateTextView.setText(String.valueOf(mCalendar.get(Calendar.YEAR)));
                    holder.mReleaseDateTextView.setContentDescription(holder.mReleaseDateTextView.getContext().getString(R.string.movie_year, String.valueOf(mCalendar.get(Calendar.YEAR))));
                }


                String imageUrl = EBaseURlTypes.MOVIE_API_IMAGE_BASE_URL.getUrlType() + EBaseImageSize.IMAGE_SIZE_W185.getImageSize() + movie.getPosterPath();
                final RelativeLayout container = holder.mMovieTitleContainer;
                // Use Picasso to load the images
                Picasso.with(holder.mMovieImageView.getContext()).load(imageUrl).placeholder(R.drawable.ic_movie_placeholder).
                        into(holder.mMovieImageView);

            }
        };
    }


    // Called when RecyclerView needs a new CustomViewHolder of the given type to represent an item.
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Passing the inflater job to the CursorAdapter object
        View view = mCursorAdapter.newView(mContext, mCursorAdapter.getCursor(), parent);
        CustomViewHolder holder = new CustomViewHolder(view);
        //Set the tag ,for access casting later
        view.setTag(holder);
        // Return new CustomViewHolder
        return holder;
    }

    // Called by RecyclerView to display the data at the specified position.
    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        // Passing the binding operation to cursor loader
        mCursorAdapter.getCursor().moveToPosition(position);
        mCursorAdapter.bindView(holder.itemView, mContext, mCursorAdapter.getCursor());
    }

    @Override
    public int getItemCount() {
        return mCursorAdapter.getCount();
    }

    // Takes care of the overhead of recycling and gives better performance and scrolling
    public class CustomViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.grid_sort_type_text_view)
        TextView mSortTypeValueTextView;

        @Bind(R.id.grid_release_date_text_view)
        TextView mReleaseDateTextView;

        @Bind(R.id.grid_poster_image_view)
        ImageView mMovieImageView;

        @Bind(R.id.grid_sort_type_image_view)
        ImageView mSortTypeIconImageView;

        @Bind(R.id.grid_title_container)
        RelativeLayout mMovieTitleContainer;

        @Bind(R.id.grid_container)
        FrameLayout mGridItemContainer;

        public CustomViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
