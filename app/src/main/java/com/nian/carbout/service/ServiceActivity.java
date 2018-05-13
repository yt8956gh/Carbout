package com.nian.carbout.service;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.nian.carbout.R;
import com.nian.carbout.waste.WasteActivity;

public class ServiceActivity extends AppCompatActivity {


    private Spinner spinner;
    private int detail_answer=0,leave=0;

    private int service_category=R.id.hotel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        getSupportActionBar().hide();
        setupSpinner();
        setupButton();
    }


    public void setupSpinner()
    {
        spinner = findViewById(R.id.spinner_choose_service_detail);
        ArrayAdapter<CharSequence> wAdapter;
        TextView tv =findViewById(R.id.service_detail);
        TextView tv2 =findViewById(R.id.service_detail2);
        TextView tv_unit =findViewById(R.id.service_unit);




        if(service_category==R.id.hotel)
        {
            wAdapter = ArrayAdapter.createFromResource(
                    this,R.array.hotel_item, android.R.layout.simple_spinner_item);

            tv.setText("住宿類型");
            tv2.setText("住宿天數");
            tv_unit.setText("天");
        }
        else if(service_category==R.id.shipment)
        {
            wAdapter = ArrayAdapter.createFromResource(
                    this,R.array.shipment_item, android.R.layout.simple_spinner_item);
            tv.setText("交通工具");
            tv2.setText("距離");
            tv_unit.setText("km");
    }
        else
        {
            wAdapter = ArrayAdapter.createFromResource(
                    this,R.array.airLift_item, android.R.layout.simple_spinner_item);
            tv.setText("交通工具");
            tv2.setText("距離");
            tv_unit.setText("km");
        }

        wAdapter.setDropDownViewResource(R.layout.spinner_style);

        spinner.setAdapter(wAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                detail_answer=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    public void setupButton()
    {
        Button button = findViewById(R.id.button_in_service);
        Button cancel = findViewById(R.id.button_in_service_cancel);

        cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ServiceActivity.this.finish();
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText et = findViewById(R.id.edit_service);
                String input = et.getText().toString();
                String notify,title;



                if("".equals(et.getText().toString().trim()))//如果輸入為空
                {
                    notify="請輸入數據";
                    title="提醒";
                    leave=0;
                    dialogShow(v, title, notify);
                }
                else
                {
                    int usage = Integer.parseInt(input);
                    //float co2 = calculate_co2(usage);

                    leave=1;

                    title="計算結果";

                    notify="本次碳足跡共2510kg";

                    //因為co2是float型態，所以必須cast成int

                    dialogShow(v,title,notify);
                }
            }
        });
    }

    public void dialogShow(View v, String title, String notify)
    {
        new AlertDialog.Builder(v.getContext())
                .setTitle(title)
                .setMessage(notify)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(leave==1) ServiceActivity.this.finish();//計算完成後結束現在的activity

                    }
                })
                .show();
    }



    public void onSelect(View view)
    {
        service_category=view.getId();
        setupSpinner();
    }
}
