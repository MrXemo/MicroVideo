package com.micro.microvideo.main.other;

import com.micro.microvideo.base.BasePresenter;
import com.micro.microvideo.base.BaseView;
import com.micro.microvideo.main.bean.CommentBean;

import java.util.List;

/**
 * Created by William on 2018/6/2.
 */

public interface DetailContract {
    interface View extends BaseView{
        void comment(List<CommentBean> beans);
    }

    interface Presenter extends BasePresenter<View>{
        void getComment();
    }
}
