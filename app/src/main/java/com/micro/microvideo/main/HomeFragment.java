package com.micro.microvideo.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.micro.microvideo.R;
import com.micro.microvideo.base.SingleFragment;
import com.micro.microvideo.http.ApiCallback;
import com.micro.microvideo.main.bean.MicroBean;
import com.micro.microvideo.main.view.HeadBanner;
import com.micro.microvideo.util.ItemOffsetDecoration;
import com.micro.microvideo.util.MarginAllDecoration;
import com.micro.microvideo.util.ZRecyclerView.ZRecyclerView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by William on 2018/5/30.
 */

public class HomeFragment extends SingleFragment{
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.progress)
    ProgressBar progress;

    @BindView(R.id.recycler_view)
    ZRecyclerView recyclerView;
    CommonAdapter<MicroBean> mAdapter;
    List<MicroBean> mList;

    public static HomeFragment newInstance() {
        
        Bundle args = new Bundle();

        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    @Override
    protected ApiCallback setApiCallback() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initEventAndData(View view) {
        title.setText("影片体验区");
        progress.setVisibility(View.GONE);
        mList = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            mList.add(new MicroBean("http://mm.chinasareview.com/wp-content/uploads/2017a/04/19/02.jpg", "测试"));
        }
        recyclerView.setLayoutManager(new GridLayoutManager(mContext,2));
        mAdapter = new CommonAdapter<MicroBean>(mContext,R.layout.adapter_home, mList) {
            @Override
            protected void convert(ViewHolder holder, MicroBean microBean, int position) {
                holder.setText(R.id.text, microBean.getName());
                Glide.with(mActivity).load(microBean.getImgurl()).error(R.drawable.ic_default_image).into((ImageView) holder.getView(R.id.cover));
                holder.setOnClickListener(R.id.cover, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(mActivity,DetailActivity.class));
                    }
                });
            }
        };

        recyclerView.setPullRefreshEnabled(false);
        recyclerView.setNoMore(true);
        recyclerView.setAdapter(mAdapter);

        HeadBanner banner = new HeadBanner(mContext,null);
        banner.setBanner(mList);
        recyclerView.addHeaderView(banner);
    }
}
