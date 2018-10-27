package com.poetrypavilion.poetrypavilion.Fragments.Poetry;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.poetrypavilion.poetrypavilion.Adapters.MyFragmentPagerAdapter;
import com.poetrypavilion.poetrypavilion.Fragments.BaseFragment;
import com.poetrypavilion.poetrypavilion.Fragments.Poetry.ChildFragment.ArticalFragment;
import com.poetrypavilion.poetrypavilion.Fragments.Poetry.ChildFragment.PoemFragment;
import com.poetrypavilion.poetrypavilion.Fragments.Poetry.ChildFragment.VoiceFragment;
import com.poetrypavilion.poetrypavilion.R;
import com.poetrypavilion.poetrypavilion.Utils.BackHande.BackHandlerHelper;
import com.poetrypavilion.poetrypavilion.Utils.BackHande.FragmentBackHandler;
import com.poetrypavilion.poetrypavilion.Utils.IndicatorLineUtil;

import java.util.ArrayList;

public class PoetryFragment extends BaseFragment{
    private ArrayList<String> TitleList = new ArrayList<>();
    private ArrayList<Fragment> Fragments = new ArrayList<>();
    private ViewPager viewpager_poetry_pavilion;
    private View PoetryView;
    private TabLayout tab_poetry_pavilion;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        PoetryView = inflater.inflate(R.layout.poetry_fragment,container,false);

        initFragmentList();
        initIDs();

        MyFragmentPagerAdapter myAdapter = new MyFragmentPagerAdapter(getChildFragmentManager(), Fragments, TitleList);
        viewpager_poetry_pavilion.setAdapter(myAdapter);
        viewpager_poetry_pavilion.setOffscreenPageLimit(2);

        //这句话是用来通知viewpager里面的内容更新了
        //myAdapter.notifyDataSetChanged();

        //已经在xml文件中设置过了
        //tab_poetry_pavilion.setTabMode(TabLayout.MODE_SCROLLABLE);

        //这里不用再添加Tab，当和viewpager搭配的时候不用再设置，但是需要重写MyFragmentPagerAdapter内的getPageTitle函数
        tab_poetry_pavilion.setupWithViewPager(viewpager_poetry_pavilion);

        /*tab_poetry_pavilion.post(new Runnable() {
            @Override
            public void run() {
                IndicatorLineUtil.reflex(tab_poetry_pavilion);
            }
        });*/
        return PoetryView;
    }

    private void initIDs() {
        viewpager_poetry_pavilion = PoetryView.findViewById(R.id.viewpager_poetry_pavilion);
        tab_poetry_pavilion = PoetryView.findViewById(R.id.tab_poetry_pavilion);
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
