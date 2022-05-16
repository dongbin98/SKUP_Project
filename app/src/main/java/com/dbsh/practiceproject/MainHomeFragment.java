package com.dbsh.practiceproject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainHomeFragment extends Fragment {

    TextView main_korName;
    TextView main_college;
    TextView main_webMail;
    userClass userClass;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.main_home_form, container, false);
        userClass = ((userClass) getActivity().getApplication());
        main_korName = (TextView) rootView.findViewById(R.id.main_korName);
        main_college = (TextView) rootView.findViewById(R.id.main_college);
        main_webMail = (TextView) rootView.findViewById(R.id.main_webmail);

        main_korName.setText(userClass.getKorName());
        main_college.setText(userClass.getColName() + " " + userClass.getDeptName() + " | " + userClass.getSchYR() + "학년 | " + "멘토교수:" + userClass.getTutorName());
        main_webMail.setText(userClass.getWebmailAddress());

        return rootView;
    }
}