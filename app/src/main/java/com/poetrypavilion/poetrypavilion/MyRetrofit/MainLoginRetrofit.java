package com.poetrypavilion.poetrypavilion.MyRetrofit;


import android.support.annotation.NonNull;

import com.poetrypavilion.poetrypavilion.Beans.HttpBeans.HttpCheckRegisterBean;
import com.poetrypavilion.poetrypavilion.Beans.HttpBeans.RegisterBody;
import com.poetrypavilion.poetrypavilion.Http.RequestInterFaces.InterfaceBuilder;
import com.poetrypavilion.poetrypavilion.Http.RequestInterFaces.RequestInterface;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainLoginRetrofit {
    private ResponseListener mResponseListener;
    private RequestInterface requestInterface;
    public MainLoginRetrofit(){
        this.requestInterface=InterfaceBuilder.getRequestInterfaceNewInstance();
    }

    public void RegisterReal(String email,String password){
        Call<HttpCheckRegisterBean> call = requestInterface.RegisterReal(email,password);

        call.enqueue(new Callback<HttpCheckRegisterBean>() {
            @Override
            public void onResponse(@NonNull Call<HttpCheckRegisterBean> call,
                                   @NonNull Response<HttpCheckRegisterBean> response) {
                new Thread(()-> mResponseListener.onRegisterRealBack(response.body())).start();
            }

            @Override
            public void onFailure(@NonNull Call<HttpCheckRegisterBean> call,
                                  @NonNull Throwable t) {
                new Thread(()-> mResponseListener.onRegisterRealBack(null)).start();
            }
        });
    }

    public void RegisterCheckEmail(String email){
        Call<HttpCheckRegisterBean> call = requestInterface.RegisterCheckEmail(email);
        call.enqueue(new Callback<HttpCheckRegisterBean>() {
            @Override
            public void onResponse(@NonNull Call<HttpCheckRegisterBean> call,
                                   @NonNull Response<HttpCheckRegisterBean> response) {
                new Thread(()-> mResponseListener.onCheckEmailBack(response.body())).start();
            }

            @Override
            public void onFailure(@NonNull Call<HttpCheckRegisterBean> call, @NonNull Throwable t) {
                new Thread(()-> mResponseListener.onCheckEmailBack(null)).start();
            }
        });
    }

    //定义发送请求后收到请求的监听器
    public interface ResponseListener {
        void onCheckEmailBack(HttpCheckRegisterBean httpCheckRegisterBean);
        void onRegisterRealBack(HttpCheckRegisterBean httpCheckRegisterBean);
    }

    public void setOnResponseBackListener(ResponseListener ResponseListener) {
        this.mResponseListener = ResponseListener;
    }
}
