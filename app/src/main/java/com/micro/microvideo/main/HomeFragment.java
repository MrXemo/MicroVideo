package com.micro.microvideo.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.micro.microvideo.R;
import com.micro.microvideo.base.ListFragment;
import com.micro.microvideo.http.ApiListCallback;
import com.micro.microvideo.main.bean.VideoBean;
import com.micro.microvideo.main.view.HeadBanner;
import com.micro.microvideo.util.RxBus;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import butterknife.BindView;

/**
 * Created by William on 2018/5/30.
 */

public class HomeFragment extends ListFragment<VideoBean>{
    @BindView(R.id.title)
    TextView title;
    CommonAdapter<VideoBean> adapter;
    List<VideoBean> mVideoBeans;
    private RxBus rxBus;        //    RxBus


    public static HomeFragment newInstance() {
        
        Bundle args = new Bundle();

        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initEventAndData(View view) {
        super.initEventAndData(view);

        request(apiServer.videoList(pageNumber, 10, null, null, null, 3), new ApiListCallback<VideoBean>() {
            @Override
            public void onSuccess(List<VideoBean> model) {
                mVideoBeans = model;
                HeadBanner banner = new HeadBanner(mContext,null);
                banner.setBanner(mVideoBeans);
                mRecycler.addHeaderView(banner);
            }

            @Override
            public void onFailure(String msg) {

            }
        });
    }

    @Override
    protected void getData(int pageNumber) {
        title.setText("影片体验区");
        requestList(apiServer.videoList(pageNumber,10,null, null, null,1));
    }

    @Override
    protected CommonAdapter<VideoBean> setAdapter(List<VideoBean> list) {
        adapter  = new CommonAdapter<VideoBean>(mContext,R.layout.adapter_home, list) {
            @Override
            protected void convert(ViewHolder holder, final VideoBean microBean, int position) {
                holder.setText(R.id.text, microBean.getName());
                Glide.with(mActivity).load(microBean.getImgurl()).error(R.drawable.ic_default_image).into((ImageView) holder.getView(R.id.cover));
                holder.setOnClickListener(R.id.cover, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, DetailActivity.class);
                        intent.putExtra("video", microBean);
                        startActivity(intent);
                    }
                });
            }
        };
        return adapter;
    }
}
