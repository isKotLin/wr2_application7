package com.example.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.activity.MainActivity;
import com.example.app.BaseFragment;
import com.example.utils.FileUtils;
import com.vigorchip.treadmill.wr2.R;

/**
 * 实景
 */
public class SceneFragment extends BaseFragment implements View.OnClickListener {
    View view;
    static int index;
    LinearLayout scene_ll_first, scene_ll_second, scene_ll_third;
    //            , scene_ll_fourth, scene_ll_fifth, scene_ll_sixth, scene_ll_seventh;
    Uri[] rawID = {
//           Uri.parse("/storage/usbhost1/1111.MOV"),
            Uri.parse("/system/treadmill/video_02.mp4"),
            Uri.parse("/system/treadmill/video_01.mp4"),
            Uri.parse("/system/treadmill/video_03.mp4"),
//            Uri.parse("/system/treadmill/video_04.mp4"),
//            Uri.parse("/system/treadmill/video_05.mp4"),
//            Uri.parse("/system/treadmill/video_06.mp4"),
//            Uri.parse("/system/treadmill/video_07.mp4")
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_scene1, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
        initData();
    }

    private void initData() {
        scene_ll_first.setOnClickListener(this);
        scene_ll_second.setOnClickListener(this);
        scene_ll_third.setOnClickListener(this);
//        scene_ll_fourth.setOnClickListener(this);
//        scene_ll_fifth.setOnClickListener(this);
//        scene_ll_sixth.setOnClickListener(this);
//        scene_ll_seventh.setOnClickListener(this);
    }

    private void initView() {
        ((MainActivity) getActivity()).setTitles(getString(R.string.shortcut_scene));
        scene_ll_first = (LinearLayout) view.findViewById(R.id.scene_ll_first);
        scene_ll_second = (LinearLayout) view.findViewById(R.id.scene_ll_second);
        scene_ll_third = (LinearLayout) view.findViewById(R.id.scene_ll_third);
//        scene_ll_fourth = (LinearLayout) view.findViewById(R.id.scene_ll_fourth);
//        scene_ll_fifth = (LinearLayout) view.findViewById(R.id.scene_ll_fifth);
//        scene_ll_sixth = (LinearLayout) view.findViewById(R.id.scene_ll_sixth);
//        scene_ll_seventh = (LinearLayout) view.findViewById(R.id.scene_ll_seventh);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.scene_ll_first:
//                ((MainActivity) getActivity()).setTitles(getResources().getString(R.string.scene_one));
                index = 0;
                break;
            case R.id.scene_ll_second:
//                ((MainActivity) getActivity()).setTitles(getResources().getString(R.string.scene_two));
                index = 1;
                break;
            case R.id.scene_ll_third:
//                ((MainActivity) getActivity()).setTitles(getResources().getString(R.string.scene_three));
                index = 2;
//                break;
//            case R.id.scene_ll_fourth:
////                ((MainActivity) getActivity()).setTitles(getResources().getString(R.string.scene_four));
//                index = 3;
//                break;
//            case R.id.scene_ll_fifth:
////                ((MainActivity) getActivity()).setTitles(getResources().getString(R.string.scene_five));
//                index = 4;
//                break;
//            case R.id.scene_ll_sixth:
////                ((MainActivity) getActivity()).setTitles(getResources().getString(R.string.scene_six));
//                index = 5;
//                break;
//            case R.id.scene_ll_seventh:
////                ((MainActivity) getActivity()).setTitles(getResources().getString(R.string.scene_seven));
//                index = 6;
//                break;
        }
        runnable.run();
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (!FileUtils.isEscit(getContext(), rawID[SceneFragment.index])) {
                Log.e("TAG", rawID[SceneFragment.index] + "");
                showToast(getString(R.string.no_exist));
            }else
                ((MainActivity) getActivity()).setCurrentIndex(9);
        }
    };
}