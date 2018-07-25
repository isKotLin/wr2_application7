package com.example.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.activity.MainActivity;
import com.example.app.BaseFragment;
import com.example.app.TreadApplication;
import com.example.utils.Text;
import com.vigorchip.treadmill.wr2.R;

import java.util.ArrayList;
import java.util.List;

import wheel.LoopView;
import wheel.OnItemSelectedListener;


/**
 * 心率控速
 * 1.
 * 默认最小速度跑一分钟，之后每过10秒比对实时心率和目标心率，
 * 如果实时心率大于目标心率，就减速0.5，反之加0.5
 * 2.
 * 如果实时心率为0，速度不变
 */
public class HeartModelFragment extends BaseFragment implements View.OnClickListener {
    static LoopView run_wv_heart_ten;
    static LoopView run_wv_heart;
    static LoopView run_wv_time;
    View view;
    private static List<String> heartAge;//年龄
    private static List<String> times;//时间
    LinearLayout heart_ll_start;
    private static final int TEXT_SIZE = 80;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_run_heart_rate, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
        TreadApplication.setMediaPlayer(getContext(),R.raw.heart);
    }

    private void initData() {
        ((MainActivity) getActivity()).setIsRunModel(true);//判断哪个页面
        ((MainActivity) getActivity()).setTitles(getString(R.string.shortcut_rate));
        heart_ll_start = (LinearLayout) view.findViewById(R.id.heart_ll_start);
        heart_ll_start.setOnClickListener(this);
        run_wv_heart_ten = (LoopView) view.findViewById(R.id.run_wv_heart_ten);
        heartAge = new ArrayList<>();
        for (int i = 15; i <= 80; i++) {//15岁到80岁
            heartAge.add(i + "");
        }

        run_wv_heart_ten.setItems(heartAge);//设置数据
        run_wv_heart_ten.setInitPosition(3);//设置初始位置
        run_wv_heart_ten.setTextSize(TEXT_SIZE);//设置字体大小
        run_wv_heart_ten.setTypeface(TreadApplication.getInstance().font);//字体样式

        run_wv_heart = (LoopView) view.findViewById(R.id.run_wv_heart);
        run_wv_heart.setTextSize(TEXT_SIZE);
        run_wv_heart.setItems(Text.getArray(3));
        run_wv_heart.setInitPosition(Text.getIndex(3));
        run_wv_heart.setNotLoop();

        run_wv_time = (LoopView) view.findViewById(R.id.run_wv_time);
        times = new ArrayList<>();
        for (int i = 5; i < 100; i++) {
            times.add(i < 10 ? "0" + i : i + "");
        }
        run_wv_time.setItems(times);//设置数据
        run_wv_time.setInitPosition(25);//设置初始位置
        run_wv_time.setTextSize(TEXT_SIZE);//设置字体大小
        run_wv_heart_ten.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(final int index) {
                run_wv_heart.setNotLoop();
                /**
                 * run_wv_heart_ten.getSelectedItem()获取当前年龄下标
                 *利用年龄下标判断心率范围
                 */
                run_wv_heart.setItems(Text.getArray(run_wv_heart_ten.getSelectedItem()));
                /**
                 * run_wv_heart_ten.getSelectedItem()获取当前年龄下标
                 *给心率loop设置初始值
                 */
                run_wv_heart.setInitPosition(Text.getIndex(run_wv_heart_ten.getSelectedItem()));
            }
        });

    }

    public static int getHeart() {//将目标心率get出来保存到服务，用来对比实时心率
        return Integer.parseInt(Text.getArray(run_wv_heart_ten.getSelectedItem()).get(run_wv_heart.getSelectedItem()));
    }

    public static int getTimes() {//倒数时间
        return Integer.parseInt(times.get(run_wv_time.getSelectedItem()));
    }

    @Override
    public void onClick(View v) {
        ((RunFragment) (HeartModelFragment.this.getParentFragment())).getData(6);
    }
}