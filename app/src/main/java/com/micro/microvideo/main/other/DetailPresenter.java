package com.micro.microvideo.main.other;

import com.micro.microvideo.base.RxPresenter;
import com.micro.microvideo.http.ApiCallback;
import com.micro.microvideo.http.ApiListCallback;
import com.micro.microvideo.main.bean.CommentBean;
import com.micro.microvideo.main.bean.MemberBean;
import com.micro.microvideo.main.bean.RoleBean;
import com.micro.microvideo.main.bean.VideoBean;

import java.util.List;

import okhttp3.RequestBody;

/**
 * Created by William on 2018/6/2.
 */

public class DetailPresenter extends RxPresenter<DetailContract.View> implements DetailContract.Presenter {

    @Override
    public void getComment(){
        request(apiServer.guess(), new ApiListCallback<VideoBean>() {
            @Override
            public void onSuccess(List<VideoBean> model) {
                mView.comment(model);
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        });
    }

    @Override
    public void payVideo(String json){
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json);
        request(apiServer.payUrl(body), new ApiCallback<String>() {
            @Override
            public void onSuccess(String url) {
                mView.openUrl(url);
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        });
    }

    @Override
    public void getRole() {
        request(apiServer.role(), new ApiListCallback<RoleBean>() {
            @Override
            public void onSuccess(List<RoleBean> model) {
                mView.getMember(model);
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        });
    }
}
