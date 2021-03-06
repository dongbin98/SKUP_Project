package com.dbsh.practiceproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class MainMenuFramgment extends Fragment {

    ImageButton main_menu_attendance_btn;
    ImageButton main_menu_timetable_btn;
    ImageButton main_menu_grade_btn;
    ImageButton main_menu_lecture_btn;
    ImageButton main_menu_scholarship_btn;
    ImageButton main_menu_regist_btn;
    ImageButton main_menu_graduate_btn;
    ImageButton main_menu_poten_btn;
    ImageButton main_menu_qr_btn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.main_menu_form, container, false);
        main_menu_attendance_btn = rootView.findViewById(R.id.main_menu_attendance_btn);
        main_menu_timetable_btn = rootView.findViewById(R.id.main_menu_timetable_btn);
        main_menu_grade_btn = rootView.findViewById(R.id.main_menu_grade_btn);
        main_menu_lecture_btn = rootView.findViewById(R.id.main_menu_lecture_btn);
        main_menu_scholarship_btn = rootView.findViewById(R.id.main_menu_scholarship_btn);
        main_menu_regist_btn = rootView.findViewById(R.id.main_menu_regist_btn);
        main_menu_graduate_btn = rootView.findViewById(R.id.main_menu_graduate_btn);
        main_menu_poten_btn = rootView.findViewById(R.id.main_menu_poten_btn);
        main_menu_qr_btn = rootView.findViewById(R.id.main_menu_qr_btn);

        main_menu_attendance_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AttendanceActivity.class);
                startActivity(intent);
            }
        });

        main_menu_timetable_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TimetableActivity.class);
                startActivity(intent);
            }
        });

        // ?????? ??????
        main_menu_grade_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), GradeActivity.class);
                startActivity(intent);
            }
        });

        // ?????? ??????
        main_menu_lecture_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LecturePlanActivity.class);
                startActivity(intent);
            }
        });

        // ?????? ??????
        main_menu_scholarship_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ScholarshipActivity.class);
                startActivity(intent);
            }
        });

        // ?????? ??????
        main_menu_regist_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TuitionActivity.class);
                startActivity(intent);
            }
        });

        // ?????? ??????
        main_menu_graduate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), GraduateActivity.class);
                startActivity(intent);
            }
        });

        // ????????? ??????
        main_menu_poten_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TempPotenSelectActivity.class);
                startActivity(intent);
            }
        });

        // QR ??????
        main_menu_qr_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), QRActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }
}