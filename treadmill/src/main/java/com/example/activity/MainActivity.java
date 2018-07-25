package com.example.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.app.BaseActivity;
import com.example.app.TreadApplication;
import com.example.dialog.DialogAdjustVelocityGradient;
import com.example.dialog.DialogIsComeOn;
import com.example.dialog.DialogRefuel;
import com.example.dialog.DialogUserAble;
import com.example.dialog.DialogUserIcon;
import com.example.fragment.AppsFragment;
import com.example.fragment.MainFragment;
import com.example.fragment.MediaFragment;
import com.example.fragment.OperatorGuideFragment;
import com.example.fragment.RunFragment;
import com.example.fragment.RunningModelFragment;
import com.example.fragment.SceneFragment;
import com.example.fragment.SettingFragment;
import com.example.fragment.ShareFragment;
import com.example.fragment.UserFragment;
import com.example.fragment.VideoFragment;
import com.example.moudle.OnClickMain;
import com.example.moudle.OnClickStart;
import com.example.moudle.SerialComm;
import com.example.okttp.Okhttp;
import com.example.okttp.UrlConstant;
import com.example.service.WindowInfoService;
import com.example.utils.FileUtils;
import com.example.utils.LogUtils;
import com.example.utils.MySharePreferences;
import com.example.utils.NowDateTime;
import com.example.utils.PublicUtls;
import com.example.utils.UpdataSystemUtil;
import com.vigorchip.treadmill.wr2.R;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.vov.vitamio.utils.Log;
import okhttp3.Call;

import static com.example.service.WindowInfoService.setTextView;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static int userIndex;//用户的位数
    ImageView home_iv_userIcon, title_iv_back;
    TextView window_tv_time;
    private int[] userIcon = {R.mipmap.avatar1, R.mipmap.avatar2, R.mipmap.avatar3, R.mipmap.avatar4, R.mipmap.avatar5};
    static TextView window_tv_date;
    public LinearLayout activity_main_ll_background;
    public RelativeLayout rl_title;
    int[] ResID = {R.mipmap.wallpaper1, R.mipmap.wallpaper2, R.mipmap.wallpaper3, R.mipmap.wallpaper4, R.mipmap.wallpaper5};
    private static final String CURRENT_FRAGMENT = "STATE_FRAGMENT_SHOW";//当前显示的fragment
    private static final String USER_INDEX = "USER_INDEX";//当前显示的fragment
    private FragmentManager fragmentManager;
    private Fragment currentFragment = new Fragment();
    private List<Fragment> fragments = new ArrayList<>();

    public static String getDATAPATH() {
        return DATAPATH;
    }

    private static String DATAPATH = "/mydata/save.txt";

    public static int getCurrentIndex() {
        return currentIndex;
    }

    private static int currentIndex = 0;//fragment的页数

    MySharePreferences mySharePreferences;
    private static boolean isVideo = false;
    private boolean isRunModel = false;
    private static boolean isLian;

    public void setSigns(int signs) {
        if (signs <= 50) {
            title_iv_back.setBackgroundResource(R.mipmap.wifi04);
            isLian = true;
        } else if (signs > 50 && signs <= 75) {
            title_iv_back.setBackgroundResource(R.mipmap.wifi03);
            isLian = true;
        } else if (signs > 75 && signs <= 90) {
            title_iv_back.setBackgroundResource(R.mipmap.wifi02);
            isLian = true;
        } else if (signs > 90 && signs <= 100) {
            isLian = true;
            title_iv_back.setBackgroundResource(R.mipmap.wifi01);
        } else {
            isLian = false;
            title_iv_back.setBackgroundResource(R.mipmap.wifi00);
        }
    }

    public boolean isVideo() {
        return isVideo;
    }

//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        NavigationBarStatusBar(this, true);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TreadApplication.setMediaPlayer(R.raw.welcome);
//        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        Log.e("TAG", "=========================onCreate");
        try {
            registerHomeKeyReceiver(this);
        } catch (Exception e) {
            Log.e("MainActivity", "Home键注册错误");
        }
        setDefaultFragment(savedInstanceState);
        initData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        WindowInfoService.isHou = true;
        Log.e("TAG", "=================onPause=================");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("TAG", "=================onDestroy=================");
        try {
            unregisterHomeKeyReceiver(this);
        } catch (Exception e) {
            Log.e("MainActivity", "Home键没有注册");
        }
