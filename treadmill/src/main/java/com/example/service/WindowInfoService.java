package com.example.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.input.InputManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.InputDevice;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.activity.MainActivity;
import com.example.app.TreadApplication;
import com.example.dialog.DialogAdjustVelocityGradient;
import com.example.dialog.DialogCountDown;
import com.example.dialog.DialogError;
import com.example.fragment.CustomModelFragment;
import com.example.fragment.CustomUser1Fragment;
import com.example.fragment.CustomUser2Fragment;
import com.example.fragment.CustomUser3Fragment;
import com.example.fragment.CustomUser4Fragment;
import com.example.fragment.CustomUser5Fragment;
import com.example.fragment.HeartModelFragment;
import com.example.fragment.MainFragment;
import com.example.fragment.MileageModelFragment;
import com.example.fragment.P10Fragment;
import com.example.fragment.P11Fragment;
import com.example.fragment.P12Fragment;
import com.example.fragment.P13Fragment;
import com.example.fragment.P14Fragment;
import com.example.fragment.P15Fragment;
import com.example.fragment.P16Fragment;
import com.example.fragment.P17Fragment;
import com.example.fragment.P18Fragment;
import com.example.fragment.P19Fragment;
import com.example.fragment.P1Fragment;
import com.example.fragment.P20Fragment;
import com.example.fragment.P21Fragment;
import com.example.fragment.P22Fragment;
import com.example.fragment.P23Fragment;
import com.example.fragment.P24Fragment;
import com.example.fragment.P25Fragment;
import com.example.fragment.P26Fragment;
import com.example.fragment.P27Fragment;
import com.example.fragment.P28Fragment;
import com.example.fragment.P29Fragment;
import com.example.fragment.P2Fragment;
import com.example.fragment.P30Fragment;
import com.example.fragment.P31Fragment;
import com.example.fragment.P32Fragment;
import com.example.fragment.P33Fragment;
import com.example.fragment.P34Fragment;
import com.example.fragment.P35Fragment;
import com.example.fragment.P36Fragment;
import com.example.fragment.P37Fragment;
import com.example.fragment.P38Fragment;
import com.example.fragment.P39Fragment;
import com.example.fragment.P3Fragment;
import com.example.fragment.P40Fragment;
import com.example.fragment.P41Fragment;
import com.example.fragment.P42Fragment;
import com.example.fragment.P43Fragment;
import com.example.fragment.P44Fragment;
import com.example.fragment.P45Fragment;
import com.example.fragment.P46Fragment;
import com.example.fragment.P47Fragment;
import com.example.fragment.P48Fragment;
import com.example.fragment.P49Fragment;
import com.example.fragment.P4Fragment;
import com.example.fragment.P50Fragment;
import com.example.fragment.P51Fragment;
import com.example.fragment.P52Fragment;
import com.example.fragment.P53Fragment;
import com.example.fragment.P54Fragment;
import com.example.fragment.P55Fragment;
import com.example.fragment.P56Fragment;
import com.example.fragment.P57Fragment;
import com.example.fragment.P58Fragment;
import com.example.fragment.P59Fragment;
import com.example.fragment.P5Fragment;
import com.example.fragment.P60Fragment;
import com.example.fragment.P61Fragment;
import com.example.fragment.P62Fragment;
import com.example.fragment.P63Fragment;
import com.example.fragment.P64Fragment;
import com.example.fragment.P6Fragment;
import com.example.fragment.P7Fragment;
import com.example.fragment.P8Fragment;
import com.example.fragment.P9Fragment;
import com.example.fragment.PreinstallModelFragment;
import com.example.fragment.RunFragment;
import com.example.fragment.RunningModelFragment;
import com.example.fragment.VideoFragment;
import com.example.moudle.OnClickStart;
import com.example.moudle.OnUpOrDown;
import com.example.moudle.SerialComm;
import com.example.utils.MySharePreferences;
import com.example.utils.MyTime;
import com.vigorchip.treadmill.wr2.R;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.example.utils.MyTime.getTimeShow;
import static com.vigorchip.treadmill.wr2.R.id.back_iv;
import static com.vigorchip.treadmill.wr2.R.id.home_iv;
import static com.vigorchip.treadmill.wr2.R.id.volume_iv_down;
import static com.vigorchip.treadmill.wr2.R.id.volume_iv_plus;

/**
 * 悬浮窗口 运动信息
 */
public class WindowInfoService extends Service implements View.OnTouchListener, View.OnClickListener {
    private static LinearLayout mFloatLayout, time_ll, mileage_ll, heat_ll, heart_ll, slopes_ll, speed_ll, end_ll, yijikang_ll, ll_info;
    private WindowManager.LayoutParams wmParams;
    private WindowManager mWindowManager;
    private SerialComm serialComm;
    private static TextView window_tv_speed, window_tv_mileage, window_tv_gradient, window_tv_heat, window_tv_heartRate, sign_times, sign_mileages, sign_heats, sign_hearts, sign_slpoess, sign_speeds;
    public static TextView window_chr_time;
    private static double mSpeed = 0.0;//速度
    private static double mileageSum = 0.0;//里程
    private static double heat = 0.0;//热量
    private static int mSlopes = 0;//坡度
    public static int heart = 0;//心率
    private static Context mContext;
    private static MainActivity mainActivity;
    private LinearLayout slpo_ll;
    private static int time;
    private static double[] speed;
    private static int[] slopes;
    private static Timer timer;
    private static MyTimerTask myTimerTask;

    //static Animation animation;//private static boolean isContinue = true;//private static DialogError dialogError;
    private static int mType;
    static boolean isSpeed;
    public static int modelType;//判断黄色数据条何时跳到下一条   0=正计时一分钟跳一段  1=倒计时  2也属于倒计时，对单条数据进行操作
    private static int number;
    private int[] savaSlopes;
    private double[] saveSpeed;
    private static DialogCountDown dialogCountDown;//跑步ready
    private static int avgHeart;//平均心率
    private static double avgSpeed;//平均速度
    private static float playSpeed;//播放速度
    private static int milSum;
    private static int heatSum;
    private static int heartSum;

    public static int getTime() {
        return time;
    }

    public static float getPlaySpeed() {
        playSpeed = (float) ((TreadApplication.getInstance().MAXSPEED - TreadApplication.getInstance().MINSPEED) / 12);
        return playSpeed;
    }

    public static double getHeatsss() {
        return heatsss;
    }

    public static String getMilsss() {
        return ((int) (milsss * 100)) / 100.0 + "";
    }

    public String getAvgSpeed() {
        if ((timeRun + 1) == 0)
            return "0.0";
        avgSpeed = avgSpeed / (timeRun + 1);
        return ((int) (avgSpeed * 10)) / 10.0 + "";
    }

    int nextTime;

    public String getPace() {
        if (timeRun == 0)
            nextTime = (int) (timeRun / mileageSum);
        else
            nextTime = (int) ((timeRun + 1) / mileageSum);
//        nextTime = (int) (3600 / avgSpeed);
        return (nextTime / 60 > 9 ? nextTime / 60 : "0" + nextTime / 60) + ":" + (nextTime % 60 > 9 ? nextTime % 60 : "0" + nextTime % 60);
//时分秒return (nextTime / 3600) + ":" + (nextTime / 60 >= 60 ? "00" : (nextTime / 60 < 10 ? "0" + nextTime / 60 : nextTime / 60)) + ":" + (nextTime % 60 < 10 ? "0" + nextTime % 60 : nextTime % 60);
    }

