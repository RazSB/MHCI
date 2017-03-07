package com.sae.raz.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sae.raz.R;


public class ConfirmDialog extends Dialog {
    ImageView img_symbol;
    TextView txt_title;
    TextView txt_message;
    Button btn_ok;

    public ConfirmDialog(Context parent) {
        super(parent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.dialog_confirm);

        img_symbol = (ImageView) findViewById(R.id.img_symbol);
        txt_title = (TextView) findViewById(R.id.txt_title);
        txt_message = (TextView) findViewById(R.id.txt_message);
        btn_ok = (Button) findViewById(R.id.btn_ok);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void setTitle(boolean isShow) {
        if (isShow)
            txt_title.setVisibility(View.VISIBLE);
        else
            txt_title.setVisibility(View.GONE);
    }

    public void setMessage(String message) {
        txt_message.setText(message);
    }

    public void setButtonText(String text) {
        btn_ok.setText(text);
    }

    public void setSymbolIcon(int resId) {
        img_symbol.setImageResource(resId);
    }
}
