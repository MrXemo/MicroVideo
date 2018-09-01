package com.micro.microvideo.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bumptech.glide.Glide;
import com.micro.microvideo.R;
import com.micro.microvideo.base.BaseFragment;
import com.micro.microvideo.main.bean.MicroBean;

import com.micro.microvideo.main.bean.VideoBean;
import com.micro.microvideo.main.other.ActorContract;
import com.micro.microvideo.main.other.ActorPresenter;
import com.micro.microvideo.main.view.BannerViewHolder;
import com.micro.microvideo.main.view.ImageBannerHolderView;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by William on 2018/5/30.
 */

public class ActorFragment extends BaseFragment<ActorPresenter> implements ActorContract.View {
    @BindView(R.id.title)
    TextView title;
    CommonAdapter<MicroBean> adapter;
    CommonAdapter<MicroBean> classifAdapter;
    @BindView(R.id.classify_recycler)
    RecyclerView classifyRecycler;
    @BindView(R.id.actor_recycler)
    RecyclerView actorRecycler;
    @BindView(R.id.banner)
    MZBannerView mBannerView;
    private BannerViewHolder mBannerViewHolder;

    public static ActorFragment newInstance() {

        Bundle args = new Bundle();

        ActorFragment fragment = new ActorFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void showError(String msg) {
        toastShow(msg);
    }

    @Override
    public void getList(List<MicroBean> model) {
        if (model.size() > 8) {
            model = model.subList(0, 8);
        }
        adapter = new CommonAdapter<MicroBean>(mContext, R.layout.adapter_actor, model) {
            @Override
            protected void convert(ViewHolder holder, final MicroBean microBean, int position) {
                holder.setText(R.id.text, microBean.getName());
                Glide.with(mActivity).load(microBean.getImgurl()).error(R.drawable.ic_default_image).into((ImageView) holder.getView(R.id.cover));
                holder.setOnClickListener(R.id.cover, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, DListActivity.class);
                        intent.putExtra("type", 1);
                        intent.putExtra("name", microBean.getName());
                        intent.putExtra("id", microBean.getId());
                        intent.putExtra("url", microBean.getImgurl());
                        intent.putExtra("remark", microBean.getRemark());
                        startActivity(intent);
                    }
                });
            }
        };
        actorRecycler.setAdapter(adapter);
    }

    @Override
    public void getCategory(List<MicroBean> model) {
        final ArrayList<MicroBean> totalModel = (ArrayList<MicroBean>) model;
        List<MicroBean> TopModel = new ArrayList<>();
        List<MicroBean> BannerModel = new ArrayList<>();
        if (model.size() > 4) {
            TopModel = model.subList(0, 4);
        }
        if (model.size() > 3) {
            BannerModel = model.subList(model.size() - 3, model.size());
        } else {
            BannerModel = model;
        }


        classifAdapter = new CommonAdapter<MicroBean>(mContext, R.layout.adapter_item, TopModel) {
            @Override
            protected void convert(ViewHolder holder, final MicroBean microBean, int position) {
                holder.setText(R.id.text, microBean.getName());
                Glide.with(mActivity).load(microBean.getImgurl()).error(R.drawable.ic_default_image).into((ImageView) holder.getView(R.id.cover));
                holder.setOnClickListener(R.id.cover, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, TotalActivity.class);
                        intent.putParcelableArrayListExtra("category", totalModel);
                        startActivity(intent);
                    }
                });
            }
        };
        classifyRecycler.setAdapter(classifAdapter);

        mBannerViewHolder = new BannerViewHolder();
        mBannerViewHolder.setOnClickListener(new BannerViewHolder.OnClickListener() {
            @Override
            public void onClick(MicroBean bean) {
                Intent intent = new Intent(mContext, TotalActivity.class);
                intent.putParcelableArrayListExtra("category", totalModel);
                startActivity(intent);
            }
        });
        mBannerView.setPages(BannerModel, new MZHolderCreator<BannerViewHolder>() {
            @Override
            public BannerViewHolder createViewHolder() {
                return mBannerViewHolder;
            }
        });
       /* convenientBanner.setPages(new CBViewHolderCreator<ImageBannerHolderView>() {
            @Override
            public ImageBannerHolderView createHolder() {
                ImageBannerHolderView view = new ImageBannerHolderView();
                view.setOnClickListener(new ImageBannerHolderView.OnClickListener() {
                    @Override
                    public void onClick(MicroBean bean) {
                        Intent intent = new Intent(mContext, TotalActivity.class);
                        intent.putParcelableArrayListExtra("category", totalModel);
                        startActivity(intent);
                    }
                });
                return view;
            }
        }, BannerModel)
                .setPageIndicator(new int[]{R.drawable.dot_unselected, R.drawable.dot_selected})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);*/
    }

    @Override
    protected ActorPresenter createPresenter() {
        return new ActorPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_actor;
    }

    @Override
    protected void initEventAndData(View view) {
        title.setText("频道");

        actorRecycler.setLayoutManager(new GridLayoutManager(mContext, 4));
        classifyRecycler.setLayoutManager(new GridLayoutManager(mContext, 4));
        actorRecycler.setHasFixedSize(true);
        classifyRecycler.setHasFixedSize(true);

       /* convenientBanner.setScrollDuration(500);
        convenientBanner.startTurning(3000);*/

        mPresenter.actorList();
        mPresenter.getCategory();
    }

    @Override
    public void onPause() {
        super.onPause();
        mBannerView.pause();//暂停轮播
    }

    @Override
    public void onResume() {
        super.onResume();
        mBannerView.start();//开始轮播
    }


    /*    @Override
    protected void getData(int pageNumber) {
        title.setText("明星分类");
        requestList(apiServer.actor(pageNumber,10,""));
    }

    @Override
    protected void initEventAndData(View view) {
        mRecycler.addItemDecoration(new MarginAllDecoration(8));
        super.initEventAndData(view);
    }*/

/*    @Override
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
                        intent.putExtra("type", 1);
                        intent.putExtra("id", microBean.getId());
                        startActivity(intent);
                    }
                });
            }
        };
        return adapter;
    }*/
}