    static int heartTime;

    public String getAvgHeart() {
        if (heartTime == 0)
            return "0";
        avgHeart = avgHeart / heartTime;
        return avgHeart * 10 / 10 + "";
    }

    public static double[] getSpeed() {
        return speed;
    }

    public static int[] getSlopes() {
        return slopes;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public static OnClickStart onClickSta;
    public static OnUpOrDown onUpOrDown;
    private static DecimalFormat mileStr;

    @Override
    public void onCreate() {
        super.onCreate();
//        createTouch();
        initData();
        createFloatImageView();
    }

//    WindowManager.LayoutParams windowL;
//    WindowManager windowM;
//    LinearLayout linear;

//    private void createTouch() {
//        windowL = new WindowManager.LayoutParams();
//        windowM = (WindowManager) getApplication().getSystemService(getApplication().WINDOW_SERVICE); //获取的是WindowManagerImpl.CompatModeWrapper
//        windowL.type = WindowManager.LayoutParams.TYPE_PHONE;//设置window type
//        windowL.format = PixelFormat.RGBA_8888;//设置图片格式，效果为背景透明
//        windowL.flags =
////                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|
//                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
////                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
//        ;
//        windowL.gravity = Gravity.BOTTOM; //调整悬浮窗显示的停靠位置
//        windowL.width = WindowManager.LayoutParams.MATCH_PARENT; //设置悬浮窗口长宽数据
//        windowL.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        LayoutInflater inflater = LayoutInflater.from(getApplication());
//        linear = (LinearLayout) inflater.inflate(R.layout.window, null);
//        linear.setPadding(0, 0, 0, 0);
//        windowM.addView(linear, windowL);//添加mFloatLayout
//        linear.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        Log.e("ACTION_DOWN", "isHide:" + isHide);
//                        windowL.flags =
//                                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
//                                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//                                        | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
//                        ;
//                        windowM.updateViewLayout(linear, windowL);
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        Log.e("ACTION_MOVE", "isHide:" + isHide);
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        break;
//                }
//                return false;
//            }
//        });
//    }

    //    public static ImageView SuspendButtonIv;
    WindowManager mWindowManagers = null;
    WindowManager.LayoutParams wmParamss;
    private float mTouchStartX;
    private float mTouchStartY;
    private float x;
    private float y;
    private static final String TAG = "FloatService";
    private static RelativeLayout roundLayout;
    private ImageView sub_iv;


    private void createFloatImageView() {//创建圆点菜单
//        LENTHER = DensityUtils.px2dip(getApplicationContext(), 60);
        mWindowManagers = (WindowManager) getApplication().getSystemService(getApplication().WINDOW_SERVICE); //获取的是WindowManagerImpl.CompatModeWrapper
        wmParamss = new WindowManager.LayoutParams();
        wmParamss.type = WindowManager.LayoutParams.TYPE_PHONE;//设置window type
        wmParamss.format = PixelFormat.RGBA_8888;//设置图片格式，效果为背景透明
        wmParamss.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        wmParamss.gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT; //调整悬浮窗显示位置
        wmParamss.width = WRAP_CONTENT;
        wmParamss.height = WRAP_CONTENT;
        //        DisplayMetrics dm = new DisplayMetrics();
//        mWindowManagers.getDefaultDisplay().getMetrics(dm);
//        wmParam.x = dm.widthPixels - LENTHER;
//        wmParam.y = dm.heightPixels / 2 - LENTHER / 2;
        LayoutInflater inflater = LayoutInflater.from(getApplication());
        roundLayout = (RelativeLayout) inflater.inflate(R.layout.window_round, null);
//        getImageView();
        mWindowManagers.addView(roundLayout, wmParamss);
//        SuspendButtonIv.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//        isRun = true;
        sub_iv = (ImageView) roundLayout.findViewById(R.id.sub_iv);
        sub_iv.setOnClickListener(this);
        sub_iv.setOnTouchListener(this);
        sub_iv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                isMove = true;
                return false;
            }
        });
        mFloatLayout.setVisibility(View.GONE);
        roundLayout.setVisibility(View.GONE);
    }

    private boolean isClicks;
    private int CURRFREQUENCY;

//    private void getImageView() {
//        SuspendButtonIv = new ImageView(getApplicationContext());
//        SuspendButtonIv.setImageResource(R.mipmap.bt_float);
////        SuspendButtonIv.setClickable(true);
//        initViewPlace = true;
//    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isMove = false;
                isClicks = true;
                break;
            case MotionEvent.ACTION_MOVE:
                if (isMove) {
                    isClicks = false;
                    x = event.getRawX();
                    y = event.getRawY();
                    updateViewPosition();
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.e(TAG, "ACTION_UP:ACTION_UP");
                if (!isClicks) {
                    facing();
                    isClicks = true;
                }
                break;
        }
        return isMove;
    }

    boolean isMove;
    static DisplayMetrics dm;
    int widthsss;
    int heightsss;

    private void updateViewPosition() {
//        更新浮动窗口位置参数
//        wmParam.x = (int) Math.abs(mTouchStartX - x);
        dm = new DisplayMetrics();
        dm = getResources().getDisplayMetrics();
        float density = dm.density; // 屏幕密度（像素比例：0.75/1.0/1.5/2.0）
        int densityDPI = dm.densityDpi; // 屏幕密度（每寸像素：120/160/240/320）
        int screenWidth = dm.widthPixels; // 屏幕宽（像素，如：3200px）
        int screenHeight = dm.heightPixels; // 屏幕高（像素，如：1280px）
        widthsss = screenWidth - roundLayout.getWidth() * 5 / 2;
        heightsss = screenHeight / 2 - roundLayout.getWidth() * 135 / 100;
        wmParamss.x = (int) (screenWidth - x - roundLayout.getWidth() / 2);
        wmParamss.y = (int) (y - screenHeight / 2);
//        wmParam.y = (int) Math.abs(y - mTouchStartY);
        mWindowManagers.updateViewLayout(roundLayout, wmParamss);
    }


    private void facing() {//自动贴边
        new Thread() {
            @Override
            public void run() {
                super.run();
                dm = new DisplayMetrics();
                dm = getResources().getDisplayMetrics();
                for (; ; ) {
                    boolean isBack = false;
                    //循环条件
                    if (wmParamss.x > dm.widthPixels / 2) {//往左边靠
                        wmParamss.x += 10;
                    } else {//往右边靠
                        wmParamss.x -= 10;
                    }
                    if (wmParamss.x > (widthsss + roundLayout.getWidth() * 3 / 2)) {//往左边靠
                        isBack = true;
                        wmParamss.x = widthsss + roundLayout.getWidth() * 3 / 2;

                    } else if (wmParamss.x < 0) {//往右边靠
                        isBack = true;
                        wmParamss.x = -2;

                    }
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mainActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mWindowManagers.updateViewLayout(roundLayout, wmParamss);
                        }
                    });
                    if (isBack) {
                        handler.removeCallbacks(this);
                        return;
                    }
                }
            }
        }.start();

