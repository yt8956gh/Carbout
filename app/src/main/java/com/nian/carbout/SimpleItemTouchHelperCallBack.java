package com.nian.carbout;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by 毛胤年 on 2018/3/4.
 */

// 項目操作類別, 繼承 ItemTouchHelper.Callback 類別
public class SimpleItemTouchHelperCallBack
        extends ItemTouchHelper.Callback {

    private final ItemTouchHelperAdapter adapter;

    public SimpleItemTouchHelperCallBack(ItemTouchHelperAdapter adapter)
    {
        this.adapter = adapter;
    }

    //開啟長按
    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    //開啟左右滑動
    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        adapter.onItemMove(viewHolder.getAdapterPosition(),target.getAdapterPosition());
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        adapter.onItemDismiss(viewHolder.getAdapterPosition());
    }
}
