package com.ellison.eigakensaku.ui.view;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ellison.eigakensaku.R;
import com.ellison.eigakensaku.constants.Constants;
import com.ellison.eigakensaku.ui.animator.AnimatorShowerImplement;
import com.ellison.eigakensaku.ui.animator.AnimatorType;
import com.ellison.eigakensaku.ui.animator.IAnimatorCallback;
import com.ellison.eigakensaku.ui.animator.IAnimatorShower;
import com.ellison.eigakensaku.ui.touch.ISwipeGestureListener;
import com.ellison.eigakensaku.utils.Utils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieHolder extends RecyclerView.ViewHolder implements ISwipeGestureListener {
    private static final String TAG = MovieHolder.class.getSimpleName();

    @BindView(R.id.iv)
    View itemView;
    @BindView(R.id.tv_title)
    TextView title;
    @BindView(R.id.iv_post)
    ImageView post;
    @BindView(R.id.tv_year)
    TextView year;
    @BindView(R.id.tv_type)
    TextView type;
    @BindView(R.id.item_menu_tip)
    TextView star_tip;
    @BindView(R.id.item_menu_icon)
    ImageView star_icon;

    private boolean isStarred = false;

    public MovieHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void onSwipeTipShowing(float dX, float dY) {
        final int itemWidth = itemView.getWidth();
        final int menuWidth = ((ViewGroup) itemView).getChildAt(1).getWidth();
        Utils.logDebug(TAG, "onStarMenuShowing() itemWidth:" + itemWidth + " menuWidth:" + menuWidth);

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
        isStarred = !isStarred;
        updateTextView(star_tip, isStarred ? R.string.text_rv_menu_type_content_unstar : R.string.text_rv_menu_type_content_star);
        updateVisibility(star_tip, View.INVISIBLE);
        updateVisibility(star_icon, View.VISIBLE);
        showStarAnimator(star_icon, isStarred, new IAnimatorCallback() {
            @Override
            public void onAnimationStart() {
            }

            @Override
            public void onAnimationEnd() {
                if (callback != null) {
                    viewScrollTo(itemView, 0, 0);
                    Utils.logDebug(TAG, "showStarAnimator() onAnimationEnd onSwipeMenuShown SCROLL RESET");

                    Utils.logDebug(TAG, "showStarAnimator() onAnimationEnd onSwipeMenuShown CALLBACK");
                    callback.onSwipeMenuShown();
                }
            }
        });
    }

    @Override
    public void onSwipeMenuSelected() {
        updateTransitionZ(itemView, Constants.SWIPE__MENU_SELECTED_TRANSITION_Z);
        updateVisibility(star_tip, View.VISIBLE);
        updateVisibility(star_icon, View.INVISIBLE);
    }

    @Override
    public void onSwipeMenuCleared() {
        updateTransitionZ(itemView, Constants.SWIPE__MENU_UNSELECTED_TRANSITION_Z);
        updateVisibility(star_tip, View.VISIBLE);
        updateVisibility(star_icon, View.INVISIBLE);
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

    private void showStarAnimator(View view, boolean isStarring, IAnimatorCallback callback) {
        if (view == null) {
            return;
        }
        Utils.logDebug(TAG, "showStarAnimator() view:" + view + " isStarring:" + isStarring + " callback:" + callback);
        AnimatorShowerImplement.getInstance().showAnimator(view, isStarring ? AnimatorType.STARRING : AnimatorType.UNSTARRING, callback);
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
