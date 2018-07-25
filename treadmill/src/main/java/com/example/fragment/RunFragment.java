package com.example.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.app.BaseFragment;
import com.example.app.TreadApplication;
import com.example.dialog.DialogCountDown;
import com.example.moudle.OnStartRun;
import com.example.moudle.SerialComm;
import com.vigorchip.treadmill.wr2.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 跑步模式选择页面的父类窗体
 */
public class RunFragment extends BaseFragment {
    View view;

    public static int getCurrentIndex() {
        return currentIndex;
    }

    private static int currentIndex;
    private int oldIndex;
    private FragmentManager childFragmentManager;
    private Fragment currentFragment = new Fragment();
    private List<Fragment> fragments = new ArrayList<>();
    private FragmentTransaction transaction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_run, container, false);
        return view;
    }

    public void setCurrentIndex(int index) {
        showLogE("oldIndex:" + oldIndex + "                            currentIndex:" + currentIndex + "                         index:" + index);

        if (index == 0) {
            isTime = true;
            isMil = false;
        } else if (index == 1) {
            isTime = false;
            isMil = true;
        } else {
            isMil = false;
            isTime = false;
        }
        oldIndex = currentIndex;
        currentIndex = index;
        showFragment();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        showLogE(TreadApplication.getInstance().getFirst() + "");
        initData();
    }

    private void initData() {
        childFragmentManager = getChildFragmentManager();
        if (fragments.size() == 0) {
            fragments.add(new TimeModelFragment());
            fragments.add(new MileageModelFragment());
            fragments.add(new PreinstallModelFragment());
            fragments.add(new CustomModelFragment());
            fragments.add(new HeartModelFragment());
            fragments.add(new RunningModelFragment());
        } else {
            fragments.add(childFragmentManager.findFragmentByTag(0 + ""));
            fragments.add(childFragmentManager.findFragmentByTag(1 + ""));
            fragments.add(childFragmentManager.findFragmentByTag(2 + ""));
            fragments.add(childFragmentManager.findFragmentByTag(3 + ""));
            fragments.add(childFragmentManager.findFragmentByTag(4 + ""));
            fragments.add(childFragmentManager.findFragmentByTag(5 + ""));
        }

        if (TreadApplication.getInstance().getFirst() && !RunningModelFragment.isNext) {
            setCurrentIndex(5);
            showLogE("进入跑步中界面");
        } else{
            choosemode();
        }
        SerialComm.setOnStartRun(new OnStartRun() {
            @Override
            public void onRun(int type) {
                getData(type);
            }
        });
    }

    private void choosemode() {//跑步跳转
        if (TreadApplication.getInstance().isRuning() ||
                TreadApplication.getInstance().getSport() == TreadApplication.getInstance().FIVE_SECOND_STATUS ||
                TreadApplication.getInstance().getSport() == TreadApplication.getInstance().END_STATUS) {
            setCurrentIndex(5);
            showLogE("进来跑步中界面");
            return;
        }
        if (SerialComm.isPre()) {//P键 程序键
            SerialComm.setIsPre(false);
            showLogE("点击P键");
            if (currentIndex != 2)
                setCurrentIndex(2);
            TreadApplication.getInstance().setSport((TreadApplication.getInstance()).PROGRAM_SETUP_STATUS);
            return;
        }
        if (SerialComm.isNo()) {//M键   直接跳运动界面
            showLogE("点击M键");
            if (isTime) {
                TreadApplication.getInstance().setSport(TreadApplication.getInstance().NORMAL_STATUS);
                setCurrentIndex(1);
                showLogE("进来倒数界面");
                return;
            }
            if (isMil) {
                TreadApplication.getInstance().setSport(TreadApplication.getInstance().USER_SETUP_STATUS);
                setCurrentIndex(3);
                showLogE("进来自定义界面");
                return;
            }
//				 else if (TreadApplication.getInstance().getSport() == TreadApplication.getInstance().PROGRAM_SETUP_STATUS)
//					setCurrentIndex(3);
            if (TreadApplication.getInstance().getSport() == TreadApplication.getInstance().USER_SETUP_STATUS) {
                TreadApplication.getInstance().setSport(TreadApplication.getInstance().HRC_SETUP_STATUS);
                setCurrentIndex(4);
                showLogE("进来心率界面");
                return;
            }
            if (TreadApplication.getInstance().getSport() == TreadApplication.getInstance().HRC_SETUP_STATUS) {
                TreadApplication.getInstance().setSport(TreadApplication.getInstance().NORMAL_STATUS);
                setCurrentIndex(0);
                showLogE("进来模式选择界面");
                return;
            }
        }
        TreadApplication.getInstance().setSport(TreadApplication.getInstance().NORMAL_STATUS);
        setCurrentIndex(0);
        SerialComm.setIsNo(false);
        showLogE("进入模式选择界面");
    }

    private static boolean isTime;//智能跑步界面

    public static void setIsTime(boolean isTime) {
        RunFragment.isTime = isTime;
    }

    public static void setIsMil(boolean isMil) {
        RunFragment.isMil = isMil;
    }

    private static boolean isMil;//倒数模式界面

    private void showFragment() {
        transaction = childFragmentManager.beginTransaction();
        if (!fragments.get(currentIndex).isAdded()) {
            if (oldIndex == 5) {
                showLogE("!fragments.get(currentIndex).isAdded()oldIndex == 5");
                transaction.hide(currentFragment);
            } else {
                showLogE("!fragments.get(currentIndex).isAdded()oldIndex != 5");
                transaction.detach(currentFragment);
            }
            transaction.add(R.id.fragment_run_fl_contain, fragments.get(currentIndex), "" + currentIndex);
        } else {
            if (oldIndex == 5) {
                showLogE("fragments.get(currentIndex).isAdded()oldIndex == 5");
                transaction.hide(currentFragment);
            } else {
                showLogE("fragments.get(currentIndex).isAdded()oldIndex != 5");
                transaction.detach(currentFragment);
            }
        }
        currentFragment = fragments.get(currentIndex);
        if (currentIndex == 5) {
            showLogE("oldIndexshow");
            transaction.show(currentFragment);
        } else {
            showLogE("oldIndexattach");
            transaction.attach(currentFragment);
        }
        transaction.commitAllowingStateLoss();
    }

    public void setCustomSave() {//保存自定义数据
        switch (CustomModelFragment.currentIndex) {
            case 0:
                CustomUser1Fragment.setSave();
//                MainFragment.setRuning(getResources().getString(R.string.shortcut_fatloss1));
                break;
            case 1:
                CustomUser2Fragment.setSave();
//                MainFragment.setRuning(getResources().getString(R.string.shortcut_fatloss2));
                break;
            case 2:
                CustomUser3Fragment.setSave();
//                MainFragment.setRuning(getResources().getString(R.string.shortcut_fatloss3));
                break;
            case 3:
                CustomUser4Fragment.setSave();
//                MainFragment.setRuning(getResources().getString(R.string.shortcut_fatloss4));
                break;
            case 4:
                CustomUser5Fragment.setSave();
//                MainFragment.setRuning(getResources().getString(R.string.shortcut_fatloss5));
                break;
        }
    }

    /**
     * 模式跑步
     *
     * @param type
     */
    public void getData(int type) {//防止点击两次出现两次处理
        if (TreadApplication.getInstance().isRuning() || TreadApplication.getInstance().getSport() == TreadApplication.getInstance().FIVE_SECOND_STATUS)
            return;
        Log.e("RunFragment", "type:" + type);
        switch (type) {
            case 0:
                MainFragment.setRuning(getResources().getString(R.string.shortcut_manual));//手动
                break;
            case 1:
                MainFragment.setRuning(getResources().getString(R.string.shortcut_countdown_time));//时间
                break;
            case 2:
                MainFragment.setRuning(getResources().getString(R.string.shortcut_distance));//距离
                break;
            case 3:
                MainFragment.setRuning(getResources().getString(R.string.shortcut_calorie));//卡路里
                break;
            case 4:
                MainFragment.setRuning(getResources().getString(R.string.shortcut_program));//程序模式
                break;
            case 5:
                setCustomSave();
                MainFragment.setRuning(getResources().getString(R.string.shortcut_fatloss));//自定义模式
                break;
            case 6:
                MainFragment.setRuning(getResources().getString(R.string.shortcut_rate));//心率模式
                break;
        }
        DialogCountDown dialogCountDown = new DialogCountDown(getContext(), type);//321倒数框
        dialogCountDown.createDialog();
    }
}