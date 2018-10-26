package com.poetrypavilion.poetrypavilion.Fragments.Find;

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
import com.poetrypavilion.poetrypavilion.Fragments.Find.ChildFragment.AttentionFragment;
import com.poetrypavilion.poetrypavilion.Fragments.Find.ChildFragment.PopularFragment;
import com.poetrypavilion.poetrypavilion.Fragments.Poetry.ChildFragment.ArticalFragment;
import com.poetrypavilion.poetrypavilion.Fragments.Poetry.ChildFragment.PoemFragment;
import com.poetrypavilion.poetrypavilion.Fragments.Poetry.ChildFragment.VoiceFragment;
import com.poetrypavilion.poetrypavilion.R;

import java.util.ArrayList;

public class FindFragment extends BaseFragment {

    private ArrayList<String> TitleList = new ArrayList<>();
    private ArrayList<Fragment> Fragments = new ArrayList<>();
    private ViewPager viewpager_find_something;
    private View FindSomethingView;
    private TabLayout tab_find_something;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        FindSomethingView = inflater.inflate(R.layout.find_fragment,container,false);

        initFragmentList();
        initIDs();

        MyFragmentPagerAdapter myAdapter = new MyFragmentPagerAdapter(getChildFragmentManager(), Fragments, TitleList);
        viewpager_find_something.setAdapter(myAdapter);
        viewpager_find_something.setOffscreenPageLimit(2);

        tab_find_something.setupWithViewPager(viewpager_find_something);

        return FindSomethingView;
    }

    private void initIDs() {
        viewpager_find_something = FindSomethingView.findViewById(R.id.viewpager_find_something);
        tab_find_something = FindSomethingView.findViewById(R.id.tab_find_something);
    }

    private void initFragmentList() {
        TitleList.clear();
        TitleList.add("热门");
        TitleList.add("关注");
        Fragments.add(new PopularFragment());
        Fragments.add(new AttentionFragment());
    }
}
