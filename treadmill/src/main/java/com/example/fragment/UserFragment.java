package com.example.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.activity.MainActivity;
import com.example.app.BaseFragment;
import com.vigorchip.treadmill.wr2.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户
 */
public class UserFragment extends BaseFragment  {
//    RadioGroup user_rg_name;
    View view;
    private int currentIndex = 0;
    private FragmentManager childFragmentManager;
    private Fragment currentFragment = new Fragment();
    private List<Fragment> fragments = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();initData();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
            currentIndex = ((MainActivity) getActivity()).getUserIndex();
            showFragment();
        }}

    private void initData() {
        currentIndex = ((MainActivity) getActivity()).getUserIndex();
        childFragmentManager = getChildFragmentManager();
        fragments.add(new User1Fragment());
        fragments.add(new User2Fragment());
        fragments.add(new User3Fragment());
        fragments.add(new User4Fragment());
        fragments.add(new User5Fragment());
        showFragment();
    }

    /**
     * 使用show() hide()切换页面
     * 显示fragment
     */
    private void showFragment() {
        FragmentTransaction transaction = childFragmentManager.beginTransaction();
        if (!fragments.get(currentIndex).isAdded()) { //如果之前没有添加过
            transaction.hide(currentFragment).add(R.id.user_fl_contain, fragments.get(currentIndex));
        } else {
            transaction.hide(currentFragment).show(fragments.get(currentIndex));
        }
        currentFragment = fragments.get(currentIndex);
        transaction.commit();
    }
}