package com.myprog.sportlife.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.myprog.sportlife.Adapter.ScreenSlidePageFragment;
import com.myprog.sportlife.R;

public class ActivityFragment extends Fragment {

    Context myContext;
    View view;
    ViewPager2 viewPager;
    ScreenSlidePageFragment pagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_activity, container, false);
        init();
        return view;
    }

    public void init(){
        viewPager = view.findViewById(R.id.fragmentActivity_ViewPager);
        pagerAdapter = new ScreenSlidePageFragment(this);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(100, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myContext = (FragmentActivity)context;
    }
}
