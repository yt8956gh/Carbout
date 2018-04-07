package com.nian.carbout;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class AnalysisActivity extends AppCompatActivity {

    private DBhelper dataHelper;
    private SQLiteDatabase db;
    private ArrayList<co2_item> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);

        dataHelper = new DBhelper(this, "co2.sqlite",null, 1);
        db = dataHelper.getWritableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM main", null);
        c.moveToFirst();


        for(int i = 0; i < c.getCount(); i++) {
            //取出欄位索引值加到字串後
            data.add(new co2_item(c.getString(2),c.getString(1),Integer.parseInt(c.getString(3)),c.getInt(1)));
            //移動至下一筆
            c.moveToNext();
        }

        RecyclerView recyclerView = findViewById(R.id.RecyclerViewInAnalysis);

        data.add(new co2_item("麥香紅茶","2018-04-06",160,46456465L));
        data.add(new co2_item("麥香奶茶","2018-04-07",140,46456465L));
        data.add(new co2_item("麥香綠茶","2018-04-08",120,46456465L));

        MyAdapter adapter = new MyAdapter(this, data);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}
