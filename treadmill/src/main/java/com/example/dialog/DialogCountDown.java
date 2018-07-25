package com.example.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.app.TreadApplication;
import com.example.fragment.RunningModelFragment;
import com.example.moudle.SerialComm;
import com.example.service.WindowInfoService;
import com.vigorchip.treadmill.wr2.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 跑步预备321
 */
public class DialogCountDown {
    Context mContext;//int[] RId = new int[]{R.mipmap.img_ready_3, R.mipmap.img_ready_2, R.mipmap.img_ready_1, R.mipmap.img_ready_go,};
    ImageView ready_iv_three, ready_iv_two, ready_iv_one, ready_iv_go;
    int mType;
    WindowInfoService windowInfoService;
    static Dialog dialogss;
//    static boolean isStop;

    public DialogCountDown(Context context, int type) {
        mContext = context;
        mType = type;
        windowInfoService = new WindowInfoService();
    }

    private static int count;

    public void createDialog() {
        TreadApplication.setMediaPlayer(R.raw.prepare);
        RunningModelFragment.setRun(false);
//        isStop = true;
        count = 0;
        TreadApplication.getInstance().setSport(TreadApplication.getInstance().FIVE_SECOND_STATUS);
        dialogss = new Dialog(mContext, R.style.Transp);
        dialogss.setContentView(R.layout.dialog_count_down);
        ready_iv_three = (ImageView) dialogss.findViewById(R.id.ready_iv_three);
        ready_iv_two = (ImageView) dialogss.findViewById(R.id.ready_iv_two);
        ready_iv_one = (ImageView) dialogss.findViewById(R.id.ready_iv_one);
        ready_iv_go = (ImageView) dialogss.findViewById(R.id.ready_iv_go);
        dialogss.getWindow().setType((WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
        dialogss.setCancelable(false);
        dialogss.show();
        ready_iv_two.setVisibility(View.GONE);
        ready_iv_one.setVisibility(View.GONE);
        ready_iv_go.setVisibility(View.GONE);
//        new Thread() {
//            @Override
//            public void run() {
//                super.run();
////                int i = 0;
////                handler.sendEmptyMessage(i);
//                try {
////                    while (true) {
////                        if (isStop|| i >= 3) {
////                            windowInfoService.handler.sendEmptyMessage(mType);
////                            dimss();
////                            handler.removeCallbacks(this);
////                            return;
////                        } else {
////                            count++;
////                            if (count >= (1000 / 50)) {
////                                i++;
////                                handler.sendEmptyMessage(i);
////
////                            }
////                        }
////                        Thread.sleep(50);
////                    }
//
//                    TreadApplication.getInstance().setLog(true);
//                    for (int i = 0; i < 4 && isStop; count++) {
//                        if (count >= (1000 / 50)) {
//                            i++;
//                            handler.sendEmptyMessage(i);
//                        }
//                        Thread.sleep(50);
//                    }
//                    if (isStop)
//                        windowInfoService.handler.sendEmptyMessage(mType);
//                    dimss();
//                    handler.removeCallbacks(this);
//                } catch (InterruptedException e) {
////                    e.printStackTrace();
//                    Log.e("DialogCountDown", "DialogCountDown出错了");
//                }
//            }
//        }.start();
        timerTask = new MyTimerTask();
        timer = new Timer(true);
        SerialComm.setBEEP(1);
        timer.schedule(timerTask, 0, 1000);//定时每秒执行一次
    }

    private static MyTimerTask timerTask;
    private static Timer timer;

    //定时任务,定时发送message
    private class MyTimerTask extends TimerTask {
        @Override
        public void run() {
//        Message message = new Message();
//        message.what = 2;
            handler.sendEmptyMessage(count);  //发送message
            count++;
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    ready_iv_two.setVisibility(View.VISIBLE);
                    SerialComm.setBEEP(1);
                    break;
                case 2:
                    ready_iv_one.setVisibility(View.VISIBLE);
                    SerialComm.setBEEP(1);
                    break;
                case 3:
                    TreadApplication.getInstance().setLog(true);
                    ready_iv_go.setVisibility(View.VISIBLE);
                    SerialComm.setBEEP(1);
                    break;
                case 4:
                    windowInfoService.handler.sendEmptyMessage(mType);
                    dimss();
                    break;
            }
        }
    };

    public static void dimss() {
        if (dialogss != null) {
            dialogss.dismiss();
            if (timer != null)
                timer.cancel();
            if (timerTask != null)
                timerTask.cancel();
        }
    }
}