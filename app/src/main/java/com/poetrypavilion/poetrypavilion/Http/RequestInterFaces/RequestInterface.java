package com.poetrypavilion.poetrypavilion.Http.RequestInterFaces;

import com.poetrypavilion.poetrypavilion.Beans.HttpBeans.HttpGetPoemBean;
import com.poetrypavilion.poetrypavilion.Http.Config;

import java.util.List;

import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RequestInterface {

    @GET("poems")
    Call<List<HttpGetPoemBean>> getPoems(@Query("poems_index") String poems_index);
}
