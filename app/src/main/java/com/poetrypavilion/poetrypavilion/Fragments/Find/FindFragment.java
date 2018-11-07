package com.poetrypavilion.poetrypavilion.Fragments.Find;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.poetrypavilion.poetrypavilion.Adapters.MyFragmentPagerAdapter;
import com.poetrypavilion.poetrypavilion.Fragments.BaseFragment;
import com.poetrypavilion.poetrypavilion.Fragments.Find.ChildFragment.AttentionFragment;
import com.poetrypavilion.poetrypavilion.Fragments.Find.ChildFragment.PopularFragment;
import com.poetrypavilion.poetrypavilion.R;
import com.poetrypavilion.poetrypavilion.Utils.BackHandle.BackHandlerHelper;
import com.poetrypavilion.poetrypavilion.databinding.FindFragmentBinding;

import java.util.ArrayList;

public class FindFragment extends BaseFragment<FindFragmentBinding> {

    private ArrayList<String> TitleList = new ArrayList<>();
    private ArrayList<Fragment> Fragments = new ArrayList<>();

    /**
     * @param savedInstanceState
     * @message 在主活动创建的时候调用这个函数，在这里需要设置ViewModel
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public int setViewXml() {
        return R.layout.find_fragment;
    }

    @Override
    public void OtherProcess() {
        initFragmentList();

        MyFragmentPagerAdapter myAdapter = new MyFragmentPagerAdapter(getChildFragmentManager(), Fragments, TitleList);
        bindingView.viewpagerFindSomething.setAdapter(myAdapter);
        bindingView.viewpagerFindSomething.setOffscreenPageLimit(2);

        bindingView.tabFindSomething.setupWithViewPager(bindingView.viewpagerFindSomething);
    }

    private void initFragmentList() {
        TitleList.clear();
        TitleList.add("热门");
        TitleList.add("关注");
        Fragments.add(new PopularFragment());
        Fragments.add(new AttentionFragment());
    }

    @Override
    public boolean onBackPressed() {
        return BackHandlerHelper.handleBackPress(this);
    }
}
