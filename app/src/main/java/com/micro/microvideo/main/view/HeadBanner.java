package com.micro.microvideo.main.view;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.micro.microvideo.R;
import com.micro.microvideo.main.DetailActivity;
import com.micro.microvideo.main.bean.MicroBean;
import com.micro.microvideo.main.bean.VideoBean;

import java.util.List;

/**
 * Created by William on 2018/6/2.
 */

public class HeadBanner extends LinearLayout {
    ConvenientBanner mBanner;
    Context mContext;

    public HeadBanner(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.view_banner, this);

        mBanner = (ConvenientBanner) findViewById(R.id.convenientBanner);


    }

    public void setBanner(List<VideoBean> model){
        mBanner.setPages(new CBViewHolderCreator<ImageBannerHolderView>() {
            @Override
            public ImageBannerHolderView createHolder() {
                ImageBannerHolderView view = new ImageBannerHolderView();
                view.setOnClickListener(new ImageBannerHolderView.OnClickListener() {
                    @Override
                    public void onClick(VideoBean bean) {
                       Intent intent = new Intent(mContext, DetailActivity.class);
                       intent.putExtra("video", bean);
                        mContext.startActivity(intent);
                    }
                });
                return view;
            }
        }, model)
                .setPageIndicator(new int[]{R.drawable.dot_unselected, R.drawable.dot_selected})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
        mBanner.setScrollDuration(1000);
        mBanner.startTurning(5000);
    }
}
