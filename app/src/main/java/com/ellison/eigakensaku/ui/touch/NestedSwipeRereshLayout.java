package com.ellison.eigakensaku.ui.touch;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ellison.eigakensaku.utils.Utils;

public class NestedSwipeRereshLayout extends SwipeRefreshLayout {
    private static final String TAG = NestedSwipeRereshLayout.class.getSimpleName();

    private INestedScrollListener nestedScrollListener = null;
    private float mInitialX = 0f, mInitialY = 0f, mCurrentX = 0f, mCurrentY = 0f;
    private int mInitialDownId = MotionEvent.INVALID_POINTER_ID, SWIPE_THRESHOLD = 0;
    private VelocityTracker mVelocityTracker;

    public interface INestedScrollListener {
        void onScrollDown();
        void onScroll(float dx, float dy);
        void onScroll(float dx, float dy, float xVelocity, float yVelocity);
        void onScrollUpCancel();
    }

    public void injectNestedScrollListener(INestedScrollListener listener) {
        this.nestedScrollListener = listener;
    }

    public NestedSwipeRereshLayout(@NonNull Context context) {
        this(context, null);
        Utils.logDebug(TAG, "NestedSwipeRereshLayout context:" + context);
    }

    public NestedSwipeRereshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        SWIPE_THRESHOLD = ViewConfiguration.get(context).getScaledTouchSlop();
        Utils.logDebug(TAG, "NestedSwipeRereshLayout context:" + context + " attrs:" + attrs + " SWIPE_THRESHOLD:" + SWIPE_THRESHOLD);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Utils.logDebug(TAG + "-edison", "dispatchTouchEvent ev:" + ev);
        int actionIndex = MotionEvent.INVALID_POINTER_ID;

        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                Utils.logDebug(TAG + "-edison", "dispatchTouchEvent DOWN");
                mInitialDownId = ev.getPointerId(0);
                actionIndex = ev.findPointerIndex(mInitialDownId);
                mVelocityTracker = VelocityTracker.obtain();

                if (actionIndex < 0) {
                    Utils.logDebug(TAG + "-edison", "dispatchTouchEvent DOWN actionIndex:" + actionIndex);
                    break;
                }

                mInitialX = ev.getX(actionIndex);
                mInitialY = ev.getY(actionIndex);
                Utils.logDebug(TAG + "-edison", "dispatchTouchEvent DOWN initialDownId:" + mInitialDownId + " mInitialX:" + mInitialX + " mInitialY:" + mInitialY);

                if (nestedScrollListener != null) {
                    Utils.logDebug(TAG + "-edison", "dispatchTouchEvent DOWN & NOTIFY");
                    nestedScrollListener.onScrollDown();
                }

                break;
            case MotionEvent.ACTION_MOVE:
                Utils.logDebug(TAG + "-edison", "dispatchTouchEvent MOVE");
                actionIndex = ev.findPointerIndex(mInitialDownId);
                if (actionIndex < 0) {
                    Utils.logDebug(TAG + "-edison", "dispatchTouchEvent MOVE actionIndex:" + actionIndex);
                    break;
                }

                mVelocityTracker.addMovement(ev);
                // Set velocity units to pixels/s.
                mVelocityTracker.computeCurrentVelocity(1000);

                mCurrentX = ev.getX(actionIndex);
                mCurrentY = ev.getY(actionIndex);
                Utils.logDebug(TAG + "-edison", "dispatchTouchEvent MOVE initialDownId:" + mInitialDownId + " mCurrentX:" + mCurrentX + " mCurrentY:" + mCurrentY);

                // if (Math.abs(mCurrentY - mInitialY) >= SWIPE_THRESHOLD) {
                //     Utils.logDebug(TAG + "-edison", "dispatchTouchEvent MOVE OVER THRESHOLD");
                if (nestedScrollListener != null) {
                    Utils.logDebug(TAG + "-edison", "dispatchTouchEvent MOVE OVER THRESHOLD & NOTIFY:(" + (mCurrentX - mInitialX) + "," + (mCurrentY - mInitialY) + ")");
                    nestedScrollListener.onScroll(mCurrentX - mInitialX, mCurrentY - mInitialY, mVelocityTracker.getXVelocity(), mVelocityTracker.getYVelocity());
                }
                // }

