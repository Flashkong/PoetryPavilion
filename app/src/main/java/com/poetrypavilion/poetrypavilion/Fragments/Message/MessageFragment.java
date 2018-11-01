package com.poetrypavilion.poetrypavilion.Fragments.Message;

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
import com.poetrypavilion.poetrypavilion.Fragments.Message.ChildFagment.NoticeFragment;
import com.poetrypavilion.poetrypavilion.Fragments.Message.ChildFagment.PrivateLetter;
import com.poetrypavilion.poetrypavilion.Fragments.Poetry.ChildFragment.ArticalFragment;
import com.poetrypavilion.poetrypavilion.Fragments.Poetry.ChildFragment.PoemFragment;
import com.poetrypavilion.poetrypavilion.Fragments.Poetry.ChildFragment.VoiceFragment;
import com.poetrypavilion.poetrypavilion.R;
import com.poetrypavilion.poetrypavilion.Utils.BackHande.BackHandlerHelper;
import com.poetrypavilion.poetrypavilion.Utils.BackHande.FragmentBackHandler;
import com.poetrypavilion.poetrypavilion.databinding.MessageFragmentBinding;

import java.util.ArrayList;

public class MessageFragment extends BaseFragment<MessageFragmentBinding> {

    private ArrayList<String> TitleList = new ArrayList<>();
    private ArrayList<Fragment> Fragments = new ArrayList<>();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public int setViewXml() {
        return R.layout.message_fragment;
    }

    @Override
    public void OtherProcess() {
        initFragmentList();

        MyFragmentPagerAdapter myAdapter = new MyFragmentPagerAdapter(getChildFragmentManager(), Fragments, TitleList);
        bindingView.viewpagerMessageBox.setAdapter(myAdapter);
        bindingView.viewpagerMessageBox.setOffscreenPageLimit(1);

        bindingView.tabMessageBox.setupWithViewPager(bindingView.viewpagerMessageBox);
    }

    private void initFragmentList() {
        TitleList.clear();
        TitleList.add("通知");
        TitleList.add("私信");
        Fragments.add(new NoticeFragment());
        Fragments.add(new PrivateLetter());
    }

    @Override
    public boolean onBackPressed() {
        return BackHandlerHelper.handleBackPress(this);
    }
}
