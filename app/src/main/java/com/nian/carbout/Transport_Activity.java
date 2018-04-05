package com.nian.carbout;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

public class Transport_Activity extends AppCompatActivity {

    private int  transport_answer=0;
    private int  TRA_answer=0;
    private int  payWay_answer=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport);

        final Spinner spinner_transport = (Spinner) findViewById(R.id.spinner_choose_transport);

        //設定SpinnerAdapter(context, 數據來源, 清單樣式)
        ArrayAdapter<CharSequence> tAdapter = ArrayAdapter.createFromResource(
                this, R.array.transport_item, android.R.layout.simple_spinner_item);

        //設定SpinnerAdapter清單中的字體樣式
        tAdapter.setDropDownViewResource(R.layout.spinner_style);

        spinner_transport.setAdapter(tAdapter);
        spinner_transport.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                transport_answer=position;

                if(position==0)//台鐵
                {
                    findViewById(R.id.spinner_choose_TRA).setVisibility(View.VISIBLE);
                    findViewById(R.id.TRA_textView).setVisibility(View.VISIBLE);
                    findViewById(R.id.PayWay_RadioGroup).setVisibility(View.VISIBLE);
                }
                else//捷運
                {
                    findViewById(R.id.spinner_choose_TRA).setVisibility(View.GONE);
                    findViewById(R.id.TRA_textView).setVisibility(View.GONE);
                    findViewById(R.id.PayWay_RadioGroup).setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final Spinner spinner_TRA = (Spinner) findViewById(R.id.spinner_choose_TRA);

        ArrayAdapter<CharSequence> TRA_Adapter = ArrayAdapter.createFromResource(
                this, R.array.TRA_item, android.R.layout.simple_spinner_item);

        TRA_Adapter.setDropDownViewResource(R.layout.spinner_style);

        spinner_TRA.setAdapter(TRA_Adapter);
        spinner_TRA.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TRA_answer=position;
                //Toast.makeText(view.getContext(), item_name, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button button = (Button)findViewById(R.id.button_in_transport);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String note = transport_answer+":"+TRA_answer+":"+
                        ((payWay_answer==R.id.cash_pay)?"Cash":"Elec");
                Toast.makeText(v.getContext(), note, Toast.LENGTH_SHORT).show();

                if(transport_answer==0)//台鐵
                {

                }
                else //捷運
                {

                }
            }
        });

        }

        //RadioGroup的執行方法
        public void onSelect(View view) {
            //RadioGroup rg = (RadioGroup) findViewById(R.id.PayWay_RadioGroup);
            payWay_answer=view.getId();
        }



}
