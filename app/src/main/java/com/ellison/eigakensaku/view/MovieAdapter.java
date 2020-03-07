package com.ellison.eigakensaku.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ellison.eigakensaku.R;
import com.ellison.eigakensaku.application.MovieApplication;
import com.ellison.eigakensaku.beans.Movie;
import com.ellison.eigakensaku.beans.MovieList;
import com.ellison.eigakensaku.debug.ImageLoadCallback;
import com.ellison.eigakensaku.utils.Utils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MovieAdapter extends RecyclerView.Adapter<MovieHolder> {
    private Context mContext;
    private MovieList mMovies;

    public MovieAdapter(Context context) {
        super();

        mContext =context;
        mMovies = new MovieList();
    }

    public void updateMovies(MovieList list) {
        if (list == null){
            mMovies.clear();
        } else  if (mMovies != null) {
            mMovies.clear();
            mMovies.addAll(list);
        } else {
            mMovies = list;
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.content_rv_item_layout, parent, false);
        return new MovieHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieHolder holder, int position) {
        Movie movie = mMovies.get(position);

        holder.title.setText(movie.getTitle());
        MovieApplication.getImageLoader(mContext).displayImage(movie.getPoster(),
                holder.post,
                Utils.getImageOptions(R.drawable.rv_item_post_loading),
                new ImageLoadCallback());
//        holder.post.setImageResource(R.drawable.rv_tem_post_for_test);
        holder.year.setText(movie.getYear());
        holder.type.setText(movie.getType());
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
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
    public void onViewRecycled(@NonNull MovieHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull MovieHolder holder) {
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull MovieHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
    }
}
