package com.poetrypavilion.poetrypavilion.MyRetrofit;


import android.support.annotation.NonNull;

import com.poetrypavilion.poetrypavilion.Beans.HttpBeans.HttpCheckRegisterBean;
import com.poetrypavilion.poetrypavilion.Http.RequestInterFaces.InterfaceBuilder;
import com.poetrypavilion.poetrypavilion.Http.RequestInterFaces.RequestInterface;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditUsetInfoRetrofit extends BaseRetrofit{
    private RequestInterface requestInterface;
    private ResponseListener mResponseListener;

    public EditUsetInfoRetrofit(){
        this.requestInterface=InterfaceBuilder.getRequestInterfaceNewInstance();
    }

    public void editUserInfo(File file, String name ,String email ,String file_name){
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body_name = RequestBody.create(mediaType,name);
        RequestBody body_email = RequestBody.create(mediaType,email);
        RequestBody body_file = RequestBody.create(MediaType.parse("application/octet-stream"),file);

        MultipartBody.Part filePart = MultipartBody.Part.createFormData("user_avatar",
                file_name, body_file);
        Call<HttpCheckRegisterBean> call =  requestInterface.EditUserInfo(body_email,body_name,filePart);

        call.enqueue(new Callback<HttpCheckRegisterBean>() {
            @Override
            public void onResponse(@NonNull Call<HttpCheckRegisterBean> call,
                                   @NonNull Response<HttpCheckRegisterBean> response) {
                new Thread(()->{
                    mResponseListener.onEditUserInfoBack(response.body());
                }).start();
            }

            @Override
            public void onFailure(@NonNull Call<HttpCheckRegisterBean> call, @NonNull Throwable t) {
                new Thread(()->{
                    mResponseListener.onEditUserInfoBack(null);
                }).start();
            }
        });
    }

    //定义发送请求后收到请求的监听器
    public interface ResponseListener {
        void onEditUserInfoBack(HttpCheckRegisterBean httpCheckRegisterBean);
    }

    public void setOnEditUserInfoBackListener(ResponseListener ResponseListener) {
        this.mResponseListener = ResponseListener;
    }
}
