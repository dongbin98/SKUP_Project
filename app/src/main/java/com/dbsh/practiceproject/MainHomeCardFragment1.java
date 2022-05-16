package com.dbsh.practiceproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MainHomeCardFragment1 extends Fragment {
    TextView college_major;
    TextView stu_info;
    TextView mail_addr;
    TextView mentor_name;
    TextView haknyun_text;
    String cm, si, ma, mn, ht;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.main_home_card1_form, container, false);

        college_major = rootView.findViewById(R.id.college_major);
        stu_info = rootView.findViewById(R.id.stu_info);
        mail_addr = rootView.findViewById(R.id.mail_addr);
        mentor_name = rootView.findViewById(R.id.mentor_name);
        haknyun_text = rootView.findViewById(R.id.haknyun_text);

        if (getArguments() != null) {
            cm = getArguments().getString("cm");
            si = getArguments().getString("si");
            ma = getArguments().getString("ma");
            mn = getArguments().getString("mn");
            ht = getArguments().getString("ht");

            college_major.setText(cm);
            stu_info.setText(si);
            mail_addr.setText(ma);
            mentor_name.setText(mn);
            haknyun_text.setText(ht);
        }

        return rootView;
    }
}
