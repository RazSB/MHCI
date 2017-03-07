package com.sae.raz.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.view.Window;
import android.view.WindowManager;

import com.sae.raz.AppConstant;
import com.sae.raz.AppPreference;
import com.sae.raz.R;
import com.sae.raz.util.CommonUtil;
import com.sae.raz.util.NetworkConnectionUtil;

import java.util.ArrayList;


public class SplashActivity extends BaseActivity {

    private static final int PERMISSION_REQUEST_CODE_FOR_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onResume() {
        super.onResume();
        verifyPermissions();
    }

    private ArrayList<String> getNeededPermissionArray() {
        ArrayList<String> arrPermissionRequests = new ArrayList<>();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            arrPermissionRequests.add(Manifest.permission.INTERNET);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            arrPermissionRequests.add(Manifest.permission.ACCESS_NETWORK_STATE);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
            arrPermissionRequests.add(Manifest.permission.ACCESS_WIFI_STATE);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            arrPermissionRequests.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            arrPermissionRequests.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
            arrPermissionRequests.add(Manifest.permission.BLUETOOTH);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
            arrPermissionRequests.add(Manifest.permission.BLUETOOTH_ADMIN);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_BOOT_COMPLETED) != PackageManager.PERMISSION_GRANTED) {
            arrPermissionRequests.add(Manifest.permission.RECEIVE_BOOT_COMPLETED);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            arrPermissionRequests.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            arrPermissionRequests.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            arrPermissionRequests.add(Manifest.permission.CAMERA);
        }

        return arrPermissionRequests;
    }

    public void verifyPermissions() {
        ArrayList<String> arrPermissionRequests = getNeededPermissionArray();

        if (!arrPermissionRequests.isEmpty()) {
            ActivityCompat.requestPermissions(this, arrPermissionRequests.toArray(new String[arrPermissionRequests.size()]), PERMISSION_REQUEST_CODE_FOR_PERMISSION);
        } else {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    gotoNextPage();
                }
            }, AppConstant.SPLASH_TIME * 1000);
        }
    }

    private void gotoNextPage() {
        boolean bSignAuto = AppPreference.readBoolean(AppPreference.KEY.SIGN_IN_AUTO, false);
        String email = AppPreference.readString(AppPreference.KEY.SIGN_IN_EMAIL, "");
        String password = AppPreference.readString(AppPreference.KEY.SIGN_IN_PASSWORD, "");

        if (bSignAuto && !email.isEmpty() && !password.isEmpty()) {
            autoLogin(email, password);

        } else {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            SplashActivity.this.finish();
        }
    }

    private void autoLogin(final String email, final String password) {
        if (!NetworkConnectionUtil.isOnline()) {
            CommonUtil.showWarningMessage(getString(R.string.msg_network_connection_error), SplashActivity.this);
            return;
        }

        myDlg.show();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                myDlg.hide();
                AppPreference.writeBoolean(AppPreference.KEY.SIGN_IN_AUTO, true);
                AppPreference.writeString(AppPreference.KEY.SIGN_IN_EMAIL, email);
                AppPreference.writeString(AppPreference.KEY.SIGN_IN_PASSWORD, password);

                finish();
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                intent.putExtra("isFirst", true);
                startActivity(intent);
            }
        }, AppConstant.SPLASH_TIME * 1000);
    }
}
