package com.poetrypavilion.poetrypavilion.ViewModels.activityviewmodels;

import android.arch.lifecycle.ViewModel;

import com.poetrypavilion.poetrypavilion.Beans.HttpBeans.HttpMainBean;
import com.poetrypavilion.poetrypavilion.Repository.EditPoemRepository;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class EditPoemActivityViewModel extends ViewModel {
    private String Dynasty = "现代";
    public String PoemTitle;
    public String Text;
    public boolean isBold=false;
    public boolean isItalic =false;
    public boolean isStrikeThrough =false;
    public boolean isUnderLine=false;
    public boolean isH1=false;
    public boolean isH2=false;
    public boolean isH3=false;
    public boolean isH4=false;
    public boolean isH5=false;
    public boolean isH6=false;
    public boolean isAlignLeft=false;
    public boolean isAlignCenter=false;
    public boolean isAlignRight=false;
    public boolean isBullets=false;
    public boolean isNumbers=false;
    private EditPoemRepository editPoemRepository;
    private ResponseListener mResponseListener;

    public EditPoemActivityViewModel(){
        editPoemRepository = new EditPoemRepository();
        initListener();
    }

    private void initListener(){
        editPoemRepository.setOnEditPoemBackListener(
                (status, message) ->
                        mResponseListener.onEditPoemBack(status,message));
    }

    public HttpMainBean sendPoem(File poem,String title,String fileName){
        return editPoemRepository.sendPoem(poem,title,this.Dynasty,fileName);
    }

    //定义发送请求后收到请求的监听器
    public interface ResponseListener {
        void onEditPoemBack(boolean status,String message);
    }

    public void setOnEditPoemBackListener(ResponseListener ResponseListener) {
        this.mResponseListener = ResponseListener;
    }
}
