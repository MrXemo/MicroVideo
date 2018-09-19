package com.micro.microvideo.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

/**
 * Created by Dell on 2016/8/19.
 */
public class WebViewSetting {
    private static final String APP_CACAHE_DIRNAME = "/webcache";
    private ProgressBar mProgressBar;
    private boolean isAnimStart = false;
    private int currentProgress;

    public WebViewSetting(final Context context, final WebView mWebView, ProgressBar progressBar) {
        mProgressBar = progressBar;

        WebSettings webSettings = mWebView.getSettings();

        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);;
        webSettings.setDefaultTextEncodingName("UTF-8");
        webSettings.setSaveFormData(true);// 保存表单数据
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//设置图片太大时，不会超出屏幕

        String cacheDirPath = context.getFilesDir().getAbsolutePath() + APP_CACAHE_DIRNAME; //缓存路径
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);  //缓存模式
        webSettings.setAppCachePath(cacheDirPath); //设置缓存路径
        webSettings.setAppCacheEnabled(true); //开启缓存功能

        webSettings.setAllowFileAccess(true);       //启用或禁止WebView访问文件数据
        webSettings.setDomStorageEnabled(true);


        webSettings.setUseWideViewPort(true);         //设置WebView使用广泛的视窗
        webSettings.setLoadWithOverviewMode(true);      //设置WebView 可以加载更多格式页面
        webSettings.setBuiltInZoomControls(true); // 设置显示缩放按钮
        webSettings.setSupportZoom(true); // 支持缩放

        //web长按编辑问题
        mWebView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            mWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);


        //webUrl跳转问题
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                mWebView.loadUrl(url);
                return true;
            }

//
        });
        
        mWebView.setWebChromeClient(new WebChromeClient() {

            //web进度条
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                currentProgress = mProgressBar.getProgress();
                if (newProgress >= 100) {
                    // 防止调用多次动画
                    isAnimStart = true;
                    mProgressBar.setProgress(newProgress);
                    // 开启属性动画让进度条平滑消失
                    startDismissAnimation(mProgressBar.getProgress());
                } else {
                    if (View.GONE == mProgressBar.getVisibility()){
//                        mProgressBar.setVisibility(View.VISIBLE);
                    }
                    // 开启属性动画让进度条平滑递增
                   if (currentProgress < newProgress){
                       startProgressAnimation(newProgress);
                   }
                }
            }
        });
    }

    /**
     * progressBar递增动画
     */
    private void startProgressAnimation(int newProgress) {
        ObjectAnimator animator = ObjectAnimator.ofInt(mProgressBar, "progress", currentProgress, newProgress);
        animator.setDuration(300);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.start();
    }

    /**
     * progressBar消失动画
     */
    private void startDismissAnimation(final int progress) {
        ObjectAnimator anim = ObjectAnimator.ofFloat(mProgressBar, "alpha", 1.0f, 0.0f);
        anim.setDuration(1500);  // 动画时长
        anim.setInterpolator(new DecelerateInterpolator());     // 减速
        // 关键, 添加动画进度监听器
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float fraction = valueAnimator.getAnimatedFraction();      // 0.0f ~ 1.0f
                int offset = 100 - progress;
                mProgressBar.setProgress((int) (progress + offset * fraction));
            }
        });

        anim.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                // 动画结束
                mProgressBar.setProgress(0);
                mProgressBar.setVisibility(View.GONE);
                isAnimStart = false;
            }
        });
        anim.start();
    }
}
