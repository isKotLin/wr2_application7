package com.example.moudle;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vigorchip.treadmill.wr2.R;


/**
 *历史记录模型
 */
public class UserViewHandle {
    public TextView user_tv_data, user_tv_time, user_tv_mileage, user_tv_heat;
    public ImageView user_iv_clear;

    public UserViewHandle(View view) {
        user_tv_data = (TextView) view.findViewById(R.id.user_tv_date);
        user_tv_time = (TextView) view.findViewById(R.id.user_tv_time);
        user_tv_mileage = (TextView) view.findViewById(R.id.user_tv_mileage);
        user_tv_heat = (TextView) view.findViewById(R.id.user_tv_heat);
        user_iv_clear = (ImageView) view.findViewById(R.id.user_iv_clean);
    }
}