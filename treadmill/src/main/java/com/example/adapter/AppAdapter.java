package com.example.adapter;

import android.content.Context;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.moudle.AppViewHandle;
import com.vigorchip.treadmill.wr2.R;

import java.util.List;

public class AppAdapter extends BaseAdapter {
    List<ResolveInfo> mApps;
    Context mContext;
    Drawable drawable;
//        private OnUninstallListener onUninstallListener;
    private OnItemListenerOnClick onItemListenerOnClick;

    public AppAdapter(Context context, List<ResolveInfo> apps) {
        mContext = context;
        mApps = apps;
    }

    @Override
    public int getCount() {
        return mApps == null ? 0 : mApps.size();
    }

    @Override
    public ResolveInfo getItem(int position) {
        return mApps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        AppViewHandle handle;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.subassembly_apps_item, null);
            handle = new AppViewHandle(convertView);
            convertView.setTag(handle);
        } else
            handle = (AppViewHandle) convertView.getTag();
        ResolveInfo resolveInfo = mApps.get(position);
        handle.apps_iv.setImageDrawable(resolveInfo.activityInfo.loadIcon(mContext.getPackageManager()));
        handle.apps_tv.setText(resolveInfo.loadLabel(mContext.getPackageManager()));
        if (position % 3 == 0)
            drawable = mContext.getResources().getDrawable(R.drawable.shape_angle_green_frame);
        else if (position % 3 == 1)
            drawable = mContext.getResources().getDrawable(R.drawable.shape_angle_orange_frame);
        else if (position % 3 == 2)
            drawable = mContext.getResources().getDrawable(R.drawable.shape_angle_blue_frame);
        handle.app_ll_item.setBackground(drawable);
//        handle.app_ll_item.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                onUninstallListener.itemLongClick(position);
//                return true;
//            }
//        });
        handle.app_ll_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemListenerOnClick.itemClick(position);
            }
        });
        return convertView;
    }

    public void setOnItemListenerOnClick(OnItemListenerOnClick onItemListenerOnClick) {
        this.onItemListenerOnClick = onItemListenerOnClick;
    }

//        public void setOnUninstallListener(OnUninstallListener onUninstallListener) {
//        this.onUninstallListener = onUninstallListener;
//    }
    public interface OnItemListenerOnClick {
        void itemClick(int position);
    }


//    public interface OnUninstallListener {
//        void itemLongClick(int index);
//    }
}