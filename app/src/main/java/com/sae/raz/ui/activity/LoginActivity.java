package com.sae.raz.ui.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.sae.raz.AppConstant;
import com.sae.raz.AppPreference;
import com.sae.raz.R;
import com.sae.raz.ui.dialog.QuestionDialog;
import com.sae.raz.util.CommonUtil;
import com.sae.raz.util.NetworkConnectionUtil;
import com.sae.raz.util.ResourceUtil;
import com.sae.raz.util.Validation;


public class LoginActivity extends BaseActivity {
    public static LoginActivity instance = null;

    EditText edt_email;
    EditText edt_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        instance = this;

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(new BitmapDrawable(getResources(), ResourceUtil.resizeIcon(R.mipmap.icon_back, 25)));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        initView();
    }

    private void initView() {
        edt_email = (EditText) findViewById(R.id.edt_email);
        edt_password = (EditText) findViewById(R.id.edt_password);

        View btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLogin();
            }
        });

        View btn_signup = findViewById(R.id.btn_signup);
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(instance, SignUpActivity.class));
            }
        });

        View btn_forgot = findViewById(R.id.btn_forgot);
        btn_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showResetPasswordDialog();
            }
        });



        String email = AppPreference.readString(AppPreference.KEY.SIGN_IN_EMAIL, "");
        String password = AppPreference.readString(AppPreference.KEY.SIGN_IN_PASSWORD, "");
        edt_email.setText(email);
        edt_password.setText(password);
    }

    private void showResetPasswordDialog() {
        final QuestionDialog dialog = new QuestionDialog(this);
        dialog.show();
        dialog.setQuestion(getString(R.string.str_reset_password));
        dialog.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                onResetPassword();
            }
        });
    }

    private void onResetPassword() {
        myDlg.show();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                myDlg.hide();
                edt_password.setText("");
            }
        }, AppConstant.SPLASH_TIME * 1000);
    }

    private void onLogin() {
        if (!isVald())
            return;

        if (!NetworkConnectionUtil.isOnline()) {
            CommonUtil.showWarningMessage(getString(R.string.msg_network_connection_error), instance);
            return;
        }

        final String email = edt_email.getText().toString().trim();
        final String password = edt_password.getText().toString().trim();
        doLogin(email, password);
    }

    public void doLogin(final String email, final String password) {
        myDlg.show();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                myDlg.hide();
                AppPreference.writeBoolean(AppPreference.KEY.SIGN_IN_AUTO, true);
                AppPreference.writeString(AppPreference.KEY.SIGN_IN_EMAIL, email);
                AppPreference.writeString(AppPreference.KEY.SIGN_IN_PASSWORD, password);

                finish();
                Intent intent = new Intent(instance, MainActivity.class);
                intent.putExtra("isFirst", true);
                startActivity(intent);
            }
        }, AppConstant.SPLASH_TIME * 1000);
    }

    private boolean isVald() {
        if (!Validation.EmptyValidation(edt_email, "No email")) {
            CommonUtil.hideKeyboard(edt_email);
            return false;
        }
        if (!Validation.EmailValid(edt_email, "Invalid email")) {
            CommonUtil.hideKeyboard(edt_email);
            return false;
        }
        if (!Validation.EmptyValidation(edt_password, "No Password")) {
            CommonUtil.hideKeyboard(edt_password);
            return false;
        }

        CommonUtil.hideKeyboard(edt_email);
        return true;
    }
}
