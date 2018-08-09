package com.micro.microvideo.base;


import android.util.Log;

import com.micro.microvideo.api.ApiServer;
import com.micro.microvideo.http.ApiCallback;
import com.micro.microvideo.http.ApiListCallback;
import com.micro.microvideo.http.ApiPageCallback;
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
 * Created by codeest on 2017/10/12.
 * 基于Rx的Presenter封装,控制订阅的生命周期
 */
public class RxPresenter<T extends BaseView> implements BasePresenter<T> {
    protected ApiServer apiServer = HttpMethods.getInstance().create(ApiServer.class);

    protected T mView;
    protected CompositeDisposable mCompositeDisposable;

    private void unSubscribe() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
        }
    }

    //    RxJava绑定
    private void addSubscription(Disposable d) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(d);
    }

    public<F> void request(Observable<HttpListResult<F>> observable, final ApiListCallback<F> apiCallback) {
        observable
                .map(new HttpListResultFunc<F>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<F>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addSubscription(d);
                    }

                    @Override
                    public void onNext(List<F> o) {
                        apiCallback.onSuccess(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        String msg;
                        if (e instanceof SocketTimeoutException) {
                            msg = "网络中断，请检查您的网络状态";
                            apiCallback.onFailure(msg);
                        } else if (e instanceof ConnectException) {
                            msg = "网络中断，请检查您的网络状态";
                            apiCallback.onFailure(msg);
                        } else {
                            Log.e("json","发送错误",e);
                            apiCallback.onFailure(e.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public<F> void request(Observable<HttpResult<F>> observable, final ApiCallback<F> apiCallback) {
        observable
                .map(new HttpResultFunc<F>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<F>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addSubscription(d);
                    }

                    @Override
                    public void onNext(F o) {
                        apiCallback.onSuccess(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        String msg;
                        if (e instanceof SocketTimeoutException) {
                            msg = "网络中断，请检查您的网络状态";
                            apiCallback.onFailure(msg);
                        } else if (e instanceof ConnectException) {
                            msg = "网络中断，请检查您的网络状态";
                            apiCallback.onFailure(msg);
                        } else {
                            Log.e("json","发送错误",e);
                            apiCallback.onFailure(e.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public<F> void requestPage(Observable<HttpListResult<F>> observable, final ApiPageCallback<F> apiCallback) {
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpListResult<F>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addSubscription(d);
                    }

                    @Override
                    public void onNext(HttpListResult<F> o) {
                        apiCallback.onSuccess(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        String msg;
                        if (e instanceof SocketTimeoutException) {
                            msg = "网络中断，请检查您的网络状态";
                            apiCallback.onFailure(msg);
                        } else if (e instanceof ConnectException) {
                            msg = "网络中断，请检查您的网络状态";
                            apiCallback.onFailure(msg);
                        } else {
                            Log.e("json","发送错误",e);
                            apiCallback.onFailure(e.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    @Override
    public void attachView(T view) {
        this.mView = view;
    }

    @Override
    public void detachView() {
        this.mView = null;
        unSubscribe();
    }
}
