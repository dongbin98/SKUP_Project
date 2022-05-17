package com.dbsh.practiceproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MainHomeCardFragment6 extends Fragment {
    TextView main_home_notice_title, main_home_notice_text;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.main_home_card6_form, container, false);
        main_home_notice_title = (TextView) rootView.findViewById(R.id.main_home_notice_title3);
        main_home_notice_text = (TextView) rootView.findViewById(R.id.main_home_notice_text3);

        return rootView;
    }
}
