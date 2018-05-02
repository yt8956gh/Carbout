package com.nian.carbout.energy;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.nian.carbout.DBhelper;
import com.nian.carbout.R;
import com.nian.carbout.transport.Transport_Activity;

import static android.view.View.GONE;

public class EnergyActivity extends AppCompatActivity {

    private int energy_category=R.id.water,detail_answer=0,leave=0;
    private Spinner spinner;
    private DBhelper dataHelper;
    private SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_energy);

        getSupportActionBar().hide();
        Toolbar toolbar = findViewById(R.id.toolbarEnergy);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        dataHelper = new DBhelper(this, "co2.sqlite",null, 1);
        db = dataHelper.getWritableDatabase();

        setupSpinner();
        setupButton();
    }


    public void setupButton()
    {
        Button button = findViewById(R.id.button_in_energy);
        Button cancel = findViewById(R.id.button_in_energy_cancel);

        cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                EnergyActivity.this.finish();
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText et = findViewById(R.id.edit_energy);
                String input = et.getText().toString();
                String notify,title,item_name;



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
                    if(((int)co2/1)!=0)
                    {
                        notify = "本次搭乘共消耗 " + (int)co2 +" kg";
                        saveData(co2*1000);
                    }
                    else if(((int)(co2*100))!=0) //大於0.01kg的
                    {
                        notify = "本次搭乘共消耗 " + (int)(co2*1000) +" g";
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

        if(energy_category==R.id.water)
        {
            item_array = getResources().getStringArray(R.array.water_item);
        }
        else if(energy_category==R.id.power)
        {
            item_array = getResources().getStringArray(R.array.power_item);
        }
        else
        {
            item_array = getResources().getStringArray(R.array.fuel_item);
        }

        item_name = item_array[detail_answer];

        Long id = dataHelper.append(db, (int)co2,item_name);
        //Toast.makeText(EnergyActivity.this, "ID: "+ id, Toast.LENGTH_SHORT).show();
    }

    public float calculate_co2(int usage)
    {
        if(energy_category==R.id.water)
        {
            return  0.167F*usage;
        }
        else if(energy_category==R.id.power)
        {
            switch (detail_answer)
            {
                case 0:
                    return 0.654F*usage;
                case 1:
                    return 0.00395F*usage;
                case 2:
                    return 0.00998F*usage;
            }
        }
        else //R.id.fuel
        {
            switch (detail_answer)
            {
                case 0:
                    return 2.61F*usage;
                case 1:
                    return 0.00998F*usage;
            }
        }


        return  0;
    }


    public void setupSpinner()
    {
        spinner = findViewById(R.id.spinner_choose_energy_detail);
        ArrayAdapter<CharSequence> eAdapter;


        if(energy_category==R.id.power)
        {
            eAdapter = ArrayAdapter.createFromResource(
                    this,R.array.power_item, android.R.layout.simple_spinner_item);
        }
        else if(energy_category==R.id.fuel)
        {
            eAdapter = ArrayAdapter.createFromResource(
                    this,R.array.fuel_item, android.R.layout.simple_spinner_item);
        }
        else
        {
            eAdapter = ArrayAdapter.createFromResource(
                    this,R.array.water_item, android.R.layout.simple_spinner_item);
        }

        eAdapter.setDropDownViewResource(R.layout.spinner_style);

        spinner.setAdapter(eAdapter);
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

                            if(leave==1) EnergyActivity.this.finish();//計算完成後結束現在的activity

                    }
                })
                .show();
    }

    public void onSelect(View view)
    {
        energy_category=view.getId();
        setupSpinner();
    }
}
