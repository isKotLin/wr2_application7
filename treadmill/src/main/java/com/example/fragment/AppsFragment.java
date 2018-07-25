package com.example.fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.activity.MainActivity;
import com.example.adapter.AppAdapter;
import com.example.app.BaseFragment;
import com.vigorchip.treadmill.wr2.R;

import java.util.Iterator;
import java.util.List;

/**
 * 应用
 */
public class AppsFragment extends BaseFragment implements
//        AppAdapter.OnUninstallListener,
        AppAdapter.OnItemListenerOnClick {
    GridView gv;
    List<ResolveInfo> mApps;
    PackageManager packageManager;
    View view;
    AppAdapter appAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_apps, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setTitles(getString(R.string.shortcut_apps));
        initData();
    }

    private void initData() {
        loadApps();
        gv = (GridView) view.findViewById(R.id.apps_gv_list);
        Iterator it = mApps.iterator();//迭代器
        while (it.hasNext()) {//判断迭代器有没有下一个值
            ResolveInfo obj = (ResolveInfo) it.next();//如果有下一个值就拿出赖
            if (//!obj.activityInfo.packageName.equals("")||!obj.activityInfo.packageName.equals("")
                    obj.activityInfo.packageName.equals("com.vigorchip.treadmill.wr2") ||
                    obj.activityInfo.packageName.equals("com.android.settings") ||
                    obj.activityInfo.packageName.equals("com.google.android.inputmethod.pinyin") ||
                    obj.activityInfo.packageName.equals("com.qt.cleanmaster") ||
                    obj.activityInfo.packageName.equals("com.vigorchip.WrMusic.wr2") ||
                    obj.activityInfo.packageName.equals("com.softwinner.update") ||
                    obj.activityInfo.packageName.equals("com.vigorchip.autocopy") ||
                    obj.activityInfo.packageName.equals("com.vigorchip.WrVideo.wr2")
                    ) {//如果迭代器循环中出现以上包名，就单独移出来
                it.remove();
            }
            //删除成功以后就不应该再输出这个删除的值，明白吗？
            continue;
        }
        appAdapter = new AppAdapter(getContext(), mApps);
        gv.setAdapter(appAdapter);
//        appAdapter.setOnUninstallListener(this);
        appAdapter.setOnItemListenerOnClick(this);
    }

    private void loadApps() {
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        packageManager = getActivity().getPackageManager();
        mApps = packageManager.queryIntentActivities(intent, 0);
    }

//    Intent uninstallIntent;
//    Uri packageURI = null;

//    /**
//     * 点击卸载应用
//     *
//     * @param index
//     */
//    @Override
//    public void itemLongClick(int index) {
//        try {
//            if (isSystemApp(packageManager.getPackageInfo(mApps.get(index).activityInfo.packageName, 0)))
//                showToast("此应用为系统应用，无法卸载");
//            else {
//                packageURI = Uri.parse("package:" + mApps.get(index).activityInfo.packageName);
//                uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
//                startActivity(uninstallIntent);
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//    }

//    /**
//     * 是否是系统应用
//     *
//     * @param pInfo
//     * @return
//     */
//    public boolean isSystemApp(PackageInfo pInfo) {
//        return ((pInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
//    }

//    /**
//     * 是否是系统更新后的应用
//     *
//     * @param pInfo
//     * @return
//     */
//    public boolean isSystemUpdateApp(PackageInfo pInfo) {
//        return ((pInfo.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0);
//    }

//    /**
//     * 是否是第三方应用
//     *
//     * @param pInfo
//     * @return
//     */
//    public boolean isUserApp(PackageInfo pInfo) {
//        return (!isSystemApp(pInfo) && !isSystemUpdateApp(pInfo));
//    }

    /**
     * 点击跳转应用
     *
     * @param position
     */
    @Override
    public void itemClick(int position) {
        try {
            Intent intent = packageManager.getLaunchIntentForPackage(mApps.get(position).activityInfo.packageName);
            startActivity(intent);
        }catch (Exception e){
            showLogE("跳转失败");
        }

    }
}
