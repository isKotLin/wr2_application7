package com.example.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.activity.MainActivity;
import com.example.app.TreadApplication;
import com.example.utils.MySharePreferences;
import com.example.view.MyLinearLayout;
import com.vigorchip.treadmill.wr2.R;

/**
 * 选择用户头像
 */
public class DialogUserIcon implements View.OnClickListener, View.OnTouchListener {
    Context mContext;
    Dialog dialog;
    int index;
    private int[] userIcon = {R.mipmap.avatar1, R.mipmap.avatar2, R.mipmap.avatar3, R.mipmap.avatar4, R.mipmap.avatar5};
    ImageView home_iv_userIcon;
    TextView dialog_tv_userName1, dialog_tv_userName2, dialog_tv_userName3, dialog_tv_userName4, dialog_tv_userName5;
    MainActivity mainActivity;
    MyLinearLayout dialog_ll_user1, dialog_ll_user2, dialog_ll_user3, dialog_ll_user4, dialog_ll_user5;
    Button first_btn, second_btn, third_btn, fourth_btn;
    int yan;

    public DialogUserIcon(Context context, ImageView iv) {
        mContext = context;
        home_iv_userIcon = iv;
    }

    public void createDialog() {
        dialog = new Dialog(mContext, R.style.Transparent);
        dialog.setContentView(R.layout.dialog_user_icon);
        dialog.setCancelable(false);
        dialog.show();
        mainActivity = new MainActivity();
        dialog_ll_user1 = (MyLinearLayout) dialog.findViewById(R.id.dialog_ll_user1);
        dialog_ll_user1.setOnClickListener(this);
        dialog_ll_user2 = (MyLinearLayout) dialog.findViewById(R.id.dialog_ll_user2);
        dialog_ll_user2.setOnClickListener(this);
        dialog_ll_user3 = (MyLinearLayout) dialog.findViewById(R.id.dialog_ll_user3);
        dialog_ll_user3.setOnClickListener(this);
        dialog_ll_user4 = (MyLinearLayout) dialog.findViewById(R.id.dialog_ll_user4);
        dialog_ll_user4.setOnClickListener(this);
        dialog_ll_user5 = (MyLinearLayout) dialog.findViewById(R.id.dialog_ll_user5);
        dialog_ll_user5.setOnClickListener(this);

        first_btn = (Button) dialog.findViewById(R.id.first_btn);
        first_btn.setOnTouchListener(this);
        second_btn = (Button) dialog.findViewById(R.id.second_btn);
        second_btn.setOnTouchListener(this);
        third_btn = (Button) dialog.findViewById(R.id.third_btn);
        third_btn.setOnTouchListener(this);
        fourth_btn = (Button) dialog.findViewById(R.id.fourth_btn);
        fourth_btn.setOnTouchListener(this);
        dialog_tv_userName1 = (TextView) dialog.findViewById(R.id.dialog_tv_userName1);
        dialog_tv_userName1.setText(new MySharePreferences(mContext, "user1").getUserName());
        dialog_tv_userName2 = (TextView) dialog.findViewById(R.id.dialog_tv_userName2);
        dialog_tv_userName2.setText(new MySharePreferences(mContext, "user2").getUserName());
        dialog_tv_userName3 = (TextView) dialog.findViewById(R.id.dialog_tv_userName3);
        dialog_tv_userName3.setText(new MySharePreferences(mContext, "user3").getUserName());
        dialog_tv_userName4 = (TextView) dialog.findViewById(R.id.dialog_tv_userName4);
        dialog_tv_userName4.setText(new MySharePreferences(mContext, "user4").getUserName());
        dialog_tv_userName5 = (TextView) dialog.findViewById(R.id.dialog_tv_userName5);
        dialog_tv_userName5.setText(new MySharePreferences(mContext, "user5").getUserName());
    }

    public void getUser(View v) {
        switch (v.getId()) {
            case R.id.dialog_ll_user1:
                index = 0;
                break;
            case R.id.dialog_ll_user2:
                index = 1;
                break;
            case R.id.dialog_ll_user3:
                index = 2;
                break;
            case R.id.dialog_ll_user4:
                index = 3;
                break;
            case R.id.dialog_ll_user5:
                index = 4;
                break;
        }
        TreadApplication.getInstance().setFirst(false);
    }

    private void getSelvce() {
        mainActivity.setUserIndex(index);
        home_iv_userIcon.setImageResource(userIcon[index]);
        if (dialog != null)
            dialog.dismiss();
    }

    @Override
    public void onClick(View v) {
        getUser(v);
        getSelvce();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {//升级配置文件
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                switch (v.getId()) {
                    case R.id.first_btn:
                        yan = yan == 0 ? 1 : 0;
                        break;
                    case R.id.second_btn:
                        yan = yan == 1 ? 2 : 0;
                        break;
                    case R.id.third_btn:
                        yan = yan == 2 ? 3 : 0;
                        break;
                    case R.id.fourth_btn:
                        yan = yan == 3 ? 4 : 0;
                        if (yan == 4)
                            try {
                                mContext.startActivity(mContext.getPackageManager().getLaunchIntentForPackage("com.vigorchip.autocopy"));
                            } catch (Exception e) {
                                Log.e("TOGO", "打开失败");
                            }
                        break;
                }
                break;
        }
        return false;
    }
}
