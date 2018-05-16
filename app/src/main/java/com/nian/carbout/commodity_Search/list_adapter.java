package com.nian.carbout.commodity_Search;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nian.carbout.DataBaseHelper;
import com.nian.carbout.R;
import com.nian.carbout.commodity.CommodityActivity;

import java.util.ArrayList;

public class list_adapter extends RecyclerView.Adapter<list_adapter.ViewHolder>{

    private ArrayList<list_item> data;
    private Context myContext;
    private SQLiteDatabase db;
    private DataBaseHelper dataHelper;

    public list_adapter(Context myContext,ArrayList<list_item> data,DataBaseHelper dataHelper)
    {
        this.data = data;
        this.myContext = myContext;
        this.dataHelper = dataHelper;
        db = dataHelper.getWritableDatabase();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext)
                .inflate(R.layout.list_cell, parent, false);

        list_adapter.ViewHolder holder = new list_adapter.ViewHolder(view);

        holder.item_name = view.findViewById(R.id.list_name);
        holder.item_co2 = view.findViewById(R.id.list_co2);

        return holder;
    }

    @Override
    public void onBindViewHolder(list_adapter.ViewHolder holder, final int position) {

        String total = String.valueOf(data.get(position).getCo2())+ data.get(position).getUnit();

        holder.item_name.setText(data.get(position).getName());
        holder.item_co2.setText(total);

        holder.itemView.setOnClickListener(new View.OnClickListener(){

            @Override public void onClick(View v) {

                final int unit = (data.get(position).getUnit().equals("kg"))?1000:1;

                String name = data.get(position).getName().replace("\n","\n規格:");
                String co2_unit = data.get(position).getCo2()+data.get(position).getUnit();

                new AlertDialog.Builder(v.getContext())
                        .setTitle("確定要新增?\n")
                        .setMessage("品名:"+name+"\n碳足跡:"+co2_unit)
                        .setNeutralButton("取消",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dataHelper.append(db, (int)data.get(position).getCo2()*unit, data.get(position).getName());
                            }
                        })
                        .show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView item_name;
        public TextView item_co2;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