//        if (transaction != null) {
//            transaction.detach(currentFragment);
//        }
//        finally {
//            registerHomeKeyReceiver(this);
//        }
    }

//    @Override
//    public void finish() {
//        /**
//         * 记住不要执行此句 super.finish(); 因为这是父类已经实现了改方法
//         * 设置该activity永不过期，即不执行onDestroy()
//         */
//        moveTaskToBack(true);
//    }

    @Override
    protected void onResume() {
        super.onResume();
        WindowInfoService.isHou = false;
        Log.e("TAG", "=================onResume=================");
    }

    private void initData() {
        Log.e("TAG", "=================initData=================");
        home_iv_userIcon = (ImageView) findViewById(R.id.home_iv_userIcon);
        window_tv_time = (TextView) findViewById(R.id.title_tv_time);
        window_tv_date = (TextView) findViewById(R.id.title_tv_date);
        rl_title = (RelativeLayout) findViewById(R.id.rl_title);
        activity_main_ll_background = (LinearLayout) findViewById(R.id.activity_main_ll_background);
        title_iv_back = (ImageView) findViewById(R.id.title_iv_back);
        mySharePreferences = new MySharePreferences(getBaseContext(), "else");
        home_iv_userIcon.setOnClickListener(this);
        activity_main_ll_background.setBackground(getResources().getDrawable(ResID[mySharePreferences.getWall()]));//设置已保存的背景
        getDialogs();
        initView();
    }

    WifiManager wifiManager;
    WifiInfo currentWifiInfo;

    private void initView() {
        Log.e("TAG", "=================initView=================");
        if (TreadApplication.getInstance().OIL_ENABLE != 0)
            createFile();
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        handler.post(runnable);
    }

    DialogRefuel dialogRefuel;
    DialogIsComeOn dialogCome;

    public static File getFile() {
        return file;
    }

    static File file;

    private void createFile() {
        file = new File(DATAPATH);
        if (file.exists()) {//检验时间 里程 次数
            try {
                String record = FileUtils.readTextFromFile(MainActivity.getDATAPATH());
                String[] temp = record.split(" ");
                if (TreadApplication.getInstance().OIL_TIME * 60 == 0) {
                    if (Double.parseDouble(temp[1]) >= TreadApplication.getInstance().OIL_DISTANCE * 1000) {
                        qiMaiSiOil();
                    }
                } else if (TreadApplication.getInstance().OIL_DISTANCE * 1000 * 60 == 0) {
                    if (Integer.parseInt(temp[0]) >= TreadApplication.getInstance().OIL_TIME * 60) {
                        qiMaiSiOil();
                    }
                } else if (Integer.parseInt(temp[0]) >= TreadApplication.getInstance().OIL_TIME * 60 ||
                        Double.parseDouble(temp[1]) >= TreadApplication.getInstance().OIL_DISTANCE * 1000) {//提示加油
                    qiMaiSiOil();
                }
                if (TreadApplication.getInstance().getCustom().equals("yijikang")) {
                    if (Integer.parseInt(temp[2]) == TreadApplication.getInstance().OIL_COUNT) {
                        if (dialogCome == null) {
                            dialogCome = new DialogIsComeOn();
                        }
                        dialogCome.creatDialog(MainActivity.this);
                    }
                }
            } catch (Exception e) {
                if (file.exists())
                    file.delete();
                createFile();
            }
        } else {//文件不存在
//            if (new File("/system/treadmill/save.txt").exists()) {//重置后
//                FileUtils.copyFile("/system/treadmill/save.txt", DATAPATH);
//            } else {
            try {
                file.createNewFile();
                FileUtils.writeText2File(DATAPATH, "0" + " " + "0" + " " + "0");
            } catch (IOException e) {
                e.printStackTrace();
            }
//            }
        }

    }

    private void qiMaiSiOil() {
        if (TreadApplication.getInstance().getCustom().equals("qimaisi")) {
            if (dialogRefuel == null) {
                dialogRefuel = new DialogRefuel();
            }
            dialogRefuel.creatDialog(MainActivity.this);
        }
    }

    //    Runnable runnable = new Runnable() {
