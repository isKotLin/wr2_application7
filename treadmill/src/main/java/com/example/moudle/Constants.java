package com.example.moudle;

/**
 *
 */
public class Constants {
    /**
     * 串口号
     */
    public static String SERIAL_PORT_DEVICE = "/dev/ttyS2";
    public static final String SERIAL_PORT_DEVICE_BULE = "/dev/ttyS1";

    /**
     * 波特率
     */
    public static final int SERIAL_PORT_BAUD = 9600;
    public static final int SERIAL_PORT_BAUD_BULE = 115200;
    /**
     * 接收消息头
     */
    public static final byte MESSAGE_RECV_HEAD = (byte) 0xFD;
    /**
     * 接收消息尾
     */
    public static final byte MESSAGE_RECV_END = (byte) 0xFE;
    /**
     * 发送消息头
     */
    public static final byte MESSAGE_POST_HEAD = (byte) 0xFB;
    /**
     * 发送消息尾
     */
    public static final byte MESSAGE_POST_END = (byte) 0xFC;
    /**
     * 返回按键值
     */
    public static final int BUTTON_COMMAND = 0x1;
    /**
     * 心跳值
     */
    public static final int HEARTBEAT_VALUE = 0x2;
    /**
     * 返回错误
     */
    public static final int ERROR_VALUE = 0x3;
    /**
     * 起始速度转速值
     */
    public static final int INITIAL_SPEED = 0x4;
    /**
     * 8km转速值
     */
    public static final int KM_RPM8 = 0x5;
    /**
     * 最高速度转速值
     */
    public static final int MAXIMUM_REV_VALUE = 0x6;
    /**
     * 扭力值
     */
    public static final int TORQUE_VALUE = 0x7;
    /**
     * 速度闭环值
     */
    public static final int SPEED_CLOSED_LOOP = 0x8;
    /**
     * 最低速度值
     */
    public static final int MINIMUM_SPEED_VALUE = 0x9;
    /**
     * 最高速度值
     */
    public static final int MAXIMUM_SPEED_VALUE = 0xa;
    /**
     * 最大扬升值
     */
    public static final int MAXIMUM_UPLIFT = 0xb;
    /**
     * 扬升学习
     */
    public static final int ASCENSION_LEARNING = 0xc;
}