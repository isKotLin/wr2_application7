package com.example.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.app.TreadApplication;
import com.vigorchip.treadmill.wr2.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 */
public class MySharePreferences {
    Context mContext;
    String userIds;
    SharedPreferences preferences;
    SharedPreferences shared;
    SharedPreferences.Editor editor;

    public MySharePreferences(Context context, String userId) {
        mContext = context;
        userIds = userId;
        preferences = mContext.getSharedPreferences(userIds, mContext.MODE_PRIVATE);
        editor = preferences.edit();
        shared = mContext.getSharedPreferences(userIds, mContext.MODE_PRIVATE);
    }

    /**
     * 编辑用户信息
     *
     * @param name
     * @param age
     * @param height
     * @param weight
     */
    public void setUserInfo(String name, String age, String height, String weight) {
        editor.putString("name", name);
        editor.putString("age", age);
        editor.putString("height", height);
        editor.putString("weight", weight);
        editor.commit();
    }
    public String getUserWeight() {
        String getWeight = shared.getString("weight", "67");
        return getWeight;
    }

    /**
     * 用户
     *
     * @return
     */
    public String getUserName() {
        String getName = null;
        switch (userIds) {
            case "user1":
                getName = shared.getString("name", mContext.getString(R.string.user1));
                break;
            case "user2":
                getName = shared.getString("name", mContext.getString(R.string.user2));
                break;
            case "user3":
                getName = shared.getString("name", mContext.getString(R.string.user3));
                break;
            case "user4":
                getName = shared.getString("name", mContext.getString(R.string.user4));
                break;
            case "user5":
                getName = shared.getString("name", mContext.getString(R.string.user5));
                break;
        }
        return getName;
    }

    public String getUserAge() {
        String getAge = shared.getString("age", "25");
        return getAge;
    }

    public String getUserHeight() {
        String getHeight = shared.getString("height", "175");
        return getHeight;
    }

    /**
     * 自定义的速度、扬升与时间
     *
     * @return
     */
    public double[] getSpeedData() {
        double speed1 = shared.getFloat("speed1", (float) TreadApplication.getInstance().MINSPEED);
        double speed2 = shared.getFloat("speed2", (float) TreadApplication.getInstance().MINSPEED);
        double speed3 = shared.getFloat("speed3", (float) TreadApplication.getInstance().MINSPEED);
        double speed4 = shared.getFloat("speed4", (float) TreadApplication.getInstance().MINSPEED);
        double speed5 = shared.getFloat("speed5", (float) TreadApplication.getInstance().MINSPEED);
        double speed6 = shared.getFloat("speed6", (float) TreadApplication.getInstance().MINSPEED);
        double speed7 = shared.getFloat("speed7", (float) TreadApplication.getInstance().MINSPEED);
        double speed8 = shared.getFloat("speed8", (float) TreadApplication.getInstance().MINSPEED);
        double speed9 = shared.getFloat("speed9", (float) TreadApplication.getInstance().MINSPEED);
        double speed10 = shared.getFloat("speed10", (float) TreadApplication.getInstance().MINSPEED);
        double speed11 = shared.getFloat("speed11", (float) TreadApplication.getInstance().MINSPEED);
        double speed12 = shared.getFloat("speed12", (float) TreadApplication.getInstance().MINSPEED);
        double speed13 = shared.getFloat("speed13", (float) TreadApplication.getInstance().MINSPEED);
        double speed14 = shared.getFloat("speed14", (float) TreadApplication.getInstance().MINSPEED);
        double speed15 = shared.getFloat("speed15", (float) TreadApplication.getInstance().MINSPEED);
        double speed16 = shared.getFloat("speed16", (float) TreadApplication.getInstance().MINSPEED);
//        double speed17 = shared.getFloat("speed17", (float) TreadApplication.getInstance().MINSPEED);
//        double speed18 = shared.getFloat("speed18", (float) TreadApplication.getInstance().MINSPEED);
//        double speed19 = shared.getFloat("speed19", (float) TreadApplication.getInstance().MINSPEED);
//        double speed20 = shared.getFloat("speed20", (float) TreadApplication.getInstance().MINSPEED);
//        double speed21 = shared.getFloat("speed21", (float) TreadApplication.getInstance().MINSPEED);
//        double speed22 = shared.getFloat("speed22", (float) TreadApplication.getInstance().MINSPEED);
//        double speed23 = shared.getFloat("speed23", (float) TreadApplication.getInstance().MINSPEED);
//        double speed24 = shared.getFloat("speed24", (float) TreadApplication.getInstance().MINSPEED);
//        double speed25 = shared.getFloat("speed25", (float) TreadApplication.getInstance().MINSPEED);
//        double speed26 = shared.getFloat("speed26", (float) TreadApplication.getInstance().MINSPEED);
//        double speed27 = shared.getFloat("speed27", (float) TreadApplication.getInstance().MINSPEED);
//        double speed28 = shared.getFloat("speed28", (float) TreadApplication.getInstance().MINSPEED);
//        double speed29 = shared.getFloat("speed29", (float) TreadApplication.getInstance().MINSPEED);
//        double speed30 = shared.getFloat("speed30", (float) TreadApplication.getInstance().MINSPEED);
        double[] getSpeed = new double[16];
        getSpeed[0] = speed1;
        getSpeed[1] = speed2;
        getSpeed[2] = speed3;
        getSpeed[3] = speed4;
        getSpeed[4] = speed5;
        getSpeed[5] = speed6;
        getSpeed[6] = speed7;
        getSpeed[7] = speed8;
        getSpeed[8] = speed9;
        getSpeed[9] = speed10;
        getSpeed[10] = speed11;
        getSpeed[11] = speed12;
        getSpeed[12] = speed13;
        getSpeed[13] = speed14;
        getSpeed[14] = speed15;
        getSpeed[15] = speed16;
//        getSpeed[16] = speed17;
//        getSpeed[17] = speed18;
//        getSpeed[18] = speed19;
//        getSpeed[19] = speed20;
//        getSpeed[20] = speed21;
//        getSpeed[21] = speed22;
//        getSpeed[22] = speed23;
//        getSpeed[23] = speed24;
//        getSpeed[24] = speed25;
//        getSpeed[25] = speed26;
//        getSpeed[26] = speed27;
//        getSpeed[27] = speed28;
//        getSpeed[28] = speed29;
//        getSpeed[29] = speed30;
        return getSpeed;
    }

