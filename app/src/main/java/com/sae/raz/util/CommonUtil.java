package com.sae.raz.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;

import com.sae.raz.R;
import com.sae.raz.ui.dialog.ConfirmDialog;


@SuppressLint("FloatMath")
public class CommonUtil {

    private static final int DEFAULT_ANIMATE_DURATION = 200;
    private static final int ANIMATION_DURATION = 500;
    // 0%
    private static final float PERCENTAGE_ZEROR = 0f;
    // 100%
    private static final float PERCENTAGE_FULL = 1f;


    public static void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    public static void animationShowFromLeft(View view) {
        Animation animation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, -PERCENTAGE_FULL,
                Animation.RELATIVE_TO_SELF, PERCENTAGE_ZEROR,
                Animation.RELATIVE_TO_SELF, PERCENTAGE_ZEROR,
                Animation.RELATIVE_TO_SELF, PERCENTAGE_ZEROR);
        animation.setDuration(DEFAULT_ANIMATE_DURATION);
        view.startAnimation(animation);
    }

    public static void animationShowFromRight(View view) {
        Animation animation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, PERCENTAGE_FULL,
                Animation.RELATIVE_TO_SELF, PERCENTAGE_ZEROR,
                Animation.RELATIVE_TO_SELF, PERCENTAGE_ZEROR,
                Animation.RELATIVE_TO_SELF, PERCENTAGE_ZEROR);
        animation.setDuration(DEFAULT_ANIMATE_DURATION);
        view.startAnimation(animation);
    }

    public static void animationBottomUp(View view) {
        Animation animation = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, PERCENTAGE_ZEROR,
                Animation.RELATIVE_TO_PARENT, PERCENTAGE_ZEROR,
                Animation.RELATIVE_TO_PARENT, PERCENTAGE_FULL,
                Animation.RELATIVE_TO_PARENT, PERCENTAGE_ZEROR);
        animation.setDuration(ANIMATION_DURATION);
        view.startAnimation(animation);
    }

    public static void animationTopDown(View view) {
        Animation animation = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, PERCENTAGE_ZEROR,
                Animation.RELATIVE_TO_PARENT, PERCENTAGE_ZEROR,
                Animation.RELATIVE_TO_PARENT, PERCENTAGE_ZEROR,
                Animation.RELATIVE_TO_PARENT, PERCENTAGE_FULL);
        animation.setDuration(ANIMATION_DURATION);
        view.startAnimation(animation);
    }

    public static void AppliationFinsh(final int time) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        }.execute();
    }

    public static void showSuccessMessage(String message, Context context) {
        final ConfirmDialog dialog = new ConfirmDialog(context);
        dialog.show();
        dialog.setSymbolIcon(R.mipmap.icon_check);
        dialog.setTitle(false);
        dialog.setMessage(message);
        dialog.setButtonText("OK");
    }

    public static void showWarningMessage(String message, Context context) {
        final ConfirmDialog dialog = new ConfirmDialog(context);
        dialog.show();
        dialog.setSymbolIcon(R.mipmap.icon_warning);
        dialog.setTitle(true);
        dialog.setMessage(message);
        dialog.setButtonText("OK");
    }
}
