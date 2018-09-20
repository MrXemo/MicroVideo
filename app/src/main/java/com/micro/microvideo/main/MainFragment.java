package com.micro.microvideo.main;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.micro.microvideo.R;
import com.micro.microvideo.app.Constants;
import com.micro.microvideo.base.SimpleFragment;
import com.micro.microvideo.base.SingleFragment;
import com.micro.microvideo.http.ApiCallback;
import com.micro.microvideo.main.bean.MemberBean;
import com.micro.microvideo.main.bean.NoticeBean;
import com.micro.microvideo.util.AppUtils;
import com.micro.microvideo.util.RxBus;
import com.micro.microvideo.util.SPUtils;
import com.micro.microvideo.util.footbar.BottomBar;
import com.micro.microvideo.util.footbar.BottomBarTab;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import me.yokeyword.fragmentation.SupportFragment;
import okhttp3.ResponseBody;

/**
 * Created by hboxs006 on 2017/10/18.
 */

public class MainFragment extends SingleFragment<MemberBean> {
    private final int FIRST = 0;
    private final int SECOND = 1;
    private final int THIRD = 2;
    private final int FOUR = 3;
//    private final int FIVE = 4;

    private BottomBarTab mTab1;
    private BottomBarTab mTab2;
    private BottomBarTab mTab3;
    private BottomBarTab mTab4;
    //    private BottomBarTab mTab5;
    private String inviteId = "5";

    private SupportFragment[] mFragments = new SupportFragment[5];
    private RxBus rxBus;        //    RxBus
    private boolean isFirst = false;

    @BindView(R.id.bottomBar)
    BottomBar bottomBar;

