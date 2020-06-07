package com.ellison.eigakensaku.myimageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class DisplayFromNetwork {
    DisplayFromDisk mDisk;
    DisplayFromMemory mMemory;

    HandlerThread mLoadWorker;
    Handler mLoadHandler;
    Handler mMainHandler;

//    LoadImageTask mLoadImageTask;
//    DisplayImageTask mDisplayImageTask;

    public DisplayFromNetwork(DisplayFromMemory memory, DisplayFromDisk disk, Context context) {
        mDisk = disk;
        mMemory = memory;

        mLoadWorker = new HandlerThread("Load Image");
        mLoadWorker.start();
        mLoadHandler = new Handler(mLoadWorker.getLooper());
        mMainHandler = new Handler();
    }

    public void load(final String url, ImageView imageView) {
        if (mLoadWorker != null && mLoadHandler != null) {
            LoadImageTask loadImageTask = new LoadImageTask();
            DisplayImageTask displayImageTask = new DisplayImageTask();

            loadImageTask.setUrl(url);
            displayImageTask.setStringAndView(url, imageView);
            loadImageTask.setDisplayImageTask(displayImageTask);

            mLoadHandler.post(loadImageTask);
        }
    }

    private final class LoadImageTask implements Runnable {
        private String url;
        private DisplayImageTask displayTask;

        public void setUrl(String url) {
            this.url = url;
        }

        public void setDisplayImageTask(DisplayImageTask displayTask) {
            this.displayTask = displayTask;
        }

        @Override
        public void run() {
            Bitmap bitmap = null;
            HttpURLConnection connection = null;

            try {
                connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(500);
                connection.setRequestMethod("GET");

                if (connection.getResponseCode() == 200) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 1;
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    Log.e("ellison2021", "start input stream to bitmap");
                    bitmap = BitmapFactory.decodeStream(connection.getInputStream(), null, options);
                }

            } catch (IOException e) {
                Log.e("ellison2021", "e:" + e);
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }

            displayTask.passBitmap(bitmap);
            mMainHandler.post(displayTask);
        }
    }

    private final class DisplayImageTask implements Runnable {
        private Bitmap bitmap;
        private ImageView imageView;
        private String url;

        public void passBitmap(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        public void setStringAndView(String url, ImageView imageView) {
            this.url = url;
            this.imageView = imageView;
        }

        @Override
        public void run() {
            if (bitmap != null) {
                if (imageView != null) {
                    Log.e("ellison2021", "bitmap to imageview");
                    imageView.setImageBitmap(bitmap);
                }

                if (mMemory != null) {
                    Log.e("ellison2021", "bitmap to memory");
                    mMemory.put(url, bitmap);
                }

                if (mDisk != null) {
                    Log.e("ellison2021", "bitmap to disk");
                    mDisk.put(url, bitmap);
                }
            }
        }
    }
}
