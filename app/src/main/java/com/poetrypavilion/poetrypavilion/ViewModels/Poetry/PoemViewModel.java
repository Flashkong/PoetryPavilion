package com.poetrypavilion.poetrypavilion.ViewModels.Poetry;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import com.poetrypavilion.poetrypavilion.Beans.HttpBeans.HttpGetPoemBean;
import com.poetrypavilion.poetrypavilion.Beans.Poetry.PoemDetail;
import com.poetrypavilion.poetrypavilion.Repository.PoemRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.LongAdder;

public class PoemViewModel extends ViewModel {
    private MutableLiveData<List<PoemDetail>> poemDetails;
    private LoadResultListener loadResultListener;
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
    public void getPoemsFromRepository(int fresh_times,TYPE type){
        //发送异步的请求，因此函数不需要接收数据
        poemRepository.getPoems(fresh_times,type);
        poemRepository.setOnResponseBackListener(new PoemRepository.ResponseListener() {
            @Override
            public void onReponseBack(List<HttpGetPoemBean> httpGetPoemBeans) {
                //如果请求成功，更新数据
                if(httpGetPoemBeans!=null){
                    if(getPoemDetails().getValue()==null){
                        //如果此时listview里面是空的
                        getPoemDetails().setValue(CastToListPoem(httpGetPoemBeans));
                        loadResultListener.onLoadOk(true);
                    }else {
                        //如果listview里面不是空的，那么就加载新的数据，并且将数据添加到同一个对象里面
                        //这里不能搞一个别的对象添加进去，会明显卡顿
                        List<PoemDetail> temp = poemDetails.getValue();
                        for (HttpGetPoemBean i:httpGetPoemBeans) {
                            temp.add(0,CastToPoem(i));
                        }
                        loadResultListener.onLoadOk(false);
                    }
                }else {
                    loadResultListener.onLoadFailure();
                }
            }
        });
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

    public interface LoadResultListener {
        //返回数据成功
        void onLoadOk(boolean IsOldPoemListNull);
        //返回数据失败
        void onLoadFailure();
    }

    public void setOnLoadResultListener(LoadResultListener loadResultListener){
        this.loadResultListener=loadResultListener;
    }
    public enum TYPE{
        REFRESH,FIRSTLOAD
    }
}
