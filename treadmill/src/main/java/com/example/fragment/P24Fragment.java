package com.example.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.app.BaseFragment;
import com.example.view.PreinstallColumnarView;
import com.vigorchip.treadmill.wr2.R;

public class P24Fragment extends BaseFragment {
    private static double[] dataRec;
    private static int[] dataLine;
    private void initData() {
        dataRec = new double[]{4, 6, 7, 8, 9, 11, 11, 12, 12, 13, 13, 14, 14, 14, 14, 14, 14, 14, 14, 13, 13, 12, 12, 11, 10, 9, 8, 7, 6, 4};
        dataLine = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        speed_pcv_show = (PreinstallColumnarView) view.findViewById(R.id.speed_pcv_show);
        pre_tv_sport = (TextView) view.findViewById(R.id.pre_tv_sport);
        pre_tv_sport.setText("P24");
        speed_pcv_show.upDataRec(dataRec);
        speed_pcv_show.upDataLine(dataLine);
    }
    private TextView pre_tv_sport;
    View view;
    PreinstallColumnarView speed_pcv_show;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_run_subassembly, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    public static double[] getSpeeds() {
        return dataRec;
    }

    public static int[] getSlopes() {
        return dataLine;
    }
}