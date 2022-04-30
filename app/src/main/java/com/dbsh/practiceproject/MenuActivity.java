package com.dbsh.practiceproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {

    TextView korName;
    TextView college;
    TextView webMail;
    
    Button lectureBtn;      // 강의
    Button lecturePlanBtn;      // 강의 계획서
    Button lectureTimetableBtn; // 강의 시간표
    Button lectureEvaluationBtn;// 강의 평가
    Button attendanceBtn;       // 출결 조회
    
    Button gradeBtn;        // 성적
    Button gradeTermBtn;       // 학기별 성적조회
    
    Button registBtn;       // 등록
    Button tuitionInfoBtn;     // 등록금고지서 및 납부확인
    
    Button scholarBtn;      // 장학
    
    Button graduateBtn;     // 졸업
    Button graduateInfoBtn;    // 졸업요건 취득확인
    
    Button nonLectureBtn;   // 비교과프로그램
    Button n_potenInfoBtn;  // 일반역량강화프로그램
    Button m_potenInfoBtn;  // 전공역량강화프로그램
    
    Button proveBtn;        // 증명 및 발급
    
    Button QRinfo;          // 큐알코드 생성

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_form);

        Intent intent = getIntent();

        korName = (TextView) findViewById(R.id.korName);
        korName.setText(((userClass) getApplication()).getKorName());
        college = (TextView) findViewById(R.id.college);
        college.setText(((userClass) getApplication()).getColName() + " " +
                ((userClass) getApplication()).getDeptName() + " | " +
                ((userClass) getApplication()).getSchYR() + "학년 | " + "멘토교수:" +
                ((userClass) getApplication()).getTutorName());
        webMail = (TextView) findViewById(R.id.webmail);
        webMail.setText(((userClass) getApplication()).getWebmailAddress());
        
        // 강의
        lectureBtn = (Button) findViewById(R.id.lectureBtn);
        lectureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(lecturePlanBtn.getVisibility() == View.GONE) {
                    lecturePlanBtn.setVisibility(View.VISIBLE);
                    lectureTimetableBtn.setVisibility(View.VISIBLE);
                    lectureEvaluationBtn.setVisibility(View.VISIBLE);
                    attendanceBtn.setVisibility(View.VISIBLE);
                }
                else {
                    lecturePlanBtn.setVisibility(View.GONE);
                    lectureTimetableBtn.setVisibility(View.GONE);
                    lectureEvaluationBtn.setVisibility(View.GONE);
                    attendanceBtn.setVisibility(View.GONE);
                }
            }
        });
        // 강의 - 강의계획서
        lecturePlanBtn = (Button) findViewById(R.id.lecturePlanBtn);
        lecturePlanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent lecturePlanIntent = new Intent(MenuActivity.this, lecturePlanActivity.class);
                startActivity(lecturePlanIntent);
            }
        });
        // 강의 - 강의시간표
        lectureTimetableBtn = (Button) findViewById(R.id.lectureTimetableBtn);
        lectureTimetableBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        // 강의 - 강의평가
        lectureEvaluationBtn = (Button) findViewById(R.id.lectureEvaluationBtn);
        lectureEvaluationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        // 강의 - 출결조회
        attendanceBtn = (Button) findViewById(R.id.attendanceBtn);
        attendanceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        // 성적
        gradeBtn = (Button) findViewById(R.id.gradeBtn);
        gradeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(gradeTermBtn.getVisibility() == View.GONE) {
                    gradeTermBtn.setVisibility(View.VISIBLE);
                }
                else {
                    gradeTermBtn.setVisibility(View.GONE);
                }
            }
        });
        // 성적 - 학기별 성적조회
        gradeTermBtn = (Button) findViewById(R.id.gradeTermBtn);
        gradeTermBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gradeIntent = new Intent(MenuActivity.this, GradeActivity.class);
                startActivity(gradeIntent);
            }
        });

        // 등록
        registBtn = (Button) findViewById(R.id.registBtn);
        registBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tuitionInfoBtn.getVisibility() == View.GONE) {
                    tuitionInfoBtn.setVisibility(View.VISIBLE);
                }
                else {
                    tuitionInfoBtn.setVisibility(View.GONE);
                }
            }
        });
        // 등록 - 등록금고지서 및 납부확인
        tuitionInfoBtn = (Button) findViewById(R.id.tuitionInfoBtn);
        tuitionInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tuitionIntent = new Intent(MenuActivity.this, TuitionActivity.class);
                startActivity(tuitionIntent);
            }
        });

        // 장학
        scholarBtn = (Button) findViewById(R.id.scholarBtn);
        scholarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        
        // 졸업
        graduateBtn = (Button) findViewById(R.id.graduateBtn);
        graduateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(graduateInfoBtn.getVisibility() == View.GONE) {
                    graduateInfoBtn.setVisibility(View.VISIBLE);
                }
                else {
                    graduateInfoBtn.setVisibility(View.GONE);
                }
            }
        });
        // 졸업요건 취득확인
        graduateInfoBtn = (Button) findViewById(R.id.graduateInfoBtn);
        graduateInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent graduateIntent = new Intent(MenuActivity.this, GraduateActivity.class);
                startActivity(graduateIntent);
            }
        });

        // 비교과
        nonLectureBtn = (Button) findViewById(R.id.nonLectureBtn);
        nonLectureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(n_potenInfoBtn.getVisibility() == View.GONE) {
                    n_potenInfoBtn.setVisibility(View.VISIBLE);
                    m_potenInfoBtn.setVisibility(View.VISIBLE);
                }
                else {
                    n_potenInfoBtn.setVisibility(View.GONE);
                    m_potenInfoBtn.setVisibility(View.GONE);
                }
            }
        });
        // 비교과 - 일반역량강화프로그램
        n_potenInfoBtn = (Button) findViewById(R.id.n_potenInfoBtn);
        n_potenInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent normalpotenIntent = new Intent(MenuActivity.this, NormalpotenActivity.class);
                startActivity(normalpotenIntent);
            }
        });
        // 비교과 - 전공역량강화프로그램
        m_potenInfoBtn = (Button) findViewById(R.id.m_potenInfoBtn);
        m_potenInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent majorpotenIntent = new Intent(MenuActivity.this, MajorpotenActivity.class);
                startActivity(majorpotenIntent);
            }
        });
        
        // QR코드
        QRinfo = (Button) findViewById(R.id.QRinfo);
        QRinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 큐알 페이지로 넘어가기
                Intent qrIntent = new Intent(MenuActivity.this, QRActivity.class);
                startActivity(qrIntent);
            }
        });
    }
}
