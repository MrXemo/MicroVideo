package com.micro.microvideo;

import android.content.Intent;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.micro.microvideo.app.Constants;
import com.micro.microvideo.base.SingleActivity;
import com.micro.microvideo.http.ApiListCallback;
import com.micro.microvideo.main.bean.SplashBean;

import java.util.List;

import butterknife.BindView;

/**
 * 这是启动页
 */
public class SplashActivity extends SingleActivity<SplashBean> {

    @BindView(R.id.splash)
    ImageView splash;
    boolean isDown = false;
    String downUrl = "";

    private CountDownTimer mCountDownTimer;

    @Override
    protected int getLayout() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initEventAndData() {
        request(apiServer.getSplashImage());
        mCountDownTimer = new CountDownTimer(4000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                jumpToLoginActivity(isDown, downUrl);
            }
        };
    }

    private void jumpToLoginActivity(boolean isDown, String url) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("isDown",isDown);
        intent.putExtra("url",url);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void jumpToWebActivity(String url) {
        Intent intent = new Intent(this, WebActivity.class);
        intent.putExtra(Constants.URL, url);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
        super.onDestroy();
    }

    @Override
    protected ApiListCallback<SplashBean> setApiCallback() {
        return new ApiListCallback<SplashBean>() {
            @Override
            public void onSuccess(final List<SplashBean> model) {
                if (model != null && model.size() > 0) {
                    Glide.with(mContext).load(model.get(0).getImgurl()).into(splash);
                    splash.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (model.get(0).getUrl().contains(".apk")) {
                                isDown = true;
                                downUrl = model.get(0).getUrl();
                                toastShow("开始下载APP");
//                                jumpToLoginActivity(true,model.get(0).getUrl());
                            } else {
                                jumpToWebActivity(model.get(0).getUrl());
                            }

                        }
                    });
                    mCountDownTimer.start();
                } else {
                    jumpToLoginActivity(false, "");
                }
            }

            @Override
            public void onFailure(String msg) {
                jumpToLoginActivity(false, "");
            }
        };
    }
}
