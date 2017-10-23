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
 * 已完成列表适配器
 */

public class ProductSellApdater extends BaseQuickAdapter<CommentModel, BaseViewHolder> {
    public RecycleItemClickListener itemClickListener;//点击事件

    public ProductSellApdater(List<CommentModel> data, RecycleItemClickListener itemClick) {
        super(R.layout.layout_item_product_sell, data);
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
        baseViewHolder.setOnClickListener(R.id.item_product_change, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onCustomClick(view, baseViewHolder.getLayoutPosition());
            }
        })
        .setOnClickListener(R.id.item_product_del, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onCustomClick(view, baseViewHolder.getLayoutPosition());
            }
        }).setOnClickListener(R.id.item_product_xia, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onCustomClick(view, baseViewHolder.getLayoutPosition());
            }
        }).setOnClickListener(R.id.item_product_deit, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        itemClickListener.onCustomClick(view, baseViewHolder.getLayoutPosition());
                    }
                });
        if (model.id.equals("0"))
            baseViewHolder.setVisible(R.id.item_product_change_box,false)
            .setImageResource(R.id.item_product_change,R.mipmap.d80_icon1);
        else
            baseViewHolder.setVisible(R.id.item_product_change_box,true)
                    .setImageResource(R.id.item_product_change,R.mipmap.d80_icon1_p);

    }


}
