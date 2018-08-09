package com.micro.microvideo.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.micro.microvideo.R;
import com.micro.microvideo.base.ListFragment;
import com.micro.microvideo.main.bean.VideoBean;
import com.micro.microvideo.util.MarginAllDecoration;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by William on 2018/8/9.
 */

public class TotalFragment extends ListFragment<VideoBean> {
    CommonAdapter<VideoBean> adapter;
    String mId;

    public static TotalFragment newInstance(String id) {

        Bundle args = new Bundle();
        args.putString("id", id);
        TotalFragment fragment = new TotalFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void getData(int pageNumber) {
        mId = getArguments().getString("id");
        requestList(apiServer.videoList(pageNumber,10,"", mId, null, null, null));
    }

    @Override
    protected void initEventAndData(View view) {
        super.initEventAndData(view);
        mRecycler.setLayoutManager(new GridLayoutManager(mContext, 2));
        mRecycler.addItemDecoration(new MarginAllDecoration(8));
    }

    @Override
    protected CommonAdapter<VideoBean> setAdapter(List<VideoBean> list) {
        adapter  = new CommonAdapter<VideoBean>(mContext,R.layout.adapter_common, list) {
            @Override
            protected void convert(ViewHolder holder, final VideoBean microBean, int position) {
                holder.setText(R.id.text, microBean.getName());
                Glide.with(mContext).load(microBean.getImgurl()).error(R.drawable.ic_default_image).into((ImageView) holder.getView(R.id.cover));
                holder.setOnClickListener(R.id.cover, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, DetailActivity.class);
                        intent.putExtra("video", microBean);
                        startActivityForResult(intent, 0);
                    }
                });
            }
        };
        return adapter;
    }
}
