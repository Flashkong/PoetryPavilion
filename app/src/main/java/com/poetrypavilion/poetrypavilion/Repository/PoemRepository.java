package com.poetrypavilion.poetrypavilion.Repository;

import com.poetrypavilion.poetrypavilion.Acache.ACache;
import com.poetrypavilion.poetrypavilion.Beans.HttpBeans.HttpGetPoemBean;
import com.poetrypavilion.poetrypavilion.Beans.Poetry.PoemDetail;
import com.poetrypavilion.poetrypavilion.Http.HttpConfig;
import com.poetrypavilion.poetrypavilion.MyRetrofit.PoemRetrofit;
import com.poetrypavilion.poetrypavilion.ViewModels.Poetry.PoemViewModel;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import okhttp3.ResponseBody;

/**
 * @message 在这里做数据的处理，包括model，cache，和request
 */
public class PoemRepository{
    private PoemRetrofit poemRetrofit;
    private ResponseListener mResponseListener;
    private List<HttpGetPoemBean> result_httpGetPoemBeans;
    private List<PoemDetail> result_Database_PoemBeans;

    public PoemRepository(){
        this.poemRetrofit=new PoemRetrofit();
    }

    //viewModel发送请求到这里获取数据
    public void getPoems(int fresh_times, PoemViewModel.TYPE type, ACache aCache){
        if(type.equals(PoemViewModel.TYPE.FIRSTLOAD)){
            // TODO 需要在这里查找缓存和数据库，如果没有，那么在发送请求
            List<PoemDetail> poemDetails = (List<PoemDetail>)aCache.getAsObject("poems_0_20");
            mResponseListener.onAcacheResponseBack(poemDetails);
        }else if(type.equals(PoemViewModel.TYPE.REFRESH)){
            //如果是要刷新数据，则需要从后端获取数据
            getPoemsFromHttp(fresh_times);
        }
    }


    // 向后端发送请求请求数据
    private void getPoemsFromHttp(int fresh_times){
        poemRetrofit.setOnResponseBackListener(new PoemRetrofit.ResponseListener() {
            @Override
            public void onReponseBack(List<HttpGetPoemBean> httpGetPoemBeans) {
                result_httpGetPoemBeans =httpGetPoemBeans;
                //调用另一个监听器
                mResponseListener.onHttpReponseBack(httpGetPoemBeans);
            }

            @Override
            public void onUserHeaderImgBack(ResponseBody responseBody,String url) {
                if(url.equals("")){
                    mResponseListener.onGetUserHeaderImgBack(responseBody,"");
                }else {
                    int i = HttpConfig.getIPAddress().length()+"getAvatar?user_avatar=".length();
                    String s = url.substring(i);
                    try {
                        mResponseListener.onGetUserHeaderImgBack(responseBody,URLDecoder.decode( s, "UTF-8" ));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        poemRetrofit.getPoems(fresh_times);
    }

    public void getUserHeaderImg(String link){
        poemRetrofit.getUserHeadImg(link);
    }

    //定义发送请求后收到请求的监听器
    public interface ResponseListener {
        void onHttpReponseBack(List<HttpGetPoemBean> httpGetPoemBeans);
        void onAcacheResponseBack(List<PoemDetail> poemDetails);
        void onGetUserHeaderImgBack(ResponseBody responseBody,String url);
    }

    public void setOnResponseBackListener(ResponseListener ResponseListener) {
        this.mResponseListener = ResponseListener;
    }
}
