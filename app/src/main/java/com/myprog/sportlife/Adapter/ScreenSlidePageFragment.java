package com.myprog.sportlife.Adapter;


import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.myprog.sportlife.fragment.DetailActivityFragment;

public class ScreenSlidePageFragment extends FragmentStateAdapter {
    public ScreenSlidePageFragment(Fragment fragment){
        super(fragment);
    }

    @Override
    public Fragment createFragment(int position) {
        DetailActivityFragment fr = new DetailActivityFragment();
        fr.setPosition(position);
        return fr;
    }

    @Override
    public int getItemCount() {
        return 200;
    }
}
