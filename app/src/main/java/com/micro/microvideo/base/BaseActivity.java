package com.micro.microvideo.base;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.micro.microvideo.R;
import com.micro.microvideo.app.Constants;
import com.micro.microvideo.util.SPUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportActivity;

/**
 * Created by codeest on 2016/8/2.
 * MVP activity基类
 */
public abstract class BaseActivity<T extends BasePresenter> extends SupportActivity implements BaseView {
    protected T mPresenter;
    protected Activity mActivity;
    private Unbinder mUnBinder;
    protected String token;

    protected abstract T createPresenter();
    protected abstract int getLayout();
    protected abstract void initEventAndData();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mPresenter = createPresenter();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//        }
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        mUnBinder = ButterKnife.bind(this);
        mActivity = this;
        token = (String) SPUtils.get(mActivity, Constants.TOKEN,"");
        if (mPresenter != null)
            mPresenter.attachView(this);
        initEventAndData();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 设置toolbar
     */
    public Toolbar initToolBar(String title) {

        Toolbar toolbar = initToolBar();
        TextView toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar_title.setText(title);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);

        }
        return toolbar;
    }

    public Toolbar initToolBar(int title) {
        Toolbar toolbar = initToolBar();
        TextView toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar_title.setText(title);
        return toolbar;
    }

    public Toolbar initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        return toolbar;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null)
            mPresenter.detachView();
        mUnBinder.unbind();
//        App.getInstance().removeActivity(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();//返回
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void toastShow(int resId) {
        Toast.makeText(mActivity.getApplicationContext(), resId, Toast.LENGTH_SHORT).show();
    }

    public void toastShow(String text) {
        if (text.equals("用户未登录")){
            SPUtils.put(getApplicationContext(), Constants.TOKEN_ABLE,false);
//            PushManager.getInstance().unBindAlias(getApplicationContext(), userId, false);
           /* RxBus.getDefault().post(new UserEvent(1, Constants.RENOVATE));
            Unicorn.logout();       //注销七鱼*/
        }
        Toast.makeText(mActivity.getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }
}