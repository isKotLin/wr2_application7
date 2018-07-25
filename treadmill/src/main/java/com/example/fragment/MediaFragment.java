package com.example.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import com.example.activity.MainActivity;
import com.example.app.BaseFragment;
import com.example.view.MyLinearLayout;
import com.vigorchip.treadmill.wr2.R;

/**
 * 多媒体
 */
public class MediaFragment extends BaseFragment implements View.OnClickListener {
    View view;
   MyLinearLayout media_ll_music, media_ll_video;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_media, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setTitles(getString(R.string.shortcut_movie));
        initData();
    }

    private void initData() {
        media_ll_music = (MyLinearLayout) view.findViewById(R.id.media_ll_music);
        media_ll_video = (MyLinearLayout) view.findViewById(R.id.media_ll_video);
        media_ll_music.setOnClickListener(this);
        media_ll_video.setOnClickListener(this);
    }
    private void into(View v) {
        switch (v.getId()) {
            case R.id.media_ll_music:
                media_ll_music.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.sclae_up_renew));
                try{
                    startActivity(getActivity().getPackageManager().getLaunchIntentForPackage("com.vigorchip.WrMusic.wr2"));
                }catch (Exception e){
                    showToast(getString(R.string.no_install));
//                    e.printStackTrace();
                }
                break;
            case R.id.media_ll_video:
                media_ll_video.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.sclae_up_renew));
                try{
                    startActivity(getActivity().getPackageManager().getLaunchIntentForPackage("com.vigorchip.WrVideo.wr2"));
                }catch (Exception e){
                    showToast(getString(R.string.no_install));
//                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        into(v);
    }
}
