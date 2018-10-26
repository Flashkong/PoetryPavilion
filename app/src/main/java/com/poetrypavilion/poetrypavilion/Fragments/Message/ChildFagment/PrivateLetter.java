package com.poetrypavilion.poetrypavilion.Fragments.Message.ChildFagment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.poetrypavilion.poetrypavilion.Fragments.BaseFragment;
import com.poetrypavilion.poetrypavilion.R;

public class PrivateLetter extends BaseFragment {
    private View PrivateLetterView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        PrivateLetterView = inflater.inflate(R.layout.privateletters_fragment, container, false);

        return PrivateLetterView;
    }
}
