package com.example.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.example.utils.DensityUtils;
import com.vigorchip.treadmill.wr2.R;

/**
 * 点击缩小
 */
public class MyLinearLayout extends LinearLayout implements View.OnTouchListener{
	int width;
	int height;
	boolean isYiChu;
	boolean isAble;
	public OnScaleListener listener;

	public MyLinearLayout(Context context) {
		super(context);
		init();
	}

	public MyLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		this.setOnTouchListener(this);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//		width = getWidth();
//		height = getHeight();
//		int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
//		int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
//		this.measure(w, h);
		height = getMeasuredHeight();
		width = getMeasuredWidth();
//		tvValues.append("方法一: height:"+height + ",width:" + width+"\n");
	}

	public void setOnListenerClicks(OnScaleListener listener) {
		this.listener = listener;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		float x = getX();
		float y = getY();
		float rx = event.getRawX();
		float ry = event.getRawY();
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.scla_click_shrink));
				isAble = true;
				isYiChu = false;
				Log.i("ACTION_DOWN","");
				break;
			case MotionEvent.ACTION_MOVE:
//				433.0
//				159.0
//				497.51416
//				310.4825
//				150
//				233
//				false
//				(rx < x):false
//				(rx > x+width):false
//				(ry < y):false
//				(ry > y + height):false

//				Log.e("TAG", "============================x:" + x);
//				Log.e("TAG", "============================y:" + y);
//				Log.e("TAG", "============================rx:" + rx);
//				Log.e("TAG", "============================ry:" + ry);
//				Log.e("TAG", "============================" + width);
//				Log.e("TAG", "============================" + height);
//				Log.e("TAG", "============================" + (rx < x || rx > x + width || ry < y || ry- DensityUtils.dip2px(getContext(),getResources().getDimension(R.dimen.my60dp)) > y  + height));
//				Log.e("TAG", "============================(rx < x):" + (rx < x));
//				Log.e("TAG", "============================(rx > x+width):" + (rx > x + width));
//				Log.e("TAG", "============================(ry < y):" + (ry < y));
//				Log.e("TAG", "============================(ry > y + height):" + (ry - DensityUtils.dip2px(getContext(), getResources().getDimension(R.dimen.my60dp)) > y + height));
				if (isAble) {
					if (rx < x || rx > x + width || ry < y || ry- DensityUtils.dip2px(getContext(),getResources().getDimension(R.dimen.my60dp)) > y  + height) {
						isAble = false;
						isYiChu = true;//移出范围
						startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.sclae_up_renew));
					} else
						isYiChu = false;//
				}
				Log.i("ACTION_MOVE","");
				break;
			case MotionEvent.ACTION_UP:
				if (!isYiChu) {
					startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.sclae_up_renew));
//					listener.onListenerClicks(this);
				}
				Log.i("ACTION_UP","");
				break;
		}
		return false;
	}

	public interface OnScaleListener {
		void onListenerClicks(View v);
	}
}
