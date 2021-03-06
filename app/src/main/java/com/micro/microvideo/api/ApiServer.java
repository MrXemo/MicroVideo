package com.micro.microvideo.api;

import com.micro.microvideo.http.HttpListResult;
import com.micro.microvideo.http.HttpResult;
import com.micro.microvideo.main.bean.CommentBean;
import com.micro.microvideo.main.bean.MemberBean;
import com.micro.microvideo.main.bean.MicroBean;
import com.micro.microvideo.main.bean.RoleBean;
import com.micro.microvideo.main.bean.SplashBean;
import com.micro.microvideo.main.bean.VideoBean;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by hboxs006 on 2017/5/31.
 */

public interface ApiServer {

    /**
     * 获取启动图
     */
    @GET("api/welcome")
    Observable<HttpListResult<SplashBean>> getSplashImage();

    /**
     * 视频分类
     */
    @GET("api/category/list")
    Observable<HttpListResult<MicroBean>> category(@Query("page") int page,
                                                   @Query("pageSize") int pageSize,
                                                   @Query("name") String name,
                                                   @Query("type") Integer type,
                                                   @Query("role_id") Integer user_id);

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
                                                    @Query("type") Integer type,
                                                    @Query("user_id") String role_id);

    /**
     * 获取视频评论
     */
    @GET("api/comment/find")
    Observable<HttpListResult<CommentBean>> comment();

    /**
     * 获取猜你喜欢
     */
    @GET("api/video/hobbyList")
    Observable<HttpListResult<VideoBean>> guess();

    /**
     * 注册接口
     */
    @GET("api/user/add")
    Observable<HttpResult<MemberBean>> register(@Query("invite_id") String invite);

    /**
     * 获取用户信息
     */
    @GET("api/user/findById")
    Observable<HttpResult<MemberBean>> getInfo(@Query("id") String id);

    /**
     * 支付接口
     */
    @POST("api/pay/getPayUrl")
    Observable<HttpResult<String>> payUrl(@Body RequestBody info);

    @GET("api/setmeal/list")
    Observable<HttpListResult<RoleBean>> role();

    /**
     * 下载app
     *
     * @param url
     * @return
     */
    @Streaming
    @GET
    Observable<ResponseBody> downloadApk(@Url String url);
}
