package com.micro.microvideo.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.micro.microvideo.R;
import com.micro.microvideo.app.Constants;
import com.micro.microvideo.base.BaseFragment;
import com.micro.microvideo.http.HttpListResult;
import com.micro.microvideo.main.bean.MicroBean;
import com.micro.microvideo.main.bean.VideoBean;
import com.micro.microvideo.main.other.HomeContract;
import com.micro.microvideo.main.other.HomePresenter;
import com.micro.microvideo.main.view.HeadBanner;
import com.micro.microvideo.main.view.ImageBannerHolderView;
import com.micro.microvideo.util.SPUtils;
import com.micro.microvideo.util.ZRecyclerView.ZRecyclerView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by William on 2018/5/30.
 */

public class HomeFragment extends BaseFragment<HomePresenter> implements HomeContract.View {
    //    @BindView(R.id.title)
//    TextView title;
    CommonAdapter<VideoBean> adapter;
    List<VideoBean> mVideoBeans;
    private String mMemberId;

    @BindView(R.id.recycler_view)
    protected ZRecyclerView mRecycler;
    @BindView(R.id.progress)
    ProgressBar progress;
    //    @BindView(R.id.banner)
//    LinearLayout banner;
//    @BindView(R.id.convenientBanner)
//    ConvenientBanner mBanner;
    protected String token;
    protected HeadBanner mHeadBanner;

    protected int pageNumber = 1;
    protected int totalPage = 1;
    protected List<VideoBean> list = null;
    private ArrayList<MicroBean> mTotalModel;

    public static HomeFragment newInstance(boolean isFirst) {

        Bundle args = new Bundle();
        args.putBoolean("is_first", isFirst);
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_index;
    }

    @Override
    protected void initEventAndData(View view) {
        token = (String) SPUtils.get(mContext, Constants.TOKEN, "");
        mRecycler.setLayoutManager(new GridLayoutManager(mContext, 2));

        //设置上拉刷新、 下拉加载
        mRecycler.setLoadingListener(new ZRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                pageNumber = 1;
                refurbish();
            }

            @Override
            public void onLoadMore() {
                if (totalPage >= pageNumber) {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getData(pageNumber);
                        }
                    }, 500);
                } else {
                    mRecycler.setNoMore(true);
                }
            }
        });

        if (!getArguments().getBoolean("is_first", false)) {
            getData(pageNumber);
        }

        mHeadBanner = new HeadBanner(mContext, null);
        mHeadBanner.setOnClickListener(new ImageBannerHolderView.OnClickListener() {
            @Override
            public void onClick(MicroBean bean) {
                if (mTotalModel != null) {
                    Intent intent = new Intent(mContext, TotalActivity.class);
                    intent.putParcelableArrayListExtra("category", mTotalModel);
                    startActivity(intent);
                }
            }
        });

        mPresenter.getCategory();
    }

    public void refurbish() {
        mMemberId = (String) SPUtils.get(mContext, Constants.MEMBER_ID, "");
        pageNumber = 1;
        Log.i("json", "============     refurbish()     ==========  " + mMemberId);
        mPresenter.videoList(pageNumber, 10, null, null, null, 1, mMemberId);
    }

    protected void getData(int pageNumber) {
//        title.setText("影片体验区");
        mMemberId = (String) SPUtils.get(mContext, Constants.MEMBER_ID, "");
        Log.i("json", "============     getData()     ==========    " + mMemberId);
        Log.i("json", "pageNumber : " + pageNumber);
        Log.i("json", "mMemberId : " + mMemberId);
        mPresenter.videoList(pageNumber, 10, null, null, null, 1, mMemberId);

    }

    protected CommonAdapter<VideoBean> setAdapter(List<VideoBean> list) {
        adapter = new CommonAdapter<VideoBean>(mContext, R.layout.adapter_home, list) {
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

    //更多数据
    private void moreDate(HttpListResult<VideoBean> model) {
        pageNumber++;
        list.addAll(model.getData());
        mRecycler.loadMoreComplete();
        adapter.notifyDataSetChanged();
    }

    //第一页数据
    private void headData(HttpListResult<VideoBean> model) {
        totalPage = model.getTotal();
        pageNumber += 1;
        list = model.getData();
        progress.setVisibility(View.GONE);
        adapter = setAdapter(list);
        mRecycler.setAdapter(adapter);
        mRecycler.refreshComplete();
        if (model.getTotal() <= 1) {
            mRecycler.setNoMore(true);
        } else {
            mRecycler.setNoMore(false);
            mRecycler.loadMoreComplete();
        }
    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void getList(HttpListResult<VideoBean> model) {
        if (pageNumber > 1) {
            moreDate(model);
        } else {
            headData(model);
        }
    }

    @Override
    public void getCategory(List<MicroBean> model) {
        mTotalModel = (ArrayList<MicroBean>) model;
        List<MicroBean> TopModel = new ArrayList<>();
        if (model.size() > 8) {
            TopModel = model.subList(0, 8);
        }
        mHeadBanner.setBanner(TopModel);
        mRecycler.addHeaderView(mHeadBanner);
    }

    @Override
    protected HomePresenter createPresenter() {
        return new HomePresenter();
    }
}
