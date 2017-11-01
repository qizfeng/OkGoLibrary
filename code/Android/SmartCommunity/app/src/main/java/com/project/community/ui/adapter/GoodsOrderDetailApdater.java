package com.project.community.ui.adapter;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.library.okgo.utils.GlideImageLoader;
import com.project.community.R;
import com.project.community.constants.AppConstants;
import com.project.community.listener.RecycleItemClickListener;
import com.project.community.model.GoodsModel;

import java.util.List;

/**
 * Created by cj on 17/9/25
 * 商品订单详情适配器
 */

public class GoodsOrderDetailApdater extends BaseQuickAdapter<GoodsModel, BaseViewHolder> {
    public RecycleItemClickListener itemClickListener;//点击事件

    public GoodsOrderDetailApdater(List<GoodsModel> data, RecycleItemClickListener itemClick) {
        super(R.layout.layout_item_goods_detail, data);
        itemClickListener = itemClick;
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, final GoodsModel model) {
        baseViewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(view, baseViewHolder.getLayoutPosition());
            }
        });
        if (getItemCount()-1==baseViewHolder.getAdapterPosition()){
            baseViewHolder.setVisible(R.id.item_line,false);
        }else {
            baseViewHolder.setVisible(R.id.item_line,true);
        }

        new GlideImageLoader().onDisplayImageWithDefault(mContext, (ImageView) baseViewHolder.getView(R.id.item_goods_detail_cover), AppConstants.URL_BASE+model.goodImage,R.mipmap.c1_image2);
        baseViewHolder.setText(R.id.item_goods_detail_name,model.goodName)
                .setText(R.id.item_goods_detail_price,"¥"+model.goodPrice)
                .setText(R.id.item_goods_detail_num,"x"+model.number);
    }
}
