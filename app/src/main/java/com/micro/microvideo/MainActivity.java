package com.micro.microvideo;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.micro.microvideo.base.SimpleActivity;
import com.micro.microvideo.main.MainFragment;

import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;


public class MainActivity extends SimpleActivity {


    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean isDown = getIntent().getBooleanExtra("isDown", false);
        String url = getIntent().getStringExtra("url");
        if (savedInstanceState == null) {
            loadRootFragment(R.id.container, MainFragment.newInstance(isDown, url));
        }
    }

    @Override
    protected void initEventAndData() {

    }


    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        // 设置横向(和安卓4.x动画相同)
        return new DefaultHorizontalAnimator();
    }
}