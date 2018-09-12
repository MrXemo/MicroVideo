package com.micro.microvideo.base;

import android.util.Log;


import com.micro.microvideo.api.ApiServer;
import com.micro.microvideo.http.ApiCallback;
import com.micro.microvideo.http.ApiListCallback;
import com.micro.microvideo.http.HttpListResult;
import com.micro.microvideo.http.HttpListResultFunc;
import com.micro.microvideo.http.HttpMethods;
import com.micro.microvideo.http.HttpResult;
import com.micro.microvideo.http.HttpResultFunc;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by codeest on 16/8/11.
 * 无MVP的activity基类
 */

public abstract class SingleActivity<T> extends SimpleActivity {
    protected ApiServer apiServer = HttpMethods.getInstance().create(ApiServer.class);

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unSubscribe();
    }

    protected abstract ApiListCallback<T> setApiCallback();

    protected CompositeDisposable mCompositeDisposable;

    //    RxJava绑定
    private void addSubscription(Disposable d) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(d);
    }

    //     RxJava解除
    private void unSubscribe() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
        }
    }
    public void request(Observable<HttpListResult<T>> observable) {
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpListResult<T>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addSubscription(d);
                    }

                    @Override
                    public void onNext(HttpListResult<T> o) {
                        setApiCallback().onSuccess(o.getData());
                    }

                    @Override
                    public void onError(Throwable e) {
                        String msg;
                        if (e instanceof SocketTimeoutException) {
                            msg = "网络中断，请检查您的网络状态";
                            setApiCallback().onFailure(msg);
                        } else if (e instanceof ConnectException) {
                            msg = "网络中断，请检查您的网络状态";
                            setApiCallback().onFailure(msg);
                        } else {
                            Log.e("json", "发送错误", e);
                            setApiCallback().onFailure(e.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