//        if (wmParamss.x > (widthsss + roundLayout.getWidth() * 3 / 2) / 2) {//往左边靠
//            wmParamss.x=widthsss + roundLayout.getWidth() * 3 / 2;
//            wmParam.x=widthsss + roundLayout.getWidth() * 3 / 2;
//        }else {//往右边靠
//            wmParamss.x=-2;
//            wmParam.x=-2;
//        }
//        if (isShow) {
//            if (wmParamss.x > widthsss) {//显示
//                wmParamss.x = widthsss;
//            }
//            if (wmParamss.x < roundLayout.getWidth() * 3 / 2) {
//                wmParamss.x = roundLayout.getWidth() * 3 / 2;
//            }
//            if (wmParamss.y > heightsss) {
//                wmParamss.y = heightsss;
//            }
//            if (wmParamss.y < -heightsss) {
//                wmParamss.y = -heightsss;
//            }
//        }
//        mWindowManagers.updateViewLayout(roundLayout, wmParamss);
//        mWindowManagers.updateViewLayout(Flayout, wmParam);
    }

    private void initData() {
        mileStr = new DecimalFormat("0.00");
        mContext = getApplicationContext();
        mainActivity = new MainActivity();
        speed = new double[16];
        slopes = new int[16];
        modelType = -1;
        dialogCountDown = new DialogCountDown(mContext, 0);
        createFloatView();
    }

    public static void setOnClickStart(OnClickStart onClickStart) {
        onClickSta = onClickStart;
    }

    public static void setOnUpOrDown(OnUpOrDown onUpOrDown1) {
        onUpOrDown = onUpOrDown1;
    }

    float lastY;
    float newY;
    private int mPointerId;
    private int mMaxVelocity;
    private VelocityTracker mVelocityTracker;

    /**
     * @param event 向VelocityTracker添加MotionEvent
     * @see android.view.VelocityTracker#obtain()
     * @see android.view.VelocityTracker#addMovement(MotionEvent)
     */
    private void acquireVelocityTracker(final MotionEvent event) {
        if (null == mVelocityTracker) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    /**
     * 释放VelocityTracker
     *
     * @see android.view.VelocityTracker#clear()
     * @see android.view.VelocityTracker#recycle()
     */
    private void releaseVelocityTracker() {
        if (null != mVelocityTracker) {
            mVelocityTracker.clear();
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    private void createFloatView() {//创建底部数据栏方法
        wmParams = new WindowManager.LayoutParams();
        mWindowManager = (WindowManager) getApplication().getSystemService(getApplication().WINDOW_SERVICE); //获取的是WindowManagerImpl.CompatModeWrapper
        wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;//设置window type
        wmParams.format = PixelFormat.RGBA_8888;//设置图片格式，效果为背景透明
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE; //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        wmParams.gravity = Gravity.BOTTOM; //调整悬浮窗显示的停靠位置
        wmParams.width = MATCH_PARENT; //设置悬浮窗口长宽数据
        wmParams.height = WRAP_CONTENT;
        LayoutInflater inflater = LayoutInflater.from(getApplication());
//        if (TreadApplication.getInstance().getCustom().equals("yijikang")) {
        mFloatLayout = (LinearLayout) inflater.inflate(R.layout.window_info_yijikang, null); //获取浮动窗口视图所在布局
        end_ll = (LinearLayout) mFloatLayout.findViewById(R.id.end_ll);
        yijikang_ll = (LinearLayout) mFloatLayout.findViewById(R.id.yijikang_ll);
        end_ll.setOnClickListener(this);
//}else if (TreadApplication.getInstance().getCustom().equals("qimaisi")){mFloatLayout = (LinearLayout) inflater.inflate(R.layout.window_info, null);} else {mFloatLayout = (LinearLayout) inflater.inflate(R.layout.window_info, null);}
        mFloatLayout.setPadding(0, 0, 0, 0);
        mWindowManager.addView(mFloatLayout, wmParams);//添加mFloatLayout
        window_tv_gradient = (TextView) mFloatLayout.findViewById(R.id.window_tv_gradient);
        slpo_ll = (LinearLayout) mFloatLayout.findViewById(R.id.slpo_ll);
        window_tv_heat = (TextView) mFloatLayout.findViewById(R.id.window_tv_heat);
        window_tv_speed = (TextView) mFloatLayout.findViewById(R.id.window_tv_speed);
        window_chr_time = (TextView) mFloatLayout.findViewById(R.id.window_chr_time);
        window_tv_mileage = (TextView) mFloatLayout.findViewById(R.id.window_tv_mileage);
        window_tv_heartRate = (TextView) mFloatLayout.findViewById(R.id.window_tv_heartRate);
        sign_times = (TextView) mFloatLayout.findViewById(R.id.sign_times);
        sign_mileages = (TextView) mFloatLayout.findViewById(R.id.sign_mileages);
        sign_heats = (TextView) mFloatLayout.findViewById(R.id.sign_heats);
        sign_hearts = (TextView) mFloatLayout.findViewById(R.id.sign_hearts);
        sign_slpoess = (TextView) mFloatLayout.findViewById(R.id.sign_slopess);
        sign_speeds = (TextView) mFloatLayout.findViewById(R.id.sign_speeds);
        setTextView();
        time_ll = (LinearLayout) mFloatLayout.findViewById(R.id.time_ll);
        mileage_ll = (LinearLayout) mFloatLayout.findViewById(R.id.mileage_ll);
        heat_ll = (LinearLayout) mFloatLayout.findViewById(R.id.heat_ll);
        heart_ll = (LinearLayout) mFloatLayout.findViewById(R.id.heart_ll);
        slopes_ll = (LinearLayout) mFloatLayout.findViewById(R.id.slopes_ll);
        speed_ll = (LinearLayout) mFloatLayout.findViewById(R.id.speed_ll);
        ll_info = (LinearLayout) mFloatLayout.findViewById(R.id.ll_info);
//        layoutParams = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        window_chr_time.setTypeface(TreadApplication.getInstance().font);
        if (TreadApplication.getInstance().SLOPES == 0)
            slopes_ll.setVisibility(View.GONE);
        myTime = new MyTime();
        serialComm = new SerialComm(TreadApplication.getInstance().getAppContext());
        mMaxVelocity = ViewConfiguration.get(this).getMaximumFlingVelocity();
//        slopes_ll.setOnClickListener(this);
//        speed_ll.setOnClickListener(this);
        setOnUpOrDown(new OnUpOrDown() {
            @Override
            public void onUP() {
                UpOrDrow();
            }
        });
    }

    int v;
    boolean isDown;

    private void UpOrDrow() {
        v = 0;
        if (mFloatLayout.getVisibility() != View.VISIBLE) {
            animUp();
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        for (; v < 30; v++) {
                            Thread.sleep(100);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mainActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mFloatLayout.getVisibility() != View.GONE && isDown)
                                animDown();
                        }
                    });
                }
            }.start();
        }
    }

    private static MyTime myTime;

    public static void setTextView() {
        sign_times.setText(mContext.getString(R.string.param_time));
        sign_mileages.setText(mContext.getString(R.string.param_distance));
        sign_heats.setText(mContext.getString(R.string.param_calorie));
        sign_hearts.setText(mContext.getString(R.string.heart));
        sign_slpoess.setText(mContext.getString(R.string.param_slope));
        sign_speeds.setText(mContext.getString(R.string.param_speed));
    }

    public static void startBtn() {
        if (TreadApplication.getInstance().getSport() == TreadApplication.getInstance().NORMAL_STATUS) {
            dialogCountDown.createDialog();
        }
    }

    static double speedss;

    public static double getmSpeed() {
//        BigDecimal bigDecimal= new BigDecimal(mSpeed);
//        bigDecimal.setScale(1,BigDecimal.ROUND_DOWN);//
//        speedss = Double.parseDouble(speedStr.format(mSpeed));
//        speedss=bigDecimal.doubleValue();
//        speedss=mSpeed;
        speedss = ((int) ((mSpeed + 0.05) * 10)) / 10.0;
        return speedss;
    }

    public static void setmSpeed(double speed) {
        if (speed > TreadApplication.getInstance().MAXSPEED)
            speed = TreadApplication.getInstance().MAXSPEED;
        if (speed <= TreadApplication.getInstance().MINSPEED)
            speed = TreadApplication.getInstance().MINSPEED;
        mSpeed = speed;
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                window_tv_speed.setText(((int) ((mSpeed + 0.05) * 10)) / 10.0 + "");
            }
        });
        Log.e("Ignoring3", "speed:" + speed + "  ((int) (mSpeed * 10)) / 10.0:" + ((int) (mSpeed * 10)) / 10.0 + "");
