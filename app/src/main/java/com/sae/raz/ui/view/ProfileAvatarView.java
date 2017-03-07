package com.sae.raz.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.sae.raz.R;
import com.sae.raz.util.MyImageLoader;


public class ProfileAvatarView extends RelativeLayout {
    Context mContext;

    LinearLayout layer_bg;
    CircleImageView circle_avatar;
    ProgressBar pb_loading;
    String mImageUrl = null;

    public ProfileAvatarView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        if (!isInEditMode())
            init(context);
    }

    public ProfileAvatarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        if (!isInEditMode())
            init(context);
    }

    public ProfileAvatarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // TODO Auto-generated constructor stub
        if (!isInEditMode())
            init(context);
    }

    private void init(Context context) {
        mContext = context;

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.profile_avatar_view, this);

        layer_bg = (LinearLayout) findViewById(R.id.layer_bg);
        circle_avatar = (CircleImageView) findViewById(R.id.circle_avatar);
        pb_loading = (ProgressBar) findViewById(R.id.pb_loading);
        pb_loading.setVisibility(View.INVISIBLE);
    }

    public void showImage(String image_url) {
        if (image_url == null)
            image_url = "";

        if (image_url.equals(mImageUrl))
            return;

        mImageUrl = image_url;

        if (!TextUtils.isEmpty(mImageUrl)) {
            MyImageLoader.showAvatar(circle_avatar, mImageUrl, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    // Empty implementation
                    pb_loading.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    // Empty implementation
                    pb_loading.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    // Empty implementation
                    pb_loading.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    // Empty implementation
                    pb_loading.setVisibility(View.GONE);
                }
            });

        } else {
            MyImageLoader.cancel(circle_avatar);
            pb_loading.setVisibility(View.GONE);

            circle_avatar.setImageResource(R.drawable.default_image_bg);
        }
    }

    public void setImageDrawable(Drawable d) {
        if (circle_avatar != null)
            circle_avatar.setImageDrawable(d);
    }

    public void setImageResource(int resId) {
        if (circle_avatar != null)
            circle_avatar.setImageResource(resId);
    }

    public void setImageBitmap(Bitmap bm) {
        if (circle_avatar != null)
            circle_avatar.setImageBitmap(bm);
    }

    public void setCustomBackground(int resId) {
        if (layer_bg != null)
            layer_bg.setBackgroundResource(resId);
    }
}
