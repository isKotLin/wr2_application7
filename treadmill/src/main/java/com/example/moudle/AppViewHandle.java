package com.example.moudle;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.view.MyLinearLayout;
import com.vigorchip.treadmill.wr2.R;


public class AppViewHandle {
    public ImageView apps_iv;
    public TextView apps_tv;
    public MyLinearLayout app_ll_item;

    public AppViewHandle(View view) {
        apps_iv = (ImageView) view.findViewById(R.id.apps_iv);
        apps_tv = (TextView) view.findViewById(R.id.apps_tv);
        app_ll_item = (MyLinearLayout) view.findViewById(R.id.app_ll_item);
    }
}