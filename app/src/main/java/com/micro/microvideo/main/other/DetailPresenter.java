package com.micro.microvideo.main.other;

import com.micro.microvideo.base.RxPresenter;
import com.micro.microvideo.http.ApiCallback;
import com.micro.microvideo.http.ApiListCallback;
import com.micro.microvideo.main.bean.CommentBean;

import java.util.List;

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

}
