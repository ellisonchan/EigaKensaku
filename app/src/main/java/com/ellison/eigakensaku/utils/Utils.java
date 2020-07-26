package com.ellison.eigakensaku.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.ellison.eigakensaku.beans.Movie;
import com.ellison.eigakensaku.beans.MovieList;
import com.ellison.eigakensaku.constants.Constants;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Utils {
    private static final String TAG = Utils.class.getSimpleName();

    private static ProgressDialog mProgressDialog;

    public static void showAlertDialog(Context context, int resId) {
        if (context != null) {
            showAlertDialog(context, context.getResources().getString(resId));
        }
    }

    public static void showAlertDialog(Context context, String message) {
        if (context == null) {
            return;
        }

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
                movie.setPoster("https://upload-images.jianshu.io/upload_images/7048342-f49955b4e4d2f3d7.png?imageMogr2/auto-orient/strip|imageView2/2/w/762");
            } else {
                movie.setPoster("https://oimagea3.ydstatic.com/image?id=-7532960978143817448&product=adpublish&w=520&h=347");
            }
            movie.setYear(String.valueOf(i + 2000));
            movie.setType("Magical");
            movie.setID("tt0098635");
            fake.add(movie);
        }

        return  fake;
    }

    public static void recycleProgressDialog() {
        mProgressDialog = null;
    }

    public static void logError(String tag, String message) {
        Log.e(tag, message);
    }

    public static void logError(String tag, String message, Throwable throwable) {
        Log.e(tag, message, throwable);
    }

    public static void logDebug(String tag, String message) {
        if (Constants.DEBUGGABLE) {
            Log.d(tag, message);
        }
    }

    public static void logDebug(String tag, String message, Throwable throwable) {
        if (Constants.DEBUGGABLE) {
            Log.d(tag, message, throwable);
        }
    }

    public static void saveMoviesToFile(MovieList list, Context context) {
        File savedFile = new File(context.getFilesDir() + File.separator + Constants.MOVIES_SAVED_SERIALED_FILE);

        ObjectOutputStream stream = null;
        try {
            stream = new ObjectOutputStream(new FileOutputStream(savedFile));
            stream.writeObject(list);
        } catch (FileNotFoundException e) {
            logError(TAG, "saveMoviesToFile FileNotFoundException:" +  e);
        } catch (InvalidClassException e) {
            logError(TAG, "saveMoviesToFile InvalidClassException:" +  e);
        } catch (NotSerializableException e) {
            logError(TAG, "saveMoviesToFile NotSerializableException:" +  e);
        } catch (IOException e) {
            logError(TAG, "saveMoviesToFile IOException:" +  e);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public static MovieList readMoviesFromFile(Context context) {
        File savedFile = new File(context.getFilesDir() + File.separator + Constants.MOVIES_SAVED_SERIALED_FILE);

        MovieList list = null;
        ObjectInputStream stream = null;
        try {
            stream = new ObjectInputStream(new FileInputStream(savedFile));
            list = (MovieList) stream.readObject();
        } catch (FileNotFoundException e) {
            logError(TAG, "readMoviesFromFile FileNotFoundException:" +  e);
        } catch (ClassNotFoundException e) {
            logError(TAG, "readMoviesFromFile ClassNotFoundException:" +  e);
        } catch (NotSerializableException e) {
            logError(TAG, "readMoviesFromFile NotSerializableException:" +  e);
        } catch (IOException e) {
            logError(TAG, "readMoviesFromFile IOException:" +  e);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                }
            }
        }
        return list;
    }
}