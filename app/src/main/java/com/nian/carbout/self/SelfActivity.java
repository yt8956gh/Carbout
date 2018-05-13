package com.nian.carbout.self;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.SearchView;

import com.nian.carbout.DataBaseHelper;
import com.nian.carbout.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class SelfActivity extends AppCompatActivity {


    private  ArrayList<list_item_self> list = new ArrayList<>();
    private  ArrayList<list_item_self> result = new ArrayList<>();
    private  RecyclerView rv;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self);


        getSupportActionBar().hide();

        SearchView searchView = findViewById(R.id.searchView_self);
        searchView.setIconifiedByDefault(true);
        searchView.onActionViewExpanded();

        importDataBase();//從apk封包/res/raw中導入資料庫檔案
        setDataBase();
        setRecycleView();


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //當按下確定鍵
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //當輸入框文字有變動時
                result.clear();

                for(int i=0;i<list.size();i++)
                {
                    //在list資料中搜尋含有searchView字串的資料
                    if(list.get(i).getName().contains(newText))
                    {
                        result.add(list.get(i));
                    }

                    setRecycleViewWithResult();
                }

                return false;
            }
        });

    }


    public void setRecycleView()//recycleView初始化
    {
        rv = findViewById(R.id.self_list_show);

        rv.setLayoutManager(new LinearLayoutManager(this));

        list_self_adapter adapter = new list_self_adapter(this,list);

        rv.setAdapter(adapter);
    }


    //顯示
    public void setRecycleViewWithResult()
    {
        list_self_adapter adapter = new list_self_adapter(this,result);

        rv.setAdapter(adapter);
    }

    public void setDataBase()
    {
        DataBaseHelper dataHelper = new DataBaseHelper(this, "self.db", null, 1);
        SQLiteDatabase db = dataHelper.getWritableDatabase();

        //依據條件搜尋SQLite，並回傳資料指標
        Cursor c = db.rawQuery("SELECT * FROM simapro", null);
        c.moveToFirst();

        float testCO2=0;
        String unit="",number="";

        for(int i = 0; i < c.getCount(); i++) {
            //取出欄位索引值加到字串後

            try{
                number = c.getString(3);
                unit = " "+c.getString(4);

                testCO2 = Float.valueOf(number);
            }
            catch (NumberFormatException e)
            {
                c.moveToNext();
                continue;
            }

            //Log.d("Detail", c.getString(1)+":"+c.getString(3));
            String name = c.getString(1);
            String init = c.getString(2);

            Log.d("Detail", name+":"+unit);


            if(!init.equals(""))
            {
                name = init;
            }


            list.add(new list_item_self(name, testCO2,unit));
            //移動至下一筆
            c.moveToNext();
        }

        c.close();
    }

    public void importDataBase() {

        String dirPath="/data/data/com.nian.carbout/databases";//資料庫目錄
        File dir = new File(dirPath);

        if(!dir.exists()) {
            dir.mkdir();
        }

        File file = new File(dir, "self.db");//目標檔案名稱

        try {

            if(!file.exists()) file.createNewFile();//創建目標複製檔案
            else return;

            //載入/res/raw中的資料庫檔案
            InputStream is = this.getApplicationContext().getResources().openRawResource(R.raw.self);
            FileOutputStream fos = new FileOutputStream(file);
            byte[] buffere=new byte[is.available()];
            is.read(buffere);
            fos.write(buffere);
            is.close();
            fos.close();

        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
