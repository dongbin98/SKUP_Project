package com.dbsh.practiceproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

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

import me.relex.circleindicator.CircleIndicator3;

public class LecturePlanDetailActivity extends AppCompatActivity {
    String token, id, year, term, cd, cn, pi, sn;           // 토큰, 학번, 년, 학기, 학수번호, 분반, 교수명, 과목명

    TabLayout tabs;
    LecturePlanDetailInfoFragment infoFragment;
    LecturePlanDetailWeekFragment weekFragment;

    private ViewPager2 mPager;
    private FragmentStateAdapter pagerAdapter;
    private int num_page = 3;

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

        tabs = findViewById(R.id.lectureplan_detail_tabs);
        String[] titles = new String[] {"강의계획서 개요", "주차별 진도계획"};
        /*
        tabs.addTab(tabs.newTab().setText("강의계획서 개요"));
        tabs.addTab(tabs.newTab().setText("주차별 진도계획"));
         */

        mPager = findViewById(R.id.container);
        pagerAdapter = new LectureDetailAdapter(this, num_page, bundle);
        mPager.setAdapter(pagerAdapter);
        mPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        mPager.setCurrentItem(0);
        mPager.setOffscreenPageLimit(2);

        mPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                if (positionOffsetPixels == 0) {
                    mPager.setCurrentItem(position);
                }
            }
            @Override
            public void onPageSelected(int position) { super.onPageSelected(position); }
        });

        // new TabLayoutMediator(tabs, mPager, (tab, position) -> tab.setText(titles[position])).attach();
        // 아래와 동일한 표현
        new TabLayoutMediator(tabs, mPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(titles[position]);
            }
        }).attach();
    }
}