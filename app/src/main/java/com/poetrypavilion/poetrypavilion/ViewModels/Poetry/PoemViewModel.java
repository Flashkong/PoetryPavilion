package com.poetrypavilion.poetrypavilion.ViewModels.Poetry;

import android.annotation.SuppressLint;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.poetrypavilion.poetrypavilion.Acache.ACache;
import com.poetrypavilion.poetrypavilion.Beans.HttpBeans.HttpGetPoemBean;
import com.poetrypavilion.poetrypavilion.Beans.Poetry.PoemDetail;
import com.poetrypavilion.poetrypavilion.Repository.PoemRepository;
import com.poetrypavilion.poetrypavilion.Utils.FileAndBitmapAndBytes;
import com.poetrypavilion.poetrypavilion.Utils.MyApplication;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import okhttp3.ResponseBody;

public class PoemViewModel extends ViewModel {
    private MutableLiveData<List<PoemDetail>> poemDetails;
    private LoadFromHttpListener loadFromHttpListener;
    private LoadFromAcacheListener loadFromAcacheListener;
    private PoemRepository poemRepository;
    private Boolean IsLoadDataOk=false;
    private int headRequestNumTotal = 0;
    private int headRequestCurrentNum=0;

    public MutableLiveData<List<PoemDetail>> getPoemDetails() {
        if(poemDetails==null){
            poemDetails=new MutableLiveData<>();
            return poemDetails;
        }else {
            return poemDetails;
        }
    }

    public void setPoemDetails(MutableLiveData<List<PoemDetail>> poemDetails) {
        this.poemDetails = poemDetails;
    }

    //在构造器里面就创建好repository
    public PoemViewModel(){
        this.poemRepository=new PoemRepository();
    }

    //UI中发送接收数据的请求
    public void getPoemsFromRepository(int top_refresh_page, TYPE type, ACache aCache){
        //需要先设置监听器，否则有可能得到监听器是null
        poemRepository.setOnResponseBackListener(new PoemRepository.ResponseListener() {
            @Override
            public void onGetUserHeaderImgBack(ResponseBody responseBody, String url) {
                //先给当前的数目加一
                headRequestCurrentNum++;
                //一个一个判断找到对应的那个,这时候poemdetail一定是有值的
                List<PoemDetail> temp = poemDetails.getValue();
                //由于前面的
                if(temp!=null){
                    if(responseBody==null){
                        //不给locallink写入信息，也不用更新缓存，这时候locallink是null，会加载默认头像
                    }else {
                        //先保存一次图片，后面每次遍历的时候就使用那张图片即可
                        String localImgLink = getLocalLink(url);
                        //保存到本地文件
                        FileAndBitmapAndBytes.ResponseBodyToFile(responseBody,localImgLink);
                        for(PoemDetail poemDetail:temp){
                            //循环遍历，对每个是同一个人发的都给他同一张图片，所以不需要break
                            if(url.equals(poemDetail.getUserImgSrcLink())){
                                //只有当返回了结果的时候才给locallink写入信息
                                poemDetail.setUserLocalLink(localImgLink);
                            }
                        }
                        //遍历完成之后，设置一次缓存
                        if(aCache.getAsObject("poems_0_20")!=null){
                            aCache.remove("poems_0_20");
                        }
                        //判断数据量，如果大于20条，那么取出20条，如果小于20条，取出全部
                        if(temp.size() >=20){
                            ArrayList<PoemDetail> t = new ArrayList<PoemDetail>(temp.subList(0,20));
                            aCache.put("poems_0_20", t);
                        }else {
                            aCache.put("poems_0_20",(ArrayList)temp);
                        }
                        //不需要在这里更新我的当前页数

                        // 判断数目，如果是最后一个请求到了，就题型前端加载数据
                        if(headRequestCurrentNum==headRequestNumTotal)
                            loadFromHttpListener.onLoadOk(false,null,false);
                    }
                }


            }

            @Override
            public void onHttpReponseBack(List<HttpGetPoemBean> httpGetPoemBeans) {
                //如果请求成功，更新数据
                if(httpGetPoemBeans!=null){
                    if(getPoemDetails().getValue()==null){
                        //声明一个Set
                        Set<String> requestHeaderSet = new HashSet<>();

                        //如果此时listview里面是空的
                        List<PoemDetail> list = CastToListPoem(httpGetPoemBeans,requestHeaderSet);
                        //getPoemDetails().setValue(list);

                        //设置缓存
                        if(aCache.getAsObject("poems_0_20")!=null){
                            aCache.remove("poems_0_20");
                        }
                        if(aCache.getAsString("top_refresh_page")!=null){
                            aCache.remove("top_refresh_page");
                        }
                        aCache.put("poems_0_20",(ArrayList)list);
                        //这时候表示我缓存的前十首诗是第一页的
                        aCache.put("top_refresh_page",String.valueOf(1));

                        //为了解决请求头像线程运行过快的问题，我在这里直接就把viewmodel里面的poemdetails给赋值
//                        poemDetails.setValue(list);
                        loadFromHttpListener.onLoadOk(true,list,true);

                        //先让前台加载数据，然后再发送请求
                        headRequestNumTotal = requestHeaderSet.size();
                        headRequestCurrentNum=0;
                        for (String str : requestHeaderSet) {
                            poemRepository.getUserHeaderImg(str);
                        }
                    }else {
                        //先搞一个set
                        Set<String> requestHeaderSet = new HashSet<>();

                        //如果listview里面不是空的，那么就加载新的数据，并且将数据添加到同一个对象里面
                        //这里不能搞一个别的对象添加进去，会明显卡顿
                        List<PoemDetail> temp = poemDetails.getValue();
                        for (HttpGetPoemBean i:httpGetPoemBeans) {
                            Objects.requireNonNull(temp).add(0,CastToPoem(i,requestHeaderSet));
                        }
                        //判断是否已经存在了，如果存在了就删除缓存
                        if(aCache.getAsObject("poems_0_20")!=null){
                            aCache.remove("poems_0_20");
                        }
                        if(aCache.getAsString("top_refresh_page")!=null){
                            aCache.remove("top_refresh_page");
                        }

                        //判断数据量，如果大于20条，那么取出20条，如果小于20条，取出全部
                        if((temp != null ? temp.size() : 0) >=20){
                            ArrayList<PoemDetail> t = new ArrayList<PoemDetail>(temp.subList(0,20));
                            aCache.put("poems_0_20", t);
                        }else {
                            aCache.put("poems_0_20",(ArrayList)temp);
                        }
                        //top_refresh_page这时候是向后端请求，因此top_refresh_page就记录了返回的最新的数据代表的页数
                        aCache.put("top_refresh_page",String.valueOf(top_refresh_page));
                        loadFromHttpListener.onLoadOk(false,null,false);

                        //先让前台加载数据，然后再发送请求
                        headRequestNumTotal = requestHeaderSet.size();
                        headRequestCurrentNum=0;
                        for(String str: requestHeaderSet){
                            poemRepository.getUserHeaderImg(str);
                        }
                    }
                }else {
                    loadFromHttpListener.onLoadFailure();
                }
            }

            @Override
            public void onAcacheResponseBack(List<PoemDetail> poemDetails) {
                if(poemDetails==null){
                    loadFromAcacheListener.onLoadFailure();
                }else {
                    loadFromAcacheListener.onLoadOk(poemDetails);
                }
            }
        });
        //发送异步的请求，因此函数不需要接收数据
        poemRepository.getPoems(top_refresh_page,type,aCache);
    }

