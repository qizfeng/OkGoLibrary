package com.project.community.ui.adapter;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.library.okgo.utils.GlideImageLoader;
import com.project.community.R;
import com.project.community.constants.AppConstants;
import com.project.community.listener.RecycleItemClickListener;
import com.project.community.model.GoodsManagerModel;

import java.util.List;

/**
 * Created by cj on 17/9/25
 * 全部商品列表适配器
 */

public class ProductSellApdater extends BaseQuickAdapter<GoodsManagerModel, BaseViewHolder> {
    public RecycleItemClickListener itemClickListener;//点击事件
    private int code=0;

    public ProductSellApdater(List<GoodsManagerModel> data, RecycleItemClickListener itemClick,int code) {
        super(R.layout.layout_item_product_sell, data);
        itemClickListener = itemClick;
        this.code=code;

    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, final GoodsManagerModel model) {

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

        if (!model.open){
            baseViewHolder.setVisible(R.id.item_product_change_box,false)
                    .setImageResource(R.id.item_product_change,R.mipmap.d80_icon1);
            baseViewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListener.onItemClick(view, baseViewHolder.getLayoutPosition());
                }
            });
        } else{
            baseViewHolder.setVisible(R.id.item_product_change_box,true)
                    .setImageResource(R.id.item_product_change,R.mipmap.d80_icon1_p);
            baseViewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    baseViewHolder.setVisible(R.id.item_product_change_box,false)
                            .setImageResource(R.id.item_product_change,R.mipmap.d80_icon1);
                }
            });
        }


        baseViewHolder.setText(R.id.item_product_title,model.name)
                .setText(R.id.item_product_price,mContext.getString(R.string.sell_price)+model.price)
                .setText(R.id.item_product_kucun,mContext.getString(R.string.build_new_goods_inventory_maohao)+model.stock+"    "+
                        mContext.getString(R.string.build_new_goods_xiaoliang_maohao)+model.sales);
        new GlideImageLoader().onDisplayImageWithDefault(mContext, (ImageView) baseViewHolder.getView(R.id.item_product_cover), AppConstants.URL_BASE+model.images, R.mipmap.c1_image2);

        if (code==0){
            baseViewHolder.setText(R.id.item_product_deit,mContext.getResources().getString(R.string.products_manager_sell_item_edit))
                    .setText(R.id.item_product_xia,mContext.getString(R.string.products_manager_sell_item_xia))
                    .setVisible(R.id.item_product_xia,true)
                    .setVisible(R.id.view_1,true)
                    .setVisible(R.id.view_2,true);
        }else if (code==1){
            baseViewHolder.setText(R.id.item_product_deit,mContext.getResources().getString(R.string.products_manager_sell_item_editkucun))
                    .setVisible(R.id.view_1,true)
                    .setVisible(R.id.item_product_xia,false)
                    .setVisible(R.id.view_2,false);
        }else {
            baseViewHolder.setText(R.id.item_product_deit,mContext.getResources().getString(R.string.products_manager_sell_item_edit))
                    .setText(R.id.item_product_xia,mContext.getString(R.string.products_manager_sell_item_shang))
                    .setVisible(R.id.view_1,true)
                    .setVisible(R.id.item_product_xia,true)
                    .setVisible(R.id.view_2,true);
        }
    }


}
