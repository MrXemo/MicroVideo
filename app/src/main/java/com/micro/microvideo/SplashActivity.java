package com.micro.microvideo;

import android.content.Intent;
import android.os.CountDownTimer;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.micro.microvideo.base.SingleActivity;
import com.micro.microvideo.http.ApiListCallback;

import java.util.List;

import butterknife.BindView;

/**
 * 这是启动页
 */
public class SplashActivity extends SingleActivity<String> {

    @BindView(R.id.splash)
    ImageView splash;

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
                jumpToLoginActivity();
            }
        };
    }

    private void jumpToLoginActivity() {
        Intent intent = new Intent(this, MainActivity.class);
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
    protected ApiListCallback<String> setApiCallback() {
        return new ApiListCallback<String>() {
            @Override
            public void onSuccess(List<String> model) {
                if (model != null && model.size() > 0) {
                    Glide.with(mContext).load(model.get(0)).into(splash);
                    mCountDownTimer.start();
                } else {
                    jumpToLoginActivity();
                }
            }

            @Override
            public void onFailure(String msg) {
                jumpToLoginActivity();
            }
        };
    }
}
