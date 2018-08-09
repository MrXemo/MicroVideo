package com.micro.microvideo.main.other;

import com.micro.microvideo.base.BasePresenter;
import com.micro.microvideo.base.BaseView;
import com.micro.microvideo.http.HttpListResult;
import com.micro.microvideo.main.bean.MicroBean;
import com.micro.microvideo.main.bean.VideoBean;

import java.util.List;

/**
 * Created by William on 2018/7/3.
 */

public interface HomeContract {
    interface View extends BaseView {
        void getList(HttpListResult<VideoBean> list);

        void getCategory(List<MicroBean> model);
    }

    interface Presenter extends BasePresenter<View> {
        void videoList(int page, int pageSize, String name, String cid, String sid, Integer type,
                       String role_id);

        void getCategory();
    }
}
