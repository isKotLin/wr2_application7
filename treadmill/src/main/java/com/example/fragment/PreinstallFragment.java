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
 * 专业运动
 */
public class PreinstallFragment extends BaseFragment  {
    View view;
    PreinstallColumnarView speed_pcv_show;
    TextView pre_tv_sport;
    double recFirst = 9.0;
    int lineFirst = 3;
    private static double[] dataRec = new double[16];
    private static int[] dataLine = new int[16];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_run_subassembly, container, false);
        initData();
        return view;
    }

    private void initData() {
        speed_pcv_show = (PreinstallColumnarView) view.findViewById(R.id.speed_pcv_show);
        pre_tv_sport= (TextView) view.findViewById(R.id.pre_tv_sport);
        speed_pcv_show.upDataRec(getDataRec(recFirst));
        speed_pcv_show.upDataLine(getDataLine(lineFirst));
        pre_tv_sport.setText(getString(R.string.program_r02));
    }

    private int[] getDataLine(int lineFirst) {
        dataLine[0] = lineFirst;
        dataLine[1] = lineFirst + 3;
        dataLine[2] = lineFirst;
        dataLine[3] = lineFirst + 3;
        dataLine[4] = lineFirst;
        dataLine[5] = lineFirst + 3;
        dataLine[6] = lineFirst;
        dataLine[7] = lineFirst + 3;
        dataLine[8] = lineFirst;
        dataLine[9] = lineFirst + 3;
        dataLine[10] = lineFirst;
        dataLine[11] = lineFirst + 3;
        dataLine[12] = lineFirst;
        dataLine[13] = lineFirst + 3;
        dataLine[14] = lineFirst;
        dataLine[15] = lineFirst - 3;
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
        dataRec[15] = recFirst - 4.0;
        return dataRec;
    }
    public static double[] getSpeeds() {
        return dataRec;
    }

    public static int[] getSlopes() {
        return dataLine;
    }
}
