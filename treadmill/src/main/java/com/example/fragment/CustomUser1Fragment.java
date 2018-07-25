package com.example.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.activity.MainActivity;
import com.example.app.BaseFragment;
import com.example.app.TreadApplication;
import com.example.utils.MySharePreferences;
import com.example.view.HistoggramView;
import com.vigorchip.treadmill.wr2.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import wheel.LoopView;

public class CustomUser1Fragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener {
    private View view;
    private RadioGroup selector_rg_ascend;
    private RadioButton selector_rb_sped, selector_rb_slop;
    private HistoggramView custom_hv_data;
    private static MySharePreferences mySharePreferences;
    private static double[] speed;
    private static int[] slopes;
    private boolean isType;
    private static int time;
    private static LoopView wv_ten;
    private static List<String> customTimes;
    LinearLayout custom_ll_start;
    static String userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_custom_user, container, false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        initData();
        TreadApplication.setMediaPlayer(getContext(),R.raw.custom1);
    }

    private void initData() {
        userId = "User" + MainActivity.getUserIndex() + "Custom1";
        mySharePreferences = new MySharePreferences(getContext(), userId);
        speed = mySharePreferences.getSpeedData();
        slopes = mySharePreferences.getSlopesData();
        time = mySharePreferences.getTime();
        selector_rg_ascend = (RadioGroup) view.findViewById(R.id.selector_rg_ascend);
        selector_rb_sped = (RadioButton) view.findViewById(R.id.selector_rb_sped);
        selector_rb_slop = (RadioButton) view.findViewById(R.id.selector_rb_slop);
        custom_hv_data = (HistoggramView) view.findViewById(R.id.custom_hv_data);
        custom_ll_start = (LinearLayout) view.findViewById(R.id.custom_ll_start);

        customTimes = new ArrayList<>();
        customTimes.clear();
        for (int i = 5; i < 100; i++) {
            customTimes.add(i < 10 ? "0" + i : i + "");
        }
        wv_ten = (LoopView) view.findViewById(R.id.wv_ten);
        wv_ten.setInitPosition(time - 5);
        wv_ten.setItems(customTimes);
        wv_ten.setTextSize(60);
        ((RadioButton) selector_rg_ascend.getChildAt(0)).setChecked(true);
        selector_rb_sped.setTextColor(Color.BLACK);
        selector_rg_ascend.setOnCheckedChangeListener(this);
        isType = true;
        custom_hv_data.upDataRec(speed,isType);
        custom_ll_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((CustomModelFragment) (CustomUser1Fragment.this.getParentFragment())).saves();
            }
        });
        custom_hv_data.setOnChartClickListener(new HistoggramView.OnChartClickListener() {
            @Override
            public void onClick(int num, float y) {
                if (isType) {
                    y = (y < TreadApplication.getInstance().MINSPEED) ? (float) TreadApplication.getInstance().MINSPEED : y;
                    y = (y > TreadApplication.getInstance().MAXSPEED) ? (float) TreadApplication.getInstance().MAXSPEED : y;
                    speed[num] = Double.parseDouble(new DecimalFormat("#0.0").format(y));
                    custom_hv_data.upDataRec(speed,isType);
                } else {
                    y = (y < 1) ? 0 : y;
                    y = (y > TreadApplication.getInstance().SLOPES) ? TreadApplication.getInstance().SLOPES : y;
                    slopes[num] = Integer.parseInt(new DecimalFormat("#0").format(y));
                    custom_hv_data.upDataRec(slopes,isType);
                }
            }
        });
        if (TreadApplication.getInstance().SLOPES == 0)
            selector_rb_slop.setVisibility(View.GONE);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.selector_rb_sped:
                selector_rb_sped.setTextColor(Color.BLACK);
                selector_rb_slop.setTextColor(Color.WHITE);
                isType = true;
                custom_hv_data.upDataRec(speed,isType);
                break;
            case R.id.selector_rb_slop:
                selector_rb_sped.setTextColor(Color.WHITE);
                selector_rb_slop.setTextColor(Color.BLACK);
                isType = false;
                custom_hv_data.upDataRec(slopes,isType);
                break;
        }
    }

    public static double[] getmSpeed() {
        return speed;
    }

    public static int[] getmSlopes() {
        return slopes;
    }

    public static int getmTime() {
        return Integer.parseInt(customTimes.get(wv_ten.getSelectedItem())
        );
    }

    public static void setSave() {
        mySharePreferences.setSpeedData(speed);
        mySharePreferences.setSlopesData(slopes);
        mySharePreferences.setTime(Integer.parseInt(customTimes.get(wv_ten.getSelectedItem())
        ));
    }
}