    public int[] getSlopesData() {
        int slopes1 = (int) shared.getFloat("slopes1", 0);
        int slopes2 = (int) shared.getFloat("slopes2", 0);
        int slopes3 = (int) shared.getFloat("slopes3", 0);
        int slopes4 = (int) shared.getFloat("slopes4", 0);
        int slopes5 = (int) shared.getFloat("slopes5", 0);
        int slopes6 = (int) shared.getFloat("slopes6", 0);
        int slopes7 = (int) shared.getFloat("slopes7", 0);
        int slopes8 = (int) shared.getFloat("slopes8", 0);
        int slopes9 = (int) shared.getFloat("slopes9", 0);
        int slopes10 = (int) shared.getFloat("slopes10", 0);
        int slopes11 = (int) shared.getFloat("slopes11", 0);
        int slopes12 = (int) shared.getFloat("slopes12", 0);
        int slopes13 = (int) shared.getFloat("slopes13", 0);
        int slopes14 = (int) shared.getFloat("slopes14", 0);
        int slopes15 = (int) shared.getFloat("slopes15", 0);
        int slopes16 = (int) shared.getFloat("slopes16", 0);
//        int slopes17 = (int) shared.getFloat("slopes17", 0);
//        int slopes18 = (int) shared.getFloat("slopes18", 0);
//        int slopes19= (int) shared.getFloat("slopes19", 0);
//        int slopes20= (int) shared.getFloat("slopes20", 0);
//        int slopes21= (int) shared.getFloat("slopes21", 0);
//        int slopes22= (int) shared.getFloat("slopes22", 0);
//        int slopes23= (int) shared.getFloat("slopes23", 0);
//        int slopes24= (int) shared.getFloat("slopes24", 0);
//        int slopes25= (int) shared.getFloat("slopes25", 0);
//        int slopes26 = (int) shared.getFloat("slopes26", 0);
//        int slopes27 = (int) shared.getFloat("slopes27", 0);
//        int slopes28 = (int) shared.getFloat("slopes28", 0);
//        int slopes29 = (int) shared.getFloat("slopes29", 0);
//        int slopes30 = (int) shared.getFloat("slopes30", 0);
        int[] getSlopes = new int[16];
        getSlopes[0] = slopes1;
        getSlopes[1] = slopes2;
        getSlopes[2] = slopes3;
        getSlopes[3] = slopes4;
        getSlopes[4] = slopes5;
        getSlopes[5] = slopes6;
        getSlopes[6] = slopes7;
        getSlopes[7] = slopes8;
        getSlopes[8] = slopes9;
        getSlopes[9] = slopes10;
        getSlopes[10] = slopes11;
        getSlopes[11] = slopes12;
        getSlopes[12] = slopes13;
        getSlopes[13] = slopes14;
        getSlopes[14] = slopes15;
        getSlopes[15] = slopes16;
//        getSlopes[16] = slopes17;
//        getSlopes[17] = slopes18;
//        getSlopes[18] = slopes19;
//        getSlopes[19] = slopes20;
//        getSlopes[20] = slopes21;
//        getSlopes[21] = slopes22;
//        getSlopes[22] = slopes23;
//        getSlopes[23] = slopes24;
//        getSlopes[24] = slopes25;
//        getSlopes[25] = slopes26;
//        getSlopes[26] = slopes27;
//        getSlopes[27] = slopes28;
//        getSlopes[28] = slopes29;
//        getSlopes[29] = slopes30;
        return getSlopes;
    }

