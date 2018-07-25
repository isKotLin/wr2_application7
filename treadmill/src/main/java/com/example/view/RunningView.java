package com.example.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.example.app.TreadApplication;

public class RunningView extends View {
    /**
     * 声明画笔
     */
    private Paint mPaint_X;//X坐标轴画笔
    private Paint mPaint_Y;//Y坐标轴画笔
    private Paint mPaint_InsideLine;//内部虚线P
    private Paint mPaint_Text;//字体画笔
    private Paint mPaint_Rec;//矩形画笔
    private Paint mPaint_Line;

    //矩形数据
    private double[] dataRec;
    //线性数据
    private int[] dataLine;
    //视图的宽高
    private float width;
    private float height;

    int index;

    public RunningView(Context context) {
        super(context);
        init();
    }

    public RunningView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * 更新数据
     */
    public void upDataRec(double[] data, int ind) {
        dataRec = data;
        index = ind;
        postInvalidate();  //更新视图
    }

    public void upDataLine(int[] datas) {
        dataLine = datas;
        this.postInvalidate();
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

        mPaint_X.setColor(Color.DKGRAY);
        mPaint_X.setStrokeWidth(3);
        mPaint_X.setAntiAlias(true);
        mPaint_X.setDither(true);

        mPaint_Y.setColor(Color.argb(110, 12, 77, 218));
        mPaint_Y.setAntiAlias(true);
        mPaint_Y.setDither(true);

        mPaint_InsideLine.setColor(Color.BLACK);
        mPaint_InsideLine.setAntiAlias(true);
        mPaint_InsideLine.setDither(true);

        mPaint_Text.setTextSize(14);
        mPaint_Text.setTextAlign(Paint.Align.CENTER);
        mPaint_Text.setColor(Color.WHITE);
        mPaint_Text.setAntiAlias(true);
        mPaint_Text.setDither(true);

        mPaint_Rec.setColor(Color.YELLOW);
        mPaint_Rec.setAntiAlias(true);
        mPaint_Rec.setDither(true);

        mPaint_Line.setColor(Color.RED);
        mPaint_Line.setStrokeWidth(3);
        mPaint_Line.setAntiAlias(true);
        mPaint_Line.setDither(true);
    }

    static boolean isShan;

    public void setIsShan(boolean b) {
        isShan = b;
    }

    float lineHeight_Event;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        width = getWidth();
        height = getHeight();
        height = (int) (height * 0.95);
        double leftHeight_Every = height / TreadApplication.getInstance().MAXSPEED; //Y轴每个数据的间距
        if (TreadApplication.getInstance().SLOPES != 0)
            lineHeight_Event = height / TreadApplication.getInstance().SLOPES;

        if (dataRec != null && dataRec.length > 0) {
            float downWeight_Every = width / (dataRec.length+1);//X轴每个数据的间距
            for (int i = 0; i < dataRec.length; i++) { //画矩形
                RectF rectGary = new RectF(
                        downWeight_Every * (i + 0.5f),
                        0,
                        (downWeight_Every * (i + 1.15f)),
                        height);
                canvas.drawRoundRect(rectGary, 0, 0, mPaint_Y);
                if (i == index) {
                    if (isShan)
                        mPaint_Rec.setColor(Color.YELLOW);
                    else
                        mPaint_Rec.setColor(Color.argb(0, 0, 0, 0));
                } else if (i < index)
                    mPaint_Rec.setColor(Color.argb(255, 196, 196, 196));
                else
                    mPaint_Rec.setColor(Color.YELLOW);
                RectF rect = new RectF(
                        downWeight_Every * (i + 0.5f),
                        (float) (height - (dataRec[i]) * leftHeight_Every),
                        (downWeight_Every * (i + 1.15f)),
                        height);
                canvas.drawRoundRect(rect, 0, 0, mPaint_Rec);

            }
        }
        if (TreadApplication.getInstance().SLOPES != 0) {
            if (dataLine != null && dataLine.length > 0) {
                float downWeight_Every = width / (dataLine.length+1);//X轴每个数据的间距
                for (int i = 0; i < dataLine.length; i++) {
                    canvas.drawLine(downWeight_Every * (i + 0.33f),
                            height - (dataLine[i] * lineHeight_Event),
                            (downWeight_Every * (i + 1.33f)),
                            height - (dataLine[i] * lineHeight_Event), mPaint_Line);
                    if (i != dataLine.length-1) {
                        canvas.drawLine((downWeight_Every * (i + 1.33f)),
                                height - (dataLine[i] * lineHeight_Event),
                                downWeight_Every * (i + 1.33f),
                                height - (dataLine[i + 1] * lineHeight_Event), mPaint_Line);
                    }
                }
            }
        }
    }
}