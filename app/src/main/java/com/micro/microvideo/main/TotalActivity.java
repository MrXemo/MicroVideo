package com.micro.microvideo.main;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.micro.microvideo.R;
import com.micro.microvideo.base.SimpleActivity;
import com.micro.microvideo.main.bean.MicroBean;
import com.micro.microvideo.main.view.CommonPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by William on 2018/8/9.
 */

public class TotalActivity extends SimpleActivity {
    @BindView(R.id.order_viewpager)
    ViewPager mViewPager;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    List<Fragment> mFragments;
    List<MicroBean> mCategory;
    int position;
    List<String> mCategoryStrs;

    @Override
    protected int getLayout() {
        return R.layout.activity_total;
    }

    @Override
    protected void initEventAndData() {
        initToolBar("全部高清影片");

        mFragments = new ArrayList<>();
        mCategoryStrs = new ArrayList<>();
        mCategory = getIntent().getParcelableArrayListExtra("category");
        position = getIntent().getIntExtra("position",0);
        for (MicroBean microBean : mCategory) {
            mFragments.add(TotalFragment.newInstance(microBean.getId()));
            mCategoryStrs.add(microBean.getName());
        }
        mViewPager.setAdapter(new CommonPagerAdapter(getSupportFragmentManager(),mFragments, mCategoryStrs));
        mViewPager.setCurrentItem(position);
        mViewPager.setOffscreenPageLimit(mFragments.size());
        mTabLayout.setupWithViewPager(mViewPager);
    }
}
