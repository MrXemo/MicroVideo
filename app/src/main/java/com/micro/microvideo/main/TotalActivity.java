package com.micro.microvideo.main;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.gxz.PagerSlidingTabStrip;
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
    @BindView(R.id.order_pager_tab)
    PagerSlidingTabStrip mPagerSlidingTabStrip;
    List<Fragment> mFragments;
    List<MicroBean> mCategory;
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
        for (MicroBean microBean : mCategory) {
            mFragments.add(TotalFragment.newInstance(microBean.getId()));
            mCategoryStrs.add(microBean.getName());
        }

        mViewPager.setAdapter(new CommonPagerAdapter(getSupportFragmentManager(),mFragments, mCategoryStrs));
        mViewPager.setOffscreenPageLimit(mFragments.size());
        mPagerSlidingTabStrip.setViewPager(mViewPager);
    }
}