    private String getLocalLink(String s){
        String LocalAddr = MyApplication.getFileDir()+"/"+s.replace('\\','-');
        return LocalAddr;
    }



    private List<PoemDetail> CastToListPoem (List<HttpGetPoemBean> poem_result,Set<String> requestHeaderSet){
        List<PoemDetail> poems = new ArrayList<>();
        for (HttpGetPoemBean i :
                poem_result) {
            PoemDetail poem = CastToPoem(i,requestHeaderSet);
            poems.add(poem);
        }
        return poems;
    }
    private PoemDetail CastToPoem (HttpGetPoemBean i,Set<String> requestHeaderSet){
        PoemDetail poem = new PoemDetail();
        poem.setDynasty(i.getDynasty());
        poem.setEditor(i.getEditor());
        poem.setLikeTotal(i.getLikeTotal());
        if(i.getNote()!=null){
            if(i.getNote().substring(0,1).equals("\n")){
                poem.setNote(i.getNote().substring(1));
            }else {
                poem.setNote(i.getNote());
            }
        }else {
            poem.setNote(i.getNote());
        }
        if(i.getPoetry()!=null){
            if(i.getPoetry().substring(0,1).equals("\n")){
                poem.setPoetry((i.getPoetry().substring(1)));
            }else {
                poem.setPoetry(i.getPoetry());
            }
        }else {
            poem.setPoetry(i.getPoetry());
        }
        if(i.getShangxi()!=null){
            if(i.getShangxi().substring(0,1).equals("\n")){
                poem.setShangxi((i.getShangxi().substring(1)));
            }else {
                poem.setShangxi(i.getShangxi());
            }
        }else {
            poem.setShangxi(i.getShangxi());
        }
        poem.setTitle(i.getTitle());
        if(i.getTranslate()!=null){
            if(i.getTranslate().substring(0,1).equals("\n")){
                poem.setTranslate(i.getTranslate().substring(1));
            }else {
                poem.setTranslate(i.getTranslate());
            }
        }else {
            poem.setTranslate(i.getTranslate());
        }
        poem.setUserImgSrcLink(i.getUser_avatar());
        if(!i.getUser_avatar().equals("0")){
            //Set自动判断元素是否存在
            requestHeaderSet.add(i.getUser_avatar());
        }
        return poem;
    }
    @Override
    protected void onCleared() {
        super.onCleared();
        poemDetails=null;
        poemRepository=null;
    }

    public interface LoadFromHttpListener {
        //返回数据成功
        void onLoadOk(boolean IsOldPoemListNull,List<PoemDetail> list,boolean isNeedLoadData);
        //返回数据失败
        void onLoadFailure();
    }

    public void setOnLoadFromHttpListener(LoadFromHttpListener loadFromHttpListener){
        this.loadFromHttpListener = loadFromHttpListener;
    }

    public interface LoadFromAcacheListener{
        //返回数据成功
        void onLoadOk(List<PoemDetail> poemDetails);
        //返回数据失败
        void onLoadFailure();
    }
    public void setOnLoadFromAcacheListener(LoadFromAcacheListener loadFromAcacheListener){
        this.loadFromAcacheListener =loadFromAcacheListener;
    }

    public enum TYPE{
        REFRESH,FIRSTLOAD
    }
}
