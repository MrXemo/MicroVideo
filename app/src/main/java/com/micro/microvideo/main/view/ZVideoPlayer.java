package com.micro.microvideo.main.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import java.util.Random;

import cn.jzvd.JZUtils;
import cn.jzvd.JZVideoPlayerStandard;

/**
 * Created by William on 2018/6/2.
 */

public class ZVideoPlayer extends JZVideoPlayerStandard {
    int second = (int) (60 * 60 * 1.5);
    String s;
    public boolean isVIP = false;

    public ZVideoPlayer(Context context) {
        super(context);

    }

    public ZVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        second = (int) ((Math.random() + 1) * second);
        s = getTime(second);
    }

    public interface setStop{
        void stop();
    }

    public setStop mStop;

    public void setStop(setStop stop) {
        mStop = stop;
    }

    @Override
    public void setProgressAndText(int progress, long position, long duration) {
        if (!isVIP){
            progress = progress / 10;
        }
        super.setProgressAndText(progress, position, duration);
//        this.bottomProgressBar.setProgress(progress);
        if (!isVIP) {
            totalTimeTextView.setText(s);
            if (currentTimeTextView.getText().equals("01:00")){
                goOnPlayOnPause();
                if (mStop != null) {
                    mStop.stop();
                }
            }
//            long progressSecond0 =  ((second * position) / duration);
//            int progressSecond = (int) ((progressSecond0 * 100) / second);
//            Log.i("json", "progress : " + progress + " progressSecond : " + progressSecond );
//            this.bottomProgressBar.setProgress(progressSecond);
        }
    }

    public static String getTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }
}
