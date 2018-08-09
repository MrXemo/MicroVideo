package com.micro.microvideo.main.other;

import com.micro.microvideo.base.BasePresenter;
import com.micro.microvideo.base.BaseView;
import com.micro.microvideo.main.bean.CommentBean;
import com.micro.microvideo.main.bean.MemberBean;
import com.micro.microvideo.main.bean.RoleBean;

import java.util.List;

/**
 * Created by William on 2018/6/2.
 */

public interface DetailContract {
    interface View extends BaseView{
        void comment(List<CommentBean> beans);

        void openUrl(String url);

        void getMember(List<RoleBean> memberBean);
    }

    interface Presenter extends BasePresenter<View>{
        void getComment();

        void payVideo(String json);

        void getRole();
    }
}
