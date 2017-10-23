package com.project.community.ui.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.project.community.R;
import com.project.community.listener.RecycleItemClickListener;
import com.project.community.model.CommentModel;

import java.util.List;

/**
 * Created by cj on 17/9/25
 * 所有订单适配器
 */

public class AllOrderApdater extends BaseQuickAdapter<CommentModel, BaseViewHolder> {
    public RecycleItemClickListener itemClickListener;//点击事件

    public AllOrderApdater(List<CommentModel> data, RecycleItemClickListener itemClick) {
        super(R.layout.layout_item_all_order, data);
        itemClickListener = itemClick;
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, final CommentModel model) {
        baseViewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(view, baseViewHolder.getLayoutPosition());
            }
        });
        baseViewHolder.setOnClickListener(R.id.item_all_order_btn_type, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onCustomClick(view, baseViewHolder.getLayoutPosition());
            }
        });

    }
}
