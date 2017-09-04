package com.project.community.view;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.library.okgo.utils.LogUtils;

/**
 * Created by qizfeng on 2015/7/27.
 * RecyclerView分割线
 */
public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

    private int space;
    private boolean hasHeader = false;//是否有头部

    public SpacesItemDecoration(int space, boolean hasHeader) {
        this.space = space;
        this.hasHeader = hasHeader;
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = space;
        outRect.right = space;
        outRect.bottom = space * 2;
        int position = parent.getChildAdapterPosition(view);
        if (hasHeader) {
            if (position == 0) {//头部
                outRect.bottom = space * 2;
                outRect.left = 0;
                outRect.right = 0;
            } else {
                try {
                     StaggeredGridLayoutManager.LayoutParams slp = (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
                    // slp.getSpanIndex(): 这个可以拿到它在同一行排序的真实顺序
                    if (slp.getSpanIndex() == 0) {
                        outRect.left = space * 2;
                        outRect.right = space;
                        outRect.bottom = space * 2;
                    } else {
                        outRect.left = space;
                        outRect.right = space * 2;
                        outRect.bottom = space * 2;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        } else {
            try {
                final StaggeredGridLayoutManager.LayoutParams slp = (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
                // slp.getSpanIndex(): 这个可以拿到它在同一行排序的真实顺序
                if (slp.getSpanIndex() == 0) {
                    outRect.left = space * 2;
                    outRect.right = space;
                    outRect.bottom = space * 2;
                } else {
                    outRect.left = space;
                    outRect.right = space * 2;
                    outRect.bottom = space * 2;
                }
            } catch (Exception e) {
               // e.printStackTrace();
            }
        }
    }
}
