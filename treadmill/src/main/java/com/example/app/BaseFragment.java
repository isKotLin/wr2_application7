package com.example.app;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.vigorchip.treadmill.wr2.R;

/**
 *
 */
public abstract class BaseFragment extends Fragment {
    Toast toast;
    View customView;

    public void showToast(String str) {
        if (toast!=null)
            toast.cancel();
         customView = View.inflate(getContext(), R.layout.toast_layout, null);
        toast = new Toast(getContext());
        ((TextView) customView.findViewById(R.id.toast)).setText(str);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(customView);
        toast.show();
    }
    public void showLogE(String message) {
        Log.e(getCurrentClassName() + ":" + getCurrentMethodName(), message);
    }

    public static String getCurrentMethodName() {
        int level = 1;
        StackTraceElement[] stacks = new Throwable().getStackTrace();
        String methodName = stacks[level].getMethodName();
        return methodName;
    }

    public static String getCurrentClassName() {
        int level = 1;
        StackTraceElement[] stacks = new Throwable().getStackTrace();
        String className = stacks[level].getClassName();
        return className;
    }


//	View view;
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		view=inflater.inflate(getLayoutNum(),container,false);
//		return view;
//	}
//
//	public abstract int getLayoutNum() ;

    //    @Override
//    public void onResume() {
//        super.onResume();
//        getFocus();
//    }
//
//    private void getFocus() {
//        getView().setFocusable(true);
//        getView().setFocusableInTouchMode(true);
//        getView().requestFocus();
//        getView().setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
//                    // 监听到返回按钮点击事件
//                    Log.e("tag", "---------------点击了返回键");
//                    return true;// 未处理
//                }
//                return false;
//            }
//        });
//    }
}