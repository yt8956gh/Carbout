package com.nian.carbout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class Transport_Activity extends AppCompatActivity {

    private int  transport_answer=0;
    private int  TRA_answer=0;
    private int  payWay_answer=R.id.cash_pay;//注意初始值


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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button button = (Button)findViewById(R.id.button_in_transport);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText et = (EditText)findViewById(R.id.ticket_price);
                String input = et.getText().toString();

                //trim()回傳去除首尾空白符號的子字串
                if("".equals(et.getText().toString().trim()))//如果輸入為空
                {
                    new AlertDialog.Builder(v.getContext())
                            .setTitle("提醒")
                            .setMessage("請輸入票價")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                }
                else
                {
                    int price = Integer.parseInt(input),co2 = 0;
                    co2 = calculate_co2(price);
                    new AlertDialog.Builder(v.getContext())
                            .setTitle("計算完成")
                            .setMessage("本次搭乘共消耗 "+co2+" Kg")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                }
            }
        });

        }

        //RadioGroup的執行方法
        public void onSelect(View view) {
            //RadioGroup rg = (RadioGroup) findViewById(R.id.PayWay_RadioGroup);
            payWay_answer=view.getId();
        }


        public int calculate_co2(int price)
        {
            int co2=0;

            if(transport_answer==0)//台鐵
            {
                if(payWay_answer==R.id.cash_pay)//現金支付
                {
                    switch(TRA_answer)
                    {
                        case 0:
                            co2 = (int)(price/1.46F*54);
                            break;
                        case 1:
                            co2 = (int)(price/1.06F*54);
                            break;
                        case 2:
                            co2 = (int)(price/1.46F*54);
                            break;
                        case 3:
                            co2 = (int)(price/1.75F*54);
                            break;
                        case 4:
                            co2 = (int)(price/2.27F*54);
                            break;
                    }
                }
                else if(payWay_answer==R.id.electronic_pay)//電子票證
                {
                    if(TRA_answer==4)//自強
                    {
                        if(price<=92) co2 = (int)(price/0.9F/1.46F*54);
                        else co2 = 70*54 + (int)((price-92)/0.9F/1.46F*54);
                    }
                    else//其他
                    {
                        co2 = (int)(price/0.9F/1.46F*54);//電子票證九折優惠
                    }
                }
            }
            else //捷運
            {
                co2 = (int)((price/5-4)*3 + 3.5F);
            }

            return co2;
        }

}
