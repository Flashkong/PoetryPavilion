package com.poetrypavilion.poetrypavilion.Fragments.Poetry;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.poetrypavilion.poetrypavilion.Adapters.MyFragmentPagerAdapter;
import com.poetrypavilion.poetrypavilion.Fragments.BaseFragment;
import com.poetrypavilion.poetrypavilion.Fragments.Poetry.ChildFragment.ArticalFragment;
import com.poetrypavilion.poetrypavilion.Fragments.Poetry.ChildFragment.PoemFragment;
import com.poetrypavilion.poetrypavilion.Fragments.Poetry.ChildFragment.VoiceFragment;
import com.poetrypavilion.poetrypavilion.R;
import com.poetrypavilion.poetrypavilion.Utils.BackHandle.BackHandlerHelper;
import com.poetrypavilion.poetrypavilion.databinding.PoetryFragmentBinding;

import java.util.ArrayList;

public class PoetryFragment extends BaseFragment<PoetryFragmentBinding>{
    private ArrayList<String> TitleList = new ArrayList<>();
    private ArrayList<Fragment> Fragments = new ArrayList<>();
    private ViewPager viewpager_poetry_pavilion;
    private View PoetryView;
    private TabLayout tab_poetry_pavilion;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public int setViewXml() {
        return R.layout.poetry_fragment;
    }

    @Override
    public void OtherProcess() {
        initFragmentList();

        MyFragmentPagerAdapter myAdapter = new MyFragmentPagerAdapter(getChildFragmentManager(), Fragments, TitleList);
        bindingView.viewpagerPoetryPavilion.setAdapter(myAdapter);
        bindingView.viewpagerPoetryPavilion.setOffscreenPageLimit(2);

        //这句话是用来通知viewpager里面的内容更新了
        //myAdapter.notifyDataSetChanged();

        //已经在xml文件中设置过了
        //tab_poetry_pavilion.setTabMode(TabLayout.MODE_SCROLLABLE);

        //这里不用再添加Tab，当和viewpager搭配的时候不用再设置，但是需要重写MyFragmentPagerAdapter内的getPageTitle函数
        bindingView.tabPoetryPavilion.setupWithViewPager(bindingView.viewpagerPoetryPavilion);

        /*tab_poetry_pavilion.post(new Runnable() {
            @Override
            public void run() {
                IndicatorLineUtil.reflex(tab_poetry_pavilion);
            }
        });*/
    }

    private void initFragmentList() {
        TitleList.clear();
        TitleList.add("诗词");
        TitleList.add("文章");
        TitleList.add("读诗");
        Fragments.add(new PoemFragment());
        Fragments.add(new ArticalFragment());
        Fragments.add(new VoiceFragment());
    }

    @Override
    public boolean onBackPressed() {
        return BackHandlerHelper.handleBackPress(this);
    }
}
