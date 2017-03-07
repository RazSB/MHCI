package com.sae.raz.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import com.sae.raz.R;
import com.sae.raz.ui.dialog.MyProgressDialog;

import me.yokeyword.fragmentation.SupportActivity;


public class BaseFragmentActivity extends SupportActivity {

    public MyProgressDialog myDlg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myDlg = new MyProgressDialog(this);
    }

    @Override
    public void startActivity(Intent intent) {
        // TODO Auto-generated method stub
        super.startActivity(intent);
        overridePendingTransition(R.anim.in_left, R.anim.out_left);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        // TODO Auto-generated method stub
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.in_left, R.anim.out_left);
    }

    @Override
    public void onBackPressedSupport() {
        myBack();
    }

    public void myBack() {
        finish();
        overridePendingTransition(R.anim.in_right, R.anim.out_right);
    }
}

