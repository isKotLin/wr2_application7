package com.example.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.activity.MainActivity;
import com.example.app.BaseFragment;
import com.example.app.TreadApplication;
import com.example.bean.MyUserInfo;
import com.example.service.WindowInfoService;
import com.example.utils.MySharePreferences;
import com.example.utils.NowDateTime;
import com.vigorchip.treadmill.wr2.R;

/**
 * 分享  http://blog.csdn.net/buptlzx/article/details/9767203
 */
public class ShareFragment extends BaseFragment {
    WindowInfoService windowInfoService;
    View view;
    TextView score_tv_time, score_tv_mileage, score_tv_heat, avg_tv_heart, avg_tv_velocity, avg_tv_slopes;
    MySharePreferences mySharePreferences, myShare;
    MyUserInfo myUserInfo;
    String str;
//    ImageView iv_QRCodes;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_share, container, false);
        Log.e("TAG", "oOOOOOOOOOOOOOOOOOOOOOOO");
        str = "oOOOOOOOOOOOOOOOOOOOOOOO";
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (TreadApplication.getInstance().isShare()) {
            initData();
            initView();
        }
    }

    private void initData() {
        Log.e("TAG", str);
        TreadApplication.getInstance().setIsShare(false);
        ((MainActivity) getActivity()).setTitles(getString(R.string.run_record));
        score_tv_time = (TextView) view.findViewById(R.id.score_tv_time);
        score_tv_mileage = (TextView) view.findViewById(R.id.score_tv_mileage);
        score_tv_heat = (TextView) view.findViewById(R.id.score_tv_heat);
        avg_tv_heart = (TextView) view.findViewById(R.id.avg_tv_heart);
        avg_tv_velocity = (TextView) view.findViewById(R.id.avg_tv_velocity);
        avg_tv_slopes = (TextView) view.findViewById(R.id.avg_tv_slopes);
//        iv_QRCodes = (ImageView) view.findViewById(R.id.iv_QRCodes);
        windowInfoService = new WindowInfoService();
        mySharePreferences = new MySharePreferences(getContext(), "user" + (MainActivity.getUserIndex() + 1));
        myShare = new MySharePreferences(getContext(), "else");
        myUserInfo = new MyUserInfo(getContext(), "user" + (MainActivity.getUserIndex() + 1));
    }

    private void initView() {
        score_tv_time.setText(windowInfoService.getTimesss());
        score_tv_heat.setText(windowInfoService.getHeatsss() + "");
        score_tv_mileage.setText(windowInfoService.getMilsss());
        avg_tv_velocity.setText(windowInfoService.getAvgSpeed());
        avg_tv_heart.setText(windowInfoService.getAvgHeart());
        avg_tv_slopes.setText(windowInfoService.getPace());
        myUserInfo.insert(NowDateTime.getInstance().getDate(), windowInfoService.getTimesss(), Double.parseDouble(windowInfoService.getMilsss()), windowInfoService.getHeatsss());
        windowInfoService.setMileageSum(0);
        windowInfoService.setHeat(0);
//        try {
//            iv_QRCodes.setImageBitmap(EncodingUtils.createQRCode("https://www.baidu.com/", DensityUtils.px2dip(getContext(),150)));
//        } catch (WriterException e) {
//            e.printStackTrace();
//        }
        Log.e("TAG", str);
    }
}