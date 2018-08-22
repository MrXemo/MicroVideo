package com.micro.microvideo.main.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.micro.microvideo.R;
import com.micro.microvideo.app.App;
import com.micro.microvideo.main.bean.MicroBean;
import com.zhouwei.mzbanner.holder.MZViewHolder;

public class BannerViewHolder implements MZViewHolder<MicroBean> {
    private ImageView mImageView;

    private OnClickListener onClickListener;

    public interface OnClickListener {
        void onClick(MicroBean bean);
    }

    public void setOnClickListener(OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }

    @Override
    public View createView(Context context) {
        // 返回页面布局
        View view = LayoutInflater.from(context).inflate(R.layout.item_header, null);
        mImageView = (ImageView) view.findViewById(R.id.iv_item_header);
        return view;
    }

    @Override
    public void onBind(Context context, int position, final MicroBean data) {
        // 数据绑定
        Glide.with(App.getInstance().getApplicationContext())
                .load(data.getImgurl())
                .placeholder(R.drawable.ic_error)
                .error(R.drawable.ic_error)
                .into(mImageView);

        mImageView.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if (onClickListener != null) {
                    onClickListener.onClick(data);
                }
            }
        });
    }
}