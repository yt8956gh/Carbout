package com.nian.carbout.commodity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.vision.barcode.Barcode;
import com.nian.carbout.R;
import com.nian.carbout.energy.EnergyActivity;

import java.io.UnsupportedEncodingException;

public class CommodityActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST = 200;
    private static final int REQEUST_QR = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commodity);
        getSupportActionBar().hide();


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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQEUST_QR && resultCode == RESULT_OK) {
            if (data != null) {
                final Barcode barcode = data.getParcelableExtra("barcode");

                String title = "掃描結果",notify="";

                String str = barcode.displayValue;

                str = str.trim();//去除前後空白

                Log.d("out", barcode.displayValue);

                String regex_left = "^[a–zA-z0-9]{10}[0-9]{7}[0-9]{4}[0-9a-z]{8}[0-9a-z]{8}[0-9]{8}[0-9]{8}.{24}(:.{10}:[0-9]+:[0-9]+:[0-9]+(:.+:[0-9.]+:[0-9.]+)*:*)?";
                String regex_right = "^\\*\\*(:*.+:[0-9.]+:[0-9.]+)*:*";
                if(str.matches(regex_left))
                {
                    Toast.makeText(getApplicationContext(),"左側QR code掃描成功",Toast.LENGTH_LONG).show();

                    String receipt_number = str.substring(0,10);
                    String receipt_date = Integer.parseInt(str.substring(10,13))+1911+"/"+str.substring(13,15)+"/"+str.substring(15,17);


                    notify = "購買日期:"+receipt_date+"\n\n";

                    String[] split_item = str.split(":");

                    for (String aSplit_item : split_item) {
                        Log.d("Item", aSplit_item);
                    }

                    if(split_item.length>2)//切品項
                    {
                        for(int i=5;i<split_item.length;i+=3)
                        {
                            notify = notify+split_item[i]+"\n";
                        }

                    }

                    dialogShow(CommodityActivity.this, title, notify);
                }
                else if(str.matches(regex_right))
                {
                    Toast.makeText(getApplicationContext(),"右側QR code掃描成功",Toast.LENGTH_LONG).show();

                    String[] split_item = str.split(":");

                    for(int i=1;i<split_item.length;i+=3)
                    {
                            notify = notify+split_item[i]+"\n";
                    }

                    dialogShow(CommodityActivity.this, title, notify);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"請掃描正確的電子發票QR code",Toast.LENGTH_LONG).show();
                    return;
                }





            }
        }
    }
}