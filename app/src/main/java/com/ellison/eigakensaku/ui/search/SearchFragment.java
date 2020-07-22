package com.ellison.eigakensaku.ui.search;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;

import com.ellison.eigakensaku.R;
import com.ellison.eigakensaku.beans.Movie;
import com.ellison.eigakensaku.beans.MovieList;
import com.ellison.eigakensaku.constants.Constants;
import com.ellison.eigakensaku.presenter.IMoviePresenter;
import com.ellison.eigakensaku.presenter.MoviePresenter;
import com.ellison.eigakensaku.ui.animator.AnimatorShowerImplement;
import com.ellison.eigakensaku.ui.animator.AnimatorType;
import com.ellison.eigakensaku.ui.animator.IAnimatorCallback;
import com.ellison.eigakensaku.ui.animator.IAnimatorShower;
import com.ellison.eigakensaku.ui.base.BaseFragment;
import com.ellison.eigakensaku.ui.touch.ItemSwipeCallback;
import com.ellison.eigakensaku.ui.view.IMovieView;
import com.ellison.eigakensaku.ui.view.MovieAdapter;
import com.ellison.eigakensaku.ui.view.MovieItemDecoration;
import com.ellison.eigakensaku.ui.view.MovieSearchBox;
import com.ellison.eigakensaku.utils.Utils;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;

