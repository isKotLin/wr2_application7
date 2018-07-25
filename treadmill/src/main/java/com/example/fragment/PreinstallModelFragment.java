package com.example.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.activity.MainActivity;
import com.example.adapter.MyFragmentPagerAdapter;
import com.example.app.BaseFragment;
import com.example.app.TreadApplication;
import com.vigorchip.treadmill.wr2.R;

import java.util.ArrayList;
import java.util.List;

import wheel.LoopView;


/**
 * 预设程式
 */
public class PreinstallModelFragment extends BaseFragment implements View.OnClickListener, ViewPager.OnPageChangeListener {
    View view;
    static List<Fragment> fragments;
    private static LoopView pre_wv_ten;
    private static int time;
    private static List<String> times;
    private static ViewPager pre_vp;
    private static LinearLayout pre_ll_start;
    //    private RelativeLayout llPointGroup;// 点的父元素  
    private ViewPager.OnPageChangeListener listener;
    //    RelativeLayout.LayoutParams params;
    static MyFragmentPagerAdapter adapter;
    static int currIndex;

    //    ImageView pointGARY;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        showLogE(System.currentTimeMillis() + "");
        view = inflater.inflate(R.layout.fragment_run_preinstall, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
        ((MainActivity) getActivity()).setIsRunModel(true);
        ((MainActivity) getActivity()).setTitles(getResources().getString(R.string.shortcut_program));
    }

    private void setFrag() {
        fragments.add(new P64Fragment());
        fragments.add(new P1Fragment());
        fragments.add(new P2Fragment());
        fragments.add(new P3Fragment());
        fragments.add(new P4Fragment());
        fragments.add(new P5Fragment());
        fragments.add(new P6Fragment());
        fragments.add(new P7Fragment());
        fragments.add(new P8Fragment());
        fragments.add(new P9Fragment());
        fragments.add(new P10Fragment());
        fragments.add(new P11Fragment());
        fragments.add(new P12Fragment());
        fragments.add(new P13Fragment());
        fragments.add(new P14Fragment());
        fragments.add(new P15Fragment());
        fragments.add(new P16Fragment());
        fragments.add(new P17Fragment());
        fragments.add(new P18Fragment());
        fragments.add(new P19Fragment());
        fragments.add(new P20Fragment());
        fragments.add(new P21Fragment());
        fragments.add(new P22Fragment());
        fragments.add(new P23Fragment());
        fragments.add(new P24Fragment());
        fragments.add(new P25Fragment());
        fragments.add(new P26Fragment());
        fragments.add(new P27Fragment());
        fragments.add(new P28Fragment());
        fragments.add(new P29Fragment());
        fragments.add(new P30Fragment());
        fragments.add(new P31Fragment());
        fragments.add(new P32Fragment());
        fragments.add(new P33Fragment());
        fragments.add(new P34Fragment());
        fragments.add(new P35Fragment());
        fragments.add(new P36Fragment());
        fragments.add(new P37Fragment());
        fragments.add(new P38Fragment());
        fragments.add(new P39Fragment());
        fragments.add(new P40Fragment());
        fragments.add(new P41Fragment());
        fragments.add(new P42Fragment());
        fragments.add(new P43Fragment());
        fragments.add(new P44Fragment());
        fragments.add(new P45Fragment());
        fragments.add(new P46Fragment());
        fragments.add(new P47Fragment());
        fragments.add(new P48Fragment());
        fragments.add(new P49Fragment());
        fragments.add(new P50Fragment());
        fragments.add(new P51Fragment());
        fragments.add(new P52Fragment());
        fragments.add(new P53Fragment());
        fragments.add(new P54Fragment());
        fragments.add(new P55Fragment());
        fragments.add(new P56Fragment());
        fragments.add(new P57Fragment());
        fragments.add(new P58Fragment());
        fragments.add(new P59Fragment());
        fragments.add(new P60Fragment());
        fragments.add(new P61Fragment());
        fragments.add(new P62Fragment());
        fragments.add(new P63Fragment());
        fragments.add(new P64Fragment());
        fragments.add(new P1Fragment());
    }

