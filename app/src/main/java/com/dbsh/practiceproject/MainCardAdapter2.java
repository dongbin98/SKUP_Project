package com.dbsh.practiceproject;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MainCardAdapter2 extends FragmentStateAdapter {
    public int mCount;
    public MainCardAdapter2(FragmentActivity fa, int count) {
        super(fa);
        mCount = count;
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(getRealPosition(position) == 0) {
            MainHomeCardFragment4 fg = new MainHomeCardFragment4();
            return fg;
        }
        else if(getRealPosition(position) == 1) {
            MainHomeCardFragment5 fg = new MainHomeCardFragment5();
            return fg;
        }
        else {
            MainHomeCardFragment6 fg = new MainHomeCardFragment6();
            return fg;
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public int getRealPosition(int position) {return position % mCount;}
}
