package com.dbsh.practiceproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class LecturePlanDetailActivity extends AppCompatActivity {
    String token, id, year, term, cd, cn, pi, sn;           // 토큰, 학번, 년, 학기, 학수번호, 분반, 교수명, 과목명

    TabLayout tabs;
    LecturePlanDetailInfoFragment infoFragment;
    LecturePlanDetailWeekFragment weekFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lectureplan_detail_form);

        Intent intent = getIntent();
        cd = intent.getStringExtra("cd");
        cn = intent.getStringExtra("cn");
        pi = intent.getStringExtra("pi");
        sn = intent.getStringExtra("sn");

        token = ((userClass) getApplication()).getToken();
        id = ((userClass) getApplication()).getId();
        year = ((userClass) getApplication()).getSchYear();
        term = ((userClass) getApplication()).getSchTerm();

        Bundle bundle = new Bundle();
        bundle.putString("token", token);
        bundle.putString("id", id);
        bundle.putString("year", year);
        bundle.putString("term", term);
        bundle.putString("cd", cd);
        bundle.putString("cn", cn);
        bundle.putString("pi", pi);
        bundle.putString("sn", sn);

        infoFragment = new LecturePlanDetailInfoFragment();
        infoFragment.setArguments(bundle);
        weekFragment = new LecturePlanDetailWeekFragment();
        weekFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().add(R.id.container, infoFragment).commit();
        tabs = findViewById(R.id.lectureplan_detail_tabs);
        tabs.addTab(tabs.newTab().setText("강의계획서 개요"));
        tabs.addTab(tabs.newTab().setText("주차별 진도계획"));

        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                Fragment selected = null;
                if (position == 0)
                    selected = infoFragment;
                else if (position == 1)
                    selected = weekFragment;
                getSupportFragmentManager().beginTransaction().replace(R.id.container, selected).commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    public void getDetailWeek(String token, String id, String year, String term, String cd, String cn) {

    }
}
