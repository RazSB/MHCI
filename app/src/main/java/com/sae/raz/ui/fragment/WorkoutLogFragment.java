package com.sae.raz.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sae.raz.R;


public class WorkoutLogFragment extends BaseFragment {

    private static WorkoutLogFragment fragment = null;

    public static WorkoutLogFragment getInstance() {
        if (fragment == null)
            fragment = new WorkoutLogFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workout, container, false);
        initView(view);

        return view;
    }

    private void initView(View view) {

    }
}
