package com.example.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.view.inputmethod.InputMethodManager;

import java.util.List;

//打开或关闭软键盘
public class KeyBoardUtils {
    /**
     * 打卡软键盘
     *
     * @parammEditText输入框
     * @parammContext上下文
     */
    public static void openKeybord(
//			EditText mEditText,
			Context mContext) {
//        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN);
//        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
		InputMethodManager imm = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE); //得到InputMethodManager的实例
		if (imm.isActive()) {//如果开启
			imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,InputMethodManager.HIDE_NOT_ALWAYS);//关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
		}
    }

    /**
     * 关闭软键盘
     *
     * @parammEditText输入框
     * @parammContext上下文
     */
    public static void closeKeybord(
//			EditText mEditText,
			Context mContext) {
//        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
		InputMethodManager imm = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE); //得到InputMethodManager的实例
		if (imm.isActive()) {//如果开启
			imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,InputMethodManager.HIDE_NOT_ALWAYS);//关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
		}
    }

    /**
     * 判断相对应的APP是否存在
     *
     * @param context
     * @param packageName(包名) (若想判断QQ，则改为com.tencent.mobileqq，若想判断微信，则改为com.tencent.mm)
     */
    public boolean isAvilible(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);//获取手机系统的所有APP包名，然后进行一一比较
        for (int i = 0; i < pinfo.size(); i++) {
            if (pinfo.get(i).packageName.equalsIgnoreCase(packageName))
                return true;
        }
        return false;
    }

    /**
     * method queries the package manager for installed packages that can
     * respond to an intent with the specified action. If no suitable package is
     * found, this method returns false.** @param context The application's environment.
     *
     * @param action The Intent action to check for availability.** @return True if an Intent with the specified action can be sent and
     *               responded to, false otherwise.
     */
    public static boolean isIntentAvailable(Context context, String action) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(action);
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() != 0;
    }
}