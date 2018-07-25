package com.example.bean;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.activity.MainActivity;
import com.example.app.UserTextFragment;
import com.example.utils.MySQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;
//数据库的增删查
//历史记录的保存设置
public class MyUserInfo {
    private String mUserId;
    private MySQLiteOpenHelper mHelper;
//    private DatabaseManager mDatabaseManager;

    public MyUserInfo(Context context, String userId) {
        mHelper = new MySQLiteOpenHelper(context);// 获取到数据库帮助类的实例
//        mDatabaseManager = DatabaseManager.getInstance(mHelper);
        mUserId = userId;
    }

    /**
     * 增删查
     */
    public void insert(String _dates, String _times, double _mileage, double _heat) {
        SQLiteDatabase writableDatabase = mHelper.getWritableDatabase();// 获取数据库对象
        String sql = "insert into user" + (MainActivity.getUserIndex() + 1) + "(_dates,_times,_mileage,_heat) values(?,?,?,?)";
        Object[] bindArgs = new Object[]{_dates, _times, _mileage, _heat};
        writableDatabase.execSQL(sql, bindArgs);
        writableDatabase.close();
        Log.e("TAG", ">>>>>>>>>>>>>>");
    }

    public void delete(int id) {
        // 获取数据库对象
        SQLiteDatabase writableDatabase = mHelper.getWritableDatabase();
        String sql = "delete from " + mUserId + " where _id=?;";
        Object[] bindagrs = new Object[]{id};
        writableDatabase.execSQL(sql, bindagrs);
        //关闭数据库
        writableDatabase.close();
        UserTextFragment.setLoading1(true);
    }

    public List<User> query() {
        SQLiteDatabase writableDatabase = mHelper.getReadableDatabase();// 获取数据库对象
        String sql = "select * from " + mUserId + ";";
        Log.e("TAG","mUserId:"+mUserId);
        Cursor rawQuery = writableDatabase.rawQuery(sql, null);
        List<User> users = new ArrayList<>();
        while (rawQuery.moveToNext()) {
            User user = new User();
            user.set_id(rawQuery.getInt(rawQuery.getColumnIndex("_id")));
            user.setDate(rawQuery.getString(rawQuery.getColumnIndex("_dates")));
            user.setTime(rawQuery.getString(rawQuery.getColumnIndex("_times")));
            user.setMileage(rawQuery.getDouble(rawQuery.getColumnIndex("_mileage")));
            user.setHeat(rawQuery.getDouble(rawQuery.getColumnIndex("_heat")));
            users.add(user);
        }
        rawQuery.close();
        writableDatabase.close();
        UserTextFragment.setLoading2(true);
        return users;
    }

//    /***
//     * 判断表中是否有值
//     */
//    public boolean isExistTabValus() {
//        boolean flag = false;
//        SQLiteDatabase db = mDatabaseManager.getReadableDatabase();//获取一个可读的数据库对象
//        Cursor curcor = null;
//        try {
//            curcor = db.rawQuery("select * from tab ", null);
//            while (curcor.moveToNext()) {
//                if (curcor.getCount() > 0) {
//                    flag = true;
//                }
//            }
//        } catch (Exception e) {
//            Log.e(TAG, "isExistTabValus  error");
//        } finally {
//            if (curcor != null) {
//                curcor.close();
//            }
//            mDatabaseManager.closeDatabase();//关闭数据库
//        }
//        return flag;
//    }
}