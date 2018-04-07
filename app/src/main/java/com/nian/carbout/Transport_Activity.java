package com.nian.carbout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    private int again=0;
    private DBhelper dataHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport);

        // The database is not actually created or opened
        // until one of getWritableDatabase() or getReadableDatabase() is called.
        // #open a SQLite-file named "co2.sqlite"
        dataHelper = new DBhelper(this, "co2.sqlite",null, 1);

        db = dataHelper.getWritableDatabase();

        final Spinner spinner_transport = findViewById(R.id.spinner_choose_transport);

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
                int View_mode;

                if(position==0) View_mode=View.VISIBLE;//台鐵模式
                else View_mode=View.GONE;//捷運模式

                findViewById(R.id.spinner_choose_TRA).setVisibility(View_mode);
                findViewById(R.id.TRA_textView).setVisibility(View_mode);
                findViewById(R.id.PayWay_RadioGroup).setVisibility(View_mode);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        final Spinner spinner_TRA = findViewById(R.id.spinner_choose_TRA);

        ArrayAdapter<CharSequence> TRA_Adapter = ArrayAdapter.createFromResource(
                this, R.array.TRA_item, android.R.layout.simple_spinner_item);

        TRA_Adapter.setDropDownViewResource(R.layout.spinner_style);

        spinner_TRA.setAdapter(TRA_Adapter);

        //只能用SelectedListener，用ClickedListener會閃退
        spinner_TRA.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TRA_answer=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //確認鍵
        Button button = findViewById(R.id.button_in_transport);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText et = findViewById(R.id.ticket_price);
                String input = et.getText().toString();
                String notify,title;

                //trim()回傳去除首尾空白符號的子字串
                if("".equals(et.getText().toString().trim()))//如果輸入為空
                {
                    notify="請輸入票價";
                    title="提醒";
                    again=1;
                    dialogShow(v, title, notify);
                }
                else
                {
                    int price = Integer.parseInt(input);
                    float co2 = calculate_co2(price);

                    title="計算結果";
                    again=0;

                    if(co2>1)
                    {
                        notify = "本次搭乘共消耗 " + (int)co2 +" kg";
                    }
                    else if(co2>=0.01)
                    {
                        notify = "本次搭乘共消耗 " + co2 +" kg";
                    }
                    else
                    {
                        notify = "由於碳足跡過低，本次計算結果不列入";
                    }

                    saveData(co2*1000);
                    dialogShow(v, title, notify);
                }

            }
        });
    }

    public void saveData(float co2)
    {
        Long id = dataHelper.append(db, (int)co2, (transport_answer==0)?"台鐵":"捷運");
        Toast.makeText(Transport_Activity.this, "ID: "+ id, Toast.LENGTH_SHORT).show();
    }


    public void onSelect(View view)
    {
        payWay_answer=view.getId();
    }

    public void dialogShow(View v, String title, String notify)
    {
        new AlertDialog.Builder(v.getContext())
                .setTitle(title)
                .setMessage(notify)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(again==0)
                        {
                            db.close();
                            Transport_Activity.this.finish();//計算完成後結束現在的activity
                        }
                    }
                })
                .show();
    }

    public float calculate_co2(int price)
    {
        float co2=0;

        if(transport_answer==0)//台鐵
        {
            if(payWay_answer==R.id.cash_pay)//現金支付
            {
                switch(TRA_answer)
                {
                        case 0://站票
                            co2 = price/1.46F*54;
                            break;
                        case 1://普快
                            co2 = price/1.06F*54;
                            break;
                        case 2://復興/區間
                            co2 = price/1.46F*54;
                            break;
                        case 3://莒光
                            co2 = price/1.75F*54;
                            break;
                        case 4://自強
                            co2 = price/2.27F*54;
                            break;
                }
            }
            else if(payWay_answer==R.id.electronic_pay)//電子票證
            {
                if(TRA_answer==4)//自強
                {
                    if(price<=92) co2 = price/0.9F/1.46F*54;
                    else co2 = 70*54 + (price-92)/0.9F/1.46F*54;
                }
                else//其他
                {
                    co2 = price/0.9F/1.46F*54;//電子票證九折優惠
                }
            }
        }
        else //捷運
        {
            co2 = ((price/5-4)*3 + 3.5F)*0.08F;
        }

        return co2;
    }

}
