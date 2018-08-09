package com.micro.microvideo.main.other;

import com.micro.microvideo.base.BasePresenter;
import com.micro.microvideo.base.BaseView;
import com.micro.microvideo.main.bean.MicroBean;

import java.util.List;

/**
 * Created by William on 2018/8/8.
 */

public interface ActorContract {
     interface View extends BaseView {
        void getList(List<MicroBean> list);

        void getCategory(List<MicroBean> model);
    }

    interface Presenter extends BasePresenter<View> {
        void actorList();

        void getCategory();
    }
}
