package com.example.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.app.TreadApplication;
import com.example.service.WindowInfoService;
import com.vigorchip.treadmill.wr2.R;

import static com.vigorchip.treadmill.wr2.R.id.sl_seekbar;

public class SeekBarHint extends SeekBar implements SeekBar.OnSeekBarChangeListener {
    private int mPopupWidth;
    private int mPopupStyle;
    public static final int POPUP_FIXED = 1;
    public static final int POPUP_FOLLOW = 0;

    private PopupWindow mPopup;
    private TextView mPopupTextView;
    private int mYLocationOffset;
    private float leftText = 0;
    private float rightText = 0;
    private float progressText = 0;
    private float step;
    private OnSeekBarChangeListener mInternalListener;
    private OnSeekBarChangeListener mExternalListener;

    private OnSeekBarHintProgressChangeListener mProgressChangeListener;

    public interface OnSeekBarHintProgressChangeListener {
        String onHintTextChanged(SeekBarHint seekBarHint, float progress);
    }

    public SeekBarHint(Context context) {
        super(context);
        init(context, null);
    }

    public SeekBarHint(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public SeekBarHint(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        setOnSeekBarChangeListener(this);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SeekBarHint);

        mPopupWidth = (int) a.getDimension(R.styleable.SeekBarHint_popupWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        mYLocationOffset = (int) a.getDimension(R.styleable.SeekBarHint_yOffset, 0);
        mPopupStyle = a.getInt(R.styleable.SeekBarHint_popupStyle, POPUP_FOLLOW);

        a.recycle();
    }

    public void setPopupStyle(int style) {
        mPopupStyle = style;
    }

    public int getPopupStyle() {
        return mPopupStyle;
    }

    private void initHintPopup() {
        String popupText = null;

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View undoView = inflater.inflate(R.layout.popup, null);
        mPopupTextView = (TextView) undoView.findViewById(R.id.text);
        if (mProgressChangeListener != null) {
            popupText = mProgressChangeListener.onHintTextChanged(this, cuclaProcess(leftText));
        }

        mPopupTextView.setText(popupText != null ? popupText : String.valueOf(cuclaProcess(leftText)));

        // mPopup.dismiss();
        if (mPopup == null)
            mPopup = new PopupWindow(undoView, mPopupWidth, ViewGroup.LayoutParams.WRAP_CONTENT, false);
        else {
            mPopup.dismiss();
            mPopup = new PopupWindow(undoView, mPopupWidth, ViewGroup.LayoutParams.WRAP_CONTENT, false);

        }
        //}
        // }
        // mPopup.setAnimationStyle(R.style.fade_animation);
        //showPopup();


    }

    public void setLeftText(float str) {
        this.leftText = str / 10;
    }

    public void setRightText(float str) {
        this.rightText = str / 10;
    }

    public void setProgressText(float str) {
        this.progressText = str / 10;
//        invalidate();
    }

    private void showPopup() {

        if (mPopupStyle == POPUP_FOLLOW) {
            mPopup.showAtLocation(this, Gravity.LEFT | Gravity.BOTTOM, (int) (this.getX() + (int) getXPosition(this)), (int) (this.getY() + mYLocationOffset + this.getHeight()));
        }
        if (mPopupStyle == POPUP_FIXED) {
            mPopup.showAtLocation(this, Gravity.CENTER | Gravity.BOTTOM, 0, (int) (this.getY() + mYLocationOffset + this.getHeight()));
        }

    }

    public void initShow() {
        initHintPopup();
        if (isSl)
            this.setMax((int) (rightText - leftText) * 10);
        else
            this.setMax((int) (TreadApplication.getInstance().MAXSPEED * 10 - TreadApplication.getInstance().MINSPEED * 10));
        this.setProgress((int) ((progressText - leftText) * 10));
        mPopup.showAtLocation(this, Gravity.START | Gravity.BOTTOM, (int) (this.getX() + (int) getXPosition(this)), (int) (SeekBarHint.this.getY() + 2 * mYLocationOffset + SeekBarHint.this.getHeight()));
        // mPopupTextView.setText("100");
    }

    private void hidePopup() {
        if (mPopup.isShowing()) {
            mPopup.dismiss();
        }
    }

    public void setHintView(View view) {
        //TODO
        //initHintPopup();
    }

    @Override
    public void setOnSeekBarChangeListener(OnSeekBarChangeListener l) {
        if (mInternalListener == null) {
            mInternalListener = l;
            super.setOnSeekBarChangeListener(l);
        } else {
            mExternalListener = l;
        }
    }

    public void setOnProgressChangeListener(OnSeekBarHintProgressChangeListener l) {
        mProgressChangeListener = l;
    }

    public void setSl(boolean sl) {
        isSl = sl;
    }

    private boolean isSl;
    int slpoes;

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
        Log.e("onProgressChanged", "progress:" + progress);
        String popupText = null;
        if (mProgressChangeListener != null) {
            popupText = mProgressChangeListener.onHintTextChanged(this, cuclaProcess(leftText));
        }

        if (mExternalListener != null) {
            mExternalListener.onProgressChanged(seekBar, progress, b);
        }

        step = cuclaProcess(leftText);
        if (isSl) {
            slpoes = (int) (step * 10 / 10);
            step = slpoes;
            if (mPopupTextView != null) {
                mPopupTextView.setText(popupText != null ? popupText : String.valueOf(slpoes));
            }
        } else if (mPopupTextView != null) {
            mPopupTextView.setText(popupText != null ? popupText : String.valueOf(step));
        }
        Log.e("nidno", "mPopupTextView:" + mPopup);
        if (mPopupStyle == POPUP_FOLLOW && mPopup != null) {
            mPopup.update((int) (this.getX() + (int) getXPosition(seekBar)), (int) (this.getY() + 2 * mYLocationOffset + this.getHeight()), -1, -1);
        }
       /* mRightPopup.update( (int)this.getWidth(), (int) (this.getY()+mYLocationOffset+this.getHeight()),-1,-1);
        
        mPopupRightView.setText(rightText+"");*/
       /* mLeftPopup.update( 0, (int) (this.getY()+mYLocationOffset+this.getHeight()),-1,-1);
        mPopupLeftView.setText(leftText+"");*/

    }

    public float cuclaProcess(float left) {
        return (leftText * 10 + getProgress()) / 10f;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        Log.e("onStartTrackingTouch", "onStartTrackingTouch");
        if (mExternalListener != null) {
            mExternalListener.onStartTrackingTouch(seekBar);
        }
        showPopup();

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        Log.e("onStopTrackingTouch", "onStopTrackingTouch");
        if (mPopupTextView != null&&TreadApplication.getInstance().isRuning()) {
            if (isSl)
                WindowInfoService.setmSlopes(Integer.parseInt(mPopupTextView.getText().toString()));
             else
                WindowInfoService.setmSpeed(Double.parseDouble(mPopupTextView.getText().toString()));
        }
        if (mExternalListener != null) {
            mExternalListener.onStopTrackingTouch(seekBar);
        }
        //hidePopup();
    }


    private float getXPosition(SeekBar seekBar) {
        float val = (((float) seekBar.getProgress() * (float) (seekBar.getWidth() - 2 * seekBar.getThumbOffset())) / seekBar.getMax());
        float offset = seekBar.getThumbOffset() * 2;

        int textWidth = mPopupWidth;
        float textCenter = (textWidth / 1.75f);

        float newX = val + offset - textCenter;

        return newX;
    }
}
