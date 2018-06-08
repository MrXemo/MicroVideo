package com.micro.microvideo.main.view;

import android.content.Context;
import android.util.Log;

import cn.jzvd.JZVideoPlayerStandard;

/**
 * Created by William on 2018/6/2.
 */

public class ZVideoPlayer extends JZVideoPlayerStandard {
    public ZVideoPlayer(Context context) {
        super(context);
    }

    @Override
    public void onStateNormal() {
        Log.i("json", "onStateNormal");
        super.onStateNormal();
    }

    @Override
    public void onStatePreparing() {
        Log.i("json", "onStatePreparing");
        super.onStatePreparing();
    }

    @Override
    public void onStatePreparingChangingUrl(int urlMapIndex, long seekToInAdvance) {
        Log.i("json", "onStatePreparingChangingUrl");
        super.onStatePreparingChangingUrl(urlMapIndex, seekToInAdvance);
    }

    @Override
    public void onStatePlaying() {
        Log.i("json", "onStatePlaying");
        super.onStatePlaying();
    }

    @Override
    public void onStatePause() {
        Log.i("json", "onStatePause");
        super.onStatePause();
    }

    @Override
    public void onStateError() {
        Log.i("json", "onStateError");
        super.onStateError();
    }

    @Override
    public void onStateAutoComplete() {
        Log.i("json", "onStateAutoComplete");
        super.onStateAutoComplete();
    }
}
