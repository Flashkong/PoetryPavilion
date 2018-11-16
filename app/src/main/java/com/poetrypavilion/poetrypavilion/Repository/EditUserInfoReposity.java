package com.poetrypavilion.poetrypavilion.Repository;

import android.widget.Toast;
import com.poetrypavilion.poetrypavilion.Beans.HttpBeans.HttpCheckRegisterBean;
import com.poetrypavilion.poetrypavilion.MyRetrofit.EditUsetInfoRetrofit;
import com.poetrypavilion.poetrypavilion.Room.Database.PoetryPavilionDB;
import com.poetrypavilion.poetrypavilion.Room.Entity.LoginUserHistory;
import com.poetrypavilion.poetrypavilion.Utils.MyApplication;
import java.io.File;
import java.io.StringReader;

public class EditUserInfoReposity {
    private EditUsetInfoRetrofit editUsetInfoRetrofit;
    private ResponseListener mResponseListener;
    private String email;
    private String name;
    private byte[] photo;

    public EditUserInfoReposity(){
        this.editUsetInfoRetrofit = new EditUsetInfoRetrofit();
        initListener();
    }

    private void initListener(){
        editUsetInfoRetrofit.setOnEditUserInfoBackListener(httpCheckRegisterBean -> {
            if(httpCheckRegisterBean==null){
                mResponseListener.onEditUserInfoBack(false,"连接服务器时出错");
            }else {
                if(httpCheckRegisterBean.isStatus()){
                    //先更新数据库里面保存的用户登录历史
                    UpdateUserInfoToDB();
                    mResponseListener.onEditUserInfoBack(true,"修改成功");
                }else {
                    switch (httpCheckRegisterBean.getMessage()){
                        case "Fail to alter,The mail has not existed!":
                            mResponseListener.onEditUserInfoBack(false,"用户没有注册成功，请重试！");
                            break;
                        case "Alter Failed!":
                            mResponseListener.onEditUserInfoBack(false,"修改失败，请重试！");
                            break;
                    }
                }
            }
        });
    }

    public void editUserInfo(File file, String name){
        //从数据库里面读取数据，得到当前要修改的用户的email
        String email = getEmailFromDb();
        if(email!=null){
            //记录email的值
            this.email = email;
            //建立用户名
            String file_name = email.replace(".","=");
            file_name+=".png";
            //发送请求
            editUsetInfoRetrofit.editUserInfo(file,name,email,file_name);
        }
    }

    private void UpdateUserInfoToDB(){
        PoetryPavilionDB db = PoetryPavilionDB.getDatabase();
        db.loginUserHistoryDao().updateUserLoginHistory(
                new LoginUserHistory(email,name,1,photo)
        );
    }

    private String getEmailFromDb(){
        PoetryPavilionDB db = PoetryPavilionDB.getDatabase();
        String[] emails = db.loginUserHistoryDao().getCurrentLoginUserEmail();
        if(emails.length>0)
            return emails[0];
        else{
            //TODO 这里的返回处理还要做一下，详细一下
            return null;
        }
    }

    public void storeMessage(String name,byte[] photo){
        this.name=name;
        this.photo=photo;
    }

    //定义发送请求后收到请求的监听器
    public interface ResponseListener {
        void onEditUserInfoBack(boolean status,String message);
    }

    public void setOnResponseBackListener(ResponseListener ResponseListener) {
        this.mResponseListener = ResponseListener;
    }
}
