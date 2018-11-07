package com.poetrypavilion.poetrypavilion.Http.RequestInterFaces;

import com.poetrypavilion.poetrypavilion.MyRetrofit.BaseRetrofit;

public class InterfaceBuilder {
    /**
     * @message 在所有的retrofit子类中使用唯一的requestInterface
     */
    private static RequestInterface requestInterface;

    public static RequestInterface getRequestInterfaceNewInstance(){
        if(requestInterface==null) {
            requestInterface = BaseRetrofit.getRetofitInstance().create(RequestInterface.class);
            return requestInterface;
        }else
            return requestInterface;
    }
}
