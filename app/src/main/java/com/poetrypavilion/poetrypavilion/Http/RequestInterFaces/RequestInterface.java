package com.poetrypavilion.poetrypavilion.Http.RequestInterFaces;

import com.poetrypavilion.poetrypavilion.Beans.HttpBeans.HttpGetUserHeaderIamge;
import com.poetrypavilion.poetrypavilion.Beans.HttpBeans.HttpMainBean;
import com.poetrypavilion.poetrypavilion.Beans.HttpBeans.HttpGetPoemBean;
import com.poetrypavilion.poetrypavilion.Beans.HttpBeans.LoginHttpBean;

import java.io.File;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RequestInterface {

    @GET("poems")
    Call<List<HttpGetPoemBean>> getPoems(@Query("poems_index") String poems_index);

    @GET("getAvatar")
    Call<ResponseBody> getUserHeadImg(@Query("user_avatar") String url);

    @GET("register")
    Call<HttpMainBean> RegisterCheckEmail(@Query("user_mail") String email);

    @POST("register")
    @FormUrlEncoded
    Call<HttpMainBean> RegisterReal(@Field("user_mail") String email, @Field("user_password") String password);

    @POST("login")
    @FormUrlEncoded
    Call<LoginHttpBean> LoginReal(@Field("mail_id") String email, @Field("password") String password);

    @POST("alterOrGetProfile")
    @Multipart
    Call<HttpMainBean> EditUserInfo(@Part("user_mail") RequestBody email, @Part("user_name") RequestBody user_name, @Part MultipartBody.Part file);

    @POST("poems")
    @Multipart
    Call<HttpMainBean> EditPoem(@Part("poem_title") RequestBody title, @Part("poem_editor") RequestBody editor,
                                @Part("poem_uploader") RequestBody email, @Part("poem_dynasty") RequestBody dynasty,
                                @Part MultipartBody.Part file);
}
