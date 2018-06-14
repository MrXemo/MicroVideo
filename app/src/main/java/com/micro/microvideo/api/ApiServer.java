package com.micro.microvideo.api;

import com.micro.microvideo.http.HttpListResult;
import com.micro.microvideo.http.HttpResult;
import com.micro.microvideo.main.bean.CommentBean;
import com.micro.microvideo.main.bean.MemberBean;
import com.micro.microvideo.main.bean.MicroBean;
import com.micro.microvideo.main.bean.VideoBean;


import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
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
                                                   @Query("name") String name,
                                                   @Query("type") Integer type);

    /**
     * 明星分类
     */
    @GET("api/star/list")
    Observable<HttpListResult<MicroBean>> actor(@Query("page") int page,
                                                @Query("pageSize") int pageSize,
                                                @Query("name") String name);

    /**
     * 视频列表
     */
    @GET("api/video/list")
    Observable<HttpListResult<VideoBean>> videoList(@Query("page") int page,
                                                    @Query("pageSize") int pageSize,
                                                    @Query("name") String name,
                                                    @Query("cid") String cid,
                                                    @Query("sid") String sid,
                                                    @Query("type") Integer type);

    /**
     * 获取视频评论
     */
    @GET("api/comment/find")
    Observable<HttpListResult<CommentBean>> comment();

    /**
     * 注册接口
     */
    @GET("api/user/add")
    Observable<HttpResult<MemberBean>> register(@Query("invite_id") String invite);

    /**
     * 支付接口
     */
    @FormUrlEncoded
    @POST("api/pay/getPayUrl")
    Observable<HttpResult<String>> payUrl(@Field("user_id") String userId,
                                          @Field("trade_type") String trade_type,
                                          @Field("total_fee") String total_fee);
}
