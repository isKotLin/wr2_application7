package com.example.dialog;


import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;

import com.example.utils.LanguageUtils;
import com.vigorchip.treadmill.wr2.R;

import java.util.Locale;

import static com.example.service.WindowInfoService.setTextView;

//语言切换
public class DialogLanguage implements View.OnClickListener {
     Dialog dialog_languages;
    Context mContext;

    public  boolean showing() {
        if (dialog_languages != null)
            return dialog_languages.isShowing();
        else
            return false;
    }

    public void creatDialogs(Context context) {
        if (showing())
            return;
        mContext = context;
        dialog_languages = new Dialog(context);
        dialog_languages.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_languages.setContentView(R.layout.dialog_language);
        dialog_languages.show();
        dialog_languages.findViewById(R.id.language_tv_chinese).setOnClickListener(this);
        dialog_languages.findViewById(R.id.language_tv_english).setOnClickListener(this);
    }

    public  void dimss() {
        if (dialog_languages != null) {
            dialog_languages.dismiss();
            dialog_languages = null;
            setTextView();

        }
    }

    @Override
    public void onClick(View v) {
        String able = mContext.getResources().getConfiguration().locale.getLanguage();
        switch (v.getId()) {
            case R.id.language_tv_chinese:
                if (able.equals("en")) {
                    LanguageUtils.updateLanguage(Locale.SIMPLIFIED_CHINESE);
                }
                break;
            case R.id.language_tv_english:
                if (able.equals("zh")) {
                    LanguageUtils.updateLanguage(Locale.ENGLISH);
                }
                break;
        }
        dimss();
    }
}
