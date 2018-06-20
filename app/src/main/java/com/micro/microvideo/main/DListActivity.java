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
import com.micro.microvideo.util.SPUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import butterknife.BindView;

/**
 * Created by William on 2018/6/3.
 */

public class DListActivity extends ListActivity<VideoBean> {
    @BindView(R.id.title)
    TextView title;
    CommonAdapter<VideoBean> adapter;
    int type;
    String id;

    @Override
    protected void getData(int pageNumber) {
        title.setText("影片列表");

        String member = (String) SPUtils.get(mContext, Constants.MEMBER_ID, "");
        if (type == 0){
            requestList(apiServer.videoList(pageNumber,10,"", id, null, null, member));
        } else {
            requestList(apiServer.videoList(pageNumber,10,"", null, id, null, member));
        }
    }

    @Override
    protected void initEventAndData() {
        type = getIntent().getIntExtra("type", 0);
        id = getIntent().getStringExtra("id");
        super.initEventAndData();
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
                        startActivity(intent);
                    }
                });
            }
        };
        return adapter;
    }
}