    private void initData() {
        currIndex = TreadApplication.getInstance().getPreIndex();
        fragments = new ArrayList<>();
        setFrag();
//        fragments.add(new PreinstallFragment());
//        fragments.add(new WarmUpFragment());
//        fragments.add(new AerobiesFragment());
//        fragments.add(new InsanityFragment());
//        fragments.add(new HealthCareFragment());
//        fragments.add(new ClimbFragment());
//        fragments.add(new PreinstallFragment());
//        fragments.add(new WarmUpFragment());
        adapter = new MyFragmentPagerAdapter(getChildFragmentManager(), fragments);
        ((MainActivity) getActivity()).setTitles(getResources().getString(R.string.shortcut_program));
        times = new ArrayList<>();
        for (int i = 5; i < 100; i++) {
            times.add(i < 10 ? "0" + i : i + "");
        }
        pre_ll_start = (LinearLayout) view.findViewById(R.id.pre_ll_start);
//        llPointGroup = (RelativeLayout) view.findViewById(R.id.llPointGroup);
        pre_ll_start.setOnClickListener(this);
        pre_wv_ten = (LoopView) view.findViewById(R.id.pre_wv_ten);
        pre_wv_ten.setTextSize(80);
        pre_wv_ten.setItems(times);
        pre_wv_ten.setInitPosition(25);
        pre_vp = (ViewPager) view.findViewById(R.id.pre_vp);
        pre_vp.setAdapter(adapter);
        pre_vp.setCurrentItem(currIndex);
        pre_vp.setOffscreenPageLimit(5);
        pre_vp.setOnPageChangeListener(this);
        if (TreadApplication.getInstance().getSport()==TreadApplication.getInstance().PROGRAM_SETUP_STATUS)
        TreadApplication.setMediaPlayer(R.raw.p1);
//        pointGARY = (ImageView) view.findViewById(pointGARY);
//        params = new RelativeLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.my10dp), getResources().getDimensionPixelSize(R.dimen.my10dp));
//        params.leftMargin = (getResources().getDimensionPixelSize(R.dimen.my10dp) * 2 * (currIndex - 1));
//        pointGARY.setLayoutParams(params);
    }

