package com.example.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.activity.MainActivity;
import com.example.app.BaseFragment;
import com.example.app.TreadApplication;
import com.example.service.WindowInfoService;
import com.vigorchip.treadmill.wr2.R;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.VideoView;

/**
 * 视频
 */

public class VideoFragment extends BaseFragment {
    View view;
    private VideoView videoView;
    private static ImageView plays_iv;//private MediaController mMediaController;
    private static MediaPlayer mPlayer;
    Uri[] rawID = {//Uri.parse("/sdcard/VID_20161003_104308.mp4"),
            Uri.parse("/system/treadmill/video_02.mp4"),//Uri.parse("/storage/usbhost1/1111.MOV"),
            Uri.parse("/system/treadmill/video_01.mp4"),
            Uri.parse("/system/treadmill/video_03.mp4"),//Uri.parse("/system/treadmill/video_04.mp4"),Uri.parse("/system/treadmill/video_05.mp4"),Uri.parse("/system/treadmill/video_06.mp4"),Uri.parse("/system/treadmill/video_07.mp4")
    };
    private static float speeds;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_video, container, false);
        return view;
    }

    public static void setMPSpeed(float speed) {
        speeds = speed;
        new Thread() {
            @Override
            public void run() {
                super.run();
                if (TreadApplication.getInstance().isRuning()) {//限制播放速度
                    if (speeds > 2.0f)
                        speeds = 2.0f;
                    else if (speeds < 0.5f)
                        speeds = 0.5f;
                    if (mPlayer != null)
                        mPlayer.setPlaybackSpeed(speeds);
                    handler.removeCallbacks(this);
                }
            }
        }.start();
    }

    public static void plays() {
        if (mPlayer != null) {
            mPlayer.start();
            plays_iv.setVisibility(View.GONE);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            ((MainActivity) getActivity()).setVideo(true);
            ((MainActivity) getActivity()).rl_title.setVisibility(View.GONE);
            videoView = (VideoView) view.findViewById(R.id.videoView);
            plays_iv = (ImageView) view.findViewById(R.id.plays_iv);
//            videoView.getHolder().setFixedSize(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
//        mMediaController = new MediaController(getContext());// videoView.setMediaController(mMediaController);//绑定控制器
//        videoView.setHardwareDecoder(true);
            videoView.setVideoQuality(MediaPlayer.VIDEOQUALITY_HIGH);//设置播放画质 高画质
            videoView.setVideoURI(rawID[SceneFragment.index]);
//            videoView.setVideoPath("http://www.midea.com/video/masvod/public/2015/02/28/20150228_14bcec18032_r1_800k.mp4");
            plays_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    plays_iv.setVisibility(View.GONE);
                    WindowInfoService.startBtn();
                }
            });
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mMediaPlayer) {
                    mPlayer = mMediaPlayer;
                    mMediaPlayer.setPlaybackSpeed(0.5f);
                    if (!TreadApplication.getInstance().isRuning())
                        handler.post(run);
                    else
                        WindowInfoService.setmSpeed(WindowInfoService.getmSpeed());
                }
            });
            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.seekTo(0);
                    mp.start();
                }
            });
        } catch (Exception e) {
            showToast(getString(R.string.play_error));
//            e.printStackTrace();
        }
    }

    private static Handler handler = new Handler();

    private Runnable run = new Runnable() {
        long currentPosition;

        public void run() {
            // 获得当前播放时间和当前视频的长度
            currentPosition = videoView.getCurrentPosition();
            if (currentPosition > 0) {
                mPlayer.pause();
                plays_iv.setVisibility(View.VISIBLE);
                handler.removeCallbacks(run);
            } else
                handler.postDelayed(run, 1);
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        if (videoView != null) {
            try {
                videoView.stopPlayback();
                videoView.suspend();
                ((MainActivity) getActivity()).setVideo(false);
                ((MainActivity) getActivity()).rl_title.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                showToast(getString(R.string.stop_error));
            }
        } else {
            showToast(getString(R.string.isnull));
        }
    }
}
