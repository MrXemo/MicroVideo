package com.micro.microvideo.main.other;

import com.micro.microvideo.base.RxPresenter;
import com.micro.microvideo.http.ApiCallback;
import com.micro.microvideo.http.ApiListCallback;
import com.micro.microvideo.main.bean.CommentBean;
import com.micro.microvideo.main.bean.MemberBean;

import java.util.List;

import okhttp3.RequestBody;

/**
 * Created by William on 2018/6/2.
 */

public class DetailPresenter extends RxPresenter<DetailContract.View> implements DetailContract.Presenter {

    @Override
    public void getComment(){
        request(apiServer.comment(), new ApiListCallback<CommentBean>() {
            @Override
            public void onSuccess(List<CommentBean> model) {
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
    public void getRole(String memberId) {
        request(apiServer.getInfo(memberId), new ApiCallback<MemberBean>() {
            @Override
            public void onSuccess(MemberBean model) {
                mView.getMember(model);
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        });
    }
}
