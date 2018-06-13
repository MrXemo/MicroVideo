package com.micro.microvideo.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.micro.microvideo.R;
import com.micro.microvideo.base.ListFragment;
import com.micro.microvideo.base.SingleFragment;
import com.micro.microvideo.http.ApiCallback;
import com.micro.microvideo.http.ApiListCallback;
import com.micro.microvideo.main.bean.MicroBean;
import com.micro.microvideo.main.bean.VideoBean;
import com.micro.microvideo.main.view.HeadBanner;
import com.micro.microvideo.util.ItemOffsetDecoration;
import com.micro.microvideo.util.MarginAllDecoration;
import com.micro.microvideo.util.ZRecyclerView.ZRecyclerView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by William on 2018/5/30.
 */

public class HomeFragment extends ListFragment<VideoBean>{
    @BindView(R.id.title)
    TextView title;
    CommonAdapter<VideoBean> adapter;
    List<VideoBean> mVideoBeans;

    private static final String URL_WX = "http://weixin.vvjvv.cn/platform/pay/unifiedorder/video?sign=11316a4c0da3b15712d3255ef8c4dffc&body=vip&mch_id=gxtttooo&notify_url=http://www.baidu.com&out_trade_no=1528901705863&redirect_url=http://www.baidu.com&spbill_create_ip=192.168.1.1&total_fee=1&trade_type=WX";
    private static final String URL_ALI = "http://weixin.vvjvv.cn/platform/pay/unifiedorder/video?sign=11316a4c0da3b15712d3255ef8c4dffc&body=vip&mch_id=gxtttooo&notify_url=http://www.baidu.com&out_trade_no=1528901705863&redirect_url=http://www.baidu.com&spbill_create_ip=192.168.1.1&total_fee=1&trade_type=ALI";


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

    @OnClick(R.id.wx)
    public void wx(){
        openUrl(URL_WX);
    }


    @OnClick(R.id.ali)
    public void ali(){
        openUrl(URL_ALI);
    }

    public void openUrl(String url) {
        // 防止有大写
        url = url.replace(url.substring(0, 7), url.substring(0, 7)
                .toLowerCase());
        Uri uri = Uri.parse(url);
        try {
            Intent it = new Intent(Intent.ACTION_VIEW, uri);
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(it);
        } catch (Exception e) {
        }
    }
}
