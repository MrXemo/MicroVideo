package com.micro.microvideo.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.micro.microvideo.R;
import com.micro.microvideo.app.Constants;
import com.micro.microvideo.util.SPUtils;

/**
 * Created by William on 2018/6/3.
 */

public class PayDialog extends DialogFragment {

    PayListener mPayListener;

    public interface PayListener {
        void wxPayListener();

        void aliPayListener();
    }

    public static PayDialog newInstance(Integer type) {

        Bundle args = new Bundle();
        args.putInt("type", type);
        PayDialog fragment = new PayDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public void setPayListener(PayListener payListener) {
        mPayListener = payListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dailog_pay, container, false);

        TextView wechat_pay = (TextView) view.findViewById(R.id.wechat_pay);
        TextView ali_pay = (TextView) view.findViewById(R.id.ali_pay);
        RadioGroup group = (RadioGroup) view.findViewById(R.id.radio_group);
        ImageView close = (ImageView) view.findViewById(R.id.close);
        if ((Integer)SPUtils.get(getActivity(), Constants.ROLE_ID, 0 ) != 0){
            group.setVisibility(View.GONE);
        }

        wechat_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPayListener != null) {
                    mPayListener.wxPayListener();
                    dismiss();
                }
            }
        });

        ali_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPayListener != null) {
                    mPayListener.aliPayListener();
                    dismiss();
                }
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return view;
    }
}
