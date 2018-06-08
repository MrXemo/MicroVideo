package com.micro.microvideo.base;

import android.os.Handler;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.micro.microvideo.R;
import com.micro.microvideo.api.ApiServer;
import com.micro.microvideo.app.Constants;
import com.micro.microvideo.http.ApiCallback;
import com.micro.microvideo.http.HttpListResult;
import com.micro.microvideo.http.HttpMethods;
import com.micro.microvideo.http.HttpResult;
import com.micro.microvideo.http.HttpResultFunc;
import com.micro.microvideo.http.PageBean;
import com.micro.microvideo.util.ItemOffsetDecoration;
import com.micro.microvideo.util.MarginAllDecoration;
import com.micro.microvideo.util.SPUtils;
import com.micro.microvideo.util.ZRecyclerView.ZRecyclerView;
import com.zhy.adapter.recyclerview.CommonAdapter;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by hboxs006 on 2017/10/12.
 */

public abstract class ListFragment<T> extends SimpleFragment  {
    protected abstract void getData(int pageNumber);
    protected abstract CommonAdapter<T> setAdapter(List<T> list );
    protected ApiServer apiServer = HttpMethods.getInstance().create(ApiServer.class);

    @BindView(R.id.recycler_view)
    ZRecyclerView mRecycler;
    @BindView(R.id.progress)
    ProgressBar progress;
    protected String token;

    protected int pageNumber = 1;
    protected int totalPage = 1;
    private List<T> list = null;
    private CommonAdapter<T> adapter = null;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initEventAndData(View view) {
        token = (String) SPUtils.get(mContext, Constants.TOKEN, "");
        mRecycler.setLayoutManager(new GridLayoutManager(mContext,2));
        mRecycler.addItemDecoration(new MarginAllDecoration(8));

        //设置上拉刷新、 下拉加载
        mRecycler.setLoadingListener(new ZRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                pageNumber = 1;
                getData(pageNumber);
            }

            @Override
            public void onLoadMore() {
                if (totalPage >= pageNumber) {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getData(pageNumber);
                        }
                    }, 500 );
                } else {
                    mRecycler.setNoMore(true);
                }
            }
        });

        getData(pageNumber);
    }

    //更多数据
    private void moreDate(HttpListResult<T> model){
        pageNumber++;
        list.addAll(model.getData());
        mRecycler.loadMoreComplete();
        adapter.notifyDataSetChanged();
    }

    //第一页数据
    private void headData(HttpListResult<T> model){
        Log.i("json","size : " + model.getData().size());
        totalPage = model.getTotal();
        pageNumber += 1;
        list = model.getData();
        progress.setVisibility(View.GONE);
        adapter = setAdapter(list);
        mRecycler.setAdapter(adapter);
        mRecycler.refreshComplete();
        if (model.getTotal() <= 1) {
            mRecycler.setNoMore(true);
        } else {
            mRecycler.setNoMore(false);
            mRecycler.loadMoreComplete();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unSubscribe();
    }

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

    public void requestList(Observable<HttpListResult<T>> observable) {
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpListResult<T>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addSubscription(d);
                    }

                    @Override
                    public void onNext(HttpListResult<T> model) {
                        Log.i("json","size : " + model.getData().size());
                        if (pageNumber > 1) {
                            moreDate(model);
                        } else {
                            headData(model);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        errorDispose(e);
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
                        errorDispose(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void errorDispose(Throwable e){
        String msg;
        if (e instanceof SocketTimeoutException) {
            msg = "网络中断，请检查您的网络状态";
            toastShow(msg);
        } else if (e instanceof ConnectException) {
            msg = "网络中断，请检查您的网络状态";
            toastShow(msg);
        } else {
            Log.e("json","发送错误",e);
            toastShow(e.getMessage());
        }
    }
}
