package com.ellison.eigakensaku.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import androidx.annotation.Nullable;

public class DisplayFromDisk {
    private DiskLruCache mCache;

    public DisplayFromDisk(Context context) {
        initDiskCache(context);
    }

    // 10MB
    private static final int MAX_SIZE = 10 * 1024 * 1024;

    // context.getExternalCacheDir():/storage/emulated/0/Android/data/com.ellison.eigakensaku/cache
    // context.getCacheDir():/data/user/0/com.ellison.eigakensaku/cache
    // Environment.getExternalStorageDirectory():/storage/emulated/0
    private File getCacheDir(Context context, final String dir) {
        Log.e("ellison2021", "context.getExternalCacheDir():" + context.getExternalCacheDir());
        Log.e("ellison2021", "context.getCacheDir():" + context.getCacheDir());
//        Log.e("ellison2021", "context.getExternalStorageDirectory():" + Environment.getExternalStorageDirectory());

        final String path = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED ? context.getExternalCacheDir().getPath() : context.getCacheDir().getPath();
        return new File(path + File.separator + dir);
    }

    private String hashKeyForDisk(String key) {
        String hashedKey = "";
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(key.getBytes());
            hashedKey = byteToHex(digest.digest());
        } catch (NoSuchAlgorithmException e) {
            Log.e("ellison2021", "key hashed e:" + e);
        }
        return hashedKey;
    }

    private String byteToHex(byte[] bytes) {
        StringBuilder output = new StringBuilder("");

        for (byte b : bytes) {
            String hex = Integer.toHexString(b & 0xFF);

            // When value < 16 put 0 to second index
            if (hex.length() == 1) {
                output.append("0");
            }
            output.append(hex);
        }

        return output.toString();
    }

    private void initDiskCache(Context context) {
        if (mCache == null || mCache.isClosed()) {
            try {
                File cacheDir = getCacheDir(context, "cacheDir");
                if (!cacheDir.exists()) {
                    cacheDir.mkdir();
                }

                mCache = DiskLruCache.open(cacheDir, 1, 1, MAX_SIZE);
            } catch (IOException e) {
                Log.e("ellison2021", "initDiskCache e:" + e);
            }
        }
    }

    @Nullable
    public Bitmap get(String url) {
        String hashedKey = hashKeyForDisk(url);
        Log.e("ellison2021", "get with key:" + hashedKey);

        if (mCache != null) {
            try {
                DiskLruCache.Snapshot snapshot = mCache.get(hashedKey);
                if (snapshot != null) {
                    InputStream inputStream = snapshot.getInputStream(0);
                    return  BitmapFactory.decodeStream(inputStream);
                }
            } catch (IOException e) {
                Log.e("ellison2021", "get bitmap from file with e:" + e);
            }
        }

        return null;
    }

    public void put(String url, Bitmap bitmap) {
        String hashedKey = hashKeyForDisk(url);
        Log.e("ellison2021", "put with key:" + hashedKey);

        DiskLruCache.Editor editor = null;
        OutputStream outputStream = null;

        try {
            editor = mCache.edit(hashedKey);
            outputStream = editor.newOutputStream(0);

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

            editor.commit();
            mCache.flush();
        } catch (IOException e) {
            Log.e("ellison2021", "bitmap to file e:" + e);
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                    outputStream.flush();
                }
            } catch (IOException e) {

            }

        }
    }
}
