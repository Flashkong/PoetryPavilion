package com.poetrypavilion.poetrypavilion.MyRetrofit;

import com.poetrypavilion.poetrypavilion.Http.Config;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class BaseRetrofit {
    /**
     * @message 在基类定义一个静态的retrofit,并且使用这唯一的retrofit来Create唯一的
     *          RequestInterface,并且在子类中可以调用这个retrofit
     */
    private static Retrofit retrofit;
    public static Retrofit getRetofitInstance(){
        //步骤4:创建Retrofit对象
        retrofit = new Retrofit.Builder()
                .baseUrl(Config.getIPAddress()) // 设置 网络请求 Url
                .addConverterFactory(GsonConverterFactory.create()) //设置使用Gson解析(记得加入依赖)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // 支持RxJava平台
                .build();
        return retrofit;
    }
}
