package com.example.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.service.WindowInfoService;
import com.vigorchip.treadmill.wr2.R;

/**
 * 设置速度
 */
public class DialogSportSpeed implements View.OnClickListener {
    Context mContext;
    static Dialog dialogs;
    LinearLayout speed_ll_add, speed_ll_sub;
    static TextView speed_tv_show;

    public DialogSportSpeed(Context context) {
        mContext = context;
    }

    public void createDialog() {
        dialogs = new Dialog(mContext);
        dialogs.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogs.setContentView(R.layout.dialog_speed_show);
        dialogs.getWindow().setType((WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
        dialogs.show();
        initSpeed();
    }

    public static boolean isShowing() {
        if (dialogs != null)
            return dialogs.isShowing();
        return false;
    }

    public static void setValue() {
        if (speed_tv_show != null)
            speed_tv_show.setText(WindowInfoService.getmSpeed() + "");
    }

    private void initSpeed() {
        speed_ll_add = (LinearLayout) dialogs.findViewById(R.id.speed_ll_add);
        speed_ll_sub = (LinearLayout) dialogs.findViewById(R.id.speed_ll_sub);
        speed_tv_show = (TextView) dialogs.findViewById(R.id.speed_tv_show);
        setValue();
        speed_ll_add.setOnClickListener(this);
        speed_ll_sub.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.speed_ll_add:
                WindowInfoService.setmSpeed(WindowInfoService.getmSpeed() + 0.1);
                break;
            case R.id.speed_ll_sub:
                WindowInfoService.setmSpeed(WindowInfoService.getmSpeed() - 0.1);
                break;
        }
//        setValue();
    }

    public static void dimss() {
        if (dialogs != null)
            dialogs.dismiss();
    }
}
