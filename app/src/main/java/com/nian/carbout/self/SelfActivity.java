package com.nian.carbout.self;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.nian.carbout.DataBaseHelper;
import com.nian.carbout.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class SelfActivity extends AppCompatActivity {


    private  ArrayList<list_item_self> list = new ArrayList<>();
    private  ArrayList<list_item_self> result = new ArrayList<>();
    private  RecyclerView rv;
    private  TextView tv;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self);

        tv = findViewById(R.id.textView_result);



        getSupportActionBar().hide();

        SearchView searchView = findViewById(R.id.searchView_self);
        searchView.setIconifiedByDefault(true);
        searchView.onActionViewExpanded();

        setDataBase();
        setRecycleView();
        setButton();


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

    public void setButton()
    {
        Button OK = findViewById(R.id.check_self);

        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                float co2=0;

                String text = tv.getText().toString();

                if(text.trim().equals(""))
                {
                    Toast.makeText(SelfActivity.this,"原料與製程不可為空。請從下方清單找到欲加入的項目，點擊它並輸入用量。",Toast.LENGTH_LONG).show();
                    return;
                }

                String[] lines = text.split("\n");
                String[] items;

                //Log.d("test", ""+lines.length);

                for(int i=0;i<lines.length;i++)
                {
                    //名稱 co2 單位
                    Scanner scanner = new Scanner(lines[i]);
                    scanner.next();
                    co2 += Float.parseFloat(scanner.next());
                }

                Toast.makeText(v.getContext(),""+co2,Toast.LENGTH_LONG).show();

                final EditText et = new EditText(SelfActivity.this);

                final float finalCo = co2;
                final float finalCo1 = co2;
                new AlertDialog.Builder(v.getContext())
                        .setView(et)
                        .setTitle("新增單品")
                        .setMessage("\n碳足跡總額為:"+co2+"\n請輸入新單品的名稱")
                        .setNeutralButton("取消",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String NewName = et.getText().toString();

                                if(NewName.trim().equals(""))
                                {
                                    Toast.makeText(SelfActivity.this,"名稱不能為空",Toast.LENGTH_LONG).show();
                                }
                                else
                                {
                                    ResourseAppend(NewName, finalCo1);
                                    Toast.makeText(SelfActivity.this,"成功建立新單品\n名稱："+NewName+"\n碳足跡："+ finalCo,Toast.LENGTH_LONG).show();
                                    SelfActivity.this.finish();
                                }

                            }
                        })
                        .show();

            }
        });
    }



    public void ResourseAppend(String name, float co2)
    {
        NewItemDBHelper DBHelper = new NewItemDBHelper(this,"resource.db",null,1);
        SQLiteDatabase db = DBHelper.getWritableDatabase();

        DBHelper.append(db,"自訂",name,co2);
    }


    public void setRecycleView()//recycleView初始化
    {
        rv = findViewById(R.id.self_list_show);

        rv.setLayoutManager(new LinearLayoutManager(this));

        list_self_adapter adapter = new list_self_adapter(this,list,tv);

        rv.setAdapter(adapter);
    }


    //顯示
    public void setRecycleViewWithResult()
    {
        list_self_adapter adapter = new list_self_adapter(this,result,tv);

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

}
