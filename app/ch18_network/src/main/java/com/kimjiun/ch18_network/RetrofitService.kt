package com.kimjiun.ch18_network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface RetrofitService {
    @GET("api/users")
    fun doGetUserList(@Query("page") page:String): Call<UserListModel>

    @GET
    fun getAvatarImage(@Url url: String): Call<ResponseBody>

    // url 경로를 동적으로 지정
    @GET("group/{id}/users/{name}")
    fun test2(@Path("id") userId: String,
              @Path("name") arg2: String
    ): Call<UserModel>


    // 전송할 데이터가 많은경우
    @GET("group/users")
    fun test3(@QueryMap options: Map<String, String>,
              @Query("name") name : String
    ): Call<UserModel>


    // 모델 객체로 지정하고 싶은경우
    @GET("group/users")
    fun test4(@Body user: UserModel,
              @Query("name") name : String
    ): Call<UserModel>

    // URL 인코딩 형태로 전송
    @FormUrlEncoded
    @GET("group/users")
    fun test5(@Body user: UserModel,
              @Query("name") name : String
    ): Call<UserModel>

    // 데이터 여러건을 한번에 지정하고 싶은 경우
    @FormUrlEncoded
    @GET("group/users")
    fun test6(@Field("title") titles: List<String>,
              @Query("name") name : String
    ): Call<UserModel>

    // 헤더지정하고싶은경우
    @Headers("Cache-Control: max-age=640000")
    @GET("group/users")
    fun test7(@Field("title") titles: List<String>,
              @Query("name") name : String
    ): Call<UserModel>


    // baseurl을 무시하고 다른 url 지정
    @GET
    fun test8(@Url url: String,
              @Query("name") name : String
    ): Call<UserModel>
}