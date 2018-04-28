package com.nian.carbout.analysis;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.widget.Toast;

import com.nian.carbout.DBhelper;
import com.nian.carbout.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class AnalysisActivity extends AppCompatActivity {

    private DBhelper dataHelper;
    private SQLiteDatabase db;
    private ArrayList<co2_item> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_analysis);

        getSupportActionBar().hide();

        Toolbar toolbar = findViewById(R.id.toolbarAnalysis);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        //setSupportActionBar(toolbar);


        dataHelper = new DBhelper(this, "co2.sqlite",null, 1);
        db = dataHelper.getWritableDatabase();

        //依據條件搜尋SQLite，並回傳資料指標
        Cursor c = db.rawQuery("SELECT * FROM main", null);
        c.moveToFirst();



        for(int i = 0; i < c.getCount(); i++) {
            //取出欄位索引值加到字串後

            data.add(new co2_item(
                    c.getString(2),
                    c.getInt(1),
                    c.getInt(3),
                    c.getInt(0)));
            //移動至下一筆
            c.moveToNext();
        }

        c.close();

        RecyclerView recyclerView = findViewById(R.id.RecyclerViewInAnalysis);

        //寫入範例
        //data.add(new co2_item("麥香紅茶","2018-04-06",160,46456465L));


        Collections.reverse(data);

        MyAdapter adapter = new MyAdapter(this, data);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}
