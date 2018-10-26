package com.poetrypavilion.poetrypavilion.Fragments.Poetry.ChildFragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.poetrypavilion.poetrypavilion.Fragments.BaseFragment;
import com.poetrypavilion.poetrypavilion.R;

public class ArticalFragment extends BaseFragment {

    private View ArticalView;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        ArticalView = inflater.inflate(R.layout.artical_fragment,container,false);

        return ArticalView;
    }
}
