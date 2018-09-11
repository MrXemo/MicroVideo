package com.micro.microvideo.main.other;

import com.micro.microvideo.base.RxPresenter;
import com.micro.microvideo.http.ApiListCallback;
import com.micro.microvideo.http.ApiPageCallback;
import com.micro.microvideo.http.HttpListResult;
import com.micro.microvideo.main.bean.MicroBean;
import com.micro.microvideo.main.bean.VideoBean;

import java.util.List;

/**
 * Created by William on 2018/7/3.
 */

public class HomePresenter extends RxPresenter<HomeContract.View> implements HomeContract.Presenter {
    @Override
    public void videoList(int page, int pageSize, String name, String cid, String sid, Integer type, String role_id) {
        requestPage(apiServer.videoList(page, pageSize, name, cid, sid, type, role_id), new ApiPageCallback<VideoBean>() {
            @Override
            public void onSuccess(HttpListResult<VideoBean> model) {
                mView.getList(model);
            }

            @Override
            public void onFailure(String msg) {

            }
        });
    }

    @Override
    public void getCategory() {
        request(apiServer.category(1,7,"", 1, 4), new ApiListCallback<MicroBean>() {
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
