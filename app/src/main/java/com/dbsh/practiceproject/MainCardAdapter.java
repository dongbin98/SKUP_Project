package com.dbsh.practiceproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MainCardAdapter extends FragmentStateAdapter {
    public int mCount;
    public MainCardAdapter(FragmentActivity fa, int count) {
        super(fa);
        mCount = count;
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(getRealPosition(position) == 0) {
            MainHomeCardFragment1 fg = new MainHomeCardFragment1();
            return fg;
        }
        else if(getRealPosition(position) == 1) {
            MainHomeCardFragment2 fg = new MainHomeCardFragment2();
            return fg;
        }
        else {
            MainHomeCardFragment3 fg = new MainHomeCardFragment3();
            return fg;
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public int getRealPosition(int position) {return position % mCount;}
}
