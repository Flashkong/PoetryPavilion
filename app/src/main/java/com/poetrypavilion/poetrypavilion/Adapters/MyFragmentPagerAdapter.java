package com.poetrypavilion.poetrypavilion.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<?> FragmentList;
    private List<?> TitleList;

    //这个构造函数作用在大的那三个界面上面
    public MyFragmentPagerAdapter(FragmentManager fm, List<?> mFragment) {
        super(fm);
        FragmentList = mFragment;
    }

    @Override
    public Fragment getItem(int position) {
        return (Fragment) FragmentList.get(position);
    }

    @Override
    public int getCount() {
        return FragmentList.size();
    }

    //这个构造函数使用在大页面里面的小页面上面
    public MyFragmentPagerAdapter(FragmentManager fm, List<?> mFragment, List<String> mTitleList) {
        super(fm);
        FragmentList = mFragment;
        TitleList = mTitleList;
    }

    //返回每个小页面对应的title,如果有这个函数，在使用tablayout的时候就不用再一个一个添加Tab
    @Override
    public CharSequence getPageTitle(int position) {
        if (TitleList != null && position < TitleList.size()) {
            return (CharSequence) TitleList.get(position);
        } else {
            return "";
        }
    }
}
