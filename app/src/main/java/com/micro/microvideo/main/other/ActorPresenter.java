package com.micro.microvideo.main.other;

import com.micro.microvideo.base.RxPresenter;
import com.micro.microvideo.http.ApiListCallback;
import com.micro.microvideo.main.bean.MicroBean;

import java.util.List;

/**
 * Created by William on 2018/8/8.
 */

public class ActorPresenter extends RxPresenter<ActorContract.View> implements ActorContract.Presenter {

    @Override
    public void actorList() {
        request(apiServer.actor(1, 8, ""), new ApiListCallback<MicroBean>() {
            @Override
            public void onSuccess(List<MicroBean> model) {
                mView.getList(model);
            }

            @Override
            public void onFailure(String msg) {

            }
        });
    }

    @Override
    public void getCategory() {
        request(apiServer.category(1,4,"", 2, 4), new ApiListCallback<MicroBean>() {
            @Override
            public void onSuccess(List<MicroBean> model) {
                mView.getCategory(model);
            }

            @Override
            public void onFailure(String msg) {

            }
        });
    }
}