//        if (DialogSportSpeed.isShowing())
//            DialogSportSpeed.setValue();
        updates();
        if (mainActivity.isVideo()) {
            VideoFragment.setMPSpeed((float) ((getmSpeed() - TreadApplication.getInstance().MINSPEED) / getPlaySpeed() / 10 + 0.5f));
        }
        isSpeed = true;
        setData();
        onUpOrDown.onUP();
    }

    public static void setData() {
        if (TreadApplication.getInstance().getSport() == TreadApplication.getInstance().END_STATUS)
            return;
        switch (modelType) {
            case 0://正计时 一分钟
                RunningModelFragment.setModel();
                break;
            case 1://倒计时模式
                RunningModelFragment.setCustom();
                break;
            case 2://预设倒数
                if (isSpeed)
                    RunningModelFragment.setPerCustomSpeed();
                else
                    RunningModelFragment.setPerCustomSlopes();
                break;
            case 3://预设正计时
                if (isSpeed)
                    RunningModelFragment.setPerSpeed();
                else
                    RunningModelFragment.setPerSlopes();
                break;
        }
    }


    public static double getMileageSum() {
        return mSpeed / 3600 * 1000;
    }

    public void setMileageSum(double mil) {
        mileageSum = mil;
        window_tv_mileage.setText(((int) (mileageSum * 100)) / 100.0 + "");
    }

    public static String getHeat() {
        return (((int) (heat * 10)) / 10.0 + "").split("\\.")[0];
    }

    public void setHeat(double hea) {
        heat = hea;
        window_tv_heat.setText(((int) (heat * 10)) / 10.0 + "");
    }

    public static int getHeart() {
//        return Integer.parseInt(heartStr.format(HeartModelFragment.getHeart()));
        return heartSum;
    }

    public void setHeart(int hearts) {
        heart = hearts & 0xff;
        window_tv_heartRate.setText(heart * 10 / 10 + "");
    }

    public static String getHearts() {
        return heart * 10 / 10 + "";
    }

    public static int getmSlopes() {
        return mSlopes;
    }

    public static void setmSlopes(int slopes) {
        if (slopes > TreadApplication.getInstance().SLOPES)
            slopes = TreadApplication.getInstance().SLOPES;
        if (slopes < 0)
            slopes = 0;
        mSlopes = slopes;
//        if (DialogSportSlopes.isShowings())
//            DialogSportSlopes.setValues();
        updates();
        window_tv_gradient.setText(mSlopes + "");
        isSpeed = false;
        setData();
        onUpOrDown.onUP();
    }

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (TreadApplication.getInstance().getSport() == TreadApplication.getInstance().ERROR_STATUS ||
                    TreadApplication.getInstance().NOTSAFE_STATUS == TreadApplication.getInstance().getSport())
                return;
            if (!mainActivity.isVideo()) {
                if (onClickSta != null && TreadApplication.getInstance().getSport() == TreadApplication.getInstance().FIVE_SECOND_STATUS
//                        && !isHou
                        )
                    onClickSta.onStarts();
            } else
                VideoFragment.plays();
//            IsSerivceRun.hideImageView();
            animUp();
            roundLayout.setVisibility(View.VISIBLE);
