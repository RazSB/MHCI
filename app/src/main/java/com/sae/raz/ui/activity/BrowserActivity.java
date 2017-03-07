package com.sae.raz.ui.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.sae.raz.AppConstant;
import com.sae.raz.R;
import com.sae.raz.ui.view.MyWebView;
import com.sae.raz.util.ResourceUtil;


public class BrowserActivity extends BaseActivity {
    public static BrowserActivity instance = null;

    SeekBar seekbar_progress;
    MyWebView webView;
    TextView toolbar_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
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
        seekbar_progress = (SeekBar) findViewById(R.id.seekbar_progress);
        webView = (MyWebView) findViewById(R.id.webView);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setSupportZoom(true);

        String url = getIntent().getStringExtra(AppConstant.EK_URL);
        showUrl(url, true);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        String title = getIntent().getStringExtra(AppConstant.EK_TITLE);
        toolbar_title.setText(title);

        webView.setOnWebViewListner(new MyWebView.OnWebViewListner() {

            @Override
            public void onScrollChanged(boolean isHitBottom) {
            }

            @Override
            public void onPageLoadStarted(String url) {
                // TODO Auto-generated method stub
                seekbar_progress.setMax(100);
                seekbar_progress.setProgress(0);
                seekbar_progress.setVisibility(View.VISIBLE);

                MyWebView.AddHistory(url);
            }

            @Override
            public void onPageLoadFinished() {
                // TODO Auto-generated method stub
                seekbar_progress.setVisibility(View.GONE);
            }

            @Override
            public void onPageLoadProgressChanged(int progress) {
                // TODO Auto-generated method stub
                seekbar_progress.setProgress(progress);
            }
        });

        findViewById(R.id.btn_prev).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String url = MyWebView.GetPrevHistory();
                if (!TextUtils.isEmpty(url))
                    showUrl(url, false);
            }
        });
        findViewById(R.id.btn_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String url = MyWebView.GetNextHistory();
                if (!TextUtils.isEmpty(url))
                    showUrl(url, false);
            }
        });
        findViewById(R.id.btn_refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                webView.reload();
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // TODO Auto-generated method stub
        super.onNewIntent(intent);

        String url = getIntent().getStringExtra(AppConstant.EK_URL);
        showUrl(url, true);
        String title = getIntent().getStringExtra(AppConstant.EK_TITLE);
        toolbar_title.setText(title);
    }

    private void showUrl(String url, boolean needtoAdd) {
        if (!TextUtils.isEmpty(url)) {
            webView.loadUrl(url);

            if (needtoAdd)
                MyWebView.AddHistory(url);
        }
    }
}
