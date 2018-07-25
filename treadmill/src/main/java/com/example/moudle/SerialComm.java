package com.example.moudle;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.activity.MainActivity;
import com.example.app.TreadApplication;
import com.example.dialog.DialogAutomatic;
import com.example.service.WindowInfoService;
import com.example.utils.FileUtils;
import com.example.utils.MyTime;
import com.vigorchip.treadmill.wr2.R;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android_serialport_api.SerialPort;

import static com.example.moudle.BluDataUtil.stringToAscii;

public class SerialComm
//        extends Service
{
    public static final int TIMES = 60;
    /**
     * 串口连接
     */
    private SerialPort mSerialPort;
    private InputStream mInputStream;
    private OutputStream mOutputStream;
//    private SerialPort mBuleSerialPort;
//    private InputStream mBuleInputStream;
//    private OutputStream mBuleOutputStream;
    private static WindowInfoService windowInfoService;
    private BluDataUtil bluDataUtil;
    private static final int SEND_DATA_SPEED_SLOPES = 01;//发送速度、坡度值命令
    private static final int SEND_DATA_TYPE = 02;//发送 TFT 当前状态值
    private static final int SEND_DATA_REFUEl = 03;//发送 加油
    private static int BEEP = 0;//不要蜂鸣
    private static int FAP = 0;//风扇关
    private int Metric = 0;
    private static boolean isSport = false;
    private MainActivity mainActivity;
    private AudioManager audioManager;  // 系统声音管理类
    private static OnClickMain onClickMain;
    private static OnStartRun onStartRun;
    private static int currErr;
    private Context context;

//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//    }

    public SerialComm(Context mContext) {
        isFrist = false;
        audioManager = (AudioManager) TreadApplication.getInstance().getApplicationContext().getSystemService(Context.AUDIO_SERVICE); // 实例化系统声音管理类
        mainActivity = new MainActivity();
        context = mContext;
        initSerial();
    }


    private void initSerial() {
        try {
            currErr = 0;
            isFrist = false;
            windowInfoService=new WindowInfoService();
            bluDataUtil = new BluDataUtil();
            mSerialPort = new SerialPort(new File(Constants.SERIAL_PORT_DEVICE), Constants.SERIAL_PORT_BAUD);
            mInputStream = mSerialPort.getInputStream();
            mOutputStream = mSerialPort.getOutputStream();
//            mBuleSerialPort = new SerialPort(new File(Constants.SERIAL_PORT_DEVICE_BULE), Constants.SERIAL_PORT_BAUD_BULE);
//            mBuleInputStream = mBuleSerialPort.getInputStream();
//            mBuleOutputStream = mBuleSerialPort.getOutputStream();
            receiveRun.run();//接收线程
            sendRun.run();//发送线程
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Handler serialPostHandler = new Handler();

    private void write() {
        try {
            if (mOutputStream != null) {
                mOutputStream.write(initPostData());
                mOutputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte[] initPostData() {
        byte[] datas = new byte[5];
        datas[0] = Constants.MESSAGE_POST_HEAD;
        if (isSport) {
            datas[1] = (byte) ((SEND_DATA_TYPE << 6 | getFAP()) & 0xFF);
            datas[2] = (byte) (((getBEEP() << 7) | TreadApplication.getInstance().getSport()) & 0xFF);
            if (getBEEP() == 1)
                setBEEP(0);
        } else {
            double speed = windowInfoService.getmSpeed();
            int slopes = windowInfoService.getmSlopes();
//        int Metric = SettingFragment.getM();
            datas[1] = (byte) (((SEND_DATA_SPEED_SLOPES << 6) | (Metric << 5) | slopes) & 0xFF);
            datas[2] = (byte) ((int) (speed * 10) & 0xFF);
        }
        isSport = !isSport;
        datas[3] = (byte) ((datas[1] + datas[2]) & 0xFF);
        datas[4] = Constants.MESSAGE_POST_END;
        return datas;
    }

    public static void setIsrefuel(boolean isrefuels) {
        isrefuel = isrefuels;
    }

    static boolean isrefuel = false;
    int orderValue = 0;
    Toast toast;
    View customView;

    public void showToast(String str) {
        if (toast != null)
            toast.cancel();
        customView = View.inflate(context, R.layout.toast_layout, null);
        toast = new Toast(context);
        ((TextView) customView.findViewById(R.id.toast)).setText(str);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(customView);
        toast.show();
    }

    Runnable sendRun = new Runnable() {
        @Override
        public void run() {
            synchronized (this) {
                send();//下控
//                buleSend();//蓝牙的
            }
            serialPostHandler.postDelayed(this, TIMES);
        }
    };

//    private void buleSend() {
//        try {
//            if (mBuleOutputStream != null) {
//                mBuleOutputStream.write(initPostString());
//                mBuleOutputStream.flush();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    private byte[] initPostString() {
        StringBuffer buffer = new StringBuffer(",");
        buffer.append("c" + (TreadApplication.getInstance().getSport() == TreadApplication.getInstance().ERROR_STATUS ? 1000 : 0) + ",");
        buffer.append("sf" + (TreadApplication.getInstance().SLOPES == 0 ? 1 : 0) + ",");
        buffer.append("st" + (TreadApplication.getInstance().isRuning() ? 20 :
                TreadApplication.getInstance().FIVE_SECOND_STATUS == TreadApplication.getInstance().getSport() ? 50 :
                        TreadApplication.getInstance().NOTSAFE_STATUS == TreadApplication.getInstance().getSport() ? 60 : 10) + ",");
        buffer.append("mp" + ((int) TreadApplication.getInstance().MAXSPEED) + ",");
        buffer.append("ms" + (TreadApplication.getInstance().SLOPES) + ",");
        buffer.append("mi" + ((int) TreadApplication.getInstance().MINSPEED) + ",");
        buffer.append("ca" + windowInfoService.getHeat() + ",");
        buffer.append("dis" + windowInfoService.getDistance() + ",");
        buffer.append("h" + windowInfoService.getHearts() + ",");
        buffer.append("s" + windowInfoService.getmSlopes() + ",");
        buffer.append("p" + windowInfoService.getmSpeed() + ",");
        buffer.append("t" + MyTime.positiveTime() + ",");
        int num = stringToAscii(buffer.toString());
        //十进制转换为十六进制
        String hexstr = bluDataUtil.toHexStr(num);
        //截取后两位
        String str3 = hexstr.substring(hexstr.length() - 2, hexstr.length());
        //十六进制转换为十进制
        int num2 = bluDataUtil.toParseInt(str3);
        //将十进制整形转换为字符串
        String str4 = String.valueOf(num2);
        //如果不足三位前边加0
        if (str4.length() == 2)
            str4 = "0" + str4;
        if (str4.length() == 1)
            str4 = "00" + str4;
        buffer.append(str4 + ",");
        buffer.append("FF");
        buffer.insert(0, "EE");
        return buffer.toString().getBytes();
    }

    private void send() {
        if (Thread.currentThread().getPriority() != 10)
            Thread.currentThread().setPriority(10);
        if (isrefuel) {
            isrefuel = false;
            if (DialogAutomatic.showing()) {
                showToast("正在加油，请稍后");
            } else {
                if (Integer.parseInt(FileUtils.readTextFromFile(MainActivity.getDATAPATH()).split(" ")[2]) < TreadApplication.getInstance().OIL_COUNT)
                    refuel();//加油
                ilos = 0;
            }
        } else {
            write();
            if (ilos == 50) {
                automatic.dimss();
                ilos = 0;
            } else
                ilos++;
        }
    }

    private int ilos;
    private static final int REFUEL_TYPE = 1;
    DialogAutomatic automatic;

    private void refuel() {
        try {
            byte[] datas = new byte[5];
            datas[0] = Constants.MESSAGE_POST_HEAD;
            int data1 = (orderValue >> 8) & 0x07;
            datas[1] = (byte) (SEND_DATA_REFUEl << 6 | REFUEL_TYPE << 3 | data1);
            datas[2] = (byte) (orderValue & 0xff);
            datas[3] = (byte) ((datas[1] + datas[2]) & 0xFF);
            datas[4] = Constants.MESSAGE_POST_END;
            if (mOutputStream != null) {
                mOutputStream.write(datas);
                mOutputStream.flush();
            }
            if (automatic == null)
                automatic = new DialogAutomatic();
            if (!automatic.showing()) {
                String[] temp = FileUtils.readTextFromFile(MainActivity.getDATAPATH()).split(" ");
                int fueqel = Integer.parseInt(temp[2]);
                FileUtils.writeText2File(MainActivity.getDATAPATH(), "0" + " " + "0" + " " + (fueqel + 1));
                MyTime.recervo();
                automatic.creatDialogauto(context, fueqel);
            }
        } catch (IOException e) {
            Log.e("TOGO", "打开失败");
//            e.printStackTrace();
        }
    }

    boolean isErr;

    private byte[] read() {
        try {
            if (mInputStream != null) {
                int len = mInputStream.available();//Log.e("SerialCommBELL", "len--------------------------" + len);
                errortran(len);//通讯错误
                byte[] buffer = new byte[len];
                if (len >= 5) {
                    if ((len = mInputStream.read(buffer)) != -1) {
                        if (len >= 5) {
                            return buffer;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通讯错误调用的方法
     *
     * @param len
     */
    private void errortran(int len) {
        if (len == 0) {
            if (index == Integer.MAX_VALUE) {
                index = 50;//Log.e("SerialCommBELL", "正在变值--------------------------");
            }//Log.e("SerialCommBELL", "正在累加--------------------------");
            index++;
        } else {//Log.e("SerialCommBELL", "正在清零--------------------------");
            index = 0;
            if (isErr) {//Log.e("SerialCommBELL", "正在消除--------------------------");
                windowInfoService.dimss();
                isErr = false;
            }
        }
        if (index >= 50 && !isErr) {//Log.e("SerialCommBELL", "出来了--------------------------");
            TreadApplication.getInstance().setSport(TreadApplication.getInstance().ERROR_STATUS);
            windowInfoService.creatDialog(17);
            TreadApplication.setMediaPlayer(R.raw.commcardfault);
            isErr = true;
        }
    }

    //    起始速度转速值 0x4 0x1 数值高 8 位 数值0x2 数值低 8 位 8Km 转速值 0x5 0x1 数值高 8 位 数值0x2 数值低 8 位 最高速度转速值0x6 0x1 数值高 8 位 数值0x2 数值低 8 位
//    扭力值 0x7 0x1 数值高 8 位 数值0x2 数值低 8 位
//    速度闭环值 0x8 0x1 数值高 8 位 数值0x2 数值低 8 位
//    最低速度值 0x9 0x1 数值高 8 位 数值0x2 数值低 8 位
//    最高速度值 0xa 0x1 数值高 8 位 数值0x2 数值低 8 位
//    最大扬升值 0xb 0x1 数值高 8 位 数值0x2 数值低 8 位
//    扬升学习 0xc 0x1 学习开始
//    扬升 AD 值 0x2 学习完成
//    注意：0x4~0xa 为工程模式所需要的数据，16 位数据分两次发送，接收到 DATA1 低 4 位为 0x1 的数据做
//    为 16 位数据的高 8 位，接收到 DATA1 低 4 位为 0x2 的数据做为 16 位数据的低 8 位。0xc 为扬升学习命令，DATA1 低 4 位为 0x1 时，代表学习开始，DATA1 低 4 位为 0x2 时，代表学习完成，DATA2 为扬升学习过程中的 AD 值显示。
    static int ZT;
    private static int index;
    Runnable receiveRun = new Runnable() {
        @Override
        public void run() {
            synchronized (this) {
                receive();//下控
//                buleReceive();//蓝牙的
            }
            serialPostHandler.postDelayed(this, TIMES); //循环调用
        }
    };

    Circular circular;

//    private void buleReceive() {
//        try {
//            if (mBuleInputStream != null) {
//                byte[] bytes = new byte[mBuleInputStream.available()];
//                if (mBuleInputStream.read(bytes) != -1) {
//                    if (circular == null)
//                        circular = new Circular();
//                    for (int i = 0; i < bytes.length; i++) {
//                        circular.put(bytes[i]);
//                    }
//                    byte[] byte1 = circular.get();
//                    if (byte1 == null)
//                        return;
//                    String content = new String(byte1, 0, byte1.length);
//                    if (bluDataUtil.SplitStr(content))
//                        BluetoothOperation(content);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    private void BluetoothOperation(String str) {
        String[] str1 = str.split(",");
        for (int i = 0; i < str1.length; i++) {
            Log.e("content", str1[i]);
        }
        if (str1[1].equals("A1")) buttonCommand(71, 2);
        else if (str1[1].equals("A0")) buttonCommand(72, 2);
        else if (str1[1].substring(0, 1).equals("s"))
            setSlopes(Integer.parseInt(str1[1].substring(1, str1[1].length())));
        else if (str1[1].substring(0, 1).equals("p"))
            setSpeed(Double.parseDouble(str1[1].substring(1, str1[1].length())));
    }

    private void receive() {
        if (Thread.currentThread().getPriority() != 10)//提高线程的优先级
            Thread.currentThread().setPriority(10);
        byte[] data = read();
//                long currentTime = System.currentTimeMillis();
//                System.out.println(currentTime + "");
//                SimpleDateFormat formatter = new SimpleDateFormat("yyyy年-MM月dd日-HH时mm分ss秒");
//                Date date = new Date(currentTime);
//                System.out.println(formatter.format(date));
//                Log.e("TAG",TextUtils.isEmpty(String.valueOf(data))+"");
//                Log.e("TAG",""+data.length);
        if (data != null && data.length >= 5 &&
                data[0] == Constants.MESSAGE_RECV_HEAD &&
                data[4] == Constants.MESSAGE_RECV_END &&
                data[3] == (byte) (data[1] + data[2])) {
            int recvType = data[1] >> 4;
            switch (recvType) {
                case Constants.BUTTON_COMMAND:
                    ZT = data[1] & 0xf;
                    buttonCommand(data[2], ZT);//按键值
                    break;
                case Constants.HEARTBEAT_VALUE:
                    windowInfoService.setHeart(data[2]);//心跳值
                    break;
                case Constants.ERROR_VALUE:
                    errorValue(data[2]);//错误提示值
                    break;
                default:
                    if (isengineering_model)
                        EngineeringModel(data[1], data[2]);
                    break;
            }
        }
    }

    int currType;
    int type;
    int num;
    int number1;//值
    int number2;
    boolean num1;//是否有值
    boolean num2;
    int valuesShow;
    int xuHao;

    /**
     * @param name    data[1]
     * @param values1 date[2]
     */
    private void EngineeringModel(int name, int values1) {
        type = (name >> 4) & 0xf;
        if (type == 0xc) {
            num = name & 0xf;
            if (num == 1
//                    ||num==2
                    ) {
                number.setText("09");
                values.setText((values1 & 0xff) + "");
            }
            currType = type;
        } else {
            if (type != currType) {//判断是否连续
                num1 = false;
                num2 = false;
            }
            num = name & 0xf;
            if (num == 1) {
                number1 = (values1 & 0xff);
                num1 = true;
                Log.e("TAG", "number1:" + number1);
            }
            if (num == 2) {
                number2 = (values1 & 0xff);
                num2 = true;
                Log.e("TAG", "number2:" + number2);
            }
            if (num1 && num2) {
                num1 = false;
                num2 = false;
                valuesShow = (number1 << 8) | number2;
                xuHao = type;
                Log.e("TAG", "valuesShow:" + valuesShow);
                Log.e("TAG", "xuHao:" + xuHao);
                handler.sendEmptyMessage(0);
            }
            currType = type;
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                number.setText("");
                values.setText("");
            } else if (msg.what == 0) {
                switch (xuHao) {
                    case Constants.INITIAL_SPEED:
                        number.setText("01");
                        break;
                    case Constants.KM_RPM8:
                        number.setText("02");
                        break;
                    case Constants.MAXIMUM_REV_VALUE:
                        number.setText("03");
                        break;
                    case Constants.TORQUE_VALUE:
                        number.setText("04");
                        break;
                    case Constants.SPEED_CLOSED_LOOP:
                        number.setText("05");
                        break;
                    case Constants.MINIMUM_SPEED_VALUE:
                        number.setText("06");
                        break;
                    case Constants.MAXIMUM_SPEED_VALUE:
                        number.setText("07");
                        break;
                    case Constants.MAXIMUM_UPLIFT:
                        number.setText("08");
                        break;
                    case 0xd:
                        number.setText("10");
                        break;
                    case 0xe:
                        number.setText("11");
                        break;
                    case 0xf:
                        number.setText("12");
                        break;
                }
                values.setText((valuesShow & 0xffff) + "");
            }
        }
    };

    private void errorValue(int errValue) {
        if (TreadApplication.getInstance().getSport() == TreadApplication.getInstance().NOTSAFE_STATUS) {
            if (errValue != 4)
                return;
        }
        switch (errValue) {
            case 0://正常
                if (TreadApplication.getInstance().getSport() != TreadApplication.getInstance().NOTSAFE_STATUS) {
                    windowInfoService.dimss();
                    currErr = 0;
                }
                break;
//            case 1:// 错误 1
//                break;
//            case 2://错误 2
//                break;
//            case 3://错误 3
//                break;
//            case 4://错误 4
//                break;
//            case 5://错误 5
//                break;
//            case 6://错误 6
//                break;
//            case 7://错误 7
//                break;
//            case 8://错误 8
//                break;
//            case 9://错误 9
//                break;
            default:
                if (errValue < 17) {
                    if (currErr == errValue)
                        break;
                    TreadApplication.getInstance().setSport(TreadApplication.getInstance().ERROR_STATUS);
                    currErr = errValue;
                    if (errValue==5){
                        TreadApplication.setMediaPlayer(R.raw.overcurrent);
                    }
                    windowInfoService.creatDialog(errValue);
                }
                break;
        }
    }

    Intent intent;
    KeyEvent keyEvent;

    private void buttonCommand(int btnCmd, int ZT) {
        btnCmd = btnCmd & 0xff;
        if (TreadApplication.getInstance().getFirst()) {
            if (btnCmd != 73)
                if (btnCmd != 7)
                    if (btnCmd != 8)
                        if (btnCmd != 9)
                            if (btnCmd != 102)
                                if (btnCmd != 103)
                                    if (btnCmd != 104)
                                        return;
        }
        switch (btnCmd) {
            case 1://媒体播放的播放、暂停键
                mediaOpera(KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE);
                break;
            case 2://界面的返回或者状态的退出
                break;
            case 3://UP 向上键
                break;
            case 4://DOWN 向下键
                break;
            case 5://LEFT 向左键
                break;
            case 6://RIGHT 向右键
                break;
            case 7://音量 + 键 媒体音量增加
                upRaise();
//				audioManager.dispatchMediaKeyEvent(event);
//                if (audioManager.getRingerMode()!=AudioManager.RINGER_MODE_NORMAL)
//                    audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
//                else
//                    audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
                break;
            case 8://音量 - 键 媒体音量减小
                dowlower();
                break;
            case 9://静音键 媒体音量静音
                sound();
                break;
            case 10://播放快进 媒体播放快进
                break;
            case 11://播放快退 媒体播放快退
                break;
            case 12://TFT 视频播放全屏转换键 媒体播放切换全屏与非全屏模式
                break;
            case 13://升降 + 键 跑步机扬升增加
                setSlopes(windowInfoService.getmSlopes() + 1);
                break;
            case 14://升降 - 键 跑步机扬升减少
                setSlopes(windowInfoService.getmSlopes() - 1);
                break;
            case 15://速度 + 键 跑步机速度增加
                Log.e("setSpeed2","(windowInfoService.getmSpeed() + 0.1):"+(windowInfoService.getmSpeed() + 0.1)+"  windowInfoService.getmSpeed():"+windowInfoService.getmSpeed());
                setSpeed(windowInfoService.getmSpeed() + 0.1);
                break;
            case 16://速度 - 键 跑步机速度减少
                Log.e("Ignoring3","windowInfoService:"+windowInfoService+"  windowInfoService.getmSpeed():"+windowInfoService.getmSpeed());
                setSpeed(windowInfoService.getmSpeed() - 0.1);
                break;
            case 17://升降快捷键 0 快速设置扬升值为 0 %
                setSlopes(0);
                break;
            case 18://升降快捷键 1 快速设置扬升值为 1 %
                setSlopes(1);
                break;
            case 19://升降快捷键 2 快速设置扬升值为 2 %
                setSlopes(2);
                break;
            case 20://升降快捷键 3 快速设置扬升值为 3 %
                setSlopes(3);
                break;
            case 21://升降快捷键 4 快速设置扬升值为 4 %
                setSlopes(4);
                break;
            case 22:// 升降快捷键 5 快速设置扬升值为 5 %
                setSlopes(5);
                break;
            case 23://升降快捷键 6 快速设置扬升值为 6 %
                setSlopes(6);
                break;
            case 24://升降快捷键 7 快速设置扬升值为 7 %
                setSlopes(7);
                break;
            case 25://升降快捷键 8 快速设置扬升值为 8 %
                setSlopes(8);
                break;
            case 26://升降快捷键 9 快速设置扬升值为 9 %
                setSlopes(9);
                break;
            case 27://升降快捷键 10 快速设置扬升值为 10 %
                setSlopes(10);
                break;
            case 28://升降快捷键 11 快速设置扬升值为 11 %:
                setSlopes(11);
                break;
            case 29://升降快捷键 12 快速设置扬升值为 12 %
                setSlopes(12);
                break;
            case 30://升降快捷键 13 快速设置扬升值为 13 %:
                setSlopes(13);
                break;
            case 31://升降快捷键 14 快速设置扬升值为 14 %:
                setSlopes(14);
                break;
            case 32://升降快捷键 15 快速设置扬升值为 15 %:
                setSlopes(15);
                break;
            case 33://升降快捷键 16 快速设置扬升值为 16 %
                setSlopes(16);
                break;
            case 34://升降快捷键 17 快速设置扬升值为 17 %:
                setSlopes(17);
                break;
            case 35://升降快捷键 18 快速设置扬升值为 18 %:
                setSlopes(18);
                break;
            case 36://升降快捷键 19 快速设置扬升值为 19 %:
                setSlopes(19);
                break;
            case 37://升降快捷键 20 快速设置扬升值为 20 %:
                setSlopes(20);
                break;
            case 38://升降快捷键 21 快速设置扬升值为 21 %
                setSlopes(21);
                break;
            case 39://升降快捷键 22 快速设置扬升值为 22 %:
                setSlopes(22);
                break;
            case 40://升降快捷键 23 快速设置扬升值为 23 %:
                setSlopes(23);
                break;
            case 41://升降快捷键 24 快速设置扬升值为 24 %:
                setSlopes(24);
                break;
            case 42://升降快捷键 25 快速设置扬升值为 25 %:
                setSlopes(25);
                break;
            case 43://速度快捷键 0 仅增加备用，此按键目前程序中无作用
                setSpeed(0);
                break;
            case 44://速度快捷键 1 快速设置速度值为 1 km / h
                setSpeed(1);
                break;
            case 45://速度快捷键 2 快速设置速度值为 2 km / h
                setSpeed(2);
                break;
            case 46://速度快捷键 3 快速设置速度值为 3 km / h
                setSpeed(3);
                break;
            case 47://速度快捷键 4 快速设置速度值为 4 km / h
                setSpeed(4);
                break;
            case 48://速度快捷键 5 快速设置速度值为 5 km / h
                setSpeed(5);
                break;
            case 49://速度快捷键 6 快速设置速度值为 6 km / h
                setSpeed(6);
                break;
            case 50://速度快捷键 7 快速设置速度值为 7 km / h
                setSpeed(7);
                break;
            case 51://速度快捷键 8 快速设置速度值为 8 km / h
                setSpeed(8);
                break;
            case 52://速度快捷键 9 快速设置速度值为 9 km / h
                setSpeed(9);
                break;
            case 53://速度快捷键 10 快速设置速度值为 10 km / h
                setSpeed(10);
                break;
            case 54://速度快捷键 11 快速设置速度值为 11 km / h
                setSpeed(11);
                break;
            case 55://速度快捷键 12 快速设置速度值为 12 km / h
                setSpeed(12);
                break;
            case 56://速度快捷键 13 快速设置速度值为 13 km / h
                setSpeed(13);
                break;
            case 57://速度快捷键 14 快速设置速度值为 14 km / h
                setSpeed(14);
                break;
            case 58://速度快捷键 15 快速设置速度值为 15 km / h
                setSpeed(15);
                break;
            case 59://速度快捷键 16 快速设置速度值为 16 km / h:
                setSpeed(16);
                break;
            case 60://速度快捷键 17 快速设置速度值为 17 km / h:
                setSpeed(17);
                break;
            case 61://速度快捷键 18 快速设置速度值为 18 km / h
                setSpeed(18);
                break;
            case 62://速度快捷键 19 快速设置速度值为 19 km / h
                setSpeed(19);
                break;
            case 63:// 速度快捷键 20 快速设置速度值为 20 km / h
                setSpeed(20);
                break;
            case 64://速度快捷键 21 快速设置速度值为 11 km / h
                setSpeed(21);
                break;
            case 65://速度快捷键 22 快速设置速度值为 22 km / h
                setSpeed(22);
                break;
            case 66://速度快捷键 23 快速设置速度值为 23 km / h
                setSpeed(23);
                break;
            case 67://速度快捷键 24 快速设置速度值为 24 km / h
                setSpeed(24);
                break;
            case 68://速度快捷键 25 快速设置速度值为 25 km / h
                setSpeed(25);
                break;
            case 69://编程模式键 打开编程设置界面(仅在跑步机停止状态有效)
                if (!TreadApplication.getInstance().isRuning() && TreadApplication.getInstance().getSport() != TreadApplication.getInstance().FIVE_SECOND_STATUS) {
                    setMode(2);
                    setIsPre(true);
                }
                break;
            case 70://倒数模式键 打开倒数模式设置界面(此模式下包含时间、距离、卡路里 三种倒数模式，仅在跑步机停止状态有效)
                if (!TreadApplication.getInstance().isRuning() && TreadApplication.getInstance().getSport() != TreadApplication.getInstance().FIVE_SECOND_STATUS)
                    setMode(getMoudle());
                break;
            case 71://开始键 跑步机开始键
                start();
                break;
            case 72://停止键
                stop();
                break;
            case 73://安全锁 有放安全锁定义为释放状态，没有安全锁定义为按下或保持 状态
                safe(ZT);
                break;
            case 76://公制和英制轮换键 公制、英制切换（目前程序无作用，默认公制）
                break;
            case 78://数字 0 键 数字 0 按键
                break;
            case 79://数字 1 键 数字 1 按键
                break;
            case 80://数字 2 键 数字 2 按键
                break;
            case 81://数字 3 键 数字 3 按键
                break;
            case 82://数字 4 键 数字 4 按键
                break;
            case 83://数字 5 键 数字 5 按键
                break;
            case 84://数字 6 键 数字 6 按键
                break;
            case 85://数字 7 键 数字 7 按键
                break;
            case 86://数字 8 键 数字 8 按键
                break;
            case 87://数字 9 键 数字 9 按键
                break;
            case 91://HOME 键 快速回到跑步机主界面
                mainActivity.setCurrentIndex(0);
                break;
            case 92: //跑步机暂停键 暂停跑步机，非停止
                break;
            case 93://使用者模式键 打开 USER 模式设置界面（仅在跑步机停止状态有效）
                setMode(4);
                break;
            case 94:  // HRC 模式键 打开 HRC 模式设置界面（仅在跑步机停止状态有效）
                setMode(5);
                break;
            case 95: //媒体播放上一曲 媒体播放上一曲
                mediaOpera(KeyEvent.KEYCODE_MEDIA_PREVIOUS);
                break;
            case 96://媒体播放下一曲 媒体播放下一曲
                mediaOpera(KeyEvent.KEYCODE_MEDIA_NEXT);
                break;
            case 97: //媒体播放键 媒体开始播放（仅预留，目前程序无作用）
                break;
            case 98://媒体暂停键 媒体暂停（仅预留，目前程序无作用）
                break;
            case 99: //媒体停止 媒体停止播放（仅预留，目前程序无作用）
                break;
            case 100: //减脂模式 打开减脂模式（仅在跑步机停止状态有效）
                break;
            case 101://康复模式 打开康复模式（仅在跑步机停止状态有效）
                break;
            case 102: //工程模式 打开工程模式（仅在跑步机停止状态有效）
                if (TreadApplication.getInstance().getSport() == TreadApplication.getInstance().NOTSAFE_STATUS && !isFrist)
                    showSystemDialog();
                break;
            case 103://工程模式 关闭工程模式（仅在跑步机停止状态有效）
                if (dialog != null && !TreadApplication.getInstance().isRuning() && isFrist) {
                    num1 = false;//是否有值
                    num2 = false;
                    isFrist = false;
                    handler.sendEmptyMessage(1);
                    dialog.dismiss();
                    isengineering_model = false;
                }
                break;
            case 104://手动加油
                Log.e("OIL_ENABLE", TreadApplication.getInstance().OIL_ENABLE + "");
                Log.e("getCustom", TreadApplication.getInstance().getCustom() + "");
                Log.e("getSport()", TreadApplication.getInstance().getSport() + "");
                Log.e("OIL_ENABLE", (TreadApplication.getInstance().OIL_ENABLE != 0) + "");
                Log.e("getCustom", (TreadApplication.getInstance().getCustom().equals("yijikang")) + "");
                Log.e("getSport()", (TreadApplication.getInstance().getSport() == TreadApplication.getInstance().NOTSAFE_STATUS) + "");
                if (TreadApplication.getInstance().OIL_ENABLE != 0)
                    if (TreadApplication.getInstance().getCustom().equals("yijikang"))
                        if (TreadApplication.getInstance().getSport() == TreadApplication.getInstance().NOTSAFE_STATUS) {
                            setIsrefuel(true);
                        }
                break;
        }
    }

    public void upRaise() {
        muteRelieve();
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
    }

    public void dowlower() {
        muteRelieve();
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
    }

    static boolean isFrist;

    @TargetApi(Build.VERSION_CODES.M)
    private void sound() {
        if (audioManager.isStreamMute(AudioManager.STREAM_MUSIC)) {
            audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
            audioManager.setStreamMute(AudioManager.STREAM_ALARM, false);
            audioManager.setStreamMute(AudioManager.STREAM_NOTIFICATION, false);
            audioManager.setStreamMute(AudioManager.STREAM_VOICE_CALL, false);
            audioManager.setStreamMute(AudioManager.STREAM_RING, false);
            audioManager.setStreamMute(AudioManager.STREAM_SYSTEM, false);
            audioManager.setStreamMute(AudioManager.STREAM_DTMF, false);
        } else {
            audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
            audioManager.setStreamMute(AudioManager.STREAM_ALARM, true);
            audioManager.setStreamMute(AudioManager.STREAM_NOTIFICATION, true);
            audioManager.setStreamMute(AudioManager.STREAM_VOICE_CALL, true);
            audioManager.setStreamMute(AudioManager.STREAM_RING, true);
            audioManager.setStreamMute(AudioManager.STREAM_SYSTEM, true);
            audioManager.setStreamMute(AudioManager.STREAM_DTMF, true);
        }
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
    }

    public void muteRelieve() {
        audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
        audioManager.setStreamMute(AudioManager.STREAM_ALARM, false);
        audioManager.setStreamMute(AudioManager.STREAM_NOTIFICATION, false);
        audioManager.setStreamMute(AudioManager.STREAM_VOICE_CALL, false);
        audioManager.setStreamMute(AudioManager.STREAM_RING, false);
        audioManager.setStreamMute(AudioManager.STREAM_SYSTEM, false);
        audioManager.setStreamMute(AudioManager.STREAM_DTMF, false);
    }

    /**
     * 媒体操作
     */
    private void mediaOpera(int opera) {
        intent = new Intent();
        intent.setAction(Intent.ACTION_MEDIA_BUTTON);
        keyEvent = new KeyEvent(KeyEvent.ACTION_DOWN, opera);
        intent.putExtra(Intent.EXTRA_KEY_EVENT, keyEvent);
        context.sendBroadcast(intent);
        keyEvent = new KeyEvent(KeyEvent.ACTION_UP, opera);
        intent.putExtra(Intent.EXTRA_KEY_EVENT, keyEvent);
        context.sendBroadcast(intent);
    }

    public static boolean isPre;

    public static boolean isPre() {
        return isPre;
    }

    public static void setIsPre(boolean isPre) {
        SerialComm.isPre = isPre;
    }

    public static boolean isNo() {
        return isNo;
    }

    public static void setIsNo(boolean isNo) {
        SerialComm.isNo = isNo;
    }

    public static boolean isNo;

    private int getMoudle() {
        isNo = true;
        return 0;
    }

    static boolean isPress;//是否按下安全鎖

    private void safe(int ZT) {
        if (ZT == 0) {
            if (isPress) {
                isPress = false;
                currErr = 0;
                windowInfoService.dimss();
            }
        } else if (ZT == 1) {
            if (!isPress) {
                windowInfoService.creatDialog(7);
                TreadApplication.setMediaPlayer(R.raw.lockwire);
                isPress = true;
                TreadApplication.getInstance().setSport(TreadApplication.getInstance().NOTSAFE_STATUS);
            }
        } else if (ZT == 2) {
//            TreadApplication.getInstance().setSport(TreadApplication.getInstance().NOTSAFE_STATUS);
        }
    }

    private void stop() {
        if (TreadApplication.getInstance().isRuning()) {
            windowInfoService.stoping();
        }
    }

    private void start() {
        if (!TreadApplication.getInstance().isRuning() && TreadApplication.getInstance().getSport() != TreadApplication.getInstance().FIVE_SECOND_STATUS)
            switch (TreadApplication.getInstance().getSport()) {
                case TreadApplication.NORMAL_STATUS:
                    windowInfoService.startBtn();
                    break;
                case TreadApplication.TIME_SETUP_STATUS:
                    onStartRun.onRun(1);
                    break;
                case TreadApplication.DISTANCE_SETUP_STATUS:
                    onStartRun.onRun(2);
                    break;
                case TreadApplication.CALORIE_SETUP_STATUS:
                    onStartRun.onRun(3);
                    break;
                case TreadApplication.PROGRAM_SETUP_STATUS:
                    onStartRun.onRun(4);
                    break;
                case TreadApplication.HRC_SETUP_STATUS:
                    onStartRun.onRun(6);
                    break;
                case TreadApplication.USER_SETUP_STATUS:
                    onStartRun.onRun(5);
                    break;
            }
    }

    private void setSpeed(double speed) {
        if (TreadApplication.getInstance().isRuning()) {
            windowInfoService.setmSpeed(speed);
        }
    }

    private void setSlopes(int slopes) {
        if (TreadApplication.getInstance().isRuning()) {
            windowInfoService.setmSlopes(slopes);
        }
    }

    private void setMode(int mod) {
        if (!TreadApplication.getInstance().isRuning() && TreadApplication.getInstance().getSport() != TreadApplication.getInstance().FIVE_SECOND_STATUS) {
            onClickMain.onMain(mod);
        }
    }

    public int getBEEP() {
        return BEEP;
    }

    public static void setBEEP(int BEEPs) {
        BEEP = BEEPs;
    }

    public int getFAP() {
        return FAP;
    }

    public void setFAP(int FAPs) {
        FAP = FAPs;
    }

    public void close() {
        try {
            if (mSerialPort != null)
                mSerialPort.close();
            if (mInputStream != null)
                mInputStream.close();
            if (mOutputStream != null) {
                mOutputStream.flush();
                mOutputStream.close();
            }
//            if (mBuleSerialPort != null)
//                mBuleSerialPort.close();
//            if (mBuleInputStream != null)
//                mBuleInputStream.close();
//            if (mBuleOutputStream != null) {
//                mBuleOutputStream.flush();
//                mBuleOutputStream.close();
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static Dialog dialog;
    static TextView number, values;
    static boolean isengineering_model;

    private void showSystemDialog() {
        isFrist = true;
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_engineering_model);
        dialog.getWindow().setType((WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
        dialog.setCancelable(false);
        number = (TextView) dialog.findViewById(R.id.number);
        values = (TextView) dialog.findViewById(R.id.values);
        isengineering_model = true;
        dialog.show();
    }

    public static void setOnClickMain(OnClickMain onClickMains) {
        onClickMain = onClickMains;
    }

    public static void setOnStartRun(OnStartRun onStartR) {
        onStartRun = onStartR;
    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        stopSelf();
//    }
}