package com.micro.microvideo.main.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by William on 2018/8/10.
 */

public class CommonPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragments;
    private List<String> mStringList;

    public CommonPagerAdapter(FragmentManager fm, List<Fragment> fragments, List<String> strings) {
        super(fm);
        mFragments = fragments;
        mStringList = strings;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mStringList.get(position);
    }
}
