package com.micro.microvideo.api;

import com.micro.microvideo.http.HttpListResult;
import com.micro.microvideo.http.HttpResult;
import com.micro.microvideo.main.bean.MicroBean;


import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by hboxs006 on 2017/5/31.
 */

public interface ApiServer {

    /**
     * 视频分类
     */
    @GET("api/category/list")
    Observable<HttpListResult<MicroBean>> category(@Query("page") int page,
                                                   @Query("pageSize") int pageSize,
                                                   @Query("name") String name );

}
