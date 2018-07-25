package com.example.dialog;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;

import com.example.activity.MainActivity;
import com.example.utils.FileUtils;
import com.vigorchip.treadmill.wr2.R;

/**
 * 提示更换加油瓶 yijikang
 */
public class DialogIsComeOn implements View.OnClickListener {
    AlertDialog dialogs;
    Button confirm_replaced, next_time;
    public void creatDialog(Context context) {
        if (dialogs != null)
            if (dialogs.isShowing())
                return;
        dialogs = new AlertDialog.Builder(context).create();
        dialogs.setCancelable(false);
        dialogs.show();
        dialogs.getWindow().setContentView(R.layout.dialog_is_come_on);
        confirm_replaced = (Button) dialogs.findViewById(R.id.confirm_replaced);
        next_time = (Button) dialogs.findViewById(R.id.next_time);
        String able = context.getResources().getConfiguration().locale.getLanguage();
        if (able.equals("en")) {
            confirm_replaced.setTextSize(20);
            next_time.setTextSize(20);
        }
        confirm_replaced.setOnClickListener(this);
        next_time.setOnClickListener(this);
    }

    public void dimss() {
        if (dialogs != null)
            dialogs.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm_replaced:
                FileUtils.writeText2File(MainActivity.getDATAPATH(), "0" + " " + "0" + " " + "0");
                dimss();
                break;
            case R.id.next_time:
                dimss();
                break;
        }
    }
}
