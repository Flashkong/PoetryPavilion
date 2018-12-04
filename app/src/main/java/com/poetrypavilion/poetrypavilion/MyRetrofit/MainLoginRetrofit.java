package com.poetrypavilion.poetrypavilion.MyRetrofit;


import android.support.annotation.NonNull;

import com.poetrypavilion.poetrypavilion.Beans.HttpBeans.HttpGetUserHeaderIamge;
import com.poetrypavilion.poetrypavilion.Beans.HttpBeans.HttpMainBean;
import com.poetrypavilion.poetrypavilion.Beans.HttpBeans.LoginHttpBean;
import com.poetrypavilion.poetrypavilion.Http.RequestInterFaces.InterfaceBuilder;
import com.poetrypavilion.poetrypavilion.Http.RequestInterFaces.RequestInterface;

import java.io.File;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainLoginRetrofit {
    private ResponseListener mResponseListener;
    private RequestInterface requestInterface;
    public MainLoginRetrofit(){
        this.requestInterface=InterfaceBuilder.getRequestInterfaceNewInstance();
    }

    public void getUserHeadImg(String url){
        Call<ResponseBody> call = requestInterface.getUserHeadImg(url);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                new Thread(
                        ()->mResponseListener.onUserHeadImgBack(response.body())
                ).start();
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                new Thread(
                        ()->mResponseListener.onUserHeadImgBack(null)
                ).start();
            }
        });
    }

    public void LoginReal(String email,String password){
        Call<LoginHttpBean> call = requestInterface.LoginReal(email,password);

        call.enqueue(new Callback<LoginHttpBean>() {
            @Override
            public void onResponse(@NonNull Call<LoginHttpBean> call,
                                   @NonNull Response<LoginHttpBean> response) {
                new Thread(
                        ()-> mResponseListener.onLoginRealBack(response.body())
                ).start();
            }

            @Override
            public void onFailure(@NonNull Call<LoginHttpBean> call,
                                  @NonNull Throwable t) {
                new Thread(()-> mResponseListener.onLoginRealBack(null)).start();
            }
        });
    }

    public void RegisterReal(String email,String password){
        Call<HttpMainBean> call = requestInterface.RegisterReal(email,password);

        call.enqueue(new Callback<HttpMainBean>() {
            @Override
            public void onResponse(@NonNull Call<HttpMainBean> call,
                                   @NonNull Response<HttpMainBean> response) {
                new Thread(()-> mResponseListener.onRegisterRealBack(response.body())).start();
            }

            @Override
            public void onFailure(@NonNull Call<HttpMainBean> call,
                                  @NonNull Throwable t) {
                new Thread(()-> mResponseListener.onRegisterRealBack(null)).start();
            }
        });
    }

    public void RegisterCheckEmail(String email){
        Call<HttpMainBean> call = requestInterface.RegisterCheckEmail(email);
        call.enqueue(new Callback<HttpMainBean>() {
            @Override
            public void onResponse(@NonNull Call<HttpMainBean> call,
                                   @NonNull Response<HttpMainBean> response) {
                new Thread(()-> mResponseListener.onCheckEmailBack(response.body())).start();
            }

            @Override
            public void onFailure(@NonNull Call<HttpMainBean> call, @NonNull Throwable t) {
                new Thread(()-> mResponseListener.onCheckEmailBack(null)).start();
            }
        });
    }

    //定义发送请求后收到请求的监听器
    public interface ResponseListener {
        void onCheckEmailBack(HttpMainBean httpMainBean);
        void onRegisterRealBack(HttpMainBean httpMainBean);
        void onLoginRealBack(LoginHttpBean body);
        void onUserHeadImgBack(ResponseBody body);
    }

    public void setOnResponseBackListener(ResponseListener ResponseListener) {
        this.mResponseListener = ResponseListener;
    }
}
