package com.example.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.bean.User;
import com.example.moudle.UserViewHandle;
import com.vigorchip.treadmill.wr2.R;

import java.util.List;

/**
 * 历史记录
 */
public class UserHistoryAdapter extends BaseAdapter {
    List<User> mList;
    Context mContext;
    InnerItemOnclickListener mListener;
    public UserHistoryAdapter(Context context, List<User> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public User getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        UserViewHandle hand;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.user_item, null);
            hand = new UserViewHandle(convertView);
            convertView.setTag(hand);
        } else
            hand = (UserViewHandle) convertView.getTag();
        hand.user_tv_data.setText(mList.get(position).getDate());
        hand.user_tv_time.setText(mList.get(position).getTime());
        hand.user_tv_mileage.setText(mList.get(position).getMileage() + "");
        hand.user_tv_heat.setText(mList.get(position).getHeat() + "");
        hand.user_iv_clear.setImageDrawable(mContext.getResources().getDrawable(R.drawable.selector_trash_press));
        hand.user_iv_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.user_iv_clean:
                        mListener.itemClick(position);
                        break;
                }

            }
        });
        return convertView;
    }

    public interface InnerItemOnclickListener {
        void itemClick(int i);
    }

    public void setOnInnerItemOnClickListener(InnerItemOnclickListener listener) {
        mListener = listener;
    }
}