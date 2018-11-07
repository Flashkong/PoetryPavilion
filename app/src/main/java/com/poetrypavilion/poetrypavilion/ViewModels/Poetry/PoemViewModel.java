package com.poetrypavilion.poetrypavilion.ViewModels.Poetry;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.poetrypavilion.poetrypavilion.Acache.ACache;
import com.poetrypavilion.poetrypavilion.Beans.HttpBeans.HttpGetPoemBean;
import com.poetrypavilion.poetrypavilion.Beans.Poetry.PoemDetail;
import com.poetrypavilion.poetrypavilion.Repository.PoemRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Handler;

public class PoemViewModel extends ViewModel {
    private MutableLiveData<List<PoemDetail>> poemDetails;
    private LoadFromHttpListener loadFromHttpListener;
    private LoadFromAcacheListener loadFromAcacheListener;
    private PoemRepository poemRepository;
    private Boolean IsLoadDataOk=false;

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
    public void getPoemsFromRepository(int fresh_times, TYPE type, ACache aCache){
        //需要先设置监听器，否则有可能得到监听器是null
        poemRepository.setOnResponseBackListener(new PoemRepository.ResponseListener() {
            @Override
            public void onHttpReponseBack(List<HttpGetPoemBean> httpGetPoemBeans) {
                //如果请求成功，更新数据
                if(httpGetPoemBeans!=null){
                    if(getPoemDetails().getValue()==null){
                        //如果此时listview里面是空的
                        List<PoemDetail> list = CastToListPoem(httpGetPoemBeans);
//                        getPoemDetails().setValue(list);

                        //设置缓存
                        if(aCache.getAsObject("poems_0_20")!=null){
                            aCache.remove("poems_0_20");
                        }
                        aCache.put("poems_0_20",(ArrayList)list);

                        loadFromHttpListener.onLoadOk(true,list);
                    }else {
                        //如果listview里面不是空的，那么就加载新的数据，并且将数据添加到同一个对象里面
                        //这里不能搞一个别的对象添加进去，会明显卡顿
                        List<PoemDetail> temp = poemDetails.getValue();
                        for (HttpGetPoemBean i:httpGetPoemBeans) {
                            Objects.requireNonNull(temp).add(0,CastToPoem(i));
                        }
                        //判断是否已经存在了，如果存在了就删除缓存
                        if(aCache.getAsObject("poems_0_20")!=null){
                            aCache.remove("poems_0_20");
                        }
                        //判断数据量，如果大于20条，那么取出20条，如果小于20条，取出全部
                        if((temp != null ? temp.size() : 0) >=20){
                            ArrayList<PoemDetail> t = new ArrayList<PoemDetail>(temp.subList(0,20));
                            aCache.put("poems_0_20",(ArrayList<PoemDetail>)t);
                        }else {
                            aCache.put("poems_0_20",(ArrayList)temp);
                        }
                        loadFromHttpListener.onLoadOk(false,null);
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
        poemRepository.getPoems(fresh_times,type,aCache);
    }



    private List<PoemDetail> CastToListPoem (List<HttpGetPoemBean> poem_result){
        List<PoemDetail> poems = new ArrayList<>();
        for (HttpGetPoemBean i :
                poem_result) {
            PoemDetail poem = new PoemDetail();
            poem.setDynasty(i.getDynasty());
            poem.setEditor(i.getEditor());
            poem.setLikeTotal(i.getLikeTotal());
            poem.setNote(i.getNote());
            poem.setPoetry(i.getPoetry());
            poem.setShangxi(i.getShangxi());
            poem.setTitle(i.getTitle());
            poem.setTranslate(i.getTranslate());
            poem.setUserImgSrc("");
            poems.add(poem);
        }
        return poems;
    }
    private PoemDetail CastToPoem (HttpGetPoemBean poem_result){
        PoemDetail poem = new PoemDetail();
        poem.setDynasty(poem_result.getDynasty());
        poem.setEditor(poem_result.getEditor());
        poem.setLikeTotal(poem_result.getLikeTotal());
        poem.setNote(poem_result.getNote());
        poem.setPoetry(poem_result.getPoetry());
        poem.setShangxi(poem_result.getShangxi());
        poem.setTitle(poem_result.getTitle());
        poem.setTranslate(poem_result.getTranslate());
        poem.setUserImgSrc("");
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
        void onLoadOk(boolean IsOldPoemListNull,List<PoemDetail> list);
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
