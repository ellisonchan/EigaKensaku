package com.ellison.eigakensaku.ui.view;

import android.graphics.Rect;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class MovieItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public MovieItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        // outRect.right = space;
        // outRect.left = space;
        outRect.bottom = space;
        outRect.top = space;
//        if(parent.getChildAdapterPosition(view) == 0){
//            outRect.top = space;
//        }
    }
}
