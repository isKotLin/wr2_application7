package com.example.moudle;
///////////////////////////////////////////////////////////////////////////////  
/////// 写了个小玩意 — ascii字符“播放器“，其实也只需要解决个缓冲问题，         ///////  
/////// 因我很无耻地认为这个环形缓冲还有其它用武之地，                        ///////  
/////// 于是贴上来了。 接受考验~ 请不吝BS~                                  ///////  
/////// main方法只是个使用例子，可以删除之。                                 ///////  
///////////////////////////////////////////////////////////////////////////////  

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * 环形缓冲区（数组实现），逻辑上缓冲区是首位衔接着，像个圆圈一样。
 *
 * @author Cupenoruler@foxmail.com
 * @version 2011-11-22
 */
public class CircularBuffer<T> {

    private Object[] buffer = null; // 数据容器
    private int capacity = 0;    // 缓冲区长度
    private int indexForPut = 0;    // put索引 （下一个要写入的位置）
    private int indexForGet = 0;    // get索引 （下一个要读取的位置）

    /* put 和 get 的线程状态标记 */
    private ThreadState putThreadState = ThreadState.RUNNING;
    private ThreadState getThreadState = ThreadState.RUNNING;

    /*************************************************\
     *            仅作测试之用,可删除此方法          *
     * 小子这里仅有6000多个txt文本，基本凑合  *
     \************************************************/
    public static void main(String[] shit) {

        //拿到源文件列表
        String txtsPath = "D:/Backup/workbench/ASCII_Player/resource/txt";
        final File[] fileSource = new File(txtsPath).listFiles();

        final CircularBuffer<String> cBuffer = new CircularBuffer<>(2048);

        //专业的读文本线程
        Thread putThread =
                new Thread() {
                    public void run() {
                        StringBuilder tempBuilder = new StringBuilder(4096); //文本缓冲
                        BufferedReader tempReader = null;
                        String tempString;

                        try {
                                /* 挨个读取文本 */
                            for (File temp : fileSource) {
                                tempBuilder.delete(0, tempBuilder.length()); //清空builder
                                tempReader = new BufferedReader(new FileReader(temp));

                                while (null != (tempString = tempReader.readLine())) {
                                    tempBuilder.append(tempString);
                                    tempBuilder.append("\n");
                                }
                                cBuffer.putElement(tempBuilder.toString()); //build完成，入库
                            }
                            tempReader.close(); //读完收工

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            System.out.println("文件不存在，txtsPath可能有误");

                        } catch (IOException e) {
                            e.printStackTrace();
                            System.out.println("tempReader挂球");

                        }
                        cBuffer.setPutThreadState(ThreadState.OVER); //put线程标记为:完成
                    }
                };
                  
                /* 开动put线程………………  */
        putThread.start();
                  
                /* get线程（当前线程 ） */
        while (true) {
            String temp = cBuffer.getElement();
            if (temp == null) {
                break;
            } else {
                System.out.println(temp);
            }

            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 指定长度的缓冲区
     *
     * @param capacity
     */
    public CircularBuffer(int capacity) {
        this.capacity = capacity;
        this.buffer = new String[this.capacity];
    }

//<pre name="code"class="java">

    /**
     * 写入数据，注意此函数会导致阻塞
     *
     * @param element
     */
    public void putElement(T element) {
        // 有空位就插入～
        // 没空位就轮询，直到有空位可插入为止~
        while (null != this.buffer[this.indexForPut]) {
            try {
                this.setPutThreadState(ThreadState.BLOCK); // put线程标记为：阻塞
                Thread.sleep(100);

            } catch (InterruptedException e) {
                e.printStackTrace(); // 例行公事
                System.out.println("线程意外停止,正在检查");

            }
        }
        this.setPutThreadState(ThreadState.RUNNING); // put线程标记为：正在运行

        // 填入元素，将put索引指向下一元素位~
        this.buffer[this.indexForPut] = element;
        this.indexForPut++;
        this.indexForPut %= this.capacity;
    }

    /**
     * 取数据，注意此函数会阻塞，若put线程结束且缓冲区为空时函数会返回null
     *
     * @return 下一个T元素  或者 null
     */
    @SuppressWarnings("unchecked")
    public T getElement() {
        // 有元素就拿出～
        // 没元素就轮询，直到有元素可拿为止~ 若是put完毕、 数据取空，则返回null以告知调用者
        while (null == this.buffer[this.indexForGet]) {
            try {
                this.setGetThreadState(ThreadState.BLOCK); // get线程标记为：阻塞
                if (this.isPutedOver() && this.isEmputy()) {
                    this.setGetThreadState(ThreadState.OVER);
                    return null;
                }
                Thread.sleep(100);

            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("线程意外停止");

            }
        }
        this.setGetThreadState(ThreadState.RUNNING); // get线程标记为：正在运行
                  
                /* 拿到元素，buffer腾出该元素位，将get索引指向下一元素~ */
        Object temp = this.buffer[this.indexForGet];
        this.buffer[this.indexForGet] = null;
        this.indexForGet++;
        this.indexForGet %= this.capacity;

        return (T) temp; // 返回拿到的元素引用
    }

    /****************************************************\
     *           --Setter and Getter--                  *
     \****************************************************/
    /**
     * 检查此环形缓冲区是否为空
     *
     * @return boolean true则表示为空，false则不为空
     */
    public boolean isEmputy() {
        // 新元素是以 索引0 向 索引length 的顺序 put入
        // 有鉴于此，这里倒过来枚举，防止出现“同向追赶”导致落空的的囧事；
        for (int i = this.buffer.length - 1; i > 0; i--) {
            if (this.buffer[i] != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检查 put 线程和 get 线程是否同时阻塞,
     *
     * @return boolean true-挂球  false-良好
     */
    public boolean isAllBlock() {
        return (this.getThreadState == ThreadState.BLOCK
                && this.putThreadState == ThreadState.BLOCK);
    }

    /**
     * 检查数据源是否缓冲完毕
     *
     * @return boolean true-完成  false-未完成
     */
    public boolean isPutedOver() {
        return this.putThreadState == ThreadState.OVER;
    }

    public void setPutThreadState(ThreadState putThreadState) {
        this.putThreadState = putThreadState;
    }

    private void setGetThreadState(ThreadState getThreadState) {
        this.getThreadState = getThreadState;
    }

}

/**
 * 线程的几种状态
 *
 * @author Cupenoruler@foxmai.com
 * @version 2011-11-22
 */
enum ThreadState {
    BLOCK,      /*暂时阻塞*/
    RUNNING,    /*运行*/
    OVER,       /*线程顺利完成*/
    INTERRUPT   /*中断,(暂时用不到)*/
}