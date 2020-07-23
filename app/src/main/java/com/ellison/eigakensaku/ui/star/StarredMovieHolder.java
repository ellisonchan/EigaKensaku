package com.ellison.eigakensaku.ui.star;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ellison.eigakensaku.R;
import com.ellison.eigakensaku.constants.Constants;
import com.ellison.eigakensaku.ui.animator.AnimatorShowerImplement;
import com.ellison.eigakensaku.ui.animator.AnimatorType;
import com.ellison.eigakensaku.ui.animator.IAnimatorCallback;
import com.ellison.eigakensaku.ui.touch.ITouchDataSync;
import com.ellison.eigakensaku.ui.touch.ISwipeGestureListener;
import com.ellison.eigakensaku.utils.Utils;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StarredMovieHolder extends RecyclerView.ViewHolder implements ISwipeGestureListener {
    private static final String TAG = StarredMovieHolder.class.getSimpleName();
    private WeakReference<ITouchDataSync> touchDataSync;

    @BindView(R.id.star_main_content)
    View itemView;
    @BindView(R.id.star_tv_title)
    TextView title;
    @BindView(R.id.star_iv_post)
    ImageView post;
    @BindView(R.id.star_tv_year)
    TextView year;
    @BindView(R.id.star_tv_type)
    TextView type;
    @BindView(R.id.star_tv_actor)
    TextView actor;
    @BindView(R.id.star_tv_country)
    TextView country;
    @BindView(R.id.star_tv_info)
    TextView info;
    @BindView(R.id.star_remove_tip)
    TextView star_tip;
    @BindView(R.id.star_remove_icon)
    ImageView star_icon;

    public StarredMovieHolder(@NonNull View itemView, ITouchDataSync sync) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        touchDataSync = new WeakReference<>(sync);
    }

    @Override
    public void onSwipeTipShowing(float dX, float dY) {
        final int menuWidth = ((ViewGroup) itemView).getChildAt(1).getWidth();
        Utils.logDebug(TAG, "onStarMenuShowing() itemWidth:" + itemView.getWidth() + " menuWidth:" + menuWidth);

        if (Math.abs(dX) <= menuWidth) {
            // Scroll until view totally show
            viewScrollTo(itemView, (int) Math.abs(dX), 0);
            Utils.logDebug(TAG, "onStarMenuShowing() scrollTo:-" +  Math.abs(dX));
        } else {
            Utils.logDebug(TAG, "onStarMenuShowing() scroll over threshold & do NOTH");
        }
    }

    @Override
    public void onSwipeMenuShowing(ISwipeMenuCallback callback) {
        updateVisibility(star_tip, View.INVISIBLE);
        updateVisibility(star_icon, View.VISIBLE);

        showStarAnimator(star_icon, new IAnimatorCallback() {
            @Override
            public void onAnimationStart() {
            }

            @Override
            public void onAnimationEnd() {
                if (callback != null) {
//                    Utils.logDebug(TAG, "showStarAnimator() onAnimationEnd SCROLL RESET");
//                    viewScrollTo(itemView, 0, 0);

//                    Utils.logDebug(TAG, "showStarAnimator() onAnimationEnd ANIMATION RESET");
//                    callback.onSwipeMenuShown();

                    if (touchDataSync.get() != null) {
                        Utils.logDebug(TAG, "showStarAnimator() onAnimationEnd STAR SYNC");
                        touchDataSync.get().onItemRemoved(getAdapterPosition());
                    }
                }
            }
        });
    }

    @Override
    public void onSwipeMenuSelected() {
        //updateTransitionZ(itemView, Constants.SWIPE__MENU_SELECTED_TRANSITION_Z);
        updateVisibility(star_tip, View.VISIBLE);
        updateVisibility(star_icon, View.INVISIBLE);
    }

    @Override
    public void onSwipeMenuCleared() {
        //updateTransitionZ(itemView, Constants.SWIPE__MENU_UNSELECTED_TRANSITION_Z);
        updateVisibility(star_tip, View.VISIBLE);
        updateVisibility(star_icon, View.INVISIBLE);
    }

    @Override
    public void onDragOver(int targetPos) {
        Utils.logDebug(TAG, "onDragOver() myPos:" + getAdapterPosition() + " targetPos:" + targetPos);
        if (touchDataSync.get() != null) {
            Utils.logDebug(TAG, "onDragOver() SWAP ITEMS");
            touchDataSync.get().onItemSwap(getAdapterPosition(), targetPos);
        }
    }

    private void updateTextView(TextView view, int id) {
        if (view == null) {
            return;
        }
        Utils.logDebug(TAG, "updateTextView() view:" + view);
        view.setText(id);
    }

    private void viewScrollTo(View view, int x, int y) {
        if (view == null) {
            return;
        }
        Utils.logDebug(TAG, "viewScrollTo() view:" + view + " x:" + x + " y:" + y);
        view.scrollTo(x, y);
    }

    private void showStarAnimator(View view, IAnimatorCallback callback) {
        if (view == null) {
            return;
        }
        Utils.logDebug(TAG, "showStarAnimator() view:" + view + " callback:" + callback);
        AnimatorShowerImplement.getInstance().showAnimator(view, AnimatorType.REMOVING, callback);
    }

    private void updateTransitionZ(View view, float z) {
        if (view == null) {
            return;
        }
        Utils.logDebug(TAG, "updateTransitionZ() view:" + view + " z:" + z);
        view.setTranslationZ(z);
    }

    private void updateVisibility(View view, int visibility) {
        if (view == null) {
            return;
        }
        Utils.logDebug(TAG, "updateVisibility() view:" + view + " visibility:" + (visibility == View.VISIBLE ? "VISIBLE" : " INVISIBLE"));
        view.setVisibility(visibility);
    }
}