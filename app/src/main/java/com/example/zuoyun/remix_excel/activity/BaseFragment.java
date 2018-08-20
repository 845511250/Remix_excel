package com.example.zuoyun.remix_excel.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by gtgs on 2016/9/2.
 */
public abstract class BaseFragment extends Fragment {
    public abstract int getLayout();
    public abstract void initData(View view);
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayout(), container, false);
        initData(view);
        return view;
    }
    public <T extends View> T findView(int viewId) {
        return (T) getActivity().findViewById(viewId);
    }

}
