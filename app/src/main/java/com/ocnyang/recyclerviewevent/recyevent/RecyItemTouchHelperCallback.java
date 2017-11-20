package com.ocnyang.recyclerviewevent.recyevent;

import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.ocnyang.recyclerviewevent.RecyAdapter;

import java.util.Collections;

/*******************************************************************
 *    * * * *   * * * *   *     *       Created by OCN.Yang
 *    *     *   *         * *   *       Time:2017/8/2 11:37.
 *    *     *   *         *   * *       Email address:ocnyang@gmail.com
 *    * * * *   * * * *   *     *.Yang  Web site:www.ocnyang.com
 *******************************************************************/


public class RecyItemTouchHelperCallback extends ItemTouchHelper.Callback {
    RecyclerView.Adapter mAdapter;
    boolean isSwipeEnable;
    boolean isFirstDragUnable;

    /**
     * list
     */
    public RecyItemTouchHelperCallback(RecyclerView.Adapter adapter) {
        mAdapter = adapter;
        isSwipeEnable = true;
        isFirstDragUnable = false;
    }

    /**
     * grid
     */
    public RecyItemTouchHelperCallback(RecyclerView.Adapter adapter, boolean isSwipeEnable, boolean isFirstDragUnable) {
        mAdapter = adapter;
        this.isSwipeEnable = isSwipeEnable;
        this.isFirstDragUnable = isFirstDragUnable;
    }

    /**
     * 移动事件的标志
     */
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {// grid
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;// 系统属性
            int swipeFlags = 0;// 没有侧滑删除
            return makeMovementFlags(dragFlags, swipeFlags);// 系统方法
        } else {// list
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;// 系统属性
            int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;// 有侧滑删除
            return makeMovementFlags(dragFlags, swipeFlags);// 系统方法
        }
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolderSrc, RecyclerView.ViewHolder viewHolderDes) {
        // 起止位置
        int fromPosition = viewHolderSrc.getAdapterPosition();
        int toPosition = viewHolderDes.getAdapterPosition();

        // 第一个不能移动，或者移动到第一个
        if (isFirstDragUnable && toPosition == 0) {
            return false;
        }

        if (fromPosition < toPosition) {// 往后面移动
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(((RecyAdapter) mAdapter).getDataList(), i, i + 1);
            }
        } else {// 往前面移动
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(((RecyAdapter) mAdapter).getDataList(), i, i - 1);
            }
        }

        // 移动的更新
        mAdapter.notifyItemMoved(fromPosition, toPosition);
//        mAdapter.notifyDataSetChanged(); // 如果用这个，到下个位置，自动给更新了，只能移动一位
        return true;
    }

    /**
     * 侧滑
     * 刷新，移除数据 （顺序无所谓，先刷新并不符合逻辑）
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        int adapterPosition = viewHolder.getAdapterPosition();
        mAdapter.notifyItemRemoved(adapterPosition);// 1 先刷新
        ((RecyAdapter) mAdapter).getDataList().remove(adapterPosition);// 2 再移除数据
//        mAdapter.notifyDataSetChanged(); // 如果用这个，图片不会更新，只更新了名字

    }

    /**
     *  Called when the ViewHolder swiped or dragged by the ItemTouchHelper is changed.
     */
    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {// 选中后，还没有归位
            viewHolder.itemView.setBackgroundColor(Color.RED);
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    /**
     * Called by the ItemTouchHelper when the user interaction with an element is over and it
     * also completed its animation.
     *  选中后，归位完成后
     */
    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        viewHolder.itemView.setBackgroundColor(Color.GREEN);
    }

    /**
     * 是否支持长按
     */
    @Override
    public boolean isLongPressDragEnabled() {
        return !isFirstDragUnable;
    }

    /**
     * 是否支持侧滑
     */
    @Override
    public boolean isItemViewSwipeEnabled() {
        return isSwipeEnable;
    }
}
