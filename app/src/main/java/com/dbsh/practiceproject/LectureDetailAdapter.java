package com.dbsh.practiceproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class LectureDetailAdapter extends FragmentStateAdapter {
    public int mCount;
    public Bundle bundle;
    public LectureDetailAdapter(FragmentActivity fa, int count, Bundle bundle) {
        super(fa);
        mCount = count;
        this.bundle = bundle;
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(getRealPosition(position) == 0) {
            LecturePlanDetailInfoFragment fg = new LecturePlanDetailInfoFragment();
            fg.setArguments(bundle);
            return fg;
        }
        else  {
            LecturePlanDetailWeekFragment fg = new LecturePlanDetailWeekFragment();
            fg.setArguments(bundle);
            return fg;
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public int getRealPosition(int position) {return position % mCount;}
}
