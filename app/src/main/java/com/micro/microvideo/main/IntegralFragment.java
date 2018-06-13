package com.micro.microvideo.main;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.micro.microvideo.R;
import com.micro.microvideo.base.ListFragment;
import com.micro.microvideo.main.bean.VideoBean;
import com.micro.microvideo.util.MarginAllDecoration;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import butterknife.BindView;

/**
 * Created by William on 2018/5/30.
 */

public class IntegralFragment extends ListFragment<VideoBean> {
    @BindView(R.id.title)
    TextView title;
    CommonAdapter<VideoBean> adapter;
    PayDialog mPayDialog;

    public static IntegralFragment newInstance() {

        Bundle args = new Bundle();

        IntegralFragment fragment = new IntegralFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void getData(int pageNumber) {
        title.setText("VIP专区");
        mPayDialog = new PayDialog();
        requestList(apiServer.videoList(pageNumber,10,null, null, null,2));
    }

    @Override
    protected void initEventAndData(View view) {
        mRecycler.addItemDecoration(new MarginAllDecoration(8));
        super.initEventAndData(view);
    }

    @Override
    protected CommonAdapter<VideoBean> setAdapter(List<VideoBean> list) {
        adapter = new CommonAdapter<VideoBean>(mContext, R.layout.adapter_common, list) {
            @Override
            protected void convert(ViewHolder holder, VideoBean microBean, int position) {
                holder.setText(R.id.text, microBean.getName());
                Glide.with(mActivity).load(microBean.getImgurl()).error(R.drawable.ic_default_image).into((ImageView) holder.getView(R.id.cover));
                holder.setOnClickListener(R.id.cover, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mPayDialog.show(getFragmentManager(), "pay");
                    }
                });
            }
        };
        return adapter;
    }
}
