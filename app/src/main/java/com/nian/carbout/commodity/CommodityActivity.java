package com.nian.carbout.commodity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.vision.barcode.Barcode;
import com.nian.carbout.DataBaseHelper;
import com.nian.carbout.R;
import com.nian.carbout.commodity_Search.CommoditySearchActivity;
import com.nian.carbout.commodity_Search.list_item;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import info.debatty.java.stringsimilarity.JaroWinkler;

public class CommodityActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST = 200;
    private static final int REQEUST_QR = 100;
    private ArrayList<shop_list> Commodity_item = new ArrayList<>();
    private ArrayList<list_item> Commodity_DB = new ArrayList<>();
    private DataBaseHelper dataHelper;
    private SQLiteDatabase db;
    private int date=0,have_date=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commodity);

        try{
            getSupportActionBar().hide();
        }
        catch(NullPointerException e)
        {
            e.printStackTrace();
        }





        CardView card = findViewById(R.id.cardView_of_QR);
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(v.getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(CommodityActivity.this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST);
                } else {
                    Intent intent = new Intent(CommodityActivity.this, QR_Activity.class);
                    startActivityForResult(intent, REQEUST_QR);
                }

            }
        });

        CardView card_search = findViewById(R.id.cardView_of_search);
        card_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    startActivity(new Intent(v.getContext(), CommoditySearchActivity.class));
            }
        });

        importDataBase();
        setDataBase();
    }

    public void dialogShow(Context context, String title, String notify)
    {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(notify)
                .setNeutralButton("離開",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                CommodityActivity.this.finish();//計算完成後結束現在的activity

                    }
                })
                .setPositiveButton("繼續", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    public void decode_QR(String str)
    {
        String title = "掃描結果",notify;
        int index_start=0;
        Commodity_item.clear();

        str = str.trim();//去除前後空白

        //正規表示法regex
        String regex_left = "^[a–zA-z0-9]{10}[0-9]{7}[0-9]{4}[0-9a-z]{8}[0-9a-z]{8}[0-9]{8}[0-9]{8}.{24}(:.{10}:[0-9]+:[0-9]+:[0-9]+(:.+:[0-9.]+:[0-9.]+)*:*)?";
        String regex_right = "^\\*\\*(:*.+:[0-9.]+:[0-9.]+)*:*";

        if(str.matches(regex_left))//左側QR code
        {
            have_date=1;

            Toast.makeText(getApplicationContext(),"左側QR code掃描成功",Toast.LENGTH_LONG).show();

            //String receipt_number = str.substring(0,10);//發票號碼，暫時不會用到
            String receipt_date = Integer.parseInt(str.substring(10,13))+1911+str.substring(13,15)+str.substring(15,17);

            date = Integer.parseInt(receipt_date);

            String[] split_item = str.split(":");

            if(split_item.length>2)//切割品項
            {
                for(int i=5;i<split_item.length;i+=3)
                {
                    Commodity_item.add(new shop_list(split_item[i],Integer.parseInt(split_item[i+1])));
                }
            }

        }
        else if(str.matches(regex_right))//右側 QR code
        {
            have_date=0;

            Toast.makeText(getApplicationContext(),"右側QR code掃描成功",Toast.LENGTH_LONG).show();

            if(str.equals("**"))
            {
                notify="右側QR code無記載內容";
                dialogShow(CommodityActivity.this, title, notify);
                return;
            }

            if(str.indexOf(2)==':')//辨識開頭是**:
                str=str.replace("**","");
            else//還是**
                str=str.replace("**:","");

            String[] split_item = str.split(":");

            for(int i=index_start;i<split_item.length;i+=3)
            {
                Commodity_item.add(new shop_list(split_item[i],Integer.parseInt(split_item[i+1])));
            }

            //dialogShow(CommodityActivity.this, title, notify);
        }
        else
        {
            Toast.makeText(getApplicationContext(),"請掃描正確的電子發票QR code",Toast.LENGTH_LONG).show();
            return;
        }

        search_in_DB();
    }

    public void search_in_DB()
    {
        int unit_change,index,max_index=0;
        double[] similarity = new double[Commodity_DB.size()];
        double max=0f;
        String title = "計算結果";
        StringBuilder notify = new StringBuilder();
        JaroWinkler jw = new JaroWinkler();//使用java-string-similarity實例


        if(have_date==1) notify.append("購買日期:")
                .append(date/10000)
                .append("/")
                .append(date%10000/100)
                .append("/")
                .append(date%100)
                .append("\n\n");

        //Commodity_item;
        for(shop_list item: Commodity_item)
        {
            //Log.d("shop list", item.getName()+ " "+ item.getNumber());
            index=0;

            //與資料庫中的品名進行比對
            for(list_item item_DB : Commodity_DB)
            {
                similarity[index] = jw.similarity(item.getName(),item_DB.getName());
                //Log.d("相似度"+item.getName(), item_DB.getName()+ ":" + similarity[index]);
                index++;
            }

            //找到最大相似度
            for(int i=0;i<similarity.length;i++)
            {
                if(max<similarity[i])
                {
                    max = similarity[i];
                    max_index = i;
                }
            }

            Log.d("MAX Similarity", Commodity_DB.get(max_index).getName()+" : "+max);

            notify.append(item.getName())
                    .append(" * ")
                    .append(item.getNumber())
                    .append("    ");

            if(max <= 0.75f) notify.append("暫無數據\n");//相似度太低
            else notify.append(Commodity_DB.get(max_index).getCo2()).append(Commodity_DB.get(max_index).getUnit()).append("\n");

            //存入DB前，先進行單位轉換
            unit_change = (Commodity_DB.get(max_index).getUnit().equals("kg"))?1000:1;
            //寫入DB
            dataHelper.append(
                    db, (int)Commodity_DB.get(max_index).getCo2()*unit_change*item.getNumber()
                    ,Commodity_DB.get(max_index).getName(),date);
        }

        dialogShow(CommodityActivity.this, title, notify.toString());
    }

    public void setDataBase()
    {
        dataHelper = new DataBaseHelper(this, "resource.db", null, 1);
        db = dataHelper.getWritableDatabase();

        //依據條件搜尋SQLite，並回傳資料指標
        Cursor c = db.rawQuery("SELECT * FROM Commodity", null);
        c.moveToFirst();

        float testCO2;
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
                name = name + spec;
            }

            Log.d("Item", name);


            Commodity_DB.add(new list_item(name, testCO2,unit));
            //移動至下一筆
            c.moveToNext();
        }

        c.close();


        dataHelper = new DataBaseHelper(this, "co2.sqlite",null, 1);
        db = dataHelper.getWritableDatabase();
    }

    public void importDataBase() {

        String dirPath="/data/data/com.nian.carbout/databases";//資料庫目錄
        File dir = new File(dirPath);

        if(!dir.exists()) {
            dir.mkdir();
        }

        File file = new File(dir, "resource.db");//目標檔案名稱

        try {

            if(!file.exists()) file.createNewFile();//創建目標複製檔案
            else return;

            //載入/res/raw中的資料庫檔案
            InputStream is = this.getApplicationContext().getResources().openRawResource(R.raw.resource);
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



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQEUST_QR && resultCode == RESULT_OK) {
            if (data != null) {
                final Barcode barcode = data.getParcelableExtra("barcode");
                decode_QR(barcode.displayValue);
            }
        }
    }
}