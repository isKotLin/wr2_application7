package com.example.fragment;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.activity.MainActivity;
import com.example.app.BaseFragment;
import com.example.app.TreadApplication;
import com.example.utils.FileUtils;
import com.vigorchip.treadmill.wr2.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

/**
 * 设置中的操作指南和系统信息
 */
public class OperatorGuideFragment extends BaseFragment {
    View view;
    TextView emmc_flash, setting_tv_ddr, banben_name, banben_music, banben_video, banben_name_music, banben_name_video,
            opear_tv_0, opear_tv_1, opear_tv_2, opear_tv_3, opear_tv_4, opear_tv_5, opear_tv_6, opear_tv_7, opear_tv_8, opear_tv_9, opear_tv_10, opear_tv_11, opear_tv_12, opear_tv_13, opear_tv_14, opear_tv_15,
            type_tv_0, type_tv_1, type_tv_2, type_tv_3, type_tv_4, type_tv_5, type_tv_6, type_tv_7, type_tv_8, type_tv_9, type_tv_10, type_tv_11, type_tv_12, type_tv_13, type_tv_14, type_tv_15,
            codes_tv_0, codes_tv_1, codes_tv_2, codes_tv_3, codes_tv_4, codes_tv_5, codes_tv_6, codes_tv_7, codes_tv_8, codes_tv_9, codes_tv_10, codes_tv_11, codes_tv_12, codes_tv_13, codes_tv_14, codes_tv_15;
    static String[] error;
    static String[] prebelom;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_opera, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
        initData();
        initFlash();
        initError();
    }

    DecimalFormat format;
    float total;
    float available;
    float sum;

    //    private ActivityManager myActivityManager;
    private void initFlash() {
//        myActivityManager =(ActivityManager)getContext().getSystemService(Activity.ACTIVITY_SERVICE);//获取系统服务信息
//        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();  //获得MemoryInfo对象
//        myActivityManager.getMemoryInfo(memoryInfo) ;    //获得系统可用内存，保存在MemoryInfo对象上
//        long memSize = memoryInfo.availMem ;
//        String leftMemSize = Formatter.formatFileSize(getContext(), memSize);  //字符类型转换
//        showLogE(getAvailableInternalMemorySize()+"");
//        showLogE(getTotalInternalMemorySize()+"");
//        showToast(leftMemSize);
        available = (float) getSDAvailableSize();
        total = (float) getSDTotalSize();
        format = new DecimalFormat("0.0");
        sum = total - available;
//        emmc_flash.setText("存储空间: "+"总共:8GB(系统区占用:"+(8.0-Double.parseDouble(format.format(total/1000000000)))+"GB, 用户区占用:"+format.format(total/1000000000)+"GB), 剩余空间:"+format.format(available/1000000000)+"GB");
//        emmc_flash.setText("存储空间: " + "系统区大小:" + (8.0 - Double.parseDouble(format.format(total / 1000000000))) + "GB, 用户区大小:" + format.format(total / 1000000000) + "GB(已用:"+format.format(sum/1000000000)+"GB, 剩余:" + format.format(available / 1000000000) + "GB)");
//        if (sum > 1000000000)
//            emmc_flash.setText("存储空间\n" + "总内存: 8GB\n系统占用内存: " + format.format(8.0 - (total / 1000000000)) + "GB \n用户可使用内存: " + format.format(total / 1000000000) + "GB(已用: " + format.format(sum / 1000000000) + "GB, 剩余: " + format.format(available / 1000000000) + "GB)");
//        else
//            emmc_flash.setText("存储空间\n" + "总内存: 8GB\n系统占用内存: " + format.format(8.0 - (total / 1000000000)) + "GB \n用户可使用内存: " + format.format(total / 1000000000) + "GB(已用: " + format.format(sum / 1000000) + "MB, 剩余: " + format.format(available / 1000000000) + "GB)");
//    }
        if (sum > 1000000000)
            emmc_flash.setText(getString(R.string.memory_space) + format.format(8.0 - (total / 1000000000)) +getString(R.string.useable_user) + format.format(total / 1000000000) + getString(R.string.been_used) + format.format(sum / 1000000000) + getString(R.string.remaining_GB) + format.format(available / 1000000000) + getString(R.string.GB));
        else
            emmc_flash.setText(getString(R.string.memory_space) + format.format(8.0 - (total / 1000000000)) + getString(R.string.useable_user) + format.format(total / 1000000000) + getString(R.string.been_used) + format.format(sum / 1000000) + getString(R.string.remaining_MB) + format.format(available / 1000000000) + getString(R.string.GB));
    }

    //获取总内存大小
    public static String getTotalMemorySize(Context context) {
        long size = 0;

        //通过读取配置文件方式获取总内大小。文件目录：/proc/meminfo  
        File file = new File("/proc/meminfo");
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            //根据命令行可以知道，系统总内存大小位于第一行  
            String totalMemarysizeStr = reader.readLine();//MemTotal:         513744 kB     
            //要获取大小，对字符串截取  
            int startIndex = totalMemarysizeStr.indexOf(':');
            int endIndex = totalMemarysizeStr.indexOf('k');
            //截取  
            totalMemarysizeStr = totalMemarysizeStr.substring(startIndex + 1, endIndex).trim();
            //转为long类型，得到数据单位是kb  
            size = Long.parseLong(totalMemarysizeStr);
            //转为以byte为单位  
            size *= 1024;
        } catch (Exception e) {
            // TODO Auto-generated catch block  
            e.printStackTrace();
        }
        return Formatter.formatFileSize(context, size);
