package com.micro.microvideo.base;

import android.util.Log;

import com.micro.microvideo.api.ApiServer;
import com.micro.microvideo.http.ApiCallback;
import com.micro.microvideo.http.HttpMethods;
import com.micro.microvideo.http.HttpResult;
import com.micro.microvideo.http.HttpResultFunc;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by hboxs006 on 2017/10/19.
 */

public abstract class SingleFragment<T> extends SimpleFragment {
    protected ApiServer apiServer = HttpMethods.getInstance().create(ApiServer.class);


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unSubscribe();

    }

    protected abstract ApiCallback<T> setApiCallback();

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

    public void request(Observable<HttpResult<T>> observable) {
        observable
                .map(new HttpResultFunc<T>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<T>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addSubscription(d);
                    }

                    @Override
                    public void onNext(T o) {
                        setApiCallback().onSuccess(o);
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
                            Log.e("json","发送错误",e);
                            setApiCallback().onFailure(e.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
