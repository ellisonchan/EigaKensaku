package com.ellison.eigakensaku.ui.base;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ellison.eigakensaku.utils.Utils;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment {
    private static final String TAG = BaseFragment.class.getSimpleName();
    protected Unbinder mBinder;

    @Override
    public void onAttach(@NonNull Context context) {
        Utils.logDebug(TAG, this + " onAttach()");
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Utils.logDebug(TAG, this + " onCreate()" + " savedInstanceState:" + savedInstanceState);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Utils.logDebug(TAG, this + " onCreateView()" + " container:" + container);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    @CallSuper
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Utils.logDebug(TAG, this + " onViewCreated()" + " view:" + view);
        init();
        super.onViewCreated(view, savedInstanceState);
    }

    @CallSuper
    protected void init() {
        Utils.logDebug(TAG, this + " init()");
        mBinder = ButterKnife.bind(this, getView());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Utils.logDebug(TAG, this + " onActivityCreated()" + " savedInstanceState:" + savedInstanceState);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        Utils.logDebug(TAG, this + " onViewStateRestored()" + " savedInstanceState:" + savedInstanceState);
        super.onViewStateRestored(savedInstanceState);
    }

    @NonNull
    @Override
    public LayoutInflater onGetLayoutInflater(@Nullable Bundle savedInstanceState) {
        Utils.logDebug(TAG, this + " onGetLayoutInflater()" + " savedInstanceState:" + savedInstanceState);
        return super.onGetLayoutInflater(savedInstanceState);
    }

    @Override
    public void onInflate(@NonNull Context context, @NonNull AttributeSet attrs, @Nullable Bundle savedInstanceState) {
        Utils.logDebug(TAG, this + " onInflate()" + " attrs:" + attrs);
        super.onInflate(context, attrs, savedInstanceState);
    }

    @Override
    public void onStart() {
        Utils.logDebug(TAG, this + " onStart()");
        super.onStart();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        Utils.logDebug(TAG, this + " onHiddenChanged()" + " hidden:" + hidden);
        super.onHiddenChanged(hidden);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        Utils.logDebug(TAG, this + " setUserVisibleHint() isVisibleToUser:" + isVisibleToUser);
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onResume() {
        Utils.logDebug(TAG, this + " onResume()");
        super.onResume();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        Utils.logDebug(TAG, this + " onConfigurationChanged()" + " newConfig:" + newConfig);
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Utils.logDebug(TAG, this + " onSaveInstanceState()" + " outState:" + outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        Utils.logDebug(TAG, this + " onPause()");
        super.onPause();
    }

    @Override
    public void onStop() {
        Utils.logDebug(TAG, this + " onStop()");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Utils.logDebug(TAG, this + " onDestroyView()");
        super.onDestroyView();
        if (mBinder != null) {
            mBinder.unbind();
        }
    }

    @Override
    public void onLowMemory() {
        Utils.logError(TAG, this + " onLowMemory()");
        super.onLowMemory();
    }

    @Override
    public void onDestroy() {
        Utils.logDebug(TAG, this + " onDestroy()");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Utils.logDebug(TAG, this + " onDetach()");
        super.onDetach();
    }
}
