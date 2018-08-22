package com.micro.microvideo.main;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.micro.microvideo.R;
import com.micro.microvideo.app.Constants;
import com.micro.microvideo.base.ListActivity;
import com.micro.microvideo.main.bean.VideoBean;
import com.micro.microvideo.main.view.DListHeadView;
import com.micro.microvideo.util.SPUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by William on 2018/6/3.
 */

public class DListActivity extends ListActivity<VideoBean> {
    CommonAdapter<VideoBean> adapter;
    int type;
    String id;
    String url;
    String name;
    String remark;
    DListHeadView mDListHeadView;

    @Override
    protected void getData(int pageNumber) {

        String member = (String) SPUtils.get(mContext, Constants.MEMBER_ID, "");
        if (type == 0){
            requestList(apiServer.videoList(pageNumber,10,"", id, null, null, member));
        } else {
            requestList(apiServer.videoList(pageNumber,10,"", null, id, null, member));
        }
    }

    @Override
    protected void initEventAndData() {
        url = getIntent().getStringExtra("url");
        type = getIntent().getIntExtra("type", 0);
        id = getIntent().getStringExtra("id");
        name = getIntent().getStringExtra("name");
        remark = getIntent().getStringExtra("remark");

        super.initEventAndData();

        mDListHeadView = new DListHeadView(mContext, null);
        mDListHeadView.setOnClickListener(new DListHeadView.OnClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });
        mDListHeadView.setDate(name, url, remark);
        mRecycler.addHeaderView(mDListHeadView);
    }


    @Override
    protected CommonAdapter<VideoBean> setAdapter(List<VideoBean> list) {
        adapter  = new CommonAdapter<VideoBean>(mContext,R.layout.adapter_d_list, list) {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode  == 1){
                finish();
            }
        }
    }
}
