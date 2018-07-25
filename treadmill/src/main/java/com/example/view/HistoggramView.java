package com.example.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.app.TreadApplication;

import java.text.DecimalFormat;

public class HistoggramView extends View {
    /**
     * 声明画笔
     */
    private Paint mPaint_X;//X坐标轴画笔
    private Paint mPaint_Y;//Y坐标轴画笔
    private Paint mPaint_InsideLine;//内部虚线P
    private Paint mPaint_Text;//字体画笔
    private Paint mPaint_Rec;//矩形画笔
    private Paint mPaint_Line;
    private Paint mPaint_back;
    int left=5;
    int right=35;
    int text_top=20;
    private OnChartClickListener listener;
    DecimalFormat format = new DecimalFormat("#0.0");
    DecimalFormat formats = new DecimalFormat("#0");
    private double[] dataRec = new double[16];//矩形数据
    private int width;//视图的宽高
    private int height;
    int leftHeight_Every;
    int downWeight_Every;
    int index;
    boolean isSpeed;
    public HistoggramView(Context context) {
        super(context);
        init();
    }
    public HistoggramView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    /**
     * 更新数据
     */
    public void upDataRec(double[] data, boolean type) {
        for (int i = 0; i < dataRec.length; i++) {
            this.dataRec[i] = data[i];
        }
        isSpeed = type;
        mPaint_Rec.setColor(Color.argb(0xff, 0x33, 0xb5, 0xe5));
        this.invalidate();//更新视图
    }
    /**
     * 更新数据
     */
    public void upDataRec(int[] data, boolean type) {
        for (int i = 0; i < dataRec.length; i++) {
            this.dataRec[i] = data[i];
        }
        isSpeed = type;
        mPaint_Rec.setColor(Color.YELLOW);
        this.invalidate();//更新视图
    }

    /**
     * 初始化画笔
     */
    private void init() {
        mPaint_X = new Paint();
        mPaint_InsideLine = new Paint();
        mPaint_Text = new Paint();
        mPaint_Rec = new Paint();
        mPaint_Y = new Paint();
        mPaint_Line = new Paint();
        mPaint_back = new Paint();
        mPaint_X.setColor(Color.DKGRAY);
        mPaint_X.setStrokeWidth(3);
        mPaint_X.setAntiAlias(true);
        mPaint_X.setDither(true);

        mPaint_Y.setColor(Color.GRAY);
        mPaint_Y.setStrokeWidth(3);
        mPaint_Y.setAntiAlias(true);
        mPaint_Y.setDither(true);

        mPaint_InsideLine.setColor(Color.LTGRAY);
        mPaint_InsideLine.setAntiAlias(true);
        mPaint_InsideLine.setDither(true);

        mPaint_Text.setTextSize(14);
        mPaint_Text.setTextAlign(Paint.Align.CENTER);
        mPaint_Text.setColor(Color.WHITE);
        mPaint_Text.setDither(true);
        mPaint_Text.setAntiAlias(true);

        mPaint_Rec.setColor(Color.YELLOW);
        mPaint_Rec.setDither(true);
        mPaint_Rec.setAntiAlias(true);

        mPaint_Line.setColor(Color.RED);
        mPaint_Line.setStrokeWidth(3);
        mPaint_Line.setDither(true);
        mPaint_Line.setAntiAlias(true);

        mPaint_back.setColor(Color.GRAY);
        mPaint_back.setAlpha(40);
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        width = getWidth();
        height = getHeight();
        leftHeight_Every = height / (int) (isSpeed ? TreadApplication.getInstance().MAXSPEED + 1.5 : TreadApplication.getInstance().SLOPES + 1); //Y轴每个数据的间距
        downWeight_Every = width / (dataRec.length+ 1);//X轴每个数据的间距
//        canvas.drawLine(40, height - 10, width, height - 10, mPaint_X);//画X坐标轴
//        canvas.drawLine(40, 0, width, 0, mPaint_X);//画X坐标轴
//        canvas.drawLine(40, height - 10, 40, 0, mPaint_Y);//画Y坐标轴

//        for (int i = 0; i < mText_Y.length; i++) {//画灰线
//            canvas.drawLine(40, height - 10 - (i * leftHeight_Every), 30, height - 10 - (i * leftHeight_Every), mPaint_InsideLine);
//        }
//        for (int i = 1; i < mText_Y.length + 1; i++) {//画Y轴坐标
//            canvas.drawText(mText_Y[i - 1], 20, leftHeight_Every * i, mPaint_Text);
//        }
        if (dataRec != null && dataRec.length > 0) {
            for (int i = 1; i < dataRec.length + 1; i++) {//画矩形
                RectF rectBack = new RectF();
                rectBack.left = left + downWeight_Every * i;
                rectBack.right = right + downWeight_Every * i;
                rectBack.top = (float) (height - ((isSpeed ? TreadApplication.getInstance().MAXSPEED : TreadApplication.getInstance().SLOPES) * leftHeight_Every));
                rectBack.bottom = height;
                canvas.drawRoundRect(rectBack, 0, 0, mPaint_back);

                RectF rect = new RectF();
                rect.left = left + downWeight_Every * i;
                rect.right = right + downWeight_Every * i;
                rect.top = (float) (height - (dataRec[i - 1] * leftHeight_Every));
                rect.bottom = height;
                canvas.drawRoundRect(rect, 0, 0, mPaint_Rec);

                if (isSpeed)
                    canvas.drawText(format.format(dataRec[i - 1]), text_top + downWeight_Every * i, (height - (int) (dataRec[i - 1] * leftHeight_Every)) - 1, mPaint_Text);
                else
                    canvas.drawText(formats.format(dataRec[i - 1]), text_top + downWeight_Every * i, (height - (int) (dataRec[i - 1] * leftHeight_Every)) - 1, mPaint_Text);
            }
        }
    }

    boolean isClick;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        float leftx;
        float rightx;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                for (int i = 1; i < dataRec.length + 1; i++) {
                    leftx = left + downWeight_Every * i;
                    rightx = right + downWeight_Every * i;
                    if (x < leftx)
                        continue;
                    if (leftx <= x && x <= rightx) {
                        float top = 0;
                        float bottom = height;
                        if (y >= top && y <= bottom) {
                            if (listener != null) { //判断是否设置监听,将点击的第几条柱子,点击柱子顶部的坐值，赋值top
                                top = (float) ((height - y) * (isSpeed ? TreadApplication.getInstance().MAXSPEED : TreadApplication.getInstance().SLOPES) * 10 / height);
                                if (i <= dataRec.length && i >= 1) {
                                    isClick = true;
                                    listener.onClick(i - 1, top / 9);
                                }
                                index = i - 1;
                            }
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (isClick) {
                    float top = 0;
                    float bottom = height ;
                    if (y >= top && y <= bottom) {
                        if (listener != null) { //判断是否设置监听,将点击的第几条柱子,点击柱子顶部的坐值
                            //top是计算出的值
                            top = (float) ((height - y) * (isSpeed ? TreadApplication.getInstance().MAXSPEED : TreadApplication.getInstance().SLOPES) * 10 / height);
                            if (index <= dataRec.length-1 && index >= 0)
                                listener.onClick(index, top / 9);
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                isClick = false;
                index = -1;
                break;
        }
        return true;
    }

    /**
     * 柱子点击时的监听接口
     */
    public interface OnChartClickListener {
        void onClick(int num, float y);//num为下标 y为拉动的值
    }

    /**
     * 设置柱子点击监听的方法
     *
     * @param listener
     */
    public void setOnChartClickListener(OnChartClickListener listener) {
        this.listener = listener;
    }
}