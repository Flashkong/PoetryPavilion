package com.poetrypavilion.poetrypavilion.Repository;

import com.poetrypavilion.poetrypavilion.Beans.HttpBeans.HttpCheckRegisterBean;
import com.poetrypavilion.poetrypavilion.MyRetrofit.MainLoginRetrofit;
import com.poetrypavilion.poetrypavilion.Utils.Encrypt.RSAUtils;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import static com.poetrypavilion.poetrypavilion.Configs.Config.publicKey;

public class MainLoginRepository {
    private MainLoginRetrofit mainLoginRetrofit;
    private ResponseListener mResponseListener;

    public MainLoginRepository(){
        this.mainLoginRetrofit = new MainLoginRetrofit();
        //在这里就设置回调接口
        setListener();
    }

    private void setListener(){
        //结果回调
        mainLoginRetrofit.setOnResponseBackListener(new MainLoginRetrofit.ResponseListener() {
            @Override
            public void onCheckEmailBack(HttpCheckRegisterBean httpCheckRegisterBean) {
                //如果http不是空的话，那么就需要更改一下status
                if(httpCheckRegisterBean!=null){
                    httpCheckRegisterBean.setStatus(!httpCheckRegisterBean.isStatus());
                    mResponseListener.onCheckEmailBack(httpCheckRegisterBean);
                }else {
                    mResponseListener.onCheckEmailBack(null);
                }
            }

            @Override
            public void onRegisterRealBack(HttpCheckRegisterBean httpCheckRegisterBean) {
                if(httpCheckRegisterBean!=null){
                    mResponseListener.onRegisterRealBack(httpCheckRegisterBean);
                }else {
                    mResponseListener.onRegisterRealBack(null);
                }
            }
        });
    }

    public void RegisterCheckEmail(String email){
        //发送请求，回调接口已经实现了
        mainLoginRetrofit.RegisterCheckEmail(email);
    }

    public HttpCheckRegisterBean RegisterReal(String email, String password){
        //TODO 加密算法处理password
        String encodedData;
        try {
             encodedData= RSAUtils.publicEncrypt(password, RSAUtils.getPublicKey(publicKey));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return new HttpCheckRegisterBean(false,"加密密码时出错，请重新注册");
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
            return new HttpCheckRegisterBean(false,"加密密码时出错，请重新注册");
        }
        //回调结果在上面
        mainLoginRetrofit.RegisterReal(email,encodedData);
        return new HttpCheckRegisterBean(true,"加密密码成功");
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
