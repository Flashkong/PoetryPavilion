package com.poetrypavilion.poetrypavilion.Http.RequestInterFaces;

import com.poetrypavilion.poetrypavilion.Beans.HttpBeans.HttpCheckRegisterBean;
import com.poetrypavilion.poetrypavilion.Beans.HttpBeans.HttpGetPoemBean;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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

    @GET("register")
    Call<HttpCheckRegisterBean> RegisterCheckEmail(@Query("user_mail") String email);

    @POST("register")
    @FormUrlEncoded
    Call<HttpCheckRegisterBean> RegisterReal(@Field("user_mail") String email, @Field("user_password") String password);

    @POST("alterOrGetProfile")
    @Multipart
    Call<HttpCheckRegisterBean> EditUserInfo(@Part("user_mail") RequestBody email, @Part("user_name") RequestBody user_name, @Part MultipartBody.Part file);
}
