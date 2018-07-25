package com.example.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.activity.MainActivity;
import com.example.app.BaseFragment;
import com.vigorchip.treadmill.wr2.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义模式
 */
public class CustomModelFragment extends BaseFragment implements View.OnClickListener {
    View view;

    public void setCurrentIndex(int currentIndexs) {
        currentIndex = currentIndexs;//title左右下标
        showFragment();
    }

    public static int currentIndex = 0;
    private FragmentManager childFragmentManager;
    private Fragment currentFragment = new Fragment();
    private List<Fragment> fragments = new ArrayList<>();
    ImageView left_iv, right_iv;
    TextView custom_tv_show;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_run_custom, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        ((MainActivity) getActivity()).setIsRunModel(true);
        ((MainActivity) getActivity()).setTitles(getResources().getString(R.string.shortcut_fatloss));
        left_iv = (ImageView) view.findViewById(R.id.left_iv);
        right_iv = (ImageView) view.findViewById(R.id.right_iv);
        custom_tv_show = (TextView) view.findViewById(R.id.custom_tv_show);
        custom_tv_show.setText(getResources().getString(R.string.label_r03));
        childFragmentManager = getChildFragmentManager();
        fragments.add(new CustomUser1Fragment());
        fragments.add(new CustomUser2Fragment());
        fragments.add(new CustomUser3Fragment());
        fragments.add(new CustomUser4Fragment());
        fragments.add(new CustomUser5Fragment());
        setCurrentIndex(0);

        left_iv.setOnClickListener(this);
        right_iv.setOnClickListener(this);
    }

    private void showFragment() {
        FragmentTransaction transaction = childFragmentManager.beginTransaction();
        if (!fragments.get(currentIndex).isAdded()) {
            transaction.detach(currentFragment).add(R.id.custom_fl_choose, fragments.get(currentIndex));
        } else {
            transaction.detach(currentFragment);
//                    .attach(fragments.get(currentIndex));
        }
        currentFragment = fragments.get(currentIndex);
        transaction.attach(currentFragment);
        transaction.commit();
    }

    public void saves() {//开始跑步并保存自定义的数据
        ((RunFragment) (CustomModelFragment.this.getParentFragment())).getData(5);
    }

    public static int getCurrentIndex() {
        return currentIndex;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_iv:
                currentIndex--;
                if (currentIndex == -1)
                    currentIndex = 4;
                showFragment();
                break;
            case R.id.right_iv:
                currentIndex++;
                if (currentIndex == 5)
                    currentIndex = 0;
                showFragment();
                break;
        }
        handler.sendEmptyMessage(0);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (currentIndex) {
                case 0:
                    custom_tv_show.setText(getResources().getString(R.string.label_r03));
                    break;
                case 1:
                    custom_tv_show.setText(getResources().getString(R.string.label_r04));
                    break;
                case 2:
                    custom_tv_show.setText(getResources().getString(R.string.level_beginner));
                    break;
                case 3:
                    custom_tv_show.setText(getResources().getString(R.string.level_intermediate));
                    break;
                case 4:
                    custom_tv_show.setText(getResources().getString(R.string.level_advanced));
                    break;
            }
        }
    };
}