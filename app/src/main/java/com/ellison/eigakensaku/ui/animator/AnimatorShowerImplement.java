package com.ellison.eigakensaku.ui.animator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.ellison.eigakensaku.constants.Constants;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AnimatorShowerImplement implements IAnimatorShower {
    @Override
    public void showAnimator(View view, AnimatorType type, IAnimatorCallback callback) {
        switch (type) {
            case CLICKED:
                break;
            case CLICKABLE:
                break;
            case UNCLICKABLE:
                break;
            default:
                break;
        }
    }

    @Override
    public void showAnimator(FloatingActionButton view, AnimatorType type, IAnimatorCallback callback) {
        switch (type) {
            case CLICKED:
                showClickedAnimator(view, callback);
                break;
            case CLICKABLE:
                showClickableAnimator(view, true, callback);
                break;
            case UNCLICKABLE:
                showClickableAnimator(view, false, callback);
                break;
            default:
                break;
        }
    }

    private void showClickableAnimator(FloatingActionButton view, boolean isClickable, IAnimatorCallback callback) {
        // init scale animator
        ObjectAnimator xAnimator = ObjectAnimator.ofFloat(view, "scaleX", Constants.SCALE_FAB_NORMAL, Constants.SCALE_FAB_CLICKABLE, Constants.SCALE_FAB_NORMAL);
        ObjectAnimator yAnimator = ObjectAnimator.ofFloat(view, "scaleY", Constants.SCALE_FAB_NORMAL, Constants.SCALE_FAB_CLICKABLE, Constants.SCALE_FAB_NORMAL);
        ObjectAnimator alphaAnimator;
        ValueAnimator colorAnimator;

        if (isClickable) {
            // enabled drawable and clear alpha
            alphaAnimator = ObjectAnimator.ofInt(view.getDrawable(), "alpha", Constants.ALPHA_FAB_DISABLE, Constants.ALPHA_FAB_ENABLE);
            // need argb color
            colorAnimator = ValueAnimator.ofArgb(Constants.COLOR_TINT_FAB_DISABLE, Constants.COLOR_TINT_FAB_ENABLE);

            // enable
            if (callback instanceof View.OnClickListener) {
                view.setOnClickListener((View.OnClickListener) callback);
            }
        } else {
            // disabled drawable and not clear alpha
            alphaAnimator = ObjectAnimator.ofInt(view.getDrawable(), "alpha", Constants.ALPHA_FAB_ENABLE, Constants.ALPHA_FAB_DISABLE);
            // need argb color
            colorAnimator = ValueAnimator.ofArgb(Constants.COLOR_TINT_FAB_ENABLE, Constants.COLOR_TINT_FAB_DISABLE);

            // disable
            view.setClickable(false);
        }

        // play scale and alpha animator
        AnimatorSet set = new AnimatorSet();
        set.setInterpolator(isClickable ? new DecelerateInterpolator() : new AccelerateInterpolator());
        set.setDuration(isClickable ? Constants.DURATION_FAB_ENABLE : Constants.DURATION_FAB_DISABLE);
        set.playTogether(xAnimator, yAnimator, alphaAnimator, colorAnimator);
        set.start();

        // update background color
        colorAnimator.addUpdateListener(animation -> {
            int color = (int) animation.getAnimatedValue();
            // ColorStateList colorStateList = ContextCompat.getColorStateList(getApplicationContext(), R.color.colorAccent);
            ColorStateList colorStateList = ColorStateList.valueOf(color);
            view.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
            view.setBackgroundTintList(colorStateList);
        });
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                callback.onAnimationStart();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                callback.onAnimationEnd();
            }
        });
    }

    private void showClickedAnimator(FloatingActionButton view, IAnimatorCallback callback) {
        final int width = view.getDrawable().getBounds().width();
        final int height = view.getDrawable().getBounds().height();
        final int left = view.getDrawable().getBounds().left;
        final int top = view.getDrawable().getBounds().top;
        final int right = view.getDrawable().getBounds().right;
        final int bottom = view.getDrawable().getBounds().bottom;

        // resource drawable scale animator
        ValueAnimator scaleDrawableAnimator = ValueAnimator.ofFloat(Constants.SCALE_FAB_NORMAL, Constants.SCALE_FAB_CLICKED, Constants.SCALE_FAB_NORMAL);
        scaleDrawableAnimator.addUpdateListener(animation -> {
            final float drawableProgress = (float) animation.getAnimatedValue() - Constants.SCALE_FAB_NORMAL;
            int newLeft = left - (int) (width * drawableProgress);
            int newTop = top - (int) (height * drawableProgress);
            int newRight = right + (int) (width * drawableProgress);
            int newBottom = bottom + (int) (height * drawableProgress);

            view.getDrawable().setBounds(newLeft, newTop, newRight, newBottom);
        });

        ObjectAnimator xAnimator = ObjectAnimator.ofFloat(view, "scaleX", Constants.SCALE_FAB_NORMAL, Constants.SCALE_FAB_CLICKED, Constants.SCALE_FAB_NORMAL);
        ObjectAnimator yAnimator = ObjectAnimator.ofFloat(view, "scaleY", Constants.SCALE_FAB_NORMAL, Constants.SCALE_FAB_CLICKED, Constants.SCALE_FAB_NORMAL);
        AnimatorSet set = new AnimatorSet();
        set.setInterpolator(new DecelerateInterpolator());
        set.setDuration(Constants.DURATION_FAB_CLICKED);
        set.playTogether(xAnimator, yAnimator, scaleDrawableAnimator);
        set.start();
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                callback.onAnimationStart();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                callback.onAnimationEnd();
            }
        });
    }
}
