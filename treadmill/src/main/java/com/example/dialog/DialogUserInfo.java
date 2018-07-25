
package com.example.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app.TreadApplication;
import com.example.utils.KeyBoardUtils;
import com.example.utils.MySharePreferences;
import com.vigorchip.treadmill.wr2.R;

/**
 * 设置用户信息
 */
public class DialogUserInfo implements View.OnClickListener {
    Context mContext;
    EditText userInfo_ed_name, userInfo_ed_age, userInfo_ed_height, userInfo_ed_weight;
    Button userInfo_btn_save, userInfo_btn_cancel;
    MySharePreferences mySharePreferences;
    String mUserId;
    ImageView userInfo_iv_icon;
    Toast toast;
    View customView;
     OnInter onInter;
    boolean isDialog;

    public  void setOnInter(OnInter onInters) {
        onInter = onInters;
    }

    public DialogUserInfo() {

    }

    static Dialog dialog;

    public void createDialog(Context context,String userId) {
        if (dialog != null)
            if (dialog.isShowing())
                return;
        isDialog = true;
        mContext = context;
        dialog = new Dialog(mContext);mUserId = userId;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setTitle(null);
        dialog.setContentView(R.layout.dialog_user_info);
        dialog.show();
        initData();
    }

    private void initData() {
        userInfo_ed_name = (EditText) dialog.findViewById(R.id.userInfo_ed_name);
        userInfo_ed_age = (EditText) dialog.findViewById(R.id.userInfo_ed_age);
        userInfo_ed_age.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    TreadApplication.setMediaPlayer(R.raw.age);
                }
            }
        });
        userInfo_ed_height = (EditText) dialog.findViewById(R.id.userInfo_ed_height);
        userInfo_ed_height.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    TreadApplication.setMediaPlayer(R.raw.height);
                }
            }
        });
        userInfo_ed_weight = (EditText) dialog.findViewById(R.id.userInfo_ed_weight);
        userInfo_ed_weight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    TreadApplication.setMediaPlayer(R.raw.weight);
                }
            }
        });
        userInfo_btn_save = (Button) dialog.findViewById(R.id.userInfo_btn_save);
        userInfo_btn_cancel = (Button) dialog.findViewById(R.id.userInfo_btn_cancel);
        userInfo_iv_icon = (ImageView) dialog.findViewById(R.id.userInfo_iv_icon);
        mySharePreferences = new MySharePreferences(mContext, mUserId);
        handler.sendEmptyMessage(0);

        userInfo_btn_save.setOnClickListener(this);
        userInfo_btn_cancel.setOnClickListener(this);
        if (mUserId.equals("user1"))
            userInfo_iv_icon.setImageResource(R.mipmap.avatar1);
        else if (mUserId.equals("user2"))
            userInfo_iv_icon.setImageResource(R.mipmap.avatar2);
        else if (mUserId.equals("user3"))
            userInfo_iv_icon.setImageResource(R.mipmap.avatar3);
        else if (mUserId.equals("user4"))
            userInfo_iv_icon.setImageResource(R.mipmap.avatar4);
        else if (mUserId.equals("user5"))
            userInfo_iv_icon.setImageResource(R.mipmap.avatar5);

        setRegion(userInfo_ed_age, 15, 80);
        setRegion(userInfo_ed_height, 50, 250);
        setRegion(userInfo_ed_weight, 30, 200);
        userInfo_ed_name.setFilters(new InputFilter[]{new InputFilter.LengthFilter(5)});
        userInfo_ed_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                Toast.makeText(mContext,s.length()+"",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                Toast.makeText(mContext,s.length()+"",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void afterTextChanged(Editable s) {
//                Toast.makeText(mContext,s.length()+"",Toast.LENGTH_SHORT).show();
                if (s.length() == 5 && isDialog) {
                    customView = View.inflate(mContext, R.layout.toast_layout, null);
                    ((TextView) customView.findViewById(R.id.toast)).setText(R.string.longest);
                    toast = new Toast(mContext);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(customView);
                    toast.show();
                }
            }
        });
    }

    //private void setRegion(EditText et)
    private void setRegion(final EditText et, final int MIN_MARK, final int MAX_MARK) {
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (start > 1) {
                    if (MIN_MARK != -1 && MAX_MARK != -1) {
                        int num = Integer.parseInt(s.toString());
                        if (num > MAX_MARK) {
                            s = String.valueOf(MAX_MARK);
                            et.setText(s);
                        } else if (num < MIN_MARK)
                            s = String.valueOf(MIN_MARK);
                        return;
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && !s.equals("")) {
                    if (MIN_MARK != -1 && MAX_MARK != -1) {
                        int markVal = 0;
                        try {
                            markVal = Integer.parseInt(s.toString());
                        } catch (NumberFormatException e) {
                            markVal = 0;
                        }
                        if (markVal > MAX_MARK) {
                            Toast.makeText(mContext, mContext.getString(R.string.not_exceed) + MAX_MARK, Toast.LENGTH_SHORT).show();
                            et.setText(String.valueOf(MAX_MARK));
                        }
                        return;
                    }
                }
            }
        });
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            userInfo_ed_name.setHint(mySharePreferences.getUserName());
            userInfo_ed_age.setHint(mySharePreferences.getUserAge());
            userInfo_ed_height.setHint(mySharePreferences.getUserHeight());
            userInfo_ed_weight.setHint(mySharePreferences.getUserWeight());
        }
    };

    private void setData(String name, String age, String height, String weight) {
        if (isNull(name))
            userInfo_ed_name.setText(userInfo_ed_name.getHint());
        if (isNull(age))
            userInfo_ed_age.setText(userInfo_ed_age.getHint());
        if (isNull(height))
            userInfo_ed_height.setText(userInfo_ed_height.getHint());
        if (isNull(weight))
            userInfo_ed_weight.setText(userInfo_ed_weight.getHint());
    }

    private boolean isNull(String s) {
        if (s == null || s.length() <= 0)
            return true;
        else
            return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.userInfo_btn_save:
                isDialog = false;
                setData(userInfo_ed_name.getText().toString(), userInfo_ed_age.getText().toString(), userInfo_ed_height.getText().toString(), userInfo_ed_weight.getText().toString());
                mySharePreferences.setUserInfo(userInfo_ed_name.getText().toString(), userInfo_ed_age.getText().toString(), userInfo_ed_height.getText().toString(), userInfo_ed_weight.getText().toString());
                mySharePreferences = null;
                mySharePreferences = new MySharePreferences(mContext, mUserId);
                handler.sendEmptyMessage(1);
                onInter.setMyUser();
            case R.id.userInfo_btn_cancel:
                isDialog = false;
                dimss();
                break;
        }
        KeyBoardUtils.closeKeybord(mContext);
    }

    public static void dimss() {
        if (dialog != null)
            dialog.dismiss();
        dialog = null;
    }

    public interface OnInter {
        void setMyUser();
    }
}