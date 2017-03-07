package com.sae.raz.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.sae.raz.R;


public class QuestionDialog extends Dialog {
    TextView txt_question;
    Button btn_ok;
    Button btn_cancel;

    public QuestionDialog(Context parent) {
        super(parent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.dialog_question);

        txt_question = (TextView) findViewById(R.id.txt_question);
        btn_ok = (Button) findViewById(R.id.btn_ok);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void setQuestion(String question) {
        txt_question.setText(question);
    }

    public void setButtonText(String strOk, String strCancel) {
        btn_ok.setText(strOk);
        btn_cancel.setText(strCancel);
    }
}
