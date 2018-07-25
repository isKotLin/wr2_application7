package com.example.utils;

import android.os.Handler;
import android.os.Message;

import com.example.activity.MainActivity;
import com.example.app.TreadApplication;
import com.example.dialog.DialogAutomatic;
import com.example.moudle.SerialComm;
import com.example.service.WindowInfoService;


public class MyTime {
    public static int getTimeShow() {
        return timeShow;
    }

    public static void setTimeShow() {
        timeShow = 0;
    }

    private static int timeShow;//跑步的时间
    private static boolean startOrStop;
    private static int timeData;//加油的时间
    private static double milData;
    //    private static double milsuData;
    private static int frequency;

    public void timeStart() {
        strTime = new StringBuffer();
        timeShow = -1;
        num = 0;
        startOrStop = true;
        if (TreadApplication.getInstance().OIL_ENABLE != 0)//加油功能
            recervo();
        handler.post(runnable);
    }

    public static void recervo() {//加油功能
        String record = FileUtils.readTextFromFile(MainActivity.getDATAPATH());
        String[] temp = record.split(" ");//split里面添加的字符就干掉字符串中添加的字符，变成字符串数组
        timeData = Integer.parseInt(temp[0]);
//        milsuData = Double.parseDouble(temp[1]);
        milData = Double.parseDouble(temp[1]);
        frequency = Integer.parseInt(temp[2]);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (Thread.currentThread().getPriority() != 9)//提高线程优先级
                Thread.currentThread().setPriority(9);
            if (startOrStop) {
                handler.sendEmptyMessage(0);
                handler.postDelayed(this, 1000);//一秒钟
            } else {
                handler.removeCallbacks(this);
            }
        }
    };

    public static void timeStop() {
        startOrStop = false;
        saveData();
    }

    public void stopS() {
        startOrStop = false;
    }

    private static void saveData() {
        if (TreadApplication.getInstance().OIL_ENABLE != 0) {
            if (!DialogAutomatic.showing()) {
                if (timeData == 0 && milData == 0 && frequency == 0) {
                    recervo();
                }
                FileUtils.writeText2File(MainActivity.getDATAPATH(), timeData + " " + Math.floor(milData) + " " + frequency);
            }
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            timeShow++;//一秒钟加一次
            if (TreadApplication.getInstance().getSport() == TreadApplication.getInstance().NORMAL_RUN_STATUS ||
                    TreadApplication.getInstance().getSport() == TreadApplication.getInstance().DISTANCE_RUN_STATUS ||
                    TreadApplication.getInstance().getSport() == TreadApplication.getInstance().CALORIE_RUN_STATUS)
                WindowInfoService.window_chr_time.setText(positiveTiming());
            else if (TreadApplication.getInstance().getSport() == TreadApplication.getInstance().TIME_RUN_STATUS ||
                    TreadApplication.getInstance().getSport() == TreadApplication.getInstance().PROGRAM_RUN_STATUS ||
                    TreadApplication.getInstance().getSport() == TreadApplication.getInstance().USER_RUN_STATUS ||
                    TreadApplication.getInstance().getSport() == TreadApplication.getInstance().HRC_RUN_STATUS)
                if (WindowInfoService.getTime() != 0)
                    WindowInfoService.window_chr_time.setText(countDown(WindowInfoService.getTime()));
            WindowInfoService.setDate();
            if (getTimeShow() % 120 == 0) {
                saveData();
            }
            if (TreadApplication.getInstance().OIL_ENABLE != 0) {
                timeData++;
                milData += WindowInfoService.getMileageSum();
                if (TreadApplication.getInstance().getCustom().equals("yijikang")) {//自动加油
                    if (TreadApplication.getInstance().OIL_TIME == 0) {//与时间无关
                        if (milData >= TreadApplication.getInstance().OIL_DISTANCE * 1000) {
                            SerialComm.setIsrefuel(true);
                        }
                    } else if (TreadApplication.getInstance().OIL_DISTANCE == 0) {//与距离无关
                        if (timeData >= TreadApplication.getInstance().OIL_TIME * 60) {
                            SerialComm.setIsrefuel(true);
                        }
                    } else {//与时间距离有关
                        if (timeData >= TreadApplication.getInstance().OIL_TIME * 60 || milData >= TreadApplication.getInstance().OIL_DISTANCE * 1000) {
                            SerialComm.setIsrefuel(true);
                        }
                    }
                }
            }
        }
    };
    static StringBuffer strTime;

    public static String positiveTiming() {//正计时时间
        if (timeShow < 0)
            setTimeShow();
        strTime.setLength(0);
        strTime.append(timeShow / 3600 > 0 ? timeShow / 3600 + ":" : "");//时
        strTime.append((timeShow % 3600 / 60 > 9 ? timeShow % 3600 / 60 : "0" + timeShow % 3600 / 60) + ":");//分
        strTime.append(timeShow % 3600 % 60 > 9 ? timeShow % 3600 % 60 : "0" + timeShow % 3600 % 60);//秒
        return strTime.toString();
    }

    //timeShow/60 是分
    //timeShow%60 是秒
    public static String positiveTime() {
        if (timeShow < 0)
            setTimeShow();
        return timeShow / 60 + "." + timeShow % 60;
    }

    static int downTime;//显示给用户的倒计时剩余时间 单位为秒
    static int num;//黄色数据条的第几列 当前列数

    public static String countDown(int min) {//min为用户选择的倒计时时间  单位为分
        strTime.setLength(0);
        downTime = min * 60 - timeShow;//用户选择的倒计时时间*60 - 正计时时间 得出倒计时时间
        /**
         * min * 60总时间
         * WindowInfoService.getSpeed().length 当前模式下的速度条条数
         * */
        if (downTime == (min * 60 - min * 60 / WindowInfoService.getSpeed().length * num)) {
//            if (timeShow == min * 60 / WindowInfoService.getSpeed().length * num) {
            if (num != WindowInfoService.getSpeed().length) {
                WindowInfoService.setNumber(num);
                num++;
            }
        }
        if (downTime == 0)
            WindowInfoService.stoping();
        if (downTime < 0)
            return "00:00";
        if (TreadApplication.getInstance().getSport() == TreadApplication.getInstance().HRC_RUN_STATUS) {
            if (timeShow >= 60) {
                if (timeShow % 10 == 0)
                    WindowInfoService.heartS();
            }
        }
        strTime.append(downTime / 3600 > 0 ? downTime / 3600 + ":" : "");//时
        strTime.append((downTime % 3600 / 60 > 9 ? downTime % 3600 / 60 : "0" + downTime % 3600 / 60) + ":");//分
        strTime.append(downTime % 3600 % 60 > 9 ? downTime % 3600 % 60 : "0" + downTime % 3600 % 60);//秒
        if (downTime <= 0)
            return "00:00";
        return strTime.toString();
    }
}
