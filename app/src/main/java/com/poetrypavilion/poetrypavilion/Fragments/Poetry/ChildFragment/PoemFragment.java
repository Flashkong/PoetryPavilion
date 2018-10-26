package com.poetrypavilion.poetrypavilion.Fragments.Poetry.ChildFragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.poetrypavilion.poetrypavilion.Fragments.BaseFragment;
import com.poetrypavilion.poetrypavilion.R;

public class PoemFragment extends BaseFragment {

    private View PoemView;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        PoemView = inflater.inflate(R.layout.poem_fragment,container,false);

        return PoemView;
    }
}
