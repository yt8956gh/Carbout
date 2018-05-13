package com.nian.carbout.self;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nian.carbout.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class list_self_adapter extends RecyclerView.Adapter<list_self_adapter.ViewHolder>{

    private ArrayList<list_item_self> data;
    private Context myContext;

    public list_self_adapter(Context myContext, ArrayList<list_item_self> data)
    {
        this.data = data;
        this.myContext = myContext;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext)
                .inflate(R.layout.list_cell, parent, false);

        list_self_adapter.ViewHolder holder = new list_self_adapter.ViewHolder(view);

        holder.item_name = view.findViewById(R.id.list_name);
        holder.item_co2 = view.findViewById(R.id.list_co2);

        return holder;
    }

    @Override
    public void onBindViewHolder(list_self_adapter.ViewHolder holder, int position) {

        DecimalFormat df=new DecimalFormat("#.#####");

        String total = df.format(data.get(position).getCo2()) + "\n" + data.get(position).getUnit();

        holder.item_name.setText(data.get(position).getName());
        holder.item_co2.setText(total);
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