//            recover();
            mType = msg.what;
            switch (msg.what) {
                case 0:
                    manualMode();//手动模式
                    break;
                case 1:
                    countdownMode();//倒计时模式
                    break;
                case 2:
                    distanceMode();//距离模式
                    break;
                case 3:
                    heatMode();//热量模式
                    break;
                case 4:
                    presetProgram();//预设程式
                    break;
                case 5:
                    customMode();//自定义模式
                    break;
                case 6:
                    heartRateControl();//心率控速
                    break;
            }
            start();
            isStart = true;
            setmSlopes(slopes[0]);
            setmSpeed(speed[0]);
            isStart = false;
            myTime.timeStart();
        }
    };
    static boolean isStart;

    private void setArray() {
        for (int i = 0; i < speed.length; i++) {
            speed[i] = TreadApplication.getInstance().MINSPEED;
            slopes[i] = 0;
        }
    }

    private void manualMode() {//手动模式
        modelType = 0;
        speed = new double[16];
        slopes = new int[16];
        setArray();
        TreadApplication.getInstance().setSport(TreadApplication.getInstance().NORMAL_RUN_STATUS);
        MainFragment.setRuning(mContext.getResources().getString(R.string.shortcut_manual));
    }

    private void countdownMode() {//倒计时
        modelType = 1;
        speed = new double[16];
        slopes = new int[16];
        time = MileageModelFragment.getMin();
        TreadApplication.getInstance().setSport(TreadApplication.getInstance().TIME_RUN_STATUS);
        setArray();
    }

    private void distanceMode() {//距离
        modelType = 0;
        speed = new double[16];
        slopes = new int[16];
        setArray();
        milSum = MileageModelFragment.getMileage();
        TreadApplication.getInstance().setSport(TreadApplication.getInstance().DISTANCE_RUN_STATUS);
    }

    private void heatMode() {//心率
        modelType = 0;
        speed = new double[16];
        slopes = new int[16];
        setArray();
        heatSum = MileageModelFragment.getHeat();
        TreadApplication.getInstance().setSport(TreadApplication.getInstance().CALORIE_RUN_STATUS);
    }

    private void presetProgram() {
        TreadApplication.getInstance().setSport(TreadApplication.getInstance().PROGRAM_RUN_STATUS);
        switch (PreinstallModelFragment.getCurrindex()) {
//            case 1:speed = WarmUpFragment.getSpeeds();slopes = WarmUpFragment.getSlopes();break;case 2:speed = AerobiesFragment.getSpeeds();slopes = AerobiesFragment.getSlopes();break;case 3:speed = InsanityFragment.getSpeeds();slopes = InsanityFragment.getSlopes();break;case 4:speed = HealthCareFragment.getSpeeds();slopes = HealthCareFragment.getSlopes();break;case 5:speed = ClimbFragment.getSpeeds();slopes = ClimbFragment.getSlopes();break;case 6:speed = PreinstallFragment.getSpeeds();slopes = PreinstallFragment.getSlopes();break;
            case 1:
                speed = P1Fragment.getSpeeds();
                slopes = P1Fragment.getSlopes();
                break;
            case 2:
                speed = P2Fragment.getSpeeds();
                slopes = P2Fragment.getSlopes();
                break;
            case 3:
                speed = P3Fragment.getSpeeds();
                slopes = P3Fragment.getSlopes();
                break;
            case 4:
                speed = P4Fragment.getSpeeds();
                slopes = P4Fragment.getSlopes();
                break;
            case 5:
                speed = P5Fragment.getSpeeds();
                slopes = P5Fragment.getSlopes();
                break;
            case 6:
                speed = P6Fragment.getSpeeds();
                slopes = P6Fragment.getSlopes();
                break;
            case 7:
                speed = P7Fragment.getSpeeds();
                slopes = P7Fragment.getSlopes();
                break;
            case 8:
                speed = P8Fragment.getSpeeds();
                slopes = P8Fragment.getSlopes();
                break;
            case 9:
                speed = P9Fragment.getSpeeds();
                slopes = P9Fragment.getSlopes();
                break;
            case 10:
                speed = P10Fragment.getSpeeds();
                slopes = P10Fragment.getSlopes();
                break;
            case 11:
                speed = P11Fragment.getSpeeds();
                slopes = P11Fragment.getSlopes();
                break;
            case 12:
                speed = P12Fragment.getSpeeds();
                slopes = P12Fragment.getSlopes();
                break;
            case 13:
                speed = P13Fragment.getSpeeds();
                slopes = P13Fragment.getSlopes();
                break;
            case 14:
                speed = P14Fragment.getSpeeds();
                slopes = P14Fragment.getSlopes();
                break;
            case 15:
                speed = P15Fragment.getSpeeds();
                slopes = P15Fragment.getSlopes();
                break;
            case 16:
                speed = P16Fragment.getSpeeds();
                slopes = P16Fragment.getSlopes();
                break;
            case 17:
                speed = P17Fragment.getSpeeds();
                slopes = P17Fragment.getSlopes();
                break;
            case 18:
                speed = P18Fragment.getSpeeds();
                slopes = P18Fragment.getSlopes();
                break;
            case 19:
                speed = P19Fragment.getSpeeds();
                slopes = P19Fragment.getSlopes();
                break;
            case 20:
                speed = P20Fragment.getSpeeds();
                slopes = P20Fragment.getSlopes();
                break;
            case 21:
                speed = P21Fragment.getSpeeds();
                slopes = P21Fragment.getSlopes();
                break;
            case 22:
                speed = P22Fragment.getSpeeds();
                slopes = P22Fragment.getSlopes();
                break;
            case 23:
                speed = P23Fragment.getSpeeds();
                slopes = P23Fragment.getSlopes();
                break;
            case 24:
                speed = P24Fragment.getSpeeds();
                slopes = P24Fragment.getSlopes();
                break;
            case 25:
                speed = P25Fragment.getSpeeds();
                slopes = P25Fragment.getSlopes();
                break;
            case 26:
                speed = P26Fragment.getSpeeds();
                slopes = P26Fragment.getSlopes();
                break;
            case 27:
                speed = P27Fragment.getSpeeds();
                slopes = P27Fragment.getSlopes();
                break;
            case 28:
                speed = P28Fragment.getSpeeds();
                slopes = P28Fragment.getSlopes();
                break;
            case 29:
                speed = P29Fragment.getSpeeds();
                slopes = P29Fragment.getSlopes();
                break;
            case 30:
                speed = P30Fragment.getSpeeds();
                slopes = P30Fragment.getSlopes();
                break;
            case 31:
                speed = P31Fragment.getSpeeds();
                slopes = P31Fragment.getSlopes();
                break;
            case 32:
                speed = P32Fragment.getSpeeds();
                slopes = P32Fragment.getSlopes();
                break;
            case 33:
                speed = P33Fragment.getSpeeds();
                slopes = P33Fragment.getSlopes();
                break;
            case 34:
                speed = P34Fragment.getSpeeds();
                slopes = P34Fragment.getSlopes();
                break;
            case 35:
                speed = P35Fragment.getSpeeds();
                slopes = P35Fragment.getSlopes();
                break;
            case 36:
                speed = P36Fragment.getSpeeds();
                slopes = P36Fragment.getSlopes();
                break;
            case 37:
                speed = P37Fragment.getSpeeds();
                slopes = P37Fragment.getSlopes();
                break;
            case 38:
                speed = P38Fragment.getSpeeds();
                slopes = P38Fragment.getSlopes();
                break;
            case 39:
                speed = P39Fragment.getSpeeds();
                slopes = P39Fragment.getSlopes();
                break;
            case 40:
                speed = P40Fragment.getSpeeds();
                slopes = P40Fragment.getSlopes();
                break;
            case 41:
                speed = P41Fragment.getSpeeds();
                slopes = P41Fragment.getSlopes();
                break;
            case 42:
                speed = P42Fragment.getSpeeds();
                slopes = P42Fragment.getSlopes();
                break;
            case 43:
                speed = P43Fragment.getSpeeds();
                slopes = P43Fragment.getSlopes();
                break;
            case 44:
                speed = P44Fragment.getSpeeds();
                slopes = P44Fragment.getSlopes();
                break;
            case 45:
                speed = P45Fragment.getSpeeds();
                slopes = P45Fragment.getSlopes();
                break;
            case 46:
                speed = P46Fragment.getSpeeds();
                slopes = P46Fragment.getSlopes();
                break;
            case 47:
                speed = P47Fragment.getSpeeds();
                slopes = P47Fragment.getSlopes();
                break;
            case 48:
                speed = P48Fragment.getSpeeds();
                slopes = P48Fragment.getSlopes();
                break;
            case 49:
                speed = P49Fragment.getSpeeds();
                slopes = P49Fragment.getSlopes();
                break;
            case 50:
                speed = P50Fragment.getSpeeds();
                slopes = P50Fragment.getSlopes();
                break;
            case 51:
                speed = P51Fragment.getSpeeds();
                slopes = P51Fragment.getSlopes();
                break;
            case 52:
                speed = P52Fragment.getSpeeds();
                slopes = P52Fragment.getSlopes();
                break;
            case 53:
                speed = P53Fragment.getSpeeds();
                slopes = P53Fragment.getSlopes();
                break;
            case 54:
                speed = P54Fragment.getSpeeds();
                slopes = P54Fragment.getSlopes();
                break;
            case 55:
                speed = P55Fragment.getSpeeds();
                slopes = P55Fragment.getSlopes();
                break;
            case 56:
                speed = P56Fragment.getSpeeds();
                slopes = P56Fragment.getSlopes();
                break;
            case 57:
                speed = P57Fragment.getSpeeds();
                slopes = P57Fragment.getSlopes();
                break;
            case 58:
                speed = P58Fragment.getSpeeds();
                slopes = P58Fragment.getSlopes();
                break;
            case 59:
                speed = P59Fragment.getSpeeds();
                slopes = P59Fragment.getSlopes();
                break;
            case 60:
                speed = P60Fragment.getSpeeds();
                slopes = P60Fragment.getSlopes();
                break;
            case 61:
                speed = P61Fragment.getSpeeds();
                slopes = P61Fragment.getSlopes();
                break;
            case 62:
                speed = P62Fragment.getSpeeds();
                slopes = P62Fragment.getSlopes();
                break;
            case 63:
                speed = P63Fragment.getSpeeds();
                slopes = P63Fragment.getSlopes();
                break;
            case 64:
                speed = P64Fragment.getSpeeds();
                slopes = P64Fragment.getSlopes();
                break;
        }
        time = PreinstallModelFragment.getTimes();
        modelType = 2;
    }

    private void customMode() {
        TreadApplication.getInstance().setSport(TreadApplication.getInstance().USER_RUN_STATUS);
        speed = new double[16];
        slopes = new int[16];
        switch (CustomModelFragment.getCurrentIndex()) {
            case 0:
                time = CustomUser1Fragment.getmTime();
                speed = CustomUser1Fragment.getmSpeed();
                slopes = CustomUser1Fragment.getmSlopes();
                break;
            case 1:
                time = CustomUser2Fragment.getmTime();
                speed = CustomUser2Fragment.getmSpeed();
                slopes = CustomUser2Fragment.getmSlopes();
                break;
            case 2:
                time = CustomUser3Fragment.getmTime();
                speed = CustomUser3Fragment.getmSpeed();
                slopes = CustomUser3Fragment.getmSlopes();
                break;
            case 3:
                time = CustomUser4Fragment.getmTime();
                speed = CustomUser4Fragment.getmSpeed();
                slopes = CustomUser4Fragment.getmSlopes();
                break;
            case 4:
                time = CustomUser5Fragment.getmTime();
                speed = CustomUser5Fragment.getmSpeed();
                slopes = CustomUser5Fragment.getmSlopes();
                break;
        }
        modelType = 2;
    }

    private void heartRateControl() {
        if (HeartModelFragment.getTimes() != 0)
            modelType = 1;
        speed = new double[16];
        slopes = new int[16];
        heartSum = HeartModelFragment.getHeart();
        time = HeartModelFragment.getTimes();
        TreadApplication.getInstance().setSport(TreadApplication.getInstance().HRC_RUN_STATUS);
        setArray();
    }

    /**
     * 比较目标心率
     */
    public static void heartS() {
        if (heart == 0)
            return;
        modelType = 1;
        if (getHeart() > heart) {
            mSpeed += 0.5;
        }
        if (getHeart() < heart) {
            mSpeed -= 0.5;
        }
        setmSpeed(mSpeed);
    }

    public static int getNumber() {
        return number;
    }

    public static void setNumber(int numbers) {
        number = numbers;
        switch (modelType) {
            case 2:
                setmSlopes(slopes[number]);
                setmSpeed(speed[number]);
                break;
        }
    }

    private void start() {
        mileageSum = 0;
        heat = 0;
        heartTime = 0;
        avgSpeed = 0;
        avgHeart = 0;
        window_tv_speed.setText(((int) (mSpeed * 10)) / 10.0 + "");
        window_tv_gradient.setText(mSlopes * 10 / 10 + "");
    }

    public static String getDistance() {
        return ((int) (mileageSum * 100)) / 100.0 + "";
    }

    public static void setDate() {//状态栏值传递
        mileageSum = mileageSum + mSpeed / 3600.0;
        MySharePreferences mySharePreferences = new MySharePreferences(mContext, "user" + (MainActivity.getUserIndex() + 1));
        String str = mySharePreferences.getUserWeight();
//        heat = heat + (getmSpeed() * 70.3 * (1 + getmSlopes() / 100)) / 3600;
        if (getmSpeed() * 0.6213711 <= 3.7) {
            heat += (1 + (0.768 * getmSpeed() * 0.6213711) + (0.137 * getmSpeed() * 0.6213711 * (getmSlopes() + 1))) * Integer.parseInt(str) / (2.2 * 3600);
//            heat += (1 + (0.768 * getmSpeed() * 0.6213711) + (0.137 * getmSpeed() * 0.6213711 * (getmSlopes() + 1)) * Integer.parseInt(str) / 3600);
        } else if (getmSpeed() * 0.6213711 > 3.7) {
            heat += (1 + (1.532 * getmSpeed() * 0.6213711) + (0.0685 * getmSpeed() * 0.6213711 * (getmSlopes() + 1))) * Integer.parseInt(str) / (2.2 * 3600);
//            heat += (1 + (1.532 * getmSpeed() * 0.6213711) + (0.0685 * getmSpeed() * 0.6213711 * (getmSlopes() + 1)) * Integer.parseInt(str) / 3600);
        }
        if (heart != 0) {
            avgHeart = heart + avgHeart;
            heartTime++;
        }
        avgSpeed = avgSpeed + mSpeed;
        if (TreadApplication.getInstance().getSport() == TreadApplication.getInstance().END_STATUS)
            return;
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mType != 2 && TreadApplication.getInstance().getSport() != TreadApplication.getInstance().END_STATUS)
//                    window_tv_mileage.setText(mileStr.format((mileageSum % 100) >= 99.99 ? 99.99 : (mileageSum % 100)));
                    window_tv_mileage.setText(mileStr.format(new BigDecimal(mileageSum % 100).setScale(2, BigDecimal.ROUND_DOWN).doubleValue()) + "");
