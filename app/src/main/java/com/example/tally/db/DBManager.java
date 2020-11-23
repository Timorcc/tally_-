package com.example.tally.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/*
 * 负责管理数据库的类，主要对于表的内容操作，crud
 * */
public class DBManager {

    private static SQLiteDatabase db;

    //初始化数据库对象
    public static void initDB(Context context) {

        DBOpenHelper helper = new DBOpenHelper(context);//得到帮助类对象
        db = helper.getWritableDatabase();              //得到数据库对象
    }
    /*
     * 读取数据库数据，写入内存
     * kind 表示收入或者支出
     */

    public static List<TypeBean> getTypeList(int kind) {
        List<TypeBean> list = new ArrayList<>();
        //读取表中的数据
        String sql = "select * from typetb where kind = " + kind;
        Cursor cursor = db.rawQuery(sql, null);
        //循环读取游标内容，存到对象中
        while (cursor.moveToNext()) {
            String typename = cursor.getString(cursor.getColumnIndex("typename"));
            int imageId = cursor.getInt(cursor.getColumnIndex("imageId"));
            int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            int kind1 = cursor.getInt(cursor.getColumnIndex("kind"));
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            TypeBean typeBean = new TypeBean(id, typename, imageId, sImageId, kind1);
            list.add(typeBean);
        }
        return list;
    }

    /*
    向记账表中插入一条元素
    String typeName;//类型
    int sImageId;//被选中类型图片
    String beizhu;//备注
    float money;//价格
    String time;//保存时间的字符串
    int year;
    int month;
    int day;
    int kind; //类型 收入 -1 支出 -0
    * */

    public static void insertItemToAccounttb(AccountBean bean) {

        ContentValues values = new ContentValues();

        values.put("typeName", bean.getTypeName());
        values.put("sImageId", bean.getsImageId());
        values.put("beizhu", bean.getBeizhu());
        values.put("money", bean.getMoney());
        values.put("time", bean.getTime());
        values.put("year", bean.getYear());
        values.put("month", bean.getMonth());
        values.put("day", bean.getDay());
        values.put("kind", bean.getKind());

        db.insert("accounttb", null, values);
    }

}