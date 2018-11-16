package com.poetrypavilion.poetrypavilion.ViewModels.activityviewmodels;

import android.arch.lifecycle.ViewModel;
import android.graphics.Bitmap;
import android.net.Uri;

import com.poetrypavilion.poetrypavilion.Repository.EditUserInfoReposity;
import java.io.ByteArrayOutputStream;
import java.io.File;

public class EditUserInfoActivityViewModel extends ViewModel {

    public enum Option {
        Camera, Photo, Ok
    }


    private EditUserInfoReposity editUserInfoReposity;
    private ResponseListener mResponseListener;
    public boolean IsHavePermission = false;
    public boolean IsSendRequest = false;
    public Option option;
    public Uri imageUri;
    public String Storage_URL;
    public Bitmap HeadBtmap;

    public EditUserInfoActivityViewModel(){
        this.editUserInfoReposity = new EditUserInfoReposity();
        initListener();
    }

    private void initListener(){
        editUserInfoReposity.setOnResponseBackListener((status, message) -> {
            mResponseListener.onEditUserInfoBack(status,message);
        });
    }

    public void editUserInfo(File file, String name , Bitmap bitmap){
        byte[] photo = BitmapToBytes(bitmap);
        //首先需要更新reposity里面记录的值
        editUserInfoReposity.storeMessage(name,photo);
        editUserInfoReposity.editUserInfo(file,name);
    }

    private byte[] BitmapToBytes(Bitmap bitmap)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    //定义发送请求后收到请求的监听器
    public interface ResponseListener {
        void onEditUserInfoBack(boolean status, String message);
    }

    public void setOnResponseBackListener(ResponseListener ResponseListener) {
        this.mResponseListener = ResponseListener;
    }
}
