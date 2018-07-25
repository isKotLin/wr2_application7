package com.example.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.example.app.TreadApplication;


public class PreinstallColumnarView extends View {
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

    public PreinstallColumnarView(Context context) {
        super(context);
        init();
    }

    public PreinstallColumnarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * 更新数据
     */
    public void upDataRec(double[] data) {
        this.dataRec = data;
        this.postInvalidate();  //更新视图
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

    float lineHeight_Event;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        width = getWidth();
        height = (int) (getHeight() * 0.95);
        double leftHeight_Every = height / TreadApplication.getInstance().MAXSPEED; //Y轴每个数据的间距
        if (TreadApplication.getInstance().SLOPES != 0)
            lineHeight_Event = height / TreadApplication.getInstance().SLOPES;
        float downWeight_Every = width / (dataRec.length+1);//X轴每个数据的间距
        if (dataRec != null && dataRec.length > 0) {
            for (int i = 0; i < dataRec.length; i++) { //画矩形
                RectF rect = new RectF(
                        downWeight_Every * (i + 0.5f),
                        (float) (height - (dataRec[i]) * leftHeight_Every),
                        (downWeight_Every * (i + 1.4f)),
                        height);
                canvas.drawRoundRect(rect, 0, 0, mPaint_Rec);
            }
        }
        if (TreadApplication.getInstance().SLOPES != 0) {
            if (dataLine != null && dataLine.length > 0) {
                for (int i = 0; i < dataLine.length; i++) {//画线
                    canvas.drawLine(downWeight_Every * (i + 0.5f),
                            height - (dataLine[i] * lineHeight_Event),
                            (downWeight_Every * (i + 1.5f)),
                            height - (dataLine[i] * lineHeight_Event), mPaint_Line);
                    if (i != dataLine.length - 1) {
                        canvas.drawLine((downWeight_Every * (i + 1.5f)),
                                height - (dataLine[i] * lineHeight_Event),
                                downWeight_Every * (i + 1.5f),
                                height - (dataLine[i + 1] * lineHeight_Event), mPaint_Line);
                    }
                }
            }
        }
    }
}

//    /**
//     * 声明画笔
//     */
//    private Paint mPaint_X;//X坐标轴画笔
//    private Paint mPaint_Y;//Y坐标轴画笔
//    private Paint mPaint_InsideLine;//内部虚线P
//    private Paint mPaint_Text;//字体画笔
//    private Paint mPaint_Rec;//矩形画笔
//    private Paint mPaint_Line;
//
//    //矩形数据
//    private double[] dataRec;
//    //线性数据
//    private int[] dataLine;
//    //视图的宽高
//    private int width;
//    private int height;
//
//    //坐标轴数据
//    private String[] mText_Y = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16"};
//    private String[] mText_X = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16"};
//
//    public PreinstallColumnarView(Context context) {
//        super(context);
//        init();
//    }
//
//    public PreinstallColumnarView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        init();
//    }
//
//    /**
//     * 更新数据
//     */
//    public void upDataRec(double[] data) {
//        this.dataRec = data;
//        this.postInvalidate();  //更新视图
//    }
//
//    public void upDataLine(int[] datas) {
//        dataLine = datas;
//        this.postInvalidate();
//    }
//
//    /**
//     * 初始化画笔
//     */
//    private void init() {
//        mPaint_X = new Paint();
//        mPaint_InsideLine = new Paint();
//        mPaint_Text = new Paint();
//        mPaint_Rec = new Paint();
//        mPaint_Y = new Paint();
//        mPaint_Line = new Paint();
//
//        mPaint_X.setColor(Color.DKGRAY);
//        mPaint_X.setStrokeWidth(3);
//
//        mPaint_Y.setColor(Color.GRAY);
//        mPaint_Y.setStrokeWidth(3);
//
//        mPaint_InsideLine.setColor(Color.LTGRAY);
//        mPaint_InsideLine.setAntiAlias(true);
//
//        mPaint_Text.setTextSize(14);
//        mPaint_Text.setTextAlign(Paint.Align.CENTER);
//        mPaint_Text.setColor(Color.WHITE);
//
//        mPaint_Rec.setColor(Color.YELLOW);
//
//        mPaint_Line.setColor(Color.RED);
//        mPaint_Line.setStrokeWidth(3);
//    }
//
//    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//        width = getWidth();
//        height = getHeight();
//        int leftHeight_Every = height / (int) (TreadApplication.getInstance().MAXSPEED>TreadApplication.getInstance().SLOPES?
//                TreadApplication.getInstance().MAXSPEED+ 1.5:TreadApplication.getInstance().SLOPES);//Y轴每个数据的间距
//        int downWeight_Every = (int) (width / (mText_X.length + 0.5));//X轴每个数据的间距
//
//        if (dataRec != null && dataRec.length > 0) {
//            for (int i = 0; i < dataRec.length; i++) {//画矩形
//                RectF rect = new RectF(downWeight_Every * (i + 1f) + 20f, (float) (height - (dataRec[i] * leftHeight_Every + 10f)), (float) (downWeight_Every * (i + 1.65f)) + 26f, height);
//                canvas.drawRoundRect(rect, 0, 0, mPaint_Rec);
//            }
//        }
//        if (TreadApplication.getInstance().SLOPES != 0) {
//            if (dataLine != null && dataLine.length > 0) {
//                for (int i = 0; i < dataLine.length; i++) {
//                    canvas.drawLine(downWeight_Every * i + 47f, (height - (dataLine[i] * leftHeight_Every)-2f),  (60f + downWeight_Every * (i + 0.60f)), (height - (dataLine[i] * leftHeight_Every)-2f), mPaint_Line);
//                    if (i != 15) {
//                        canvas.drawLine( (60f + downWeight_Every * (i + 0.6f)), (height - (dataLine[i] * leftHeight_Every)-2f), (47f + downWeight_Every * (i + 1f)), (height - (dataLine[i + 1] * leftHeight_Every)-2f), mPaint_Line);
//                    }
//                }
//            }
//        }
//    }
//}