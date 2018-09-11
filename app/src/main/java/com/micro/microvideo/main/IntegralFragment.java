package com.micro.microvideo.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.micro.microvideo.R;
import com.micro.microvideo.app.Constants;
import com.micro.microvideo.base.ListFragment;
import com.micro.microvideo.http.ApiCallback;
import com.micro.microvideo.http.ApiListCallback;
import com.micro.microvideo.main.bean.NoticeBean;
import com.micro.microvideo.main.bean.RoleBean;
import com.micro.microvideo.main.bean.VideoBean;
import com.micro.microvideo.util.ItemOffsetDecoration;
import com.micro.microvideo.util.MarginAllDecoration;
import com.micro.microvideo.util.RxBus;
import com.micro.microvideo.util.SPUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.RequestBody;

/**
 * Created by William on 2018/5/30.
 */

public class IntegralFragment extends ListFragment<VideoBean> {
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.toolbar_view)
    Toolbar mToolbar;
    CommonAdapter<VideoBean> adapter;
//    PayDialog mPayDialog;
//    boolean isPay = false;
    private String mMember;

    public static IntegralFragment newInstance(boolean isFirst) {

        Bundle args = new Bundle();
        args.putBoolean("is_first", isFirst);
        IntegralFragment fragment = new IntegralFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void getData(int pageNumber) {
        mToolbar.setVisibility(View.VISIBLE);
        title.setText("发现");
        Integer role = (Integer) SPUtils.get(mContext, Constants.ROLE_ID, 0);
        Log.i("json", "getData: role : " + role);
        Log.i("json", "============     getData()     ==========  " + mMember);
        mMember = (String) SPUtils.get(mContext, Constants.MEMBER_ID, "");
        /*request(apiServer.role(), new ApiListCallback<RoleBean>() {
            @Override
            public void onSuccess(List<RoleBean> model) {
                mPayDialog = PayDialog.newInstance((ArrayList<RoleBean>) model);
                mPayDialog.setPayListener(new PayDialog.PayListener() {
                    @Override
                    public void wxPayListener(BigDecimal total_fee) {
                        pay("WX", total_fee.toString());
                    }

                    @Override
                    public void aliPayListener(BigDecimal total_fee) {
                        pay("ALI", total_fee.toString());
                    }
                });
            }

            @Override
            public void onFailure(String msg) {
            }
        });*/

        if (!getArguments().getBoolean("is_first", false)) {
            requestList(apiServer.videoList(pageNumber,10,null, null, null,2, mMember));
        }
    }

    @Override
    protected void initEventAndData(View view) {
        mRecycler.addItemDecoration(new ItemOffsetDecoration(mContext, R.dimen.x10));
        super.initEventAndData(view);
    }

    public void refurbish(){
        mMember = (String) SPUtils.get(mContext, Constants.MEMBER_ID, "");
        pageNumber = 1;
        Log.i("json", "============     refurbish()     ==========  " + mMember);
        requestList(apiServer.videoList(1,10,null, null, null,2, mMember));
    }

    @Override
    protected CommonAdapter<VideoBean> setAdapter(List<VideoBean> list) {
        adapter = new CommonAdapter<VideoBean>(mContext, R.layout.adapter_integral, list) {
            @Override
            protected void convert(ViewHolder holder, final VideoBean microBean, int position) {
                holder.setText(R.id.text, microBean.getName());
                Glide.with(mActivity).load(microBean.getImgurl()).error(R.drawable.ic_default_image).into((ImageView) holder.getView(R.id.cover));
                holder.setOnClickListener(R.id.cover, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, DetailActivity.class);
                        intent.putExtra("video", microBean);
                        startActivity(intent);
                    }
                });
            }
        };
        return adapter;
    }

    private void pay(String type, String amount){
        String user = (String) SPUtils.get(mContext, "member_id", "");

        JSONObject obj  = new JSONObject();
        try {
            obj.put("user_id", user);
            obj.put("trade_type", type);
            obj.put("total_fee", amount);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("json", "obj : " + obj.toString());
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),obj.toString());

        requests(apiServer.payUrl(body), new ApiCallback<String>() {
            @Override
            public void onSuccess(String url) {
//                openUrl(url);
            }

            @Override
            public void onFailure(String msg) {

            }
        });
    }

//    public void openUrl(String url) {
//        url = url.replace(url.substring(0, 7), url.substring(0, 7)
//                .toLowerCase());
//        Uri uri = Uri.parse(url);
//        try {
//            Intent it = new Intent(Intent.ACTION_VIEW, uri);
//            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(it);
//        } catch (Exception e) {
//        }
//    }
}
