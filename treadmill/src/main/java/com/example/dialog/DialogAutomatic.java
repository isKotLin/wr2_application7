package com.example.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.app.TreadApplication;
import com.vigorchip.treadmill.wr2.R;

/**
 * 自动加油 yijikang
 */

public class DialogAutomatic {
    static Dialog aldialog;
    TextView times;

    public static boolean showing() {
        if (aldialog != null)
            return aldialog.isShowing();
        else
            return false;
    }

    public void creatDialogauto(Context context, int fueqel) {
        aldialog = new Dialog(context);
        aldialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        aldialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        aldialog.setContentView(R.layout.dialog_toast);
        aldialog.setCancelable(false);
        times = (TextView) aldialog.findViewById(R.id.times);
        times.setText(TreadApplication.getInstance().OIL_COUNT - 1 - fueqel + "");
        aldialog.show();
    }

    public static void dimss() {
        if (aldialog != null && aldialog.isShowing()) {
            aldialog.dismiss();
//            aldialog = null;
        }
    }
}
