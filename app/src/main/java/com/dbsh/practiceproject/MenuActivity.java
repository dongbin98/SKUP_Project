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
    
    Button gradeBtn;        // 성적
    Button Gradeinfo;       // 학기별 성적조회
    
    Button registBtn;       // 등록
    Button Tuitioninfo;     // 등록금고지서 및 납부확인
    
    Button scholarBtn;      // 장학
    
    Button graduateBtn;     // 졸업
    Button Graduateinfo;    // 졸업요건 취득확인
    
    Button nonLectureBtn;   // 비교과프로그램
    Button normalpoteninfo; // 일반역량강화프로그램
    Button majorpoteninfo;  // 전공역량강화프로그램
    
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

            }
        });
        
        // 성적
        gradeBtn = (Button) findViewById(R.id.gradeBtn);
        gradeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Gradeinfo.getVisibility() == View.GONE) {
                    Gradeinfo.setVisibility(View.VISIBLE);
                }
                else {
                    Gradeinfo.setVisibility(View.GONE);
                }
            }
        });
        // 성적 - 학기별 성적조회
        Gradeinfo = (Button) findViewById(R.id.Gradeinfo);
        Gradeinfo.setOnClickListener(new View.OnClickListener() {
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
                if(Tuitioninfo.getVisibility() == View.GONE) {
                    Tuitioninfo.setVisibility(View.VISIBLE);
                }
                else {
                    Tuitioninfo.setVisibility(View.GONE);
                }
            }
        });
        // 등록 - 등록금고지서 및 납부확인
        Tuitioninfo = (Button) findViewById(R.id.Tuitioninfo);
        Tuitioninfo.setOnClickListener(new View.OnClickListener() {
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
                if(Graduateinfo.getVisibility() == View.GONE) {
                    Graduateinfo.setVisibility(View.VISIBLE);
                }
                else {
                    Graduateinfo.setVisibility(View.GONE);
                }
            }
        });
        // 졸업요건 취득확인
        Graduateinfo = (Button) findViewById(R.id.Graduateinfo);
        Graduateinfo.setOnClickListener(new View.OnClickListener() {
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
                if(normalpoteninfo.getVisibility() == View.GONE) {
                    normalpoteninfo.setVisibility(View.VISIBLE);
                    majorpoteninfo.setVisibility(View.VISIBLE);
                }
                else {
                    normalpoteninfo.setVisibility(View.GONE);
                    majorpoteninfo.setVisibility(View.GONE);
                }
            }
        });
        // 비교과 - 일반역량강화프로그램
        normalpoteninfo = (Button) findViewById(R.id.Normalpoteninfo);
        normalpoteninfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent normalpotenIntent = new Intent(MenuActivity.this, NormalpotenActivity.class);
                startActivity(normalpotenIntent);
            }
        });
        // 비교과 - 전공역량강화프로그램
        majorpoteninfo = (Button) findViewById(R.id.Majorpoteninfo);
        majorpoteninfo.setOnClickListener(new View.OnClickListener() {
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
                // 등록금 페이지로 넘어가기
                Intent tuitionIntent = new Intent(MenuActivity.this, QRActivity.class);
                startActivity(tuitionIntent);
            }
        });
    }
}
