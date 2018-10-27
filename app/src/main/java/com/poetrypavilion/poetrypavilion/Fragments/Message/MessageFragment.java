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

import java.util.ArrayList;

public class MessageFragment extends BaseFragment {

    private ArrayList<String> TitleList = new ArrayList<>();
    private ArrayList<Fragment> Fragments = new ArrayList<>();
    private ViewPager viewpager_message_box;
    private View MessageBoxView;
    private TabLayout tab_message_box;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        MessageBoxView = inflater.inflate(R.layout.message_fragment,container,false);

        initFragmentList();
        initIDs();

        MyFragmentPagerAdapter myAdapter = new MyFragmentPagerAdapter(getChildFragmentManager(), Fragments, TitleList);
        viewpager_message_box.setAdapter(myAdapter);
        viewpager_message_box.setOffscreenPageLimit(1);

        tab_message_box.setupWithViewPager(viewpager_message_box);

        return MessageBoxView;
    }

    private void initIDs() {
        viewpager_message_box = MessageBoxView.findViewById(R.id.viewpager_message_box);
        tab_message_box = MessageBoxView.findViewById(R.id.tab_message_box);
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
