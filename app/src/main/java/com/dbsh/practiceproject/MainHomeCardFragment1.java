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

    userClass userClass;

    TextView college_major;
    TextView stu_info;
    TextView mail_addr;
    TextView mentor_name;
    TextView haknyun_text;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.main_home_card1_form, container, false);

        college_major = rootView.findViewById(R.id.card1_college_major);
        stu_info = rootView.findViewById(R.id.card1_stu_info);
        mail_addr = rootView.findViewById(R.id.card1_mail_addr);
        mentor_name = rootView.findViewById(R.id.card1_mentor_name);
        haknyun_text = rootView.findViewById(R.id.card1_haknyun_text);

        userClass = ((userClass) getActivity().getApplication());

        String cm = userClass.getColName() + "\n" + userClass.getDeptName();
        String si = userClass.getId() + " " + userClass.getKorName();
        String ma = userClass.getEmailAddress();
        String mn = userClass.getTutorName() + " 멘토";
        String ht = userClass.getSchYR() + "학년";

        college_major.setText(cm);
        stu_info.setText(si);
        mail_addr.setText(ma);
        mentor_name.setText(mn);
        haknyun_text.setText(ht);

        return rootView;
    }
}
