package com.ellison.eigakensaku.ui.star;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;

import com.ellison.eigakensaku.R;
import com.ellison.eigakensaku.beans.MovieList;
import com.ellison.eigakensaku.presenter.IMoviePresenter;
import com.ellison.eigakensaku.presenter.MoviePresenter;
import com.ellison.eigakensaku.ui.base.BaseFragment;
import com.ellison.eigakensaku.ui.touch.ItemSwipeDragCallback;
import com.ellison.eigakensaku.utils.Utils;

import java.util.concurrent.Executors;

public class StarFragment extends BaseFragment {
    private static final String TAG = StarFragment.class.getSimpleName();

    private IMoviePresenter mMoviePresenter;
    private StarredMovieAdapter mMovieAdapter;
    private Handler mHandler = new Handler();

    @BindView(R.id.star_frag_rv)
    RecyclerView mRecyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_star, container, false);
        return root;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mMovieAdapter = new StarredMovieAdapter(getActivity());
        mMoviePresenter = new MoviePresenter();
        ItemTouchHelper.Callback callback = new ItemSwipeDragCallback();
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);

        if (mRecyclerView != null) {
            mRecyclerView.setLayoutManager(linearLayoutManager);
            mRecyclerView.setAdapter(mMovieAdapter);
            touchHelper.attachToRecyclerView(mRecyclerView);
        }

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                MovieList list = new MovieList(mMoviePresenter.getStarredMovie(getActivity().getApplicationContext()));
                Utils.logDebug(TAG, "init list:" + list);

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Utils.logDebug(TAG, "UI show list");
                        mMovieAdapter.updateMovies(list);
                    }
                });
            }
        });
    }
}