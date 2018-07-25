package com.example.moudle;

import android.util.Log;

/**
 * 环形缓冲区
 */

public class Circular {
    int NMAX = 512;
    int iput = 0;//环形缓冲区的当前放入位置
    int iget = 0;//缓冲区的当前取出位置
    int n = 0;//环形缓冲区中的元素总数量
    byte buffer[] = new byte[NMAX];


    /**
     * 从环形缓冲区中取一个元素
     */
    public byte gain() {
        int pos;
        if (n > 0) {
            pos = iget;
            iget = addring(iget);
            n--;
            Log.e("content", "get-->" + buffer[pos]);
            return buffer[pos];
        } else {
            Log.e("content", "Buffer is Empty");
//            return 0.0;
            return Byte.parseByte(null);
        }
    }

    /**
     * 从环形缓冲区中取一个元素
     */
    public byte[] get() {
        if (n >= 6) {
            int pos = iget;
            int num = 0;//字节长度
            int head_found = 0;
            int slack_byte = 0;//无效字节
            int startIndex = 0;//开始读取位置
            for (int i = 0; i < n; i++) {
                if (head_found == 1) {
                    num++;
                    if (buffer[pos] == 'F' && buffer[addring(pos)] == 'F') {
                        iget = startIndex;
                        n -= slack_byte;
                        byte[] byte1 = new byte[num];
                        for (int j = 0; j < num; j++) {
                            byte1[j] = gain();
                        }
                        return byte1;
                    } else {
                        pos = addring(pos);
                    }
                } else if (buffer[pos] == 'E' && buffer[addring(pos)] == 'E') {
                    head_found = 1;
                    startIndex = pos;
                    num = 2;
                    pos = addring(pos);
                } else {
                    pos = addring(pos);
                    slack_byte++;
                }
            }
        }
        return null;
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

    /**
     * 环形缓冲区的地址编号计算函数，如果到达唤醒缓冲区的尾部，将绕回到头部。
     * 环形缓冲区的有效地址编号为：0到(NMAX-1)
     */
    public int addring(int i) {
        return (i + 1) == NMAX ? 0 : i + 1;
    }
}