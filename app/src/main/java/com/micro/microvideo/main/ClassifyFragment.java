package com.micro.microvideo.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.micro.microvideo.R;
import com.micro.microvideo.base.ListFragment;
import com.micro.microvideo.main.bean.MicroBean;
import com.micro.microvideo.util.MarginAllDecoration;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import butterknife.BindView;

/**
 * Created by William on 2018/5/30.
 */

public class ClassifyFragment extends ListFragment<MicroBean>{
    @BindView(R.id.title)
    TextView title;
    CommonAdapter<MicroBean> adapter;

    public static ClassifyFragment newInstance() {
        
        Bundle args = new Bundle();

        ClassifyFragment fragment = new ClassifyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void getData(int pageNumber) {
        title.setText("影片分类");
        requestList(apiServer.category(pageNumber,10,"", null));
    }

    @Override
    protected void initEventAndData(View view) {
        mRecycler.addItemDecoration(new MarginAllDecoration(8));
        super.initEventAndData(view);
    }

    @Override
    protected CommonAdapter<MicroBean> setAdapter(List<MicroBean> list) {
        adapter  = new CommonAdapter<MicroBean>(mContext,R.layout.adapter_common, list) {
            @Override
            protected void convert(ViewHolder holder, final MicroBean microBean, int position) {
                holder.setText(R.id.text, microBean.getName());
                Glide.with(mActivity).load(microBean.getImgurl()).error(R.drawable.ic_default_image).into((ImageView) holder.getView(R.id.cover));
                holder.setOnClickListener(R.id.cover, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, DListActivity.class);
                        intent.putExtra("type", 0);
                        intent.putExtra("id", microBean.getId());
                        startActivity(intent);
                    }
                });
            }
        };
        return adapter;
    }
}
