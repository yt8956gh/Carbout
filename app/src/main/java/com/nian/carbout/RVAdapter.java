package com.nian.carbout;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;
/**
 * Created by 毛胤年 on 2018/2/13.
 */

public class RVAdapter
        extends RecyclerView.Adapter<RVAdapter.ViewHolder>
        implements ItemTouchHelperAdapter{

    // 儲存項目資料的 List 物件
    private List<Home_item> items;

    private int lastPosition = -1;
    private Context context;

    public RVAdapter(List<Home_item> items, Context context)
    {
        this.items = items;
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(
            ViewGroup parent, int viewType) {
        //建立包有item的畫片配置檔
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.cardview_in_main, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    // 使用 ViewHolder 包裝項目使用的畫面元件
    public class ViewHolder extends RecyclerView.ViewHolder {

        // 編號、名稱、說明與是否選擇
        protected TextView item_date;
        protected TextView item_name;
        protected TextView item_co2;
        //protected CheckBox selected_check;

        // 包裝元件
        protected View rootView;

        public ViewHolder(View view) {
            super(view);

            // 使用父類別 ViewHolder 宣告的「itemView」欄位變數，
            //   取得編號、名稱、說明與是否選擇元件
            item_co2 = (TextView)itemView.findViewById(R.id.item_co2);
            item_name = (TextView)itemView.findViewById(R.id.item_name);
            item_date = (TextView)itemView.findViewById(R.id.item_date);

            // 設定包裝元件
            rootView = view;
        }

    }



    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final Home_item home_item = items.get(position);

        holder.item_date.setText(Long.toString(home_item.getKey()));
        holder.item_date.setText(home_item.getName());
        holder.item_co2.setText(home_item.getCO2());

        setAnimation(holder.rootView, position);
    }

    private void setAnimation(View view, int position) {

        if(position > lastPosition) {

            Animation animation = AnimationUtils.loadAnimation(
                    context, android.R.anim.slide_in_left);

            view.startAnimation(animation);

            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {return items.size();}
    //新增項目
    public void add(Home_item item)
    {
        items.add(item);
        //通知已新增項目
        notifyItemInserted(items.size());
    }

    public void remove(int position)
    {
        items.remove(position);
        //通知已刪除項目
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, items.size());
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition)
    {
        if(fromPosition < toPosition)
        {
            for(int i = fromPosition; i < toPosition; i++)
            {
                Collections.swap(items, i, i+1);
            }
        }
        else
        {
            for(int i = toPosition; i < fromPosition; i--)
            {
                Collections.swap(items, i, i-1);
            }
        }

        notifyItemMoved(fromPosition, toPosition);
    }


    @Override
    public void onItemDismiss(int position)
    {
        remove(position);
    }

    /*
    public class ViewHolder extends RecyclerView.ViewHolder{

        protected TextView date_text;
        protected TextView co2_text;
        protected TextView name_text;

        protected View rootView;

        public ViewHolder(View itemView) {
            super(itemView);

            //取得xml中的元件後，包入ViewHolder
            date_text = (TextView) itemView.findViewById(R.id.item_date);
            name_text = (TextView) itemView.findViewById(R.id.item_name);
            co2_text = (TextView) itemView.findViewById(R.id.item_co2);

            rootView = itemView;
        }
    }*/
}
