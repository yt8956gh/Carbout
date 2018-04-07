package com.nian.carbout.news;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nian.carbout.R;
import com.nian.carbout.analysis.co2_item;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<news_item> mdata;

    public NewsAdapter(Context context, ArrayList<news_item> data)//取得呼叫端context
    {
        this.mContext = context;
        this.mdata = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.news_cell,parent,false);
        ViewHolder holder = new ViewHolder(view);
        holder.cell_title = view.findViewById(R.id.item_title);
        holder.cell_text = view.findViewById(R.id.item_text);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        news_item cell = mdata.get(position);

        holder.cell_title.setText(cell.getTitle());
        holder.cell_text.setText(cell.getText());
    }

    @Override//回傳陣列數量
    public int getItemCount() {
        return mdata.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView cell_title;
        public TextView cell_text;

        public ViewHolder(View itemView) {

            super(itemView);
        }
    }
}
