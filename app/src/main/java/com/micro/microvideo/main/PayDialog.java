package com.micro.microvideo.main;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.micro.microvideo.R;
import com.micro.microvideo.app.Constants;
import com.micro.microvideo.main.bean.RoleBean;
import com.micro.microvideo.util.SPUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by William on 2018/6/3.
 */

public class PayDialog extends DialogFragment {

    PayListener mPayListener;
    private ImageView mClose;
    private BigDecimal total_fee = new BigDecimal("100");
    private ArrayList<RoleBean> mRoles;
    private TextView text;
    private TextView price;

    public interface PayListener {
        void wxPayListener(BigDecimal total_fee);

        void aliPayListener(BigDecimal total_fee);
    }

    public static PayDialog newInstance(ArrayList<RoleBean> model) {

        Bundle args = new Bundle();
        args.putSerializable("role", model);
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
        text = (TextView) view.findViewById(R.id.text);
        price = (TextView) view.findViewById(R.id.price);
        initView(view);

        wechat_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPayListener != null) {
                    mPayListener.wxPayListener(total_fee);
                    dismiss();
                }
            }
        });

        ali_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPayListener != null) {
                    mPayListener.aliPayListener(total_fee);
                    dismiss();
                }
            }
        });

        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return view;
    }

    @SuppressLint("SetTextI18n")
    private void initView(View view){
        mRoles = (ArrayList<RoleBean>) getArguments().getSerializable("role");

        mClose = (ImageView) view.findViewById(R.id.close);
        int role_id = (Integer)SPUtils.get(getActivity(), Constants.ROLE_ID, 0 );

        switch (role_id) {
            case 0:
                text.setText("黄金会员即时优惠");
                price.setText(mRoles.get(0).getOne().divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP).toString());
                total_fee = mRoles.get(0).getOne();     //
                break;
            case 1:
                text.setText("铂金会员即时优惠");
                price.setText(mRoles.get(1).getOne().divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP).toString());
                total_fee = mRoles.get(1).getOne();
                break;
            case 2:
                text.setText("钻石会员即时优惠");
                price.setText(mRoles.get(2).getOne().divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP).toString());
                total_fee = mRoles.get(2).getOne();
                break;
            case 3:
                text.setText("包年会员即时优惠");
                price.setText(mRoles.get(3).getOne().divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP).toString());
                total_fee = mRoles.get(3).getOne();
                break;
            case 4:
                text.setText("永久会员即时优惠");
                price.setText(mRoles.get(4).getOne().divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP).toString());
                total_fee = mRoles.get(4).getOne();
                break;
        }
    }
}
