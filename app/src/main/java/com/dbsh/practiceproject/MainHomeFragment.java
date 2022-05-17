package com.dbsh.practiceproject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Calendar;

public class MainHomeFragment extends Fragment {
    private static final String timetableURL = "https://sportal.skuniv.ac.kr/sportal/common/selectList.sku";

    userClass userClass;

    private ViewPager2 mPager;
    private FragmentStateAdapter pagerAdapter;
    private int num_page = 3;

    ArrayList<String> todayList;
    Calendar cal = Calendar.getInstance();
    String token, id, year, term;

    Bundle bundle1, bundle2, bundle3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.main_home_form, container, false);
        userClass = ((userClass) getActivity().getApplication());

        token = userClass.getToken();
        id = userClass.getId();
        year = userClass.getSchYear();
        term = userClass.getSchTerm();

        bundle1 = new Bundle();

        String college_major = userClass.getColName() + "\n" + userClass.getDeptName();
        String stu_info = userClass.getId() + " " + userClass.getKorName();
        String mail_addr = userClass.getEmailAddress();
        String mentor_name = userClass.getTutorName() + " 멘토";
        String haknyun_text = userClass.getSchYR() + "학년";

        bundle1.putString("cm", college_major);
        bundle1.putString("si", stu_info);
        bundle1.putString("ma", mail_addr);
        bundle1.putString("mn", mentor_name);
        bundle1.putString("ht", haknyun_text);

        mPager = rootView.findViewById(R.id.viewpager);
        pagerAdapter = new CardAdapter(getActivity(), num_page, bundle1);
        mPager.setAdapter(pagerAdapter);
        mPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        mPager.setCurrentItem(0);
        mPager.setOffscreenPageLimit(3);

        mPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                if (positionOffsetPixels == 0) {
                    mPager.setCurrentItem(position);
                }
            }
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }
        });
        return rootView;
    }
}