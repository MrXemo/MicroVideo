package com.micro.microvideo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.micro.microvideo.app.Constants;
import com.micro.microvideo.base.SimpleActivity;
import com.micro.microvideo.util.WebViewSetting;

import butterknife.BindView;

/**
 * Created by William on 2018/9/18.
 */

public class WebActivity extends SimpleActivity {
    @BindView(R.id.wv_content)
    WebView mWebView;
    @BindView(R.id.web_progressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.toolbar_right_menu)
    TextView menu;
    @BindView(R.id.toolbar_close)
    ImageView close;

    String url;
    String title;

    @Override
    protected int getLayout() {
        return R.layout.fragment_web;
    }

    @SuppressLint("JavascriptInterface")
    @Override
    protected void initEventAndData() {
        url = getIntent().getStringExtra(Constants.URL);
//        url = "https://www.baidu.com";
//        Log.i("json", "initEventAndData: url : " + url);
        title = getIntent().getStringExtra(Constants.TITLE);
        close.setVisibility(View.VISIBLE);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToLoginActivity();
            }
        });

        mWebView.loadUrl(url);
        new WebViewSetting(mContext, mWebView, mProgressBar);
        mWebView.addJavascriptInterface(this, "interface");
    }

    boolean isClose = false;

    @Override
    public void onBackPressedSupport() {
        jumpToLoginActivity();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void jumpToLoginActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

}
