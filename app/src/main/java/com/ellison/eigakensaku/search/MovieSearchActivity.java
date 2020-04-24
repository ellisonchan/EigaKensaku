package com.ellison.eigakensaku.search;

import android.content.Context;

import com.ellison.eigakensaku.beans.Movie;
import com.ellison.eigakensaku.constants.Constants;
import com.ellison.eigakensaku.presenter.IMoviePresenter;
import com.ellison.eigakensaku.presenter.MoviePresenter;
import com.ellison.eigakensaku.utils.Utils;
import com.ellison.eigakensaku.view.IMovieView;
import com.ellison.eigakensaku.R;
import com.ellison.eigakensaku.base.BaseActivity;
import com.ellison.eigakensaku.beans.MovieList;
import com.ellison.eigakensaku.view.MovieAdapter;
import com.ellison.eigakensaku.view.MovieItemDecoration;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieSearchActivity extends BaseActivity implements IMovieView,
        EditText.OnEditorActionListener,
        TextWatcher,
        SwipeRefreshLayout.OnRefreshListener,
        MovieAdapter.ILoadMoreListener {

    private static final String TAG = "MovieSearchActivity";
    private String mKeywords;
    private IMoviePresenter mMoviePresenter;
    private MovieAdapter mMovieAdapter;

    @BindView(R.id.search_box)
    EditText mSearchBox;
    //    MovieSearchBox mSearchBox;

    @BindView(R.id.fab)
    FloatingActionButton mFABtn;

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;

    @BindView(R.id.rv_layout)
    RecyclerView mRecyclerView;

    @Override
    protected void init() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        setContentView(R.layout.activity_noscrolling);
        // setContentView(R.layout.activity_scrolling);
        ButterKnife.bind(this);

        if (mSearchBox != null)
            mSearchBox.setOnEditorActionListener(this);

        if (mSearchBox != null)
            mSearchBox.addTextChangedListener(this);

        if (mRefreshLayout != null)
            mRefreshLayout.setOnRefreshListener(this);

        mMoviePresenter = new MoviePresenter(this);

        initRecyclerView();
    }

    private void initRecyclerView() {
        StaggeredGridLayoutManager sgLM = new StaggeredGridLayoutManager(Constants.MOVIE_LIST_ROW_NUMBER, StaggeredGridLayoutManager.VERTICAL);
        mMovieAdapter = new MovieAdapter(this);
        mMovieAdapter.setILoadMoreListener(this);
        MovieItemDecoration decoration=new MovieItemDecoration(Constants.MOVIE_LIST_ITEM_SPACE);

        if (mRecyclerView != null) {
            mRecyclerView.setLayoutManager(sgLM);
            mRecyclerView.setAdapter(mMovieAdapter);
            mRecyclerView.addItemDecoration(decoration);
        }
    }

    @Override
    protected void onFabBtnClicked() {
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
        updateFAButton(s.toString());
    }

    private boolean ensureKeywordNotNull() {
        if (mSearchBox == null || mSearchBox.getText() == null || mSearchBox.getText().toString().isEmpty()) {
            Log.e(TAG, "Search operation blocked since no input keyword.");

            if (mRefreshLayout != null && mRefreshLayout.isRefreshing()) {
                mRefreshLayout.post(()->mRefreshLayout.setRefreshing(false));
            }

            Toast.makeText(MovieSearchActivity.this, R.string.text_keyword_empty, Toast.LENGTH_SHORT).show();
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
                mRefreshLayout.post(()->mRefreshLayout.setRefreshing(true));
            }
            searchMovieRequest(Constants.GET_REQUEST_NO_PAGE_INDEX + 1);
            if (mRecyclerView != null)
                mRecyclerView.setVisibility(View.INVISIBLE);
        }
    }

    public void onLoadMoreClicked(int moreIndex) {
        searchMovieRequest(moreIndex);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item == null) {
            return false;
        }

        if (item.getItemId() == R.id.action_settings_search) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateFAButton(String string) {
        if(string != null && !string.isEmpty()) {
            mFABtn.setEnabled(true);
        } else {
            mFABtn.setEnabled(false);
        }
        mKeywords =string;
    }

    private void searchMovieRequest(int pageIndex) {
        if (mMoviePresenter != null) {
            mMoviePresenter.searchMovie(mKeywords, pageIndex);
//            // for test
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    // Test load finish
//                    // showResult(Utils.makeFakeList(), pageIndex);
//
//                    // Test load to end
//                    showResult(new MovieList(), pageIndex);
//                }
//            }, 3000);
        }
    };

    @Override
    public void showProgress() {
        if (mRefreshLayout != null && mRefreshLayout.isRefreshing()) {
            Utils.showProgressDialog(this, R.string.text_progress_refresh_waiting);
        } else {
            Utils.showProgressDialog(this, R.string.text_progress_waiting);
        }
    }

    @Override
    public void hideProgress() {
        if (mRefreshLayout != null) {
            mRefreshLayout.post(()->mRefreshLayout.setRefreshing(false));
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
            Utils.showAlertDialog(this, errorMsg);
        }
    }
}