package com.project.community.listener;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.util.Collections;
import java.util.List;

/**
 * Created by qizfeng on 17/8/17.
 */

public class RecyclerItemTouchHelperCallBack extends ItemTouchHelper.Callback {
    public RecyclerView recyclerView;
    public List mListData;

    public RecyclerItemTouchHelperCallBack(RecyclerView recyclerView, List mListData) {
        this.recyclerView = recyclerView;
        this.mListData = mListData;
    }

    /**
     * 拖动标识
     */
    private int dragFlags;
    /**
     * 删除滑动标识
     */
    private int swipeFlags;

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        dragFlags = 0;
        swipeFlags = 0;
        if (recyclerView.getLayoutManager() instanceof GridLayoutManager
                || recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
            dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN
                    | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        } else {
            dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            if (viewHolder.getAdapterPosition() != 0)
                swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        }
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        int fromPosition = viewHolder.getAdapterPosition();
        int toPosition = target.getAdapterPosition();
        if (toPosition != 0) {
            if (fromPosition < toPosition)
                //向下拖动
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(mListData, i, i + 1);
                }
            else {
                //向上拖动
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(mListData, i, i - 1);
                }
            }
            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
        }
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        int positon = viewHolder.getAdapterPosition();
        recyclerView.getAdapter().notifyItemRemoved(positon);
        mListData.remove(positon);
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            viewHolder.itemView.setPressed(true);
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        viewHolder.itemView.setPressed(false);
    }
}
