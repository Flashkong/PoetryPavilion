package com.poetrypavilion.poetrypavilion.Fragments.Poetry.ChildFragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.poetrypavilion.poetrypavilion.Fragments.BaseFragment;
import com.poetrypavilion.poetrypavilion.R;

public class VoiceFragment extends BaseFragment {
    private View VoiceView;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        VoiceView = inflater.inflate(R.layout.voice_fragment,container,false);

        return VoiceView;
    }
}
