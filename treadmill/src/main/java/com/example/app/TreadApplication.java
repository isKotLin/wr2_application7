package com.example.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Process;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import com.example.bean.NetWorkChangeBroadcastReceiver;
import com.example.dialog.DialogCountDown;
import com.example.dialog.DialogUserInfo;
import com.example.utils.MySharePreferences;
import com.tencent.bugly.crashreport.CrashReport;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;

import io.vov.vitamio.Vitamio;

public class TreadApplication extends Application {
    private static TreadApplication instance;

    public static TreadApplication getInstance() {
        return instance;
    }

    private static boolean isFirst;
    public int sport;
    public static final int NORMAL_STATUS = 0;//普通状态
    public static final int NOTSAFE_STATUS = 1; //安全锁脱落
    public static final int TIME_SETUP_STATUS = 2;//时间模式设置
    public static final int DISTANCE_SETUP_STATUS = 3;//距离模式设置
    public static final int CALORIE_SETUP_STATUS = 4;//卡路里模式设置
    public static final int PROGRAM_SETUP_STATUS = 5;//自动程式模式设置
    public static final int FIVE_SECOND_STATUS = 6;//开始准备
    public static final int NORMAL_RUN_STATUS = 7;//手动跑步
    public static final int TIME_RUN_STATUS = 8;//时间倒数跑步
    public static final int DISTANCE_RUN_STATUS = 9;//距离倒数跑步
    public static final int CALORIE_RUN_STATUS = 10;//卡路里倒数跑步
    public static final int PROGRAM_RUN_STATUS = 11;//自动程式跑步
    public static final int END_STATUS = 12;//运动结束
    public static final int ERROR_STATUS = 14;//错误状态
    public static final int HRC_SETUP_STATUS = 16;//HRC 模式设置
    public static final int HRC_RUN_STATUS = 17;//HRC 跑步
    public static final int USER_SETUP_STATUS = 18;//用户模式设置
    public static final int USER_RUN_STATUS = 19;//用户跑步
    public static final int FAT_RUN_STATUS = 21;//减脂模式跑步
    public static final int RECOVERY_RUN_STATUS = 23;//康复模式跑步
    public String WIN_INFO_NAME = "com.example.service.WindowInfoService";
    private static int preIndex;
    public static double MAXSPEED = 16.8;//最大速度
    public static double MINSPEED = 0.6;//最小速度
    public static int SLOPES = 15;//最大扬升
    public static int OIL_DISTANCE = 200;//加油路程
    public static int OIL_TIME = (50 * 60);//加油时间
    public static int OIL_COUNT = 5;//加油次数
    public static int OIL_ENABLE = 0;//加油开关
    public static String APP_TYPE = "";
    public static Typeface font;
    static Context context;
    public static boolean isN0 = true;

    public static boolean isShare() {
        return isShare;
    }

    public static void setIsShare(boolean isShare) {
        TreadApplication.isShare = isShare;
    }

    private static boolean isShare;
    public static SharedPreferences sp;

    public static int getPreIndex() {//在程序模式的第几个界面
        if (preIndex == 64)//程序模式64个
            preIndex = 0;
        return ++preIndex;
    }

    public static void setPreIndex(int preIndex) {
        TreadApplication.preIndex = preIndex;
    }

    public static boolean isLogs() {
        return logs;
    }

    private static boolean logs;

    public static void setLog(boolean log) {
        logs = log;
    }

    public static String getCustom() {
        return custom;
    }

    private static String custom = "yijikang";

    @Override
    public void onCreate() {
        super.onCreate();// 获取当前包名
        instance = this;
        Log.e("TAG", "==============Treadmill=====onCreate==================");
        //bugly初始化
        String packageName = TreadApplication.getInstance().getApplicationContext().getPackageName();//获取包名
        String processName = getProcessName(Process.myPid());//获取进程名
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(TreadApplication.getInstance().getApplicationContext());
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
        CrashReport.initCrashReport(TreadApplication.getInstance().getApplicationContext(), "44fb8d7b72", false, strategy);
        //配置文件
        if (new File("/system/treadmill/smart_run.dat").exists()) {
            if (GetBuildProproperties("Speed-max", "/system/treadmill/smart_run.dat") != null)
                MAXSPEED = Double.parseDouble(GetBuildProproperties("Speed-max", "/system/treadmill/smart_run.dat")) / 10;
            if (GetBuildProproperties("Speed-min", "/system/treadmill/smart_run.dat") != null)
                MINSPEED = Double.parseDouble(GetBuildProproperties("Speed-min", "/system/treadmill/smart_run.dat")) / 10;
            if (GetBuildProproperties("Incline-max", "/system/treadmill/smart_run.dat") != null)
                SLOPES = Integer.parseInt(GetBuildProproperties("Incline-max", "/system/treadmill/smart_run.dat"));
            if (GetBuildProproperties("Incline-max", "/system/treadmill/smart_run.dat") != null)
                APP_TYPE = GetBuildProproperties("app_type", "/system/treadmill/smart_run.dat");
            if (GetBuildProproperties("oil-enable", "/system/treadmill/smart_run.dat") != null)
                OIL_ENABLE = Integer.parseInt(GetBuildProproperties("oil-enable", "/system/treadmill/smart_run.dat"));
            if (GetBuildProproperties("oil-distance", "/system/treadmill/smart_run.dat") != null)
                OIL_DISTANCE = Integer.parseInt(GetBuildProproperties("oil-distance", "/system/treadmill/smart_run.dat"));
            if (GetBuildProproperties("oil-time", "/system/treadmill/smart_run.dat") != null)
                OIL_TIME = Integer.parseInt(GetBuildProproperties("oil-time", "/system/treadmill/smart_run.dat"));
            if (GetBuildProproperties("oil-count", "/system/treadmill/smart_run.dat") != null)
                OIL_COUNT = Integer.parseInt(GetBuildProproperties("oil-count", "/system/treadmill/smart_run.dat"));
            custom = GetBuildProproperties("Custom", "/system/treadmill/smart_run.dat");
            if (custom == null)
                custom = "yijikang";
        }

        isFirst = true;
        sport = 0;
        font = Typeface.createFromAsset(TreadApplication.getInstance().getApplicationContext().getAssets(), "font/Fragma Light.ttf");//字体
//        switchLanguage(new MySharePreferences(getApplicationContext(), "else").getString());
        SystemClock.setCurrentTimeMillis(new MySharePreferences(getApplicationContext(), "date").getDate());//设置时间
//        NetWorkChangeBroadcastReceiver.setCurrentTimeMillis(new MySharePreferences(getApplicationContext(), "date").getDate());
        new NetWorkChangeBroadcastReceiver().startCalibrateTime(this);//校准网络时间
        Vitamio.isInitialized(getApplicationContext());
        TreadApplication.getInstance().context = getApplicationContext();
        NetWorkChangeBroadcastReceiver.setIsGood(false);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
    }

