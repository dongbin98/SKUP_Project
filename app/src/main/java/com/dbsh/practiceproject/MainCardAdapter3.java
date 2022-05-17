package com.dbsh.practiceproject;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MainCardAdapter3 extends FragmentStateAdapter {
    public int mCount;
    public MainCardAdapter3(FragmentActivity fa, int count) {
        super(fa);
        mCount = count;
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(getRealPosition(position) == 0) {
            MainHomeCardFragment7 fg = new MainHomeCardFragment7();
            return fg;
        }
        else if(getRealPosition(position) == 1) {
            MainHomeCardFragment8 fg = new MainHomeCardFragment8();
            return fg;
        }
        else {
            MainHomeCardFragment9 fg = new MainHomeCardFragment9();
            return fg;
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public int getRealPosition(int position) {return position % mCount;}
}
