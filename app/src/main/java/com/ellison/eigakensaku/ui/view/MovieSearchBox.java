package com.ellison.eigakensaku.ui.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

public class MovieSearchBox extends AppCompatEditText {
    public MovieSearchBox(Context context) {
        this(context, null);
    }

    public MovieSearchBox(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MovieSearchBox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
}
