package com.poetrypavilion.poetrypavilion.Fragments;

import android.support.v4.app.Fragment;

import com.poetrypavilion.poetrypavilion.Utils.BackHande.FragmentBackHandler;

public class BaseFragment extends Fragment implements FragmentBackHandler {

    @Override
    public boolean onBackPressed() {
        return false;
    }
}
