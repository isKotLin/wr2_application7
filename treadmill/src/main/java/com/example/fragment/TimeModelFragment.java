package com.example.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import com.example.activity.MainActivity;
import com.example.app.BaseFragment;
import com.example.app.TreadApplication;
import com.example.view.MyLinearLayout;
import com.vigorchip.treadmill.wr2.R;

/**
 * 选择模式页面的fragment
 */
public class TimeModelFragment extends BaseFragment implements View.OnClickListener {
    private View view;
    private MyLinearLayout manual_ll_mode, custdown_ll_model, pre_ll_tall, custom_ll, heart_ll_rate;
    static RunFragment runFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_run_time, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
    }

    private void initView() {
        ((MainActivity) getActivity()).setIsRunModel(false);
        runFragment = ((RunFragment) (TimeModelFragment.this.getParentFragment()));
        ((MainActivity) getActivity()).setTitles(getString(R.string.shortcut_run));
        manual_ll_mode = (MyLinearLayout) view.findViewById(R.id.manual_ll_mode);
        custdown_ll_model = (MyLinearLayout) view.findViewById(R.id.custdown_ll_model);
        pre_ll_tall = (MyLinearLayout) view.findViewById(R.id.pre_ll_tall);
        custom_ll = (MyLinearLayout) view.findViewById(R.id.custom_ll);
        heart_ll_rate = (MyLinearLayout) view.findViewById(R.id.heart_ll_rate);
        manual_ll_mode.setOnClickListener(this);
        custdown_ll_model.setOnClickListener(this);
        pre_ll_tall.setOnClickListener(this);
        custom_ll.setOnClickListener(this);
        heart_ll_rate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.manual_ll_mode:
                manual_ll_mode.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.sclae_up_renew));
                runFragment.getData(0);
                break;
            case R.id.custdown_ll_model:
                runFragment.setCurrentIndex(1);
                break;
            case R.id.pre_ll_tall:
                TreadApplication.getInstance().setSport(TreadApplication.getInstance().PROGRAM_SETUP_STATUS);
                runFragment.setCurrentIndex(2);
                break;
            case R.id.custom_ll:
                TreadApplication.getInstance().setSport(TreadApplication.getInstance().USER_SETUP_STATUS);
                runFragment.setCurrentIndex(3);
                break;
            case R.id.heart_ll_rate:
                TreadApplication.getInstance().setSport(TreadApplication.getInstance().HRC_SETUP_STATUS);
                runFragment.setCurrentIndex(4);
                break;
        }
    }
}