package com.ellison.eigakensaku.view;

import android.content.Context;
import android.util.AttributeSet;

import com.ellison.eigakensaku.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

public class MovieCard extends CardView {
    public MovieCard(@NonNull Context context) {
        this(context, null);
    }

    public MovieCard(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MovieCard(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
//        setRadius(15);
//        setCardElevation(8);
//        setContentPadding(5,5,5,5);
    }
}
