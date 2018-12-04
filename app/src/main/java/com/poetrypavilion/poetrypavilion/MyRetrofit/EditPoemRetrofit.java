package com.poetrypavilion.poetrypavilion.MyRetrofit;


import android.support.annotation.NonNull;

import com.poetrypavilion.poetrypavilion.Beans.HttpBeans.HttpMainBean;
import com.poetrypavilion.poetrypavilion.Http.RequestInterFaces.InterfaceBuilder;
import com.poetrypavilion.poetrypavilion.Http.RequestInterFaces.RequestInterface;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditPoemRetrofit extends BaseRetrofit {
    private RequestInterface requestInterface;
    private ResponseListener mResponseListener;

    public EditPoemRetrofit(){
        this.requestInterface=InterfaceBuilder.getRequestInterfaceNewInstance();
    }

    public void sendPoem(File file,String Title,String Editor,String Uploader,String Dynasty,String fileName){
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody poem_title = RequestBody.create(mediaType,Title);
        RequestBody poem_editor = RequestBody.create(mediaType,Editor);
        RequestBody poem_uploader = RequestBody.create(mediaType,Uploader);
        RequestBody poem_dynasty = RequestBody.create(mediaType,Dynasty);
        RequestBody body_file = RequestBody.create(MediaType.parse("application/octet-stream"),file);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("poem",
                fileName, body_file);
        Call<HttpMainBean> call = requestInterface.EditPoem(poem_title,poem_editor,poem_uploader,poem_dynasty,filePart);
        call.enqueue(new Callback<HttpMainBean>() {
            @Override
            public void onResponse(@NonNull Call<HttpMainBean> call, @NonNull Response<HttpMainBean> response) {
                new Thread(()->
                        mResponseListener.onEditPoemBack(response.body())
                ).start();
            }

            @Override
            public void onFailure(@NonNull Call<HttpMainBean> call, @NonNull Throwable t) {
                new Thread(
                        ()->mResponseListener.onEditPoemBack(null)
                ).start();
            }
        });
    }

    //定义发送请求后收到请求的监听器
    public interface ResponseListener {
        void onEditPoemBack(HttpMainBean httpMainBean);
    }

    public void setOnEditPoemBackListener(ResponseListener ResponseListener) {
        this.mResponseListener = ResponseListener;
    }
}
