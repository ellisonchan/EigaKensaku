package com.ellison.eigakensaku.ui.animator;

import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public interface IAnimatorShower {
    void showAnimator(FloatingActionButton view, AnimatorType type, IAnimatorCallback callback);
    void showAnimator(View view, AnimatorType type, IAnimatorCallback callback);
}