    public static int doorStatus;

    public static int getDoorStatus() {
        doorStatus = TreadApplication.sp.getInt("DOORSTATUS", 1);
        return doorStatus;
    }

    public static void setDoorStatus(int doorStatusss) {//后门
        doorStatus = doorStatusss;
        TreadApplication.sp.edit().putInt("DOORSTATUS", doorStatusss).commit();
    }

    public static Context getAppContext() {
        return TreadApplication.getInstance().context;
    }


    // 获取build.prop中的指定属性
    public static String GetBuildProproperties(String PropertiesName, String path) {
        try {
            InputStream is = new BufferedInputStream(new FileInputStream(new File(path)));
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String strTemp = "";
            while ((strTemp = br.readLine()) != null) {// 如果文件没有读完则继续
                if (strTemp.indexOf(PropertiesName) != -1)
                    return strTemp.substring(strTemp.indexOf("=") + 1);
            }
            br.close();
            is.close();
            return null;
        } catch (Exception e) {
            if (e.getMessage() != null)
                System.out.println(e.getMessage());
            else
                e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    public int getSport() {
        return sport;
    }

    public void setSport(int sports) {
        sport = sports;
    }

    public static Boolean getFirst() {
        return isFirst;
    }

    public static void setFirst(Boolean first) {
        isFirst = first;
    }

    protected void switchLanguage(String language) {
        Resources resources = getResources();//设置应用语言类型
        Configuration config = resources.getConfiguration();
        DisplayMetrics dm = resources.getDisplayMetrics();
        if (language.equals("en"))
            config.locale = Locale.ENGLISH;
        else
            config.locale = Locale.SIMPLIFIED_CHINESE;
        resources.updateConfiguration(config, dm);
    }

    public boolean isRuning() {
        if (TreadApplication.getInstance().getSport() == TreadApplication.getInstance().NORMAL_RUN_STATUS ||
                TreadApplication.getInstance().getSport() == TreadApplication.getInstance().TIME_RUN_STATUS ||
                TreadApplication.getInstance().getSport() == TreadApplication.getInstance().DISTANCE_RUN_STATUS ||
                TreadApplication.getInstance().getSport() == TreadApplication.getInstance().CALORIE_RUN_STATUS ||
                TreadApplication.getInstance().getSport() == TreadApplication.getInstance().PROGRAM_RUN_STATUS ||
                TreadApplication.getInstance().getSport() == TreadApplication.getInstance().HRC_RUN_STATUS ||
                TreadApplication.getInstance().getSport() == TreadApplication.getInstance().USER_RUN_STATUS ||
                TreadApplication.getInstance().getSport() == TreadApplication.getInstance().FAT_RUN_STATUS ||
                TreadApplication.getInstance().getSport() == TreadApplication.getInstance().RECOVERY_RUN_STATUS)
            return true;//跑步中
        else
            return false;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        DialogCountDown.dimss();
        DialogUserInfo.dimss();
    }

    public static void setMediaPlayerP(Context context, int path) {
        MediaPlayer mp = MediaPlayer.create(context, path);
        mp.start();
    }

    public static void setMediaPlayer(int path) {
        setMediaPlayer(getAppContext(), path);
    }

    private static MediaPlayer imp;
    private static int oldPath;

    public static void setMediaPlayer(final Context context, final int path) {
        if (path == oldPath && imp != null && imp.isPlaying()) {
            oldPath = path;
            return;
        }
        if (oldPath != path && imp != null) {
            imp.pause();
            imp.release();
            imp = null;
            imp = MediaPlayer.create(context, path);
        }
        oldPath = path;
        imp = MediaPlayer.create(context, path);
        imp.start();
        imp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
                imp = null;
                imp = MediaPlayer.create(context, path);
            }
        });
    }
}
