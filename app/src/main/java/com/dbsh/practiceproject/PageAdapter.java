package com.dbsh.practiceproject;

import android.graphics.pdf.PdfDocument;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class PageAdapter extends FragmentPagerAdapter {
    int number;
    Bundle bundle;
    public PageAdapter(@NonNull FragmentManager fm, Bundle bundle, int number) {
        super(fm);
        this.bundle = bundle;
        this.number = number;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                LecturePlanDetailInfoFragment frag1 = new LecturePlanDetailInfoFragment();
                frag1.setArguments(bundle);
                return frag1;
            case 1:
                LecturePlanDetailWeekFragment frag2 = new LecturePlanDetailWeekFragment();
                frag2.setArguments(bundle);
                return frag2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return number;
    }
}
