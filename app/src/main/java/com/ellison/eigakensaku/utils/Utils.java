package com.ellison.eigakensaku.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.ellison.eigakensaku.beans.Movie;
import com.ellison.eigakensaku.beans.MovieList;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class Utils {
    private static ProgressDialog mProgressDialog;

    public static void showAlertDialog(Context context, int resId) {
        if (context != null) {
            showAlertDialog(context, context.getResources().getString(resId));
        }
    }

    public static void showAlertDialog(Context context, String message) {
        if (!((Activity) context).isFinishing()) {
            AlertDialog dialog = new AlertDialog.Builder(context)
                    .setMessage(message)
                    .setPositiveButton(android.R.string.ok, (dialog1, which) -> dialog1.dismiss())
                    .create();
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();
        }
    }

    public static void showProgressDialog(Context context, int resId) {
        if (context == null) {
            return;
        }
        showProgressDialog(context, context.getResources().getString(resId));
    }

    public static void showProgressDialog(Context context, String progressMessage) {
        if (context == null) {
            return;
        }

        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setTitle(progressMessage);
            mProgressDialog.show();
        } else {
            if (mProgressDialog.isShowing()){
                mProgressDialog.dismiss();
            }
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setTitle(progressMessage);
            mProgressDialog.show();
        }
    }

    public static void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public static DisplayImageOptions getImageOptions(int defaultIconId) {
        return getImageOptions(defaultIconId, 0);
    }

    public static DisplayImageOptions getImageOptions(int defaultIconId, int cornerRadiusPixels) {
        return new DisplayImageOptions.Builder()
                .displayer(new RoundedBitmapDisplayer(cornerRadiusPixels))
                .showImageOnLoading(defaultIconId)
                .showImageOnFail(defaultIconId)
                .showImageForEmptyUri(defaultIconId)
                .cacheInMemory(true)
                .cacheOnDisc()
                .build();
    }

    public static MovieList makeFakeList() {
        MovieList fake = new MovieList();

        for (int i = 0; i < 10; i++) {
            Movie movie = new Movie();
            movie.setTitle("Harry Potter " + (i + 1));
            if (i == 1 || i ==3) {
                movie.setPoster("https://cn.bing.com/th?id=OIP.fkcuaLhZJevCj6ja5lpm1wHaJv&pid=Api&rs=1");
            } else {
                movie.setPoster("https://i.ebayimg.com/images/i/291520000466-0-1/s-l1000.jpg");
            }
            movie.setYear(String.valueOf(i + 2000));
            movie.setType("Magical");
            fake.add(movie);
        }

        return  fake;
    }

    public static void recycleProgressDialog() {
        mProgressDialog = null;
    }
}