public class SearchFragment extends BaseFragment
        implements View.OnClickListener,
        IMovieView,
        EditText.OnEditorActionListener,
        TextWatcher,
        SwipeRefreshLayout.OnRefreshListener,
        IAnimatorCallback,
        MovieAdapter.ILoadMoreListener {
    private static final String TAG = SearchFragment.class.getSimpleName();

    private String mKeywords;
    private IMoviePresenter mMoviePresenter;
    private MovieAdapter mMovieAdapter;
    private IAnimatorShower mAnimator;
    private boolean mResumed = false;

    @BindView(R.id.search_box)
    MovieSearchBox mSearchBox;

    @BindView(R.id.fab)
    FloatingActionButton mFABtn;

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;

    @BindView(R.id.rv_layout)
    RecyclerView mRecyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Utils.logDebug(TAG, this + " onCreateView()" + " container:" + container + " savedInstanceState:" + savedInstanceState);
        View root = inflater.inflate(R.layout.fragment_search, container, false);
        return root;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        if (mSearchBox != null) {
            mSearchBox.setOnEditorActionListener(this);
            mSearchBox.addTextChangedListener(this);
        }

        if (mRefreshLayout != null)
            mRefreshLayout.setOnRefreshListener(this);

        mMoviePresenter = new MoviePresenter(this);

        initRecyclerView(savedInstanceState);

        mFABtn.getDrawable().setAlpha(Constants.ALPHA_FAB_DISABLE);
        mFABtn.setClickable(false);

        mAnimator = AnimatorShowerImplement.getInstance();
    }

    private void initRecyclerView(Bundle savedInstanceState) {
        Utils.logDebug(TAG, this + " initRecyclerView()");
        StaggeredGridLayoutManager sgLM = new StaggeredGridLayoutManager(Constants.MOVIE_LIST_ROW_NUMBER, StaggeredGridLayoutManager.VERTICAL);
        mMovieAdapter = new MovieAdapter(getActivity());
        mMovieAdapter.setILoadMoreListener(this);
        MovieItemDecoration decoration = new MovieItemDecoration(Constants.MOVIE_LIST_ITEM_SPACE);
        ItemSwipeCallback touchCallback = new ItemSwipeCallback();
        ItemTouchHelper touchHelper = new ItemTouchHelper(touchCallback);
        touchCallback.syncTouchHelper(touchHelper);

        if (mRecyclerView != null) {
            mRecyclerView.setLayoutManager(sgLM);
            mRecyclerView.setAdapter(mMovieAdapter);
            touchHelper.attachToRecyclerView(mRecyclerView);
            // mRecyclerView.requestDisallowInterceptTouchEvent(true);
            // mRecyclerView.addItemDecoration(decoration);

            MovieList list = Utils.readMoviesFromFile(getActivity());
            Utils.logDebug(TAG, this + " initRecyclerView() list:" + list);
            // Restore state if view is destroyed but fragment's instance not.
            if (list != null) {
                mMovieAdapter.updateMovies(list);
            }

            // Restore state if view and fragment's recycled when need memory.
            if (savedInstanceState != null) {
                Serializable data = (MovieList) savedInstanceState.getSerializable(Constants.BUNDLE_KEY_MOVIES_SAVED_LIST);
                if (data instanceof MovieList) {
                    mMovieAdapter.updateMovies((MovieList) data);
                }
            }
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        Utils.logDebug(TAG, this + " onViewStateRestored()" + " savedInstanceState:" + savedInstanceState);
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mResumed = true;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Utils.logDebug(TAG, "onSaveInstanceState() list:" + mMovieAdapter.getMovies());
        super.onSaveInstanceState(outState);
        outState.putSerializable(Constants.BUNDLE_KEY_MOVIES_SAVED_LIST, mMovieAdapter.getMovies());
    }

    @Override
    public void onPause() {
        super.onPause();
        mResumed = false;
    }

    @Override
    public void onStop() {
        super.onStop();
        mResumed = false;
    }

    @Override
    public void onAnimationStart() {
    }

    @Override
    public void onAnimationEnd() {
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab) {
            onFabBtnClicked();
        }
    }

    protected void onFabBtnClicked() {
        mAnimator.showAnimator(mFABtn, AnimatorType.CLICKED, this);

        if (!ensureKeywordNotNull()) {
            return;
        }

        // Start search operation.
        ((InputMethodManager) mSearchBox.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(mSearchBox.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        searchMovieRequest(Constants.GET_REQUEST_NO_PAGE_INDEX + 1);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (mSearchBox == null) {
            return false;
        }

        if (actionId == EditorInfo.IME_ACTION_SEARCH
                || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {

            ((InputMethodManager) mSearchBox.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(mSearchBox.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

            if (!ensureKeywordNotNull()) {
                return true;
            }

            if (mFABtn.isEnabled()) {
                searchMovieRequest(Constants.GET_REQUEST_NO_PAGE_INDEX + 1);
            }
            return true;
        }
        return false;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        updateFAButton(s.toString());
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    private boolean ensureKeywordNotNull() {
        if (mSearchBox == null || mSearchBox.getText() == null || mSearchBox.getText().toString().isEmpty()) {
            Utils.logError(TAG, "Search operation blocked since no input keyword.");

            if (mRefreshLayout != null && mRefreshLayout.isRefreshing()) {
                mRefreshLayout.post(() -> mRefreshLayout.setRefreshing(false));
            }

            Toast.makeText(getActivity(), R.string.text_keyword_empty, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onRefresh() {
        if (!ensureKeywordNotNull()) {
            return;
        }

        if (mFABtn.isEnabled()) {
            if (mRefreshLayout != null) {
                mRefreshLayout.post(() -> mRefreshLayout.setRefreshing(true));
            }
            searchMovieRequest(Constants.GET_REQUEST_NO_PAGE_INDEX + 1);
            if (mRecyclerView != null)
                mRecyclerView.setVisibility(View.INVISIBLE);
        }
    }

    public void onLoadMoreClicked(int moreIndex) {
        if (!ensureKeywordNotNull()) {
            new Handler().postDelayed(
                    () -> mMovieAdapter.updateMovies(null, true), 300);
            return;
        }

        searchMovieRequest(moreIndex);
    }

    public void updateFAButton(String string) {
        if (string != null && !string.isEmpty()) {
            if (mKeywords == null || mKeywords.isEmpty()) {
                if (mResumed) {
                    mAnimator.showAnimator(mFABtn, AnimatorType.CLICKABLE, this);
                }
            } else if (!mResumed) {
                mFABtn.setOnClickListener(this);
                ColorStateList colorStateList = ColorStateList.valueOf(Constants.COLOR_TINT_FAB_ENABLE);
                mFABtn.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
                mFABtn.setBackgroundTintList(colorStateList);
            }
        } else {
            if (mKeywords != null && !mKeywords.isEmpty()) {
                mAnimator.showAnimator(mFABtn, AnimatorType.UNCLICKABLE, this);
            }
        }
        mKeywords = string;
    }

    private void searchMovieRequest(int pageIndex) {
        if (mMoviePresenter != null) {
            mMoviePresenter.searchMovie(mKeywords, pageIndex);
        }
    }

    @Override
    public void showProgress() {
        if (mRefreshLayout != null && mRefreshLayout.isRefreshing()) {
            Utils.showProgressDialog(getActivity(), R.string.text_progress_refresh_waiting);
        } else {
            Utils.showProgressDialog(getActivity(), R.string.text_progress_waiting);
        }
    }

    @Override
    public void hideProgress() {
        if (mRefreshLayout != null) {
            mRefreshLayout.post(() -> mRefreshLayout.setRefreshing(false));
        }

        if (mRecyclerView != null)
            mRecyclerView.setVisibility(View.VISIBLE);

        Utils.dismissProgressDialog();
    }

    @Override
    public void showResult(MovieList list, int pageIndex) {
        if (mMovieAdapter != null) {
            if (pageIndex <= Constants.GET_REQUEST_NO_PAGE_INDEX + 1) {
                // First load
                mMovieAdapter.updateMovies(list);
            } else {
                // More load
                mMovieAdapter.updateMovies(list, true);
            }
        }
    }

    @Override
    public void showFailed(String errorMsg, int pageIndex) {
        if (Constants.SEARCH_ERROR_RESULT_KEY_NONE_TIP.equals(errorMsg) && pageIndex >= Constants.GET_REQUEST_NO_PAGE_INDEX + 1) {
            // Load more to end
            mMovieAdapter.updateMovies(new MovieList(), true);
        } else {
            // Load failed
            mMovieAdapter.updateMovies(null, true);
            Utils.showAlertDialog(getActivity(), errorMsg);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mResumed = false;
        Utils.saveMoviesToFile(mMovieAdapter.getMovies(), getActivity());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mResumed = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Utils.recycleProgressDialog();
        mResumed = false;
        Utils.saveMoviesToFile(null, getActivity());
    }
}