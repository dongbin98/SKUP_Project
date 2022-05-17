package com.dbsh.practiceproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class CardAdapter extends FragmentStateAdapter {
    public int mCount;
    Bundle bundle1, bundle2, bundle3;
    public CardAdapter(FragmentActivity fa, int count, Bundle bundle1) {
        super(fa);
        mCount = count;
        this.bundle1 = bundle1;
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(getRealPosition(position) == 0) {
            MainHomeCardFragment1 fg = new MainHomeCardFragment1();
            fg.setArguments(bundle1);
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
