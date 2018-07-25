package com.example.utils;

import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class FileUtils {
    /**
     * 获取SD卡路径
     *
     * @return
     */
    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
    }
    /**
     * 检测网络资源是否存在　      Url
     *
     * @param strUrl
     * @return
     */
    public static boolean isNetFileAvailable(String strUrl) {
        InputStream netFileInputStream = null;
        try {
            URL url = new URL(strUrl);
            URLConnection urlConn = url.openConnection();
            netFileInputStream = urlConn.getInputStream();
            if (null != netFileInputStream) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            return false;
        } finally {
            try {
                if (netFileInputStream != null)
                    netFileInputStream.close();
            } catch (IOException e) {
            }
        }
    }

//	图片的Uri 获得其在文件系统中的路径呢

    /**
     * Try to return the absolute file path from the given Uri
     *
     * @param context
     * @param uri
     * @return the file path or null
     */
    public static String UriToStr(Context context, Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    public static boolean isEscit(Context context, Uri uri) {
        File file = new File(UriToStr(context, uri));
        return file.exists();
    }

    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    Log.i("后台", appProcess.processName);
                    return true;
                } else {
                    Log.i("前台", appProcess.processName);
                    return false;
                }
            }
        }
        return false;
    }
//	图片的路径地址获得Uri
//	String type = Utils.ensureNotNull(intent.getType());
//	Log.d(TAG, "uri is " + uri);
//	if (uri.getScheme().equals("file") && (type.contains("image/"))) {
//		String path = uri.getEncodedPath();
//		Log.d(TAG, "path1 is " + path);
//		if (path != null) {
//			path = Uri.decode(path);
//			Log.d(TAG, "path2 is " + path);
//			ContentResolver cr = this.getContentResolver();
//			StringBuffer buff = new StringBuffer();
//			buff.append("(")
//					.append(MediaStore.Images.ImageColumns.DATA)
//					.append("=")
//					.append("'" + path + "'")
//					.append(")");
//			Cursor cur = cr.query(
//					MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//					new String[] { MediaStore.Images.ImageColumns._ID },
//					buff.toString(), null, null);
//			int index = 0;
//			for (cur.moveToFirst(); !cur.isAfterLast(); cur
//					.moveToNext()) {
//				index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
//				// set _id value
//				index = cur.getInt(index);
//			}
//			if (index == 0) {
//				//do nothing
//			} else {
//				Uri uri_temp = Uri
//						.parse("content://media/external/images/media/"
//								+ index);
//				Log.d("TAG", "uri_temp is " + uri_temp);
//				if (uri_temp != null) {
//					uri = uri_temp;
//				}
//			}
//		}
//	}

    //版本名            http://blog.csdn.net/analyzesystem/article/details/52311754
    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    //版本号
    public static int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;

        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }

    /**
     * 获取版本号
     *
     * @return
     */
    public int getVersionCode(Context context, String getPackageName) {
        PackageManager manager = context.getPackageManager();//获取包管理器
        try {
            //通过当前的包名获取包的信息
            PackageInfo info = manager.getPackageInfo(getPackageName, 0);//获取包对象信息
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取版本名
     *
     * @return
     */
    public static String getVersionName(Context context, String getPackageName) {
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo packageInfo = manager.getPackageInfo(getPackageName, PackageManager.GET_ACTIVITIES); //第二个参数代表额外的信息，例如获取当前应用中的所有的Activity
//            ActivityInfo[] activities = packageInfo.activities;
//            showActivities(activities);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void showActivities(ActivityInfo[] activities) {
        for (ActivityInfo activity : activities) {
            Log.i("activity=========", activity.name);
        }
    }

    /**
     * @param path 这个是路径的
     * @return
     */
    public static String readTextFromFile(String path) {
        File file = new File(path);
        StringBuffer sb = new StringBuffer();
        try {
            FileInputStream is = new FileInputStream(file);
            byte b[] = new byte[1024];
            while (true) {
                int len = is.read(b);
                if (len == -1) {
                    break;
                }
                String content = new String(b, 0, len);
                sb.append(content);
            }
            is.close();
        } catch (Exception e) {
            Log.e("FileUtils", "readTextFromFile出错了");
//            e.printStackTrace();
        }
        return sb.toString();
    }

    public static boolean writeText2File(String path, String content) {
        try {

            File file = new File(path);
            if (!file.exists())
                file.createNewFile();
            FileOutputStream os = new FileOutputStream(file);
            byte[] bytes = content.getBytes();
            os.write(bytes);
            os.getFD().sync();
//            os.flush();
            os.close();
            return true;
        } catch (Exception e) {
            Log.e("FileUtils", "writeText2File出错了");
//            e.printStackTrace();
        }
        return false;
    }

    public static boolean copyFile(String fromFilePath, String toFilePath) {
        File fromFile = new File(fromFilePath);
        File toFile = new File(toFilePath);
        try {
            FileInputStream is = new FileInputStream(fromFile);
            FileOutputStream os = new FileOutputStream(toFile);
            byte b[] = new byte[1024];
            int len;
            while ((len = is.read(b)) > 0) {
                os.write(b, 0, len);
            }
//            os.getFD().sync();
            os.flush();
            os.close();
            is.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}