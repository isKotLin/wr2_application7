package com.example.app;

import android.support.v7.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {

    //	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		initData();
//		setContentView(getLayoutId());
//		initView();
//	}
//
//	private void initData() {
//	}
//
//	protected abstract void initView();
//
//	protected abstract int getLayoutId();
//
//	public void showToast(String showInfo) {
//		Toast.makeText(this, showInfo, Toast.LENGTH_SHORT).show();
//	}
//
//	public void logEUtils(String className, String printInfo) {
//		Log.e(className, printInfo);
//	}
//
//	public void logVUtils(String className, String printInfo) {
//		Log.v(className, printInfo);
//	}
//
//	public void logWUtils(String className, String printInfo) {
//		Log.w(className, printInfo);
//	}
//
//	public void logDUtils(String className, String printInfo) {
//		Log.d(className, printInfo);
//	}
//
//	public void logIUtils(String className, String printInfo) {
//		Log.i(className, printInfo);
//	}
}