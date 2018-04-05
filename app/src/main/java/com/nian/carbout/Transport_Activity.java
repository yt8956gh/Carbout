package com.nian.carbout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class Transport_Activity extends AppCompatActivity {

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
                String item_name = spinner_transport.getSelectedItem().toString();
                Toast.makeText(view.getContext(), item_name, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
