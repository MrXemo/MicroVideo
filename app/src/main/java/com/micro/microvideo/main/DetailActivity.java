package com.micro.microvideo.main;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.bumptech.glide.Glide;
import com.micro.microvideo.R;
import com.micro.microvideo.base.BaseActivity;
import com.micro.microvideo.main.bean.CommentBean;
import com.micro.microvideo.main.bean.VideoBean;
import com.micro.microvideo.main.other.DetailContract;
import com.micro.microvideo.main.other.DetailPresenter;
import com.micro.microvideo.util.ItemOffsetDecoration;
import com.micro.microvideo.util.MarginAllDecoration;
import com.micro.microvideo.util.SPUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

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

    @BindView(R.id.video_cover)
    RecyclerView covers;
    CommonAdapter<String> mAdapter;
    List<String> mList;
    PayDialog mPayDialog;

    @BindView(R.id.video_comment)
    RecyclerView comment;
    @BindView(R.id.edit_comment)
    EditText mEditText;
    CommonAdapter<CommentBean> adapter;
    List<CommentBean> mCommentList;
    VideoBean mVideoBean;

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

        mVideoBean = getIntent().getParcelableExtra("video");

        if (mVideoBean == null || mVideoBean.getVideourl() == null) {
            toastShow("数据错误");
            finish();
        }
        initVideo();
        initRecycler();
        Integer role = (Integer) SPUtils.get(this, "role_id", 1);
        mPayDialog = PayDialog.newInstance(role);
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
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
    }

    public void initRecycler() {
        mList = mVideoBean.getImgs();

        covers.setLayoutManager(new GridLayoutManager(this, 3));
        covers.addItemDecoration(new MarginAllDecoration(4));
        mAdapter = new CommonAdapter<String>(this, R.layout.view_cover, mList) {
            @Override
            protected void convert(ViewHolder holder, String microBean, int position) {
                Glide.with(mActivity).load(microBean).error(R.drawable.ic_default_image).into((ImageView) holder.getView(R.id.cover));
            }
        };

        covers.setAdapter(mAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setAutoMeasureEnabled(true);

        comment.setLayoutManager(layoutManager);
        comment.setHasFixedSize(true);
        comment.setNestedScrollingEnabled(false);
        comment.addItemDecoration(new ItemOffsetDecoration(this, R.dimen.x1));
        mPresenter.getComment();
    }

    public void initVideo() {
        try {

            mJzVideoPlayerStandard = (JZVideoPlayerStandard) findViewById(R.id.videoplayer);
            mJzVideoPlayerStandard.setUp(mVideoBean.getVideourl(),
                    JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, mVideoBean.getName());
        } catch (NullPointerException e) {
            showError("数据异常");
            finish();
        }


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


        if (!isVip) {
            SeekBar seekBar = mJzVideoPlayerStandard.progressBar;
            seekBar.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    toastShow("非会员不能快进");
                    return true;
                }
            });
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    long second = (long) progress * mJzVideoPlayerStandard.getDuration() / 100000L % 60;
                    if (second >= 60) {
                        JZVideoPlayerStandard.goOnPlayOnPause();
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        }
    }

    @OnClick(R.id.seed_comment)
    public void seedComment() {
        String comment = mEditText.getText().toString();
        mCommentList.add(new CommentBean(comment, "阿瑟瑞"));
        adapter.notifyDataSetChanged();
        mEditText.setText("");
    }

    @Override
    public void comment(List<CommentBean> beans) {
        mCommentList = beans;
        adapter = new CommonAdapter<CommentBean>(this, R.layout.view_comment, mCommentList) {
            @Override
            protected void convert(ViewHolder holder, CommentBean microBean, int position) {
                holder.setText(R.id.video_comment, microBean.getRemark());
                holder.setText(R.id.name, microBean.getUsername());
                Glide.with(mContext).load(microBean.getImgurl()).error(R.drawable.ic_avatar).into((ImageView) holder.getView(R.id.avatar));
            }
        };
        comment.setAdapter(adapter);
    }
}
