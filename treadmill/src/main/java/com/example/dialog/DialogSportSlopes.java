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
 * 改变坡度
 */
public class DialogSportSlopes implements View.OnClickListener {
    Context mContext;
    static Dialog dialog;
    LinearLayout slopes_ll_add, slopes_ll_sub;
    static TextView slopes_tv_show;

    public DialogSportSlopes(Context context) {
        mContext = context;
    }

    public void createDialog() {
        dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_slopes_show);
        dialog.getWindow().setType((WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
//
//        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
//        int width = wm.getDefaultDisplay().getWidth();
//        int height = wm.getDefaultDisplay().getHeight();
//
//        Window dialogWindow = dialog.getWindow();
//        WindowManager.LayoutParams params = dialogWindow.getAttributes(); // 获取对话框当前的参数值
//        params.height = (int) (height * 0.58);
//        params.width = (int) (width * 0.615);
//        dialogWindow.setAttributes(params);
//        dialogWindow.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
        dialog.show();
        initData();
    }

    private void initData() {
        slopes_ll_add = (LinearLayout) dialog.findViewById(R.id.slopes_ll_add);
        slopes_ll_sub = (LinearLayout) dialog.findViewById(R.id.slopes_ll_sub);
        slopes_tv_show = (TextView) dialog.findViewById(R.id.slopes_tv_show);
//        slopes_tv_show.setText(WindowInfoService.getmSlopes() > 9 ? WindowInfoService.getmSlopes() + "" : "0" + WindowInfoService.getmSlopes());
        setValues();
        slopes_ll_add.setOnClickListener(this);
        slopes_ll_sub.setOnClickListener(this);
    }
    public static boolean isShowings(){
        if (dialog!=null)
            return dialog.isShowing();
        return false;
    }
    public static void setValues(){
        if (slopes_tv_show!=null)
            slopes_tv_show.setText(WindowInfoService.getmSlopes()+"");
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.slopes_ll_add:
                WindowInfoService.setmSlopes(WindowInfoService.getmSlopes() + 1);
                break;
            case R.id.slopes_ll_sub:
                WindowInfoService.setmSlopes(WindowInfoService.getmSlopes() - 1);
                break;
        }
//        slopes_tv_show.setText(WindowInfoService.getmSlopes()>9?WindowInfoService.getmSlopes()+"":"0"+WindowInfoService.getmSlopes());
//        slopes_tv_show.setText(WindowInfoService.getmSlopes()+"");
    }

    public static void dimss() {
        if (dialog != null)
            dialog.dismiss();
    }
}