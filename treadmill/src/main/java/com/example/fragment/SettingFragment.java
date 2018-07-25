package com.example.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.RecoverySystem;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.activity.MainActivity;
import com.example.app.BaseFragment;
import com.example.app.TreadApplication;
import com.example.dialog.DialogLanguage;
import com.example.utils.MySharePreferences;
import com.example.view.MyLinearLayout;
import com.vigorchip.treadmill.wr2.R;

/**
 * 设置
 */
public class SettingFragment extends BaseFragment implements View.OnClickListener {
    View view;
    MyLinearLayout setting_ll_skin, setting_ll_tone, setting_ll_wifi, setting_ll_opera, setting_ll_clear, setting_ll_recover,setting_ll_language;
    ImageView setting_iv_tone;
    TextView setting_tv_tone;
    MainActivity mainActivity;
    int[] ResID = {R.mipmap.wallpaper1, R.mipmap.wallpaper2, R.mipmap.wallpaper3, R.mipmap.wallpaper4, R.mipmap.wallpaper5};
    private int currIndex;
    private boolean isSelector;
    private static MySharePreferences mySharePreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settings, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mainActivity = (MainActivity) getActivity();
        mainActivity.setTitles(getString(R.string.shortcut_settings));
        initData();
    }

    private void initData() {
        setting_ll_skin = (MyLinearLayout) view.findViewById(R.id.setting_ll_skin);
        setting_ll_tone = (MyLinearLayout) view.findViewById(R.id.setting_ll_tone);
        setting_iv_tone = (ImageView) view.findViewById(R.id.setting_iv_tone);
        setting_tv_tone = (TextView) view.findViewById(R.id.setting_tv_tone);
        setting_ll_wifi = (MyLinearLayout) view.findViewById(R.id.setting_ll_wifi);
        setting_ll_opera = (MyLinearLayout) view.findViewById(R.id.setting_ll_opera);
        setting_ll_clear = (MyLinearLayout) view.findViewById(R.id.setting_ll_clear);
        setting_ll_language = (MyLinearLayout) view.findViewById(R.id.setting_ll_language);
        setting_ll_recover = (MyLinearLayout) view.findViewById(R.id.setting_ll_recover);
        setting_ll_skin.setOnClickListener(this);
        setting_ll_tone.setOnClickListener(this);
        setting_ll_wifi.setOnClickListener(this);
        setting_ll_opera.setOnClickListener(this);
        setting_ll_clear.setOnClickListener(this);
        setting_ll_recover.setOnClickListener(this);
        setting_ll_language.setOnClickListener(this);
        mySharePreferences = new MySharePreferences(getContext(), "else");
        currIndex = mySharePreferences.getWall();
        mainActivity.activity_main_ll_background.setBackground(getActivity().getResources().getDrawable(ResID[currIndex % ResID.length]));
        getTone();
    }

    public void getBTN(View v) {
        switch (v.getId()) {
            case R.id.setting_ll_skin:
                setting_ll_skin.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.sclae_up_renew));
                mainActivity.activity_main_ll_background.setBackground(getActivity().getResources().getDrawable(ResID[++currIndex % ResID.length]));
                if (currIndex == ResID.length)
                    currIndex = 0;
                mySharePreferences.setWall(currIndex);
                break;
            case R.id.setting_ll_tone:
                setting_ll_tone.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.sclae_up_renew));
                changTone();
                break;
            case R.id.setting_ll_wifi:
                setting_ll_wifi.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.sclae_up_renew));
                startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
                break;
            case R.id.setting_ll_opera:
                ((MainActivity) getActivity()).setCurrentIndex(7);
                break;
            case R.id.setting_ll_clear:
                setting_ll_clear.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.sclae_up_renew));
                try {
                    startActivity(getActivity().getPackageManager().getLaunchIntentForPackage("com.qt.cleanmaster"));
                } catch (Exception e) {
                    showToast(getString(R.string.no_install));
//                    e.printStackTrace();
                }
                break;
            case R.id.setting_ll_language:
                new DialogLanguage().creatDialogs(getContext());
                break;
            case R.id.setting_ll_recover:
                setting_ll_recover.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.sclae_up_renew));
                if (TreadApplication.getInstance().isRuning()) {
                    showToast(getString(R.string.run_stop));
                } else {
                    recover();
                }
                return;
        }
    }

    private void recover() {
        myDialog = new AlertDialog.Builder(getContext()).create();
        myDialog.show();
        myDialog.getWindow().setContentView(R.layout.dialog_recover);
        myDialog.getWindow().findViewById(R.id.ce).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().findViewById(R.id.que).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                DataOutputStream os = null;
//                Process process = null;
//                try {
//                    process = Runtime.getRuntime().exec("su");
//                    os = new DataOutputStream(process.getOutputStream());
//                    os.writeBytes("mount -o remount /system" + "\n");
//                    os.writeBytes("cp /data/data/com.vigorchip.treadmill.wr2/save.txt /system/treadmill/save.txt" + "\n");
//                    os.writeBytes("chmod 0644 /system/treadmill/save.txt" + "\n");
//                    os.writeBytes("exit" + "\n");
//                    os.flush();
//                    process.waitFor();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                } finally {
//                    try {
//                        if (null != os) {
//                            os.close();
//                        }
//                        if (null != process) {
//                            process.destroy();
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
                new Thread("Reboot") {
                    @Override
                    public void run() {
                        try {
                            RecoverySystem.rebootWipeUserData(getContext());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }.start();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (myDialog != null) {
            myDialog.dismiss();
        }
    }

    AlertDialog myDialog;

    private void changTone() {
        if (isSelector) {
            setting_ll_tone.setSelected(isSelector);
            setting_iv_tone.setImageResource(R.mipmap.ic_sound_on);
            setting_tv_tone.setText(getContext().getResources().getString(R.string.up));
            setting_ll_tone.setBackground(getResources().getDrawable(R.drawable.shape_yellow_frame));
            isSelector = false;
            Settings.System.putInt(getActivity().getContentResolver(), Settings.System.SOUND_EFFECTS_ENABLED, 1);
        } else {
            setting_ll_tone.setSelected(isSelector);
            setting_iv_tone.setImageResource(R.mipmap.ic_sound_off);
            setting_tv_tone.setText(getContext().getResources().getString(R.string.off));
            setting_ll_tone.setBackground(getResources().getDrawable(R.drawable.shape_blue_frame));
            isSelector = true;
            Settings.System.putInt(getActivity().getContentResolver(), Settings.System.SOUND_EFFECTS_ENABLED, 0);
        }
    }

    private void getTone() {
        if (Settings.System.getInt(getActivity().getContentResolver(), Settings.System.SOUND_EFFECTS_ENABLED, 0) == 0) {
            setting_ll_tone.setSelected(isSelector);
            setting_iv_tone.setImageResource(R.mipmap.ic_sound_off);
            setting_tv_tone.setText(getContext().getResources().getString(R.string.off));
            isSelector = true;
            setting_ll_tone.setBackground(getResources().getDrawable(R.drawable.shape_blue_frame));
        } else {
            setting_ll_tone.setSelected(isSelector);
            setting_iv_tone.setImageResource(R.mipmap.ic_sound_on);
            setting_tv_tone.setText(getContext().getResources().getString(R.string.up));
            setting_ll_tone.setBackground(getResources().getDrawable(R.drawable.shape_yellow_frame));
            isSelector = false;
        }
    }

    public static int getM() {
        return m;
    }

    public static void setM(int m) {
        SettingFragment.m = m;
    }

    private static int m = 0;
//
//    @Override
//    public void onCheckedChanged(RadioGroup group, int checkedId) {
//        switch (checkedId) {
//            case R.id.setting_rb_british:
//                m = 0;
//                break;
//            case R.id.setting_rb_metric:
//                m = 1;
//                break;
//        }
//    }

    @Override
    public void onClick(View v) {
        getBTN(v);
    }
}
