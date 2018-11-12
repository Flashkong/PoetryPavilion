package com.poetrypavilion.poetrypavilion.ViewModels.activityviewmodels;

import android.arch.lifecycle.ViewModel;
import android.os.Trace;

import com.poetrypavilion.poetrypavilion.Beans.HttpBeans.HttpCheckRegisterBean;
import com.poetrypavilion.poetrypavilion.Repository.MainLoginRepository;

public class MainLoginActivityViewModel extends ViewModel {
    private MainLoginRepository mainLoginRepository;
    private ResponseListener mResponseListener;

    public MainLoginActivityViewModel(){
        this.mainLoginRepository=new MainLoginRepository();
        setListener();
    }

    private void setListener(){
        //结果回调
        mainLoginRepository.setOnResponseBackListener(new MainLoginRepository.ResponseListener() {
            @Override
            public void onCheckEmailBack(HttpCheckRegisterBean httpCheckRegisterBean) {
                if(httpCheckRegisterBean==null){
                    mResponseListener.onCheckEmailBack(false,"连接服务器时出错");
                }else {
                    if(httpCheckRegisterBean.isStatus()){
                        mResponseListener.onCheckEmailBack(true,"邮箱可用");
                    }else{
                        mResponseListener.onCheckEmailBack(false,"该邮箱已被注册");
                    }
                }
            }

            @Override
            public void onRegisterRealBack(HttpCheckRegisterBean httpCheckRegisterBean) {
                if(httpCheckRegisterBean==null){
                    mResponseListener.onRegisterRealBack(false,"连接服务器时出错");
                }else {
                    if(httpCheckRegisterBean.isStatus()){
                        mResponseListener.onRegisterRealBack(true,"注册成功");
                    }else {
                        switch (httpCheckRegisterBean.getMessage()){
                            case "Fail to register,The mail has existed!":
                                mResponseListener.onRegisterRealBack(false,"该邮箱已被注册");
                                break;
                            case "Register Failed!":
                                mResponseListener.onRegisterRealBack(false,"注册失败");
                        }
                    }
                }
            }
        });
    }

    public HttpCheckRegisterBean RegisterReal(String email, String password){
        //监听回调在下面
        return mainLoginRepository.RegisterReal(email,password);
    }

    public void RegisterCheckEmail(String email){

        //请求数据
        mainLoginRepository.RegisterCheckEmail(email);
    }

    //定义发送请求后收到请求的监听器
    public interface ResponseListener {
        void onCheckEmailBack(boolean status, String message);
        void onRegisterRealBack(boolean status, String message);
    }

    public void setOnResponseBackListener(ResponseListener ResponseListener) {
        this.mResponseListener = ResponseListener;
    }
}
