package com.poetrypavilion.poetrypavilion.ViewModels.activityviewmodels;

import android.arch.lifecycle.ViewModel;

import com.poetrypavilion.poetrypavilion.Beans.HttpBeans.HttpMainBean;
import com.poetrypavilion.poetrypavilion.Repository.MainLoginRepository;

public class MainLoginActivityViewModel extends ViewModel {
    private MainLoginRepository mainLoginRepository;
    private ResponseListener mResponseListener;

    public MainLoginRepository getMainLoginRepository() {
        return mainLoginRepository;
    }

    public MainLoginActivityViewModel(){
        this.mainLoginRepository=new MainLoginRepository();
        setListener();
    }

    private void setListener(){
        //结果回调
        mainLoginRepository.setOnResponseBackListener(new MainLoginRepository.ResponseListener() {
            @Override
            public void onCheckEmailBack(HttpMainBean httpMainBean) {
                if(httpMainBean ==null){
                    mResponseListener.onCheckEmailBack(false,"连接服务器时出错");
                }else {
                    if(httpMainBean.isStatus()){
                        mResponseListener.onCheckEmailBack(true,"邮箱可用");
                    }else{
                        mResponseListener.onCheckEmailBack(false,"该邮箱已被注册");
                    }
                }
            }

            @Override
            public void onRegisterRealBack(boolean status,String message) {
                mResponseListener.onRegisterRealBack(status,message);
            }

            @Override
            public void onLoginRealBack(boolean status, String message) {
                mResponseListener.onLoginRealBack(status,message);
            }
        });
    }

    public HttpMainBean RegisterReal(String email, String password){
        //监听回调在下面
        return mainLoginRepository.RegisterReal(email,password);
    }

    public HttpMainBean LoginReal(String email,String password){
        return mainLoginRepository.LoginReal(email,password);
    }

    public void RegisterCheckEmail(String email){

        //请求数据
        mainLoginRepository.RegisterCheckEmail(email);
    }

    //定义发送请求后收到请求的监听器
    public interface ResponseListener {
        void onCheckEmailBack(boolean status, String message);
        void onRegisterRealBack(boolean status, String message);
        void onLoginRealBack(boolean status,String message);
    }

    public void setOnResponseBackListener(ResponseListener ResponseListener) {
        this.mResponseListener = ResponseListener;
    }
}
