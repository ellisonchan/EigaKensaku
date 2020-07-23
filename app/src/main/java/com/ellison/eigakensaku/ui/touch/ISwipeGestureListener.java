package com.ellison.eigakensaku.ui.touch;

public interface ISwipeGestureListener {
    void onSwipeTipShowing(float dX, float dY);
    void onSwipeMenuShowing(ISwipeMenuCallback callback);
    void onSwipeMenuSelected();
    void onSwipeMenuCleared();
    void onDragOver(int targetPos);

    interface ISwipeMenuCallback{
        void onSwipeMenuShown();
    }
}
