package com.nian.carbout.waste;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nian.carbout.DataBaseHelper;
import com.nian.carbout.R;

public class WasteActivity extends AppCompatActivity {

    private int waste_category=R.id.incineration,detail_answer=0,leave=0;
    private Spinner spinner;
    private DataBaseHelper dataHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waste);

        getSupportActionBar().hide();

        dataHelper = new DataBaseHelper(this, "co2.sqlite",null, 1);
        db = dataHelper.getWritableDatabase();

        setupSpinner();
        setupButton();
    }



    public void setupButton()
    {
        Button button = findViewById(R.id.button_in_waste);
        Button cancel = findViewById(R.id.button_in_waste_cancel);

        cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                WasteActivity.this.finish();
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText et = findViewById(R.id.edit_waste);
                String input = et.getText().toString();
                String notify,title;



                if("".equals(et.getText().toString().trim()))//如果輸入為空
                {
                    notify="請輸入用量";
                    title="提醒";
                    leave=0;
                    dialogShow(v, title, notify);
                }
                else
                {
                    int usage = Integer.parseInt(input);
                    float co2 = calculate_co2(usage);

                    leave=1;

                    title="計算結果";

                    //因為co2是float型態，所以必須cast成int
                    if(co2>=1)
                    {
                        notify = "處理廢棄物共產生 " + (int)co2 +" kg的碳足跡";
                        saveData(co2*1000);
                    }
                    else if(((int)(co2*100))!=0) //大於0.01kg的
                    {
                        notify = "處理廢棄物共產生 " + (int)(co2*1000) +" g的碳足跡";
                        saveData(co2*1000);
                    }
                    else
                    {
                        notify = "由於碳足跡過低，本次計算結果不列入";
                    }

                    dialogShow(v,title,notify);
                }
            }
        });
    }


    public void saveData(float co2)
    {
        String[] item_array;
        String item_name;
        Long id;

        if(waste_category==R.id.incineration)
        {
            item_array = getResources().getStringArray(R.array.incineration_item);
            item_name = item_array[detail_answer];
            id = dataHelper.append(db, (int)co2,item_name+"焚化爐");
        }
        else if(waste_category==R.id.waste_water)
        {
            id = dataHelper.append(db, (int)co2,"廢水");
        }
        else//recover
        {
            id = dataHelper.append(db, (int)co2,"回收");
        }

        Toast.makeText(WasteActivity.this, "ID: "+ id, Toast.LENGTH_SHORT).show();
    }

    public float calculate_co2(int usage)
    {
        if(waste_category==R.id.incineration)
        {
            switch (detail_answer)
            {
                case 0:
                    return 0.606F*usage;
                case 1:
                    return 0.737F*usage;
                case 2:
                    return 0.33F*usage;
            }
        }
        else if(waste_category==R.id.waste_water)
        {
            return  0.000033F*usage;
        }


        return  0;//recover
    }


    public void setupSpinner()
    {
        spinner = findViewById(R.id.spinner_choose_waste_detail);
        ArrayAdapter<CharSequence> wAdapter;
        TextView tv =findViewById(R.id.waste_detail);


        if(waste_category==R.id.incineration)
        {
            wAdapter = ArrayAdapter.createFromResource(
                    this,R.array.incineration_item, android.R.layout.simple_spinner_item);

            tv.setText("焚化爐\n位　置");
        }
        else if(waste_category==R.id.waste_water)
        {
            wAdapter = ArrayAdapter.createFromResource(
                    this,R.array.waste_water_item, android.R.layout.simple_spinner_item);
            tv.setText("類型");
        }
        else
        {
            wAdapter = ArrayAdapter.createFromResource(
                    this,R.array.recover_item, android.R.layout.simple_spinner_item);
            tv.setText("類型");
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


    public void dialogShow(View v, String title, String notify)
    {
        new AlertDialog.Builder(v.getContext())
                .setTitle(title)
                .setMessage(notify)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(leave==1) WasteActivity.this.finish();//計算完成後結束現在的activity

                    }
                })
                .show();
    }

    public void onSelect(View view)
    {
        waste_category=view.getId();
        setupSpinner();
    }
}

