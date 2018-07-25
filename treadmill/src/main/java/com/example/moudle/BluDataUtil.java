package com.example.moudle;

import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lenovo on 2016/11/23.
 */

public class BluDataUtil {

    /**
     * 字符串的拼接,
     * 用来处理接收到的字符串
     *
     */
    String strData = null;                 //用来接收蓝牙收到的字符串
    public boolean SplitStr(String str){
        strData += str;
        Pattern p = Pattern.compile("EE(.*)FF");      //截取EE  FF  之间的所有字符串
        Matcher m = p.matcher(strData);
        while (m.find()){
            //Log.i("蓝牙","-------------------"+strData+"--------------------");

            String str5 = m.group(1);                      //找到符合要求的字符串,EE与FF之间
            //去掉后四位校验码
            String str6 = SplitStrTF(str5,0,str5.length()-4);
            //ASCII求和
            int num = stringToAscii(str6);
            //十进制转换为十六进制
            String hexstr = toHexStr(num);
            //截取后两位
            String str3 = hexstr.substring(hexstr.length()-2,hexstr.length());
            //十六进制转换为十进制
            int num2 = toParseInt(str3);
            //将十进制整形转换为字符串
            String str4 = String.valueOf(num2);
            //如果不足三位前边加0
            if(str4.length() == 2){
                str4 = "0" + str4;
            }
            if(str4.length() == 1){
                str4 = "00" + str4;
            }
            //取后边的校验码
            String str2 = SplitStrTF(str5,str5.length()-4,str5.length()-1);
            //Log.i("蓝牙","---计算值： " + str4 + " ------- 收到值： " + str2 + "---");
            //Toast.makeText(context,"---计算值： " + str4 + " ------- 收到值： " + str2 + "---",Toast.LENGTH_SHORT).show();
            //判断校验码是否正确
            Log.e("content","str4:"+str4);
            if(str4.equals(str2)){
                //数据正确
                //解析字符串放到BluResult中
                strData = null;
                return true;
            }
            strData = null;
        }
        return false;
    }

    /**
     * 截取字符串的前i和后n位
     */
    public String  SplitStrTF(String str,int i,int n){
        int length = str.length();
        if(length<8 ){

        }
        return str.substring(i,n);
    }

    /**
     * 将字符串转成ASCII的Java方法,并且求和
     */
    public static int stringToAscii(String  str) {
        int num = 0;
        StringBuffer sbu = new StringBuffer();
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if(i != chars.length)
            {
                sbu.append(chars[i] +"-"+ (int)chars[i]).append(",");
                num = num + (int)chars[i];
            }
            else {
                sbu.append((int)chars[i]);
            }
        }
        return num;
    }
    /**
     * 十进制转十六进制
     */
    public String toHexStr(int um){
        return Integer.toHexString(um);
    }

    /**
     * 十六进制转十进制
     */
    public int toParseInt(String str){
        return Integer.parseInt(str, 16);
    }

}
