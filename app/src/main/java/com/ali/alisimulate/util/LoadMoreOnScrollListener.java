package com.ali.alisimulate.util;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class LoadMoreOnScrollListener extends RecyclerView.OnScrollListener{
    private LinearLayoutManager mLinearLayoutManager;

    private int currentPage = 1;

    /**
     * 已经加载出来的item个数
     */
    private int totalItemCount;

    /**
     * 上一个totalItemCount
     */
    private int previousTotal = 0;

    /**
     * 屏幕上可见item个数
     */
    private int visibleItemCount;

    /**
     * 屏幕可见item的第一个
     */
    private int firstVisiableItem;

    /**
     * 是否正在上拉数据
     */
    private boolean isPulling = true;

    public LoadMoreOnScrollListener(LinearLayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        // 可见item个数
        visibleItemCount = recyclerView.getChildCount();
        // item总数
        totalItemCount = mLinearLayoutManager.getItemCount();
        // 第一个可见item
        firstVisiableItem = mLinearLayoutManager.findFirstVisibleItemPosition();

        if (isPulling) {
            if (totalItemCount > previousTotal) {
                // 数据成功获取才会执行 说明数据已经加载结束
                isPulling = false;
                previousTotal = totalItemCount;
            }
        }

        //如果获取数据失败，则不会这行此处，因为loading始终为true
        //当最后一个item可见时，执行加载
        if (!isPulling && totalItemCount - visibleItemCount <= firstVisiableItem) {
            currentPage ++;
            onLoadMore(currentPage);
            isPulling = true;
        }
    }

    public abstract void onLoadMore(int currentPage);
}
