package com.micro.microvideo.main.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;

import com.micro.microvideo.R;
import com.micro.microvideo.app.App;
import com.micro.microvideo.main.bean.MicroBean;
import com.micro.microvideo.main.bean.VideoBean;


/**
 * Created by Dell on 2016/11/28.
 */

public class ImageBannerHolderView implements Holder<MicroBean> {

    private View view;

    private OnClickListener onClickListener;

    public interface OnClickListener {
        void onClick(MicroBean bean);
    }

    public void setOnClickListener(OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }

    @Override
    public View createView(Context context) {
        view = LayoutInflater.from(context).inflate(R.layout.item_header, null);
        return view;
    }


    @Override
    public void UpdateUI(final Context context, final int position, final MicroBean data) {

        final ImageView imageView = (ImageView) view.findViewById(R.id.iv_item_header);
//        TextView textView = (TextView)view.findViewById(R.id.tv_item_header);

        Glide.with(App.getInstance().getApplicationContext())
                .load(data.getImgurl())
                .placeholder(R.drawable.ic_error)
                .error(R.drawable.ic_error)
                .into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {

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
