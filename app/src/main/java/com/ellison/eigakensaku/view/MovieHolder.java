package com.ellison.eigakensaku.view;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ellison.eigakensaku.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tv_title)
    TextView title;
    @BindView(R.id.iv_post)
    ImageView post;
    @BindView(R.id.tv_year)
    TextView year;
    @BindView(R.id.tv_type)
    TextView type;

    public MovieHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
