package com.micro.microvideo.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.micro.microvideo.R;
import com.micro.microvideo.app.Constants;
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

    private BottomBarTab mTab1;
    private BottomBarTab mTab2;
    private BottomBarTab mTab3;
    private BottomBarTab mTab4;
    private BottomBarTab mTab5;

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

        String memberId = (String) SPUtils.get(getContext(), "member_id", "");
        if (memberId == null || memberId.equals("")) {
            Log.i("json", "member_id 等于空");
            request(apiServer.register("3"));
        } else {
            Log.i("json", "member_id 不等于空" + SPUtils.get(getContext(), "member_id", ""));
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
        mTab1 = new BottomBarTab(mContext, R.drawable.ic_main_home, R.drawable.ic_main_nav_home, "体验");
        mTab2 = new BottomBarTab(mContext, R.drawable.ic_main_integral, R.drawable.ic_main_nav_integral, "会员区");
        mTab3 = new BottomBarTab(mContext, R.drawable.ic_main_classify, R.drawable.ic_main_nav_classify, "分类");
        mTab4= new BottomBarTab(mContext, R.drawable.ic_main_actor, R.drawable.ic_main_nav_actor, "艺人");
        mTab5 = new BottomBarTab(mContext, R.drawable.ic_main_member, R.drawable.ic_main_nav_member, "我的");

        Integer roleId = (Integer) SPUtils.get(mContext, Constants.ROLE_ID, 0);
        if (roleId == 1) {
            mTab1.setText("会员区");
            mTab2.setText("超级会员");
        } else if (roleId == 2){
            mTab1.setText("超级会员");
            mTab2.setText("黄金会员");
        } else if (roleId == 3){
            mTab1.setText("黄金会员");
            mTab2.setText("铂金会员");
        }

        bottomBar.addItem(mTab1)
                .addItem(mTab2)
                .addItem(mTab3)
                .addItem(mTab4)
                .addItem(mTab5);
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
                String memberId = (String) SPUtils.get(getContext(), "member_id", "");
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
                SPUtils.put(getContext(), Constants.MEMBER_ID, model.getId());
//                if (model.getRole_id().compareTo((Integer) SPUtils.get(mContext, Constants.ROLE_ID, 0)) > 0){
                    SPUtils.put(getContext(), Constants.ROLE_ID, model.getRole_id());

                    if (model.getRole_id() == 1) {
                        mTab1.setText("会员区");
                        mTab2.setText("超级会员");
                    } else if (model.getRole_id() == 2){
                        mTab1.setText("超级会员");
                        mTab2.setText("黄金会员");
                    } else if (model.getRole_id() == 3){
                        mTab1.setText("黄金会员");
                        mTab2.setText("铂金会员");
                    }

                    if (mFragments != null) {
                        for (SupportFragment fragment : mFragments) {
                            if (fragment instanceof HomeFragment) {
                                ((HomeFragment)fragment).refurbish();
                            } else if (fragment instanceof IntegralFragment){
                                ((IntegralFragment)fragment).refurbish();
                            }
                        }
                    }
//                }
//                toastShow(model.getRoleText());
            }

            @Override
            public void onFailure(String msg) {

            }
        };
    }
}