    public static int getTimes() {
        time = Integer.parseInt(times.get(pre_wv_ten.getSelectedItem()));
        return time;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (null != listener) {
            listener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
//        params.leftMargin = (getResources().getDimensionPixelSize(R.dimen.my10dp) * 2 * (position - 1));
//        if (position != 7)
//            pointGARY.setLayoutParams(params);
        TreadApplication.getInstance().setPreIndex(position);

    }

    @Override
    public void onPageSelected(int position) {//position表示在扩展Fragments中即将要显示的Fragment的索引
        if (position == 0) {//首位扩展的item
            //延迟执行才能看到动画
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    pre_vp.setCurrentItem(fragments.size() - 2, false);//跳转到末位
                }
            }, 500);
        } else if (position == fragments.size() - 1) {//末位扩展的item
            //延迟执行才能看到动画
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    pre_vp.setCurrentItem(1, false);//跳转到首位
                }
            }, 500);
        }

        Log.i("MediaPosition", String.valueOf(position));
        if (position == 1){
            TreadApplication.setMediaPlayer(R.raw.p1);
        } else if (position == 2) {
            TreadApplication.setMediaPlayer(R.raw.p2);
        } else if (position == 3) {
            TreadApplication.setMediaPlayer(R.raw.p3);
        } else if (position == 4) {
            TreadApplication.setMediaPlayer(R.raw.p4);
        } else if (position == 5) {
            TreadApplication.setMediaPlayer(R.raw.p5);
        } else if (position == 6) {
            TreadApplication.setMediaPlayer(R.raw.p6);
        } else if (position == 7) {
            TreadApplication.setMediaPlayer(R.raw.p7);
        } else if (position == 8) {
            TreadApplication.setMediaPlayer(R.raw.p8);
        } else if (position == 9) {
            TreadApplication.setMediaPlayer(R.raw.p9);
        } else if (position == 10) {
            TreadApplication.setMediaPlayer(R.raw.p10);
        } else if (position == 11) {
            TreadApplication.setMediaPlayer(R.raw.p11);
        } else if (position == 12) {
            TreadApplication.setMediaPlayer(R.raw.p12);
        } else if (position == 13) {
            TreadApplication.setMediaPlayer(R.raw.p13);
        } else if (position == 14) {
            TreadApplication.setMediaPlayer(R.raw.p14);
        } else if (position == 15) {
            TreadApplication.setMediaPlayer(R.raw.p15);
        } else if (position == 16) {
            TreadApplication.setMediaPlayer(R.raw.p16);
        } else if (position == 17) {
            TreadApplication.setMediaPlayer(R.raw.p17);
        } else if (position == 18) {
            TreadApplication.setMediaPlayer(R.raw.p18);
        } else if (position == 19) {
            TreadApplication.setMediaPlayer(R.raw.p19);
        } else if (position == 20) {
            TreadApplication.setMediaPlayer(R.raw.p20);
        } else if (position == 21) {
            TreadApplication.setMediaPlayer(R.raw.p21);
        } else if (position == 22) {
            TreadApplication.setMediaPlayer(R.raw.p22);
        } else if (position == 23) {
            TreadApplication.setMediaPlayer(R.raw.p23);
        } else if (position == 24) {
            TreadApplication.setMediaPlayer(R.raw.p24);
        } else if (position == 25) {
            TreadApplication.setMediaPlayer(R.raw.p25);
        } else if (position == 26) {
            TreadApplication.setMediaPlayer(R.raw.p26);
        } else if (position == 27) {
            TreadApplication.setMediaPlayer(R.raw.p27);
            Log.i("setMediaPlayer", String.valueOf(position));
        } else if (position == 28) {
            TreadApplication.setMediaPlayer(R.raw.p28);
            Log.i("setMediaPlayer", String.valueOf(position));
        } else if (position == 29) {
            TreadApplication.setMediaPlayer(R.raw.p29);
            Log.i("setMediaPlayer", String.valueOf(position));
        } else if (position == 30) {
            Log.i("setMediaPlayer", String.valueOf(position));
            TreadApplication.setMediaPlayer(R.raw.p30);
        } else if (position == 31) {
            Log.i("setMediaPlayer", String.valueOf(position));
            TreadApplication.setMediaPlayer(R.raw.p31);
        } else if (position == 32) {
            TreadApplication.setMediaPlayer(R.raw.p32);
        } else if (position == 33) {
            TreadApplication.setMediaPlayer(R.raw.p33);
        } else if (position == 34) {
            TreadApplication.setMediaPlayer(R.raw.p34);
        } else if (position == 35) {
            TreadApplication.setMediaPlayer(R.raw.p35);
        } else if (position == 36) {
            TreadApplication.setMediaPlayer(R.raw.p36);
        } else if (position == 37) {
            TreadApplication.setMediaPlayer(R.raw.p37);
        } else if (position == 38) {
            TreadApplication.setMediaPlayer(R.raw.p38);
        } else if (position == 39) {
            TreadApplication.setMediaPlayer(R.raw.p39);
        } else if (position == 40) {
            TreadApplication.setMediaPlayer(R.raw.p40);
        } else if (position == 41) {
            TreadApplication.setMediaPlayer(R.raw.p41);
        } else if (position == 42) {
            TreadApplication.setMediaPlayer(R.raw.p42);
        } else if (position == 43) {
            TreadApplication.setMediaPlayer(R.raw.p43);
        } else if (position == 44) {
            TreadApplication.setMediaPlayer(R.raw.p44);
        } else if (position == 45) {
            TreadApplication.setMediaPlayer(R.raw.p45);
        } else if (position == 46) {
            TreadApplication.setMediaPlayer(R.raw.p46);
        } else if (position == 47) {
            TreadApplication.setMediaPlayer(R.raw.p47);
        } else if (position == 48) {
            TreadApplication.setMediaPlayer(R.raw.p48);
        } else if (position == 49) {
            TreadApplication.setMediaPlayer(R.raw.p49);
        } else if (position == 50) {
            TreadApplication.setMediaPlayer(R.raw.p50);
        } else if (position == 51) {
            TreadApplication.setMediaPlayer(R.raw.p51);
        } else if (position == 52) {
            TreadApplication.setMediaPlayer(R.raw.p52);
        } else if (position == 53) {
            TreadApplication.setMediaPlayer(R.raw.p53);
        } else if (position == 54) {
            TreadApplication.setMediaPlayer(R.raw.p54);
        } else if (position == 55) {
            TreadApplication.setMediaPlayer(R.raw.p55);
        } else if (position == 56) {
            TreadApplication.setMediaPlayer(R.raw.p56);
        } else if (position == 57) {
            TreadApplication.setMediaPlayer(R.raw.p57);
        } else if (position == 58) {
            TreadApplication.setMediaPlayer(R.raw.p58);
        } else if (position == 59) {
            TreadApplication.setMediaPlayer(R.raw.p59);
        } else if (position == 60) {
            TreadApplication.setMediaPlayer(R.raw.p60);
        } else if (position == 61) {
            TreadApplication.setMediaPlayer(R.raw.p61);
        } else if (position == 62) {
            TreadApplication.setMediaPlayer(R.raw.p62);
        } else if (position == 63) {
            TreadApplication.setMediaPlayer(R.raw.p63);
        } else if (position == 64) {
            TreadApplication.setMediaPlayer(R.raw.p64);
        }

        if (null != listener) {
            listener.onPageSelected(position);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (null != listener) {
            listener.onPageScrollStateChanged(state);
        }
    }

    /**
     * 通过扩展fragments中元素的索引来计算对应扩展前fragments集合中的索引
     *
     * @param broadenPosition 扩展fragments中的position
     * @param size            扩展前fragments的大小，等于导航条的大小，等于扩展后fragments的大小减2
     * @return 标准fragments集合中的position
     */
    protected int getRealPosition(int broadenPosition, int size) {
        int position;
        if (broadenPosition == 0) {
            position = size - 1;
        } else if (broadenPosition == size + 1) {
            position = 0;
        } else {
            position = broadenPosition - 1;
        }

        return position;
    }

    /**
     * 给ViewPager设置OnPageChangeListener监听器
     */
    protected void setOnPageChangeListener(ViewPager.OnPageChangeListener l) {
        listener = l;
    }

    @Override
    public void onClick(View v) {
        ((RunFragment) (PreinstallModelFragment.this.getParentFragment())).getData(4);
    }

    public static int getCurrindex() {
        return pre_vp.getCurrentItem();
    }
}