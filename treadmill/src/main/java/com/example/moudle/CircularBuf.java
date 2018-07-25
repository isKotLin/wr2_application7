package com.example.moudle;

/**
 * 环形缓冲区
 */

public class CircularBuf {
    int NMAX = 3;
    int iput = 0;//环形缓冲区的当前放入位置
    int iget = 0;//缓冲区的当前取出位置
    int n = 0;//环形缓冲区中的元素总数量
    double buffer[] = new double[NMAX];

    /**
     * 环形缓冲区的地址编号计算函数，如果到达唤醒缓冲区的尾部，将绕回到头部。
     * 环形缓冲区的有效地址编号为：0到(NMAX-1)
     */
    public int addring(int i) {
        return (i + 1) == NMAX ? 0 : i + 1;
    }

    /**
     * 从环形缓冲区中取一个元素
     */
    public double get() {
        int pos;
        if (n > 0) {
            pos = iget;
            iget = addring(iget);
            n--;
            System.out.println("get-->" + buffer[pos]);
            return buffer[pos];
        } else {
            System.out.println("Buffer is Empty");
            return 0.0;
        }
    }

    /**
     * 向环形缓冲区中放入一个元素
     */
    public void put(double z) {
        if (n < NMAX) {
            buffer[iput] = z;
            System.out.println("put<--" + buffer[iput]);
            iput = addring(iput);
            n++;
        } else
            System.out.println("Buffer is full");
    }

    public static void main(String[] args) {
        CircularBuf cb = new CircularBuf();
        cb.put(2);
        cb.put(24648.4346463135465121);
        cb.put(3.88);
        cb.get();
        cb.put(4.45);
        cb.get();
        cb.get();
        cb.get();
        cb.put(5);
        cb.get();
    }
}