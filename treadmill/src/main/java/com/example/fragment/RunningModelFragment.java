package com.example.fragment;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.activity.MainActivity;
import com.example.app.BaseFragment;
import com.example.app.TreadApplication;
import com.example.service.WindowInfoService;
import com.example.utils.MyTime;
import com.example.view.RunningView;
import com.vigorchip.treadmill.wr2.R;

/**
 * 跑步中
 */
public class RunningModelFragment extends BaseFragment implements View.OnTouchListener {
    static View view;
    private static double[] speeds;
    private static int[] slpoess;
    private static RunningView running_rv_show;
    static int time;//列
    static int slopes;
    static double speed;
    private static boolean isRun;
    private LinearLayout running_ll_show;

    private MediaPlayer UpMedia;
    private MediaPlayer DownMedia;
    private int Flag = 0;
    private int upPath = R.raw.speed_up;
    private int upOldPath;
    private int downPath = R.raw.speed_down;
    private int downOldPath;

    Handler mediaHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:

                    break;
                case 2:

                    break;

            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragmen_running, container, false);
        return view;
    }

    public static boolean isNext;

    @Override
    public void onResume() {
        super.onResume();
        running_rv_show = (RunningView) view.findViewById(R.id.running_rv_show);
        running_ll_show = (LinearLayout) view.findViewById(R.id.running_ll_show);
        if (!isNext) {//开机时弹出赋值后消失，用户看不到，为了有数据
            speeds = new double[16];
            slpoess = new int[16];
            isNext = true;
            showLogE("跳出跑步中界面");
            ((MainActivity) getActivity()).setCurrentIndex(0);
            getContext().startService(new Intent(TreadApplication.getAppContext(), WindowInfoService.class));
        }
        initData();
    }

    private void initData() {
        ((MainActivity) getActivity()).setIsRunModel(false);
        if (TreadApplication.getInstance().isRuning())
            ((MainActivity) getActivity()).setTitles(MainFragment.getRunmodel().equals(getString(R.string.shortcut_run)) ? getString(R.string.shortcut_manual) : MainFragment.getRunmodel());
        else if (TreadApplication.getInstance().getSport() == TreadApplication.getInstance().END_STATUS)
            ((MainActivity) getActivity()).setTitles(getString(R.string.stoping));
        if (TreadApplication.getInstance().isLogs()) {
            TreadApplication.getInstance().setLog(false);
            handler.postDelayed(runnable, 0);
        }
        running_ll_show.setOnTouchListener(this);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {

        } else {
            speed = WindowInfoService.getmSpeed();
            slopes = WindowInfoService.getmSlopes();
            initData();
        }
    }


    public static Runnable runnable = new Runnable() {//设置运动界面的数据条闪动
        @Override
        public void run() {
            if (!TreadApplication.getInstance().isRuning() && TreadApplication.getInstance().getSport() != TreadApplication.getInstance().END_STATUS)
                return;
            handler.sendEmptyMessage(0);
            handler.postDelayed(this, 500);
        }
    };
    static boolean isSham;
    static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (!TreadApplication.getInstance().isRuning()) {//运动结束时赋值给结束页面
                running_rv_show.upDataLine(slpoess);
                running_rv_show.upDataRec(speeds, time);
                if (isSham)//设置数据条出现后就不隐藏
                    return;
            } else {
                speeds = WindowInfoService.getSpeed();
                slpoess = WindowInfoService.getSlopes();
                if (WindowInfoService.modelType == 0) {
                    huoQuLie();
                    setModel();
                } else if (WindowInfoService.modelType == 1)
                    time = WindowInfoService.getNumber();
                else if (WindowInfoService.modelType == 2)
                    time = WindowInfoService.getNumber();
                else if (WindowInfoService.modelType == 3) {
                    huoQuLie();
                }
            }
            if (running_rv_show == null)
                running_rv_show = (RunningView) view.findViewById(R.id.running_rv_show);
            running_rv_show.upDataLine(slpoess);
            running_rv_show.upDataRec(speeds, time);
            isSham = !isSham;
            running_rv_show.setIsShan(isSham);
        }
    };

    public static void clear() {//跑步停止时清除数据
        try {
            if (speeds != null && slpoess != null)
                for (int i = 0; i < speeds.length; i++) {
                    speeds[i] = 0;
                    slpoess[i] = 0;
                }
            time = 0;
            if (running_rv_show != null) {
                running_rv_show.upDataLine(slpoess);
                running_rv_show.upDataRec(speeds, time);
            }
            if (handler != null && runnable != null)
                handler.removeCallbacks(runnable);
        } catch (Exception e) {
            Log.e("treadmill", "RunningModelFragment的clear为空");
        }
    }

    private static void huoQuLie() {//计算倒计时的数据条  到了time的值就跳下一段
        time = (MyTime.getTimeShow() / 3600 * 60 + MyTime.getTimeShow() % 3600 / 60) % 16;
    }

    public static void setCustom() {//时间 心率 的倒计时
        speed = WindowInfoService.getmSpeed();
        slopes = WindowInfoService.getmSlopes();
        for (int i = WindowInfoService.getNumber(); i < 16; i++) {
            speeds[i] = WindowInfoService.getmSpeed();
            slpoess[i] = WindowInfoService.getmSlopes();
        }
        running_rv_show.upDataRec(speeds, WindowInfoService.getNumber());
        running_rv_show.upDataLine(slpoess);
    }

    public static void setModel() {//手动 距离 卡路里 实景 正计时并一分钟跳格改变后面的数据
//        time = Integer.parseInt(WindowInfoService.getTimes().substring(0, 2)) % 16;
        time = (MyTime.getTimeShow() / 3600 * 60 + MyTime.getTimeShow() % 3600 / 60) % 16;
        speed = WindowInfoService.getmSpeed();
        slopes = WindowInfoService.getmSlopes();
        for (int i = time % 16; i < 16; i++) {
            speeds[i] = WindowInfoService.getmSpeed();
            slpoess[i] = WindowInfoService.getmSlopes();
        }
        running_rv_show.upDataRec(speeds, time);
        running_rv_show.upDataLine(slpoess);
    }

    public static void setPerCustomSpeed() {//设置程序模式的单条数据条的速度数据
        speed = WindowInfoService.getmSpeed();
        speeds[WindowInfoService.getNumber()] = WindowInfoService.getmSpeed();
        running_rv_show.upDataRec(speeds, WindowInfoService.getNumber());
    }

    public static void setPerCustomSlopes() {//设置程序模式的单条数据条的坡度数据
        slopes = WindowInfoService.getmSlopes();
        slpoess[WindowInfoService.getNumber()] = WindowInfoService.getmSlopes();
        running_rv_show.upDataLine(slpoess);
    }

    public static void setPerSpeed() {//设置程序模式的单条数据条的速度数据
        speed = WindowInfoService.getmSpeed();
        speeds[(MyTime.getTimeShow() / 3600 * 60 + MyTime.getTimeShow() % 3600 / 60) % 16] = WindowInfoService.getmSpeed();
        running_rv_show.upDataRec(speeds, (MyTime.getTimeShow() / 3600 * 60 + MyTime.getTimeShow() % 3600 / 60) % 16);
    }

    public static void setPerSlopes() {//设置程序模式的单条数据条的坡度数据
        slopes = WindowInfoService.getmSlopes();
        slpoess[(MyTime.getTimeShow() / 3600 * 60 + MyTime.getTimeShow() % 3600 / 60) % 16] = WindowInfoService.getmSlopes();
        running_rv_show.upDataLine(slpoess);
    }

    public static boolean isRun() {
        return isRun;
    }

    public static void setRun(boolean run) {
        isRun = run;
    }

    float lastX;
    float lastY;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.e("onTouch", "ACTION_DOWN");
                lastX = event.getRawX();
                lastY = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e("X:" + (event.getRawX() - lastX), "Y" + (event.getRawY() - lastY));
                break;
            case MotionEvent.ACTION_UP://手势设置数据
                if (TreadApplication.getInstance().isRuning()){
                if (Math.abs(event.getRawX() - lastX) > Math.abs(event.getRawY() - lastY)) {
                    if (TreadApplication.getInstance().SLOPES!=0){
                    if (event.getRawX() - lastX >= 50) {
                        WindowInfoService.setmSlopes(WindowInfoService.getmSlopes() + 1);
                        showToasts(WindowInfoService.getmSlopes()+"");
                    } else if (lastX - event.getRawX() >= 50) {
                        WindowInfoService.setmSlopes(WindowInfoService.getmSlopes() - 1);
                        showToasts(WindowInfoService.getmSlopes()+"");
                    }}
                } else {
                    if (lastY - event.getRawY()  >= 50) {
                        TreadApplication.setMediaPlayer(R.raw.speed_up);
//                        if (UpMedia!=null&&upPath == upOldPath && UpMedia.isPlaying()){
//                            upOldPath = upPath;
//                        } else {
//                            upOldPath = upPath;
//                            UpMedia = MediaPlayer.create(getContext(), upPath);
//                            Log.i("UpMedia第一个", "");
//                            if (!UpMedia.isPlaying()) {
//                                UpMedia.start();
//                                Flag = Flag + 1;
//                                Log.i("UpMedia第二个", String.valueOf(Flag));
//                                if (Flag == 8) {
//                                    UpMedia.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                                        @Override
//                                        public void onCompletion(MediaPlayer mp) {
//                                            if (!mp.isPlaying()) {
//                                                new Handler().postDelayed(new Runnable() {
//                                                    public void run() {
//                                                        UpMedia.release();
//                                                        UpMedia = null;
//                                                        Flag = 0;
//                                                        UpMedia = MediaPlayer.create(getContext(), upPath);
//                                                        Log.i("UpMedia第三个", String.valueOf(UpMedia == null));
//                                                        if (DownMedia != null) {
//                                                            DownMedia.release();
//                                                            Log.i("UpMedia第四个", String.valueOf(DownMedia != null));
//                                                        }
//                                                    }
//                                                }, 2000);
//                                            }
//                                        }
//                                    });
//                                }
//                            }
//                        }
                        WindowInfoService.setmSpeed(WindowInfoService.getmSpeed() + 1);//加速
                        showToasts(WindowInfoService.getmSpeed()+"");
                    } else if (event.getRawY() - lastY>= 50) {
                        TreadApplication.setMediaPlayer(R.raw.speed_down);
                        WindowInfoService.setmSpeed(WindowInfoService.getmSpeed() - 1);//减速
                        showToasts(WindowInfoService.getmSpeed()+"");
                    }
                }}
                break;
        }
        return true;
    }

