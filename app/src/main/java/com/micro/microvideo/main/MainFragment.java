package com.micro.microvideo.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.micro.microvideo.R;
import com.micro.microvideo.base.SingleFragment;
import com.micro.microvideo.http.ApiCallback;
import com.micro.microvideo.main.bean.MemberBean;
import com.micro.microvideo.main.bean.NoticeBean;
import com.micro.microvideo.util.RxBus;
import com.micro.microvideo.util.SPUtils;
import com.micro.microvideo.util.footbar.BottomBar;
import com.micro.microvideo.util.footbar.BottomBarTab;

import butterknife.BindView;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by hboxs006 on 2017/10/18.
 */

public class MainFragment extends SingleFragment<MemberBean> {
    private final int FIRST = 0;
    private final int SECOND = 1;
    private final int THIRD = 2;
    private final int FOUR = 3;
    private final int FIVE = 4;

    private SupportFragment[] mFragments = new SupportFragment[5];
    private RxBus rxBus;        //    RxBus

    @BindView(R.id.bottomBar)
    BottomBar bottomBar;

    public static MainFragment newInstance() {

        Bundle args = new Bundle();

        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        String memberId = (String) SPUtils.get(getContext(),"member_id", "");
        if (memberId == null || memberId.equals("")) {
            Log.i("json", "member_id 等于空");
            request(apiServer.register("3"));
        } else {
            Log.i("json", "member_id 不等于空" + SPUtils.get(getContext(),"member_id", ""));
        }

        if (savedInstanceState == null) {
            mFragments[FIRST] = HomeFragment.newInstance();
            mFragments[SECOND] = IntegralFragment.newInstance();
            mFragments[THIRD] = ClassifyFragment.newInstance();
            mFragments[FOUR] = ActorFragment.newInstance();
            mFragments[FIVE] = MemberFragment.newInstance();

            loadMultipleRootFragment(R.id.fl_tab_container, FIRST,
                    mFragments[FIRST],
                    mFragments[SECOND],
                    mFragments[THIRD],
                    mFragments[FOUR],
                    mFragments[FIVE]);
        } else {
            // 这里库已经做了Fragment恢复,所有不需要额外的处理了, 不会出现重叠问题

            // 这里我们需要拿到mFragments的引用,也可以通过getChildFragmentManager.getFragments()自行进行判断查找(效率更高些),用下面的方法查找更方便些
            mFragments[FIRST] = findChildFragment(HomeFragment.class);
            mFragments[SECOND] = findChildFragment(IntegralFragment.class);
            mFragments[THIRD] = findChildFragment(ClassifyFragment.class);
            mFragments[FOUR] = findChildFragment(ActorFragment.class);
            mFragments[FIVE] = findChildFragment(MemberFragment.class);
        }
        return mView;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initEventAndData(View view) {
        bottomBar.addItem(new BottomBarTab(mContext, R.drawable.ic_main_home, R.drawable.ic_main_nav_home, "体验"))
                .addItem(new BottomBarTab(mContext, R.drawable.ic_main_integral, R.drawable.ic_main_nav_integral, "会员区"))
                .addItem(new BottomBarTab(mContext, R.drawable.ic_main_classify, R.drawable.ic_main_nav_classify, "分类"))
                .addItem(new BottomBarTab(mContext, R.drawable.ic_main_actor, R.drawable.ic_main_nav_actor, "艺人"))
                .addItem(new BottomBarTab(mContext, R.drawable.ic_main_member, R.drawable.ic_main_nav_member, "我的"));
        bottomBar.setOnTabSelectedListener(new BottomBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, int prePosition) {
                showHideFragment(mFragments[position], mFragments[prePosition]);
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rxBus = RxBus.getIntanceBus();
        rxBus.registerRxBus(NoticeBean.class, new Consumer<NoticeBean>() {
            @Override
            public void accept(@NonNull NoticeBean event) throws Exception {
                String memberId = (String) SPUtils.get(getContext(),"member_id", "");
                request(apiServer.getInfo(memberId));
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (rxBus != null) {
            rxBus.unSubscribe(this);
        }
    }

    @Override
    protected ApiCallback<MemberBean> setApiCallback() {
        return new ApiCallback<MemberBean>() {
            @Override
            public void onSuccess(MemberBean model) {
                SPUtils.put(getContext(),"member_id", model.getId());
                toastShow(model.getRoleText());
            }

            @Override
            public void onFailure(String msg) {

            }
        };
    }
}
