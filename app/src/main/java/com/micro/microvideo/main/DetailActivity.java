package com.micro.microvideo.main;

import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.micro.microvideo.R;
import com.micro.microvideo.base.BaseActivity;
import com.micro.microvideo.main.bean.CommentBean;
import com.micro.microvideo.main.bean.MicroBean;
import com.micro.microvideo.main.other.DetailContract;
import com.micro.microvideo.main.other.DetailPresenter;
import com.micro.microvideo.util.ItemOffsetDecoration;
import com.micro.microvideo.util.MarginAllDecoration;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

/**
 * Created by William on 2018/6/2.
 */

public class DetailActivity extends BaseActivity<DetailPresenter> implements DetailContract.View {

    private JZVideoPlayerStandard mJzVideoPlayerStandard;
    private boolean isVip = false;  //是否为VIP
    private boolean isFirst = true;
    CountDownTimer mTask;

    @BindView(R.id.video_cover)
    RecyclerView covers;
    CommonAdapter<MicroBean> mAdapter;
    List<MicroBean> mList;
    PayDialog mPayDialog;

    @BindView(R.id.video_comment)
    RecyclerView comment;
    @BindView(R.id.edit_comment)
    EditText mEditText;
    CommonAdapter<CommentBean> adapter;
    List<CommentBean> mCommentList;

    @Override
    public void showError(String msg) {
        toastShow(msg);
    }

    @Override
    protected DetailPresenter createPresenter() {
        return new DetailPresenter();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_detail;
    }

    @Override
    protected void initEventAndData() {
        initVideo();
        initRecycler();
        mPayDialog = new PayDialog();
    }

    @Override
    public void onBackPressedSupport() {
        if (JZVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressedSupport();
    }

    @Override
    protected void onDestroy() {
        if (mTask != null) {
            mTask.cancel();
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
    }

    public void initRecycler(){
        mList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            mList.add(new MicroBean("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1528005765634&di=b151c3c681eb5e22a2defcd5ffdc0198&imgtype=jpg&src=http%3A%2F%2Fimg3.imgtn.bdimg.com%2Fit%2Fu%3D3757545233%2C2682684164%26fm%3D214%26gp%3D0.jpg", "测试"));
        }
        covers.setLayoutManager(new GridLayoutManager(this,3));
        covers.addItemDecoration(new MarginAllDecoration(4));
        mAdapter = new CommonAdapter<MicroBean>(this,R.layout.view_cover, mList) {
            @Override
            protected void convert(ViewHolder holder, MicroBean microBean, int position) {
                Glide.with(mActivity).load(microBean.getImgurl()).error(R.drawable.ic_default_image).into((ImageView) holder.getView(R.id.cover));
            }
        };

        covers.setAdapter(mAdapter);


        mCommentList = new ArrayList<>();
        for (int i = 0; i <6; i++) {
            mCommentList.add(new CommentBean("hansdvimd活动ii你覅哦阿瑟东到少年时覅四年Dion"));
        }
        comment.setLayoutManager(new LinearLayoutManager(this));
        comment.addItemDecoration(new ItemOffsetDecoration(this, R.dimen.x1));
        adapter = new CommonAdapter<CommentBean>(this,R.layout.view_comment, mCommentList) {
            @Override
            protected void convert(ViewHolder holder, CommentBean microBean, int position) {
                holder.setText(R.id.video_comment, microBean.getComment());
            }
        };
        comment.setAdapter(adapter);
    }

    public void initVideo(){
        mJzVideoPlayerStandard = (JZVideoPlayerStandard) findViewById(R.id.videoplayer);
        mJzVideoPlayerStandard.setUp("http://jzvd.nathen.cn/c6e3dc12a1154626b3476d9bf3bd7266/6b56c5f0dc31428083757a45764763b0-5287d2089db37e62345123a1be272f8b.mp4"
                , JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "测试视频");


        mJzVideoPlayerStandard.seekToInAdvance = -1;
        ImageView imageView = mJzVideoPlayerStandard.startButton;

        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
//
                    //3为播放中状态  这状态未改变
                    if (mJzVideoPlayerStandard.currentState != 3) {
                        //判断是否为vip
                        if (!isVip) {
                            if (isFirst) {
                                mTask.start();
                                isFirst = false;
                            } else {
                                mPayDialog.show(getSupportFragmentManager(), "pay");
                                return true;
                            }
                        }
                        Log.i("json", "setOnTouchListener  这是将要播放");
                    } else {
                        Log.i("json", "setOnTouchListener  这是将要停止");
                    }
                }
                return false;
            }
        });
        ImageView full = mJzVideoPlayerStandard.fullscreenButton;
        full.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (!isVip) {
                        toastShow("非会员暂不能全屏观看");
                        return true;
                    }
                }
                return false;
            }
        });

        mTask = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                JZVideoPlayerStandard.goOnPlayOnPause();
            }
        };
    }

    @OnClick(R.id.seed_comment)
    public void seedComment(){
        String comment = mEditText.getText().toString();
        mCommentList.add(new CommentBean(comment));
        adapter.notifyDataSetChanged();
        mEditText.setText("");
    }
}
