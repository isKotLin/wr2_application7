package com.example.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.activity.MainActivity;
import com.example.app.BaseFragment;
import com.example.app.TreadApplication;
import com.vigorchip.treadmill.wr2.R;

import java.util.ArrayList;
import java.util.List;

import wheel.LoopView;

/**
 * 倒数模式
 */
public class MileageModelFragment extends BaseFragment implements View.OnClickListener, Animation.AnimationListener {
    private  View view;
    private static LoopView mileage_wv_ten, run_wv_time_ten, heat_wv_hundred;
    private LinearLayout down_ll_start;
    private  RelativeLayout times_rl_center;
    private  RelativeLayout mileage_rl_center;
    private  RelativeLayout heat_rl_center;
    private  Animation animation;
    private int mType;//1=时间 2=距离 3=卡路里
    static List<String> listTime;
    static List<String> listMil;
    static List<String> listHeat;
    private static final float TEXT_SIZE = 80;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_run_mileage, container, false);
        return view;}

    @Override
    public void onResume() {
        super.onResume();
        init();
        initDataTime();
        initDataMil();
        initDataHeat();
    }

    private void init() {
        ((MainActivity) getActivity()).setIsRunModel(true);
        ((MainActivity) getActivity()).setTitles(getString(R.string.shortcut_countdown));
        down_ll_start = (LinearLayout) view.findViewById(R.id.down_ll_start);
        down_ll_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mType != 0)
                    onClickStart();
            }
        });


    }

    private void initDataTime() {
        mType = 0;
        times_rl_center = (RelativeLayout) view.findViewById(R.id.times_rl_center);
        run_wv_time_ten = (LoopView) view.findViewById(R.id.run_wv_time_ten);
        times_rl_center.setOnClickListener(this);
        run_wv_time_ten.setVisibility(View.INVISIBLE);
        listTime = new ArrayList<>();
//        run_wv_time_ten.setMax("99");
        for (int i = 5; i < 100; i++) {
            listTime.add(i < 10 ? "0" + i : i + "");
        }
        run_wv_time_ten.setItems(listTime);
        run_wv_time_ten.setInitPosition(25);
        run_wv_time_ten.setTextSize(TEXT_SIZE);//设置字体大小
    }

    private void initDataMil() {
        mileage_rl_center = (RelativeLayout) view.findViewById(R.id.mileage_rl_center);
        mileage_wv_ten = (LoopView) view.findViewById(R.id.mileage_wv_ten);
        listMil = new ArrayList<>();
//        mileage_wv_ten.setMax("99");
        for (int i = 1; i < 100; i++) {
            listMil.add(i < 10 ? "0" + i : i + "");
        }
        mileage_wv_ten.setInitPosition(0);
        mileage_wv_ten.setItems(listMil);
        mileage_rl_center.setOnClickListener(this);
        mileage_wv_ten.setVisibility(View.INVISIBLE);
        mileage_wv_ten.setTextSize(TEXT_SIZE);//设置字体大小
    }

    private void initDataHeat() {
        heat_rl_center = (RelativeLayout) view.findViewById(R.id.heat_rl_center);
        heat_wv_hundred = (LoopView) view.findViewById(R.id.heat_wv_hundred);
        listHeat = new ArrayList<>();
//        heat_wv_hundred.setMax("999");
        for (int i = 10; i < 991; i = i + 10) {
            listHeat.add(i < 100 ? "0" + i : i + "");
        }
        listHeat.add(999 + "");
        heat_wv_hundred.setItems(listHeat);
        heat_wv_hundred.setInitPosition(1);
        heat_rl_center.setOnClickListener(this);
        heat_wv_hundred.setVisibility(View.INVISIBLE);
        heat_wv_hundred.setTextSize(TEXT_SIZE);//设置字体大小
    }

    public void recoveyAnim() {//回复动画，点击一个回复旧的一个
        animation = AnimationUtils.loadAnimation(TreadApplication.getInstance().getApplicationContext(), R.anim.scale_trans_recovery);
        switch (mType) {
            case 1:
                times_rl_center.startAnimation(animation);
                break;
            case 2:
                mileage_rl_center.startAnimation(animation);
                break;
            case 3:
                heat_rl_center.startAnimation(animation);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        recoveyAnim();
        animation = AnimationUtils.loadAnimation(getContext(), R.anim.trans_scale_anim);
        times_rl_center.setClickable(false);
        mileage_rl_center.setClickable(false);
        heat_rl_center.setClickable(false);
        switch (v.getId()) {
            case R.id.times_rl_center:
                TreadApplication.setMediaPlayer(R.raw.time);
                mType = 1;
                TreadApplication.getInstance().setSport(TreadApplication.getInstance().TIME_SETUP_STATUS);
                mileage_wv_ten.setVisibility(View.INVISIBLE);
                heat_wv_hundred.setVisibility(View.INVISIBLE);
                times_rl_center.startAnimation(animation);
                break;
            case R.id.mileage_rl_center:
                TreadApplication.setMediaPlayer(R.raw.distance);
                mType = 2;
                TreadApplication.getInstance().setSport(TreadApplication.getInstance().DISTANCE_SETUP_STATUS);
                run_wv_time_ten.setVisibility(View.INVISIBLE);
                heat_wv_hundred.setVisibility(View.INVISIBLE);
                mileage_rl_center.startAnimation(animation);
                break;
            case R.id.heat_rl_center:
                TreadApplication.setMediaPlayer(R.raw.calorie);
                mType = 3;
                TreadApplication.getInstance().setSport(TreadApplication.getInstance().CALORIE_SETUP_STATUS);
                mileage_wv_ten.setVisibility(View.INVISIBLE);
                run_wv_time_ten.setVisibility(View.INVISIBLE);
                heat_rl_center.startAnimation(animation);
                break;
        }
        animation.setAnimationListener(this);
    }

    public static int getMin() {
        return Integer.parseInt(listTime.get(run_wv_time_ten.getSelectedItem()));
    }

    public static int getMileage() {
        return Integer.parseInt(listMil.get(mileage_wv_ten.getSelectedItem()));
    }

    public static int getHeat() {
        return Integer.parseInt(listHeat.get(heat_wv_hundred.getSelectedItem()));
    }

    @Override
    public void onAnimationStart(Animation animation) {
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        switch (mType) {//设置点击一次后不可点击，动画结束后才可以点击
            case 1:
                run_wv_time_ten.setVisibility(View.VISIBLE);
                heat_rl_center.setClickable(true);
                mileage_rl_center.setClickable(true);
                break;
            case 2:
                mileage_wv_ten.setVisibility(View.VISIBLE);
                times_rl_center.setClickable(true);
                heat_rl_center.setClickable(true);
                break;
            case 3:
                heat_wv_hundred.setVisibility(View.VISIBLE);
                times_rl_center.setClickable(true);
                mileage_rl_center.setClickable(true);
                break;
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
    }

    public void onClickStart() {
        Log.e("Mileage","mType:"+mType);
        ((RunFragment) (MileageModelFragment.this.getParentFragment())).getData(mType);
    }

//    @Override
//    public void onSelect(String text) {
//        switch (mType) {
//            case 1:
//                min = Integer.parseInt(text);
//                break;
//            case 2:
//                mileage = Integer.parseInt(text);
//                break;
//            case 3:
//                heat = Integer.parseInt(text);
//                break;
//        }
//    }
}