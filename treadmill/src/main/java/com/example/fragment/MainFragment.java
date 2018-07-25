package com.example.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.activity.MainActivity;
import com.example.app.BaseFragment;
import com.example.app.TreadApplication;
import com.example.view.MyLinearLayout;
import com.vigorchip.treadmill.wr2.R;

import static com.example.app.TreadApplication.END_STATUS;
import static com.example.app.TreadApplication.FIVE_SECOND_STATUS;
import static com.example.app.TreadApplication.isN0;

/**
 * 首页
 */
public class MainFragment extends BaseFragment implements View.OnClickListener {
    View view;
    static MyLinearLayout fragment_main_ll_run;
    MyLinearLayout fragment_main_ll_media, fragment_main_ll_scene, fragment_main_ll_apps, fragment_main_ll_user, fragment_main_ll_setting;
    int currIndex;
    static TextView run_tv_model;
    static Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mContext = getContext();
        ((MainActivity) getActivity()).setTitles(getString(R.string.home_page));
        if (
//                ((MainActivity) getActivity()).isB && ((MainActivity) getActivity()).nobor < ((MainActivity) getActivity()).number &&
                !TreadApplication.getInstance().isRuning() &&
                        TreadApplication.getInstance().getSport() != END_STATUS &&
                        TreadApplication.getInstance().getSport() != FIVE_SECOND_STATUS) {
            if (isN0&&!TextUtils.isEmpty(((MainActivity) getActivity()).downSystemUrl)) {//判断系统更新地址是否为空
                isN0=false;
                ((MainActivity) getActivity()).downSystemLoad();//系统下载
            } else if (isN0&&!TextUtils.isEmpty(((MainActivity) getActivity()).downloadUrl)) {
                isN0=false;
                ((MainActivity) getActivity()).downLoad();//app下载
            }
        }
        initData();
    }

    public void initData() {
        fragment_main_ll_run = (MyLinearLayout) view.findViewById(R.id.fragment_main_ll_run);
        fragment_main_ll_media = (MyLinearLayout) view.findViewById(R.id.fragment_main_ll_media);
        fragment_main_ll_scene = (MyLinearLayout) view.findViewById(R.id.fragment_main_ll_scene);
        fragment_main_ll_apps = (MyLinearLayout) view.findViewById(R.id.fragment_main_ll_apps);
        fragment_main_ll_user = (MyLinearLayout) view.findViewById(R.id.fragment_main_ll_user);
        fragment_main_ll_setting = (MyLinearLayout) view.findViewById(R.id.fragment_main_ll_setting);
        run_tv_model = (TextView) view.findViewById(R.id.run_tv_model);
        fragment_main_ll_run.setOnClickListener(this);
        fragment_main_ll_media.setOnClickListener(this);
        fragment_main_ll_scene.setOnClickListener(this);
        fragment_main_ll_apps.setOnClickListener(this);
        fragment_main_ll_user.setOnClickListener(this);
        fragment_main_ll_setting.setOnClickListener(this);
        getMole();
    }

    private void getMole() {//运动模式判断
        if (TreadApplication.getInstance().getSport() == TreadApplication.getInstance().NORMAL_RUN_STATUS)
            setRuning(getResources().getString(R.string.shortcut_manual));//手动
        else if (TreadApplication.getInstance().getSport() == TreadApplication.getInstance().TIME_RUN_STATUS)
            setRuning(getResources().getString(R.string.shortcut_countdown_time));//时间
        else if (TreadApplication.getInstance().getSport() == TreadApplication.getInstance().DISTANCE_RUN_STATUS)
            setRuning(getResources().getString(R.string.shortcut_distance));//距离
        else if (TreadApplication.getInstance().getSport() == TreadApplication.getInstance().CALORIE_RUN_STATUS)
            setRuning(getResources().getString(R.string.shortcut_calorie));//卡路里
        else if (TreadApplication.getInstance().getSport() == TreadApplication.getInstance().PROGRAM_RUN_STATUS)
            setRuning(getResources().getString(R.string.shortcut_program));//程序模式
        else if (TreadApplication.getInstance().getSport() == TreadApplication.getInstance().USER_RUN_STATUS)
            setRuning(getResources().getString(R.string.shortcut_fatloss));//自定义
        else if (TreadApplication.getInstance().getSport() == TreadApplication.getInstance().HRC_RUN_STATUS)
            setRuning(getResources().getString(R.string.shortcut_rate));//心率
        else if (TreadApplication.getInstance().getSport() == TreadApplication.getInstance().END_STATUS) {
            setRuning(getResources().getString(R.string.stoping));//正在停止
        } else
            referter();//无变化
    }

    public static void setRuning(String model) {//设置主界面显示的运动模式
        fragment_main_ll_run.setBackground(mContext.getResources().getDrawable(R.mipmap.bg_home_running_r));
        run_tv_model.setText(model);
    }

    public static String getRunmodel() {
        return run_tv_model.getText() + "";
    }

    public static void referter() {//恢复主界面显示的初始模式
        fragment_main_ll_run.setBackground(mContext.getResources().getDrawable(R.mipmap.bg_home_running));
        run_tv_model.setText(R.string.shortcut_run);
    }

    public static Context getmContext() {
        return mContext;
    }

    private void getFragmentIndex(int RId) {
        switch (RId) {
            case R.id.fragment_main_ll_run:
                currIndex = 1;//运动
                break;
            case R.id.fragment_main_ll_media:
                currIndex = 2;//多媒体
                break;
            case R.id.fragment_main_ll_scene:
                currIndex = 3;//实景
                break;
            case R.id.fragment_main_ll_apps:
                currIndex = 4;//应用
                break;
            case R.id.fragment_main_ll_user:
                currIndex = 5;//用户
                break;
            case R.id.fragment_main_ll_setting:
                currIndex = 6;//设置
                break;
        }
    }

    @Override
    public void onClick(View v) {
        getFragmentIndex(v.getId());
        ((MainActivity) getActivity()).setCurrentIndex(currIndex);
    }
}
