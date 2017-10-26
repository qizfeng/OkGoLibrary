package com.project.community.ui.adapter;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.library.okgo.utils.GlideImageLoader;
import com.project.community.R;
import com.project.community.listener.RecycleItemClickListener;
import com.project.community.model.GoodsManagerModel;

import java.util.List;

/**
 * Created by cj on 17/9/25
 * 全部商品列表适配器
 */

public class ProductSellApdater extends BaseQuickAdapter<GoodsManagerModel, BaseViewHolder> {
    public RecycleItemClickListener itemClickListener;//点击事件

    public ProductSellApdater(List<GoodsManagerModel> data, RecycleItemClickListener itemClick) {
        super(R.layout.layout_item_product_sell, data);
        itemClickListener = itemClick;

    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, final GoodsManagerModel model) {
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
        if (!model.open)
            baseViewHolder.setVisible(R.id.item_product_change_box,false)
            .setImageResource(R.id.item_product_change,R.mipmap.d80_icon1);
        else
            baseViewHolder.setVisible(R.id.item_product_change_box,true)
                    .setImageResource(R.id.item_product_change,R.mipmap.d80_icon1_p);

        baseViewHolder.setText(R.id.item_product_title,model.name)
                .setText(R.id.item_product_price,mContext.getString(R.string.sell_price)+model.price)
                .setText(R.id.item_product_kucun,mContext.getString(R.string.build_new_goods_inventory_maohao)+model.stock+"     "+R.string.build_new_goods_xiaoliang_maohao+model.sales);
        new GlideImageLoader().onDisplayImageWithDefault(mContext, (ImageView) baseViewHolder.getView(R.id.item_product_cover), model.images, R.mipmap.c1_image2);

    }


}
