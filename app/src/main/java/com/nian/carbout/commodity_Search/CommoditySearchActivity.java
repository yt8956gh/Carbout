package com.nian.carbout.commodity_Search;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;

import com.nian.carbout.DataBaseHelper;
import com.nian.carbout.R;
import com.nian.carbout.commodity.CommodityActivity;
import com.nian.carbout.commodity.shop_list;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import info.debatty.java.stringsimilarity.JaroWinkler;

public class CommoditySearchActivity extends AppCompatActivity {


    private  ArrayList<list_item> list = new ArrayList<>();
    private  ArrayList<list_item> result = new ArrayList<>();
    private DataBaseHelper dataHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_commodity);


        getSupportActionBar().hide();

        SearchView searchView = findViewById(R.id.searchView_commodity);
        searchView.setIconifiedByDefault(true);
        searchView.onActionViewExpanded();

        dataHelper = new DataBaseHelper(this, "co2.sqlite",null, 1);

        setDataBase();
        setRecycleView();


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newString) {

                result.clear();

                search_in_DB(newString);

                setRecycleViewWithResult();

                return false;
            }
        });

    }

    public void setRecycleView()
    {
        list_adapter adapter = new list_adapter(this,list,dataHelper);

        RecyclerView rv = findViewById(R.id.search_list_show);

        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
    }

    public void setRecycleViewWithResult()
    {
        list_adapter adapter = new list_adapter(this,result,dataHelper);

        RecyclerView rv = findViewById(R.id.search_list_show);

        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
    }

    public void setDataBase()
    {
        DataBaseHelper dataHelper = new DataBaseHelper(this, "resource.db", null, 1);
        SQLiteDatabase db = dataHelper.getWritableDatabase();

        //依據條件搜尋SQLite，並回傳資料指標
        Cursor c = db.rawQuery("SELECT * FROM Commodity", null);
        c.moveToFirst();

        float testCO2=0;
        String unit="",number="",total_string;

        for(int i = 0; i < c.getCount(); i++) {
            //取出欄位索引值加到字串後

            try{
                total_string = c.getString(3);
                if(total_string.matches("[0-9]+kg"))
                {
                    number = total_string.replace("kg","");
                    unit = "kg";
                }
                else if(total_string.matches("[0-9]+g"))
                {
                    number = total_string.replace("g","");
                    unit = "g";
                }

                testCO2 = Integer.parseInt(number);
            }
            catch (NumberFormatException e)
            {
                c.moveToNext();
                continue;
            }

            //Log.d("Detail", c.getString(1)+":"+c.getString(3));
            String name = c.getString(1);
            String spec = c.getString(2);


            if(!spec.equals("-"))
            {
                name = name + "\n" + spec;
            }


            list.add(new list_item(name, testCO2,unit));
            //移動至下一筆
            c.moveToNext();
        }

        c.close();
    }

    public void search_in_DB(String newString)
    {

        JaroWinkler jw = new JaroWinkler();//使用java-string-similarity實例


            //與資料庫中的品名進行比對
            for(list_item item_DB : list)
            {
                if(jw.similarity(newString,item_DB.getName())>=0.75f)
                {
                    result.add(item_DB);
                }
                else if(item_DB.getName().contains(newString))
                {
                    result.add(item_DB);
                }
            }
    }

}
