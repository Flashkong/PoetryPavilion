package com.poetrypavilion.poetrypavilion.Fragments.Find.ChildFragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.poetrypavilion.poetrypavilion.Fragments.BaseFragment;
import com.poetrypavilion.poetrypavilion.R;

public class AttentionFragment extends BaseFragment {
    private View AttentionView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        AttentionView = inflater.inflate(R.layout.attention_fragment, container, false);

        return AttentionView;
    }
}
