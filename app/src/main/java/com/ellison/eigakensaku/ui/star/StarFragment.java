package com.ellison.eigakensaku.ui.star;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import butterknife.BindView;

import com.ellison.eigakensaku.R;
import com.ellison.eigakensaku.ui.base.BaseFragment;

public class StarFragment extends BaseFragment {
    @BindView(R.id.text_star)
    TextView mWarn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_star, container, false);
        return root;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        if(mWarn != null) {
            mWarn.setText(R.string.warning_starred_movie_none);
        }
    }
}