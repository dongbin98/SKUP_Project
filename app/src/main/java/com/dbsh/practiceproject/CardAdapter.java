package com.dbsh.practiceproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class CardAdapter extends FragmentStateAdapter {
    public int mCount;
    Bundle bundle1, bundle2, bundle3;
    public CardAdapter(FragmentActivity fa, int count, Bundle bundle1, Bundle bundle2, Bundle bundle3) {
        super(fa);
        mCount = count;
        this.bundle1 = bundle1;
        this.bundle2 = bundle2;
        this.bundle3 = bundle3;
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        MainHomeCardFragment1 fg = new MainHomeCardFragment1();
        fg.setArguments(bundle1);
        return fg;
    }

    @Override
    public int getItemCount() {
        return 2000;
    }

    public int getRealPosition(int position) {return position % mCount;}
}
