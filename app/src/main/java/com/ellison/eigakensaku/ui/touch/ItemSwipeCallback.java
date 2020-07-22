package com.ellison.eigakensaku.ui.touch;

import android.graphics.Canvas;
import com.ellison.eigakensaku.constants.Constants;
import com.ellison.eigakensaku.utils.Utils;

import java.lang.reflect.Field;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class ItemSwipeCallback extends ItemTouchHelper.Callback implements ISwipeGestureListener.ISwipeMenuCallback {
    private static final String TAG = ItemSwipeCallback.class.getSimpleName();
    private ItemTouchHelper itemTouchHelper;

    public void syncTouchHelper(ItemTouchHelper helper) {
        itemTouchHelper = helper;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, ItemTouchHelper.LEFT);
    }

    @Override
    public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
        return Constants.SWIPE_SHOW_MENU_THRESHOLD_FOR_SEARCH;
    }

    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        Utils.logDebug(TAG, "onSelectedChanged() viewHolder:" + viewHolder + " actionState:" + actionState);
        super.onSelectedChanged(viewHolder, actionState);

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            if (viewHolder instanceof ISwipeGestureListener) {
                Utils.logDebug(TAG, "onSelectedChanged() viewHolder:" + viewHolder + " MENU SELECTED");
                ((ISwipeGestureListener) viewHolder).onSwipeMenuSelected();
            }
        }
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        Utils.logDebug(TAG, "onChildDraw() viewHolder:" + viewHolder + " dX:" + dX + " dY:" + dY + " actionState:" + actionState + " isCurrentlyActive:" + isCurrentlyActive);

        // Instead of transition scroll view when swipe.
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            Utils.logDebug(TAG, "onChildDraw() viewHolder:" + viewHolder + " SWIPE");

            if (viewHolder instanceof ISwipeGestureListener) {
                Utils.logDebug(TAG, "onChildDraw() viewHolder:" + viewHolder + " SHOWING MENU TIP");
                ((ISwipeGestureListener) viewHolder).onSwipeTipShowing(dX, dY);
            }
        } else {
            Utils.logDebug(TAG, "onChildDraw() viewHolder:" + viewHolder + " !SWIPE");
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        Utils.logDebug(TAG, "onSwiped() viewHolder:" + viewHolder + " direction:" + direction);

        if (viewHolder instanceof ISwipeGestureListener) {
            Utils.logDebug(TAG, "onSwiped() viewHolder:" + viewHolder + " SHOW MENU ICON");
            ((ISwipeGestureListener) viewHolder).onSwipeMenuShowing(this);
        }
    }

    @Override
    public void onSwipeMenuShown() {
        Utils.logDebug(TAG, "onSwipeMenuShown() reset menu & icon");
        try {
            Field recoverAnimationField = itemTouchHelper.getClass().getDeclaredField("mRecoverAnimations");
            recoverAnimationField.setAccessible(true);
            ((List) recoverAnimationField.get(itemTouchHelper)).clear();
            // Utils.logDebug(TAG, "onSwiped() reset after dx:" + dx.get(itemTouchHelper) + " startx:" + startx.get(itemTouchHelper) + " animations:" + recoverAnimationField.get(itemTouchHelper));
        } catch (Exception e) {
            Utils.logDebug(TAG, "onSwipeMenuShown() reset reflect e:", e);
        }
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        Utils.logDebug(TAG, "clearView() viewHolder:" + viewHolder);
        super.clearView(recyclerView, viewHolder);

        if (viewHolder instanceof ISwipeGestureListener) {
            Utils.logDebug(TAG, "clearView() viewHolder:" + viewHolder + " MENU CLEARED");
            ((ISwipeGestureListener) viewHolder).onSwipeMenuCleared();
        }
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        Utils.logDebug(TAG, "onMoved() source holder:" + viewHolder + " target holder:" + target);
        return false;
    }
}