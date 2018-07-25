package com.example.app;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.activity.MainActivity;
import com.example.adapter.UserHistoryAdapter;
import com.example.bean.MyUserInfo;
import com.example.bean.User;
import com.example.dialog.DialogUserInfo;
import com.example.utils.MySharePreferences;
import com.vigorchip.treadmill.wr2.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户信息模板
 */
public abstract class UserTextFragment extends BaseFragment implements View.OnClickListener {
    View view;
    ImageView user_iv_icon;
    LinearLayout user_ll_cancellation, user_ll_edit;
    TextView user_tv_name, user_tv_age, user_tv_height, user_tv_weight, user_tv_loading;
    ListView history_lv_user;
    UserHistoryAdapter userHistoryAdapter;
    List<User> users;
    List<User> list;
    MyUserInfo myUserInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.subassembly_fragment_user, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    /**
     * 用户编号
     */
    public String getUserId() {
        return "user" + (MainActivity.getUserIndex() + 1);
    }

    public abstract int getUserIcon();

    private void initData() {
        ((MainActivity) getActivity()).setTitles(getContext().getResources().getString(R.string.user_info));
        if (dialogUserInfo == null)
            dialogUserInfo = new DialogUserInfo();
        isLoading = true;
        setLoading2(true);
        setLoading1(true);
        myUserInfo = new MyUserInfo(getContext(), getUserId());
        user_tv_loading = (TextView) view.findViewById(R.id.user_tv_loading);
        user_tv_loading.setVisibility(View.VISIBLE);
        user_iv_icon = (ImageView) view.findViewById(R.id.user_iv_icon);
        user_tv_name = (TextView) view.findViewById(R.id.user_tv_name);
        user_tv_age = (TextView) view.findViewById(R.id.user_tv_age);
        user_tv_height = (TextView) view.findViewById(R.id.user_tv_height);
        user_tv_weight = (TextView) view.findViewById(R.id.user_tv_weight);
        user_ll_edit = (LinearLayout) view.findViewById(R.id.user_ll_edit);
        user_ll_cancellation = (LinearLayout) view.findViewById(R.id.user_ll_cancellation);
        history_lv_user = (ListView) view.findViewById(R.id.history_lv_user);
        user_iv_icon.setBackground(getActivity().getResources().getDrawable(getUserIcon()));
        users = new ArrayList<>();
        userHistoryAdapter = new UserHistoryAdapter(getContext(), users);
        history_lv_user.setAdapter(userHistoryAdapter);
        flush();
        user_ll_edit.setOnClickListener(this);
        user_ll_cancellation.setOnClickListener(this);
        userHistoryAdapter.setOnInnerItemOnClickListener(listener);
        flushUserInfo();
        dialogUserInfo.setOnInter(new DialogUserInfo.OnInter() {
            @Override
            public void setMyUser() {
                flushUserInfo();
            }
        });
    }

    private void flushUserInfo() {
        Log.e("UserTextFragment", " getUserId():"+ getUserId());
        MySharePreferences mySharePreferences = new MySharePreferences(getContext(), getUserId());
        user_tv_name.setText(mySharePreferences.getUserName());
        user_tv_age.setText(mySharePreferences.getUserAge());
        user_tv_height.setText(mySharePreferences.getUserHeight());
        user_tv_weight.setText(mySharePreferences.getUserWeight());
    }

    boolean isLoading;
    static boolean isLoading1;
    static boolean isLoading2;


    UserHistoryAdapter.InnerItemOnclickListener listener = new UserHistoryAdapter.InnerItemOnclickListener() {
        @Override
        public void itemClick(int i) {
            if (isLoading && isLoading1 && isLoading2) {
                isLoading = false;
                setLoading1(false);
                setLoading2(false);
                if (i < users.size()) {
                    myUserInfo.delete(users.get(i).get_id());
                    flush();
                }
            } else
                showToast(getString(R.string.deleting_data));
        }
    };
    DialogUserInfo dialogUserInfo;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_ll_edit:
                dialogUserInfo.createDialog(getContext(), getUserId());
                break;
            case R.id.user_ll_cancellation:
                if (TreadApplication.getInstance().isRuning()) {
                    showToast(getString(R.string.run_stop));
                } else {
                    TreadApplication.getInstance().setFirst(true);
                    ((MainActivity) getActivity()).getDialogs();
                    ((MainActivity) getActivity()).setCurrentIndex(0);
                }
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        dialogUserInfo.dimss();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            user_tv_loading.setVisibility(View.GONE);
            userHistoryAdapter.notifyDataSetChanged();
            isLoading = true;
        }
    };

    public void flush() {//刷新数据
        if (users != null) {
            new Thread("clear") {
                @Override
                public void run() {
                    super.run();
                    synchronized (this) {
                        users.clear();  //先清空集合
                        list = myUserInfo.query();
                        for (int i = 0; i < list.size(); i++) {
                            users.add(i, list.get(list.size() - (1 + i)));
                        }
                        handler.sendEmptyMessage(0);
                        handler.removeCallbacks(this);
                    }
                }
            }.start();
        }
    }

    public static void setLoading1(boolean loading1) {
        isLoading1 = loading1;
    }

    public static void setLoading2(boolean loading2) {
        isLoading2 = loading2;
    }
}