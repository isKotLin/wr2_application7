package com.example.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.app.TreadApplication;
import com.example.service.WindowInfoService;
import com.example.utils.DensityUtils;
import com.example.view.SeekBarHint;
import com.vigorchip.treadmill.wr2.R;

/**
 * 调整速度和坡度的dialog
 */
public class DialogAdjustVelocityGradient implements View.OnClickListener, SeekBarHint.OnSeekBarHintProgressChangeListener {
    Context mContext;
    static Dialog dialog;
    private SeekBarHint mSeekBar, sl_seekbar;
    ImageView sp_iv_jian, sp_iv_jia, sl_iv_jian, sl_iv_jia, clear_iv;
    TextView sl_tv;

    public DialogAdjustVelocityGradient(Context context) {
        mContext = context;
    }

    public void createDialog() {
        if (dialog!=null&&dialog.isShowing())
            return;
        dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_adjust_velocity_gradient);
        dialog.getWindow().setType((WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
//        lp.alpha = 100f;
        lp.y = -DensityUtils.px2dip(mContext, 50f);
        initData();
        dialog.show();
    }

    private void initData() {
        sp_iv_jian = (ImageView) dialog.findViewById(R.id.sp_iv_jian);
        sl_tv = (TextView) dialog.findViewById(R.id.sl_tv);
        sp_iv_jia = (ImageView) dialog.findViewById(R.id.sp_iv_jia);
        sl_iv_jian = (ImageView) dialog.findViewById(R.id.sl_iv_jian);
        sl_iv_jia = (ImageView) dialog.findViewById(R.id.sl_iv_jia);
        clear_iv = (ImageView) dialog.findViewById(R.id.clear_iv);
        mSeekBar = (SeekBarHint) dialog.findViewById(R.id.seekbar);
        mSeekBar.setPopupStyle(SeekBarHint.POPUP_FOLLOW);
        mSeekBar.setProgressText((float) WindowInfoService.getmSpeed() * 10);
        mSeekBar.setLeftText((float) (TreadApplication.getInstance().MINSPEED * 10));
        mSeekBar.setRightText((float) (TreadApplication.getInstance().MAXSPEED * 10));
        mSeekBar.setOnProgressChangeListener(this);
        mSeekBar.post(new Runnable() {
            public void run() {
                mSeekBar.initShow();
                // mSeekBar.setMax();
            }
        });

        sl_seekbar = (SeekBarHint) dialog.findViewById(R.id.sl_seekbar);
        if (TreadApplication.getInstance().SLOPES == 0) {
            sl_tv.setVisibility(View.GONE);
            sl_iv_jian.setVisibility(View.GONE);
            sl_iv_jia.setVisibility(View.GONE);
            sl_seekbar.setVisibility(View.GONE);
        } else {
            sl_seekbar.setSl(true);
            sl_seekbar.setPopupStyle(SeekBarHint.POPUP_FOLLOW);
            sl_seekbar.setProgressText(WindowInfoService.getmSlopes() * 10);
            sl_seekbar.setLeftText(0);
            sl_seekbar.setRightText(TreadApplication.getInstance().SLOPES * 10);
            sl_seekbar.setOnProgressChangeListener(this);
            sl_seekbar.post(new Runnable() {
                public void run() {
                    sl_seekbar.initShow();
                }
            });
        }
        sp_iv_jian.setOnClickListener(this);
        sp_iv_jia.setOnClickListener(this);
        sl_iv_jian.setOnClickListener(this);
        sl_iv_jia.setOnClickListener(this);
        clear_iv.setOnClickListener(this);
        dialog.setOnKeyListener(onKeyListener);
    }

    /**
     * add a keylistener for progress dialog
     */
    private DialogInterface.OnKeyListener onKeyListener = new DialogInterface.OnKeyListener() {
        @Override
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                dimss();
            }
            return false;
        }
    };

    public static boolean isShowings() {
        if (dialog != null)
            return dialog.isShowing();
        return false;
    }

    public void update() {
        mSeekBar.setProgress((int) (WindowInfoService.getmSpeed() * 10 - TreadApplication.getInstance().MINSPEED * 10));
        sl_seekbar.setProgress(WindowInfoService.getmSlopes() * 10);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sp_iv_jian:
                WindowInfoService.setmSpeed(WindowInfoService.getmSpeed() - 0.1);
                break;
            case R.id.sp_iv_jia:
                WindowInfoService.setmSpeed(WindowInfoService.getmSpeed() + 0.1);
                break;
            case R.id.sl_iv_jian:
                WindowInfoService.setmSlopes(WindowInfoService.getmSlopes() - 1);
                break;
            case R.id.sl_iv_jia:
                WindowInfoService.setmSlopes(WindowInfoService.getmSlopes() + 1);
                break;
            case R.id.clear_iv:
                dimss();
                break;
        }
        update();
    }

    public static void dimss() {
        if (dialog != null && isShowings())
            dialog.dismiss();
    }

    @Override
    public String onHintTextChanged(SeekBarHint seekBarHint, float progress) {
        Log.e("YAHHAGH", "onHintTextChanged" + " progress:" + progress);
        return null;
    }

}