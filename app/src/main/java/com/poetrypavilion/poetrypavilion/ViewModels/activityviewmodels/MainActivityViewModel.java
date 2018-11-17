package com.poetrypavilion.poetrypavilion.ViewModels.activityviewmodels;

import android.arch.lifecycle.ViewModel;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.poetrypavilion.poetrypavilion.Beans.HttpBeans.HttpCheckRegisterBean;
import com.poetrypavilion.poetrypavilion.Repository.MainAcitvityReposity;
import com.poetrypavilion.poetrypavilion.Repository.MainLoginRepository;
import com.poetrypavilion.poetrypavilion.Room.Entity.UserInfoTuple;

public class MainActivityViewModel extends ViewModel {
    private MainAcitvityReposity mainAcitvityReposity;
    private ResponseListener mResponseListener;
    public boolean IsLogin = false;
    public String user_name;
    public Bitmap user_Head;

    public MainActivityViewModel(){
        this.mainAcitvityReposity = new MainAcitvityReposity();
        initListener();
    }

    private void initListener(){
        mainAcitvityReposity.setOnResponseBackListener(userInfoTuple -> {
            this.IsLogin = userInfoTuple != null;
            if(IsLogin){
                user_Head = BitmapFactory.decodeByteArray(userInfoTuple.user_photo, 0,
                        userInfoTuple.user_photo.length);
                user_name = userInfoTuple.user_name;
            }

            mResponseListener.onGetLoginUserBack();
        });
    }

    public void getUserInfo(){
        mainAcitvityReposity.getUserInfo();
    }

    //定义发送请求后收到请求的监听器
    public interface ResponseListener {
        void onGetLoginUserBack();
    }

    public void setGetLoginUserBackListener(ResponseListener ResponseListener) {
        this.mResponseListener = ResponseListener;
    }
}
