package com.poetrypavilion.poetrypavilion.MyRetrofit;

import android.support.annotation.NonNull;

import com.poetrypavilion.poetrypavilion.Beans.HttpBeans.HttpGetPoemBean;
import com.poetrypavilion.poetrypavilion.Http.RequestInterFaces.InterfaceBuilder;
import com.poetrypavilion.poetrypavilion.Http.RequestInterFaces.RequestInterface;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PoemRetrofit extends BaseRetrofit{
    private RequestInterface requestInterface;
    private ResponseListener mResponseListener;

    public PoemRetrofit(){
        this.requestInterface=InterfaceBuilder.getRequestInterfaceNewInstance();
    }

    //定义发送请求后收到请求的监听器
    public interface ResponseListener {
        void onReponseBack(List<HttpGetPoemBean> httpGetPoemBeans);
        void onUserHeaderImgBack(ResponseBody responseBody,String url);
    }

    public void setOnResponseBackListener(ResponseListener ResponseListener) {
        this.mResponseListener = ResponseListener;
    }

    public void getUserHeadImg(String url){
        Call<ResponseBody> call = requestInterface.getUserHeadImg(url);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                new Thread(
                        ()->mResponseListener.onUserHeaderImgBack(response.body(),response.raw().request().url().toString())
                ).start();
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                new Thread(
                        ()->mResponseListener.onUserHeaderImgBack(null,"")
                ).start();
            }
        });
    }

    public void getPoems(int fresh_times){
        //对 发送请求 进行封装
        Call<List<HttpGetPoemBean>> call = requestInterface.getPoems(String.valueOf(fresh_times));

        //发送网络请求(异步)
        call.enqueue(new Callback<List<HttpGetPoemBean>>() {

            //请求成功时回调
            @Override
            public void onResponse(@NonNull Call<List<HttpGetPoemBean>> call,
                                   @NonNull Response<List<HttpGetPoemBean>> response) {
                //设置监听器返回结果
                //这里回调之后居然回到了主线程，很奇怪，为了避免在主线性处理数据，需要在这里再开一个线程
                new Thread(()->mResponseListener.onReponseBack(response.body())).start();
            }

            //请求失败时回调
            @Override
            public void onFailure(@NonNull Call<List<HttpGetPoemBean>> call,
                                  @NonNull Throwable throwable) {
                new Thread(()->mResponseListener.onReponseBack(null)).start();
            }
        });
    }
}