//                    window_tv_mileage.setText(String.format("%.2f",mileageSum % 100));
                else if (mType == 2) {
                    window_tv_mileage.setText(mileStr.format((milSum - mileageSum) < 0 ? 0 : (milSum - mileageSum)));
                    if (milSum <= mileageSum)
                        stoping();
                }
                if (mType != 3 && TreadApplication.getInstance().getSport() != TreadApplication.getInstance().END_STATUS)
//                    window_tv_heat.setText(speedStr.format((heat % 1000) >= 999.9 ? 999.9 : (heat % 1000)));
                    window_tv_heat.setText(new BigDecimal(heat % 1000).setScale(1, BigDecimal.ROUND_DOWN).doubleValue() + "");
                else if (mType == 3) {
                    window_tv_heat.setText(((int) (((heatSum - heat) < 0 ? 0 : (heatSum - heat)) * 10)) / 10.0 + "");
                    if (heatSum <= heat) {
                        heat = heatSum;
                        stoping();
                    }
                }
            }
        });
    }

    public static String getTimesss() {
        return timesss;
    }

    private static String timesss;
    private static double heatsss;
    private static double milsss;
    private static int timeRun;

    public static void stoping() {
        TreadApplication.getInstance().setSport(TreadApplication.getInstance().END_STATUS);
        TreadApplication.setMediaPlayer(R.raw.stop);
        if (gradient.isShowings())
            gradient.dimss();
        if (MainActivity.getCurrentIndex() == 1 && RunFragment.getCurrentIndex() == 5)
            MainActivity.setTitles(mContext.getString(R.string.stoping));
        onUpOrDown.onUP();
        MyTime.timeStop();
        timesss = MyTime.positiveTiming();
        timeRun = getTimeShow();
        heatsss = ((int) (heat * 10)) / 10.0;
        milsss = mileageSum;
//        if (isHou) {
//            Intent intent = TreadApplication.getInstance().getBaseContext().getPackageManager().getLaunchIntentForPackage("com.vigorchip.treadmill.wr2");
//            TreadApplication.getInstance().getBaseContext().startActivity(intent);
//        }
        timer = new Timer();
        myTimerTask = new MyTimerTask();
        timer.schedule(myTimerTask, 0, 200);
    }

    static DialogAdjustVelocityGradient gradient;

    private static void updates() {
        if (gradient != null && gradient.isShowings()) {
            gradient.update();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.slopes_ll://扬升
//                if (TreadApplication.getInstance().isRuning())
//                    new DialogSportSlopes(getApplicationContext()).createDialog();
//                break;
//            case R.id.speed_ll://速度
//                if (TreadApplication.getInstance().isRuning())
//                    new DialogSportSpeed(getApplicationContext()).createDialog();
//                break;
//            case adjust_ll://调整
//                if (TreadApplication.getInstance().isRuning()) {
//                    isDown = false;
//                    gradient = new DialogAdjustVelocityGradient(getApplicationContext());
//                    gradient.createDialog();
//                }
//                break;
            case R.id.end_ll://结束
                if (TreadApplication.getInstance().isRuning()) {
                    isDown = false;
                    stoping();
                }
                break;
            case R.id.sub_iv://小白点
                if (TreadApplication.getInstance().isRuning()) {
                    if (mFloatLayout.getVisibility() == View.GONE) {
                        mFloatLayout.setVisibility(View.VISIBLE);
                        Animation anima = AnimationUtils.loadAnimation(mContext, R.anim.water_up);
                        anima.setDuration(200);
                        yijikang_ll.startAnimation(anima);
                    } else if (mFloatLayout.getVisibility() == View.VISIBLE) {
                        animDown();
                    }
                }
                break;
            case back_iv://返回键
                setBackPressed(KeyEvent.KEYCODE_BACK);
                break;
            case volume_iv_plus://音量加
                serialComm.upRaise();
                break;
            case home_iv://HOME键
                setBackPressed(KeyEvent.KEYCODE_HOME);
//                final int repeatCount = (KeyEvent.FLAG_VIRTUAL_HARD_KEY & KeyEvent.FLAG_LONG_PRESS) != 0 ? 1 : 0;
//                final KeyEvent evDown = new KeyEvent(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
//                        KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_HOME, repeatCount, 0, KeyCharacterMap.VIRTUAL_KEYBOARD,
//                        0, KeyEvent.FLAG_VIRTUAL_HARD_KEY | KeyEvent.FLAG_FROM_SYSTEM | KeyEvent.FLAG_VIRTUAL_HARD_KEY,
//                        InputDevice.SOURCE_KEYBOARD);
//                final KeyEvent evUp = new KeyEvent(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
//                        KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HOME, repeatCount, 0, KeyCharacterMap.VIRTUAL_KEYBOARD, 0,
//                        KeyEvent.FLAG_VIRTUAL_HARD_KEY | KeyEvent.FLAG_FROM_SYSTEM | KeyEvent.FLAG_VIRTUAL_HARD_KEY,
//                        InputDevice.SOURCE_KEYBOARD);
//                Class<?> ClassInputManager;
//                try {
//                    ClassInputManager = Class.forName("android.hardware.input.InputManager");
//                    Method[] methods = ClassInputManager.getMethods();
//                    System.out.println("cchen " + Arrays.toString(methods));
//                    Method methodInjectInputEvent = null;
//                    Method methodGetInstance = null;
//                    for (Method method : methods) {
//                        System.out.println("cchen " + method.getName());
//                        if (method.getName().contains("getInstance")) {
//                            methodGetInstance = method;
//                        }
//                        if (method.getName().contains("injectInputEvent")) {
//                            methodInjectInputEvent = method;
//                        }
//                    }
//                    Object instance = methodGetInstance.invoke(ClassInputManager, null);
//                    boolean bool = InputManager.class.isInstance(instance);
//                    System.out.println("cchen  -- " + bool);
////                     methodInjectInputEvent =InputManager.getMethod("injectInputEvent",KeyEvent.class, Integer.class);
//                    methodInjectInputEvent.invoke(instance, evDown, 0);
//                    methodInjectInputEvent.invoke(instance, evUp, 0);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                break;
            case volume_iv_down://音量减
                serialComm.dowlower();
                break;
        }
    }

    private void setBackPressed(int value) {
        final int repeatCount = (KeyEvent.FLAG_VIRTUAL_HARD_KEY & KeyEvent.FLAG_LONG_PRESS) != 0 ? 1 : 0;
        final KeyEvent evDown = new KeyEvent(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
                KeyEvent.ACTION_DOWN, value, repeatCount, 0, KeyCharacterMap.VIRTUAL_KEYBOARD,
                0, KeyEvent.FLAG_VIRTUAL_HARD_KEY | KeyEvent.FLAG_FROM_SYSTEM | KeyEvent.FLAG_VIRTUAL_HARD_KEY,
                InputDevice.SOURCE_KEYBOARD);
        final KeyEvent evUp = new KeyEvent(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
                KeyEvent.ACTION_UP, value, repeatCount, 0, KeyCharacterMap.VIRTUAL_KEYBOARD, 0,
                KeyEvent.FLAG_VIRTUAL_HARD_KEY | KeyEvent.FLAG_FROM_SYSTEM | KeyEvent.FLAG_VIRTUAL_HARD_KEY,
                InputDevice.SOURCE_KEYBOARD);
        Class<?> ClassInputManager;
        try {
            ClassInputManager = Class.forName("android.hardware.input.InputManager");
            Method[] methods = ClassInputManager.getMethods();
            System.out.println("cchen " + Arrays.toString(methods));
            Method methodInjectInputEvent = null;
            Method methodGetInstance = null;
            for (Method method : methods) {
                System.out.println("cchen " + method.getName());
                if (method.getName().contains("getInstance")) {
                    methodGetInstance = method;
                }
                if (method.getName().contains("injectInputEvent")) {
                    methodInjectInputEvent = method;
                }
            }
            Object instance = methodGetInstance.invoke(ClassInputManager, (Object[]) null);
            boolean bool = InputManager.class.isInstance(instance);
            System.out.println("cchen  -- " + bool);
            // methodInjectInputEvent =InputManager.getMethod("injectInputEvent",KeyEvent.class, Integer.class);
            methodInjectInputEvent.invoke(instance, evDown, 0);
            methodInjectInputEvent.invoke(instance, evUp, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mSlopes <= 0 && mSpeed <= 0) {
                        stop();
                    } else {
                        mFloatLayout.setVisibility(View.VISIBLE);
                        roundLayout.setVisibility(View.VISIBLE);
//                        IsSerivceRun.hideImageView();
                        mSpeed = (mSpeed - 0.1) >= 0 ? (mSpeed - 0.1) : 0.0;
                        window_tv_speed.setText(((int) (mSpeed * 10)) / 10.0 + "");
                        setmSlopes(mSlopes > 0 ? --mSlopes : 0);
                    }
                }
            });
        }
    }

    public static boolean isHou;

    private static void stop() {
        if (isHou) {
            Intent intent = TreadApplication.getInstance().getBaseContext().getPackageManager().getLaunchIntentForPackage("com.vigorchip.treadmill.wr2");
            TreadApplication.getInstance().getBaseContext().startActivity(intent);
        }
        myTime.stopS();
//        DialogSportSlopes.dimss();
//        DialogSportSpeed.dimss();
        if (gradient.isShowings())
            gradient.dimss();
        onClickSta.onStarts();
        try {
            RunningModelFragment.clear();
        } catch (Exception e) {
            Log.e("treadmill", "WindowInfoService的调用clear为空");
        }
        if (myTimerTask != null) {
            myTimerTask.cancel();
            myTimerTask = null;
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        MainFragment.referter();
        if (!TreadApplication.getInstance().isShare())
            TreadApplication.getInstance().setIsShare(true);
        window_chr_time.setText("00:00");
        window_tv_mileage.setText("0.00");
        window_tv_heat.setText("0");
        window_tv_heartRate.setText("0");
        RunningModelFragment.setRun(true);
//        IsSerivceRun.showImageView();
        mFloatLayout.setVisibility(View.GONE);
        roundLayout.setVisibility(View.GONE);
        MyTime.setTimeShow();
    }

    public void creatDialog(int type) {
        DialogCountDown.dimss();
        stop();
        mSpeed = 0.0;//速度
        mileageSum = 0.0;//里程
        heat = 0.0;//热量
        mSlopes = 0;//坡度
        window_chr_time.setText("00:00");
        window_tv_mileage.setText(((int) (mileageSum * 100)) / 100.0 + "");
        window_tv_gradient.setText(mSlopes * 10 / 10 + "");
        window_tv_speed.setText(((int) (mSpeed * 10)) / 10.0 + "");
        window_tv_heat.setText(((int) (heat * 10)) / 10.0 + "");
        window_tv_heartRate.setText(heart * 10 / 10 + "");
        if (dialogError == null)
            dialogError = new DialogError(mContext);
        dialogError.creatDialog(type);
    }

    DialogError dialogError;

    public void dimss() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                for (int i = 0; i < 2; i++) {
                    mainActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            SerialComm.setBEEP(1);
                        }
                    });
                    try {
                        Thread.sleep(140);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
        if (!isHou)
            onClickSta.onStarts();
        mFloatLayout.setVisibility(View.GONE);
        roundLayout.setVisibility(View.GONE);
//        IsSerivceRun.showImageView();
        dialogError.dimss();
        mSpeed = 0.0;//速度
        mileageSum = 0.0;//里程
        heat = 0.0;//热量
        mSlopes = 0;//坡度
        window_chr_time.setText("00:00");
        window_tv_mileage.setText(((int) (mileageSum * 100)) / 100.0 + "");
        window_tv_gradient.setText(mSlopes * 10 / 10 + "");
        window_tv_speed.setText(((int) (mSpeed * 10)) / 10.0 + "");
        window_tv_heat.setText(((int) (heat * 10)) / 10.0 + "");
        window_tv_heartRate.setText(heart * 10 / 10 + "");
        TreadApplication.getInstance().setSport(TreadApplication.getInstance().NORMAL_STATUS);
        MainFragment.referter();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatLayout != null)
            mWindowManager.removeView(mFloatLayout);//移除悬浮窗口