    public static MainFragment newInstance(boolean isDown, String url) {

        Bundle args = new Bundle();
        args.putBoolean("isDown", isDown);
        args.putString("url", url);
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        String memberId = (String) SPUtils.get(getContext(), "member_id", "");
        if (getArguments().getBoolean("isDown")) {

            onDown(getArguments().getString("url"));
            Log.i("json", "url : " + getArguments().getString("url"));
        }
        if (memberId == null || memberId.equals("")) {
            Log.i("json", "member_id 等于空");
            Log.i("json", "getVersionCode(mContext) : " + getVersionCode(mContext));
            isFirst = true;
            request(apiServer.register(String.valueOf(getVersionCode(mContext))));
        } else {
            Log.i("json", "member_id 不等于空" + SPUtils.get(getContext(), "member_id", ""));
            request(apiServer.getInfo(memberId));
        }

        if (savedInstanceState == null) {
            mFragments[FIRST] = HomeFragment.newInstance(isFirst);
            mFragments[SECOND] = ActorFragment.newInstance();
//            mFragments[THIRD] = ClassifyFragment.newInstance();
            mFragments[THIRD] = IntegralFragment.newInstance(isFirst);
            mFragments[FOUR] = MemberFragment.newInstance();

            loadMultipleRootFragment(R.id.fl_tab_container, FIRST,
                    mFragments[FIRST],
                    mFragments[SECOND],
                    mFragments[THIRD],
                    mFragments[FOUR]);
        } else {
            // 这里库已经做了Fragment恢复,所有不需要额外的处理了, 不会出现重叠问题

            // 这里我们需要拿到mFragments的引用,也可以通过getChildFragmentManager.getFragments()自行进行判断查找(效率更高些),用下面的方法查找更方便些
            mFragments[FIRST] = findChildFragment(HomeFragment.class);
            mFragments[SECOND] = findChildFragment(ActorFragment.class);
//            mFragments[THIRD] = findChildFragment(ClassifyFragment.class);
            mFragments[THIRD] = findChildFragment(IntegralFragment.class);
            mFragments[FOUR] = findChildFragment(MemberFragment.class);
        }
        return mView;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initEventAndData(View view) {
        mTab1 = new BottomBarTab(mContext, R.drawable.ic_main_home, R.drawable.ic_main_nav_home, "首页");
        mTab2 = new BottomBarTab(mContext, R.drawable.ic_main_integral, R.drawable.ic_main_nav_integral, "频道");
//        mTab3 = new BottomBarTab(mContext, R.drawable.ic_main_classify, R.drawable.ic_main_nav_classify, "分类");
        mTab3 = new BottomBarTab(mContext, R.drawable.ic_main_actor, R.drawable.ic_main_nav_actor, "发现");
        mTab4 = new BottomBarTab(mContext, R.drawable.ic_main_member, R.drawable.ic_main_nav_member, "我的");

        bottomBar.addItem(mTab1)
                .addItem(mTab2)
                .addItem(mTab3)
                .addItem(mTab4);
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
                if (model.getRole_id().compareTo((Integer) SPUtils.get(mContext, Constants.ROLE_ID, 0)) != 0) {
                    SPUtils.put(getContext(), Constants.ROLE_ID, model.getRole_id());

                    if (mFragments != null) {
                        for (SupportFragment fragment : mFragments) {
                            if (fragment instanceof HomeFragment) {
                                ((HomeFragment) fragment).refurbish();
                            } else if (fragment instanceof IntegralFragment) {
                                ((IntegralFragment) fragment).refurbish();
                            }
                        }
                    }
                }

                if (isFirst) {
                    Log.i("json", "============     isFirst     ==========");
                    if (mFragments != null) {
                        for (SupportFragment fragment : mFragments) {
                            if (fragment instanceof HomeFragment) {
                                ((HomeFragment) fragment).refurbish();
                            } else if (fragment instanceof IntegralFragment) {
                                ((IntegralFragment) fragment).refurbish();
                            }
                        }
                    }
                }
//                toastShow(model.getRoleText());
            }

            @Override
            public void onFailure(String msg) {

            }
        };
    }

    /**
     * 获取当前本地apk的版本
     *
     * @param mContext
     * @return
     */
    public static int getVersionCode(Context mContext) {
        int versionCode = 0;
        try {
            //获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = mContext.getPackageManager().
                    getPackageInfo(mContext.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    private static String savePath;
    private static int REQUEST_PERMISSION = 2;

    private void onDown(final String url) {
        int permission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        new RxPermissions(getActivity()).request(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            downApk(url);
                        } else {
                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                            Toast.makeText(getActivity(), "开启权限失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void downApk(String apkURL) {
        savePath = mContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + File.separator + "micro";
        apiServer.downloadApk(apkURL).subscribeOn(Schedulers.io()).subscribe(new Consumer<ResponseBody>() {
            @Override
            public void accept(ResponseBody responseBody) throws Exception {
                //保存文件
                Observable.just(responseBody).map(new Function<ResponseBody, Boolean>() {
                    @Override
                    public Boolean apply(ResponseBody responseBody) throws Exception {
                        return writeFileToSDCard(responseBody, savePath);
                    }

                }).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(Boolean aBoolean) throws Exception {
                                if (aBoolean) {
                                    AppUtils.installApk(mContext, savePath);
                                } else {

//                                    SPUtils.put(mContext, Constants.APP_VERSION_CODE, -1);
//                                    deleteErrorApk(mApkInfo);
                                    toastShow("apk保存失败");
//                                    cancelNotification();
                                }
//                                downloading = false;
                                //取消dialog // TODO: 2017/10/25
//                                progressDialog.dismiss();
                            }

                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                if (throwable != null && !TextUtils.isEmpty(throwable.getLocalizedMessage())) {

//                                    SPUtils.put(mContext, Constants.APP_VERSION_CODE, -1);
//                                    deleteErrorApk(mApkInfo);
                                    throwable.printStackTrace();
                                    Log.d("json", "apk保存过程出错" + throwable.getLocalizedMessage());
//                                    downloading = false;
                                    //取消dialog // TODO: 2017/10/25
//                                    progressDialog.dismiss();
//                                    cancelNotification();
                                }
                            }
                        });
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                if (throwable != null && !TextUtils.isEmpty(throwable.getLocalizedMessage())) {
                    throwable.printStackTrace();
                    Log.d("json", "下载出错" + throwable.getLocalizedMessage());
                }
            }
        });
    }

    private static String TAG = "json";

    /**
     * 保存文件到本地
     *
     * @param body 回传文件
     * @param path 保存路径
     * @return
     */
    private static boolean writeFileToSDCard(ResponseBody body, String path) {

        try {
            File file = new File(path);

            if (file.exists()) {
                if (file.delete()) {
                    Log.d("json", "文件存在:删除成功");
                } else {
                    Log.d(TAG, "文件存在:删除失败");
                }
            }
            if (file.createNewFile()) {
                Log.d(TAG, "新文件创建成功");
            } else {
                /**
                 * 下载失败清除
                 */
//                SPUtils.put(mContext, Constants.APP_VERSION_CODE, -1);
//                deleteErrorApk(mApkInfo);
                Log.d(TAG, "新文件创建失败");
            }
            Log.d(TAG, "path:" + path);

            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                byte[] fileReader = new byte[4096];
                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(file);
                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        Log.i("json", "inputStream.read(fileReader) = -1");
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;
                    Log.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                }
                outputStream.flush();
                return true;
            } catch (Exception e) {
                /**
                 * 下载失败清除
                 */
//                SPUtils.put(mContext, Constants.APP_VERSION_CODE, -1);
//                deleteErrorApk(mApkInfo);
                e.printStackTrace();
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            /**
             * 下载失败清除
             */
//            SPUtils.put(mContext, Constants.APP_VERSION_CODE, -1);
//            deleteErrorApk(mApkInfo);
            e.printStackTrace();
            return false;
        }
    }

//    private static NotificationManager notificationManager;
//    private static final int NOTIFY_ID = 1;
//    /**
//     * 取消通知
//     */
//    private static void cancelNotification() {
//        notificationManager.cancel(NOTIFY_ID);
//    }

}
