package com.sae.raz.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.sae.raz.R;
import com.sae.raz.ui.dialog.MyProgressDialog;


public class BaseActivity extends AppCompatActivity {

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
    public void onBackPressed() {
        myBack();
    }

    public void myBack() {
        finish();
        overridePendingTransition(R.anim.in_right, R.anim.out_right);
    }
}

