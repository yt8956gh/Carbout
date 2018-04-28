package com.nian.carbout;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DBhelper extends SQLiteOpenHelper {
    public DBhelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE  TABLE main " +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT , " +
                "date INTEGER NOT NULL , " +
                "info VARCHAR NOT NULL, " +
                "co2 INTEGER)");
    }

    // 當資料庫是較舊版時，呼叫execSQL刪除資料表，並呼叫onCreate重新建立資料庫
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS main");
        onCreate(db);
    }


    public long append(SQLiteDatabase db, int co2, String info)
    {
        Long id;
        Date dNow = new Date();//得到Date實例
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyyMMdd");

        ContentValues cv = new ContentValues();
        cv.put("date", Integer.parseInt(dateFormatter.format(dNow)));
        cv.put("info", info);
        cv.put("co2", String.valueOf(co2));
        id = db.insert("main",null,cv);

        return id;
    }

}
