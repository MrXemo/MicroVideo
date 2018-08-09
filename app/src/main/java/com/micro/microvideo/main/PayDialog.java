package com.micro.microvideo.main;

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
    private RadioButton mRadioButton4;
    private RadioButton mRadioButton3;
    private RadioButton mRadioButton2;
    private RadioButton mRadioButton1;
    private RadioButton mRadioButton0;
    private RadioGroup mGroup;
    private BigDecimal total_fee = new BigDecimal("100");
    private ArrayList<RoleBean> mRoles;

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

    private void initView(View view){
        mRoles = (ArrayList<RoleBean>) getArguments().getSerializable("role");

        mGroup = (RadioGroup) view.findViewById(R.id.radio_group);
        mRadioButton0 = (RadioButton) view.findViewById(R.id.radio_0);
        mRadioButton1 = (RadioButton) view.findViewById(R.id.radio_1);
        mRadioButton2 = (RadioButton) view.findViewById(R.id.radio_2);
        mRadioButton3 = (RadioButton) view.findViewById(R.id.radio_3);
        mRadioButton4 = (RadioButton) view.findViewById(R.id.radio_4);
        mClose = (ImageView) view.findViewById(R.id.close);
        int role_id = (Integer)SPUtils.get(getActivity(), Constants.ROLE_ID, 0 );

        if (mRoles != null && mRoles.size() >= 5) {
            total_fee = mRoles.get(3).getOne();
            String s0 = "包周会员￥ " + mRoles.get(0).getOne().divide(new BigDecimal("100"),
                    2, RoundingMode.HALF_UP).toString() + "元";
            mRadioButton0.setText(s0);
            String s1 = "包月会员￥ " + mRoles.get(1).getOne().divide(new BigDecimal("100"),
                    2, RoundingMode.HALF_UP).toString() + "元";
            mRadioButton1.setText(s1);
            String s2 = "包季会员￥ " + mRoles.get(2).getOne().divide(new BigDecimal("100"),
                    2, RoundingMode.HALF_UP).toString() + "元";
            mRadioButton2.setText(s2);
            String s3 = "包年会员￥ " + mRoles.get(3).getOne().divide(new BigDecimal("100"),
                    2, RoundingMode.HALF_UP).toString() + "元";
            mRadioButton3.setText(s3);
            String s4 = "永久会员￥ " + mRoles.get(4).getOne().divide(new BigDecimal("100"),
                    2, RoundingMode.HALF_UP).toString() + "元";
            mRadioButton4.setText(s4);

            mGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    switch (radioGroup.getCheckedRadioButtonId()) {
                        case R.id.radio_0:
                            total_fee = mRoles.get(0).getOne();
                            break;
                        case R.id.radio_1:
                            total_fee = mRoles.get(1).getOne();
                            break;
                        case R.id.radio_2:
                            total_fee = mRoles.get(2).getOne();
                            break;
                        case R.id.radio_3:
                            total_fee = mRoles.get(3).getOne();
                            break;
                        case R.id.radio_4:
                            total_fee = mRoles.get(4).getOne();
                            break;
                    }

                }
            });
        }

        switch (role_id) {
            case 1:
                mRadioButton0.setVisibility(View.GONE);
                break;
            case 2:
                mRadioButton0.setVisibility(View.GONE);
                mRadioButton1.setVisibility(View.GONE);
                break;
            case 3:
                mRadioButton0.setVisibility(View.GONE);
                mRadioButton1.setVisibility(View.GONE);
                mRadioButton2.setVisibility(View.GONE);
                break;
            case 4:
                mRadioButton0.setVisibility(View.GONE);
                mRadioButton1.setVisibility(View.GONE);
                mRadioButton2.setVisibility(View.GONE);
                mRadioButton3.setVisibility(View.GONE);
                break;
            case 5:
                mGroup.setVisibility(View.GONE);
                break;
        }
    }
}
