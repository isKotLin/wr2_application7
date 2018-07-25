package com.example.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.app.BaseFragment;
import com.example.view.PreinstallColumnarView;
import com.vigorchip.treadmill.wr2.R;

public class P1Fragment extends BaseFragment {
    View view;
    PreinstallColumnarView speed_pcv_show;
    private static double[] dataRec;
    private static int[] dataLine;
    private TextView pre_tv_sport;

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

    private void initData() {
        dataRec = new double[]{1, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 4, 4, 5, 6, 6, 7, 6, 5, 5, 4, 4, 4, 4, 3, 3, 2, 2, 2, 2};
        dataLine = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        speed_pcv_show = (PreinstallColumnarView) view.findViewById(R.id.speed_pcv_show);
        pre_tv_sport = (TextView) view.findViewById(R.id.pre_tv_sport);
        pre_tv_sport.setText("P01");
        speed_pcv_show.upDataRec(dataRec);
        speed_pcv_show.upDataLine(dataLine);
    }

    public static double[] getSpeeds() {
        return dataRec;
    }

    public static int[] getSlopes() {
        return dataLine;
    }
}