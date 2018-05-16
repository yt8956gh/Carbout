package com.nian.carbout.self;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class NewItemDBHelper extends SQLiteOpenHelper {
    public NewItemDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE IF NOT EXISTS Commodity" +
                        "(type VARCHAR NOT NULL, " +
                        "name VARCHAR NOT NULL, " +
                        "spec VARCHAR, " +
                        "co2 VARCHAR NOT NULL, " +
                        "unit VARCHAR, "+
                        "other1 VARCHAR, "+
                        "other2 VARCHAR) "
                );
    }

    // 當資料庫是較舊版時，呼叫execSQL刪除資料表，並呼叫onCreate重新建立資料庫
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS Commodity");
        onCreate(db);
    }


    public void append(SQLiteDatabase db,String type ,String name, float co2)
    {

        String co2_unit;

        if(co2>0f)
        {
            co2_unit = (int)co2+"kg";
        }
        else
        {
            co2_unit = (int)co2*1000+"g";
        }


        ContentValues cv = new ContentValues();
        cv.put("type",type);
        cv.put("name",name);
        cv.put("co2", co2_unit);
        cv.put("spec","-");
        db.insert("Commodity",null,cv);

        Log.d("DB_test", name+":"+co2_unit);
    }
}
