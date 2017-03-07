package com.sae.raz.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.sae.raz.AppConstant;
import com.sae.raz.AppPreference;
import com.sae.raz.R;
import com.sae.raz.ui.activity.MainActivity;
import com.sae.raz.ui.view.ProfileAvatarView;
import com.sae.raz.util.CommonUtil;
import com.sae.raz.util.NetworkConnectionUtil;
import com.sae.raz.util.ResourceUtil;
import com.sae.raz.util.Validation;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.FileNotFoundException;
import java.util.Locale;


public class ProfileFragment extends BaseFragment {

    private static ProfileFragment fragment = null;

    EditText edt_username;
    EditText edt_email;
    EditText edt_password;

    ProfileAvatarView img_user_avatar;
    private boolean mBHasPhoto = false;
    private Bitmap mOrgBmp = null;
    private Uri mCropImageUri;

    public static ProfileFragment getInstance() {
        if (fragment == null)
            fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initView(view);

        return view;
    }

    private void initView(View view) {
        edt_username = (EditText) view.findViewById(R.id.edt_username);
        edt_email = (EditText) view.findViewById(R.id.edt_email);
        edt_password = (EditText) view.findViewById(R.id.edt_password);

        img_user_avatar = (ProfileAvatarView) view.findViewById(R.id.img_user_avatar);
        img_user_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(CropImage.getPickImageChooserIntent(getContext()), CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE);
            }
        });

        View btn_signup = view.findViewById(R.id.btn_save);
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveAccount();
            }
        });

        String email = AppPreference.readString(AppPreference.KEY.SIGN_IN_EMAIL, "");
        String password = AppPreference.readString(AppPreference.KEY.SIGN_IN_PASSWORD, "");
        String username = AppPreference.readString(AppPreference.KEY.SIGN_IN_FULLNAME, "");
        edt_email.setText(email);
        edt_password.setText(password);
        edt_username.setText(username);
    }

    public void onSaveAccount() {
        if (!isValid())
            return;

        if (!NetworkConnectionUtil.isOnline()) {
            CommonUtil.showWarningMessage(getString(R.string.msg_network_connection_error), mActivity);
            return;
        }

        String username = edt_username.getText().toString().trim();
        String email = edt_email.getText().toString().trim();;
        email = email.toLowerCase(Locale.getDefault());
        String password = edt_password.getText().toString().trim();
        if (mBHasPhoto) {

        }

        doSaveAccount(email, password, username, mOrgBmp);
    }

    public void doSaveAccount(final String email, final String password, final String username, final Bitmap avatar) {
        dlg_progress.show();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                dlg_progress.hide();
                AppPreference.writeBoolean(AppPreference.KEY.SIGN_IN_AUTO, true);
                AppPreference.writeString(AppPreference.KEY.SIGN_IN_EMAIL, email);
                AppPreference.writeString(AppPreference.KEY.SIGN_IN_PASSWORD, password);
                AppPreference.writeString(AppPreference.KEY.SIGN_IN_FULLNAME, username);
                ((MainActivity)mActivity).txt_username.setText(username);

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
            Toast.makeText(mActivity, "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
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
                .getIntent(getContext());

        startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    @Override
    @SuppressLint("NewApi")
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    Uri imageUri = CropImage.getPickImageResultUri(mActivity, data);

                    // For API >= 23 we need to check specifically that we have permissions to read external storage.
                    if (CropImage.isReadExternalStoragePermissionsRequired(mActivity, imageUri)) {
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
                        Bitmap bitmap = ResourceUtil.decodeUri(mActivity, result.getUri(), AppConstant.AVATAR_SIZE);

                        if (mOrgBmp != null)
                            mOrgBmp.recycle();
                        mOrgBmp = bitmap;

                        img_user_avatar.setImageBitmap(bitmap);
                        mBHasPhoto = true;

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Toast.makeText(mActivity, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(mActivity, "Cropping canceled", Toast.LENGTH_LONG).show();
                }
                break;

            default:
                break;
        }
    }
}
