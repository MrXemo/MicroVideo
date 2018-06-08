package com.micro.microvideo.main;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.micro.microvideo.R;
import com.micro.microvideo.base.SingleFragment;
import com.micro.microvideo.http.ApiCallback;

import butterknife.BindView;

/**
 * Created by William on 2018/5/30.
 */

public class MemberFragment extends SingleFragment{
    @BindView(R.id.title)
    TextView title;

    public static MemberFragment newInstance() {
        
        Bundle args = new Bundle();

        MemberFragment fragment = new MemberFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    @Override
    protected ApiCallback setApiCallback() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_member;
    }

    @Override
    protected void initEventAndData(View view) {
        title.setText("个人中心");
    }
}
