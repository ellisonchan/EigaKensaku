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
import android.view.ViewTreeObserver;
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
import com.ellison.eigakensaku.ui.touch.NestedSwipeRereshLayout;
import com.ellison.eigakensaku.ui.touch.SwipeCollapseType;
import com.ellison.eigakensaku.ui.view.IMovieView;
import com.ellison.eigakensaku.ui.view.MovieAdapter;
import com.ellison.eigakensaku.ui.view.MovieItemDecoration;
import com.ellison.eigakensaku.ui.view.MovieSearchBox;
import com.ellison.eigakensaku.utils.Utils;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;

public class SearchFragment extends BaseFragment
        implements View.OnClickListener,
        IMovieView,
        EditText.OnEditorActionListener,
        TextWatcher,
        SwipeRefreshLayout.OnRefreshListener,
        IAnimatorCallback,
        NestedSwipeRereshLayout.INestedScrollListener,
        MovieAdapter.IDataUpdateCallback {
    private static final String TAG = SearchFragment.class.getSimpleName();

    private String mKeywords;
    private IMoviePresenter mMoviePresenter;
    private MovieAdapter mMovieAdapter;
    private IAnimatorShower mAnimator;
    private boolean mResumed = false;

    private SwipeCollapseType mCollapseState = SwipeCollapseType.NORMAL;

    @BindView(R.id.app_bar)
    AppBarLayout mAppBar;

    @BindView(R.id.search_box)
    MovieSearchBox mSearchBox;

    @BindView(R.id.fab)
    FloatingActionButton mFABtn;

    @BindView(R.id.refresh_layout)
    NestedSwipeRereshLayout mRefreshLayout;

    @BindView(R.id.rv_layout)
    RecyclerView mRecyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Utils.logDebug(Utils.TAG_SEARCH, this + " onCreateView()" + " container:" + container + " savedInstanceState:" + savedInstanceState);
        // View root = inflater.inflate(R.layout.fragment_search, container, false);
        View root = inflater.inflate(R.layout.fragment_search_nested, container, false);
        // View root = inflater.inflate(R.layout.fragment_search_nested_reresh, container, false);
        return root;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        if (mSearchBox != null) {
            mSearchBox.setOnEditorActionListener(this);
            mSearchBox.addTextChangedListener(this);
            mSearchBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utils.logDebug(Utils.TAG_SEARCH, " onClick() v:" + v + " STATE:" + getCollapseState());
                    if (getCollapseState() == SwipeCollapseType.COLLAPSED
                            || getCollapseState() == SwipeCollapseType.COLLAPSING
                            || getCollapseState() == SwipeCollapseType.EXPANDING) {
                        ViewGroup.LayoutParams params = getViewParams(mAppBar);
                        if (params != null) {
                            Utils.logDebug(Utils.TAG_SEARCH, " onClick() params.height:" + params.height);
                            params.height = mBarHeight;
                            setViewParams(mAppBar, params);
                            setCollapseState(SwipeCollapseType.NORMAL);
                            Utils.logDebug(Utils.TAG_SEARCH, " onClick() AFTER params.height:" + params.height);
                        }

                        Utils.logDebug(Utils.TAG_SEARCH, " onClick() mFABtn.getTranslationY:" + mFABtn.getTranslationY());
                        if (mFABtn != null) {
                            if (mFABtn.getTranslationY() != 0) {
                                Utils.logDebug(Utils.TAG_SEARCH, " onClick() mFABtn TRANSITION RESET");
                                mFABtn.setTranslationY(0);
                            }
                            if (mFABtn.getVisibility() != View.VISIBLE) {
                                Utils.logDebug(Utils.TAG_SEARCH, " onClick() mFABtn VISIBLE");
                                mFABtn.setVisibility(View.VISIBLE);
                            }
                        }

                    }
                }
            });
        }

        if (mRefreshLayout != null) {
            mRefreshLayout.setOnRefreshListener(this);
            mRefreshLayout.injectNestedScrollListener(this);
        }

        mMoviePresenter = new MoviePresenter(this);

        initRecyclerView(savedInstanceState);

        mFABtn.getDrawable().setAlpha(Constants.ALPHA_FAB_DISABLE);
        mFABtn.setClickable(false);

        mAnimator = AnimatorShowerImplement.getInstance();

        mAppBar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Utils.logDebug(Utils.TAG_SEARCH, " OnGlobalLayoutListener() mAppBar:" + mAppBar + " mSearchBox:" + mSearchBox + " APPBAR_COLLAPSE_OFFSET_THRESHOLD:" + APPBAR_COLLAPSE_OFFSET_THRESHOLD);
                if (APPBAR_COLLAPSE_OFFSET_THRESHOLD != 0 || mAppBar == null || mSearchBox == null) {
                    return;
                }

                mBarWidth = mAppBar.getWidth();
                mBarHeight = mAppBar.getHeight();
                mEditBoxWidth = mSearchBox.getWidth();
                mEditBoxHeight = mSearchBox.getHeight();
                FAB_TRANSITION_OFFSET_THRESHOLD = APPBAR_COLLAPSE_OFFSET_THRESHOLD = mBarHeight - mEditBoxHeight - Constants.APPBAR_COLLAPSE_OFFSET_SLOP;
                FAB_VISIBLE_OFFSET_THRESHOLD = APPBAR_COLLAPSE_OFFSET_THRESHOLD - FAB_INVISIBLE_OFFSET_THRESHOLD;

                Utils.logDebug(Utils.TAG_SEARCH, " OnGlobalLayoutListener() UPDATE mBarWidth:" + mBarWidth + " mBarHeight:" + mBarHeight
                        + " mEditBoxWidth:" + mEditBoxWidth + " mEditBoxHeight:" + mEditBoxHeight
                        + " APPBAR_COLLAPSE_OFFSET_THRESHOLD:" + APPBAR_COLLAPSE_OFFSET_THRESHOLD
                        + " FAB_TRANSITION_OFFSET_THRESHOLD:" + FAB_TRANSITION_OFFSET_THRESHOLD
                        + " FAB_INVISIBLE_OFFSET_THRESHOLD:" + FAB_INVISIBLE_OFFSET_THRESHOLD
                        + " FAB_VISIBLE_OFFSET_THRESHOLD:" + FAB_VISIBLE_OFFSET_THRESHOLD);
            }
        });
    }

    private void initRecyclerView(Bundle savedInstanceState) {
        Utils.logDebug(Utils.TAG_SEARCH, this + " initRecyclerView()");
        StaggeredGridLayoutManager sgLM = new StaggeredGridLayoutManager(Constants.MOVIE_LIST_ROW_NUMBER, StaggeredGridLayoutManager.VERTICAL);
        mMovieAdapter = new MovieAdapter(getActivity());
        mMovieAdapter.setIDataUpdateCallback(this);
        MovieItemDecoration decoration = new MovieItemDecoration(Constants.MOVIE_LIST_ITEM_SPACE);
        ItemSwipeCallback touchCallback = new ItemSwipeCallback();
        ItemTouchHelper touchHelper = new ItemTouchHelper(touchCallback);
        touchCallback.syncTouchHelper(touchHelper);

        if (mRecyclerView != null) {
            mRecyclerView.setLayoutManager(sgLM);
            mRecyclerView.setAdapter(mMovieAdapter);
            touchHelper.attachToRecyclerView(mRecyclerView);

            MovieList list = Utils.readMoviesFromFile(getActivity());
            Utils.logDebug(Utils.TAG_SEARCH, this + " initRecyclerView() list:" + list);
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
        Utils.logDebug(Utils.TAG_SEARCH, this + " onViewStateRestored()" + " savedInstanceState:" + savedInstanceState);
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mResumed = true;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Utils.logDebug(Utils.TAG_SEARCH, "onSaveInstanceState() list:" + mMovieAdapter.getMovies());
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

    @Override
    public void onLoadMoreClicked(int moreIndex) {
        if (!ensureKeywordNotNull()) {
            new Handler().postDelayed(
                    () -> mMovieAdapter.updateMovies(null, true), 300);
            return;
        }

        searchMovieRequest(moreIndex);
    }

    @Override
    public boolean isItemStarred(Movie movie) {
        Utils.logDebug(Utils.TAG_SEARCH, "isItemStarred movie:" + movie);
        return mMoviePresenter == null ? false : mMoviePresenter.isMovieStarred(movie, getActivity().getApplicationContext());
    }

    @Override
    public void onItemStarred(Movie movie, boolean isStarred) {
        Utils.logDebug(Utils.TAG_SEARCH, "onItemStarred movie:" + movie + " isStarred:" + isStarred);
        if (mMoviePresenter != null) {
            mMoviePresenter.starMovie(movie, isStarred, getActivity().getApplicationContext());
        }
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

    private int cachedX = 0, cachedY = 0,
            mBarHeightWhenDown = 0, mBarWidth = 0, mBarHeight = 0,
            mEditBoxWidth = 0, mEditBoxHeight = 0;
    public int APPBAR_COLLAPSE_OFFSET_THRESHOLD = 0,
            FAB_INVISIBLE_OFFSET_THRESHOLD = 50,
            FAB_VISIBLE_OFFSET_THRESHOLD = 0,
            FAB_TRANSITION_OFFSET_THRESHOLD = 0;

    /**
     * Get appbar / fab 's collapse state.
     */
    private SwipeCollapseType getCollapseState() {
        return mCollapseState;
    }

    /**
     * Set appbar / fab 's collapse state.
     */
    private void setCollapseState(SwipeCollapseType state) {
        mCollapseState = state;
    }

    private ViewGroup.LayoutParams getViewParams(View view) {
        return view == null ? null : view.getLayoutParams();
    }

    private void setViewParams(View view, ViewGroup.LayoutParams params) {
        if (view != null) {
            view.setLayoutParams(params);
        }
    }

    @Override
    public void onScrollDown() {
        Utils.logDebug(Utils.TAG_SEARCH, "onScrollDown cachedX:" + cachedX + " cachedY:" + cachedY + " mCachedHeight:" + mBarHeightWhenDown);
        ViewGroup.LayoutParams params = getViewParams(mAppBar);

        if (params != null) {
            mBarHeightWhenDown = params.height;
        }
        Utils.logDebug(Utils.TAG_SEARCH, "onScrollDown WIDTH:" + params.width + " HEIGHT:" + params.height + " mCachedHeight:" + mBarHeightWhenDown);
    }

    @Override
    public void onScrollUpCancel() {
        Utils.logDebug(Utils.TAG_SEARCH, "onScrollUp cachedX:" + cachedX + " cachedY:" + cachedY + " mCachedHeight:" + mBarHeightWhenDown);
        // Reset the scroll value.
        mBarHeightWhenDown = cachedX = cachedY = 0;
    }

    @Override
    public void onScroll(float dx, float dy, float xVelocity, float yVelocity) {
        Utils.logDebug(Utils.TAG_SEARCH, "onScroll dx:" + dx + " dy:" + dy + " xVelocity:" + xVelocity + " yVelocity:" + yVelocity + " cachedX:" + cachedX + " cachedY:" + cachedY);
        if (dy >= cachedY - Constants.SCROLL_HANDLE_THRESHOLD
                && dy <= cachedY + Constants.SCROLL_HANDLE_THRESHOLD) {
                // Not handle if swipe down offset less than threshold.
                Utils.logDebug(Utils.TAG_SEARCH, "onScroll CACHED / BELOW THRESHOLD & NOTH");
                return;
        }

        Utils.logDebug(Utils.TAG_SEARCH, "onScroll NO CACHED & FIX");
        fixAppBarAndFAB(dx, dy);

        Utils.logDebug(Utils.TAG_SEARCH, "onScroll NO CACHED & UPDATED");
        cachedX = (int) dx;
        cachedY = (int) dy;
    }

    @Override
    public void onScroll(float dx, float dy) {
    }

    private void fixAppBarAndFAB(float dx, float dy) {
        Utils.logDebug(Utils.TAG_SEARCH, "fixAppBarAndFAB dx:" + dx + " dy:" + dy);

        if (mAppBar == null || mFABtn == null) {
            return;
        }

        if (dy < 0 && getCollapseState() == SwipeCollapseType.COLLAPSED) {
            Utils.logDebug(Utils.TAG_SEARCH, "fixAppBarAndFAB COLLAPSED & NOTH");
            return;
        }

        if (dy > 0 && getCollapseState() == SwipeCollapseType.NORMAL) {
            Utils.logDebug(Utils.TAG_SEARCH, "fixAppBarAndFAB NORMAL & NOTH");
            return;
        }

        fixAppBar((int) dy);
        fixFAB((int) dy);
    }

    private void fixAppBar(int heightOffset) {
        Utils.logDebug(Utils.TAG_SEARCH, "fixAppBar heightOffset:" + heightOffset + " APPBAR_COLLAPSE_OFFSET_THRESHOLD:" + APPBAR_COLLAPSE_OFFSET_THRESHOLD);
        ViewGroup.LayoutParams params = getViewParams(mAppBar);

        if (params == null) {
            return;
        }

        Utils.logDebug(Utils.TAG_SEARCH, "fixAppBar CURRENT WIDTH:" + params.width + " HEIGHT:" + params.height + " mBarHeightWhenDown:" + mBarHeightWhenDown);

        if (heightOffset < 0) {
            // Collapse
            if (Math.abs(heightOffset) >= APPBAR_COLLAPSE_OFFSET_THRESHOLD) {
                // Set width to threshold when dy over threshold before collapsed if width not collapsed to threshold.
                if (params.height != mBarHeight - APPBAR_COLLAPSE_OFFSET_THRESHOLD) {
                    params.height = mBarHeight - APPBAR_COLLAPSE_OFFSET_THRESHOLD;
                    setViewParams(mAppBar, params);
                    Utils.logDebug(Utils.TAG_SEARCH, "fixAppBar FIX HEIGHT BEFORE COLLAPSED & WIDTH:" + params.width + " HEIGHT:" + params.height);
                }

                // Collapsed
                Utils.logDebug(Utils.TAG_SEARCH, "fixAppBar COLLAPSING & OVER THRESHOLD & COLLAPSED");
                setCollapseState(SwipeCollapseType.COLLAPSED);
                return;
            }
            setCollapseState(SwipeCollapseType.COLLAPSING);
        } else {
            // Expand
            if (Math.abs(heightOffset) >= APPBAR_COLLAPSE_OFFSET_THRESHOLD) {
                // Restore width when dy over threshold before expanded if not expanded to original size.
                if (params.height != mBarHeight) {
                    params.height = mBarHeight;
                    setViewParams(mAppBar, params);
                    Utils.logDebug(Utils.TAG_SEARCH, "fixAppBar FIX HEIGHT BEFORE EXPANDED & WIDTH:" + params.width + " HEIGHT:" + params.height);
                }

                // Expanded
                Utils.logDebug(Utils.TAG_SEARCH, "fixAppBar EXPANDING & OVER THRESHOLD & NORMAL");
                setCollapseState(SwipeCollapseType.NORMAL);
                return;
            }
            setCollapseState(SwipeCollapseType.EXPANDING);
        }

        params.height = mBarHeightWhenDown + heightOffset;
        setViewParams(mAppBar, params);
        Utils.logDebug(Utils.TAG_SEARCH, "fixAppBar AFTER WIDTH:" + params.width + " HEIGHT:" + params.height);
    }

    private void fixFAB(int transitionY) {
        Utils.logDebug(Utils.TAG_SEARCH, "fixFAB transitionY:" + transitionY);

        if (transitionY < 0) {
            // fade out
            if (Math.abs(transitionY) >= FAB_INVISIBLE_OFFSET_THRESHOLD) {
                Utils.logDebug(Utils.TAG_SEARCH, "fixFAB OVER THRESHOLD & INVISIBLE");

                // show invisible animator
                // ...
                mFABtn.setVisibility(View.INVISIBLE);
                return;
            }

            // transition FAB
            Utils.logDebug(Utils.TAG_SEARCH, "fixFAB BELOW THRESHOLD & TRANSITION");
            mFABtn.setTranslationY(transitionY);
        } else {
            // fade in
            if (transitionY < FAB_VISIBLE_OFFSET_THRESHOLD) {
                Utils.logDebug(Utils.TAG_SEARCH, "fixFAB BELOW THRESHOLD & DO NOTH");
                return;
            } else {
                Utils.logDebug(Utils.TAG_SEARCH, "fixFAB OVER THRESHOLD & VISIBLE");
                // show visible animator
                // ...
                mFABtn.setVisibility(View.VISIBLE);

                if (transitionY < FAB_TRANSITION_OFFSET_THRESHOLD) {
                    // transition FAB
                    Utils.logDebug(Utils.TAG_SEARCH, "fixFAB BELOW THRESHOLD & TRANSITION");
                    mFABtn.setTranslationY(FAB_TRANSITION_OFFSET_THRESHOLD - transitionY);
                } else {
                    // transition to original location
                    Utils.logDebug(Utils.TAG_SEARCH, "fixFAB OVER THRESHOLD & TRANSITION TO ORIGINAL");
                    mFABtn.setTranslationY(0);
                }
            }
        }
    }
}