package com.example.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.app.BaseFragment;
import com.example.view.PreinstallColumnarView;
import com.vigorchip.treadmill.wr2.R;

/**
 * 减脂运动
 */
public class InsanityFragment extends BaseFragment {
    View view;
    PreinstallColumnarView speed_pcv_show;
    double recFirst = 5.0;
    int lineFirst = 6;
    private static double[] dataRec = new double[16];
    private static int[] dataLine = new int[16];
    private TextView pre_tv_sport;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_run_subassembly, container, false);
        initData();
        return view;
    }

    private void initData() {
        speed_pcv_show = (PreinstallColumnarView) view.findViewById(R.id.speed_pcv_show);
        speed_pcv_show.upDataRec(getDataRec(recFirst));
        speed_pcv_show.upDataLine(getDataLine(lineFirst));
        pre_tv_sport = (TextView) view.findViewById(R.id.pre_tv_sport);
        pre_tv_sport.setText(getString(R.string.program_f03));
    }

    private int[] getDataLine(int lineFirst) {
        dataLine[0] = lineFirst - 2;
        dataLine[1] = lineFirst;
        dataLine[2] = lineFirst;
        dataLine[3] = lineFirst;
        dataLine[4] = lineFirst;
        dataLine[5] = lineFirst;
        dataLine[6] = lineFirst;
        dataLine[7] = lineFirst;
        dataLine[8] = lineFirst;
        dataLine[9] = lineFirst;
        dataLine[10] = lineFirst;
        dataLine[11] = lineFirst;
        dataLine[12] = lineFirst;
        dataLine[13] = lineFirst;
        dataLine[14] = lineFirst;
        dataLine[15] = 0;
        return dataLine;
    }

    private double[] getDataRec(double recFirst) {
        dataRec[0] = recFirst - 1.0;
        dataRec[1] = recFirst + 2.0;
        dataRec[2] = recFirst;
        dataRec[3] = recFirst + 2.0;
        dataRec[4] = recFirst;
        dataRec[5] = recFirst + 2.0;
        dataRec[6] = recFirst;
        dataRec[7] = recFirst + 2.0;
        dataRec[8] = recFirst;
        dataRec[9] = recFirst + 2.0;
        dataRec[10] = recFirst;
        dataRec[11] = recFirst + 2.0;
        dataRec[12] = recFirst;
        dataRec[13] = recFirst + 2.0;
        dataRec[14] = recFirst;
        dataRec[15] = recFirst - 2.0;
        return dataRec;
    }

    public static double[] getSpeeds() {
        return dataRec;
    }

    public static int[] getSlopes() {
        return dataLine;
    }
}

