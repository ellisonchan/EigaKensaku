package com.ellison.eigakensaku.ui.info;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import butterknife.BindView;

import com.ellison.eigakensaku.R;
import com.ellison.eigakensaku.ui.base.BaseFragment;

public class InfoFragment extends BaseFragment {
    @BindView(R.id.text_info)
    TextView mSettings;

     public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
         View root = inflater.inflate(R.layout.fragment_info, container, false);
         return root;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        if(mSettings != null) {
            mSettings.setText(R.string.settings_info);
        }
    }
}