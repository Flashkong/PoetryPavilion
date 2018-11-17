package com.poetrypavilion.poetrypavilion.Repository;

import com.poetrypavilion.poetrypavilion.Beans.HttpBeans.HttpCheckRegisterBean;
import com.poetrypavilion.poetrypavilion.Room.Database.PoetryPavilionDB;
import com.poetrypavilion.poetrypavilion.Room.Entity.UserInfoTuple;

public class MainAcitvityReposity {
    private ResponseListener mResponseListener;

    public void getUserInfo(){
        //直接从本地数据库里面读取用户名和头像
        PoetryPavilionDB db = PoetryPavilionDB.getDatabase();
        UserInfoTuple current_user = null;
        try {
            UserInfoTuple[] infoTuples= db.loginUserHistoryDao().getUserNameAndHeadImg();
            current_user = infoTuples[0];
        }catch (Exception e){
            e.printStackTrace();
        }
        if(current_user==null){
            mResponseListener.onGetLoginUserBack(null);
        }else {
            mResponseListener.onGetLoginUserBack(current_user);
        }
    }

    //定义发送请求后收到请求的监听器
    public interface ResponseListener {
        void onGetLoginUserBack(UserInfoTuple userInfoTuple);
    }

    public void setOnResponseBackListener(ResponseListener ResponseListener) {
        this.mResponseListener = ResponseListener;
    }
}
