package com.poetrypavilion.poetrypavilion.Repository;

import android.widget.Toast;

import com.poetrypavilion.poetrypavilion.Beans.HttpBeans.HttpMainBean;
import com.poetrypavilion.poetrypavilion.MyRetrofit.EditPoemRetrofit;
import com.poetrypavilion.poetrypavilion.Room.Database.PoetryPavilionDB;
import com.poetrypavilion.poetrypavilion.Utils.MyApplication;

import java.io.File;

public class EditPoemRepository {
    private EditPoemRetrofit editPoemRetrofit;
    private ResponseListener mResponseListener;

    public EditPoemRepository(){
        this.editPoemRetrofit = new EditPoemRetrofit();
        initListener();
    }

    private void initListener(){
        editPoemRetrofit.setOnEditPoemBackListener(new EditPoemRetrofit.ResponseListener() {
            @Override
            public void onEditPoemBack(HttpMainBean httpMainBean) {
                if(httpMainBean==null){
                    mResponseListener.onEditPoemBack(false,"连接服务器时出错");
                }else {
                    switch (httpMainBean.getMessage()){
                        case "Fail to add poem,The mail has not existed!":
                            mResponseListener.onEditPoemBack(false,"用户不存在");
                            break;
                        case "successful to upload poem!":
                            mResponseListener.onEditPoemBack(true,"上传成功");
                            break;
                        case "fail to upload poem!":
                            mResponseListener.onEditPoemBack(false,"上传失败");
                            break;
                    }
                }
            }
        });
    }

    public HttpMainBean sendPoem(File file,String title,String Dynasty,String fileName){
        PoetryPavilionDB db = PoetryPavilionDB.getDatabase();
        String[] emails = db.loginUserHistoryDao().getCurrentLoginUserEmail();
        String[] names = db.loginUserHistoryDao().getCurrentLoginUserName();
        if(emails.length<=0)
            return new HttpMainBean(false,"没有找到当前登录用户的信息，请尝试重新登录");
        else{
            if(names.length<=0){
                return new HttpMainBean(false,"没有找到当前登录用户的信息，请尝试重新登录");
            }else {
                editPoemRetrofit.sendPoem(file,title,names[0],emails[0],Dynasty,fileName);
                return new HttpMainBean(true,"成功");
            }
        }
    }

    //定义发送请求后收到请求的监听器
    public interface ResponseListener {
        void onEditPoemBack(boolean status,String message);
    }

    public void setOnEditPoemBackListener(ResponseListener ResponseListener) {
        this.mResponseListener = ResponseListener;
    }
}