//        return size;
    }

    /**
     * 获得SD卡总大小
     *
     * @return
     */
    private long getSDTotalSize() {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
//        return Formatter.formatFileSize(getContext(), blockSize * totalBlocks);
        return blockSize * totalBlocks;
    }

    /**
     * 获得sd卡剩余容量，即可用大小
     */
    private long getSDAvailableSize() {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return blockSize * availableBlocks;
//        return Formatter.formatFileSize(getContext(), blockSize * availableBlocks);
    }

    /**
     * 获取手机内部剩余存储空间
     */
    public static long getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
    }

    /**
     * 获取手机内部总的存储空间
     */
    public static long getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return totalBlocks * blockSize;
    }

    /**
     * 获得机身内存总大小
     *
     * @return
     */
    private String getRomTotalSize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return Formatter.formatFileSize(getContext(), blockSize * totalBlocks);
    }

    /**
     * 获得机身可用内存
     */
    private String getRomAvailableSize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return Formatter.formatFileSize(getContext(), blockSize * availableBlocks);
    }

    /**
     * @param context
     */
    private String getAvailMemory(Context context) {
        // 获取android当前可用内存大小
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        return Formatter.formatFileSize(getContext(), mi.availMem);
    }

    private void initError() {
        if (TreadApplication.getInstance().getCustom().equals("qimaisi")) {
            opear_tv_0.setText(prebelom[0]);
            opear_tv_1.setText(prebelom[1]);
            opear_tv_2.setText(prebelom[2]);
            opear_tv_3.setText(prebelom[3]);
            opear_tv_4.setText(prebelom[4]);
            opear_tv_5.setText(prebelom[5]);
            opear_tv_6.setText(prebelom[6]);
            opear_tv_7.setText(prebelom[7]);
            opear_tv_8.setText(prebelom[8]);
            type_tv_0.setText(error[0]);
            type_tv_1.setText(error[1]);
            type_tv_2.setText(error[2]);
            type_tv_3.setText(error[3]);
            type_tv_4.setText(error[4]);
            type_tv_5.setText(error[5]);
            type_tv_6.setText(error[6]);
            type_tv_7.setText(error[7]);
            type_tv_8.setText(error[8]);
            Goen();
        }
    }

    private void Goen() {
        codes_tv_9.setVisibility(View.GONE);
        codes_tv_10.setVisibility(View.GONE);
        codes_tv_11.setVisibility(View.GONE);
        codes_tv_12.setVisibility(View.GONE);
        codes_tv_13.setVisibility(View.GONE);
        codes_tv_14.setVisibility(View.GONE);
        codes_tv_15.setVisibility(View.GONE);
        type_tv_9.setVisibility(View.GONE);
        type_tv_10.setVisibility(View.GONE);
        type_tv_11.setVisibility(View.GONE);
        type_tv_12.setVisibility(View.GONE);
        type_tv_13.setVisibility(View.GONE);
        type_tv_14.setVisibility(View.GONE);
        type_tv_15.setVisibility(View.GONE);
        opear_tv_9.setVisibility(View.GONE);
        opear_tv_10.setVisibility(View.GONE);
        opear_tv_11.setVisibility(View.GONE);
        opear_tv_12.setVisibility(View.GONE);
        opear_tv_13.setVisibility(View.GONE);
        opear_tv_14.setVisibility(View.GONE);
        opear_tv_15.setVisibility(View.GONE);
    }

    private void initData() {
        setting_tv_ddr.setText("DDR : " + getTotalMemorySize(getContext()));
        error = getResources().getStringArray(R.array.error_qi);
        prebelom = getResources().getStringArray(R.array.error_per_qi);
        banben_name.setText(FileUtils.getVersionName(getContext()));
        if (checkPackage("com.vigorchip.WrMusic.wr2"))
            banben_name_music.setText(FileUtils.getVersionName(getContext(), "com.vigorchip.WrMusic.wr2"));
        else
            banben_music.setVisibility(View.GONE);
        if (checkPackage("com.vigorchip.WrVideo.wr2"))
            banben_name_video.setText(FileUtils.getVersionName(getContext(), "com.vigorchip.WrVideo.wr2"));
        else
            banben_video.setVisibility(View.GONE);
    }

    private void initView() {
        setting_tv_ddr = (TextView) view.findViewById(R.id.setting_tv_ddr);
        emmc_flash = (TextView) view.findViewById(R.id.emmc_flash);
        codes_tv_0 = (TextView) view.findViewById(R.id.codes_tv_0);
        codes_tv_1 = (TextView) view.findViewById(R.id.codes_tv_1);
        codes_tv_2 = (TextView) view.findViewById(R.id.codes_tv_2);
        codes_tv_3 = (TextView) view.findViewById(R.id.codes_tv_3);
        codes_tv_4 = (TextView) view.findViewById(R.id.codes_tv_4);
        codes_tv_5 = (TextView) view.findViewById(R.id.codes_tv_5);
        codes_tv_6 = (TextView) view.findViewById(R.id.codes_tv_6);
        codes_tv_7 = (TextView) view.findViewById(R.id.codes_tv_7);
        codes_tv_8 = (TextView) view.findViewById(R.id.codes_tv_8);
        codes_tv_9 = (TextView) view.findViewById(R.id.codes_tv_9);
        codes_tv_10 = (TextView) view.findViewById(R.id.codes_tv_10);
        codes_tv_11 = (TextView) view.findViewById(R.id.codes_tv_11);
        codes_tv_12 = (TextView) view.findViewById(R.id.codes_tv_12);
        codes_tv_13 = (TextView) view.findViewById(R.id.codes_tv_13);
        codes_tv_14 = (TextView) view.findViewById(R.id.codes_tv_14);
        codes_tv_15 = (TextView) view.findViewById(R.id.codes_tv_15);

        type_tv_0 = (TextView) view.findViewById(R.id.type_tv_0);
        type_tv_1 = (TextView) view.findViewById(R.id.type_tv_1);
        type_tv_2 = (TextView) view.findViewById(R.id.type_tv_2);
        type_tv_3 = (TextView) view.findViewById(R.id.type_tv_3);
        type_tv_4 = (TextView) view.findViewById(R.id.type_tv_4);
        type_tv_5 = (TextView) view.findViewById(R.id.type_tv_5);
        type_tv_6 = (TextView) view.findViewById(R.id.type_tv_6);
        type_tv_7 = (TextView) view.findViewById(R.id.type_tv_7);
        type_tv_8 = (TextView) view.findViewById(R.id.type_tv_8);
        type_tv_9 = (TextView) view.findViewById(R.id.type_tv_9);
        type_tv_10 = (TextView) view.findViewById(R.id.type_tv_10);
        type_tv_11 = (TextView) view.findViewById(R.id.type_tv_11);
        type_tv_12 = (TextView) view.findViewById(R.id.type_tv_12);
        type_tv_13 = (TextView) view.findViewById(R.id.type_tv_13);
        type_tv_14 = (TextView) view.findViewById(R.id.type_tv_14);
        type_tv_15 = (TextView) view.findViewById(R.id.type_tv_15);

        opear_tv_0 = (TextView) view.findViewById(R.id.opear_tv_0);
        opear_tv_1 = (TextView) view.findViewById(R.id.opear_tv_1);
        opear_tv_2 = (TextView) view.findViewById(R.id.opear_tv_2);
        opear_tv_3 = (TextView) view.findViewById(R.id.opear_tv_3);
        opear_tv_4 = (TextView) view.findViewById(R.id.opear_tv_4);
        opear_tv_5 = (TextView) view.findViewById(R.id.opear_tv_5);
        opear_tv_6 = (TextView) view.findViewById(R.id.opear_tv_6);
        opear_tv_7 = (TextView) view.findViewById(R.id.opear_tv_7);
        opear_tv_8 = (TextView) view.findViewById(R.id.opear_tv_8);
        opear_tv_9 = (TextView) view.findViewById(R.id.opear_tv_9);
        opear_tv_10 = (TextView) view.findViewById(R.id.opear_tv_10);
        opear_tv_11 = (TextView) view.findViewById(R.id.opear_tv_11);
        opear_tv_12 = (TextView) view.findViewById(R.id.opear_tv_12);
        opear_tv_13 = (TextView) view.findViewById(R.id.opear_tv_13);
        opear_tv_14 = (TextView) view.findViewById(R.id.opear_tv_14);
        opear_tv_15 = (TextView) view.findViewById(R.id.opear_tv_15);

        banben_name = (TextView) view.findViewById(R.id.banben_name);
        banben_music = (TextView) view.findViewById(R.id.banben_music);
        banben_video = (TextView) view.findViewById(R.id.banben_video);
        banben_name_music = (TextView) view.findViewById(R.id.banben_name_music);
        banben_name_video = (TextView) view.findViewById(R.id.banben_name_video);
        ((MainActivity) getActivity()).setTitles(getResources().getString(R.string.operation_guide));
    }

    /**
     * 检测该包名所对应的应用是否存在
     *
     * @param packageName
     * @return
     */
    public boolean checkPackage(String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            getActivity().getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}