//        @Override
//        public void run() {
//            handler.sendEmptyMessage(0);
//            handler.postDelayed(this, 1000);
//        }
//    };
//    Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            window_tv_time.setText(NowDateTime.getInstance().getTimes());
////                window_tv_date.setText(NowDateTime.getInstance().getDate());
//            currentWifiInfo = wifiManager.getConnectionInfo();
//            setSigns(Math.abs(currentWifiInfo.getRssi()));
//            if (TreadApplication.getInstance().getSport() != TreadApplication.getInstance().PROGRAM_SETUP_STATUS) {
//                TreadApplication.getInstance().setPreIndex(0);
//            }
//        }
//    };
    private int isWifi = 0;
    public boolean isB = true;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            handler.sendEmptyMessage(0);
            if ( isWifi <= 14) {
//                if (!isB && currentWifiInfo != null && Math.abs(currentWifiInfo.getRssi()) <= 100) {
                isWifi++;
                isB = false;
                LogUtils.e("APP:"+isWifi);
                if (isWifi == 8) {
                    getSystemString();
                } else if (isWifi == 10) {
                    getString();
                } else if (isWifi == 12) {
                    getDoorStatus();
                }
//                }
            }
            handler.postDelayed(this, 1000);
        }
    };
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            window_tv_time.setText(NowDateTime.getInstance().getTimes());
//                window_tv_date.setText(NowDateTime.getInstance().getDate());
            currentWifiInfo = wifiManager.getConnectionInfo();
            setSigns(Math.abs(currentWifiInfo.getRssi()));
            if (TreadApplication.getInstance().getSport() != TreadApplication.getInstance().PROGRAM_SETUP_STATUS) {
                TreadApplication.getInstance().setPreIndex(0);
            }
        }
    };
    private String TAG = "TAG";

    public void getSystemString() {
        HashMap<String, String> map = new HashMap<>();
        try {
            if (PublicUtls.getProperty("custom.system.type", TAG).equals(TAG) || PublicUtls.getProperty("custom.system.version", TAG).equals(TAG))
                return;
            map.put("type", PublicUtls.getProperty("custom.system.type", TAG));
            map.put("version", PublicUtls.getProperty("custom.system.version", TAG));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Okhttp.get(false, UrlConstant.IS_SYSTEM_NEED_UPDATA, map, new Okhttp.CallBac() {
            @Override
            public void onError(Call call, Exception e, String state, int id) {
//                isB = true;
            }

            @Override
            public void onResponse(String response, int id) {
//                isB = true;
                parseSystemJSON(response);
            }

            @Override
            public void onNoNetwork(String state) {
                Log.e("state:" + state, " 没有连接网络");
            }
        });
    }

    public String downSystemUrl;
    String version_system_content;//系统更新内容

    private void parseSystemJSON(String response) {
        try {
            JSONObject respose_json = new JSONObject(response);
            if (respose_json.optBoolean("success") && !respose_json.optString("data").equals("null")) {
                JSONObject data_json = respose_json.optJSONObject("data");
                downSystemUrl = data_json.optString("version_down_url");
                version_system_content = data_json.optString("version_content");
            } else {
//                Logutil.e(respose_json.optString("message"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//    private void parseSystemJSON(String response) {
//        try {
//            JSONObject response_json = new JSONObject(response);
//            Logutil.e("initView",response);
//            if (response_json.optBoolean("success")) {
//                downSystemUrl = null;
//                JSONObject data_json = response_json.optJSONObject("data");
//                int version_no1 = data_json.optInt("version_no1");
//                int version_no2 = data_json.optInt("version_no2");
//                int version_no3 = data_json.optInt("version_no3");
//                String version_down_url = data_json.getString("version_down_url");
//                String version_name = PublicUtls.getProperty("ro.build.date.utc", "0");
//                String[] temp = version_name.split("\\.");
//                int nowVersion01 = Integer.parseInt(temp[0]);
//                int nowVersion02 = Integer.parseInt(temp[1]);
//                int nowVersion03 = Integer.parseInt(temp[2]);
//                if (nowVersion01 < version_no1) {
//                    Logutil.e("系统升级");
////                    downLoad(version_no1 + "." + version_no1 + "." + version_no3, version_down_url);
//                    downSystemUrl = version_down_url;
//                } else if (nowVersion01 == version_no1) {
//                    if (nowVersion02 < version_no2) {
//                        Logutil.e("系统升级");
//                        downSystemUrl = version_down_url;
//                    } else if (nowVersion02 == version_no2) {
//                        if (nowVersion03 < version_no3) {
//                            Logutil.e("系统升级");
//                            downSystemUrl = version_down_url;
//                        }
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public void getDoorStatus() {
        HashMap<String, String> map = new HashMap<>();
        map.put("mac", getMacAddress());
        map.put("tag", TreadApplication.getInstance().getCustom());
        Okhttp.get(false, UrlConstant.BACK_DOOR_URL, map, new Okhttp.CallBac() {
            @Override
            public void onError(Call call, Exception e, String state, int id) {
                Log.e("id:" + id, " 服务器错误");
//                isB = true;
            }

            @Override
            public void onResponse(String response, int id) {
                Log.e("id:" + id, " response:" + response);
//                isB = true;
                josnDoor(response);
            }

            @Override
            public void onNoNetwork(String state) {
                Log.e("state:" + state, " 没有连接网络");
//                if (nobor >= 10) {
//                    isB = true;
//                } else {
//                    nobor++;
//                }
            }
        });
    }

//    public int nobor;

    private void josnDoor(String response) {
        try {
            JSONObject response_json = new JSONObject(response);
            if (response_json.optBoolean("success")) {
                JSONObject data_json = response_json.optJSONObject("data");
                int machine_status = data_json.optInt("machine_status");
                TreadApplication.setDoorStatus(machine_status);
                LogUtils.e("machine_status:" + machine_status);
                if (machine_status == 0) {
                    DialogUserAble userAble = new DialogUserAble(MainActivity.this);
                    userAble.creatDialog();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String downloadUrl;
    String version_content;//更新内容

    public void getString() {
        HashMap<String, String> map = new HashMap<>();
        map.put("type", TreadApplication.getInstance().APP_TYPE);
        map.put("version", PublicUtls.getVerName(this));
        Log.e("APP:"+TreadApplication.getInstance().APP_TYPE,PublicUtls.getVerName(this));
        Okhttp.get(false, UrlConstant.IS_NEED_UPDATA_URL, map, new Okhttp.CallBac() {
            @Override
            public void onError(Call call, Exception e, String state, int id) {
//                isB = true;
                Log.e("APP:getString:onError");
            }

            @Override
            public void onResponse(String response, int id) {
//                isB = true;
                parseJSON(response);
            }

            @Override
            public void onNoNetwork(String state) {
                Log.e("APP:state:" + state, " 没有连接网络");
            }
        });
    }

    private void parseJSON(String response) {
        try {
            Log.e("APP:"+response);
            JSONObject respose_json = new JSONObject(response);
//            boolean success = respose_json.optBoolean("success");
            if (respose_json.optBoolean("success") && !respose_json.optString("data").equals("null")) {
                JSONObject data_json = respose_json.optJSONObject("data");
                downloadUrl = data_json.optString("version_down_url");
                version_content = data_json.optString("version_content");
            } else {
                LogUtils.e(respose_json.optString("message"));
            }
//            JSONObject response_json = new JSONObject(response);
//            if (response_json.optBoolean("success")) {
//                downloadUrl = null;
//                JSONObject data_json = response_json.optJSONObject("data");
//                int version_no1 = data_json.optInt("version_no1");
//                int version_no2 = data_json.optInt("version_no2");
//                int version_no3 = data_json.optInt("version_no3");
//                String version_down_url = data_json.getString("version_down_url");
//                String version_name = PublicUtls.getVerName(this);
//                String[] temp = version_name.split("\\.");
//                int nowVersion01 = Integer.parseInt(temp[0]);
//                int nowVersion02 = Integer.parseInt(temp[1]);
//                int nowVersion03 = Integer.parseInt(temp[2]);
//                if (nowVersion01 < version_no1) {
//                    Logutil.e("app升级");
////                    downLoad(version_no1 + "." + version_no1 + "." + version_no3, version_down_url);
//                    downloadUrl = version_down_url;
//                } else if (nowVersion01 == version_no1) {
//                    if (nowVersion02 < version_no2) {
//                        Logutil.e("app升级");
//                        downloadUrl = version_down_url;
//                    } else if (nowVersion02 == version_no2) {
//                        if (nowVersion03 < version_no3) {
//                            Logutil.e("app升级");
//                            downloadUrl = version_down_url;
//                        }
//                    }
//                }
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void downSystemLoad() {
//        nobor = number;
        dialogs = new AlertDialog.Builder(MainActivity.this).create();
        dialogs.setCancelable(false);
        dialogs.show();
        dialogs.getWindow().setContentView(R.layout.update);
        update_pb = (ProgressBar) dialogs.findViewById(R.id.update_pb);
        replaced = (Button) dialogs.findViewById(R.id.replaced);
        next = (Button) dialogs.findViewById(R.id.next);
        update_tv = (TextView) dialogs.findViewById(R.id.update_tv);
        update_tv.setText("系统更新");
        close = (Button) dialogs.findViewById(R.id.close);
        close.setOnClickListener(this);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogs.isShowing())
                    dialogs.dismiss();
                if (!TextUtils.isEmpty(downloadUrl)) {
                    downLoad();
                }
            }
        });
        replaced.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaced.setVisibility(View.GONE);
                next.setVisibility(View.GONE);
                update_tv.setText("升级完成后，将自动重启");
                update_pb.setVisibility(View.VISIBLE);
                close.setVisibility(View.VISIBLE);
                Okhttp.downloadFile(downSystemUrl, "update.zip", new Okhttp.FileCallBac() {
                    @Override
                    public void onNoNetwork(String state) {
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(final File response, int id) {
                        if (dialogs != null && dialogs.isShowing()) {
                            UpdataSystemUtil.updataSystem(MainActivity.this, response);
                        }
                    }

                    @Override
                    public void inProgress(float progress, long total, int id) {
                        update_pb.setProgress((int) (progress * 100));
                    }
                });
            }
        });
    }

    ProgressBar update_pb;
    Button replaced, next, close;
    public AlertDialog dialogs;
    private TextView update_tv;

    public void downLoad() {
//        nobor = number;
        dialogs = new AlertDialog.Builder(MainActivity.this).create();
        dialogs.setCancelable(false);
        dialogs.show();
        dialogs.getWindow().setContentView(R.layout.update);
        update_pb = (ProgressBar) dialogs.findViewById(R.id.update_pb);
        replaced = (Button) dialogs.findViewById(R.id.replaced);
        next = (Button) dialogs.findViewById(R.id.next);
        update_tv = (TextView) dialogs.findViewById(R.id.update_tv);
        update_tv.setText("软件更新");
        close = (Button) dialogs.findViewById(R.id.close);
        close.setOnClickListener(this);
        next.setOnClickListener(this);
        replaced.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaced.setVisibility(View.GONE);
                next.setVisibility(View.GONE);
                update_tv.setText("升级完成后，将自动重启");
                update_pb.setVisibility(View.VISIBLE);
                close.setVisibility(View.VISIBLE);
                Okhttp.downloadFile(downloadUrl, "SmartRun.apk", new Okhttp.FileCallBac() {
                    @Override
                    public void onNoNetwork(String state) {
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(final File response, int id) {
                        if (dialogs != null && dialogs.isShowing()) {
                            sendBroadcast(new Intent("android.intent.action.rc.userinstall.enable"));
                            installApk(MainActivity.this, response);
                            dialogs.dismiss();
                            sendBroadcast(new Intent("android.intent.action.rc.userinstall.disable"));
//                            DataOutputStream os = null;
//                            Process process = null;
//                            try {
//                                process = Runtime.getRuntime().exec("su");
//                                os = new DataOutputStream(process.getOutputStream());
//                                os.writeBytes("mount -o remount /system" + "\n");
//                                os.writeBytes("cp " + response.getAbsolutePath() + " /system/priv-app/SmartRun.apk" + "\n");
//                                os.writeBytes("chmod 0644 /system/priv-app/SmartRun.apk" + "\n");
//                                os.writeBytes("rm " + response.getAbsolutePath() + "\n");
//                                os.writeBytes("reboot" + "\n");
//                                os.flush();
//                                process.waitFor();
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
                        }
                    }

                    @Override
                    public void inProgress(float progress, long total, int id) {
                        update_pb.setProgress((int) (progress * 100));
                    }
                });
            }
        });
    }

    /**
     * 安装apk
     */
    public static void installApk(Activity activity, File apkfile) {
        if (!apkfile.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= 24) {
            //判读版本是否在7.0以上
            Uri apkUri = FileProvider.getUriForFile(activity,
                    "com.vigorchip.treadmill.wr2",
                    apkfile);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(apkfile), "application/vnd.android.package-archive");
        }

        activity.startActivity(intent);
        activity.finish();
    }

//    public int number = 10;

    public static String getMacAddress() {
 /*获取mac地址有一点需要注意的就是android 6.0版本后，以下注释方法不再适用，不管任何手机都会返回"02:00:00:00:00:00"这个默认的mac地址，这是googel官方为了加强权限管理而禁用了getSYstemService(Context.WIFI_SERVICE)方法来获得mac地址。*/
        //        String macAddress= "";
//        WifiManager wifiManager = (WifiManager) MyApp.getContext().getSystemService(Context.WIFI_SERVICE);
//        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//        macAddress = wifiInfo.getMacAddress();
//        return macAddress;

        String macAddress = null;
        StringBuffer buf = new StringBuffer();
        NetworkInterface networkInterface = null;
        try {
            networkInterface = NetworkInterface.getByName("eth1");
            if (networkInterface == null) {
                networkInterface = NetworkInterface.getByName("wlan0");
            }
            if (networkInterface == null) {
                return "02:00:00:00:00:02";
            }
            byte[] addr = networkInterface.getHardwareAddress();
            for (byte b : addr) {
                buf.append(String.format("%02X:", b));
            }
            if (buf.length() > 0) {
                buf.deleteCharAt(buf.length() - 1);
            }
            macAddress = buf.toString();
        } catch (SocketException e) {
            e.printStackTrace();
            return "02:00:00:00:00:02";
        }
        return macAddress;
    }

    public static void setTitles(String context) {
        window_tv_date.setText(context);
    }

    /**
     * 引导页Dialog
     */
    public void getDialogs() {
        Log.e("TAG", "=================getDialogs=================");
        if (TreadApplication.getInstance().getFirst()) {
            DialogUserIcon dialogUserIcon = new DialogUserIcon(this, home_iv_userIcon);
            dialogUserIcon.createDialog();
        } else if (home_iv_userIcon.getDrawable() == null) {

        }
    }

    /**
     * 设置fragment页数
     *
     * @param index
     */
    public void setCurrentIndex(int index) {
        Log.e("TAG", "=================setCurrentIndex=================");
        currentIndex = index;
        showFragment();
    }

    private void setDefaultFragment(Bundle savedInstanceState) {
        Log.e("TAG", "=================setDefaultFragment=================");
        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState != null) { // “内存重启”时调用
            currentIndex = savedInstanceState.getInt(CURRENT_FRAGMENT, 0); //获取“内存重启”时保存的索引下标
            fragments.removeAll(fragments);//注意，添加顺序要跟下面添加的顺序一样！！！！
//            fragments.add(fragmentManager.findFragmentByTag(0 + ""));
//            fragments.add(fragmentManager.findFragmentByTag(1 + ""));
//            fragments.add(fragmentManager.findFragmentByTag(2 + ""));
//            fragments.add(fragmentManager.findFragmentByTag(3 + ""));
//            fragments.add(fragmentManager.findFragmentByTag(4 + ""));
//            fragments.add(fragmentManager.findFragmentByTag(5 + ""));
//            fragments.add(fragmentManager.findFragmentByTag(6 + ""));
//            fragments.add(fragmentManager.findFragmentByTag(7 + ""));
//            fragments.add(fragmentManager.findFragmentByTag(8 + ""));
//            fragments.add(fragmentManager.findFragmentByTag(9 + ""));
//            fragments.add(fragmentManager.findFragmentByTag(10 + ""));
            fragments.add(new MainFragment());//0 首页
            fragments.add(new RunFragment());//1 运动模式选择
            fragments.add(new MediaFragment());//2 多媒体
            fragments.add(new SceneFragment());//3 实景
            fragments.add(new AppsFragment());//4 应用
            fragments.add(new UserFragment());//5 用户
            fragments.add(new SettingFragment());//6 设置
            fragments.add(new OperatorGuideFragment());//7 操作指南
            fragments.add(new ShareFragment());//8 跑步结束
            fragments.add(new VideoFragment());//9 实景播放的fragment
            showFragment();//恢复fragment页面
        } else {//正常启动时调用
            fragments.add(new MainFragment());
            fragments.add(new RunFragment());
            fragments.add(new MediaFragment());
            fragments.add(new SceneFragment());
            fragments.add(new AppsFragment());
            fragments.add(new UserFragment());
            fragments.add(new SettingFragment());
            fragments.add(new OperatorGuideFragment());
            fragments.add(new ShareFragment());
            fragments.add(new VideoFragment());
        }
        if (TreadApplication.getInstance().getFirst() && !RunningModelFragment.isNext) {//判断那种情况进去
            setCurrentIndex(1);
        } else {
            setTextView();
        }
        SerialComm.setOnClickMain(new OnClickMain() {
            @Override
            public void onMain(int mod) {
                setCurrentIndex(1);
            }
        });
        WindowInfoService.setOnClickStart(new OnClickStart() {
            @Override
            public void onStarts() {
                Log.e("MainActivity", "TreadApplication.getInstance().getSport():" + TreadApplication.getInstance().getSport());
                if (TreadApplication.getInstance().getSport() == TreadApplication.getInstance().END_STATUS) {
                    TreadApplication.getInstance().setSport(TreadApplication.getInstance().NORMAL_STATUS);
                    setCurrentIndex(8);
                } else if (TreadApplication.getInstance().getSport() == TreadApplication.getInstance().FIVE_SECOND_STATUS) {
                    if (TreadApplication.getInstance().getSport() == TreadApplication.getInstance().FIVE_SECOND_STATUS ||
                            TreadApplication.getInstance().isRuning())
                        setCurrentIndex(1);
                } else {
                    Log.e("TAG", "=====================1========+setCurrentIndex(0)");
                    setCurrentIndex(0);
                }
            }
        });
    }

    /**
     * 使用show() hide()切换页面 显示fragment
     */
    private void showFragment() {
        Log.e("TAG", "=================showFragment=================");
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (!fragments.get(currentIndex).isAdded())  //如果之前没有添加过
            transaction.detach(currentFragment).add(R.id.activity_main_frameLayout, fragments.get(currentIndex), "" + currentIndex);  //第三个参数为添加当前的frgment时绑定一个tag
        else
            transaction.detach(currentFragment);
        currentFragment = fragments.get(currentIndex);
        transaction.attach(currentFragment);
        transaction.commitAllowingStateLoss();
    }

