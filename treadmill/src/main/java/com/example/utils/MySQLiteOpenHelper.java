package com.example.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 *数据库 存放历史记录
 */
public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    public MySQLiteOpenHelper(Context context) {
        super(context, "userInfo.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql1 = "create table if not exists user1(_id integer primary key autoincrement,_dates text not null,_times text not null,_mileage float not null,_heat float not null)";
        db.execSQL(sql1);
        String sql2 = "create table if not exists user2(_id integer primary key autoincrement,_dates text not null,_times text not null,_mileage float not null,_heat float not null)";
        db.execSQL(sql2);
        String sql3 = "create table if not exists user3(_id integer primary key autoincrement,_dates text not null,_times text not null,_mileage float not null,_heat float not null)";
        db.execSQL(sql3);
        String sql4 = "create table if not exists user4(_id integer primary key autoincrement,_dates text not null,_times text not null,_mileage float not null,_heat float not null)";
        db.execSQL(sql4);
        String sql5 = "create table if not exists user5(_id integer primary key autoincrement,_dates text not null,_times text not null,_mileage float not null,_heat float not null)";
        db.execSQL(sql5);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}