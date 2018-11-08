package com.poetrypavilion.poetrypavilion.Fragments;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.poetrypavilion.poetrypavilion.Utils.BackHandle.FragmentBackHandler;

public abstract class BaseFragment<T extends ViewDataBinding> extends Fragment implements FragmentBackHandler {
    protected T bindingView;
    @Override
    public boolean onBackPressed() {
        return false;
    }

    /**
    * @message 在BaseFragment里面实现了一个OnCreateView的方法，这样在子类里面就不用再一个一个写了
    */
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater,@Nullable ViewGroup container
            ,@Nullable Bundle savedInstanceState) {
        bindingView = DataBindingUtil.inflate(inflater, setViewXml(), container, false);
        //调用
        OtherProcess();
        //显示正在加载界面UI
        loadAnimationOfLoad();
        //从缓存加载数据
        loadData();
        return bindingView.getRoot();
    }

    public abstract int setViewXml();

    /**
    * @message 这里写子类在OnCreateView那里需要实现的方法
    */
    public abstract void OtherProcess();

    /**
     * @message 有些Fragment并不需要loadData方法，如果没有的话就不重写这个方法
     */
    protected void loadData() {}

    protected void refreshData(){}

    protected void loadAnimationOfLoad(){}
}
