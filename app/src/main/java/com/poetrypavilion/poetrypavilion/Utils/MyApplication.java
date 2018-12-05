package com.poetrypavilion.poetrypavilion.Utils;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import okhttp3.OkHttpClient;


public class MyApplication extends Application {
    @SuppressLint("StaticFieldLeak")
    private static Context context;
    private static String fileDir;

    @Override
    public void onCreate(){
        super.onCreate();
        context = getApplicationContext();
        fileDir = getFilesDir().getPath();
        Stetho.initializeWithDefaults(this);
        new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();
    }

    public static Context getContext(){
        return context;
    }

    public static String getFileDir(){
        return fileDir;
    }
}