//
//    private void setSpeedUpMedia(int path){
//        if (UpMedia != null && path == upOldPath && !UpMedia.isPlaying()){
//            upOldPath = path;
//            return;
//        }
//        upOldPath = path;
//        if (UpMedia != null && !UpMedia.isPlaying()) {
//            UpMedia.release();
//            UpMedia = null;
//        }
//        UpMedia = MediaPlayer.create(getContext(),path);
//        UpMedia.start();
//    }
//
//    private void setSpeedDownMedia(int path){
//        if (DownMedia != null && path == downOldPath && !DownMedia.isPlaying()){
//            downOldPath = path;
//            return;
//        }
//        downOldPath = path;
//        if (DownMedia != null) {
//            DownMedia = MediaPlayer.create(getContext(),path);
//            DownMedia.start();
//            if (DownMedia != null && !DownMedia.isPlaying()) {
//                DownMedia.release();
//                DownMedia = null;
//                DownMedia = MediaPlayer.create(getContext(),path);
//                DownMedia.start();
//            }
//        }
//    }



    Toast toasts;
    View customViews;
    public void showToasts(String str) {
        if (toasts!=null)
            toasts.cancel();
        customViews = View.inflate(getContext(), R.layout.toast_running, null);
        toasts = new Toast(getContext());
        ((TextView) customViews.findViewById(R.id.running_tv)).setText(str);
        toasts.setGravity(Gravity.CENTER, 0, -100);
        toasts.setView(customViews);
        toasts.show();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (toasts!=null)
                    toasts.cancel();
                handler.removeCallbacks(this);
            }
        },500);
       }
}
