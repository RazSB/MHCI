package com.sae.raz.ui.fragment;

import android.app.Activity;

import com.sae.raz.ui.activity.BaseFragmentActivity;
import com.sae.raz.ui.dialog.MyProgressDialog;

import me.yokeyword.fragmentation.SupportFragment;
import me.yokeyword.fragmentation.anim.FragmentAnimator;


public class BaseFragment extends SupportFragment {

    public MyProgressDialog dlg_progress;
    public BaseFragmentActivity mActivity;

    @Override
    protected FragmentAnimator onCreateFragmentAnimator() {
        FragmentAnimator fragmentAnimator = _mActivity.getFragmentAnimator();
        fragmentAnimator.setEnter(0);
        fragmentAnimator.setExit(0);
        return fragmentAnimator;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (BaseFragmentActivity) activity;
        dlg_progress = new MyProgressDialog(activity);
    }
}
