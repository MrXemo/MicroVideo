package com.micro.microvideo.main;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.micro.microvideo.R;
import com.micro.microvideo.app.Constants;
import com.micro.microvideo.base.BaseActivity;
import com.micro.microvideo.main.bean.CommentBean;
import com.micro.microvideo.main.bean.NoticeBean;
import com.micro.microvideo.main.bean.RoleBean;
import com.micro.microvideo.main.bean.VideoBean;
import com.micro.microvideo.main.other.DetailContract;
import com.micro.microvideo.main.other.DetailPresenter;
import com.micro.microvideo.main.view.ZVideoPlayer;
import com.micro.microvideo.util.ItemOffsetDecoration;
import com.micro.microvideo.util.MarginAllDecoration;
import com.micro.microvideo.util.RxBus;
import com.micro.microvideo.util.SPUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

/**
 * Created by William on 2018/6/2.
 */

public class DetailActivity extends BaseActivity<DetailPresenter> implements DetailContract.View {

    private ZVideoPlayer mJzVideoPlayerStandard;
    private boolean isVip = false;  //是否为VIP
    private boolean isFirst = true;

//    @BindView(R.id.video_cover)
//    RecyclerView covers;
//    CommonAdapter<String> mAdapter;
    List<String> mList;
    PayDialog mPayDialog;

    @BindView(R.id.introduction)
    TextView introduction;


    @BindView(R.id.video_comment)
    RecyclerView comment;
    /*@BindView(R.id.edit_comment)
    EditText mEditText;*/
    CommonAdapter<VideoBean> adapter;
    List<VideoBean> mCommentList;
    VideoBean mVideoBean;
    boolean isPay = false;
    private TextView mTotalTime;
    private SimpleDateFormat mFormat;
    private boolean isDialog = false;

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
        mFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mVideoBean = getIntent().getParcelableExtra("video");

        if (mVideoBean == null || mVideoBean.getVideourl() == null) {
            toastShow("数据错误");
            finish();
        }
        introduction.setText(mVideoBean.getName());

        if ((Integer) SPUtils.get(this, Constants.ROLE_ID, 0) == 5) {
            isVip = true;
        }
        initVideo();
        initRecycler();
        mPresenter.getRole();
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

/*        covers.setLayoutManager(new GridLayoutManager(this, 3));
        covers.addItemDecoration(new MarginAllDecoration(4));
        mAdapter = new CommonAdapter<String>(this, R.layout.view_cover, mList) {
            @Override
            protected void convert(ViewHolder holder, String microBean, int position) {
                Glide.with(mActivity).load(microBean).error(R.drawable.ic_default_image).into((ImageView) holder.getView(R.id.cover));
            }
        };

        covers.setAdapter(mAdapter);*/

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

            mJzVideoPlayerStandard = (ZVideoPlayer) findViewById(R.id.videoplayer);
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

        SeekBar seekBar = mJzVideoPlayerStandard.progressBar;
//        mJzVideoPlayerStandard.setProgressAndText();
        mTotalTime = mJzVideoPlayerStandard.totalTimeTextView;
        ImageView full = mJzVideoPlayerStandard.fullscreenButton;
//        mCurrentTime = mJzVideoPlayerStandard.currentTimeTextView;

        if (!isVip) {
            full.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        toastShow("非会员暂不能全屏观看");
                        return true;
                    }
                    return false;
                }
            });
            seekBar.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    toastShow("非会员不能快进");
                    return true;
                }
            });

            mJzVideoPlayerStandard.setStop(new ZVideoPlayer.setStop() {
                @Override
                public void stop() {
                    if (!isDialog) {
                        isDialog = true;
                        mPayDialog.show(getSupportFragmentManager(), "pay");
                    }
                }
            });
        } else {
            mJzVideoPlayerStandard.isVIP = true;
        }
    }

    /*@OnClick(R.id.seed_comment)
    public void seedComment() {
        String comment = mEditText.getText().toString();
        mCommentList.add(new CommentBean(comment, "阿瑟瑞"));
        adapter.notifyDataSetChanged();
        mEditText.setText("");
    }*/

    @Override
    public void comment(List<VideoBean> beans) {
        mCommentList = beans;
        adapter = new CommonAdapter<VideoBean>(this, R.layout.view_guess, mCommentList) {
            @Override
            protected void convert(ViewHolder holder, VideoBean videoBean, int position) {
                Glide.with(mContext).load(videoBean.getImgurl()).error(R.drawable.ic_avatar).into((ImageView) holder.getView(R.id.avatar));
                holder.setText(R.id.name, videoBean.getName());
            }
        };
        comment.setAdapter(adapter);
    }

    private void pay(String type, String amount) {
        String user = (String) SPUtils.get(this, "member_id", "");

        JSONObject obj = new JSONObject();
        try {
            obj.put("user_id", user);
            obj.put("trade_type", type);
            obj.put("total_fee", amount);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("json", "obj : " + obj.toString());


        mPresenter.payVideo(obj.toString());
    }

    @Override
    public void openUrl(String url) {
        isPay = true;
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

    @Override
    protected void onResume() {
        super.onResume();
        if (isPay) {
            RxBus.getIntanceBus().post(new NoticeBean());
            setResult(1);
            finish();
        }
    }

    @Override
    public void getMember(List<RoleBean> model) {
        mPayDialog = PayDialog.newInstance((ArrayList<RoleBean>) model);
        mPayDialog.setPayListener(new PayDialog.PayListener() {
            @Override
            public void wxPayListener(BigDecimal total_fee) {
                pay("WX", total_fee.toString());
            }

            @Override
            public void aliPayListener(BigDecimal total_fee) {
                pay("ALI", total_fee.toString());
            }
        });
    }
}