                break;
            case MotionEvent.ACTION_UP:
                Utils.logDebug(TAG + "-edison", "dispatchTouchEvent UP & RESET");
                notifyScrollForUpCancel(MotionEvent.ACTION_UP);
//                actionIndex = ev.findPointerIndex(mInitialDownId);
//                if (actionIndex < 0) {
//                    Utils.logDebug(TAG + "-edison", "dispatchTouchEvent UP actionIndex:" + actionIndex);
//                    break;
//                }
//                mCurrentX = ev.getX(actionIndex);
//                mCurrentY = ev.getY(actionIndex);
//                Utils.logDebug(TAG + "-edison", "dispatchTouchEvent UP initialDownId:" + mInitialDownId + " mCurrentX:" + mCurrentX + " mCurrentY:" + mCurrentY);
                break;
            case MotionEvent.ACTION_CANCEL:
                Utils.logDebug(TAG + "-edison", "dispatchTouchEvent CANCEL & RESET");
                notifyScrollForUpCancel(MotionEvent.ACTION_CANCEL);
                break;
        }

        return super.dispatchTouchEvent(ev);
    }

    private void notifyScrollForUpCancel(int actionCode) {
        mInitialDownId = MotionEvent.INVALID_POINTER_ID;
        mInitialX = mInitialY = mCurrentX = mCurrentY = 0;
        if (nestedScrollListener != null) {
            Utils.logDebug(TAG + "-edison", "dispatchTouchEvent" + (actionCode == MotionEvent.ACTION_UP ? " UP" : " CANCEL")  + " & NOTIFY");
            nestedScrollListener.onScrollUpCancel();
        }
        if (mVelocityTracker != null) {
            mVelocityTracker.clear();
            mVelocityTracker.recycle();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Utils.logDebug(TAG + "-edison", "onInterceptTouchEvent ev:" + ev);
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean b) {
        Utils.logDebug(TAG, "requestDisallowInterceptTouchEvent b:" + b);
        super.requestDisallowInterceptTouchEvent(b);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        Utils.logDebug(TAG + "-edison", "onTouchEvent ev:" + ev);
        return super.onTouchEvent(ev);
    }

//    @Override
//    public void setRefreshing(boolean refreshing) {
//        Utils.logDebug(TAG, "setRefreshing refreshing:" + refreshing);
//        super.setRefreshing(refreshing);
//    }
//
//    @Override
//    public boolean isRefreshing() {
//        Utils.logDebug(TAG, "isRefreshing:" + super.isRefreshing());
//        return super.isRefreshing();
//    }
//
//    @Override
//    public void setDistanceToTriggerSync(int distance) {
//        Utils.logDebug(TAG, "setDistanceToTriggerSync distance:" + distance);
//        super.setDistanceToTriggerSync(distance);
//    }
//
//    @Override
//    public boolean canChildScrollUp() {
//        Utils.logDebug(TAG, "canChildScrollUp:" + super.canChildScrollUp());
//        return super.canChildScrollUp();
//    }
//
//    @Override
//    public int getNestedScrollAxes() {
//        Utils.logDebug(TAG, "getNestedScrollAxes");
//        return super.getNestedScrollAxes();
//    }
//
//    @Override
//    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
//        Utils.logDebug(TAG, "onNestedPreScroll target:" + target + " dx:" + dx + " dy:" + dy + " consumed:" + Arrays.toString(consumed));
//        super.onNestedPreScroll(target, dx, dy, consumed);
//    }
//
//    @Override
//    public void onNestedScrollAccepted(View child, View target, int axes) {
//        Utils.logDebug(TAG, "onNestedScrollAccepted child:" + child + " target:" + target + " axes:" + axes);
//        super.onNestedScrollAccepted(child, target, axes);
//    }
//
//    @Override
//    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
//        Utils.logDebug(TAG, "onStartNestedScroll child:" + child + " target:" + target + " nestedScrollAxes:" + nestedScrollAxes);
//        return super.onStartNestedScroll(child, target, nestedScrollAxes);
//    }
//
//    @Override
//    public boolean startNestedScroll(int axes) {
//        Utils.logDebug(TAG, "startNestedScroll axes:" + axes);
//        return super.startNestedScroll(axes);
//    }
//
//    @Override
//    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
//        Utils.logDebug(TAG, "onNestedScroll target:" + target + " dxConsumed:" + dxConsumed + " dyConsumed:" + dyConsumed + " dxUnconsumed:" + dxUnconsumed + " dyUnconsumed:" + dyUnconsumed);
//        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
//    }
//
//    @Override
//    public void stopNestedScroll() {
//        Utils.logDebug(TAG, "stopNestedScroll");
//        super.stopNestedScroll();
//    }
//
//    @Override
//    public void onStopNestedScroll(View target) {
//        Utils.logDebug(TAG, "onStopNestedScroll target:" + target);
//        super.onStopNestedScroll(target);
//    }
//
//    @Override
//    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
//        Utils.logDebug(TAG, "onNestedPreFling target:" + target + " velocityX:" + velocityX + " velocityY:" + velocityY);
//        return super.onNestedPreFling(target, velocityX, velocityY);
//    }
//
//    @Override
//    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
//        Utils.logDebug(TAG, "onNestedFling target:" + target + " velocityX:" + velocityX + " velocityY:" + velocityY + " consumed:" + consumed);
//        return super.onNestedFling(target, velocityX, velocityY, consumed);
//    }
//
//    @Override
//    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
//        Utils.logDebug(TAG, "dispatchNestedScroll dxConsumed:" + dxConsumed + " dyConsumed:" + dyConsumed + " dxUnconsumed:" + dxUnconsumed + " dyUnconsumed:" + dyUnconsumed + " offsetInWindow:" + Arrays.toString(offsetInWindow));
//        return super.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
//    }
//
//    @Override
//    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
//        Utils.logDebug(TAG, "dispatchNestedPreScroll dx:" + dx + " dy:" + dy + " consumed:" + Arrays.toString(consumed) + " offsetInWindow:" + Arrays.toString(offsetInWindow));
//        return super.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
//    }
//
//    @Override
//    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
//        Utils.logDebug(TAG, "dispatchNestedFling velocityX:" + velocityX + " velocityY:" + velocityY + " consumed:" + consumed);
//        return super.dispatchNestedFling(velocityX, velocityY, consumed);
//    }
//
//    @Override
//    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
//        Utils.logDebug(TAG, "dispatchNestedPreFling velocityX:" + velocityX + " velocityY:" + velocityY);
//        return super.dispatchNestedPreFling(velocityX, velocityY);
//    }
}