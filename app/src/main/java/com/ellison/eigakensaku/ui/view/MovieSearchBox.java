package com.ellison.eigakensaku.ui.view;

import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;

import com.ellison.eigakensaku.ui.search.SearchFragment;

import java.lang.ref.WeakReference;

import androidx.appcompat.widget.AppCompatEditText;

public class MovieSearchBox extends AppCompatEditText {
    private static final String TAG = MovieSearchBox.class.getSimpleName();

    public MovieSearchBox(Context context) {
        this(context, null);
    }

    public MovieSearchBox(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MovieSearchBox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        return super.onSaveInstanceState();
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
    }
}
