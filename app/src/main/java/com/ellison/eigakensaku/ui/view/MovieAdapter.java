package com.ellison.eigakensaku.ui.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.ellison.eigakensaku.R;
import com.ellison.eigakensaku.beans.Movie;
import com.ellison.eigakensaku.beans.MovieList;
import com.ellison.eigakensaku.constants.Constants;
import com.ellison.eigakensaku.glide.ImageRequestListener;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = MovieAdapter.class.getSimpleName();
    private Context mContext;
    private MovieList mMovies;
    private ILoadMoreListener iLoadMoreListener;
    private LoadMoreState mState = LoadMoreState.COMPLETED;
    private int mCurrentPage = 0;

    public interface ILoadMoreListener {
        void onLoadMoreClicked(int index);
    }

    public void setILoadMoreListener(ILoadMoreListener listener) {
        iLoadMoreListener = listener;
    }

    public enum LoadMoreState {
        LOADING, COMPLETED, END
    }

    private void updateLoadingState(LoadMoreState state){
        mState = state;
    }

    public MovieAdapter(Context context) {
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

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;

        if (viewType == Constants.TYPE_RV_CONTENT) {
            itemView = LayoutInflater.from(mContext).inflate(R.layout.content_rv_item_layout, parent, false);
            return new MovieHolder(itemView);
        } else if (viewType == Constants.TYPE_RV_LOAD) {
            itemView = LayoutInflater.from(mContext).inflate(R.layout.load_rv_item_layout, parent, false);
            return new LoadingHodler(itemView);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MovieHolder) {
            Movie movie = mMovies.get(position);
            MovieHolder movieHolder = (MovieHolder) holder;
            Log.d(TAG, "Movie[" + position + "]:" + movie);

            // Show title
            movieHolder.title.setText(movie.getTitle());

            // Show post

            // With customized image loader
            // EllisonImageDisplayer.getDisplayer(mContext).displayImage(holder.post, movie.getPoster());

            // With UIL
//            DisplayImageOptions options = new DisplayImageOptions.Builder()
//                    .showImageOnFail(R.drawable.rv_item_post_place_holder_error)
//                    .showImageOnLoading(R.drawable.rv_item_post_place_holder)
//                    .build();
//            MovieApplication.getImageLoader(mContext).displayImage(movie.getPoster(),
//                    movieHolder.post,
//                    options,
//                    new ImageLoadCallback());

            // With Glide
            RequestOptions options = new RequestOptions()
                    // .transform(new GrayscaleTransformation())
                    // .fitCenter()
                    // .placeholder(R.drawable.rv_item_post_place_holder)
                    .format(DecodeFormat.PREFER_ARGB_8888)
                    .placeholder(R.drawable.rv_item_post_place_holder_img)
                    .override(200, 250)
                    // .override(Target.SIZE_ORIGINAL)
                    // .skipMemoryCache(true)
                    // .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .error(R.drawable.rv_item_post_place_holder_error_emoji);
                    // .error(R.drawable.rv_item_post_place_holder_error);

            Glide.with(mContext)
                    .load(movie.getPoster())
                    .apply(options)
                    // .transition(GenericTransitionOptions.withNoTransition())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .listener(new ImageRequestListener())
                    .into(movieHolder.post);

            // Show year
            movieHolder.year.setText(movie.getYear());

            // Show type
            movieHolder.type.setText(movie.getType());
        } else if (holder instanceof LoadingHodler) {
            LoadingHodler loadingHodler = (LoadingHodler) holder;
            switch (mState) {
                case LOADING:
                    Log.d(TAG, "LOADING VISIBLE & VISIBLE");
                    loadingHodler.itemView.setVisibility(View.VISIBLE);
                    loadingHodler.progressbar.setVisibility(View.VISIBLE);
                    loadingHodler.loadingdes.setText(R.string.text_rv_item_load);
                    break;
                case COMPLETED:
                    Log.d(TAG, "COMPLETED INVISIBLE");
                    loadingHodler.itemView.setVisibility(View.INVISIBLE);
                    break;
                case END:
                    Log.d(TAG, "END VISIBLE & GONE");
                    loadingHodler.itemView.setVisibility(View.VISIBLE);
                    loadingHodler.progressbar.setVisibility(View.GONE);
                    loadingHodler.loadingdes.setText(R.string.text_rv_item_load_end);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            boolean isSwipeUp;

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

                    if (layoutManager instanceof StaggeredGridLayoutManager) {
                        final int count = layoutManager.getItemCount();
                        if (count < ((StaggeredGridLayoutManager) layoutManager).getSpanCount()) {
                            Log.e(TAG, "Current items' count less than span count.");
                            return;
                        }
                        int[] last = new int[count];
                        ((StaggeredGridLayoutManager) layoutManager).findLastCompletelyVisibleItemPositions(last);

                        if (last[0] == count - 1 && isSwipeUp && iLoadMoreListener != null) {
                            // Do load more operation.
                            iLoadMoreListener.onLoadMoreClicked(mCurrentPage + 1);
                            updateLoadingState(LoadMoreState.LOADING);
                            notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                isSwipeUp = dy > 0;
            }
        });
    }

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);

        if (getItemViewType(holder.getLayoutPosition()) == Constants.TYPE_RV_LOAD) {
            ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();

            if (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
                ((StaggeredGridLayoutManager.LayoutParams) layoutParams).setFullSpan(true);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position < mMovies.size()) {
            return Constants.TYPE_RV_CONTENT;
        } else {
            return Constants.TYPE_RV_LOAD;
        }
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return mMovies.size() + 1;
    }
}