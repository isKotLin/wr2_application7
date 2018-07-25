package com.example.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 *
 */
public class IsSerivceRun {
	private static final String WIN_INFO_NAME = "com.example.service.WindowInfoService";

    /**
     * 用来判断服务是否运行.
     *
     * @param className 判断的服务名字
     * @return true 在运行 false 不在运行
     * @paramContext
     */
    public static boolean isServiceRunning(Context mContext, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(30);
        if (!(serviceList.size() > 0)) {
            return false;
        }
        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(className) == true) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

//	/**
//	 * 打开时关闭，关闭时打开
//	 *
//	 * @param className
//	 */
//	public static void isServiceRunning(String className) {
//		if (className.equals(WIN_INFO_NAME)) {//信息
//			if (WindowInfoService.isHide()) {
//				WindowInfoService.mShow();
//				WindowInfoService.setIsHide(false);
//			} else {
//				WindowInfoService.hide();
//				WindowInfoService.setIsHide(true);
//			}
//		}
//		if (className.equals(WIN_INFO_BTN)) {//按钮
//			if (WindowButtonService.isHide()) {
//				WindowButtonService.mShow();
//				WindowButtonService.setIsHide(false);
//			} else {
//				WindowButtonService.hide();
//				WindowButtonService.setIsHide(true);
//			}
//		}
//	}
	/**
	 * 隐藏时显示
	 *
	 * @param className
	 */
	public static void startServiceRunn(String className) {
		if (className.equals(WIN_INFO_NAME)) {//信息
		}
//		if (className.equals(WIN_INFO_BTN)) {//按钮
//			if (WindowButtonService.isHide()) {
//				WindowButtonService.mShow();
//				WindowButtonService.setIsHide(false);
//			}
//		}
//		if (className.equals(WIN_INFO_DIALOG)) {//按钮
//			if (DialogError.isHide()) {
//				DialogError.mShow();
//				DialogError.setIsHide(false);
//			}
//		}
	}
	public static void showImageView(){
	}
	/**
	 * 显示时隐藏
	 *
	 * @param className
	 */
	public static void stopServiceRunn(String className) {
		if (className.equals(WIN_INFO_NAME)) {//信息
		}
//		if (className.equals(WIN_INFO_BTN)) {//按钮
//			if (!WindowButtonService.isHide()) {
//				WindowButtonService.hide();
//				WindowButtonService.setIsHide(true);
//			}
//		}
//		if (className.equals(WIN_INFO_DIALOG)) {//按钮
//			if (!DialogError.isHide()) {
//				DialogError.hide();
//				DialogError.setIsHide(true);
//			}
//		}
	}

	public static boolean isActivityRunning(Context mContext
//			, String activityClassName
	) {
		String activityClassName="com.example.activity.MainActivity";
		ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningTaskInfo> info = activityManager.getRunningTasks(1);
		if (info != null && info.size() > 0) {
			ComponentName component = info.get(0).topActivity;
			Log.e("TAg","================"+component.getClassName());
			if (activityClassName.equals(component.getClassName())) {
				return true;
			}
		}
		return false;
	}
}