//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        Log.e("TAG", "=================onRestart=================");
//    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        Log.e("TAG", "=================onStart=================");
//    }

    /**
     * 恢复fragment
     */
    private void restoreFragment() {
        Log.e("TAG", "=================restoreFragment=================");
        FragmentTransaction mBeginTreansaction = fragmentManager.beginTransaction();
        for (int i = 0; i < fragments.size(); i++) {
            if (i == currentIndex)
                mBeginTreansaction.attach(fragments.get(i));
            else
                mBeginTreansaction.detach(fragments.get(i));
        }
        mBeginTreansaction.commitAllowingStateLoss();
        currentFragment = fragments.get(currentIndex); //把当前显示的fragment记录下来
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        setDefaultFragment(savedInstanceState);
        setUserIndex(savedInstanceState.getInt(USER_INDEX));
        home_iv_userIcon.setImageResource(userIcon[getUserIndex()]);
        Log.e("TAG", "=================onRestoreInstanceState=================");
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.e("TAG", "=================onSaveInstanceState=================");
        outState.putInt(CURRENT_FRAGMENT, currentIndex);  //“内存重启”时保存当前的fragment名字
        outState.putInt(USER_INDEX, userIndex);
//        TreadApplication.getInstance().setSport(TreadApplication.getInstance().getSport());
//        super.onSaveInstanceState(outState);
    }

    /**
     * 退出键
     */
    @Override
    public void onBackPressed() {
        Log.e("TAG", "=================onBackPressed=================");
        if (TreadApplication.getInstance().getSport() == TreadApplication.getInstance().TIME_SETUP_STATUS ||
                TreadApplication.getInstance().getSport() == TreadApplication.getInstance().DISTANCE_SETUP_STATUS ||
                TreadApplication.getInstance().getSport() == TreadApplication.getInstance().CALORIE_SETUP_STATUS ||
                TreadApplication.getInstance().getSport() == TreadApplication.getInstance().PROGRAM_SETUP_STATUS ||
                TreadApplication.getInstance().getSport() == TreadApplication.getInstance().USER_SETUP_STATUS ||
                TreadApplication.getInstance().getSport() == TreadApplication.getInstance().HRC_SETUP_STATUS) {
            TreadApplication.getInstance().setSport(TreadApplication.getInstance().NORMAL_STATUS);
        }
        RunFragment.setIsMil(false);
        RunFragment.setIsTime(false);
        if (isRunModel && currentIndex == 1) {
            setCurrentIndex(1);
        } else if (currentIndex == 7) {
            setCurrentIndex(6);
        } else if (currentIndex == 9) {
            setCurrentIndex(3);
        } else if (currentIndex != 0) {
            setCurrentIndex(0);
        }
    }

    public static void setVideo(boolean video) {
        isVideo = video;
    }

    public static int getUserIndex() {
        return userIndex;
    }

    public void setUserIndex(int userIndexs) {
        userIndex = userIndexs;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_iv_userIcon:
                if (currentIndex != 5)
                    setCurrentIndex(5);
                break;
            case R.id.next:
            case R.id.close:
                if (dialogs!=null&&dialogs.isShowing())
                    dialogs.dismiss();
                break;
        }
    }

    private static HomeWatcherReceiver mHomeKeyReceiver = null;

    private void registerHomeKeyReceiver(Context context) throws Exception {
        mHomeKeyReceiver = new HomeWatcherReceiver();
        final IntentFilter homeFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        context.registerReceiver(mHomeKeyReceiver, homeFilter);
    }

    private static void unregisterHomeKeyReceiver(Context context) throws Exception {
        if (null != mHomeKeyReceiver) {
            context.unregisterReceiver(mHomeKeyReceiver);
        }
    }

    public void setIsRunModel(boolean isRunModes) {
        isRunModel = isRunModes;
    }

    private class HomeWatcherReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (DialogAdjustVelocityGradient.isShowings())
                DialogAdjustVelocityGradient.dimss();
            if (intent.getAction().equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                setCurrentIndex(0);
                if (TreadApplication.getInstance().getSport() == TreadApplication.getInstance().TIME_SETUP_STATUS ||
                        TreadApplication.getInstance().getSport() == TreadApplication.getInstance().DISTANCE_SETUP_STATUS ||
                        TreadApplication.getInstance().getSport() == TreadApplication.getInstance().CALORIE_SETUP_STATUS ||
                        TreadApplication.getInstance().getSport() == TreadApplication.getInstance().PROGRAM_SETUP_STATUS ||
                        TreadApplication.getInstance().getSport() == TreadApplication.getInstance().USER_SETUP_STATUS ||
                        TreadApplication.getInstance().getSport() == TreadApplication.getInstance().HRC_SETUP_STATUS) {
                    TreadApplication.getInstance().setSport(TreadApplication.getInstance().NORMAL_STATUS);
                    RunFragment.setIsMil(false);
                    RunFragment.setIsTime(false);
                }
            }
        }
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        switch (keyCode) {
//            case KeyEvent.KEYCODE_VOLUME_DOWN:
//                Log.e("event", "KeyEvent.KEYCODE_VOLUME_DOWN");
//                return true;
//            case KeyEvent.KEYCODE_VOLUME_UP:
//                Log.e("event", "KeyEvent.KEYCODE_VOLUME_UP");
//                return true;
//            case KeyEvent.KEYCODE_VOLUME_MUTE:
//                Log.e("event", "KeyEvent.KEYCODE_VOLUME_MUTE");
//                return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    /**
     * 导航栏，状态栏隐藏
     *
     * @param activity
     */
    public static void NavigationBarStatusBar(Activity activity, boolean hasFocus) {
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            View decorView = activity.getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    /**
     * 导航栏，状态栏透明
     *
     * @param activity
     */
    public static void setNavigationBarStatusBarTranslucent(Activity activity) {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = activity.getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            activity.getWindow().setNavigationBarColor(Color.TRANSPARENT);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        ActionBar actionBar = activity.getActionBar();
        actionBar.hide();
    }
}