//        if (SuspendButtonIv != null)
//            mWindowManagers.removeView(SuspendButtonIv);//移除悬浮窗口
        if (roundLayout != null) {
            mWindowManagers.removeView(roundLayout);//移除悬浮窗口
        }
        serialComm.close();
        stopSelf();
    }

    Animation
            animationTime;
    // ,            animationMileage, animationHeat, animationHeart, animationSlopes, animationSpeed;
    int index;//控件动画位置

//    private void initAnim() {
//        Log.e("TAG", "汗汗汗6");
//        new Thread() {
//            @Override
//            public void run() {
//                super.run();
//                for (int i = 0; i < 5; i++) {
//                    try {
//                        Thread.sleep(60);
//                    } catch (InterruptedException e) {
//                    }
//                    animHandler.sendEmptyMessage(index++);
//                }
//                handler.removeCallbacks(this);
//            }
//        }.start();
//    }

//    static float curTranslationY;

    public void animUp() {
//         curTranslationY=mileage_ll.getTranslationY();
//        if (TreadApplication.getInstance().getCustom().equals("yijikang")) {
//            Log.e("animUp", "TreadApplication.getInstance().getCustom():" + TreadApplication.getInstance().getCustom());
        mFloatLayout.setVisibility(View.VISIBLE);
        animationTime = AnimationUtils.loadAnimation(mContext, R.anim.water_up);
        animationTime.setDuration(100);
        yijikang_ll.startAnimation(animationTime);

//        animationTime.setAnimationListener(listener);
//        } else {
//            animationTime = AnimationUtils.loadAnimation(mContext, R.anim.water_up);
//            animationMileage = AnimationUtils.loadAnimation(mContext, R.anim.water_up);
//            animationHeat = AnimationUtils.loadAnimation(mContext, R.anim.water_up);
//            animationHeart = AnimationUtils.loadAnimation(mContext, R.anim.water_up);
//            animationSlopes = AnimationUtils.loadAnimation(mContext, R.anim.water_up);
//            animationSpeed = AnimationUtils.loadAnimation(mContext, R.anim.water_up);
//            index = 0;
//            time_ll.startAnimation(animationTime);
////        obiectAnim(time_ll);
//            initAnim();
//        }
    }

    public void animDown() {
        Log.e("getVisibility", "animDown");
        Animation animations = AnimationUtils.loadAnimation(mContext, R.anim.water_down);
        animations.setDuration(200);
        yijikang_ll.startAnimation(animations);
        animations.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isDown = true;
                yijikang_ll.setY(0);
                mFloatLayout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
//        } else {
//            Log.e("TAGHHH", "TreadApplication.getInstance().getCustom():" + TreadApplication.getInstance().getCustom());
//            animationTime = AnimationUtils.loadAnimation(mContext, R.anim.water_down);
//            animationMileage = AnimationUtils.loadAnimation(mContext, R.anim.water_down);
//            animationHeat = AnimationUtils.loadAnimation(mContext, R.anim.water_down);
//            animationHeart = AnimationUtils.loadAnimation(mContext, R.anim.water_down);
//            animationSlopes = AnimationUtils.loadAnimation(mContext, R.anim.water_down);
//            animationSpeed = AnimationUtils.loadAnimation(mContext, R.anim.water_down);
//            index = 0;
//            time_ll.startAnimation(animationTime);
////        obiectAnim(time_ll);
//            initAnim();
//        }
    }

//    private static void obiectAnim(LinearLayout ll) {
//        ObjectAnimator animators = ObjectAnimator.ofFloat(ll, "translationY", curTranslationY, 100f);
//        animators.setDuration(1000);
//        animators.start();
//    }

//    Handler animHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case 0:
//                    mileage_ll.startAnimation(animationMileage);
////                    obiectAnim(mileage_ll);
//                    break;
//                case 1:
//                    heat_ll.startAnimation(animationHeat);
////                    obiectAnim(heat_ll);
//                    break;
//                case 2:
//                    heart_ll.startAnimation(animationHeart);
////                    obiectAnim(heart_ll);
//                    break;
//                case 3:
//                    slopes_ll.startAnimation(animationSlopes);
////                    obiectAnim(slopes_ll);
//                    break;
//                case 4:
//                    speed_ll.startAnimation(animationSpeed);
////                    obiectAnim(speed_ll);
////                    obiectAnim(mFloatLayout);
////                    animationSpeed.setAnimationListener(listener);
//                    break;
//            }
//        }
//    };
}