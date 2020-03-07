package com.ellison.eigakensaku.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

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
