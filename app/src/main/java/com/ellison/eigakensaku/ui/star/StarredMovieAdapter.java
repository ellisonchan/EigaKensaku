package com.ellison.eigakensaku.ui.star;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.ellison.eigakensaku.R;
import com.ellison.eigakensaku.beans.Movie;
import com.ellison.eigakensaku.beans.MovieList;
import com.ellison.eigakensaku.glide.ImageRequestListener;
import com.ellison.eigakensaku.ui.touch.ISwipeDataSync;
import com.ellison.eigakensaku.ui.touch.ITouchDataSync;
import com.ellison.eigakensaku.utils.Utils;

public class StarredMovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ITouchDataSync {
    private static final String TAG = StarredMovieAdapter.class.getSimpleName();
    private Context mContext;
    private MovieList mMovies;
    private IStarredMoiveOperationCallback iStarredMoiveOperationCallback;
    private LoadMoreState mState = LoadMoreState.COMPLETED;
    private int mCurrentPage = 0;

    public interface IStarredMoiveOperationCallback {
        void onItemRemoved(Movie movie);
        void onItemSwap(Movie before, Movie after);
    }

    public void setIStarredMoiveOperationCallback(IStarredMoiveOperationCallback listener) {
        iStarredMoiveOperationCallback = listener;
    }

    public enum LoadMoreState {
        LOADING, COMPLETED, END
    }

    private void updateLoadingState(LoadMoreState state){
        mState = state;
    }

    public StarredMovieAdapter(Context context) {
        super();

        mContext = context;
        mMovies = new MovieList();
    }

    public void updateMovies(MovieList list) {
        updateMovies(list, false);
    }

    public void updateMovies(MovieList list, boolean isLoadMore) {
        if (list == null) {
            if (!isLoadMore) {
                mMovies.clear();
                mCurrentPage = 0;
            }
            updateLoadingState(LoadMoreState.COMPLETED);
        } else if (mMovies != null) {
            if (!isLoadMore) {
                mMovies.clear();
                mCurrentPage = 1;
                updateLoadingState(LoadMoreState.COMPLETED);
            } else {
                if (list.isEmpty()) {
                    updateLoadingState(LoadMoreState.END);
                } else {
                    mCurrentPage++;
                    updateLoadingState(LoadMoreState.COMPLETED);
                }
            }
            mMovies.addAll(list);
        } else {
            mMovies = list;
            mCurrentPage = 1;
            updateLoadingState(LoadMoreState.COMPLETED);
        }

        notifyDataSetChanged();
    }

    public MovieList getMovies() {
        return mMovies;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.starred_rv_item_layout, parent, false);
        return new StarredMovieHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Utils.logDebug(TAG, "onBindViewHolder() holder:" + holder + " pos:" + position);

        if (holder instanceof StarredMovieHolder) {
            Movie movie = mMovies.get(position);
            StarredMovieHolder movieHolder = (StarredMovieHolder) holder;
            Log.d(TAG, "Movie[" + position + "]:" + movie);

            // Show post With Glide
            RequestOptions options = new RequestOptions()
                    .format(DecodeFormat.PREFER_ARGB_8888)
                    .placeholder(R.drawable.rv_item_post_place_holder_img)
                    .override(100, 150)
                    .error(R.drawable.rv_item_post_place_holder_error_emoji);

            Glide.with(mContext)
                    .load(movie.getPoster())
                    .apply(options)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .listener(new ImageRequestListener())
                    .into(movieHolder.post);

            // Show title
            movieHolder.title.setText(movie.getTitle());

            // Show year
            movieHolder.year.setText(movie.getYear());

            // Show type
            movieHolder.type.setText(movie.getType());

            // show actor

            // show country

            // show info
        }
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    @Override
    public void onItemRemoved(int pos) {
        Utils.logDebug(TAG, "onItemRemoved() pos:" + pos);
        if (iStarredMoiveOperationCallback != null) {
            iStarredMoiveOperationCallback.onItemRemoved(mMovies.get(pos));
        }
    }

    @Override
    public void onItemSwap(int before, int after) {
        Utils.logDebug(TAG, "onItemSwap() before:" + before + " after:" + after);
        if (iStarredMoiveOperationCallback != null) {
            iStarredMoiveOperationCallback.onItemSwap(mMovies.get(before), mMovies.get(after));
        }
    }
}