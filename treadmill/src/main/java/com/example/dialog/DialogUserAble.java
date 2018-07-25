package com.example.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.WindowManager;
import android.widget.TextView;

import com.vigorchip.treadmill.wr2.R;

//后门锁
public class DialogUserAble {

    Context mContext;
    Dialog dialog;
    TextView dialog_tv_error, dialog_tv_solve;

    public DialogUserAble(Context context) {
        mContext = context;
    }

    public void creatDialog() {
        if (dialog == null)
            dialog = new Dialog(mContext, R.style.Transparent);
        dialog.setCancelable(false);
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.setContentView(R.layout.dialog_door);
        dialog.show();
    }

    public void dimss() {
        if (dialog != null)
            dialog.dismiss();
    }
}
