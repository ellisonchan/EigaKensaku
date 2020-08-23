package com.ellison.eigakensaku.star;

import android.content.Context;

import com.ellison.eigakensaku.beans.Movie;
import com.ellison.eigakensaku.constants.Constants;
import com.ellison.eigakensaku.utils.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

public class StarSystem {
    private static final String TAG = StarSystem.class.getSimpleName();

    private static StarSystem sInstance = null;
    private LinkedHashSet<Movie> mCachedStarredMovie = null;
    private Context sContext = null;

    private StarSystem(Context context) {
        sContext = context;
        mCachedStarredMovie = readStarredMovie();
    }

    public static final StarSystem getInstance(Context context) {
        if (sInstance == null) {
            synchronized (StarSystem.class) {
                if (sInstance == null) {
                    sInstance = new StarSystem(context);
                }
            }
        }
        return sInstance;
    }

    public List<Movie> getStarredMovie() {
        Utils.logDebug(TAG, "getStarredMoive mCachedStarredMovie:" + mCachedStarredMovie);

        List<Movie> movieList = new ArrayList<>(mCachedStarredMovie);
        Collections.reverse(movieList);
        Utils.logDebug(TAG, "getStarredMoive movieList:" + movieList);
        return movieList;
    }

    public boolean isMoiveStarred(Movie movie) {
        Utils.logDebug(TAG, "isMoiveStarred movie:" + movie);
        return mCachedStarredMovie == null ? false : mCachedStarredMovie.contains(movie);
    }

    public void starMoive(Movie movie, boolean isStarred) {
        Utils.logDebug(TAG, "starMoive movie:" + movie + " isStarred:" + isStarred);
        if (mCachedStarredMovie != null) {
            boolean starringResult = isStarred ? mCachedStarredMovie.add(movie) : mCachedStarredMovie.remove(movie);
        } else {
            Utils.logError(TAG, "starMovie CACHED FILE NULL & NOTH");
        }
    }

    public void syncAndClearCache() {
        Utils.logError(TAG, "syncAndClearCache");
        syncStarSystem();
        clearCache();
    }

    private void syncStarSystem() {
        if (mCachedStarredMovie == null || mCachedStarredMovie.size() == 0) {
            Utils.logError(TAG, "syncStarSystem CACHED SET NULL/EMPTY & NOTH");
            return;
        }

        writeStarredMovie(mCachedStarredMovie);
    }

    private void clearCache() {
        Utils.logError(TAG, "clearCache");
        sInstance = null;
        mCachedStarredMovie = null;
        sContext = null;
    }

    private LinkedHashSet<Movie> readStarredMovie() {
        Utils.logDebug(TAG, "readStarredMovie sContext:" + sContext);
        if (sContext == null) {
            return null;
        }

        File starredFile = new File(sContext.getFilesDir() + File.separator + Constants.MOVIES_STARRED_SERIALED_FILE);
        ObjectInputStream objectInputStream = null;
        LinkedHashSet<Movie> readStarredMovie = null;

        try {
            objectInputStream = new ObjectInputStream(new FileInputStream(starredFile));
            readStarredMovie = (LinkedHashSet<Movie>) objectInputStream.readObject();
        } catch (IOException e) {
            Utils.logError(TAG, "readStarredMovie IOException:" + e);
        } catch (ClassNotFoundException e) {
            Utils.logError(TAG, "readStarredMovie ClassNotFoundException:" + e);
        } finally {
            try {
                if (objectInputStream != null) {
                    objectInputStream.close();
                }
            } catch (IOException e) {
            }
        }

        Utils.logDebug(TAG, "readStarredMovie RESULT:" + readStarredMovie);

        if (readStarredMovie == null) {
            readStarredMovie = new LinkedHashSet<Movie>(Constants.MOVIES_STARRED_SERIALED_SIZE_CAPACITY);
            Utils.logDebug(TAG, "readStarredMovie NULL & INIT RESULT:" + readStarredMovie);
        }

        return readStarredMovie;
    }

    private void writeStarredMovie(LinkedHashSet<Movie> movieSet) {
        Utils.logDebug(TAG, "writeStarredMovie movieSet:" + movieSet + " sContext:" + sContext);
        if (sContext == null) {
            return;
        }

        File starredFile = new File(sContext.getFilesDir() + File.separator + Constants.MOVIES_STARRED_SERIALED_FILE);
        ObjectOutputStream objectOutputStream = null;

        try {
            objectOutputStream = new ObjectOutputStream(new FileOutputStream(starredFile));
            objectOutputStream.writeObject(movieSet);
        } catch (IOException e) {
            Utils.logError(TAG, "writeStarredMovie IOException:" + e);
        } finally {
            try {
                if (objectOutputStream != null) {
                    objectOutputStream.close();
                }
            } catch (IOException e) {
            }
        }
    }
}
