package com.micro.microvideo.main.view;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bumptech.glide.Glide;
import com.micro.microvideo.R;
import com.micro.microvideo.main.DetailActivity;
import com.micro.microvideo.main.TotalActivity;
import com.micro.microvideo.main.bean.MicroBean;
import com.micro.microvideo.main.bean.VideoBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import butterknife.BindView;

/**
 * Created by William on 2018/6/2.
 */

public class HeadBanner extends LinearLayout {
//    ConvenientBanner mBanner;
    Context mContext;
//    @BindView(R.id.classify_recycler)
    RecyclerView classifyRecycler;
    CommonAdapter<MicroBean> classifAdapter;

    private ImageBannerHolderView.OnClickListener onClickListener;

    public interface OnClickListener {
        void onClick(MicroBean bean);
    }

    public void setOnClickListener(ImageBannerHolderView.OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }

    public HeadBanner(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.view_banner, this);

        classifyRecycler = (RecyclerView) findViewById(R.id.classify_recycler);

        classifyRecycler.setLayoutManager(new GridLayoutManager(mContext, 4));
    }

    public void setBanner(List<MicroBean> model){
        classifAdapter  = new CommonAdapter<MicroBean>(mContext,R.layout.adapter_item, model) {
            @Override
            protected void convert(ViewHolder holder, final MicroBean microBean, int position) {
                holder.setText(R.id.text, microBean.getName());
                Glide.with(mContext).load(microBean.getImgurl()).error(R.drawable.ic_default_image).into((ImageView) holder.getView(R.id.cover));
                holder.setText(R.id.text, microBean.getName());
                Glide.with(mContext).load(microBean.getImgurl()).error(R.drawable.ic_default_image).into((ImageView) holder.getView(R.id.cover));
                holder.setOnClickListener(R.id.cover, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (onClickListener != null) {
                            onClickListener.onClick(microBean);
                        }
                    }
                });
            }
        };
        classifyRecycler.setAdapter(classifAdapter);
    }
}
