package com.sae.raz.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.sae.raz.AppConstant;
import com.sae.raz.AppPreference;
import com.sae.raz.R;
import com.sae.raz.ui.view.ProfileAvatarView;
import com.sae.raz.util.CommonUtil;
import com.sae.raz.util.NetworkConnectionUtil;
import com.sae.raz.util.ResourceUtil;
import com.sae.raz.util.Validation;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.FileNotFoundException;
import java.util.Locale;


public class SignUpActivity extends BaseActivity {
    public static SignUpActivity instance = null;

    Toolbar toolbar;

    EditText edt_username;
    EditText edt_email;
    EditText edt_password;

    ProfileAvatarView img_user_avatar;
    private boolean mBHasPhoto = false;
    private Bitmap mOrgBmp = null;
    private Uri mCropImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        instance = this;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
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
        edt_username = (EditText) findViewById(R.id.edt_username);
        edt_email = (EditText) findViewById(R.id.edt_email);
        edt_password = (EditText) findViewById(R.id.edt_password);

        img_user_avatar = (ProfileAvatarView) findViewById(R.id.img_user_avatar);
        img_user_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.startPickImageActivity(instance);
            }
        });

        View btn_signup = findViewById(R.id.btn_signup);
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSignup();
            }
        });

        View btn_to_login = findViewById(R.id.btn_to_login);
        btn_to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void onSignup() {
        if (!isValid())
            return;

        if (!NetworkConnectionUtil.isOnline()) {
            CommonUtil.showWarningMessage(getString(R.string.msg_network_connection_error), instance);
            return;
        }

        String username = edt_username.getText().toString().trim();
        String email = edt_email.getText().toString().trim();;
        email = email.toLowerCase(Locale.getDefault());
        String password = edt_password.getText().toString().trim();
        if (mBHasPhoto) {

        }

        doSignup(email, password, username, mOrgBmp);
    }

    public void doSignup(final String email, final String password, final String username, final Bitmap avatar) {
        myDlg.show();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                myDlg.hide();
                AppPreference.writeBoolean(AppPreference.KEY.SIGN_IN_AUTO, true);
                AppPreference.writeString(AppPreference.KEY.SIGN_IN_EMAIL, email);
                AppPreference.writeString(AppPreference.KEY.SIGN_IN_PASSWORD, password);
                AppPreference.writeString(AppPreference.KEY.SIGN_IN_FULLNAME, username);

                finish();
                Intent intent = new Intent(instance, MainActivity.class);
                intent.putExtra("isFirst", true);
                startActivity(intent);
            }
        }, AppConstant.SPLASH_TIME * 1000);
    }

    private boolean isValid() {
        if (!Validation.EmptyValidation(edt_username, "Please enter your name")) {
            CommonUtil.hideKeyboard(edt_username);
            return false;
        }
        if (!Validation.EmptyValidation(edt_email, "Email address is not valid.")) {
            CommonUtil.hideKeyboard(edt_email);
            return false;
        }
        if (!Validation.EmailValid(edt_email, "Email address is not valid.")) {
            CommonUtil.hideKeyboard(edt_email);
            return false;
        }
        if (!Validation.EmptyValidation(edt_password, "Please enter your password.")) {
            CommonUtil.hideKeyboard(edt_password);
            return false;
        }
        if (!Validation.LengthMoreValidation(edt_password, AppConstant.PASSWORD_LENGTH, "Password length should be 4 or more digits.")) {
            CommonUtil.hideKeyboard(edt_password);
            return false;
        }

        CommonUtil.hideKeyboard(edt_email);
        return true;
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // required permissions granted, start crop image activity
            startCropImageActivity(mCropImageUri);
        } else {
            Toast.makeText(instance, "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Start crop image activity for the given image.
     */
    private void startCropImageActivity(Uri imageUri) {
        Intent intent = CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .setAspectRatio(1, 1)
                .getIntent(instance);

        startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    @Override
    @SuppressLint("NewApi")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    Uri imageUri = CropImage.getPickImageResultUri(this, data);

                    // For API >= 23 we need to check specifically that we have permissions to read external storage.
                    if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                        // request permissions and handle the result in onRequestPermissionsResult()
                        mCropImageUri = imageUri;
                        requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
                    } else {
                        // no permissions required or already grunted, can start crop image activity
                        startCropImageActivity(imageUri);
                    }
                }
                break;

            case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        Bitmap bitmap = ResourceUtil.decodeUri(instance, result.getUri(), AppConstant.AVATAR_SIZE);

                        if (mOrgBmp != null)
                            mOrgBmp.recycle();
                        mOrgBmp = bitmap;

                        img_user_avatar.setImageBitmap(bitmap);
                        mBHasPhoto = true;

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Toast.makeText(instance, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(instance, "Cropping canceled", Toast.LENGTH_LONG).show();
                }
                break;

            default:
                break;
        }
    }
}
