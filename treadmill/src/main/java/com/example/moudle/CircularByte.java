package com.example.moudle;

import android.util.Log;

/**
 * 环形缓冲区
 */

public class CircularByte {
    int NMAX = 512;
    int iput = 0;//环形缓冲区的当前放入位置
    int iget = 0;//缓冲区的当前取出位置
    int n = 0;//环形缓冲区中的元素总数量
    byte buffer[] = new byte[NMAX];

    /**
     * 环形缓冲区的地址编号计算函数，如果到达唤醒缓冲区的尾部，将绕回到头部。
     * 环形缓冲区的有效地址编号为：0到(NMAX-1)
     */
    public int addring(int i) {
        return (i + 1) == NMAX ? 0 : i + 1;
    }

//    /**
//     * 从环形缓冲区中取一个元素
//     */
//    public byte get() {
//        int pos = 0;
//        if (n > 0) {
//            pos = iget;
//            iget = addring(iget);
//            n--;
//            Log.e("content", "get-->" + buffer[pos]);
//            return buffer[pos];
//        } else {
//            Log.e("content", "Buffer is Empty");
////            return 0.0;
//            return Byte.parseByte(null);
//        }
//    }

    /**
     * 从环形缓冲区中取一个元素
     */
    public byte get(int index) {
        int pos;
        iget=index;
        if (n > 0) {
            pos = iget;
            iget = addring(iget);
            n--;
            Log.e("content", "get-->"+pos);
            return buffer[pos];
        } else {
            Log.e("content", "Buffer is Empty");
//            return 0.0;
            return Byte.parseByte(null);
        }
    }


    public int getReadIndex() {
        return readIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public int getNum() {
        return num + 1;
    }

    int readIndex;//开始读取位置
    int endIndex;//开始读取位置

    public void setNum(int nums) {
        num = nums;
    }
    int num ;//字节长度
    public boolean read() {
        int pos = iget;//当前下标
        num=0;
        int slack_byte=0;//无效字节
        for (int i = 0; i < n; i++) {
            if (buffer[pos] == 'E' && buffer[addring(pos)] == 'E') {
                readIndex = pos;
                num = 1;
                Log.e("contentbuffer", "num:" + num);
            }
            if (buffer[pos] == 'F' && buffer[addring(pos)] == 'F') {
                Log.e("contentbuffer", "i:" + i + " n:" + n);
                endIndex = pos;
                n-=slack_byte;
                return true;
            }
            pos = addring(pos);
            if (num > 0) {
                num++;
                Log.e("contentbuffer", "num:" + num);
            }
            if (num==0){
                slack_byte++;
            }
        }
        return false;
    }

    /**
     * 向环形缓冲区中放入一个元素
     */
    public void put(byte z) {
        if (n < NMAX) {
            buffer[iput] = z;
            Log.e("content", "put<--" + buffer[iput]);
            iput = addring(iput);
            n++;
        } else {
            Log.e("content", "Buffer is full n:" + n);
        }
    }
}