    public void setSpeedData(double[] speed) {
        editor.putFloat("speed1", (float) speed[0]);
        editor.putFloat("speed2", (float) speed[1]);
        editor.putFloat("speed3", (float) speed[2]);
        editor.putFloat("speed4", (float) speed[3]);
        editor.putFloat("speed5", (float) speed[4]);
        editor.putFloat("speed6", (float) speed[5]);
        editor.putFloat("speed7", (float) speed[6]);
        editor.putFloat("speed8", (float) speed[7]);
        editor.putFloat("speed9", (float) speed[8]);
        editor.putFloat("speed10", (float) speed[9]);
        editor.putFloat("speed11", (float) speed[10]);
        editor.putFloat("speed12", (float) speed[11]);
        editor.putFloat("speed13", (float) speed[12]);
        editor.putFloat("speed14", (float) speed[13]);
        editor.putFloat("speed15", (float) speed[14]);
        editor.putFloat("speed16", (float) speed[15]);
        editor.commit();
    }

    public void setSlopesData(int[] slopes) {
        editor.putFloat("slopes1", slopes[0]);
        editor.putFloat("slopes2", slopes[1]);
        editor.putFloat("slopes3", slopes[2]);
        editor.putFloat("slopes4", slopes[3]);
        editor.putFloat("slopes5", slopes[4]);
        editor.putFloat("slopes6", slopes[5]);
        editor.putFloat("slopes7", slopes[6]);
        editor.putFloat("slopes8", slopes[7]);
        editor.putFloat("slopes9", slopes[8]);
        editor.putFloat("slopes10", slopes[9]);
        editor.putFloat("slopes11", slopes[10]);
        editor.putFloat("slopes12", slopes[11]);
        editor.putFloat("slopes13", slopes[12]);
        editor.putFloat("slopes14", slopes[13]);
        editor.putFloat("slopes15", slopes[14]);
        editor.putFloat("slopes16", slopes[15]);
        editor.commit();
    }

    public int getTime() {
        int time = shared.getInt("time", 15);
        return time;
    }

    public void setTime(int time) {
        editor.putInt("time", time);
        editor.commit();
    }

    public int getWall() {//获取背景
        int index = shared.getInt("wall", 0);
        return index;
    }

    public void setWall(int index) {//设置背景
        editor.putInt("wall", index);
        editor.commit();
    }

    public void commitString(String language) {
        editor.putString("language", language);
        editor.commit();
    }

    public String getString() {
        String strLanguage = shared.getString("language", "zh");
        return strLanguage;
    }

    public void setDate(long time) {
        editor.putLong("date", time);
        editor.commit();
    }

    public long getDate() {
        Log.e("initData()", initData() + "");
        long date = shared.getLong("date", initData());
        return date;
    }

    StringBuffer buffer;

    private long initData() {//最后打包的时间
        String data="2.0.80305.01";
        buffer = new StringBuffer("201");
//        buffer.append(FileUtils.getVersionName(context).substring(4, 9));
        buffer.append(data.substring(4, 9));
        buffer.append(" 00:00:00");
//        buffer = new StringBuffer("201");
//        buffer.append(FileUtils.getVersionName(context).substring(4, 9));
//        buffer.append(" 00:00:00");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        String strDate = buffer.toString();
        Date date = null;
        try {
            date = sdf.parse(strDate);
        } catch (ParseException e) {
//            e.printStackTrace();
            Log.e("TAG", "时间初始化错误");
        }
        return date.getTime();
    }
}
