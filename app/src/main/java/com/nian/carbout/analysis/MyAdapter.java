package com.nian.carbout.analysis;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nian.carbout.R;

import java.util.ArrayList;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<co2_item> mdata;
    private String unit;

    //取得呼叫端context與data
    public MyAdapter(Context context, ArrayList<co2_item> data)
    {
        this.mContext = context;
        this.mdata = data;
    }

    @Override  //將cell的xml與instance連結起來
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.co2_cell,parent,false);
        ViewHolder holder = new ViewHolder(view);
        holder.cell_co2 = view.findViewById(R.id.item_co2);
        holder.cell_name = view.findViewById(R.id.item_name);
        holder.cell_date = view.findViewById(R.id.item_date);
        holder.cell_unit = view.findViewById(R.id.item_unit);

        return holder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        co2_item cell = mdata.get(position);//取得「ArrayList<co2_item> mdata」中的資料
        int co2,date_number = cell.getDate();
        String date;

        date = date_number/10000 + "-" + date_number%10000/100 +"-" + date_number%100 ;

        //依據數字大小，顯示適當單位
        if((cell.getCO2()/1000.0F)>1)
        {
            co2 = (int)(cell.getCO2()/1000F);
            unit="kg";
        }
        else
        {
            co2 = cell.getCO2();
            unit=" g";
        }

        holder.cell_co2.setText(String.valueOf(co2));
        holder.cell_unit.setText(unit);
        holder.cell_date.setText(date);
        holder.cell_name.setText(cell.getName());
    }

    @Override//回傳陣列數量
    public int getItemCount() {
        return mdata.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView cell_co2;
        public TextView cell_name;
        public TextView cell_date;
        public TextView cell_unit;

        public ViewHolder(View itemView) {

            super(itemView);
        }
    }
}
