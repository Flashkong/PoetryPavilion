package com.poetrypavilion.poetrypavilion.Repository;

import android.widget.Toast;

import com.poetrypavilion.poetrypavilion.Beans.HttpBeans.HttpGetUserHeaderIamge;
import com.poetrypavilion.poetrypavilion.Beans.HttpBeans.HttpMainBean;
import com.poetrypavilion.poetrypavilion.Beans.HttpBeans.LoginHttpBean;
import com.poetrypavilion.poetrypavilion.MyRetrofit.MainLoginRetrofit;
import com.poetrypavilion.poetrypavilion.Room.Database.PoetryPavilionDB;
import com.poetrypavilion.poetrypavilion.Room.Entity.LoginUserHistory;
import com.poetrypavilion.poetrypavilion.Utils.Encrypt.RSAUtils;
import com.poetrypavilion.poetrypavilion.Utils.FileAndBitmapAndBytes;
import com.poetrypavilion.poetrypavilion.Utils.MyApplication;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import okhttp3.ResponseBody;

import static com.poetrypavilion.poetrypavilion.Configs.Config.publicKey;

public class MainLoginRepository {
    private MainLoginRetrofit mainLoginRetrofit;
    private ResponseListener mResponseListener;
    private String RegistEmail;
    private String LoginEmail;
    private String LoginName;
    private String UserHeaderLink;
    private byte[] UserHeaderImg;

    public String getLoginName() {
        return LoginName;
    }

    public byte[] getUserHeaderImg() {
        return UserHeaderImg;
    }

    public MainLoginRepository(){
        this.mainLoginRetrofit = new MainLoginRetrofit();
        //在这里就设置回调接口
        setListener();
    }

    private void setListener(){
        //结果回调
        mainLoginRetrofit.setOnResponseBackListener(new MainLoginRetrofit.ResponseListener() {
            @Override
            public void onCheckEmailBack(HttpMainBean httpMainBean) {
                //如果http不是空的话，那么就需要更改一下status
                if(httpMainBean !=null){
                    httpMainBean.setStatus(!httpMainBean.isStatus());
                    mResponseListener.onCheckEmailBack(httpMainBean);
                }else {
                    mResponseListener.onCheckEmailBack(null);
                }
            }

            @Override
            public void onRegisterRealBack(HttpMainBean httpMainBean) {
                if(httpMainBean ==null){
                    mResponseListener.onRegisterRealBack(false,"连接服务器时出错");
                }else {
                    if(httpMainBean.isStatus()){
                        mResponseListener.onRegisterRealBack(true,"注册成功");
                        //这时候就已经登录成功了，将登录历史信息写入数据库
                        //默认用户名是青莲居士
                        writeIntoDataBase(RegistEmail,"青莲居士",new byte[0]);
                    }else {
                        switch (httpMainBean.getMessage()){
                            case "Fail to register,The mail has existed!":
                                mResponseListener.onRegisterRealBack(false,"该邮箱已被注册");
                                break;
                            case "Register Failed!":
                                mResponseListener.onRegisterRealBack(false,"注册失败");
                                break;
                        }
                    }
                }
            }

            @Override
            public void onLoginRealBack(LoginHttpBean loginHttpBean) {
                if(loginHttpBean ==null){
                    mResponseListener.onLoginRealBack(false,"连接服务器时出错");
                }else {
                    if(loginHttpBean.isStatus()){
                        LoginName=loginHttpBean.getUser_name();
                        UserHeaderLink =loginHttpBean.getUser_avatar();
                        //这时直接发送请求，不再通过viewmodel发送
                        mainLoginRetrofit.getUserHeadImg(UserHeaderLink);
                    }else {
                        switch (loginHttpBean.getMessage()){
                            case "Fail to login,The mail has not existed!":
                                mResponseListener.onLoginRealBack(false,"邮箱不存在");
                                break;
                            case "Fail to login,The password is wrong!":
                                mResponseListener.onLoginRealBack(false,"密码错误");
                                break;
                        }
                    }
                }
            }

            @Override
            public void onUserHeadImgBack(ResponseBody body) {
                if(body==null){
                    mResponseListener.onLoginRealBack(true,"登录成功,但是获取用户头像失败");
                }else {
                    File file=FileAndBitmapAndBytes.ResponseBodyToFile(body,MyApplication.getFileDir()+"/temp.png");
                    UserHeaderImg=FileAndBitmapAndBytes.fileToBytes(file);
                    writeIntoDataBase(LoginEmail,LoginName,UserHeaderImg);
                    mResponseListener.onLoginRealBack(true,"登录成功");
                }
            }
        });
    }

    private void writeIntoDataBase(String email,String name,byte[] bytes){
        PoetryPavilionDB db = PoetryPavilionDB.getDatabase();
        LoginUserHistory history = new LoginUserHistory(email,name,1,bytes);
        db.loginUserHistoryDao().InsertLoginHistory(history);
    }

    public void RegisterCheckEmail(String email){
        //发送请求，回调接口已经实现了
        mainLoginRetrofit.RegisterCheckEmail(email);
    }

    public HttpMainBean LoginReal(String email,String password){
        String encodedData;
        try {
            encodedData= RSAUtils.publicEncrypt(password, RSAUtils.getPublicKey(publicKey));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return new HttpMainBean(false,"加密密码时出错，请重新注册");
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
            return new HttpMainBean(false,"加密密码时出错，请重新注册");
        }
        mainLoginRetrofit.LoginReal(email,encodedData);
        this.LoginEmail=email;
        return new HttpMainBean(true,"加密密码成功");
    }

    public HttpMainBean RegisterReal(String email, String password){
        String encodedData;
        try {
             encodedData= RSAUtils.publicEncrypt(password, RSAUtils.getPublicKey(publicKey));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return new HttpMainBean(false,"加密密码时出错，请重新注册");
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
            return new HttpMainBean(false,"加密密码时出错，请重新注册");
        }
        //回调结果在上面
        mainLoginRetrofit.RegisterReal(email,encodedData);
        //保存发送请求的数据
        this.RegistEmail = email;
        return new HttpMainBean(true,"加密密码成功");
    }

    //定义发送请求后收到请求的监听器
    public interface ResponseListener {
        void onCheckEmailBack(HttpMainBean httpMainBean);
        void onRegisterRealBack(boolean status,String message);
        void onLoginRealBack(boolean status,String message);
    }

    public void setOnResponseBackListener(ResponseListener ResponseListener) {
        this.mResponseListener = ResponseListener;
    }
}
