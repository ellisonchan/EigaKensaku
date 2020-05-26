package com.ellison.eigakensaku.ui.view;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import com.ellison.eigakensaku.R;

public class LoadingHodler extends RecyclerView.ViewHolder {
    @BindView(R.id.progress_layout)
    LinearLayout loadinglayout;
    @BindView(R.id.progress_bar)
    ProgressBar progressbar;
    @BindView(R.id.load_cd)
    TextView loadingdes;

    public LoadingHodler(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}