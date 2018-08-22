package com.micro.microvideo.main.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.micro.microvideo.R;
import com.micro.microvideo.main.bean.MicroBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import butterknife.BindView;

/**
 * Created by William on 2018/8/21.
 */

public class DListHeadView extends LinearLayout{
    Context mContext;
    ImageView cover;
    ImageView back;
    TextView mTextView;
    TextView mRemark;

    private OnClickListener onClickListener;

    public interface OnClickListener {
        void onClick();
    }

    public void setOnClickListener(OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }

    public DListHeadView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.view_d_list_head, this);

        cover = (ImageView) findViewById(R.id.cover);
        back = (ImageView) findViewById(R.id.action_return);
        mTextView = (TextView) findViewById(R.id.text);
        mRemark = (TextView) findViewById(R.id.remark);
    }

    public void setDate(String name, String url, String remark){
        Glide.with(mContext).load(url).into(cover);
        mTextView.setText(name);
        mRemark.setText(remark);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null) {
                    onClickListener.onClick();
                }
            }
        });
    }
}
