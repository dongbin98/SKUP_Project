package com.dbsh.practiceproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class TempPotenSelectActivity extends AppCompatActivity {
    Button temp_nPotenBtn;
    Button temp_mPotenBtn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temp_poten_select_form);
        Intent intent = getIntent();

        temp_nPotenBtn = (Button) findViewById(R.id.temp_nPotenBtn);
        temp_nPotenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TempPotenSelectActivity.this, NormalpotenActivity.class);
                startActivity(intent);
            }
        });
        // 비교과 - 전공역량강화프로그램
        temp_mPotenBtn = (Button) findViewById(R.id.temp_mPotenBtn);
        temp_mPotenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TempPotenSelectActivity.this, MajorpotenActivity.class);
                startActivity(intent);
            }
        });
    }
}
